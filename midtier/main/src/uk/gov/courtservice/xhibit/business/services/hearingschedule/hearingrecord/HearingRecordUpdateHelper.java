package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.services.directions.DirectionsHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;

/**
 * <p>
 * Title: HearingRecordUpdateHelper
 * </p>
 * <p>
 * Description: This is the helper class for the updates of all hearing record
 * related information.
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
public class HearingRecordUpdateHelper {

	// logger
	private static final Logger log = CSServices.getLogger(HearingRecordUpdateHelper.class);

	// Helpers
	private HearingRecordValidationHelper validationHelper;

	private HearingRecordDateHelper dateHelper;

	// BisRefController
	private BisRefControllerLocal bisRefControllerLocal;

	/**
	 * Default constructor that instantiate: - HearingRecordValidationHelper -
	 * HearingRecordDateHelper
	 */
	public HearingRecordUpdateHelper() {		
		validationHelper = new HearingRecordValidationHelper();
		dateHelper = new HearingRecordDateHelper();
		bisRefControllerLocal = (BisRefControllerLocal) CSServices.getEJBServices()
				.createLocalSession(BisRefControllerLocalHome.class);
	}

	/**
	 * This method will update a hearing record and other related Hearing Record
	 * information The components that will be updated are: - Hearing -
	 * DirectionsForCase (estimated trial time) - DefHearingRecord - SHJudge -
	 * SHLegRep
	 * 
	 * @param String 
	 *            caseType
	 * @param HearingRecordUpdateValue
	 *            hearingRecordUpdateValue
	 * @throws HearingRecordException
	 */
	public void saveFormADetails(String caseType, DefHearingRecordValue defHearingRecordValue, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordUpdateHelper.saveFormADetails() called");
		
		try {
			this.validateAndUpdateDefHearingRecord(defHearingRecordValue, caseType, userDisplayName);
		} catch (FormAException ex) {
			throw ex;
		} catch (HearingRecordException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		
		log.debug("HearingRecordUpdateHelper.saveFormADetails() OK");
	}
	
	/**
	 * This method will update a hearing record and other related Hearing Record
	 * information The components that will be updated are: - Hearing -
	 * DirectionsForCase (estimated trial time) - DefHearingRecord - SHJudge -
	 * SHLegRep
	 * 
	 * @param hearingRecordUpdateValue
	 *            HearingRecordUpdateValue
	 * @throws HearingRecordException
	 */
	public void updateHearingRecord(HearingRecordUpdateValue hearingRecordUpdateValue, DefendantOnCaseBasicValue defOnCase, String currentPrisonStatus, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordUpdateHelper.updateHearingRecord() called");
		// Moved business validation to be within the 'old' export functionality which is now reused
							
		// validate and update the hearing, return the hearing basic value.
		this.validateAndUpdateHearing(hearingRecordUpdateValue.getHearingBasicValue(), userDisplayName);

		// validate and update the DirectionsForCase and fire court log event if
		// required
		this.validateAndUpdateDirectionsForCase(hearingRecordUpdateValue.getDirectionsForCaseValue(),
				hearingRecordUpdateValue.getDefHearingRecordValue().getRefDefHearingTypeID());

		// validate and update the defHearingRecord
		try {
			this.validateAndUpdateDefHearingRecord(hearingRecordUpdateValue.getDefHearingRecordValue(),
					hearingRecordUpdateValue.getCaseType(), userDisplayName);
		} catch (FormAException ex) {
			throw ex;
		} catch (HearingRecordException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}

		// validate and update the judge
		this.validateAndUpdateSHJudge(hearingRecordUpdateValue.getHrSHJudgeValue(), userDisplayName);

		// validate and update the SHLegRep
		this.validateAndUpdateSHLegRep(hearingRecordUpdateValue.getHrSHLegRepValues(), userDisplayName);

		// validate and update the DefendantOnCase
		this.validateAndUpdateDefendantOnCase(hearingRecordUpdateValue.getDefHearingRecordValue(), defOnCase, userDisplayName);
		
		this.validateAndUpdateDefendant(defOnCase.getDefendantID(), currentPrisonStatus, userDisplayName); 
		
		log.debug("HearingRecordUpdateHelper.updateHearingRecord() OK");
	}

	private void validateAndUpdateDefendant(Integer defendantID, String currentPrisonStatus,
			String userDisplayName) throws HearingRecordException {
		if(defendantID != null) {
			DefendantMaintainer defMain = new DefendantMaintainer();
			try {
				defMain.updateFormA(defendantID, currentPrisonStatus, userDisplayName);
			} catch (ObjectNotFoundException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
				throw new HearingRecordException("", ex.getMessage(), ex);
			}
		}
		
	}

	private void validateAndUpdateDefendantOnCase(DefHearingRecordValue defHearingRecordValue, DefendantOnCaseBasicValue defOnCase, String userDisplayName) throws HearingRecordException {
		final Integer defendantOnCaseId = defHearingRecordValue.getDefendantOnCaseID();
		if (defendantOnCaseId != null) {
			final String bcStatus = defHearingRecordValue.getEndBailStatus();
			if (bcStatus != null) {
				DefendantOnCaseHelper defendantOnCaseHelper = new DefendantOnCaseHelper();
				try {
					defendantOnCaseHelper.updateFormA(defendantOnCaseId, defOnCase, bcStatus, userDisplayName);
				} catch (ObjectNotFoundException ex) {
					CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
					throw new HearingRecordException("", ex.getMessage(), ex);
				}
			}
		}
	}
	
	/**
	 * When updating the estimate trial time the def hearing type has to be =
	 * PAD. When setting the adjourned reason to DS then there has to be a date.
	 * <p/>
	 * This will search for the refsystemcode by primary key. The refsystem code
	 * will be returned
	 * 
	 * @param id
	 *            Integer the primary key for the refsystem codes.
	 * @return refCode String - the code from the refsystem.
	 * @throws HearingRecordException
	 */
	private String getRefSystemCode(Integer id) throws HearingRecordException {
		String refCode = null;
		log.debug("HearingRecordUpdateHelper.getRefSystemCode() called");

		if (id == null) {
			return refCode;
		}
		RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
		criteria.setPrimaryKey(id);

		try {
			Collection col = bisRefControllerLocal.findSystemCodes(criteria);

			// get only the first one since searching for primary key
			if (col != null && col.size() > 0) {
				// get the code.
				Iterator it = col.iterator();
				RefSystemCodeBasicValue refBasic = (RefSystemCodeBasicValue) it.next();
				refCode = refBasic.getCode();
			}
		} catch (BisRefControllerException e) {
			// could not find the ref hearing type with for primary key
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new HearingRecordException(e.getUserMessage(), e.getMessage(), e);
		}
		log.debug("HearingRecordUpdateHelper.getRefSystemCode() exit");
		return refCode;
	}

	/**
	 * This will create and validate the CRUD value for direction for case. It
	 * is the estimated trial time that will be updated.
	 * 
	 * @param directionsForCaseValue
	 *            DirectionsForCaseValue
	 * @param refDefHrgTypeId
	 *            Integer
	 * @throws HearingRecordException
	 */
	private void validateAndUpdateDirectionsForCase(DirectionsForCaseValue directionsForCaseValue,
			Integer refDefHrgTypeId) throws HearingRecordException {
		log.debug("HearingRecordUpdateHelper.validateAndUpdateDirectionsForCase() called");

		// check we have a value to pass that will create a court log event and
		// an entry in the directionsforcase table.
		if (directionsForCaseValue != null && directionsForCaseValue.getCourtLogCRUDValues() != null
				&& directionsForCaseValue.getCourtLogCRUDValues().length > 0) {
			// validate the defhearingtype
			this.validateDefHearingType(refDefHrgTypeId);

			// ST - Replaced the logic which was here with a call to the
			// DirectionsHelper
			// so the logic to deal with this data is all in one place
			// wrap the DirectionsForCaseValue in a DirectionsValue to pass
			DirectionsValue directionsValue = new DirectionsValue();
			directionsValue.setDirectionsForCaseValue(directionsForCaseValue);
			// pass to the DirectionsHelper to deal with
			try {
				DirectionsHelper.saveDirections(directionsValue);
			} catch (CourtLogBusinessException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, getClass());
				log.fatal(ex);

				if (ex.getUserMessageAsMessage().getParameters().length > 0) {
					throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(),
							ex.getUserMessageAsMessage().getParameters(), ex.getMessage());
				}

				throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
			}
		}
		// if we already have a directionforcase value but have changed the
		// defHearingType we need to validate so that the defHearingType is
		// still valid.
		// else if(directionsForCaseValue != null &&
		// directionsForCaseValue.getDirectionsForCaseBasicValue() != null &&
		// directionsForCaseValue.getDirectionsForCaseBasicValue().getId() !=
		// null &&
		// directionsForCaseValue.getDirectionsForCaseBasicValue().getTrialTimeEstimate()
		// != null)
		// {
		// //validate the defhearingtype
		// this.validateDefHearingType(refDefHrgTypeId);
		// }
		log.debug("HearingRecordUpdateHelper.validateAndUpdateDirectionsForCase() finished");
	}

	/**
	 * This will validate the defHearingType passed in. It has to be of a
	 * certain type for updateing the trial estimate time.
	 * 
	 * @param refDefHrgTypeId
	 *            Integer
	 * @throws HearingRecordException
	 */
	private void validateDefHearingType(Integer refDefHrgTypeId) throws HearingRecordException {
		log.debug("validateDefHearingType() called");
		// check we're in the right hearing type
		if (refDefHrgTypeId != null) {
			String defHearingTypeDesc = this.getRefSystemCode(refDefHrgTypeId);

			if (defHearingTypeDesc != null
					&& (!defHearingTypeDesc.equalsIgnoreCase(HearingRecordConstants.REF_HEARING_CODE_P_AND_D))) {
				log.debug("The hearing type is not for P&D, will throw an exception");
				throw new HearingRecordException(HearingRecordConstants.ESTIMATED_TRIAL_TIME_FAILURE,
						"The hearing type code does not match the one for P&D");
			}
		}
		log.debug("validateDefHearingType() exited");
	}

	/**
	 * This method will first validate the hearing value and if successfull
	 * validation update the hearing.
	 * 
	 * @param hrHearingValue
	 *            HRHearingValue
	 * @return HearingBasicValue
	 * @throws HearingRecordException
	 * @throws FormAException
	 * 
	 */
	private HearingBasicValue validateAndUpdateHearing(HearingBasicValue hearingBasicValue, String userDisplayName)
			throws HearingRecordException, FormAException {
		log.debug("HearingRecordUpdateHelper.validateAndUpdateHearing(HRHearingValue) called");
		log.debug("Start to validating and updating hearing values");

		// Just return if hearingValue is null
		if (hearingBasicValue == null) {
			return null;
		}

		HearingMaintainer hearingMaintainer = null;

		try {
			// update the hearingValue
			hearingMaintainer = new HearingMaintainer();
			log.debug("Will try to update the hearingBasicValue : " + hearingBasicValue.toString());
			hearingMaintainer.update(hearingBasicValue, userDisplayName);
			log.debug("Updated successfully!");
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
			throw hex;
		}
		// get a date helper that will store the first and the last date of the
		// scheduled hearings.
		dateHelper = validationHelper.setFirstAndLastHearingDates(hearingBasicValue.getId());

		log.debug("Finish validateAndUpdateHearing(HRHearingValue) OK");
		return hearingBasicValue;
	}

	/**
	 * Method to first validate the defHearingRecord and if all successful
	 * update the record.
	 * 
	 * @param defHearingRecordValue
	 *            DefHearingRecordValue
	 * @param caseType
	 *            String
	 * @throws HearingRecordException
	 * @throws FormAException
	 * 
	 * @throws HrBailStatusException
	 */
	private void validateAndUpdateDefHearingRecord(DefHearingRecordValue defHearingRecordValue, String caseType,
			String userDisplayName) throws HearingRecordException, FormAException {
		log.debug("HearingRecordUpdateHelper.validateAndUpdateDefHearingRecord(DefHearingRecordValue) called");
		log.debug("Start to validating and updating DefHearingRecord values");

		// Just return if the hearing record value is null.
		if (defHearingRecordValue == null) {
			return;
		}

		// Get the defHearingRecordValue and transform to basic value before
		// starting the
		// validation and updates.
		log.debug("The HRDefHearingObject before transform to DefHearingBasicValue : "
				+ defHearingRecordValue.toString());

		DefHearingRecordValueHelper defHearingRecordValueHelper = new DefHearingRecordValueHelper();

		DefHearingRecordBasicValue defHearingRecordBasicValue = defHearingRecordValueHelper
				.buildBasicValue(defHearingRecordValue);

		log.debug("The DefHearingRecordBasicValue after transformation from HRDefHearingValue : "
				+ defHearingRecordBasicValue.toString());

		// Validate the all bail statuses
		this.validateBailStatuses(defHearingRecordBasicValue, caseType);

		// validate applicationDate
		if (defHearingRecordBasicValue.getDateBailApplication() != null) {
			validationHelper.validateApplicationDate(dateHelper, defHearingRecordBasicValue.getDateBailApplication());
		}
		// validate if adjourn reason and the date has been populated.
		if (defHearingRecordBasicValue.getRefAdjournmentID() != null
				&& defHearingRecordBasicValue.getRefAdjournmentID().intValue() != 0) {

			// Search for the adjourned code for the adjourned reason given.
			String adjournedCode = this.getRefSystemCode(defHearingRecordBasicValue.getRefAdjournmentID());

			if (defHearingRecordBasicValue.getAdjournedDate() == null
					&& adjournedCode.equalsIgnoreCase(HearingRecordConstants.ADJOURNED_DS_CODE)) {
				throw new FormAException(HearingRecordConstants.NO_ADJOURNED_DATE_GIVEN_EXC,
						"An adjourn reason has been given but not an adjourned date");
			}
		}

		// try to update the defhearing record (and the directionsForCase: fix
		// for X12B062)
		try {
			DefHearingRecordMaintainer defHearingRecordMaintainer = new DefHearingRecordMaintainer();
			log.debug("Will try to update the DefHearingRecord with values : " + defHearingRecordBasicValue.toString());

			defHearingRecordMaintainer.update(defHearingRecordBasicValue, userDisplayName);
			log.debug("Update was succesfull!");
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
			throw hex;
		}
		log.debug("Finish validateAndUpdateDefHearingRecord(DefHearingRecordValue) OK");
	}

	/**
	 * Validate all the 3 different type of bail statuses : - start bail status
	 * - end bail status - new bail status
	 * 
	 * @param defHearingRecordBasicValue
	 *            DefHearingRecordBasicValue
	 * @param caseType
	 *            String
	 * @throws FormAException
	 */
	private void validateBailStatuses(DefHearingRecordBasicValue defHearingRecordBasicValue, String caseType)
			throws FormAException {
		Integer defendanOnCaseId = defHearingRecordBasicValue.getDefendantOnCaseID();

		// validate the end bail status
		if ((defHearingRecordBasicValue.getEndBailStatus() != null)
				&& (!defHearingRecordBasicValue.getEndBailStatus().equals(""))) {
			validationHelper.validateBailStatus(defHearingRecordBasicValue.getEndBailStatus());

			// validate the defendant and the end bail status so that right
			// values
			// are populated for different types of defendants.
			validationHelper.validateDefendantDataWithBailStatus(defendanOnCaseId,
					defHearingRecordBasicValue.getEndBailStatus(), caseType);
		}

		// validate the start status
		if ((defHearingRecordBasicValue.getStartBailStatus() != null)
				&& (!defHearingRecordBasicValue.getStartBailStatus().equals(""))) {
			validationHelper.validateBailStatus(defHearingRecordBasicValue.getStartBailStatus());

			// validate the defendant and the start bail status so that
			// right values
			// are populated for different types of defendants.
			validationHelper.validateDefendantDataWithBailStatus(defendanOnCaseId,
					defHearingRecordBasicValue.getStartBailStatus(), caseType);
		}

		// validate the new bail status
		if ((defHearingRecordBasicValue.getNewBailStatus() != null)
				&& (!defHearingRecordBasicValue.getNewBailStatus().equals(""))) {
			validationHelper.validateBailStatus(defHearingRecordBasicValue.getNewBailStatus());

			// validate the defendant and the new bail status so that right
			// values
			// are populated for different types of defendants.
			validationHelper.validateDefendantDataWithBailStatus(defendanOnCaseId,
					defHearingRecordBasicValue.getNewBailStatus(), caseType);
		}
	}

	/**
	 * This will validate and update a SHJudge.
	 * 
	 * @param hrSHJudgeValue
	 *            HRSHJudgeValue
	 * @throws HearingRecordException
	 */
	private void validateAndUpdateSHJudge(HRSHJudgeValue hrSHJudgeValue, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordUpdateHelper.validateAndUpdateSHJudge(HRSHJudgeValue) called");
		log.debug("Start to validating and updating JudgeValue values");

		if (hrSHJudgeValue != null && hrSHJudgeValue.getRefJudgeID() != null) {
			ShJudgeMaintainer shJudgeMaintainer = null;
			SHJudgeBasicValue shJudgeBasicValue = null;
			HRSHJudgeValueHelper hrSHJudgeValueHelper = null;

			// Get the SHJudgeBasicValue and transform to basic value before
			// starting the updates.
			hrSHJudgeValueHelper = new HRSHJudgeValueHelper();

			shJudgeBasicValue = hrSHJudgeValueHelper.buildBasicValue(hrSHJudgeValue);

			// if the record already exist then update else create a new
			// record.
			try {

				shJudgeMaintainer = new ShJudgeMaintainer();

				if (shJudgeBasicValue.getId() != null) {
					shJudgeMaintainer.update(shJudgeBasicValue, userDisplayName);
				} else {
					shJudgeMaintainer.create(shJudgeBasicValue, userDisplayName);
				}
			} catch (ObjectNotFoundException ex) {
				// The SHJudge could not be found for the update.
				CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
				HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
				throw hex;
			}
		}
		log.debug("Finish validateAndUpdateSHJudge(HRSHJudgeValue) OK");
	}

	/**
	 * This method will validate and update a collection of SHLegRep values.
	 * 
	 * @param hrSHLegRepValues
	 *            Collection
	 * @throws HearingRecordException
	 */
	private void validateAndUpdateSHLegRep(Collection hrSHLegRepValues, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordUpdateHelper.validateAndUpdateSHLegRep(Collection) called");
		log.debug("Start to validating and updating Legal Representative values");

		if (hrSHLegRepValues == null) {
			return;
		}
		// get the Value helper and maintainer.
		HRSHLegRepValueHelper hrSHLegRepValueHelper = new HRSHLegRepValueHelper();
		ShLegRepMaintainer shLegRepMaintainer = new ShLegRepMaintainer();
		Vector vShLegRepValues = (Vector) hrSHLegRepValues;

		for (int i = 0; i < vShLegRepValues.size(); i++) {
			// Get the HRSHLegRepValues and convert to a basic value before
			// update.
			// The conververion will get all the values incl. the ones that
			// are not
			// used in the HRSHLegRepValue, e.g. hearingstartDate etc.
			HRSHLegRepValue hrShValue = (HRSHLegRepValue) vShLegRepValues.elementAt(i);
			SHLegRepBasicValue basicValue = hrSHLegRepValueHelper.buildBasicValue(hrShValue);
			try {
				shLegRepMaintainer.update(basicValue, userDisplayName);
			} catch (ObjectNotFoundException ex) {
				CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
				HearingRecordException hex = new HearingRecordException("", ex.getMessage(), ex);
				throw hex;
			}
		}
		log.debug("HearingRecordUpdateHelper.validateAndUpdateSHLegRep(Collection) finished");
	}
}
