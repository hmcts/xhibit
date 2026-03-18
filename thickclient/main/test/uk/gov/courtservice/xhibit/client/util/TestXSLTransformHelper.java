//package uk.gov.courtservice.xhibit.client.util;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import org.apache.log4j.Logger;
//
//import java.io.*;
//
///**
// * @author Jon Powell (Electronic Data Systems)
// * @date 29-Sep-2003
// * <p/>
// * Written to support changeover from XMLServices to XSLServices methods in
// * XSLTransformHelper
// * <p/>
// * As usual, writing a test before making the changes seemed like a good idea at
// * the time .. however, it is so much effort to get sample XML, all the
// * schema and assertTrue against transformed text, i was wasting hours trying to test
// * a change which took 5 minutes.
// */
//public class TestXSLTransformHelper extends TestCase {
//
//    // logging
//    private static final Logger log = Logger.getLogger(TestXSLTransformHelper.class.getName());
//
//    // class under test
//    private XSLTransformHelper helper = null;
//
//    private final static String EMPTY_STRING = "";
//    private final static String FORMS_DIR = "test" + File.separator + "forms";
//    private final static String CREST_FORM_B_XML = FORMS_DIR + File.separator + "NewCrestFormB.xml";
//    private final static String CREST_FORM_B_XSL = "crestFormBFO";
//
//
//    /**
//     * Create new test class
//     *
//     * @param testName will be passed in by JUnit runner
//     */
//    public TestXSLTransformHelper(String testName) {
//        super(testName);
//    }
//
//
//    /**
//     * Have JUnit pick up all tests in this class
//     */
//    public static Test suite() {
//        return new TestSuite(TestXSLTransformHelper.class);
//    }
//
//
//    /**
//     * Initialise variables common to each test. Re-run before each test.
//     */
//    protected void setUp() {
//        helper = new XSLTransformHelper();
//    }
//
//
//    /**
//     * Cleanup variables used by tests. Re-run after each test.
//     */
//    protected void tearDown() {
//        helper = null;
//    }
//
//
//
//    //
//    // method tests (EXPECTED, ACTUAL)
//    //
//
//    // test transform(xml, xslFile)
//    // THIS TEST IS BROKEN
//    public void testTransform() throws Exception {
//        log.debug("[testTransform] ");
//
//        fail("this test is incomplete");
//
//        // check that empty string is returned if either param is null
//        // (can't test null first value as matches > 1 method)
//        String result = helper.transform("", null);
//        assertEquals(EMPTY_STRING, result);
//
//        // check we have some xml to transform
//        File xmlFile = new File(CREST_FORM_B_XML);
//        assertTrue("could not find " + xmlFile.getPath(), xmlFile.exists());
//        String xml = fileToString(xmlFile);
//
//        // do transform and make simple checks
//        //result = helper.transform(xml, CREST_FORM_B_XSL);
//        //assertNotNull("error during transform", result);
//    }
//
//
//    private String fileToString(File file) {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String aLine = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((aLine = reader.readLine()) != null) {
//                buffer.append(aLine);
//            }
//            return buffer.toString();
//        } catch (FileNotFoundException e) {
//            log.error("cant find " + file.getPath());
//            e.printStackTrace();
//        } catch (IOException e) {
//            log.error("error reading " + file.getPath());
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//}
//
//