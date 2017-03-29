/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2017, Chengyu Sun (csun@calstatela.edu).
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
package csns.helper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csns.model.academics.Course;
import csns.model.academics.Enrollment;
import csns.model.advisement.PersonalProgram;
import csns.model.advisement.PersonalProgramEntry;

public class ProgramChecker {

    List<Map<Course, PersonalProgramEntry>> entryMaps;

    public ProgramChecker( PersonalProgram personalProgram )
    {
        entryMaps = personalProgram.getEntryMaps();
    }

    public int checkRequirements( Collection<Enrollment> enrollments )
    {
        int count = 0; // number of entries updated

        Iterator<Enrollment> iterator = enrollments.iterator();
        while( iterator.hasNext() )
        {
            Enrollment enrollment = iterator.next();
            if( enrollment.getGrade() == null
                || !enrollment.getGrade().isPassingGrade() ) continue;

            Course course = enrollment.getSection().getCourse();
            for( Map<Course, PersonalProgramEntry> entryMap : entryMaps )
            {
                PersonalProgramEntry entry = entryMap.get( course );
                if( entry != null && entry.getEnrollment() == null )
                {
                    entry.setEnrollment( enrollment );
                    iterator.remove();
                    ++count;
                    break;
                }
            }
        }

        return count;
    }

}
