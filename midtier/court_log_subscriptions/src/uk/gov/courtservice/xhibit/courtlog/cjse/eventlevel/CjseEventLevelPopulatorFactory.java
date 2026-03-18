package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Cjse Event Level Populator Factory
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is responsible for configuring and returning instances of
 * CjseEventLevelPopulator. Also provides access to a list of the event levels
 * defined.
 * </p>
 * <p>
 * <b>NOTE:</b> For now, this class is only designed to work with
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby, Sarah Tong
 * @version $Id: CjseEventLevelPopulatorFactory.java,v 1.1 2004/04/20 14:40:55
 *          pznwc5 Exp $
 */
public class CjseEventLevelPopulatorFactory {
    /**
     * The component event level properties file name.
     * <p>
     * Used as:
     * <code>CSServices.getConfigServices().getProperties(PROPERTIES_FILE_NAME)</code>
     * <p>
     * <code>cjse.event.mapping.properties</code>
     */
    public static final String PROPERTIES_FILE_NAME = "cjse.event.level";

    /**
     * The prefix to use in defining an event level numeric value.
     * <p>
     * Used as: <code>define.level.CASE=0</code>
     * <p>
     * The suffix defines a new event level that can be used in the
     * configuration xml file defined by the
     * CjsePopulatorFactory.CONFIG_FILE_KEY.
     * <p>
     * <code>define.level.</code>
     * 
     * @see CjsePopulatorFactory.CONFIG_FILE_KEY
     */
    public static final String EVENT_LEVEL_KEY_PREFIX = "define.level.";

    /**
     * Key used to acquire the CRN event level populator
     * <p>
     * Used as: <code>
     * CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
     * CjseEventLevelPopulatorFactory.CRN_EVENT_LEVEL);
     * </code>
     * </p>
     */
    public static final String CRN_EVENT_LEVEL = "CRN";

    /**
     * Key used to acquire the CASE event level populator
     * <p>
     * Used as: <code>
     * CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
     * CjseEventLevelPopulatorFactory.CASE_EVENT_LEVEL);
     * </code>
     * </p>
     */
    public static final String CASE_EVENT_LEVEL = "CASE";
    
    /**
     * Key used to acquire the DEPORTATION event level populator
     * <p>
     * Used as: <code>
     * CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
     * CjseEventLevelPopulatorFactory.DEPORTATION_EVENT_LEVEL);
     * </code>
     * </p>
     */
    public static final String DEPORTATION_EVENT_LEVEL = "DEPORTATION";
    
    /**
     * Key used to acquire the HATE_CRIME event level populator
     * <p>
     * Used as: <code>
     * CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
     * CjseEventLevelPopulatorFactory.HATE_CRIME_EVENT_LEVEL);
     * </code>
     * </p>
     */
    public static final String HATE_CRIME_EVENT_LEVEL = "HATE_CRIME";

    /**
     * Key used to acquire the DEFENDANT event level populator
     * <p>
     * Used as: <code>
     * CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
     * CjseEventLevelPopulatorFactory.DEFENDANT_EVENT_LEVEL);
     * </code>
     * </p>
     */
    public static final String DEFENDANT_EVENT_LEVEL = "DEFENDANT";

    /**
     * The prefix to use in defining an event level populator class.
     * <p>
     * Used as:
     * <code>define.class.CASE=uk.gov.courtservice.xhibit.business.messaging.cjse.populators.CaseCjseEventLevelPopulator</code>
     * <p>
     * The suffix defines a new event level that can be used in the
     * configuration xml file defined by the
     * CjsePopulatorFactory.CONFIG_FILE_KEY.
     * <p>
     * <code>define.class.</code>
     * 
     * @see CjsePopulatorFactory.CONFIG_FILE_KEY
     */
    public static final String EVENT_CLASS_KEY_PREFIX = "define.class.";

    // A map of pre configured CjseEventLevelPopulators.
    private HashMap _cjseEventLevelPopulators = new HashMap();

    // A map of event levels keyed on the name of the event level
    private HashMap _cjseEventLevelDefinitions = new HashMap();

