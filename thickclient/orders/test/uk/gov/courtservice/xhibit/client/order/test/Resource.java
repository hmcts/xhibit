package uk.gov.courtservice.xhibit.client.order.test;

import java.util.Locale;
import java.util.ResourceBundle;

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
public class Resource extends java.util.ListResourceBundle {
    static final Object[][] contents = new String[][] {
            { "XMLDataJSPLocation", "file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/Bail Order.xml" },
            { "DataEntryTemplate", "file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/DataEntryTemplate.xml" },
            { "3Feb_1990", "3-Feb-1990" },
            { "dob", "dob" },
            { "simpleTransform", "file:/d:/projects/XHIBIT/thickclient/orders/xml/documentTransforms/Bail Order.xslt" },
            { "OrderWriterOutputURL", "" },
            { "XHIBITOrdersClient",
                    "file:/d/projects/XHIBIT/config/src/config/bundles/XHIBITOrdersClient_en_GB.properties" } };

    private ResourceBundle res = CSServices.getConfigServices().getBundle("XHIBITOrdersClient", Locale.UK);

    /**
     * Return the contents of the bundle
     * 
     * @return the contents
     */
    public Object[][] getContents() {
        return contents;
    }

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