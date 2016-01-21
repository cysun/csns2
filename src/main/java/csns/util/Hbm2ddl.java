/*
 * This file is part of the CSNetwork Services (CSNS) project.
 * 
 * Copyright 2012-2016 Chengyu Sun (csun@calstatela.edu).
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

import java.util.Map;

import javax.persistence.Persistence;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class Hbm2ddl {

    public static void main( String args[] )
    {
        if( args.length == 0 )
        {
            System.err.println( "java Hbm2ddl <outputFile>" );
            return;
        }

        System.out.print( "Export DDL to " + args[0] + " ... " );

        EntityManagerFactoryImpl entityManagerFactory = (EntityManagerFactoryImpl) Persistence
            .createEntityManagerFactory( "csns2" );
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) entityManagerFactory
            .getSessionFactory();
        MetadataSources metadataSources = new MetadataSources(
            new StandardServiceRegistryBuilder()
                .applySetting( "hibernate.dialect",
                    sessionFactory.getDialect().toString() )
                .build() );
        Map<String, ClassMetadata> allClassMetadata = sessionFactory
            .getAllClassMetadata();
        for( String className : allClassMetadata.keySet() )
            metadataSources.addAnnotatedClassName( className );

        SchemaExport schemaExport = new SchemaExport(
            (MetadataImplementor) metadataSources.buildMetadata() );
        schemaExport.setOutputFile( args[0] )
            .setDelimiter( ";" )
            .setFormat( true )
            .setHaltOnError( true );

        // . output script to console (and file if outputFile is set): true
        // . export to database: false
        // . only drop the tables: false
        // . only create the tables: true
        schemaExport.execute( true, false, false, true );

        System.out.println( "Done." );
    }

}
