package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import javax.jws.WebService;
import javax.jws.HandlerChain;

import weblogic.jws.*;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.HashMap;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;

import uk.gov.courtservice.xhibit.services.scjsestub.outbound.ScjseOutboundProcessorStub;
import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: CJSEPortImpl class implements web service endpoint interface CJSEPort
 * </p>
 * <p>
 * Description:
 * SCJSE Web Service Stub designed for testing where the remote SCJSE domain is not available
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: CJSEPortImpl.java,v 1.8 2006/10/30 16:05:12 qz4rwx Exp $ Exp $
 */

@WebService(
  serviceName="CJSEService",
  targetNamespace="http://schemas.cjse.gov.uk/endpoint/wsdl/",
  endpointInterface="uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server.CJSEPort")
@WLHttpTransport(
  contextPath="stubdelivery/stubservices",
  serviceUri="StubDelivery",
  portName="StubDelivery")
@HandlerChain(file="CJSEHandlerConfig.xml", name="CJSEChain")
public class CJSEPortImpl implements CJSEPort {

    private static final SimpleDateFormat ORACLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final String DELIVER_MESSAGE_FILE = System.getProperty("webservice.stub.dir") + System.getProperty("file.separator", "/") + "WebServiceDeliverMessage.txt";

    private static final String EXPECTED_SOURCE_ID = "C00CourtServicesHub";
    private static final String EXPECTED_DESTINATION_ID = "Z00CJSE";
    private static final String EXPECTED_EXEC_MODE = "ASYNCH";

    private static final String INBOUND_TESTING_ENABLED = "INBOUND_TESTING_ENABLED";
    private static final String DEFAULT_INBOUND_TESTING_ENABLED = "FALSE";

    private static final String SPECIALISED_VALIDATION_ENABLED = "SPECIALISED_VALIDATION_ENABLED";
    private static final String DEFAULT_SPECIALISED_VALIDATION_ENABLED = "FALSE";

    private static final Logger log = CSServices.getLogger(CJSEPortImpl.class);

    public CJSEPortImpl() {}

    /**
     * Processes the SubmitRequest returning an SubmitResponse
     *
     * @param SubmitRequest
     * @return SubmitResponse
     */
    public uk.gov.cjse.schemas.endpoint.types.SubmitResponse submit(uk.gov.cjse.schemas.endpoint.types.SubmitRequest SubmitRequest)

    {
        SubmitResponse submitResponse = setSubmitResponse(SubmitRequest);

        if(isInboundTestingEnabled())
        {
            log.debug("About to send Deliver Message to SCJSE Gateway Inbound Web Service:" + SubmitRequest.getRequestID());
            sendDeliverMessage(SubmitRequest.getRequestID());
        }

        return submitResponse;
    }

    public uk.gov.cjse.schemas.endpoint.types.RetrieveResponse retrieve(uk.gov.cjse.schemas.endpoint.types.RetrieveRequest RetrieveRequest)
    {
        return null;
    }

    /**
     * Builds the SubmitResponse
     *
     * @param SubmitRequest
     * @return SubmitResponse
     */
    protected SubmitResponse setSubmitResponse(SubmitRequest submitRequest)
    {
        SubmitResponse submitResponse = new SubmitResponse();

        try
        {
            if(!isSubmitRequestValid(submitRequest,submitResponse))
            {
                log.debug("Submit Request was invalid");
            }
            else
            {
                logSubmitRequest(submitRequest);
                setSubmitResponseFromFile(submitResponse);
            }
        }
        catch(Exception e)
        {
            log.warn("***** Exception in ServiceWSStub: " + e);
            e.printStackTrace(System.out);
            submitResponse.setResponseCode(300);
            submitResponse.setResponseText("Outbound Message Error: Unknown Exception on SCJSE Web Service Stub:" + e.getMessage());
            return submitResponse;
        }
        finally
        {
            logSubmitResponse(submitResponse);
        }

        return submitResponse;
    }

