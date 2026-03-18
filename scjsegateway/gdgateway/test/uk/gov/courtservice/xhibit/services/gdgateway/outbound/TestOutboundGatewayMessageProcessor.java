package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundUpdateStatusVO;
import uk.gov.courtservice.xhibit.services.gdgateway.common.GdGatewayResponseCodes;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertyCodes;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: JUnit Test for OutboundGatewayMessageProcessor
 * </p> 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: TestOutboundGatewayMessageProcessor.java,v 1.6 2006/12/07 17:28:05 qz4rwx Exp $ Exp $
 */

public class TestOutboundGatewayMessageProcessor extends TestCase
{
    private static final Logger log = CSServices.getLogger(TestOutboundGatewayMessageProcessor.class);
    
    OutboundGatewayMessageProcessorNoDb outboundGatewayMessageProcessorNoDb = null;
    
    OutboundMessageVO outboundMessageVO = null;
    OutboundUpdateStatusVO outboundUpdateStatusVO = null;
    SubmitResponse submitResponse = null;
    
    Vector<Object> configProperties = null;
    
    Calendar calendar = null;

    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }
    
    public TestOutboundGatewayMessageProcessor(String name)
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
     * Create a Test useing reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(TestOutboundGatewayMessageProcessor.class);
    }
    
    protected void setUp() {
        try {
            outboundGatewayMessageProcessorNoDb = new OutboundGatewayMessageProcessorNoDb();
            outboundMessageVO      = new OutboundMessageVO();
            outboundUpdateStatusVO = new OutboundUpdateStatusVO();
            submitResponse         = new SubmitResponse();           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void tearDown() {
        outboundGatewayMessageProcessorNoDb = null;
        outboundMessageVO = null;
        outboundUpdateStatusVO = null;
        submitResponse = null;
        configProperties = null;
    }
    
    public final void testGetWebServiceUrl() {
        try
        {
            outboundGatewayMessageProcessorNoDb.getWebServiceUrl();
            assertEquals("http://130.177.3.58:8001/stubdelivery/stubservices/StubDelivery",outboundGatewayMessageProcessorNoDb.getWebServiceUrl());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }

    public final void testSetProperties() {
        try
        {
            outboundMessageVO.setRequestId(new Long(100));
            outboundMessageVO.setDestinationIdentifier("Z00CJSE");
            outboundMessageVO.setSourceIdentifier("C00CourtServicesHub");
            outboundMessageVO.setExecMode("ASYNCH");
            outboundMessageVO.setOutboundStatusId(new Long(1));
            outboundMessageVO.setRequestTimestamp(DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T23:08:56"));
            outboundMessageVO.setClobData("<CLOB_DATA>");
            outboundMessageVO.setSendAttempts(new Long(1));
            
            HashMap properties = (HashMap)outboundGatewayMessageProcessorNoDb.setProperties(outboundMessageVO);

            String propertyName    = null;
            String propertyValue   = null;

            Iterator it = properties.keySet().iterator();

            while (it.hasNext())
            {
                propertyName = (String)it.next();
                log.debug("propertyName: " + propertyName);

                propertyValue = (String)properties.get(propertyName);
                log.debug("propertyValue: " + propertyValue);
                if(propertyName.trim().equals(OutboundConstants.REQUEST_ID))
                {
                    log.debug("***** Found the Request Id:" + propertyValue);
                    assertEquals("100",propertyValue);
                }
                else if(propertyName.trim().equals(OutboundConstants.SOURCE_IDENTIFIER))
                {
                    log.debug("***** Found the Source Id:" + propertyValue);
                    assertEquals("C00CourtServicesHub",propertyValue);
                }
                else if(propertyName.trim().equals(OutboundConstants.DESTINATION_IDENTIFIER))
                {
                    log.debug("***** Found the Destination Id:" + propertyValue);
                    assertEquals("Z00CJSE",propertyValue);
                } 
                else if(propertyName.trim().equals(OutboundConstants.EXEC_MODE))
                {
                    log.debug("***** Found the Exec Mode:" + propertyValue);
                    assertEquals("ASYNCH",propertyValue);
                }
                else if(propertyName.trim().equals(OutboundConstants.REQUEST_TIMESTAMP))
                {
                    log.debug("***** Found the Request Timestamp:" + propertyValue);
                    assertEquals(DateTimeUtilities.convertOracleDate(outboundMessageVO.getRequestTimestamp()),propertyValue);
                }                     
                else
                {
                    fail("Unknown propertyName: " + propertyName);
                }                
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }

    public final void testProcessSubmitResponse() {
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(1);
            submitResponse.setResponseText("");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(1,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("SUCCESS",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(102);
            submitResponse.setResponseText("");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(102,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("SUCCESS",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(103);
            submitResponse.setResponseText("");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(103,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("SUCCESS",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(200);
            submitResponse.setResponseText("Error");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(200,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("200",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("Error",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(201);
            submitResponse.setResponseText("TemporaryProblem");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(201,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("201",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("TemporaryProblem",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(202);
            submitResponse.setResponseText("ServerFailure");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(202,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("202",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("ServerFailure",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(300);
            submitResponse.setResponseText("FatalError");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(300,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("300",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("FatalError",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(304);
            submitResponse.setResponseText("WrongSource");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(304,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("304",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("WrongSource",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(305);
            submitResponse.setResponseText("WrongDestination");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(305,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("305",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("WrongDestination",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(306);
            submitResponse.setResponseText("InvalidRequestId");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(306,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("306",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("InvalidRequestId",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(307);
            submitResponse.setResponseText("ModeError");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(307,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("307",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("ModeError",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            submitResponse.setRequestID("200");
            submitResponse.setResponseCode(1);
            submitResponse.setResponseText("");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(300,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("300",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("submitResponseRequestId not the same as submitRequestId",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(null, new Long(100));
            log.debug("***** responseCode:" + responseCode);
            assertEquals(300,responseCode);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("300",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("submitResponse null",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(1);
            submitResponse.setResponseText("");
            
            int responseCode = outboundGatewayMessageProcessorNoDb.processSubmitResponse(submitResponse, null);
            log.debug("***** responseCode:" + responseCode);
            assertEquals(300,responseCode);
            
            assertEquals(null, null);
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("300",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("original requestId null",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    } 
    
    public final void testUpdateOutboundStatus() {
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Success,null,null);
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("SUCCESS",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Error,GdGatewayResponseCodes.Error,"Error");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("200",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("Error",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        } 

        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Error,GdGatewayResponseCodes.TemporaryProblem,"TemporaryProblem");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("201",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("TemporaryProblem",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Error,GdGatewayResponseCodes.ServerFailure,"ServerFailure");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("ERROR",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("202",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("ServerFailure",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Fatal,GdGatewayResponseCodes.FatalError,"FatalError");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("300",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("FatalError",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        } 

        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Fatal,GdGatewayResponseCodes.WrongSource,"WrongSource");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("304",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("WrongSource",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        } 
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Fatal,GdGatewayResponseCodes.WrongDestination,"WrongDestination");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("305",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("WrongDestination",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Fatal,GdGatewayResponseCodes.InvalidRequestId,"InvalidRequestId");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("306",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("InvalidRequestId",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(new Long(100),OutboundStatusCodes.Fatal,GdGatewayResponseCodes.ModeError,"ModeError");
 
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("FATAL",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals("307",outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals("ModeError",outboundGatewayMessageProcessorNoDb.failureText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }        
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundStatus(null,null,null,null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
    
    
    public final void testUpdateOutboundTrigger() {
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),1);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_OK,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),102);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_OK,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),103);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_OK,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),200);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_ERROR,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),201);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_ERROR,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),202);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_ERROR,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),300);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),304);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),305);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),306);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(new Long(100),307);
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            outboundGatewayMessageProcessorNoDb.updateOutboundTrigger(null,0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }    

    public final void testProcessMessage() {
        try
        {
            outboundGatewayMessageProcessorNoDb.processMessage(new Long(100));
            
            assertEquals(100,outboundGatewayMessageProcessorNoDb.requestId.longValue());
            assertEquals("SUCCESS",outboundGatewayMessageProcessorNoDb.outboundStatusCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureCode);
            assertEquals(null,outboundGatewayMessageProcessorNoDb.failureText);
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_OK,outboundGatewayMessageProcessorNoDb.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }

        try
        {
            OutboundRemoteException outboundRemoteException =
                new OutboundRemoteException();
            
            outboundRemoteException.processMessage(new Long(100));
            
            assertEquals(100,outboundRemoteException.requestId.longValue());
            assertEquals("ERROR",outboundRemoteException.outboundStatusCode);
            assertEquals("200",outboundRemoteException.failureCode);
            assertEquals("RemoteException",outboundRemoteException.failureText);
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_ERROR,outboundRemoteException.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        try
        {
            OutboundServiceException outboundServiceException =
                new OutboundServiceException();
            outboundServiceException.processMessage(new Long(100));
            
            assertEquals(100,outboundServiceException.requestId.longValue());
            assertEquals("FATAL",outboundServiceException.outboundStatusCode);
            assertEquals("300",outboundServiceException.failureCode);
            assertEquals("ServiceException",outboundServiceException.failureText);
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundServiceException.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        OutboundMessageDataAccessException outboundMessageDataAccessException =
            new OutboundMessageDataAccessException();
        
        try
        {
            outboundMessageDataAccessException.processMessage(new Long(100));
        }
        catch(CSUnrecoverableException ce)
        {
            log.debug("Failure Test:" + ce.getMessage());
            assertEquals("DataAccessException in OutboundGatewayMessageBean requestId: 100",ce.getMessage());   
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundMessageDataAccessException.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        OutboundUpdateStatusDataAccessException outboundUpdateStatusDataAccessException =
            new OutboundUpdateStatusDataAccessException();
        
        try
        {
            outboundUpdateStatusDataAccessException.processMessage(new Long(100));
        }
        catch(CSUnrecoverableException ce)
        {
            log.debug("Failure Test:" + ce.getMessage());
            assertEquals("DataAccessException in OutboundGatewayMessageBean requestId: 100",ce.getMessage());   
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundUpdateStatusDataAccessException.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
        
        OutboundConfigPropsDataAccessException outboundConfigPropsDataAccessException =
            new OutboundConfigPropsDataAccessException();
        
        try
        {
            outboundConfigPropsDataAccessException.processMessage(new Long(100));
        }
        catch(CSUnrecoverableException ce)
        {
            log.debug("Failure Test:" + ce.getMessage());
            assertEquals("DataAccessException in OutboundGatewayMessageBean requestId: 100",ce.getMessage());   
            assertEquals(ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL,outboundConfigPropsDataAccessException.trackingCode);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }
  
    protected class OutboundGatewayMessageProcessorNoDb extends OutboundGatewayMessageProcessor
    {
        protected Long requestId = null;
        protected String outboundStatusCode = null;
        protected String failureCode = null;
        protected String failureText = null;
        protected ItemTrackingInternalCode trackingCode = null;

        protected String getWebServiceUrl()
        {
            return super.getWebServiceUrl();
        }

        protected Map<String, String> setProperties(OutboundMessageVO value)
        {
            return super.setProperties(value);
        }
   
        protected void setProxyProperties(Map<String, String> properties)
        {
            super.setProxyProperties(properties);
        }

        protected int processSubmitResponse(SubmitResponse submitResponse, Long requestId)
        {
            return super.processSubmitResponse(submitResponse,requestId);
        }

        protected void updateOutboundTrigger(Long requestId, int responseCode)
        {
            super.updateOutboundTrigger(requestId,responseCode);
        }
        
        protected OutboundMessageVO getOutboundMessage(Long requestId)
        {
            log.debug("OutboundGatewayMessageProcessorNoDb: getOutboundMessage, requestId:" + requestId); 
            try
            {
                outboundMessageVO.setRequestId(new Long(100));
                outboundMessageVO.setDestinationIdentifier("Z00CJSE");
                outboundMessageVO.setSourceIdentifier("C00CourtServicesHub");
                outboundMessageVO.setExecMode("ASYNCH");
                outboundMessageVO.setOutboundStatusId(new Long(1));
                outboundMessageVO.setRequestTimestamp(DateTimeUtilities.processOracleDateParameterForDate("2001-07-04T23:08:56"));
                outboundMessageVO.setClobData("<CLOB_DATA>");
                outboundMessageVO.setSendAttempts(new Long(1));
                
                return outboundMessageVO;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        
        protected void updateOutboundStatus(Long requestId,
                OutboundStatusCodes outboundStatusCode,
                GdGatewayResponseCodes failureCode,
                String failureText)
        { 
            log.debug("OutboundGatewayMessageProcessorNoDb: updateOutboundStatus, requestId:" + requestId); 
            super.updateOutboundStatus(requestId,outboundStatusCode,failureCode,failureText);
        }
        
        protected void updateOutboundMessageStatus(OutboundUpdateStatusVO value)
        {
            log.debug("OutboundGatewayMessageProcessorNoDb: updateOutboundMessageStatus, requestId:" + value.getRequestId());            
            this.requestId = value.getRequestId();
            this.outboundStatusCode = value.getOutboundStatusCode();
            this.failureCode = value.getFailureCode();
            this.failureText = value.getFailureText();
        }
        
        protected String getConfigProperty(ConfigPropertyCodes configPropertyCodes)
        {
            log.debug("OutboundGatewayMessageProcessorNoDb: Start getConfigProperty:" + configPropertyCodes.toString());  
         
            String configProperty = null;
            
            if(configPropertyCodes.equals(configPropertyCodes.SCJSE_WEB_IP_ADDR))
            {
                configProperty =  "130.177.3.58";
            }
            else if(configPropertyCodes.equals(configPropertyCodes.SCJSE_WEB_PORT))
            {
                configProperty = "8001";
            }
            else if(configPropertyCodes.equals(configPropertyCodes.SCJSE_WEB_NAME))
            {
                configProperty = "stubdelivery/stubservices/StubDelivery";
            }
            else if(configPropertyCodes.equals(configPropertyCodes.WEB_SERVICE_CONNECTION_TIMEOUT))
            {
                configProperty = "120000";
            }
            else if(configPropertyCodes.equals(configPropertyCodes.WEB_SERVICE_READ_TIMEOUT))
            {
                configProperty = "120000";
            }
            
            log.debug("OutboundGatewayMessageProcessorNoDb: End getConfigProperty:" + configProperty);  
            
            return configProperty;
        }

        protected void sendOutboundTriggerMessage(Long requestId, ItemTrackingInternalCode trackingCode)
        {  
            log.debug("OutboundGatewayMessageProcessorNoDb: sendOutboundTriggerMessage, requestId:" + requestId); 
            this.requestId = requestId;
            this.trackingCode = trackingCode;            
        }
        
        protected SubmitResponse executWebServiceClient(String payload, Map properties)
            throws RemoteException, ServiceException
        {
            submitResponse.setRequestID("100");
            submitResponse.setResponseCode(1);
            submitResponse.setResponseText("");
            return submitResponse;
        }
        
        protected void handleError(Exception e)
        {
            //Does nothing in JUnit test
        }
        
        protected String getConnectionTimeout()
        {
            return "120000";
        }
        
        protected String getReadTimeout()
        {
            return "1200000";
        }
        
        protected boolean isStaticPropertiesFile() {
            return false;
        }
        
        protected String getProxyPassword() {
            return "password";
        }
    } 
    
    protected class OutboundRemoteException extends OutboundGatewayMessageProcessorNoDb
    {
        protected SubmitResponse executWebServiceClient(String payload, Map properties)
        throws RemoteException, ServiceException
        {
            log.debug("OutboundRemoteException: Throw RemoteException");  
            throw new RemoteException();
        }
    }
    
    protected class OutboundServiceException extends OutboundGatewayMessageProcessorNoDb
    {
        protected SubmitResponse executWebServiceClient(String payload, Map properties)
        throws RemoteException, ServiceException
        {
            log.debug("OutboundServiceException: Throw DataAccessException");  
            throw new ServiceException();
        }
    }
    
    protected class OutboundMessageDataAccessException extends OutboundGatewayMessageProcessorNoDb
    {
        protected OutboundMessageVO getOutboundMessage(Long requestId)
        {
            log.debug("OutboundDataAccessException: getOutboundMessage throws DataAccessException");
            throw new DataAccessException("getOutboundMessage throws DataAccessException");            
        }
    }
    
    protected class OutboundUpdateStatusDataAccessException extends OutboundGatewayMessageProcessorNoDb
    {
        protected void updateOutboundMessageStatus(OutboundUpdateStatusVO value)
        {
            log.debug("OutboundUpdateStatusDataAccessException: updateOutboundMessageStatus throws DataAccessException");
            throw new DataAccessException("updateOutboundMessageStatus throws DataAccessException");
        }
    }
    
    protected class OutboundConfigPropsDataAccessException extends OutboundGatewayMessageProcessorNoDb
    {
        protected String getConfigProperty(ConfigPropertyCodes configPropertyCodes)
        {
            log.debug("OutboundConfigPropsDataAccessException: getConfigProperty throws DataAccessException");
            throw new DataAccessException("getConfigProperty throws DataAccessException");
        }
    }
}

