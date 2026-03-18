package uk.gov.courtservice.xhibit.business.database.query.counsel;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtRoom;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.Defendant;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.Person;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.Sitting;

/**
 * The custom row processor used to create a <code>CourtList</code> object and
 * all contained objects (i.e. <code>ScheduledHearing</code>s.
 * 
 * @author tz0d5m
 * @version $Id: CounselFaciltiesRowProcessor.java,v 1.3 2004/12/20 09:53:14
 *          tz0d5m Exp $
 */
public final class CounselFaciltiesRowProcessor extends AbstractRowProcessor {
    // /////////////////////////////////////////////////////////////////////////
    // All of the constants used to define the database column mappings...
    // /////////////////////////////////////////////////////////////////////////

    // for use in processCourtList(..)...
    private static final String COURT_ID = "COURT_ID";

    private static final String COURT_TYPE = "COURT_TYPE";

    private static final String COURT_NAME = "COURT_NAME";

    private static final String COURT_SHORT_NAME = "COURT_SHORT_NAME";

    private static final String START_DATE = "START_DATE";

    private static final String REQUEST_DATE = "REQUEST_DATE";

    // for use in processCourtRoom(..)...
    private static final String COURT_ROOM_ID = "COURT_ROOM_ID";

    private static final String COURT_SITE_SHORT_NAME = "COURT_SITE_SHORT_NAME";

    private static final String COURT_ROOM_DISPLAY_NAME = "COURT_SITE_DISPLAY_NAME";

    // for use in processSitting(..)...
    private static final String SITTING_ID = "SITTING_ID";

    private static final String FLOATING = "FLOATING";

    private static final String JUDGE_NAME = "JUDGE_NAME";

    private static final String SITTING_TIME = "SITTING_TIME";

    // for use in processScheduledHearing(..)...
    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    private static final String CASE_ID = "CASE_ID";

    private static final String CASE_TITLE = "CASE_TITLE";

    private static final String CASE_TYPE = "CASE_TYPE";

    private static final String CASE_NUMBER = "CASE_NUMBER";

    private static final String HEARING_TYPE = "HEARING_TYPE";

    private static final String NOT_BEFORE_TIME = "NOT_BEFORE_TIME";

    // for use in processDefendant(..)...
    private static final String DEF_ID = "DEF_ID";

    private static final String DEF_FIRST_NAME = "DEF_FIRST_NAME";

    private static final String DEF_MIDDLE_NAME = "DEF_MIDDLE_NAME";

    private static final String DEF_SURNAME = "DEF_SURNAME";

    private static final String DEF_NAME_IS_MASKED = "DEF_IS_MASKED";

    private static final String DEF_MASKED_NAME = "DEF_MASKED_NAME";

    // for use in processDefendantAdvocate(..)...
    private static final String DEF_ADVOCATE_ID = "DEF_ADVOCATE_ID";

    private static final String DEF_ADVOCATE_TITLE = "DEF_ADVOCATE_TITLE";

    private static final String DEF_ADVOCATE_FIRST_NAME = "DEF_ADVOCATE_FIRST_NAME";

    private static final String DEF_ADVOCATE_MIDDLE_NAME = "DEF_ADVOCATE_MIDDLE_NAME";

    private static final String DEF_ADVOCATE_SURNAME = "DEF_ADVOCATE_SURNAME";

    // for use in processProsecutionAdvocate(..)...
    private static final String PROS_ADVOCATE_ID = "PROS_ADVOCATE_ID";

    private static final String PROS_ADVOCATE_TITLE = "PROS_ADVOCATE_TITLE";

    private static final String PROS_ADVOCATE_FIRST_NAME = "PROS_ADVOCATE_FIRST_NAME";

    private static final String PROS_ADVOCATE_MIDDLE_NAME = "PROS_ADVOCATE_MIDDLE_NAME";

    private static final String PROS_ADVOCATE_SURNAME = "PROS_ADVOCATE_SURNAME";

    // for use in processStaff(..)...
    private static final String STAFF_ID = "STAFF_ID";

