/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2013, Chengyu Sun (csun@calstatela.edu).
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
package csns.model.assessment.dao;

import java.util.Date;
import java.util.List;

import csns.model.academics.Department;
import csns.model.assessment.MFTScore;
import csns.model.core.User;

public interface MFTScoreDao {

    List<Date> getDates( Department department );

    List<Integer> getYears( Department department );

    MFTScore getScore( Department department, Date date, User user );

    List<MFTScore> getScores( Department department, Date date );

    List<MFTScore> getScores( Department department, Integer year );

    List<MFTScore> getMedianScores( Department department, Integer beginYear,
        Integer endYear );

    MFTScore saveScore( MFTScore score );

}
