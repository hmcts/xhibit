package uk.gov.courtservice.xhibit.courtlog.helpers.event;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CJSE Event Level Factory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is tasked with returning a properly configured map of Cjse Event
 * Levels keyed to XHIBIT event id. It uses the EventLevelHandler to parse an
 * XML file defining the overall mapping logic from XHIBIT Court Log Events to
 * CJSE Court Log Events. Much of the file will be ignored and only the xhibit
 * event id and cjse event level will be extracted.
 * </p>
 * <p>
 * It will attempt to look for a mapping file defined by CONFIG_FILE_KEY in the
 * properties file defined by PROPERTIES_FILE_NAME before defaulting to
 * DEFAULT_CONFIG_FILE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby, Sarah Tong
 * @version $Id: EventLevelFactory.java,v 1.4 2006/06/05 12:28:54 bzjrnl Exp $
 */
public class EventLevelFactory {
    private static final Logger log = CSServices.getLogger(EventLevelFactory.class);

    private static Properties componentProperties;

    /**
     * If a configuration file is not defined in the component properties file,
     * then this is the filename that will be used.
     * <p>
     * <code>config/xml/cjse_integration/mapping.xml</code>
     */
    public static final String DEFAULT_CONFIG_FILE = "config/xml/cjse_integration/mapping.xml";

    /**
     * The properties key that is used to look up the mapping configuration
     * filename from the component properties file.
     * <p>
     * <code>config.file</code>
     * </p>
     */
    public static final String CONFIG_FILE_KEY = "config.file";

    /**
     * The component properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>cjse.event.mapping.properties</code>
     */
    public static final String PROPERTIES_FILE_NAME = "cjse.event";

    private EventLevelFactory() {
        // prevent external instantiation...
    }

    /**
     * Gets the map of event levels as defined by the configuration file.
     * 
     * @return the map of event levels keyed to XHIBIT event ID.
     */
    public static Map getEventLevels() {
        HashMap eventLevels = new HashMap();

        // Get the component properties.
        componentProperties = CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME);

        // Get the URL for the xml configuration/mapping file using the filename
        // from
        // the properties file or the default filename should the property not
        // be defined.
        URL mappingFileURL = EventLevelFactory.class.getResource(componentProperties.getProperty(CONFIG_FILE_KEY,
                DEFAULT_CONFIG_FILE));

        // If the file for the filename is absent then this URL will be null - a
        // create exception.
        if (mappingFileURL == null) {
            log.error("Could not locate the mapping configuration file: "
                    + componentProperties.getProperty(CONFIG_FILE_KEY, DEFAULT_CONFIG_FILE));
            throw new CSUnrecoverableException("Could not locate the mapping configuration file.");
        }

        parseMappingFile(eventLevels, mappingFileURL);

        return eventLevels;
    }

    /**
     * Uses a sax parser to parse the defined mappings file and populate the
     * HashMap of event levels.
     * 
     * @param eventLevels
     *            The HashMap to be populated.
     * @param mappingFileURL
     *            The file URL of the mappings XML file.
     */
    private static void parseMappingFile(HashMap eventLevels, URL mappingFileURL) {
        // Set up the parser and parse the mapping file.
        // Note that it is generally better to supply an URL as the input source
        // to provide free relative URL (import/include) resolution.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(mappingFileURL.toString()), new EventLevelHandler(eventLevels));
        } catch (ParserConfigurationException ex) {
            log.error("Could not configure parser.", ex);
            throw new CSUnrecoverableException("Could not configure parser.");
        } catch (IOException ex) {
            log.error("Could not read configuration file.", ex);
            throw new CSUnrecoverableException("Could not read configuration file.");
        } catch (SAXException ex) {
            log.error("Problem in parsing configuration file.", ex);
            throw new CSUnrecoverableException("Problem in parsing configuration file.");
        }
    }
}