    /**
     * returns true if the Submit Request is valid otherwise false
     *
     * @param SubmitRequest
     * @param SubmitResponse
     * @return boolean
     */
    protected boolean isSubmitRequestValid(SubmitRequest submitRequest, SubmitResponse submitResponse)
    {
        boolean isValid = true;

        submitResponse.setRequestID(submitRequest.getRequestID());
        submitResponse.setResponseCode(1);
        submitResponse.setResponseText("Outbound Message Successfully Received");

        if(submitRequest.getRequestID()==null ||
           submitRequest.getRequestID().trim().equals(""))
        {
            log.debug("Submit Request Id null or spaces");
            submitResponse.setResponseCode(306);
            submitResponse.setResponseText("Outbound Message Error: InvalidRequestID");
            isValid=false;
        }

        if(submitRequest.getSourceID()==null ||
           submitRequest.getSourceID().trim().equals(""))
        {
            log.debug("Submit Source Id null or spaces");
            submitResponse.setResponseCode(304);
            submitResponse.setResponseText("Outbound Message Error: InvalidSourceID");
            isValid=false;
        }

        if(submitRequest.getDestinationID()==null ||
           submitRequest.getDestinationID().length==0 ||
           submitRequest.getDestinationID()[0]==null ||
           submitRequest.getDestinationID()[0].trim().equals(""))
        {
            log.debug("Submit Destination Id null or spaces");
            submitResponse.setResponseCode(305);
            submitResponse.setResponseText("Outbound Message Error: InvalidDestinationID");
            isValid=false;
        }

        if(submitRequest.getExecMode()==null ||
           submitRequest.getExecMode().getValue()==null ||
           submitRequest.getExecMode().getValue().trim().equals(""))
        {
            log.debug("Submit Exec Mode null or spaces");
            submitResponse.setResponseCode(307);
            submitResponse.setResponseText("Outbound Message Error: InvalidExecMode");
            isValid=false;
        }

        if(submitRequest.getTimestamp()==null)
        {
            log.debug("Submit Timestamp null");
            submitResponse.setResponseCode(300);
            submitResponse.setResponseText("Outbound Message Error: MissingTimestamp");
            isValid=false;
        }

        try
        {
            String dateString = convertDate(submitRequest.getTimestamp().getTime(),ORACLE_DATE_FORMAT);
            log.debug("Request Timestamp in Oracle Format: " + dateString);
        }
        catch(Exception e)
        {
            submitResponse.setResponseCode(300);
            submitResponse.setResponseText("Outbound Message Error: InvalidTimestamp as not of Oracle format");
            isValid=false;
        }

        if(isValid && isSpecialisedValidationEnabled())
        {
            log.debug("SpecialisedValidationEnabled");

            if(!submitRequest.getSourceID().equalsIgnoreCase(EXPECTED_SOURCE_ID))
            {
                submitResponse.setResponseCode(304);
                submitResponse.setResponseText("Outbound Message Specialised Test Failure: Source Id:" + submitRequest.getSourceID() + " did not match expected Source Id:" + EXPECTED_SOURCE_ID);
                isValid=false;
            }

            if(!submitRequest.getDestinationID()[0].equalsIgnoreCase(EXPECTED_DESTINATION_ID))
            {
                submitResponse.setResponseCode(305);
                submitResponse.setResponseText("Outbound Message Specialised Test Failure: Destination Id:" + submitRequest.getDestinationID()[0] + " did not match expected Destination Id:" + EXPECTED_DESTINATION_ID);
                isValid=false;
            }

            if(!submitRequest.getExecMode().getValue().equalsIgnoreCase(EXPECTED_EXEC_MODE))
            {
                submitResponse.setResponseCode(307);
                submitResponse.setResponseText("Outbound Message Specialised Test Failure: Exec Mode:" + submitRequest.getExecMode().getValue() + " did not match expected Exec Mode:" + EXPECTED_EXEC_MODE);
                isValid=false;
            }
        }

        log.debug("isValid: " + isValid);

        return isValid;
    }

    /**
     * Sets the SubmitResponse from a properties file
     * (WebServiceStubProperties.txt - the location of which is
     * determined by -Dwebservice.stub.dir)
     *
     * @param SubmitRequest
     */
    protected void setSubmitResponseFromFile(SubmitResponse submitResponse) throws Exception
    {
        try
        {
            boolean isTimeoutTestEnabled = false;
            long delay = 0;

            String propertyName    = null;
            String propertyValue   = null;

            HashMap stubPropertiesMap = getSubmitResponseProperties();

            if(stubPropertiesMap!=null)
            {
                Iterator it = stubPropertiesMap.keySet().iterator();

                while (it.hasNext())
                {
                    propertyName = ((String)it.next()).trim();
                    log.debug("Web Service Stub propertyName: " + propertyName);

                    propertyValue = ((String)stubPropertiesMap.get(propertyName)).trim();
                    log.debug("Web Service Stub propertyValue: " + propertyValue);

                    if(propertyName.trim().equals("submitResponseCode"))
                    {
                      log.debug("***** Found the submitResponseCode");
                      submitResponse.setResponseCode(new Integer(propertyValue).intValue());
                    }
                    else if(propertyName.trim().equals("submitResponseText"))
                    {
                        log.debug("***** Found the submitResponseText");
                        submitResponse.setResponseText("Outbound Message Response Text: " + propertyValue);
                    }
                    else if(propertyName.trim().equals("differentRequestId"))
                    {
                        log.debug("***** Found the differentRequestId");
                        submitResponse.setRequestID(propertyValue);
                    }
                    else if(propertyName.trim().equals("invalidRequestId"))
                    {
                        log.debug("***** Found the invalidRequestId");
                        submitResponse.setRequestID(propertyValue);
                    }
                    else if(propertyName.trim().equals("timeoutTestEnabled"))
                    {
                        log.debug("***** Found the timeoutTestEnabled");
                        if(propertyValue.toLowerCase().equals("true"))
                        {
                            log.debug("***** isTimeoutTestEnabled set to true");
                            isTimeoutTestEnabled=true;
                        }
                    }
                    else if(propertyName.trim().equals("delay"))
                    {
                        log.debug("***** Found the delay");
                        delay=new Long(propertyValue).longValue();
                    }
                }
            }

            log.debug("***** isTimeoutTestEnabled: " + isTimeoutTestEnabled + " with delay: " + delay);

            if(isTimeoutTestEnabled)
            {
                timeoutDelay(delay);
            }
        }
        catch(Exception e)
        {
            log.warn("***** Exception setting SubmitRequest properties from file: " + e);
            throw e;
        }
    }

