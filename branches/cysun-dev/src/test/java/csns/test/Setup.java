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
package csns.test;

import java.util.Scanner;

import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class Setup extends AbstractTransactionalTestNGSpringContextTests {

    /**
     * This method is to ensure that the application context is prepared before
     * the BeforeSuite method init() is called; otherwise the application
     * context is prepared in a BeforeClass method and executeSqlScript() in
     * BeforeSuite methods will cause a NullPointerException.
     */
    @Override
    @BeforeSuite
    protected void springTestContextPrepareTestInstance() throws Exception
    {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeSuite(alwaysRun = true,
        dependsOnMethods = { "springTestContextPrepareTestInstance" })
    public void init()
    {
        executeSqlScript( "classpath:csns-create.sql" );
        executeSqlScript( "classpath:csns-test-insert.sql", false );
    }

    @AfterSuite(alwaysRun = true)
    public void cleanup()
    {
        executeSqlScript( "classpath:csns-drop.sql", false );
    }

    /**
     * Spring's executeSqlScript() splits the script into statements and
     * executes each statement individually. The problem is that the split is
     * based on simple delimiters like semicolon and it does not recognize the
     * syntax of create function/procedure. So in order to run csns-create.sql,
     * we have to read the file into a string and pass the whole thing to the
     * JDBC driver.
     */
    @SuppressWarnings("deprecation")
    private void executeSqlScript( String path )
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            Resource resource = applicationContext.getResource( path );
            Scanner in = new Scanner( resource.getFile() );
            while( in.hasNextLine() )
            {
                sb.append( in.nextLine() );
                sb.append( "\n" );
            }
            in.close();
            simpleJdbcTemplate.update( sb.toString() );
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

}
