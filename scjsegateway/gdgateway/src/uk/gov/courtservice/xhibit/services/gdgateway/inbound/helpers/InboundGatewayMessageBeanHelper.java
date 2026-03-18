package uk.gov.courtservice.xhibit.services.gdgateway.inbound.helpers;

import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.ACK_REQUESTED;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.CORRELATION_ID;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.CREATION_DATE_TIME;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.DESTINATION_ID;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.EXEC_MODE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.EXPIRY_DATE_TIME;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_IDENTIFIER;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_SCHEMA_IDENTIFIER;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_SCHEMA_NAMESPACE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_SCHEMA_VERSION;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_TYPE_TYPE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.MESSAGE_TYPE_VERSION;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.ORIGINATING_SYSTEM_ENVIRONMENT;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.ORIGINATING_SYSTEM_NAME;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.ORIGINATING_SYSTEM_ORG_UNIT_CODE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.PAYLOAD_TYPE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.REQUESTING_SYSTEM_ENVIRONMENT;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.REQUESTING_SYSTEM_NAME;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.REQUESTING_SYSTEM_ORG_UNIT_CODE;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.REQUEST_ID;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.SOURCE_ID;
import static uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePropertyName.TIMESTAMP;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequest;
import uk.gov.cjse.schemas.messages.exception.x200606.Exception;
import uk.gov.cjse.schemas.messages.exception.x200606.RelatesTo;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.config.ConfigServicesImpl;
import uk.gov.courtservice.xhibit.business.services.exiss.inbound.InboundMessagePayloadType;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.InboundGdgateDatabase;
import uk.gov.courtservice.xhibit.services.gdgateway.common.LogUtility;
import uk.gov.courtservice.xhibit.services.gdgateway.common.XMLMarshaller;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.DeliverServiceHandler;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.InboundGatewayMessageBean;
import uk.gov.courtservice.xhibit.services.scjsegateway.inbound.ScjseInboundGateway;

/**
 * <p>
 * Title: Helperclass for the InboundGatewayMDB that creates the appropriate
 * maps
 * </p>
 * <p>
 * Description: Helperclass receives a call from the InboundGatewayMDB and
 * constructs the appropriate Map<String, String> from the DeliverRequest
 * object it is passed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @ejb.security-identity run-as="XHBInternal"
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/ScjseInboundQueue"
 * 
 * @author Rob Sumner
 * @version $Id: InboundGatewayMessageBeanHelper.java,v 1.2 2006/09/21 13:08:29
 *          szn20z Exp $
 */

public class InboundGatewayMessageBeanHelper {

    private DeliverServiceHandler contentHandler;

    private static final ResourceBundle scjseGDResources = ConfigServicesImpl.getInstance().getBundle(
            "SCJSEGDResources");

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private final TextMessage message;

    private final InboundMessageVO value;

    private static final Logger log = CSServices.getLogger(InboundGatewayMessageBeanHelper.class);

    public InboundGatewayMessageBeanHelper(TextMessage message, InboundMessageVO value) {
        this.message = message;
        this.value = value;
    }

