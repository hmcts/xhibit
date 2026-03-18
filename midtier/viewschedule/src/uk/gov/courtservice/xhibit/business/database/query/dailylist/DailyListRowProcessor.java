package uk.gov.courtservice.xhibit.business.database.query.dailylist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.schedule.ScheduleRowProcessor;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_judge.XhbRefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_justice.XhbRefJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.CourtHouseValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.CourtSiteValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.CourtValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DocumentValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.JudiciaryValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.ListValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.ScheduledValue;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.SittingValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;

/**
 * <p>
 * Title: DailyListRowProcessor
 * </p>
 * <p>
 * Description: The RowProcessor for Daily list, which is the main processor. It
 * extends the ScheduleRowProcessor since the core data is similar. Daily list
 * contains more data such as court, list, justice information etc.
 * 
 * Hierachy of the Values populated by this processor. DailyListValue - Top
 * level - return value - max: 1 value ListValue - first level - max: 1 value
 * CourtValue - first level - max: 1 value CourtSiteValues - first level - max:
 * 0-m values SittingValue - second level - max: 0-m values ScheduledValue -
 * third level - this contains other values, such as courtroom, case, hearing
 * etc. - max: 0-m values
 * 
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
public class DailyListRowProcessor extends ScheduleRowProcessor {
    // The logger for this class.
    private static final Logger log = CSServices.getLogger(DailyListRowProcessor.class);

    // ----------- Database column look ups -------------//
    private static final String COURT_SITE_ID = "COURT_SITE_ID";

    private static final String SITTING_ID = "SITTING_ID";

    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    private static final String JUDGE_ID = "JUDGE_ID";

    private static final String REF_JUSTICE_ID_1 = "JUSTICE1_ID";

    private static final String REF_JUSTICE_ID_2 = "JUSTICE2_ID";

    private static final String REF_JUSTICE_ID_3 = "JUSTICE3_ID";

    private static final String REF_JUSTICE_ID_4 = "JUSTICE4_ID";

    private static final String JUSTICE_NAME_1 = "JUSTICE1_NAME";

    private static final String JUSTICE_NAME_2 = "JUSTICE2_NAME";

    private static final String JUSTICE_NAME_3 = "JUSTICE3_NAME";

    private static final String JUSTICE_NAME_4 = "JUSTICE4_NAME";

    private static final String REF_JUSTICE_TITLE_1 = "JUSTICE1_TITLE";

    private static final String REF_JUSTICE_TITLE_2 = "JUSTICE2_TITLE";

    private static final String REF_JUSTICE_TITLE_3 = "JUSTICE3_TITLE";

    private static final String REF_JUSTICE_TITLE_4 = "JUSTICE4_TITLE";

    private Map justiceCache = new HashMap();

    // ----------- Processors -------------//
    // The childRowProcessor for court data.
    private final ReflectionRowProcessor courtRowProcessor = new ReflectionRowProcessor(CourtValue.class,
            Bindings.COURT);

    // The childRowProcessor for list data.
    private final ReflectionRowProcessor listRowProcessor = new ReflectionRowProcessor(ListValue.class, Bindings.LIST);

    // The childRowProcessor for document data.
    private final ReflectionRowProcessor documentRowProcessor = new ReflectionRowProcessor(DocumentValue.class,
            Bindings.DOCUMENT);

    // The childRowProcessor for courtSite data.
    private final ReflectionRowProcessor courtHouseRowProcessor = new ReflectionRowProcessor(CourtHouseValue.class,
            Bindings.COURT_HOUSE);

    // The childRowProcessor for sitting data.
    private final ReflectionRowProcessor sittingRowProcessor = new ReflectionRowProcessor(SittingValue.class,
            Bindings.SITTING);

    // The childRowProcessor for refcourt.
    private final ReflectionRowProcessor refCourtRowProcessor = new ReflectionRowProcessor(XhbRefCourtBasicValue.class,
            Bindings.REF_COURT);

    // The childRowProcessor for RefJudge.
    private final ReflectionRowProcessor refJudgeRowProcessor = new ReflectionRowProcessor(XhbRefJudgeBasicValue.class,
            Bindings.REF_JUDGE);

    // The childRowProcessor for Court address
    private final ReflectionRowProcessor courtAddressRowProcessor = new ReflectionRowProcessor(
            XhbAddressBasicValue.class, Bindings.COURT_ADDRESS);

    // The childRowProcessor for Court House address
    private final ReflectionRowProcessor courtHouseAddressRowProcessor = new ReflectionRowProcessor(
            XhbAddressBasicValue.class, Bindings.COURT_HOUSE_ADDRESS);

    // ----------- Instance caches -------------//
    // contains the courtsiteid and the courtsitevalue
    private final Map courtSiteCache = new HashMap();

    // we need a Collection to ensure that the values are retrieved in same
    // order as entered (PRE00180), as sorting now performed by database
    private final Collection orderedCourtSiteCache = new ArrayList();

    // contains the sitting and the sittingvalue.
    private final Map sittingCache = new HashMap();

    // contains the schedued hearing value and sitting id it is in
    private final Map scheduledCache = new HashMap();

    // The daily list value that will be populated
    private final DailyListValue dailyListValue = new DailyListValue();

    // Boolean to make sure that list data and court data is only captured
    // once.
    private boolean isFirstRow = true;

    /**
     * Constructor that takes the court id as an argument. It will call the
     * superclass to set up the childProcessors required from there. It will
     * also set up the specific row processors for this class.
     * 
     * @param courtId
     *            Integer
     */
    public DailyListRowProcessor(Integer courtId) {
        // call the super to get all todays scheduled data
        super(courtId);

        log.debug("DailyListRowProcessor called");

        // add the processors for daily list.
        addChildProcessor(listRowProcessor);
        addChildProcessor(courtRowProcessor);
        addChildProcessor(courtHouseRowProcessor);
        addChildProcessor(sittingRowProcessor);
        addChildProcessor(refCourtRowProcessor);
        addChildProcessor(refJudgeRowProcessor);
        addChildProcessor(courtAddressRowProcessor);
        addChildProcessor(courtHouseAddressRowProcessor);
        addChildProcessor(documentRowProcessor);

        log.debug("DailyListRowProcessor finished");
    }

    /**
     * Implementation of row processor. This is the main processor and will call
     * other processors to populate the Daily list data. This will first call
     * the super class to get the TodayScheduleValue.
     * 
     * @param row -
     *            The Row that is being processed.
     */
    public void processRow(Row row) {
        // Call the super class to populate the ScheduledHearingValue
        super.processRow(row);

        log.debug("processRow called");

        // Get the key required to populate the values.
        final Integer courtSiteId = new Integer(row.getInt(COURT_SITE_ID));
        final Integer sittingId = new Integer(row.getInt(SITTING_ID));
        final Integer scheduledHearingId = new Integer(row.getInt(SCHEDULED_HEARING_ID));

        final CourtSiteValue courtSiteValue;
        final SittingValue sittingValue;
        final ScheduledValue scheduledValue;

        log.debug("courtSiteId : " + courtSiteId);
        log.debug("sittingId : " + sittingId);
        log.debug("scheduledHearingId : " + scheduledHearingId);

        // Set the header.
        setHeaderInformation();

        // Get or create the courtsite value from the cache or from the
        // processor
        if (courtSiteCache.containsKey(courtSiteId)) {
            log.debug("courtSiteCache.containsKey(courtSiteId)");
            courtSiteValue = (CourtSiteValue) courtSiteCache.get(courtSiteId);
        } else {
            log.debug("courtSiteCache does not contain (courtSiteId)");
            courtSiteValue = new CourtSiteValue();
            CourtHouseValue courtHouseValue = (CourtHouseValue) courtHouseRowProcessor.getObject();
            courtHouseValue.setAddress((XhbAddressBasicValue) courtHouseAddressRowProcessor.getObject());
            courtSiteValue.setCourtHouseValue(courtHouseValue);

            // add the new courtsite to the cache
            courtSiteCache.put(courtSiteId, courtSiteValue);
            // and add to the ordered list
            orderedCourtSiteCache.add(courtSiteValue);
        }

        // If the sitting is not in the sittingCourtSiteCache then then just get
        // a
        // new sittingvalue from the rowprocessor
        if (sittingCache.containsKey(sittingId)) {
            log.debug("sittingCourtSiteCache.containsKey(sittingId)");
            sittingValue = (SittingValue) sittingCache.get(sittingId);
        } else {
            log.debug("sittingCourtSiteCache does not contain (sittingId)");
            // get the sitting from the processor
            sittingValue = (SittingValue) sittingRowProcessor.getObject();

            // set a new JudiciaryValue to avoid nulls.
            sittingValue.setJudiciaryValue(new JudiciaryValue());

            // Add the sittingvalue to the list in courtsite value.
            courtSiteValue.getSittingValues().add(sittingValue);

            // add the sittingid and the sittingvalue to the cache.
            sittingCache.put(sittingId, sittingValue);
        }

        // set the sitting judge
        setJudge(sittingValue, row);

        // set the sitting justice
        setJustice(sittingValue, row);

        if (scheduledHearingId != 0) {
            if (scheduledCache.containsKey(scheduledHearingId)) {
                log.debug("scheduledCache.containsKey(scheduledHearingId)");
                scheduledValue = (ScheduledValue) scheduledCache.get(scheduledHearingId);
            } else {
                log.debug("scheduledCache does not contain (scheduledHearingId)");
                // populate a new ScheduledValue
                scheduledValue = new ScheduledValue(scheduledHearingId);

                // add the scheduled hearing to the sitting.
                sittingValue.getScheduledValues().add(scheduledValue);

                // add the scheduled hearing to the cached of scheduledhearings.
                scheduledCache.put(scheduledHearingId, scheduledValue);
            }

            // Then set the scheduledhearing value from the super class
            // regardless if it
            // has been set before since it might have been change.
            ScheduledHearingValue scheduledHearingValue = getScheduledHearingValue(scheduledHearingId);
            scheduledValue.setScheduledHearingValue(scheduledHearingValue);

            // populate the scheduledValue with a refcourt, if exist.
            setRefCourt(scheduledValue);
        }

        log.debug("processRow finished");
    }

    /**
     * This method will set the judge for a particular sitting. It will only set
     * the value ones per sitting.
     * 
     * @param sittingValue
     *            SittingValue
     * @param row
     *            The row that is being processed.
     */
    private void setJudge(SittingValue sittingValue, Row row) {
        log.debug("setJudge() called");
        Integer refJudgeId = new Integer(row.getInt(JUDGE_ID));
        log.debug("refJudgeId : " + refJudgeId);

        // if there is a judge value that hasn't been populated
        if (refJudgeId != null
                && (sittingValue.getJudiciaryValue() == null
                        || sittingValue.getJudiciaryValue().getJudgeValue() == null || sittingValue.getJudiciaryValue()
                        .getJudgeValue().getRefJudgeId() == null)) {
            log.debug("Will set the sitting with the judge value.");
            if (sittingValue.getJudiciaryValue() == null) {
                sittingValue.setJudiciaryValue(new JudiciaryValue());
            }
            sittingValue.getJudiciaryValue().setJudgeValue((XhbRefJudgeBasicValue) refJudgeRowProcessor.getObject());
        }
        log.debug("setJudge() finished");
    }

    /**
     * This method will set the justice for a particular sitting. It will only
     * set the same value ones per sitting.
     * 
     * @param sittingValue
     *            SittingValue
     * @param row
     *            The row that is being processed.
     */
    private void setJustice(SittingValue sittingValue, Row row) {
        log.debug("setJustice() called");

        // Check so that the sitting hasn't already has been populated with
        // justices.
        // If the sitting already exist the justices has already been added and
        // we
        // do not need to add them again.

        if (!justiceCache.containsKey(sittingValue.getSittingId())) {
            // justices for a row.
            final String justiceName1 = row.getString(JUSTICE_NAME_1);
            final String justiceName2 = row.getString(JUSTICE_NAME_2);
            final String justiceName3 = row.getString(JUSTICE_NAME_3);
            final String justiceName4 = row.getString(JUSTICE_NAME_4);

            // check if the justices are not null. We only need to check the
            // name
            // since the justices might be entered as freetext. Create if
            // not null.
            if (justiceName1 != null) {
                log.debug("justiceName1 : " + justiceName1);
                final Integer justiceId = new Integer(row.getInt(REF_JUSTICE_ID_1));
                final String justiceTitle = row.getString(REF_JUSTICE_TITLE_1);
                sittingValue.getJudiciaryValue().getJusticeList().add(
                        createJustice(justiceId, justiceName1, justiceTitle));
            }

            if (justiceName2 != null) {
                log.debug("justiceName2 : " + justiceName2);
                final Integer justiceId = new Integer(row.getInt(REF_JUSTICE_ID_2));
                final String justiceTitle = row.getString(REF_JUSTICE_TITLE_2);
                sittingValue.getJudiciaryValue().getJusticeList().add(
                        createJustice(justiceId, justiceName2, justiceTitle));
            }

            if (justiceName3 != null) {
                log.debug("justiceName3 : " + justiceName3);
                final Integer justiceId = new Integer(row.getInt(REF_JUSTICE_ID_3));
                final String justiceTitle = row.getString(REF_JUSTICE_TITLE_3);
                sittingValue.getJudiciaryValue().getJusticeList().add(
                        createJustice(justiceId, justiceName3, justiceTitle));
            }

            if (justiceName4 != null) {
                log.debug("justiceName4 : " + justiceName4);
                final Integer justiceId = new Integer(row.getInt(REF_JUSTICE_ID_4));
                final String justiceTitle = row.getString(REF_JUSTICE_TITLE_4);
                sittingValue.getJudiciaryValue().getJusticeList().add(
                        createJustice(justiceId, justiceName4, justiceTitle));
            }
            // add the sitting id to the justice cache to identify the
            // sitting id that have been completed.
            justiceCache.put(sittingValue.getSittingId(), null);
        }
        log.debug("setJustice() finished");
    }

    /**
     * Metod to create a new justice basic value.
     * 
     * @param refJusticeId
     *            Integer
     * @param justiceName
     *            String
     * @param justiceTitle
     *            String
     * @return XhbRefJusticeBasicValue the populated justice value
     */
    private XhbRefJusticeBasicValue createJustice(Integer refJusticeId, String justiceName, String justiceTitle) {
        log.debug("createJustice() called");

        XhbRefJusticeBasicValue value = new XhbRefJusticeBasicValue();
        value.setRefJusticeId(refJusticeId);
        value.setJusticeName(justiceName);
        value.setTitle(justiceTitle);

        log.debug("createJustice() finished");
        return value;
    }

    /**
     * This method will set the refcourt for a particular scheduled hearing if
     * it has not already been populated.
     * 
     * @param scheduleValue
     *            ScheduledValue
     * 
     */
    private void setRefCourt(ScheduledValue scheduleValue) {
        log.debug("setRefCourt() called");

        // If the scheduled hearing has already been populated with the
        // refcourt data then just return without any further processing. Else
        // popualate the scheduled hearing value with the refcourt.
        if (scheduleValue.getRefCourtBasicValue() != null
                && scheduleValue.getRefCourtBasicValue().getRefCourtId() != null) {
            return;
        }

        log.debug("Will try to populate the XhbRefCourtBasicValue");
        // Get the data from the row process and populate the scheduleValue
        XhbRefCourtBasicValue refCourtBasicValue = (XhbRefCourtBasicValue) refCourtRowProcessor.getObject();

        // finally set the ScheduledValue
        scheduleValue.setRefCourtBasicValue(refCourtBasicValue);
        log.debug("setRefCourt() finished");
    }

    /**
     * Method to set the court data and the list data. This will only be set
     * ones in the top level Value DailyListValue.
     */
    private void setHeaderInformation() {
        if (!isFirstRow)
            return;

        log.debug("setHeaderInformation() called");
        // Set the list data.
        ListValue listValue = (ListValue) listRowProcessor.getObject();
        if (listValue.getHearingListId() != null && listValue.getHearingListId().intValue() != 0) {
            log.debug("Setting the list value");
            dailyListValue.setListValue(listValue);
        }

        DocumentValue documentValue = (DocumentValue) documentRowProcessor.getObject();
        if (documentValue.getHearingListId() != null && documentValue.getHearingListId().intValue() != 0) {
            log.debug("Setting the document value");
            dailyListValue.setDocumentValue(documentValue);
        }

        // Set the court data and its address
        CourtValue courtValue = (CourtValue) courtRowProcessor.getObject();
        if (courtValue != null && courtValue.getCourtId().intValue() != 0) {
            log.debug("Setting the court value");
            dailyListValue.setCourtValue(courtValue);
            dailyListValue.getCourtValue().setAddress((XhbAddressBasicValue) courtAddressRowProcessor.getObject());
        }
        // set the flag to false to ensure the data is only set ones.
        isFirstRow = false;

        log.debug("setHeaderInformation() finished");
    }

    /**
     * Method to return the enitre populated DailyListValue
     * 
     * @return DailyListValue containing the whole list.
     */
    public DailyListValue getDailyList() {
        log.debug("getDailyList() called");

        // Populate the courtsite values before returning the dailylist values.
        dailyListValue.setCourtSiteValues(new ArrayList(orderedCourtSiteCache));

        log.debug("getDailyList() finished");
        return dailyListValue;
    }
}
