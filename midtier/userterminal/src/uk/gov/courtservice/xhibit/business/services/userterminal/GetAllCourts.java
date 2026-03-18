package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
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
 * @version $Id: GetAllCourts.java,v 1.1 2015/10/06 15:58:56 atwells Exp $
 */

public class GetAllCourts extends QueryOperation {

    private static final Logger log = CSServices.getLogger(GetAllCourts.class);

    /**
     * The query: SELECT * FROM XHB_TERMINAL_DEFAULT WHERE TERMINAL_ID=
     */
    private static final String GETALLCOURTSSQL = "SELECT * FROM XHB_COURT";
    

    public GetAllCourts() {
        super(CSServices.getServiceLocator().getDataSource(), GETALLCOURTSSQL);
        
        log.debug("Query object created");
    }
    
    
    /*
     * 
     */
    public XhbCourtBasicValue[] getData() {
        XhbCourtRowProcessor rp = new XhbCourtRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        return (XhbCourtBasicValue[]) results.toArray(new XhbCourtBasicValue[results.size()]);
    }

    class XhbCourtRowProcessor extends ReflectionRowProcessor {
        public XhbCourtRowProcessor() {
            super(XhbCourtBasicValue.class);
            registerDefaultBindings();
        }
    }
}
