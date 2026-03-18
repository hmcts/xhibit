package uk.gov.courtservice.xhibit.services.gdgateway.common;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequest;
import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequest;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: XMLMarshaller: Helper methods to serialise between ReceiveRequest,
 * DeliverRequest and Exception java obects and XML
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
 * @version $Id: XMLMarshaller.java
 */
public class XMLMarshaller {

    private static final Logger log = CSServices.getLogger(XMLMarshaller.class);

    private static final String RECEIVE_ERROR = "RECEIVEERROR";

    private static final String DELIVER_ERROR = "DELIVERERROR";

    // WORKAROUND VARIABLES
    // TODO Check this if correct string to replace!
    private static final String STRING_START = "<string xmlns=\"\" xsi:type=\"java:java.lang.String\">";

    private static final String STRING_END = "</string>";

    /**
     * Marshall to ReceiveRequest XML from ReceiveRequest Java object. Default
     * Mapping is used there is no custom mapping files.
     * 
     * @param receiveRequest
     * @return
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static String marshallReceiveObjectToXml(ReceiveRequest receiveRequest) throws IOException,
            ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.info("marshallReceiveObjectToXml: START");

        if (receiveRequest == null) {
            throw new IllegalArgumentException("marshallReceiveObjectToXml - receiveRequest is null");
        }

        StringWriter stringWriter = new StringWriter();

        Marshaller marshaller = new Marshaller(stringWriter);

        marshaller.setNamespaceMapping("mm", "http://schemas.cjse.gov.uk/messages/metadata/2006-05");
        marshaller.setNamespaceMapping("mf", "http://schemas.cjse.gov.uk/messages/format/2006-05");
        marshaller.setNamespaceMapping("msg", "http://schemas.cjse.gov.uk/messages/messaging/2006-05");
        marshaller.setNamespaceMapping("ex", "http://schemas.cjse.gov.uk/messages/exception/2006-06");
        marshaller.setNamespaceMapping("xmime", "http://www.w3.org/2005/05/xmlmime");
        marshaller.setNamespaceMapping("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        marshaller.marshal(receiveRequest);

        log.info("marshallReceiveObjectToXml: END");
        return stringWriter.toString();
    }

    private static Marshaller getCDataMarshaller(String[] elements, Writer writer) throws IOException,IllegalArgumentException {
        log.info("getCDataMarshaller: START");

        if (elements == null || writer == null) {
            throw new IllegalArgumentException("getCDataMarshaller - elements or writer is null");
        }

        // Create output format
        OutputFormat format = new OutputFormat(Method.XML, "UTF-8", true);

        format.setCDataElements(elements);
        format.setNonEscapingElements(elements); // Those elements should NOT
                                                    // be escaped..

        // Create the serializer
        XMLSerializer serializer = new XMLSerializer(writer, format);

        // Create the document handler
        /**
         * Note: The CDATA tag is not added when using the ContentHandler. It
         * could be the version of Castor (1.0) is using an older version of
         * SAX.
         */
        DocumentHandler handler = serializer.asDocumentHandler(); // Deprecated
                                                                    // but
                                                                    // ContentHandler
                                                                    // does not
                                                                    // work
        // ContentHandler handler = serializer.asContentHandler();

