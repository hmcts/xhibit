package uk.gov.courtservice.xhibit.client.im.util.sorters;

// jdk
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CJSE Event Mapper Handler Factory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Factory class that handles the instantiation of the appropriate type of
 * DestinationSorter.
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
 */

public class DestinationSorterFactory {
    private static final Logger log = CSServices.getLogger(DestinationSorterFactory.class);

    /**
     * The destination sorter properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>cjse.event.mapping.properties</code>
     */
    public static final String PROPERTIES_FILE_NAME = "messaging.destinations.sorters";

    /**
     * The prefix to use in defining a destination court.
     * <p>
     * Used as: <code>define.level.ISLEWORTH=0</code>
     * </p>
     * <code>define.level.</code>
     * 
     * @see CjsePopulatorFactory.CONFIG_FILE_KEY
     */
    public static final String COURT_KEY_PREFIX = "define.court.";

    /**
     * The prefix to use in defining an event level populator class.
     * <p>
     * Used as:
     * <code>define.class.ISLEWORTH=uk.gov.courtservice.xhibit.client.im.util.sorters.IsleworthDestinationSorter</code>
     * </p>
     * <code>define.class.</code>
     * 
     * @see CjsePopulatorFactory.CONFIG_FILE_KEY
     */
    public static final String COURT_CLASS_KEY_PREFIX = "define.class.";

    // A map of pre configured DestinationSorters.
    private HashMap _destinationSorters = new HashMap();

    // Classloader singleton instance of class.
    private static DestinationSorterFactory _instance = new DestinationSorterFactory();

    /**
     * Get instance of DestinationSorterFactory.
     * 
     * @return instance of DestinationSorterFactory.
     */
    public static DestinationSorterFactory getInstance() {
        return _instance;
    }

    /**
     * Get the correct DestinationSorter for the given court. This is most
     * definitely case sensitive. If no DestinationSorter is found the default
     * will be returned.
     * 
     * @param level
     *            The court for which to retrieve a DestinationSorter.
     * @return The correct DestinationSorter.
     */
    public DestinationSorter getDestinationSorter(String court) {
        log.debug("getDestinationSorter(String court) start with court = " + court);
        DestinationSorter destinationSorter = (DestinationSorter) _destinationSorters.get(court);
        if (destinationSorter != null) {
            return destinationSorter;
        } else {
            return (DestinationSorter) _destinationSorters.get("DEFAULT");
        }
    }

    /**
     * Private constructor that goes to the configuration file and instantiates
     * the defined CjseEventLevelPopulators.
     */
    private DestinationSorterFactory() {
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
            if (propertyName.startsWith(COURT_CLASS_KEY_PREFIX)) // find
            // 'interesting'
            // properties;
            {
                // Get the name of the event level.
                String courtName = propertyName.substring(COURT_CLASS_KEY_PREFIX.length());

                // Get the class name.
                String sorterClassName = props.getProperty(propertyName);

                // Get an instance of the class.
                DestinationSorter destinationSorter = getDestinationSorterInstance(sorterClassName);

                // Set the court as defined in the properties file, or failing
                // that to 0.
                destinationSorter.setCourt(Integer.valueOf(props.getProperty(COURT_KEY_PREFIX + courtName, "0")));

                // Add to the event level populators map.
                log.debug("DestinationSorterFactory() adding courtName " + courtName + ", destinationSorter "
                        + destinationSorter.getClass().toString());
                _destinationSorters.put(courtName, destinationSorter);
            }
        }
    }

    /**
     * Utility method to create an instance of the named DestinationSorter.
     * Currently expects a no-arg constructor.
     * 
     * @param populatorClassName
     *            The classname of an CjseEventLevelPopulator.
     * @return an instantiated CjseEventLevelPopulator.
     */
    private DestinationSorter getDestinationSorterInstance(String sorterClassName) {
        // Attempt to instantiate it.
        try {
            Class candidateClass = Class.forName(sorterClassName);
            if (DestinationSorter.class.isAssignableFrom(candidateClass)) {
                return (DestinationSorter) candidateClass.newInstance();
            } else {
                // Not an instance of CjseEventPopulatorHandler so throw an
                // exception.
                throw new CSUnrecoverableException("Class: " + sorterClassName
                        + " defined as a destination sorter is not an instance" + "of DestinationSorter.");
            }
        } catch (ClassNotFoundException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not find class named: " + sorterClassName, ex);
        } catch (IllegalAccessException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not access class named: " + sorterClassName, ex);
        } catch (InstantiationException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not instantiate class named: " + sorterClassName, ex);
        }

    }
}
