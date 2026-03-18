package uk.gov.courtservice.framework.services;

import java.io.InputStream;
import java.util.Locale;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * <p>
 * Title: Resource Services
 * </p>
 * <p>
 * Description: Loads a resource (xsl, xml, images etc) for a given locale.
 * </p>
 * <p>
 * Use: Use ResourceServices.getInstance() to get an instance of the resource
 * services
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell (Xdevelopment 2003)
 * @version 1.0
 */

public class ResourceServices {

    /**
     * Lookup error key
     */

    private static final String LOOKUP_ERROR_KEY = "resourceservices.lookuperror";

    /**
     * The singleton instance
     */
    private static final ResourceServices instance = new ResourceServices();

    /**
     * Get the singleton
     * 
     * @return the ResourceServices singleton instance
     */
    public static ResourceServices getInstance() {
        return instance;
    }

    /**
     * Stop external construction of the singleton
     */
    private ResourceServices() {
    }

    /**
     * Get the resource for the default locale
     * 
     * @param name,
     *            the name of the resource to retrieve
     * @return an input stream to read the resource
     * @throws IllegalArgumentException
     *             if parameter is null
     * @throws CSUnrecoverableException
     *             if the resource can not be found
     */
    public InputStream getResourceAsStream(String name) throws CSUnrecoverableException {
        if (name == null) {
            throw new IllegalArgumentException("name: " + name);
        }
        return getResourceAsStream(name, Locale.getDefault());
    }

    /**
     * Get the resource for the specified locale
     * 
     * @param name,
     *            the name of the resource to retrieve
     * @param locale,
     *            the locale to use
     * @return an input stream to read the resource
     * @throws IllegalArgumentException
     *             if either parameter is null
     * @throws CSUnrecoverableException
     *             if the resource can not be found
     */
    public InputStream getResourceAsStream(String name, Locale locale) throws CSUnrecoverableException {
        if (name == null) {
            throw new IllegalArgumentException("name: " + name);
        }
        if (locale == null) {
            throw new IllegalArgumentException("locale: " + locale);
        }

        InputStream is = null;

        if (name.lastIndexOf(".") != -1) {
            String nameWithoutExtension = name.substring(0, name.lastIndexOf("."));
            String extension = name.substring(name.lastIndexOf("."));
            // System.out.println("Searching for : " + nameWithoutExtension
            // + "_" + locale.getLanguage() + "_" + locale.getCountry() +
            // extension);
            is = ResourceServices.class.getClassLoader().getResourceAsStream(
                    nameWithoutExtension + "_" + locale.getLanguage() + "_" + locale.getCountry() + extension);
            if (is != null) {
                return is;
            }
            // System.out.println("Searching for : " + nameWithoutExtension
            // + "_" + locale.getLanguage() + extension);
            is = ResourceServices.class.getClassLoader().getResourceAsStream(
                    nameWithoutExtension + "_" + locale.getLanguage() + extension);
            if (is != null) {
                return is;
            }
        } else {
            // System.out.println("Searching for : " + name + "_" +
            // locale.getLanguage() + "_" + locale.getCountry());
            is = ResourceServices.class.getClassLoader().getResourceAsStream(
                    name + "_" + locale.getLanguage() + "_" + locale.getCountry());
            if (is != null) {
                return is;
            }
            // System.out.println("Searching for : " + name + "_" +
            // locale.getLanguage());
            is = ResourceServices.class.getClassLoader().getResourceAsStream(name + "_" + locale.getLanguage());
            if (is != null) {
                return is;
            }
        }
        // System.out.println("Searching for : " + name);
        is = ResourceServices.class.getClassLoader().getResourceAsStream(name);
        if (is != null) {
            return is;
        }
        // EC: To Do

        // if name is Foo.txt and locale is new Locale("en", "GB");

        // lookup Foo_en_GB.txt if not found
        // lookup Foo_en.txt if not found
        // lookup Foo.txt if not found throw exception

        // if name is Foo and locale is new Locale("en", "GB");

        // lookup Foo_en_GB if not found
        // lookup Foo_en if not found
        // lookup Foo if not found throw exception

        // lookup code is ResourceServices.class.getResourceAsStream(name);

        throw new CSUnrecoverableException(new Message(LOOKUP_ERROR_KEY, new Object[] { name, locale }),
                "Could not find resource \"" + name + "\" in locale \"" + locale + "\"");
    }
}
