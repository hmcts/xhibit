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
public class UpdateDataAction extends AbstractAction {

    private static final Logger log = CSServices.getLogger(UpdateDataAction.class);

    /**
     * Empty default constructor
     */
    public UpdateDataAction() {
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
        	String daySelected = "";
        	String monthSelected = "";
        	String yearSelected = "";
        	
        	boolean errorWithInputs = false;
        	// Court name, if populated is from a drop-down so will be valid
        	if (actionEnvironment.getRequestParameter("courtnameselected") != null) {
        		courtName = actionEnvironment.getRequestParameter("courtnameselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	
        	// JavaScript validation is in place to validate dates before update can be pressed
        	if (actionEnvironment.getRequestParameter("dayselected") != null) {
        		daySelected = actionEnvironment.getRequestParameter("dayselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	if (actionEnvironment.getRequestParameter("monthselected") != null) {
        		monthSelected = actionEnvironment.getRequestParameter("monthselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	if (actionEnvironment.getRequestParameter("yearselected") != null) {
        		yearSelected = actionEnvironment.getRequestParameter("yearselected").toString();
        	} else {
        		errorWithInputs = true;
        	}
        	
        	if (errorWithInputs) {
        		error = "Update was not successful, error with input params. If necessary contact the XHIBIT Support team for assistance.";
        		allok = false;
        	} else {
	        	
        		Date resetDate = null;
	        	// Convert date fields into a Java date
	        	try {
		        	Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthSelected);
		        	Calendar cal = Calendar.getInstance();
		        	cal.setTime(date);
		        	GregorianCalendar gc = new GregorianCalendar(new Integer(yearSelected).intValue(), cal.get(Calendar.MONTH), new Integer(daySelected).intValue());
		        	resetDate = new Date(gc.getTimeInMillis());
	        	} catch (java.text.ParseException pe) {
	        		log.error("Error parsing date with in params: " +daySelected + " : " + monthSelected + " : " + yearSelected);
	        		error = "Error parsing date with in params: " +daySelected + " : " + monthSelected + " : " + yearSelected + ";  If necessary contact the XHIBIT Support team for assistance.";
	        		pe.printStackTrace();
	        		allok = false;
	        	}
	        	
	        	// If all valid then do the update
	        	if (allok) {
	        		if (resetDate != null) {
	        			log.debug("Updating date to " + resetDate.toString());
	        		} else {
	        			allok = false;
	        			error = "Date to reset to is null.  If necessary contact the XHIBIT Support team for assistance.";
	        		}
	        		try {
		        		Integer courtId = 0; // In the context of this method this means that we want all courts 
		        		if (courtName.toUpperCase() != "ALL") {
		        			courtId = drc.getCourtId(courtName);
		        		}
		        		Collection<ManagedCase> managedCases = drc.getManagedCasesByCourt(courtId);
		        		
		        		// Find how many days between current date and date that dates-of-birth are being reset to
		        		int daysBetween = getDayCount(new Date(), resetDate);
		        		if (managedCases.size() == 0) {
		        			allok = false;
		        			error = "No cases available to update. If necessary contact the XHIBIT Support team for assistance.";
		        		} else {
			        		for (ManagedCase returnedCase : managedCases) {
			        			// For each case update the date of birth to be the age they were on the date input
			        			//Integer caseId = returnedCase.getCaseId();
			        			Date defendantDOB = returnedCase.getDefendantDOB();
			        			
			        			// Some defendants on NLE dont seem to have a DOB so if that's the case then skip it, log it but don't error
			        			if (defendantDOB != null) {
				        			Calendar c = Calendar.getInstance();
				        			c.setTime(defendantDOB);
				        			c.add(Calendar.DATE, daysBetween);
				        			
				        			Date newDefendantDOB = c.getTime();
				        			drc.updateDefendantDOB(newDefendantDOB, returnedCase.getDefendantId());
			        			} else {
			        				log.error("Defendant does not have a DOB. CaseId: "+returnedCase.getCaseId()+", defendant name: "+returnedCase.getDefendantFirstName()+" "+returnedCase.getDefendantSurname());
			        			}
			        		}

			        		// Once complete do the update of CREST
			        		drc.updateCREST();
		        		}
	        		} catch (Exception e) {
	        			allok = false;
	        			error = "Failed to update - please see logs or contact the XHIBIT Support team for assistance.";
	        			e.printStackTrace();
	        		}
	        	}
	        }
        	
        	// Set any return/error text and post back to same page
        	if (allok) {
        		actionEnvironment.setRequestParameter("responseText", "Update successful");
        	} else {
        		actionEnvironment.setRequestParameter("responseText", error);
        	}
        	actionEnvironment.setResponseName("reset_data");

        } else {
            actionEnvironment.setResponseName("dontthinkweshouldgethere");
        }
    }
    
    private int getDayCount(Date d1, Date d2) {
    	int diff = -1;
    	
    	if (d1.getTime() > d2.getTime()) {
    	   	diff = (int) Math.round((d1.getTime() - d2.getTime()) / (double) 86400000);
    	} else {
    		diff = (int) Math.round((d2.getTime() - d1.getTime()) / (double) 86400000) * -1; // Multiplying by -1 to make it negative as d2 is greater than d1
    	}
    	
    	return diff;
    }
    
}