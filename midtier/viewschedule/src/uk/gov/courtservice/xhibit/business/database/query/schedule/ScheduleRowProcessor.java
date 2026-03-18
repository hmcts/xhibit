package uk.gov.courtservice.xhibit.business.database.query.schedule;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;

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
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class ScheduleRowProcessor extends AbstractRowProcessor {

    // Column name for scheduled hearing id
    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    // Column name for floating
    private static final String IS_FLOATING = "IS_FLOATING";

    // Column name for sitting sequence number
    private static final String SITTING_SEQ_NO = "SITTING_SEQUENCE_NO";

    // Column name for surname
    private static final String SURNAME = "JUDGE_SURNAME";

    // Column name for id
    private static final String REF_JUDGE_ID = "JUDGE_ID";

    private static final String JUDGE_FULL_LIST_TITLE1 = "JUDGE_FULL_TITLE1";

    // Column name for id
    private static final String HEARING_LIST_START_DATE = "START_DATE";

    // column for courtSite short name
    private static final String SHORT_NAME = "SHORT_NAME";

    // column for crest Court id
    private static final String CREST_COURT_ID = "CREST_COURT_ID";

    // Schedule object that is created
    private final TodaysScheduleValue scheduleValue;

    // Instance cache
    private final Map scheduledHearingCache = new HashMap();

    // Case processor
    private final CaseRowProcessor caseProcessor = new CaseRowProcessor();

    // Court room processor
    private final CourtRoomRowProcessor courtRoomProcessor = new CourtRoomRowProcessor();

    // Defendant on case processor
    private final DefendantOnCaseRowProcessor defendantOnCaseProcessor = new DefendantOnCaseRowProcessor();

    // Defendant processor
    private final DefendantRowProcessor defendantProcessor = new DefendantRowProcessor();

    // Ref hearing processor
    private final RefHearingTypeRowProcessor refHearingProcessor = new RefHearingTypeRowProcessor();

    // Scheduled hearing processor
    private final ScheduledHearingRowProcessor scheduledHearingProcessor = new ScheduledHearingRowProcessor();

    /**
     * Instantiates the object
     * 
     * @param courtId
     */
    public ScheduleRowProcessor(Integer courtId) {
        scheduleValue = new TodaysScheduleValue(courtId);
        addChildProcessor(caseProcessor);
        addChildProcessor(courtRoomProcessor);
        addChildProcessor(defendantOnCaseProcessor);
        addChildProcessor(defendantProcessor);
        addChildProcessor(refHearingProcessor);
        addChildProcessor(scheduledHearingProcessor);
    }

    /**
     * Get a scheduled hearing from the cache.
     * 
     * @param scheduledHearingId
     *            Integer
     * @return ScheduledHearingValue the value
     */
    public ScheduledHearingValue getScheduledHearingValue(Integer scheduledHearingId) {
        // Get the object from the cache
        return (ScheduledHearingValue) scheduledHearingCache.get(scheduledHearingId);
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {

        Integer scheduledHearingId = new Integer(row.getInt(SCHEDULED_HEARING_ID));

        final ScheduledHearingValue val;
        if (scheduledHearingCache.containsKey(scheduledHearingId)) {
            val = getScheduledHearingValue(scheduledHearingId);
        } else {
            // Create a new object
            val = new ScheduledHearingValue();

            // Set the case basic value
            if (caseProcessor.getCase().getId() != null && caseProcessor.getCase().getId().intValue() != 0)
                val.setCaseBasicValue(caseProcessor.getCase());

            // Set the court room value
            if (courtRoomProcessor.getCourtRoom().getId() != null
                    && courtRoomProcessor.getCourtRoom().getId().intValue() != 0)
                val.setCourtRoomValue(courtRoomProcessor.getCourtRoom());

            // Set the ref hearing type basic value
            if (refHearingProcessor.getRefHearingType().getId() != null
                    && refHearingProcessor.getRefHearingType().getId().intValue() != 0)
                val.setRefHearingTypeBasicValue(refHearingProcessor.getRefHearingType());

            // Set the scheduled hearing basic value
            if (scheduledHearingProcessor.getScheduledHearing().getId() != null
                    && scheduledHearingProcessor.getScheduledHearing().getId().intValue() != 0)
                val.setScheduledHearingBasicValue(scheduledHearingProcessor.getScheduledHearing());

            // Set the sitting sequence number
            val.setSittingSequenceNo(new Integer(row.getInt(SITTING_SEQ_NO)));
            // Set the judge
            val.setJudge(getJudge(row));
            // Set the judge id
            val.setRefJudgeId(new Integer(row.getInt(REF_JUDGE_ID)));
            // Set floating
            val.setIsFloating(new Boolean(row.getInt(IS_FLOATING) == 1));
            // set the courtsite shortname
            val.setCourtSiteShortName(row.getString(SHORT_NAME));
            // set the crest court id
            val.setCrestCourtId(row.getString(CREST_COURT_ID));
            // Set hearing list start date
            Calendar hearingListStartDate = Calendar.getInstance();
            hearingListStartDate.setTime(row.getDate(HEARING_LIST_START_DATE));
            val.setHearingListStartDate(hearingListStartDate);
            // Add to cache
            scheduledHearingCache.put(scheduledHearingId, val);
            // and to the collection
            this.scheduleValue.getScheduledHearings().add(val);
        }

        // Add defendant
        if (defendantProcessor.getDefendant().getId() != null
                && defendantProcessor.getDefendant().getId().intValue() != 0)
            val.addDefendantBasicValue(defendantProcessor.getDefendant());

        // Add defendant on case
        if (defendantOnCaseProcessor.getDefendantOnCase().getId() != null
                && defendantOnCaseProcessor.getDefendantOnCase().getId().intValue() != 0)
            val.addDefendantOnCaseBasicValue(defendantOnCaseProcessor.getDefendantOnCase());

    }

    /**
     * Returns the schedule
     * 
     * @return TodaysScheduleValue
     */
    public TodaysScheduleValue getSchedule() {
        return scheduleValue;
    }

    // get the judges name
    private String getJudge(Row row) {

        String surname = row.getString(SURNAME);
        String fullTitle = row.getString(JUDGE_FULL_LIST_TITLE1);

        // Business rule change
        // IF FULL_LIST_TITLE1 is not NULL
        // THEN display FULL_LIST_TITLE1
        // ELSE IF SURNAME is not null
        // THEN display SURNAME
        // ELSE display blank

        if (fullTitle != null && fullTitle.trim().length() > 0) {
            return fullTitle;
        } else if (surname != null && surname.trim().length() > 0) {
            return surname;
        } else {
            return "";
        }
    }
}