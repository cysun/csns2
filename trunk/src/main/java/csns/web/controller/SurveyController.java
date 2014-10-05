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
package csns.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Department;
import csns.model.academics.dao.DepartmentDao;
import csns.model.core.User;
import csns.model.qa.Question;
import csns.model.qa.QuestionSection;
import csns.model.survey.Survey;
import csns.model.survey.SurveyType;
import csns.model.survey.dao.SurveyDao;
import csns.security.SecurityUtils;

@Controller
public class SurveyController {

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Resource(name = "contentTypes")
    private Properties contentTypes;

    private static final Logger logger = LoggerFactory.getLogger( SurveyController.class );

    @RequestMapping(value = "/department/{dept}/survey/autocomplete")
    public String autocomplete( @PathVariable String dept,
        @RequestParam String term, HttpServletResponse response )
        throws JSONException, IOException
    {
        Department department = departmentDao.getDepartment( dept );
        JSONArray jsonArray = new JSONArray();
        List<Survey> surveys = surveyDao.searchSurveysByPrefix( department,
            term, 20 );
        for( Survey survey : surveys )
        {
            Map<String, String> json = new HashMap<String, String>();
            json.put( "id", survey.getId().toString() );
            json.put( "value", survey.getName() );
            json.put( "label", survey.getName() );
            jsonArray.put( json );
        }

        response.setContentType( "application/json" );
        jsonArray.write( response.getWriter() );
        return null;
    }

