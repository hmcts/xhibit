package uk.gov.courtservice.xhibit.business.models.crestformsbf;

import java.io.Serializable;

/**
 * <p>
 * Title: The Crest Form B - F Form
 * </p>
 * <p>
 * Description: The crest form b - f.
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

public class CrestFormsBFForm implements Comparable, Serializable {

    private final CrestFormsBFCase caze;

    private final CrestFormsBFDefendant defendant;

    private final CrestFormsBFType type;

    private static final long serialVersionUID = -8777416421052979289L;

    /**
     * Construct a form for the given case, defendant and type.
     * 
     * @param caze
     *            the form's case
     * @param defendant
     *            the form's defendant
     * @param type
     *            the form's type
     */
    public CrestFormsBFForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant, CrestFormsBFType type) {
        if (caze == null) {
            throw new IllegalArgumentException("caze: " + caze);
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant: " + defendant);
        }
        if (type == null) {
            throw new IllegalArgumentException("type: " + type);
        }
        this.caze = caze;
        this.defendant = defendant;
        this.type = type;
    }

    /**
     * Get the type of the form
     * 
     * @return a <code>CrestFormsBFType</code> object representing the type
     *         the form is for
     */
    public CrestFormsBFType getType() {
        return type;
    }

    /**
     * Get the case the form belongs to
     * 
     * @return a <code>CrestFormsBFCase</code> object representing the case
     *         the form is for
     */
    public CrestFormsBFCase getCase() {
        return caze;
    }

    /**
     * Get the defendant the form belongs to
     * 
     * @return a <code>CrestFormsBFDefendant</code> object representing the
     *         defendant the form is for
     */
    public CrestFormsBFDefendant getDefendant() {
        return defendant;
    }

    /**
     * @return a <code>String</code>containing a representation of this
     *         object
     */
    public String toString() {
        return " caze: (" + caze + ") defendant: (" + defendant + ") type: (" + type + ")";
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param object
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public final boolean equals(Object object) {
        return object instanceof CrestFormsBFForm && equals((CrestFormsBFForm) object);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param form
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public boolean equals(CrestFormsBFForm form) {
        return form != null && caze.equals(form.caze) && defendant.equals(form.defendant) && type.equals(form.type);
    }

    /**
     * Returns a hashcode for this object, implemented for the benifit of
     * collections.
     * 
     * @return a hash code value for this object
     */
    public int hashCode() {
        return caze.hashCode() ^ defendant.hashCode() ^ type.hashCode();
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
        return compareTo((CrestFormsBFForm) object);
    }

    /**
     * Compare against another CrestFormsBFFForm
     * 
     * @pararm form the object to compare to
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    public int compareTo(CrestFormsBFForm form) {
        int result = caze.compareTo(form.caze);
        if (result == 0) {
            result = defendant.compareTo(form.defendant);
            if (result == 0) {
                result = type.compareTo(form.type);
            }
        }
        return result;
    }

}
