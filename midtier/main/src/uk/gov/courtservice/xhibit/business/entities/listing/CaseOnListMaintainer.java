package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;

/**
 * <p>
 * Title: CaseOnListMaintainer
 * </p>
 * <p>
 * Description: CaseOnList Entity Maintainer Class
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
 * 
 * @amend groenmg - ctx-1192 - removal date to the update method
 */
public class CaseOnListMaintainer extends AbstractEntityMaintainer {
	private CaseOnListHome home = null;

	/**
	 * Default constructor.
	 */
	public CaseOnListMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public CaseOnList findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * @param integer
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
	 * Find the collection using the list id.
	 * 
	 * @param integer
	 *            List id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByListId(Integer listId) throws ObjectNotFoundException {
		try {
			log.debug("findByListId");
			return this.getHome().findByListId(listId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass());
			throw new EJBException(f);
		}
	}

	/**
	 * Find the collection using the court site id and court room id.
	 * 
	 * @param integer
	 *            Court site id to use when performing the search
	 * @param integer
	 *            Court room id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findByCourtSiteIdAndCourtRoomId(Integer courtSiteId, Integer courtRoomId) throws ObjectNotFoundException {
		try {
			log.debug("findByCourtSiteIdAndCourtRoomId");
			return this.getHome().findByCourtSiteIdAndCourtRoomId(courtSiteId, courtRoomId);
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
	 *            CaseOnList
	 * @return CaseOnListBasicValue
	 */
	public CaseOnListBasicValue getBasicValue(CaseOnList local) {
		log.debug("getBasicValue");
		CaseOnListBasicValue value = new CaseOnListBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
	
	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            CaseOnList
	 * @return CaseOnListComplexValue
	 */
	public CaseOnListComplexValue getComplexValue(CaseOnList local) {
		log.debug("Entered: getComplexValue");
		CaseOnListComplexValue value = new CaseOnListComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}
    
    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<CaseOnListBasicValue>
     */
    public Collection<CaseOnListBasicValue> getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(false, locals);
    }

	/**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<CaseOnListComplexValue>
     */
    public Collection<CaseOnListComplexValue> getComplexValues(Collection locals) {
        log.debug("Entered: getComplexValues"); 
        return getCollectionValues(true, locals);
    }

