package uk.gov.courtservice.xhibit.web.framework.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Title: SafeIterator
 * </p>
 * <p>
 * Used to allow wrappeds to be returned from objects without endangering data
 * encapsulation.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         SafeIterator.java,v $ Revision 1.3 2003/03/21 11:49:25 fz0n8j Revised
 *         thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:09 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */

public class SafeIterator implements Iterator {
    /**
     * The wrapped iterator
     */
    private final Iterator wrapped;

    /**
     * Constructs a new SafeIterator to wrap the existing iterator
     * 
     * @param newWrapped
     *            the iterator to wrap
     * @throws IllegalArgumentException
     *             if the parameter is null
     */
    public SafeIterator(Iterator newWrapped) throws IllegalArgumentException {
        if (newWrapped == null) {
            throw new IllegalArgumentException("newWrapped");
        }
        this.wrapped = newWrapped;
    }

    /**
     * Returns <tt>true</tt> if the wrapped iterator has more elements.
     * 
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public final boolean hasNext() {
        return wrapped.hasNext();
    }

    /**
     * Returns the next element in the wrapped interation.
     * 
     * @return the next element in the iteration.
     * @exception NoSuchElementException
     *                iteration has no more elements.
     */
    public final Object next() throws NoSuchElementException {
        return wrapped.next();
    }

    /**
     * Allways throws an UnsupportedOperationException it is this that makes the
     * itertor safe.
     * 
     * @exception UnsupportedOperationException
     *                if the <tt>remove</tt> operation is not supported by
     *                this Iterator.
     */
    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
