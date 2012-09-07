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
package csns.web.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import csns.model.core.File;
import csns.model.core.Message;
import csns.model.core.User;

public class Email extends Message {

    private static final long serialVersionUID = 1L;

    private final String attachmentDownloadUrl;

    private List<User> recipients;

    private List<File> attachments;

    public Email( Properties applicationProperties )
    {
        super();

        attachmentDownloadUrl = applicationProperties.getProperty( "url" )
            + "/download?fileId=";

        recipients = new ArrayList<User>();
        attachments = new ArrayList<File>();
    }

    public void addAttachment( File file )
    {
        if( attachments == null ) attachments = new ArrayList<File>();
        attachments.add( file );

        StringBuffer sb = new StringBuffer();
        int index = attachments.size();
        if( index == 1 ) sb.append( "\n\n[Attachments]\n\n" );
        sb.append( "\t(" )
            .append( index )
            .append( ") " )
            .append( file.getName() )
            .append( " - " )
            .append( attachmentDownloadUrl )
            .append( file.getId() )
            .append( "\n" );

        content += sb;
    }

    public String[] getTo()
    {
        String to[] = new String[recipients.size()];
        for( int i = 0; i < recipients.size(); ++i )
            to[i] = recipients.get( i ).getPrimaryEmail();
        return to;
    }

    public List<User> getRecipients()
    {
        return recipients;
    }

    public void setRecipients( List<User> recipients )
    {
        this.recipients = recipients;
    }

    public List<File> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( List<File> attachments )
    {
        this.attachments = attachments;
    }

}
