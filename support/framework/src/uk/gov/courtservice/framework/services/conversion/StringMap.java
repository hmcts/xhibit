package uk.gov.courtservice.framework.services.conversion;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * A map from Strings to Strings. Basically, a Properties, with extra methods
 * including parsing, formating, and bean introspection.
 * 
 * @author Nick Lawson
 * @author Pete Raymond Originally written for workstep by Nick Lawson this
 *         class has been updated and ammended by Pete Raymond
 * 
 */
public class StringMap {

    private final HashMap map = new HashMap();

    private Logger log = CSServices.getLogger(StringMap.class);

    private static String NULL_STRING = "null";

    private final ValueConverter converter;

    public StringMap(ValueConverter converter) // throws
    // CSConfigurationException
    {
        this.converter = converter;
    }

    public int size() {
        return map.size();
    }

    /**
     * Remove all entries.
     */
    public void clear() {
        map.clear();
    }

    public boolean containsName(String name) {
        return map.containsKey(name);
    }

    /**
     * Returns an iterator over the names in the map. remove() is supported by
     * the iterator.
     */
    public Iterator names() {
        return map.keySet().iterator();
    }

    /**
     * Add a String.
     */
    public void putString(String name, String value) {
        map.put(name, value);
    }

    /**
     * Get a String.
     */
    public String getString(String name) {
        String stringValue = (String) map.get(name);
        if (stringValue != null)
            return (String) map.get(name);
        else
            return "";
    }

    /**
     * Synonym for putString().
     */
    public void put(String name, String value) {
        map.put(name, value);
    }

    /**
     * Synonym for getString().
     */
    public String get(String name) {
        String stringValue = (String) map.get(name);
        if (stringValue != null)
            return (String) map.get(name);
        else
            return "";
    }

    public ArrayList getIndexedStrings(String name) {
        return (ArrayList) map.get(name);
    }

    /**
     * Synonym for getString().
     */
    public String getIndexedString(String name, int index) {
        ArrayList stringValues = (ArrayList) map.get(name);
        String value = null;
        if ((stringValues != null) && (index <= stringValues.size())) {
            value = (String) stringValues.get(index);
        }
        if (value != null)
            return value;
        else
            return "";
    }

    public ArrayList getValuesAsMaps(ArrayList values) throws CSUnrecoverableException {
        putList("tmpKey", values);
        return getInnerMaps("tmpKey");
    }

    /**
     * Processes arraylists of vos and creates a map for each. Stores the
     * created maps in the innerMap HashMap. These are accessed via
     * getInnerMap()
     */
    public void putList(String name, ArrayList list) throws CSUnrecoverableException {
        if ((list == null) || (list.size() == 0))
            return;
        // get the first item and check its a value
        Object obj = list.get(0);
        if ((obj instanceof CSValueObject) == false)
            return;
        CSValueObject value = null;
        StringMap innerMap = null;
        ArrayList innerMaps = new ArrayList();
        Iterator itr = list.iterator();
        while ((itr != null) && (itr.hasNext())) {
            value = (CSValueObject) itr.next();
            innerMap = new StringMap(converter);
            innerMap.copyValueToMap(value);
            innerMaps.add(innerMap);
        }
        map.put(name, innerMaps);
    }

    public void addInnerMap(String name, StringMap innerMap) {
        Object obj = map.get(name);
        ArrayList inMaps = null;
        if (obj == null) {
            inMaps = new ArrayList();
        } else {
            inMaps = (ArrayList) obj;
        }
        inMaps.add(innerMap);
        map.put(name, inMaps);
    }

    public ArrayList getInnerMaps(String name) {
        Object obj = map.get(name);
        if (obj != null)
            return (ArrayList) obj;
        else
            return new ArrayList(0);
    }

    public void putIndexedString(String name, String value) {
        // check if there is already an arraylist for this name
        ArrayList stringValues = (ArrayList) map.get(name);
        if (stringValues == null) {
            stringValues = new ArrayList();
            stringValues.add(value);
        } else {
            stringValues.add(value);
        }
        map.put(name, stringValues);
    }

    /**
     * Set a boolean value. The boolean value is formatted using the supplied
     * converter.
     */
    public void putBoolean(String name, boolean value) {
        map.put(name, converter.formatBoolean(value));
    }

