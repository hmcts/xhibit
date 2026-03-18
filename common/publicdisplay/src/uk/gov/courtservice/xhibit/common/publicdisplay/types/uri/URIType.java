package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class URIType {
    private final String type;

    URIType(final String type) {
        this.type = type;
    }

    /**
     * Returns the type as a string
     * 
     * @return the type as a string.
     */
    public String getType() {
        return type;
    }

    /**
     * Added for convenience.
     * 
     * @see String#equals
     * 
     * @param obj
     *            the object to compare to.
     * 
     * @return true if the same.
     */
    public boolean equals(Object obj) {
        return type.equals(obj);
    }

    /**
     * Added for convenience.
     * 
     * @see String#hashCode
     * 
     * @return the hashcode.
     */
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * Returns the string representation of the URIType.
     * 
     * @return the string representation of the URIType.
     */
    public String toString() {
        return type;
    }
}
