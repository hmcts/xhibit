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
public class RemoveManagedCaseAction extends AbstractAction {

    private static final Logger log = CSServices.getLogger(RemoveManagedCaseAction.class);

    /**
     * Empty default constructor
     */
    public RemoveManagedCaseAction() {
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
        	String courtName = "SWANSEA";
        	boolean allok = true;
        	
        	// Do some validation first
        	Integer caseIdToRemove = new Integer(0);
        	
        	boolean errorWithInputs = false;
        	// Case id to be removed - should never be an input problem as value is driven dynamically when user clicks the remove button 
        	if (actionEnvironment.getRequestParameter("caseidselected") != null) {
        		caseIdToRemove = new Integer(actionEnvironment.getRequestParameter("caseidselected").toString());
        	} else {
        		errorWithInputs = true;
        	}

        	// Court name, if populated is from a drop-down so will be valid
        	if (actionEnvironment.getRequestParameter("courtnameselected") != null) {
        		courtName = actionEnvironment.getRequestParameter("courtnameselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	
        	if (errorWithInputs) {
        		error = "Removal was not successful, error with input params";
        		allok = false;
        	} else {
	        	
        		try {
        			if (caseIdToRemove > 0) {
        				drc.removeManagedCase(caseIdToRemove);
        			} else {
        				allok = false;
           				error = "Case id of "+caseIdToRemove+" - please see logs.";
        			}
        		} catch (Exception e) {
        			allok = false;
       				error = "Failed to remove case - please see logs.";
        			e.printStackTrace();
        		}
	        }
        	
        	// Set any return/error text and post back to same page
        	if (allok) {
        		actionEnvironment.setRequestParameter("responseText", "Case removed successfully.");
        	} else {
        		actionEnvironment.setRequestParameter("responseText", error+" If necessary contact the XHIBIT Support team for assistance.");
        	}
        	
        	// Ensure all managed cases are returned
        	try {
        		Integer courtId = 0; // In the context of this method this means that we want all courts 
        		Collection<ManagedCase> managedCases = drc.getManagedCasesByCourt(courtId);
        		actionEnvironment.setRequestParameter("managedCases", managedCases);
        		
        		actionEnvironment.setRequestParameter("selectedCourtName", courtName);
        		
    		} catch (Exception e) {
    			log.error("RemoveManagedCase: Unable to get list of all managed cases to return - please see logs or contact the XHIBIT Support team for assistance.");
    			e.printStackTrace();
    		}
        	actionEnvironment.setResponseName("managecases");

        } else {
            actionEnvironment.setResponseName("dontthinkweshouldgethere");
        }
    }
}