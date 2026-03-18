package uk.gov.courtservice.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Array;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/*
 * TO DO  Check if error handling is consistent with other framework classes.
 */

/**
 * This class is used for help when using reflection to set data on objects
 */
public class ClassAndMethodHelper {
    //The logger for this class.
    private static Logger log = CSServices.getLogger(ClassAndMethodHelper.class);

    /**
     * Generic method to execute a method on an executor object passing the
     * specified parameter
     *
     * @param parameter
     * @param methodName
     * @param executor
     * @return Object
     */

    public Object execute(Object parameter, String methodName, Object executor) {
        log.debug("***** execute parameter: " + parameter.getClass() + " methodName: " + methodName);

        Method m = null;
        Class c = null;

        if (parameter.getClass().getName().equals("java.util.GregorianCalendar")) {
            log.debug("***** parameter is GregorianCalendar so set the method parm to Calendar : "
                    + parameter.getClass());
            c = java.util.Calendar.class;
        } else {
            c = parameter.getClass();
        }

        Class[] methodParamTypes = { c };
        Object[] parameters = { parameter };

        try {
            m = executor.getClass().getMethod(methodName, methodParamTypes);
        } catch (NoSuchMethodException nsme) {
            log.debug("<< NoSuchMethodExceptionexecute executing on class : " + executor.getClass()
                    + " with parameter: " + parameter.getClass() + " methodName: " + methodName);

            CSServices.getDefaultErrorHandler().handleError(nsme, ClassAndMethodHelper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("No Such Method Exception when creating: " + methodName
                    + " method for: " + parameter.getClass(), nsme);
        }

        try {
            return m.invoke(executor, parameters);
        } catch (IllegalAccessException iae) {
            log.debug("<< IllegalAccessException executing on class : " + executor.getClass() + " with parameter: "
                    + parameter.getClass() + " methodName: " + methodName);

            CSServices.getDefaultErrorHandler().handleError(iae, ClassAndMethodHelper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("IllegalAccessException when invoking: " + methodName + " method for: "
                    + parameter.getClass(), iae);
        } catch (IllegalArgumentException iarge) {
            log.debug("<< IllegalArgumentException executing on class : " + executor.getClass() + " with parameter: "
                    + parameter.getClass() + " methodName: " + methodName);

            CSServices.getDefaultErrorHandler().handleError(iarge, ClassAndMethodHelper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("IllegalArgumentException when invoking: " + methodName
                    + " method for: " + parameter.getClass(), iarge);
        } catch (InvocationTargetException ite) {
            log.debug("<< InvocationTargetException executing on class : " + executor.getClass() + " with parameter: "
                    + parameter.getClass() + " methodName: " + methodName);

            CSServices.getDefaultErrorHandler().handleError(ite, ClassAndMethodHelper.class);

            log.debug("InvocationTargetException (RMI Exception) when invoking: " + methodName + " method for: "
                    + parameter.getClass() + " Exception was: " + ite);
            ite.printStackTrace();

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("InvocationTargetException (RMI Exception) when invoking: " + methodName
                    + " method for: " + parameter.getClass(), ite);
        } catch (Exception e) {
            log.debug("<< Exception executing on class : " + executor.getClass() + " with parameter: "
                    + parameter.getClass() + " methodName: " + methodName);

            CSServices.getDefaultErrorHandler().handleError(e, ClassAndMethodHelper.class);

            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("Exception when invoking: " + methodName + " method for: "
                    + parameter.getClass(), e);
        }
    }

    /**
     * determineMethodParameter: This returns an Object which will be passed as
     * a parameter to set the appropriate property data structure   
     *
     * This method is quite specialized and the following must be true:
     * 1. The Class argument has exactly one method with a name matching the methodName argument
     * 2. The method matching the methodName argument has itself exactly one argument
     * 3. The original property class determined from the propertyValue argument will be one of:
     * (a) Be an instance of the method argument in (2)
     * (b) Be applicable to be passed into a single argument constructor of the method argument's class
     * (c) Be a formatted (Oracle dates only) Date String when the method argument is a Calendar
     *
     * @param Class clazz
     * @param String methodName
     * @param String propertyValue
     * @return Object
     */

    public Object determineMethodParameter(Class clazz, String methodName, String propertyValue) {
        log.debug("determineMethodParameter, clazz: " + clazz);
        log.debug("determineMethodParameter, methodName: " + methodName);
        log.debug("determineMethodParameter, propertyValue: " + propertyValue);
        log.debug("determineMethodParameter, propertyValueClass: " + propertyValue.getClass());

        Method methodArray[] = clazz.getMethods();

        Method m = null;
        Class classArray[] = null;
        Class methodParameterClass = null;
        Class propertyValueClass = null;

        //Assumes the class will have exactly one method with the specified name and exactly one parameter
        //Otherwise we could hold the expected parameter type in the progressPropertyMapping instead

        for (int i = 0; i < Array.getLength(methodArray); i++) {
            m = (Method) methodArray[i];

            if (m.getName().trim().equals(methodName.trim())) {
                log.debug("Method match found: " + methodName);
                classArray = m.getParameterTypes();

                if (Array.getLength(classArray) == 1) {
                    log.debug("Appropriate parameter found");
                    methodParameterClass = classArray[0];
                    break;
                }
            }
        }

        if (methodParameterClass == null) {
            log.debug("determineMethodParameter, methodParameterClass was null: "
                    + " probably config error in progressPropertyMapping.txt or "
                    + " mismatch between JMS structure (ie database schema) and Web Service data object structure");

            // this is an configuration exception, not a business exception
            throw new CSUnrecoverableException("Failed to determine parameter class for method: " + methodName
                    + " on Class class: " + clazz + " probably config error in progressPropertyMapping.txt or "
                    + " mismatch between JMS structure (ie database schema) and Web Service data object structure");
        }

        log.debug("determineMethodParameter, methodParameterClass:" + methodParameterClass.getName());

        propertyValueClass = propertyValue.getClass();

        log.debug("determineMethodParameter, propertyValueClass:" + propertyValueClass.getName());

        if (propertyValueClass.getName().equals(methodParameterClass.getName())) {
            log
                    .debug("<< propertyValueClass is an instance of methodParameterClass so it can be used as a parameter >>");
            return propertyValue;
        }

        if (methodParameterClass.getName().equals("java.util.Calendar")) {
            log.debug("<< methodParameterClass is a Calendar >>");

            try {
                return DateTimeUtilities.processOracleDateParameter(propertyValue);
            } catch (Exception e) {
                // this is an unexpected exception, not a business exception
                throw new CSUnrecoverableException("Failed to create parameter for class: " + methodParameterClass
                        + " passing the constructor value class: " + propertyValue.getClass()
                        + " due to an error with the Calendar (either it was null or did not "
                        + " follow the Oracle Timestamp format)", e);
            }
        }

        if (methodParameterClass.getName().equals("java.util.Date")) {
            log.debug("<< methodParameterClass is a Date >>");

            try {
                return DateTimeUtilities.processOracleDateParameterForDate(propertyValue);
            } catch (Exception e) {
                // this is an unexpected exception, not a business exception
                throw new CSUnrecoverableException("Failed to create parameter for class: " + methodParameterClass
                        + " passing the constructor value class: " + propertyValue.getClass()
                        + " due to an error with the Date (either it was null or did not "
                        + " follow the Oracle Timestamp format)", e);
            }
        }

        try {
            log.debug("<< Construct object of methodParameterClass using propertyValue as parameter  >>");

            //methodParameterClass must have a constructor that takes a propertyValueClass as a single parameter

            //Array of classes containing the type of parameter to be passed to the constructor, which will always be a String
            Class constructorParmsClasses[] = { java.lang.String.class };

            Constructor methodParameterConstructor = methodParameterClass.getConstructor(constructorParmsClasses);
            log.debug("Created constructor of parameter:" + methodParameterConstructor.getName());

            Object constructorParms[] = { propertyValue };
            return methodParameterConstructor.newInstance(constructorParms);

        } catch (Exception e) {
            // this is an unexpected exception, not a business exception
            throw new CSUnrecoverableException("Failed to create parameter for class: " + methodParameterClass
                    + " passing the constructor value class: " + propertyValue.getClass()
                    + " probably config error in progressPropertyMapping.txt or "
                    + " mismatch between JMS structure (ie database schema) and Web Service data object structure", e);
        }
    }
}