    /**
     * Creates and returns a Map for a Message and it's appropriate properties
     * 
     * @param dr
     * @return Map<String, String>
     * @throws Exception
     */
    private Map<String, String> getMessageMap(DeliverRequest dr) throws java.lang.Exception {
        log.info("getMessageMap(DeliverRequest) started");
        Map<String, String> properties = populateGenericMessageProperties();

        properties.put(ACK_REQUESTED.toString(), new Boolean(dr.getAckRequested()).toString());
        properties.put(MESSAGE_IDENTIFIER.toString(), dr.getMessageIdentifier());
        properties.put(REQUESTING_SYSTEM_ENVIRONMENT.toString(), dr.getRequestingSystem().getEnvironment());
        properties.put(REQUESTING_SYSTEM_NAME.toString(), dr.getRequestingSystem().getName());
        properties.put(REQUESTING_SYSTEM_ORG_UNIT_CODE.toString(), dr.getRequestingSystem().getOrgUnitCode());
        properties.put(CREATION_DATE_TIME.toString(), dr.getMessageMetadata().getCreationDateTime().toString());

        if (dr.getMessageMetadata().getExpiryDateTime() != null) {
            properties.put(EXPIRY_DATE_TIME.toString(), dr.getMessageMetadata().getExpiryDateTime().toString());
        }

        properties.put(ORIGINATING_SYSTEM_NAME.toString(), dr.getMessageMetadata().getOriginatingSystem().getName());
        properties.put(ORIGINATING_SYSTEM_ORG_UNIT_CODE.toString(), dr.getMessageMetadata().getOriginatingSystem()
                .getOrgUnitCode());
        properties.put(ORIGINATING_SYSTEM_ENVIRONMENT.toString(), dr.getMessageMetadata().getOriginatingSystem()
                .getEnvironment());
        properties.put(MESSAGE_SCHEMA_IDENTIFIER.toString(), dr.getMessageFormat().getMessageSchema()
                .getMessageSchemaStructureChoice().getIdentifier());

        if (dr.getMessageFormat().getMessageSchema().getMessageSchemaStructureChoice()
                .getMessageSchemaStructureChoiceSequence().getNamespace() != null) {
            properties.put(MESSAGE_SCHEMA_NAMESPACE.toString(), dr.getMessageFormat().getMessageSchema()
                    .getMessageSchemaStructureChoice().getMessageSchemaStructureChoiceSequence().getNamespace());
        }

        if (dr.getMessageFormat().getMessageSchema().getMessageSchemaStructureChoice()
                .getMessageSchemaStructureChoiceSequence().getVersion() != null) {
            properties.put(MESSAGE_SCHEMA_VERSION.toString(), dr.getMessageFormat().getMessageSchema()
                    .getMessageSchemaStructureChoice().getMessageSchemaStructureChoiceSequence().getVersion());
        }

        properties.put(MESSAGE_TYPE_TYPE.toString(), dr.getMessageFormat().getMessageType().getType());
        properties.put(MESSAGE_TYPE_VERSION.toString(), dr.getMessageFormat().getMessageType().getVersion());

        properties.put(PAYLOAD_TYPE.toString(), InboundMessagePayloadType.MESSAGE.toString());

        log.info("getMessageMap(DeliverRequest) completed");
        return properties;
    }

