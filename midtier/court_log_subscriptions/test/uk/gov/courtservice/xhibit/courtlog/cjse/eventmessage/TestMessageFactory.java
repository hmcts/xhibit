//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import java.util.Locale;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventFixture;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//
///**
// * <p>Title: TestCase for testing MessageFactory.</p>
// * <p>Description:</p>
// * <p>
// * This unit test checks that MessageFactory successfully returns an internationalised
// * string for a given message code.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Eds</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestMessageFactory extends TransactionTestCase
//{
//
//    /**
//     * Construct and instance of TestMessageFactory that runs all methods
//     * within scope of a transaction that is rolled back.
//     * @param name The name of the tests.
//     * @throws Exception When there is a problem setting up the transaction.
//     */
//    public TestMessageFactory(String name) throws Exception
//    {
//        super(name, true);
//    }
//
//    /**
//     * This test checks that the MessageFactory successfully retrieves a localised
//     * message.
//     * @throws Exception When there are EJB issues.
//     */
//    public void testGetMessageForMessageCodeUK() throws Exception
//    {
//        CourtLogSubscriptionValue clsv =
//                CjseEventFixture.getCaseCLSubsValue1(new Integer(-1), "");
//        XhbCase theCase = XhbCaseBeanHelper.findByPrimaryKey(new Integer(1));
//        String message = MessageFactory.getMessageForMessageCode(
//                "testGetMessageForMessageCode", Locale.UK, clsv, theCase);
//        assertEquals(
//                "Expected value did not match actual:",
//                "T20028779 TEST T DEFENDANT T20028779-1MNOPQR",
//                message);
//    }
//}