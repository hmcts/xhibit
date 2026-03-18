package uk.gov.courtservice.xhibit.business.models.crestformsbf;

import java.io.Serializable;

/**
 * <p>
 * Title: The Crest Form B - F Form Type
 * </p>
 * <p>
 * Description: The type of a Crest Form these should be accessed using the
 * static getTypes method which returns .
 * </p>
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

public class CrestFormsBFType implements Comparable, Serializable {

    /**
     * The name of the form
     */
    private final String name;

    /**
     * The display name of the form
     */
    private final String displayName;
    
    private static final long serialVersionUID = 1718151044661864683L;

    /*
     * Construct a Form type with the name used as a display name
     */
    public CrestFormsBFType(String name) {
        this(name, name);
    }

    /*
     * Construct a Form type with the name and a different display name
     */
    public CrestFormsBFType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    /**
     * @return a <code>String</code>containing the name of the form
     */
    public String getName() {
        return name;
    }

    /**
     * @return a <code>String</code>containing the display name of the form
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return a <code>String</code>containing a representation of this
     *         object
     */
    public String toString() {
        return getName();
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param object
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public final boolean equals(Object object) {
        return object instanceof CrestFormsBFType && equals((CrestFormsBFType) object);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param type
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public boolean equals(CrestFormsBFType type) {
        return type != null && name.equals(type.name);
    }

    /**
     * Returns a hashcode for this object, implemented for the benifit of
     * collections.
     * 
     * @return a hash code value for this object
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Compare against another object
     * 
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws ClassCastException
     *             if the object can not be compared
     */
    public final int compareTo(Object object) {
        return compareTo((CrestFormsBFType) object);
    }

    /**
     * Compare against another CrestFormsBFType
     * 
     * @pararm object the object to compare to
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    public int compareTo(CrestFormsBFType type) {
        return name.compareTo(type.name);
    }

}
