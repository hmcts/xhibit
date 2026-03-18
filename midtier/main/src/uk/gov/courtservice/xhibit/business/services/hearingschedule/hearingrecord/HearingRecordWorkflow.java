package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingSummaryValue;

/**
 * 
 * <p>
 * Title: HearingRecordWorkflow
 * </p>
 * <p>
 * Description:Controls the delegation of Hearing Record functionality to
 * appropriate helper classes.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Anthony Martin / Marie Holmberg
 * @version 1.0
 */
public class HearingRecordWorkflow {

	private static Logger log = CSServices.getLogger(HearingRecordWorkflow.class);

	private HearingListSummaryValueHelper hearingListSummaryValueHelper;

	private HearingRecordRetrievalForViewHelper retrievalForViewHelper;

	private HearingRecordUpdateHelper updateHelper;

	private HearingRecordExportHelper exportHelper;

	private HearingRecordStatusHelper statusHelper;
	
	private BwHistoryHelper bwHistoryHelper;

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param hearingID
	 *            Integer
	 * @param defendantID
	 *            Integer
	 * @return HearingRecordValue
	 * @throws HearingRecordException
	 */
	public HearingRecordValue retrieveHearingRecord(Integer hearingID, Integer defendantID, String userDisplayName)
			throws HearingRecordException {

		log.debug("HearingRecordWorkflow.retrieveHearingRecord() called");
		retrievalForViewHelper = new HearingRecordRetrievalForViewHelper();
		HearingRecordValue value = null;
		try {
			value = retrievalForViewHelper.retrieveHearingRecord(hearingID, defendantID, userDisplayName);
		} catch (HearingRecordRetrievalException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		log.debug("HearingRecordWorkflow.retrieveHearingRecord() finished");
		return value;
	}

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param hearingID
	 *            Integer
	 * @return HearingSummaryValue
	 * @throws HearingRecordException
	 */
	public HearingSummaryValue retrieveHearingSummaryValue(Integer hearingID, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordWorkflow.retrieveHearingRecordList() called");
		hearingListSummaryValueHelper = new HearingListSummaryValueHelper();
		return hearingListSummaryValueHelper.retrieveHearingSummaryValue(hearingID, userDisplayName);
	}

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param String
	 *            caseType
	 * @param DefHearingRecordValue
	 *            defHearingRecordValue
	 * @throws HearingRecordException
	 */
	public void saveFormADetails(String caseType, DefHearingRecordValue defHearingRecordValue, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordWorkflow.saveFormADetails() called");
		getUpdateHelper().saveFormADetails(caseType, defHearingRecordValue, userDisplayName);
		log.debug("HearingRecordWorkflow.saveFormADetails() ok");
	}
	
	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param hearingRecordUpdateValue
	 *            HearingRecordUpdateValue
	 * @throws HearingRecordException
	 */
	public void updateHearingRecord(HearingRecordUpdateValue hearingRecordUpdateValue, DefendantOnCaseBasicValue defOnCase, String currentPrisonStatus, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordWorkflow.updateHearingRecord() called");
		getUpdateHelper().updateHearingRecord(hearingRecordUpdateValue, defOnCase, currentPrisonStatus, userDisplayName);
		DefHearingRecordValue defHearingRecordValue = hearingRecordUpdateValue.getDefHearingRecordValue();
		try {
			getBwHistoryHelper().updateBWHistoryStatusEnded(defHearingRecordValue.getDefendantOnCaseID(), defHearingRecordValue.getEndBailStatus(),
					userDisplayName);
		} catch (BwHistoryControllerException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		log.debug("HearingRecordWorkflow.updateHearingRecord() ok");
	}

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param allHearingRecords
	 *            Collection
	 * @param courtClerkName
	 *            String
	 * @return Boolean success
	 * @throws HearingRecordException
	 */
	public Boolean validateHearingRecord(Integer defendantOnCaseId, Integer hearingId, String courtClerkName, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordWorkflow.validateHearingRecord() called");
		Boolean success = null;
		try {
			exportHelper = new HearingRecordExportHelper();
			success = exportHelper.validateHearingRecord(defendantOnCaseId, hearingId, courtClerkName, userDisplayName);
		} catch (HearingRecordExportException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new HearingRecordException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
		log.debug("HearingRecordWorkflow.validateHearingRecord() ok");
		return success;
	}

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @param hearingID
	 *            Integer
	 * @return Boolean
	 * @throws HearingRecordException
	 */
	public Boolean isExported(Integer hearingID) throws HearingRecordException {
		log.debug("entered HearingRecordWorkflow.isExported() called");
		statusHelper = new HearingRecordStatusHelper();
		return statusHelper.isExported(hearingID);
	}

	private HearingRecordUpdateHelper getUpdateHelper() {
		if (updateHelper == null) {
			updateHelper = new HearingRecordUpdateHelper();
		}
		return updateHelper;
	}
	
	private BwHistoryHelper getBwHistoryHelper() {
		if (bwHistoryHelper == null) {
			bwHistoryHelper = new BwHistoryHelper();
		}
		return bwHistoryHelper;
	}
}