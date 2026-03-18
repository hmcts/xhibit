package uk.gov.courtservice.framework.services.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ConfigServices;

/**
 * <p>
 * ConfigServicesImpl
 * </p>
 * <p>
 * Description: Insulates the application components from knowedge of property
 * storage. Some properties may be stored in XML configuration files and some in
 * name=value properties files or a database.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @author Paul Grove - completely changed configuration
 * @version 1.1
 * @editors Frederik Vandendriessche - changed BUNDLE_BASENAME accessibility to
 *          public so other components may read from the bundle root.
 */
public class ConfigServicesImpl implements ConfigServices {

    private static ConfigServicesImpl instance = new ConfigServicesImpl();

    private static boolean initialized;

    private static Properties applicationProps = new Properties();

    private static Logger log = CSServices.getLogger(ConfigServicesImpl.class);

    public static String BUNDLE_BASENAME = "config/bundles/";

    private static String APPLICATIONS_CONFIG_PATH = "/config/application.properties";

    /**
     * @todo standardise extension to properties ?
     */

    private ConfigServicesImpl() {
    }

    /**
     * Get a reference to the config.
     */
    public static synchronized ConfigServicesImpl getInstance() throws CSConfigurationException {
        if (initialized == false) {
            initialize();
        }
        return instance;
    }

    /**
     * Returns a property from the default CS Hub properties file called
     * cshub.properties which is stored at "classpath root"/config
     * 
     * @param key
     *            the key for which you want he property
     * @return String object of the property for the given key
     * @throws XhibitConfigurationException
     *             if configuration files cannot be correctly read
     */
    public String getProperty(String key) throws CSConfigurationException {
        // Properties props =
        // (Properties)propertiesMap.get(ConfigServicesImpl.CS_HUB_PROPERTIES);
        return System.getProperty(key, applicationProps.getProperty(key));
    }

    /**
     * The method checks the system property, then the application bundle and
     * then the default value
     * 
     * @param key
     *            the key for which you want he property
     * @param def
     *            Default value
     * @return String object of the property for the given key
     * @throws XhibitConfigurationException
     *             if configuration files cannot be correctly read
     */
    public String getProperty(String key, String def) throws CSConfigurationException {
        // Properties props =
        // (Properties)propertiesMap.get(ConfigServicesImpl.CS_HUB_PROPERTIES);
        return System.getProperty(key, applicationProps.getProperty(key, def));
    }

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
    public String getProperty(Properties props, String key, String def) throws CSConfigurationException {
        if (props == null) {
            return System.getProperty(key, def);
        } else {
            return System.getProperty(key, props.getProperty(key, def));
        }
    }

    /**
     * Provides a name/value properties file as a properties object to minimise
     * required changed to to other parts of the application while ensuring that
     * configuration data is loaded from a central location.
     * 
     * @param componentName
     *            the String for the component type e.g. listimport
     * @return Properties object based on the file loaded
     * @throws CSconfigurationException
     *             if configuration files cannot be correctly read
     */
    public Properties getProperties(String componentName) throws CSConfigurationException {
        String errMsg = null;
        String file = "/config/components/" + componentName + ".properties";
        try {
            InputStream is = ConfigServices.class.getResourceAsStream(file);
            if (is == null) {
                errMsg = "null InputStream for file=" + file + " for componentName=" + componentName;
                CSConfigurationException e = new CSConfigurationException(errMsg);
                // system test commented on the number of stack traces being
                // thrown.
                // here, the handleError method prints the stack even though not
                // finding
                // a component specific properties file is an expected
                // condition.
                // changed to simply print a warning message to the log (JonP)
                log.warn("file " + file + " not found (component will use defaults)");
                // CSServices.getDefaultErrorHandler().handleError(e,
                // ConfigServicesImpl.class, errMsg);
                throw e;
            }
            Properties componentProps = new Properties();
            componentProps.load(is);
            return componentProps;
        } catch (IOException e) {
            errMsg = "Error loading file=" + file + " for componentName=" + componentName;
            CSConfigurationException ex = new CSConfigurationException(errMsg, e);
            CSServices.getDefaultErrorHandler().handleError(ex, ConfigServicesImpl.class);
            throw ex;
        }

    }

    private static void initialize() throws CSConfigurationException {

        log.info(ConfigServices.class.getName() + ".initialize(): Thread=" + Thread.currentThread());
        try {
            // loadCSHubProperties();
            loadApplicationProperties();
        } catch (IOException e) {
            CSConfigurationException ex = new CSConfigurationException(e);
            CSServices.getDefaultErrorHandler().handleError(ex, ConfigServicesImpl.class);
            throw ex;
        }
        initialized = true;
        log.info("Exhibit ConfigServices intialized");
    }

    /**
     * Uses the Applications List Iterator to get the name of each application
     * running on the CS Hub and load each applications master properties file.
     */
    private static void loadApplicationProperties() throws IOException, CSConfigurationException {

        try {
            InputStream is = ConfigServices.class.getResourceAsStream(APPLICATIONS_CONFIG_PATH);
            if (is == null) {
                String msg = "file not found: file=" + APPLICATIONS_CONFIG_PATH;
                // weblogicLogger.info(msg);
                CSConfigurationException ex = new CSConfigurationException(msg);
                CSServices.getDefaultErrorHandler().handleError(ex, ConfigServices.class);
                throw ex;
            }
            applicationProps.load(is);
            // propertiesMap.put(appName, props);
            log.info("Loading application properties");
        } catch (java.lang.NullPointerException e) {
            String msg = "Null pointer Error loading file=" + APPLICATIONS_CONFIG_PATH;
            // weblogicLogger.info(msg);
            CSConfigurationException ex = new CSConfigurationException(msg, e);
            CSServices.getDefaultErrorHandler().handleError(ex, ConfigServicesImpl.class);
            throw ex;
        } catch (IOException e) {
            String msg = "Error loading file=" + APPLICATIONS_CONFIG_PATH;
            // weblogicLogger.info(msg);
            CSConfigurationException ex = new CSConfigurationException(msg, e);
            CSServices.getDefaultErrorHandler().handleError(ex, ConfigServicesImpl.class);
            throw ex;
        }

    }

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
    public ResourceBundle getBundle(String baseName) throws CSConfigurationException {

        baseName = ConfigServicesImpl.BUNDLE_BASENAME + baseName;

        return ResourceBundle.getBundle(baseName);
    }

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
    public ResourceBundle getBundle(String baseName, Locale locale) throws CSConfigurationException {

        String errMsg = null;
        baseName = ConfigServicesImpl.BUNDLE_BASENAME + baseName;
        try {
            return ResourceBundle.getBundle(baseName, locale);
        } catch (MissingResourceException e) {
            errMsg = "Resource bundle " + baseName + "_" + locale + " not available";
            CSConfigurationException ex = new CSConfigurationException(errMsg, e);
            CSServices.getDefaultErrorHandler().handleError(ex, ConfigServicesImpl.class, errMsg);
            throw ex;
        }
    }

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
    public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) throws CSConfigurationException {

        baseName = ConfigServicesImpl.BUNDLE_BASENAME + baseName;
        return ResourceBundle.getBundle(baseName, locale, loader);
    }

}
