package uk.gov.courtservice.xhibit.courtlog.witness;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogRuntimeException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * A utility class to hold all of the the test data used by the witness
 * subscriber tests.
 *
 * @author tz0d5m
 * @version $Revision: 1.5 $
 */
public class WitnessTestData
{
    protected static final int NUMBER_OF_WITNESS_CATEGORY_EVENTS = 5;
    protected static final Integer LAST_WITNESS_ID = new Integer(1); 

    private static final Object[][] LOG_ENTRIES = {
        { "1",  "10100", "30/04/2004 11:00", null, "<Listed_Def_On_Case_Ids><Def_On_Case_Id><doc_id>1</doc_id></Def_On_Case_Id></Listed_Def_On_Case_Ids>" },
        { "34", "20904", "30/04/2004 11:10", "1",  "<E20904_Witness_Sworn_Options><E20904_WSO_Number></E20904_WSO_Number><E20904_WSO_Name>Test Defendant</E20904_WSO_Name><E20904_WSO_Type>E20904_Defendant_sworn</E20904_WSO_Type><E20904_WSO_ID>-1</E20904_WSO_ID></E20904_Witness_Sworn_Options><defendant_on_case_id>1</defendant_on_case_id><defendant_masked_flag>N</defendant_masked_flag><defendant_name>Test Defendant</defendant_name><defendant_masked_name/>"},
        { "35", "20905", "30/04/2004 11:15", null, "<E20905_WSO_Name>Test Defendant</E20905_WSO_Name>"},
        { "55", "30100", "30/04/2004 11:20", null, "<Listed_Def_On_Case_Ids><Def_On_Case_Id><doc_id>1</doc_id></Def_On_Case_Id></Listed_Def_On_Case_Ids><E30100_Short_Adjourn_Options><E30100_SAO_Time>12:00</E30100_SAO_Time><E30100_SAO_Type>E30100_Case_adjourned_until</E30100_SAO_Type></E30100_Short_Adjourn_Options>"},
        { "5",  "10500", "30/04/2004 12:00", null, "<Listed_Def_On_Case_Ids><Def_On_Case_Id><doc_id>1</doc_id></Def_On_Case_Id></Listed_Def_On_Case_Ids>"},
        { "34", "20904", "30/04/2004 12:10", null, "<E20904_Witness_Sworn_Options><E20904_WSO_Number>1</E20904_WSO_Number><E20904_WSO_Name>Fred</E20904_WSO_Name><E20904_WSO_Type>E20904_Prosecution_witness_expert_sworn</E20904_WSO_Type><E20904_WSO_ID>-1</E20904_WSO_ID></E20904_Witness_Sworn_Options>"},
        { "55", "30100", "30/04/2004 13:00", null, "<Listed_Def_On_Case_Ids><Def_On_Case_Id><doc_id>1</doc_id></Def_On_Case_Id></Listed_Def_On_Case_Ids><E30100_Short_Adjourn_Options><E30100_SAO_Time>14:00</E30100_SAO_Time><E30100_SAO_Type>E30100_Case_adjourned_until</E30100_SAO_Type></E30100_Short_Adjourn_Options>" },
        { "5",  "10500", "30/04/2004 14:00", null, "<Listed_Def_On_Case_Ids><Def_On_Case_Id><doc_id>1</doc_id></Def_On_Case_Id></Listed_Def_On_Case_Ids>" },
        { "35", "20905", "30/04/2004 14:30", null, "<E20905_WSO_Name>Fred</E20905_WSO_Name><E20905_WSO_Number>1</E20905_WSO_Number>"},
        { "34", "20904", "30/04/2004 14:40", null, "<E20904_Witness_Sworn_Options><E20904_WSO_Number>2</E20904_WSO_Number><E20904_WSO_Name>Boris</E20904_WSO_Name><E20904_WSO_Type>E20904_Defence_witness_expert_sworn</E20904_WSO_Type><E20904_WSO_ID>1</E20904_WSO_ID></E20904_Witness_Sworn_Options>" }
    };

    private static final String XML_HEADER = "<?xml version=''1.0'' encoding=''UTF-8''?><event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''{0}.xsd''>";
    private static final String XML_FOOTER = "<free_text/><TYPE>{0}</TYPE></event>";

