package uk.gov.courtservice.xhibit.business.services.directionsforcase;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;

/**
 * <p>
 * Title: DirectionsForCaseHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class DirectionsForCaseHelper {
	private static final Logger LOG = CSServices.getLogger(DirectionsForCaseHelper.class);

	private DirectionsForCaseMaintainer directionsForCaseMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public DirectionsForCaseHelper() {
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
	}
	
	/**
     * Get the directions for the case id.
     * 
     * @param caseId
     *            the id of the case to retrieve.
     * @return DirectionsForCaseBasicValue
     * @throws FinderException
     */
    public DirectionsForCaseBasicValue getDirectionsForCaseByCaseId(Integer caseId) throws FinderException {
        String methodName = "getCase() ";
        LOG.debug(methodName + "(" + caseId + ")");
        try {
        	DirectionsForCase local = directionsForCaseMaintainer.findByCaseId(caseId);
        	DirectionsForCaseBasicValue basicValue = directionsForCaseMaintainer.getBasicValue(local);
            LOG.debug(methodName + " exited");
            return basicValue;
        } catch (ObjectNotFoundException ex) {
        	CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

	/**
	 * Description: Save Directions For Case  
	 * 
	 * @param DirectionsForCaseBasicValue
	 * @throws ObjectNotFoundException 
	 */
    public void saveDirectionsForCase(DirectionsForCaseBasicValue basicValue, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "saveDirectionsForCase() ";
        LOG.debug(methodName + " entered");
        try {
        	if (basicValue.getId() == null) {
        		directionsForCaseMaintainer.create(basicValue, userDisplayName);
        	} else {
        		directionsForCaseMaintainer.update(basicValue, userDisplayName);
        	}
    		LOG.debug(methodName + " exited");
	    } catch (ObjectNotFoundException ex) {
	    	CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
	        throw new EJBException(ex);
	    }          
	}	 
}