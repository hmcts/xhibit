package uk.gov.courtservice.xhibit.business.services.hearingschedule;

import java.util.Collection;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndWorkflow;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndedAlreadyException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndedValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader.HearingHeaderWorkFlow;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.FormAException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordExportException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordWorkflow;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.LinkHearingWorkflow;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleWorkFlow;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsDatabaseManager;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingSummaryValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.CaseInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.SittingInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.AddHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;

/**
 * <p>
 * Title: HearingScheduleControllerBean
 * </p>
 * <p>
 * Description: Business Methods for Hearing Schedule.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Fitton
 * @author Ian Hannaford, Steve Tully
 * @version 1.0
 * 
 * @ejb.bean name="HearingScheduleController" description="Hearing Schedule
 *           Session Bean" type="Stateless" view-type="both"
 *           jndi-name="HearingScheduleControllerHome"
 *           local-jndi-name="HearingScheduleControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 *                  <Change History/>
 * 
 *                  <P>
 *                  10/02/03 - PDF - First issue.
 *                  </P>
 *                  <P>
 *                  12/02/03 - PDF - Place stubs for scheduling methods
 *                  <P>
 *                  19/03/03 - JB - Added getTodaysScheduleAsXML
 *                  </P>
 *                  <P>
 *                  29/04/03 - MH - Added addLegalRepValues method.
 *                  </P>
 *                  <P>
 *                  29/04/03 - IH, ST - Refactoring Work
 *                  </P>
 *                  <P>
 *                  13/08/03 - Pat Fox - added getTodaysScheduleForCourtRoom
 *                  </P>
 *                  <P>
 *                  19/10/04 - ST - Add Hearing
 *                  <P>
 *                  13/03/09 - JP - Marked <code>getScheduledHearings</code> as
 *                  both so that it can be called via its local interface
 *                  </P>
 */