    private static final String INSERT_SQL = "INSERT INTO xhb_court_log_entry"
        + "(case_id, scheduled_hearing_id, event_desc_id, date_time, defendant_on_case_id, log_entry_xml)"
        + " VALUES (?, ?, ?, ?, ?, ?)";

    private static final int EVENT_DESC_ID = 0;
    private static final int EVENT_TYPE = 1;
    private static final int DATE_TIME = 2;
    private static final int DEF_ON_CASE_ID = 3;
    private static final int XML = 4;

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    /** Constant used to represent one hour in milliseconds */
    private static final int ONE_HOUR = 1000 * 60 * 60;
    /** Constant used to represent one day in milliseconds */
    private static final int ONE_DAY = ONE_HOUR * 24;

    /**
     * Utility method to construct a number of witness related court log
     * entries for the specified case, for an appeal case type.  Following
     * this method call all previous court log entries for the case will be
     * removed, and replaced with the new ones.
     *
     * @param caseId The case the log entries are to be associated with
     * @return The number of newly created court log entries.
     * @see #constructCourtLog(java.lang.Integer, java.lang.Integer,
     *      java.lang.Integer, java.lang.String)
     */
    protected static int constructAppealCourtLog(Integer caseId)
    {
        return constructCourtLog(caseId,
                                 new Integer[] {
                                     WitnessHelper.APPEAL_WITNESS_SWORN_INT,
                                     WitnessHelper.APPEAL_WITNESS_RELEASED_INT
                                 },
                                 WitnessHelper.APPEAL_CASE_TYPE,
                                 8);
    }

