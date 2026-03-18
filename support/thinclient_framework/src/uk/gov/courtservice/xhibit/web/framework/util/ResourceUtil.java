package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * Title: Resource Util
 * </p>
 * <p>
 * Description: A bunch of utilities for manipulating resources (data stored in
 * files in the class path)
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 */
public class ResourceUtil {
    /**
     * <p>
     * Loads the requested resource using the current class's loader.
     * </p>
     * 
     * @param resourceName
     *            a String specifying the name of the requested resource
     * @return an InputStream to read data from the resource
     * @throws ResourceNotFoundException
     *             if the resource can not be found
     */
    public static InputStream getResourceAsStream(String resourceName) throws ResourceNotFoundException {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        InputStream resourceInputStream = classLoader.getResourceAsStream(resourceName);
        if (resourceInputStream != null) {
            return resourceInputStream;
        } else {
            throw new ResourceNotFoundException(resourceName);
        }
    }

    /**
     * <p>
     * Loads the requested resource according to the locale.
     * </p>
     * 
     * @param resourceName
     *            a String specifying the name of the requested resource
     * @param locale
     *            the locale
     * @param propertyName
     *            the name of the property to retrieve
     * @return the string representing the property.
     * @throws ResourceNotFoundException
     *             if the resource can not be found
     */
    public static String getPropertyForResourceAndLocale(String resourceName, Locale locale, String propertyName)
            throws ResourceNotFoundException {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        ResourceBundle rb = ResourceBundle.getBundle(resourceName, locale, classLoader);
        if (rb.getObject(propertyName) != null) {
            return (String) rb.getObject(propertyName);
        } else {
            throw new ResourceNotFoundException(resourceName + " value : " + propertyName);
        }
    }

    /**
     * <p>
     * Loads the requested resource and parses it into a typed property
     * collection
     * </p>
     * 
     * @param resourceName
     *            a String specifying the name of the requested properties
     *            resource
     * @return the standard Properties object representing the properties inside
     *         the requested resource
     * @throws IOException
     *             if an error occurred when reading the resource
     * @throws ResourceNotFoundException
     *             if the resource can not be found
     */
    public static TypedProperties getResourceAsTypedProperties(String resourceName) throws IOException,
            ResourceNotFoundException {
        return new TypedProperties(getResourceAsStream(resourceName));
    }

    /**
     * Stops this class being constructed unnecessarily
     */
    private ResourceUtil() {
    }

}
