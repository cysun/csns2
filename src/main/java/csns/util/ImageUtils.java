/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import csns.model.core.File;
import csns.model.core.dao.FileDao;

@Component
public class ImageUtils {

    public static final int PROFILE_PICTURE_SIZE = 320;

    public static final int PROFILE_THUMBNAIL_SIZE = 24;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory
        .getLogger( ImageUtils.class );

    // Copied from Adam Gawne-Cain's "2019 answer" at
    // https://stackoverflow.com/questions/3432388/imageio-not-able-to-write-a-jpeg-file
    static BufferedImage ensureOpaque( BufferedImage bi )
    {
        if( bi.getTransparency() == BufferedImage.OPAQUE ) return bi;
        int w = bi.getWidth();
        int h = bi.getHeight();
        int[] pixels = new int[w * h];
        bi.getRGB( 0, 0, w, h, pixels, 0, w );
        BufferedImage bi2 = new BufferedImage( w, h,
            BufferedImage.TYPE_INT_RGB );
        bi2.setRGB( 0, 0, w, h, pixels, 0, w );
        return bi2;
    }

    public File resizeToProfilePicture( File file )
    {
        return resize( file, PROFILE_PICTURE_SIZE, false );
    }

    public File resizeToProfileThumbnail( File file )
    {
        return resize( file, PROFILE_THUMBNAIL_SIZE, false );
    }

    public File resize( File file, int targetSize, boolean always )
    {
        File newFile = null;

        try
        {
            BufferedImage image = ImageIO.read( fileIO.getDiskFile( file ) );

            if( !always && image.getWidth() < targetSize
                && image.getHeight() < targetSize ) return file;

            image = ensureOpaque( image );
            BufferedImage newImage = Scalr.resize( image, targetSize );
            java.io.File tempFile = java.io.File.createTempFile( "temp", null );
            ImageIO.write( newImage, "jpg", tempFile );

            newFile = new File();
            newFile.setName( file.getName() + "_" + targetSize + ".jpg" );
            newFile.setType( "image/jpeg" );
            newFile.setSize( tempFile.length() );
            newFile.setOwner( file.getOwner() );
            newFile.setParent( file.getParent() );
            newFile.setPublic( file.isPublic() );
            newFile = fileDao.saveFile( newFile );

            FileUtils.copyFile( tempFile,
                fileIO.getDiskFile( newFile, false ) );
        }
        catch( IOException e )
        {
            logger.error( "Fail to resize image " + file.getName(), e );
        }

        return newFile;
    }

}
