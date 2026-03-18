package uk.gov.courtservice.xhibit.client.order.util;

import java.util.Locale;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * 
 * <p>
 * Title: Resource
 * </p>
 * <p>
 * Description: Test ResourceBundle
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class Resource {
    /**
     * Return the value from the bundle given the key
     * 
     * @param key
     *            the key
     * @return the value
     */
    public static String getOrdersClientBundle(String key) {
        return CSServices.getConfigServices().getBundle("XHIBITOrdersClient", Locale.UK).getString(key);
    }

    /**
     * Return the error message from the bundle given the key
     * 
     * @param key
     *            the key
     * @return the error message
     */
    public static String getOrdersErrorText(String key) {
        return CSServices.getConfigServices().getBundle("errorText", Locale.UK).getString(key);
    }
}