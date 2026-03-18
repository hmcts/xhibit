package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeHome;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff;
import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaffMaintainer;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHStaffBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.helper.FormattedDisplayHelper;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.AttendeeHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.AttendeeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJusticeCriteria;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;

/**
 * <p>
 * Title: Hearing Header Staff Helper
 * </p>
 * <p>
 * Description: Methods to retrieve and remove Attendees and their corresponding
 * history.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @editors Ian Hannaford
 * @version 1.0
 */
public class HHStaffHelper {
    private final String MISSING_DATA = "HearingSchedule.Missing_Data";

    private BisRefControllerLocal bisRefDelegate;

    private ScheduledHearingMaintainer scheduledHearingMaintainer;

    private SchedHearingAttendeeMaintainer shaMaintainer;

    private ShJusticeMaintainer shJusticeMaintainer;

    private ShStaffMaintainer shsMaintainer;

    private SittingMaintainer sittingMaintainer;

    private CourtSiteMaintainer courtSiteMaintainer;

    private static final Logger log = CSServices.getLogger(HHStaffHelper.class);

    // private final Integer APPEAL_TYPE = new Integer(45);

    public HHStaffHelper(BisRefControllerLocal bisRefDelegate) {
        this.bisRefDelegate = bisRefDelegate;
        scheduledHearingMaintainer = new ScheduledHearingMaintainer();
        shaMaintainer = new SchedHearingAttendeeMaintainer();
        shJusticeMaintainer = new ShJusticeMaintainer();
        shsMaintainer = new ShStaffMaintainer();
        sittingMaintainer = new SittingMaintainer();
        courtSiteMaintainer = new CourtSiteMaintainer();

    }

    public Collection getAttendees(Integer scheduledHearingId, boolean updateSittingInfo, String userDisplayName)
            throws HearingScheduleException {
        String methodName = "getAttendees - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        // if the sitting data should be moved: Check if has already been copied
        // to scheduledHearingAttendee otherwise move it.
        if (updateSittingInfo) {
            SittingInfoMover sim = new SittingInfoMover();
            sim.moveSittingInfo(scheduledHearingId, userDisplayName);
        }

        List attendeesList = new ArrayList();
        try {
            // find the attendees for this scheduled hearing
            SchedHearingAttendeeHome shAttendeeHome = (SchedHearingAttendeeHome) CSServices.getServiceLocator()
                    .getLocalHome(SchedHearingAttendeeHome.class);
            Collection attendees = shAttendeeHome.findByScheduledHearingId(scheduledHearingId);

            int judgeId = 0;
            int courtReporterId = 0;
            SchedHearingAttendee judge = null;
            SchedHearingAttendee courtReporter = null;

            // Iterate through attendee, checking whether they are a judge
            // or court reporter.
            // Get the latest judge and court reporter by checking the
            // primary keys.
            // We are assuming that the highest primary key is the latest
            // jusge and court reporter.
            Iterator it = attendees.iterator();
            while (it.hasNext()) {
                SchedHearingAttendee attendee = (SchedHearingAttendee) it.next();
                if (attendee.getAttendeeType() != null) {

                    // if the attendee is a Judge
                    if (attendee.getAttendeeType().equals(PersonValue.JUDGE)) {
                        // if the current judge id is less than the attendee id
                        // then replace the current judge with the this attendee
                        if (judgeId < attendee.getShAttendeeId().intValue()) {
                            judgeId = attendee.getShAttendeeId().intValue();
                            judge = attendee;
                        }
                    } else {
                        // if the attendee is a court reporter
                        if (attendee.getAttendeeType().equals(PersonValue.COURT_REPORTER)) {
                            // if the current courtReporterId is less than
                            // the attendee id
                            // then replace the current courtReporter with
                            // the this attendee
                            if (courtReporterId < attendee.getShAttendeeId().intValue()) {
                                courtReporterId = attendee.getShAttendeeId().intValue();
                                courtReporter = attendee;
                            }
                        } else {
                            // it will reach here if the attendee is not a
                            // judge or court reporter, so just
                            // add it the the collection
                            attendeesList.add(getAttendeeDetails(attendee));
                        }
                    }
                }
            }

            // if a judge has been found, then add them to the collection
            if (judge != null) {
                attendeesList.add(getAttendeeDetails(judge));
            }
            // if a courtReporter has been found, then add them to the
            // collection
            if (courtReporter != null) {
                attendeesList.add(getAttendeeDetails(courtReporter));
            }

        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e.toString(), e);
        }
        debug(methodName + "Exited OK");

