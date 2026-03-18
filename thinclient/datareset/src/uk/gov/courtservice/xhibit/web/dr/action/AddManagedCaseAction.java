package uk.gov.courtservice.xhibit.web.dr.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.datareset.DataResetControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.datareset.vo.ManagedCase;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application. <p/>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 */
public class AddManagedCaseAction extends AbstractAction {

    private static final Logger log = CSServices.getLogger(AddManagedCaseAction.class);

    /**
     * Empty default constructor
     */
    public AddManagedCaseAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
    	
        DataResetControllerBeanBusinessDelegate drc = DataResetControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        
        if (actionEnvironment.getRequestParameterNames().hasMoreElements()) {
        	
        	String error = "";
        	boolean allok = true;

        	// Do some validation first
        	String courtName = "";
        	String caseType = "";
        	Integer caseNumber = new Integer(0);
        	
        	boolean errorWithInputs = false;
        	// Court name, if populated is from a drop-down so will be valid
        	if (actionEnvironment.getRequestParameter("courtnameselected") != null) {
        		courtName = actionEnvironment.getRequestParameter("courtnameselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	
        	// Get new case details
        	if (actionEnvironment.getRequestParameter("casetypeselected") != null) {
        		caseType = actionEnvironment.getRequestParameter("casetypeselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	// Case number should be checked as all numeric in javascript but we'll check it here too...
        	if (actionEnvironment.getRequestParameter("casenumberselected") != null) {
        		try {
        			caseNumber = new Integer(actionEnvironment.getRequestParameter("casenumberselected").toString());
        		} catch (NumberFormatException nfe) {
        			errorWithInputs = true;
        			error += "Invalid casenumber. ";
        		}
        	} else {
        		errorWithInputs = true;
        	}
        	
        	if (errorWithInputs) {
        		error += "Add case was not successful, error with input params. ";
        		allok = false;
        	} else {
	        	
        		try {
        			// Get the courtid
        			Integer courtId = 0; // In the context of this method this means that we want all courts 
	        		courtId = drc.getCourtId(courtName);
	        		
	        		// Determine if the case already exists (at all in XHIBIT)
	        		String caseIdStr = drc.checkCaseExists(courtId, caseType, caseNumber);
	        		Integer caseId = new Integer(caseIdStr);
	        		if (caseId.intValue() > 0) { // Case exists
		        		// Determine if case is already being "managed"
	        			boolean caseAlreadyManaged = !drc.checkCaseAlreadyAdded(caseId).equals("0");
	        			if (!caseAlreadyManaged) {
	        				drc.addManagedCase(caseId);
	        			} else {
		        			// case already added
		        			allok = false;
		        			error +="This case ("+caseType+caseNumber+") is already in the managed case list. ";
		        		}
	        		} else {
	        			// case doesn't exist
	        			allok = false;
	        			error +="This case ("+caseType+caseNumber+") is not a valid case for "+courtName+". ";
	        		}
	        		
        		} catch (Exception e) {
        			allok = false;
       				error = "Failed to add case ("+caseType+caseNumber+") - please see logs.";
        			e.printStackTrace();
        		}
	        }
        	
        	// Set any return/error text and post back to same page
        	if (allok) {
        		actionEnvironment.setRequestParameter("responseText", "Case "+caseType+caseNumber+" added successfully.");
        	} else {
        		actionEnvironment.setRequestParameter("responseText", error+"If necessary contact the XHIBIT Support team for assistance.");
        	}
        	
        	// Ensure all managed cases are returned
        	try {
        		Integer courtId = 0; // In the context of this method this means that we want all courts 
        		Collection<ManagedCase> managedCases = drc.getManagedCasesByCourt(courtId);
        		actionEnvironment.setRequestParameter("managedCases", managedCases);
        		
        		actionEnvironment.setRequestParameter("selectedCourtName", courtName);
        		
    		} catch (Exception e) {
    			log.error("AddManagedCase: Unable to get list of all managed cases to return - please see logs or contact the XHIBIT Support team for assistance.");
    			e.printStackTrace();
    		}
        	actionEnvironment.setResponseName("managecases");

        } else {
            actionEnvironment.setResponseName("dontthinkweshouldgethere");
        }
    }
}