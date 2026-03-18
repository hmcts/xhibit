package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.List;



import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;



public class CheckForValidPilotCourtbyCourtId extends QueryOperation{

private static final Logger log = CSServices.getLogger(CheckForValidPilotCourtbyCourtId.class);
    
    private static final String SELECT_IS_PILOT_BY_COURTID_SQL = "SELECT IS_PILOT FROM XHB_COURT WHERE COURT_ID = ";
    
    public CheckForValidPilotCourtbyCourtId(int courtId) {

        super(CSServices.getServiceLocator().getDataSource(), SELECT_IS_PILOT_BY_COURTID_SQL  + Integer.valueOf(courtId));
        
        log.debug("Query object created");
    }
    
    public XhbCourtPilotBasicValue[] getData() {
    	XhbPilotCourtRowProcessor rp = new XhbPilotCourtRowProcessor();
        setRowProcessor(rp);
        execute(new Object[] {});
        List results = rp.getResults();
        return (XhbCourtPilotBasicValue[]) results.toArray(new XhbCourtPilotBasicValue[results.size()]);
    }

    class XhbPilotCourtRowProcessor extends ReflectionRowProcessor {
        public XhbPilotCourtRowProcessor() {
            super(XhbCourtPilotBasicValue.class);
            registerDefaultBindings();
        }
    }   
   
    
}
