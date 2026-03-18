//package uk.gov.courtservice.xhibit.client.counselfacilities;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;
//
///**
// * Written to support changeover from remote print services to local print
// * formatting
// *
// * @author Jon Powell (Electronic Data Systems)
// * @version $Revision: 1.4 $
// */
//public class TestCounselFacilitiesHelper extends TestCase
//{
//	// logging
//    private static final Logger log = Logger.getLogger(TestCounselFacilitiesHelper.class);
//
//    /**
//     * Create new test class
//     *
//     * @param testName
//     *            will be passed in by JUnit runner
//     */
//    public TestCounselFacilitiesHelper(String testName)
//    {
//        super(testName);
//    }
//
//    public void testTidyUp()
//    {
//        log.debug("[testTidyUp]");
//        String result = CounselFacilitiesHelper.tidyUp(null);
//        assertNotNull("should have returned an empty string", result);
//        result = CounselFacilitiesHelper.tidyUp("FOO");
//        assertEquals("should not have modified the string", "FOO", result);
//    }
//
//    /*
//  	 * Expect formatted document to start something like this:
//     *
//     * <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
//	 *   <fo:layout-master-set>
//	 *     <fo:simple-page-master master-name="simple" page-height="21.0cm" page-width="29.7cm" margin-top="1cm" margin-bottom="1cm" margin-left="2cm" margin-right="2cm">
//	 *       <fo:region-body margin-bottom="1.5cm" margin-top="1cm"/>
//	 *       <fo:region-before extent="1cm"/>
//	 *       <fo:region-after extent="1.5cm"/>
//	 *     </fo:simple-page-master>
//	 *   </fo:layout-master-set>
//     * ...
//     */
//	public void testGetFormattedDocument() throws Exception
//    {
//        log.debug("[testGetFormattedDocument]");
//        String formatted = CounselFacilitiesHelper.getFormattedDocument(new CourtList());
//        log.debug("formattedDocument=" + formatted);
//    }
//}
//