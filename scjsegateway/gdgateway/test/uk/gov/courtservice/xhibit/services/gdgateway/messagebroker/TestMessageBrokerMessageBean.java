package uk.gov.courtservice.xhibit.services.gdgateway.messagebroker;

import org.apache.log4j.BasicConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestMessageBrokerMessageBean extends TestCase {
    
    static {
        BasicConfigurator.configure();
    }
    
    MessageBrokerMessageBean mbmb;
    
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
        return new TestSuite(TestMessageBrokerMessageBean.class);
    }
    
    public void setUp() {
        mbmb = new MessageBrokerMessageBean();
    }
    
    public void tearDown() {
        mbmb = null;
    }
    
    public void testIsInbound() {
        assertTrue(mbmb.isInbound("INBOUND"));
    }
    
    public void testIsOutbound() {
        assertTrue(mbmb.isOutbound("OUTBOUND"));
    }
    
    public void testIsNotInbound() {
        assertFalse(mbmb.isInbound("INBOUNDXXX"));
    }
    
    public void testIsInboundWithNull() {
        assertFalse(mbmb.isOutbound(null));
    }
    
    public void testIsOutboundWithNull() {
        assertFalse(mbmb.isOutbound(null));
    }
}