    /**
     * Gets the SubmitResponseProperties from file
     * @return HashMap
     */
    protected HashMap getSubmitResponseProperties() throws Exception
    {
         return WebServiceStubProperties.getStubProperties(WebServiceStubProperties.STUB_PROPERTIES_FILE);
    }

    /**
     * Gets the Deliver Message Properties from file
     * @return HashMap
     */
    protected HashMap<String,String> getDeliverMessageProperties() throws Exception
    {
         return WebServiceStubProperties.getStubProperties(WebServiceStubProperties.DELIVER_PROPERTIES_FILE);
    }

    /**
     * Builds a delay into sending the Request
     *
     * @param long
     */
    protected void timeoutDelay(long delay)
    {
        try
        {
            Date currentTime = Calendar.getInstance().getTime();
            long currentMilliSeconds = currentTime.getTime();
            long newMilliSeconds = currentTime.getTime();
            long milliSecondsPeriod = delay;
            log.debug("***** currentMilliSeconds:" + currentMilliSeconds);

            do
            {
                currentTime = Calendar.getInstance().getTime();
                newMilliSeconds = currentTime.getTime();
                long difference = newMilliSeconds - currentMilliSeconds;
                long halfway = milliSecondsPeriod/2;

                if(difference == halfway || difference == milliSecondsPeriod)
                {
                    log.debug("***** newMilliSeconds:" + newMilliSeconds);
                }
            }
            while(newMilliSeconds - currentMilliSeconds < milliSecondsPeriod);
        }
        catch(Exception e)
        {
            log.warn("***** Exception during timeout delay: " + e);
        }
    }

    /**
     * Gets the date in a String in the specified format
     *
     * @param Date
     * @param SimpleDateFormat
     * @return String
     */
    protected String convertDate(Date date, SimpleDateFormat formatter)
    {
  	     return formatter.format(date);
    }

    /**
     * Logs the SubmitRequest
     *
     * @param SubmitRequest
     */
    protected void logSubmitRequest(SubmitRequest submitRequest)
    {
        try
        {
            if(log.isDebugEnabled())
            {
                log.debug("***** submitRequest properties are: " + "\n" +
                          " Request Id: " + submitRequest.getRequestID() + "\n" +
                          " Source Id: " + submitRequest.getSourceID() + "\n" +
                          " Destination Id: " + submitRequest.getDestinationID()[0] + "\n" +
                          " Exec Mode: " + submitRequest.getExecMode().getValue() + "\n" +
                          " Request Timestamp: " + convertDate(submitRequest.getTimestamp().getTime(),ORACLE_DATE_FORMAT) + "\n" +
                          " Message: " + submitRequest.getMessage() + "\n");
            }
        }
        catch(Exception e)
        {
            log.warn("***** Exception logging SubmitRequest: " + e);
        }
    }

    /**
     * Logs the SubmitResponse
     *
     * @param SubmitResponse
     */
    protected void logSubmitResponse(SubmitResponse submitResponse)
    {
        try
        {
            if(log.isDebugEnabled())
            {
                log.debug("***** submitResponse properties are: " + "\n" +
                          " Request Id: " + submitResponse.getRequestID() + "\n" +
                          " Response Code: " + submitResponse.getResponseCode() + "\n" +
                          " Response Code: " + submitResponse.getResponseText() + "\n");
            }
        }
        catch(Exception e)
        {
            log.warn("***** Exception logging SubmitResponse: " + e);
        }
    }

