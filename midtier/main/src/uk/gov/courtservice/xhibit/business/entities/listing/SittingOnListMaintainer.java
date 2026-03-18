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
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;

/**
 * <p>
 * Title: SittingOnListMaintainer
 * </p>
 * <p>
 * Description: Sitting Entity Maintainer Class
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
public class SittingOnListMaintainer extends AbstractEntityMaintainer {
	private SittingOnListHome home = null;

	/**
	 * Default constructor.
	 */
	public SittingOnListMaintainer() {
	}

	/**
	 * Find the entity using the supplied primary key.
	 * 
	 * @param id
	 *            Primary key to use when performing the search
	 * @return The local interface of the returned entity
	 * @throws ObjectNotFoundException
	 */
	public SittingOnList findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Find the collection matching the judge id and after the specified date.
	 * 
	 * @param integer
	 *            Judge id to use when performing the search
	 * @param Date
	 * 			  current date
	 * @return collection List of SittingOnListBasicValue
	 * @throws ObjectNotFoundException
	 */
	public Collection findByJudgeIdAndDate(Integer judgeId, Date currentDate) throws ObjectNotFoundException {
		Collection<SittingOnListBasicValue> retList = new ArrayList<SittingOnListBasicValue>();

		try {
			log.debug("findByJudgeIdAndDate");
			Collection<SittingOnList> results = this.getHome().findByJudgeIdAndDate(judgeId, currentDate);
			for (SittingOnList local : results) {
				retList.add(getBasicValue(local));
			}
			return retList;
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
	 *            SittingOnList
	 * @return SittingOnListBasicValue
	 */
	public SittingOnListBasicValue getBasicValue(SittingOnList local) {
		log.debug("getBasicValue");
		SittingOnListBasicValue value = new SittingOnListBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Complex Value given a local entity.
	 * 
	 * @param local
	 *            SittingOnList
	 * @return SittingOnListComplexValue
	 */
	public SittingOnListComplexValue getComplexValue(SittingOnList local) {
		log.debug("getBasicValue");
		SittingOnListComplexValue value = new SittingOnListComplexValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	/**
	 * Create and return a Collection given a local entity.
	 * 
	 * @param locals
	 *            Collection
	 * @return Collection<SittingOnListBasicValue>
	 */
	public Collection<SittingOnListBasicValue> getBasicValues(Collection locals) {
		log.debug("Entered: getBasicValues");
		return getCollectionValues(false, locals);
	}

	/**
	 * Create and return a Collection given a local entity.
	 * 
	 * @param locals
	 *            Collection
	 * @return Collection<SittingOnListComplexValue>
	 */
	public Collection<SittingOnListComplexValue> getComplexValues(Collection locals) {
		log.debug("Entered: getComplexValues");
		return getCollectionValues(true, locals);
	}

	/**
	 * The SittingOnList Home.
	 * 
	 * @return SittingOnListHome
	 */
	public SittingOnListHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise SittingOnListHome");
			this.home = (SittingOnListHome) CSServices.getServiceLocator().getLocalHome(SittingOnListHome.class);
		}
		return this.home;
	}

	protected void loadValue(SittingOnListBasicValue value, SittingOnList local) {
		value.setSittingOnListId(local.getSittingOnListId());
		value.setSittingNumber(local.getSittingNumber());
		value.setListId(local.getListId());
		value.setTimeMarkingId(local.getTimeMarkingId());
		value.setTimeListed(convertToCalendar(local.getTimeListed()));
		value.setJudgeRefId(local.getJudgeRefId());
		value.setJp1(local.getJp1());
		value.setJp2(local.getJp2());
		value.setJp3(local.getJp3());
		value.setJp4(local.getJp4());
		value.setListNoteText(local.getListNoteText());
		value.setFreeTextNoteClassId(local.getFreeTextNoteClassId());
		value.setObsInd(local.getObsInd());
		value.setCourtRoomId(local.getCourtRoomId());
		value.setCourtSiteId(local.getCourtSiteId());
		value.setCreatedBy(local.getCreatedBy());
		value.setCreationDate(local.getCreationDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setVersion(local.getVersion());
	}

	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
		log.debug("Entered: getCollectionValues");
		if (locals == null)
			return null;

		Collection<T> values = new ArrayList<T>();
		Iterator it = locals.iterator();
		T rowValue = null;
		SittingOnList element = null;
		while (it.hasNext()) {
			element = (SittingOnList) it.next();
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
		if (!(value instanceof SittingOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			SittingOnListBasicValue basicValue = (SittingOnListBasicValue) value;
			Date timeListed = basicValue.getTimeListed() != null ? basicValue.getTimeListed().getTime() : null;
			SittingOnList local = getHome().create(basicValue.getSittingOnListId(), basicValue.getSittingNumber(),
					basicValue.getListId(), basicValue.getTimeMarkingId(), timeListed, basicValue.getJudgeRefId(),
					basicValue.getJp1(), basicValue.getJp2(), basicValue.getJp3(), basicValue.getJp4(),
					basicValue.getListNoteText(), basicValue.getFreeTextNoteClassId(),
					basicValue.getObsInd(), userDisplayName, basicValue.getCourtRoomId(), basicValue.getCourtSiteId());
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof SittingOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			SittingOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			SittingOnListBasicValue basicValue = (SittingOnListBasicValue) value;

			// Update the record
			local.setSittingNumber(basicValue.getSittingNumber());
			local.setTimeMarkingId(basicValue.getTimeMarkingId());
			local.setTimeListed(basicValue.getTimeListed() != null ? basicValue.getTimeListed().getTime() : null);
			local.setJudgeRefId(basicValue.getJudgeRefId());
			local.setJp1(basicValue.getJp1());
			local.setJp2(basicValue.getJp2());
			local.setJp3(basicValue.getJp3());
			local.setJp4(basicValue.getJp4());
			local.setListNoteText(basicValue.getListNoteText());
			local.setFreeTextNoteClassId(basicValue.getFreeTextNoteClassId());
			local.setCourtRoomId(basicValue.getCourtRoomId());
			local.setCourtSiteId(basicValue.getCourtSiteId());
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

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();
	}

	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof SittingOnListBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current DB values
			SittingOnList local = getHome().findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			SittingOnListBasicValue basicValue = (SittingOnListBasicValue) value;

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
