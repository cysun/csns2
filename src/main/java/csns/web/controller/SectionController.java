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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import csns.helper.GradeSheet;
import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.academics.Term;
import csns.model.academics.Section;
import csns.model.academics.dao.CourseDao;
import csns.model.academics.dao.TermDao;
import csns.model.academics.dao.SectionDao;
import csns.model.core.User;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.dao.ForumDao;
import csns.security.SecurityUtils;
import csns.web.editor.TermPropertyEditor;

@Controller
public class SectionController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private TermDao termDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory
        .getLogger( SectionController.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Term.class,
            (TermPropertyEditor) context.getBean( "termPropertyEditor" ) );
    }

    private String list( String type, Term term, ModelMap models,
        HttpSession session )
    {
        if( term != null )
            session.setAttribute( "term", term );
        else if( session.getAttribute( "term" ) != null )
            term = (Term) session.getAttribute( "term" );
        else
            term = new Term();

        User user = SecurityUtils.getUser();
        List<Term> terms = null;
        List<Section> sections = null;
        String view = "error";
        switch( type )
        {
            case "taught":
                terms = termDao.getTermsByInstructor( user );
                sections = sectionDao.getSectionsByInstructor( user, term );
                view = "section/taught";
                break;

            case "taken":
                terms = termDao.getTermsByStudent( user );
                sections = sectionDao.getSectionsByStudent( user, term );
                view = "section/taken";
                break;

            case "evaluated":
                terms = termDao.getTermsByEvaluator( user );
                sections = sectionDao.getSectionsByEvaluator( user, term );
                view = "section/evaluated";
                break;

            default:
                logger.warn( "Invalid section type: " + type );
        }

        Term currentTerm = new Term();
        if( !terms.contains( currentTerm ) ) terms.add( 0, currentTerm );
        Term nextTerm = currentTerm.next();
        if( !terms.contains( nextTerm ) ) terms.add( 0, nextTerm );
        Term nextNextTerm = nextTerm.next();
        if( !terms.contains( nextNextTerm ) ) terms.add( 0, nextNextTerm );

        models.put( "user", user );
        models.put( "term", term );
        models.put( "terms", terms );
        models.put( "sections", sections );
        return view;
    }

    @RequestMapping("/section/taught")
    public String taught( @RequestParam(required = false) Term term,
        ModelMap models, HttpSession session, HttpServletResponse response )
    {
        Cookie cookie = new Cookie( "default-home", "/section/taught" );
        cookie.setPath( "/" );
        cookie.setMaxAge( 100000000 );
        response.addCookie( cookie );

        return list( "taught", term, models, session );
    }

    @RequestMapping("/section/taken")
    public String taken( @RequestParam(required = false) Term term,
        ModelMap models, HttpSession session, HttpServletResponse response )
    {
        Cookie cookie = new Cookie( "default-home", "/section/taken" );
        cookie.setPath( "/" );
        cookie.setMaxAge( 100000000 );
        response.addCookie( cookie );

        return list( "taken", term, models, session );
    }

    @RequestMapping("/section/evaluated")
    public String evaluated( @RequestParam(required = false) Term term,
        ModelMap models, HttpSession session, HttpServletResponse response )
    {
        Cookie cookie = new Cookie( "default-home", "/section/evaluated" );
        cookie.setPath( "/" );
        cookie.setMaxAge( 100000000 );
        response.addCookie( cookie );

        return list( "evaluated", term, models, session );
    }

    @RequestMapping("/section/add")
    public String add( @RequestParam Long courseId,
        @RequestParam Integer termCode )
    {
        Term term = new Term( termCode );
        Course course = courseDao.getCourse( courseId );
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        Section section = sectionDao.addSection( term, course, user );

        Forum forum = forumDao.getForum( course );
        if( !forum.isModerator( user ) )
        {
            forum.getModerators().add( user );
            forumDao.saveForum( forum );
        }

        logger.info( user.getUsername() + " added section " + section.getId() );

        return "redirect:/section/taught";
    }

    @RequestMapping("/section/delete")
    public String delete( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        if( section.getEnrollments().size() > 0 )
        {
            models.put( "message", "error.section.nonempty" );
            models.put( "backUrl",
                "/section/taught#section-" + section.getId() );
            return "error";
        }

        section = sectionDao.deleteSection( section );
        logger.info( SecurityUtils.getUser().getUsername() + " deleted section "
            + section.getId() );

        return "redirect:/section/taught";
    }

    @RequestMapping("/section/fdelete")
    public String fdelete( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        section.getEnrollments().clear();
        section = sectionDao.deleteSection( section );

        logger.info( SecurityUtils.getUser().getUsername()
            + " force deleted section " + section.getId() );

        return "redirect:/section/taught";
    }

    @RequestMapping(value = "/section/edit", method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        Department department = section.getCourse().getDepartment();

        List<User> instructors = new ArrayList<User>();
        instructors.addAll( department.getFaculty() );
        instructors.addAll( department.getInstructors() );
        instructors.removeAll( section.getInstructors() );

        JSONArray jsonArray = new JSONArray();
        for( User instructor : instructors )
        {
            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", instructor.getId().toString() );
            json.put( "value", instructor.getName() );
            json.put( "label",
                instructor.getCin() + " " + instructor.getName() );
            jsonArray.put( json );
        }

        models.put( "section", section );
        models.put( "instructors", jsonArray );

        return "section/edit";
    }

    @RequestMapping(value = "/section/edit", params = "instructor=add")
    public String addInstructor( @RequestParam Long id,
        @RequestParam Long instructorId )
    {
        Section section = sectionDao.getSection( id );
        User instructor = userDao.getUser( instructorId );
        if( !section.getInstructors().contains( instructor ) )
        {
            section.getInstructors().add( instructor );
            sectionDao.saveSection( section );

            logger.info(
                "Instructor " + instructorId + " added to section " + id );
        }

        return "redirect:/section/edit?id=" + id;
    }

    @RequestMapping(value = "/section/edit", params = "instructor=remove")
    public String removeInstructor( @RequestParam Long id,
        @RequestParam Long instructorId )
    {
        Section section = sectionDao.getSection( id );
        User instructor = userDao.getUser( instructorId );
        if( section.getInstructors().contains( instructor ) )
        {
            section.getInstructors().remove( instructor );
            sectionDao.saveSection( section );

            logger.info(
                "Instructor " + instructorId + " removed from section " + id );
        }

        return "redirect:/section/edit?id=" + id;
    }

    @RequestMapping(value = "/section/search")
    public String search( @RequestParam(required = false) String text,
        ModelMap models )
    {
        List<Section> sections = null;
        if( StringUtils.hasText( text ) )
            sections = sectionDao.searchSections( text, -1 );
        models.addAttribute( "sections", sections );
        return "section/search";
    }

    @RequestMapping("/section/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        Section section = sectionDao.getSection( id );
        models.put( "gradeSheet", new GradeSheet( section ) );
        return "section/view";
    }

}
