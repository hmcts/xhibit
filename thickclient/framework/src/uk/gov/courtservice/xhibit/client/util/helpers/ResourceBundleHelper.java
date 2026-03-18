package uk.gov.courtservice.xhibit.client.util.helpers;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.config.ConfigServicesImpl;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Resource Bundle Helper
 * </p>
 * <p>
 * Description: Helper classes for resource bundles
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ResourceBundleHelper.java,v 1.5 2006/06/05 12:30:42 bzjrnl Exp $
 */

public class ResourceBundleHelper {
    public static final String resourceNotFoundStringStart = "Resource with key '";

    private static final Logger log = CSServices.getLogger(ResourceBundleHelper.class);

    private static final boolean internalDebug = false;

    private static final Object[] emptyObject = new Object[] {};

    public static ResourceBundle XHIBITConstantResourceBundle;

    private static Hashtable allResources;

    static {
        try {
            allResources = new Hashtable();
            XHIBITConstantResourceBundle = ConfigServicesImpl.getInstance().getBundle(XhibitBundles.XhibitConstant,
                    Locale.getDefault());
            if (XHIBITConstantResourceBundle == null) {
                log.error("XHIBIConstantResourceBundle == null !!");
            } else {
                allResources.put(XhibitBundles.XhibitConstant, XHIBITConstantResourceBundle);
            }
        } catch (Exception e) {
            log.error("Exception during Static initialisation of XHIBITConstant - Resource Bundle");
        }
    }

    private ResourceBundleHelper() {
    }

    /**
     * This is the main method in XHIBIT 2 GUI to get access to a resource
     * defined in a _<locale>.properties file. The advantage of using this
     * method (and the ones below), and not your own is that you get caching of
     * the resource bundles done for you. No Exception thrown, but a nice error
     * message in the log.
     * 
     * @param resourcesName
     *            The name of the resource file must be defined in
     *            .util.XhibitBundles, and this static reference must be used.
     * @param resourceKey
     *            the resource key that you have defined in the _<locale>.properties
     *            file. If you are specifying an incorrect key, you will get a
     *            default value as ["ResourceKey '"+resourceKey +"' not found in
     *            '"+resourcesName+"'"] returned.
     * @param parameters
     *            array of objects that are to be placed within the retrieved
     *            resource (using MessageFormat).
     * 
     * @return the formatted resource text.
     */
    public static String getResource(final String resourcesName, final String resourceKey, final Object[] parameters) {
        String resourceValue;
        ResourceBundle resources = getResourceBundle(resourcesName);
        try {
            resourceValue = resources.getString(resourceKey);
        } catch (MissingResourceException e) {
            resourceValue = "ResourceKey '" + resourceKey + "' not found in '" + resourcesName + "'";
            log.error(resourceValue);
        }

        if (parameters == null || parameters.length == 0) {
            return resourceValue;
        } else {
            return MessageFormat.format(resourceValue, parameters);
        }
    }

    /**
     * This is the most commonly used method in XHIBIT 2 GUI to get access to a
     * resource defined in a _<locale>.properties file. The advantage of using
     * this method (and the one below), and not your own is that you get caching
     * of the resource bundles done for you. No Exception thrown, but a nice
     * error message in the log.
     * 
     * @param resourcesName
     *            The name of the resource file must be defined in
     *            .util.XhibitBundles, and this static reference must be used.
     * @param resourceKey
     *            the resource key that you have defined in the _<locale>.properties
     *            file. If you are specifying an incorrect key, you will get a
     *            default value as ["ResourceKey '"+resourceKey +"' not found in
     *            '"+resourcesName+"'"] returned.
     * @return the resource text.
     */
    public static String getResource(String resourcesName, String resourceKey) {
        return getResource(resourcesName, resourceKey, emptyObject);
    }

    /**
     * This is the second most important (and existing) method in XHIBIT 2 GUI
     * Development - for retrieving resources that is!. Using this method, in
     * contrast with the one above, does not require you to specify the name of
     * the _<locale>.properties file where to find the value of the key
     * specified as arugment. This method will assume you have defined the
     * resource in the file represtend by XhibitBundles.XhibitConstant
     */
    public static String getResource(String resourceKey) {
        return getResource(XhibitBundles.XhibitConstant, resourceKey, emptyObject);
    }

