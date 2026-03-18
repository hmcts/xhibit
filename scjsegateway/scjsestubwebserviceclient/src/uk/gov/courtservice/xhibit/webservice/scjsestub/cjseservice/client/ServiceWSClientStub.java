package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.client;

import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient;
import uk.gov.cjse.schemas.endpoint.types.ExecMode;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>
 * Title: Web Service Client used to send a SCJSE Gateway Outbound messages to a stubbed 
 * SCJSE Web Service and also to send a stubbed SCJSE Outbound Message to the
 * SCJSE Gateway Inbound Web Service
 * </p>
 * <p>
 * Description: Creates a SubmitRequest using the properties and String passed in
 * 
 * The URL will be set from a JAVA_OPTION for additional flexibility and also 
 * where this class is acting to create a stubbed SCJSE Outbound Message there
 * will have been no previous access to the GD Gateway Database 
 * Where the URL is not specified it will be looked up from the superclass
 * Where the class is sending SCJSE Outbound Message to a stubbed SCJSE Web Service
 * the URL will have been set on the GD Gateway Database
 * 
 * Expected format is http://${wls.hostname}:${wls.port}/${ws_context_name}/${ws_name}
 
 * The optional WSDL properties will be defaulted if not set from a property
 * This is expected to be the case when acting to create a stubbed SCJSE Outbound Message
 * Where the class is sending SCJSE Outbound Message to a stubbed SCJSE Web Service
 * however it is expected that these values have previously been retrieved from the
 * Outbound Messages record on the GD Gateway Database
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 *
 * @author GJS
 * @version $Id: ServiceWSClientStub.java,v 1.9 2006/10/24 14:15:16 qz4rwx Exp $ Exp $
 */

public class ServiceWSClientStub extends ServiceWSClient
{
    private static final String STUB_WEB_SERVICE_LOCATION    = "STUB_WEB_SERVICE_LOCATION";
    private static final String INBOUND_WEB_SERVICE_LOCATION = "INBOUND_WEB_SERVICE_LOCATION";
    
    //These are defaults for the mandatory properties when developers don't deploy the test files
    //They are appropriate for the outbound SCJSE Stub as the properties for  the 
    //outbound SCJSE gateway stub are passed in
    private static final String DEFAULT_SOURCE_ID = "Z00CJSE";
    private static final String DEFAULT_DESTINATION_ID = "C00CourtServicesHub";
    private static final String DEFAULT_EXEC_MODE = "ASYNCH";
 
    private static final String WEB_SERVICE_CONNECTION_TIMEOUT = "WEB_SERVICE_CONNECTION_TIMEOUT";
    private static final String WEB_SERVICE_READ_TIMEOUT = "WEB_SERVICE_READ_TIMEOUT";

    private String targetWebService = null;
    
    /**
     * Calls a Web Service passing the message and properties in a SubmitRequest
     * and returning a SubmitResponse from the remote Web Service
     *
     * It is expected that this will be used as a stub to replace the SCJSE
     * processing ie the message passed in will be a DeliverRequest
     * and the destination will be the SCJSE Gateway Inbound Web Service
     * The WSDL properties will not  have been set in this scenario
     * 
     * It can also be used as a stub client web service on the outbound
     * - data will be passed to a remote Web Service which can be set from the JAVA_OPTIONS
     * The WSDL properties will have been set in this scenario and the message
     * will be a ReceiveRequest
     * 
     * @param String message
     * @param Map properties
     * @return SubmitResponse
     * @throws RemoteException, ServiceException
     */
    public SubmitResponse callSubmitRequest(String message, Map properties) throws RemoteException, ServiceException
    {
        log.info("callSubmitRequest: START");
        SubmitRequest submitRequest = null;
        
        try
        {
            log.debug("[ServiceWSClientStub] callSubmit");
        
            submitRequest = setSubmitRequest(message, properties);

            if(isSCJSEGatewayWSClientStub(submitRequest))
            {
                log.debug("[ServiceWSClientStub] targetWebService set to Stub Web Service");
                targetWebService = CSServices.getConfigServices().getProperty(STUB_WEB_SERVICE_LOCATION);                
            } 
            else
            {
                log.debug("[ServiceWSClientStub] targetWebService set to Inbound Web Service");
                targetWebService = CSServices.getConfigServices().getProperty(INBOUND_WEB_SERVICE_LOCATION);
                
                setSubmitRequestDefaults(submitRequest);
                
                setSubmitRequestFromFile(submitRequest);
                
                if(getConnectionTimeout()==null)
                {
                    setConnectionTimeout(new Integer(CSServices.getConfigServices().getProperty(WEB_SERVICE_CONNECTION_TIMEOUT,"0")));
                    log.debug("***** ConnectionTimeout set in Web Service Client Stub to:" + getConnectionTimeout());
                }

                if(getReadTimeout()==null)
                {
                    setReadTimeout(new Integer(CSServices.getConfigServices().getProperty(WEB_SERVICE_READ_TIMEOUT,"0")));
                    log.debug("***** ReadTimeout set in Web Service Client Stub to:" + getReadTimeout());
                }
            }
            
            logSubmitRequest(submitRequest);
            
            log.info("callSubmitRequest: END, about to return SubmitResponse");
            return invokeWebService(submitRequest);
        }
        catch(Exception e)
        {
            log.debug("***** Exception in ServiceWSClientStub: " + e);
            e.printStackTrace();
            handleError(e);
            throw new RemoteException(e.getMessage());
        }
    }

