//package uk.gov.courtservice.framework.services;
//
//import java.util.Properties;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//import uk.gov.courtservice.framework.services.discovery.DiscoveryTest;
//import uk.gov.courtservice.framework.services.discovery.DiscoveryTestNameIdentifier;
//import uk.gov.courtservice.framework.services.discovery.DiscoveryTestProperty;
//import uk.gov.courtservice.framework.services.discovery.InvalidDiscoveryTest;
//import uk.gov.courtservice.framework.services.discovery.NegativeDiscoveryTest;
//
///**
// * <p>
// * Title: Translation Bundle Test
// * </p>
// * <p>
// * Description: A translation
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company: Electronic Data Systems
// * </p>
// *
// * @author William Fardell, Xdevelopment (2004)
// * @version $Id: TestDiscoveryServices.java,v 1.6 2006/07/13 12:58:03 xzfdtb Exp $
// */
//public class TestDiscoveryServices extends TestCase {
//
//    //
//    // The Framework!
//    //
//
//    /**
//     * Execution entry point. Allows the test to be run in stand alone mode.
//     *
//     * @param args
//     *            String array of command line arguments
//     */
//    public static void main(String args[]) {
//        TestRunner.run(suite());
//    }
//
//    /**
//     * Create a Test useing reflection to determine tests
//     */
//    public static Test suite() {
//        return new TestSuite(TestDiscoveryServices.class);
//    }
//
//    private Properties propertiesBackup;
//
//    /**
//     * TestCase implementation.
//     *
//     * @see TestCase#setUp() TestCase
//     */
//    public void setUp() {
//        propertiesBackup = (Properties) System.getProperties().clone();
//    }
//
//    /**
//     * TestCase implementation.
//     *
//     * @see TestCase#tearDown() TestCase
//     */
//    public void tearDown() {
//        System.setProperties(propertiesBackup);
//        propertiesBackup = null;
//    }
//
//    //
//    // The Tests!
//    //
//
//    public void testCreateInstanceNull() {
//        try {
//            DiscoveryServices.getInstance().createInstance(null);
//            fail("IllegalArgumentException not thrown.");
//        } catch (IllegalArgumentException iae) {
//            // Success
//        }
//    }
//
//    public void testCreateInstanceNegative() {
//        NegativeDiscoveryTest discoveryTest = (NegativeDiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                NegativeDiscoveryTest.class);
//        assertNull(discoveryTest);
//    }
///*
//    public void testCreateInstanceNameIdentifier() {
//        DiscoveryTest discoveryTest = (DiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                DiscoveryTest.class);
//        assertEquals(DiscoveryTestNameIdentifier.class, discoveryTest.getClass());
//    }
//
//*/
//    public void testCreateInstanceProperty() {
//        System.setProperty("uk.gov.courtservice.framework.services.discovery.DiscoveryTest",
//                "uk.gov.courtservice.framework.services.discovery.DiscoveryTestProperty");
//        DiscoveryTest discoveryTest = (DiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                DiscoveryTest.class);
//        assertEquals(DiscoveryTestProperty.class, discoveryTest.getClass());
//    }
//
//    public void testInvalidCreateInstanceNameIdentifier() {
//        InvalidDiscoveryTest discoveryTest = (InvalidDiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                InvalidDiscoveryTest.class);
//        assertNull(discoveryTest);
//    }
//
//    public void testInvalidCreateInstanceProperty() {
//        System.setProperty("uk.gov.courtservice.framework.services.discovery.InvalidDiscoveryTest",
//                "uk.gov.courtservice.framework.services.discovery.InvalidDiscoveryTestProperty");
//        InvalidDiscoveryTest discoveryTest = (InvalidDiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                InvalidDiscoveryTest.class);
//        assertNull(discoveryTest);
//    }
//
//    public void testCreateInstanceNotInstanceProperty() {
//        System.setProperty("uk.gov.courtservice.framework.services.discovery.InvalidDiscoveryTest",
//                "uk.gov.courtservice.framework.services.discovery.DiscoveryTestProperty");
//        InvalidDiscoveryTest discoveryTest = (InvalidDiscoveryTest) DiscoveryServices.getInstance().createInstance(
//                InvalidDiscoveryTest.class);
//        assertNull(discoveryTest);
//    }
//}
//