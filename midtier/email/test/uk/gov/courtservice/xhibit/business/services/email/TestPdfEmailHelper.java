//package uk.gov.courtservice.xhibit.business.services.email;
//
//import javax.mail.internet.InternetAddress;
//
//import uk.gov.courtservice.xhibit.business.vos.services.email.PdfEmailValue;
//import junit.framework.TestCase;
//
///**
// * Test class for checking functionality of the email helpers.
// *
// * @author tz0d5m
// * @version $Id: TestPdfEmailHelper.java,v 1.2 2006/07/13 12:58:01 xzfdtb Exp $
// */
//public class TestPdfEmailHelper extends TestCase
//{
//    public TestPdfEmailHelper(String testName)
//    {
//        super(testName);
//    }
//
//    public void testSendEmail() throws Exception
//    {
//        PdfEmailValue value = new PdfEmailValue(
//                    "andy.turner-eds@eds.com",
//                    "andy.turner-eds@eds.com",
//                    "Subject",
//                    new InternetAddress("andy.turner-eds@eds.com"),
//                    "Message body",
//                    new byte[] { 1,1,1,1,1,1,1,1,1,1 },
//                    false,
//                    new Integer(3));
//        PdfEmailHelper.sendEmail(value);
//    }
//}
//