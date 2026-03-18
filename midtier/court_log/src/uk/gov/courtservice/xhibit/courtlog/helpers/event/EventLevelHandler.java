package uk.gov.courtservice.xhibit.courtlog.helpers.event;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * Title: ContentHandler for CJSE event level configuration
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is a cut down version of the one used in the mid tier CJSE work.
 * It is only concerned with the xhibit_id and the level at present.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby, Rakesh Lakhani
 * @version $Id: EventLevelHandler.java,v 1.3 2006/06/05 12:28:54 bzjrnl Exp $
 */
public class EventLevelHandler extends DefaultHandler {
    private static final int CONF_NOT_STARTED = 0;

    private static final int CONF_STARTED = 1;

    private static final int CONF_FINISHED = 2;

    private HashMap _eventLevels;

    private int _cjseCLConfigurationStatus = CONF_NOT_STARTED;

    private Locator _locator;

    /**
     * This constant defines the root element of an event mapping XML document.
     * <code>CJSE_CL_CONFIGURATION</code>
     */
    public static final String XML_ELEMENT_CONFIG = "CJSE_CL_CONFIGURATION";

    /**
     * This constant defines the element used within the root element to define
     * a mapping of an incoming event. It will have attribute for type, level
     * and Xhibit ID. <code>INCOMING_EVENT</code>
     * 
     * @see XML_ATT_LEVEL
     * @see XML_ATT_XHIBITID
     */
    public static final String XML_ELEMENT_EVENT_CONFIG = "INCOMING_EVENT";

    /**
     * This constant defines the attribute on the incoming event element that
     * specifies the event level of the events concerned. <code>level</code>
     * 
     * @see XML_ELEMENT_EVENT_CONFIG
     */
    public static final String XML_ATT_LEVEL = "level";

    /**
     * This constant defines the attribute on the incoming event element that
     * specifies the Xhibit event id of the incoming event on which the mapping
     * will occur. <code>xhibitid</code>
     * 
     * @see XML_ELEMENT_EVENT_CONFIG
     */
    public static final String XML_ATT_XHIBITID = "xhibitid";

    /**
     * Constructor method that is given the map to populate with
     * CjseEventPopulators on a pass-by-reference basis.
     * 
     * @param eventPopulators
     *            the map to populate with CjseEventPopulators, keyed to Xhibit
     *            event id
     */
    public EventLevelHandler(HashMap eventLevels) {
        if (eventLevels == null)
            throw new RuntimeException("Cannot pass in a null HashMap.");
        _eventLevels = eventLevels;
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
     * Check that we have completed validation.
     * 
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void endDocument() throws SAXException {
        if (_cjseCLConfigurationStatus != CONF_FINISHED) {
            throw new SAXException("Configuration file not properly terminated by closing tag.");
        }
    }

    /**
     * This method is used to parse all start elements and is responsible for
     * instantiating CjseEventPopulator instances when required. If there is a
     * current CjseEventMapperHandler then the method will delegate handling to
     * it.
     * 
     * @param namespaceURI
     *            The namespace URI of the element.
     * @param localName
     *            The local name of the element.
     * @param qName
     *            The raw name of the element.
     * @param atts
     *            The attributes associated with the element.
     * @throws SAXException
     *             When there is a problem.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        // Elements to be given special handling...
        // XML_CONFIG, XML_EVENT_CONFIG
        // All other elements to be ignored

        // If we've started processing.
        if (this._cjseCLConfigurationStatus == CONF_STARTED) {
            if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT_CONFIG)) {
                // Add the item to the Map
                _eventLevels.put(atts.getValue("", XML_ATT_XHIBITID), atts.getValue("", XML_ATT_LEVEL));
            }
        }
        // else see if we are just starting the configuration file.
        else if (localName.equalsIgnoreCase(XML_ELEMENT_CONFIG) && _cjseCLConfigurationStatus == CONF_NOT_STARTED) {
            // set status accordingly.
            this._cjseCLConfigurationStatus = CONF_STARTED;
        } else {
            // Not an expected element.
            // Throw exception here
            throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                    .getLineNumber())
                    + ".");
        }
    }

    /**
     * This method is responsible for passing the closing tags of an element
     * 
     * @param namespaceURI
     *            The namespace URI of the element.
     * @param localName
     *            The local name of the element.
     * @param qName
     *            The raw name of the element.
     * @throws SAXException
     *             When there is a problem.
     */
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        // Elements to be given special handling...
        // CJSE_CL_CONFIGURATION
        if (localName.equalsIgnoreCase(XML_ELEMENT_CONFIG)) {
            // Set configuration status to finished.
            _cjseCLConfigurationStatus = CONF_FINISHED;
        }
    }
}