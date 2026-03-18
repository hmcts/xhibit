package uk.gov.courtservice.xhibit.business.services.exiss.inbound;

import org.apache.log4j.BasicConfigurator;

import uk.gov.courtservice.xhibit.business.services.exiss.outbound.ExiRefTypes;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestInboundMessageBean extends TestCase {
    
    static {
        BasicConfigurator.configure();
    }
    
    InboundMessageBean imb;
    
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
        return new TestSuite(TestInboundMessageBean.class);
    }
    
    public void setUp() {
        imb = new InboundMessageBean();
    }
    
    public void tearDown() {
        imb = null;
    }
    
    public void testIsDeliverError() {
        assertTrue(imb.isDeliverError(InboundMessagePayloadType.DELIVERERROR.toString()));
    }
    
    public void testIsException() {
        assertTrue(imb.isException(InboundMessagePayloadType.EXCEPTION.toString()));
    }
    
    public void testIsNotException() {
        assertFalse(imb.isException(InboundMessagePayloadType.MESSAGE.toString()));
    }
    
    public void testIsNotDeliverError() {
        assertFalse(imb.isDeliverError(InboundMessagePayloadType.MESSAGE.toString()));
    }
}
