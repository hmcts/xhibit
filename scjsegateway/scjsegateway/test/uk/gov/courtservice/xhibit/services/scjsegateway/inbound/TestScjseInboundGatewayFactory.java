package uk.gov.courtservice.xhibit.services.scjsegateway.inbound;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <p>
 * Title: ScjseInboundGatewayFactory Test
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TestScjseInboundGatewayFactory.java,v 1.1 2006/08/24 11:08:23 bzjrnl Exp $
 */
public class TestScjseInboundGatewayFactory extends TestCase {

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
        return new TestSuite(TestScjseInboundGatewayFactory.class);
    }

    private Properties propertiesBackup; 
    
    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        System.out.println("Back Up Properties");
        propertiesBackup = System.getProperties();
        Properties propertiesTemp = (Properties)propertiesBackup.clone();
        System.setProperties(propertiesTemp);
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        System.out.println("Restore Properties");
        System.setProperties(propertiesBackup);
        propertiesBackup = null;
    }

    //
    // The Tests!
    //
    
    public void testGetScjseInboundGatewaySystemProperty() {
        System.setProperty("uk.gov.courtservice.xhibit.services.scjsegateway.inbound.ScjseInboundGateway",
                           "uk.gov.courtservice.xhibit.services.scjsegateway.inbound.SystemPropertyScjseInboundGateway");
        assertEquals(SystemPropertyScjseInboundGateway.class, ScjseInboundGatewayFactory.getInstance()
                .getScjseInboundGateway().getClass());
    }
    
    public void testGetScjseInboundGatewayResource() {
        assertEquals(ResourceScjseInboundGateway.class, ScjseInboundGatewayFactory.getInstance()
                .getScjseInboundGateway().getClass());
    }


}
