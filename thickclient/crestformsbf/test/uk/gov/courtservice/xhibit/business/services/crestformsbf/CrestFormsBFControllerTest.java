//package uk.gov.courtservice.xhibit.business.services.crestformsbf;
//
//import junit.framework.TestCase;
//import junit.framework.Assert;
//import junit.framework.TestSuite;
//import junit.framework.Test;
//
//import junit.textui.TestRunner;
//
//import uk.gov.courtservice.framework.security.AccessInfo;
//
//import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFForm;
//import uk.gov.courtservice.xhibit.business.services.crestformsbf.CrestFormsBFControllerBeanBusinessDelegate;
//
///**
// * Test the CrestFormsBFController
// */
//public class CrestFormsBFControllerTest extends TestCase {
//    /**
//     * Create a suite of tests from this class
//     * @return a new suite of tests built by refelection from this class
//     */
//    public static Test suite()
//    {
//        return new TestSuite(CrestFormsBFControllerTest.class);
//    }
//
//    /**
//     * Use the text (command line) test runner to run the test suite
//     * @param args command line arguments
//     */
//    public static void main(String args[])
//    {
//        TestRunner.run(suite());
//    }
//
//    private CrestFormsBFControllerBeanBusinessDelegate delegate;
//
//    public void setUp()
//    {
//        delegate = CrestFormsBFControllerBeanBusinessDelegate.DelegateFactory.getInstance(new AccessInfo());
//    }
//
//    public void tearDown()
//    {
//        delegate = null;
//    }
//
//    public void testUpDown()
//    {
//    }
//}
//
//
//