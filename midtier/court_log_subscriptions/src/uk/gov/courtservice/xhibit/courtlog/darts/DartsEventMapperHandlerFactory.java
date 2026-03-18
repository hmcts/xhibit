package uk.gov.courtservice.xhibit.courtlog.darts;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapperHandler;

/**
 * <p>
 * Title: DARTS Event Mapper Handler Factory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Factory class that handles the instantiation of the appropriate type of
 * CjseEventMapperHandler (1-M or 1-1).
 * </p>
 * <p>
 * This class is almost identical the Cjse version except that a separate configuration file is used and therefore
 * the darts configuration is free to change without affect on the cjse version.
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */

public class DartsEventMapperHandlerFactory {
   
    
    /**
     * The component mapper handler properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>cjse.event.mapping</code>
     */
    public static final String PROPERTIES_FILE_NAME = "darts.event.mapping";
    
    /**
     * The prefix to use in defining an event mapping and associated event
     * mapping class.
     * <p>
     * Used as:
     * <code>define.mapping.ONETOMANY=uk.gov.courtservice.xhibit.business.messaging.cjse.eventmapper.OneToManyCjseEventMapperHandler</code>
     * <p>
     * The suffix defines a new mapping type that can be used in the
     * configuration xml file defined by the CjseClHelper.CONFIG_FILE_KEY.
     * <p>
     * <code>define.mapping.</code>
     */
    public static final String EVENT_LEVEL_KEY_PREFIX = "define.mapping.";

    // The currently defined event mapper handlers, for 1-M and 1-1 mapping.
    private HashMap _cjseEventMapperHandlers = new HashMap();

    // The singleton instance that will be used.
    private static DartsEventMapperHandlerFactory _instance = new DartsEventMapperHandlerFactory();

    /**
     * Get an instance of the DartsEventMapperHandlerFactory.
     * 
     * @return an instance of the DartsEventMapperHandlerFactory.
     */
    public static DartsEventMapperHandlerFactory getInstance() {
        return _instance;
    }

    /**
     * Gets a CjseEventMapperHandler for the passed in type. This is most
     * definitely case sensitive.
     * 
     * @param type
     *            the type of mapper to instantiate.
     * @return an instantiated mapper.
     */
    public CjseEventMapperHandler getCjseEventMapperHandler(String type) {
        CjseEventMapperHandler mapper = (CjseEventMapperHandler) _cjseEventMapperHandlers.get(type);
        if (mapper != null) {
            mapper.reset();
            return mapper;
        } else
            throw new CSUnrecoverableException("There is no CjseEventMapperHandler defined for type: " + type);
    }

    /**
     * Private constructor that goes to the configuration file and instantiates
     * the defined DartsEventMapperHandler.
     */
    private DartsEventMapperHandlerFactory() {
        Properties props = CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME);

        // Slightly inefficient way of doing this, but making the assumption
        // that
        // we will have relatively few non level related properties and that
        // this
        // process is only done once at initialisation.
        Enumeration propertyNames = props.propertyNames();
        String propertyName;
        while (propertyNames.hasMoreElements()) {
            propertyName = (String) propertyNames.nextElement();
            if (propertyName.startsWith(EVENT_LEVEL_KEY_PREFIX)) {
                String eventLevel = propertyName.substring(EVENT_LEVEL_KEY_PREFIX.length());
                String handlerClassName = props.getProperty(propertyName);
                _cjseEventMapperHandlers.put(eventLevel, getEventMapperHandlerInstance(handlerClassName));
            }
        }
    }

    /**
     * Utility method to create an instance of the named CjseEventMapperHandler.
     * Currently expects a no-arg constructor.
     * 
     * @param populatorClassName
     *            The classname of an CjseEventMapperHandler.
     * @return an instantiated CjseEventMapperHandler.
     */
    private CjseEventMapperHandler getEventMapperHandlerInstance(String mapperHandlerClassName) {
        // Attempt to instantiate it.
        try {
            Class candidateClass = Class.forName(mapperHandlerClassName);
            if (CjseEventMapperHandler.class.isAssignableFrom(candidateClass)) {
                return (CjseEventMapperHandler) candidateClass.newInstance();
            } else {
                // Not an instance of CjseEventPopulatorHandler so throw an
                // exception.
                throw new CSUnrecoverableException("Class: " + mapperHandlerClassName
                        + " defined as an event level populator is not an instance" + "of CjseEventMapperHandler.");
            }
        } catch (ClassNotFoundException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not find class named: " + mapperHandlerClassName, ex);
        } catch (IllegalAccessException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not access class named: " + mapperHandlerClassName, ex);
        } catch (InstantiationException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not instantiate class named: " + mapperHandlerClassName, ex);
        }

    }
}