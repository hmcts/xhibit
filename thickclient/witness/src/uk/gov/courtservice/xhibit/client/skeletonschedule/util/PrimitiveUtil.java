package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

/**
 * <p>
 * Title: Primitive Util
 * </p>
 * <p>
 * Description: Util methods for manipulating primitvies
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */
public final class PrimitiveUtil {

    private PrimitiveUtil() {
        // Stop unnecesary creation of util class
    }

    public static String toString(Object[] a) {
        if (a == null) {
            return "null";
        } else {
            if (0 < a.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(a[0]);
                for (int i = 1; i < a.length; i++) {
                    buffer.append(", ");
                    buffer.append(a[i]);
                }
                return buffer.toString();
            } else {
                return "";
            }
        }
    }
}