public class HearingScheduleControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private ScheduleWorkFlow scheduleWorkFlow = new ScheduleWorkFlow();

	private HearingRecordWorkflow hearingRecordWorkflow = new HearingRecordWorkflow();

	private HearingEndWorkflow hearingEndWorkflow = new HearingEndWorkflow();

	private LinkHearingWorkflow linkHearingWorkflow = new LinkHearingWorkflow();

	private HearingHeaderWorkFlow hearingHeaderWorkFlow = new HearingHeaderWorkFlow();
	
    private final ScheduledHearingMaintainer scheduledHearingMaintainer = new ScheduledHearingMaintainer();

	/**
	 * Add a new Hearing to the Todays Schedule.
	 * 
	 * @param addHearingValue
	 *            details for the new Hearing
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addHearing(AddHearingValue addHearingValue, String userDisplayName) throws HearingScheduleException {

		String methodName = "addHearing() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Param Value :: " + addHearingValue);
		}

		try {
			scheduleWorkFlow.addHearing(addHearingValue, userDisplayName);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Move a case between courts within a days schedule
	 * 
	 * @param value
	 *            Details of the case to move
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void moveCase(MoveCaseValue mCValue, String userDisplayName) throws HearingScheduleException {

		String methodName = "moveCase() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Param Value :: " + mCValue);
		}

		try {
			scheduleWorkFlow.moveCase(mCValue, userDisplayName);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Obtains details of a case.
	 * 
	 * @param caseNumber
	 *            The CREST number of the case to look for
	 * @param caseType
	 *            The type of the case to search for
	 * @param courtId
	 *            The id of the court that the case is to be heard in
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseInfoValue getCaseDetails(Integer caseNumber, String caseType, Integer courtId)
			throws HearingScheduleException {

		String methodName = "getCaseDetails() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Params :: caseNumber = " + caseNumber + ", caseType = " + caseType
					+ ", courtId = " + courtId);
		}

		try {
			return getCaseDetails(caseNumber, caseType, courtId, null);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Obtains details of a case.
	 * 
	 * @param caseNumber
	 *            The CREST number of the case to look for
	 * @param caseType
	 *            The type of the case to search for
	 * @param courtId
	 *            The id of the court that the case is to be heard in
	 * @param hearingType
	 *            The hearing type to be added if relevant
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseInfoValue getCaseDetails(Integer caseNumber, String caseType, Integer courtId, String hearingType)
			throws HearingScheduleException {

		String methodName = "getCaseDetails() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Params :: caseNumber = " + caseNumber + ", caseType = " + caseType
					+ ", courtId = " + courtId + ", hearingType = " + hearingType);
		}

		try {
			return scheduleWorkFlow.getCaseDetails(caseNumber, caseType, courtId, hearingType);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Obtains details of a sitting.
	 * 
	 * @param courtRoomId
	 *            The id of the court room within the court
	 * @param sittingTime
	 *            The time that the sitting is scheduled
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public SittingInfoValue getSittingDetails(Integer courtRoomId, Date sittingTime) throws HearingScheduleException {

		String methodName = "getSittingDetails() - ";

		if (log.isDebugEnabled()) {
			log.debug(
					methodName + " Supplied Params :: courtRoomId = " + courtRoomId + ", sittingTime = " + sittingTime);
		}

		try {
			return scheduleWorkFlow.getSittingDetails(courtRoomId, sittingTime);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	// ------------------------- Hearing Record Methods
	// -----------------------//

	/**
	 * Provides a value object constructed from data associated with the Hearing
	 * Record. This data is retrieved from: Hearing, SHLegalRep, SHJudge,
	 * Export, Defendant, Case, CourtReporter, CounselFacilities,
	 * LinkedCasesList, Judge, HearingDisplay, and Justice data sources.
	 * 
	 * @param Integer
	 *            hearingID primary key.
	 * @param Integer
	 *            defendantID primary key.
	 * @return HearingRecordValue compound value object constructed from two
	 *         sub-value-objects (HearingRecordUpdateValue,
	 *         HearingRecordDisplayValue). These, in turn, are constructed from
	 *         other sub-value-objects representing the data obtained from their
	 *         respective Entity objects.
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public HearingRecordValue retrieveHearingRecord(Integer hearingID, Integer defendantID, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordControllerBean.retrieveHearingRecord() called");

		try {
			return hearingRecordWorkflow.retrieveHearingRecord(hearingID, defendantID, userDisplayName);
		} catch (HearingRecordException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.retrieveHearingRecord()");
		}
	}

	/**
	 * Provides a hearing summary value object, constructed from: One
	 * ExportAValue containing information about the export status of the
	 * summaries A Collection of HearinListSummaryValues which provide the
	 * client with the information necessary for creating the 'Summary Screen'.
	 * 
	 * @param Integer
	 *            hearingID object indicating primary key.
	 * @return HearingSummaryValue
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public HearingSummaryValue retrieveHearingSummaryValue(Integer hearingID, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordControllerBean.retrieveHearingSummaryValue() called");
		try {
			return hearingRecordWorkflow.retrieveHearingSummaryValue(hearingID, userDisplayName);
		} catch (HearingRecordException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.retrieveHearingSummaryValue()");
		}
	}

	/**
	 * Method to save the Form A details.
	 * 
	 * @param DefHearingRecordUpdateValue
	 *            representing the updateable fields for Form A.
	 * @return void
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void saveFormADetails(String caseType, DefHearingRecordValue defHearingRecordValue, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordControllerBean.saveFormADetails() called");
		try {
			hearingRecordWorkflow.saveFormADetails(caseType, defHearingRecordValue, userDisplayName);
		} catch (FormAException e) {
			// a validation exception has occurred, mark the transaction for rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} catch (HearingRecordException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.saveFormADetails()");
		}
	}
	
	/**
	 * Method provided to implement the Update Hearing Record functionality
	 * Updates the Xhibit database for Hearing Record with values provided in
	 * the form of a HearingRecordUpdateValue object.
	 * 
	 * @param HearingRecordUpdateValue
	 *            representing the updateable fields for Hearing Record.
	 * @return void
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateHearingRecord(HearingRecordUpdateValue hearingRecordUpdateValue, DefendantOnCaseBasicValue defOnCase, String currentPrisonStatus, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordControllerBean.updateHearingRecord() called");
		try {
			hearingRecordWorkflow.updateHearingRecord(hearingRecordUpdateValue, defOnCase, currentPrisonStatus, userDisplayName);
		} catch (FormAException e) {
			// a validation exception has occurred, mark the transaction for rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} catch (HearingRecordException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.updateHearingRecord()");
		}
	}

	/**
	 * This will trigger the validation of the hearing records for a hearing and all
	 * its linked hearings.
	 * 
	 * @amended Amended as part of CTX update.
	 * 
	 * @param hearingID
	 * @param courtClerkName
	 * @throws HearingRecordExportException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Boolean validateHearingRecord(Integer defendantOnCaseId, Integer hearingId, String courtClerkName, String userDisplayName)
			throws HearingRecordException {
		log.debug("HearingRecordControllerBean.validateHearingRecord() called");
		Boolean success = null;
		try {
			success = hearingRecordWorkflow.validateHearingRecord(defendantOnCaseId, hearingId, courtClerkName, userDisplayName);
		} catch (HearingRecordException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.validateHearingRecord()");
		}
		return success;
	}

	/**
	 * Confirms that all hearing records associated with the given hearingID
	 * have been exported. Returns true if hearing has been exported otherwise
	 * false.
	 * 
	 * @param hearingID
	 * @return Boolean
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Boolean isExported(Integer hearingID) throws HearingRecordException {
		log.debug("HearingRecordControllerBean.isExported() called");
		try {
			return hearingRecordWorkflow.isExported(hearingID);
		} catch (HearingRecordException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.isExported()");
		}
	}

	// -------------------------- End Hearing
	// Methods--------------------------//

	/**
	 * Method to "manully" enter the hearing duration for 1 hearing.
	 * 
	 * @param hearingID
	 * @param newDuration
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void amendHearingDuration(Integer defHearingRecordId, Long newDuration, String userDisplayName) {
		log.debug("entered HearingScheduleControllerBean.amendHearingDuration()");
		hearingEndWorkflow.amendHearingDuration(defHearingRecordId, newDuration, userDisplayName);
		log.debug("finishing HearingRecordControllerBean.amendHearingDuration()");
	}

	/**
	 * Method that will automatically re-calculate the hearing duration for 1
	 * hearing.
	 * 
	 * @param defHearingRecordValue
	 * @throws HearingEndedValidationException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void reCalculateHearingDuration(DefHearingRecordValue defHearingRecordValue, String userDisplayName)
			throws HearingEndedValidationException {
		log.debug("entered HearingScheduleControllerBean.reCalculateHearingDuration()");
		try {
			hearingEndWorkflow.reCalculateHearingDuration(defHearingRecordValue, userDisplayName);
		} catch (HearingEndedValidationException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			log.debug("finishing HearingRecordControllerBean.reCalculateHearingDuration()");
		}
	}

	/**
	 * Get the DefHearingRecordValue for the given hearing and defendant ids.
	 * 
	 * @param hearingID
	 *            Integer the id of the hearing
	 * @param defID
	 *            The id of the defendant
	 * @return DefHearingRecordValue
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public DefHearingRecordValue getDefHearingRecord(Integer hearingID, Integer defID) throws HearingRecordException {
		log.debug("entered getDefHearingRecord");
		try {
			return hearingEndWorkflow.getDefHearingRecord(hearingID, defID);
		} catch (HearingRecordException hre) {
			hre.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(hre, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw hre;
		} finally {
			log.debug("finishing getDefHearingRecord");
		}
	}

	/**
	 * Get the DefHearingRecordValue for the given hearing and defendant ids.
	 * 
	 * @param hearingID
	 *            Integer the id of the hearing
	 * @param defID
	 *            The id of the defendant
	 * @return DefHearingRecordValue
	 * @throws HearingRecordException
	 * @throws CSUnrecoverableException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public DefHearingRecordBasicValue getDefHearingRecordBasicValue(Integer hearingID, Integer defOnCaseID)
			throws ObjectNotFoundException {
		log.debug("entered getDefHearingRecord");
		try {
			DefHearingRecordMaintainer maintainer = DefHearingRecordMaintainer.getInstance();
			DefHearingRecord defHearingRecord = maintainer.findByDefendantOnCaseIDAndHearingID(defOnCaseID, hearingID);
			DefHearingRecordBasicValue basicValue = maintainer.getDefHearingRecordBasicValue(defHearingRecord);
			return basicValue;
		} catch (ObjectNotFoundException onfe) {
			onfe.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(onfe, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw onfe;
		} finally {
			log.debug("finishing getDefHearingRecord");
		}
	}

	/**
	 * Get all of the XhbDefHearingRecordBasicValue for the given defendant id.
	 * 
	 * @param defID
	 *            The id of the defendant
	 * @return XhbDefHearingRecordBasicValue
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public XhbDefHearingRecordBasicValue[] getDefHearingRecordByDefOnCaseId(Integer defOnCaseID) {
		XhbDefHearingRecordBasicValue[] defHearingRecord = XhbDefHearingRecordBeanHelper2
				.findByDefOnCaseIdValue(defOnCaseID);
		return defHearingRecord;
	}

	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#endHearing(uk.gov.courtservice
	 *      .xhibit.business.entities.hearing.Hearing, java.lang.Integer,
	 *      java.lang.Integer)
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void deleteEndHearing(Integer hearingId, Integer scheduledHearingId, Integer defendantOnCaseId,
			String userDisplayName) throws HearingEndedValidationException {
		try {
			hearingEndWorkflow.deleteEndHearing(hearingId, scheduledHearingId, defendantOnCaseId, userDisplayName);
		} catch (HearingEndedValidationException e) {
			// Should not get here. The HearingEndedValidationException is
			// thrown by code
			// shared with endHearing() which should not throw when used in this
			// context.
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#endHearing(uk.gov.courtservice
	 *      .xhibit.business.entities.hearing.Hearing, java.lang.Integer,
	 *      java.lang.Integer)
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void endHearing(Integer hearingId, Integer scheduledHearingId, Integer defendantOnCaseId,
			String userDisplayName) throws HearingEndedValidationException {
		try {
			hearingEndWorkflow.endHearing(hearingId, scheduledHearingId, defendantOnCaseId, userDisplayName);
		} catch (HearingEndedAlreadyException e) {
			// if this is a hearing already ended exception, then do not
			// log,
			// rollback the transaction, and re-throw
			ctx.setRollbackOnly();
			throw e;
		} catch (HearingEndedValidationException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#endUnendedHearing(
	 *      java.lang.Integer, java.lang.Integer)
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void	endUnendedHearing(Integer defendantOnCaseId, Integer hearingId, String userDisplayName) 
		throws HearingEndedValidationException {
		try {
			hearingEndWorkflow.endUnendedHearing(defendantOnCaseId, hearingId, userDisplayName);
		} catch (HearingEndedValidationException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}
	
	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#endAllUnendedHearings(java.lang
	 *      .Integer)
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void endAllUnendedHearings(Integer hearingId, String userDisplayName)
			throws HearingEndedValidationException {
		try {
			hearingEndWorkflow.endAllUnendedHearings(hearingId, userDisplayName);
		} catch (HearingEndedValidationException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#isHearingEnded(uk.gov.courtservice
	 *      .xhibit.business.entities.hearing.Hearing)
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public boolean isHearingEnded(Hearing hearing) {
		return hearingEndWorkflow.isHearingEnded(hearing);
	}

	/**
	 * @see uk.gov.courtservice.xhibit.business.services.hearingschedule
	 *      .endhearing.HearingEndWorkflow#isHearingEnded(uk.gov.courtservice
	 *      .xhibit.business.entities.xhb_hearing.XhbHearing)
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public boolean isHearingEnded(XhbHearing hearing) {
		return hearingEndWorkflow.isHearingEnded(hearing);
	}

	/**
	 * refer to: uk.gov.courtservice.xhibit.client.delegate.
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Boolean isHearingEnded(Integer schedHearingID) {
		log.debug("entered HearingScheduleControllerBean.isHearingEnded()");
		Boolean hearingEnded = hearingEndWorkflow.isHearingEnded(schedHearingID);
		log.debug("finishing HearingRecordControllerBean.isHearingEnded() - returning " + hearingEnded);

		return hearingEnded;
	}

	// ------------------------------------------------------------------------//

	/**
	 * @todo implement this method
	 * @param schedHearingIds
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public ScheduledHearingValue[] getScheduledHearings(Integer[] schedHearingIds) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getScheduledHearings()");

		try {
			debug("HearingScheduleControllerBean.getScheduledHearings(): exec.");
			return scheduleWorkFlow.getScheduledHearings(schedHearingIds);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getScheduledHearings()");
		}
	}

	// --------------------------Link Hearing
	// Methods--------------------------//

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param caseId
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public boolean previouslyLinked(Integer caseId) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.previouslyLinked()");

		try {
			debug("HearingScheduleControllerBean.previouslyLinked(): exec.");
			return linkHearingWorkflow.previouslyLinked(caseId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.previouslyLinked()");
		}
	}

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param scheduledHearingId
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public LinkSuggestionValue suggestLinkCases(Integer scheduledHearingId) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.suggestLinkCases()");

		try {
			debug("HearingScheduleControllerBean.suggestLinkCases(): exec.");
			return linkHearingWorkflow.suggestLinkCases(scheduledHearingId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.suggestLinkCases()");
		}
	}

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param scheduledHearingIds
	 * @param leadScheduledHearingId
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId,
			String userDisplayName) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.linkCases()");

		try {
			debug("HearingScheduleControllerBean.linkCases(): exec.");
			linkHearingWorkflow.linkCases(caseSchedHearingValues, leadScheduledHearingId, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.linkCases()");
		}
	}

	/**
	 * This method will link the hearings passed in. They will all get the same
	 * linked hearing id as the lead hearing. If the lead hearing already has a
	 * linked id then the others will get the same and also the ones they are
	 * lnked with.
	 * 
	 * @param leadHearingID
	 * @param hearingIDs
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void linkHearings(Integer leadHearingID, Collection hearingIDs, String userDisplayName)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.linkHearings()");

		try {
			debug("HearingScheduleControllerBean.linkHearings(): exec.");
			linkHearingWorkflow.linkHearings(leadHearingID, hearingIDs, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.linkHearings()");
		}
	}

	/**
	 * This will unlink this one hearing that is passed in. If there is only one
	 * more hearing with the same id - then this will be unlinked as well. This
	 * because there shouldn't be just one hearing for one linkedhearing id. If
	 * the hearing is linked with more than one other hearings then only this
	 * hearing will be unlinked.
	 * 
	 * @param hearingID
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void unlinkHearing(Integer hearingID, String userDisplayName) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.unlinkHearing()");

		try {
			debug("HearingScheduleControllerBean.unlinkHearing(): exec.");
			linkHearingWorkflow.unlinkHearing(hearingID, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.unlinkHearing()");
		}
	}

	/**
	 * This will call the listCasesWithHearings(String caseNumberTypeCriterium,
	 * Integer CourtID, Integer leadHearingID). Therefore this method could be
	 * used for other purposes as well. The linkedHearingID will be set to null.
	 * 
	 * @param caseNumberTypeCriterium
	 * @param CourtID
	 * @return CaseHearingValue
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseHearingValue listCaseWithHearings(String caseNumberTypeCriterium, Integer courtID)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.listCaseWithHearings()");
		CaseHearingValue value = null;
		try {
			debug("HearingScheduleControllerBean.listCaseWithHearings(): exec.");
			value = linkHearingWorkflow.listCaseWithHearings(caseNumberTypeCriterium, courtID);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.listCaseWithHearings()");
		}
		return value;
	}

	/**
	 * This will search for the case passed in and return the case and all
	 * related hearings except from the ones that have the same linkedHearingID
	 * as the one passed in. The case passed in needs massaging since the
	 * argument will be 1 string containing the case type and number - e.g.
	 * T20030932
	 * 
	 * @param caseNumberTypeCriterium
	 * @param CourtID
	 * @param leadHearingID
	 * @return CaseHearingValue
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseHearingValue listCaseWithHearings(String caseTypeAndNumber, Integer courtID, Integer leadHearingID)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.listCaseWithHearings()");
		CaseHearingValue value = null;
		try {
			debug("HearingScheduleControllerBean.listCaseWithHearings(): exec.");
			value = linkHearingWorkflow.listCaseWithHearings(caseTypeAndNumber, courtID, leadHearingID);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.listCaseWithHearings()");
		}
		return value;
	}

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param scheduledHearingId
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void unLinkCase(ScheduledHearingBasicValue shBasicValue, String userDisplayName)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.unLinkCase()");

		try {
			debug("HearingScheduleControllerBean.unLinkCase(): exec.");
			linkHearingWorkflow.unLinkCase(shBasicValue, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.unLinkCase()");
		}
	}

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param linkedShId
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseSchedHearingValue[] getLinkedSchedHearingsByLinkId(Integer linkedShId) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getLinkedSchedHearingsByLinkId()");

		try {
			debug("HearingScheduleControllerBean.getLinkedSchedHearingsByLinkId(): exec.");
			return linkHearingWorkflow.getLinkedSchedHearingsByLinkId(linkedShId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getLinkedSchedHearingsByLinkId()");
		}
	}

	/**
	 * See
	 * uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.
	 * LinkHearingHelper
	 * 
	 * @param schedHearingId
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseSchedHearingValue[] getLinkedSchedHearingsByShId(Integer schedHearingId)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getLinkedSchedHearingsByShId()");

		try {
			debug("HearingScheduleControllerBean.getLinkedSchedHearingsByShId(): exec.");
			return linkHearingWorkflow.getLinkedSchedHearingsByShId(schedHearingId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getLinkedSchedHearingsByShId()");
		}
	}

	/**
	 * This method calls the getHearingHeader(Integer scheduledHearingId,
	 * boolean updateSittingInfo). It is used so that the hearing header is
	 * automatically updated with the correct sitting data.
	 * 
	 * @param scheduledHearingId
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public HearingHeaderValue getHearingHeader(Integer scheduledHearingId, String userDisplayName)
			throws HearingScheduleException {
		return this.getHearingHeader(scheduledHearingId, true, userDisplayName);
	}

	/**
	 * @description Retreives all information required for the hearing header,
	 *              including staff, legal representative and attendee history,
	 *              for a given scheduled hearing.
	 * 
	 *              It will update the sitting data if the parameter
	 *              updateSittingInfo is true. This since we don't want to
	 *              update the sittingdata when a case is opened to be moved.
	 *              When the user selects to move a case, the sitting data is
	 *              also moved if this flag is set to true. This should NOT
	 *              happen. The sitting data should only be moved when a case is
	 *              truly opened.
	 * 
	 * @param scheduledHearingId
	 *            Integer
	 * @return HearingHeaderValue value object
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public HearingHeaderValue getHearingHeader(Integer scheduledHearingId, boolean updateSittingInfo,
			String userDisplayName) throws HearingScheduleException {

		debug("entered HearingScheduleControllerBean.getHearingHeader()");

		try {
			return hearingHeaderWorkFlow.getHearingHeader(scheduledHearingId, updateSittingInfo, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description retreives all staff for a given scheduled hearing. Includes
	 *              court clerks, ushers, justices, last judge and last court
	 *              reporter.
	 * @param scheduledHearingId
	 * @return collection of PersonValue value objects
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getAttendees(Integer scheduledHearingId, String userDisplayName) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getAttendees()");

		try {
			debug("HearingScheduleControllerBean.getAttendees(): exec.");
			return hearingHeaderWorkFlow.getAttendees(scheduledHearingId, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getAttendees()");
		}

	}

	/**
	 * @description retreives all legal representatives for a given scheduled
	 *              hearing. Includes prosecution, defence, objectors appellant,
	 *              repondent (representatives). If defence, the defendant is
	 *              also included. If defendant is representing themselves the
	 *              legal rep and the defendant are the same.
	 * @param scheduledHearingId
	 * @return collection of LegalRepValue value objects
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getLegalReps(Integer scheduledHearingId, Integer caseId) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getLegalReps()");

		try {
			debug("HearingScheduleControllerBean.getLinkedSchedHearingsByShId(): exec.");
			return hearingHeaderWorkFlow.getLegalReps(scheduledHearingId, caseId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getLegalReps()");
		}

	}

	/**
	 * @description retrieves all staff that have attended all scheduled
	 *              hearings related to this hearing
	 * @param scheduledHearingId
	 * @return collection of AttendeeHistory value objects
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getAttendeeHistory(Integer scheduledHearingId) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.getAttendeeHistory()");

		try {
			debug("HearingScheduleControllerBean.getAttendeeHistory(): exec.");
			return hearingHeaderWorkFlow.getAttendeeHistory(scheduledHearingId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		} finally {
			debug("finishing HearingScheduleControllerBean.getAttendeeHistory()");
		}

	}

	/**
	 * @description Add staff to the given scheduled hearing, this includes
	 *              justices, judges, court reporters and ushers. If the
	 *              attendee is a judge they can be added to subsequent hearings
	 *              aswell by setting the getIsAttendingSubsequentSH flag to
	 *              true.
	 * @param scheduledHearingId
	 *            Integer
	 * @param attendeeValues
	 *            Collection
	 * @return
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addAttendees(Integer scheduledHearingId, Collection attendeeValues, String userDisplayName)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.addAttendees()");

		try {
			hearingHeaderWorkFlow.addAttendees(scheduledHearingId, attendeeValues, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description This method will add the legal reps when the
	 *              schedHearingDefID is not known. The shLegReps are
	 *              temporarily set with the defendantID, which will be used to
	 *              look up the real scheduledHearingDefId that is required to
	 *              add Defence shLegReps.
	 * @param scheduledHearingId
	 *            Integer
	 * @param caseID
	 *            Integer
	 * @param shLegRepBasicValues
	 *            Collection
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void addLegalRepValues(Integer scheduledHearingId, Integer caseID, Collection shLegRepBasicValues,
			String userDisplayName) throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.addLegalRepValues()");

		try {
			hearingHeaderWorkFlow.addLegalRepValues(scheduledHearingId, caseID, shLegRepBasicValues, userDisplayName);
			// Update the Progress Trigger table as the Defendant reps have
			// changed
			scheduleWorkFlow.progressTriggerCaseUpdate(caseID, scheduledHearingId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description Add legal representatives to the given scheduled hearing.
	 * @param scheduledHearingId
	 *            Integer
	 * @param shLegRepBasicValues
	 *            Collection
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void addLegalReps(Integer scheduledHearingId, Collection shLegRepBasicValues, String userDisplayName)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.addLegalReps()");

		try {
			hearingHeaderWorkFlow.addLegalReps(scheduledHearingId, shLegRepBasicValues, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description Remove legal representatives from the related scheduled
	 *              hearing.
	 * @param scheduledHearingId
	 *            Integer
	 * @parma caseId Integer
	 * @param shLegRepBasicValues
	 *            Collection
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void removeLegalReps(Integer scheduledHearingId, Integer caseId, Collection shLegRepBasicValues)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.removeLegalReps()");

		try {
			hearingHeaderWorkFlow.removeLegalReps(shLegRepBasicValues);
			// Update the Progress Trigger table as the Defendant reps have
			// changed
			scheduleWorkFlow.progressTriggerCaseUpdate(caseId, scheduledHearingId);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description Update legal representatives from the related scheduled
	 *              hearing.
	 * @param scheduledHearingId
	 *            Integer
	 * @parm shLegRepBasicValues Collection
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateLegalReps(Integer scheduledHearingId, Collection shLegRepBasicValues, String userDisplayName)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.updateLegalReps()");

		try {
			hearingHeaderWorkFlow.updateLegalReps(shLegRepBasicValues, userDisplayName);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description Remove attendees from the related scheduled hearing.
	 * @param attendeeValues
	 *            Collection
	 * @param scheduledHearingId
	 *            Integer
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void removeAttendees(Integer scheduledHearingId, Collection attendeeValues, Boolean forSitting)
			throws HearingScheduleException {
		debug("entered HearingScheduleControllerBean.removeAttendees()");

		try {
			hearingHeaderWorkFlow.removeAttendees(scheduledHearingId, attendeeValues, forSitting);
		} catch (HearingScheduleException e) {
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			// a business exception has occurred, mark the transaction for
			// rollback
			ctx.setRollbackOnly();
			// rethrow the business exception
			throw e;
		}
	}

	/**
	 * @description Updates some case attributes and the hearingProgress
	 *              attribute on scheduled hearing
	 * @param caseBasicValue
	 *            CaseBasicValue
	 * @param scheduledHearingId
	 *            Integer
	 * @param hearingProgress
	 *            Integer
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void updateHHMain(CaseBasicValue caseBasicValue, Integer scheduledHearingId, Integer hearingProgress,
			String userDisplayName) throws HearingScheduleException {

		debug("updateHHMain() - Supplied Param Values :: caseBasicValue : " + caseBasicValue + "scheduledHearingId : "
				+ scheduledHearingId + " hearingProgress: " + hearingProgress);

		try {
			hearingHeaderWorkFlow.updateHHMain(caseBasicValue, scheduledHearingId, hearingProgress, userDisplayName);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw e;
		}
	}

	/**
	 * 
	 * @param hearingProgress
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateHearingProgress(Integer scheduledHearingId, Integer hearingProgress, String userDisplayName)
			throws HearingScheduleException {
		String methodName = "updateHearingProgress() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Param Values :: scheduledHearingId :" + scheduledHearingId
					+ " hearingProgress: " + hearingProgress);
		}

		try {
			scheduleWorkFlow.updateHearingProgress(scheduledHearingId, hearingProgress, userDisplayName);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Create new 'U' case.
	 * 
	 * @param AddCaseValue
	 *            - Details of the case to be created
	 * @return AddCaseValue - Details of the case that has been created
	 * @throws HearingScheduleException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseBasicValue createNewUCase(AddCaseValue addCaseValue, String userDisplayName)
			throws HearingScheduleException {
		String methodName = "createNewUCase() - ";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Supplied Params :: addCaseValue = " + addCaseValue);
		}

		try {
			return scheduleWorkFlow.createNewUCase(addCaseValue, userDisplayName);
		} catch (HearingScheduleException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			log.debug(methodName + " Transaction ROLLBACK ");
			throw e;
		}
	}

	/**
	 * Convenience method for the log4j debugger tool.
	 * 
	 * @param message
	 */
	private void debug(String message) {
		log.debug(message);
	}
	
	/**
     * Finds the hearing id given the schedule hearing Id.
     * 
     * @param scheduledHearingId
     *           
     * @return The hearing Id
     * @exception ObjectNotFoundException
	 * @ejb.interface-method view-type="remote"     
	 */
    public Integer getHearingIdFromScheduleHearing(Integer scheduledHearingId) {
       try {
    	   return scheduledHearingMaintainer.findByPK(scheduledHearingId).getHearingId();
       }catch(ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);

       }
    }
    
	/**
     * Find if the judge is sitting on a hearing list for the date.
     * 
     * @param refJudgeId
     * @param date
     *           
     * @return boolean
	 * @ejb.interface-method view-type="remote"     
	 */
    public boolean isJudgeSittingOnDate(Integer refJudgeId, Date date) {
    	HearingScheduleDatabaseManager dbMan = new HearingScheduleDatabaseManager();
    	return dbMan.isJudgeSittingOnDate(refJudgeId, date);
    }
    
	/**
	 * Returns all hearing basic values by caseId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @return Collection
	 * @throws ObjectNotFoundException
	 */
	public Collection findHearingByCaseId(Integer caseId) throws ObjectNotFoundException {
		String METHOD_NAME = "findHearingByCaseId()";
		log.debug(METHOD_NAME);
		HearingMaintainer hrMaintainer = new HearingMaintainer();
		return hrMaintainer.findByCaseId(caseId);
	}
}