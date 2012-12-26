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
package csns.model.advisement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import csns.model.core.File;
import csns.model.core.User;

@Entity
@Table(name = "advisement_records")
public class AdvisementRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private User advisor;

    private String comment;

    @ManyToMany
    @JoinTable(name = "advisement_record_attachments",
        joinColumns = @JoinColumn(name = "record_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id"))
    @OrderBy("name asc")
    private List<File> attachments;

    private Date date;

    // Whether the record is editable. Some auto-generated records for course
    // waivers and transfers and so on are not editable.
    private boolean editable;

    // Whether comment is for advisors only, i.e. whether the record is hidden
    // from the student.
    @Column(name = "for_advisors_only", nullable = false)
    private boolean forAdvisorsOnly;

    // Whether the comment has been emailed to the student.
    @Column(name = "emailed_to_student", nullable = false)
    private boolean emailedToStudent;

    private boolean deleted;

    public AdvisementRecord()
    {
        attachments = new ArrayList<File>();
        date = new Date();
        editable = true;
        forAdvisorsOnly = false;
        emailedToStudent = false;
        deleted = false;
    }

    public AdvisementRecord( User student, User advisor )
    {
        this();

        this.student = student;
        this.advisor = advisor;
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

    public User getAdvisor()
    {
        return advisor;
    }

    public void setAdvisor( User advisor )
    {
        this.advisor = advisor;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public List<File> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( List<File> attachments )
    {
        this.attachments = attachments;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable( boolean editable )
    {
        this.editable = editable;
    }

    public boolean isForAdvisorsOnly()
    {
        return forAdvisorsOnly;
    }

    public void setForAdvisorsOnly( boolean forAdvisorsOnly )
    {
        this.forAdvisorsOnly = forAdvisorsOnly;
    }

    public boolean isEmailedToStudent()
    {
        return emailedToStudent;
    }

    public void setEmailedToStudent( boolean emailedToStudent )
    {
        this.emailedToStudent = emailedToStudent;
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
