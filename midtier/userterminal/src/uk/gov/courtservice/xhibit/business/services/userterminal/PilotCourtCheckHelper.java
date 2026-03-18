package uk.gov.courtservice.xhibit.business.services.userterminal;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;

public class PilotCourtCheckHelper {
	
	protected final static Logger log = Logger.getLogger(PilotCourtCheckHelper.class);
	
	private static final String PILOT_ENABLED ="PILOT_ENABLED";
    private static final String YES = "Y";
    private static final String TRUE="true";
	
	 public static void checkForValidPilotCourt(Integer courtId) throws PilotApplicationLoginException {    	 
        String[] isPilotArray = CheckForValidPilotCourtbyCourtId(courtId.intValue());
        String pilotDBValue= isPilotArray[0];
        String pilotWebLogicValue = isPilotArray[1];       
        if ((YES.equals(pilotDBValue) && !TRUE.equals(pilotWebLogicValue)) || (!YES.equals(pilotDBValue) && TRUE.equals(pilotWebLogicValue))) {           
            
            throw new PilotApplicationLoginException();
        }
    }
    
    
    
    public static String[] CheckForValidPilotCourtbyCourtId(int courtId){
        log.debug("Check for pilot court by Court Id : "+courtId);
        CheckForValidPilotCourtbyCourtId checkForValidPilotCourtbyCourtId = new CheckForValidPilotCourtbyCourtId(courtId);
       
        
        XhbCourtPilotBasicValue[] XhbCourtPilotBasicValueArray = checkForValidPilotCourtbyCourtId.getData();
        XhbCourtPilotBasicValue xhbCourtPilotBasicValue = XhbCourtPilotBasicValueArray[0];
        String isPilotDB = xhbCourtPilotBasicValue.getIsPilot();
        String isPilotWeblogicServer = System.getProperty(PILOT_ENABLED);
      
        
        return new String[] {isPilotDB,isPilotWeblogicServer};
    }  
    
    public static XhbTerminalBasicValue[] getTerminalByName(String terminalId){
        log.debug("Finding terminal ID: "+terminalId);
        GetTerminal gt = new GetTerminal(terminalId);
        return gt.getData();
    }
}
