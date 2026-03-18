package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: UpdateTempTerminal.java,v 1.1 2015/10/06 15:58:56 atwells Exp $
 */

public class UpdateTempTerminal extends QueryOperation {

    private static final Logger log = CSServices.getLogger(UpdateTempTerminal.class);

    /**
     * The query: SELECT * FROM XHB_TERMINAL ORDER BY LOCATION
     */
    private static final String ALLCOURTSQL = "SELECT * FROM XHB_TERMINAL ORDER BY LOCATION";
    
    
    private static final String SELECTEDCOURTSSQL = "SELECT * FROM XHB_TERMINAL WHERE COURT_ID=";
    private static final String SELECTEDCOURTSSQLORDER = " ORDER BY LOCATION ASC";
    

    /**0
     *  Constructor compiles the query
     */
    public UpdateTempTerminal() {
        super(CSServices.getServiceLocator().getDataSource(), ALLCOURTSQL);
        log.debug("Query object created");
    }
    
    public UpdateTempTerminal(String courtId) {
        super(CSServices.getServiceLocator().getDataSource(), SELECTEDCOURTSSQL + Integer.valueOf(courtId) + SELECTEDCOURTSSQLORDER);
        
        log.debug("Query object created");
    }
    
    /**
     * Returns an array of CourtListValue.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * 
     * @return Suumary by name data for the specified court rooms
     */
    
    
    public XhbTerminalBasicValue[] getData() {
        XhbTerminalRowProcessor rp = new XhbTerminalRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        return (XhbTerminalBasicValue[]) results.toArray(new XhbTerminalBasicValue[results.size()]);
    }

    class XhbTerminalRowProcessor extends ReflectionRowProcessor {
        public XhbTerminalRowProcessor() {
            super(XhbTerminalBasicValue.class);
            registerDefaultBindings();
        }
    }
}
