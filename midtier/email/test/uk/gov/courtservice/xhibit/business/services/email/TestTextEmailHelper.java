//package uk.gov.courtservice.xhibit.business.services.email;
//
//import javax.mail.internet.InternetAddress;
//
//import junit.framework.TestCase;
//
//import uk.gov.courtservice.xhibit.business.vos.services.email.TextEmailValue;
//
//
///**
// * Test class for checking functionality of the email helpers.
// */
//public class TestTextEmailHelper extends TestCase
//{
//
//    public TestTextEmailHelper(String testName)
//    {
//        super(testName);
//    }
//
//
//    /**
//     * Test the TextEmailHelper used by Public Display.
//     */
//    public void testTextEmailHelper() throws Exception
//    {
//        InternetAddress[] recipients = {new InternetAddress("robert.boothby-eds@eds.com")};
//        TextEmailValue value = new TextEmailValue(
//                    recipients,
//                    "Hello",
//                    new InternetAddress("robert.boothby-eds@eds.com"),
//                    "I said HELLO!",
//                    "Gibberish",
//                    false,
//                    new Integer(1));
//        TextEmailHelper.sendEmail(value);
//    }
//}
//