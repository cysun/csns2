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
package csns.importer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import csns.model.academics.Department;
import csns.model.assessment.MFTScore;
import csns.model.core.User;

public class MFTScoreImporter {

    private Department department;

    private Date date;

    private String text;

    private List<MFTScore> scores;

    private List<User> failedUsers;

    public MFTScoreImporter()
    {
        scores = new ArrayList<MFTScore>();
        failedUsers = new ArrayList<User>();
    }

    public void clear()
    {
        scores.clear();
        failedUsers.clear();
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public List<MFTScore> getScores()
    {
        return scores;
    }

    public void setScores( List<MFTScore> scores )
    {
        this.scores = scores;
    }

    public List<User> getFailedUsers()
    {
        return failedUsers;
    }

    public void setFailedUsers( List<User> failedUsers )
    {
        this.failedUsers = failedUsers;
    }

}
