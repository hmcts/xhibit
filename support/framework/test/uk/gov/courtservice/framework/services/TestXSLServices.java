//package uk.gov.courtservice.framework.services;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//
///**
// * <p>
// * Title: TestXSLservices
// * </p>
// * <p>
// * Description: A suite of tests for XSLServices.
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Will Fardell (Xdevelopment 2003)
// */
//public class TestXSLServices extends TestCase {
//    public TestXSLServices(String s) {
//        super(s);
//    }
//
//    /**
//     * Create a suite (using introspection) for all the tests in this class
//     *
//     * @return the suite of tests
//     */
//    public static Test suite() {
//        return new TestSuite(TestXSLServices.class);
//    }
//
//    /**
//     * Main method to allow test to be run as a stand alone application
//     *
//     * @params args command line arguments (currently not used)
//     */
//    public static void main(String args[]) {
//        TestRunner.run(suite());
//    }
//
//    /**
//     * Sets up the fixture This method is called before a test is executed.
//     */
//    protected void setUp() throws Exception {
//    }
//
//    /**
//     * Tears down the fixture This method is called after a test is executed.
//     */
//    protected void tearDown() throws Exception {
//    }
//
//    /**
//     * Test the transform functionality
//     *
//     * @throws Exception
//     *             if an error occures
//     */
//    public void testTransform() throws Exception {
//
//        assertEquals("Defence masked name 01 Case Opened", transformResource("20906-01-test.xml",
//                "config/courtlog/transformer/internet/20906.xsl"));
//
//        assertEquals("Defence masked name 02 Case Opened", transformResource("20906-02-test.xml",
//                "config/courtlog/transformer/internet/20906.xsl"));
//
//    }
//
//    /**
//     * Transform the xml in the named resource using the xsl in the named
//     * resource
//     *
//     * @param xmlName
//     *            the name of the xml resource
//     * @param xslName
//     *            the name of the xsl resource
//     * @return the transformed xml
//     * @throws Exception
//     *             if an error occures
//     */
//    private static String transformResource(String xmlName, String xslName) throws Exception {
//        return CSServices.getXSLServices().transform(getResourceAsString(xmlName), xslName, null, null);
//    }
//
//    /**
//     * Read the contents of the nammed resource into a String
//     *
//     * @param name
//     *            the name of the resource
//     * @return a String object containg the contents of the resource
//     */
//    private static String getResourceAsString(String name) throws IOException {
//        Reader in = getResourceAsReader(name);
//        try {
//            StringBuffer buffer = new StringBuffer();
//            for (int c = in.read(); c != -1; c = in.read()) {
//                buffer.append((char) c);
//            }
//            return buffer.toString();
//        } finally {
//            in.close();
//        }
//    }
//
//    /**
//     * Get a character stream reader for reading a resource located on the class
//     * path
//     *
//     * @param name
//     *            the name of the resource
//     * @return the reader
//     */
//    private static Reader getResourceAsReader(String name) {
//        return new BufferedReader(new InputStreamReader(getResourceAsStream(name)));
//    }
//
//    /**
//     * Get a stream for reading a resource located on the class path
//     *
//     * @param name
//     *            the name of the resource
//     * @return a stream for reading from the resource
//     * @throws IllegalArgumentException
//     *             if the named resource does not exist
//     */
//    private static InputStream getResourceAsStream(String name) throws IllegalArgumentException {
//        InputStream in = TestXSLServices.class.getClassLoader().getResourceAsStream(name);
//        if (in != null) {
//            return in;
//        } else {
//            throw new IllegalArgumentException("Could not locate reource " + name);
//        }
//    }
//}
//