    /**
     * Delegates to the CSServices.getDefaultErrorHandler to handle the error
     * @param Exception e
     */
    protected void handleError(Exception e)
    {
        CSServices.getDefaultErrorHandler().handleError(e, ServiceWSClientStub.class);
    }
    
    /**
     * Returns the Service URL which includes the Web Service Name of the
     * Web Service to be invoked
     * As this is development or stubbed testing the remote
     * Web Service URL to be specified from the Weblogic startup file if required.
     * If the targetWebService is not set the Url returned will be that
     * originally set on the database
     * @return String serviceUrl
     */
    protected String getServiceUrl()
    { 
        if(targetWebService!=null)
        {
            log.debug("Target Web Service for the Web Service Client Stub is: " + targetWebService);
            return targetWebService;
        }
        else
        {
            log.debug("Stub Service Url not specified as Java Option so return the Url set from the database");
            return super.getServiceUrl();
        }
    }
    
    /**
     * Returns true where the CJSE properties are prepopulated indicating
     * the full outbound processing has executed (the message will be a ReceiveRequest)
     * and this transaction is acting as an Outbound SCJSE Gateway WS Client Stub 
     * which send the message the Inbound SCJSE Web Service Stub
     * 
     * Returns false where CJSE properties are not prepopulated indicating only the
     * RequestId and Message (which will be a DeliverRequest) will be populated and
     * this transaction is acting as an Outbound SCJSE WS Client Stub which will send
     * the message to the Inbound SCJSE Gateway Web Service
     *  
     * @param SubmitRequest
     * @return boolean
     */
    protected boolean isSCJSEGatewayWSClientStub(SubmitRequest submitRequest)
    {
        if(submitRequest.getRequestID()!=null &&
           submitRequest.getSourceID()!=null &&
           submitRequest.getDestinationID()!=null &&
           submitRequest.getDestinationID().length!=0 &&
           submitRequest.getDestinationID()[0]!=null &&
           submitRequest.getExecMode()!=null &&
           submitRequest.getTimestamp()!=null &&
           submitRequest.getMessage()!=null)
        {
           log.debug("[ServiceWSClientStub] submitRequest prepopulated so WS Client Stub" +
                     " will act as an Outbound SCJSE Gateway WS Client Stub call the Inbound SCJSE Web Service Stub");
           return true;         
        }
        else
        {    
            log.debug("[ServiceWSClientStub] submitRequest NOT prepopulated" +
                      " (only Request ID and Message passed in) so WS Client Stub acts" +
                      " as an Outbound SCJSE WS Client Stub and will call SCJSE Gateway Inbound Web Service");  
            return false;
        }
    } 

    /**
     * Sets the SubmitRequest defaults
     *  
     * @param SubmitRequest     
     */
    protected void setSubmitRequestDefaults(SubmitRequest submitRequest) throws Exception
    {
        try
        {
            if(submitRequest.getSourceID()==null)
            {
                submitRequest.setSourceID(DEFAULT_SOURCE_ID);
            }
            if(submitRequest.getDestinationID()==null ||
               submitRequest.getDestinationID().length==0 ||
               submitRequest.getDestinationID()[0]==null)
            {
                submitRequest.setDestinationID(new String[]{DEFAULT_DESTINATION_ID});
            }
            if(submitRequest.getExecMode()==null)
            {
                submitRequest.setExecMode(ExecMode.fromString(DEFAULT_EXEC_MODE));
            }
            if(submitRequest.getTimestamp()==null)
            {
                Calendar currentTime = null;
                
                try
                {
                    String currentTimeString = DateTimeUtilities.convertOracleDate(Calendar.getInstance().getTime());
                    currentTime = DateTimeUtilities.processOracleDateParameter(currentTimeString);
                }
                catch(Exception e)
                {
                    log.debug("[ServiceWSClientStub] Error defaulting Request Timestamp for Outbound SCJSE Stub Message so default to current time");                
                    currentTime = Calendar.getInstance();
                }
            
                submitRequest.setTimestamp(currentTime);
            }
        }
        catch(Exception e)
        {
            log.debug("***** Exception setting SubmitRequest defaults: " + e);
            throw e;
        }
    }    

