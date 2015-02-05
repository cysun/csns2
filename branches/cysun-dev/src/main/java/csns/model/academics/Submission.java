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
import java.util.List;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import csns.model.core.File;
import csns.model.core.User;

@Entity
@Table(name = "submissions",
    uniqueConstraints = @UniqueConstraint(columnNames = { "student_id",
        "assignment_id" }))
@Inheritance
@DiscriminatorColumn(name = "submission_type")
@DiscriminatorValue("REGULAR")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    protected User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    protected Assignment assignment;

    @Column(name = "due_date")
    protected Calendar dueDate;

    protected String grade;

    protected String comments;

    @Column(name = "grade_mailed", nullable = false)
    protected boolean gradeMailed;

    // I can't quite remember why I added fileCount instead of just using
    // files.size(). It's probably because files.size() will cause the
    // lazy-loaded files collection to be initialized, and it'd be very
    // inefficient for the submission list page (which only needs the file
    // count) when there are lots of submissions.
    @Column(name = "file_count", nullable = false)
    protected int fileCount;

    @OneToMany(mappedBy = "submission")
    @OrderBy("name asc")
    protected List<File> files;

    public Submission()
    {
        gradeMailed = false;
        fileCount = 0;
        files = new ArrayList<File>();
    }

    public Submission( User student, Assignment assignment )
    {
        this();
        this.student = student;
        this.assignment = assignment;
    }

    public boolean isOnline()
    {
        return false;
    }

    public void incrementFileCount()
    {
        ++fileCount;
    }

    public void decrementFileCount()
    {
        --fileCount;
    }

    public File getFileByName( String fileName )
    {
        for( File file : files )
            if( file.getName().equals( fileName ) ) return file;

        return null;
    }

    public Calendar getEffectiveDueDate()
    {
        return dueDate != null ? dueDate : getAssignment().getDueDate();
    }

    public boolean isPastDue()
    {
        return Calendar.getInstance().after( getEffectiveDueDate() );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public User getStudent()
    {
        return student;
    }

    public void setStudent( User student )
    {
        this.student = student;
    }

    public Assignment getAssignment()
    {
        return assignment;
    }

    public void setAssignment( Assignment assignment )
    {
        this.assignment = assignment;
    }

    public Calendar getDueDate()
    {
        return dueDate;
    }

    public void setDueDate( Calendar dueDate )
    {
        this.dueDate = dueDate;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade( String grade )
    {
        this.grade = grade;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public boolean isGradeMailed()
    {
        return gradeMailed;
    }

    public void setGradeMailed( boolean gradeMailed )
    {
        this.gradeMailed = gradeMailed;
    }

    public int getFileCount()
    {
        return fileCount;
    }

    public void setFileCount( int fileCount )
    {
        this.fileCount = fileCount;
    }

    public List<File> getFiles()
    {
        return files;
    }

    public void setFiles( List<File> files )
    {
        this.files = files;
    }

}
