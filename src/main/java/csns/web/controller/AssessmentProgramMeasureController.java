/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Chengyu Sun (csun@calstatela.edu).
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import csns.model.assessment.ProgramMeasure;
import csns.model.assessment.ProgramOutcome;
import csns.model.assessment.dao.ProgramOutcomeDao;
import csns.security.SecurityUtils;

@Controller
public class AssessmentProgramMeasureController {

    @Autowired
    private ProgramOutcomeDao programOutcomeDao;

    private static final Logger logger = LoggerFactory
        .getLogger( AssessmentProgramMeasureController.class );

    @RequestMapping("/department/{dept}/assessment/program/{field}/measures")
    public String measures( @PathVariable String field,
        @RequestParam Long fieldId, ModelMap models )
    {
        switch( field )
        {
            case "outcome":
                models.put( "outcome",
                    programOutcomeDao.getProgramOutcome( fieldId ) );
                return "assessment/program/measure/" + field;

            default:
                logger.warn( "Unsupported program field: " + field );
                return "redirect:../../list";
        }
    }

    @RequestMapping("/department/{dept}/assessment/program/{field}/measure/remove")
    public String remove( @PathVariable String field,
        @RequestParam Long fieldId, @RequestParam Long measureId )
    {
        switch( field )
        {
            case "outcome":
                ProgramOutcome outcome = programOutcomeDao
                    .getProgramOutcome( fieldId );
                outcome.removeMeasure( measureId );
                outcome = programOutcomeDao.saveProgramOutcome( outcome );

                logger.info(
                    SecurityUtils.getUser().getUsername() + " removed measure "
                        + measureId + " from " + field + " " + fieldId );

                return "redirect:../measures?fieldId=" + fieldId + "&edit=true";

            default:
                logger.warn( "Unsupported program field: " + field );
                return "redirect:../../list";
        }
    }

    @RequestMapping("/department/{dept}/assessment/program/{field}/measure/reorder")
    @ResponseStatus(HttpStatus.OK)
    public void reorder( @PathVariable String field, @RequestParam Long fieldId,
        @RequestParam Long measureId, @RequestParam int newIndex )
    {
        switch( field )
        {
            case "outcome":
                ProgramOutcome outcome = programOutcomeDao
                    .getProgramOutcome( fieldId );
                ProgramMeasure measure = outcome.removeMeasure( measureId );
                if( measure != null )
                    outcome.getMeasures().add( newIndex, measure );
                outcome = programOutcomeDao.saveProgramOutcome( outcome );
                break;

            default:
                logger.warn( "Unsupported program field: " + field );
        }

        logger.info( SecurityUtils.getUser().getUsername()
            + " reordered measure " + measureId + " in assessment program "
            + field + " " + fieldId + " to index " + newIndex );
    }

}
