package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: One To Many CJSE Event Mapper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class has the responsibility for evaluating a field from an incoming XML
 * Court Log Event and returning details of the CJSE message that this field's
 * value indicates. Primarily intended to be used in the one (court log event)
 * to many (cjse event) mappings. The intent is that there will be one
 * OneToManyCjseEventMapper instance per incoming court log event that needs to
 * be mapped.
 * </p>
 * <p>
 * Instantiation of this class is expected to be handled by the
 * OneToManyCjseEventMapperHandler.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 *
 * @author Bob Boothby
 * @version 1.0
 * @see OneToManyCjseEventMapperHandler
 */
public class OneToManyCjseEventMapper implements CjseEventMapper {
    // Get a logger for this class
    private static final Logger log = CSServices.getLogger(OneToManyCjseEventMapper.class);

    // Default mapping key
    private static final String DEFAULT_MAPPING_KEY = "*";

    // The field that this differentiator will be looking at.
    private String _xpath;

    // This map is keyed to the potential values that the field could have
    // and the values are instances of CjseEventDetail containing the
    // CJSE event id and the code representing the CJSE event message.
    private HashMap _eventInfo = new HashMap();

    /**
     * This class should always be constructed with a valid xpath.
     *
     * @param xpath
     *            the xpath that this class will use to retrieve the field value
     *            from the incoming XML event that will be used to look up the
     *            output event details.
     */
    public OneToManyCjseEventMapper(String xpath) {
        _xpath = xpath;
    }

    /**
     * This method is used to add valid mappings, taking a potential value for
     * the field that this differentiator will be looking at and the appropriate
     * CjseEventDetail.
     *
     * @param xpathFieldValue
     *            The value of the field for which the CjseMessageDetails
     *            applies.
     * @param cjseEventDetail
     *            The CjseEventDetail which applies for the given
     *            xpathFieldValue.
     */
    public void addMapping(String xpathFieldValue, CjseEventDetail cjseEventDetail) {
        _eventInfo.put(xpathFieldValue, cjseEventDetail);
    }

    /**
     * The method to be used at runtime that takes an incoming piece of XML from
     * an event and returns the appropriate message detail for it.
     *
     * @param event
     *            the incoming event to be evaluated
     * @return The CjseEventDetail for the output event or null if this event
     *         does not go to CJSE.
     */
    public CjseEventDetail getCjseEventDetail(CourtLogSubscriptionValue event) {
        String eventXML = event.getCourtLogViewValue().getLogEntry();
        try {
            // First turn the string XML into a DOM.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream in = new ByteArrayInputStream(eventXML.getBytes("UTF-8"));
            Document eventDocument = builder.parse(in);

            // Then using the XPathAPI to get the value of the node.
            Node valueNode = XPathAPI.selectSingleNode(eventDocument, _xpath);

            if (valueNode != null) // Is there a node for the XPath?
            {
                Object returnValue = _eventInfo.get(valueNode.getNodeValue());

                if (returnValue != null) // have we got a mapping for
                // this event?
                {
                    return (CjseEventDetail) returnValue;
                } else {
                    Object defaultRetValue = _eventInfo.get(DEFAULT_MAPPING_KEY);

                    if (defaultRetValue != null) // have we got a default
                    // mapping for this
                    // event?
                    {
                        return (CjseEventDetail) defaultRetValue;
                    } else {
                        // this is actually fine, just means the event doesn't
                        // get sent to the CJSE i.e. not every value of the
                        // xpath generates an event
                        log.warn("No CJSE Event is defined for the value: " + valueNode.getNodeValue()
                                + " generated by the xpath: " + _xpath + " for the XML: " + eventXML);
                        return null;
                    }
                }
            } else {
                // this should only ever happen where an xhibit event can
                // generate multiple cjse events, but not all events will be
                // generated each time i.e not every xpath will be present in
                // the xml from xhibit
                log.warn("Could not get a node for the XPath: " + _xpath + " for the xml: " + eventXML);
                return null;
            }
        } catch (ParserConfigurationException ex) {
            throw new CSUnrecoverableException("Problem in parser configuration.", ex);
        } catch (FactoryConfigurationError ex) {
            throw new CSUnrecoverableException("Problem in factory configuration.", ex);
        } catch (IOException ex) {
            throw new CSUnrecoverableException("Error reading input XML: " + eventXML, ex);
        } catch (SAXException ex) {
            throw new CSUnrecoverableException("Error parsing input XML: " + eventXML, ex);
        } catch (TransformerException ex) {
            throw new CSUnrecoverableException("Problem in reading XPath: " + _xpath + " in XML: " + eventXML, ex);
        }
    }
}