package uk.gov.courtservice.xhibit.business.services.version;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Get 1 row from the table XHB_TERMINAL based on terminal id
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: GetTerminal.java,v 1.1 2013/11/13 17:48:21 hingstb Exp $
 */

public class GetTerminal extends QueryOperation {

    private static final Logger log = CSServices.getLogger(GetTerminal.class);

    /**
     * The query: SELECT * FROM XHB_TERMINAL WHERE TERMINAL_NAME=
     */
    private static final String SELECTEDTERMINALSQL = "SELECT * FROM XHB_TERMINAL WHERE TERMINAL_NAME='";
    

    public GetTerminal(String terminalId) {
        super(CSServices.getServiceLocator().getDataSource(), SELECTEDTERMINALSQL + terminalId + "'");
        
        log.debug("Query object created");
    }
    
    
    /*
     * 
     */
    public XhbTerminalBasicValue[] getData() {
        XhbTerminalRowProcessor rp = new XhbTerminalRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        // Should only be one entry returned, however that may not be the case and will have to be handled further up the calling thread
        return ((XhbTerminalBasicValue[]) results.toArray(new XhbTerminalBasicValue[results.size()]));
    }

    class XhbTerminalRowProcessor extends ReflectionRowProcessor {
        public XhbTerminalRowProcessor() {
            super(XhbTerminalBasicValue.class);
            registerDefaultBindings();
        }
    }
}
