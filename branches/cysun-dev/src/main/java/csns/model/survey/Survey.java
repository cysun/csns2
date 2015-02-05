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
package csns.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.academics.Department;
import csns.model.core.User;
import csns.model.qa.QuestionSheet;

@Entity
@Table(name = "surveys")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private SurveyType type;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
        fetch = FetchType.LAZY)
    @JoinColumn(name = "question_sheet_id", nullable = false, unique = true)
    private QuestionSheet questionSheet;

    @Column(name = "publish_date")
    private Calendar publishDate;

    @Column(name = "close_date")
    private Calendar closeDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private Date date;

    @OneToMany(mappedBy = "survey")
    @OrderBy("id asc")
    private List<SurveyResponse> responses;

    @Column(nullable = false)
    private boolean deleted;

    public Survey()
    {
        type = SurveyType.ANONYMOUS;
        questionSheet = new QuestionSheet();
        responses = new ArrayList<SurveyResponse>();
        deleted = false;
    }

    public Survey clone()
    {
        Survey newSurvey = new Survey();
        newSurvey.name = "Copy of " + name;
        newSurvey.type = type;
        newSurvey.questionSheet = questionSheet.clone();

        return newSurvey;
    }

    public boolean isPublished()
    {
        return publishDate != null
            && Calendar.getInstance().after( publishDate );
    }

    public boolean isClosed()
    {
        return closeDate != null && Calendar.getInstance().after( closeDate );
    }

    public int getNumOfResponses()
    {
        return responses.size();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public SurveyType getType()
    {
        return type;
    }

    public void setType( SurveyType type )
    {
        this.type = type;
    }

    public QuestionSheet getQuestionSheet()
    {
        return questionSheet;
    }

    public void setQuestionSheet( QuestionSheet questionSheet )
    {
        this.questionSheet = questionSheet;
    }

    public Calendar getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate( Calendar publishDate )
    {
        this.publishDate = publishDate;
    }

    public Calendar getCloseDate()
    {
        return closeDate;
    }

    public void setCloseDate( Calendar closeDate )
    {
        this.closeDate = closeDate;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor( User author )
    {
        this.author = author;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public List<SurveyResponse> getResponses()
    {
        return responses;
    }

    public void setResponses( List<SurveyResponse> responses )
    {
        this.responses = responses;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted( boolean deleted )
    {
        this.deleted = deleted;
    }

}