    /**
     * Check if the resource exists. Saves the coder having to handle exceptions
     * 
     * @param resourcesName
     * @param resourceKey
     * @return
     */
    public static boolean isResourceAvailable(String resourcesName, String resourceKey) {
        boolean resourceFound = false;
        ResourceBundle resources = getResourceBundle(resourcesName);
        try {
            String resourceValue = resources.getString(resourceKey);
            resourceFound = true;
        } catch (MissingResourceException e) {
            log.error("ResourceKey '" + resourceKey + "' not found in " + resourcesName);
            resourceFound = false;
        }
        return resourceFound;
    }

    /**
     * @todo: could be rewritten to ensure clients may pass in an XhibitBundles
     *        static string, rahter than a reference to the resourcebundle
     *        itself.
     */
    public static Vector getDistinctResourcesStartingWith(ResourceBundle rsc, String startOfResourceKey) {
        Vector rscWithPotDoubles = getResourcesStartingWith(rsc, startOfResourceKey);
        Vector rscNoDoubles = new Vector();

        Iterator potentialDoubleIterator = rscWithPotDoubles.iterator();
        while (potentialDoubleIterator.hasNext()) {
            String potentialDouble = (String) potentialDoubleIterator.next();
            if (!rscNoDoubles.contains(potentialDouble)) {
                // it's now clear the potentialDouble is actually no double at
                // all, so add it!
                rscNoDoubles.add(potentialDouble);
            }
        }
        return rscNoDoubles;
    }

    /**
     * @todo: could be rewritten to ensure clients may pass in an XhibitBundles
     *        static string, rahter than a reference to the resourcebundle
     *        itself.
     */
    public static Vector getResourcesStartingWith(ResourceBundle rsc, String startOfResourceKey) {
        Vector resourceStrings = new Vector();
        Vector resourceKeys = new Vector();

        Enumeration rscKeyEnumeration = rsc.getKeys();
        while (rscKeyEnumeration.hasMoreElements()) {
            String rscKey = (String) rscKeyEnumeration.nextElement();
            if (rscKey.startsWith(startOfResourceKey)) {
                resourceStrings.add(getResource(rsc, rscKey));
            }
        }
        return resourceStrings;
    }

    /**
     * this is one of the methods used to actualy get access to a properties
     * file, and if necessary load it from into -caching/chached- memory.
     */
    public static String getResource(ResourceBundle rsc, String resourceKey) {
        String DefaultLocaleDependentResource = "";
        try {
            // log.debug("Getting resource with " + resourceKey + " from
            // resourcebundle: " + rsc.toString());
            DefaultLocaleDependentResource = rsc.getString(resourceKey);
            if (DefaultLocaleDependentResource == null) {
                log.debug("Resource with key " + resourceKey + " has value NULL in ResourceBundle: " + rsc.toString());
            } else {
                if (internalDebug)
                    log.debug("Got resource with key " + resourceKey + " : has value '"
                            + DefaultLocaleDependentResource + "' in ResourceBundle: " + rsc.toString());
            }
        } catch (MissingResourceException e) {
            log.error("Exception caugth whilst getting resource with key " + resourceKey
                    + " is NULL in ResourceBundle: " + rsc.toString());
        } finally {
            return DefaultLocaleDependentResource;
        }
    }

    /*
     * Bug_Release_X010102-030113 -- added debug information to identify any
     * issues with loading of resource files.
     * 
     */
    public static ResourceBundle getResourceBundle(String baseName) {
        ResourceBundle rsc = null;
        try {
            if (allResources.containsKey(baseName)) {
                rsc = (ResourceBundle) allResources.get(baseName);
            } else {
                Locale theLocale = Locale.getDefault();
                if (internalDebug)
                    log.debug("The Locale to load '" + baseName + "' is " + theLocale.toString());
                rsc = ConfigServicesImpl.getInstance().getBundle(baseName, theLocale);
                allResources.put(baseName, rsc);
                if (internalDebug)
                    log.debug("Loaded resourcebundle '" + baseName + "' into memory...");
            }

            if (rsc == null) {
                /**
                 * @todo: create and throw a descriptive (ie null encountered
                 *        when loading) exception
                 */
                log.error("ResourceBundle with baseName " + baseName + " is null.");
            }
        } catch (CSConfigurationException CSCe) {
            log.error("An exception was thrown whilst loading a resource bundle, using the ConfigServicesImpl");
            /**
             * @todo: wrap in descriptive (ie trouble accessing resourcebundle
             *        with baseName) exception
             */
            XHIBITErrorHandler.handleError(CSCe);
        }
        return rsc;
    }
}