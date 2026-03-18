package uk.gov.courtservice.xhibit.client.crestformsbf.util;

/**
 * Provide common functionality for classes implementing XArray
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public abstract class AbstractXArray implements XArray {

    /**
     * Return true if the arrays contain the same values.
     * 
     * @param object
     *            the object to compare to
     * @return true if equal
     */
    public boolean equals(Object object) {
        return object instanceof AbstractXArray && equals((AbstractXArray) object);
    }

    /**
     * Return true if the arrays contain the same values.
     * 
     * @param array
     *            the ObjectXArray to compare to
     * @return true if equal
     */
    public boolean equals(AbstractXArray array) {
        int l = length();
        if (array == null || array.length() != l) {
            return false;
        } else {
            for (int i = 0; i < l; i++) {
                if (get(i) != array.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Get a hash code.
     * 
     * @return the hash code of the underlying array
     */
    public int hashCode() {
        int hashCode = 1;
        for (int i = 0, l = length(); i < l; i++) {
            Object obj = get(i);
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }
}
