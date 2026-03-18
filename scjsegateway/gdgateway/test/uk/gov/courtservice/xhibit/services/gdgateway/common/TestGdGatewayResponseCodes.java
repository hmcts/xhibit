package uk.gov.courtservice.xhibit.services.gdgateway.common;

import org.apache.log4j.BasicConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <p>
 * Title: TestGdGatewayResponseCodes
 * </p>
 * <p>
 * Description: Application for testing GdGatewayResponseCodes class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rob Sumner
 * @version $Id: TestGdGatewayResponseCodes.java,v 1.3 2006/09/07 13:52:01 qz4rwx Exp $
 */

public class TestGdGatewayResponseCodes extends TestCase {

    // Setup Log4j configuration
    static {
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
        return new TestSuite(TestGdGatewayResponseCodes.class);
    }

    /**
     * Setup parameters before each individual test is run
     */
    public void setUp() {
        // No set up necessary
    }

    /**
     * Tests to run
     */

    // Tests enum can be defined when supplying code
    public void testCode() throws Exception {
        assertEquals(GdGatewayResponseCodes.valueOf(1), GdGatewayResponseCodes.Success);
    }

    // Tests enum can be defined successful as normal
    public void testEnum() throws Exception {
        assertEquals(GdGatewayResponseCodes.FatalError, GdGatewayResponseCodes.valueOf(300));
    }

    // Tests exception is successfully thrown when enum is defined via an
    // invalid code
    public void testInvalidEnum() throws Exception {
        try {
            GdGatewayResponseCodes.valueOf(500);
            fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }

    /**
     * Setup parameters after each test is done
     */
    public void tearDown() {
        // No tear down necessary
    }
}
