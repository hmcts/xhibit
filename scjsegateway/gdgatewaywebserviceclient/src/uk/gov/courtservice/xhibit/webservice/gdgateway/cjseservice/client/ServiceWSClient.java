package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEPort;
//import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEService;
//import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEService_Impl;

import uk.gov.cjse.schemas.endpoint.types.ExecMode;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;

import uk.gov.courtservice.framework.util.DateTimeUtilities;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import java.util.Calendar;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Title: Abstract Web Service Client used to send Outbound messages to a remote Web Service
 * </p>
 * <p>
 * Description: Creates a SubmitRequest using the properties and String passed in
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: ServiceWSClient.java,v 1.3 2006/10/24 14:16:58 qz4rwx Exp $ Exp $
 */
public abstract class ServiceWSClient
{
    protected static final Logger log = CSServices.getLogger(ServiceWSClient.class);

    private String serviceUrl = null;
    private Integer connectionTimeout = null;
    private Integer readTimeout = null;
    
    private CJSEPort port;
    
   /**
    * Calls a Web Service passing the message and properties in a SubmitRequest
    * and returning a SubmitResponse from the remote Web Service
    * @param String message
    * @param Map properties
    * @return SubmitResponse
    * @throws RemoteException, ServiceException    
    */    
    public abstract SubmitResponse callSubmitRequest(String message, Map properties) throws RemoteException, ServiceException;
   
    /**
     * Invokes a Web Service passing the message and properties in a SubmitRequest
     * @param  SubmitRequest
     * @param  String representing the serviceUrl 
     * @return SubmitResponse
     * @throws RemoteException, ServiceException    
     */     
     protected SubmitResponse invokeWebService(SubmitRequest submitRequest) 
         throws RemoteException, ServiceException
     {
         SubmitResponse submitResponse = new SubmitResponse();
         
         CJSEService service = new CJSEService_Impl();
         port = service.getDelivery();

         Stub stub = (Stub) port;
         
         stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, getServiceUrl());

         if(getConnectionTimeout()!=null)
         {
             log.debug("Set the Connection Timeout Property (weblogic.wsee.transport.connection.timeout) to:" + getConnectionTimeout());
             stub._setProperty("weblogic.wsee.transport.connection.timeout", getConnectionTimeout());
         }
         else
         {
             log.debug("Connection Timeout not in database or weblogic startup so weblogic.wsee.transport.connection.timeout to defaults to 0");
             stub._setProperty("weblogic.wsee.transport.connection.timeout", new Integer(0));
         }
         
         if(getReadTimeout()!=null)
         {
             log.debug("Set the Read Timeout Property (weblogic.wsee.transport.read.timeout) to:" + getReadTimeout());
             stub._setProperty("weblogic.wsee.transport.read.timeout", getReadTimeout());
         }
         else
         {
             log.debug("Read Timeout not in database or weblogic startup so weblogic.wsee.transport.read.timeout to defaults to 0");
             stub._setProperty("weblogic.wsee.transport.read.timeout", new Integer(0));
         }
         
         submitResponse = port.submit(submitRequest);

         if (submitResponse != null)
         {
            log.debug("[ServiceWSClient] Request Id = " +
                        submitResponse.getRequestID());
            log.debug("[ServiceWSClient] Response Code = " +
                        submitResponse.getResponseCode());
            log.debug("[ServiceWSClient] Response Text = " +
                        submitResponse.getResponseText());
         }

         log.debug("[ServiceWSClient] callSubmit: Completed invocation of the CJSE Web Service and received a response");

