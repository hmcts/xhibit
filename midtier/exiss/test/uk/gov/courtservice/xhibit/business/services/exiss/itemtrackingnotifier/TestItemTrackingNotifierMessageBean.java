package uk.gov.courtservice.xhibit.business.services.exiss.itemtrackingnotifier;

import org.apache.log4j.BasicConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestItemTrackingNotifierMessageBean extends TestCase
{
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
        return new TestSuite(TestItemTrackingNotifierMessageBean.class);
    }
    
    public void setup() {
        // No set up necessary
    }

    public void testNull() throws Exception {
        ItemTrackingNotifierMessageBean itnmb = new ItemTrackingNotifierMessageBean();
        assertEquals(0, itnmb.countQueues(null));
    }

    public void testZero() throws Exception {
        ItemTrackingNotifierMessageBean itnmb = new ItemTrackingNotifierMessageBean();
        assertEquals(0, itnmb.countQueues(""));
    }

    public void testOne() throws Exception {
        ItemTrackingNotifierMessageBean itnmb = new ItemTrackingNotifierMessageBean();
        assertEquals(1, itnmb.countQueues("oneQueue"));
    }

    public void testMany() throws Exception {
        ItemTrackingNotifierMessageBean itnmb = new ItemTrackingNotifierMessageBean();
        assertEquals(3, itnmb.countQueues("queueOne ,queueTwo, queueThree"));
    }
    
    public void tearDown() {
        // No tear down necessary
    }
}
