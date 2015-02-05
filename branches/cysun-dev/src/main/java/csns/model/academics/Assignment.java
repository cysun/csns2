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
package csns.model.academics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import csns.model.academics.Section;
import csns.model.core.Resource;

@Entity
@Table(name = "assignments")
@Inheritance
@DiscriminatorColumn(name = "assignment_type")
@DiscriminatorValue("REGULAR")
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "resource_id")
    protected Resource description;

    @Column(nullable = false)
    protected String alias;

    @Column(name = "total_points")
    protected String totalPoints;

    @Column(name = "publish_date")
    protected Calendar publishDate;

    @Column(name = "due_date")
    protected Calendar dueDate;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    protected Section section;

    @Column(name = "max_file_size")
    protected Long maxFileSize;

    @Column(name = "file_extensions")
    protected String fileExtensions;

    @Column(name = "available_after_due_date", nullable = false)
    protected boolean availableAfterDueDate;

    @OneToMany(mappedBy = "assignment", cascade = { CascadeType.MERGE,
        CascadeType.PERSIST })
    protected List<Submission> submissions;

    @Column(nullable = false)
    protected boolean deleted;

    @Transient
    protected Set<String> fileExtensionSet;

    public Assignment()
    {
        dueDate = Calendar.getInstance();
        dueDate.add( Calendar.DATE, 7 );
        dueDate.set( Calendar.HOUR_OF_DAY, 23 );
        dueDate.set( Calendar.MINUTE, 59 );
        dueDate.set( Calendar.SECOND, 59 );
        dueDate.set( Calendar.MILLISECOND, 0 );

        submissions = new ArrayList<Submission>();
        fileExtensionSet = new HashSet<String>();
        availableAfterDueDate = true;
        deleted = false;
    }

    public Assignment clone()
    {
        Assignment assignment = new Assignment();

        assignment.name = name;
        assignment.alias = alias;
        assignment.totalPoints = totalPoints;
        assignment.dueDate = null;
        assignment.maxFileSize = maxFileSize;
        assignment.fileExtensions = fileExtensions;
        assignment.availableAfterDueDate = availableAfterDueDate;

        if( description != null ) assignment.description = description.clone();

        return assignment;
    }

    public boolean isOnline()
    {
        return false;
    }

    public boolean isPastDue()
    {
        return dueDate != null && Calendar.getInstance().after( dueDate );
    }

    public boolean isPublished()
    {
        return publishDate != null
            && Calendar.getInstance().after( publishDate );
    }

    public boolean isFileExtensionAllowed( String fileExtension )
    {
        if( StringUtils.hasText( fileExtensions ) && fileExtensionSet.isEmpty() )
        {
            String extensions[] = fileExtensions.split( " " );
            fileExtensionSet.clear();
            for( String extension : extensions )
                fileExtensionSet.add( extension.toLowerCase() );
        }

        return fileExtensionSet.isEmpty() ? true
            : fileExtensionSet.contains( fileExtension.toLowerCase() );
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

    public Resource getDescription()
    {
        return description;
    }

    public void setDescription( Resource description )
    {
        this.description = description;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias( String alias )
    {
        this.alias = alias;
    }

    public String getTotalPoints()
    {
        return totalPoints;
    }

    public void setTotalPoints( String totalPoints )
    {
        this.totalPoints = totalPoints;
    }

    public Calendar getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate( Calendar publishDate )
    {
        this.publishDate = publishDate;
    }

    public Calendar getDueDate()
    {
        return dueDate;
    }

    public void setDueDate( Calendar dueDate )
    {
        this.dueDate = dueDate;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public Long getMaxFileSize()
    {
        return maxFileSize;
    }

    public void setMaxFileSize( Long maxFileSize )
    {
        this.maxFileSize = maxFileSize;
    }

    public String getFileExtensions()
    {
        return fileExtensions;
    }

    public void setFileExtensions( String fileExtensions )
    {
        this.fileExtensions = fileExtensions;
    }

    public boolean isAvailableAfterDueDate()
    {
        return availableAfterDueDate;
    }

    public void setAvailableAfterDueDate( boolean availableAfterDueDate )
    {
        this.availableAfterDueDate = availableAfterDueDate;
    }

    public List<Submission> getSubmissions()
    {
        return submissions;
    }

    public void setSubmissions( List<Submission> submissions )
    {
        this.submissions = submissions;
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
