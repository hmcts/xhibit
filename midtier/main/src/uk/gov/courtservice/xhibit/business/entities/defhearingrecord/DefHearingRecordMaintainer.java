package uk.gov.courtservice.xhibit.business.entities.defhearingrecord;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordComplexValue;

/**
 * <p>
 * Title:DefHearingRecordMaintainer
 * </p>
 * <p>
 * Description: Maintainer for the DefHearingRecord
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran / Marie Holmberg
 * @version 1.0
 */
public class DefHearingRecordMaintainer extends AbstractEntityMaintainer {
	private static DefHearingRecordMaintainer instance;

	private static final Logger log = CSServices.getLogger(DefHearingRecordMaintainer.class);

	private static DefHearingRecordHome home = null;

	/**
	 * Default constructor that instantiates the home.
	 */
	public DefHearingRecordMaintainer() {
		if (home == null) {
			home = (DefHearingRecordHome) CSServices.getServiceLocator().getLocalHome(DefHearingRecordHome.class);
		}
	}

	/**
	 * Accessor method to treat this class as a singleton (GoF), however, the
	 * constructor is still public as used in numerous places
	 * 
	 * @return The static instance of this maintainer
	 */
	public static DefHearingRecordMaintainer getInstance() {
		if (instance == null) {
			synchronized (DefHearingRecordMaintainer.class) {
				if (instance == null) {
					DefHearingRecordMaintainer tmp = new DefHearingRecordMaintainer();
					instance = tmp;
				}
			}
		}

		return instance;
	}

	/**
	 * Returns a Basic value from the local entity
	 * 
	 * @param local
	 *            DefHearingRecord
	 * @return DefHearingRecordBasicValue
	 */
	public DefHearingRecordBasicValue getDefHearingRecordBasicValue(DefHearingRecord local) {
		log.debug("local.getHearingRecordId() : " + local.getHearingRecordId());
		Integer version = local.getVersion();
		if (version == null)// make sure we don't get a nullpointer
		// exception
		{
			version = new Integer(-1);
		}
		DefHearingRecordBasicValue bv = new DefHearingRecordBasicValue(local.getHearingRecordId(), version);
		copyEntityPropsToVO(local, bv);

		log.debug(" testing for hearing record on returned basic value " + bv.getHearingID());
		log.debug(" testing for defHearingRecordId " + bv.getId());
		return bv;
	}

	/**
	 * Returns a Complex value from the local entity
	 * 
	 * @param local
	 *            DefHearingRecord
	 * @return DefHearingRecordComplexValue
	 */
	public DefHearingRecordComplexValue getDefHearingRecordComplexValue(DefHearingRecord local) {
		String methodName = "getDefHearingRecordComplexValue() - ";
		log.debug(methodName + "called");

		DefHearingRecordComplexValue cv = createComplexVO(local);

		log.debug(methodName + "exited - OK");
		return cv;
	}

	/**
	 * Create method
	 * 
	 * @param value
	 *            CSAbstractValue
	 * @return CSEntityLocal
	 */
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		String methodName = "create() - ";
		log.debug(methodName + "called");