	/**
	 * The CaseOnList Home.
	 * 
	 * @return CaseOnListHome
	 */
	public CaseOnListHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise CaseOnListHome");
			this.home = (CaseOnListHome) CSServices.getServiceLocator().getLocalHome(CaseOnListHome.class);
		}
		return this.home;
	}

	protected void loadValue(CaseOnListBasicValue value, CaseOnList local) {
	   	value.setCaseOnListId(local.getCaseOnListId());
	   	value.setCaseId(local.getCaseId());
	   	value.setListId(local.getListId());
	   	value.setSittingOnListId(local.getSittingOnListId());
	   	value.setCourtSiteId(local.getCourtSiteId());
	   	value.setCourtRoomId(local.getCourtRoomId());
	   	value.setReserved(local.getReserved());
	   	value.setFloaterCase(local.getFloaterCase());
	   	value.setTimeMarkingId(local.getTimeMarkingId());
	   	value.setTimeListed(convertToCalendar(local.getTimeListed()));
	   	value.setIsCourtRoomListEntry(local.getIsCourtRoomListEntry());
	   	value.setHearingTypeId(local.getHearingTypeId());
	   	value.setReasonForRemoval(local.getReasonForRemoval());
		value.setCrackedIneffectiveId(local.getCrackedIneffectiveId());
		value.setObsInd(local.getObsInd());       
		value.setSeqNo(local.getSeqNo());
	   	value.setCreatedBy(local.getCreatedBy());
	   	value.setCreationDate(local.getCreationDate());
	   	value.setLastUpdatedBy(local.getLastUpdatedBy());
	   	value.setLastUpdateDate(local.getLastUpdateDate());
	   	value.setVersion(local.getVersion());
		value.setCaseDiaryFixtureId(local.getCaseDiaryFixtureId());
		value.setDateOfRemoval(local.getDateOfRemoval());
		value.setListNotePredefinedId(local.getListNotePredefinedId());
		value.setListNoteText(local.getListNoteText());
		value.setParentCaseOnListId(local.getParentCaseOnListId());
		value.setNhaFirmList(local.getNhaFirmList());
		value.setVacationPreDefinedRsonId(local.getVacationPreDefinedRsonId());
	}
    
	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        CaseOnList element = null;
        while (it.hasNext()) {
        	element = (CaseOnList) it.next();
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
		if (!(value instanceof CaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			CaseOnListBasicValue basicValue = (CaseOnListBasicValue) value;
			Date timeListed = basicValue.getTimeListed() != null ? basicValue.getTimeListed().getTime() : null;
			CaseOnList local = getHome().create(basicValue.getCaseOnListId(), basicValue.getCaseId(), basicValue.getListId(), 
					basicValue.getSittingOnListId(), basicValue.getCourtSiteId(), basicValue.getCourtRoomId(), basicValue.getReserved(), 
					basicValue.getFloaterCase(), basicValue.getTimeMarkingId(), timeListed,
					basicValue.getIsCourtRoomListEntry(), basicValue.getHearingTypeId(), basicValue.getReasonForRemoval(), basicValue.getCrackedIneffectiveId(),
					basicValue.getObsInd(), userDisplayName, basicValue.getSeqNo(), basicValue.getCaseDiaryFixtureId(), basicValue.getDateOfRemoval(),
					basicValue.getListNotePredefinedId(), basicValue.getListNoteText(), basicValue.getParentCaseOnListId(), basicValue.getNhaFirmList(),
					basicValue.getVacationPreDefinedRsonId());
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof CaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			CaseOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			CaseOnListBasicValue basicValue = (CaseOnListBasicValue) value;

			// Update the record
			local.setCaseId(basicValue.getCaseId());
			local.setListId(basicValue.getListId());
			local.setSittingOnListId(basicValue.getSittingOnListId());
			local.setCourtRoomId(basicValue.getCourtRoomId());
			local.setCourtSiteId(basicValue.getCourtSiteId());
			local.setReserved(basicValue.getReserved());
			local.setFloaterCase(basicValue.getFloaterCase());
			local.setTimeMarkingId(basicValue.getTimeMarkingId());
			local.setTimeListed(basicValue.getTimeListed() != null ?  basicValue.getTimeListed().getTime() : null);
			local.setIsCourtRoomListEntry(basicValue.getIsCourtRoomListEntry());
			local.setHearingTypeId(basicValue.getHearingTypeId());
			local.setReasonForRemoval(basicValue.getReasonForRemoval());
			local.setCrackedIneffectiveId(basicValue.getCrackedIneffectiveId());
			local.setSeqNo(basicValue.getSeqNo());
			local.setObsInd(basicValue.getObsInd());
			local.setUpdated(userDisplayName);
			local.setCaseDiaryFixtureId(basicValue.getCaseDiaryFixtureId());
			local.setListNotePredefinedId(basicValue.getListNotePredefinedId());
			local.setListNoteText(basicValue.getListNoteText());
			local.setParentCaseOnListId(basicValue.getParentCaseOnListId());
			local.setNhaFirmList(basicValue.getNhaFirmList());
			local.setVacationPreDefinedRsonId(basicValue.getVacationPreDefinedRsonId());
			local.setDateOfRemoval(basicValue.getDateOfRemoval());

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
		if (!(value instanceof CaseOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			CaseOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			CaseOnListBasicValue basicValue = (CaseOnListBasicValue) value;

			// Update the record
			local.setVacationPreDefinedRsonId(basicValue.getVacationPreDefinedRsonId());
			local.setReasonForRemoval(basicValue.getReasonForRemoval());
			local.setUpdated(userDisplayName);
			if ("Y".equals(local.getObsInd()) && basicValue.getDateOfRemoval() != null) {
				local.setDateOfRemoval(basicValue.getDateOfRemoval());
			} else {
				local.setDateOfRemoval(new Date());
				local.setObsInd("Y");
			}

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		} 	
	}

}