    /**
     * Sets the SubmitRequest from a properties file
     * (WebServiceClientStubProperties.txt - the location of which is 
     * determined by -Dwebservice.stub.dir)
     *  
     * @param SubmitRequest     
     */
    protected void setSubmitRequestFromFile(SubmitRequest submitRequest) throws Exception
    {
        try
        {
            //Enables the tester to configure the request properties in WebServiceStubProperties.txt

            boolean isTimeoutTestEnabled = false;
            long delay = 0;

            String propertyName    = null;
            String propertyValue   = null;

            HashMap stubClientPropertiesMap = getSubmitRequestProperties();
            
            if(stubClientPropertiesMap!=null)
            {
              log.debug("WebServiceStubProperties.txt has beend eployed with SCJSE WS Stub properties");
                        
              Iterator it = stubClientPropertiesMap.keySet().iterator();

              while (it.hasNext())
              {
                propertyName = ((String)it.next()).trim();
                log.debug("Web Service Client Stub propertyName: " + propertyName);

                  propertyValue = ((String)stubClientPropertiesMap.get(propertyName)).trim();
                  log.debug("Web Service Client Stub propertyValue: " + propertyValue);

                  if(propertyName.trim().equals("sourceId"))
                  {
                    log.debug("***** Found the sourceId");
                    submitRequest.setSourceID(propertyValue);
                  }
                  else if(propertyName.trim().equals("destinationId"))
                  {
                    log.debug("***** Found the destinationId");
                    submitRequest.setDestinationID(new String[]{propertyValue});
                  }
                  else if(propertyName.trim().equals("execMode"))
                  {
                    log.debug("***** Found the execMode");
                    submitRequest.setExecMode(ExecMode.fromString(propertyValue));
                  }
                  else if(propertyName.trim().equals("requestTimestamp"))
                  {
                    log.debug("***** Found the requestTimestamp");
                    
                    Calendar currentTime = null;
                    
                    try
                    {
                        currentTime = DateTimeUtilities.processOracleDateParameter(propertyValue);
                    }
                    catch(Exception e)
                    {
                        log.debug("[ServiceWSClientStub] Error defaulting Request Timestamp for Outbound SCJSE Stub Message so default to current time");                
                        currentTime = Calendar.getInstance();
                    }
                
                    submitRequest.setTimestamp(currentTime);
                  }
                  else if(propertyName.trim().equals("differentRequestId"))
                  {
                      log.debug("***** Found the differentRequestId");
                      submitRequest.setRequestID(propertyValue);
                  }
                  else if(propertyName.trim().equals("invalidRequestId"))
                  {
                      log.debug("***** Found the invalidRequestId");
                      submitRequest.setRequestID(propertyValue);
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
            log.debug("***** Exception setting SubmitRequest properties from file: " + e);
            throw e;
        }
    }    

    /**
     * Gets the SubmitRequestProperties from file
     * @return HashMap
     */
    protected HashMap getSubmitRequestProperties() throws Exception
    {
         return WebServiceClientStubProperties.getStubProperties();
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
            log.debug("***** Exception during timeout delay: " + e);
        }
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
            log.debug("***** submitRequest properties are: " + "\n" +
                " Request Id: " + submitRequest.getRequestID() + "\n" + 
                " Source Id: " + submitRequest.getSourceID() + "\n" + 
                " Destination Id: " + submitRequest.getDestinationID()[0] + "\n" + 
                " Exec Mode: " + submitRequest.getExecMode().getValue() + "\n" + 
                " Request Timestamp: " + DateTimeUtilities.convertOracleDate(submitRequest.getTimestamp().getTime()) + "\n" + 
                " Message: " + submitRequest.getMessage() + "\n");
        }
        catch(Exception e)
        {
            log.debug("***** Exception logging SubmitRequest: " + e);
        }
    }    
}
