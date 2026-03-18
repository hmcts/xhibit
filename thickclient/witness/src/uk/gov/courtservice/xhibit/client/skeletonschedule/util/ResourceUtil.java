/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 14, 2003
 * Time: 11:47:22 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.exceptions.ResourceNotFoundException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Resource Util
 * </p>
 * <p>
 * Description: A bunch of utilities for manipulating resources (data stored in
 * files in the class path)
 * </p>
 * <p/>
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
    private static final Logger log = CSServices.getLogger(ResourceUtil.class);

    private static final String REPORT_TITLES = "skeletonschedule.print.title.resource";

    /**
     * <p>
     * Loads the requested resource using the current class's loader.
     * </p>
     * 
     * @param resourceName
     *            a String specifying the name of the requested resource
     * @return an InputStream to read data from the resource
     * @throws
     *         uk.gov.courtservice.xhibit.client.exceptions.ResourceNotFoundException
     *         if the resource can not be found
     */
    public static InputStream getResourceAsStream(String resourceName) throws ResourceNotFoundException {
        log.debug("$$$ resourceName " + resourceName);
        InputStream resourceInputStream = null;
        try {
            resourceInputStream = ResourceUtil.class.getResource(resourceName).openStream();
        } catch (IOException e) {
            log.debug(e, e); // To change body of catch statement use Options
            // | File Templates.
            log.debug("$$$ IOException " + e.getMessage());
            throw new ResourceNotFoundException(resourceName);
        }
        if (resourceInputStream != null) {
            return resourceInputStream;
        } else {
            log.debug("$$$ ResourceNotFoundException ");
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
     * Stops this class being constructed unnecessarily
     */
    private ResourceUtil() {
    }

    /**
     * <p>
     * Loads the standard Title resources from config.
     * </p>
     * 
     * @param witnessData
     *            data previously loaded
     */
    public static Map loadResources(Map witnessData) throws CSRecoverableException {
        try {
            HashMap data = (HashMap) witnessData;
            Properties messages = new Properties();
            log.debug("$$$ ResourceUtil.loadResources " + witnessData);
            messages.load(getResourceAsStream(getResource(REPORT_TITLES)));
            data.putAll(messages); // add the messages to the data hashmap
            // . . .
            return data;
            // byte[] content =
            // PrintTransformUtil.printTransform(getParameter("xslStyleSheet"),
            // data);
            // responseEnvironment.sendBytes("application/pdf",content);
        } catch (IOException e) {
            throw new CSRecoverableException("", "", e);
        }

    }

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }
}
