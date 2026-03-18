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
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListComplexValue;

/**
 * <p>
 * Title: DefOnCaseOnListMaintainer
 * </p>
 * <p>
 * Description: DefOnCaseOnList Entity Maintainer Class
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
public class DefOnCaseOnListMaintainer extends AbstractEntityMaintainer {
	private DefOnCaseOnListHome home = null;

	/**
	 * Default constructor.
	 */
	public DefOnCaseOnListMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public DefOnCaseOnList findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find the collection using the case on list id.
	 * 
	 * @param integer
	 *            Case on list id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection<DefOnCaseOnList> findByCaseOnListId(Integer caseOnListId) throws ObjectNotFoundException {
		try {
			log.debug("findByCaseOnListId");
			return this.getHome().findByCaseOnListId(caseOnListId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Find the collection using the defendant on case id.
	 * 
	 * @param integer
	 *            Defendant on case id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection<DefOnCaseOnList> findByDefendantOnCaseId(Integer defendantOnCaseId) throws ObjectNotFoundException {
		try {
			log.debug("findByDefendantOnCaseId");
			return this.getHome().findByDefendantOnCaseId(defendantOnCaseId);
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
	 *            DefOnCaseOnList
	 * @return DefOnCaseOnListBasicValue
	 */
	public DefOnCaseOnListBasicValue getBasicValue(DefOnCaseOnList local) {
		log.debug("getBasicValue");
		DefOnCaseOnListBasicValue value = new DefOnCaseOnListBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            DefOnCaseOnList
	 * @return DefOnCaseOnListComplexValue
	 */
	public DefOnCaseOnListComplexValue getComplexValue(DefOnCaseOnList local) {
		log.debug("Entered: getComplexValue");
		DefOnCaseOnListComplexValue value = new DefOnCaseOnListComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
	/**
	 * Create and return a Collection given a local entity.
	 * 
	 * @param locals
	 *            Collection
	 * @return Collection<DefOnCaseOnListBasicValue>
	 */
	public Collection<DefOnCaseOnListBasicValue> getBasicValues(Collection locals) {
		log.debug("Entered: getBasicValues"); 
		return getCollectionValues(false, locals);
	}

	/**
	 * Create and return a Collection given a local entity.
	 * 
	 * @param locals
	 *            Collection
	 * @return Collection<DefOnCaseOnListComplexValue>
	 */
	public Collection<DefOnCaseOnListComplexValue> getComplexValues(Collection locals) {
		log.debug("Entered: getComplexValues"); 
		return getCollectionValues(true, locals);
	}

	/**
	 * The DefOnCaseOnList Home.
	 * 
	 * @return DefOnCaseOnListHome
	 */
	public DefOnCaseOnListHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise DefOnCaseOnListHome");
			this.home = (DefOnCaseOnListHome) CSServices.getServiceLocator().getLocalHome(DefOnCaseOnListHome.class);
		}
		return this.home;
	}

	protected void loadValue(DefOnCaseOnListBasicValue value, DefOnCaseOnList local) {
		value.setDefOnCaseOnListId(local.getDefOnCaseOnListId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setCaseOnListId(local.getCaseOnListId());
		value.setCaseId(local.getCaseId());
		value.setObsInd(local.getObsInd());
		value.setCreatedBy(local.getCreatedBy());
		value.setCreationDate(local.getCreationDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setVersion(local.getVersion());
		value.setIsCourtRoomListEntry(local.getIsCourtRoomListEntry());
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
		log.debug("Entered: getCollectionValues");
		if (locals == null)
			return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        DefOnCaseOnList element = null;
        while (it.hasNext()) {
        	element = (DefOnCaseOnList) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof DefOnCaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			DefOnCaseOnListBasicValue basicValue = (DefOnCaseOnListBasicValue) value;
			DefOnCaseOnList local = getHome().create(basicValue.getDefendantOnCaseId(), 
					basicValue.getCaseOnListId(), basicValue.getCaseId(),
					basicValue.getObsInd(), userDisplayName, basicValue.getIsCourtRoomListEntry());
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof DefOnCaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			DefOnCaseOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			DefOnCaseOnListBasicValue basicValue = (DefOnCaseOnListBasicValue) value;

			// Update the record
			local.setDefendantOnCaseId(basicValue.getDefendantOnCaseId());
			local.setCaseOnListId(basicValue.getCaseOnListId());			
			local.setCaseId(basicValue.getCaseId());
			local.setObsInd(basicValue.getObsInd());
			local.setUpdated(userDisplayName);	
			local.setIsCourtRoomListEntry(basicValue.getIsCourtRoomListEntry());

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
		throw new java.lang.UnsupportedOperationException();	
	}
	
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof DefOnCaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			DefOnCaseOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				log.error("Optimistic Lock Error: Entity: " + local.getVersion() + "Requested version: "
						+ value.getVersion());
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			DefOnCaseOnListBasicValue basicValue = (DefOnCaseOnListBasicValue) value;

			// Update the record
			local.setObsInd(basicValue.getObsInd());
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