    // Classloader singleton instance of class.
    private static CjseEventLevelPopulatorFactory _instance = new CjseEventLevelPopulatorFactory();

    /**
     * Get instance of CjseEventLevelPopulatorFactory.
     * 
     * @return instance of CjseEventLevelPopulatorFactory.
     */
    public static CjseEventLevelPopulatorFactory getInstance() {
        return _instance;
    }

    /**
     * Get the correct CjseEventLevelPopulator for the given event level. This
     * is most definitely case sensitive.
     * 
     * @param level
     *            The event level for which to retrieve a
     *            CjseEventLevelPopulator.
     * @return The correct CjseEventLevelPopulator.
     */
    public CjseEventLevelPopulator getCjseEventLevelPopulator(String level) {
        CjseEventLevelPopulator eventLevelPopulator = (CjseEventLevelPopulator) _cjseEventLevelPopulators.get(level);
        if (eventLevelPopulator != null)
            return eventLevelPopulator;
        else
            throw new CSUnrecoverableException("There is no CjseEventLevelPopulator defined for level: " + level);
    }

    /**
     * Gets the event level for the given event level description. This is most
     * definitely case sensitive.
     * 
     * @param eventLevelName
     *            The event level description to return the level for.
     * @return The correct event level
     */
    public Integer getEventLevelForDescription(String eventLevelName) {
        Integer eventLevel = (Integer) _cjseEventLevelDefinitions.get(eventLevelName);
        if (eventLevel != null)
            return eventLevel;
        else
            throw new CSUnrecoverableException("There is no event level defined for description: " + eventLevelName);
    }

    /**
     * Private constructor that goes to the configuration file and instantiates
     * the defined CjseEventLevelPopulators.
     */
    private CjseEventLevelPopulatorFactory() {
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
            if (propertyName.startsWith(EVENT_CLASS_KEY_PREFIX)) // find
            // 'interesting'
            // properties;
            {
                // Get the name of the event level.
                String eventLevelName = propertyName.substring(EVENT_CLASS_KEY_PREFIX.length());

                // Get the class name.
                String populatorClassName = props.getProperty(propertyName);

                // Get an instance of the class.
                CjseEventLevelPopulator cjseEventLevelPopulator = getEventLevelPopulatorInstance(populatorClassName);

                // Add to the event level populators map.
                _cjseEventLevelPopulators.put(eventLevelName, cjseEventLevelPopulator);

                // Get the event level as defined in the properties file, or
                // failing
                // that to 0.
                Integer eventLevel = Integer.valueOf(props.getProperty(EVENT_LEVEL_KEY_PREFIX + eventLevelName, "0"));

                // Add to the event level definitions map.
                _cjseEventLevelDefinitions.put(eventLevelName, eventLevel);
            }
        }
    }

    /**
     * Utility method to create an instance of the named
     * CjseEventLevelPopulator. Currently expects a no-arg constructor.
     * 
     * @param populatorClassName
     *            The classname of an CjseEventLevelPopulator.
     * @return an instantiated CjseEventLevelPopulator.
     */
    private CjseEventLevelPopulator getEventLevelPopulatorInstance(String populatorClassName) {
        // Attempt to instantiate it.
        try {
            Class candidateClass = Class.forName(populatorClassName);
            if (CjseEventLevelPopulator.class.isAssignableFrom(candidateClass)) {
                return (CjseEventLevelPopulator) candidateClass.newInstance();
            } else {
                // Not an instance of CjseEventPopulatorHandler so throw an
                // exception.
                throw new CSUnrecoverableException("Class: " + populatorClassName
                        + " defined as an event level populator is not an instance" + "of CjseEventLevelPopulator.");
            }
        } catch (ClassNotFoundException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not find class named: " + populatorClassName, ex);
        } catch (IllegalAccessException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not access class named: " + populatorClassName, ex);
        } catch (InstantiationException ex) {
            // Throw exception
            throw new CSUnrecoverableException("Could not instantiate class named: " + populatorClassName, ex);
        }

    }
}