package uk.gov.courtservice.framework.services;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSConfigurationException;

/**
 * <p>
 * Config
 * </p>
 * <p>
 * Description: Insulates the application components from knowedge of property
 * storage. Some properties may be stored in XML configuration files and some in
 * name=value properties files
 * </p>
 * <p>
 * The nameing of properties files must follow the following convention all
 * master properties files for each application must have the application name
 * first precceded by .properties e.g. xhibit.properties. For component property
 * files must have the application name first precceded by .<componentName> the
 * .properties e.g. xhibit.listimport.properties, see developer guide for the
 * naming conventions of property files and resource bundles
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @author Paul Grove
 * @version 1.1
 */
public interface ConfigServices {
    public static String ERROR_MESSAGES = "errorText";

    /**
     * Provides a String value for a property as defined by the key parameter
     * from the daufalt properties file. The default CS Hub generic proprties
     * file as defined as CSHub.properties. To minimise required changed to to
     * other parts of the application while ensuring that configuration data is
     * loaded from a central location.
     * 
     * @param String
     *            the key for which you want he property
     * @return String object of the property for the given key
     * @throws XhibitConfigurationException
     *             if configuration files cannot be correctly read
     */
    public String getProperty(String key) throws CSConfigurationException;

    /**
     * The method checks the system property, then the specified property file
     * and then the default value
     * 
     * @param props
     *            the property file to search in
     * @param key
     *            the key for which you want he property
     * @param def
     *            The default value
     * @return String object of the property for the given key
     * @throws XhibitConfigurationException
     *             if configuration files cannot be correctly read
     */
    public String getProperty(Properties props, String key, String def) throws CSConfigurationException;

    /**
     * Provides a String value for a property as defined by the key parameter
     * from the daufalt properties file. The default CS Hub generic proprties
     * file as defined as CSHub.properties. To minimise required changed to to
     * other parts of the application while ensuring that configuration data is
     * loaded from a central location.
     * 
     * @param String
     *            the key for which you want he property
     * @param default
     *            value in case property is not found
     * @return String object of the property for the given key
     * @throws XhibitConfigurationException
     *             if configuration files cannot be correctly read
     */
    public String getProperty(String key, String def) throws CSConfigurationException;

    /*
     * /** Provides a String value for a property as defined by the key
     * parameter from the correct application proprties file as defined by the
     * passed Application object. To minimise required changed to to other parts
     * of the application while ensuring that configuration data is loaded from
     * a central location. @param Application the application for which the
     * properties file is for @param String the key for which you want he
     * property @return String object of the property for the given key @throws
     * XhibitConfigurationException if configuration files cannot be correctly
     * read
     */
    /*
     * public String getProperty(Application app, String key) throws
     * CSConfigurationException;
     */
    /**
     * Provides a name/value properties file as a properties object to minimise
     * required changed to to other parts of the application while ensuring that
     * configuration data is loaded from a central location.
     * 
     * @param String
     *            the String for the component type, must use full properties
     *            name e.g. xhibit.listimport
     * @return Properties object based on the file loaded
     * @throws CSconfigurationException
     *             if configuration files cannot be correctly read
     */
    public Properties getProperties(String componentName) throws CSConfigurationException;

    /**
     * Rather than call ResourcrBundle.getResource directly applications call
     * this method which then locates the correct prperties file and then calls
     * ResourcrBundle.getResource with the correct file. This is done to
     * minimise required changes to other parts of the application while
     * ensuring that configuration data is loaded from a central location.
     * 
     * @param baseName
     *            see {@link java.util.ResourceBundle ResourceBundle} class
     *            description.
     * @return The correct {@link java.util.ResourceBundle ResourceBundle} from
     *         the central repository
     * @throws CSConfigurationException
     */
    public ResourceBundle getBundle(String baseName) throws CSConfigurationException;

    /**
     * Rather than call ResourcrBundle.getResource directly applications call
     * this method which then locates the correct prperties file and then calls
     * ResourcrBundle.getResource with the correct file. This is done to
     * minimise required changes to other parts of the application while
     * ensuring that configuration data is loaded from a central location.
     * 
     * @param baseName
     *            see {@link java.util.ResourceBundle ResourceBundle} class
     *            description.
     * @param locale
     *            see {@link java.util.ResourceBundle ResourceBundle} class
     *            description.
     * @return The correct {@link java.util.ResourceBundle ResourceBundle} from
     *         the central repository
     * @throws CSConfigurationException
     */
    public ResourceBundle getBundle(String baseName, Locale locale) throws CSConfigurationException;

    /**
     * Rather than call ResourcrBundle.getResource directly applications call
     * this method which then locates the correct prperties file and then calls
     * ResourcrBundle.getResource with the correct file. This is done to
     * minimise required changes to other parts of the application while
     * ensuring that configuration data is loaded from a central location.
     * 
     * @param baseName
     *            see {@link java.util.ResourceBundle ResourceBundle} class
     *            description.
     * @param locale
     *            see {@link java.util.ResourceBundle ResourceBundle} class
     *            description.
     * @param loader
     *            the ClassLoader to load the resource from.
     * @return The correct {@link java.util.ResourceBundle ResourceBundle} from
     *         the central repository
     * @throws CSConfigurationException
     */
    public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) throws CSConfigurationException;

    /**
     * Loads the Log4j properties file from a central repository
     * 
     * @return Properties object for Log4j loaded from the central repository
     */
    // public Properties getLog4jProps();
}
