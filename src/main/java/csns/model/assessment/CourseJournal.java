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
package csns.model.assessment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import csns.model.academics.Assignment;
import csns.model.academics.Enrollment;
import csns.model.academics.Section;
import csns.model.core.Resource;

@Entity
@Table(name = "course_journals")
public class CourseJournal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "journal")
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "course_journal_handouts",
        joinColumns = @JoinColumn(name = "course_journal_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @OrderColumn(name = "handout_order")
    private List<Resource> handouts;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "course_journal_assignments",
        joinColumns = @JoinColumn(name = "course_journal_id"),
        inverseJoinColumns = @JoinColumn(name = "assignment_id"))
    @OrderColumn(name = "assignment_order")
    private List<Assignment> assignments;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "course_journal_student_samples",
        joinColumns = @JoinColumn(name = "course_journal_id"),
        inverseJoinColumns = @JoinColumn(name = "enrollment_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {
            "course_journal_id", "enrollment_id" }))
    @OrderBy("id asc")
    private List<Enrollment> studentSamples;

    @Column(name = "submit_date")
    private Date submitDate;

    @Column(name = "approve_date")
    private Date approveDate;

    public CourseJournal()
    {
        handouts = new ArrayList<Resource>();
        assignments = new ArrayList<Assignment>();
        studentSamples = new ArrayList<Enrollment>();
    }

    public CourseJournal( Section section )
    {
        this();
        this.section = section;
    }

    public boolean isSubmitted()
    {
        return submitDate != null;
    }

    public boolean isApproved()
    {
        return approveDate != null;
    }

    public boolean removeHandout( Long resourceId )
    {
        for( Resource handout : handouts )
            if( handout.getId().equals( resourceId ) )
                return handouts.remove( handout );

        return false;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Section getSection()
    {
        return section;
    }

    public void setSection( Section section )
    {
        this.section = section;
    }

    public List<Resource> getHandouts()
    {
        return handouts;
    }

    public void setHandouts( List<Resource> handouts )
    {
        this.handouts = handouts;
    }

    public List<Assignment> getAssignments()
    {
        return assignments;
    }

    public void setAssignments( List<Assignment> assignments )
    {
        this.assignments = assignments;
    }

    public List<Enrollment> getStudentSamples()
    {
        return studentSamples;
    }

    public void setStudentSamples( List<Enrollment> studentSamples )
    {
        this.studentSamples = studentSamples;
    }

    public Date getSubmitDate()
    {
        return submitDate;
    }

    public void setSubmitDate( Date submitDate )
    {
        this.submitDate = submitDate;
    }

    public Date getApproveDate()
    {
        return approveDate;
    }

    public void setApproveDate( Date approveDate )
    {
        this.approveDate = approveDate;
    }

}
