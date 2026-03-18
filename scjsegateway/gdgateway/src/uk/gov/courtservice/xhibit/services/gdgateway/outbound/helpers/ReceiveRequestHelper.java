package uk.gov.courtservice.xhibit.services.gdgateway.outbound.helpers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.cjse.schemas.messages.format.x200605.MessageFormat;
import uk.gov.cjse.schemas.messages.format.x200605.MessageSchema;
import uk.gov.cjse.schemas.messages.format.x200605.MessageType;
import uk.gov.cjse.schemas.messages.messaging.x200605.MessageSchemaStructureChoice;
import uk.gov.cjse.schemas.messages.messaging.x200605.MessageSchemaStructureChoiceSequence;
import uk.gov.cjse.schemas.messages.messaging.x200605.RequestingSystem;
import uk.gov.cjse.schemas.messages.metadata.x200605.MessageMetadata;
import uk.gov.cjse.schemas.messages.metadata.x200605.OriginatingSystem;
import uk.gov.cjse.schemas.messages.receive.x200605.Message;
import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequest;
import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequestStructureChoice;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.ClassAndMethodHelper;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.services.gdgateway.common.XMLMarshaller;

/**
 * <p>
 * Title: ReceiveRequestHelper: Helper methods to ensure data can be mapped to
 * the Receive Request objects
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: ReceiveRequestHelper.java
 */
public class ReceiveRequestHelper {
    private static final Logger log = CSServices.getLogger(ReceiveRequestHelper.class);

    private ClassAndMethodHelper classAndMethodHelper = new ClassAndMethodHelper();

    private static final String DELIVER_ERROR = "DELIVERERROR"; 

    private static final String JMSX_DELIVERY_COUNT      = "JMSXDeliveryCount";
    private static final String JMS_BEA_REDELIVERY_LIMIT = "JMS_BEA_RedeliveryLimit";
    private static final String JMS_BEA_DELIVERY_FAILURE_REASON =  "JMS_BEA_DeliveryFailureReason";
    
    /**
     * createReceiveRequestStructure: Maps properties within the Map parameter
     * to the appropriate Receive Request Data Structures
     * 
     * @param Map
     *            propertiesMap
     * @param String
     *            payload
     * @return ReceiveRequestStructure contains the ReceiveRequestStructure with
     *         all its data set to the properties in the string and the
     *         ConfigurablePropertiesStructure, which contains all the
     *         configrable static properties
     */
    
    public ReceiveRequest createReceiveRequest(Map propertiesAndValuesMap, String payload) throws IOException,
            ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.info("createReceiveRequest START:");

        ReceiveRequest receiveRequest = new ReceiveRequest();
        MessageFormat mfs = new MessageFormat();
        MessageMetadata mms = new MessageMetadata();
        RequestingSystem requestingSystem = new RequestingSystem();
        OriginatingSystem originatingSystem = new OriginatingSystem();
        MessageType mts = new MessageType();
        MessageSchema mss = new MessageSchema();
        MessageSchemaStructureChoice mssc = new MessageSchemaStructureChoice();
        MessageSchemaStructureChoiceSequence msscs = new MessageSchemaStructureChoiceSequence();
        ReceiveRequestStructureChoice rrsc = new ReceiveRequestStructureChoice();

        String propertyName = null;
        String propertyValue = null;
        String propertyMapping = null;

        StringTokenizer st = null;
        String className = null;
        String methodName = null;
        Object methodParameterPropertyValue = null;
        boolean deliverException = false;

