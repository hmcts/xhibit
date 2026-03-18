//package uk.gov.courtservice.xhibit.courtlog.probation;
//
//import java.util.Calendar;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.helpers.SubscriptionValueAssembler;
//import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
//import uk.gov.courtservice.xhibit.courtlog.publicdisplay.PublicDisplayHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * @author pznwc5
// */
//public class TestPublicDisplayHelper extends TransactionTestCase
//{
//    private CourtLogSubscriptionValue subVal;
//
//    public TestPublicDisplayHelper(String name) throws Exception
//    {
//        super(name, true);
//    }
//
//    /*
//     * @see TestCase#setUp()
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        XhbCourtLogEntryBasicValue basicVal = new XhbCourtLogEntryBasicValue();
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, 2004);
//        cal.set(Calendar.MONTH, Calendar.APRIL);
//        cal.set(Calendar.DATE, 7);
//
//        basicVal.setDateTime(cal.getTime());
//        basicVal.setLogEntryXml("<event/>");
//        basicVal.setCaseId(new Integer(7));
//        basicVal.setEventDescId(new Integer(1));
//
//        XhbCourtLogEntryBasicValue newVal = XhbCourtLogEntryBeanHelper.create(basicVal);
//        CourtLogViewValue viewVal = ViewValueAssembler.createCourtLogViewValue(newVal);
//        subVal = SubscriptionValueAssembler.getSubsciptionValue(viewVal);
//    }
//
//    public void testSendMessage()
//    {
//        PublicDisplayHelper.sendMessage(subVal);
//    }
//}
//