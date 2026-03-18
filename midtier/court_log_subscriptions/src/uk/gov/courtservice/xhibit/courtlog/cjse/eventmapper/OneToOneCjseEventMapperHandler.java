package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;

/**
 * <p>
 * Title: Event mapper handler for one to one and many to one mappings.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class handles the configuration of DefaultCjseEventMappers for both one
 * to one and one to many mappings. It parses the small portion of the mapping
 * XML file that defines the cjse event that is being mapped to. It expects to
 * see only an EVENT element with attributes of 'cjseid' and 'cjsemessagecode'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @see XML_ELEMENT_EVENT
 * @author Bob Boothby
 * @version 1.0
 */
public class OneToOneCjseEventMapperHandler extends DefaultHandler implements CjseEventMapperHandler {

    /**
     * This constant defines the element within the incoming event element that
     * specifies which CJSE event it would map to.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.cjse.CjseSwitchHandler.XML_ELEMENT_EVENT_CONFIG
     * @see XML_ATT_CJSEID
     * @see XML_ATT_CJSEMESSAGECODE
     */
    public static final String XML_ELEMENT_EVENT = "EVENT";

    /**
     * This constant defines the attribute on the cjse event element that
     * specifies the cjse event id to map to.
     * 
     * @see XML_ELEMENT_EVENT
     */
    public static final String XML_ATT_CJSEID = "cjseid";

    /**
     * This constant defines the attribute on the cjse event element that
     * specifies the cjse message code to use for the message.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageFactory
     * @see XML_ELEMENT_EVENT
     */
    public static final String XML_ATT_CJSEMESSAGECODE = "cjsemessagecode";

    // Used to check whether we already have had an event element.
    private boolean _hasEventElement = false;

    // The mapper that we are generating.
    private DefaultCjseEventMapper _generatedMapper;

    // For error reporting.
    private Locator _locator;

    /**
     * Overridden method that looks for the specific element (and tags) that
     * define the cjse event for a one to one or one to many mapping.
     * 
     * @param namespaceURI
     *            The namespace of the element.
     * @param localName
     *            The local name of the element.
     * @param qName
     *            The qualified name of the element.
     * @param atts
     *            The attributes associated with the element.
     * @throws SAXException
     *             When an unexpected element occurs.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT) // The element
                // is the event
                // element
                && // AND
                !_hasEventElement) // There is no other event element
        // already parsed.
        {
            initialiseEventMapper(atts.getValue("", XML_ATT_CJSEID), atts.getValue("", XML_ATT_CJSEMESSAGECODE));
            _hasEventElement = true;
        } else {
            // There's a problem.
            // Not an expected element.
            // Throw exception here
            throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                    .getLineNumber())
                    + ".");
        }
    }

    /**
     * Set the Locator to be used for error/content reporting.
     * 
     * @param locator
     *            the locator to be supplied by the parser.
     */
    public void setDocumentLocator(Locator locator) {
        _locator = locator;
    }

    /**
     * Returns an instance of DefaultCjseEventMapper containing the mapping
     * information.
     * 
     * @return A properly configured CjseEventMapper.
     */
    public CjseEventMapper getMapper() {
        /** @todo if we don't have a mapper throw an exception. */
        return _generatedMapper;
    }

    /**
     * Reset for reuse.
     */
    public void reset() {
        _hasEventElement = false;
        _generatedMapper = null;
    }

    private void initialiseEventMapper(String cjseEventCode, String cjseMessageCode) {
        _generatedMapper = new DefaultCjseEventMapper(new CjseEventDetail(cjseEventCode, cjseMessageCode));
    }
}