    /**
     * Creates and returns a Map for an Exception and it's appropriate
     * properties
     * 
     * @param dr
     * @return Map<String, String>
     * @throws Exception
     */
    private Map<String, String> getExceptionMap(DeliverRequest dr) throws java.lang.Exception {
        log.info("getExceptionMap(DeliverRequest) started");
        Map<String, String> properties = populateGenericMessageProperties();

        if (new Boolean(dr.getAckRequested()).toString() != null) {
            properties.put(ACK_REQUESTED.toString(), new Boolean(dr.getAckRequested()).toString());
        }

        properties.put(MESSAGE_IDENTIFIER.toString(), dr.getMessageIdentifier());
        properties.put(REQUESTING_SYSTEM_ENVIRONMENT.toString(), dr.getRequestingSystem().getEnvironment());
        properties.put(REQUESTING_SYSTEM_NAME.toString(), dr.getRequestingSystem().getName());
        properties.put(REQUESTING_SYSTEM_ORG_UNIT_CODE.toString(), dr.getRequestingSystem().getOrgUnitCode());
        properties.put(CREATION_DATE_TIME.toString(), dr.getMessageMetadata().getCreationDateTime().toString());

        if (dr.getMessageMetadata().getExpiryDateTime() != null) {
            properties.put(EXPIRY_DATE_TIME.toString(), dr.getMessageMetadata().getExpiryDateTime().toString());
        }

        properties.put(ORIGINATING_SYSTEM_NAME.toString(), dr.getMessageMetadata().getOriginatingSystem().getName());
        properties.put(ORIGINATING_SYSTEM_ORG_UNIT_CODE.toString(), dr.getMessageMetadata().getOriginatingSystem()
                .getOrgUnitCode());
        properties.put(ORIGINATING_SYSTEM_ENVIRONMENT.toString(), dr.getMessageMetadata().getOriginatingSystem()
                .getEnvironment());
        properties.put(MESSAGE_SCHEMA_IDENTIFIER.toString(), dr.getMessageFormat().getMessageSchema()
                .getMessageSchemaStructureChoice().getIdentifier());

        if (dr.getMessageFormat().getMessageSchema().getMessageSchemaStructureChoice()
                .getMessageSchemaStructureChoiceSequence().getNamespace() != null) {
            properties.put(MESSAGE_SCHEMA_NAMESPACE.toString(), dr.getMessageFormat().getMessageSchema()
                    .getMessageSchemaStructureChoice().getMessageSchemaStructureChoiceSequence().getNamespace());
        }

        if (dr.getMessageFormat().getMessageSchema().getMessageSchemaStructureChoice()
                .getMessageSchemaStructureChoiceSequence().getVersion() != null) {
            properties.put(MESSAGE_SCHEMA_VERSION.toString(), dr.getMessageFormat().getMessageSchema()
                    .getMessageSchemaStructureChoice().getMessageSchemaStructureChoiceSequence().getVersion());
        }

        properties.put(MESSAGE_TYPE_TYPE.toString(), dr.getMessageFormat().getMessageType().getType());
        properties.put(MESSAGE_TYPE_VERSION.toString(), dr.getMessageFormat().getMessageType().getVersion());

        if (dr.getDeliverRequestStructureChoice().getException() != null) {
            if (dr.getDeliverRequestStructureChoice().getException().getRelatesTo() != null) {
                //Get hold of correlation ID
                String correlationStr = getCorrelationID(dr.getDeliverRequestStructureChoice().getException()
                        .getRelatesTo().getAnyObject(0).toString());
                if (correlationStr != null && !"".equals(correlationStr) && isRelatesToLong(correlationStr)) {
                    //Set Correlation ID to properties if valid number
                    properties.put(CORRELATION_ID.toString(), correlationStr);
                } else {
                    log.info("No value contained for correlation ID in Relates To for Exception");
                }

            }
        }
        properties.put(PAYLOAD_TYPE.toString(), InboundMessagePayloadType.EXCEPTION.toString());

        log.info("getExceptionMap(DeliverRequest) completed");
        return properties;
    }

