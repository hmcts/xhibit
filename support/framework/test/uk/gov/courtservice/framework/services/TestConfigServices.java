//package uk.gov.courtservice.framework.services;
//
//import java.util.MissingResourceException;
//import java.util.Properties;
//import java.util.ResourceBundle;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//
///**
// * <p>
// * Title:
// * </p>
// * <p>
// * Description:
// * </p>
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Pete Raymond
// * @author Paul Grove - added lots of new tests
// * @version 1.1
// */
//public class TestConfigServices extends TestCase {
//
//    private static Logger log = Logger.getLogger(TestConfigServices.class.getName());
//
//    public TestConfigServices(String s) {
//        super(s);
//    }
//
//    public static Test suite() {
//        return new TestSuite(TestConfigServices.class);
//    }
//
//    // run before each test
//    protected void setUp() {
//    }
//
//    // run after each test
//    protected void tearDown() {
//    }
//
//    public void testGetApplicationProperty() throws Exception {
//        log.debug("## [testGetApplicationProperty]");
//        String xhibitPropertiesKey = "xhibitprops.test.key";
//        String xhibitPropertiesValue = "xhibitPropsTestValue";
//        String returnValue = CSServices.getConfigServices().getProperty(xhibitPropertiesKey);
//        assertEquals(xhibitPropertiesValue, returnValue);
//    }
//
//    /*
//     * public void testGetEAProperty() throws Exception { String eaPropertiesKey =
//     * "ea.test.key"; String eaPropertiesValue = "ea.test.value"; String
//     * returnValue =
//     * CSServices.getConfigServices().getProperty(Applications.ENABLING_APPS,
//     * eaPropertiesKey); assertEquals(eaPropertiesValue, returnValue); }
//     *
//     * public void testGetCShubProperty() throws Exception { String
//     * csHubPropertiesKey = "JNDI.PROVIDER_URL"; String csHubPropertiesValue =
//     * "t3://localhost:7001"; String returnValue =
//     * CSServices.getConfigServices().getProperty(csHubPropertiesKey);
//     * assertEquals(csHubPropertiesValue, returnValue); }
//     */
//
//    public void testGetComponentProperties() throws Exception {
//        String componentName = "gui.testComponent";
//        String componentValue = "componentTestValue";
//        String componentTestKey = "test.key";
//        Properties testProps = CSServices.getConfigServices().getProperties(componentName);
//        String returnValue = testProps.getProperty(componentTestKey);
//        assertEquals("testGetComponentProperties not equal", componentValue, returnValue);
//    }
//
//    public void testGetResourceBundle() throws Exception {
//        ResourceBundle res = CSServices.getConfigServices().getBundle("errorText");
//        assertEquals(res.getObject("test").toString(), "test message");
//    }
//
//    public void testComponentPropertiesMissing() throws Exception {
//        log.debug("[testComponentPropertiesMissing]");
//        String componentName = "non.existent.component";
//        try {
//            CSServices.getConfigServices().getProperties(componentName);
//            fail("getProperties should have thrown a CSConfigurationException");
//        } catch (CSConfigurationException intentionallyIgnored) {
//        }
//    }
//
//    public void testResourceBundleMissing() {
//        log.debug("[testResourceBundleMissing]");
//        String propertyName = "non.existent.property";
//        try {
//            CSServices.getConfigServices().getBundle(propertyName);
//            fail("getBundle should have thrown a CSConfigurationException");
//        } catch (CSConfigurationException intentionallyIgnored) {
//        } catch (MissingResourceException alsoIgnored) {
//        }
//    }
//
//}