         return submitResponse;                
     }
     
     /**
      * Builds a test submit request message
      * Also extracts the serviceUrl
      * @param String
      * @param Map          
      * @return SubmitRequest 
      */
     protected SubmitRequest setSubmitRequest(String message, Map properties)
     {
         SubmitRequest  submitRequest  = new SubmitRequest();

         submitRequest.setMessage(message);

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
                 submitRequest.setRequestID(propertyValue);
             }
             else if(propertyName.trim().equals(OutboundConstants.SOURCE_IDENTIFIER))
             {
                 log.debug("***** Found the Source Id:" + propertyValue);
                 submitRequest.setSourceID(propertyValue);
             }
             else if(propertyName.trim().equals(OutboundConstants.DESTINATION_IDENTIFIER))
             {
                 log.debug("***** Found the Destination Id:" + propertyValue);
                 submitRequest.setDestinationID(new String[] {propertyValue});
             } 
             else if(propertyName.trim().equals(OutboundConstants.EXEC_MODE))
             {
                 log.debug("***** Found the Exec Mode:" + propertyValue);
                 submitRequest.setExecMode(ExecMode.fromString(propertyValue));
             }
             else if(propertyName.trim().equals(OutboundConstants.REQUEST_TIMESTAMP))
             {
                 log.debug("***** Found the Request Timestamp:" + propertyValue);
                 submitRequest.setTimestamp(getFormattedTimestamp(propertyValue));
             }
             else if(propertyName.trim().equals(OutboundConstants.SERVICE_URL))
             {
                 log.debug("***** Found the Service Url:" + propertyValue);
                 setServiceUrl(propertyValue);
             }
             else if(propertyName.trim().equals(OutboundConstants.CONNECTION_TIMEOUT))
             {
                 log.debug("***** Found the Connection Timeout:" + propertyValue);
                 try
                 {
                     setConnectionTimeout(new Integer(propertyValue));
                 }
                 catch(Exception e)
                 {
                     log.info("***** Exception in setting ConnectionTimeout so it will default" + e);
                 }
             } 
             else if(propertyName.trim().equals(OutboundConstants.READ_TIMEOUT))
             {
                 log.debug("***** Found the Read Timeout:" + propertyValue);
                 try
                 {
                     setReadTimeout(new Integer(propertyValue));
                 }
                 catch(Exception e)
                 {
                     log.info("***** Exception in setting ReadTimeout so it will default" + e);
                 }
             } 
             else
             {
                 setAdditionalProperties(submitRequest,propertyName,propertyValue);
             }
        }    
        return submitRequest;
    }

    /**
     * Sets additional request properties
     * Does nothing by default but can be overridden in subclasses          
     * @param SubmitRequest
     * @param String propertyName
     * @param String propertyValue     
     */    
     protected void setAdditionalProperties(SubmitRequest submitRequest,
                                            String propertyName, 
                                            String propertyValue)
     {
         // Does nothing by default
         log.debug("***** Unknown property found propertyName:" + propertyName + " propertyValue:" + propertyValue);
     }
     
    /**
     * Returns the formatted calendar based on the dateString
     * @param String          
     * @return Calendar     
     */    
    protected Calendar getFormattedTimestamp(String dateString) throws CSUnrecoverableException
    {     
        try
        {
            return DateTimeUtilities.processOracleDateParameter(dateString);
        }
        catch(Exception e)
        {
            // this is an unexpected Parse exception, not a business exception
            throw new CSUnrecoverableException("Failed to create Calendar for property: " + dateString +
                 " due to an error with the formatting (either it was null or did not " +
                 " follow the Oracle Timestamp format)", e);                
        }     
    }
    
    /**
     * Returns the Service URL          
     * @return String serviceUrl     
     */    
    protected String getServiceUrl()
    {
        log.debug("[ServiceWSClient] get serviceUrl: " + serviceUrl);
        return serviceUrl;
    }

     /**
      * Sets the Service URL
      * @param String serviceUrl 
      */ 
    protected void setServiceUrl(String serviceUrl)
    {
        log.debug("[ServiceWSClient] Set serviceUrl: " + serviceUrl);  
        this.serviceUrl = serviceUrl;
    }
    
    /**
     * Returns the Connection Timeout
     * @return int connectionTimeout     
     */    
    protected Integer getConnectionTimeout()
    {
        log.debug("getConnectionTimeout: " + connectionTimeout);
        return connectionTimeout;
    }

     /**
      * Sets the connectionTimeout
      * @param Integer connectionTimeout 
      */ 
    protected void setConnectionTimeout(Integer connectionTimeout)
    {
        log.debug("Set connectionTimeout: " + connectionTimeout);  
        this.connectionTimeout = connectionTimeout;
    }
    
    /**
     * Returns the readTimeout          
     * @return Integer readTimeout     
     */    
    protected Integer getReadTimeout()
    {
        log.debug("get readTimeout: " + readTimeout);
        return readTimeout;
    }

     /**
      * Sets the readTimeout
      * @param Integer readTimeout 
      */ 
    protected void setReadTimeout(Integer readTimeout)
    {
        log.debug("Set readTimeout: " + readTimeout);  
        this.readTimeout = readTimeout;
    }
}