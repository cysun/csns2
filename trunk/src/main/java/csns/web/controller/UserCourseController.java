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

import csns.model.academics.Course;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.advisement.AdvisementRecord;
import csns.model.advisement.CourseSubstitution;
import csns.model.advisement.CourseTransfer;
import csns.model.advisement.CourseWaiver;
import csns.model.advisement.dao.CourseSubstitutionDao;
import csns.model.advisement.dao.CourseTransferDao;
import csns.model.advisement.dao.CourseWaiverDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class UserCourseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private CourseSubstitutionDao courseSubstitutionDao;

    @Autowired
    private CourseTransferDao courseTransferDao;

    @Autowired
    private CourseWaiverDao courseWaiverDao;

    @Autowired
    private FileIO fileIO;

    @RequestMapping("/user/courses")
    public String courses( @RequestParam Long userId, ModelMap models )
    {
        User user = userDao.getUser( userId );
        models.put( "user", user );
        models.put( "coursesTaken", enrollmentDao.getEnrollments( user ) );
        models.put( "courseSubstitutions",
            courseSubstitutionDao.getCourseSubstitutions( user ) );
        models.put( "courseTransfers",
            courseTransferDao.getCourseTransfers( user ) );
        models.put( "courseWaivers", courseWaiverDao.getCourseWaivers( user ) );
        return "user/courses";
    }

    @RequestMapping("/user/course/substitute")
    public String substitute(
        @RequestParam Long userId,
        @RequestParam Long originalId,
        @RequestParam Long substituteId,
        @RequestParam(required = false) String comment,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles )
    {
        User student = userDao.getUser( userId );
        Course original = courseDao.getCourse( originalId );
        Course substitute = courseDao.getCourse( substituteId );

        AdvisementRecord record = new AdvisementRecord( student,
            SecurityUtils.getUser() );
        record.setComment( "<p>Substitute " + original.getCode() + " with "
            + substitute.getCode() + ".</p>" + comment );
        if( uploadedFiles != null )
            record.getAttachments().addAll(
                fileIO.save( uploadedFiles, student, false ) );

        courseSubstitutionDao.saveCourseSubstitution( new CourseSubstitution(
            original, substitute, record ) );

        // Course Work is the 3rd tab
        return "redirect:/user/view?id=" + userId + "#2";
    }

    @RequestMapping("/user/course/transfer")
    public String transfer(
        @RequestParam Long userId,
        @RequestParam Long courseId,
        @RequestParam(required = false) String comment,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles )
    {
        User student = userDao.getUser( userId );
        Course course = courseDao.getCourse( courseId );

        AdvisementRecord record = new AdvisementRecord( student,
            SecurityUtils.getUser() );
        record.setComment( "<p>Transfer credits for " + course.getCode()
            + ".</p>" + comment );
        if( uploadedFiles != null )
            record.getAttachments().addAll(
                fileIO.save( uploadedFiles, student, false ) );

        courseTransferDao.saveCourseTransfer( new CourseTransfer( course,
            record ) );

        // Course Work is the 3rd tab
        return "redirect:/user/view?id=" + userId + "#2";
    }

    @RequestMapping("/user/course/waive")
    public String waive(
        @RequestParam Long userId,
        @RequestParam Long courseId,
        @RequestParam(required = false) String comment,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles )
    {
        User student = userDao.getUser( userId );
        Course course = courseDao.getCourse( courseId );

        AdvisementRecord record = new AdvisementRecord( student,
            SecurityUtils.getUser() );
        record.setComment( "<p>Waive the requirement of " + course.getCode()
            + ".</p>" + comment );
        if( uploadedFiles != null )
            record.getAttachments().addAll(
                fileIO.save( uploadedFiles, student, false ) );

        courseWaiverDao.saveCourseWaiver( new CourseWaiver( course, record ) );

        // Course Work is the 3rd tab
        return "redirect:/user/view?id=" + userId + "#2";
    }

}
