/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import csns.model.core.Resource;
import csns.model.core.ResourceType;
import csns.model.survey.SurveyChart;

@Entity
@Table(name = "assessment_program_measures")
public class ProgramMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "description_id")
    private Resource description;

    @ManyToOne
    @JoinColumn(name = "rubric_id")
    private Rubric rubric;

    @ManyToOne
    @JoinColumn(name = "survey_chart_id")
    private SurveyChart surveyChart;

    public ProgramMeasure()
    {
        description = new Resource( "Program Measure Description",
            ResourceType.TEXT );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Resource getDescription()
    {
        return description;
    }

    public void setDescription( Resource description )
    {
        this.description = description;
    }

    public Rubric getRubric()
    {
        return rubric;
    }

    public void setRubric( Rubric rubric )
    {
        this.rubric = rubric;
    }

    public SurveyChart getSurveyChart()
    {
        return surveyChart;
    }

    public void setSurveyChart( SurveyChart surveyChart )
    {
        this.surveyChart = surveyChart;
    }

}
