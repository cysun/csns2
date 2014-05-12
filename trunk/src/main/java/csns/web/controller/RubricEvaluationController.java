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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csns.model.assessment.RubricEvaluation;
import csns.model.assessment.dao.RubricEvaluationDao;

@Controller
public class RubricEvaluationController {

    @Autowired
    private RubricEvaluationDao rubricEvaluationDao;

    @RequestMapping("/rubric/evaluation/view")
    public String view( @RequestParam Long id, ModelMap models )
    {
        models.put( "evaluation", rubricEvaluationDao.getRubricEvaluation( id ) );
        return "rubric/evaluation/view";
    }

    @RequestMapping("/rubric/evaluation/set")
    @ResponseBody
    public String set( @RequestParam Long id, @RequestParam int index,
        @RequestParam int value )
    {
        RubricEvaluation evaluation = rubricEvaluationDao.getRubricEvaluation( id );
        evaluation.getRatings().set( index, value );
        rubricEvaluationDao.saveRubricEvaluation( evaluation );
        return "";
    }

}
