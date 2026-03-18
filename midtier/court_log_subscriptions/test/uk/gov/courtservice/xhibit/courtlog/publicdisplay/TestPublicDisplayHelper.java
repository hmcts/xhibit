//package uk.gov.courtservice.xhibit.courtlog.publicdisplay;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.helpers.SubscriptionValueAssembler;
//import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//
//
///**
// * @author pznwc5
// * @version $Revision: 1.4 $
// */
//public class TestPublicDisplayHelper extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the test method
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestPublicDisplayHelper(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    public void testSendMessage()
//    {
//        Long logEntryId = getCourtLogEntryId();
//        XhbCourtLogEntry logEntry =
//            XhbCourtLogEntryBeanHelper2.findByPrimaryKey(logEntryId);
//        CourtLogSubscriptionValue subValue =
//            SubscriptionValueAssembler.getSubsciptionValue(
//                    ViewValueAssembler.createCourtLogViewValue(
//                            logEntry.getData()));
//        PublicDisplayHelper.sendMessage(subValue);
//    }
//}
//