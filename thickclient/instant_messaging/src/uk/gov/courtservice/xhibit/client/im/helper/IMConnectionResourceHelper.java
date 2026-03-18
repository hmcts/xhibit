package uk.gov.courtservice.xhibit.client.im.helper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: IMConnectionResourceHelper
 * </p>
 * <p>
 * Description: Retrieves Instant Messaging properties from bundle
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMConnectionResourceHelper {
    private static final String INSTANT_MSG_RESOURCES = "XHIBITInstantMessagingResources_en_GB";

    private static final int IM_RETRIES = 20;

    private static ResourceBundle bundle;

    /**
     * Private contructor so that this clas can't be instantiated
     */
    private IMConnectionResourceHelper() {
        // private constructor
    }

    /**
     * Retrieve the properties from the bundle given the key.
     * 
     * @param key
     *            The key for the property
     * @return the number of times to retry the connection, or the default value
     * @throws CSConfigurationException
     */
    public static int getConnectionRetries(String key) throws CSConfigurationException {
        // Get properties for disposal references...
        try {
            return getIMProperty(key);
        } catch (NumberFormatException ex) {
            return IM_RETRIES;
        } catch (MissingResourceException ex) {
            return IM_RETRIES;
        }
    }

    /**
     * Retrieve the property from the bundle.
     * 
     * @param key
     * @return
     * @throws CSConfigurationException
     * @throws NumberFormatException
     * @throws MissingResourceException
     */
    public static int getIMProperty(String key) throws CSConfigurationException, NumberFormatException,
            MissingResourceException {
        ResourceBundle bundle = CSServices.getConfigServices().getBundle(INSTANT_MSG_RESOURCES);
        return Integer.parseInt(bundle.getString(key));
    }
}