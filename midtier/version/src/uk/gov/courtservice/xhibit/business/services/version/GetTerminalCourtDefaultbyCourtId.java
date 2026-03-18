package uk.gov.courtservice.xhibit.business.services.version;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;

/**
 * 
 * @author atwells
 *
 */

public class GetTerminalCourtDefaultbyCourtId extends QueryOperation {

    private static final Logger log = CSServices.getLogger(GetTerminalCourtDefaultbyCourtId.class);
    
    private static final String SELECTEDTERMINALCOURTDEFAULTBYTERMINALIDANDCOURTIDSQL = "SELECT * FROM XHB_TERMINAL_COURT_DEFAULT WHERE COURT_ID=";

    
    public GetTerminalCourtDefaultbyCourtId(int courtId) {

        super(CSServices.getServiceLocator().getDataSource(), SELECTEDTERMINALCOURTDEFAULTBYTERMINALIDANDCOURTIDSQL  + Integer.valueOf(courtId));
        
        log.debug("Query object created");
    }
    
    public XhbTerminalCourtDefaultBasicValue[] getData() {
        XhbTerminalCourtDefaultRowProcessor rp = new XhbTerminalCourtDefaultRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        return (XhbTerminalCourtDefaultBasicValue[]) results.toArray(new XhbTerminalCourtDefaultBasicValue[results.size()]);
    }

    class XhbTerminalCourtDefaultRowProcessor extends ReflectionRowProcessor {
        public XhbTerminalCourtDefaultRowProcessor() {
            super(XhbTerminalCourtDefaultBasicValue.class);
            registerDefaultBindings();
        }
    }
}