		if (!(value instanceof DefHearingRecordBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}

		try {
			DefHearingRecordBasicValue bv = (DefHearingRecordBasicValue) value;

			// Make sure we don't get NullpointerException since the
			// following values
			// will be null at times
			Timestamp adjournedDate = convertDateToTimestamp(bv.getAdjournedDate());
			Timestamp startDateNewBailStatus = convertDateToTimestamp(bv.getStartDateNewBailStatus());
			Timestamp dateBailApplication = convertDateToTimestamp(bv.getDateBailApplication());
			Timestamp hearingStartDate = convertDateToTimestamp(bv.getHearingStartDate());
			Timestamp hearingEndDate = convertDateToTimestamp(bv.getHearingEndDate());

			if (log.isDebugEnabled()) {
				log.debug("home.create bv.getRefAdjournmentID()" + bv.getRefAdjournmentID());
				log.debug("home.create adjournedDate" + adjournedDate);
				log.debug("home.create bv.getIsAdjourned()" + bv.getIsAdjourned());
				log.debug("home.create startDateNewBailStatus" + startDateNewBailStatus);
				log.debug("home.create bv.getNewBailStatus()" + bv.getNewBailStatus());
				log.debug("home.create dateBailApplication" + dateBailApplication);
				log.debug("home.create bv.getSubstBailApplication()" + bv.getSubstBailApplication());
				log.debug("home.create bv.getOralEvidence()" + bv.getOralEvidence());
				log.debug("home.create bv.getResultBailApplication()" + bv.getResultBailApplication());
				log.debug("home.create bv.getIsHraApplication()" + bv.getIsHraApplication());
				log.debug("home.create bv.getRefDefHearingTypeID()" + bv.getRefDefHearingTypeID());
				log.debug("home.create bv.getEndBailStatus()" + bv.getEndBailStatus());
				log.debug("home.create bv.getStartBailStatus()" + bv.getStartBailStatus());
				log.debug("home.create bv.getDefendantOnCaseID()" + bv.getDefendantOnCaseID());
				log.debug("home.create bv.getHearingID()" + bv.getHearingID());
				log.debug("home.create bv.getHearingDateFreetext1()" + bv.getHearingDateFreetext1());
				log.debug("home.create bv.getHearingDateFreetext2()" + bv.getHearingDateFreetext2());
				log.debug("home.create bv.getHearingDateFreetext3()" + bv.getHearingDateFreetext3());

				log.debug("home.create bv.getHearingStartDate()" + hearingStartDate);
				log.debug("home.create bv.getHearingEndDate()" + hearingEndDate);
				log.debug("home.create bv.getLastCalculatedDuration()" + bv.getLastCalculatedDuration());
				log.debug("home.create bv.getS41Application()" + bv.getS41Application());
				log.debug("home.create bv.getS41Granted()" + bv.getS41Granted());
				log.debug("home.create bv.getS41ApplicationMade()" + bv.getS41ApplicationMade());
				log.debug("home.create userDisplayName" + userDisplayName);
			}

			CSEntityLocal local = home.create(bv.getRefAdjournmentID(),
					// new Timestamp(bv.getAdjournedDate().getTime()),
					adjournedDate, bv.getIsAdjourned(), startDateNewBailStatus,
					// new
					// Timestamp(bv.getStartDateNewBailStatus().getTime()),
					bv.getNewBailStatus(), dateBailApplication,
					// new Timestamp(bv.getDateBailApplication().getTime()),
					bv.getSubstBailApplication(), bv.getOralEvidence(), bv.getResultBailApplication(),
					bv.getIsHraApplication(), bv.getRefDefHearingTypeID(), bv.getEndBailStatus(),
					bv.getStartBailStatus(), bv.getDefendantOnCaseID(), bv.getHearingID(), bv.getHearingDateFreetext1(),
					bv.getHearingDateFreetext2(), bv.getHearingDateFreetext3(), hearingStartDate, hearingEndDate,
					bv.getLastCalculatedDuration(), bv.getMpHearingType(), userDisplayName, bv.getTrialInDefAbsence(),
					bv.getSentenceInDefAbsence(), bv.getFormAStatus(), bv.getFormACourtClerk(), bv.getS41Application(),
					bv.getS41Granted(), bv.getS41ApplicationMade());

			if (local == null) {
				log.debug("DefHearingRecordMaintainer failed to succesfully create local ref");
			} else {
				log.debug("DefHearingRecordMaintainer succesfully created " + local);
			}
			return local;
		} catch (CreateException c) {
			CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
			throw new EJBException(c);
		}
	}

	/**
	 * Update method
	 * 
	 * @param value
	 * @throws ObjectNotFoundException
	 */
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		String methodName = "update() - ";
		log.debug(methodName + "called");

