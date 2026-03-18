package uk.gov.courtservice.xhibit.web.dr.action;

import java.util.Collection;

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
public class ManageCasesAction extends AbstractAction {

    private static final Logger log = CSServices.getLogger(ManageCasesAction.class);

    /**
     * Empty default constructor
     */
    public ManageCasesAction() {
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
    	String courtName = "SWANSEA";
    	
        if (actionEnvironment.getRequestParameterNames().hasMoreElements()) {
        	// Get elements from request based on what can be done here
        	// Court name, if populated is from a drop-down so will be valid
        	if (actionEnvironment.getRequestParameter("courtnameselected") != null) {
        		courtName = actionEnvironment.getRequestParameter("courtnameselected").toString();
        	}
        }
        
    	// Always need to get a list of all cases before going to this screen
    	try {
    		Integer courtId = 0; // In the context of this method this means that we want all courts 
    		Collection<ManagedCase> managedCases = drc.getManagedCasesByCourt(courtId);
    		actionEnvironment.setRequestParameter("managedCases", managedCases);
    		
    		actionEnvironment.setRequestParameter("selectedCourtName", courtName);
    		
		} catch (Exception e) {
			log.error("Unable to complete manage cases action - please see logs or contact the XHIBIT Support team for assistance.");
			e.printStackTrace();
		}
    	
        actionEnvironment.setResponseName("managecases");
    }
}