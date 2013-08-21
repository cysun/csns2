/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012, Chengyu Sun (csun@calstatela.edu).
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
package csns.web.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class FileManagerController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( FileManagerController.class );

    @RequestMapping("/file/")
    public String files( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        models.put( "user", user );
        models.put( "files", fileDao.listFiles( user ) );
        return "file/view";
    }

    @RequestMapping("/file/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        File folder = fileDao.getFile( id );
        if( folder == null || !folder.isFolder() || folder.isDeleted() )
            return "redirect:/";

        models.put( "folder", folder );
        models.put( "files", fileDao.listFiles( folder ) );
        models.put( "user", SecurityUtils.getUser() );
        return "file/view";
    }

    @RequestMapping("/file/create")
    public String create( @RequestParam(required = false) Long parentId,
        @RequestParam String name )
    {
        File parent = null;
        String redirectUrl = "redirect:/file/";
        if( parentId != null )
        {
            parent = fileDao.getFile( parentId );
            redirectUrl += "view?id=" + parentId;
        }

        File file = new File();
        file.setName( name );
        file.setFolder( true );
        file.setRegular( true );
        file.setParent( parent );
        if( parent != null ) file.setPublic( parent.isPublic() );
        file.setOwner( SecurityUtils.getUser() );
        fileDao.saveFile( file );

        return redirectUrl;
    }

    @RequestMapping("/file/upload")
    public String upload( @RequestParam(required = false) Long parentId,
        @RequestParam("file") MultipartFile uploadedFile, ModelMap models )
    {
        File parent = null;
        String redirectUrl = "redirect:/file/";
        if( parentId != null )
        {
            parent = fileDao.getFile( parentId );
            redirectUrl += "view?id=" + parentId;
        }

        if( !uploadedFile.isEmpty() )
        {
            User user = SecurityUtils.getUser();
            long diskQuota = user.getDiskQuota() * 1024L * 1024L;
            long diskUsage = fileDao.getDiskUsage( user );
            if( diskUsage + uploadedFile.getSize() > diskQuota )
            {
                models.put( "message", "error.file.quota.exceeded" );
                return "error";
            }

            File file = new File();
            file.setName( uploadedFile.getOriginalFilename() );
            file.setType( uploadedFile.getContentType() );
            file.setSize( uploadedFile.getSize() );
            file.setOwner( user );
            file.setRegular( true );
            if( parent != null )
            {
                file.setParent( parent );
                file.setPublic( parent.isPublic() );
            }
            file = fileDao.saveFile( file );

            fileIO.save( file, uploadedFile );
        }

        return redirectUrl;
    }

    @RequestMapping("/file/edit")
    public String edit( @RequestParam Long id, ModelMap models )
    {
        File file = fileDao.getFile( id );
        if( file == null || !file.isRegular() || file.isDeleted() )
            return "redirect:/";

        models.put( "file", file );
        return "file/edit";
    }

    @RequestMapping("/file/rename")
    public String rename( @RequestParam Long id, @RequestParam String name )
    {
        File file = fileDao.getFile( id );
        if( !file.isRegular() || file.isDeleted() ) return "redirect:/";

        file.setName( name );
        file = fileDao.saveFile( file );
        return "redirect:/file/edit?id=" + id;
    }

    @RequestMapping("/file/replace")
    public String replace( @RequestParam Long id,
        @RequestParam("file") MultipartFile uploadedFile, ModelMap models )
    {
        File file = fileDao.getFile( id );
        if( !file.isRegular() || file.isDeleted() || file.isFolder() )
            return "redirect:/";

        if( !uploadedFile.isEmpty() )
        {
            User user = SecurityUtils.getUser();
            long diskQuota = user.getDiskQuota() * 1024L * 1024L;
            long diskUsage = fileDao.getDiskUsage( user );
            if( diskUsage - file.getSize() + uploadedFile.getSize() > diskQuota )
            {
                models.put( "message", "error.file.quota.exceeded" );
                return "error";
            }

            file.setName( uploadedFile.getOriginalFilename() );
            file.setType( uploadedFile.getContentType() );
            file.setSize( uploadedFile.getSize() );
            file.setDate( new Date() );
            file = fileDao.saveFile( file );
            fileIO.save( file, uploadedFile );
        }

        return "redirect:/file/edit?id=" + id;
    }

    @RequestMapping("/file/toggle")
    public String toggle( @RequestParam Long id, ModelMap models )
    {
        File file = fileDao.getFile( id );
        file.setPublic( !file.isPublic() );
        file = fileDao.saveFile( file );

        logger.info( SecurityUtils.getUser().getUsername() + " set file "
            + file.getId() + " to " + (file.isPublic() ? "public" : "private") );

        models.put( "file", file );
        return "file/toggle";
    }

    @RequestMapping(value = "/file/copy", params = "!copy")
    public String copy1( @RequestParam Long srcId,
        @RequestParam(required = false) Long destId, ModelMap models )
    {
        File src = fileDao.getFile( srcId );
        File dest = destId != null ? fileDao.getFile( destId ) : null;
        List<File> subfolders = dest != null ? fileDao.listFolders( dest )
            : fileDao.listFolders( SecurityUtils.getUser() );

        models.put( "src", src );
        models.put( "dest", dest );
        models.put( "subfolders", subfolders );
        return "file/copy";
    }

    @RequestMapping("/file/copy")
    public String copy2( @RequestParam Long srcId,
        @RequestParam(required = false) Long destId, ModelMap models )
    {
        File src = fileDao.getFile( srcId );
        if( !src.isRegular() || src.isDeleted() ) return "redirect:/";

        File dest = destId != null ? fileDao.getFile( destId ) : null;
        if( dest != null
            && (!dest.isRegular() || dest.isDeleted() || !dest.isFolder() || src.isAncestor( dest )) )
            return "redirect:/";

        User user = SecurityUtils.getUser();
        long diskUsage = fileDao.getDiskUsage( user );
        diskUsage += calcDiskUsage( src );
        if( diskUsage > user.getDiskQuota() * 1024L * 1024L )
        {
            models.put( "message", "error.file.quota.exceeded" );
            return "error";
        }

        copy( src, dest, user );

        return dest != null ? "redirect:/file/view?id=" + dest.getId()
            : "redirect:/file/";
    }

    @RequestMapping(value = "/file/move", params = "!move")
    public String move1( @RequestParam Long srcId,
        @RequestParam(required = false) Long destId, ModelMap models )
    {
        File src = fileDao.getFile( srcId );
        File dest = destId != null ? fileDao.getFile( destId ) : null;
        List<File> subfolders = dest != null ? fileDao.listFolders( dest )
            : fileDao.listFolders( SecurityUtils.getUser() );

        models.put( "src", src );
        models.put( "dest", dest );
        models.put( "subfolders", subfolders );
        return "file/move";
    }

    @RequestMapping("/file/move")
    public String move( @RequestParam Long srcId,
        @RequestParam(required = false) Long destId, ModelMap models )
    {
        File src = fileDao.getFile( srcId );
        if( !src.isRegular() || src.isDeleted() ) return "redirect:/";

        File dest = destId != null ? fileDao.getFile( destId ) : null;
        if( dest != null
            && (!dest.isRegular() || dest.isDeleted() || !dest.isFolder() || src.isAncestor( dest )) )
            return "redirect:/";

        src.setParent( dest );
        src = fileDao.saveFile( src );
        logger.info( "File " + src.getId() + " moved by "
            + SecurityUtils.getUser().getUsername() );

        return dest != null ? "redirect:/file/view?id=" + dest.getId()
            : "redirect:/file/";
    }

    @RequestMapping("/file/delete")
    public String delete( @RequestParam Long id )
    {
        File file = fileDao.getFile( id );
        if( !file.isRegular() || file.isDeleted() ) return "redirect:/";

        delete( file );

        File parent = file.getParent();
        return parent != null ? "redirect:/file/view?id=" + parent.getId()
            : "redirect:/file/";
    }

    private long calcDiskUsage( File file )
    {
        if( !file.isFolder() ) return file.getSize();

        long diskUsage = 0;
        for( File child : fileDao.listFiles( file ) )
            diskUsage += calcDiskUsage( child );

        return diskUsage;
    }

    private void copy( File file, File dest, User user )
    {
        File newFile = file.clone();
        newFile.setOwner( user );
        newFile.setParent( dest );
        newFile = fileDao.saveFile( newFile );

        if( !file.isFolder() )
            fileIO.copy( file, newFile );
        else
        {
            List<File> children = fileDao.listFiles( file );
            for( File child : children )
                copy( child, newFile, user );
        }
    }

    private void delete( File file )
    {
        file.setDeleted( true );
        file.setSize( null );
        fileDao.saveFile( file );

        if( !file.isFolder() )
        {
            fileIO.delete( file );
            logger.info( "File " + file.getId() + " deleted by "
                + SecurityUtils.getUser().getUsername() );
        }
        else
        {
            List<File> children = fileDao.listFiles( file );
            for( File child : children )
                delete( child );
        }
    }

}