    private static final String STAFF_ROLE = "STAFF_ROLE";

    private static final String STAFF_NAME = "STAFF_NAME";

    // /////////////////////////////////////////////////////////////////////////
    // The constants used to define the court staff roles...
    // /////////////////////////////////////////////////////////////////////////

    private static final String USHER_ROLE = "U";

    private static final String COURT_CLERK_ROLE = "CC";

    // /////////////////////////////////////////////////////////////////////////
    // The other constants...
    // /////////////////////////////////////////////////////////////////////////

    private static final String YES = "Y";

    private static final String IS_FLOATING = "1";

    // /////////////////////////////////////////////////////////////////////////
    // The instance variables...
    // /////////////////////////////////////////////////////////////////////////

    private CourtList courtList = null;

    /**
     * The implementation of the framework method to process the row for a court
     * list entry. This will create the <code>CourtList</code> object if it
     * has not already been initialised.
     * 
     * @param row
     *            The current row being processed.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        // populate courtList...
        if (this.courtList == null) {
            this.courtList = new CourtList(row.getInteger(COURT_ID), row.getString(COURT_TYPE), row
                    .getString(COURT_NAME), row.getString(COURT_SHORT_NAME), row.getTimestamp(START_DATE), row
                    .getTimestamp(REQUEST_DATE));
        }

        processCourtRoom(row, this.courtList);
    }

    /**
     * Process the row to determine if the court room details are already
     * present in the <code>CourtList</code> value object. If not, then a
     * <code>CourtRoom</code> will be created and added.
     * 
     * If the sitting has been identified as floating, then no room will be
     * created, instead the floating court room on the <code>CourtList</code>
     * object will be used to further process the row.
     */
    private void processCourtRoom(final Row row, final CourtList list) {
        // first determine if it is a floating sitting...
        final boolean floating = IS_FLOATING.equals(row.getString(FLOATING));

        if (floating == true) {
            // process using the floating court room...
            processSitting(row, list.acquireFloatingCourtRoom());
        } else {
            final Integer courtRoomId = row.getInteger(COURT_ROOM_ID);

            if (courtRoomId != null) {
                CourtRoom courtRoom = list.getCourtRoom(courtRoomId);

                if (courtRoom == null) {
                    courtRoom = new CourtRoom(courtRoomId, row.getString(COURT_SITE_SHORT_NAME), row
                            .getString(COURT_ROOM_DISPLAY_NAME), floating);
                    list.addCourtRoom(courtRoom);
                }

                // process using the new (or found) court room...
                processSitting(row, courtRoom);
            }
        }
    }

    /**
     * Process the row to determine if the sitting details are already present
     * in the <code>CourtList</code> value object. If not, then a
     * <code>Sitting</code> will be created and added.
     */
    private void processSitting(final Row row, final CourtRoom courtRoom) {
        final Integer sittingId = row.getInteger(SITTING_ID);

        // although should not be null, it can not be prevented, so check...
        if (sittingId != null) {
            Sitting sitting = courtRoom.getSitting(sittingId);

            if (sitting == null) {
                sitting = new Sitting(sittingId, row.getString(JUDGE_NAME), row.getTimestamp(SITTING_TIME));

                courtRoom.addSitting(sitting);
            }

            processStaff(row, sitting);
            processScheduledHearing(row, sitting);
        }
    }

    /**
     * Create and populate the scheduled hearing details from the results, and
     * add them to the <code>Sitting</code> if appropriate.
     */
    private void processScheduledHearing(final Row row, final Sitting sitting) {
        final Integer scheduledHearingId = row.getInteger(SCHEDULED_HEARING_ID);

        // although should not be null, it can not be prevented, so check...
        if (scheduledHearingId != null) {
            ScheduledHearing scheduledHearing = sitting.getScheduledHearing(scheduledHearingId);

            if (scheduledHearing == null) {
                scheduledHearing = new ScheduledHearing(scheduledHearingId, row.getString(CASE_TITLE), row
                        .getString(HEARING_TYPE), row.getString(CASE_TYPE), row.getString(CASE_NUMBER), row
                        .getInteger(CASE_ID), row.getTimestamp(NOT_BEFORE_TIME));

                sitting.addScheduedHearing(scheduledHearing);
            }

            processProsecutionAdvocate(row, scheduledHearing);
            processDefendant(row, scheduledHearing);
        }
    }

