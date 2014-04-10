/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.Post;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.PostDao;
import csns.model.news.News;
import csns.model.news.dao.NewsDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.web.editor.CalendarPropertyEditor;
import csns.web.validator.NewsValidator;

@Controller
@SessionAttributes("news")
public class NewsControllerS {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private NewsValidator newsValidator;

    @Autowired
    private FileIO fileIO;

    private static final Logger logger = LoggerFactory.getLogger( NewsControllerS.class );

    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Calendar.class,
            new CalendarPropertyEditor( "MM/dd/yyyy" ) );
    }

    @RequestMapping(value = "/department/{dept}/news/post",
        method = RequestMethod.GET)
    public String post( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        News news = new News();
        news.setDepartment( department );
        models.put( "news", news );
        return "news/post";
    }

    @RequestMapping(value = "/department/{dept}/news/post",
        method = RequestMethod.POST)
    public String post( @ModelAttribute News news, @PathVariable String dept,
        @RequestParam Long forumId, @RequestParam(value = "file",
            required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus )
    {
        newsValidator.validate( news, result );
        if( result.hasErrors() ) return "news/post";

        Post post = news.getTopic().getFirstPost();
        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        if( uploadedFiles != null )
            post.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );
        post.setAuthor( user );
        post.setDate( new Date() );

        Forum forum = forumDao.getForum( forumId );
        news.getTopic().setForum( forum );
        news = newsDao.saveNews( news );

        user.incrementNumOfForumPosts();
        forum.incrementNumOfTopics();
        forum.incrementNumOfPosts();
        forum.setLastPost( news.getTopic().getFirstPost() );
        subscriptionDao.subscribe( news.getTopic(), user );

        logger.info( user.getUsername() + " posted news " + news.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/news/current";
    }

    @RequestMapping(value = "/department/{dept}/news/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long id, ModelMap models )
    {
        models.put( "news", newsDao.getNews( id ) );
        return "news/edit";
    }

    @RequestMapping(value = "/department/{dept}/news/edit",
        method = RequestMethod.POST)
    public String edit(
        @ModelAttribute News news,
        @PathVariable String dept,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus )
    {
        newsValidator.validate( news, result );
        if( result.hasErrors() ) return "news/edit";

        Post post = news.getTopic().getFirstPost();
        User user = SecurityUtils.getUser();
        if( uploadedFiles != null )
            post.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );
        post.setEditedBy( user );
        post.setEditDate( new Date() );
        postDao.savePost( post );
        news = newsDao.saveNews( news );

        logger.info( user.getUsername() + " edited news " + news.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/news/current";
    }

    @RequestMapping("/department/{dept}/news/deleteAttachment")
    public @ResponseBody
    String deleteAttachment( @ModelAttribute News news,
        @RequestParam Long fileId )
    {
        List<File> attachments = news.getTopic()
            .getFirstPost()
            .getAttachments();
        for( File attachment : attachments )
            if( attachment.getId().equals( fileId ) )
            {
                attachments.remove( attachment );
                logger.info( SecurityUtils.getUser().getUsername()
                    + " deleted attachment " + fileId );
                break;
            }

        return "";
    }

}
