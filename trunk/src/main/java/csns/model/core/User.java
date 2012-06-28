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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "users")
public class User implements Serializable, Comparable<User>, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String cin;

    /**
     * CSNS used to encrypt CIN, but we no longer do that because it's better to
     * have access to the CINs for student and class management. Some CINs in
     * the system are still encrypted, and these CINs are indicated by
     * <code>cinEncrypted=true</code>.
     */
    @Column(name = "cin_encrypted", nullable = false)
    private boolean cinEncrypted;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @ElementCollection
    @CollectionTable(name = "authorities",
        joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    private String gender;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private String address1;

    private String address2;

    private String city;

    private String state;

    private String zip;

    @Column(name = "primary_email", nullable = false, unique = true)
    private String primaryEmail;

    @Column(name = "secondary_email", unique = true)
    private String secondaryEmail;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "office_phone")
    private String officePhone;

    public User()
    {
        cinEncrypted = false;
        enabled = true;
        roles = new HashSet<String>();
    }

    public int compareTo( User user )
    {
        if( user == null )
            throw new IllegalArgumentException( "Cannot compare to NULL user" );

        int comparison = getLastName().compareTo( user.getLastName() );
        return comparison != 0 ? comparison : getFirstName().compareTo(
            user.getFirstName() );
    }

    public boolean isSameUser( User user )
    {
        return user != null && user.getId().equals( id );
    }

    public boolean isAdmin()
    {
        return roles.contains( "ROLE_ADMIN" );
    }

    public boolean isDepartmentAdmin()
    {
        for( String role : roles )
            if( role.startsWith( "DEPT_ROLE_ADMIN_" ) ) return true;

        return false;
    }

    public boolean isFaculty()
    {
        for( String role : roles )
            if( role.startsWith( "DEPT_ROLE_FACULTY_" ) ) return true;

        return false;
    }

    public boolean isInstructor()
    {
        for( String role : roles )
            if( role.startsWith( "DEPT_ROLE_INSTRUCTOR_" ) ) return true;

        return false;
    }

    public List<String> getDepartments( String roleName )
    {
        String departmentRole = "DEPT_" + roleName + "_";

        List<String> departments = new ArrayList<String>();
        for( String role : roles )
            if( role.startsWith( departmentRole ) )
                departments.add( role.substring( departmentRole.length() ) );

        return departments;
    }

    public String getName()
    {
        return firstName + " " + lastName;
    }

    public String getAddress()
    {
        StringBuffer address = address1 != null ? new StringBuffer( address1 )
            : new StringBuffer();

        if( StringUtils.hasText( address2 ) )
            address.append( ", " ).append( address2 );
        if( StringUtils.hasText( city ) )
            address.append( ", " ).append( city );
        if( StringUtils.hasText( state ) )
            address.append( ", " ).append( state );
        if( StringUtils.hasText( zip ) ) address.append( " " ).append( zip );

        return address.toString();
    }

    public String getEmail()
    {
        return primaryEmail != null ? primaryEmail : secondaryEmail;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities()
    {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for( String role : roles )
            authorities.add( new SimpleGrantedAuthority( role ) );
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    public boolean hasRole( String role )
    {
        return roles.contains( role );
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getCin()
    {
        return cin;
    }

    public void setCin( String cin )
    {
        this.cin = cin;
    }

    public boolean isCinEncrypted()
    {
        return cinEncrypted;
    }

    public void setCinEncrypted( boolean cinEncrypted )
    {
        this.cinEncrypted = cinEncrypted;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public void setRoles( Set<String> roles )
    {
        this.roles = roles;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender( String gender )
    {
        this.gender = gender;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday( Date birthday )
    {
        this.birthday = birthday;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1( String address1 )
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2( String address2 )
    {
        this.address2 = address2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip( String zip )
    {
        this.zip = zip;
    }

    public String getPrimaryEmail()
    {
        return primaryEmail;
    }

    public void setPrimaryEmail( String primaryEmail )
    {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail()
    {
        return secondaryEmail;
    }

    public void setSecondaryEmail( String secondaryEmail )
    {
        this.secondaryEmail = secondaryEmail;
    }

    public String getCellPhone()
    {
        return cellPhone;
    }

    public void setCellPhone( String cellPhone )
    {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone()
    {
        return homePhone;
    }

    public void setHomePhone( String homePhone )
    {
        this.homePhone = homePhone;
    }

    public String getOfficePhone()
    {
        return officePhone;
    }

    public void setOfficePhone( String officePhone )
    {
        this.officePhone = officePhone;
    }

}
