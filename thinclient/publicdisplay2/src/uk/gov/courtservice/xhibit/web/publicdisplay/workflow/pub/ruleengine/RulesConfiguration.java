package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.exceptions.UnrecognizedEventException;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.exceptions.RulesConfigurationException;

/**
 * <p>
 * Title: Rule Configuration
 * </p>
 * <p>
 * Description: This class Creates conditional documents containing a display
 * document type, the associated rules for a event type.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RulesConfiguration.java,v 1.4 2006/06/05 12:32:37 bzjrnl Exp $
 */

public class RulesConfiguration {
    private static final Logger log = Logger.getLogger(RulesConfiguration.class);

    /**
     * The file for the XML Configuration
     */
    private static final String XML_RULE_FILE = RulesConfiguration.class.getPackage().getName().replace('.', '/')
            + "/RuleMapping.xml";

    /**
     * Map of event types to Conditional Documents
     */
    private EventMappings eventMap;

    /**
     * Empty constructor
     */
    private RulesConfiguration() {
    }

    /**
     * Creates a populated rules configuration
     * 
     * @return
     * @throws RulesConfigurationException
     */
    public static RulesConfiguration newConfiguration() throws RulesConfigurationException {
        RulesConfiguration rc = new RulesConfiguration();
        rc.loadConfiguration();
        return rc;
    }

    /**
     * Returns the conditional documents that can be effected by the passed in
     * event. The conditional documents may not be valid for the generated event
     * and will need to be checked.
     * 
     * @param eventType
     * @return ConditionalDocument array. May be empty but never null.
     */
    public ConditionalDocument[] getConditionalDocumentsForEvent(EventType eventType) throws UnrecognizedEventException {
        try {
            return eventMap.getConditionalDocumentsForEvent(eventType);
        } catch (Exception ex) {
            throw new UnrecognizedEventException(eventType);
        }
    }

    /**
     * Reads the XML and populates the HashMaps
     * 
     * @throws RulesConfigurationException
     */
    public void loadConfiguration() throws RulesConfigurationException {
        SAXParser saxParser = getParser();
        RulesConfigurationHandler handler = new RulesConfigurationHandler();
        try {
            saxParser.getXMLReader().setErrorHandler(handler);
        } catch (SAXException ex) {
            throw new RulesConfigurationException("Error registering Error handler", ex);
        }

        try {
            saxParser.parse(getXmlStream(), handler);
            eventMap = handler.getEventMappings();
        } catch (SAXException ex) {
            throw new RulesConfigurationException("Error parsing document", ex);
        } catch (IOException ex) {
            throw new RulesConfigurationException("Error reading IOStream for " + XML_RULE_FILE, ex);
        }
    }

    /**
     * Get the Rule configuration XML file as an Input Stream
     * 
     * @return
     */
    private InputStream getXmlStream() {
        InputStream xml = getClass().getClassLoader().getResourceAsStream(XML_RULE_FILE);
        return xml;
    }

    /**
     * Get an instance of the SAXParser.
     * 
     * @return
     * @throws RulesConfigurationException
     */
    private SAXParser getParser() throws RulesConfigurationException {
        SAXParser saxParser;
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (FactoryConfigurationError ex) {
            throw new RulesConfigurationException("Error getting parser", ex);
        } catch (SAXException ex) {
            throw new RulesConfigurationException("Error getting parser", ex);
        } catch (ParserConfigurationException ex) {
            throw new RulesConfigurationException("Error getting parser", ex);
        }
        return saxParser;
    }
}