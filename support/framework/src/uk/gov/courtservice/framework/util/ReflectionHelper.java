package uk.gov.courtservice.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * <p>
 * Title: ReflectionHelper
 * </p>
 * <p>
 * Description: Reflection Helper class that searches class hierarchies for
 * specified methods and fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author AW Daley
 */
/*
 * Ref Date Author Description
 * 
 * 83,52539 29-04-2003 AW Daley Initial version.
 */
public class ReflectionHelper {
    /**
     * Returns a field given a path to the field.
     * 
     * @param cls
     *            The sub class in the hierarchy where the search is to begin
     * @param fieldPath
     *            The path of the field to return. A field contained in a
     *            hiearchy of objects can be returned by specifying each
     *            attribute name seperated by a delimiter. (e.g
     *            courtReporterFirm.firmName)
     * @param fieldPathDelimiter
     *            field path delimiter
     * @return Specified field
     * @throws NoSuchFieldException
     */
    public static Field getFieldFromFieldPath(Class cls, String fieldPath, String fieldPathDelimiter)
            throws NoSuchFieldException {
        Field requiredField = null;

        // Get Each Field Name
        String fieldNamesToParse = new String(fieldPath);
        StringTokenizer tokens = new StringTokenizer(fieldNamesToParse, fieldPathDelimiter);
        // If field names not found then throw Exception
        if (tokens.countTokens() == 0)
            throw new NoSuchFieldException(fieldPath);

        // If only one field
        if (tokens.countTokens() == 1)
            return getFieldFromClassHierarchy(cls, tokens.nextToken());

        // For each field
        while (tokens.hasMoreTokens()) {
            // Recursive
            requiredField = getFieldFromClassHierarchy(cls, tokens.nextToken());
            cls = requiredField.getType();
        }

        return requiredField;

    }

    /**
     * Returns the getter method for a field given the path to the field
     * 
     * @param fieldPath
     *            The path of the field to return. A field contained in a
     *            hiearchy of objects can be returned by specifying each
     *            attribute name seperated by a period. (e.g
     *            courtReporterFirm.firmName)
     * @param fieldPathDelimiter
     *            field path delimiter
     * @return Array of Getter methods to retreive the specified field
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     */
    public static Method[] getGetterMethodForFieldPath(Class cls, String fieldPath, String fieldPathDelimiter)
            throws NoSuchMethodException, NoSuchFieldException {
        ArrayList requiredMethods = new ArrayList();
        String methodName = null;
        Class[] parameterTypes = {};

        // Get Each Field Name
        String fieldNamesToParse = new String(fieldPath);
        StringTokenizer tokens = new StringTokenizer(fieldNamesToParse, fieldPathDelimiter);
        // If field names not found then throw Exception
        if (tokens.countTokens() == 0)
            throw new NoSuchFieldException(fieldPath);

        // If only one field then return its getter method
        if (tokens.countTokens() == 1) {
            methodName = buildGetterMethodName(tokens.nextToken());
            Method method = getMethodFromClassHierarchy(cls, methodName, parameterTypes);
            requiredMethods.add(method);

            return (Method[]) (requiredMethods.toArray(new Method[requiredMethods.size()]));
        }

        Field requiredField = null;
        String lastToken = null;

        // For each field
        Class clsType = cls;
        Class declaringCls = null;

        while (tokens.hasMoreTokens()) {
            // Recursive
            lastToken = new String(tokens.nextToken());
            requiredField = getFieldFromClassHierarchy(clsType, lastToken);
            clsType = requiredField.getType();

            // Get RequiredMethod for field
            methodName = buildGetterMethodName(lastToken);
            declaringCls = requiredField.getDeclaringClass();
            Method method = getMethodFromClassHierarchy(declaringCls, methodName, parameterTypes);
            requiredMethods.add(method);
        }

        return (Method[]) (requiredMethods.toArray(new Method[requiredMethods.size()]));

    }

    /**
     * Recursive method to traverse the class hierarchy searching for the
     * specified method.
     * 
     * @param class
     * @param methodName
     *            method name to serach for
     * @param parameterTypes
     *            parameter types
     * @return the specified method or null if not found
     * @throws NoSuchMethodException
     */
    public static Method getMethodFromClassHierarchy(Class cls, String methodName, Class[] parameterTypes)
            throws NoSuchMethodException {
        Method requiredMethod;

        try {

            requiredMethod = cls.getDeclaredMethod(methodName, parameterTypes);
        }

        catch (NoSuchMethodException nme) {
            // Return null if top of class structure
            if (cls.getSuperclass() != null)
                requiredMethod = getMethodFromClassHierarchy(cls.getSuperclass(), methodName, parameterTypes);
            else
                throw new NoSuchMethodException(methodName);
        }

        return requiredMethod;

    }

    /**
     * Recursive method to traverse the class hierarchy searching for the
     * specified field.
     * 
     * @param subClass
     *            The sub class in the hierarchy where the search is to begin
     * @param fieldName
     *            field to search for.
     * @return the specific field or null if not found
     * @throws NoSuchFieldException
     */
    public static Field getFieldFromClassHierarchy(Class cls, String fieldName) throws NoSuchFieldException {
        Field requiredField;

        try {

            requiredField = cls.getDeclaredField(fieldName);
        }

        catch (NoSuchFieldException nme) {
            // Return null if top of class structure
            if (cls.getSuperclass() != null)
                requiredField = getFieldFromClassHierarchy(cls.getSuperclass(), fieldName);
            else
                throw new NoSuchFieldException(fieldName);
        }

        return requiredField;

    }

    /**
     * Build getter method name for given field name.
     * 
     * @param fieldName
     *            field name for which getter method name is to be built
     * @return getter method name
     */
    private static String buildGetterMethodName(String fieldName) {
        return "get".concat(fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1, fieldName.length())));
    }

}