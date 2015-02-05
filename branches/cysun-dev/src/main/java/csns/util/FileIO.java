/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
 * 
 * CSNS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * CSNS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CSNS. If not, see http://www.gnu.org/licenses/agpl.html.
 */
package csns.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;

@Component
public class FileIO {

    @Autowired
    FileDao fileDao;

    @Value("#{applicationProperties['file.dir']}")
    private String fileDir;

    @Resource(name = "contentTypes")
    Properties contentTypes;

    private static final Logger logger = LoggerFactory.getLogger( FileIO.class );

    public FileIO()
    {
    }

    public java.io.File getDiskFile( File file )
    {
        return getDiskFile( file, true );
    }

    public java.io.File getDiskFile( File file, boolean followReference )
    {
        java.io.File diskFile = new java.io.File( fileDir, file.getId()
            .toString() );

        return diskFile.exists() || !followReference
            || file.getReference() == null ? diskFile : new java.io.File(
            fileDir, file.getReference().getId().toString() );
    }

    public File save( MultipartFile uploadedFile, User user, boolean isPublic )
    {
        return save( uploadedFile, user, null, isPublic );
    }

    public File save( MultipartFile uploadedFile, User user, File parent,
        boolean isPublic )
    {
        if( uploadedFile.isEmpty() ) return null;

        File file = new File();
        file.setName( uploadedFile.getOriginalFilename() );
        file.setType( uploadedFile.getContentType() );
        file.setSize( uploadedFile.getSize() );
        file.setOwner( user );
        file.setParent( parent );
        file.setPublic( isPublic );
        file = fileDao.saveFile( file );

        java.io.File diskFile = getDiskFile( file, false );
        try
        {
            uploadedFile.transferTo( diskFile );
        }
        catch( Exception e )
        {
            logger.error( "Failed to save uploaded file", e );
        }

        return file;
    }

    public List<File> save( MultipartFile[] uploadedFiles, User user,
        boolean isPublic )
    {
        List<File> files = new ArrayList<File>();
        for( MultipartFile uploadedFile : uploadedFiles )
            if( !uploadedFile.isEmpty() )
                files.add( save( uploadedFile, user, isPublic ) );
        return files;
    }

    public void save( File file, MultipartFile uploadedFile )
    {
        java.io.File diskFile = getDiskFile( file, false );
        try
        {
            uploadedFile.transferTo( diskFile );
        }
        catch( Exception e )
        {
            logger.error( "Failed to save uploaded file", e );
        }
    }

    public void copy( InputStream in, OutputStream out )
    {
        byte[] buffer = new byte[4096];
        int bytesRead;
        try
        {
            while( (bytesRead = in.read( buffer )) != -1 )
                out.write( buffer, 0, bytesRead );
        }
        catch( IOException e )
        {
            logger.error( "Failed to copy input to output", e );
        }
    }

    public void copy( File file, OutputStream out )
    {
        try
        {
            java.io.File diskFile = getDiskFile( file );
            FileInputStream in = new FileInputStream( diskFile );
            copy( in, out );
            in.close();
        }
        catch( Exception e )
        {
            logger.error( "Failed to copy file to output", e );
        }
    }

    public void copy( File from, File to )
    {
        try
        {
            java.io.File diskFile = getDiskFile( to, false );
            FileOutputStream out = new FileOutputStream( diskFile );
            copy( from, out );
            out.close();
        }
        catch( Exception e )
        {
            logger.error( "Failed to copy file to output", e );
        }
    }

    public void delete( File file )
    {
        java.io.File diskFile = getDiskFile( file, false );
        if( !diskFile.delete() )
            logger.error( "Failed to delete file " + diskFile.getAbsolutePath() );
    }

    public void write( File file, HttpServletResponse response )
    {
        String contentType = contentTypes.getProperty( file.getFileExtension()
            .toLowerCase() );
        if( contentType == null ) contentType = file.getType();

        try
        {
            response.setContentType( contentType );
            response.setHeader( "Content-Length", file.getSize().toString() );
            response.setHeader( "Content-Disposition", "inline; filename="
                + file.getName().replace( ' ', '_' ) );
            copy( file, response.getOutputStream() );
        }
        catch( Exception e )
        {
            logger.error( "Fail to write file to response", e );
        }
    }

}
