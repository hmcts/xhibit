package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;

/**
 * <p>
 * Title: This class handles the configuration and instantiation of the
 * OneToManyCjseEventMappers
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class parses a portion of the configuration file not dealt with by
 * CjseSwitchHandler, it instantiates a OneToManyCjseEventMapper per Xhibit
 * event in need of mapping.
 * </p>
 * <p>
 * The XML expected consists of a DIFFERENTIATING_FIELD element with an attibute
 * called 'xpath', defining which portion of the incoming event XML to
 * scrutinise. Following this field are arbitrarily many EVENT elements with
 * these attributes:
 * <ul>
 * <li> 'fieldvalue' The value of the differentiating field that matches to the
 * associated 'cjseid' and 'cjsemessagecode' attributes. </li>
 * <li> 'cjseid' The CJSE event type ID that this event should map to for the
 * given value in the differentiating field. </li>
 * <li> 'cjsemessagecode' The messagecode to pass to the MessageFactory to build
 * the human readable portion of the message. </li>
 * </ul>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby, Sandip Sangha, Rakesh Lakhani
 * @version 1.0
 */
public class OneToManyCjseEventMapperHandler extends DefaultHandler implements CjseEventMapperHandler {
    private static final Logger log = CSServices.getLogger(OneToManyCjseEventMapperHandler.class);

    /**
     * This constant defines the element within the incoming event element that
     * specifies the differentiating field.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.cjse.CjseSwitchHandler.XML_ELEMENT_EVENT_CONFIG
     * @see XML_ATT_XPATH
     */
    public static final String XML_ELEMENT_DIFF_FIELD = "DIFFERENTIATING_FIELD";

    /**
     * This constant defines the attribute on the cjse event element that
     * specifies the xpath string to obtain the value to use from the court logs
     * event entry.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageFactory
     * @see XML_ELEMENT_DIFF_FIELD
     */
    public static final String XML_ATT_XPATH = "xpath";

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
     * specifies the field value to use for the message.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageFactory
     * @see XML_ELEMENT_EVENT
     */
    public static final String XML_ATT_FIELDVALUE = "fieldvalue";

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

    // The mapper that we are generating.
    private OneToManyCjseEventMapper _generatedMapper;

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
        if (localName.equalsIgnoreCase(XML_ELEMENT_DIFF_FIELD)) {
            // The element is the differentiating field element - create a
            // mapper
            initialiseEventMapper(atts.getValue("", XML_ATT_XPATH));
        } else if (localName.equalsIgnoreCase(XML_ELEMENT_EVENT)) {
            // The element is the event element - add the event mapping
            addEventMapping(atts.getValue("", XML_ATT_FIELDVALUE), atts.getValue("", XML_ATT_CJSEID), atts.getValue("",
                    XML_ATT_CJSEMESSAGECODE));
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
     * Returns an instance of DefaultCjseEventMapper containing the mapping
     * information.
     * 
     * @return A properly configured CjseEventMapper.
     */
    public CjseEventMapper getMapper() {
        return _generatedMapper;
    }

    /**
     * Reset for reuse.
     */
    public void reset() {
        _generatedMapper = null;
    }

    private void initialiseEventMapper(String xpath) {
        String methodName = "initialiseEventMapper - ";
        log.debug(methodName + "entry :: xpath= " + xpath);

        _generatedMapper = new OneToManyCjseEventMapper(xpath);
    }

    private void addEventMapping(String xpathFieldValue, String cjseEventCode, String cjseMessageCode)
            throws SAXException {
        String methodName = "addEventMapping - ";
        log.debug(methodName + "entry :: xpathFieldValue= " + xpathFieldValue + ", cjseEventCode= " + cjseEventCode
                + ", cjseMessageCode " + cjseMessageCode);

        CjseEventDetail cjseEvtDetail = new CjseEventDetail(cjseEventCode, cjseMessageCode);
        OneToManyCjseEventMapper mapper = (OneToManyCjseEventMapper) getMapper();

        // Check that we are no attempting to process an event mapping before
        // a mapper has been defined
        if (mapper == null) {
            log.error(methodName + "No event mapper defined");
            throw new SAXException("No event mapper defined." + _locator == null ? "" : (" Line number: " + _locator
                    .getLineNumber())
                    + ".");
        }
        mapper.addMapping(xpathFieldValue, cjseEvtDetail);
    }
}
