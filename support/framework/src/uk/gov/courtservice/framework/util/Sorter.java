package uk.gov.courtservice.framework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Sorter
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: Sorter.java,v 1.12 2006/07/14 10:26:06 bzjrnl Exp $
 */
/*
 * Ref Date Author Description
 * 
 * 315, 02-04-2003 AW Daley Sort modified to provide ascending 52305 or
 * descending order. compareTo method can now be on a superclass of the object
 * to be compared.
 * 
 * 83,52539 29-04-2003 AW Daley Modifed to use ReflectionHelper class.
 * 
 * 30-10-2003 AW Daley compare method modified to allow objects to be sorted
 * that contain null member attributes.
 */
public class Sorter {
    public static final Boolean ASCENDING = new Boolean(true);

    public static final Boolean DESCENDING = new Boolean(false);

    /**
     * Private constructor for the utility class
     */
    private Sorter() {
    }

    /**
     * Statis utility method to sort data
     * 
     * @param data:
     *            Data to be sorted
     * @param keys:
     *            Keys by which to be sorted
     */
    public static void sort(List data, final String keys[]) {

        Comparator cmp = new Sorter.ReflectionComparator(keys);

        Collections.sort(data, cmp);

    }

    /**
     * Statis utility method to sort data
     * 
     * @param data:
     *            Data to be sorted
     * @param keys:
     *            Keys by which to be sorted
     */
    public static void sort(Object[] data, final String keys[]) {

        Comparator cmp = new Sorter.ReflectionComparator(keys);

        Arrays.sort(data, cmp);

    }
    
    /**
     * Statis utility method to sort data
     * 
     * @param data:
     *            Array of Data to be sorted
     * @param keys:
     *            Keys by which to be sorted
     * @param ascending
     *            True - ascending order
     */
    public static void sort(Object data[], final String keys[], Boolean ascending) {
        Comparator cmp = new Sorter.ReflectionComparator(keys, ascending);

        Arrays.sort(data, cmp);
    }

    /**
     * Statis utility method to sort data
     * 
     * @param data:
     *            Collection of Data to be sorted
     * @param keys:
     *            Keys by which to be sorted
     * @param ascending
     *            True - ascending order
     */
    public static void sort(List data, final String keys[], Boolean ascending) {
        Comparator cmp = new Sorter.ReflectionComparator(keys, ascending);

        Collections.sort(data, cmp);
    }

    /**
     * Comparator implementation based on reflection
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * <p>
     * Copyright: Copyright (c) 2002
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Meeraj Kunnumpurath
     * @version 1.0
     */
    private static class ReflectionComparator implements Comparator {

        /**
         * Keys by which to sort
         */
        private String[] keys;

        /**
         * Specifies ascending or descending order
         */
        private Boolean ascending;

        /**
         * Constructor inityalizes the sort keys
         * 
         * @param keys
         */
        public ReflectionComparator(String[] keys) {
            this.keys = keys;
            setAscending(new Boolean(true));
        }

        /**
         * Constructor initializes the sort keys
         * 
         * @param ascending
         *            True - ascending
         */
        public ReflectionComparator(String[] keys, Boolean ascending) {
            this.keys = keys;
            setAscending(ascending);
        }

        /**
         * Sets ascending or descending order
         * 
         * @param ascending
         *            True - ascending
         */
        public void setAscending(Boolean ascending) {
            this.ascending = ascending;
        }

        /**
         * Gets ascending or descending order
         * 
         * @return True - ascending
         */
        public Boolean getAscending() {
            return this.ascending;
        }

        public boolean equals(Object obj) {
            return true;
        }

        /**
         * Compare method based on the compareTo implementation
         * 
         * @param obj1
         * @param obj2
         * @return
         */
        public int compare(Object obj1, Object obj2) {
            String methodName = "compare() - ";
            try {
                for (int i = 0; i < keys.length; i++) {
                    // Get Method to read property...
                    PropertyDescriptor pd1 = new PropertyDescriptor(keys[i], obj1.getClass());
                    Method meth1 = pd1.getReadMethod();
                    PropertyDescriptor pd2 = new PropertyDescriptor(keys[i], obj2.getClass());
                    Method meth2 = pd2.getReadMethod();

                    // Get the 2 objects to compare on...
                    Object res1 = meth1.invoke(obj1, null);
                    Object res2 = meth2.invoke(obj2, null);

                    // If both objects are null then they are equal
                    if (res1 == null && res2 == null)
                        continue;

                    // If one object is null and the other not then
                    // they are not equal
                    else if (res1 == null)
                        return adjustCompareResult(new Integer(-1));

                    else if (res2 == null)
                        return adjustCompareResult(new Integer(1));

                    /*
                     * Note compareTo can be on the class itself, or its
                     * superclasses
                     */
                    Method compareTo = ReflectionHelper.getMethodFromClassHierarchy(res1.getClass(), "compareTo",
                            new Class[] { Object.class });

                    // Method compareTo = getCompareMethod(res1.getClass())
                    // ;
                    if (compareTo == null)
                        throw new NoSuchMethodException(methodName);

                    Integer r = (Integer) compareTo.invoke(res1, new Object[] { res2 });

                    if (r.intValue() != 0)

                        // If descending then reverse sign on result
                        return adjustCompareResult(r);
                }

                return 0;
            } catch (Exception ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                throw new CSUnrecoverableException(ex);
            }
        }

        private int adjustCompareResult(Integer result) {
            // If descending then reverse sign on result
            if (getAscending().booleanValue())
                return result.intValue();
            else
                return result.intValue() * (-1);
        }
    }
}