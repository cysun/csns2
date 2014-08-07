/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2014, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import csns.model.academics.Department;
import csns.model.core.User;

@Entity
@Table(name = "survey_charts")
public class SurveyChart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "x_title")
    private String xTitle;

    @ElementCollection
    @CollectionTable(name = "survey_chart_xlabels",
        joinColumns = @JoinColumn(name = "chart_id"))
    @Column(name = "xlabel")
    @OrderColumn(name = "label_order")
    private List<String> xLabels;

    @Column(name = "y_title")
    private String yTitle;

    @Column(name = "y_min")
    private Integer yMin;

    @Column(name = "y_max")
    private Integer yMax;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chart_id")
    @OrderColumn(name = "series_index")
    private List<SurveyChartSeries> series;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private Date date;

    private boolean deleted;

    public SurveyChart()
    {
        xLabels = new ArrayList<String>();
        series = new ArrayList<SurveyChartSeries>();
        date = new Date();
        deleted = false;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getxTitle()
    {
        return xTitle;
    }

    public void setxTitle( String xTitle )
    {
        this.xTitle = xTitle;
    }

    public List<String> getxLabels()
    {
        return xLabels;
    }

    public void setxLabels( List<String> xLabels )
    {
        this.xLabels = xLabels;
    }

    public String getyTitle()
    {
        return yTitle;
    }

    public void setyTitle( String yTitle )
    {
        this.yTitle = yTitle;
    }

    public Integer getyMin()
    {
        return yMin;
    }

    public void setyMin( Integer yMin )
    {
        this.yMin = yMin;
    }

    public Integer getyMax()
    {
        return yMax;
    }

    public void setyMax( Integer yMax )
    {
        this.yMax = yMax;
    }

    public List<SurveyChartSeries> getSeries()
    {
        return series;
    }

    public void setSeries( List<SurveyChartSeries> series )
    {
        this.series = series;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor( User author )
    {
        this.author = author;
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

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted( boolean deleted )
    {
        this.deleted = deleted;
    }

}
