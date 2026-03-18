package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;

/**
 * Abstract helper class to implement common functionality.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.9 $
 */
public abstract class AbstractHelper {
    protected static final String METHOD_ENTER = "Entered: ";

    protected static final String METHOD_EXIT = "Exited: ";

    // private HashMap used to contain the cached reflection details
    // for the value objects
    private static final HashMap<String, Property[]> valueObjects = new HashMap<String, Property[]>();

    protected final Logger log = CSServices.getLogger(getClass());

    public AbstractHelper() {
        // Empty
    }

    /**
     * Handles a finder exception by rethrowing as an EJBException.
     * <p>
     * Logs it too.
     * </p>
     * 
     * @param message
     *            String
     * @param originalException
     *            Exception
     * @return javax.ejb.EJBException
     */
    protected EJBException handleException(String message, Exception originalException) {
        this.handleError(originalException, message);
        CSUnrecoverableException unrecoverable = new CSUnrecoverableException(message, originalException);
        return new EJBException(unrecoverable);
    }

    /**
     * Forward call to CSServices...
     */
    private void handleError(Exception anException, String message) {
        CSServices.getDefaultErrorHandler().handleError(anException, AbstractHelper.class.getClass(), message);
    }

    /**
     * A new Instance of the default collection type underlying the Collection
     * Interface.
     * <p>
     * If I have chosen the wrong type of Collection implementation, replace
     * ArrayList here.
     * </p>
     * 
     * @return java.util.ArrayList
     */
    protected Collection<CSAbstractValue> newCollection() {
        return new ArrayList<CSAbstractValue>();
    }

    /*
     * The following sucks, I know, but it is late and these two exceptions are
     * causing f=grief. They don't need to exist separately as they mean the
     * same thing in the two different controllers. I wish there was only one,
     * but it is too late now.
     */
    protected BisRefControllerException buildBisObjectNotFoundException(String errorCode,
            AbstractSearchCriteria criteria, ObjectNotFoundException anException) {
        return new BisRefControllerException(errorCode, "Object with key [" + criteria.getPrimaryKey() + "] not found",
                anException);
    }

    protected SysRefControllerException buildSysObjectNotFoundException(String errorCode,
            AbstractSearchCriteria criteria, ObjectNotFoundException anException) {
        return new SysRefControllerException(errorCode, "Object with key [" + criteria.getPrimaryKey() + "] not found",
                anException);
    }

    /**
     * Copy the properties from one value object to another
     * 
     * @param from
     *            the VO to copy values from
     * @param to
     *            the VO to copy values to
     */
    public void copyVO(CSAbstractValue from, CSAbstractValue to) {
        // validate the passed in parameters
        if (from == null)
            throw new IllegalArgumentException("from == null");

        if (to == null)
            throw new IllegalArgumentException("to == null");

        // cache here as we reference a few times
        final Class fromClass = from.getClass();

        // determine if we can copy the values, are they assignment-compatible?
        if (fromClass.isInstance(to) == false)
            throw new IllegalArgumentException("from and to are not assignment-compatible");

        // get the properties from the cache, or create into cache
        final Property[] properties = getProperties(fromClass);

        try {
            for (int i = 0; i < properties.length; i++) {
                properties[i].copyProperty(from, to);
            } // end of: for (int i = 0; i < properties.length; i++) {
        } catch (final IllegalAccessException e) {
            throw new CSUnrecoverableException(e);
        } catch (final InvocationTargetException e) {
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Accessor method for the <code>Property[]</code> that holds the cached
     * reflection details for the properties of the value object. If not found
     * in the cache, they will get dynamically loaded, and then put into the
     * cache.
     * 
     * @param fromClass
     *            The <code>Class</code> Object that we wish to get the
     *            properties for
     * @return A <code>Property[]</code> containing the <cope>Propery</code>
     *         Object for each property of the value object
     */
    private static Property[] getProperties(Class fromClass) {
        final String fromClassName = fromClass.getName();

        // first attempt to read the properties from the cache
        Property[] properties = valueObjects.get(fromClassName);

        // the properties have not been cached previously, so read & cache now
        if (properties == null) {
            try {
                final PropertyDescriptor[] descriptors = Introspector.getBeanInfo(fromClass).getPropertyDescriptors();

                properties = new Property[descriptors.length];

                for (int i = 0; i < descriptors.length; i++) {
                    final String methodName = descriptors[i].getName();
                    final Method readMethod = descriptors[i].getReadMethod();
                    final Method writeMethod = descriptors[i].getWriteMethod();

                    properties[i] = new Property(methodName, readMethod, writeMethod);
                } // end of: for (int i = 0; i < descriptors.length; i++)
                // {

                // now copy the properties to the cache
                valueObjects.put(fromClassName, properties);
            } catch (final IntrospectionException e) {
                throw new CSUnrecoverableException(e);
            }
        }

        return properties;
    }

    /**
     * Local class used to store the properties for a value objects properties.
     * It holds a cached reference to the read and write methods, and the
     * property name.
     */
    static class Property {
        
        private final String name;

        private final Method readMethod;

        private final Method writeMethod;

        /**
         * Only constructor used to set up this immutable property
         * 
         * @param name
         *            The name of the property
         * @param readMethod
         *            The get method for the property
         * @param writeMethod
         *            The set method for the property
         */
        public Property(String name, Method readMethod, Method writeMethod) {
            this.name = name;
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
        }

        public String getName() {
            return this.name;
        }
        
        /**
         * Copy the property from one value object to another
         * 
         * @param from
         *            A subclass of <code>CSAbstractValue</code> that the
         *            readMethod is to be invoked on
         * @param to
         *            A subclass of <code>CSAbstractValue</code> that the
         *            writeMethod is to be invoked on
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         */
        public void copyProperty(CSAbstractValue from, CSAbstractValue to) throws InvocationTargetException,
                IllegalAccessException {
            if ((readMethod != null) && (writeMethod != null)) {
                // need to convert the return value to an Object array
                final Object[] readValues = new Object[] { readMethod.invoke(from, (Object[])null) };
                writeMethod.invoke(to, readValues);
            } 
        }
    }
}