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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Assignment;
import csns.model.academics.Submission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class SubmissionController {

    @Autowired
    FileDao fileDao;

    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    SubmissionDao submissionDao;

    @Autowired
    FileIO fileIO;

    @RequestMapping(value = "submission/view", params = "id")
    public String view1( @RequestParam Long id, ModelMap models )
    {
        models.put( "submission", submissionDao.getSubmission( id ) );
        return "submission/view";
    }

    @RequestMapping(value = "/submission/view", params = "assignmentId")
    public String view2( @RequestParam Long assignmentId, ModelMap models )
    {
        User user = SecurityUtils.getUser();
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        Submission submission = submissionDao.getSubmission( user, assignment );
        if( submission == null )
            submission = submissionDao.saveSubmission( new Submission(
                user,
                assignment ) );

        models.put( "submission", submission );
        return "submission/view";
    }

    @RequestMapping("/submission/upload")
    public String upload( @RequestParam Long id,
        @RequestParam MultipartFile uploadedFile, ModelMap models )
    {
        Submission submission = submissionDao.getSubmission( id );
        if( submission.isPastDue() )
        {
            models.put( "message", "error.submission.pastdue" );
            models.put( "backUrl", "/submission/view?id=" + submission.getId() );
            return "error";
        }

        String fileName = uploadedFile.getOriginalFilename();
        if( !submission.getAssignment().isFileExtensionAllowed(
            File.getFileExtension( fileName ) ) )
        {
            models.put( "message", "error.submission.file.type" );
            models.put( "backUrl", "/submission/view?id=" + submission.getId() );
            return "error";
        }

        User user = SecurityUtils.getUser();
        File file = new File();
        file.setName( fileName );
        file.setType( uploadedFile.getContentType() );
        file.setSize( uploadedFile.getSize() );
        file.setDate( new Date() );
        file.setOwner( user );
        file.setSubmission( submission );
        file = fileDao.saveFile( file );

        fileIO.save( file, uploadedFile );

        return "redirect:/submission/view?id=" + submission.getId();
    }

    @RequestMapping("/submission/remove")
    public String remove( @RequestParam Long fileId )
    {
        File file = fileDao.getFile( fileId );
        Submission submission = file.getSubmission();
        if( !submission.isPastDue() )
        {
            file.setSubmission( null );
            fileDao.saveFile( file );
        }

        return "redirect:/submission/view?id=" + submission.getId();
    }

}
