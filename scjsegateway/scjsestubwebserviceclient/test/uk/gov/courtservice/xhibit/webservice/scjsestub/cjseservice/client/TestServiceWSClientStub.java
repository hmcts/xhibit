package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient;
import uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.client.ServiceWSClientStub;
import uk.gov.cjse.schemas.endpoint.types.ExecMode;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.text.ParseException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: JUnit Test for ServiceWSClientStub
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: TestServiceWSClientStub.java,v 1.1 2006/10/03 16:15:56 qz4rwx Exp $ Exp $
 */

public class TestServiceWSClientStub extends TestCase
{
    private static final Logger log = CSServices.getLogger(TestServiceWSClientStub.class);

    ServiceWSClientStubNoFile serviceWSClientStub = null;
    
    String requestDateString = null;
    
    HashMap<String,String> submitRequestHashmap = null;  
    
    boolean isTimeoutTest = false;
    boolean isDifferentRequestId = false;
    boolean isInvalidRequestId = false;

    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }

    public TestServiceWSClientStub(String name)
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
        return new TestSuite(TestServiceWSClientStub.class);
    }

    protected void setUp() {
        try
        {
            serviceWSClientStub = new ServiceWSClientStubNoFile();
            submitRequestHashmap = new HashMap<String,String>();            
        }
        catch(Exception e)
        {
            log.debug("Failed to return ServiceWSClientStub from ServiceWSClientFactory which is the default");
            e.printStackTrace();
        }
    }

    protected void tearDown() {
        serviceWSClientStub  = null;
        submitRequestHashmap = null;
        requestDateString    = null;
        isTimeoutTest        = false;
        isDifferentRequestId = false;
        isInvalidRequestId   = false;
    }

    public final void testSetSubmitRequest() {        
        try
        {
            String message = "<TEST_MESSAGE>"; 
            Map properties = setProperties();
            
            SubmitRequest submitRequest = serviceWSClientStub.setSubmitRequest(message, properties);
            
            assertEquals("<TEST_MESSAGE>", submitRequest.getMessage());
            assertEquals("100", submitRequest.getRequestID());
            assertEquals(null, submitRequest.getSourceID());
            
            log.debug("submitRequest DestinationID:" + submitRequest.getDestinationID());            
            assertEquals(null, submitRequest.getDestinationID());
            
            log.debug("submitRequest ExecMode:" + submitRequest.getExecMode());            
            assertEquals(null, submitRequest.getExecMode());
            
            assertEquals(null, submitRequest.getTimestamp());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
 
    public final void testSetSubmitRequestAllProperties() {        
        try
        {
            String message = "<TEST_MESSAGE_ALL_PROPERTIES>"; 
            Map properties = setAllProperties();
            
            SubmitRequest submitRequest = serviceWSClientStub.setSubmitRequest(message, properties);
            
            assertEquals("<TEST_MESSAGE_ALL_PROPERTIES>", submitRequest.getMessage());
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

    public final void testIsSCJSEGatewayWSClientStub() {        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(true, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");            
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");            
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");            
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);            
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {null});
            submitRequest.setTimestamp(DateTimeUtilities.processOracleDateParameter(DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime())));
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setDestinationID(new String[] {"Z00CJSE"});
            
            boolean isSCJSEGatewayWSClientStub   = serviceWSClientStub.isSCJSEGatewayWSClientStub(submitRequest);
            
            log.debug("isSCJSEGatewayWSClientStub:" + isSCJSEGatewayWSClientStub);            
            
            assertEquals(false, isSCJSEGatewayWSClientStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    } 

    public final void testSetSubmitRequestDefaults() {        
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            
            serviceWSClientStub.setSubmitRequestDefaults(submitRequest);
            
            //Note - in the stub the defaults are set when the stub is acting as a
            //stub for thr remote SCJSE WS Client and therefore the source and destination
            //are opposite to that on the SCJSE Gateway
            
            assertEquals("Z00CJSE", submitRequest.getSourceID());
            assertEquals("C00CourtServicesHub", submitRequest.getDestinationID()[0]);
            assertEquals("ASYNCH", submitRequest.getExecMode().getValue());
            assertNotNull(submitRequest.getTimestamp());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }

    public final void testSetSubmitRequestFromFile() {   
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            
            serviceWSClientStub.setSubmitRequestFromFile(submitRequest);
            
            //Note - in the stub the defaults are set when the stub is acting as a
            //stub for thr remote SCJSE WS Client and therefore the source and destination
            //are opposite to that on the SCJSE Gateway
                        
            assertEquals("Z00CJSE", submitRequest.getSourceID());
            assertEquals("C00CourtServicesHub", submitRequest.getDestinationID()[0]);
            assertEquals("ASYNCH", submitRequest.getExecMode().getValue());
            assertNotNull(submitRequest.getTimestamp());
            
            assertEquals(false, serviceWSClientStub.isTimeoutTestEnabledInStub);
            assertEquals(0, serviceWSClientStub.isTimeoutTestDelayInStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            isDifferentRequestId = true;
            
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            
            serviceWSClientStub.setSubmitRequestFromFile(submitRequest);
            
            //Note - in the stub the defaults are set when the stub is acting as a
            //stub for thr remote SCJSE WS Client and therefore the source and destination
            //are opposite to that on the SCJSE Gateway
                      
            //Different Request ID set
            assertEquals("200", submitRequest.getRequestID());
            assertEquals("Z00CJSE", submitRequest.getSourceID());
            assertEquals("C00CourtServicesHub", submitRequest.getDestinationID()[0]);
            assertEquals("ASYNCH", submitRequest.getExecMode().getValue());
            assertNotNull(submitRequest.getTimestamp());
            
            assertEquals(false, serviceWSClientStub.isTimeoutTestEnabledInStub);
            assertEquals(0, serviceWSClientStub.isTimeoutTestDelayInStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        finally
        {
            isDifferentRequestId = false;
        }
        
        try
        {
            isTimeoutTest = true;
            isInvalidRequestId = true;
            
            SubmitRequest submitRequest = new SubmitRequest();
            
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setRequestID("100");
            
            serviceWSClientStub.setSubmitRequestFromFile(submitRequest);
            
            //Note - in the stub the defaults are set when the stub is acting as a
            //stub for thr remote SCJSE WS Client and therefore the source and destination
            //are opposite to that on the SCJSE Gateway
            
            //Invalid Request ID set
            assertEquals("XYZ", submitRequest.getRequestID());
            assertEquals("Z00CJSE", submitRequest.getSourceID());
            assertEquals("C00CourtServicesHub", submitRequest.getDestinationID()[0]);
            assertEquals("ASYNCH", submitRequest.getExecMode().getValue());
            assertNotNull(submitRequest.getTimestamp());
            
            assertEquals(true, serviceWSClientStub.isTimeoutTestEnabledInStub);
            assertEquals(500, serviceWSClientStub.isTimeoutTestDelayInStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        finally
        {
            isTimeoutTest = false;
            isInvalidRequestId = false;
        }
    }
    
    protected Map<String, String> setProperties()
    {
        Map<String, String> properties = new HashMap<String, String>();
       
        properties.put(OutboundConstants.REQUEST_ID,"100");
        
        return properties;
    }

    protected Map<String, String> setAllProperties() throws ParseException
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
    
    protected class ServiceWSClientStubNoFile extends ServiceWSClientStub
    {
        boolean isTimeoutTestEnabledInStub = false;
        long    isTimeoutTestDelayInStub = 0;
        
        protected SubmitRequest setSubmitRequest(String message, Map properties)
        {  
            return super.setSubmitRequest(message,properties);
        }
        
        protected String getServiceUrl()
        {
            return "http://130.177.3.58:8001/stubdelivery/stubservices/StubDelivery";
        }

        protected boolean isSCJSEGatewayWSClientStub(SubmitRequest submitRequest)
        {
            return super.isSCJSEGatewayWSClientStub(submitRequest);
        }
        
        protected void setSubmitRequestDefaults(SubmitRequest submitRequest) throws Exception
        {
            super.setSubmitRequestDefaults(submitRequest);
        }        

        protected void setSubmitRequestFromFile(SubmitRequest submitRequest) throws Exception
        {
            super.setSubmitRequestFromFile(submitRequest);
        }

        protected HashMap<String,String> getSubmitRequestProperties() throws Exception
        {
            try
            {
                submitRequestHashmap = new HashMap<String,String>();
                isTimeoutTestEnabledInStub = false;
                isTimeoutTestDelayInStub   = 0;
                
                requestDateString = DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime());
	            log.debug("setProperties: requestDateString:" + requestDateString);

                submitRequestHashmap.put("sourceId","Z00CJSE");
                submitRequestHashmap.put("destinationId","C00CourtServicesHub");
                submitRequestHashmap.put("execMode","ASYNCH");
                submitRequestHashmap.put("requestTimestamp",requestDateString);

                if(isDifferentRequestId)
                {
                    submitRequestHashmap.put("differentRequestId","200");
                }
                
                if(isInvalidRequestId)
                {
                    submitRequestHashmap.put("invalidRequestId","XYZ");
                }
                
                if(isTimeoutTest)
                {
                    submitRequestHashmap.put("timeoutTestEnabled","true");
                    submitRequestHashmap.put("delay","500");
                }
                                
                return submitRequestHashmap;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        } 
        
        protected void timeoutDelay(long delay)
        {
            isTimeoutTestEnabledInStub = true;
            isTimeoutTestDelayInStub   = delay;
            
            super.timeoutDelay(delay);
        }
    
        protected void handleError(Exception e)
        {
            //Does nothing in JUnit test
        }
    }     
}