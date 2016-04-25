/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016, Mahdiye Jamali (mjamali@calstatela.edu).
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
package csns.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {

    private Workbook workbook;

    private Sheet sheet;

    private int rows, cols;

    private int currentRow = 0;

    private DataFormatter dataFormatter;

    private static final Logger logger = LoggerFactory
        .getLogger( ExcelReader.class );

    public ExcelReader( InputStream input )
    {
        try
        {
            workbook = WorkbookFactory.create( input );
            sheet = workbook.getSheetAt( 0 );
            rows = sheet.getPhysicalNumberOfRows();
            cols = sheet.getRow( 0 ).getPhysicalNumberOfCells();
            dataFormatter = new DataFormatter();
        }
        catch( Exception e )
        {
            rows = 0;
            cols = 0;
            logger.error( "Failed to read Excel file", e );
        }
    }

    public void close()
    {
        try
        {
            workbook.close();
        }
        catch( IOException e )
        {
            logger.error( "Failed to close Excel file", e );
        }
    }

    public boolean hasNextRow()
    {
        return currentRow < rows;
    }

    public String[] nextRow()
    {
        String values[] = new String[cols];
        for( int i = 0; i < cols; ++i )
            values[i] = dataFormatter
                .formatCellValue( sheet.getRow( currentRow ).getCell( i ) );

        ++currentRow;
        return values;
    }

}
