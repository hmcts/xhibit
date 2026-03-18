package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

import java.io.Serializable;
import java.util.Locale;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.InvalidURIFormatException;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description: Description: A Unique Resource Identifier is as the
 * name suggests a method of accessing a specific item of data in a manner which
 * can be identified as a String. In the case of Public Displays this string is
 * of the format (in extended BNF notation):
 * 
 * <pre>
 *         publicdisplay://document/&lt;courtId&gt;/&lt;objectName&gt;:(&lt;courtRoomId&gt;,)+
 *         publicdisplay://display/&lt;courthouseName&gt;/&lt;courtSite&gt;/&lt;location&gt;/&lt;display&gt;
 * </pre>
 * 
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.9 $
 */
public abstract class AbstractURI implements Serializable, Comparable {
	
	static final long serialVersionUID = 111750358058789340L;
	
    protected static final String URI_PREFIX = "pd://";

    private final String cachedURI;

    /**
     * Creates a new AbstractURI object.
     * 
     * @param uriType
     *            the URIType
     * @pre uriType != null
     */
    protected AbstractURI(String cachedURI) {
        if (cachedURI == null) {
            throw new InvalidURIFormatException("Uri can not be null.");
        }
        this.cachedURI = cachedURI;
    }

    /**
     * Returns the cached String representation of this URI.
     * 
     * @return the cached String representation of this URI.
     */
    protected String getCachedURI() {
        return cachedURI;
    }

    /**
     * Returns the type of uri this is as a String.
     * 
     * @return the type of URI this is.
     * @post return != null
     */
    public abstract String getType();

    /**
     * Returns the locale if any of the URI. Subclasses should either return the
     * default locale or the locale relevant to the URI.
     * 
     * @return the localization of the object this URI points to.
     */
    public abstract Locale getLocale();

    /**
     * Comparison method.
     * 
     * @param o
     *            object to compare to.
     * @return see Object.compareTo()
     * @pre cachedURI != null
     */
    public int compareTo(Object o) {
        return cachedURI.compareTo(String.valueOf(o));
    }

    /**
     * Provides object comparison.
     * 
     * @param obj
     *            the object to compare wiyj.
     * @return true if they are semantically equal.
     * @pre obj instanceof AbstractURI
     * @pre cachedURI != null
     */
    public boolean equals(Object obj) {
        return obj instanceof AbstractURI && cachedURI.equals(((AbstractURI) obj).cachedURI);
    }

    /**
     * Returns a hashcode for the URI.
     * 
     * @return a hashcode for the URI.
     * @pre cachedURI != null
     */
    public int hashCode() {
        return cachedURI.hashCode();
    }

    /**
     * Returns the URI in String format:
     * 
     * @return the URI as a string.
     * @pre cachedURI != null
     */
    public String toString() {
        return cachedURI;
    }

    //
    // Utilities
    //	

    protected static boolean equals(char[] expected, int expectedOffset, char[] actual, int actualOffset, int length) {
        try {
            for (int i = 0; i < length; i++) {
                if (expected[expectedOffset + i] != actual[actualOffset + i]) {
                    return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            return false;
        } catch (NullPointerException npe) {
            return false;
        }
    }

    protected static int indexOf(char[] source, int offset, char delimiter) {
        for (int i = offset; i < source.length; i++) {
            if (source[i] == delimiter) {
                return i;
            }
        }
        return -1;
    }

}
