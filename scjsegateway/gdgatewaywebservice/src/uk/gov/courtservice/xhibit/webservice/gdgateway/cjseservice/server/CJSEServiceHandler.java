package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.server;

import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.namespace.QName;

import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * This class implements a handler in the handler chain, used to access the SOAP
 * request and response message.
 * <p>
 * This class extends the <code>javax.xml.rpc.handler.GenericHandler</code>
 * abstract classs and simply prints the SOAP request and response messages to
 * the server log file before the messages are processed by the backend Java
 * class that implements the Web Service itself.
 */
public class CJSEServiceHandler extends GenericHandler {
    
    private static final Logger log = CSServices.getLogger(CJSEServiceHandler.class);
    
    private HandlerInfo handlerInfo;

    /**
     * Initializes the instance of the handler. Creates a nonCatalogLogger to
     * log messages to.
     */
    public void init(HandlerInfo hi) {
        log.debug("init");
        handlerInfo = hi;
    }

    /**
     * Specifies that the SOAP request message be logged to a log file before
     * the message is sent to the Java class that implements the Web Service.
     */
    public boolean handleRequest(MessageContext context) 
    {
        if (log.isDebugEnabled()) {            
            logBanner("handleRequest START:");
            logMessageContext(context);
            logBanner("handleRequest END:");
        }
        return true;
    }

    /**
     * Specifies that the SOAP response message be logged to a log file before
     * the message is sent back to the client application that invoked the Web
     * service.
     */
    public boolean handleResponse(MessageContext context) {
        if (log.isDebugEnabled()) {            
            logBanner("handleResponse START:");
            logMessageContext(context);
            logBanner("handleResponse END:");
        }
        return true;
    }

    /**
     * Specifies that a message be logged to the log file if a SOAP fault is
     * thrown by the Handler instance.
     */
    public boolean handleFault(MessageContext context) {
        if (log.isDebugEnabled()) {            
            logBanner("handleFault START:");
            logMessageContext(context);
            logBanner("handleFault END:");
        }
        return true;
    }

    public QName[] getHeaders() {
        return handlerInfo.getHeaders();
    }

    private static String getDocumentAsXml(Document doc) throws TransformerConfigurationException, TransformerException {
        DOMSource domSource = new DOMSource(doc);
        String xmlString = getDocumentAsXml(domSource);
        return xmlString;
    }

    private static String getDocumentAsXml(DOMSource domSource) throws TransformerConfigurationException,
            TransformerException {
        log.debug("getDocumentAsXml - Begin");
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
        // "yes");
        // transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        // transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
        //transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        // we want to pretty format the XML output
        // note : this is broken in jdk1.5 beta!
        //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(domSource, sr);
        return sw.toString();
    }

    private void logMessageContext(MessageContext context) {
        SOAPMessageContext messageContext = (SOAPMessageContext) context;
        SOAPMessage soapMessage = messageContext.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPHeader soapHeader = soapEnvelope.getHeader();

            Iterator bodyIter = soapBody.getChildElements();
            if (bodyIter.hasNext()) {
                Node node = (Node) bodyIter.next();
                DOMSource domSource = new DOMSource(node);
                String xmlBodyString = getDocumentAsXml(domSource);
                log.debug("xmlString SOAPBody = \n\n " + xmlBodyString);
            }

            // soapHeader
            if (soapHeader == null) {
                log.debug("There is no SOAPHeader");
            } else {
                StringBuffer xmlHeaderBuffer = new StringBuffer();
                Iterator headerIter = soapHeader.examineAllHeaderElements();
                while (headerIter.hasNext()) {
                    Node node = (Node) headerIter.next();
                    DOMSource domSource = new DOMSource(node);
                    xmlHeaderBuffer.append(getDocumentAsXml(domSource) + "\n");
                }
                log.debug("xmlString SOAPHeader = \n\n " + xmlHeaderBuffer.toString());
            }

        } catch (TransformerConfigurationException tce) {
            log.debug("handleRequest TransformerConfigurationException " + tce, tce);
        } catch (TransformerException te) {
            log.debug("handleRequest TransformerException " + te, te);
        } catch (SOAPException se) {
            log.debug("handleRequest SOAPException " + se, se);
        } catch (Exception e) {
            log.debug("handleRequest Exception " + e, e);
        }
    }
    
    /**
     * Writes a banner to the log
     * @param bannerText the String to be displayed in the banner.
     */
    private void logBanner(final String bannerText) {
        log.debug("***********************************");
        log.debug(bannerText);
        log.debug("***********************************");
    }

}
