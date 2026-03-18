package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.DataTypeConverter;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.LinkHearingWorkflow;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.SchedHearingLocationValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;

/**
 * <p>
 * Title: Hearing Header Workflow
 * </p>
 * <p>
 * Description: This class carries out the main functionality of the hearing
 * header methods.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Abdul Rahim Hussain
 * @editors Ian Hannaford, Steve Tully
 * @version $Id: HearingHeaderWorkFlow.java,v 1.23 2005/01/06 11:15:28 szn20z
 *          Exp $
 */
public class HearingHeaderWorkFlow {

    private CourtSiteMaintainer courtSiteMaintainer;
    
    // Case mainitainer
    private CaseMaintainer caseMaintainer;

    // Schedule hearing maintainer
    private ScheduledHearingMaintainer scheduledHearingMaintainer;

    // Bis ref controller
    private BisRefControllerLocal bisRef;

    // case controller
    private CaseControllerLocal caseController;

    // sh controller
    private HearingScheduleControllerLocal hsController;

    // Staff helper
    private HHStaffHelper hhStaffHelper;

    // Legal rep helper
    private HHLegalRepHelper hhLegRepHelper;

    // Logger
    private static Logger log = CSServices.getLogger(HearingHeaderWorkFlow.class);

    private static final String NO_INDICTMENT_FAILED_SET_SEVERED = "hearingheader.noindictmenttosetsevered";

    private static final String NOT_TRIAL_CASE_SEV_IND = "hearingheader.nottrialcaseforseveredindicment";

    private static final String NOT_TRIAL_CASE_CLASS_CODE = "hearingheader.notrialcaseforclasscode";

    private static final String NOT_TRIAL_CASE_OFFENCE_GROUP = "hearingheader.nottrialcaseforoffencegroup";

    private static final String CASE_DETAILS_EXPORT_IN_PROGRESS = "hearingheader.casedetailsexportinprogress";

    private static final String EXPORT_IN_PROGRESS = "U";

    private static final String READY_FOR_EXPORT = "R";

    private static final String TRIAL_CASE = "T";

    /**
     * No Args constructor for this class. Get references to the
     * BisRefController, CaseController and HearingSceduleController.
     */
    public HearingHeaderWorkFlow() {
        // Get the bisref controller
        bisRef = ((BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                BisRefControllerLocalHome.class));

        // Get the case controller
        caseController = ((CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                CaseControllerLocalHome.class));

        // Get hearing schedule controller
        hsController = ((HearingScheduleControllerLocal) CSServices.getEJBServices().createLocalSession(
                HearingScheduleControllerLocalHome.class));

        caseMaintainer = new CaseMaintainer();
        scheduledHearingMaintainer = new ScheduledHearingMaintainer();
        hhStaffHelper = new HHStaffHelper(bisRef);
        hhLegRepHelper = new HHLegalRepHelper(bisRef);
        courtSiteMaintainer = new CourtSiteMaintainer();
    }

    /**
     * @description retreives all information required for the hearing header,
     *              including staff, legal representative and attendee history,
     *              for a given scheduled hearing.
     * @param scheduledHearingId
     *            Integer
     * @return HearingHeaderValue value object
     * @throws HearingScheduleException
     */
    public HearingHeaderValue getHearingHeader(Integer scheduledHearingId, boolean updateSittingInfo, String userDisplayName)
            throws HearingScheduleException {

        String methodName = "getHearingHeader() - ";

        if (log.isDebugEnabled()) {
            log.debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);
        }

        HearingHeaderValue hearingHeaderValue = null;

