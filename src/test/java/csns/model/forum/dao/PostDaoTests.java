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
package csns.model.forum.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import csns.model.forum.Forum;
import csns.model.forum.Post;

@Test(groups = "PostDaoTests", dependsOnGroups = "ForumDaoTests")
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class PostDaoTests extends AbstractTestNGSpringContextTests {

    @Autowired
    ForumDao forumDao;

    @Autowired
    PostDao postDao;

    @Test
    public void searchPosts()
    {
        Forum forum = forumDao.getForum( 3000L );
        List<Post> posts = postDao.searchPosts( forum, "welcome", 10 );
        assert posts.size() == 1;
    }

}