    /**
     * Create and populate the defendants details from the results, and add them
     * to the <code>ScheduledHearing</code> if appropriate.
     */
    private void processDefendant(final Row row, final ScheduledHearing scheduledHearing) {
        final Integer defendantId = row.getInteger(DEF_ID);

        if (defendantId != null) {
            Defendant defendant = scheduledHearing.getDefendant(defendantId);

            if (defendant == null) {
                defendant = new Defendant(defendantId, row.getString(DEF_FIRST_NAME), row.getString(DEF_MIDDLE_NAME),
                        row.getString(DEF_SURNAME), YES.equals(row.getString(DEF_NAME_IS_MASKED)), row
                                .getString(DEF_MASKED_NAME));

                scheduledHearing.addDefendant(defendant);
            }

            processDefendantAdvocate(row, defendant);
        }
    }

    /**
     * Create and populate the defence advocates details from the results, and
     * add them to the <code>Defendant</code> if appropriate.
     */
    private void processDefendantAdvocate(final Row row, final Defendant defendant) {
        final Integer defAdvocateId = row.getInteger(DEF_ADVOCATE_ID);

        if (defAdvocateId != null) {
            Person defAdvocate = defendant.getDefendantAdvocate(defAdvocateId);

            if (defAdvocate == null) {
                defAdvocate = new Person(defAdvocateId, row.getString(DEF_ADVOCATE_TITLE), row
                        .getString(DEF_ADVOCATE_FIRST_NAME), row.getString(DEF_ADVOCATE_MIDDLE_NAME), row
                        .getString(DEF_ADVOCATE_SURNAME));

                defendant.addDefendantAdvocate(defAdvocate);
            }
        }
    }

    /**
     * Create and populate the prosecution advocates details from the results,
     * and add them to the <code>ScheduledHearing</code> if appropriate.
     */
    private void processProsecutionAdvocate(final Row row, final ScheduledHearing scheduledHearing) {
        final Integer prosAdvocateId = row.getInteger(PROS_ADVOCATE_ID);

        if (prosAdvocateId != null) {
            Person prosAdvocate = scheduledHearing.getProsecutionAdvocate(prosAdvocateId);

            if (prosAdvocate == null) {
                prosAdvocate = new Person(prosAdvocateId, row.getString(PROS_ADVOCATE_TITLE), row
                        .getString(PROS_ADVOCATE_FIRST_NAME), row.getString(PROS_ADVOCATE_MIDDLE_NAME), row
                        .getString(PROS_ADVOCATE_SURNAME));

                scheduledHearing.addProsecutionAdvocate(prosAdvocate);
            }
        }
    }

    /**
     * Populate the court clerks and ushers details from the results, and add
     * them to the <code>Sitting</code> if appropriate.
     */
    private void processStaff(final Row row, final Sitting sitting) {
        final String role = row.getString(STAFF_ROLE);

        if (COURT_CLERK_ROLE.equals(role)) {
            sitting.addCourtClerk(row.getInteger(STAFF_ID), row.getString(STAFF_NAME));
        } else if (USHER_ROLE.equals(role)) {
            sitting.addUsher(row.getInteger(STAFF_ID), row.getString(STAFF_NAME));
        }
    }

    /**
     * Method to acquire the newly constructed <code>CourtList</code>. Note,
     * that if the query has not been executed, or there were no results, then
     * the returned object will be an unitialised new instance.
     * 
     * @return The <code>CourtList</code>.
     */
    public CourtList getCourtList() {
        if (this.courtList == null) {
            return new CourtList();
        }

        return this.courtList;
    }
}