        try {

            // Find scheduled hearing
            ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(scheduledHearingId);

            /* Set simple Hearing Header attributes */
            // set id
            hearingHeaderValue = new HearingHeaderValue();
            hearingHeaderValue.setId(scheduledHearing.getHearing().getHearingId());

            // set timelisted
            hearingHeaderValue.setTimeListed(DataTypeConverter.convertToCalendar(scheduledHearing.getNotBeforeTime()));

            // set hearing progress
            hearingHeaderValue.setHearingProgress(scheduledHearing.getHearingProgress());

            Hearing hearing = scheduledHearing.getHearing();

            // set case basic value
            Integer caseId = hearing.getCaseId();
            Case cs = caseMaintainer.findByPrimaryKey(caseId);

            hearingHeaderValue.setHhCase(caseMaintainer.getCaseBasicValue(cs));

            // set hearingType
            try {
                Integer refHearingTypeId = hearing.getRefHearingTypeId();

                RefHearingTypeCriteria refHearingTypeCriteria = new RefHearingTypeCriteria();
                refHearingTypeCriteria.setPrimaryKey(refHearingTypeId);
                Collection hearingTypes = bisRef.findHearingTypes(refHearingTypeCriteria);

                if (!hearingTypes.isEmpty()) {
                    Iterator it = hearingTypes.iterator();
                    RefHearingTypeBasicValue refHearingTypeValue = (RefHearingTypeBasicValue) it.next();
                    hearingHeaderValue.setHearingType(refHearingTypeValue.getHearingTypeDesc());
                }
            } catch (BisRefControllerException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
            }

            // set staff values
            hearingHeaderValue.setStaffValues(getAttendees(scheduledHearingId, updateSittingInfo, userDisplayName));

            // set legal Rep values
            // This will need to be set differently for B cases
            hearingHeaderValue.setLegalRepValues(getLegalReps(scheduledHearingId, caseId));

            // set Attendee History values
            hearingHeaderValue.setAttendeeHistoryValues(getAttendeeHistory(scheduledHearingId));

            // set linked cases
            LinkHearingWorkflow linkHearingWorkflow = new LinkHearingWorkflow();
            hearingHeaderValue.setCaseSchedHearingValues(linkHearingWorkflow
                    .getLinkedSchedHearingsByShId(scheduledHearingId));

            log.debug(methodName + "Exited OK");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hse = new HearingScheduleException("", ex.getMessage(), ex);
            throw hse;
        }

        log.debug(methodName + "Exited OK");

