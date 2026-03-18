package uk.gov.courtservice.xhibit.client.crestformsbf.util;

/**
 * Classes implementing this interface allows classes to be handled as arrays
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

public class ObjectXArray extends AbstractXArray {

    private final Object[] delegate;

    /**
     * Construct an ObjectXArray arround the given delegate
     * 
     * @param the
     *            underlying array
     */
    public ObjectXArray(Object[] delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate");
        }
        this.delegate = delegate;
    }

    /**
     * XArray implementation
     * 
     * @see XArray.set(int, Object) XArray
     */
    public void set(int index, Object object) {
        delegate[index] = object;
    }

    /**
     * XArray implementation
     * 
     * @see XArray.get(int) XArray
     */
    public Object get(int index) {
        return delegate[index];
    }

    /**
     * XArray implementation
     * 
     * @see XArray.length() XArray
     */
    public int length() {
        return delegate.length;
    }
}