    @RequestMapping("/department/{dept}/survey/current")
    public String current( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "surveys", surveyDao.getOpenSurveys( department ) );
        return "survey/current";
    }

    @RequestMapping("/department/{dept}/survey/list")
    public String list( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "department", department );
        models.put( "openSurveys", surveyDao.getOpenSurveys( department ) );
        models.put( "unpublishedSurveys",
            surveyDao.getUnpublishedSurveys( department ) );
        return "survey/list";
    }

    @RequestMapping("/department/{dept}/survey/closed")
    public String closed( @PathVariable String dept, ModelMap models )
    {
        Department department = departmentDao.getDepartment( dept );
        models.put( "closedSurveys", surveyDao.getClosedSurveys( department ) );
        return "survey/closed";
    }

    @RequestMapping("/department/{dept}/survey/search")
    public String search( @PathVariable String dept, @RequestParam String term,
        HttpSession session )
    {
        Department department = departmentDao.getDepartment( dept );
        List<Survey> surveys = surveyDao.searchSurveys( department, term, 20 );
        session.setAttribute( "surveySearchTerm", term );
        session.setAttribute( "surveySearchResults", surveys );
        return "redirect:/department/" + dept + "/survey/list#search";
    }

    @RequestMapping("/department/{dept}/survey/view")
    public String view( @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( id ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "survey/view";
    }

    @RequestMapping("/department/{dept}/survey/editQuestionSheet")
    public String editQuestions( @RequestParam Long surveyId,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( surveyId ) );
        models.put( "sectionIndex", sectionIndex != null ? sectionIndex : 0 );
        return "survey/editQuestionSheet";
    }

    @RequestMapping("/department/{dept}/survey/clone")
    public String clone( @PathVariable String dept, @RequestParam Long id )
    {
        Survey oldSurvey = surveyDao.getSurvey( id );
        Survey newSurvey = oldSurvey.clone();
        newSurvey.setDepartment( departmentDao.getDepartment( dept ) );
        newSurvey.setAuthor( SecurityUtils.getUser() );
        newSurvey.setDate( new Date() );
        newSurvey = surveyDao.saveSurvey( newSurvey );

        logger.info( SecurityUtils.getUser().getUsername() + " cloned survey "
            + newSurvey.getId() + " from " + oldSurvey.getId() );

        return "redirect:edit?id=" + newSurvey.getId();
    }

    @RequestMapping("/department/{dept}/survey/deleteSection")
    public String deleteSection( @RequestParam Long surveyId,
        @RequestParam int sectionIndex )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            survey.getQuestionSheet().getSections().remove( sectionIndex );
            surveyDao.saveSurvey( survey );

            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted section " + sectionIndex + " from survey "
                + surveyId );
        }

        return "redirect:editQuestionSheet?surveyId=" + surveyId;
    }

    @RequestMapping("/department/{dept}/survey/reorderQuestion")
    public String reorderQuestion( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, @RequestParam Long questionId,
        @RequestParam int newIndex, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            QuestionSection questionSection = survey.getQuestionSheet()
                .getSections()
                .get( sectionIndex );
            Question question = questionSection.removeQuestion( questionId );
            if( question != null )
            {
                questionSection.getQuestions().add( newIndex, question );
                surveyDao.saveSurvey( survey );
            }

            logger.info( SecurityUtils.getUser().getUsername()
                + " reordered question " + questionId + " in survey "
                + surveyId + " to index " + newIndex );
        }

        response.setContentType( "text/plain" );
        response.getWriter().print( "" );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/deleteQuestion")
    public String deleteQuestion( @RequestParam Long surveyId,
        @RequestParam int sectionIndex, @RequestParam Long questionId )
    {
        Survey survey = surveyDao.getSurvey( surveyId );
        if( !survey.isPublished() )
        {
            survey.getQuestionSheet()
                .getSections()
                .get( sectionIndex )
                .removeQuestion( questionId );
            surveyDao.saveSurvey( survey );

            logger.info( SecurityUtils.getUser().getUsername()
                + " deleted question " + questionId + " in survey " + surveyId );
        }

        return "redirect:editQuestionSheet?surveyId=" + surveyId
            + "&sectionIndex=" + sectionIndex;
    }

    @RequestMapping("/department/{dept}/survey/remove")
    public String remove( @RequestParam Long id )
    {
        Survey survey = surveyDao.getSurvey( id );
        survey.setDeleted( true );
        surveyDao.saveSurvey( survey );

        logger.info( SecurityUtils.getUser().getUsername() + " removed survey "
            + id );

        return "redirect:list";
    }

    @RequestMapping("/department/{dept}/survey/publish")
    public String publish( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( id );
        if( !survey.isPublished() )
        {
            survey.setPublishDate( Calendar.getInstance() );
            survey = surveyDao.saveSurvey( survey );
            logger.info( SecurityUtils.getUser().getUsername()
                + " published survey " + id );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        response.setContentType( "text/plain" );
        response.getWriter().print(
            dateFormat.format( survey.getPublishDate().getTime() ) );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/close")
    public String close( @RequestParam Long id, HttpServletResponse response )
        throws IOException
    {
        Survey survey = surveyDao.getSurvey( id );
        if( !survey.isClosed() )
        {
            survey.setCloseDate( Calendar.getInstance() );
            survey = surveyDao.saveSurvey( survey );
            logger.info( SecurityUtils.getUser().getUsername()
                + " closed survey " + id );
        }

        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        response.setContentType( "text/plain" );
        response.getWriter().print(
            dateFormat.format( survey.getCloseDate().getTime() ) );

        return null;
    }

    @RequestMapping("/department/{dept}/survey/results")
    @PreAuthorize("principal.isFaculty(#dept)")
    public String results( @PathVariable String dept, @RequestParam Long id,
        @RequestParam(required = false) Integer sectionIndex, ModelMap models )
    {
        models.put( "survey", surveyDao.getSurvey( id ) );
        models.put( "sectionIndex", sectionIndex == null ? 0 : sectionIndex );
        return "survey/results";
    }

    @RequestMapping(value = "/department/{dept}/survey/results",
        params = "export=excel")
    public String exportExcel( @RequestParam Long id,
        HttpServletResponse response ) throws IOException
    {
        Survey survey = surveyDao.getSurvey( id );
        Workbook wb = new XSSFWorkbook();

        for( int i = 0; i < survey.getQuestionSheet().getNumOfSections(); ++i )
            exportExcel( survey, i, wb );

        response.setContentType( contentTypes.getProperty( "xlsx" ) );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + survey.getName().replaceAll( " ", "_" ) + "_Results.xlsx" );

        wb.write( response.getOutputStream() );

        logger.info( SecurityUtils.getUser().getUsername()
            + " exported the results of survey " + survey.getId() );

        return null;
    }

    private void exportExcel( Survey survey, int sectionIndex, Workbook wb )
    {
        Sheet sheet = wb.createSheet( "Section " + (sectionIndex + 1) );
        int numOfResponses = survey.getNumOfResponses();

        // The first column is the timestamp of the response.
        DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        Row row = sheet.createRow( 0 );
        row.createCell( 0 ).setCellValue( "Date" );
        for( int i = 0; i < numOfResponses; ++i )
            sheet.createRow( 1 + i )
                .createCell( 0 )
                .setCellValue(
                    dateFormat.format( survey.getResponses()
                        .get( i )
                        .getAnswerSheet()
                        .getDate() ) );

        // If the survey is NAMED, the next two columns are the CIN and name
        // of the respondent.
        int startColumnIndex = 1;
        if( survey.getType() == SurveyType.NAMED )
        {
            row = sheet.getRow( 0 );
            row.createCell( 1 ).setCellValue( "CIN" );
            row.createCell( 2 ).setCellValue( "Name" );
            for( int i = 0; i < numOfResponses; ++i )
            {
                User respondent = survey.getResponses()
                    .get( i )
                    .getAnswerSheet()
                    .getAuthor();
                row = sheet.getRow( 1 + i );
                row.createCell( 1 ).setCellValue( respondent.getCin() );
                row.createCell( 2 )
                    .setCellValue(
                        respondent.getLastName() + ", "
                            + respondent.getFirstName() );
            }
            startColumnIndex = 3;
        }

        int numOfQuestions = survey.getQuestionSheet()
            .getSections()
            .get( sectionIndex )
            .getQuestions()
            .size();
        for( int i = 0; i < numOfQuestions; ++i )
        {
            sheet.getRow( 0 )
                .createCell( startColumnIndex + i )
                .setCellValue( "Q" + (i + 1) );
            for( int j = 0; j < numOfResponses; ++j )
                sheet.getRow( 1 + j )
                    .createCell( startColumnIndex + i )
                    .setCellValue(
                        survey.getResponses()
                            .get( j )
                            .getAnswerSheet()
                            .getSections()
                            .get( sectionIndex )
                            .getAnswers()
                            .get( i )
                            .toString() );
        }
    }

}
