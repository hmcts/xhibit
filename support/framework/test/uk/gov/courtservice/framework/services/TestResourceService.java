//package uk.gov.courtservice.framework.services;
//
//import java.io.InputStream;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//
//public class TestResourceService extends TestCase {
//    // constants
//    private final static String VALID_RESOURCE = "config/xml/courtlogprint.xsl";
//
//    private final static String INVALID_RESOURCE = "foobar.txt";
//
//    public TestResourceService(String s) {
//        super(s);
//    }
//
//    public static Test suite() {
//        return new TestSuite(TestResourceService.class);
//    }
//
//    public void testGetResourceAsStream() {
//        // resource which should exist
//        try {
//            InputStream is = ResourceServices.getInstance().getResourceAsStream(VALID_RESOURCE);
//            assertNotNull("getResourceAsStream returned null InputStream", is);
//        } catch (Exception e) {
//            fail("testGetResourceAsStream " + e.toString());
//        }
//
//        // resource which should not exist
//        try {
//            ResourceServices.getInstance().getResourceAsStream(INVALID_RESOURCE);
//            fail("getResourceAsStream should have thrown a CSUnrecoverableException");
//        } catch (CSUnrecoverableException intentionallyIgnored) {
//        } catch (Exception e) {
//            fail("getResourceAsStream should have thrown a CSUnrecoverableException " + e.toString());
//        }
//    }
//
//}
//