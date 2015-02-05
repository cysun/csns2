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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.Post;
import csns.model.forum.Topic;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.PostDao;
import csns.model.forum.dao.TopicDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.util.NotificationService;
import csns.web.validator.MessageValidator;

@Controller
@SessionAttributes("post")
public class ForumTopicControllerS {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private MessageValidator messageValidator;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private NotificationService notificationService;

    private Logger logger = LoggerFactory.getLogger( ForumTopicControllerS.class );

    @RequestMapping(value = "/department/{dept}/forum/topic/create",
        method = RequestMethod.GET)
    public String create( @RequestParam Long forumId, ModelMap models )
    {
        Topic topic = new Topic( forumDao.getForum( forumId ) );
        models.put( "post", new Post( topic ) );
        return "forum/topic/create";
    }

    @RequestMapping(value = "/department/{dept}/forum/topic/create",
        method = RequestMethod.POST)
    public String create(
        @ModelAttribute Post post,
        @PathVariable String dept,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus )
    {
        messageValidator.validate( post, result );
        if( result.hasErrors() ) return "forum/topic/create";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        if( uploadedFiles != null )
            post.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );

        post.setAuthor( user );
        post.setDate( new Date() );
        user.incrementNumOfForumPosts();
        Topic topic = post.getTopic();
        topic.addPost( post );
        Forum forum = topic.getForum();
        forum.incrementNumOfTopics();
        forum.incrementNumOfPosts();
        forum.setLastPost( post );
        topic = topicDao.saveTopic( topic );

        subscriptionDao.subscribe( topic, user );

        String subject = "New Topic in CSNS Forum - " + forum.getShortName();
        String vTemplate = "notification.new.forum.topic.vm";
        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "topic", topic );
        vModels.put( "dept", dept );
        notificationService.notifiy( forum, subject, vTemplate, vModels, false );

        logger.info( user.getUsername() + " created topic " + topic.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/forum/topic/view?id="
            + topic.getId();
    }

    @RequestMapping(value = "/department/{dept}/forum/topic/edit",
        method = RequestMethod.GET)
    public String edit( @RequestParam Long postId, ModelMap models )
    {
        models.put( "post", postDao.getPost( postId ) );
        return "forum/topic/edit";
    }

    @RequestMapping(value = "/department/{dept}/forum/topic/edit",
        method = RequestMethod.POST)
    public String edit(
        @ModelAttribute Post post,
        @PathVariable String dept,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus )
    {
        messageValidator.validate( post, result );
        if( result.hasErrors() ) return "forum/topic/edit";

        User user = SecurityUtils.getUser();
        post.setEditedBy( user );
        post.setEditDate( new Date() );
        if( uploadedFiles != null )
            post.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );
        post = postDao.savePost( post );

        logger.info( user.getUsername() + " edited post " + post.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/forum/topic/view?id="
            + post.getTopic().getId();
    }

    @RequestMapping(value = "/department/{dept}/forum/topic/reply",
        method = RequestMethod.GET)
    public String reply( @RequestParam Long id,
        @RequestParam(required = false) Long postId, ModelMap models )
    {
        Topic topic = topicDao.getTopic( id );
        Post post = new Post();
        post.setTopic( topic );
        post.setSubject( "Re: " + topic.getName() );

        if( postId != null )
        {
            Post replyToPost = postDao.getPost( postId );
            StringBuffer sb = new StringBuffer();
            sb.append( "<blockquote>" )
                .append( "<div class=\"quote-title\">" )
                .append( replyToPost.getAuthor().getUsername() )
                .append( " wrote:</div>" )
                .append( "<div class=\"quote-body\">" )
                .append( replyToPost.getContent() )
                .append( "</div></blockquote><br />" );
            post.setContent( sb.toString() );
        }

        models.put( "post", post );
        return "forum/topic/reply";
    }

    @RequestMapping(value = "/department/{dept}/forum/topic/reply",
        method = RequestMethod.POST)
    public String reply(
        @ModelAttribute Post post,
        @PathVariable String dept,
        @RequestParam(value = "file", required = false) MultipartFile[] uploadedFiles,
        BindingResult result, SessionStatus sessionStatus )
    {
        messageValidator.validate( post, result );
        if( result.hasErrors() ) return "forum/topic/reply";

        User user = userDao.getUser( SecurityUtils.getUser().getId() );
        if( uploadedFiles != null )
            post.getAttachments().addAll(
                fileIO.save( uploadedFiles, user, true ) );

        user.incrementNumOfForumPosts();
        post.setAuthor( user );
        post.setDate( new Date() );
        Topic topic = post.getTopic();
        topic.addPost( post );
        Forum forum = topic.getForum();
        forum.incrementNumOfPosts();
        forum.setLastPost( post );
        topic = topicDao.saveTopic( topic );

        subscriptionDao.subscribe( topic, user );

        String subject = "New Reply in CSNS Forum - " + forum.getShortName();
        String vTemplate = "notification.new.forum.reply.vm";
        Map<String, Object> vModels = new HashMap<String, Object>();
        vModels.put( "topic", topic );
        vModels.put( "dept", dept );
        notificationService.notifiy( topic, subject, vTemplate, vModels, true );

        logger.info( user.getUsername() + " replied to topic " + topic.getId() );

        sessionStatus.setComplete();
        return "redirect:/department/" + dept + "/forum/topic/view?id="
            + post.getTopic().getId();
    }

    @RequestMapping("/department/{dept}/forum/topic/deleteAttachment")
    public @ResponseBody
    String deleteAttachment( @ModelAttribute Post post,
        @RequestParam Long fileId )
    {
        for( File attachment : post.getAttachments() )
            if( attachment.getId().equals( fileId ) )
            {
                post.getAttachments().remove( attachment );
                logger.info( SecurityUtils.getUser().getUsername()
                    + " deleted attachment " + fileId );
                break;
            }

        return "";
    }

}
