package uk.gov.courtservice.xhibit.business.admin.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;

public class UnauthorisedCaseAdminHelper {	
	
	private int courtId;
	private XhbCourtBasicValue[] courts;
    private int defendantId;
    private String defendantName;
    private ArrayList defendants;
    private String returnMessage;
    protected final Logger log = CSServices.getLogger(getClass());
    private UserTerminalControllerBeanBusinessDelegate crac = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();

    /**
     * This constructor is called when the unauthorisedCasesAdministration.jsp is invoked.
     * 
     * @param courtIdString
     * @param caseType
     * @param caseNumber
     * @param defendantIdString
     * @param whichButton
     */
	public UnauthorisedCaseAdminHelper(String courtIdString, String caseType, String caseNumber, String defendantIdString, String whichButton) {
     
        if(courtIdString != null && !courtIdString.equals("")){
            //Court ID has been supplied
            courtId = Integer.parseInt(courtIdString);
            
            // If caseNumber is not null then a valid case number has been passed through (i.e. 8 digits)
            if ((caseNumber != null) && (caseType != null)) {
                // Are we finding all defendants, are we setting an unauthorised case to authorised or are we checking the status?
                String action = "";
                if (whichButton != null) {
                    if (whichButton.equals("CheckAuthStatus")) {
                        action = "CheckAuthStatus";
                    } else if (whichButton.equals("FindDefendants")) {
                        action = "FindDefendants";
                    } else if (whichButton.equals("SetAsAuthorised")) {
                        action = "SetAsAuthorised";
                    }
                }
 
                // Which action?
                if (action.equals("FindDefendants")) {
                    findDefendants(courtIdString, caseType, caseNumber, defendantIdString);
                    
                } else if (action.equals("SetAsAuthorised")) {
                    log.debug("Setting as authorised for case: type:" + caseType + ", case number: " + caseNumber + " for court id = " + courtIdString + " and defendant id = " + defendantIdString);
                    if (defendantIdString.length() > 0) {
                        HashMap<String, String> doAuth = crac.setAsAuthorised(courtIdString, caseType, caseNumber, defendantIdString);
                        if ((doAuth != null) && (doAuth.get("returnMessage") != null)) {
                            if (doAuth.get("returnMessage").equals("Success")) {
                                returnMessage = "This has now been set to authorised and should not appear on the Unauthorised Case Status report.";                                
                            } else {
                                returnMessage = doAuth.get("returnMessage").toString();
                            }
                        }
                    }
                    
                    // Return the defendants to poulate the dropdown again
                    findDefendants(courtIdString, caseType, caseNumber, defendantIdString);

                } else if (action.equals("CheckAuthStatus")) {
                    log.debug("Check authStatus for case: type:" + caseType + ", case number: " + caseNumber + " for court id = " + courtIdString + " and defendant id = " + defendantIdString);
                    HashMap<String, String> x = crac.checkAuthStatus(courtIdString, caseType, caseNumber, defendantIdString);
                    if ((x != null) && (x.get("authStatus") != null)) {
                        String authStatus = x.get("authStatus").toString();
                        // What is the status we've had returned?
                        if (authStatus.equals("") || authStatus.equals("N")) {
                            returnMessage = "Authorisation status = to be exported";
                        } else if (authStatus.equals("R")) {
                            returnMessage = "Authorisation initiated by normal mechanism";
                        } else if (authStatus.equals("U")) {
                            returnMessage = "Authorisation in progress";
                        } else if (authStatus.equals("E")) {
                            returnMessage = "Case has been set to authorised";
                        } else {
                            returnMessage = "Authorisation status = " + authStatus + "; Unknown!";
                        }
                    } else if ((x != null) && (x.get("returnMessage") != null)) {
                        returnMessage = x.get("returnMessage").toString();
                    }
                    
                    // Return the defendants to poulate the dropdown again
                    findDefendants(courtIdString, caseType, caseNumber, defendantIdString);
                }
            }
        }           
		
		
		//Initialise collections for use by Combo Boxes
		initialiseCourt(courtIdString);
	}
    
    /**
     * Finds the defendants for a given case.
     * 
     * @param courtIdString
     * @param caseType
     * @param caseNumber
     * @param defendantIdString
     */
    private void findDefendants(String courtIdString, String caseType, String caseNumber, String defendantIdString) {
        log.debug("Find defendants for case: type:" + caseType + ", case number: " + caseNumber + " for court id = " + courtIdString);
        HashMap<String, Object> defendantList = new HashMap<String, Object>();
        defendantList = crac.findDefendants(courtIdString, caseType, caseNumber);
        if ((defendantList != null) && (defendantList.get("defendants") != null)) {
            defendants = (ArrayList) defendantList.get("defendants");
            
            // Loop through defendants and populate a list of defendant ids and names to populate
            // back to screen
            if ((defendants != null) && (defendants.size() == 0)) {
                returnMessage = "No defendants found for this case.";
            }
        } else if ((defendantList != null) && (defendantList.get("returnMessage") != null)) {
            returnMessage = defendantList.get("returnMessage").toString();
        } else {
            // No defendants
            returnMessage = "An unknown error occurred retrieving the defendants, please check the logs.";
        }
    }
	
	/**
	 * If given a courtId, assign to instance variable and 
	 * retrieve court list from Midtier
	 * @param courtIdString
	 */
	private void initialiseCourt(String courtIdString){
        
        courts = crac.getCourtSelectionList();
        if(courtIdString != null && !courtIdString.equals("")){
			courtId = Integer.parseInt(courtIdString);
		}
        if (courts != null) {
            Arrays.sort(courts, new XhbCourtComparator());
        }
	}
	
	/*
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
     * Return the Defendant ID
     * @return
     */
    public int getDefendantId() {
        return defendantId;
    }

    
    public ArrayList getDefendants() {
        return defendants;
    }
    
    public String getDefendantName(int defendantId) {
        // Get the name of the defendant for this id
        return defendantName;
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

