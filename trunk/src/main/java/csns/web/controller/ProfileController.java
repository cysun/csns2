/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2015, Chengyu Sun (csun@calstatela.edu).
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.helper.CourseMapper;
import csns.helper.ProgramStatus;
import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.academics.Program;
import csns.model.academics.dao.CourseMappingDao;
import csns.model.academics.dao.DepartmentDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.academics.dao.ProgramDao;
import csns.model.advisement.dao.AdvisementRecordDao;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.mailinglist.Mailinglist;
import csns.security.SecurityUtils;

@Controller
public class ProfileController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private CourseMappingDao courseMappingDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private AdvisementRecordDao advisementRecordDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    private static final Logger logger = LoggerFactory.getLogger( ProfileController.class );

    @RequestMapping("/profile")
    public String profile( ModelMap models )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        models.put( "user", user );
        return "profile/account";
    }

    @RequestMapping("/profile/courses")
    public String courses( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        models.put( "coursesTaken", enrollmentDao.getEnrollments( user ) );
        return "profile/courses";
    }

    @RequestMapping("/profile/program")
    public String program( ModelMap models )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        models.put( "user", user );
        models.put( "departments", departmentDao.getDepartments() );

        if( user.getMajor() != null )
            models.put( "programs", programDao.getPrograms( user.getMajor() ) );

        Program program = user.getProgram();
        if( program != null )
        {
            ProgramStatus programStatus = new ProgramStatus( user.getProgram() );
            CourseMapper courseMapper = new CourseMapper(
                courseMappingDao.getCourseMappings( program.getDepartment() ) );
            List<Enrollment> enrollments = enrollmentDao.getEnrollments( user );
            for( Enrollment enrollment : enrollments )
            {
                boolean added = programStatus.addEnrollment( enrollment );
                if( !added )
                {
                    Set<Course> mappedCourses = courseMapper.getMappedCourses( enrollment.getSection()
                        .getCourse() );
                    if( mappedCourses.size() == 1 )
                        added = programStatus.addMappedEnrollment(
                            mappedCourses.iterator().next(), enrollment );
                    if( !added )
                        programStatus.addOtherEnrollment( enrollment );
                }
            }
            models.put( "programStatus", programStatus );
        }

        return "profile/program";
    }

    @RequestMapping("/profile/advisement")
    public String advisement( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        models.put( "records", advisementRecordDao.getAdvisementRecords( user ) );
        return "profile/advisement";
    }

    @RequestMapping("/profile/forums")
    public String forumSubscriptions( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions(
            user, Forum.class );

        List<Forum> departmentForums = new ArrayList<Forum>();
        List<Forum> courseForums = new ArrayList<Forum>();
        List<Forum> otherForums = new ArrayList<Forum>();
        for( Subscription subscription : subscriptions )
        {
            Forum forum = (Forum) subscription.getSubscribable();
            if( forum.getDepartment() != null )
                departmentForums.add( forum );
            else if( forum.getCourse() != null )
                courseForums.add( forum );
            else
                otherForums.add( forum );
        }

        models.put( "departmentForums", departmentForums );
        models.put( "courseForums", courseForums );
        models.put( "otherForums", otherForums );
        return "profile/forums";
    }

    @RequestMapping("/profile/mailinglists")
    public String mailinglistSubscriptions( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        List<Subscription> subscriptions = subscriptionDao.getSubscriptions(
            user, Mailinglist.class );

        models.put( "subscriptions", subscriptions );
        return "profile/mailinglists";
    }

    @RequestMapping("/profile/setMajor")
    public String setMajor( @RequestParam(required = false) Long majorId )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.setMajor( majorId == null ? null
            : departmentDao.getDepartment( majorId ) );
        user.setProgram( null );
        user = userDao.saveUser( user );

        if( user.getMajor() == null )
            logger.info( user.getUsername() + " removed major." );
        else
            logger.info( user.getUsername() + " set major to "
                + user.getMajor().getAbbreviation() );

        // Program is the 3rd tab in Profile
        return "redirect:../profile#2";
    }

    @RequestMapping("/profile/setProgram")
    public String setProgram( @RequestParam(required = false) Long programId )
    {
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        user.setProgram( programId == null ? null
            : programDao.getProgram( programId ) );
        user = userDao.saveUser( user );

        if( user.getProgram() == null )
            logger.info( user.getUsername() + " removed program." );
        else
            logger.info( user.getUsername() + " set program to "
                + user.getProgram().getId() );

        // Program is the 3rd tab in Profile
        return "redirect:../profile#2";
    }

}
