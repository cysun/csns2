/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2014, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.qa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import csns.model.core.File;

@Entity
@DiscriminatorValue("TEXT")
public class TextAnswer extends Answer {

    private static final long serialVersionUID = 1L;

    private String text;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private File attachment;

    public TextAnswer()
    {
    }

    public TextAnswer( TextQuestion textQuestion )
    {
        super( textQuestion );
    }

    @Override
    public int check()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return text != null ? text : "";
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public File getAttachment()
    {
        return attachment;
    }

    public void setAttachment( File attachment )
    {
        this.attachment = attachment;
    }

}
