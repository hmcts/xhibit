package uk.gov.courtservice.xhibit.business.entities.directionsforcase;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseComplexValue;

/**
 * Maintainer for directions for case entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class DirectionsForCaseMaintainer extends AbstractEntityMaintainer {
    private DirectionsForCaseHome home = null;

    /**
     * Default constructor.
     */
    public DirectionsForCaseMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public DirectionsForCase findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        try {
            log.debug("Entered: findByPrimaryKey");
            return this.getHome().findByPrimaryKey(key);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Find the entity using the case listing entry id.
     * 
     * @param Integer
     *            Case id to use when performing the search
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public DirectionsForCase findByCaseId(Integer caseId) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findByCaseId");
            return this.getHome().findByCaseId(caseId);
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (FinderException f) {
        	throw new EJBException(f);
        }	
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            DirectionsForCase
     * @return DirectionsForCaseBasicValue
     */
    public DirectionsForCaseBasicValue getBasicValue(DirectionsForCase local) {
        log.debug("Entered: getBasicValue");
        DirectionsForCaseBasicValue value = new DirectionsForCaseBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            DirectionsForCase
     * @return DirectionsForCaseComplexValue
     */
    public DirectionsForCaseComplexValue getComplexValue(DirectionsForCase local) {
        log.debug("Entered: getComplexValue");
        DirectionsForCaseComplexValue value = new DirectionsForCaseComplexValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue((DirectionsForCaseBasicValue) value, local);
        return value;
    }    
    
    /**
     * The DirectionsForCase Home.
     * 
     * @return DirectionsForCaseHome
     */
    public DirectionsForCaseHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise DirectionsForCaseHome");
            this.home = (DirectionsForCaseHome) CSServices.getServiceLocator().getLocalHome(DirectionsForCaseHome.class);
        }
        return this.home;
    }

    protected void loadValue(DirectionsForCaseBasicValue value, DirectionsForCase local) {    	
    	value.setDirectionsForCaseId(local.getDirectionsForCaseId());
    	value.setFreetext(local.getFreetext());
    	value.setDateTime(local.getDateTime());
    	value.setListDate(local.getListDate());
    	value.setListType(local.getListType());
    	value.setListedAs(local.getListedAs());
    	value.setDirectionsText(local.getDirectionsText());
    	value.setTrialTimeUnit(local.getTrialTimeUnit());
    	value.setTrialTimeEstimate(local.getTrialTimeEstimate());
    	value.setHasPanddForm(local.getHasPanddForm());
    	value.setCaseId(local.getCaseId());		
    }
    
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof DirectionsForCaseBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			DirectionsForCaseBasicValue basicValue = (DirectionsForCaseBasicValue) value;
			DirectionsForCase result = getHome().create(
					basicValue.getFreetext(), basicValue.getDateTime(),
					basicValue.getListDate(), basicValue.getListType(), basicValue.getListedAs(),
					basicValue.getDirectionsText(), basicValue.getTrialTimeUnit(), basicValue.getTrialTimeEstimate(),
					basicValue.getHasPanddForm(), basicValue.getCaseId(),
					userDisplayName);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		
        if (!(value instanceof DirectionsForCaseBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {        
        	// Get the current db values
        	DirectionsForCase local = getHome().findByPrimaryKey(value.getId());
        	        	
	        // Check the version
	        if (local.getVersion() == null || value.getVersion() == null || !local.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	        DirectionsForCaseBasicValue basicValue = (DirectionsForCaseBasicValue) value;

        	// Update the record        	
        	local.setFreetext(basicValue.getFreetext());
        	local.setDateTime(basicValue.getDateTime());
        	local.setListDate(basicValue.getListDate());
        	local.setListType(basicValue.getListType());
        	local.setListedAs(basicValue.getListedAs());
        	local.setDirectionsText(basicValue.getDirectionsText());
        	local.setTrialTimeUnit(basicValue.getTrialTimeUnit());
        	local.setTrialTimeEstimate(basicValue.getTrialTimeEstimate());
        	local.setHasPanddForm(basicValue.getHasPanddForm());
        	local.setCaseId(basicValue.getCaseId());	
        	local.setUpdated(userDisplayName);
        	
        	
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new UnsupportedOperationException();
	}		
}