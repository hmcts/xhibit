package uk.gov.courtservice.xhibit.database.gdgateway;

import java.util.Date;

import javax.sql.DataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.InboundGdgateDatabase;

/**
 * <p>
 * Title: RunInboundGdgateDatabase
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
 * @author Colette Surtees
 * @version $Id: RunInboundGdgateDatabase.java,v 1.4 2006/09/04 14:56:15 jzj6wd Exp $
 */

public class RunInboundGdgateDatabase {
    
    /** The log4j <code>Logger</code> instance */
    protected static final Logger log = CSServices.getLogger(RunInboundGdgateDatabase.class);
    
    InboundGdgateDatabase database;
    
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
        RunInboundGdgateDatabase db = new RunInboundGdgateDatabase();
        
        final Long id = db.insertInboundMessage();
        
        db.getInboundMessage(id);
        
        log.info("RunInboundGdgateDatabase - tests OK!");
    }

    public RunInboundGdgateDatabase() {
        // Initialise database, override to create standalone datasource
        database = new InboundGdgateDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };
    }
        
    private Long insertInboundMessage()
    {
        // Test insert inbound messqage
        // NOTE - the RequestIdentifier and SourceIdentifier combination must be unique.
        final InboundMessageVO inboundMessageVO = new InboundMessageVO();
        inboundMessageVO.setRequestIdentifier("reqestIdentifier6002");
        inboundMessageVO.setSourceIdentifier("Z00CJSE");
        inboundMessageVO.setDestinationIdentifer("C00CourtServiceHub");
        inboundMessageVO.setExecMode("ASYNCH");
        inboundMessageVO.setRequestTimeStamp(new Date());
        inboundMessageVO.setClobData("clob");
        
        Long id = database.insertInboundMessages(inboundMessageVO);        
        log.info("insertInboundMessages success. id = " + id);
        
        return id;
    }
    
    private void getInboundMessage(final Long id)
    {
        InboundMessageVO inboundMessage = database.getMessageByMessageId(id);
        log.info("getMessageByMessageId success. Retreived Inbound Clob info: " + inboundMessage.toString());
    }
    
    
}
