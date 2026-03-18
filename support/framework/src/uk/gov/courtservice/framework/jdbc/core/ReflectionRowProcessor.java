package uk.gov.courtservice.framework.jdbc.core;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import uk.gov.courtservice.framework.jdbc.core.columneditor.ColumnExtractionStrategy;
import uk.gov.courtservice.framework.jdbc.exception.PropertyExtractionException;
import uk.gov.courtservice.framework.jdbc.exception.UnableToReadBindingException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Processes result sets to generate Java bean instances. The class
 * can be driven by a bindings file that maps bean properties to resultset
 * columns. If the mapping is not specified the class assumes the bean property
 * name is same as the resultset column name. <font color="red">This class is
 * not thread safe</font>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class ReflectionRowProcessor extends AbstractRowProcessor {

    // Property name for getClass
    private static final String CLASS = "class";

    // Property name for getUpdateCount
    private static final String UPDATE_COUNT = "updateCount";

    // Property name for isDirty
    private static final String DIRTY = "dirty";
    
    // The target bean object
    protected Object target;

    // Target class
    private Class targetClass;

    // Class of the target
    private PropertyDescriptor[] descriptors;

    // Map of properties to column indices
    protected Map props = new HashMap();

    // Results
    private List results = new ArrayList();

    /**
     * Constructor initializes the target object
     * 
     * @param targetClass
     */
    public ReflectionRowProcessor(Class clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("val");

        try {
            // Set the target class
            targetClass = clazz;

            // Store the descriptors
            descriptors = Introspector.getBeanInfo(getTargetClass()).getPropertyDescriptors();

        } catch (IntrospectionException ex) {
            throw new PropertyExtractionException(ex.getMessage(), ex);
        }

    }

    /**
     * Constructor initializes the target object
     * 
     * @param targetClass
     * @param Bindings
     *            file
     */
    public ReflectionRowProcessor(Class clazz, String bindingsFile) {
        this(clazz);
        registerProperties(ReflectionRowProcessor.readBinding(bindingsFile));
    }

    /**
     * This method registers the default bindings by using a simple algorithm to
     * map bean properties to resultset column names
     * 
     */
    public void registerDefaultBindings() {

        for (int i = 0; descriptors != null && i < descriptors.length; i++) {
            String propName = descriptors[i].getName();
            if (!propName.equalsIgnoreCase(CLASS) && !propName.equalsIgnoreCase(UPDATE_COUNT)
            		&& !propName.equalsIgnoreCase(DIRTY)
                    && descriptors[i].getWriteMethod() != null) {
                // convert the column name to the SQL standard
                registerProperty(propName, convertColumnName(propName));
            }
        }

    }

    /**
     * Used to convert a variable name based on the java convention (e.g.
     * myVariable) to the SQL name convention employed (e.g. my_variable)
     * 
     * @param variableName
     *            The <code>String</code> value of the name of the instance
     *            variable contained in the java value object
     * @return A <code>String</code> containing the converted column name,
     *         note that the <b>case is not guaranteed</b>
     * @author Andrew Turner
     */
    private static final String convertColumnName(final String variableName) {
        // only called internally, so we know that the passed in parameter is
        // not going to be null!
        final StringBuffer returnBuffer = new StringBuffer(variableName.length() + 5);

        for (int i = 0; i < variableName.length(); i++) {
            final char charAt = variableName.charAt(i);

            if (Character.isUpperCase(charAt)) {
                returnBuffer.append('_');
            } // end of: if (Character.isUpperCase(charAt)) {

            returnBuffer.append(charAt);
        } // end of: for (int i = 0; i < variableName.length(); i++) {

        return returnBuffer.toString();
    }

    /**
     * Row processor implementation
     * 
     * @param row
     */
    public void processRow(Row row) {

        String property = null;
        PropertyMap propertyMap = null;

        try {

            // Pre-processing
            preProcessRow(row);

            // Instantiate the target object
        	target = getTargetClass().newInstance();
            // Get the iterator to the properties
            Iterator it = props.keySet().iterator();

            while (it.hasNext()) {

                property = (String) it.next();
                // Get the property map
                propertyMap = (PropertyMap) props.get(property);
                String colName = propertyMap.getColName();

                Class type = propertyMap.getType();
                ColumnExtractionStrategy strategy = ColumnExtractionStrategy.getStrategy(type);

                Object args[] = new Object[] { strategy.getValue(row, colName) };

                propertyMap.getWriteMethod().invoke(target, args);

            }

            // Add the target to results
            results.add(target);

            // Post-processing
            postProcessRow(row);

        } catch (IllegalAccessException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        } catch (InstantiationException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        } catch (InvocationTargetException ex) {
            String msg = property + ";" + propertyMap + ";" + ex.getMessage();
            throw new PropertyExtractionException(msg, ex);
        }

    }

    /**
     * Returns the populated object
     * 
     * @return
     */
    public Object getObject() {
        return target;
    }

    /**
     * Returns the current results
     * 
     * @return
     */
    public List getResults() {
        return results;
    }

    /**
     * Registers the property for the bean
     * 
     * @param propertyName
     * @param colIndex
     */
    public void registerProperty(String propertyName, String colName) {

        if (propertyName == null)
            throw new IllegalArgumentException("propertyName");
        if (colName == null)
            throw new IllegalArgumentException("colName");

        // Cache the write method
        for (int i = 0; i < getDescriptors().length; i++) {

            if (getDescriptors()[i].getName().equals(propertyName)) {

                Method wMethod = getDescriptors()[i].getWriteMethod();
                Class type = getDescriptors()[i].getPropertyType();

                props.put(propertyName, new PropertyMap(wMethod, colName, type));
                return;
            }

        }

        throw new PropertyExtractionException("Specified property not found: " + propertyName + " on "
                + getTargetClass());

    }

    /**
     * Registers the property for the bean
     * 
     * @param propertyName
     * @param colIndex
     */
    public void registerProperties(Properties props) {

        if (props == null)
            throw new IllegalArgumentException("props");

        Enumeration enumeration = props.keys();

        // Register the properties
        while (enumeration.hasMoreElements()) {
            String propName = (String) enumeration.nextElement();
            String colName = (String) props.get(propName);
            registerProperty(propName, colName);
        }

    }

    /**
     * This is a utility method that can be used by sibclasses to read binding
     * information from the classpath
     * 
     * @param bindingResource
     * @return
     */
    protected static Properties readBinding(String bindingResource) {

        InputStream input = null;

        try {

            Properties prop = new Properties();
            ClassLoader cl = ReflectionRowProcessor.class.getClassLoader();
            input = cl.getResourceAsStream(bindingResource);
            if (input == null)
                throw new UnableToReadBindingException(bindingResource + " not found");

            prop.load(input);

            return prop;

        } catch (IOException ex) {
            throw new UnableToReadBindingException(ex.getMessage(), ex);
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
    }

    /**
     * Gets the target class
     * 
     * @return
     */
    protected Class getTargetClass() {
        return targetClass;
    }

    /**
     * Gets the property descriptors
     * 
     * @return
     */
    protected PropertyDescriptor[] getDescriptors() {
        return descriptors;
    }

    // /Stores the col name and write method for a property</p>/
    protected static class PropertyMap {

        // Write method
        private Method writeMethod;

        // Column index
        private String colName;

        // Property class
        private Class type;

        // Initializes the write method and column index
        PropertyMap(Method val1, String val2, Class val3) {
            writeMethod = val1;
            colName = val2;
            type = val3;
        }

        // Gets the write method
        Method getWriteMethod() {
            return writeMethod;
        }

        // Gets the column name
        String getColName() {
            return colName;
        }

        // Gets the property class
        Class getType() {
            return type;
        }

        // Pretty print
        public String toString() {
            return type + ";" + colName;
        }

    }

}