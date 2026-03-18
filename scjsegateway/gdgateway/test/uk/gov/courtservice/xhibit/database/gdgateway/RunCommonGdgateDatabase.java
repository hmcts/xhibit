package uk.gov.courtservice.xhibit.database.gdgateway;

import java.util.Collection;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;

/**
 * <p>
 * Title: RunCommonGdgateDatabase
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
 * @author Rob Sumner
 * @version $Id: RunCommonGdgateDatabase.java,v 1.2 2006/09/06 09:39:31 jzj6wd Exp $
 */
public class RunCommonGdgateDatabase {
    
    /** The log4j <code>Logger</code> instance */
    protected static final Logger log = CSServices.getLogger(RunCommonGdgateDatabase.class);
    
    CommonGdgateDatabase database;
    
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
        RunCommonGdgateDatabase db = new RunCommonGdgateDatabase();
        
        db.getConfigProperties();
        
        log.info("RunInboundGdgateDatabase - tests OK!");
    }

    public RunCommonGdgateDatabase() {
        // Initialise database, override to create standalone datasource
        database = new CommonGdgateDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };
    }
    
    private void getConfigProperties() {
        Collection configProperties = database.getProperties();
        log.info("getProperties success. Retreived Config Properties Collection of size: " + configProperties.size());
    }

}
