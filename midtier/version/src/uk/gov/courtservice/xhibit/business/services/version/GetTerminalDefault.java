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
 * Description: Get 1 row from the table XHB_TERMINAL_DEFAULT based on terminal id
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: GetTerminalDefault.java,v 1.2 2013/11/13 17:49:39 hingstb Exp $
 */

public class GetTerminalDefault extends QueryOperation {

    private static final Logger log = CSServices.getLogger(GetTerminalDefault.class);

    /**
     * The query: SELECT * FROM XHB_TERMINAL_DEFAULT WHERE TERMINAL_NAME=
     */
    private static final String SELECTEDTERMINALDEFAULTSQL = "SELECT * FROM XHB_TERMINAL_DEFAULT WHERE TERMINAL_NAME='";
    

    public GetTerminalDefault(String terminalId) {
        super(CSServices.getServiceLocator().getDataSource(), SELECTEDTERMINALDEFAULTSQL + terminalId + "'");
        
        log.debug("Query object created");
    }
    
    
    /*
     * 
     */
    public XhbTerminalDefaultBasicValue[] getData() {
        XhbTerminalDefaultRowProcessor rp = new XhbTerminalDefaultRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        // Should only be one entry returned, however that may not be the case and will have to be handled further up the calling thread
        return ((XhbTerminalDefaultBasicValue[]) results.toArray(new XhbTerminalDefaultBasicValue[results.size()]));
    }

    class XhbTerminalDefaultRowProcessor extends ReflectionRowProcessor {
        public XhbTerminalDefaultRowProcessor() {
            super(XhbTerminalDefaultBasicValue.class);
            registerDefaultBindings();
        }
    }
}
