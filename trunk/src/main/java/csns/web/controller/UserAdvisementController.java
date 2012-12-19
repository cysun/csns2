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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import csns.model.advisement.AdvisementRecord;
import csns.model.advisement.dao.AdvisementRecordDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class UserAdvisementController {

    @Autowired
    UserDao userDao;

    @Autowired
    AdvisementRecordDao advisementRecordDao;

    @Autowired
    FileIO fileIO;

    @RequestMapping("/user/advisement")
    public String advisement( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );
        models.put( "user", user );
        models.put( "advisementRecords",
            advisementRecordDao.getAdvisementRecords( user ) );
        return "user/advisement";
    }

    @RequestMapping("/user/advisement/add")
    public String add(
        @RequestParam Long userId,
        @RequestParam String comment,
        @RequestParam(required = false) Boolean forAdvisorsOnly,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles )
    {
        User student = userDao.getUser( userId );
        AdvisementRecord record = new AdvisementRecord( student,
            SecurityUtils.getUser() );
        record.setComment( comment );
        if( forAdvisorsOnly != null )
            record.setForAdvisorsOnly( forAdvisorsOnly );
        if( uploadedFiles != null )
            record.getAttachments().addAll(
                fileIO.save( uploadedFiles, student, false ) );

        record = advisementRecordDao.saveAdvisementRecord( record );

        return "redirect:/user/view?id=" + userId + "#ui-tabs-3";
    }

}