        Iterator it = propertiesAndValuesMap.keySet().iterator();
        while (it.hasNext()) {
            propertyName = (String) it.next();

            log.debug("propertyName: " + propertyName);

            if (propertyName.equals(JMSX_DELIVERY_COUNT) ||
                propertyName.equals(JMS_BEA_REDELIVERY_LIMIT)||
                propertyName.equals(JMS_BEA_DELIVERY_FAILURE_REASON)) {
                continue;
            }

            propertyValue = (String) propertiesAndValuesMap.get(propertyName);
            log.debug("propertyValue: " + propertyValue);

            // Check if exception (ReceiveRequest will deal with exceptions of type DELIVERERROR)
            if (propertyName.equals("XHBMessageTypeType")) {
                if (DELIVER_ERROR.equals(propertyValue)) {
                    deliverException = true;
                }
            }
            // Prefix Receive onto XHBMessageIdentifier
            if (propertyName.equals("XHBMessageIdentifier")) {
                propertyName = "Receive" + propertyName;
            }
            
            propertyMapping = getOutboundPropertyMapping(propertyName);
                        
            log.debug("propertyMapping: " + propertyMapping);

            if (propertyMapping == null) {
                log.debug("Not a business or configrable property so ignore it:" + propertyName);
                continue;
            }

            st = new StringTokenizer(propertyMapping, ",");

            className = st.nextToken();
            methodName = st.nextToken();
            if (log.isDebugEnabled()) {
                log.debug("className: " + className + " methodName: " + methodName);
            }

            /*
             * NOTE: Properties not used OR set in the JMS Properties. These
             * either have default values or are optional and not required to be
             * set by XHIBIT. XHBAckRequested XHBDataControllerOrganisation
             * XHBDataControllerReferencedElementURI
             * XHBDataControllerControlsEntireMessage
             */
            if (className.trim().equalsIgnoreCase("ReceiveRequest")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(
                        receiveRequest.getClass(), methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, receiveRequest);
                log.debug("ReceiveRequest found execute receive request");
            } else if (className.trim().equalsIgnoreCase("MessageType")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(mts.getClass(),
                        methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, mts);
                log.debug("MessageType found execute mts");
            } else if (className.trim().equalsIgnoreCase("MessageSchema")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(mss.getClass(),
                        methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, mss);
                log.debug("MessageSchema found execute mss");
            } else if (className.trim().equalsIgnoreCase("OriginatingSystem")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(
                        originatingSystem.getClass(), methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, originatingSystem);
                log.debug("OriginatingSystem found execute originatingSystem");
            } else if (className.trim().equalsIgnoreCase("RequestingSystem")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(
                        requestingSystem.getClass(), methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, requestingSystem);
                log.debug("RequestingSystem found execute requestingSystem");
            } else if (className.trim().equalsIgnoreCase("MessageMetadata")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(mms.getClass(),
                        methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, mms);
                log.debug("MessageMetadata found execute mms");
            } else if (className.trim().equalsIgnoreCase("MessageSchemaStructureChoice")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(mssc.getClass(),
                        methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, mssc);
                log.debug("MessageSchemaStructureChoice found execute mssc");
            } else if (className.trim().equalsIgnoreCase("MessageSchemaStructureChoiceSequence")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(msscs.getClass(),
                        methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, msscs);
                log.debug("MessageSchemaStructureChoiceSequence found execute msscs");
            } else {
                log.debug("No structure matched the classname: " + className);
            }
            
            /*The following is a fix for a spurious live defect which only presents itself in a very small percentage of
            messages where the creation date time is corrupted. After running the class in debug mode, it seems that the
            problem occurs deep within the reflection code above. After lengthy investigation, it has been decided that 
            the best way to resolve this is to overwrite the creation date time property at this stage */
            if(propertyName.equals("XHBCreationDateTime")){  
                log.debug("XHBCreationDateTime Before overwrite="+mms.getCreationDateTime().toString());
                try{
                    mms.setCreationDateTime(DateTimeUtilities.processOracleDateParameterForDate(propertyValue));
                    log.debug("XHBCreationDateTime after overwrite="+mms.getCreationDateTime().toString());
                }catch(Exception e){
                    throw new CSUnrecoverableException("Failed to set CreationDateTime to " + propertyValue
                        + " due to an error with the Calendar (either it was null or did not "
                        + " follow the Oracle Timestamp format)", e);                    
                }
                
            }
        }

        mssc.setMessageSchemaStructureChoiceSequence(msscs);
        mss.setMessageSchemaStructureChoice(mssc);
        mfs.setMessageType(mts);
        mfs.setMessageSchema(mss);
        mms.setOriginatingSystem(originatingSystem);

