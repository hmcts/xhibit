package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingComplexValue;

/**
 * <p>
 * Title: FixtureDeftAttendingMaintainer
 * </p>
 * <p>
 * Description: FixtureDeftAttending Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jas Boparai
 * @version 1.0
 */
public class FixtureDeftAttendingMaintainer extends AbstractEntityMaintainer {
	private FixtureDeftAttendingHome home = null;

	/**
	 * Default constructor.
	 */
	public FixtureDeftAttendingMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public FixtureDeftAttending findByPrimaryKey(Integer key) throws ObjectNotFoundException {
		try {
			log.debug("findByPrimaryKey");
			return this.getHome().findByPrimaryKey(key);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}

	/**
	 * Find the collection using the case diary fixture id.
	 * 
	 * @param integer
	 *            Case diary fixture id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCaseDiaryFixtureId(Integer caseDiaryFixtureId) throws ObjectNotFoundException {
		try {
			log.debug("findByCaseDiaryFixtureId");
			return this.getHome().findByCaseDiaryFixtureId(caseDiaryFixtureId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            FixtureDeftAttending
     * @return FixtureDeftAttendingComplexValue
     */
    public FixtureDeftAttendingComplexValue getComplexValue(FixtureDeftAttending local) {
        log.debug("Entered: getComplexValue");
        FixtureDeftAttendingComplexValue value = new FixtureDeftAttendingComplexValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }
    
	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            FixtureDeftAttending
	 * @return FixtureDeftAttendingBasicValue
	 */
	public FixtureDeftAttendingBasicValue getBasicValue(FixtureDeftAttending local) {
		log.debug("getBasicValue");
		FixtureDeftAttendingBasicValue value = new FixtureDeftAttendingBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<FixtureDeftAttendingBasicValue>
     */
    public Collection<FixtureDeftAttendingBasicValue> getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(false, locals);
    }

	@SuppressWarnings("unchecked")
	private <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        FixtureDeftAttending element = null;
        while (it.hasNext()) {
        	element = (FixtureDeftAttending) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }
	
	/**
	 * The FixtureDeftAttending Home.
	 * 
	 * @return FixtureDeftAttendingHome
	 */
	public FixtureDeftAttendingHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise FixtureDeftAttendingHome");
			this.home = (FixtureDeftAttendingHome) CSServices.getServiceLocator().getLocalHome(FixtureDeftAttendingHome.class);
		}
		return this.home;
	}

	protected void loadValue(FixtureDeftAttendingBasicValue value, FixtureDeftAttending local) {
	   	value.setFixtureDeftAttendingId(local.getFixtureDeftAttendingId());
	   	value.setDefendantOnCaseId(local.getDefendantOnCaseId());
	   	value.setCaseDiaryFixtureId(local.getCaseDiaryFixtureId());
	   	value.setAttending(local.getAttending());	   	
		value.setObsInd(local.getObsInd());        
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof FixtureDeftAttendingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			FixtureDeftAttendingBasicValue basicValue = (FixtureDeftAttendingBasicValue) value;
			FixtureDeftAttending result = getHome().create(
					basicValue.getDefendantOnCaseId(), basicValue.getCaseDiaryFixtureId(),
					basicValue.getAttending(), "N", userDisplayName);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        if (!(value instanceof FixtureDeftAttendingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {        
        	// Get the current db values
        	FixtureDeftAttending local = getHome().findByPrimaryKey(value.getId());
        	        	
	        // Check the version
	        if (local.getVersion() == null || value.getVersion() == null || !local.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	        FixtureDeftAttendingBasicValue basicValue = (FixtureDeftAttendingBasicValue) value;

        	// Update the record
	        local.setDefendantOnCaseId(basicValue.getDefendantOnCaseId());
	        local.setCaseDiaryFixtureId(basicValue.getCaseDiaryFixtureId());
	        local.setAttending(basicValue.getAttending());
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
	
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {	
		if (!(value instanceof FixtureDeftAttendingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			// Get the current db values        	        	
			FixtureDeftAttending local = getHome().findByPrimaryKey(value.getId());
	    	        	
	        // Check the version
	        if (local.getVersion() == null || value.getVersion() == null || !local.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	 
	    	// Update the record
			local.setObsInd("Y");
			local.setUpdated(userDisplayName);
			
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
	    } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    }		
	}
}
