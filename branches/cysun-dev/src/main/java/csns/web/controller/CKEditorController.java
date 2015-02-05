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
public class CKEditorController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( CKEditorController.class );

    @RequestMapping("/ckeditor/upload")
    public String upload( @RequestParam("upload") MultipartFile uploadedFile,
        ModelMap models )
    {
        User user = SecurityUtils.getUser();
        File parent = fileDao.getCKEditorFolder( user );
        File file = fileIO.save( uploadedFile, user, parent, true );

        logger.info( user.getUsername() + " uploaded (via CKEditor) file "
            + file.getId() );

        models.put( "file", file );
        return "ckeditor/upload";
    }

}