    /**
     * Sends the Deliver Message to the inbound web service
     * All errors logged and suppressed
     *
     * @param String
     */
    protected void sendDeliverMessage(String requestId)
    {
        try {
            String deliverMessage = getDeliverMessage();

            HashMap<String,String> propertiesAndValuesMap = new HashMap<String,String>();

            propertiesAndValuesMap = getDeliverMessageProperties();

            String messageType = propertiesAndValuesMap.get("XHBMessageTypeType").trim();

            log.debug("***** messageType:" + messageType);

            String xHBMessageIdentifier = null;

            xHBMessageIdentifier = propertiesAndValuesMap.get("XHBMessageIdentifier");

            log.debug("***** xHBMessageIdentifier:" + xHBMessageIdentifier);

            if(propertiesAndValuesMap.get("XHBMessageIdentifier")!=null)
            {
                propertiesAndValuesMap.remove("XHBMessageIdentifier");
            }

            if(messageType.equalsIgnoreCase("RECEIVEERROR"))
            {
                log.debug("***** messageType is RECEIVEERROR so use the original outbound message Id for the deliver inbound message:" + requestId);
                propertiesAndValuesMap.put("XHBMessageIdentifier",requestId);
                propertiesAndValuesMap.put("DeliverRequest",requestId);
            }
            else if(xHBMessageIdentifier!=null && !xHBMessageIdentifier.trim().equals(""))
            {
                log.debug("***** messageType is NOT RECEIVEERROR: " + messageType + " so use the xHBMessageIdentifier read from file:" + xHBMessageIdentifier);
                propertiesAndValuesMap.put("XHBMessageIdentifier",xHBMessageIdentifier);
                propertiesAndValuesMap.put("DeliverRequest",xHBMessageIdentifier);
            }
            else
            {
                long randomId = Math.round((Math.random() * 1000));
                String randomIdString = new Long(randomId).toString();

                log.debug("***** messageType is NOT RECEIVEERROR: " + messageType + " but xHBMessageIdentifier has not been specified so randomly generates it: " + randomIdString);

                propertiesAndValuesMap.put("XHBMessageIdentifier",randomIdString);
                propertiesAndValuesMap.put("DeliverRequest",randomIdString);
            }

            //Force create of the stub version of the outbound processor which is designed for Deliver Requests
            //Do not go via the Factory as we will always require the stub version in this scenario
            //as the impl version is not designed to handle Deliver Requests
            ScjseOutboundProcessorStub scjseOutboundProcessor = new ScjseOutboundProcessorStub();

            scjseOutboundProcessor.processRequest(propertiesAndValuesMap, deliverMessage);
        }
        catch(Exception e)
        {
            log.warn("Warning: An error occured sending the deliver message to the remote inbound web service.");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Gets the Deliver Message from file
     * Returns null if there is an error
     *
     * @return String
     */
    protected String getDeliverMessage()
    {
        String buffer = "";

        try {
            StringBuilder builder = new StringBuilder();

            Reader reader = new BufferedReader(new FileReader(DELIVER_MESSAGE_FILE));

            try {
                for (int c = reader.read(); c != -1; c = reader.read()) {
                    builder.append((char) c);
                }
            } finally {
                close(reader);
            }

            buffer = builder.toString();
        }
        catch(Exception e)
        {
            log.warn("Warning: An error occured reading the deliver message from file.");
            e.printStackTrace(System.out);
            return null;
        }

        if(log.isDebugEnabled())
        {
            log.debug("Deliver message read from file is:" + buffer);
        }

        return buffer;
    }

    /**
     * Closes the reader for the Deliver Message file
     *
     * @param Reader
     */
    private void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            log.warn("Warning: An error occured closing the reader.");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Return true if we require the inbound testing as part of the stub
     * Will be FALSE by default
     *
     * @return boolean
     */
    protected boolean isInboundTestingEnabled()
    {
        String isInboundTestingEnabled =
            System.getProperty(INBOUND_TESTING_ENABLED,DEFAULT_INBOUND_TESTING_ENABLED);

        log.debug("<< isInboundTestingEnabled: " + isInboundTestingEnabled + " >>");

        return isInboundTestingEnabled != null && isInboundTestingEnabled.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require the specialised validation of the Submit Request
     * Will be FALSE by default for the WS Stub
     *
     * @return boolean
     */
    protected boolean isSpecialisedValidationEnabled()
    {
        String isSpecialisedValidationEnabled =
            System.getProperty(SPECIALISED_VALIDATION_ENABLED,DEFAULT_SPECIALISED_VALIDATION_ENABLED);

        log.debug("<< isSpecialisedValidationEnabled: " + isSpecialisedValidationEnabled + " >>");

        return isSpecialisedValidationEnabled != null && isSpecialisedValidationEnabled.equalsIgnoreCase("TRUE");
    }
}