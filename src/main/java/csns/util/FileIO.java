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
package csns.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;

public class FileIO {

    private String fileDir;

    private static final Logger logger = LoggerFactory.getLogger( FileIO.class );

    public FileIO()
    {
    }

    public void setFileDir( String fileDir )
    {
        this.fileDir = fileDir;
    }

    public void save( File file, MultipartFile uploadedFile )
    {
        String fileId = file.getId().toString();
        java.io.File diskFile = new java.io.File( fileDir, fileId );
        try
        {
            uploadedFile.transferTo( diskFile );
        }
        catch( Exception e )
        {
            logger.error( "Failed to save uploaded file", e );
        }
    }

}