        // Create the marshaller
        Marshaller marshaller = new Marshaller(handler);
        log.info("getCDataMarshaller: END");
        return marshaller;

    }

    /**
     * Unmarshall Exception XML to ReceiveRequest Java object. Default Mapping
     * is used there is no custom mapping files.
     * 
     * @param xmlString
     * @return
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static uk.gov.cjse.schemas.messages.receive.x200605.Exception unmarshallExceptionXmlToReceiveException(
            String xmlString) throws IOException, ValidationException, MarshalException, MappingException,
            IllegalArgumentException {
        log.info("unmarshallExceptionXmlToReceiveException: START");

        if (xmlString == null) {
            throw new IllegalArgumentException("unmarshallExceptionXmlToReceiveException - xmlString is null");
        }

        StringReader reader = new StringReader(xmlString);
        Unmarshaller unmarshaller = new Unmarshaller(uk.gov.cjse.schemas.messages.receive.x200605.Exception.class);

        uk.gov.cjse.schemas.messages.receive.x200605.Exception exception = (uk.gov.cjse.schemas.messages.receive.x200605.Exception) unmarshaller
                .unmarshal(reader);

        log.info("unmarshallExceptionXmlToReceiveException: END");
        return exception;
    }

    /**
     * Unmarshall Exception XML to ReceiveRequest Java object. Default Mapping
     * is used there is no custom mapping files.
     * 
     * @param xmlString
     * @return
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static uk.gov.cjse.schemas.messages.deliver.x200605.Exception unmarshallDeliverExceptionXmlToObject(
            String xmlString) throws IOException, ValidationException, MarshalException, MappingException,
            IllegalArgumentException {
        log.info("unmarshallDeliverExceptionXmlToObject: START");

        if (xmlString == null) {
            throw new IllegalArgumentException("unmarshallDeliverExceptionXmlToObject - xmlString is null");
        }

        StringReader reader = new StringReader(xmlString);
        Unmarshaller unmarshaller = new Unmarshaller(uk.gov.cjse.schemas.messages.deliver.x200605.Exception.class);

        uk.gov.cjse.schemas.messages.deliver.x200605.Exception exception = (uk.gov.cjse.schemas.messages.deliver.x200605.Exception) unmarshaller
                .unmarshal(reader);

        log.info("unmarshallDeliverExceptionXmlToObject: END");
        return exception;
    }

    /**
     * Marshall to DeliverRequest XML from DeliverRequest Java object. Default
     * Mapping is used there is no custom mapping files. This method is used by
     * the Inbound Stub processing
     * 
     * @param deliverRequest
     * @return
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static String marshallDeliverObjectToXml(DeliverRequest deliverRequest) throws IOException,
            ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.info("marshallDeliverObjectToXml: START");

        if (deliverRequest == null) {
            throw new IllegalArgumentException("marshallDeliverObjectToXml - deliverRequest is null");
        }

        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = getCDataMarshaller(new String[] { "ex:OriginalExceptionData" }, stringWriter);

        marshaller.setNamespaceMapping("mm", "http://schemas.cjse.gov.uk/messages/metadata/2006-05");
        marshaller.setNamespaceMapping("mf", "http://schemas.cjse.gov.uk/messages/format/2006-05");
        marshaller.setNamespaceMapping("msg", "http://schemas.cjse.gov.uk/messages/messaging/2006-05");
        marshaller.setNamespaceMapping("ex", "http://schemas.cjse.gov.uk/messages/exception/2006-06");
        marshaller.setNamespaceMapping("xmime", "http://www.w3.org/2005/05/xmlmime");
        marshaller.setNamespaceMapping("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        marshaller.marshal(deliverRequest);

        log.info("marshallDeliverObjectToXml: END");
        return stringWriter.toString();
    }

    /**
     * 
     * Unmarshall DeliverRequest XML to DeliverRequest Java object. Default
     * Mapping is used there is no custom mapping files.
     * 
     * @param xmlString
     * @return
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static DeliverRequest unmarshallDeliverXmlToObject(String xmlString) throws IOException,
            ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.info("unmarshallDeliverXmlToObject: START");

        if (xmlString == null) {
            throw new IllegalArgumentException("unmarshallDeliverXmlToObject - xmlString is null");
        }

        StringReader reader = new StringReader(xmlString);
        Unmarshaller unmarshaller = new Unmarshaller(DeliverRequest.class);
        DeliverRequest deliverRequest = (DeliverRequest) unmarshaller.unmarshal(reader);

        log.info("unmarshallDeliverXmlToObject: END");
        return deliverRequest;
    }

    /**
     * Marshall to Exception XML from Exception Java object. Default Mapping is
     * used there is no custom mapping files. There is no namespace set here as
     * this exception structure does not belong to the Deliver/Recieve Structure
     * at this point.
     * 
     * @param exception
     * @return String
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     * @throws MappingException
     */
    public static String marshallExceptionObjectToXml(uk.gov.cjse.schemas.messages.exception.x200606.Exception exception)
            throws IOException, ValidationException, MarshalException, MappingException, IllegalArgumentException {
        log.info("marshallExceptionObjectToXml: START");

        if (exception == null) {
            throw new IllegalArgumentException("marshallExceptionObjectToXml - exception is null");
        }

        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = new Marshaller(stringWriter);
        marshaller.marshal(exception);

        log.info("marshallExceptionObjectToXml: END");
        return stringWriter.toString();
    }

    /**
     * NOTE: This is work around code for Castor Limitations. A raw XML string
     * cannot be set to an anyObject without the addition of extra <String>
     * tags. This method is used to replace strings created as part of the
     * anyObject value(representing an XML<any> type. 1) In the case of a
     * messageType which is not a Deliver or Receive Error, it replaces a
     * temporary string with the payload information.
     * 
     * 2) In the case of messageType of a Receive or Deliver Error, we remove
     * the <String> tags in the <Relates To> element.
     * 
     * @param xmlString
     * @param payload
     * @param messageType
     * @return
     */
    public static String replaceAnyObjectStrings(String xmlString, String payload, String messageType) {
        log.info("replaceAnyObjectStrings: START");
        if (xmlString == null || messageType == null) {
            throw new IllegalArgumentException("replaceAnyObjectStrings - xmlString or messageType is null");
        }

        String replacedXML = null;
        if (DELIVER_ERROR.equals(messageType) || RECEIVE_ERROR.equals(messageType)) {
            replacedXML = xmlString.replace("<ex:RelatesTo>" + STRING_START, "<ex:RelatesTo>");
            replacedXML = replacedXML.replace(STRING_END + "</ex:RelatesTo>", "</ex:RelatesTo>");
        } else {
            replacedXML = xmlString.replace(STRING_START
                    + "uk.gov.courtservice.xhibit.services.outbound.WORKAROUNDFORANYNODE" + STRING_END, payload);
        }

        log.info("replaceAnyObjectStrings: END");
        return replacedXML;
    }
}