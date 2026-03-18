package uk.gov.courtservice.xhibit.courtlog.darts;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
 * Title: Darts Event Populator Map Factory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p/> This code is borrowed from the CJSE Populator code.
 * This class is tasked with returning a properly configured map of
 * CjseEventPopulators keyed to XHIBIT event id. It uses the DartsSwitchHandler
 * to parse an XML file defining the overall mapping logic from XHIBIT Court Log
 * Events to DARTS Events.
 * </p>
 * <p> The DARTS_CONFIG_FILE will contain the list of all mappings that are needed to convert the
 * events to Darts events.
 * </p>
 * <p>
 * This mapping file differs from the cjse mapping file so that DARTS mappings are decoupled from
 * CJSE mappings so that events can be added and removed independantly.
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenuzela
 * @version 1.0
 */
public class DartsEventPopulatorMapFactory {
    private static final Logger log = CSServices.getLogger(DartsEventPopulatorMapFactory.class);

    
    /**
     * This file is used to identify the darts mappings
     * <code>config/xml/cjse_integration/darts_mapping.xml</code>
     */
    public static final String DARTS_MAPPING_CONFIG_FILE = "/config/xml/cjse_integration/darts_mapping.xml";

    /**
     * Gets the map of event populators as defined by the configuration file.
     * 
     * @return the map of event populators keyed to XHIBIT event ID.
     */
    public static Map getEventPopulators() {
       HashMap eventPopulators = new HashMap();

       URL mappingFileURL =DartsEventPopulatorMapFactory.class.getResource(DARTS_MAPPING_CONFIG_FILE);

        // If the file for the filename is absent then this URL will be null - a
        // create exception.
        if (mappingFileURL == null) {
            log.error("Could not locate the mapping configuration file: " +  DARTS_MAPPING_CONFIG_FILE);
            throw new CSUnrecoverableException("Could not locate the mapping configuration file.");
        }

        parseMappingFile(eventPopulators, mappingFileURL);

        return eventPopulators;
    }

    /**
     * Uses a sax parser to parse the defined mappings file and populate the
     * HashMap of event populators.
     * 
     * @param eventPopulators
     *            The HashMap to be populated.
     * @param mappingFileURL
     *            The file URL of the mappings XML file.
     */
    private static void parseMappingFile(HashMap eventPopulators, URL mappingFileURL) {
        // Set up the parser and parse the mapping file.
        // Note that it is generally better to supply an URL as the input source
        // to provide free relative URL (import/include) resolution.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(mappingFileURL.toString()), new DartsSwitchHandler(eventPopulators));
        } catch (ParserConfigurationException ex) {
            log.error("Could not configure parser.", ex);
            throw new CSUnrecoverableException("Could not configure parser.", ex);
        } catch (IOException ex) {
            log.error("Could not read configuration file.", ex);
            throw new CSUnrecoverableException("Could not read configuration file.", ex);
        } catch (SAXException ex) {
            log.error("Problem in parsing configuration file.", ex);
            throw new CSUnrecoverableException("Problem in parsing configuration file.", ex);
        }
    }
}