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
package csns.model.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import csns.model.academics.Submission;
import csns.security.SecurityUtils;

@Entity
@Table(name = "files")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    private String type;

    private Long size;

    @Column(nullable = false)
    private Date date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "owner_id")
    private User owner;

    /**
     * The parent field is mostly used for the files created by the file manager
     * to reference their parent folder. For non-regular files (i.e. files not
     * created by the file manager), this field can be used to reference the
     * "previous version" of the file.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private File parent;

    /**
     * The reference is kind of like a shortcut on Windows or a symbolic link on
     * Linux, which indicates that this file is a copy of the referenced file.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private File reference;

    @JsonIgnore
    @Column(name = "folder", nullable = false)
    private boolean isFolder;

    @JsonIgnore
    @Column(name = "public", nullable = false)
    private boolean isPublic;

    /**
     * If this field is not null, it means that this file is part of an
     * assignment submission.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    /**
     * A file is <em>regular</em> if it is uploaded through the file manager
     * interface. There are many non-regular files such as files uploaded as
     * homework submissions, or image files uploaded as parts of a wiki page or
     * a forum post. Only regular files count toward a user's disk quota.
     */
    @JsonIgnore
    @Column(nullable = false)
    private boolean regular;

    @JsonIgnore
    @Column(nullable = false)
    private boolean deleted;

    public File()
    {
        date = new Date();
        isFolder = false;
        isPublic = false;
        regular = false;
        deleted = false;
    }

    public File clone()
    {
        File newFile = new File();

        newFile.name = name;
        newFile.type = type;
        newFile.size = size;
        newFile.date = new Date();
        newFile.owner = SecurityUtils.getUser();

        newFile.parent = parent;
        newFile.reference = this;

        newFile.isFolder = isFolder;
        newFile.isPublic = isPublic;
        newFile.submission = submission;

        newFile.regular = regular;
        newFile.deleted = deleted;

        return newFile;
    }

    public boolean isSameFile( File file )
    {
        return file != null && file.getId().equals( id );
    }

    public boolean isAncestor( File decendent )
    {
        File parent = decendent;
        while( parent != null )
        {
            if( isSameFile( parent ) ) return true;
            parent = parent.getParent();
        }

        return false;
    }

    @JsonIgnore
    public List<File> getAncestors()
    {
        List<File> ancestors = new ArrayList<File>();

        File parent = getParent();
        while( parent != null )
        {
            ancestors.add( 0, parent );
            parent = parent.getParent();
        }

        return ancestors;
    }

    @JsonIgnore
    public String getFullPath()
    {
        String fullPath = name;

        File parent = getParent();
        while( parent != null )
        {
            fullPath = parent.getName() + "/" + fullPath;
            parent = parent.getParent();
        }
        fullPath = "/" + fullPath;

        return fullPath;
    }

    public static String getFileExtension( String fileName )
    {
        int lastDotIndex = fileName.lastIndexOf( '.' );
        return lastDotIndex != -1 ? fileName.substring( lastDotIndex + 1 ) : "";
    }

    @JsonIgnore
    public String getFileExtension()
    {
        return File.getFileExtension( name );
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

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize( Long size )
    {
        this.size = size;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner( User owner )
    {
        this.owner = owner;
    }

    public File getParent()
    {
        return parent;
    }

    public void setParent( File parent )
    {
        this.parent = parent;
    }

    public File getReference()
    {
        return reference;
    }

    public void setReference( File reference )
    {
        this.reference = reference;
    }

    @JsonIgnore
    public boolean isFolder()
    {
        return isFolder;
    }

    public void setFolder( boolean isFolder )
    {
        this.isFolder = isFolder;
    }

    @JsonIgnore
    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic( boolean isPublic )
    {
        this.isPublic = isPublic;
    }

    public Submission getSubmission()
    {
        return submission;
    }

    public void setSubmission( Submission submission )
    {
        this.submission = submission;
    }

    public boolean isRegular()
    {
        return regular;
    }

    public void setRegular( boolean regular )
    {
        this.regular = regular;
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
