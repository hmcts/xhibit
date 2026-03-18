package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.framework.services.CSServices;


/**
 * <p>
 * Title: DeliverServiceHandler - 
 * </p>
 * <p>
 * Description: Handler to handle SAX Events when parsing XML.
 * Also contains method to resolve any unknown entities.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @version $Id: DeliverServiceHandler.java,v 1.4 2006/10/17 11:59:20 szfnvt Exp $
 */
public class DeliverServiceHandler extends DefaultHandler {

    private static final Logger log = CSServices.getLogger(DeliverServiceHandler.class);

    public DeliverServiceHandler() {
        // Default constructor
    }

    public void log(String o) {
        if (log.isDebugEnabled()) {
            log.debug("************* " + o + " ************");
        }
    }

    public void startDocument() {
        log("Starting the document");
    }

    public void endDocument() {
        log("Document end");
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        log("startElement");
        log("namespaceURI: " + namespaceURI);
        log("localName: " + localName);
        log("qName: " + qName);
        log("atts: " + atts);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        log("endElement");
        log("namespaceURI: " + namespaceURI);
        log("localName: " + localName);
        log("qName: " + qName);
    }

    // Error Handling
    public void warning(SAXParseException saxpe) {
        log.warn("Warning: " + saxpe);
    }

    public void error(SAXParseException saxpe) throws SAXException {
        log.error("Error: " + saxpe);
        throw saxpe;
    }

    public void fatalError(SAXParseException saxpe) throws SAXException {
        log.fatal("Fatal Error: " + saxpe);
        throw saxpe;
    }

    // Find imported XSDs off the class path
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        log("resolveEntity - publicId: " + publicId);
        log("resolveEntity - systemId: " + systemId);
        if (systemId != null) {
            int lastSlash = systemId.lastIndexOf("/");
            String file = systemId.substring(lastSlash + 1, systemId.length());
            InputStream in = getResourceAsStream(file);
            log("inputStream = " + in);
            return new InputSource(in);
        } else {
            return null;
        }
    }

    // Get resource from class path
    protected static InputStream getResourceAsStream(String name) {
        return DeliverServiceHandler.class.getClassLoader().getResourceAsStream(name);
    }

}
