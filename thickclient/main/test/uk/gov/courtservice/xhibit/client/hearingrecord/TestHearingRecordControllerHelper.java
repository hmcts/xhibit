//package uk.gov.courtservice.xhibit.client.hearingrecord;
//
//import java.util.ArrayList;
//
//import org.apache.log4j.Logger;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.LinkedHearingRecordValues;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * @date    02-Oct-2003
// *
// * Written to support changeover from remote print services to local print
// * formatting
// */
//public class TestHearingRecordControllerHelper extends TestCase
//{
//
//	// logging
//	private static final Logger log = Logger.getLogger(TestHearingRecordControllerHelper.class.getName());
//
//	// class under test
//	private HearingRecordControllerHelper helper = null;
//
//	// supporting
//    private LinkedHearingRecordValues values = null;
//
//
//	/**
//	 * Create new test class
//	 * @param testName  will be passed in by JUnit runner
//	 */
//	public TestHearingRecordControllerHelper(String testName)
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
//		return new TestSuite(TestHearingRecordControllerHelper.class);
//	}
//
//
//	/**
//	 * Initialise variables common to each test. Re-run before each test.
//	 */
//	protected void setUp()
//	{
//		helper = new HearingRecordControllerHelper();
//        values = new LinkedHearingRecordValues();
//        values.setHearingRecordValues(new ArrayList());
//	}
//
//
//	/**
//	 * Cleanup variables used by tests. Re-run after each test.
//	 */
//	protected void tearDown()
//	{
//		helper = null;
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
//    * <?xml version="1.0" encoding="UTF-8"?>
//    * <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
//    *   <fo:layout-master-set>
//    *     <fo:simple-page-master margin-right="10mm" margin-left="10mm" margin-bottom="10mm" margin-top="10mm" page-width="210mm" page-height="297mm" master-name="all">
//    *       <fo:region-body margin-bottom="10mm" margin-top="0mm"/>
//    *       <fo:region-before extent="10mm"/>
//    *       <fo:region-after extent="10mm"/>
//    *     </fo:simple-page-master>
//    *   </fo:layout-master-set>
//    *   <fo:page-sequence format="1" master-reference="all">
//    *     <fo:flow flow-name="xsl-region-body"/>
//    *   </fo:page-sequence>
//    * </fo:root>
//    *
//    * This formats a document which has no hearing records, but it should at
//    * least test the mechanism
//    */
//	public void testGetFormattedDocument()
//	{
//        log.debug("[testGetFormattedDocument]");
//        try
//        {
//            String[] formatted = helper.getFormattedDocument(values);
//            assertTrue(formatted[0].indexOf("simple-page-master")>0);
//            //log.debug("formattedDocument=" + formatted);
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