        if (deliverException) {
            log.debug("payload contains Exception");
            
            /**
             * NOTE:    Unfortunately a way to integrate the exception.xml straight into the ReceiveRequest could not be
             * found.  The xml has therefore been unmarshalled and then the Exception object set to the ReceiveRequest.
            */
            
            if (payload== null || payload.length() <= 0){
                throw new IllegalArgumentException(
                        "ReceiveRequestHelper - payload does not contain any information.  " +
                        "Exception Structure cannot be created.");
            }
            
            log.debug("payload: "+ payload);
            
            uk.gov.cjse.schemas.messages.receive.x200605.Exception exception = XMLMarshaller
                    .unmarshallExceptionXmlToReceiveException(payload);
            rrsc.setException(exception);
        } else {
            log.debug("payload contains Message");
            Message m = new Message();
            
            /** 
             * NOTE: This is work around code for Castor Limitations. A temporary string is set as the AnyObject value instead of the payload.
             * There is currently a problem setting the payload XML string here.  
             * Setting the string directly adds extra surrounding String tags which forms invalid XML.
             * An alternative part solution is to add the xml string in an AnyNode of type TEXT.
             * This alternative adds the XML string without the extra tags but the XML String is encoded!
             * It is possible to decode the string but is more performance intensive than the existing implemented workaround.
             */
                        
            m.setAnyObject("uk.gov.courtservice.xhibit.services.outbound.WORKAROUNDFORANYNODE");
            rrsc.setMessage(m);

        }
        receiveRequest.setReceiveRequestStructureChoice(rrsc);
        
        // This is left as the default value
        receiveRequest.setAckRequested(false);
        receiveRequest.setMessageFormat(mfs);
        receiveRequest.setMessageMetadata(mms);
        receiveRequest.setRequestingSystem(requestingSystem);
        log.info("createReceiveRequest :END");
        return receiveRequest;
    }

    /**
     * Method to return an outbound property object details based on the specified property
     * Returns a String with the following format
     * <ClassName>,<MethodName>
     * 
     * @param String propertyName
     * @return String
     */
    private String getOutboundPropertyMapping(String propertyName){
        String property = null;

        log.debug("getOutboundPropertyMapping, key is:" + propertyName);

        try {
            
            if(propertyName.equals(OutboundPropertyMapping.ReceiveXHBMessageIdentifier.name()))
            {
                property=OutboundPropertyMapping.ReceiveXHBMessageIdentifier.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.DeliverXHBMessageIdentifier.name()))
            {
                property=OutboundPropertyMapping.DeliverXHBMessageIdentifier.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBMessageTypeType.name()))
            {
                property=OutboundPropertyMapping.XHBMessageTypeType.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBMessageTypeVersion.name()))
            {
                property=OutboundPropertyMapping.XHBMessageTypeVersion.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBMessageSchemaNamespace.name()))
            {
                property=OutboundPropertyMapping.XHBMessageSchemaNamespace.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBMessageSchemaVersion.name()))
            {
                property=OutboundPropertyMapping.XHBMessageSchemaVersion.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBMessageSchemaIdentifier.name()))
            {
                property=OutboundPropertyMapping.XHBMessageSchemaIdentifier.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBOriginatingSystemName.name()))
            {
                property=OutboundPropertyMapping.XHBOriginatingSystemName.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBOriginatingSystemOrgUnitCode.name()))
            {
                property=OutboundPropertyMapping.XHBOriginatingSystemOrgUnitCode.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBOriginatingSystemEnvironment.name()))
            {
                property=OutboundPropertyMapping.XHBOriginatingSystemEnvironment.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBCreationDateTime.name()))
            {
                property=OutboundPropertyMapping.XHBCreationDateTime.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBExpiryDateTime.name()))
            {
                property=OutboundPropertyMapping.XHBExpiryDateTime.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBRequestingSystemName.name()))
            {
                property=OutboundPropertyMapping.XHBRequestingSystemName.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBRequestingSystemOrgUnitCode.name()))
            {
                property=OutboundPropertyMapping.XHBRequestingSystemOrgUnitCode.getCode();
            }
            else if(propertyName.equals(OutboundPropertyMapping.XHBRequestingSystemEnvironment.name()))
            {
                property=OutboundPropertyMapping.XHBRequestingSystemEnvironment.getCode();
            }
            else
            {
                log.info("getOutboundPropertyMapping no mapping found for property:" + propertyName);
            }            
            
            log.debug("Retrieved value for specific getOutboundPropertyMapping:" + property);

        } catch (Exception e) {
            log.error("Start Exception in getOutboundPropertyMapping for property:" + propertyName,e);
        }
        
        return property;
    }
    
    /**
     * Helper class to establish method and execute
     * 
     * @return ClassAndMethodHelper
     */
    private ClassAndMethodHelper getClassAndMethodHelper() {
        if (classAndMethodHelper == null) {
            classAndMethodHelper = new ClassAndMethodHelper();
        }
        return classAndMethodHelper;
    }
}