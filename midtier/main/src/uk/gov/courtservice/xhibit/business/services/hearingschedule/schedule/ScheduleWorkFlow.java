package uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList;
import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudge;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseAccessException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.caze.CaseLoadingException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseRetrievalIntController;
import uk.gov.courtservice.xhibit.business.services.caze.CaseRetrievalIntControllerException;
import uk.gov.courtservice.xhibit.business.services.createcase.GenerateCaseNumberControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.InvalidHearingTimeException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.LinkHearingWorkflow;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.CaseInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.SittingInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.AddHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.HearingProgressValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.common.progress.caseupdate.ProgressTriggerMaintainer;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.AddCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.MoveCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Id: ScheduleWorkFlow.java,v 1.98 2014/06/20 17:22:26 atwells Exp $
 * 
 * @amend
 *  LW 27/03/2018
 *  CTX-404 Remove exists check from getCaseDetails. All cases must already exist in xhibit
 *  since crest has been removed and mercator calls are no longer required
 *  
 *  LW 27/03/2018
 *  CTX 1681 Set case.CastListed =Y when case is added to daily list via 'Add Hearing'
 */
public class ScheduleWorkFlow {
    private static final Logger log = CSServices.getLogger(ScheduleWorkFlow.class);

    private static final String YES = "Y";
    
    private static final String CHARGE_IMPORT_INDICATOR_TYPE_O = "O";
    
    private static final String INVALID_HEARING_TIME = "scheduledhearingworkflow.invalidhearingtime";

    // the below values are only to be set in the constructor
    private final SittingMaintainer sittingMaintainer;

    private final HearingMaintainer hearingMaintainer;

    private final HearingListMaintainer hearingListMaintainer;

    private final CaseMaintainer caseMaintainer;

    private final ScheduleHelper scheduleHelper;

    private final TodaysScheduleHelper todaysScheduleHelper;

    private final SchedHearingAttendeeMaintainer attendeeMaintainer;

    private final CourtRoomMaintainer courtRoomMaintainer;

    private final ScheduledHearingMaintainer scheduledHearingMaintainer;

    private final ShJudgeMaintainer shJudgeMaintainer;

    private final HearingTypeHelper hearingTypeHelper;

    /**
     * Constructor for the ScheduleWorkFlow object
     */
    public ScheduleWorkFlow() {
        this.sittingMaintainer = new SittingMaintainer();
        this.hearingMaintainer = new HearingMaintainer();
        this.hearingListMaintainer = new HearingListMaintainer();
        this.caseMaintainer = new CaseMaintainer();
        this.attendeeMaintainer = new SchedHearingAttendeeMaintainer();
        this.scheduleHelper = new ScheduleHelper();
        this.todaysScheduleHelper = new TodaysScheduleHelper();
        this.courtRoomMaintainer = new CourtRoomMaintainer();
        this.shJudgeMaintainer = new ShJudgeMaintainer();
        this.scheduledHearingMaintainer = new ScheduledHearingMaintainer();
        this.hearingTypeHelper = new HearingTypeHelper();
    }

