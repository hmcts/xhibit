package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseNonAvailDaysBasicValue;

/**
 * <p>
 * Title: CaseNonAvailDaysMaintainer
 * </p>
 * <p>
 * Description: Case Non Available Days Entity Maintainer Class
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
public class CaseNonAvailDaysMaintainer extends AbstractEntityMaintainer {
	private CaseNonAvailDaysHome home = null;

	/**
	 * Default constructor.
	 */
	public CaseNonAvailDaysMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public CaseNonAvailDays findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find the collection using the case id.
	 * 
	 * @param caseId
	 *            Case id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCaseId(Integer caseId) throws ObjectNotFoundException {
		try {
			log.debug("findByCaseId");
			return this.getHome().findByCaseId(caseId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Find the collection using the case id and date.
	 * 
	 * @param caseId
	 *            Case id to use when performing the search
	 * @param date
	 *            date to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCaseIdAndDate(Integer caseId, Date date) throws ObjectNotFoundException {
		try {
			log.debug("findByCaseIdAndDate");
			return this.getHome().findByCaseIdAndDate(caseId, DateTimeUtilities.stripTimeToUtilDate(date));
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Find the collection using the case id and date.
	 * 
	 * @param caseId
	 *            Case id to use when performing the search
	 * @param fromDate
	 *            from date to use when performing the search
	 * @param toDate
	 *            to date to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCaseIdAndDate(Integer caseId, Date fromDate, Date toDate) throws ObjectNotFoundException {
		try {
			log.debug("findByCaseIdAndDate");
			return this.getHome().findByCaseIdAndDate(caseId, DateTimeUtilities.stripTimeToUtilDate(fromDate),
														DateTimeUtilities.stripTimeToUtilDate(toDate));
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Create and return a Basic Value given a local entity.
	 * 
	 * @param local
	 *            CaseNonAvailDays
	 * @return CaseNonAvailDaysBasicValue
	 */
	public CaseNonAvailDaysBasicValue getBasicValue(CaseNonAvailDays local) {
		log.debug("getBasicValue");
		CaseNonAvailDaysBasicValue value = new CaseNonAvailDaysBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * The CaseNonAvailDays Home.
	 * 
	 * @return CaseNonAvailDaysHome
	 */
	public CaseNonAvailDaysHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise CaseNonAvailDaysHome");
			this.home = (CaseNonAvailDaysHome) CSServices.getServiceLocator().getLocalHome(CaseNonAvailDaysHome.class);
		}
		return this.home;
	}

	protected void loadValue(CaseNonAvailDaysBasicValue value, CaseNonAvailDays local) {
	   	value.setNadId(local.getNadId());
	   	value.setCaseId(local.getCaseId());
	   	value.setStartDate(local.getStartDate());
	   	value.setEndDate(local.getEndDate());
	   	value.setReason(local.getReason());
	   	value.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof CaseNonAvailDaysBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {
        	CaseNonAvailDaysBasicValue basicValue = (CaseNonAvailDaysBasicValue) value;
        	CaseNonAvailDays result = getHome().create(
					basicValue.getCaseId(), basicValue.getStartDate(), 
					basicValue.getEndDate(), basicValue.getReason(),
					basicValue.getObsInd(), userDisplayName);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();
	}
		
	public CaseNonAvailDays update(CaseNonAvailDaysBasicValue value, String userDisplayName) throws ObjectNotFoundException {	
        try {
        	// Get the current db values
        	CaseNonAvailDays cnad = getHome().findByPrimaryKey(value.getId());
	        
	        // Check the version
	        if (cnad.getVersion() == null || value.getVersion() == null || !cnad.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	        CaseNonAvailDaysBasicValue basicValue = (CaseNonAvailDaysBasicValue) value;
	        
	        // Update the record
	        cnad.setCaseId(basicValue.getCaseId());
	        cnad.setStartDate(basicValue.getStartDate());
	        cnad.setEndDate(basicValue.getEndDate());
	        cnad.setReason(basicValue.getReason());
	        cnad.setUpdated(userDisplayName);	        
	        return cnad;
	        
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
		if (!(value instanceof CaseNonAvailDaysBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			// Get the current db values        	        	
			CaseNonAvailDays local = getHome().findByPrimaryKey(value.getId());
	    	        	
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
