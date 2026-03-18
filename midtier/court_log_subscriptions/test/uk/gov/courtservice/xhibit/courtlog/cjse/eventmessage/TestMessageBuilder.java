//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventFixture;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//
///**
// * <p>Title: Test cases for MessageBuilder</p>
// * <p>Description: </p>
// * <p>
// * This class tests the message builder for both pure string messages and
// * for mixed element messages.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Eds</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestMessageBuilder extends TransactionTestCase
//{
//    /**
//     * Construct an instance of the MessageBuilder test case.
//     * @param name The name of the test
//     */
//    public TestMessageBuilder(String name) throws Exception
//    {
//        super(name, true);
//    }
//
//    /**
//     * This test checks that the message builder behaves as advertise for the
//     * building of a pure string.
//     */
//    public void testGetBuiltPureStringMessage()
//    {
//        //get some message elements (string only).
//        MessageElement[] elements = MessageElementFactory.getElementsForMessageText(
//                "ABCDEF $$$ GHIJKL$$$MNOPQR");
//        assertEquals(
//                "Number of message elements returned did not match the expected number.",
//                elements.length,
//                3);
//
//        //Create the message builder for testing.
//        MessageBuilder messagebuilder = new MessageBuilder(elements);
//
//        //Don't need any of these in the basic String case.
//        CourtLogSubscriptionValue courtLogSubscription =  null;
//        XhbCase theCase=  null;
//
//        //Get the message and check it is as expected.
//        String message = messagebuilder.getBuiltMessage(
//                courtLogSubscription, theCase);
//        assertEquals(
//                "Message returned not message expected.",
//                message,
//                "ABCDEF  GHIJKLMNOPQR");
//    }
//
//    /**
//     * This test checks that the MessageBuilder successfully brings together
//     * several different types of MessageElement.
//     * @throws Exception When there is a problem in building the message or in
//     * retrieving an instance of XhbCase.
//     */
//    public void testGetBuiltMessage() throws Exception
//    {
//        //get some message elements.
//        MessageElement[] elements = MessageElementFactory.getElementsForMessageText(
//                "CaseNumber$$$ $$$DefendantNames$$$MNOPQR");
//        assertEquals(
//                "Number of message elements returned did not match the expected number.",
//                elements.length, 4);
//
//        //Create the message builder for testing.
//        MessageBuilder messagebuilder = new MessageBuilder(elements);
//
//        //Fill in the gaps of the
//        CourtLogSubscriptionValue courtLogSubscription =
//                CjseEventFixture.getCaseCLSubsValue1(new Integer(10101), "");
//        /**
//         * @todo CourtLogSubscriptionValue will be changed to primary keys
//         * as a Longs, when this happens the conversion below will no longer be
//         * necessary
//         */
//        XhbCase theCase=  XhbCaseBeanHelper.findByPrimaryKey(
//                courtLogSubscription.getCourtLogViewValue().getCaseId());
//
//        //Get the message and check it is as expected.
//        String message = messagebuilder.getBuiltMessage(
//                courtLogSubscription, theCase);
//        assertEquals("Message returned not message expected.",
//                     message,
//                     "T20028779 TEST T DEFENDANT T20028779-1MNOPQR");
//    }
//}