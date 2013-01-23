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

import csns.model.academics.Course;
import csns.model.academics.Department;
import csns.model.forum.Forum;

public interface ForumDao {

    Forum getForum( Long id );

    /**
     * Get the forum by the given name. It is the caller's responsibility to
     * make sure that the forum exists and the name is unique; otherwise an
     * exception will be raised.
     */
    Forum getForum( String name );

    Forum getForum( Course course );

    List<Forum> getSystemForums();

    List<Forum> getCourseForums( Department department );

    List<Forum> searchForums( String term, int maxResults );

    Forum saveForum( Forum forum );

}