    /**
     * @param addHearingValue -
     *            Details of the hearing to be added to the schedule
     * @throws HearingScheduleException
     */
    public void addHearing(AddHearingValue addHearingValue, String userDisplayName) throws HearingScheduleException {
        boolean isNewSitting = false;

        // used to determine the sequence number of the sitting if we
        // need to create one
        int sittingSequenceNumber = 1;

        if (log.isDebugEnabled()) {
            log.debug("addHearing() - called with :: " + addHearingValue);
        }

        // Find the case...
        CaseBasicValue cbv = null;
        try {
            Case myCase = caseMaintainer.findByNumberTypeAndCourt(addHearingValue.getCaseNumber(), addHearingValue
                    .getCaseType(), addHearingValue.getCourtId());
            cbv = caseMaintainer.getCaseBasicValue(myCase);
            Integer caseId = cbv.getId();

            // Obtain the hearing type ID...
            Integer refHearingTypeId = addHearingValue.getRefHearingTypeId();

            // Lookup the courtsite Id based on CourtRoomId...
            Integer courtSiteId = scheduleHelper.getCourtSiteId(addHearingValue.getCourtRoomId());
            log.debug("courtSiteId:" + courtSiteId);

            // Try to find a Hearing to attach this to
            Hearing hearing = null;
            HearingTypeMatchResult result = hearingTypeHelper.getHearingRule(caseId, refHearingTypeId);
            if (result.isNewHearingRequired()) {
                // No eligible Hearing record found so create a new one
                HearingBasicValue hbv = new HearingBasicValue(caseId, refHearingTypeId, addHearingValue.getCourtId(),
                        null);
                hearing = (Hearing) hearingMaintainer.create(hbv, userDisplayName);
            } else {
                // There is an eligible Hearing.
                hearing = result.getHearing();
                hearing.setRefHearingTypeId(refHearingTypeId);
                HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearing);
                hearingMaintainer.update(hbv, userDisplayName);
            }

            Date startOfDay = DateTimeUtilities.stripTimeToUtilDate(addHearingValue.getNotBeforeTime());
            log.debug("startOfDay:" + startOfDay);
            Collection sittings = sittingMaintainer.findByDateAndCourt(startOfDay, addHearingValue.getCourtRoomId(),
                    courtSiteId);

            log.debug("Number of sittings found:" + sittings.size());

            // Find the Sitting
            Sitting sitting = scheduleHelper.sittingExists(sittings, new Timestamp(addHearingValue.getNotBeforeTime()
                    .getTime()));

            if (sitting != null) {
                log.debug("a sitting was found as best match.  sittingId:" + sitting.getSittingId());

                // MH - if the sitting exists but is a floating sitting we don't
                // want
                // to use this so we set the sitting to null.
                if (sitting.getIsFloating().equals("1") || sitting.getIsFloating().equalsIgnoreCase("Y")) {
                    log.debug("...but it is floating, so we cannot use it");
                    sittingSequenceNumber = sitting.getSittingSequenceNo().intValue() + 1;
                    sitting = null;
                }
                // check if the judge is the same in the new hearing as in the
                // sitting.
                // if not we want to create a new sitting.
                else if (addHearingValue.getRefJudgeID() != null) {
                    if (sitting.getRefJudgeId() != null
                            && sitting.getRefJudgeId().intValue() != addHearingValue.getRefJudgeID().intValue()) {
                        log.debug("In if, sitting.getRefJudgeId(): " + sitting.getRefJudgeId()
                                + ", addHearingValue.getRefJudgeId()" + addHearingValue.getRefJudgeID());
                        // the new sitting we create will need to come after
                        // this one
                        sittingSequenceNumber = sitting.getSittingSequenceNo().intValue() + 1;
                        sitting = null;
                    } else if (sitting.getRefJudgeId() == null) {
                        log.debug("In else if since the sitting judge is null!");
                        // the new sitting we create will need to come after
                        // this one
                        sittingSequenceNumber = sitting.getSittingSequenceNo().intValue() + 1;
                        sitting = null;
                    }
                }
            }

            log.debug("sittingSequenceNumber:" + sittingSequenceNumber);

            // Create Sitting, if not found
            if (sitting == null) {
                log
                        .debug("Need to create a new sitting.  Either there were no prior sittings or any possible matches were unsuitable");
                sitting = createSitting(addHearingValue, courtSiteId, sittingSequenceNumber, userDisplayName);
                isNewSitting = true;
                // if we have inserted a sitting in the middle of existing
                // sittings we need to update the sequence number for any
                // sittings which come afterwards
                if (sittings.size() > 0 && sittingSequenceNumber != (sittings.size() + 1)) {
                    updateSittingSeqNums(sittings, sittingSequenceNumber);
                }
            }

            // Create Scheduled Hearing VO...
            ScheduledHearingBasicValue shbv = new ScheduledHearingBasicValue();

            shbv.setAddHearingUsed(YES);
            shbv.setNotBeforeTime(addHearingValue.getNotBeforeTime());
            // MH - set the caseprogress so that the GUI don't fall over
            shbv.setHearingProgress(new Integer(0));

            // set original time
            shbv.setOriginalTime(addHearingValue.getNotBeforeTime());

            if (isNewSitting) {
                shbv.setSequenceNo(new Integer(1));
            } else {
                Integer newSequenceNumber = scheduleHelper.adjustSHSequenceNumbers(sitting, shbv, userDisplayName);
                shbv.setSequenceNo(newSequenceNumber);
            }

            // Create the entity...
            log.debug("Will try to create scheduled hearing : " + shbv);
            ScheduledHearing scheduledHearing = (ScheduledHearing) scheduledHearingMaintainer.create(shbv, hearing,
                    sitting, userDisplayName);

            // MH - we need the case id to look up the right defOnCaseID as
            // well.
            // Add defendants to the new scheduled hearing
            log.debug("Will check if we have any defendants to add to the DefSchedHrg");
            if (addHearingValue.getDefendants() != null && addHearingValue.getDefendants().size() > 0) {
                log.debug("Call the createSHDefendants with caseid : " + caseId);
                scheduleHelper.createSHDefendants(caseId, scheduledHearing, addHearingValue.getDefendants(), userDisplayName);
            }
            
            //Update case listed to Y.
            cbv.setCaseListed(YES);
            caseMaintainer.update(cbv, userDisplayName);

            // tell public displays that the case has been moved
            notifyNewPublicDisplays(addHearingValue, userDisplayName);
            progressTriggerCaseUpdate(getLinkedHearings(caseId, hearing.getHearingId()).toArray(
                    new Integer[0]), ProgressTriggerMaintainer.getYesFlag());
            
            synchroniseCase(caseId);
    

        } catch (ObjectNotFoundException onfe) {
            throw new HearingScheduleException("", onfe.toString(), onfe);
        }
        if (log.isDebugEnabled()) {
            log.debug("addHearing() - Exited OK");
        }
    }

    // Ensure Case is Synchronised.  The safest way of doing this is following the process of open case in the 
    // Case Controller bean.
     private void synchroniseCase(Integer caseId){
         try {
            new CaseRetrievalIntController().openCase(caseId);
         } catch (CaseLoadingException cle) {
             log.info("CaseLoadingException: "+ cle);            
        } catch (CaseAccessException cae) {
            CSServices.getDefaultErrorHandler().handleError(cae, getClass(), cae.toString());
        } catch (CaseRetrievalIntControllerException crice) {
            CSServices.getDefaultErrorHandler().handleError(crice, getClass(), crice.toString());
        }
         
     }
     
    /**
     * Find all linked cases for this case and then setup the Progress Trigger
     * 
     * @param caseId
     *            the case id which has had the hearing added
     */
    public void progressTriggerCaseUpdate(Integer caseId, Integer schHearingId) {
        if (log.isDebugEnabled()) {
            log.debug("progressTriggerCaseUpdate caseId: " + caseId);
            log.debug("progressTriggerCaseUpdate schHearingId: " + schHearingId);
        }
        HashSet <Integer> caseIds = new HashSet <Integer>();
        // add the original case id
        caseIds.add(caseId);
        caseIds.addAll(getLinkedSchedHearings(caseId, schHearingId));
        // Update the Progress Trigger table with the case id(s)
        // This is not called by addHearing so flag should be N
        progressTriggerCaseUpdate(caseIds.toArray(new Integer[caseIds.size()]), ProgressTriggerMaintainer
                .getNoFlag());
    }

    /**
     * Find all linked cases for this case and then setup the Progress Trigger
     * 
     * @param caseId
     *            the case id which has had the hearing added
     */
    private void progressTriggerCaseUpdate(Integer[] caseIds, String newHearing) {
        if (log.isDebugEnabled()) {
            log.debug("progressTriggerCaseUpdate caseId: " + caseIds);
        }
        // Update the Progress Trigger table with the case id(s)
        ProgressTriggerMaintainer.updateCaseTrigger(caseIds, newHearing);
    }

    /**
     * Get the case ids of the scheduled hearings linked to this scheduled
     * hearing
     * 
     * @param schedHearingId
     * @return a Collection of case ids linked via the scheduled hearing
     */
    private Collection<Integer> getLinkedSchedHearings(Integer caseId, Integer schedHearingId) {
        if (log.isDebugEnabled()) {
            log.debug("getLinkedSchedHearings schHearingId: " + schedHearingId);
        }
        // Use a HashSet to avoid duplicate case ids
        HashSet <Integer> caseIds = new HashSet <Integer>();
        LinkHearingWorkflow linkedShHearingWflow = new LinkHearingWorkflow();
        try {
            CaseSchedHearingValue[] caseSchVal = linkedShHearingWflow.getLinkedSchedHearingsByShId(schedHearingId);
            for (CaseSchedHearingValue c : caseSchVal){
                caseIds.add(c.getCaseId());
            }
        } catch (HearingScheduleException e) {
            ProgressTriggerMaintainer.logError(caseId, Calendar.getInstance().getTime(), ProgressTriggerMaintainer
                    .getNoFlag(), e);
        }
        finally{
            return caseIds;
        }
    }

    /**
     * Get the case ids of other cases linked to this via the linked hearing,
     * and then the linked scheduled hearings
     * 
     * @param caseId
     *            the case id for the current case
     * @param hearingId
     *            the current hearing id
     * @return a Collection of case ids linked via the hearing
     */
    private Collection<Integer> getLinkedHearings(Integer caseId, Integer hearingId) {
        if (log.isDebugEnabled()) {
            log.debug("getLinkedHearings HearingId: " + hearingId);
        }
        // Get the scheduled hearings for this hearing
        final Collection<XhbScheduledHearing> schedHearings = Collections.checkedCollection(XhbScheduledHearingBeanHelper2.findByHearingId(hearingId), XhbScheduledHearing.class);
        HashSet <Integer> caseIds = new HashSet <Integer>();
        // Add the original case id
        caseIds.add(caseId);
        // Get all the case ids for the linlked scheduled hearings for this
        // hearing
        for (XhbScheduledHearing schHearing : schedHearings){
            caseIds.addAll(getLinkedSchedHearings(caseId, schHearing.getScheduledHearingId()));
        }
        return caseIds;
    }

    /**
     * Move a case to an existing or a new sitting.
     * 
     * @param mCValue
     *            The move case value
     * @throws HearingScheduleException
     */
    public void moveCase(MoveCaseValue mCValue, String userDisplayName) throws HearingScheduleException {
        boolean isNewSitting = false;
        ArrayList<ScheduledHearingBasicValue> schedHearings = new ArrayList<ScheduledHearingBasicValue>();

        if (log.isDebugEnabled()) {
            log.debug("moveCase() - mCValue=" + mCValue);
        }

        try {
            Sitting sitting = null;
            ScheduledHearing sh = scheduledHearingMaintainer.findByPK(mCValue.getScheduledHearingId());

            // Get the original sitting that this scheduled hearing was
            // originally associated with.
            final Sitting originalSitting = sh.getSitting();
            final boolean wasFloating = originalSitting.getIsFloating().equals("1");

            // Sets the courtRoomId to the court court room id if it is
            // null. This
            // assumes that it is a move within the same court room.
            if (mCValue.getNewCourtRoomId() == null) {
                mCValue.setNewCourtRoomId(originalSitting.getCourtRoomId());
                mCValue.setOldCourtRoomId(originalSitting.getCourtRoomId());
            }

            log.debug("got a scheduled hearing");

            ScheduledHearingBasicValue shbv = scheduledHearingMaintainer.getScheduledHearingBasicValue(sh);
            schedHearings.add(shbv);

            Collection schedColl = scheduledHearingMaintainer.findByLinkedSchedHearingId(mCValue.getLinkedSHId());
            // populating array list with all scheduled hearings (linked)
            if ((schedColl != null) && (schedColl.size() > 0)) {
                log.debug("linked scheduled hearings collection size = " + schedColl.size());
                Iterator itr = schedColl.iterator();
                while (itr.hasNext()) {
                    ScheduledHearingBasicValue sValue = scheduledHearingMaintainer
                            .getScheduledHearingBasicValue((ScheduledHearing) itr.next());
                    if (!(sValue.getId().equals(mCValue.getScheduledHearingId()))) {
                        schedHearings.add(sValue);
                    }
                }
            }

            // okay! have an array list full of scheduled hearing basic
            // values

            if (mCValue.isAdjourned() == false) {
                log.debug("case not adjourned");
                // Get Courtsite
                Integer courtSiteId = scheduleHelper.getCourtSiteId(mCValue.getNewCourtRoomId());
                log.debug("court site ID = " + courtSiteId + " checking for sitting...");
                // Get sitting for that time...
                sitting = scheduleHelper.sittingExists(mCValue.getNewHearingTime(), mCValue.getNewCourtRoomId(),
                        courtSiteId);
                if (sitting != null) {
                    log.debug("Sitting PK = " + sitting.getSittingId());
                }

                if (sitting == null) {
                    log.debug("creating new sitting");
                    if (isValidHearingTime(mCValue)) {
                        // Create a new sitting
                        sitting = scheduleHelper.createSitting(mCValue.getCourtId(), courtSiteId, mCValue
                                .getNewCourtRoomId(), mCValue.getNewHearingTime(), userDisplayName);
                        log.debug("new sitting created id=" + sitting.getPrimaryKey());
                        isNewSitting = true;
                    } else {
                        String errMsg = "No valid time available for creation of new sitting";
                        throw new InvalidHearingTimeException(INVALID_HEARING_TIME, errMsg);
                    }
                }

                sh.setSitting(sitting);
                //
                if (schedColl != null) {
                    Iterator itr = schedColl.iterator();
                    while (itr.hasNext()) {
                        ScheduledHearing shear = (ScheduledHearing) itr.next();
                        shear.setSitting(sitting);
                    }
                }

                // Set time...
                for (int i = 0; i < schedHearings.size(); i++) {
                    shbv = schedHearings.get(i);
                    shbv.setNotBeforeTime(mCValue.getNewHearingTime());
                    // set moved from: this will need to be changed when we
                    // know exactly what value is suppose to go into the
                    // table
                    // but for the moment
                    if (!wasFloating) {
                        setMovedFrom(mCValue, shbv);
                    }
                    log.debug("value = " + shbv.toString());
                }

                // Set sequence number...
                if (isNewSitting) {
                    log.debug("setting sequence numbers for new sitting");
                    for (int i = 0; i < schedHearings.size(); i++) {
                        log.debug("sequence numbers loop number " + i);
                        shbv = schedHearings.get(i);
                        shbv.setSequenceNo(new Integer(i + 1));
                    }
                } else {
                    log.debug("setting sequence numbers for existing sitting");
                    for (int i = 0; i < schedHearings.size(); i++) {
                        shbv = schedHearings.get(i);
                        Integer newSequenceNumber = scheduleHelper.adjustSHSequenceNumbers(sitting, shbv, userDisplayName);
                        shbv.setSequenceNo(newSequenceNumber);
                    }
                }

                log.debug("seq numbers set");
            } else {
                for (int i = 0; i < schedHearings.size(); i++) {
                    shbv = schedHearings.get(i);
                    shbv.setHearingProgress(HearingProgressValue.ADJOURNED);
                }
            }

            updateStaff(originalSitting, sitting, mCValue, schedHearings, userDisplayName);

            // Update the sched Hearing entity
            for (int i = 0; i < schedHearings.size(); i++) {
                shbv = schedHearings.get(i);
                log.debug("schedhearing=" + shbv.toString());

                scheduledHearingMaintainer.update(shbv, userDisplayName);
            }

            // Remove SH reference from old court room from CRLiveStatus
            updateCRLiveStatus(mCValue);

            // deQActivate SH publicDisplay flag
            deActivatePublicDisplayForMovedCase(mCValue, userDisplayName);

            // tell public displays that the case has been moved
            notifyNewPublicDisplays(mCValue, sh, wasFloating, userDisplayName);
        } catch (ObjectNotFoundException onfe) {
            CSServices.getDefaultErrorHandler().handleError(onfe, getClass(), onfe.toString());
            throw new HearingScheduleException("", onfe.toString(), onfe);
        }
    }

    // /**
    // * Prepare a public display event for bench warrant hearing to notify
    // the new
    // * public displays
    // * @param hearingValue
    // * @deprecated - use notifyNewPublicDisplays(AddHearingValue)
    // */
    // private void notifyNewPublicDisplays(BWHearingValue hearingValue)
    // {
    // PublicDisplayEvent pde = null;
    // Integer courtId = hearingValue.getCourtId();
    // Integer courtRoomId = hearingValue.getCourtRoomId();
    //
    // // Default case active to false as this is a new case and can not
    // // be added to the schedule active
    // CaseChangeInformation cci = new CaseChangeInformation(false);
    //
    // pde = new AddCaseEvent(
    // new CourtRoomIdentifier(courtId, courtRoomId),
    // cci);
    //
    // notifyNewPublicDisplays(pde);
    // }

    /**
     * Prepare a public display event for add hearing to notify the new public
     * displays
     * 
     * @param hearingValue
     */
    private void notifyNewPublicDisplays(AddHearingValue addHearingValue, String userDisplayName) {
        PublicDisplayEvent pde = null;
        Integer courtId = addHearingValue.getCourtId();
        Integer courtRoomId = addHearingValue.getCourtRoomId();

        // Default case active to false as this is a new case and can not
        // be added to the schedule active
        CaseChangeInformation cci = new CaseChangeInformation(false);

        String courtName = getCourtName(courtId);
		Integer courtRoomNo = getCourtRoomNumber(courtRoomId);
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtRoomId);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        pde = new AddCaseEvent(new CourtRoomIdentifier(courtId, courtRoomId, courtName, courtRoomNo, publicNotices), cci);

        notifyNewPublicDisplays(pde, userDisplayName);
    }

    /**
     * Prepare a public display event for move case to notify the new public
     * displays
     * 
     * @param mCValue
     * @param sh
     * @param wasCaseFloating
     */
    private void notifyNewPublicDisplays(MoveCaseValue mCValue, ScheduledHearing sh, boolean wasCaseFloating, String userDisplayName) {
        PublicDisplayEvent pde = null;
        Integer courtId = mCValue.getCourtId();
        Integer oldCourtRoomId;
        if (wasCaseFloating) {
            // oldCourtRoomId = new Integer(DisplayDocumentURI.UNASSIGNED);
            oldCourtRoomId = new Integer(Integer.MAX_VALUE);
        } else {
            oldCourtRoomId = mCValue.getOldCourtRoomId();
        }
        Integer newCourtRoomId = mCValue.getNewCourtRoomId();

        CaseChangeInformation cci = new CaseChangeInformation(sh.getIsCaseActive().equals("Y"));
        
        String courtName = getCourtName(courtId);
		Integer oldCourtRoomNo = getCourtRoomNumber(oldCourtRoomId);
		Integer newCourtRoomNo = getCourtRoomNumber(newCourtRoomId);

		try {
			DisplayablePublicNoticeValue[] publicNotices = null;
		
	        if (mCValue.isAdjourned()) {
	        	publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(oldCourtRoomId);
	            pde = new HearingStatusEvent(new CourtRoomIdentifier(courtId, oldCourtRoomId, courtName, oldCourtRoomNo, publicNotices), cci);
	        } else {
	        	publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(newCourtRoomId);
	            pde = new MoveCaseEvent(new CourtRoomIdentifier(courtId, oldCourtRoomId, courtName, oldCourtRoomNo, publicNotices),
	            		new CourtRoomIdentifier(courtId, newCourtRoomId, courtName, newCourtRoomNo, publicNotices), cci);
	        }
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the old or new court room id.");
		}

        notifyNewPublicDisplays(pde, userDisplayName);
    }

    /**
     * This is the new method used to notify public displays of either a move
     * case event or a hearing status event.
     * 
     * @param mCValue
     *            The Value object representing the moving or adjourning of a
     *            case.
     * @param sh
     *            The entity bean representing the scheduled hearing in
     *            question.
     */
    private void notifyNewPublicDisplays(PublicDisplayEvent pde, String userDisplayName) {
        PddaHelper pddaHelper = new PddaHelper();
        pddaHelper.sendMessage(pde, userDisplayName);
        pddaHelper.close();
    }

    /**
     * Returns true if the movecasevalue has a new hearing time or the scheduled
     * hearing has a not before time In the latter case, the new hearing time is
     * set to the not before time of the sh.
     * 
     * @param mCValue
     * @return
     */
    private boolean isValidHearingTime(MoveCaseValue mCValue) {
        log.debug("isValidHearingTime() start");
        boolean isValid = false;
        try {
            if (mCValue.getNewHearingTime() == null) {
                ScheduledHearing sh = scheduledHearingMaintainer.findByPK(mCValue.getScheduledHearingId());
                Timestamp ts = sh.getNotBeforeTime();
                log.debug("new hearing time null: existing sched hearing time=" + ts);
                if (ts != null) {
                    mCValue.setNewHearingTime(new Date(ts.getTime()));
                    isValid = true;
                }
            } else {
                isValid = true;
            }
        } catch (ObjectNotFoundException e) {
            // someting unexpected
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
        }

        return isValid;
    }

    /**
     * @param caseNumber
     * @param caseType
     * @param courtId
     * @param hearingType
     * @return
     * @throws HearingScheduleException
     * 
     */
    public CaseInfoValue getCaseDetails(Integer caseNumber, String caseType, Integer courtId, String hearingType)
            throws HearingScheduleException {
        CaseInfoValue caseInfoValue = null;
        try {
            log.debug("getCaseDetails() - called");

            if (log.isDebugEnabled()) {
                log.debug("getCaseDetails() - Finding case " + caseNumber + caseType + " Court: " + courtId);
            }
            
         /*   
          * Removed as part of CTX -404
          * Code only commented out since mercator calls are supposed to be commented out not removed.
          * Remove at some point in the future.
          * // Does case exist?
            if (scheduleHelper.caseExists(caseNumber, caseType, courtId) == false) {
                log.debug("*** Case does NOT Exist ***");
                AddCaseValue acv = new AddCaseValue();
                acv.setCaseNumber(caseNumber);
                acv.setCaseType(caseType);
                acv.setCourtID(courtId);
                acv.setHearingType(hearingType);
                IntegrationFacade intFacade = IntegrationFacadeFactory.getInstance().getIntegrationFacade();
                log.debug("*** BEFORE INT GETCASE ***");
                intFacade.getCase(acv);
                log.debug("*** AFTER INT GETCASE ***");
                log.debug("*** About to throw MercatorException ***");
                //throw new MercatorException();
            //TEMP CODE: PR59196
            }else{
                log.debug("*** Case Exists ***"); 
            }*/
            
            // Get Case details
            final Case myCase = caseMaintainer.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
            CaseBasicValue cbv = caseMaintainer.getCaseBasicValue(myCase);
            Integer caseId = cbv.getId();
            // Get case defendants
            CaseControllerLocal caseController = (CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                    CaseControllerLocalHome.class);
            Collection collDefendants = caseController.getDefendants(caseId);
            if (log.isDebugEnabled()) {
                log.debug("getCaseDetails() - Retrieved " + collDefendants.size() + " defendants");
            }

            // Create a VO
            caseInfoValue = new CaseInfoValue();
            caseInfoValue.setCaseBasicValue(cbv);
            caseInfoValue.setDefendants(collDefendants);
        } catch (CaseControllerException e) {
            handleWrapAndRethrowException(e);
        } catch (DefendantControllerException e) {
            handleWrapAndRethrowException(e);
        } catch (ObjectNotFoundException onfe) {
            CSServices.getDefaultErrorHandler().handleError(onfe, getClass(), onfe.toString());
            throw new HearingScheduleException("hearingschedule.linkhearing.case_not_found", onfe.toString(), onfe);
        }

        return caseInfoValue;
    }

    /**
     * Utility method to deal with handling Exceptions from other controllers,
     * and wrapping and rethrowing them as HearingScheduleException
     * 
     * This method always throws a <code>HearingScheduleException</code>.
     * 
     * @param ex
     *            The Exception to handle and rethrow, must be a
     *            <code>CSBusinessException</code> or child of.
     * @throws HearingScheduleException
     *             the new Exception to throw
     */
    private void handleWrapAndRethrowException(CSBusinessException ex) throws HearingScheduleException {
        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
        if (ex.getUserMessageAsMessage().getParameters().length > 0) {
            throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getUserMessageAsMessage()
                    .getParameters(), ex.getMessage(), ex);
        }

        throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
    }

    /**
     * @param AddCaseValue
     * @return AddCaseValue
     * @throws HearingScheduleException
     */
    public CaseBasicValue createNewUCase(AddCaseValue addCaseValue, String userDisplayName) throws HearingScheduleException {
    	 try {
             CaseBasicValue cs = new CaseBasicValue();
             cs.setCaseTitle(addCaseValue.getCaseTitle());
             cs.setCaseType(addCaseValue.getCaseType());
             cs.setCourtID(addCaseValue.getCourtID());
             cs.setChargeImportIndicator(CHARGE_IMPORT_INDICATOR_TYPE_O);
             cs.setDefaultHearingType(addCaseValue.getHearingTypeId());
             
         	final String caseNumber = GenerateCaseNumberControllerBeanBusinessDelegate.DelegateFactory.getInstance()
 					.generateCaseNumber(cs.getCourtID(), cs.getCaseType());
         	
          	cs.setCaseNumber(Integer.parseInt(caseNumber.substring(1)));
     	
         	final Case caze = caseMaintainer.createCase(cs, userDisplayName);
         	final CaseBasicValue caseBasic = caseMaintainer.getCaseBasicValue(caze);
     	
            return caseBasic;
         } catch(SQLException e){
        	 CSServices.getDefaultErrorHandler().handleError(e, getClass());
             throw new EJBException(e);
         }
    	 catch (final EJBException e) {
         	CSServices.getDefaultErrorHandler().handleError(e, getClass());
             throw e;
         }
    }

    /**
     * @param courtRoomId
     * @param sittingTime
     * @return
     * @throws HearingScheduleException
     */
    public SittingInfoValue getSittingDetails(Integer courtRoomId, Date sittingTime) throws HearingScheduleException {
        SittingInfoValue sittingInfoValue = new SittingInfoValue();

        if (log.isDebugEnabled()) {
            log.debug("getSittingDetails() - called :: courtRoomId: " + courtRoomId + " sittingTime: " + sittingTime);
        }

        // Get the courtsite from courtroom
        Integer courtSiteId = scheduleHelper.getCourtSiteId(courtRoomId);
        if (log.isDebugEnabled()) {
            log.debug("getSittingDetails() - CourtSiteId: " + courtSiteId);
        }

        // Lookup the sitting for this time
        Sitting sitting = scheduleHelper.sittingExists(sittingTime, courtRoomId, courtSiteId);
        if (sitting == null) {
            HearingScheduleException hse = new HearingScheduleException("", "No Sitting found");
            CSServices.getDefaultErrorHandler().handleError(hse, getClass(), hse.toString());
            throw hse;
        }

        if (log.isDebugEnabled()) {
            log.debug("getSittingDetails() - Sitting Id: " + sitting.getSittingId());
        }

        SittingBasicValue sittingBasicValue = sittingMaintainer.getSittingBasicValue(sitting);

        // Find the Judge .. from ref data...
        String judgeName = scheduleHelper.getJudgeName(sitting.getRefJudgeId());

        // Build the VO
        sittingInfoValue.setSittingBasicValue(sittingBasicValue);
        sittingInfoValue.setJudgeName(judgeName);

        // Return the VO...
        log.debug("getSittingDetails() - Exited OK");

        return sittingInfoValue;
    }

    /**
     * @param schedHearingIds
     * @return
     * @throws HearingScheduleException
     */
    public ScheduledHearingValue[] getScheduledHearings(Integer[] schedHearingIds) throws HearingScheduleException {
        return todaysScheduleHelper.getScheduledHearings(schedHearingIds);
    }

    /**
     * Description of the Method
     * 
     * @param scheduledHearingId
     *            Description of the Parameter
     * @param hearingProgress
     *            Description of the Parameter
     * @exception HearingScheduleException
     *                Description of the Exception
     */
    public void updateHearingProgress(Integer scheduledHearingId, Integer hearingProgress, String userDisplayName)
            throws HearingScheduleException {
        scheduleHelper.updateHearingProgress(scheduledHearingId, hearingProgress, userDisplayName);
    }

    /**
     * setMovedFrom
     * 
     * @param mCValue
     *            The new movedFrom value
     * @param value
     *            The new movedFrom value
     * @exception ObjectNotFoundException
     *                Description of the Exception
     * @throws HearingScheduleException
     */
    private void setMovedFrom(MoveCaseValue mCValue, ScheduledHearingBasicValue value) throws ObjectNotFoundException {
        log.debug("setMovedFrom() start");
        Integer oldCourtRoomId = mCValue.getOldCourtRoomId();

        Integer newCourtRoomId = mCValue.getNewCourtRoomId();

        if (!oldCourtRoomId.equals(newCourtRoomId)) {
            CourtRoom crtRoom = courtRoomMaintainer.findByPrimaryKey(oldCourtRoomId);
            log.debug("Crt Room = " + crtRoom.getCourtRoomName());
            value.setMovedFrom(crtRoom.getCourtRoomName());
            value.setMovedFromCourtRoomId(oldCourtRoomId);
        }
    }

    /**
     * Update the staff
     * 
     * @param originalSitting
     *            The sitting the case was originally being heard in
     * @param sitting
     *            The sitting we are moving the case to.
     * @param mCValue
     *            The move case value
     * @param scheduledHearingValues
     *            list of scheduledHearingValues
     * @exception ObjectNotFoundException
     */
    private void updateStaff(Sitting originalSitting, Sitting sitting, MoveCaseValue mCValue,
            ArrayList scheduledHearingValues, String userDisplayName) throws ObjectNotFoundException {
        SchedHearingAttendee attendee = null;

        for (int i = 0; i < scheduledHearingValues.size(); i++) {
            ScheduledHearingBasicValue shbv = (ScheduledHearingBasicValue) scheduledHearingValues.get(i);

            // get the local reference for the scheduled hearing
            ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(shbv.getId());

            if (log.isDebugEnabled()) {
                log.debug("sheduled hearing basic value = " + shbv.toString());
            }

            // ******************************************************************************
            // Pete - the interpretation of the requirements for this has
            // changed
            // but I'm leaving the code below in for the moment as this may
            // change.
            // // update the schedule hearing with the judge from the
            // // sitting which it is being moved to.
            // if (sitting != null && !(mCValue.isUseExistingJudge())) {
            // log.debug("use existiing judge = false");
            // //get judge id of sitting where case has been moved to
            // //if no judge then nothing to populate in attendee table
            // Integer sittingJudge = sitting.getRefJudgeId();
            // log.debug("updating judges ----judgeID = " + sittingJudge);
            // if (sittingJudge != null) {
            // shav = scheduleHelper.populateAttendeeValue(shav,
            // sittingJudge, shbv.getId(), PersonValue.JUDGE);
            // log.debug("shav value object = " + shav.toString());
            // attendee = (SchedHearingAttendee)
            // attendeeMaintainer.create(shav);
            // attendee.setScheduledHearing((ScheduledHearing)
            // scheduledHearingMaintainer.findByPK(shbv.getId()));
            // log.debug("created attendee record");
            // }
            // }
            // ******************************************************************************
            if (sitting != null && !(mCValue.isUseExistingJudge())) {
                log.debug("use existing judge = false");
                attendee = getScheduledHearingAttendee(scheduledHearing, shbv, PersonValue.JUDGE);
                if (attendee != null) {
                    removeSHAttendee(attendee);
                }

                // If the sitting we are moving the case to has a judge then
                // assign that judge
                if (sitting.getRefJudgeId() != null) {
                    addJudgeToSchedHearing(sitting.getRefJudgeId(), shbv, userDisplayName);
                }
            } else if (sitting != null && mCValue.isUseExistingJudge()) {
                // keep the original judge for the scheduled hearing
                updateWithExistingJudge(scheduledHearing, shbv, originalSitting, userDisplayName);
            }

            // added by Pete
            updateCourtClerk(mCValue, scheduledHearing, shbv);
            updateCourtReporter(mCValue, scheduledHearing, shbv);
        }
    }

    /**
     * Remove a scheduledheairng attendee. This is achieved by deleting the
     * rquired rwo from the scheduledhearing attendee table. When changes are
     * made to the shattendee table to track times then this can be changed to a
     * logical deletion.
     * 
     * @param attendee
     *            the SchedHearingAttendee to remove
     */
    private void removeSHAttendee(SchedHearingAttendee attendee) throws ObjectNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("removeSHAttendee() start - removing shattendee id=" + attendee.getPrimaryKey() + " type="
                    + attendee.getAttendeeType());
        }

        // judge seems to be a special case. ShJudge has a referential
        // constraint link
        // to SchedHearingAttendee whereas CourtReporter and SHStaff do not. So
        // where a link exists between ShJudge and SchedHearingAttendee
        // the SHJudge needs to be deleted as well. This would also be the case
        // when crest form A is crated and the judge is a deputy.

        if (attendee.getAttendeeType().equals(PersonValue.JUDGE)) {
            Integer shAttendeeId = (Integer) attendee.getPrimaryKey();
            log.debug("deleting a judge, finding sh_judge" + " with shattendee id=" + shAttendeeId);

            try {
                ShJudge shJudge = shJudgeMaintainer.findByShAttendeeId(shAttendeeId);
                shJudgeMaintainer.delete((Integer) shJudge.getPrimaryKey(), shJudge.getVersion());
            } catch (ObjectNotFoundException e) {
                // this is expected so no error log
                log.debug("No shJudge fround with shAttendeeId=" + shAttendeeId);
            }
        }
        log.debug("deleting shAttendee");
        attendeeMaintainer.delete((Integer) attendee.getPrimaryKey(), attendee.getVersion());
    }

    /**
     * Checks if the court reporter needs to be moved with the scheduled hearing
     * if not the link through the scheduled hearing attendee is broken. Court
     * reporter is synonymous with short hand writer
     * 
     * @param moveCaseVal
     * @param shbv
     */
    private void updateCourtReporter(MoveCaseValue moveCaseVal, ScheduledHearing scheduledHearing,
            ScheduledHearingBasicValue shbv) {
        log.debug("updateCourtReporter() start");
        if (moveCaseVal.isUseExistingSHWriter() == false) {
            log.debug("removing court reporter off scheduled hearing id=" + shbv.getId());
            try {
                SchedHearingAttendee attendee = getScheduledHearingAttendee(scheduledHearing, shbv,
                        PersonValue.COURT_REPORTER);
                if (attendee != null) {
                    removeSHAttendee(attendee);
                }
            } catch (ObjectNotFoundException e) {
                String errMsg = "No court reporter found duing move case even though "
                        + " move requested. moveCaseValue=" + moveCaseVal + " scheduledHearingValue=" + shbv;

                CSServices.getDefaultErrorHandler().handleError(e, getClass(), errMsg);
                throw new CSUnrecoverableException(errMsg, e);
            }
        } else {
            log.debug("moveCaseVal.isUseExistingSHWriter() == true");
        }
    }

    /**
     * Checks if the court clerk needs to be moved with the scheduled hearing if
     * not the link through the scheduled hearing attendee is broken
     * 
     * @param moveCaseVal
     * @param shbv
     */
    private void updateCourtClerk(MoveCaseValue moveCaseVal, ScheduledHearing scheduledHearing,
            ScheduledHearingBasicValue shbv) {
        log.debug("updateCourtClerk() start");
        if (moveCaseVal.isUseExistingCourtClerk() == false) {
            log.debug("removing court clerk off scheduled hearing id=" + shbv.getId());
            try {
                SchedHearingAttendee attendee = getScheduledHearingAttendee(scheduledHearing, shbv,
                        PersonValue.COURT_CLERK);
                if (attendee != null) {
                    removeSHAttendee(attendee);
                }
            } catch (ObjectNotFoundException e) {
                String errMsg = "No court clerk found duing move case even though " + " move requested. moveCaseValue="
                        + moveCaseVal + " scheduledHearingValue=" + shbv;

                CSServices.getDefaultErrorHandler().handleError(e, getClass(), errMsg);
                throw new CSUnrecoverableException(errMsg, e);
            }
        } else {
            log.debug("moveCaseVal.isUseExistingCourtClerk() == false");
        }
    }

    private SchedHearingAttendee getScheduledHearingAttendee(ScheduledHearing scheduledHearing,
            ScheduledHearingBasicValue shbv, String personType) throws ObjectNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("getScheduledHearingAttendee() start shbv=" + shbv + " personType=" + personType);
        }

        // get the local references for all the scheduled hearing attendees
        Collection attendees = scheduledHearing.getScheduledHearingAttendee();
        log.debug("attendees size=" + attendees.size());

        // find the court clerk currently assigned to this scheduled
        // hearing and remove from
        for (Iterator i = attendees.iterator(); i.hasNext();) {
            SchedHearingAttendee attendee = (SchedHearingAttendee) i.next();
            log.debug("checking attendeeId=" + attendee.getPrimaryKey());
            if ((attendee.getScheduledHearingId().equals(shbv.getId()))
                    && (attendee.getAttendeeType().equalsIgnoreCase(personType))) {
                log.debug("found attendeeId=" + attendee.getPrimaryKey());
                return attendee;
            }
        }

        // return null if no attendee found...
        return null;
    }

    /**
     * Update the scheduled hearing the the appropriate ref judge in the
     * attendee table. If the jugde is already in the attendees then no work to
     * be done. If the scheduled hearing no judge in the attendence then judge
     * is taken from original sitting. Else nothing can be done.
     * 
     * @param scheduledHearing
     *            Description of the Parameter
     * @param scheduledHearingBasicValue
     *            Description of the Parameter
     * @param originalSitting
     *            The sitting the case was originally being heard in
     * @exception ObjectNotFoundException
     *                Description of the Exception
     */
    private void updateWithExistingJudge(ScheduledHearing scheduledHearing,
            ScheduledHearingBasicValue scheduledHearingBasicValue, Sitting originalSitting, String userDisplayName)
            throws ObjectNotFoundException {
        log.debug("use existing judge true");

        // first try attendees
        Integer refJudgeId = scheduleHelper.getExistingJudgeFromAttendees(scheduledHearing);

        // If reference judge is already an attendee then no
        // need to create a new attendee.
        if (refJudgeId != null) {
            return;
        }

        // try retrieving refJudge from the original sitting
        refJudgeId = originalSitting.getRefJudgeId();

        // PFox: If there was no judge already associated with either
        // attendees or sittings then we can't set anything.
        if (refJudgeId != null) {
            log.debug("updating judges ----judgeID = " + refJudgeId);

            addJudgeToSchedHearing(refJudgeId, scheduledHearingBasicValue, userDisplayName);
        } else {
            log.debug("No Judge Retrieved no Attendees updated " + refJudgeId);
        }
    }

    /**
     * Add a judge to the scheduledHearing by adding a SchedHearingAttendee
     * 
     * @param refJudgeId
     * @param scheduledHearingBasicValue
     */
    private void addJudgeToSchedHearing(final Integer refJudgeId,
            final ScheduledHearingBasicValue scheduledHearingBasicValue, String userDisplayName) throws ObjectNotFoundException {
        final SchedHearingAttendeeBasicValue schedHearingAttendeeBasicValue = scheduleHelper.populateAttendeeValue(
                refJudgeId, scheduledHearingBasicValue.getId(), PersonValue.JUDGE);

        log.debug("schedHearingAttendeeBasicValue value object = " + schedHearingAttendeeBasicValue.toString());

        attendeeMaintainer.create(schedHearingAttendeeBasicValue, scheduledHearingMaintainer
                .findByPK(scheduledHearingBasicValue.getId()), userDisplayName);

        log.debug("created attendee record");
    }

    /**
     * Calls appropriate CourtLogMessagingHelper method to clear CRLiveStatus
     * information of Schedule Hearing information.
     * 
     * @param mcValue
     *            Description of the Parameter
     */
    private void updateCRLiveStatus(MoveCaseValue mcValue) {
        CrLiveStatusHelper.moveCaseFromRoom(mcValue.getScheduledHearingId(), mcValue.getOldCourtRoomId());
    }

    /**
     * Calls appropriate scheduledHearingMaintainer methods to deactivate SH.
     * 
     * @param mcValue
     */
    private void deActivatePublicDisplayForMovedCase(MoveCaseValue mcValue, String userDisplayName) throws ObjectNotFoundException {
        ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(mcValue.getScheduledHearingId());
        ScheduledHearingBasicValue schedHearBv = scheduledHearingMaintainer
                .getScheduledHearingBasicValue(scheduledHearing);
        schedHearBv.setIsCaseActive(Boolean.FALSE);
        scheduledHearingMaintainer.update(schedHearBv, userDisplayName);
    }

    /**
     * Creates a new sitting using an AddHearingValue
     * 
     * @param addHearingValue
     * @param courtSiteId
     * @param sittingSequenceNumber
     * @return the Sitting that has been created
     * @throws HearingScheduleException
     */
    private Sitting createSitting(AddHearingValue addHearingValue, Integer courtSiteId, int sittingSequenceNumber, String userDisplayName)
            throws HearingScheduleException {
        // 1. Today's hearing list...
        // MH - need to get the date with no time set since new Date() will
        // give the current time as well.
        HearingList hearingList = null;
        try {
            hearingList = hearingListMaintainer.findByCourtIdAndDate(addHearingValue.getCourtId(), ScheduleHelper
                    .getStartOfDay());
            log.debug("Found hearinglist with id :" + hearingList.getListId());
        } catch (ObjectNotFoundException onfe) {
            throw new HearingScheduleException("addHearing.hearinglistnotfound", onfe.toString(), onfe);
        }

        // 2. Basic VO...
        SittingBasicValue sbv = scheduleHelper.createSittingVO(addHearingValue.getCourtRoomId(), courtSiteId,
                addHearingValue.getNotBeforeTime(), hearingList.getListId());
        // MH - The sitting is not a floating sitting.
        sbv.setIsFloating("0");

        // Need to get the correct sequence number...
        sbv.setSittingSequenceNo(new Integer(sittingSequenceNumber));

        // Set the new judge
        log.debug("Setting the new judge : " + addHearingValue.getRefJudgeID());
        sbv.setRefJudgeID(addHearingValue.getRefJudgeID());

        // 3. Create sitting...
        Sitting sitting = (Sitting) sittingMaintainer.create(sbv, userDisplayName);

        // MH - set the CMR
        sitting.setHearingList(hearingList);

        return sitting;
    }

    /**
     * Updates the sequence number of sittings later than the one we have
     * created
     * 
     * @param sittings
     *            The sittings for the court room and date
     * @param sittingSequenceNumber
     *            The sequence number of the sitting we created
     */
    private void updateSittingSeqNums(Collection sittings, int sittingSequenceNumber) {
        log.debug("updateSittingSeqNums called using sittingSequenceNumber:" + sittingSequenceNumber);
        final Iterator it = sittings.iterator();

        while (it.hasNext()) {
            final Sitting sitting = (Sitting) it.next();
            final int thisSeqNum = sitting.getSittingSequenceNo().intValue();
            log.debug("sittingId:" + sitting.getSittingId());
            log.debug("thisSeqNum:" + thisSeqNum);

            if (thisSeqNum >= sittingSequenceNumber) {
                log.debug("Need to update sittingSequenceNumber for this sitting.  New value is:" + (thisSeqNum + 1));
                // this sitting is later than the one we created so increment
                // the sequence number by one
                sitting.setSittingSequenceNo(new Integer(thisSeqNum + 1));
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
	
	public Integer getCourtRoomNumber(Integer courtRoomId) {
		Integer courtRoomNo = 0;
		try {
			courtRoomNo = courtRoomMaintainer.findByPrimaryKey(courtRoomId).getCrestCourtRoomNo();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court room number.");
			e.printStackTrace();
		}
		return courtRoomNo;
	}
}
