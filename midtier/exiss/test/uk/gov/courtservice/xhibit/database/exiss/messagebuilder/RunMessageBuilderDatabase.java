package uk.gov.courtservice.xhibit.database.exiss.messagebuilder;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;

import uk.gov.courtservice.framework.testutils.StandAloneDataSource;

/**
 * <p>
 * Title: RunMessageBuilderDatabase
 * </p>
 * <p>
 * Description: Application for testing database, not a test as relies on
 * database. Assumes there are records.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jeremy Shields, Will Fardell
 * @version $id:$
 */
public class RunMessageBuilderDatabase {
    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();

        // Initialise Database Properties
        System.setProperty("database.driver", "oracle.jdbc.xa.client.OracleXADataSource");
        System.setProperty("database.url", "jdbc:oracle:thin:@130.177.1.35:14152:CSDBDEV3");
        System.setProperty("database.user", "cjit");
        System.setProperty("database.password", "cjit");
    }

    public static void main(String[] args) {
        // Initialise database, override to create standalone datasource
        MessageBuilderDatabase database = new MessageBuilderDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };

        // Test Get Properties
        database.getProperties(1);
        
        // Test Get Clob
        database.getClobData(1);

    }
}
