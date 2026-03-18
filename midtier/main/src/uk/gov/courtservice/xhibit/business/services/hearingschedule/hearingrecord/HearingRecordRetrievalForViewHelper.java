package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudge;
import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJusticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRVerdictValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;

/**
 * <p>
 * Title: HearingRecordRetrievalForViewHelper
 * </p>
 * <p>
 * Description: This class will be used for view hearing record. It will
 * retrieve all the hearing record information, such as hearings, defendant
 * details, case details, hearing record details, court reporters, legal
 * representatives etc.
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
public class HearingRecordRetrievalForViewHelper {
    private static final Logger log = CSServices.getLogger(HearingRecordRetrievalForViewHelper.class);

    // Value Objects (or Collections of them) that will be populated
    private ExportAValue exportAValue = null;

    private HRCaseValue hrCaseValue = null;

    private HRDefendantValue defendantValue = null;

    private DefHearingRecordValue defHearingRecordValue = null;

    private HRJudgeValue hrJudgeValue = new HRJudgeValue();

    private HRHearingDisplayValue hearingDisplayValue = null;

    private HearingRecordDisplayValue displayValue = null;

    private HearingRecordUpdateValue updateValue = null;

    private HearingRecordValue hearingRecordValue = null;

    private HRSHJudgeValue hrShJudgeValue = new HRSHJudgeValue();

    private HearingBasicValue hearingBasicValue = null;

    private DirectionsForCaseValue directionsForCaseValue = null;

    private HRVerdictValue hrVerdictValue = new HRVerdictValue();

    private Collection counselValues = new Vector();

    private Collection hrLinkedCaseValues = new Vector();

    private Vector shLegReps = new Vector();

    private Vector scheuledHearings = new Vector();

    private Vector hrJustices = new Vector();

    // Other helpers and maintainers required, only set in the constructor
    private final HRLinkedCaseListValueHelper linkedCaseListHelper;

    private final HearingRecordStatusHelper statusHelper;

    private final HRCaseValueHelper caseValueHelper;

    private final HRDefendantValueHelper defendantHelper;

    private final DefHearingRecordValueHelper defHearingRecordHelper;

    private final HRSHLegRepValueHelper legRepHelper;

    private final HRCounselValueHelper counselHelper;

    private final HRCourtReporterValueHelper courtReporterHelper;

    private final HRJudgeValueHelper judgeHelper;

    private final HRSHJudgeValueHelper hrShJudgeHelper;

    private final HRJusticeValueHelper justiceHelper;

    private final HearingMaintainer hearingMaintainer;

    private final ShJusticeMaintainer shJusticMaintainer;

    private final SchedHearingAttendeeMaintainer shaMaintainer;

    private final ScheduledHearingMaintainer shMaintainer;

    private final ShJudgeMaintainer shJudgeMaintainer;

    private final SchedHearingDefendantMaintainer shdMaintainer;

    // the hearings start date.
    private Date startDate = null;

    // Defendant on case id that is required in several places.
    private Integer defendantOnCaseID = null;

    // unique set of the caseids that are linked to the hearing.
    private HashSet uniqueCaseIDs = new HashSet();

    /*
     * This will collect the sceduledHearingAttendees when it is a judge. We
     * only
     */
    private HashSet shAttendeeIDsForJudge = new HashSet();

    /*
     * Unique map that will store the refLegRepID and an array containing legal
     * role and solFirmOrRefRegalRep since we have many shLegReps per counsel
     */
    private HashMap refLegRepsPros = new HashMap();

    private HashMap refLegRepsDefn = new HashMap();

    /**
     * Default constructor that intantiate helpers.
     */
    public HearingRecordRetrievalForViewHelper() {
        linkedCaseListHelper = new HRLinkedCaseListValueHelper();
        statusHelper = new HearingRecordStatusHelper();
        caseValueHelper = new HRCaseValueHelper();
        legRepHelper = new HRSHLegRepValueHelper();
        courtReporterHelper = new HRCourtReporterValueHelper();
        judgeHelper = new HRJudgeValueHelper();
        hrShJudgeHelper = new HRSHJudgeValueHelper();
        justiceHelper = new HRJusticeValueHelper();
        defHearingRecordHelper = new DefHearingRecordValueHelper();
        defendantHelper = new HRDefendantValueHelper();
        counselHelper = new HRCounselValueHelper();
        hearingMaintainer = new HearingMaintainer();
        shMaintainer = new ScheduledHearingMaintainer();
        shJusticMaintainer = new ShJusticeMaintainer();
        shJudgeMaintainer = new ShJudgeMaintainer();
        shaMaintainer = new SchedHearingAttendeeMaintainer();
        shdMaintainer = new SchedHearingDefendantMaintainer();
    }

    /**
     * This is the public method that will build all Value Objects for view
     * Hearing Record. It will return a HearingRecordValue that contains
     * HearingRecordUpdateValue and HearingRecordDisplayValue. These then
     * contain several other smaller value objects.
     * 
     * @param hearingID
     *            Integer
     * @param defendantID
     *            Integer
     * @return HearingRecordValue
     * @throws HearingRecordRetrievalException
     * @throws HearingRecordException
     */
    public HearingRecordValue retrieveHearingRecord(Integer hearingID, Integer defendantID, String userDisplayName)
            throws HearingRecordRetrievalException, HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.retrieveHearingRecord(Integer hearingID,"
                + " Integer defendantID) called");

        // Hearing entity
        Hearing hearing = this.findHearing(hearingID);

        // ------------------------------- Hearing details
        // --------------------------
        hearingBasicValue = this.getHearingBasicValue(hearing);

        log.debug("Got the hearing basic value with id: " + hearingBasicValue.getId().toString());

        // -------------------------- Linked Hearing details
        // ------------------------
        // Get all the linked case ids but not the one for the main hearing
        // with the hearingID passed in and set the Collection for
        // HrLinkedCaseValues
        this.setHrLinkedCaseValues(hearingID, hearingBasicValue.getLinkedHearingID());

        // ----------------------------- ExportA details
        // ----------------------------
        // Set the ExportAValue if the hearing has been exported (or attempted)
        // previously
        this.setExportAValue(hearingBasicValue);

        // ------------------------------- Case details
        // -----------------------------
        // Set the HrCaseValue
        this.setHrCaseValue();

        // -------------------------- DirectionsForCase details
        // ---------------------
        // set the DirectionsForCase Value
        this.setDirectionsForCaseValue();

        // --------------------------- Defendant details
        // ----------------------------
        // Set the HRDefendantValue
        this.setHrDefendantValue(defendantID);

        // ------------------------ DefHearingRecord details
        // ------------------------
        // Set the DefHearingRecordValue
        this.setDefHearingRecordValue(hearingID);

        // ------------------------ ScheduledHearings details
        // -----------------------
        // Work through all the scheduled hearings and set all the required
        // values on a scheduled hearing level.
        this.setScheduledHearingValues(hearingBasicValue.getId());

        // Only change the start date if currently set to null
        if (defHearingRecordValue.getHearingStartDate() == null) {
            defHearingRecordValue.setHearingStartDate(startDate);
        }

        // --------- Judge details (both HrJudgeValue and HRSHJudgeValue)
        // -----------
        // this will set the last judge in the set.
        this.setJudgeValues(userDisplayName);

        // ------------------------------ Counsel Details
        // ---------------------------
        // this will set all the unique counsel values.
        this.setHrCounselValues();

        // ------------------------------ Case Verdict
        // ---------------------------
        // Find the case verdict code.
        this.setCaseVerdict();

        // -------------------------- Hearing Display Value
        // -------------------------
		try {
			hearingDisplayValue = this.setHearingDisplayValue();
		} catch (FinderException e) {
			e.printStackTrace();
		}

        // --------------------- Hearing Record Display Value
        // -----------------------
        displayValue = this.setHearingRecordDisplayValue();

        // --------------------- Hearing Record Update Value
        // ------------------------
        updateValue = this.setHearingRecordUpdateValue(hrCaseValue.getCaseType());

        // ---------------------- Hearing Record Top Value
        // --------------------------
        hearingRecordValue = this.setHearingRecordValue();
        log.debug("HearingRecordRetrievalForViewHelper.retrieveHearingRecord(Integer hearingID,"
                + " Integer defendantID) finished");
        return hearingRecordValue;
    }

    /**
     * This method will set the HRJudgeValue and the HRSHJudgeValue from the
     * last entered SchedHearingAttendee.
     * 
     * @throws HearingRecordException
     */
    private void setJudgeValues(String userDisplayName) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setJudgeValues() called");
        if (!shAttendeeIDsForJudge.isEmpty()) {
            Iterator it = shAttendeeIDsForJudge.iterator();
            // temporary integer to store the highest integer.
            Integer highestShAttId = new Integer(-1);

            // loop through to get the highest shAttendee id.
            while (it.hasNext()) {
                Integer shAttID = (Integer) it.next();
                if (shAttID.intValue() > highestShAttId.intValue()) {
                    highestShAttId = new Integer(shAttID.intValue());
                }
            }
            log.debug("HighestShAttId : " + highestShAttId.toString());
            // some defensive coding
            if (highestShAttId.intValue() == -1) {
                // this indicates an unexpected error
                throw new CSUnrecoverableException("Unexpected Error trying to find the highest shAttendee id");
            }
            // get the attendee entity with the highest id
            SchedHearingAttendee attendee = this.getShAttendeeByPK(highestShAttId);
            // set the HrJudgeValue
            this.setHrJudgeValue(attendee.getRefJudgeId());
            // set the HrShJudgeValue
            this.setHrShJudge(highestShAttId, attendee.getRefJudgeId(), userDisplayName);
        }
        log.debug("HearingRecordRetrievalForViewHelper.setJudgeValues() finished");
    }

    /**
     * This will find (if exist) a SchedHearingAttendee entity (return type) by
     * primary key.
     * 
     * @param shAttID
     *            Integer
     * @return SchedHearingAttendee entity
     * @throws HearingRecordException
     */
    private SchedHearingAttendee getShAttendeeByPK(Integer shAttID) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.getShAttendeeByPK(Integer shAttID" + shAttID.toString()
                + ") called");
        try {
            // try to find the attendee by the primary key.
            SchedHearingAttendee attendee = shaMaintainer.findByPrimaryKey(shAttID);
            log.debug("HearingRecordRetrievalForViewHelper.getShAttendeeByPK(Integer shAttID) finished");
            return attendee;
        } catch (ObjectNotFoundException ex) {// The attendee could not be
            // found.
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordException(HearingRecordConstants.SHATTENDEE_NOT_FOUND, ex.getMessage(), ex);
        }
    }

    /**
     * This method will work through each Scheduled hearing and set the values
     * that are derived from scheduled hearings. For each scheduled hearing: we
     * will set the shAttenddes, court reporters, judge, justices, LegReps and
     * counsels.
     * 
     * @throws HearingRecordException
     */
    private void setScheduledHearingValues(Integer hearingId) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setScheduledHearingValues() called");

        // Get the scheduled hearings via the hearing CMR
        Collection scheduledHearings = getScheduledHearings(hearingId);

        log.debug("There are " + scheduledHearings.size() + " number of scheduled hearings found");
        // loop through all the schduled hearings.
        Iterator it = scheduledHearings.iterator();
        while (it.hasNext()) {
            // Get the entity
            ScheduledHearing schedHearing = (ScheduledHearing) it.next();
            log.debug("Process Scheduled hearing with id : " + schedHearing.toString());

            // find the shattendees.
            Collection shAttendees = shaMaintainer.findByScheduledHearingId(schedHearing.getScheduledHearingId());

            // only do all of this expensive (relatively) processing to
            // calculate
            // the appropriate start date if the one we have is null
            if (this.defHearingRecordValue.getHearingStartDate() == null) {
                try {
                    SchedHearingDefendant shDefendant = shdMaintainer.findBySchedHearingIdAndDefOnCaseId(schedHearing
                            .getScheduledHearingId(), this.defendantOnCaseID);

                    // only calculate the start date if for the required
                    // defendant
                    if (shDefendant != null) {
                        // calculate the start date if it isn't set. use
                        // notbefore if
                        // original hasn't been populated
                        if (schedHearing.getOriginalTime() != null) {
                            this.setAndCalculateHearingStartDate(schedHearing.getOriginalTime());
                        } else {
                            this.setAndCalculateHearingStartDate(schedHearing.getNotBeforeTime());
                        }
                    }
                } catch (ObjectNotFoundException e) {
                    // we don't care if the defendant is not found, just
                    // continue as normal
                }
            }

            // Need to set the court reporters here since they are per
            // scheduled hearing
            Collection hrCourtReporters = this.setAllShAttendeeValues(shAttendees);

            // set the hearing record scheduled hearing values per scheduled
            // hearing.
            // And add the hrShValue to the Collection for scheduled
            // hearings.
            scheuledHearings.addElement(this.setHrHearingScheduleValue(hrCourtReporters,
                    schedHearing.getOriginalTime(), schedHearing.getScheduledHearingId()));
            // add the shLegReps to the collection
            this.setHrShLegRepValues(schedHearing.getScheduledHearingId());
        }
        log.debug("HearingRecordRetrievalForViewHelper.setScheduledHearingValues() finished");
    }

    /**
     * This populates the scheduled hearings for the complex hearing value.
     * hearingComplexValue.getScheduledHearings()
     * 
     * @param hearingID
     *            Integer
     * @return Collection of ScheduledHearings
     * @throws HearingRecordException
     */
    private Collection getScheduledHearings(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.getScheduledHearings(Integer " + "hearingID"
                + hearingID.toString() + ") called");
        try {
            Collection schedHearings = shMaintainer.findByHearingId(hearingID);
            log.debug("HearingRecordRetrievalForViewHelper.getScheduledHearings(Integer " + "hearingID"
                    + hearingID.toString() + ") finished");
            return schedHearings;
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordRetrievalException(HearingRecordConstants.SCHED_HEARING_NOT_FOUND, ex.getMessage(),
                    ex);
        }
    }

    /**
     * This method will set the DirectionsForCaseValue There will not always be
     * a direction for case so don't throw an excpetion.
     */
    private void setDirectionsForCaseValue() {
        // this line to always run since the GUI is dependant on this to not be
        // null.
        directionsForCaseValue = new DirectionsForCaseValue();
        directionsForCaseValue.setDirectionsForCaseBasicValue(new XhbDirectionsForCaseBasicValue());
        try {
            XhbDirectionsForCaseBasicValue basicValue = XhbDirectionsForCaseBeanHelper2
                    .findByCaseIDValue(hearingBasicValue.getCaseID());
            // populate with values from the basic value
            directionsForCaseValue.setDirectionsForCaseBasicValue(basicValue);
        } catch (XhbDirectionsForCaseBeanNotFoundException e) {// don't do
            // anything
            // since this is
            // expected.
            log.info("setDirectionsForCaseValue No XhbDirectionsForCase found for case id:"
                    + hearingBasicValue.getCaseID());
        }
    }

    /**
     * This method will simply set the HRScheduledHearingValue with passed in
     * values.
     * 
     * @param hrCourtReporters
     *            Collection
     * @param originalTime
     *            Date
     * @param scheduledHearingID
     *            Integer
     * @return HRScheduledHearingValue
     */
    private HRScheduledHearingValue setHrHearingScheduleValue(Collection hrCourtReporters, Date originalTime,
            Integer scheduledHearingID) {
        log.debug("HearingRecordRetrievalForViewHelper.setHrHearingScheduleValue() called");
        HRScheduledHearingValue hrShValue = new HRScheduledHearingValue();
        hrShValue.setHrCourtReporter(hrCourtReporters);
        hrShValue.setOriginalTime(originalTime);
        hrShValue.setScheduledHearingId(scheduledHearingID);
        log.debug("HearingRecordRetrievalForViewHelper.setHrHearingScheduleValue() finished");
        return hrShValue;
    }

    /**
     * This will populate all the HrCounsels from the values that have collected
     * in the HashMap refLegRepsDefn and the HashMap refLegRepsPros.
     * 
     * @throws HearingRecordException
     */
    private void setHrCounselValues() throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setHrCounselValues() called");

        // Process defence counsel
        Iterator it = refLegRepsDefn.keySet().iterator();
        while (it.hasNext()) {
            Integer refLegalRepID = (Integer) it.next();
            String legalRole = ((String[]) refLegRepsDefn.get(refLegalRepID))[0];
            String solFirmOrRefLegalRep = ((String[]) refLegRepsDefn.get(refLegalRepID))[1];
            String substituteOrInstructed = ((String[]) refLegRepsDefn.get(refLegalRepID))[2];
            String substitutedRefLegalRepStr = ((String[]) refLegRepsDefn.get(refLegalRepID))[3];
            Integer substitutedRefLegalRep = null;
            if (substitutedRefLegalRepStr != null) {
                substitutedRefLegalRep = Integer.valueOf(substitutedRefLegalRepStr);
            }
            // create a hearing record counsel value and add it to the
            // collection
            HRCounselValue hrCounselValue = counselHelper.getRefLegalRepresentative(
                    refLegalRepID, 
                    legalRole, 
                    solFirmOrRefLegalRep, 
                    hearingBasicValue.getId(),
                    substituteOrInstructed,
                    substitutedRefLegalRep);
            counselValues.add(hrCounselValue);
        }

        // Process prosecution counsel
        it = refLegRepsPros.keySet().iterator();
        while (it.hasNext()) {
            Integer refLegalRepID = (Integer) it.next();
            String legalRole = ((String[]) refLegRepsPros.get(refLegalRepID))[0];
            String solFirmOrRefLegalRep = ((String[]) refLegRepsPros.get(refLegalRepID))[1];
            // create a hearing record counsel value and add it to the
            // collection
            HRCounselValue hrCounselValue = counselHelper.getRefLegalRepresentative(
                    refLegalRepID, 
                    legalRole,
                    solFirmOrRefLegalRep, 
                    hearingBasicValue.getId(), 
                    null,
                    null);
            counselValues.add(hrCounselValue);
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrCounselValues() finished");
    }

    /**
     * This will get all ShLegReps for a scheduled hearing who are prosectors or
     * have represented the particular defendant. It will also populate the
     * HashMap refLegRepsDefn or refLegRepsPros with the refLegRepID and an
     * array containing the legalRole and solFirmOrRefLegalRep so that this can
     * be used at a later stage to fetch all the counsels.
     * 
     * @param scheduledHearingID
     *            Integer
     * @throws HearingRecordException
     */
    private void setHrShLegRepValues(Integer scheduledHearingID) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setHrShLegRepValues() called");
        // get the SHLegRep for the scheduled hearing id and defendant
        Collection collShLegReps = legRepHelper.getSHLegReps(scheduledHearingID, defendantOnCaseID);
        log.debug(">>>>>>>> There are " + collShLegReps.size() + " shLegreps in the collection" + "for the sh id : "
                + scheduledHearingID.toString());
        Iterator it = collShLegReps.iterator();
        while (it.hasNext()) {
            // get a complex value from the collection
            SHLegRepComplexValue complexValue = (SHLegRepComplexValue) it.next();
            // add reflegrepid and array containing legal role and
            // solFirmOrRefLegalRep to the hashmap - used to get counsels
            // later.
            
            if (complexValue.getLegalRole().equalsIgnoreCase(HearingRecordConstants.LEGAL_ROLE_DEFENCE)) {
                String substitutedRefLegalRepId = null;
                
                if (complexValue.getSubstitutedRefLegalRepID() != null) {
                    substitutedRefLegalRepId = 
                        complexValue.getSubstitutedRefLegalRepID().toString();
                }
                
                refLegRepsDefn.put(complexValue.getRefLegalRepID(), new String[] { 
                    complexValue.getLegalRole(),
                    complexValue.getSolFirmOrRefLegalRep(),
                    complexValue.getSubInst(),
                    substitutedRefLegalRepId});
            } else {
                refLegRepsPros.put(complexValue.getRefLegalRepID(), new String[] { 
                    complexValue.getLegalRole(),
                    complexValue.getSolFirmOrRefLegalRep(),
                    null,
                    null});
            }
            // build HRSHLegRep from the complexValue and add it to
            // shLegReps
            // collection
            shLegReps.add(legRepHelper.buildHRSHLegRepValueFromComplex(complexValue));
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrShLegRepValues() finished");
    }

    /**
     * This method will check all entries in the collection and set the
     * appropriate attendee. Attendees that will be set are court reporters
     * (returned), Judges and Justices. Court reporters are being returned since
     * they are unique per scheduled hearing.
     * 
     * @param shAttendees
     *            Collection
     * @return Collection of HRCourtReporters
     * @throws HearingRecordException
     */
    private Collection setAllShAttendeeValues(Collection shAttendees) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setShAttendeeValues() called");
        // instantiate this collection since the print functionality crashes
        // when it is null
        Collection hrCourtReporters = new Vector();
        if (shAttendees.isEmpty()) {
            return hrCourtReporters;
        }
        // Iterate through all the attendees to check the type of attendee.
        Iterator iShAttendees = shAttendees.iterator();
        while (iShAttendees.hasNext()) {
            // Get the Attendee entity
            SchedHearingAttendee shAttendee = (SchedHearingAttendee) iShAttendees.next();

            // -------------------------- CourtReporter details
            // ---------------------
            if (shAttendee.getAttendeeType().equalsIgnoreCase(HearingRecordConstants.SH_ATT_COURT_REPORTER)) {
                // set the HrCourtReporterValue
                if (shAttendee.getRefCourtRepId() != null) {
                    hrCourtReporters = courtReporterHelper.buildHRCourtReporters(shAttendee.getRefCourtRepId());
                    log.debug("Set the court reporter with refCourtReporterId: " + shAttendee.getRefCourtRepId());
                }
            }
            // ------------------------------ HR Judge details
            // ----------------------
            else if (shAttendee.getAttendeeType().equalsIgnoreCase(HearingRecordConstants.SH_ATT_JUDGE)) {
                // Add the shAttednee id to the unique set and look up the
                // judge later.
                this.shAttendeeIDsForJudge.add(shAttendee.getShAttendeeId());
                log.debug("Added shAttID to set of judges :" + shAttendee.getShAttendeeId());
            }
            // ----------------------------- ShJustice details
            // ----------------------
            else if (shAttendee.getAttendeeType().equalsIgnoreCase(HearingRecordConstants.SH_ATT_JUSTICE)) {
                // This will add the justice value to the collection of
                // justices.
                this.setJusticeValue(shAttendee.getShJustice());
                log.debug("Set the judge with shjustice id :" + shAttendee.getShJustice().getShJusticeId());
            }
        }// end of shAttendee while
        log.debug("HearingRecordRetrievalForViewHelper.setShAttendeeValues() finished");
        return hrCourtReporters;
    }

    /**
     * This will set a HRJusticeValue and add it to the Collection of justices.
     * Get the justice from the CMR, get the basic value and build a hearing
     * record value. Finally add it to a collection.
     * 
     * @param justiceLocal
     *            ShJustice
     */
    private void setJusticeValue(ShJustice justiceLocal) {
        log.debug("HearingRecordRetrievalForViewHelper.setJusticeValue(ShJustice justiceLocal) called");
        SHJusticeBasicValue basicValue = shJusticMaintainer.getShJusticeBasicValue(justiceLocal);
        HRJusticeValue justiceValue = justiceHelper.buildHrJusticeValue(basicValue);
        hrJustices.addElement(justiceValue);
        log.debug("HearingRecordRetrievalForViewHelper.setJusticeValue(ShJustice justiceLocal) finished");
    }

    /**
     * This will set the HrJudgeValue
     * 
     * @param refJudgeID
     *            Integer
     * @throws HearingRecordException
     */
    private void setHrJudgeValue(Integer refJudgeID) throws HearingRecordException {
        // some defensive programming
        if (refJudgeID == null) {
            throw new CSUnrecoverableException("setHrJudgeValue() expected non-null value for refJudgeID");
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrJudgeValue(Integer refJudgeID) called");
        Collection judges = judgeHelper.buildHRJudge(refJudgeID);
        Iterator judgeIt = judges.iterator();
        while (judgeIt.hasNext()) {
            // get the judge value - we are only interested to fetch one
            // judge
            // to set the Hearing judge value.
            hrJudgeValue = (HRJudgeValue) judgeIt.next();
            log.debug("HRJudgeValue : " + hrJudgeValue.toString());
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrJudgeValue(Integer refJudgeID) finished");
    }

    /**
     * This method is used to calculate the earliest date from that passed in,
     * and the value stored in the variable startDate for use later
     * 
     * @param shDate
     *            Date
     */
    private void setAndCalculateHearingStartDate(Date shDate) {
        log.debug("HearingRecordRetrievalForViewHelper.setAndCalculateHearingStartDate(Date shDate) called");

        if ((shDate != null) && ((this.startDate == null) || shDate.before(this.startDate))) {
            this.startDate = shDate;
        }

        log.debug("HearingRecordRetrievalForViewHelper.setAndCalculateHearingStartDate(Date shDate) finished");
    }

    /**
     * This will set the DefHearingRecordValue
     * 
     * @param hearingID
     *            Integer
     * @throws HearingRecordException
     */
    private void setDefHearingRecordValue(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setDefHearingRecordValue(" + "Integer hearingID "
                + hearingID.toString() + ") called");
        defHearingRecordValue = defHearingRecordHelper.getDefHearingRecordValue(defendantOnCaseID, hearingID);
        log.debug("HearingRecordRetrievalForViewHelper.setDefHearingRecordValue(" + "Integer hearingID "
                + hearingID.toString() + ") finished");
    }

    /**
     * This method will set the HrDefendantValue. It will also set the Integer
     * defendantOnCaseID
     * 
     * @param defendantID
     *            Integer
     * @throws HearingRecordException
     */
    private void setHrDefendantValue(Integer defendantID) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setHrDefendantValue(Integer" + " defendantID "
                + defendantID.toString() + ") called");
        this.defendantValue = defendantHelper.getDefendantHrValue(defendantID, this.hearingBasicValue.getCaseID(),
                this.hearingBasicValue.getId());

        this.defendantOnCaseID = defendantHelper.getDefendantOnCaseID(defendantID, this.hearingBasicValue.getCaseID());
        log.debug("HearingRecordRetrievalForViewHelper.setHrDefendantValue(Integer" + " defendantID "
                + defendantID.toString() + ") finished");
    }

    /**
     * This will set the HrCaseValue
     * 
     * @throws HearingRecordException
     */
    private void setHrCaseValue() throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setHrCaseValue() called");
        hrCaseValue = caseValueHelper.getHRCaseValue(hearingBasicValue.getCaseID());
        log.debug("HearingRecordRetrievalForViewHelper.setHrCaseValue() finished");
    }

    /**
     * Retrieve the case-level verdicts for the case
     */
    private void setCaseVerdict() {
        log.debug("HearingRecordRetrievalForViewHelper.setCaseVerdict() called for case:" + hrCaseValue.getCaseID());
        Collection verdicts = XhbVerdictBeanHelper2.findByCaseIdForCase(hrCaseValue.getCaseID());

        // Although a Collection is returned, it should only ever contain 1
        // verdict
        // for a given case, so just get the first element
        if (!verdicts.isEmpty()) {
            XhbVerdict item = (XhbVerdict) verdicts.iterator().next();
            hrVerdictValue.setCaseId(item.getCaseId());
            hrVerdictValue.setRefVerdictId(item.getRefVerdictId());
            hrVerdictValue.setVerdictCode(item.getXhbRefSystemCode().getCode());
            hrVerdictValue.setVerdictDate(item.getVerdictDate());
            hrVerdictValue.setVerdictId(item.getVerdictId());
        }

        log.debug("HearingRecordRetrievalForViewHelper.setCaseVerdict() finished");
    }

    /**
     * This will populate the HearingBasicValue if not already done and find the
     * exportA value for the hearing - if it exists.
     * 
     * @param hearingID
     *            Integer
     * @throws HearingRecordException
     */
    private void setExportAValue(HearingBasicValue hearingBasicValue) throws HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setExportAValue(Integer hearingID) called");
        // Get the ExportAValue that is related to the hearing or its linked
        // hearingid
        exportAValue = statusHelper.getExportValue(hearingBasicValue);
        log.debug("HearingRecordRetrievalForViewHelper.setExportAValue(Integer hearingID) finished");
    }

    /**
     * This will find all the cases that are not for the passed in hearingid and
     * create a HRLinkedCaseValue for each. The values will be added to the
     * HrLinkedCaseValue Collection
     * 
     * @param hearingID
     *            Integer
     * @param linkedHearingID
     *            Integer
     * @throws HearingRecordRetrievalException
     * @throws HearingRecordException
     */
    private void setHrLinkedCaseValues(Integer hearingID, Integer linkedHearingID)
            throws HearingRecordRetrievalException, HearingRecordException {
        log.debug("HearingRecordRetrievalForViewHelper.setHrLinkedCaseValues(Integer hearingID) called");
        // Populate a collection with all the caseId's except from the one that
        // is for the Linked hearingID passed in.
        Collection linkedCaseIDs = this.getAllLinkedCases(hearingID, linkedHearingID);
        // Populate the Collection of HRLinkedCaseValues.
        hrLinkedCaseValues = linkedCaseListHelper.getLinkedCaseList(linkedCaseIDs);
        // populate the collection so it is not null if there are no linked
        // cases. This so that the print doesn't crash.
        if (hrLinkedCaseValues == null) {
            hrLinkedCaseValues = new Vector();
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrLinkedCaseValues(Integer hearingID) finished");
    }

    /**
	 * This will set the Hearing Display Value
	 * 
	 * @return HRHearingDisplayValue
	 * @throws FinderException
	 */
	private HRHearingDisplayValue setHearingDisplayValue() throws FinderException {
        log.debug("HearingRecordRetrievalForViewHelper.setHearingDisplayValue() called");
        hearingDisplayValue = new HRHearingDisplayValue(hearingBasicValue.getId());
        hearingDisplayValue.setHrJudgeValue(hrJudgeValue);
        HRJusticeValueHelper hrJusticeValueHelper = new HRJusticeValueHelper();
		Collection hrJusticeBasicValue = hrJusticeValueHelper.findByHearingId(hearingBasicValue.getId());
		hearingDisplayValue.setHrJusticeValues(hrJusticeBasicValue);
        hearingDisplayValue.setHrScheduledHearingValues(scheuledHearings);
        log.debug("HearingRecordRetrievalForViewHelper.setHearingDisplayValue() finished");
        return hearingDisplayValue;
    }

    /**
     * Set the viewable Hearing record value
     * 
     * @return HearingRecordDisplayValue
     */
    private HearingRecordDisplayValue setHearingRecordDisplayValue() {
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordDisplayValue() called");
        displayValue = new HearingRecordDisplayValue();
        displayValue.setHrCaseValue(hrCaseValue);
        displayValue.setHrCounselValue(counselValues);
        displayValue.setHrDefendantValue(defendantValue);
        displayValue.setHrHearingDisplayValue(hearingDisplayValue);
        displayValue.setHrLinkedCaseListValue(hrLinkedCaseValues);
        displayValue.setVerdictValue(hrVerdictValue);
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordDisplayValue() finished");
        return displayValue;
    }

    /**
     * Set the updateable Hearing record value
     * 
     * @return HearingRecordUpdateValue
     */
    private HearingRecordUpdateValue setHearingRecordUpdateValue(final String caseType) {
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordUpdateValue() called");
        updateValue = new HearingRecordUpdateValue();
        updateValue.setCaseType(caseType);
        updateValue.setHearingBasicValue(hearingBasicValue);
        updateValue.setDefHearingRecordValue(defHearingRecordValue);
        updateValue.setHrSHJudgeValue(hrShJudgeValue);
        updateValue.setHrSHLegRepValues(shLegReps);
        updateValue.setDirectionsForCaseValue(directionsForCaseValue);
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordUpdateValue() finished");
        return updateValue;
    }

    /**
     * Set the top level hearing record value.
     * 
     * @return HearingRecordValue
     */
    private HearingRecordValue setHearingRecordValue() {
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordValue() called");
        hearingRecordValue = new HearingRecordValue();
        if (exportAValue != null) {
            hearingRecordValue.setCourtClerkExporter(exportAValue.getCourtClerkName());
            hearingRecordValue.setExportID(exportAValue.getId());
            hearingRecordValue.setExportStatus(exportAValue.getStatusFlag());
        }
        hearingRecordValue.setHearingRecordDisplayValue(displayValue);
        hearingRecordValue.setHearingRecordUpdateValue(updateValue);
        log.debug("HearingRecordRetrievalForViewHelper.setHearingRecordValue() finished");
        return hearingRecordValue;
    }

    /**
     * Helper method to find a hearing and transform it to a HearingComplexValue
     * given a hearingId
     * 
     * @param hearingId
     *            Integer
     * @return Hearing the hearing
     * @throws HearingRecordRetrievalException
     */
    private Hearing findHearing(Integer hearingId) throws HearingRecordRetrievalException {
        log.debug("HearingRecordRetrievalForViewHelper.findHearing(Integer hearingID) called");
        Hearing hearing = null;
        try {
            hearing = hearingMaintainer.findByPK(hearingId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingRecordRetrievalException(HearingRecordConstants.HEARING_NOT_FOUND, ex.getMessage(), ex);
        }
        log.debug("HearingRecordRetrievalForViewHelper.findHearing(Integer hearingID) finished");
        return hearing;
    }

    /**
     * This will use an existing hearing if exist and then create a basic value.
     * It the hearing is null then try to find it again.
     * 
     * @param hearingId
     *            Integer
     * @return HearingBasicValue
     * @throws HearingRecordException
     */
    private HearingBasicValue getHearingBasicValue(Hearing hearing) throws HearingRecordException {
        log.debug("getHearingBasicValue(Integer hearingId) called");
        return hearingMaintainer.getHearingBasicValue(hearing);
    }

    /**
     * This will find all linked hearings and return a Collection of
     * HearingBasicValues but not the hearing that is passed in. It will also
     * add all unique caseIds to a Set.
     * 
     * @param hearingId
     *            Integer
     * @param linkedHearingId
     *            Integer
     * @return Collection of HearingBasicValues
     * @throws HearingRecordRetrievalException
     */
    private Collection getAllLinkedCases(Integer hearingId, Integer linkedHearingId) {
        log.debug("HearingRecordRetrievalForViewHelper.getAllLinkedHearings(Integer hearingID) called");
        Vector linkedCases = null;
        Collection allLinkedHearings = hearingMaintainer.findByLinkedHearingId(linkedHearingId);
        if (allLinkedHearings.isEmpty()) {
            return allLinkedHearings;
        }

        for (Iterator it = allLinkedHearings.iterator(); it.hasNext();) {
            Hearing h = (Hearing) it.next();
            // we only want to populate a collection of the related linked
            // hearings but not the hearing with the hearingId passed in.
            if (!h.getHearingId().equals(hearingId)) {
                // add the caseID to a unique set
                uniqueCaseIDs.add(h.getCaseId());
            }
        }

        // Create a Collection of the unique case ids to return
        if (!uniqueCaseIDs.isEmpty()) {
            linkedCases = new Vector();
            for (Iterator it = uniqueCaseIDs.iterator(); it.hasNext();) {
                linkedCases.addElement(it.next());
            }
        }
        log.debug("HearingRecordRetrievalForViewHelper.getAllLinkedHearings(Integer hearingID) finished");
        return linkedCases;
    }

    /**
     * This will get the lasted Shjudge or if it doesn't exist create a new one.
     * It can only be one shJudge per Attendee and we want the one created last.
     * If there is no judge for the highest scheudlued hearing id - then we need
     * to create one.
     * 
     * @param highestShAttID
     *            Integer
     * @param refJudgeId
     *            Integer
     * @throws HearingRecordException
     */
    private void setHrShJudge(Integer highestShAttID, Integer refJudgeId, String userDisplayName) {
        log.debug("HearingRecordRetrievalForViewHelper.setHrShJudge(Integer "
                + "highestShAttID, Integer refJudgeId) called");
        try {
            log.debug(">>>>>>>>>>>>>>>Try to find the judge for shAttid : " + highestShAttID.toString());
            hrShJudgeValue = hrShJudgeHelper.getHRJudgeValue(highestShAttID);
        } catch (HearingRecordException e) {
            // if hearing record exception the shJudge doesn't exist so
            // create
            // a new one.
            log.debug(">>>>>>>>>>>>>>> no judge exist so create one..... with shattid : " + highestShAttID.toString()
                    + " and refjudgeid : " + refJudgeId.toString());
            this.createShJudge(highestShAttID, refJudgeId, userDisplayName);
        }
        log.debug("HearingRecordRetrievalForViewHelper.setHrShJudge(Integer "
                + "highestShAttID, Integer refJudgeId) finished");
    }

    /**
     * This method will create a new SHJudge record and store it to the
     * database. It will after creation set the hearing record sh judge value.
     * 
     * @param shAttId
     *            refJudgeId
     * @param refJudgeId
     *            refJudgeId
     * @throws HearingRecordException
     */
    private void createShJudge(Integer shAttId, Integer refJudgeId, String userDisplayName) {
        log.debug("HearingRecordRetrievalForViewHelper.createShJudge(Integer "
                + "shAttId, Integer refJudgeId) finished");
        // set a basicValue
        SHJudgeBasicValue basicValue = new SHJudgeBasicValue();
        basicValue.setShAttendeeID(shAttId);
        basicValue.setRefJudgeID(refJudgeId);
        basicValue.setDeputyHCJ(HearingRecordConstants.JUDGE_DEPUTY_HCJ_NO);
        // create the shJudge
        ShJudge entity = (ShJudge) shJudgeMaintainer.create(basicValue, userDisplayName);
        // NOTE: setting the version to 1 here since only 1 transaction.
        basicValue.setVersion(new Integer(1));
        basicValue.setId(entity.getShJudgeId());
        hrShJudgeValue = hrShJudgeHelper.buildHRSHJudgeValueFromBasic(basicValue);
        log.debug("HearingRecordRetrievalForViewHelper.createShJudge(Integer "
                + "shAttId, Integer refJudgeId) finished");
    }
}
