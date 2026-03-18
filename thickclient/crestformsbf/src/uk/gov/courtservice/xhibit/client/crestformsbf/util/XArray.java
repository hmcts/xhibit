package uk.gov.courtservice.xhibit.client.crestformsbf.util;

/**
 * Classes implementing this interface allows classes to be handled as arrays
 * this is a heavilly cut down version of the list collection which requires too
 * many methods to be implemented.
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

public interface XArray {
    /**
     * Set the object at given index
     * 
     * @param index
     *            the index
     * @param object
     *            the value
     * @throws an
     *             IndexOutOfBoundsException if the index is >= length or < 0
     */
    public void set(int index, Object object);

    /**
     * Return the value at the given index
     * 
     * @param index
     *            the index
     * @return the value at the given index
     * @throws an
     *             IndexOutOfBoundsException if the index is >= length or < 0
     */
    public Object get(int index);

    /**
     * Return the length of the
     * 
     * @param index
     *            the index
     * @return the value at the given index
     */
    public int length();
}
