package uk.gov.courtservice.xhibit.courtlog.vos;

import java.io.Serializable;
import java.util.Date;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;

/**
 * Value object used to hold the details of a scheduled hearing required for
 * display to the client to allow them to select a scheduled hearing particular
 * events should occur for.
 * 
 * @author tz0d5m
 * @version $Revision: 1.6 $
 */
public class CourtLogScheduledHearingValue implements Serializable, Comparable {
    static final long serialVersionUID = 9057771786777308406L;
	
	private Integer scheduledHearingId;

    private String hearingType;

    private Date date;

    // cache the string representation of this value object...
    private String cachedValue = null;

    /**
     * Default constructor.
     * 
     * This constructor should not be called directly, as it does not enforce
     * that the properties are set. It should only be used by the Serialization
     * process.
     */
    public CourtLogScheduledHearingValue() {
        super();
    }

    /**
     * Constructor used to set all of the required fields for this value object.
     * 
     * @param scheduledHearingId
     *            The scheduled hearing id
     * @param hearingType
     *            The hearing type
     * @param date
     *            The date of the scheduled hearing
     */
    public CourtLogScheduledHearingValue(Integer scheduledHearingId, String hearingType, Date date) {
        if ((date == null) || (hearingType == null) || (scheduledHearingId == null)) {
            throw new IllegalArgumentException("Null parameters passed...");
        }

        this.scheduledHearingId = scheduledHearingId;
        this.hearingType = hearingType;
        this.date = date;
    }

    /**
     * Mutator method for the date property.
     * 
     * @param A
     *            <code>Date</code> representing the date.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accessor method for the date property.
     * 
     * @return An <code>Date</code> representing the date.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Mutator method for the scheduledHearingId property.
     * 
     * @param An
     *            <code>Integer</code> representing the scheduledHearingId.
     */
    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    /**
     * Accessor method for the scheduledHearingId property.
     * 
     * @return An <code>Integer</code> representing the scheduledHearingId.
     */
    public Integer getScheduledHearingId() {
        return this.scheduledHearingId;
    }

    /**
     * Mutator method for the hearingType property.
     * 
     * @param A
     *            <code>String</code> representing the hearingType.
     */
    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    /**
     * Accessor method for the hearingType property.
     * 
     * @return A <code>String</code> representing the hearingType.
     */
    public String getHearingType() {
        return this.hearingType;
    }

    /**
     * Overridden method used to determine if the passed in object is equal to
     * this one.
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return <i>true</i> if the object is the same as this, <i>false</i>
     *         otherwise.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof CourtLogScheduledHearingValue) {
            final CourtLogScheduledHearingValue other = (CourtLogScheduledHearingValue) obj;

            if (equals(this.date, other.date) && equals(this.scheduledHearingId, other.scheduledHearingId)
                    && equals(this.hearingType, other.hearingType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Private heper method to remove duplicate comparing code. If obj1 is
     * <i>null</i>, then obj2 must also be <i>null</i>, otherwise
     * obj1.equals(obj2) must return <i>true</i> for the objects to be
     * considered equal.
     * 
     * @param obj1
     *            One of the objects to compare
     * @param obj2
     *            The other object to compare
     * @return <i>true</i> if the objects are equal, <i>false</i> otherwise.
     */
    private boolean equals(Object obj1, Object obj2) {
        return ((obj1 != null) ? obj1.equals(obj2) : (obj2 == null));
    }

    /**
     * Required implementation of the <code>Comparable</code> interface,
     * allows for custom sorting of arrays of this value object.
     * 
     * @param obj
     *            the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        final CourtLogScheduledHearingValue other = (CourtLogScheduledHearingValue) obj;

        int value = this.date.compareTo(other.date);

        if (value == 0) {
            value = this.scheduledHearingId.compareTo(other.scheduledHearingId);
        }

        return value;
    }

    /**
     * Return this value object as a meaningful <code>String</code>
     * representation. This method is used for client display purposes, and as
     * such care should be taken when altering.
     * 
     * @return This value object represented as a <code>String</code>.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (cachedValue == null) {
            cachedValue = XDateFormat.format(getDate(), XDateFormat.DATEFORMAT) + " - " + getHearingType();
        }

        return cachedValue;
    }

    /*
     * Return this value object as a meaningful <code>String</code>
     * representation. This should not be used for client display purposes, but
     * instead only for debugging.
     * 
     * @return This value object represented as a <code>String</code>.
     * 
     * public String toDebug() { final StringBuffer buffer = new
     * StringBuffer(60);
     * 
     * buffer.append("scheduledHearingId = ").append(this.scheduledHearingId);
     * buffer.append("; hearingType = ").append(this.hearingType);
     * buffer.append("; date = ").append(this.date);
     * 
     * return buffer.toString(); }
     */
}