        return hearingHeaderValue;

    }

    /**
     * Returns a collection of Attendes. The sitting info may or may not be
     * updated depending on the boolean passed in.
     *
     * @param scheduledHearingId
     *            Integer primary key for the schedulded hearing
     * @param updateSittingInfo
     *            boolean true if the sitting info should be updated
     * @return Collection of attendees.
     * @throws HearingScheduleException
     */
    public Collection getAttendees(Integer scheduledHearingId, boolean updateSittingInfo, String userDisplayName)
            throws HearingScheduleException {
        return hhStaffHelper.getAttendees(scheduledHearingId, updateSittingInfo, userDisplayName);
    }

    /**
     * Returns a collection of attendees but will also always update the sitting
     * info. This will call the this.getAttendees(Integer scheduledHearingId,
     * boolean updateSittingInfo) with value true (always update the sitting
     * info)
     *
     * @param scheduledHearingId
     *            Integer
     * @return Collection of attendees
     * @throws HearingScheduleException
     */
    public Collection getAttendees(Integer scheduledHearingId, String userDisplayName) throws HearingScheduleException {
        return this.getAttendees(scheduledHearingId, true, userDisplayName);
    }

    public Collection getLegalReps(Integer scheduledHearingId, Integer caseId) throws HearingScheduleException {
        return hhLegRepHelper.getLegalReps(scheduledHearingId, caseId);
    }
    

    public Collection getAttendeeHistory(Integer scheduledHearingId) throws HearingScheduleException {
        return hhStaffHelper.getAttendeeHistory(scheduledHearingId);
    }

    /**
     * @description Add staff to the given scheduled hearing, this includes
     *              justices, judges, court reporters and ushers if the attendee
     *              is a judge they can be added to subsequent hearings aswell
     *              by setting the getIsAttendingSubsequentSH flag to true.
     *              Calls addAttendees on hhStafHelper.
     * @param scheduledHearingId
     *            Integer
     * @param attendeeValues
     *            Collection
     * @throws HearingScheduleException
     */
    public void addAttendees(Integer scheduledHearingId, Collection attendeeValues, String userDisplayName) throws HearingScheduleException {
        hhStaffHelper.addAttendees(scheduledHearingId, attendeeValues, userDisplayName);
    }

    /**
     * This method will add the legal reps when the schedHearingDefID is not
     * known. The shLegReps are temporarily set with the defendantOnCaseID,
     * which will be used to look up the real scheduledHearingDefId that is
     * required to add Defence shLegReps.
     *
     * @param scheduledHearingId
     *            Integer
     * @param shLegRepBasicValues
     *            Collection
     * @throws HearingScheduleException
     */
    public void addLegalReps(Integer scheduledHearingId, Collection shLegRepBasicValues, String userDisplayName)
            throws HearingScheduleException {
        hhLegRepHelper.addLegalReps(scheduledHearingId, shLegRepBasicValues, userDisplayName);

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
     */
    public void addLegalRepValues(Integer scheduledHearingId, Integer caseID, Collection shLegRepBasicValues, String userDisplayName)
            throws HearingScheduleException {
        hhLegRepHelper.addLegalRepValues(scheduledHearingId, caseID, shLegRepBasicValues, userDisplayName);
    }

    /**
     * @description Add legal representative to the given scheduled hearing.
     *              Calls addLegalRep on hhLegRepHelper
     * @param legRep
     *            SHLegRepBasicValue
     * @throws HearingScheduleException
     */
    public void addLegalRep(SHLegRepBasicValue legRep, String userDisplayName) throws HearingScheduleException {
        hhLegRepHelper.addLegalRep(legRep, userDisplayName);
    }

    /**
     * @description Remove legal representatives from the related scheduled
     *              hearing.
     * @param shLegRepBasicValues
     *            Collection
     * @throws HearingScheduleException
     */
    public void removeLegalReps(Collection shLegRepBasicValues) throws HearingScheduleException {
        hhLegRepHelper.removeLegalReps(shLegRepBasicValues);
    }
    
    /**
     * @description Update legal representatives from the related scheduled
     *              hearing.
     * @param shLegRepBasicValues
     *            Collection
     * @throws HearingScheduleException
     */
    public void updateLegalReps(Collection shLegRepBasicValues, String userDisplayName) throws HearingScheduleException {
        hhLegRepHelper.updateLegalReps(shLegRepBasicValues, userDisplayName);
    }

    /**
     * @description Remove attendees from the related scheduled hearing.
     * @param attendeeValues
     *            collection of attendeeValues
     * @param scheduledHearingId
     *            Integer
     * @throws HearingScheduleException
     */
    public void removeAttendees(Integer scheduledHearingId, Collection attendeeValues, Boolean forSitting)
            throws HearingScheduleException {
        hhStaffHelper.removeAttendees(scheduledHearingId, attendeeValues, forSitting);
    }

    /**
     * Updates the hearing hearder case information.
     *
     * @param caseBasicValue
     *            CaseBasicValue
     * @param scheduledHearingId
     *            Integer
     * @param hearingProgress
     *            Integer
     * @throws HearingScheduleException
     */
    public void updateHHMain(CaseBasicValue caseBasicValue, Integer scheduledHearingId, Integer hearingProgress, String userDisplayName)
            throws HearingScheduleException {
        try {
            if (caseBasicValue == null) {
                // no update required!
                return;
            }
            // validate the severedindictment flag and charges.
            validateSeveredIndictment(caseBasicValue);

            // validate other data set for the case
            validateCaseTypeAndSetData(caseBasicValue);

            /* ctx-2608 : Updating to set to E so that it 
            bypasses what broker would have done */
            caseBasicValue.setIndChangeStatus("E");


            // update the case
            log.debug("Updating the case - using caseController.updateCase() : " + caseBasicValue);
            caseController.updateCase(caseBasicValue, userDisplayName);
            
            notifyNewPublicDisplays(scheduledHearingId, userDisplayName);
            
        } catch (NullPointerException npex) {
            CSServices.getDefaultErrorHandler().handleError(npex, getClass(), npex.toString());
            HearingScheduleException hse = new HearingScheduleException(
                    "hearingschedule.casebasicvalue.nullpointerexception", npex.getMessage(), npex);
            throw hse;
        } catch (CaseControllerException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hse = new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex
                    .getMessage(), ex);
            throw hse;
        }

        log.debug("Calling updateHearingProgress");
        if (scheduledHearingId != null) {
            hsController.updateHearingProgress(scheduledHearingId, hearingProgress, userDisplayName);
        }
    }

    
    /**
     * The public display data may have to be regenerated if the 'hide in public display'
     * value is changed.  This method sends and UpdateCaseEvent to trigger regeneration
     * of the cached public display HTML pages.
     * @param scheduledHearingId
     */
    private void notifyNewPublicDisplays(Integer scheduledHearingId, String userDisplayName) {
        try {
            ScheduledHearing sh = scheduledHearingMaintainer.findByPK(scheduledHearingId);
            Integer courtId = courtSiteMaintainer.findByPrimaryKey(sh.getSitting().getCourtSiteId()).getCourtId();
            String courtName = getCourtName(courtId);
			Integer courtRoomNo = getCourtRoomNumber(sh);
			DisplayablePublicNoticeValue[] publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(sh.getSitting().getCourtRoomId());
            CourtRoomIdentifier cri = new CourtRoomIdentifier(courtId, sh.getSitting().getCourtRoomId(), courtName, courtRoomNo, publicNotices);
            CaseChangeInformation cci = new CaseChangeInformation(sh.getIsCaseActive().equals("Y"));
            UpdateCaseEvent uce = new UpdateCaseEvent(cri, cci);
            PddaHelper notifier = new PddaHelper();
            notifier.sendMessage(uce, userDisplayName);
            notifier.close();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        } catch (PublicNoticeCourtRoomUnknownException e) {
        	log.error("Unable to find any public notices for either the court room id.");
        	e.printStackTrace();
		}
    }
    
    
   

    /**
     * Method to validate if the class code and the offence group have been set
     * when the case is not a trial case. If so an exception will be thrown.
     *
     * @param caseBasicValue
     *            CaseBasicValue that contains the classCode and the
     *            offenceGroup
     * @throws HearingScheduleException
     */
    private void validateCaseTypeAndSetData(CaseBasicValue caseBasicValue) throws HearingScheduleException {
        if (caseBasicValue.getCaseType() != null && !caseBasicValue.getCaseType().equalsIgnoreCase(TRIAL_CASE)) {
            if (caseBasicValue.getClassCode() != null) {
                throw new HearingScheduleException(NOT_TRIAL_CASE_CLASS_CODE,
                        "Class code can only be set when the case is of type Trial");
            }
            if (caseBasicValue.getOffenceGroupCode() != null && caseBasicValue.getOffenceGroupCode().length() > 0) {
                throw new HearingScheduleException(NOT_TRIAL_CASE_OFFENCE_GROUP,
                        "Offence group update can only be set when the case is of type Trial");
            }
        }
    }

    /**
     * Method to check if the case.severedindictment flag has been set. If it
     * has been set then we need to make sure there are indictments on the case
     * if not an HearingScheduleException will be thrown.
     *
     * This flag can only be set for T-cases!
     *
     * @param caseBasicValue
     *            CaseBasicValue
     * @throws HearingScheduleException
     */
    private void validateSeveredIndictment(CaseBasicValue caseBasicValue) throws HearingScheduleException {
        // Need to check if the severed Indictment flag has been selected,
        // if so we need to make there is at least one indictment on the case
        // and that it hasn't been deleted.
        if (caseBasicValue.getCrestSeveredInd() != null && caseBasicValue.getCrestSeveredInd().equalsIgnoreCase("Y")) {
            // if the severed indictment has been set and the case is not
            // trial
            // then
            // throw an exception. This should only be set for Trial cases!
            if (caseBasicValue.getCaseType() != null && (!caseBasicValue.getCaseType().equalsIgnoreCase(TRIAL_CASE))) {
                throw new HearingScheduleException(NOT_TRIAL_CASE_SEV_IND,
                        "Severed indictment can only be set when the case is of type Trial");
            }

            log.debug("Will try to validate the case and indictments for caseid : " + caseBasicValue.getId());

            Collection indictments = XhbChargeBeanHelper2.findNonObsoleteByCaseIdAndChargeType(
				    caseBasicValue.getId(), ChargeTypes.INDICTMENT.getChargeType());

            // if there are no indictments on the case an exception is thrown
            if (indictments.size() == 0) {
                log.debug("The doesIndictmentExist is false so will throw an exception");
                HearingScheduleException hse = new HearingScheduleException(NO_INDICTMENT_FAILED_SET_SEVERED,
                        "There are no indicments on this case so the severed ind flag cannot be set");
                throw hse;
            }
        }
    }
    
    
    public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
		return courtName;
	}
	
	
	public Integer getCourtRoomNumber(ScheduledHearing sh) {
		Integer courtRoomNo = 0;
		if ((sh != null) && (sh.getSitting() != null) && (sh.getSitting().getCourtSiteId() != null)) {
			CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
			try {
				courtRoomNo = courtRoomMaintainer.findByPrimaryKey(sh.getSitting().getCourtRoomId()).getCrestCourtRoomNo();
			} catch (ObjectNotFoundException e) {
				log.error("Cannot find the court room number.");
				e.printStackTrace();
			}
		}
		return courtRoomNo;
	}
}