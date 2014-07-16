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
package csns.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Course;
import csns.model.academics.Section;
import csns.model.academics.dao.SectionDao;
import csns.model.assessment.Rubric;
import csns.model.assessment.dao.RubricDao;
import csns.model.assessment.dao.RubricEvaluationDao;

@Controller
public class RubricResultsController {

    @Autowired
    private RubricDao rubricDao;

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    @Autowired
    private SectionDao sectionDao;

    @RequestMapping(value = "/department/{dept}/rubric/results", params = "id")
    public String results( @RequestParam Long id, ModelMap models )
    {
        Rubric rubric = rubricDao.getRubric( id );
        List<Section> sections = sectionDao.getSectionsByRubric( rubric );
        Collections.sort( sections );

        Map<Course, List<Section>> mappedSections = new TreeMap<Course, List<Section>>();
        for( Section section : sections )
        {
            List<Section> ss = mappedSections.get( section.getCourse() );
            if( ss == null )
            {
                ss = new ArrayList<Section>();
                mappedSections.put( section.getCourse(), ss );
            }
            ss.add( section );
        }

        models.put( "rubric", rubric );
        models.put( "mappedSections", mappedSections );
        return "rubric/results";
    }

}
