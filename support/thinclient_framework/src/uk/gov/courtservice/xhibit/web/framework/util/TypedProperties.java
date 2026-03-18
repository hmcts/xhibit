package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Typed Properties
 * </p>
 * <p>
 * Description: Adds type checking (and sensible lookup) to the standard
 * properties collection
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.7 $ $Log:
 *         TypedProperties.java,v $ Revision 1.5 2004/03/11 16:42:15 qzd3k3 Code
 *         linting.
 * 
 * Revision 1.4.58.1 2004/03/10 15:46:52 qzd3k3 Code linting, most of these
 * fixes are poor exception handling. This may result as a side effect in more
 * bugs appearing; but that is a necessary as they were hidden bugs before.
 * 
 * Revision 1.4 2003/10/01 15:33:54 bzw8gp Jon Powell <p/> organise imports
 * (remove unused) <p/> Revision 1.3 2003/03/21 11:49:25 fz0n8j Revised
 * thinclient framework! <p/> Revision 1.4 2003/03/19 18:38:52 fz0n8j Framework
 * changes. <p/> Revision 1.3 2003/03/17 11:32:10 fz0n8j Added revision cvs
 * comments. ecawley <p/> Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log
 * comments - ecawley
 */
public class TypedProperties extends RuntimeException {

    private static final Logger log = CSServices.getLogger(TypedProperties.class);

    /**
     * The seperator for list properties
     */
    public static final String LIST_SEPERATOR = ";";

    /**
     * The seperator for sub properties
     */
    public static final String SUB_PROPERITES_SEPERATOR = ".";

    /**
     * The wrapped properties
     */
    private final Map wrapped;

    /**
     * Construct an empty typed properties collection
     */
    public TypedProperties() {
        wrapped = new HashMap();
    }

    /**
     * Construct a typed properties collection from the given stream
     * 
     * @param inputStream
     *            to construct properties from
     * @throws IllegalArgumentException
     *             if the inputStream is null
     * @throws IOException
     *             if an error occures reading from the stream
     */
    public TypedProperties(InputStream inputStream) throws IllegalArgumentException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream");
        }
        Properties properties = new Properties();
        properties.load(inputStream);
        wrapped = properties;
    }

    /**
     * Construct a typed properties collection from the given map
     * 
     * @param map
     *            the map to wrap
     * @throws IllegalArgumentException
     *             if the map is null
     */
    private TypedProperties(Map map) throws IllegalArgumentException {
        if (map == null) {
            throw new IllegalArgumentException("map");
        }
        wrapped = map;
    }

    /**
     * Returns the required property as a String
     * 
     * @param name
     *            the name of the property to retrieve
     * @return the requested property as a String
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws PropertyNotFoundException
     *             if the property is not found
     */
    public String getStringProperty(String name) throws IllegalArgumentException, PropertyNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        String property = (String) wrapped.get(name);
        if (property == null) {
            throw new PropertyNotFoundException(name);
        }
        return property.trim();
    }

    /**
     * Returns the required property as a String
     * 
     * @param name
     *            the name of the property to retrieve
     * @param altValue
     *            the value to return of the property does not exist
     * @return the requested property as a String
     * @throws IllegalArgumentException
     *             if either parameter is null
     */
    public String getStringProperty(String name, String altValue) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        if (altValue == null) {
            throw new IllegalArgumentException("altValue");
        }
        String property = (String) wrapped.get(name);
        if (property != null) {
            return property.trim();
        } else {
            return altValue;
        }
    }

    /**
     * Returns the required property as a List passed using the seperator
     * defined in LIST_SEPERATOR.
     * 
     * @param name
     *            the name of the property to retrieve
     * @return the requested property as a List
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws PropertyNotFoundException
     *             if the property is not found
     */
    public List getListProperty(String name) throws IllegalArgumentException, PropertyNotFoundException {
        return PrimitiveUtil.parseList(getStringProperty(name), LIST_SEPERATOR);
    }

    /**
     * Returns the required property as an iterator over a list passed using the
     * seperator defined in LIST_SEPERATOR.
     * 
     * @param name
     *            the name of the property to retrieve
     * @return the requested property as a List Iterator
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws PropertyNotFoundException
     *             if the property is not found
     */
    public Iterator getIteratorProperty(String name) throws IllegalArgumentException, PropertyNotFoundException {
        return getListProperty(name).iterator();
    }

    /**
     * Returns the required property as a Class
     * 
     * @param name
     *            the name of the property to retrieve
     * @return the requested property as a Class
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws PropertyNotFoundException
     *             if the property is not found
     * @throws PropertyFormatException
     *             if the property is not a valid class
     */
    public Class getClassProperty(String name) throws IllegalArgumentException, PropertyNotFoundException,
            PropertyFormatException {
        String property = getStringProperty(name);
        try {
            return Class.forName(property);
        } catch (ClassNotFoundException cnfe) {
            log.warn(cnfe);
            throw new PropertyFormatException(name, property, "java.lang.Class");
        }
    }

    /**
     * Return a map of sup properties for the given name
     * 
     * @return a String repsentation of the object useful for debugging
     */
    public TypedProperties getSubProperties(String name) {
        Map subProperties = new HashMap();

        String prefix = name + SUB_PROPERITES_SEPERATOR;
        int prefixLength = prefix.length();

        Iterator names = wrapped.keySet().iterator();
        while (names.hasNext()) {
            String current = (String) names.next();
            if (current.startsWith(prefix)) {
                subProperties.put(current.substring(prefixLength), wrapped.get(current));
            }
        }

        return new TypedProperties(subProperties);
    }

    /**
     * Get property names
     * 
     * @return an iterator over a list of property names.
     */
    public Iterator getPropertyNames() {
        return new SafeIterator(wrapped.keySet().iterator());
    }

    /**
     * Return a String repsentation of the object useful for debugging
     * 
     * @return a String repsentation of the object useful for debugging
     */
    public String toString() {
        Iterator nameIterator = wrapped.keySet().iterator();
        if (nameIterator.hasNext()) {
            StringBuffer buffer = new StringBuffer();
            String name = (String) nameIterator.next();
            buffer.append(name);
            buffer.append(": ");
            buffer.append((String) wrapped.get(name));
            while (nameIterator.hasNext()) {
                name = (String) nameIterator.next();
                buffer.append(PrimitiveUtil.NL);
                buffer.append(name);
                buffer.append(": ");
                buffer.append((String) wrapped.get(name));
            }
            return buffer.toString();
        } else {
            return "none";
        }
    }
}
