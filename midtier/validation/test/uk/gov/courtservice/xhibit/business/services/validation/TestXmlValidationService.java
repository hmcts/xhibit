package uk.gov.courtservice.xhibit.business.services.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Validation Service Test
 * 
 * @author William Fardell
 * @version $Id: TestXmlValidationService.java,v 1.2 2010/04/19 08:09:00 dunnepi Exp $
 */
public class TestXmlValidationService extends TestCase {

    //
    // The Framework!
    //

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
        return new TestSuite(TestXmlValidationService.class);
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
    }

    //
    // The Tests!
    //	

    public void test() {
        
    }

}
