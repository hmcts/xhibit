package uk.gov.courtservice.xhibit.courtlog.darts;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapperHandler;
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventPopulator;

/**
 * <p>
 * Title: Switching ContentHandler for DARTS event population configuration
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is concerned with controlling the flow of control of creating and
 * configuring the event populators. It handles the initialisation of
 * CjseEventPopulators, the passing of configuration information to the
 * appropriate CjseEventMappingHandlers and the recording of CjseEventPopulators
 * by Xhibit event id in the appropriate map.
 * </p>
 * <p>
 * This code is a replicate of the cjseSwitchHandler and only differs so that DARTS
 * mapping file is able to change independent of the cjse mappings.
 * <p>
 * Company: Loigca
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
public class DartsSwitchHandler extends DefaultHandler {
    private static final int CONF_NOT_STARTED = 0;
    private static final int CONF_STARTED = 1;
    private static final int CONF_FINISHED = 2;

    private HashMap _eventPopulators;
    private CjseEventMapperHandler _currentEventMapperHandler = null;
    private CjseEventPopulator _currentCjsePopulator = null;

    private boolean _hasCurrentEventMapperHandler = false;
    private int _cjseCLConfigurationStatus = CONF_NOT_STARTED;
    private Locator _locator;

    /**
     * This constant defines the root element of an event mapping XML document.
     * <code>DARTS_CL_CONFIGURATION</code>
     */
    public static final String XML_ELEMENT_CONFIG = "DARTS_CL_CONFIGURATION";

    /**
     * This constant defines the element used within the root element to define
     * a mapping of an incoming event. It will have attribute for type, level
     * and Xhibit ID. <code>INCOMING_EVENT</code>
     * 
     * @see XML_ATT_TYPE
     * @see XML_ATT_LEVEL
     * @see XML_ATT_XHIBITID
     */
    public static final String XML_ELEMENT_EVENT_CONFIG = "INCOMING_EVENT";

    /**
     * This constant defines the attribute on the incoming event element that
     * specifies the type of mapping between the incoming event and the output
     * event(s). <code>type</code>
     * 
     * @see XML_ELEMENT_EVENT_CONFIG
     */
    public static final String XML_ATT_TYPE = "type";

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
     * This constant defines the attribute on the incoming event element that
     * specifies that the incoming event will not be sent to the CJSE.
     * <code>donotsendtocjse</code>
     * 
     * @see XML_ELEMENT_EVENT_CONFIG
     */
    public static final String XML_ATT_DONOTSENDTOCJSE = "donotsendtocjse";

        
    
    /**
     * Constructor method that is given the map to populate with
     * CjseEventPopulators on a pass-by-reference basis.
     * 
     * @param eventPopulators
     *            the map to populate with CjseEventPopulators, keyed to Xhibit
     *            event id
     */
    public DartsSwitchHandler(HashMap eventPopulators) {
        if (eventPopulators == null)
            throw new RuntimeException("Cannot pass in a null HashMap for eventPopulators.");
        _eventPopulators = eventPopulators;
    }

    
    
    /**
     * This method is called by to register a CjseEventPopulator for a
     * particular event id.
     * 
     * @param eventID
     *            The Xhibit event ID to register for (only one populator per
     *            event ID).
     * @param populator
     *            The CjseEventPopulator to use for this particular event.
     * @throws SAXException
     *             when there is a duplicate populator for a particular event
     *             id.
     */
    public void registerPopulatorForEventID(Integer eventID, CjseEventPopulator populator) throws SAXException {
        if (_eventPopulators.get(eventID) == null)
            _eventPopulators.put(eventID, populator);
        else
            throw new SAXException("Event number: " + eventID + " already has an event populator configured."
                    + _locator == null ? "" : (" Duplication at line number: " + _locator.getLineNumber() + "."));
    }

    
    /**
     * Method that initialises a populator and sets it to be the current
     * handler.
     * 
     * @param populatorClassName
     *            the class name for the populator.
     * @throws SAXException
     *             when there is a problem in initialisation.
     */
    private void initialisePopulator(String level, String type, String xhibitEventId, String doNotSendToCjse)
            throws SAXException {
        _currentCjsePopulator = new CjseEventPopulator();

        // Fill in level populator.
        _currentCjsePopulator.setLevelPopulator(DartsEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(level));

        // Set up the event mapper handler.
        _currentEventMapperHandler = DartsEventMapperHandlerFactory.getInstance().getCjseEventMapperHandler(type);
        _currentEventMapperHandler.setDocumentLocator(_locator);
        _hasCurrentEventMapperHandler = true;

        if (doNotSendToCjse == null || !doNotSendToCjse.equalsIgnoreCase("true")) {
            registerPopulatorForEventID(Integer.valueOf(xhibitEventId), _currentCjsePopulator);
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
        // All other elements to be passed to the 'current handler'

        // If we've started processing.
        if (this._cjseCLConfigurationStatus == CONF_STARTED) {
            // Check whether we have a current populator handler.
            if (_hasCurrentEventMapperHandler) {
                // delegate handling to it.
                _currentEventMapperHandler.startElement(namespaceURI, localName, qName, atts);
            }
            // Check whether we should be configuring a new event populator.
            else if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT_CONFIG)) {
                initialisePopulator(atts.getValue("", XML_ATT_LEVEL), atts.getValue("", XML_ATT_TYPE), atts.getValue(
                        "", XML_ATT_XHIBITID), atts.getValue("", XML_ATT_DONOTSENDTOCJSE));
            } else {
                // Not an expected element.
                // Throw exception here
                throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                        .getLineNumber())
                        + ".");
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
     * This method is responsible for passing the closing tags of an element and
     * will clear the current CjseEventMapperHandler if it's configuration
     * section is finished.
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
        // CJSE_CL_CONFIGURATION, CJSE_EVENT_POPULATOR_HANDLER
        if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT_CONFIG) && _hasCurrentEventMapperHandler) {
            // Get the event mapper.
            _currentCjsePopulator.setEventMapper(_currentEventMapperHandler.getMapper());

            // facilitate garbage collection.
            _currentEventMapperHandler = null;
            _currentCjsePopulator = null;

            // clear flag.
            _hasCurrentEventMapperHandler = false;
        } else if (_hasCurrentEventMapperHandler) {
            // Pass through.
            _currentEventMapperHandler.endElement(namespaceURI, localName, qName);
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_CONFIG)) {
            // Set configuration status to finished.
            _cjseCLConfigurationStatus = CONF_FINISHED;
        } else {
            // Not an expected element.
            // Throw exception here
            throw new SAXException("Unexpected element" + _locator == null ? "" : (" at line number: " + _locator
                    .getLineNumber())
                    + ".");
        }
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
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @param prefix
     *            The prefix.
     * @param uri
     *            The uri.
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.startPrefixMapping(prefix, uri);
        // else do nothing...
    }

    /**
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @param prefix
     *            The prefix.
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.endPrefixMapping(prefix);
        // else do nothing...
    }

    
    /**
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @param ch
     *            an array of characters that in part will contain the relevant
     *            ones.
     * @param start
     *            where in the array that the relevant characters start.
     * @param length
     *            where in the array that the relevant characters end.
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.characters(ch, start, length);
        // else do nothing...
    }

    /**
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @param ch
     *            an array of characters that in part will contain the relevant
     *            ones.
     * @param start
     *            where in the array that the relevant characters start.
     * @param length
     *            where in the array that the relevant characters end.
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.ignorableWhitespace(ch, start, length);
        // else do nothing...
    }

    /**
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     * @param target
     *            The instruction target.
     * @param data
     *            The instruction data.
     */
    public void processingInstruction(String target, String data) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.processingInstruction(target, data);
        // else do nothing...
    }

    /**
     * Passes through to the current CjseEventMapperHandler if present.
     * 
     * @param name
     *            the name of the skipped entity.
     * @throws org.xml.sax.SAXException
     *             when there is a problem.
     */
    public void skippedEntity(String name) throws SAXException {
        if (_hasCurrentEventMapperHandler)
            _currentEventMapperHandler.skippedEntity(name);
        // else do nothing...
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

    
}