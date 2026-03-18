package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;


import java.util.Date;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCalendarHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacade;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacadeFactory;

/**
 * <p>
 * Title: HearingRecordExportHelper
 * </p>
 * <p>
 * Description: This is the helper class for the Export of the hearing records.
 * A single hearing can be exported as well as linked hearings can be exported.
 * 
 * This class will: - Validate so all hearings (linked or single) has been ended
 * before the export starts. - Check if the hearing (linked or single) has
 * previosuly been exported or is in a state that the hearing should not be
 * exported. - Create a new entry in ExportA if it hasn't been exported - Update
 * an existing entry in ExportA if it has been triggered for export previously
 * but failed. - Make a call to the Integration tier to trigger the export to
 * CREST. This is a synchronous call so this method call will wait for response
 * back from the Integration tier.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author: Marie Holmberg
 * @version 1.0
 */

public class HearingRecordExportHelper {

	private static final Logger log = CSServices.getLogger(HearingRecordExportHelper.class);

	private HearingRecordValidationHelper hearingRecordValidationHelper;

	private DirectionsForCaseMaintainer directionsForCaseMaintainer;

	public HearingRecordExportHelper() {
		hearingRecordValidationHelper = new HearingRecordValidationHelper();		
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
	}

	/**
	 * This will validate all hearings that are passed in. The arguments will be a
	 * collection of hearing ids and the name of the court clerk exporter. The
	 * collection of hearing ids is the hearings that need to be exported and
	 * they should all be linked.
	 * 
	 * @param allHearingIDs
	 *            - Collection of hearing ids (Integers).
	 * @param courtClerkName
	 *            - the name of the person who perform the export - String
	 * @return Boolean
	 * @throws HearingRecordExportException
	 * @throws HearingRecordException
	 */
	public Boolean validateHearingRecord(Integer defendantOnCaseId, Integer hearingId, String courtClerkName, String userDisplayName)
			throws HearingRecordExportException, HearingRecordException {
		log.debug("HearingRecordExportHelper.validateHearingRecord(Integer "
				+ "defendantOnCaseId, String courtClerkName) called");

		log.debug("CourtClerkName >>>>>>>>>>> : " + courtClerkName);

		// returns this - left here so has minimal code change
		Boolean success = false;

		// 1. Validate so that the hearing has been completed/ended else.
		// this will also check if the case is of the right type.
		log.debug("validate hearing and case");
		try {
			// Also - get the last hearing in the Collection. It is enough
			// to get the first one since they are all linked but might have been ended at
			// different times which is  the reason to pass them all in.
			log.debug("Get the hearingvalue");
			
			// new for CTX - no longer export from xhibit to crest.
			// Therefore only check the business rules now.
			this.validateHearingAndCase(defendantOnCaseId, hearingId);

			success = true;
			
		} catch (HearingRecordHearingNotEndedException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordExportException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		
		return success;
	}

	/**
	 * Returns an instance of the IntegrationFacade
	 * 
	 * @return IntegrationFacade
	 */
	protected IntegrationFacade getIntegrationFacade() {
		log.debug("HearingRecordExportHelper.getIntegrationFacade() called");
		return IntegrationFacadeFactory.getInstance().getIntegrationFacade();
	}

	/**
	 * This will loop through all the passed in hearings and make sure that they
	 * are all ended. If they are not ended a
	 * HearingRecordHearingNotEndedException will be thrown. Linked hearings can
	 * be ended at different times which is the reason to check so that all
	 * hearings have been completed before export can be done.
	 * 
	 * @param aAllHearingIDs
	 *            - Array of all the hearing IDs
	 * @return HearingBasicValue
	 * @throws HearingRecordHearingNotEndedException
	 * @throws HearingRecordException
	 * @throws FormAException
	 */
	private void validateHearingAndCase(Integer defendantOnCaseId, Integer hearingId)
			throws HearingRecordHearingNotEndedException, HearingRecordException, FormAException {
		log.debug("HearingRecordExportHelper.validateHearingEnded(ArrayList aAllHearingIDs) called");

		Boolean isCaseTypesValid = null;
		HearingBasicValue hearingBV = null;
		Hearing hearing = null;

		log.debug("validate so that all hearings have been ended");

		hearing = getHearing(hearingId);
		//hearing.
		hearingBV = HearingMaintainer.getInstance().getHearingBasicValue(hearing);

			
		// check if the case is of the right type
		isCaseTypesValid = hearingRecordValidationHelper.validateCaseTypes(hearingBV.getCaseID());

		// check the defence representation - must have a category if it
		// is a barrister
		hearingRecordValidationHelper.validateHearingRepresentation(hearing, defendantOnCaseId);

		if (isCaseTypesValid.booleanValue() == false) {
			throw new FormAException(HearingRecordConstants.EXPORT_INVALID_CASE_EXC,
					"The case type is not valid for export.");
		}
		
		DefHearingRecord defHearingRecord;
    	DefHearingRecordMaintainer defHearingRecordMaintainer = DefHearingRecordMaintainer.getInstance();
				
		try {
			defHearingRecord = defHearingRecordMaintainer
						.findByDefendantOnCaseIDAndHearingID(defendantOnCaseId, hearingId);
			if(defHearingRecord.getHearingEndDate() == null){
					throw new HearingRecordHearingNotEndedException(HearingRecordConstants.HEARING_NOT_ENDED_EXC,
							"The hearing must be ended before exporting it");				
			}			
			
		} catch (ObjectNotFoundException e1) {
			throw new FormAException("No Defendant Hearing Record found that matches.",
					"No Defendant Hearing Record found that matches");
		}
				
		XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);

		if (doc != null) {
							
			//////////////////////start of added new business rules.///////////////////
			/* ctx-2039 kudzinc - MP Hearing validation */
			if (defHearingRecord.getMpHearingType() == null) {
				throw new FormAException(HearingRecordConstants.NO_MP_HEARING_TYPE, "No MP Hearing type set");
			}

			/* ctx-2039 kudzinc - Adjourned Date validation */
			if ((defHearingRecord.getAdjournedDate() != null)
					&& (defHearingRecord.getHearingEndDate() != null)) {
				Date adjDate = defHearingRecord.getAdjournedDate();
				Date hEDate = defHearingRecord.getHearingEndDate();
					if (!(adjDate.after(hEDate))) {
						throw new FormAException(HearingRecordConstants.INVALID_ADJOURNED_DATE_EXC,
							"The given adjournedDate:  " + adjDate + " must be after the last hearing date: " + hEDate);
					}
			}
			
			/* ctx-2039 kudzinc - Hearing start date validation */
			Date date = new Date();
			if (defHearingRecord.getHearingStartDate() != null) {
				if (defHearingRecord.getHearingStartDate().after(date)) {
					throw new FormAException(HearingRecordConstants.HEARING_START_DATE_IN_FUTURE,
							"Hearing Start Date in future");
				}
				if (defHearingRecord.getHearingEndDate() != null) {
					if (defHearingRecord.getHearingStartDate()
							.after(defHearingRecord.getHearingEndDate())) {
						throw new FormAException(HearingRecordConstants.HEARING_START_DATE_AFTER_END_DATE,
								"Hearing Start Date after End date");
					}
				}
			}

			if (defHearingRecord.getHearingStartDate() != null) {
				Date day = defHearingRecord.getHearingStartDate();
				RefCalendarHelper calHelper = new RefCalendarHelper();
				try {
					boolean courtCalDay = calHelper
							.isCourtAvailableOnDate(hearingBV.getCourtID(), day);
					if (courtCalDay == false) {
						throw new FormAException(HearingRecordConstants.HEARING_START_DATE_VALID_COURT_DATE,
								"Hearing Start Date is not a valid court sitting date");
					}
				} catch (ObjectNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			/* ctx-2039 kudzinc - Hearing end date validation */
			if (defHearingRecord.getHearingEndDate() != null) {
				if (defHearingRecord.getHearingEndDate().after(date)) {
					throw new FormAException(HearingRecordConstants.HEARING_END_DATE_IN_FUTURE,
							"Hearing End Date in future");
				}
			}

			if (defHearingRecord.getHearingEndDate() != null) {
				Date day = defHearingRecord.getHearingEndDate();
				RefCalendarHelper calHelper = new RefCalendarHelper();
				try {
					boolean courtCalDay = calHelper
							.isCourtAvailableOnDate(hearingBV.getCourtID(), day);
					if (courtCalDay == false) {
						throw new FormAException(HearingRecordConstants.HEARING_END_DATE_VALID_COURT_DATE,
								"Hearing End Date is not a valid court sitting date");
					}
				} catch (ObjectNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			/* ctx-2039 kudzinc - further validation */
			if (defHearingRecord.getMpHearingType().equals("P")
					&& defHearingRecord.getStartDateNewBailStatus() != null) {
				Date SDNBS = defHearingRecord.getStartDateNewBailStatus();
				Date SH = hearingBV.getHearingStartDate();
				Date EH = hearingBV.getHearingEndDate();
				if (SDNBS.after(EH) || SDNBS.before(SH)) {
					/* if date is not between these two dates, throw error */
					throw new FormAException(HearingRecordConstants.HEARING_NEW_BAIL_DATE_HEARING_START_END_DATE,
							"Hearing adjourned date must be between hearing start and end date");
				}
			}

			if (defHearingRecord.getRefDefHearingTypeId() != null) {
				RefSystemCodeMaintainer refSystemCodeMaintainer = new RefSystemCodeMaintainer();
				try {
					RefSystemCode code = refSystemCodeMaintainer
							.findByPrimaryKey(defHearingRecord.getRefDefHearingTypeId());

					if ("PAD".equals(code.getCode())) {
						Integer timeEstimate = null;
						DirectionsForCase  directionCase = directionsForCaseMaintainer.findByCaseId(hearingBV.getCaseID());
						if(directionCase != null && directionCase.getTrialTimeEstimate() != null){
							timeEstimate = Math.round(directionCase.getTrialTimeEstimate());
						}
						if (timeEstimate == null || timeEstimate <= 0) {
							throw new FinderException();
						}
					}
				} 
				catch (FinderException e) {
					throw new FormAException(HearingRecordConstants.HEARING_ESTIMATED_TRIAL_LENGTH,
							"Estimated Trial length cant be 0");
				} 
			
			}		
			
			//////////////////////end of added new business rules.///////////////////
			
			final boolean nonObsoleteDefendant = (doc.getObsInd() == null || !doc.getObsInd().equals("Y"));
			
			if (nonObsoleteDefendant && (defHearingRecord.getLastCalculatedDuration() == null
					|| defHearingRecord.getLastCalculatedDuration().intValue() == 0)) {
				throw new HearingRecordException(HearingRecordConstants.EXPORT_INVALID_NO_CALCULATED_DURATION,
						"A DefHearingRecord does not have a last calculated duration set.");
			}

			if (nonObsoleteDefendant && defHearingRecord.getMpHearingType() == null) {
				throw new HearingRecordException(HearingRecordConstants.EXPORT_INVALID_NO_DEF_HEARING_TYPE,
						"A DefHearingRecord does not have a hearing type set");
			}

			// QC 6717 - Legal Aid Release 8.6 - Summer 2013
			if (nonObsoleteDefendant && defHearingRecord.getIsAdjourned() == null) {
				throw new HearingRecordException(HearingRecordConstants.EXPORT_INVALID_NO_CASE_ADJOURNED,
						"A DefHearingRecord does not have the adjourned flag set");
			}
		}
		
		log.debug("HearingRecordExportHelper.validateHearingEnded(Vector vAllHearingIDs) exit");

	}

	/**
	 * This will return the HearingBasicValue for a hearingID
	 * 
	 * @param hearingID
	 *            Integer
	 * @return HearingBasicValue
	 * @throws HearingRecordException
	 */
	private Hearing getHearing(Integer hearingID) throws HearingRecordException {
		log.debug("HearingRecordExportHelper.getHearing(Integer hearingID) called");

		Hearing hearing = null;

		try {
			log.debug(">>>>> Hearing id passed in for checking so ended >>>>>>>> :" + hearingID.toString());

			hearing = HearingMaintainer.getInstance().findByPK(hearingID);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			HearingRecordExportException hex = new HearingRecordExportException("", ex.getMessage(), ex);
			throw hex;
		}
		log.debug("HearingRecordExportHelper.getHearing(Integer hearingID) exit");
		return hearing;
	}

}