//package uk.gov.courtservice.xhibit.client.maintaincharges;
//
//import org.apache.log4j.Logger;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import uk.gov.courtservice.xhibit.business.vos.services.charge.PrintChargesValue;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * @date    02-Oct-2003
// *
// * Written to support changeover from remote print services to local print
// * formatting
// */
//public class TestChargesControllerHelper extends TestCase
//{
//
//	// logging
//	private static final Logger log = Logger.getLogger(TestChargesControllerHelper.class.getName());
//
//	// Charges Data
//    private PrintChargesValue pcValue = null;
//	private final static String HDR_LINE_1 = "HeaderLineOne";
//    private final static String HDR_LINE_2 = "HeaderLineTwo";
//
//
//	/**
//	 * Create new test class
//	 * @param testName  will be passed in by JUnit runner
//	 */
//	public TestChargesControllerHelper(String testName)
//	{
//		super(testName);
//	}
//
//
//	/**
//	 * Have JUnit pick up all tests in this class
//	 */
//	public static Test suite()
//	{
//		return new TestSuite(TestChargesControllerHelper.class);
//	}
//
//
//	/**
//	 * Initialise variables common to each test. Re-run before each test.
//	 */
//	protected void setUp()
//	{
//		//helper = ChargesControllerHelper.getInstance();
//        pcValue = new PrintChargesValue();
//        pcValue.setHeaderLine1(HDR_LINE_1);
//        pcValue.setHeaderLine2(HDR_LINE_2);
//	}
//
//
//	/**
//	 * Cleanup variables used by tests. Re-run after each test.
//	 */
//	protected void tearDown()
//	{
//		//helper = null;
//	}
//
//
//
//	//
//	// method tests (EXPECTED, ACTUAL)
//	//
//
//   /*
// 	* Expect formatted document to start something like this:
//    *
//    *
//    * This formats a document which has no hearing records, but it should at
//    * least test the mechanism
//    */
//	public void testGetFormattedDocument()
//	{
//        log.debug("[testGetFormattedDocument]");
//        try
//        {
//            String formatted = ChargesControllerHelper.getFormattedDocument(pcValue);
//            assertTrue(formatted.indexOf("simple-page-master")>0);
//            assertTrue("header line did not appear in the formatted document", formatted.indexOf(HDR_LINE_1)>0);
//        }
//		catch (Exception e)
//        {
//            fail("exception occurred getting formatted document : " + e);
//        }
//	}
//
//
//
//}
//
//