package uk.gov.courtservice.xhibit.client.util.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.config.ConfigServicesImpl;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;

/**
 * <p>
 * Title: Property Helper
 * </p>
 * <p>
 * Description: Helper classes for property files
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PropertyHelper.java,v 1.6 2006/06/05 12:30:42 bzjrnl Exp $
 */

public class PropertyHelper {
    public static final String propertyNotFoundStringStart = "Property with key '";

    private static final Logger log = CSServices.getLogger(PropertyHelper.class);

    private static final boolean internalDebug = false;

    private static final String PATH_SEP = System.getProperty("file.separator");

    public static Properties XHIBITConstantProperties;

    private static Hashtable allProperties;

    static {
        try {
            allProperties = new Hashtable();
            XHIBITConstantProperties = ConfigServicesImpl.getInstance().getProperties(XhibitProperties.XhibitConstant);
            if (XHIBITConstantProperties == null) {
                log.error("XHIBITConstantProperties == null !!");
            } else {
                allProperties.put(XhibitProperties.XhibitConstant, XHIBITConstantProperties);
            }
        } catch (Exception e) {
            log.error("Exception during Static initialisation of XHIBITConstant - Properties");
        }
    }

    private PropertyHelper() {
    }

    // //////////////////////////////////////////////////////////////////////////
    // /////////////// //////////////////////
    // /////////////// GUI PROPERTIES //////////////////////
    // /////////////// //////////////////////
    // //////////////////////////////////////////////////////////////////////////
    /**
     * This is the main method in XHIBIT 2 GUI to get access to a property
     * defined in a .properties file. The name of the properties file must be
     * defined in .util.XhibitProperties, and this static referece must be ussed
     * as the first arugment in the method call. The second argument in the
     * property key as you have defined in the .properties file. If you are
     * specifying an incorrect key, you will get a default value as
     * ["PropertyKey '"+ propertyKey+"' not found in "+ propertiesName]
     * returned. No Exception thrown, but a nice error message in the log. <p/>
     * Advantage of using this method (and the one below), and not your own ones
     * is that you get caching of the properties files done for you.
     */
    public static String getProperty(String propertiesName, String propertyKey) {
        String propertyValue = propertyNotFoundStringStart + propertyKey + "' not found in " + propertiesName;

        Properties properties = getProperties(propertiesName);

        if (properties != null) {
            propertyValue = (String) properties.get(propertyKey);
            if (propertyValue == null) {
                propertyValue = propertyNotFoundStringStart + propertyKey + "' not found in " + propertiesName;
                log.debug(propertyValue);
            }
        }
        return propertyValue;
    }

    /**
     * This is the second most important (and existing) method in XHIBIT 2 GUI
     * Development. Using this method, in contrast with the one above, does not
     * require you to specify the name of the properties file where to find the
     * value of the key specified as arugment. This method will assume you have
     * define the property in the file represented by
     * XhibitProperties.XhibitConstant
     */
    public static String getProperty(String propertyKey) {
        return getProperty(XhibitProperties.XhibitConstant, propertyKey);
    }

    private static String userPropsDir = null;

    public static String getUserPropsDir() {
        if (userPropsDir == null) {
            userPropsDir = System.getProperty("user.home") + PATH_SEP + "Application Data" + PATH_SEP + "EDS"
                    + PATH_SEP + "Xhibit";
            File f = new File(userPropsDir);
            if (!f.exists())
                f.mkdirs();
        }
        return userPropsDir;
    }

    /**
     * Will create a properties file and place it in the user home directory.
     * Note that the file name called include a directory structure as well, in
     * which case, the System.getProperty("file.separator") should be used for
     * platform independance.
     * 
     * @param fileName
     *            the name of the file.
     * @param fileText
     *            the content of the file.
     * @throws CSRecoverableException
     */
    public static void createUserHomeProperties(String fileName, String fileText) throws CSRecoverableException {
        try {
            fileName = getUserPropsDir() + PATH_SEP + fileName;
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(fileText.getBytes());
        } catch (FileNotFoundException e) {
            throw new CSRecoverableException("properties.ioerror.create", "Failed to write property file", e);
        } catch (IOException e) {
            throw new CSRecoverableException("properties.ioerror.create", "Failed to write property file", e);
        }
    }

    /**
     * Will obtain the properties file, if one exists, from the user home
     * directory. Note that the file name called include a directory structure
     * as well, in which case, the System.getProperty("file.separator") should
     * be used for platform independance. <p/> Null is returned if there is no
     * property file, or if there was an error making the properties object.
     * 
     * @param fileName
     *            the name of the properties file to load.
     * @return the properties object of the given file.
     */
    public static Properties getUserHomeProperties(String fileName) {
        Properties properties = null;
        fileName = getUserPropsDir() + PATH_SEP + fileName;

        // Obtain a stream to the properties file - return null if not found.
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // Handle and throw null.
            return null;
        }

        // Load and return a properties object from the stream.
        try {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            // Handle the exception and throw null.
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Do nothing.
                log.fatal(e, e);
                throw new CSUnrecoverableException(e);

            }
        }

        return properties;
    }

    /**
     * Will save the properties to the named file.
     * 
     * @param properties
     *            the properties object holding all the key/value pairs.
     * @param fileName
     *            the name of the file to save the properties to.
     * @throws CSRecoverableException
     *             if there is an error writing to the file.
     */
    public static void storeUserHomeProperties(Properties properties, String fileName) throws CSRecoverableException {
        FileOutputStream outputStream = null;
        try {
            fileName = getUserPropsDir() + PATH_SEP + fileName;
            outputStream = new FileOutputStream(fileName);
            properties.store(outputStream, null);
        } catch (IOException e) {
            throw new CSRecoverableException("properties.ioerror.save", "Failed to write property file", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.fatal(e, e);
                    throw new CSUnrecoverableException(e);

                }
            }
        }
    }

    /**
     * this is one of the methods used to actualy get access to a properties
     * file, and if necessary load it from into -caching/chached- memory.
     */
    public static Properties getProperties(String propertiesName) {
        Properties p = null;
        try {
            if (internalDebug)
                log.debug("Getting property file '" + propertiesName + "' ...");
            if (allProperties.containsKey(propertiesName)) {
                p = (Properties) allProperties.get(propertiesName);
                if (internalDebug)
                    log.debug("Found property file '" + propertiesName + "' in memory...");
            } else {
                if (internalDebug)
                    log.debug("Loading properties file now - ConfigServicesImpl.getInstance().getProperties("
                            + propertiesName + ")");
                try {
                    p = ConfigServicesImpl.getInstance().getProperties(propertiesName);
                    allProperties.put(propertiesName, p);
                    if (internalDebug)
                        log.debug("Loaded property file '" + propertiesName + "' into memory...");
                } catch (CSConfigurationException e) {
                    XHIBITErrorHandler.handleError(e);
                }
            }

            if (p == null) {
                log.error("Property file " + propertiesName + " is null!!");
            }
        } catch (CSConfigurationException CSCe) {
            log.error("An exception was thrown whilst loading properties file '" + propertiesName + "'.");
            XHIBITErrorHandler.handleError(CSCe);
        }
        return p;
    }

}