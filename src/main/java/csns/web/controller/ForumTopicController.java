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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csns.model.core.File;
import csns.model.core.Subscription;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.model.core.dao.SubscriptionDao;
import csns.model.core.dao.UserDao;
import csns.model.forum.Forum;
import csns.model.forum.Post;
import csns.model.forum.Topic;
import csns.model.forum.dao.ForumDao;
import csns.model.forum.dao.TopicDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;
import csns.util.MassMailSender;
import csns.web.helper.ServiceResponse;
import csns.web.validator.MessageValidator;

@Controller
@SessionAttributes("post")
public class ForumTopicController {

    @Autowired
    UserDao userDao;

    @Autowired
    FileDao fileDao;

    @Autowired
    ForumDao forumDao;

    @Autowired
    TopicDao topicDao;

    @Autowired
    SubscriptionDao subscriptionDao;

    @Autowired
    MessageValidator messageValidator;

    @Autowired
    FileIO fileIO;

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    MassMailSender massMailSender;

    @Value("#{applicationProperties.url}")
    String appUrl;

    @Value("#{applicationProperties.email}")
    String appEmail;

    private static final Logger logger = LoggerFactory.getLogger( ForumTopicController.class );

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
            for( MultipartFile uploadedFile : uploadedFiles )
            {
                if( uploadedFile.isEmpty() ) continue;

                File file = new File();
                file.setName( uploadedFile.getOriginalFilename() );
                file.setType( uploadedFile.getContentType() );
                file.setSize( uploadedFile.getSize() );
                file.setOwner( user );
                file.setPublic( true );
                file = fileDao.saveFile( file );
                fileIO.save( file, uploadedFile );
                post.getAttachments().add( file );
            }

        post.setAuthor( user );
        post.setDate( new Date() );
        user.incrementNumOfForumPosts();
        Topic topic = post.getTopic();
        Forum forum = topic.getForum();
        topic.addPost( post );
        forum.incrementNumOfTopics();
        forum.incrementNumOfPosts();
        forum.setLastPost( post );
        topic = topicDao.saveTopic( topic );

        subscriptionDao.subscribe( topic, user );

        List<Subscription> subscriptions = subscriptionDao.getSubscriptions( forum );
        List<String> addresses = new ArrayList<String>();
        for( Subscription subscription : subscriptions )
            if( subscription.getSubscriber() != user )
                addresses.add( subscription.getSubscriber().getEmail() );
        if( addresses.size() > 0 )
        {
            Map<String, Object> vModels = new HashMap<String, Object>();
            vModels.put( "topic", topic );
            vModels.put( "dept", dept );
            vModels.put( "appUrl", appUrl );
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, "notification.new.forum.topic.vm", vModels );

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom( appEmail );
            email.setTo( appEmail );
            email.setSubject( "New Topic in CSNS Forum - "
                + forum.getShortName() );
            email.setText( text );
            massMailSender.send( email, addresses );
        }

        return "redirect:/department/" + dept + "/forum/topic/view?id="
            + topic.getId();
    }

    @RequestMapping("/department/{dept}/forum/topic/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        Topic topic = topicDao.getTopic( id );
        topic.incrementNumOfViews();
        topicDao.saveTopic( topic );

        if( SecurityUtils.isAuthenticated() )
        {
            User user = userDao.getUser( SecurityUtils.getUser().getId() );
            models.put( "isModerator", topic.getForum().isModerator( user ) );
            models.put( "subscription",
                subscriptionDao.getSubscription( topic, user ) );
        }

        models.put( "topic", topic );
        return "forum/topic/view";
    }

    @RequestMapping("/department/{dept}/forum/topic/pin")
    public String pin( @RequestParam Long id, ModelMap models )
    {
        Topic topic = topicDao.getTopic( id );
        topic.setPinned( true );
        topicDao.saveTopic( topic );

        logger.info( SecurityUtils.getUser().getUsername() + " pinned topic "
            + topic.getId() );

        models.put( "result", new ServiceResponse() );
        return "jsonView";
    }

    @RequestMapping("/department/{dept}/forum/topic/unpin")
    public String unpin( @RequestParam Long id, ModelMap models )
    {
        Topic topic = topicDao.getTopic( id );
        topic.setPinned( false );
        topicDao.saveTopic( topic );

        logger.info( SecurityUtils.getUser().getUsername() + " unpinned topic "
            + topic.getId() );

        models.put( "result", new ServiceResponse() );
        return "jsonView";
    }

    @RequestMapping("/department/{dept}/forum/topic/delete")
    public String delete( @PathVariable String dept, @RequestParam Long id )
    {
        Topic topic = topicDao.getTopic( id );
        topic.setDeleted( true );
        topicDao.saveTopic( topic );

        logger.info( SecurityUtils.getUser().getUsername() + " deleted topic "
            + topic.getId() );

        return "redirect:/department/" + dept + "/forum/view?id="
            + topic.getForum().getId();
    }

}