        // return to the caller
        return attendeesList;
    }

    /**
     * Private method to retrieve the attendee details.
     * 
     * @param attendee
     *            SchedHearingAttendee
     * @return PersonValue
     */
    private PersonValue getAttendeeDetails(SchedHearingAttendee attendee) {
        String methodName = "getAttendeeDetails - ";
        debug(methodName + "called with : shAttendeeId =  " + attendee.getShAttendeeId());

        String attendeeType = attendee.getAttendeeType();
        PersonValue person = null;

        if (attendeeType == null) {
            debug(methodName + "received null attendeeType");
            throw new CSConfigurationException("Received null attendeeType");
        }

        try {
            // if the attendee is a court clerk or usher
            if (attendeeType.equals(PersonValue.COURT_CLERK) || attendeeType.equals(PersonValue.USHER)) {
                debug("***" + methodName + "The current attendee is a court clerk or usher ***");
                ShStaff staff = attendee.getShStaff();
                person = new PersonValue(attendee.getShAttendeeId(), attendee.getVersion(), staff.getShStaffId(), staff
                        .getVersion(), staff.getStaffName(), staff.getStaffRole());

            }
            // if the attendee is a justice
            else if (attendeeType.equals(PersonValue.JUSTICE)) {
                debug("*** The current attendee is a justice ***");
                ShJustice justice = attendee.getShJustice();

                if (attendee.getRefJusticeId() != null)
                // where ref_justice_id has been added to attendee information
                // after CREST Form B-F changes
                {
                    RefJusticeCriteria refJusticeCriteria = new RefJusticeCriteria();
                    refJusticeCriteria.setPrimaryKey(attendee.getRefJusticeId());
                    Collection justices = bisRefDelegate.findJustices(refJusticeCriteria);
                    // only expecting one judge back
                    if (justices != null && !justices.isEmpty()) {
                        Iterator it = justices.iterator();
                        RefJusticeBasicValue jus = (RefJusticeBasicValue) it.next();

                        person = new PersonValue(attendee.getShAttendeeId(), attendee.getVersion(), justice
                                .getShJusticeId(), justice.getVersion(), jus.getJusticeName(), attendeeType);
                    }
                } else
                // where ref_justice_id has NOT been added to attendee
                // information
                {

                    person = new PersonValue(attendee.getShAttendeeId(), attendee.getVersion(), justice
                            .getShJusticeId(), justice.getVersion(), justice.getJusticeName(), attendeeType);
                }

            }
            // if the attendee is a court reporter
            else if (attendeeType.equals(PersonValue.COURT_REPORTER)) {
                debug("*** The current attendee is a court reporter ***");
                RefCourtReporterCriteria refCourtReporterCriteria = new RefCourtReporterCriteria();
                refCourtReporterCriteria.setPrimaryKey(attendee.getRefCourtRepId());

                Collection courtReporters = bisRefDelegate.findCourtReporters(refCourtReporterCriteria);
                if (courtReporters != null && !courtReporters.isEmpty()) {
                    // There should only be one court reporter returned
                    debug("*** There have been " + courtReporters.size() + " returned  ***");
                    Iterator it = courtReporters.iterator();
                    RefCourtReporterBasicValue refCourtReporterValue = (RefCourtReporterBasicValue) it.next();

                    // get the court reporters name.
                    String fullName = FormattedDisplayHelper.getDisplayName(refCourtReporterValue);

                    person = new PersonValue(attendee.getShAttendeeId(), attendee.getVersion(), refCourtReporterValue
                            .getId(), refCourtReporterValue.getVersion(), fullName, attendeeType);
                } else {
                    debug(methodName + "Did not find court reporter with court reporterId = "
                            + attendee.getRefCourtRepId());
                    throw new CSConfigurationException("Did not find court reporter with court reporterId = "
                            + attendee.getRefCourtRepId());
                }
            }
            // if the attendee is a judge
            else if (attendeeType.equals(PersonValue.JUDGE)) {
                debug("*** The current attendee is a judge ***");
                RefJudgeCriteria refJudgeCriteria = new RefJudgeCriteria();
                refJudgeCriteria.setPrimaryKey(attendee.getRefJudgeId());
                Collection judges = bisRefDelegate.findJudges(refJudgeCriteria);
                // only expecting one judge back
                if (judges != null && !judges.isEmpty()) {
                    Iterator it = judges.iterator();
                    RefJudgeBasicValue judge = (RefJudgeBasicValue) it.next();

                    // PRE00177 - changed to show the required display name
                    final String title = FormattedDisplayHelper.getDisplayName(judge);

                    person = new PersonValue(attendee.getShAttendeeId(), attendee.getVersion(), judge.getId(), judge
                            .getVersion(), title, attendeeType);
                } else {
                    debug(methodName + "Did not find judge with RefJudgeId = " + attendee.getRefJudgeId());
                    throw new CSConfigurationException("Did not find judge with RefJudgeId = "
                            + attendee.getRefJudgeId());
                }
            }
        } catch (BisRefControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new CSConfigurationException(e.toString(), e);
        }

        debug(methodName + "Exited returning PersonValue = " + person);
        return person;
    }

    /**
     * For the scheduledHearingId Find all scheduledHearingIds for this
     * ScheduledHearing For each scheduledHearing Create a AttendeeHistoryList
     * populate history with ScheduledHearingBasicValue and Collection of
     * PersonValues representing the attendees for that scheduledHearing return
     * the collection of AttendeeHistoryValues
     * 
     * @param scheduledHearingId -
     *            scheduled hearing id
     * @return List of AttendeeHistoryValues - each containts a
     *         scheduledHearingBasicValue and a collection of PersonValues
     * @throws HearingScheduleException
     */

    public Collection getAttendeeHistory(Integer scheduledHearingId) throws HearingScheduleException {
        String methodName = "getAttendeeHistory - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        Collection historyList = new ArrayList();
        // get the scheduledHearings
        ScheduledHearing scheduledHearing = null;
        Collection scheduledHearings = null;
        try {
            debug("*** finding scheduled hearing using scheduledHearingId " + scheduledHearingId + " ***");
            scheduledHearing = scheduledHearingMaintainer.findByPK(scheduledHearingId);
            scheduledHearings = scheduledHearingMaintainer.findByHearingId(scheduledHearing.getHearingId());
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new HearingScheduleException(MISSING_DATA,
                    "Error finding scheduledHearing and related scheduledHearings", e);
        }

        Iterator iterator = scheduledHearings.iterator();
        ScheduledHearingBasicValue basicValue = null;
        AttendeeHistoryValue attendeeHistory = null;

        while (iterator.hasNext()) {
            scheduledHearing = (ScheduledHearing) iterator.next();
            // create the basic values for the scheduled hearings
            debug("*** creating the basic value objects ***");
            basicValue = scheduledHearingMaintainer.createBasicVO(scheduledHearing);

            debug("*** Retreiving the attendees and person values ***");
            Collection attendees = scheduledHearing.getScheduledHearingAttendee();
            Collection personValues = getPersonValuesFromAttendees(attendees);
            debug("*** The size of person value collection is " + personValues.size() + " ***");
            // create an AttendeeHistory Value from
            // a) ScheduledHearingBasicValue
            // b) Collection of PersonValues
            attendeeHistory = new AttendeeHistoryValue();
            attendeeHistory.setScheduledHearingBasicValue(basicValue);
            attendeeHistory.setPersonValueList(personValues);
            // add the history value to the collection
            debug("*** Adding the attendee history to the collection ***");
            historyList.add(attendeeHistory);
        }

        debug("***Returning the collection to the caller ***");
        return historyList;
    }

    /**
     * Iterate over collection of SchedHearingAttendees and extract the person
     * values using getAttendeeDetails. Add person values to collection and
     * return
     * 
     * @param attendees -
     *            collection of attendees
     * @return Collection of PersonValue
     */
    private Collection getPersonValuesFromAttendees(Collection attendees) {
        debug("*** Inside private method getPersonValuesFromAttendees() ***");
        Collection personValueList = new ArrayList();
        Iterator iterator = attendees.iterator();
        while (iterator.hasNext()) {
            SchedHearingAttendee attendee = (SchedHearingAttendee) iterator.next();
            PersonValue personValue = getAttendeeDetails(attendee);
            personValueList.add(personValue);
        }
        debug("*** Returning from getPersonValuesFromAttendees() ***");
        return personValueList;
    }

    /*
     * 
     * REMOVED FOR BUG FIX XI2B015 List attendees = new ArrayList(); Calendar
     * startTime = null; Calendar endTime = null;
     * 
     * try { ScheduledHearing sh =
     * scheduledHearingMaintainer.findByPK(scheduledHearingId); Collection
     * scheduledHearings =
     * scheduledHearingMaintainer.findByHearingId(sh.getScheduledHearingId());
     * Iterator it = scheduledHearings.iterator(); while (it.hasNext()) {
     * ScheduledHearing sheduledHearing = (ScheduledHearing)it.next(); startTime =
     * DataTypeConverter.convertToCalendar(sheduledHearing.getStartTime());
     * endTime =
     * DataTypeConverter.convertToCalendar(sheduledHearing.getEndTime());
     * Collection shAttendees = sheduledHearing.getScheduledHearingAttendee();
     * Iterator shIt = shAttendees.iterator(); while (shIt.hasNext()) {
     * SchedHearingAttendee shA = (SchedHearingAttendee)shIt.next(); PersonValue
     * attendee = getAttendeeDetails(shA); AttendeeHistoryValue
     * attendeeHistoryValue = new
     * AttendeeHistoryValue(shA.getShAttendeeId(),shA.getVersion(),attendee);
     * attendeeHistoryValue.setStartTime(startTime);
     * attendeeHistoryValue.setEndTime(endTime);
     * attendees.add(attendeeHistoryValue); } } log.debug( methodName + "Exited
     * OK"); return attendees; } catch (ObjectNotFoundException e) {
     * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
     * throw new EJBException(e); }
     */

    /**
     * Method to add a collection of attendees. Call singular addAttendee
     * method.
     * 
     * @param scheduledHearingId
     *            sheduledHearingId
     * @param attendeeValues
     *            Collection of attendee objects
     * @throws HearingScheduleException
     */
    public void addAttendees(Integer scheduledHearingId, Collection attendeeValues, String userDisplayName) throws HearingScheduleException {

        String methodName = "addAttendees - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        Iterator it = attendeeValues.iterator();
        while (it.hasNext()) {
            AttendeeValue attendee = (AttendeeValue) it.next();
            addAttendee(scheduledHearingId, attendee, userDisplayName);
        }
        debug(methodName + "Exited OK");
    }

    /**
     * Removes all attendees in the collection. Calls the singualr
     * removeAttendee method.
     * 
     * @param scheduledHearingId -
     *            the scheduled hearing id
     * @param attendeeValues -
     *            A collection of attendeeValues
     * @throws HearingScheduleException
     */
    public void removeAttendees(Integer scheduledHearingId, Collection attendeeValues, Boolean forSitting)
            throws HearingScheduleException {
        String methodName = "removeAttendees - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        Iterator it = attendeeValues.iterator();
        while (it.hasNext()) {
            AttendeeValue attendee = (AttendeeValue) it.next();
            removeAttendee(scheduledHearingId, attendee, forSitting);
        }
        debug(methodName + "Exited OK");
    }

    /**
     * Add a new attendee to the schedule hearing. This method also has the side
     * effect of changing the AttendeeHistoryValue.
     * 
     * @param scheduledHearingId -
     *            the scheduled hearing id
     * @param attendeeComp
     *            an AttendeeValue object
     */
    private void addAttendee(Integer scheduledHearingId, AttendeeValue attendeeComp, String userDisplayName) {
        String methodName = "addAttendee - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        if (attendeeComp == null)
            throw new IllegalArgumentException("attendeeComp");
        if (scheduledHearingId == null)
            throw new IllegalArgumentException("scheduledHearingId");

        SchedHearingAttendee schedHearingAttendee = null;
        ShStaff shStaff = null;
        ShJustice shJustice = null;

        try { // getting the person from the attendeeComp object
            PersonValue attendee = attendeeComp.getPerson();
            String personType = attendee.getPersonType();

            if (personType == null) {
                debug(methodName + "personType from attendeeComp was null");
                throw new CSConfigurationException("personType from attendeeComp was null");
            }

            // locating the scheduleHearing
            debug("*** " + methodName + "Locating scheduleHearing using scheduleHearingId " + scheduledHearingId
                    + " ***");
            ScheduledHearing sh = scheduledHearingMaintainer.findByPK(scheduledHearingId);

            // create the new basic value object
            SchedHearingAttendeeBasicValue shaValue = new SchedHearingAttendeeBasicValue();

            // set the attendee type
            shaValue.setAttendeeType(personType);

            // if attendee is a court reporter
            if (personType.equals(PersonValue.COURT_REPORTER)) {
                debug("***" + methodName + "Current Attendee is a CourtReporter ***");
                shaValue.setRefCourtReporterID(attendee.getId());
                schedHearingAttendee = (SchedHearingAttendee) shaMaintainer.create(shaValue, sh, userDisplayName);

                // Set for Sitting
                if (attendeeComp.getIsAttendingSubsequentSH() != null
                        && attendeeComp.getIsAttendingSubsequentSH().booleanValue()) {
                    // call update sitting
                    debug("***" + methodName + "Calling update Sitting ***");
                    updateSitting(scheduledHearingId, attendee.getId(), personType, null, null, userDisplayName);
                }
            }
            // if the attendee is a court clerk of usher
            else if (personType.equals(PersonValue.COURT_CLERK) || personType.equals(PersonValue.USHER)) {
                debug("***" + methodName + "The current attendee is a court clerk or usher ***");
                debug("***" + methodName + "Current Attendee is for SITTING : "
                        + attendeeComp.getIsAttendingSubsequentSH() + " ***");

                schedHearingAttendee = (SchedHearingAttendee) shaMaintainer.create(shaValue, sh, userDisplayName);

                shStaff = addStaff(attendee, userDisplayName);
                schedHearingAttendee.setShStaff(shStaff);

                // Set for Sitting
                if (attendeeComp.getIsAttendingSubsequentSH() != null
                        && attendeeComp.getIsAttendingSubsequentSH().booleanValue()) {
                    // call update sitting
                    debug("***" + methodName + " Calling update Sitting for COURT_CLERK or USHER ***");
                    updateSitting(scheduledHearingId, null, personType, shStaff, null, userDisplayName);
                }
            } else if (personType.equals(PersonValue.JUSTICE)) {
                debug("***" + methodName + "Current Attendee is a Justice ***");
                shaValue.setRefJusticeID(attendee.getId());
                schedHearingAttendee = (SchedHearingAttendee) shaMaintainer.create(shaValue, sh, userDisplayName);
                shJustice = addJustice(attendee, sh, userDisplayName);
                schedHearingAttendee.setShJustice(shJustice);

                /**
                 * @todo This is not implemented yet as it requires further
                 *       logic regarding the type of scheduled hearings in
                 *       sittings are that are eligable for justice update
                 */
                /*
                 * if ( attendeeComp.getIsAttendingSubsequentSH() != null &&
                 * attendeeComp.getIsAttendingSubsequentSH().booleanValue() ) { //
                 * call update sitting debug ("***" + methodName + "Calling
                 * update Sitting ***" ) ; updateSitting( scheduledHearingId,
                 * null, personType, null, shJustice); }
                 */
            } else if (personType.equals(PersonValue.JUDGE)) {
                debug("***" + methodName + "Current Attendee is a Judge ***");
                shaValue.setRefJudgeID(attendee.getId());
                schedHearingAttendee = (SchedHearingAttendee) shaMaintainer.create(shaValue, sh, userDisplayName);

                if (attendeeComp.getIsAttendingSubsequentSH() != null
                        && attendeeComp.getIsAttendingSubsequentSH().booleanValue()) {
                    // call update sitting
                    debug("***" + methodName + "Calling update Sitting ***");
                    updateSitting(scheduledHearingId, attendee.getId(), personType, null, null, userDisplayName);
                }

                // notify the public displays that the judge has changed
                debug("***" + methodName + "Calling Public Display to notify that the judge has changed ***");

                if (personType.equals(PersonValue.JUDGE)) {
                    // Meeraj - should this be called anymore?
                    // notifyPublicDisplays(
                    // sh.getSitting().getCourtRoomId(),
                    // sh.getSitting().getCourtSiteId() );
                    notifyNewPublicDisplays(sh, userDisplayName);
                }
            }
        } // try

        // set the schedule hearing in the VO
        // schedHearingAttendee.setScheduledHearing( sh );
        catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }

        debug(methodName + "Exited OK");
    }

    /**
     * Remove attendee from the scheduled hearing.
     * 
     * @param scheduledHearingId -
     *            scheduleHearingId
     * @param attendeeComp -
     *            AttendeeValue
     */

    private void removeAttendee(Integer scheduledHearingId, AttendeeValue attendeeComp, Boolean forSitting) {
        String methodName = "removeAttendees - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        // checking for null values in attendee record
        PersonValue attendee = attendeeComp.getPerson();
        if (attendee == null || attendee.getParentId() == null || attendee.getParentVersion() == null
                || attendee.getId() == null || attendee.getVersion() == null || attendee.getPersonType() == null) {
            debug(methodName + " PersonValue received null values in PersonType ");
            throw new IllegalArgumentException("Received null values in PersonType");
        }

        // delete the attendee
        try {
            // COURT STAFF
            if (attendee.getPersonType().equals(PersonValue.COURT_CLERK)
                    || attendee.getPersonType().equals(PersonValue.USHER)) {
                debug(methodName + "Attendee is Court Staff with StaffId " + attendee.getId());

                // Check if removing for sitting or just SH */
                if (forSitting.booleanValue()) {
                    debug(methodName + "Deleting Court Staff for Sitting");
                    Collection sittingAttendees = shaMaintainer.findBySHStaffId(attendee.getId());
                    debug(methodName + "Number of Attendees in Sitting:- " + sittingAttendees.size());

                    // retrieve SchduleHearingValue for
                    // SchduleHearingAttendee
                    SchedHearingAttendee sha = shaMaintainer.findByPrimaryKey(attendee.getParentId());
                    ScheduledHearing shv = scheduledHearingMaintainer.findByPK(sha.getScheduledHearingId());

                    removeSittingAttendees(sittingAttendees, shv);
                } else {
                    // delete for SH
                    debug(methodName + "Deleting Court Staff for SH");
                    shaMaintainer.delete(attendee.getParentId(), attendee.getParentVersion());
                }
            }
            // COURT REPORTER
            else if (attendee.getPersonType().equals(PersonValue.COURT_REPORTER)) {
                if (forSitting.booleanValue()) {
                    // delete for Sitting
                    debug(methodName + "Deleteing Court Reporter for Sitting");
                    Collection sittingAttendees = shaMaintainer.findByRefCourtReporterId(attendee.getId());
                    debug(methodName + "Number of Attendees in Sitting:- " + sittingAttendees.size());

                    // retrieve SchduleHearingValue for
                    // SchduleHearingAttendee
                    SchedHearingAttendee sha = shaMaintainer.findByPrimaryKey(attendee.getParentId());
                    ScheduledHearing shv = scheduledHearingMaintainer.findByPK(sha.getScheduledHearingId());

                    removeSittingAttendees(sittingAttendees, shv);
                } else {
                    // delete for SH
                    debug(methodName + "Deleting Court Reporter for SH with SH_Attendee_ID:- " + attendee.getParentId()
                            + "and Version :- " + attendee.getParentVersion());
                    shaMaintainer.delete(attendee.getParentId(), attendee.getParentVersion());
                }
            }
            // JUSTICE
            else if (attendee.getPersonType().equals(PersonValue.JUSTICE)) {
                // delete for SH
                debug(methodName + "Deleting Justice for SH");
                this.shJusticeMaintainer.delete(attendee.getId(), attendee.getVersion());
                shaMaintainer.delete(attendee.getParentId(), attendee.getParentVersion());
            }
        } catch (ObjectNotFoundException e) {
            // if we can't find the object ot delete then someone else has
            // deleted it
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new OptimisticLockException(e);
        }

        debug(methodName + "Exited OK");
    }

    /**
     * Add a justice attendee to the schedule hearing
     * 
     * @param attendee
     *            -PersonValue
     * @param sh -
     *            the shcedule hearing object
     * @return ShJustice
     */
    private ShJustice addJustice(PersonValue attendee, ScheduledHearing sh, String userDisplayName) {
        String methodName = "addJustice - ";
        debug(methodName + "called with : person Id =  " + attendee.getId());

        // set up the basic value
        SHJusticeBasicValue shjValue = new SHJusticeBasicValue();
        shjValue.setJusticeName(attendee.getFullName());
        shjValue.setHearingID(sh.getHearingId());

        // create a new justice
        ShJustice shJustice = (ShJustice) shJusticeMaintainer.create(shjValue, userDisplayName);

        debug(methodName + "Exited OK");
        return shJustice;
    }

    /**
     * Add a staff the the schedule hearing
     * 
     * @param attendee -
     *            PersonValue
     * @return ShStaff
     */
    private ShStaff addStaff(PersonValue attendee, String userDisplayName) {
        String methodName = "addStaff - ";
        debug(methodName + "called with : person Id =  " + attendee.getId());

        // create the basic value object
        SHStaffBasicValue shsValue = new SHStaffBasicValue();
        shsValue.setStaffName(attendee.getFullName());
        shsValue.setStaffRole(attendee.getPersonType());

        // create the new staff object
        ShStaff staff = (ShStaff) shsMaintainer.create(shsValue, userDisplayName);

        debug(methodName + "Exited OK");
        return staff;
    }

    /**
     * Updates the sitting
     * 
     * @param scheduledHearingId
     *            the scheduled hearing id
     * @param refJudgeId
     *            the ref jufge id
     */
    private void updateSitting(Integer scheduledHearingId, Integer attendeeId, String personType, ShStaff shStaff,
            ShJustice shJustice, String userDisplayName) {
        String methodName = "updateSitting - ";
        debug(methodName + "called with :scheduledHearingId =  " + scheduledHearingId + ", RefJudgeID" + attendeeId);

        try {
            ScheduledHearing sh = scheduledHearingMaintainer.findByPK(scheduledHearingId);

            // get sequence no of passed scheduled hearing
            Integer currSeqNo = sh.getSequenceNo();
            if (currSeqNo == null) {
                // sequence no should not be null
                throw new CSConfigurationException("Sequence no for scheduled hearing is null. scheduledHearingId =  "
                        + scheduledHearingId);
            }
            debug("ScheduledHearingId : " + scheduledHearingId + " Sequence no :" + currSeqNo);

            // get the sitting
            Sitting sitting = sittingMaintainer.findByPK(sh.getSittingId());
            Iterator sittings = sitting.getScheduledHearings().iterator();

            // for each scheduled hearing in sitting
            while (sittings.hasNext()) {
                // get scheduled hearing
                ScheduledHearing scheduledHearing = (ScheduledHearing) sittings.next();

                debug("Looped Scheduled Hearings, ScheduledHearingId = " + scheduledHearing.getScheduledHearingId()
                        + " sequence no =  " + scheduledHearing.getSequenceNo());

                // if seqno > current seqno
                if (scheduledHearing.getSequenceNo() != null
                        && scheduledHearing.getSequenceNo().compareTo(currSeqNo) > 0) {

                    // create scheduled hearing attendee for this judge
                    SchedHearingAttendeeBasicValue shaValue = new SchedHearingAttendeeBasicValue();

                    // Set Person Type of Attendee
                    shaValue.setAttendeeType(personType);

                    // Set specific id in appropriate type column depending
                    // on type
                    if (personType.equals(PersonValue.JUDGE)) {
                        shaValue.setRefJudgeID(attendeeId);
                        log.debug("*** updateSitting JUDGE : " + attendeeId + " ***");
                    } else if (personType.equals(PersonValue.JUSTICE)) {
                        /**
                         * @todo Need to check hearing type is Appeal before
                         *       amending. This needs to be done once
                         *       establishing which kinds of appeal are
                         *       applicable here
                         */

                        shaValue.setShJusticeID(shJustice.getShJusticeId());
                        log.debug("*** updateSitting JUSTICE : " + attendeeId + " ***");
                    } else if (personType.equals(PersonValue.COURT_REPORTER)) {
                        shaValue.setRefCourtReporterID(attendeeId);
                        log.debug("*** updateSitting COURT REPORTER : " + attendeeId + " ***");
                    } else if (personType.equals(PersonValue.USHER) || personType.equals(PersonValue.COURT_CLERK)) {
                        shaValue.setShStaffID(shStaff.getShStaffId());
                        log.debug("*** updateSitting USHER/COURT CLERK : " + shStaff.getShStaffId() + " ***");
                    }

                    SchedHearingAttendee sha = (SchedHearingAttendee) shaMaintainer.create(shaValue, scheduledHearing, userDisplayName);

                    // ensure staff information is updated for
                    // SchedHearingAttendee
                    sha.setShStaff(shStaff);

                    debug("ScheduledHearingAttendee created : " + shaValue);
                }
            }

            if (personType.equals(PersonValue.JUDGE)) {
                log.debug("*** updateSitting JUDGE sitting value: " + attendeeId + " ***");
                // update sitting with new judgeId
                SittingBasicValue sittingValue = sittingMaintainer.getSittingBasicValue(sitting);
                sittingValue.setRefJudgeID(attendeeId);
                sittingMaintainer.update(sittingValue, userDisplayName);

            }

            debug(methodName + "Exited OK");
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
    }

    /**
     * Notify Public Displays
     * 
     * @param courtRoomId -
     *            court room id
     * @param courtSiteId -
     *            court site id
     * @throws HearingScheduleException
     */
    /*
     * private void notifyPublicDisplays( Integer courtRoomId, Integer
     * courtSiteId ) throws HearingScheduleException {
     * debug("notifyPublicDisplays() courtRoomId: " + courtRoomId +
     * "courtSiteId: " + courtSiteId ); // need courtRoomId and courtSiteId to
     * build urn PDNotificationValue notificationValue = new
     * PDNotificationValue(); notificationValue.setNotificationId(
     * PDNotificationInterface.JUDGE_CHANGED ); // get the court urn for this
     * scheduled hearing String urn; try { CourtLogMessagingHelper clmHelper =
     * new CourtLogMessagingHelper(); urn = clmHelper.buildCourtURN(
     * courtRoomId, courtSiteId );
     * 
     * notificationValue.addParameter( PDNotificationValue.URN, urn );
     * subscriptionHelper.publishEvent("publicDisplayListener",
     * notificationValue); } catch ( CourtLogException e ) {
     * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
     * throw new HearingScheduleException(
     * e.getUserMessageAsMessage().getKey(),e.getMessage(),e); } catch (
     * SubscriptionException e ) {
     * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
     * throw new HearingScheduleException(
     * e.getUserMessageAsMessage().getKey(),e.getMessage(),e); } }
     */

    private void notifyNewPublicDisplays(ScheduledHearing sh, String userDisplayName) {
        try {
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
     * Private method for deleting attendees from XHB_Sched_Hearing_Attendee
     * table. this method takes in a parameter of attencess which require
     * removing.
     * 
     * @param sittingAttendees
     *            Collection
     */
    private void removeSittingAttendees(Collection sittingAttendees, ScheduledHearing sh)
            throws ObjectNotFoundException {
        SchedHearingAttendee sha;

        if (sittingAttendees != null && sittingAttendees.size() > 0) {
            debug("removeSittingAttendees: - SH sitting id = " + sh.getSittingId());

            // loop through all SchedHearingAttendees for staffID
            Iterator sittingAttendeeIter = sittingAttendees.iterator();
            while (sittingAttendeeIter.hasNext()) {
                Object item = sittingAttendeeIter.next();
                if (item != null && item instanceof SchedHearingAttendee) {

                    sha = ((SchedHearingAttendee) item);

                    // Check if in same SH
                    debug("removeSittingAttendees: - SchedHearingAttendee sitting id = "
                            + sha.getScheduledHearing().getSittingId());

                    if (sha.getScheduledHearing().getSittingId().compareTo(sh.getSittingId()) == 0) {
                        // Check if in same sitting
                        if (sha.getScheduledHearing().getSequenceNo().compareTo(sh.getSequenceNo()) >= 0) {
                            debug("removeSittingAttendees:- Deleting Attendees in Sitting for ShAttendeeId:- "
                                    + sha.getShAttendeeId());

                            shaMaintainer.delete(sha.getShAttendeeId(), sha.getVersion());
                        }
                    }
                }
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

    /**
     * Private method for debug. This method checks that debugging is on, If so
     * then the message passed in is sent to the Logger.debug method.
     * 
     * @param mes
     *            String
     */
    private void debug(String mes) {
        if (log.isDebugEnabled()) {
            log.debug(mes);
        }
    }
}
