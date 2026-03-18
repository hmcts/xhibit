package uk.gov.courtservice.xhibit.courtlog.cjse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * Title: CjseEventCascadeHandler
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class parses the cascade configuration file and creates an Array of sub
 * event ids for each event present in the file.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: CjseEventCascadeHandler.java,v 1.2 2004/05/07 12:59:13 tz0d5m
 *          Exp $
 */
public class CjseEventCascadeHandler extends DefaultHandler {
    private static final int CASCADE_NOT_STARTED = 0;

    private static final int CASCADE_STARTED = 1;

    private static final int CASCADE_FINISHED = 2;

    private int _cjseCascadeConfigStatus = CASCADE_NOT_STARTED;

    private boolean _hasCurrentCascadeList = false;

    /**
     * This constant defines the root element of an event cascade XML document.
     * <code>CJSE_CASCADE_CONFIGURATION</code>
     */
    public static final String XML_ELEMENT_CONFIG = "CJSE_CASCADE_CONFIGURATION";

    /**
     * This constant defines the element used within the root element to define
     * a cascade of an incoming event. It will have attribute for xhibit id.
     * <code>INCOMING_EVENT</code>
     */
    public static final String XML_ELEMENT_EVENT_CONFIG = "INCOMING_EVENT";

    /**
     * This constant defines the attribute on the incoming event element that
     * specifies the Xhibit event id of the incoming event on which the cascade
     * will occur. <code>xhibitid</code>
     * 
     * @see XML_ELEMENT_EVENT_CONFIG
     */
    public static final String XML_ATT_XHIBITID = "xhibitid";

    /**
     * This constant defines the element within the incoming event element that
     * specifies a sub event of the Xhibit event. It will have an attribute for
     * sub xhibit id.
     */
    public static final String XML_ELEMENT_SUBEVENT = "SUBEVENT";

    /**
     * This constant defines the attribute on the sub event element that defines
     * a sub event for this xhibit event.
     * 
     * @see XML_ELEMENT_SUBEVENT
     */
    public static final String XML_ATT_SUBEVENTID = "subxhibitid";

    // The event cascade list.
    private HashMap _eventCascades;

    private Integer _xhibitId;

    private List _subXhibitIds;

    // For error reporting.
    private Locator _locator;

    /**
     * Constructor method that is given the map to populate with
     * CjseEventPopulators on a pass-by-reference basis.
     * 
     * @param eventPopulators
     *            the map to populate with CjseEventPopulators, keyed to Xhibit
     *            event id
     */
    public CjseEventCascadeHandler(HashMap eventCascades) {
        if (eventCascades == null)
            throw new RuntimeException("Cannot pass in a null List.");
        _eventCascades = eventCascades;
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
        if (_cjseCascadeConfigStatus != CASCADE_FINISHED) {
            throw new SAXException("Cascade file not properly terminated by closing tag.");
        }
    }

    /**
     * Overridden method that looks for the specific element (and tags) that
     * define the xhibit event cascade.
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
        if (_cjseCascadeConfigStatus == CASCADE_STARTED) {
            if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT_CONFIG)) {
                // store the xhibit id and reset the sub id list
                _xhibitId = new Integer(atts.getValue("", XML_ATT_XHIBITID));
                _subXhibitIds = new ArrayList();
                _hasCurrentCascadeList = true;
            } else if (localName.equalsIgnoreCase(XML_ELEMENT_SUBEVENT)) {
                // The element is the sub event element - add to the cascade
                _subXhibitIds.add(new Integer(atts.getValue("", XML_ATT_SUBEVENTID)));
            } else {
                // There's a problem.
                // Not an expected element.
                // Throw exception here
                throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                        .getLineNumber())
                        + ".");
            }

        }
        // else see if we are just starting the configuration file.
        else if (localName.equalsIgnoreCase(XML_ELEMENT_CONFIG) && _cjseCascadeConfigStatus == CASCADE_NOT_STARTED) {
            // set status accordingly.
            _cjseCascadeConfigStatus = CASCADE_STARTED;
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
     * This method is responsible for parsing the closing tags of an element and
     * will clear the current xhibit cascade if it's configuration section is
     * finished.
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
        // XML_ELEMENT_EVENT_CONFIG, XML_ELEMENT_CONFIG
        if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT_CONFIG) && _hasCurrentCascadeList) {
            // add to the map
            _eventCascades.put(_xhibitId, _subXhibitIds.toArray(new Integer[] {}));

            // facilitate garbage collection.
            _xhibitId = null;
            _subXhibitIds = null;

            // clear flag.
            _hasCurrentCascadeList = false;
        } else if (_hasCurrentCascadeList) {
            // continue processing sub ids
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_CONFIG)) {
            // Set configuration status to finished.
            _cjseCascadeConfigStatus = CASCADE_FINISHED;
        } else {
            // Not an expected element.
            // Throw exception here
            throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                    .getLineNumber())
                    + ".");
        }
    }
}