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
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventCascadeHandler;

/**
 * <p>
 * Title: DartsEventCascadeFilterFactory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p/> This class is tasked with returning a map of XHIBIT Sub Events keyed to
 * XHIBIT event id. such a mapping (cascade) will exist when a single XHIBIT
 * Event generates multiple simultaneous Darts Events. It uses the
 * <code>DartsEventCascadeHandler</code> to parse an XML file defining the
 * overall mapping logic from XHIBIT Court Log Events to Sub Events.
 * </p>
 * <p> It will attempt to look for a mapping file defined by CONFIG_FILE_KEY in
 * the properties file defined by PROPERTIES_FILE_NAME before defaulting to
 * DEFAULT_CONFIG_FILE. 
 * </p>
 * <p>
 * This class is identical to the CjseEventCascadeFilterFactory, it is defined as a 
 * class so the Darts event cascades can be handled separately from cjse if future
 * changes to the interface are implemented.  The class is created as a singleton so
 * that once tehe cascades have been loaded once they are held in the instance rather 
 * than having to parse the mapping file for each instance.
 * </p>
 * 
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20081010 
 *          
 */

public class DartsEventCascadeFilterFactory {

    private static final Logger log = CSServices.getLogger(DartsEventCascadeFilterFactory.class);

    /**
     * If a configuration file is not defined in the component properties file,
     * then this is the filename that will be used. <p/>
     * <code>config/xml/cjse_integration/cascade.xml</code>
     */
    public static final String DARTS_CONFIG_FILE = "/config/xml/cjse_integration/cascade.xml";


    /**
     * The HashMap containing all the mappings.
     */
    private static HashMap _eventCascades = new HashMap();
    
    /** Singleton instance */
    private static DartsEventCascadeFilterFactory instance;


    
    /**
     * Private constructor to stop instantiation
     */    
    private DartsEventCascadeFilterFactory() {
   
        // Get the URL for the xml configuration/mapping file using the filename
        // from the properties file or the default filename should the property
        // not be defined.
        URL cascadeFileURL = DartsEventCascadeFilterFactory.class.getResource( DARTS_CONFIG_FILE);

        // If the file for the filename is absent then this URL will be null -
        // a create exception.
        if (cascadeFileURL == null) {
            log.error("Could not locate the cascade configuration file: " + DARTS_CONFIG_FILE);
            throw new CSUnrecoverableException("Could not locate the mapping configuration file.");
        }
        parseMappingFile(_eventCascades, cascadeFileURL);
    }

    
    /**
     * Singleton access method
     * 
     * @return Singleton instance
     */
    public static DartsEventCascadeFilterFactory getInstance() {
        if (instance == null) {
            synchronized (DartsEventCascadeFilterFactory.class) {
                if (instance == null) {
                    instance = new DartsEventCascadeFilterFactory();
                }
            }
        }
        return instance;
    }
    
    
    /**
     * Gets the map of sub events as defined by the configuration file.
     * 
     * @return the map of eub events keyed to XHIBIT event ID.
     */
    public Map getEventCascadeFilters() {
        return _eventCascades;
    }

    /**
     * Uses a sax parser to parse the defined mappings file and populate the
     * HashMap of event cascades.
     * 
     * @param eventCascades
     *            The HashMap to be populated.
     * @param mappingFileURL
     *            The file URL of the mappings XML file.
     */
    private static void parseMappingFile(HashMap eventCascades, URL mappingFileURL) {
        // Set up the parser and parse the mapping file.
        // Note that it is generally better to supply an URL as the input source
        // to provide free relative URL (import/include) resolution.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        try {
            SAXParser parser = factory.newSAXParser();
            // CjseEventCascadeHandler used as the DARTS config structure is identical to CJSE
            parser.parse(new InputSource(mappingFileURL.toString()), new CjseEventCascadeHandler(eventCascades));
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