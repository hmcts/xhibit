package uk.gov.courtservice.xhibit.courtlog.cjse;

import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>Title: Class to supply fixure data to JUnit tests</p>
 * <p>Description: </p>
 * <p>
 * When writing unit tests across the whole piece, it has become obvious that
 * we could spend and awful lot of the time writing a lot of duplicated code in
 * setUp and tearDown methods, this class should reduce that problem.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Eds</p>
 * @author Bob Boothby
 * @version 1.0
 */
public class CjseEventFixture extends java.lang.Object
{
    /**
     * Do not construct.
     */
    private CjseEventFixture()
    {
        //Do not construct.
    }

    /**
     * Get a case level CourtLogSubscriptionValue containing data:
     * <ul>
     * <li>Isleworth court.</li>
     * <li>Court site 1.</li>
     * <li>Court room 1.</li>
     * <li>Hearing ID of 1.</li>
     * <li>An CourtLogViewValue containing.
     * <ul>
     * <li>Case ID of 1.</li>
     * <li>Entry date set according to time of call.</li>
     * <li>Event type set to that passed in.</li>
     * <li><Log entry set to that passed in./li>
     * </ul>
     * </li>
     * <li>PN event type of NOT_SET.</li>
     * </ul>
     * @param eventType The integer reflecting the desired eventType.
     * @param logEntry The XML log entry for the desired event.
     * @return an instance of CourtLogSubscriptionValue for a case level
     * event.
     */
    public static CourtLogSubscriptionValue getCaseCLSubsValue1(
            Integer eventType, String logEntry)
    {
        CourtLogSubscriptionValue value = getUndifferentiatedCLSubsValue();
        //First hearing for a scheduled hearing in the above court room.
        value.setHearingId(new Integer(1));

        //Set the case for this value object
        value.getCourtLogViewValue().setCaseId(new Integer(1));
        value.getCourtLogViewValue().setEntryDate(new java.util.Date());
        value.getCourtLogViewValue().setEventType(eventType);
        value.getCourtLogViewValue().setLogEntry(logEntry);
        return value;
    }

    /*
     * Get a defendant level CourtLogSubscriptionValue containing data:
     *
     * @param eventType The integer reflecting the desired eventType.
     * @param logEntry The XML log entry for the desired event.
     * @return an instance of CourtLogSubscriptionValue for a defendant level
     * event.
     *
    public static CourtLogSubscriptionValue getDefendantCLSubsValue1(
            Integer eventType, String logEntry)
    {
        //CourtLogSubscriptionValue value = getUndifferentiatedCLSubsValue();
        //customise value
        // @todo implement
        return null;
    }
*/

    /*
     * Get a CRN level CourtLogSubscriptionValue containing data:
     *
     * @param eventType The integer reflecting the desired eventType.
     * @param logEntry The XML log entry for the desired event.
     * @return an instance of CourtLogSubscriptionValue for a CRN level
     * event.
     *
    public static CourtLogSubscriptionValue getCRNCLSubsValue1(
            Integer eventType, String logEntry)
    {
        //CourtLogSubscriptionValue value = getUndifferentiatedCLSubsValue();
        //customise value
        // @todo implement
        return null;
    }
    */

    /**
     * Get a CourtLogSubscriptionValue containing basic data reflecting:
     * <ul>
     * <li>Isleworth court</li>
     * <li>Court site 1</li>
     * <li>Court room 1</li>
     * <li>An empty CourtLogViewValue</li>
     * <li>PN event type of NOT_SET</li>
     * </ul>
     * @return an instance of CourtLogSubscriptionValue containing basic data
     */
    private static CourtLogSubscriptionValue getUndifferentiatedCLSubsValue()
    {
        //Basic graph of objects.
        CourtLogSubscriptionValue value =  new CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        value.setCourtLogViewValue(viewValue);

        //Snaresbrook site A court room 1
        value.setCourtSiteId(new Integer(3));
        value.setCourtRoomId(new Integer(31));
        value.setCourtURN("//ISLEW/1/1");

        //NOT_SET - from CourtLogMessagingHelper
        value.setPnEventType(new Integer(-1));

        return value;
    }
}
