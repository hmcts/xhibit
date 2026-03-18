package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Xhibit2 ResourceHelper
 * </p>
 * <p>
 * Description: Retrieves configuration information
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

public final class ResourceHelper {
    private static ResourceBundle res = CSServices.getConfigServices().getBundle("XHIBITOrdersClient", Locale.UK);

    /**
     * 
     */
    public ResourceHelper() {
    }

    /**
     * Returns a Resource
     * 
     * @param s
     * @return
     */
    public static String getResourceString(String s) {
        try {
            return res.getString(s);
        } catch (MissingResourceException ex) {
            return s;
        }
    }

    /**
     * Returns a default value (supplied) if the resource is not found
     * 
     * @param s
     * @return String
     */
    public static String getResourceString(String s, String defVal) {
        try {
            return getResourceString(s);
        } catch (MissingResourceException ex) {
            return defVal;
        }
    }
}