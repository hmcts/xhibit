package uk.gov.courtservice.framework.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:ReflectionEqualsHelper
 * </p>
 * <p>
 * Description: Provides a reflection implementation of the 'equals' method.
 * This helper is intended to be used for equating actual and expected test
 * result objects.
 * </p>
 * 
 * <p>
 * This helper could also be called from the 'equals' method on a
 * CSAbstractValue object. This helper could then be used to equate actual and
 * expected values objects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 17-10-2003 AW Daley Initial Version
 */
public class ReflectionEqualsHelper {
    private static Logger log = CSServices.getLogger(ReflectionEqualsHelper.class);

    /**
     * Equals method that uses reflection to determine details of the class to
     * be equated.
     * 
     * @param obj
     *            object to be equated
     * @param otherObj
     *            object to be equated
     * @return true if equals
     */
    public static boolean equals(Object obj, Object otherObj) {
        Boolean res;
        String methodName = "equals";

        if (log.isDebugEnabled())
            log.debug("ReflectionEqualsHelper " + methodName + " entered:");

        if (otherObj.getClass() != obj.getClass()) {

            if (log.isDebugEnabled())
                log.debug(methodName + "Objects to equate are of different types");

            return false;
        }

        // Obtain all the fields of the class to compare.
        Field[] fields = obj.getClass().getDeclaredFields();

        PropertyDescriptor pd;
        try {
            // Attempts to call .equals method on each field.
            if (log.isDebugEnabled())
                log.debug(methodName + "Number of fields to equate: " + fields.length);

            for (int i = 0; i < fields.length; i++) {
                String fieldName = null;
                try {
                    // Get Method to read property...
                    fieldName = fields[i].getName();

                    if (log.isDebugEnabled())
                        log.debug(methodName + "Equating field: " + fieldName);

                    pd = new PropertyDescriptor(fieldName, otherObj.getClass());

                }

                // Thrown if property does not have getter/setter methods
                // a private attribute
                catch (IntrospectionException ie) {
                    // Process next field
                    if (log.isDebugEnabled())
                        log.debug(methodName + "Field " + fieldName + " does not have getters/setters");
                    continue;
                }

                Method meth = pd.getReadMethod();
                if (log.isDebugEnabled())
                    log.debug(methodName + " Read method for field " + fieldName + " " + meth.getName());

                // If null then field cannot be read move onto next field
                if (meth == null)
                    continue;

                // Get the 2 objects to compare on...
                Object res1 = meth.invoke(obj, null);
                Object res2 = meth.invoke(otherObj, null);

                // If both objects are null then they are equal
                if (res1 == null && res2 == null)
                    continue;

                // If one object is null and the other not then
                // they are not equal
                else if (res1 == null)
                    return false;

                else if (res2 == null)
                    return false;

                // Get the comparison method from the class hierarchy
                Method comparisonMethod = ReflectionHelper.getMethodFromClassHierarchy(res1.getClass(), methodName,
                        new Class[] { Object.class });

                if (comparisonMethod == null)
                    throw new NoSuchMethodException(methodName);

                // Invoke comparison method on fields
                res = (Boolean) comparisonMethod.invoke(res1, new Object[] { res2 });

                // Abort comparison if the values of a field do not equal.
                if (res.booleanValue() == false) {
                    if (log.isDebugEnabled())
                        log.debug(methodName + "Objects are NOT equal");

                    return false;
                }

            }

            // If gets here then all fields of the object are equal.
            if (log.isDebugEnabled())
                log.debug(methodName + "Objects are equal");

            return true;
        } catch (Exception ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, ReflectionEqualsHelper.class, ex.toString());
            throw new CSUnrecoverableException(ex);
        }
    }

}
