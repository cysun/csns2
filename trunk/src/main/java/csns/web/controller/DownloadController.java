/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2015, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csns.model.academics.Assignment;
import csns.model.academics.Section;
import csns.model.academics.Submission;
import csns.model.academics.dao.AssignmentDao;
import csns.model.academics.dao.SectionDao;
import csns.model.academics.dao.SubmissionDao;
import csns.model.core.File;
import csns.model.core.User;
import csns.model.core.dao.FileDao;
import csns.security.SecurityUtils;
import csns.util.FileIO;

@Controller
public class DownloadController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private AssignmentDao assignmentDao;

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private FileIO fileIO;

    @Resource(name = "contentTypes")
    private Properties contentTypes;

    private static Logger logger = LoggerFactory.getLogger( DownloadController.class );

    private Collection<File> removeDuplicates( Collection<File> files )
    {
        Map<String, File> fileMap = new HashMap<String, File>();
        for( File file : files )
        {
            File file2 = fileMap.get( file.getName() );
            if( file2 == null || file.getDate().after( file2.getDate() ) )
                fileMap.put( file.getName(), file );
        }
        return fileMap.values();
    }

    private long addToZip( ZipOutputStream zip, String dir,
        Collection<File> files ) throws IOException
    {
        files = removeDuplicates( files );

        long totalSize = 0;
        for( File file : files )
        {
            ZipEntry entry = new ZipEntry( dir + "/" + file.getName() );
            zip.putNextEntry( entry );
            fileIO.copy( file, zip );
            zip.closeEntry();
            totalSize += entry.getCompressedSize();
        }
        return totalSize;
    }

    @RequestMapping(value = "/download", params = "fileId")
    public String downloadFile( @RequestParam Long fileId,
        HttpServletResponse response, ModelMap models ) throws IOException
    {
        File file = fileDao.getFile( fileId );

        if( file.isDeleted() )
        {
            models.put( "message", "error.file.deleted" );
            return "error";
        }

        if( !fileIO.getDiskFile( file ).exists() )
        {
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return null;
        }

        String contentType = contentTypes.getProperty( file.getFileExtension()
            .toLowerCase() );
        if( contentType == null ) contentType = file.getType();

        response.setContentType( contentType );
        response.setHeader( "Content-Length", file.getSize().toString() );
        response.setHeader( "Content-Disposition", "inline; filename="
            + file.getName().replace( ' ', '_' ) );

        fileIO.copy( file, response.getOutputStream() );

        String username = SecurityUtils.isAnonymous() ? "guest"
            : SecurityUtils.getUser().getUsername();
        logger.info( username + " downloaded " + file.getId() );

        return null;
    }

    @RequestMapping(value = "/download", params = "submissionId")
    public String downloadSubmissionFiles( @RequestParam Long submissionId,
        HttpServletResponse response, ModelMap models ) throws IOException
    {
        Submission submission = submissionDao.getSubmission( submissionId );
        String name = submission.getAssignment().getAlias().replace( ' ', '_' );

        response.setContentType( "application/zip" );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + name + ".zip" );

        ZipOutputStream zip = new ZipOutputStream( response.getOutputStream() );
        addToZip( zip, name, submission.getFiles() );
        zip.close();
        return null;
    }

    @RequestMapping(value = "/download", params = "assignmentId")
    public String downloadAssignmentFiles( @RequestParam Long assignmentId,
        HttpServletResponse response, ModelMap models ) throws IOException
    {
        Assignment assignment = assignmentDao.getAssignment( assignmentId );
        String name = assignment.getAlias().replace( ' ', '_' );

        response.setContentType( "application/zip" );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + name + ".zip" );

        ZipOutputStream zip = new ZipOutputStream( response.getOutputStream() );
        for( Submission submission : assignment.getSubmissions() )
        {
            String dir = submission.getStudent().getLastName() + "."
                + submission.getStudent().getFirstName();
            addToZip( zip, dir, submission.getFiles() );
        }
        zip.close();
        return null;
    }

    @RequestMapping(value = "/download", params = "sectionId")
    public String downloadSectionFiles( @RequestParam Long sectionId,
        HttpServletResponse response, ModelMap models ) throws IOException
    {
        Section section = sectionDao.getSection( sectionId );
        String name = section.getCourse().getCode();
        response.setContentType( "application/zip" );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + name + ".zip" );

        User user = SecurityUtils.getUser();
        ZipOutputStream zip = new ZipOutputStream( response.getOutputStream() );
        for( Assignment assignment : section.getAssignments() )
        {
            if( assignment.isPastDue() && !assignment.isAvailableAfterDueDate() )
                continue;

            String dir = assignment.getAlias();
            Submission submission = submissionDao.getSubmission( user,
                assignment );
            if( submission != null )
                addToZip( zip, dir, submission.getFiles() );
        }
        zip.close();
        return null;
    }

    @RequestMapping(value = "/download", params = "folderId")
    public String downloadFolderFiles( @RequestParam Long folderId,
        HttpServletResponse response, ModelMap models ) throws IOException
    {
        File folder = fileDao.getFile( folderId );
        response.setContentType( "application/zip" );
        response.setHeader( "Content-Disposition", "attachment; filename="
            + folder.getName() + ".zip" );
        ZipOutputStream zip = new ZipOutputStream( response.getOutputStream() );
        addToZip( zip, folder.getName(), fileDao.listFiles( folder ) );
        zip.close();

        String username = SecurityUtils.isAnonymous() ? "guest"
            : SecurityUtils.getUser().getUsername();
        logger.info( username + " downloaded folder " + folderId );

        return null;
    }

}
