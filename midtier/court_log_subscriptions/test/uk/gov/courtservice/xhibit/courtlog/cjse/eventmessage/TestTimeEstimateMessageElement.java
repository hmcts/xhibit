package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.TimeEstimateMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;

/**
 * <p>Title: TestTimeEstimateMessageElement</p>
 * <p>Description: Tests the retrieval of a time estimate in days from the
 * events 20901 and 40702</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Sarah Tong
 * @version $Id: TestTimeEstimateMessageElement.java,v 1.1 2004/04/20 14:43:54 pznwc5 Exp $
 */
public class TestTimeEstimateMessageElement extends TestCase
{
    private static final Integer DIRECTIONS_BY_CASE = new Integer(40711);
    private static final Integer TIME_ESTIMATE      = new Integer(20901);

    private static final String xmlStringDays =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<event xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"20901.xsd\">" +
            " <type>20900</type>" +
            " <date>2002-11-25</date>" +
            " <time/>" +
            " <E20901_Time_Estimate_Options>" +
            "         <E20901_TEO_time>10</E20901_TEO_time>" +
            "         <E20901_TEO_units>E20901_days</E20901_TEO_units>" +
            " </E20901_Time_Estimate_Options>" +
            "</event>";

    private static final String xmlStringWeeks =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<event xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"20901.xsd\">" +
            " <type>20900</type>" +
            " <date>2002-11-25</date>" +
            " <time/>" +
            " <E20901_Time_Estimate_Options>" +
            "         <E20901_TEO_time>10</E20901_TEO_time>" +
            "         <E20901_TEO_units>E20901_weeks</E20901_TEO_units>" +
            " </E20901_Time_Estimate_Options>" +
            "</event>";

    private static final String xmlStringMonths =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<event xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"20901.xsd\">" +
            " <type>20900</type>" +
            " <date>2002-11-25</date>" +
            " <time/>" +
            " <E20901_Time_Estimate_Options>" +
            "         <E20901_TEO_time>10</E20901_TEO_time>" +
            "         <E20901_TEO_units>E20901_months</E20901_TEO_units>" +
            " </E20901_Time_Estimate_Options>" +
            "</event>";

    private static final String xmlStringDirDays =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='40711.xsd'>" +
                "  <free_text/>" +
                "  <type>40711</type>" +
                "  <Directions_By_Case_Options>" +
                "  <E40711_Time_Estimate>" +
                "    <E40711_Time>10</E40711_Time>" +
                "    <E40711_Time_Estimate_Options>E40711_Days</E40711_Time_Estimate_Options>" +
                "  </E40711_Time_Estimate>" +
                "  </Directions_By_Case_Options>" +
		"  </event>";

    private static final String xmlStringDirWeeks =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='40711.xsd'>" +
        "  <free_text/>" +
        "  <type>40711</type>" +
        "  <Directions_By_Case_Options>" +
        "  <E40711_Time_Estimate>" +
        "    <E40711_Time>10</E40711_Time>" +
        "    <E40711_Time_Estimate_Options>E40711_Weeks</E40711_Time_Estimate_Options>" +
        "  </E40711_Time_Estimate>" +
        "  </Directions_By_Case_Options>" +
        "  </event>";

    private static final String xmlStringDirMonths =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='40711.xsd'>" +
        "  <free_text/>" +
        "  <type>40711</type>" +
        "  <Directions_By_Case_Options>" +
        "  <E40711_Time_Estimate>" +
        "    <E40711_Time>10</E40711_Time>" +
        "    <E40711_Time_Estimate_Options>E40711_Months</E40711_Time_Estimate_Options>" +
        "  </E40711_Time_Estimate>" +
        "  </Directions_By_Case_Options>" +
        "  </event>";

    public TestTimeEstimateMessageElement(String s)
    {
        super(s);
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    /**
     * Test a time estimate in days from event 20901
     */
    public void testGetElementDays()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringDays);
        viewValue.setEventType(TIME_ESTIMATE);
        value.setCourtLogViewValue(viewValue);

        // this attribute is not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        assertEquals("10", stringRet);
    }

    /**
     * Test a time estimate in weeks from event 20901
     */
    public void testGetElementWeeks()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringWeeks);
        viewValue.setEventType(TIME_ESTIMATE);
        value.setCourtLogViewValue(viewValue);

        // this attribute is not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        assertEquals("70", stringRet);
    }

    /**
     * Test a time estimate in months from event 20901
     */
    public void testGetElementMonths()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringMonths);
        viewValue.setEventType(TIME_ESTIMATE);
        value.setCourtLogViewValue(viewValue);

        // this attributes not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        double days = 365.0/12.0 * 10;
        String sDays = days + "";
        sDays = sDays.substring(0, sDays.indexOf(".")+3);
        assertEquals(sDays, stringRet);
    }

    /**
     * Test a time estimate in days from event 40702
     */
    public void testGetElementDirDays()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringDirDays);
        viewValue.setEventType(DIRECTIONS_BY_CASE);
        value.setCourtLogViewValue(viewValue);

        // this attribute is not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        assertEquals("10", stringRet);
    }

    /**
     * Test a time estimate in weeks from event 40702
     */
    public void testGetElementDirWeeks()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringDirWeeks);
        viewValue.setEventType(DIRECTIONS_BY_CASE);
        value.setCourtLogViewValue(viewValue);

        // this attribute is not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        assertEquals("70", stringRet);
    }

    /**
     * Test a time estimate in months from event 40702
     */
    public void testGetElementDirMonths()
    {
        TimeEstimateMessageElement timeEstMessageElement =
                new TimeEstimateMessageElement();
        CourtLogSubscriptionValue value = new  CourtLogSubscriptionValue();
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry(xmlStringDirMonths);
        viewValue.setEventType(DIRECTIONS_BY_CASE);
        value.setCourtLogViewValue(viewValue);

        // this attributes not used in the message body
        XhbCase theCase = null;

        String stringRet = timeEstMessageElement.getElement(value, theCase);
        double days = 365.0/12.0 * 10;
        String sDays = days + "";
        sDays = sDays.substring(0, sDays.indexOf(".")+3);
        assertEquals(sDays, stringRet);
    }
}