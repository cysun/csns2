/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2016-2017, Chengyu Sun (csun@calstatela.edu).
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    private int current;

    private String[] currentRow;

    Map<String, Integer> colIndexes;

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

            // First row should be the header row
            current = -1;
            next();
            logger.debug( Arrays.toString( currentRow ) );

            colIndexes = new HashMap<String, Integer>();
            for( int i = 0; i < currentRow.length; ++i )
                colIndexes.put( currentRow[i].toUpperCase().trim(), i );
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

    public boolean hasNext()
    {
        return current + 1 < rows;
    }

    public boolean next()
    {
        if( ++current >= rows ) return false;

        currentRow = new String[cols];
        for( int i = 0; i < cols; ++i )
            currentRow[i] = dataFormatter
                .formatCellValue( sheet.getRow( current ).getCell( i ) ).trim();

        return true;
    }

    public String get( int colIndex )
    {
        return currentRow[colIndex];
    }

    public String get( String colName )
    {
        return currentRow[colIndexes.get( colName.toUpperCase() )];
    }

    public String[] getRow()
    {
        return currentRow;
    }

    public boolean hasColumn( String colName )
    {
        return colIndexes.keySet().contains( colName.toUpperCase() );
    }

}
