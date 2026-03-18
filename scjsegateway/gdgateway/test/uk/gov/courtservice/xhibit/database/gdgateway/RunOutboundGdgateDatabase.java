package uk.gov.courtservice.xhibit.database.gdgateway;

import java.util.Date;

import javax.sql.DataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.OutboundGdGateDatabase;

/**
 * <p>
 * Title: RunOutboundGdgateDatabase
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
 * @version $Id: RunOutboundGdgateDatabase.java,v 1.1 2006/09/29 09:19:47 rzvddy Exp $
 */

public class RunOutboundGdgateDatabase {
    
    /** The log4j <code>Logger</code> instance */
    protected static final Logger log = CSServices.getLogger(RunOutboundGdgateDatabase.class);
    
    OutboundGdGateDatabase database;
    
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
        RunOutboundGdgateDatabase db = new RunOutboundGdgateDatabase();
        
        final Long id = db.insertOutboundMessage();
        
        db.getOutboundMessage(id);
        
        log.info("RunOutboundGdgateDatabase - tests OK!");
    }

    public RunOutboundGdgateDatabase() {
        // Initialise database, override to create standalone datasource
        database = new OutboundGdGateDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };
    }
        
    private Long insertOutboundMessage()
    {
        // Test insert outbound messqage
        final OutboundMessageVO outboundMessageVO = new OutboundMessageVO();
        outboundMessageVO.setRequestId(new Long(1994));
        outboundMessageVO.setSourceIdentifier("C00CourtServiceHub");
        outboundMessageVO.setDestinationIdentifier("Z00CJSE");
        outboundMessageVO.setExecMode("ASYNCH");
        outboundMessageVO.setRequestTimestamp(new Date());
        outboundMessageVO.setClobData("clob_1994");
        outboundMessageVO.setSendAttempts(new Long(1));
        
        log.info("insertOutboundMessages about to send: " + outboundMessageVO);

        Long id = database.insertOutboundMessage(outboundMessageVO);        
        log.info("insertOutboundMessages success. id = " + id);
        
        return id;
    }
    
    private void getOutboundMessage(final Long id)
    {
        OutboundMessageVO outboundMessage = database.getMessageByRequestId(id);
        log.info("getMessageByRequestId success. Retreived Outbound Clob info: " + outboundMessage.toString());
    }
    
    
}
