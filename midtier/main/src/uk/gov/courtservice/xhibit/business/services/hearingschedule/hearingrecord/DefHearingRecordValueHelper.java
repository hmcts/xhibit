package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;

/**
 * <p>
 * Title: DefHearingRecordValueHelper
 * </p>
 * <p>
 * Description: Helper class to transform the DefHearingRecordValue to/from
 * DefHearingRecordBasicValue and DefHearingRecordComplexValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class DefHearingRecordValueHelper implements HRBasicValueHelper {
	// logger
	private static final Logger log = CSServices.getLogger(DefHearingRecordValueHelper.class);

	private DefHearingRecordMaintainer maintainer = null;

	private BisRefControllerLocal bisRefController;

	/**
	 * Default constructor
	 */
	public DefHearingRecordValueHelper() {
		maintainer = new DefHearingRecordMaintainer();
		bisRefController = (BisRefControllerLocal) CSServices.getEJBServices()
				.createLocalSession(BisRefControllerLocalHome.class);
	}

	/**
	 * Build a DefHearingRecordBasicValue from a DefHearingRecordValue
	 * 
	 * @param defHearingRecordValue
	 *            DefHearingRecordValue
	 * @return DefHearingRecordBasicValue
	 * @throws HearingRecordException
	 */
	public DefHearingRecordBasicValue buildBasicValue(DefHearingRecordValue defHearingRecordValue) {
		log.debug(
				"DefHearingRecordValueHelper.buildBasicValue(" + "DefHearingRecordValue defHearingRecordValue) called");
		log.debug(">>>>>>>>>>>>>> DefHearingRecordValue before : " + defHearingRecordValue.toString());

		DefHearingRecordBasicValue defHearingRecordBasic = new DefHearingRecordBasicValue(defHearingRecordValue.getId(),
				defHearingRecordValue.getVersion());

		defHearingRecordBasic.setHearingID(defHearingRecordValue.getHearingID());
		defHearingRecordBasic.setStartBailStatus(defHearingRecordValue.getStartBailStatus());
		defHearingRecordBasic.setEndBailStatus(defHearingRecordValue.getEndBailStatus());
		defHearingRecordBasic.setHearingDateFreetext1(defHearingRecordValue.getHearingDateFreetext1());
		defHearingRecordBasic.setHearingDateFreetext2(defHearingRecordValue.getHearingDateFreetext2());
		defHearingRecordBasic.setHearingDateFreetext3(defHearingRecordValue.getHearingDateFreetext3());
		defHearingRecordBasic.setDefendantOnCaseID(defHearingRecordValue.getDefendantOnCaseID());
		defHearingRecordBasic.setRefDefHearingTypeID(defHearingRecordValue.getRefDefHearingTypeID());
		defHearingRecordBasic.setIsHraApplication(defHearingRecordValue.getIsHraApplication());
		defHearingRecordBasic.setResultBailApplication(defHearingRecordValue.getResultBailApplication());
		defHearingRecordBasic.setOralEvidence(defHearingRecordValue.getOralEvidence());
		defHearingRecordBasic.setSubstBailApplication(defHearingRecordValue.getSubstBailApplication());
		defHearingRecordBasic.setDateBailApplication(defHearingRecordValue.getDateBailApplication());
		defHearingRecordBasic.setNewBailStatus(defHearingRecordValue.getNewBailStatus());
		defHearingRecordBasic.setStartDateNewBailStatus(defHearingRecordValue.getStartDateNewBailStatus());
		defHearingRecordBasic.setIsAdjourned(defHearingRecordValue.getIsAdjourned());
		defHearingRecordBasic.setAdjournedDate(defHearingRecordValue.getAdjournedDate());
		defHearingRecordBasic.setRefAdjournmentID(defHearingRecordValue.getRefAdjournmentID());

		// end hearing changes, require end hearing to be defendant level...
		defHearingRecordBasic.setHearingStartDate(defHearingRecordValue.getHearingStartDate());
		defHearingRecordBasic.setHearingEndDate(defHearingRecordValue.getHearingEndDate());
		defHearingRecordBasic.setLastCalculatedDuration(defHearingRecordValue.getLastCalculatedDuration());
		defHearingRecordBasic.setMpHearingType(defHearingRecordValue.getMpHearingType());

		/* C.Kudzin - ctx-2050 adding trial and sentence in defendant absence */
		defHearingRecordBasic.setTrialInDefAbsence(defHearingRecordValue.getTrialInDefAbsence());
		defHearingRecordBasic.setSentenceInDefAbsence(defHearingRecordValue.getSentenceInDefAbsence());
		defHearingRecordBasic.setFormAStatus(defHearingRecordValue.getFormAStatus());
		defHearingRecordBasic.setFormACourtClerk(defHearingRecordValue.getFormACourtClerk());
		
		defHearingRecordBasic.setS41Application(defHearingRecordValue.getS41Application());
		defHearingRecordBasic.setS41Granted(defHearingRecordValue.getS41Granted());
		defHearingRecordBasic.setS41ApplicationMade(defHearingRecordValue.getS41ApplicationMade());

		log.debug(">>>>>>>>>>>>>> DefHearingRecordBasicValue after : " + defHearingRecordBasic.toString());
		log.debug("DefHearingRecordValueHelper.buildBasicValue("
				+ "DefHearingRecordValue defHearingRecordValue) finsihed");

		return defHearingRecordBasic;
	}

	/**
	 * Looks up a RefSystemCode of for the defHearingTypeID.
	 * 
	 * @param id
	 *            - the primary key for the defHearingType
	 * @param code
	 *            boolean is true when code should be returned else false and
	 *            the decode will be returned.
	 * @return String - the description
	 * @throws HearingRecordException
	 */
	private String getRefSystemCodeDesc(Integer id, boolean code) throws HearingRecordException {
		log.debug("getRefSystemCodeDesc() called for id: " + id);
		String desc = null;
		RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
		criteria.setPrimaryKey(id);
		try {
			Collection refCode = bisRefController.findSystemCodes(criteria);

			// since we are search for the primary key value we know it will
			// only be
			// one value in the collection
			if (refCode != null && refCode.size() > 0) {
				Iterator it = refCode.iterator();
				RefSystemCodeBasicValue value = (RefSystemCodeBasicValue) it.next();

				// return the code or the decode.
				desc = ((code) ? value.getCode() : value.getDecode());
			}
		} catch (BisRefControllerException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw new HearingRecordException(ex.getUserMessage(), "Failed to find the DefHearingType for id " + id, ex);
		}
		log.debug("getRefSystemCodeDesc() finished");
		return desc;
	}

	/**
	 * This will find a DefHearingRecord for a specific hearing and defendant On
	 * a case.
	 * 
	 * @param defOnCaseID
	 *            Integer
	 * @param hearingID
	 *            Integer
	 * @return DefHearingRecordValue
	 * @throws HearingRecordException
	 */
	public DefHearingRecordValue getDefHearingRecordValue(Integer defOnCaseID, Integer hearingID)
			throws HearingRecordException {

		log.debug("DefHearingRecordValueHelper.getDefHearingRecordValue(Integer hearingID, "
				+ "Integer defOnCaseID) called");

		DefHearingRecordValue hrValue = null;
		try {
			// get local DefHearingRecordValue from maintainer,
			// then use this to get the DefHearingRecordBasicValue from
			// maintainer
			DefHearingRecord defHearingRecord = maintainer.findByDefendantOnCaseIDAndHearingID(defOnCaseID, hearingID);
			DefHearingRecordBasicValue basicValue = maintainer.getDefHearingRecordBasicValue(defHearingRecord);

			/*
			 * ctx-2047 - kudzinc Find previous FormA's for that
			 * defendantOnCaseId where the hearingId is different to current
			 * hearingId, check Main Hearing and then on the current hearing,
			 * set HearingType to Primary.
			 */
			Boolean oldMpHearingType = false;
			Boolean oldRecordExist = false;
			Collection oldRecords = maintainer.findByDefendantOnCaseID(defOnCaseID);
			Iterator it = oldRecords.iterator();

			while (it.hasNext()) {
				DefHearingRecord oldDefHearingRecord = (DefHearingRecord) it.next();
				if (oldDefHearingRecord.getHearingRecordId().intValue() != basicValue.getId().intValue()) {
					oldRecordExist = true;
					if (("M").equals(oldDefHearingRecord.getMpHearingType())) {
						oldMpHearingType = true;
					}
				}
			}

			// create new DefHearingRecordValue using id and version of
			// basic value
			// then populate fields from basic value into new value object
			hrValue = new DefHearingRecordValue(basicValue.getId(), basicValue.getVersion());
			hrValue.setHearingID(basicValue.getHearingID());
			hrValue.setStartBailStatus(basicValue.getStartBailStatus());
			hrValue.setEndBailStatus(basicValue.getEndBailStatus());
			hrValue.setHearingDateFreetext1(basicValue.getHearingDateFreetext1());
			hrValue.setHearingDateFreetext2(basicValue.getHearingDateFreetext2());
			hrValue.setHearingDateFreetext3(basicValue.getHearingDateFreetext3());
			hrValue.setDefendantOnCaseID(basicValue.getDefendantOnCaseID());
			hrValue.setRefDefHearingTypeID(basicValue.getRefDefHearingTypeID());
			hrValue.setIsHraApplication(basicValue.getIsHraApplication());
			hrValue.setResultBailApplication(basicValue.getResultBailApplication());
			hrValue.setOralEvidence(basicValue.getOralEvidence());
			hrValue.setSubstBailApplication(basicValue.getSubstBailApplication());
			hrValue.setDateBailApplication(basicValue.getDateBailApplication());
			hrValue.setNewBailStatus(basicValue.getNewBailStatus());
			hrValue.setStartDateNewBailStatus(basicValue.getStartDateNewBailStatus());
			hrValue.setIsAdjourned(basicValue.getIsAdjourned());
			hrValue.setAdjournedDate(basicValue.getAdjournedDate());
			hrValue.setRefAdjournmentID(basicValue.getRefAdjournmentID());

			// end hearing changes, require end hearing to be defendant
			// level...
			hrValue.setHearingStartDate(basicValue.getHearingStartDate());
			hrValue.setHearingEndDate(basicValue.getHearingEndDate());
			hrValue.setLastCalculatedDuration(basicValue.getLastCalculatedDuration());

			if (basicValue.getMpHearingType() == null && oldMpHearingType) {
				hrValue.setMpHearingType("M");
				hrValue.setDefaultHearingType(true);
			} else if (basicValue.getMpHearingType() == null && !oldRecordExist) {
				hrValue.setMpHearingType("P");
				hrValue.setDefaultHearingType(true);
			} else {
				hrValue.setMpHearingType(basicValue.getMpHearingType());
			}

			/*
			 * C.Kudzin - ctx-2050 adding trial and sentence in defendant
			 * absence
			 */
			hrValue.setTrialInDefAbsence(basicValue.getTrialInDefAbsence());
			hrValue.setSentenceInDefAbsence(basicValue.getSentenceInDefAbsence());

			hrValue.setFormAStatus(basicValue.getFormAStatus());
			hrValue.setFormACourtClerk(basicValue.getFormACourtClerk());
			
			hrValue.setS41Application(basicValue.getS41Application());
			hrValue.setS41Granted(basicValue.getS41Granted());
			hrValue.setS41ApplicationMade(basicValue.getS41ApplicationMade());

			// JF and MH added
			// set the reference data desc for defHearingtype desc
			if (basicValue.getRefDefHearingTypeID() != null) {
				log.debug("Will try to find the defhearing type description for id : "
						+ basicValue.getRefDefHearingTypeID());

				hrValue.setRefDefHearingTypeDesc(this.getRefSystemCodeDesc(basicValue.getRefDefHearingTypeID(), true));
			}

			// find the reference data desc for adjournment
			if (basicValue.getRefAdjournmentID() != null) {
				log.debug("Will try to find the adjourn description for id : " + basicValue.getRefAdjournmentID());
				hrValue.setRefAdjournmentDesc(this.getRefSystemCodeDesc(basicValue.getRefAdjournmentID(), false));
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException("", ex.getMessage(), ex);
		}
		log.debug("DefHearingRecordValueHelper.getDefHearingRecordValue(Integer hearingID, "
				+ "Integer defOnCaseID) finished");
		return hrValue;
	}

	// ----------------------- Private Methods
	// ------------------------------------

	/*
	 * 
	 * This will find a specific DefHearingRecord entity and return a.
	 * 
	 * @param defHearingRecordID Integer @return DefHearingRecordBasicValue
	 * 
	 * @throws HearingRecordException
	 * 
	 * private DefHearingRecord findDefHearingRecord(Integer defHearingRecordID)
	 * throws HearingRecordException {
	 * log.debug("DefHearingRecordValueHelper.findDefHearingRecord("+ "Integer
	 * defHearingRecordID) called");
	 * 
	 * DefHearingRecord defHearingRecordEntity = null;
	 * 
	 * try { defHearingRecordEntity =
	 * maintainer.findByPrimaryKey(defHearingRecordID); }
	 * catch(ObjectNotFoundException ex) {
	 * CSServices.getDefaultErrorHandler().handleError(ex, getClass(),
	 * ex.toString()); HearingRecordException hex = new HearingRecordException(
	 * "", ex.getMessage(), ex); throw hex; }
	 * 
	 * log.debug("DefHearingRecordValueHelper.findDefHearingRecord("+ "Integer
	 * defHearingRecordID) finished"); return defHearingRecordEntity; }
	 */
}