    /**
     * Utility method to construct a number of court log entries of the passed
     * in types for the specified case.  Following this method call all
     * previous court log entries for the case will be removed, and replaced
     * with the new ones.
     *
     * @param caseId The case the log entries are to be associated with
     * @param eventTypeIds an <code>Integer[]</code> of all event types to
     *        create.
     * @param caseType The case type to set the case to
     * @param numDaysEvents The multiple of the event types to create, by day. 
     * @return The number of newly created court log entries.
     */
    private static int constructCourtLog(Integer scheduledHearingId,
                                           Integer[] eventTypeIds,
                                           String caseType,
                                           int numDaysEvents)
    {
        final XhbCourtLogEventDesc[] eventTypes = new XhbCourtLogEventDesc[eventTypeIds.length];

        for (int i = 0; i < eventTypeIds.length; i++)
        {
            eventTypes[i] = EventHelper.getXhbCourtLogEventDescByEventType(eventTypeIds[i]);
        }

        final XhbScheduledHearing xhbScheduledHearing =
                EntityHelper.getXhbScheduledHearing(scheduledHearingId);
        final XhbCase xhbCase = xhbScheduledHearing.getXhbHearing().getXhbCase();
        xhbCase.setCaseType(caseType);

        // ensure that there are no previous court log entries...
        xhbCase.getXhbCourtLogEntries().clear();

        int courtLogEventsCreated = 0;

        // now construct the court log entries that we need for the tests...
        for (int i = 0; i < numDaysEvents; i++)
        {
            for (int j = 0; j < eventTypes.length; j++)
            {
                final long tmp = System.currentTimeMillis() - (numDaysEvents * ONE_DAY)
                        + (i * ONE_DAY) + (j * ONE_HOUR);
                getNewCourtLogEntryId(xhbCase, xhbScheduledHearing, eventTypes[j], new Date(tmp));
                courtLogEventsCreated++;
            }
        }

        return courtLogEventsCreated;
    }
    
    
    /**
     * Utility method to construct a number of court log events for the
     * specified scheduled hearing, for a trial case type.  Following
     * this method call all previous court log entries for the case will be
     * removed, and replaced with the new ones.
     *
     * @param scheduledHearingId The scheduled hearing id the log entries are
     * to be associated with
     */
    protected static void constructTrialCourtLog(Integer scheduledHearingId, Connection connection)
    {
        final XhbScheduledHearing xhbScheduledHearing =
            EntityHelper.getXhbScheduledHearing(scheduledHearingId);
        final XhbCase xhbCase = xhbScheduledHearing.getXhbHearing().getXhbCase();
    
        xhbCase.getXhbCourtLogEntries().clear();
    
        PreparedStatement pstmt = null;
        
        try
        {
            pstmt = connection.prepareStatement(INSERT_SQL);
            pstmt.setInt(1, xhbCase.getCaseId().intValue());
            pstmt.setInt(2, scheduledHearingId.intValue());
    
            for (int i = 0; i < LOG_ENTRIES.length; i++) {
                pstmt.setInt(3, Integer.parseInt((String) LOG_ENTRIES[i][EVENT_DESC_ID]));
                pstmt.setDate(4, new java.sql.Date(dateFormat.parse((String) LOG_ENTRIES[i][DATE_TIME]).getTime()));
    
                if (LOG_ENTRIES[i][DEF_ON_CASE_ID] != null)
                {   
                    pstmt.setInt(5, Integer.parseInt((String) LOG_ENTRIES[i][DEF_ON_CASE_ID]));
                }
                else
                {
                    pstmt.setNull(5, Types.INTEGER);
                }
    
                final String xml = XML_HEADER + LOG_ENTRIES[i][XML] + XML_FOOTER;
                MessageFormat messageFormat = new MessageFormat
                (xml);
                Object[] parameters = { LOG_ENTRIES[i][EVENT_TYPE] };
    
                pstmt.setString(6, messageFormat.format(parameters));
    
                pstmt.execute();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new CourtLogRuntimeException(e);
        }
        finally
        {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * TODO
     * @param xhbSscheduledHearing
     * @return
     */
    protected static Integer getCaseId(XhbScheduledHearing xhbSscheduledHearing)
    {
        return xhbSscheduledHearing.getXhbHearing().getCaseId();
    }

    /**
     * TODO
     * @param scheduledHearingId
     * @return
     */
    protected static Integer getCaseId(Integer scheduledHearingId)
    {
        return getCaseId(EntityHelper.getXhbScheduledHearing(scheduledHearingId));
    }
    
    
    /**
     * Utility method that constructs a court log entry without any attached
     * defendant on case or offence, and also has a cleared out logEntryXml.
     *
     * @param xhbCase The case the log event is associated with
     * @param xhbCourtLogEventDesc The event description for the new
     *        court log event
     * @param dateTime The date (and time) that the court log entry takes place
     * @return The primary key of the newly created court log entry
     */
    private static Long getNewCourtLogEntryId(XhbCase xhbCase,
                                              XhbScheduledHearing xhbScheduledHearing,
                                              XhbCourtLogEventDesc xhbCourtLogEventDesc,
                                              Date dateTime)
    {
        // validation not needed as private method, we can guarantee what
        // is passed to this method.

        final XhbCourtLogEntryBasicValue value = new XhbCourtLogEntryBasicValue();
        // not-null constraints on database...
        value.setDateTime(dateTime);
        value.setLogEntryXml("<test />");
        value.setCaseId(xhbCase.getCaseId());
        value.setScheduledHearingId(xhbScheduledHearing.getScheduledHearingId());
        value.setEventDescId(xhbCourtLogEventDesc.getEventDescId());

        final XhbCourtLogEntry courtLogEntry = XhbCourtLogEntryBeanHelper2.createLocal(value);

        // ensure that a court log entry was created...
        Assert.assertNotNull("Error creating the value", courtLogEntry);
        Assert.assertNotNull("Entry id returned was null", courtLogEntry.getEntryId());
        
        return courtLogEntry.getEntryId();
    }

    /**
     * TODO
     * @param scheduledHearingId
     * @return
     */
    protected static CourtLogCRUDValue createWitnessSwornCourtLogCRUDValue(
            Integer scheduledHearingId)
    {
        final Integer caseId = getCaseId(scheduledHearingId);

        // construct the CRUD value to pass to the end hearing delete logic
        final CourtLogCRUDValue clcv = new CourtLogCRUDValue();
        clcv.setEventType(WitnessHelper.TRIAL_WITNESS_SWORN_INT);
        clcv.setCaseId(caseId);
        clcv.setScheduledHearingId(scheduledHearingId);
        clcv.setEntryDate(new Date());

        return clcv;
    }

    /**
     * TODO
     * @param scheduledHearingId
     * @return
     */
    protected static CourtLogCRUDValue createWitnessReleasedCourtLogCRUDValue(
            Integer scheduledHearingId)
    {
        final CourtLogCRUDValue clcv = 
                createWitnessSwornCourtLogCRUDValue(scheduledHearingId);
        clcv.setEventType(WitnessHelper.TRIAL_WITNESS_RELEASED_INT);

        return clcv;
    }
}
