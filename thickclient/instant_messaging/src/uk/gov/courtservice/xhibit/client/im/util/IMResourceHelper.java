package uk.gov.courtservice.xhibit.client.im.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: IMResourceHelper
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

public final class IMResourceHelper {
    private static ResourceBundle res = CSServices.getConfigServices().getBundle("XHIBITIMClient", Locale.UK);

    /**
     * Retrives the required key from the ResourceBundle
     * 
     * @param s
     *            key
     * @return String value
     */
    public static String getResourceString(String s) {
        try {
            return res.getString(s);
        } catch (MissingResourceException ex) {
            return s;
        }
    }
}