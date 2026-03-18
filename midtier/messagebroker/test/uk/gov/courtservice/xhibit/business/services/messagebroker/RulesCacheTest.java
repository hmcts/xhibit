package uk.gov.courtservice.xhibit.business.services.messagebroker;

import org.apache.log4j.BasicConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class RulesCacheTest extends TestCase {
    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();
    }

    /**
     * Execution entry point. Allows the test to be run in stand alone mode.
     * 
     * @param args
     *            String array of command line arguments
     */
    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * Create a Test useing reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(RulesCacheTest.class);
    }
    
    String cacheDuration01;
    String cacheDuration02;
    String cacheDuration03;
    String cacheDuration04;

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        cacheDuration01 = "12000000";
        cacheDuration02 = "illegalValue";
        cacheDuration03 =  null;
        cacheDuration04 = "";
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        cacheDuration01 = null;
        cacheDuration02 = null;
        cacheDuration03 = null;
        cacheDuration04 = null;
    }

    //
    // Tests
    //
    public void testValidValue() throws Exception {
        assertEquals(12000000, RulesCache._getCacheDuration(cacheDuration01));
    }

    public void testIllegalValue() throws Exception {
        assertEquals(900000, RulesCache._getCacheDuration(cacheDuration02));
    }

    public void testNullValue() throws Exception {
        assertEquals(900000, RulesCache._getCacheDuration(cacheDuration03));
    }

    public void testEmptyValue() throws Exception {
        assertEquals(900000, RulesCache._getCacheDuration(cacheDuration04));
    }
}
