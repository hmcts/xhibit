package uk.gov.courtservice.xhibit.services.scjsestub.outbound.helpers;

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
import uk.gov.cjse.schemas.messages.deliver.x200605.Message;
import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequest;
import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequestStructureChoice;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.ClassAndMethodHelper;
import uk.gov.courtservice.xhibit.services.gdgateway.common.XMLMarshaller;
import uk.gov.courtservice.xhibit.services.gdgateway.outbound.helpers.OutboundPropertyMapping;

/**
 * <p>
 * Title: DeliverRequestHelper: Helper methods to ensure data can be mapped to
 * the Deliver Request objects
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
 * @version $Id: DeliverRequestHelper.java
 */
public class DeliverRequestHelper {
    private static final Logger log = CSServices.getLogger(DeliverRequestHelper.class);

    private ClassAndMethodHelper classAndMethodHelper = new ClassAndMethodHelper();

    private static final String RECEIVE_ERROR = "RECEIVEERROR"; 
    
    private static final String JMSX_DELIVERY_COUNT      = "JMSXDeliveryCount";
    private static final String JMS_BEA_REDELIVERY_LIMIT = "JMS_BEA_RedeliveryLimit";
    private static final String JMS_BEA_DELIVERY_FAILURE_REASON =  "JMS_BEA_DeliveryFailureReason";

    /**
     * createDeliverRequestStructure: Maps properties within the Map parameter
     * to the appropriate Deliver Request Data Structures
     * 
     * @param Map
     *            propertiesMap
     * @param String
     *            payload
     * @return DeliverRequestStructure contains the DeliverRequestStructure with
     *         all its data set to the properties in the string and the
     *         ConfigurablePropertiesStructure, which contains all the
     *         configrable static properties
     */
    
    public DeliverRequest createDeliverRequest(Map propertiesAndValuesMap, String payload) throws IOException,
            ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.debug("createDeliverRequest START:");

        DeliverRequest deliverRequest = new DeliverRequest();
        MessageFormat mfs = new MessageFormat();
        MessageMetadata mms = new MessageMetadata();
        RequestingSystem requestingSystem = new RequestingSystem();
        OriginatingSystem originatingSystem = new OriginatingSystem();
        MessageType mts = new MessageType();
        MessageSchema mss = new MessageSchema();
        MessageSchemaStructureChoice mssc = new MessageSchemaStructureChoice();
        MessageSchemaStructureChoiceSequence msscs = new MessageSchemaStructureChoiceSequence();
        DeliverRequestStructureChoice rrsc = new DeliverRequestStructureChoice();

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
                propertyName.equals(JMS_BEA_REDELIVERY_LIMIT) ||
                propertyName.equals(JMS_BEA_DELIVERY_FAILURE_REASON)) {
                continue;
            }

            propertyValue = (String) propertiesAndValuesMap.get(propertyName);
            log.debug("propertyValue: " + propertyValue);

            // Check if exception
            if (propertyName.equals("XHBMessageTypeType")) {
                if (RECEIVE_ERROR.equals(propertyValue)) {
                    deliverException = true;
                }
            }
            
            // Prefix Deliver onto XHBMessageIdentifier
            if (propertyName.equals("XHBMessageIdentifier")) {
                propertyName = "Deliver" + propertyName;
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
            if (className.trim().equalsIgnoreCase("DeliverRequest")) {
                methodParameterPropertyValue = getClassAndMethodHelper().determineMethodParameter(
                        deliverRequest.getClass(), methodName, propertyValue);
                getClassAndMethodHelper().execute(methodParameterPropertyValue, methodName, deliverRequest);
                log.debug("DeliverRequest found execute deliver request");
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
        }

        mssc.setMessageSchemaStructureChoiceSequence(msscs);
        mss.setMessageSchemaStructureChoice(mssc);
        mfs.setMessageType(mts);
        mfs.setMessageSchema(mss);
        mms.setOriginatingSystem(originatingSystem);

        if (deliverException) {
            log.debug("payload contains Exception");
            
            /**
             * NOTE:    Unfortunately a way to integrate the exception.xml straight into the DeliverRequest could not be
             * found.  The xml has therefore been unmarshalled and then the Exception object set to the DeliverRequest.
            */
            
            if (payload== null || payload.length() <= 0){
                throw new IllegalArgumentException(
                        "DeliverRequestHelper - payload does not contain any information.  " +
                        "Exception Structure cannot be created.");
            }
            
            log.debug("************ payload: "+ payload);
            
            uk.gov.cjse.schemas.messages.deliver.x200605.Exception exception = XMLMarshaller
                    .unmarshallDeliverExceptionXmlToObject(payload);
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
        deliverRequest.setDeliverRequestStructureChoice(rrsc);
        
        // This is left as the default value
        deliverRequest.setAckRequested(false);
        deliverRequest.setMessageFormat(mfs);
        deliverRequest.setMessageMetadata(mms);
        deliverRequest.setRequestingSystem(requestingSystem);
        log.debug("createDeliverRequest :END");
        return deliverRequest;
    }

    /**
     * Method to return an outbound property object details based on the specified property
     * Returns a String with the following format
     * <ClassName>,<MethodName>
     * 
     * @param String propertyName
     * @return String
     */
    public String getOutboundPropertyMapping(String propertyName){
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