/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2017, Chengyu Sun (csun@calstatela.edu).
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import csns.helper.ProgramChecker;
import csns.model.academics.CourseMapping;
import csns.model.academics.Enrollment;
import csns.model.academics.dao.CourseMappingDao;
import csns.model.academics.dao.EnrollmentDao;
import csns.model.advisement.dao.AdvisementRecordDao;
import csns.model.advisement.dao.PersonalProgramDao;
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
    private EnrollmentDao enrollmentDao;

    @Autowired
    private CourseMappingDao courseMappingDao;

    @Autowired
    private PersonalProgramDao personalProgramDao;

    @Autowired
    private AdvisementRecordDao advisementRecordDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    private static final Logger logger = LoggerFactory
        .getLogger( ProfileController.class );

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
        if( user.getPersonalProgram() != null )
        {
            List<Enrollment> enrollments = enrollmentDao.getEnrollments( user );
            enrollments.removeAll( user.getPersonalProgram().getEnrollments() );
            ProgramChecker programChecker = new ProgramChecker(
                user.getPersonalProgram() );
            int entriesUpdated = programChecker
                .checkRequirements( enrollments );
            List<CourseMapping> courseMappings = courseMappingDao
                .getCourseMappings(
                    user.getPersonalProgram().getProgram().getDepartment() );
            if( courseMappings.size() > 0 ) entriesUpdated += programChecker
                .checkRequirements( courseMappings, enrollments );
            if( entriesUpdated > 0 )
            {
                personalProgramDao
                    .savePersonalProgram( user.getPersonalProgram() );
                logger.info( "Auto updated " + entriesUpdated
                    + " entries in the personal program of "
                    + user.getUsername() );
            }
        }

        models.put( "user", user );
        return "profile/program";
    }

    @RequestMapping("/profile/advisement")
    public String advisement( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        models.put( "records",
            advisementRecordDao.getAdvisementRecords( user ) );
        return "profile/advisement";
    }

    @RequestMapping("/profile/forums")
    public String forumSubscriptions( ModelMap models )
    {
        User user = SecurityUtils.getUser();
        List<Subscription> subscriptions = subscriptionDao
            .getSubscriptions( user, Forum.class );

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
        List<Subscription> subscriptions = subscriptionDao
            .getSubscriptions( user, Mailinglist.class );

        models.put( "subscriptions", subscriptions );
        return "profile/mailinglists";
    }

}
