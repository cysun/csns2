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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.assessment.RubricAssignment;
import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.RubricSubmission;
import csns.model.assessment.dao.RubricEvaluationDao;
import csns.model.assessment.dao.RubricSubmissionDao;
import csns.model.core.User;
import csns.security.SecurityUtils;

@Controller
public class RubricEvaluationController {

    @Autowired
    private RubricSubmissionDao rubricSubmissionDao;

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    private static final Logger logger = LoggerFactory.getLogger( RubricEvaluationController.class );

    @RequestMapping("/rubric/evaluation/{role}/view")
    public String view( @PathVariable String role,
        @RequestParam Long submissionId, ModelMap models )
    {
        User user = SecurityUtils.getUser();
        RubricSubmission submission = rubricSubmissionDao.getRubricSubmission( submissionId );
        RubricAssignment assignment = submission.getAssignment();
        if( assignment.isPublished() && !assignment.isPastDue() )
        {
            RubricEvaluation evaluation = submission.getEvaluation( user );
            if( evaluation == null
                && !submission.getStudent().isSameUser( user ) )
            {
                evaluation = new RubricEvaluation( submission, user );
                submission.getEvaluations().add( evaluation );
                submission = rubricSubmissionDao.saveRubricSubmission( submission );
            }
        }

        models.put( "role", role );
        models.put( "submission", submission );
        models.put( "evaluation", submission.getEvaluation( user ) );
        return "rubric/evaluation/view";
    }

    @RequestMapping(value = "/rubric/evaluation/{role}/set", params = {
        "index", "value" })
    @ResponseBody
    public ResponseEntity<String> set( @RequestParam Long id,
        @RequestParam int index, @RequestParam int value )
    {
        RubricEvaluation evaluation = rubricEvaluationDao.getRubricEvaluation( id );
        // Ignore the request if the rubric assignment is already past due.
        if( evaluation.getSubmission().getAssignment().isPastDue() )
            return new ResponseEntity<String>( HttpStatus.BAD_REQUEST );

        evaluation.getRatings().set( index, value + 1 );
        evaluation.setCompleted();
        rubricEvaluationDao.saveRubricEvaluation( evaluation );

        logger.info( SecurityUtils.getUser().getUsername() + " rated "
            + (value + 1) + " for indicator " + index
            + " in rubric evaluation " + id );

        return new ResponseEntity<String>( HttpStatus.OK );
    }

    @RequestMapping(value = "/rubric/evaluation/{role}/set",
        params = "comments")
    @ResponseBody
    public ResponseEntity<String> set( @RequestParam Long id,
        @RequestParam String comments )
    {
        RubricEvaluation evaluation = rubricEvaluationDao.getRubricEvaluation( id );
        // Ignore the request if the rubric assignment is already past due.
        if( evaluation.getSubmission().getAssignment().isPastDue() )
            return new ResponseEntity<String>( HttpStatus.BAD_REQUEST );

        evaluation.setComments( comments );
        rubricEvaluationDao.saveRubricEvaluation( evaluation );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.TEXT_PLAIN );
        return new ResponseEntity<String>( comments, headers, HttpStatus.OK );
    }

}
