package uk.gov.courtservice.xhibit.business.admin.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;

import org.apache.log4j.Logger;


public class CaseResyncHelper {	
	
	private int courtId;
	private XhbCourtBasicValue[] courts;
    private String returnMessage;
    protected final Logger log = CSServices.getLogger(getClass());
    private UserTerminalControllerBeanBusinessDelegate crac = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();

    /**
     * Instance method. If the model has blank data then sets up data, else performs a case resync or checks the status of the case dependent on the action of the user.
     * 
     * @param courtIdString
     * @param caseType
     * @param caseNumber
     */
	public CaseResyncHelper(String courtIdString, String caseType, String caseNumber, String whichButton){

		if(courtIdString != null && !courtIdString.equals("")){
			//Court ID has been supplied
			courtId = Integer.parseInt(courtIdString);
            
            // If caseNumber is not null then a valid case number has been passed through (i.e. 8 digits)
            if ((caseNumber != null) && (caseType != null)) {
                // Therefore are we syncing this case or checking its status
                boolean resyncing = true;
                if ((whichButton != null) && (whichButton.equals("CheckStatus"))) {
                    resyncing = false;
                }

                //Check to see if we're resyncing
                if (resyncing) {
                    log.debug("Resyncing case type:" + caseType + ", case number: " + caseNumber + " for court id = " + courtIdString);
                    
                    HashMap<String, String> doAuth = crac.doResync(courtIdString, caseType, caseNumber);
                    if ((doAuth != null) && (doAuth.get("returnMessage") != null)) {
                        if (doAuth.get("returnMessage").equals("Success")) {
                            returnMessage = "The case has been sent to for synchronisation.";
                        } else {
                            returnMessage = doAuth.get("returnMessage").toString();
                        }
                    } else {
                        returnMessage = "There was an error synchronising the case. Please check the logs.";
                    }

                } else { // Check the status if the case
                    log.debug("Checking status of case: type:" + caseType + ", case number: " + caseNumber + " for court id = " + courtIdString);
                    HashMap<String, String> x = crac.getCaseStatus(courtIdString, caseType, caseNumber);
                    if ((x != null) && (x.get("caseStatus") != null)) {
                        String caseStatus = x.get("caseStatus").toString();
                        // What is the status we've had returned?
                        if (caseStatus.equals("S")) {
                            returnMessage = "Case is still awaiting to be synchronised";
                        } else if (caseStatus.equals("P")) {
                            returnMessage = "Case synchronisation in progress";
                        } else if (caseStatus.equals("RF")) {
                            returnMessage = "Case has failed to synchronise";
                        } else if (caseStatus.equals("O")) {
                            returnMessage = "Case has been successfully synchronised";
                        } else if (caseStatus.equals("C")) {
                            returnMessage = "Case has been loaded into XHIBIT but not yet opened";
                        } else if (caseStatus.equals("LF")) {
                            returnMessage = "An initial case load from CREST has been attempted but the case is failing to synchronise";
                        } else if ((caseStatus.equals("R")) || (caseStatus.equals("IP")) || (caseStatus.equals("N"))) {
                            returnMessage = "The case has never been successfully loaded and is in the process of loading for the first time";
                        } else {
                            returnMessage = "Case status = " + caseStatus + "; Unknown!";
                        }
                    } else if ((x != null) && (x.get("returnMessage") != null)) {
                        returnMessage = x.get("returnMessage");                    
                    } else {
                        returnMessage = "There was an error getting the status of this case. Please check the logs.";
                    }
                }
            }
		}
		
		//Initialise collections for use by Combo Boxes
		initialiseCourt(courtIdString);
	}
	
	/**
	 * If given a courtId, assign to instance variable and 
	 * retrieve court list from Midtier
	 * @param courtIdString
	 */
	private void initialiseCourt(String courtIdString){
        UserTerminalControllerBeanBusinessDelegate del = 
            UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance(); 
        courts = del.getCourtSelectionList();
        if(courtIdString != null && !courtIdString.equals("")){
			courtId = Integer.parseInt(courtIdString);
		}
        if (courts != null) {
            Arrays.sort(courts, new XhbCourtComparator());
        }
	}
	
	/**
	 * Return the Court ID
	 * @return
	 */
	public int getCourtId() {
		return courtId;
	}

	
	public XhbCourtBasicValue[] getCourts() {
		return courts;
	}
    
    public String getReturnMessage() {
        return returnMessage;
    }
	
	/**
     * Comparator class used to order courts by court name
     *  
     * @author atwells
     *
	 */
    private class XhbCourtComparator implements Comparator {
        public int compare(Object obj1, Object obj2) {
            XhbCourtBasicValue court1 = (XhbCourtBasicValue) obj1;
            XhbCourtBasicValue court2 = (XhbCourtBasicValue) obj2;

            String courtName1 = court1.getCourtName();
            String courtName2 = court2.getCourtName();

            return courtName1.compareTo(courtName2);
        }
    }
}