		if (!(value instanceof DefHearingRecordBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}

		DefHearingRecordBasicValue bv = (DefHearingRecordBasicValue) value;
		try {
			// Find home...
			DefHearingRecord local = home.findByKeyAndVersion(bv.getId(), bv.getVersion());

			// Locking check...
			if (!local.getVersion().equals(bv.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				local.setRefAdjournmentId(bv.getRefAdjournmentID());
				local.setAdjournedDate(convertDateToTimestamp(bv.getAdjournedDate()));
				local.setIsAdjourned(bv.getIsAdjourned());
				local.setStartDateNewBailStatus(convertDateToTimestamp(bv.getStartDateNewBailStatus()));
				local.setNewBailStatus(bv.getNewBailStatus());
				local.setDateBailApplication(convertDateToTimestamp(bv.getDateBailApplication()));
				local.setSubstBailApplication(bv.getSubstBailApplication());
				local.setOralEvidence(bv.getOralEvidence());
				local.setResultBailApplication(bv.getResultBailApplication());
				local.setIsHraApplication(bv.getIsHraApplication());
				local.setRefDefHearingTypeId(bv.getRefDefHearingTypeID());
				local.setEndBailStatus(bv.getEndBailStatus());
				log.debug("X12B059 - endBailStatus being set to " + bv.getEndBailStatus());
				local.setStartBailStatus(bv.getStartBailStatus());
				// local.setDateOfCommittal(new
				// Timestamp(bv.getDateBailApplication().getTime()));
				local.setDefendantOnCaseId(bv.getDefendantOnCaseID());
				// local.setHearingId(bv.getHearingID());
				local.setHearingDateFreeTxt1(bv.getHearingDateFreetext1());
				local.setHearingDateFreeTxt2(bv.getHearingDateFreetext2());
				local.setHearingDateFreeTxt3(bv.getHearingDateFreetext3());

				local.setHearingStartDate(convertDateToTimestamp(bv.getHearingStartDate()));
				local.setHearingEndDate(convertDateToTimestamp(bv.getHearingEndDate()));
				local.setLastCalculatedDuration(bv.getLastCalculatedDuration());
				local.setMpHearingType(bv.getMpHearingType());
				local.setTrialInDefAbsence(bv.getTrialInDefAbsence());
				local.setSentenceInDefAbsence(bv.getSentenceInDefAbsence());

				local.setFormAStatus(bv.getFormAStatus());
				local.setFormACourtClerk(bv.getFormACourtClerk());
				
				local.setS41Application(bv.getS41Application());
				local.setS41Granted(bv.getS41Granted());
				local.setS41ApplicationMade(bv.getS41ApplicationMade());

				local.setUpdated(userDisplayName);
			}

			log.debug(methodName + "defHearingRecordMaintainer.update() exited - OK");
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new OptimisticLockException(e);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new EJBException(ex);
		}
	}

	/**
	 * Deletes the entity
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		String methodName = "delete() - ";
		log.debug(methodName + "called");

		try {
			DefHearingRecord local = home.findByKeyAndVersion(id, version);
			if (!local.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				local.remove();
				log.debug(methodName + "exited - OK");
			}
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new OptimisticLockException(e);
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		} catch (RemoveException r) {
			CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
			throw new EJBException(r);
		}
	}

	/**
	 * Find DefHearingRecord by the primary key
	 * 
	 * @param id
	 *            Integer
	 * @return DefHearingRecord
	 * @throws ObjectNotFoundException
	 */
	public DefHearingRecord findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		try {
			return home.findByPrimaryKey(id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
	}

	/**
	 * Find DefHearingRecord by the DefendantOnCaseID and HearingID
	 * 
	 * @param Integer
	 *            defendantOnCaseID
	 * @param Integer
	 *            hearingID
	 * @return DefHearingRecord
	 * @throws ObjectNotFoundException
	 */
	public DefHearingRecord findByDefendantOnCaseIDAndHearingID(Integer docID, Integer hID)
			throws ObjectNotFoundException {
		log.debug("docID = " + docID.intValue() + "  hID = " + hID.intValue());
		try {
			return home.findByDefendantOnCaseIDAndHearingID(docID, hID);
		} catch (ObjectNotFoundException e) {
			log.warn("findByDefendantOnCaseIDAndHearingID " + e);
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
	}

	/**
	 * Find DefHearingRecord by the DefendantOnCaseID
	 * 
	 * @param Integer
	 *            defendantOnCaseID
	 * @return DefHearingRecord
	 * @throws ObjectNotFoundException
	 */
	public Collection findByDefendantOnCaseID(Integer docID) throws ObjectNotFoundException {
		log.debug("docID = " + docID.intValue());
		try {
			return home.findByDefendantOnCaseID(docID);
		} catch (ObjectNotFoundException e) {
			log.warn("findByDefendantOnCaseID " + e);
			throw e;
		} catch (FinderException f) {
			CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
			if (f instanceof ObjectNotFoundException)
				throw (ObjectNotFoundException) f;
			throw new EJBException(f);
		}
	}

	/**
	 * Method to be used when update and delete DefHearingRecord's. This will
	 * make sure that the right record with the right version is attempted to be
	 * deleted for the Optimistic locking.
	 * 
	 * @param key
	 * @param version
	 * @return DefHearingRecord
	 * @exception ObjectNotFoundException
	 * @exception FinderException
	 *                throws EJBException
	 */
	public DefHearingRecord findByKeyAndVersion(Integer key, Integer version) throws ObjectNotFoundException {
		log.debug("findByKeyAndVersion(" + key + ", " + version + ") called");

		try {
			DefHearingRecord defHearingRecord = home.findByKeyAndVersion(key, version);
			log.debug("find successful; returning findByKeyAndVersion( Integer key, Integer version )");
			return defHearingRecord;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new EJBException(ex);
		}
	}

	/**
	 * Create a complex valu from the local entity
	 * 
	 * @param local
	 * @return DefHearingRecordComplexValue
	 */
	private DefHearingRecordComplexValue createComplexVO(DefHearingRecord local) {
		String methodName = "createComplexVO() - ";
		log.debug(methodName + "called");

		DefHearingRecordComplexValue cv = new DefHearingRecordComplexValue(local.getHearingRecordId(),
				local.getVersion());

		copyEntityPropsToVO(local, cv);
		return cv;
	}

	private void copyEntityPropsToVO(DefHearingRecord local, DefHearingRecordBasicValue bv) {
		String methodName = "copyEntityPropsToVO() - ";
		log.debug(methodName + "called");
		bv.setRefAdjournmentID(local.getRefAdjournmentId());
		bv.setAdjournedDate(local.getAdjournedDate());
		bv.setIsAdjourned(local.getIsAdjourned());
		bv.setStartDateNewBailStatus(local.getStartDateNewBailStatus());
		bv.setNewBailStatus(local.getNewBailStatus());
		bv.setDateBailApplication(local.getDateBailApplication());
		bv.setSubstBailApplication(local.getSubstBailApplication());
		bv.setOralEvidence(local.getOralEvidence());
		bv.setResultBailApplication(local.getResultBailApplication());
		bv.setIsHraApplication(local.getIsHraApplication());
		bv.setRefDefHearingTypeID(local.getRefDefHearingTypeId());
		bv.setEndBailStatus(local.getEndBailStatus());
		log.debug("X12B059 - retrieving endBailStatus from database: value = " + local.getEndBailStatus());
		bv.setStartBailStatus(local.getStartBailStatus());
		bv.setDefendantOnCaseID(local.getDefendantOnCaseId());
		bv.setHearingID(local.getHearingId());
		bv.setHearingDateFreetext1(local.getHearingDateFreeTxt1());
		bv.setHearingDateFreetext2(local.getHearingDateFreeTxt2());
		bv.setHearingDateFreetext3(local.getHearingDateFreeTxt3());

		bv.setHearingStartDate(local.getHearingStartDate());
		bv.setHearingEndDate(local.getHearingEndDate());
		bv.setLastCalculatedDuration(local.getLastCalculatedDuration());
		bv.setMpHearingType(local.getMpHearingType());

		/*
		 * C.Kudzin - ctx-2050 adding trial and sentence in defendant absence
		 */
		bv.setTrialInDefAbsence(local.getTrialInDefAbsence());
		bv.setSentenceInDefAbsence(local.getSentenceInDefAbsence());

		bv.setFormAStatus(local.getFormAStatus());
		bv.setFormACourtClerk(local.getFormACourtClerk());
		
		bv.setS41Application(local.getS41Application());
		bv.setS41Granted(local.getS41Granted());
		bv.setS41ApplicationMade(local.getS41ApplicationMade());

		log.debug(methodName + " finished ");
	}

	/**
	 * Private helper method used to convert a <code>java.util.Date</code>
	 * Object into a <code>java.sql.Timestamp</code> object.
	 * 
	 * @param date
	 *            The <code>java.util.Date</code> object to convert
	 * @return The resultant <code>java.sql.Timestamp</code>, or returns
	 *         <i>null</i> if the passed in <code>java.util.Date</code> is
	 *         <i>null</i>
	 */
	private Timestamp convertDateToTimestamp(java.util.Date date) {
		return ((date != null) ? new Timestamp(date.getTime()) : null);
	}
}