    /**
     * Get a boolean value. The string value is parsed using the supplied
     * converter. Returns false if there is no entry for the name, or it is
     * null.
     */
    public boolean getBoolean(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseBoolean(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Boolean.TYPE, stringValue, e);
            }
        }
        return false;
    }

    /**
     * Set a char value. The value is formatted as a String of length 1.
     */
    public void putChar(String name, char value) {
        map.put(name, String.valueOf(value));
    }

    /**
     * Get a char value. Returns \0 if there is no entry for the name, or it is
     * null. Otherwise, if the length is exactly 1, return the only char.
     * Otherwise throw a parse exception.
     */
    public char getChar(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            if (stringValue.length() == 1) {
                return stringValue.charAt(0);
            }
            throw new ValueConvertException(name, Character.TYPE, stringValue);
        }
        return '\0';
    }

    /**
     * Set a byte value. The value is formatted using the supplied converter.
     */
    public void putByte(String name, byte value) {
        map.put(name, converter.formatByte(value));
    }

    /**
     * Get an byte value. The string value is parsed using the supplied
     * converter. Returns 0 if there is no entry for the name, or it is null.
     */
    public byte getByte(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseByte(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Byte.TYPE, stringValue, e);
            }
        }
        return 0;
    }

    /**
     * Set a short value. The value is formatted using the supplied converter.
     */
    public void putShort(String name, short value) {
        map.put(name, converter.formatShort(value));
    }

    /**
     * Get a short value. The string value is parsed using the supplied
     * converter. Returns 0 if there is no entry for the name, or it is null.
     */
    public short getShort(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseShort(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Short.TYPE, stringValue, e);
            }
        }
        return 0;
    }

    /**
     * Set an int value. The int value is formatted using the supplied
     * converter.
     */
    public void putInt(String name, int value) {
        map.put(name, converter.formatInt(value));
    }

    /**
     * Get an int value. The string value is parsed using the supplied
     * converter. Returns 0 if there is no entry for the name, or it is null.
     */
    public int getInt(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseInt(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Integer.TYPE, stringValue, e);
            }
        }
        return 0;
    }

    /**
     * Set a long value. The value is formatted using the supplied converter.
     */
    public void putLong(String name, long value) {
        map.put(name, converter.formatLong(value));
    }

    /**
     * Get a long value. The string value is parsed using the supplied
     * converter. Returns 0 if there is no entry for the name, or it is null.
     */
    public long getLong(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseLong(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Long.TYPE, stringValue, e);
            }
        }
        return 0;
    }

    /**
     * Set a float value. The value is formatted using the supplied converter.
     */
    public void putFloat(String name, float value) {
        map.put(name, converter.formatFloat(value));
    }

    /**
     * Get a float value. The string value is parsed using the supplied
     * converter. Returns 0.00 if there is no entry for the name, or it is null.
     */
    public float getFloat(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseFloat(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Float.TYPE, stringValue, e);
            }
        }
        return 0.00f;
    }

    /**
     * Set a double value. The double value is formatted using the supplied
     * converter.
     */
    public void putDouble(String name, double value) {
        map.put(name, converter.formatDouble(value));
    }

    /**
     * Get a double value. The string value is parsed using the supplied
     * converter. Returns 0.00 if there is no entry for the name, or it is null.
     */
    public double getDouble(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseDouble(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, Double.TYPE, stringValue, e);
            }
        }
        return 0.00;
    }

    /**
     * Set a Date value. The Date value is formatted using the supplied
     * converter.
     */
    public void putDate(String name, java.util.Date value) {
        map.put(name, converter.formatDate(value));
    }

    /**
     * Get a Date value. The string value is parsed using the supplied
     * converter. Returns null if there is no entry for the name, or it is null.
     */
    public java.util.Date getDate(String name) throws ValueConvertException {
        String stringValue = (String) map.get(name);
        if (stringValue != null) {
            try {
                return converter.parseDate(stringValue);
            } catch (ValueConvertException e) {
                CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                throw new ValueConvertException(name, java.util.Date.class, stringValue, e);
            }
        }
        return null;
    }

    /**
     * Get a bean's properties. This copies properties from here to the bean.
     * 
     * Same as
     * {@link #getBeanProperties(java.lang.Object,java.lang.String) getBeanProperties}
     * with a zero-length prefix.
     * 
     * 
     * @return the supplied bean.
     * @throws ValueConvertException
     *             if a non-null value wont parse.
     * @throws CSException
     *             if an Exception is thrown by the bean's setter method.
     */
    public Object copyMapToValue(Object bean) throws ValueConvertException, CSUnrecoverableException {
        Object val = null;

        val = copyMapToValue(bean, "");

        return val;
    }

    /**
     * Get a bean's properties. This copies properties from here to the bean.
     * 
     * A property is copied if it has a simple setter, is a boolean, char, byte,
     * short, int, long, float, double, String or Date, and the prefix +
     * property name map to a non-null value. Otherwise the bean property is
     * unchanged.
     * 
     * @param bean
     *            the Bean whose properties are updated.
     * @param prefix
     *            the prefix of bean property names in the map.
     * @return the supplied bean.
     * @throws ValueConvertException
     *             if a non-null value wont parse.
     * @throws CSException
     *             if an Exception is thrown by the bean's setter method.
     */
    public Object copyMapToValue(Object bean, String prefix) throws ValueConvertException, CSUnrecoverableException {

        Method[] methods = bean.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            String methodName = m.getName();
            Class[] types = m.getParameterTypes();
            int modifier = m.getModifiers();
            if (methodName.startsWith("set") && methodName.length() > 3 && Modifier.isPublic(modifier)
                    && types.length == 1) {
                String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                Class type = types[0];
                String stringValue = null;
                if (type == java.lang.String.class) {
                    stringValue = (String) map.get(prefix + propertyName);
                } else if (type == java.util.ArrayList.class) {
                    log.debug("ArrayList found: not processing");
                }
                if (stringValue != null) {
                    Object objectValue = null;

                    try {
                        if (type == String.class) {
                            objectValue = stringValue;
                        } else if (type == Boolean.TYPE) {
                            objectValue = new Boolean(converter.parseBoolean(stringValue));
                        } else if (type == Character.TYPE) {
                            if (stringValue.length() != 1) {
                                throw new CSUnrecoverableException();
                            }
                            objectValue = new Character(stringValue.charAt(0));
                        } else if (type == Byte.TYPE) {
                            objectValue = new Byte(converter.parseByte(stringValue));
                        } else if (type == Short.TYPE) {
                            objectValue = new Short(converter.parseShort(stringValue));
                        } else if (type == Integer.TYPE) {
                            objectValue = new Integer(converter.parseInt(stringValue));
                        } else if (type == Long.TYPE) {
                            objectValue = new Long(converter.parseLong(stringValue));
                        } else if (type == Float.TYPE) {
                            objectValue = new Double(converter.parseDouble(stringValue));
                        } else if (type == Double.TYPE) {
                            objectValue = new Double(converter.parseDouble(stringValue));
                        } else if (type == java.util.Date.class) {
                            objectValue = converter.parseDate(stringValue);
                        }

                    } catch (ValueConvertException e) {
                        CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                        throw new ValueConvertException(propertyName, type, stringValue, e);
                    }
                    if (objectValue != null) {
                        try {
                            log.debug("\t type=" + type + " " + propertyName + "=" + objectValue);
                            m.invoke(bean, new Object[] { objectValue });
                        } catch (InvocationTargetException e) {
                            // put this error back in if it is indeed a
                            // fatal exception
                            // throw new
                            // CSUnrecoverableException(e.getTargetException());
                            CSUnrecoverableException ex = new CSUnrecoverableException(e.getTargetException());
                            CSServices.getDefaultErrorHandler().handleError(ex, StringMap.class);
                            throw ex;
                        } catch (IllegalAccessException e) {
                            // impossible
                            CSUnrecoverableException ex = new CSUnrecoverableException(e);
                            CSServices.getDefaultErrorHandler().handleError(e, StringMap.class);
                            // e.printStackTrace();
                            throw ex;
                        }
                    }
                }
            }
        }
        return bean;
    }

    /**
     * Put a bean's properties.
     * 
     * This copies properties from the bean to here. Same as
     * {@link #putBeanProperties(java.lang.Object,java.lang.String) putBeanProperties()}
     * with a zero-length prefix.
     * 
     * @throws CSException
     *             if an Exception is thrown by the bean's getter method.
     */
    public void copyValueToMap(Object bean) throws CSUnrecoverableException {
        copyValueToMap(bean, "");

    }

    /**
     * Put a bean's properties.
     * 
     * This copies properties from the bean to here. A property is copied if it
     * has a simple getter, and is a boolean, char, byte, short, int, long,
     * float, double, String or Date.
     * 
     * Properties are formatted using the supplied converter.
     * 
     * @param bean
     *            whose properties are copied.
     * @param prefix
     *            attached to each property name.
     * @throws CSException
     *             if an Exception is thrown by the bean's getter method.
     * 
     * Updated 26 / 6 /2002 Pete Raymond Added check for class type and class
     * class during conversion e.g. (type == Integer.TYPE) || (type ==
     * Integer.class) following discovery that return type of Integer is only
     * matched by type == Integer.class
     */
    public void copyValueToMap(Object bean, String prefix) throws CSUnrecoverableException {
        Method[] methods = bean.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            String methodName = m.getName();
            // System.out.println("method=" + methodName);
            Class[] types = m.getParameterTypes();
            int modifier = m.getModifiers();
            if (methodName.startsWith("get") && methodName.length() > 3 && Modifier.isPublic(modifier)
                    && types.length == 0) {

                String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                Class type = m.getReturnType();

                String stringValue = null;
                Object[] args = new Object[] {};
                try {
                    if (type == String.class) {
                        stringValue = (String) m.invoke(bean, args);
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
                        Boolean boolValue = (Boolean) m.invoke(bean, args);
                        if (boolValue != null) {
                            stringValue = converter.formatBoolean(boolValue.booleanValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Byte.TYPE) || (type == Byte.class)) {
                        Byte byteValue = (Byte) m.invoke(bean, args);
                        if (byteValue != null) {
                            stringValue = converter.formatByte(byteValue.byteValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Character.TYPE) || (type == Character.class)) {
                        Character charValue = (Character) m.invoke(bean, args);
                        if (charValue != null) {
                            stringValue = charValue.toString();
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Short.TYPE) || (type == Short.class)) {
                        Short shortValue = (Short) m.invoke(bean, args);
                        if (shortValue != null) {
                            stringValue = converter.formatShort(shortValue.shortValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Integer.TYPE) || (type == Integer.class)) {
                        Integer intValue = (Integer) m.invoke(bean, args);
                        if (intValue != null) {
                            stringValue = converter.formatInt(intValue.intValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        // System.out.println("integer string value=" +
                        // stringValue);
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Long.TYPE) || (type == Long.class)) {
                        Long longValue = (Long) m.invoke(bean, args);
                        if (longValue != null) {
                            stringValue = converter.formatLong(longValue.longValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Float.TYPE) || (type == Float.class)) {
                        Float floatValue = (Float) m.invoke(bean, args);
                        if (floatValue != null) {
                            stringValue = converter.formatFloat(floatValue.floatValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if ((type == Double.TYPE) || (type == Double.class)) {
                        Double doubleValue = (Double) m.invoke(bean, args);
                        if (doubleValue != null) {
                            stringValue = converter.formatDouble(doubleValue.doubleValue());
                        } else {
                            stringValue = NULL_STRING;
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if (type == java.util.Date.class) {
                        java.util.Date dateValue = (java.util.Date) m.invoke(bean, args);

                        if (dateValue != null) {
                            stringValue = converter.formatDate(dateValue);
                        }
                        map.put(prefix + propertyName, stringValue);
                    } else if (type == java.util.ArrayList.class) {
                        ArrayList list = (ArrayList) m.invoke(bean, args);
                        putList(propertyName, list);

                    }
                } catch (InvocationTargetException e) {
                    // put this error back in if it is indeed a fatal
                    // exception
                    // throw new
                    // CSUnrecoverableException(e.getTargetException());
                    CSUnrecoverableException ex = new CSUnrecoverableException(e.getTargetException());
                    CSServices.getDefaultErrorHandler().handleError(ex, StringMap.class);
                    throw ex;
                } catch (IllegalAccessException e) {
                    CSUnrecoverableException ex = new CSUnrecoverableException(e);
                    CSServices.getDefaultErrorHandler().handleError(ex, StringMap.class);
                    throw ex;
                }
            }
        }
    }

    private void putValue(String name, String value) {
        if (value != null) {
            value = value.trim();
            if (value.length() > 0) {
                this.put(name, value);
            }
        }
    }

    /**
     * Show all names and vos on the printer.
     */
    public void list(PrintWriter w) {
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            w.println("\t" + entry.getKey() + ": \"" + entry.getValue() + "\"");
        }
    }

    /**
     * Show all names and vos on the printer.
     */
    public void list(PrintStream s) {
        list(new PrintWriter(s, true));
    }

    /**
     * Show all names and vos on System.out.
     */
    public void list() {
        list(new PrintWriter(System.out, true));
    }

    /*
     * Returns a ordered string in form of "field=value;.......;field=value"
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator entries = map.entrySet().iterator();
        TreeSet treeSet = new TreeSet();

        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            treeSet.add(entry.getKey() + "=" + entry.getValue());
        }

        Iterator elements = treeSet.iterator();
        int pos = 0;
        while (elements.hasNext()) {
            pos++;
            String s = (String) elements.next();
            /*
             * if(pos == treeSet.size() - 1) { buf.append(s); break; }
             */
            buf.append(s + ";");
        }

        return buf.toString();
    }

}