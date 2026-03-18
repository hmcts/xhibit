package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientImpl;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.text.ParseException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: JUnit Test for ServiceWSClientImpl
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: TestServiceWSClientImpl.java,v 1.1 2006/09/22 14:04:43 rzvddy Exp $ Exp $
 */

public class TestServiceWSClientImpl extends TestCase
{
    private static final Logger log = CSServices.getLogger(TestServiceWSClientImpl.class);

    ServiceWSClient serviceWSClient = null;
    ServiceWSClientImpl serviceWSClientImpl = null;
    String requestDateString = null;

    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }

    public TestServiceWSClientImpl(String name)
    {
       super(name);
    }

    /**
     * Execution entry point. Allows the test to be run in stand alone mode.
     *
     * @param args String array of command line arguments
     */
    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * Create a Test using reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(TestServiceWSClientImpl.class);
    }

    protected void setUp() {
        try
        {
            serviceWSClient = createServiceWSClient();
        }
        catch(Exception e)
        {
            log.debug("Failed to return ServiceWSClientImpl from ServiceWSClientFactory which is the default");
            e.printStackTrace();
        }
    }

    protected void tearDown() {
        serviceWSClientImpl=null;
    }

    public final void testServiceWSClientImplCreated() {
        try
        {
            String serviceWSClientString = serviceWSClient.getClass().getName();
            
            log.debug("serviceWSClientString: " + serviceWSClientString);
            
            assertEquals("uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientImpl", serviceWSClientString);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    public final void testSetSubmitRequest() {
        
        serviceWSClientImpl = (ServiceWSClientImpl)serviceWSClient;
        
        try
        {
            String message = "<TEST_MESSAGE>"; 
            Map properties = setProperties();
            
            SubmitRequest submitRequest = serviceWSClientImpl.setSubmitRequest(message, properties);
            
            assertEquals("<TEST_MESSAGE>", submitRequest.getMessage());
            assertEquals("100", submitRequest.getRequestID());
            assertEquals("C00CourtServicesHub", submitRequest.getSourceID());
            
            log.debug("submitRequest DestinationID:" + submitRequest.getDestinationID()[0]);            
            assertEquals("Z00CJSE", submitRequest.getDestinationID()[0]);
            
            log.debug("submitRequest ExecMode:" + submitRequest.getExecMode().toString());            
            assertEquals("ASYNCH", submitRequest.getExecMode().toString());
            
            log.debug("requestDateString:" + requestDateString);
            log.debug("submitRequest DateString:" + DateTimeUtilities.convertOracleDate(submitRequest.getTimestamp().getTime()));
            
            assertEquals(requestDateString, DateTimeUtilities.convertOracleDate(submitRequest.getTimestamp().getTime()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    protected Map<String, String> setProperties() throws ParseException
    {
        Map<String, String> properties = new HashMap<String, String>();

        requestDateString = DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime());
        log.debug("setProperties: requestDateString:" + requestDateString);
        
        properties.put(OutboundConstants.REQUEST_ID,"100");
        properties.put(OutboundConstants.SOURCE_IDENTIFIER,"C00CourtServicesHub");
        properties.put(OutboundConstants.DESTINATION_IDENTIFIER,"Z00CJSE");
        properties.put(OutboundConstants.EXEC_MODE,"ASYNCH");
        properties.put(OutboundConstants.REQUEST_TIMESTAMP,requestDateString);
        
        return properties;
    }
    
    protected ServiceWSClient createServiceWSClient() {
        try {
            String className = "uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientImpl";
            
            log.debug("createServiceWSClient className:" + className);
            
            return (ServiceWSClient) Class.forName(className).newInstance();
            
        } catch (Throwable ex) {
            log.debug("createServiceWSClient failed returning ServiceWSClientImpl. Error was: " + ex);
            return new ServiceWSClientImpl();
        }
    }
}