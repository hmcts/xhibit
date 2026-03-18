package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server.CJSEPortImpl;
import uk.gov.cjse.schemas.endpoint.types.ExecMode;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;

import java.util.Calendar;

import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>
 * Title: JUnit Test for ServiceWSStub
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: TestServiceWSStub.java,v 1.2 2006/10/24 14:15:34 qz4rwx Exp $ Exp $
 */

public class TestServiceWSStub extends TestCase
{ 
    private static final SimpleDateFormat ORACLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    ServiceWSStubNoFile serviceWSStub = null;

    String requestDateString = null;    
 
    boolean isTimeoutTest = false;
    
    public TestServiceWSStub(String name)
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
        return new TestSuite(TestServiceWSStub.class);
    }

    protected void setUp() {
        try
        {
            serviceWSStub = new ServiceWSStubNoFile();  
            isTimeoutTest = false;
        }
        catch(Exception e)
        {
            System.out.println("Failed to return serviceWSStub from ServiceWSFactory which is the default");
            e.printStackTrace();
        }
    }

    protected void tearDown() {
        serviceWSStub  = null;
        requestDateString    = null; 
        isTimeoutTest = false;
    }

    public final void testIsSubmitRequestValid() {
        
        SubmitRequest  submitRequest  = getSubmitRequest();
        SubmitResponse submitResponse = new SubmitResponse();
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);
            
            assertEquals(true, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(1, submitResponse.getResponseCode());
            assertEquals("Outbound Message Successfully Received", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setRequestID(null);
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals(null, submitResponse.getRequestID());
            assertEquals(306, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidRequestID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setRequestID("");
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("", submitResponse.getRequestID());
            assertEquals(306, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidRequestID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setRequestID("100");
        submitRequest.setSourceID(null);
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(304, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidSourceID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setSourceID("");
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(304, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidSourceID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setSourceID("C00CourtServicesHub");
        submitRequest.setDestinationID(new String[]{});
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(305, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidDestinationID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setDestinationID(new String[]{null});
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(305, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidDestinationID", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setDestinationID(new String[]{"Z00CJSE"});
        submitRequest.setExecMode(null);
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(307, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidExecMode", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        submitRequest.setExecMode(ExecMode.asynch);
        submitRequest.setTimestamp(null);
        
        try
        {  
            boolean isValid = serviceWSStub.isSubmitRequestValid(submitRequest, submitResponse);            
            assertEquals(false, isValid);
            assertEquals("100", submitResponse.getRequestID());
            assertEquals(300, submitResponse.getResponseCode());
            assertEquals("Outbound Message Error: InvalidTimestamp as not of Oracle format", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    public final void testSetSubmitResponseFromFile() {
        try
        {
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setSuccessProperties();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("100", submitResponse.getRequestID());
            assertEquals(1, submitResponse.getResponseCode());
            assertEquals(null, submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setErrorProperties();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("100", submitResponse.getRequestID());
            assertEquals(200, submitResponse.getResponseCode());
            assertEquals("Outbound Message Response Text: ERROR", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setFatalProperties();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("100", submitResponse.getRequestID());
            assertEquals(300, submitResponse.getResponseCode());
            assertEquals("Outbound Message Response Text: FATAL", submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setDifferentRequestId();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("200", submitResponse.getRequestID());
            assertEquals(1, submitResponse.getResponseCode());
            assertEquals(null, submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setInvalidRequestId();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("XYZ", submitResponse.getRequestID());
            assertEquals(1, submitResponse.getResponseCode());
            assertEquals(null, submitResponse.getResponseText());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            isTimeoutTest = true;
            
            SubmitResponse submitResponse = new SubmitResponse();
            submitResponse.setRequestID("100");
            
            setDelayProperties();
            
            serviceWSStub.setSubmitResponseFromFile(submitResponse);

            assertEquals("100", submitResponse.getRequestID());
            assertEquals(1, submitResponse.getResponseCode());
            assertEquals(null, submitResponse.getResponseText());
            
            assertEquals(true, serviceWSStub.isTimeoutTestEnabledInStub);
            assertEquals(500, serviceWSStub.isTimeoutTestDelayInStub);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        finally
        {
            isTimeoutTest = false;
        } 
    }

    protected void setSuccessProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","1");
        
        serviceWSStub.setProperties(properties);
    }
    
    protected void setErrorProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","200");
        properties.put("submitResponseText","ERROR");
        
        serviceWSStub.setProperties(properties);
    }
    
    protected void setFatalProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","300");
        properties.put("submitResponseText","FATAL");
        
        serviceWSStub.setProperties(properties);
    }
 
    protected void setDifferentRequestId()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","1");
        properties.put("differentRequestId","200");
        
        serviceWSStub.setProperties(properties);
    }
 
    protected void setInvalidRequestId()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","1");
        properties.put("invalidRequestId","XYZ");
        
        serviceWSStub.setProperties(properties);
    }
 
    protected void setDelayProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
       
        properties.put("submitResponseCode","1");
        properties.put("timeoutTestEnabled","true");
        properties.put("delay","500");        
        
        serviceWSStub.setProperties(properties);
    }
    
    protected SubmitRequest getSubmitRequest()
    {
        try
        {
            SubmitRequest submitRequest = new SubmitRequest();
           
            submitRequest.setRequestID("100");
            submitRequest.setSourceID("C00CourtServicesHub");                
            submitRequest.setDestinationID(new String[]{"Z00CJSE"});
            submitRequest.setExecMode(ExecMode.asynch);
            submitRequest.setMessage("<TEST_MESSAGE>");
            submitRequest.setTimestamp(processDateParameter(serviceWSStub.convertDate(Calendar.getInstance().getTime(),ORACLE_DATE_FORMAT),ORACLE_DATE_FORMAT));
            
            return submitRequest;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    protected Calendar processDateParameter(String timeStamp, SimpleDateFormat format) throws ParseException {
       
        Calendar time = Calendar.getInstance();

        time.setTime(format.parse(timeStamp));

        return time;
    }
    
    protected class ServiceWSStubNoFile extends CJSEPortImpl
    {        
        HashMap<String,String> properties = new HashMap<String,String>();

        boolean isTimeoutTestEnabledInStub = false;
        long    isTimeoutTestDelayInStub = 0;
        
        protected SubmitResponse setSubmitResponse(SubmitRequest submitRequest)
        {
            return super.setSubmitResponse(submitRequest);
        }

        protected String getServiceUrl()
        {
            return "http://130.177.3.58:8001/stubdelivery/stubservices/StubDelivery";
        }

        protected boolean isSubmitRequestValid(SubmitRequest submitRequest, SubmitResponse submitResponse)
        {
            return super.isSubmitRequestValid(submitRequest,submitResponse);
        }
       
        protected void setSubmitResponseFromFile(SubmitResponse submitResponse) throws Exception
        {
            isTimeoutTestEnabledInStub = false;
            isTimeoutTestDelayInStub   = 0;
            
            super.setSubmitResponseFromFile(submitResponse);
        }
        protected void timeoutDelay(long delay)
        {
            isTimeoutTestEnabledInStub = true;
            isTimeoutTestDelayInStub   = delay;
            
            super.timeoutDelay(delay);            
        }
        
        protected void setProperties(HashMap<String,String> newProperties)
        {
            properties = newProperties;
        }
        
        protected HashMap getSubmitResponseProperties() throws Exception
        {                   
            return properties;
        }        
    }
}