    private boolean isRelatesToLong(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Value contained for correlation ID in Relates To is not a number: " + value);
            return false;
        }
        return true;
    }

    //Method to retrive Correlation ID from MessageIdentifier tags (if exists)
    private String getCorrelationID(String param) {
        int end = param.indexOf("</MessageIdentifier>");
        if (end > 0) {
            int start = param.lastIndexOf(">", end);
            if (start > 0) {
                return param.substring(start + 1, end);
            }
        }
        return null;
    }

    /**
     * Creates and returns a Map for a DeliverError and it's appropriate
     * properties
     * 
     * @return Map<String, String>
     * @throws Exception
     */
    private Map<String, String> getDeliverErrorMap() throws java.lang.Exception {
        log.info("getDeliverErrorMap() started");
        Map<String, String> properties = populateGenericMessageProperties();

        properties.put(PAYLOAD_TYPE.toString(), InboundMessagePayloadType.DELIVERERROR.toString());

        log.info("getDeliverErrorMap() completed");
        return properties;
    }

    /**
     * Convenience method to populate generic message properties
     * @return
     * @throws java.lang.Exception
     */
    private Map<String, String> populateGenericMessageProperties() throws java.lang.Exception {
        Map<String, String> properties = new HashMap<String, String>();

        properties.put(REQUEST_ID.toString(), value.getRequestIdentifier());
        properties.put(SOURCE_ID.toString(), value.getSourceIdentifier());
        properties.put(DESTINATION_ID.toString(), value.getDestinationIdentifer());
        properties.put(EXEC_MODE.toString(), value.getExecMode());
        properties.put(TIMESTAMP.toString(), new Long(message.getJMSTimestamp()).toString());

        return properties;
    }

    /**
     * Invokes the schema validation process
     * @param xmlString
     * @throws ClassNotFoundException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public void validateXMLAgainstSchema(String xmlString) throws ClassNotFoundException, SAXException, IOException,
            ParserConfigurationException {
        log.info("validateXMLAgainstSchema(String) started");
        InputStream deliverStream = getResourceAsStream(scjseGDResources.getString("SCHEMA_FILE_NAME"));

        if (deliverStream != null) {
            out("Deliver XSD Stream NOT NULL");
            validate(xmlString, deliverStream);
        } else {
            out("Deliver XSD Stream is NULL");
        }
        log.info("validateXMLAgainstSchema(String) completed");
    }

    /**
     * Processes a payload that has failed schema validation.  An Exception structure is generated
     * indicating the error and this is sent to the the ScjseInboundQueue.
     * @param gateway
     * @param e
     * @param database
     */
    public void createAndSendExceptionMessage(ScjseInboundGateway gateway, java.lang.Exception e,
            InboundGdgateDatabase database) throws java.lang.Exception {
        log.info("createAndSendExceptionMessage(ScjseInboundGateway, Exception,  InboundGdgateDatabase) started");
        String exception_string = processXMLValidationFail(e, database);
        // send 'Deliver Error message'
        try {
            if (exception_string != null && exception_string.length() > 0) {
                out("about to send DeliverError message");
                gateway.sendMessage(getDeliverErrorMap(), exception_string);
                out("just sent DeliverError message");
            }
        } catch (java.lang.Exception ex) {
            log.error("createAndSendExceptionMessage failed - cannnot send exception",ex);
            throw ex;
        }
        log.info("createAndSendExceptionMessage(ScjseInboundGateway, Exception,  InboundGdgateDatabase) completed");
    }

    /**
     * Sends a message to the ScjseInboundQueue.  The type of message is either Message or Exception.
     * @param xmlString
     * @param gateway
     * @throws java.lang.Exception
     */
    public void sendMessageToGateway(String xmlString, ScjseInboundGateway gateway) throws java.lang.Exception {
        log.info("sendMessageToGateway(String, ScjseInboundGateway) started");
        try {
            DeliverRequest deliverRequest = XMLMarshaller.unmarshallDeliverXmlToObject(xmlString);
            LogUtility.logDeliverRequest(deliverRequest);
            if (deliverRequest.getDeliverRequestStructureChoice().getMessage() != null) {
                // call mapMessage
                // if message create map and send
                out("about to send MessageStructure message");
                gateway.sendMessage(getMessageMap(deliverRequest), deliverRequest.getDeliverRequestStructureChoice()
                        .getMessage().getAnyObject().toString());
                out("just sent MessageStructure message");
            } else if (deliverRequest.getDeliverRequestStructureChoice().getException() != null) {
                // call mapException
                out("about to send ExceptionStructure message");                
                gateway.sendMessage(getExceptionMap(deliverRequest), xmlString);
                out("just sent ExceptionStructure message");
            } else {
                // shouldn't get here, error!
                log.error("deliverRequest has no message or exception message");
            }
        } catch (IOException ioe) {
            log.error("sendMessageToGateway - IOException in unmarshallDeliverXmlToObject",ioe);
            throw ioe;
        } catch (ValidationException ve) {
            log.error("sendMessageToGateway - ValidationException in unmarshallDeliverXmlToObject",ve);
            throw ve;
        } catch (MarshalException mare) {
            log.error("sendMessageToGateway - MarshalException in unmarshallDeliverXmlToObject",mare);
            throw mare;
        } catch (MappingException mape) {
            log.error("sendMessageToGateway - MappingException in unmarshallDeliverXmlToObject",mape);
            throw mape;
        } finally {
            // Do nothing
        }
        log.info("sendMessageToGateway(String, ScjseInboundGateway) completed");
    }

    /**
     * Increments the mal-formed XML count for the day and returns a marshalled/serialized
     * version of the Exception object
     */
    private String processXMLValidationFail(java.lang.Exception e, InboundGdgateDatabase database)
        throws java.lang.Exception      
    {
        String returnString = "";
        try {
            String newCount = database.incrementMalformedXMLCount();
            log.info("new malformedXMLCount <" + newCount + ">");

            returnString = createExceptionString(e);
        } catch (SQLException sqle) {
            // their have been too many exceptions today so do nothing
            log.error("not sent to midtier as malformed message limit has been exceeded for today", sqle);
        }
        return returnString;
    }

    /**
     * Obtains a marshalled/serialized version of the Exception object and returns it 
     * @param e
     * @return
     */
    private String createExceptionString(java.lang.Exception e) throws java.lang.Exception {
        // validation failed
        // must create an exception object that will get sent back to sender
        String exception_string = "";
        uk.gov.cjse.schemas.messages.exception.x200606.Exception error_exception;
        error_exception = createDeliverErrorException(e);
        try {
            exception_string = XMLMarshaller.marshallExceptionObjectToXml(error_exception);
        } catch (IOException ioe) {
            log.error("createExceptionString - IOException in marshallExceptionObjectToXml", ioe);
            throw ioe;
        } catch (ValidationException vale) {
            log.error("createExceptionString - ValidationException in marshallExceptionObjectToXml", vale);
            throw vale;
        } catch (MarshalException mare) {
            log.error("createExceptionString - MarshalException in marshallExceptionObjectToXml", mare);
            throw mare;
        } catch (MappingException mape) {
            log.error("createExceptionString - MappingException in marshallExceptionObjectToXml", mape);
            throw mape;
        } finally {
            // Do nothing
        }
        return exception_string;
    }

    /**
     * Populates an Exception object containing the details of the exception. 
     */
    public Exception createDeliverErrorException(java.lang.Exception e) {
        log.info("createDeliverErrorException(java.lang.Exception) started");
        Exception retException = new Exception();
        retException.setDetail(e.getMessage());
        retException.setName(scjseGDResources.getString("DELIVER_OI_VALIDATION_ERROR"));
        retException.setAnyObject(new Object[] {});
        retException.setCode(scjseGDResources.getString("VALIDATION_ERROR_CODE"));

        retException.setInnerException(null);
        retException.setOriginalExceptionData(value.getClobData());

        RelatesTo relatesTo = new RelatesTo();
        relatesTo.setAnyObject(new String[] { "<MessageIdentifier>" + value.getRequestIdentifier()
                + "</MessageIdentifier>" });
        retException.setRelatesTo(relatesTo);

        log.info("createDeliverErrorException(java.lang.Exception) completed");
        return retException;
    }

    /**
     * Validaets the re-constituted XML sctring against the DeliverService schema
     * @param xmlString
     * @param xsdStream
     * @throws SAXException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ParserConfigurationException
     */
    private void validate(String xmlString, InputStream xsdStream) throws SAXException, IOException,
            ClassNotFoundException, ParserConfigurationException {
        out("validate: START");

        // Create SAXParserFactory
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(true);
        saxFactory.setNamespaceAware(true);

        // Create SAXParser
        SAXParser parser = saxFactory.newSAXParser();
        parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

        // Set Schema to validate against
        parser.setProperty(SCHEMA_SOURCE, xsdStream);

        StringReader sr = new StringReader(xmlString);
        InputSource is = new InputSource(sr);

        parser.parse(is, getHandler());
        out("validate: END");

    }

    /**
     * Returns a handler for the DeliverService structure
     * @return
     * @throws SAXException
     */
    private DeliverServiceHandler getHandler() throws SAXException {
        out("DeliverServiceHandler:START");
        if (contentHandler == null) {
            contentHandler = new DeliverServiceHandler();
        }
        out("getContentHandler:END");
        return contentHandler;
    }

    /**
     * Returns the file identified by the passed-in parameter as an InputStream
     * @param name
     * @return
     * @throws java.lang.ClassNotFoundException
     */
    private static InputStream getResourceAsStream(String name) throws java.lang.ClassNotFoundException {
        return InboundGatewayMessageBean.class.getClassLoader().getResourceAsStream(name);
    }

    /**
     * Logging utility
     * @param in
     */
    private static void out(String in) {
        log.debug(in + "\n\n");
    }
}
