package uk.gov.courtservice.xhibit.business.models.crestformsbf;

import java.io.Serializable;

/**
 * <p>
 * Title: The Crest Form B - F Defendant
 * </p>
 * <p>
 * Description: The defendant a crest form b - f belongs to.
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

public class CrestFormsBFDefendant implements Comparable, Serializable {

    private final Integer id;

    private final String firstName;

    private final String middleName;

    private final String lastName;

    private final String fullName;

    private static final long serialVersionUID = 6272769135266940233L;

    /**
     * Construct a valid defendant from the data
     */
    public CrestFormsBFDefendant(Integer id, String firstName, String middleName, String lastName) {
        if (id == null) {
            throw new IllegalArgumentException("id: " + id);
        }

        this.id = id;
        this.firstName = firstName == null ? "" : firstName;
        this.middleName = middleName == null ? "" : middleName;
        this.lastName = lastName == null ? "" : lastName;

        this.fullName = createFullName(this.firstName, this.middleName, this.lastName);

        if (fullName == null) {
            throw new IllegalArgumentException("firstName: " + firstName + " middleName: " + middleName + " lastName: "
                    + lastName);
        }
    }

    /**
     * Get the id (primary key) of the defendant
     * 
     * @return An <code>Integer</code> representing the defendant id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the first name of the defendant
     * 
     * @return A <code>String</code> containing the defendant's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the middle name of the defendant
     * 
     * @return A <code>String</code> containing the defendant's middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Get the last name of the defendant
     * 
     * @return A <code>String</code> containing the defendant's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the full name of the defendant <first-name> <middle-name> <last-name>
     * 
     * @return A <code>String</code> containing the defendant's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return a <code>String</code>containing a representation of this
     *         object
     */
    public String toString() {
        return "id: " + id + " fullName: " + fullName;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param object
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public final boolean equals(Object object) {
        return object instanceof CrestFormsBFDefendant && equals((CrestFormsBFDefendant) object);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param defendant
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public boolean equals(CrestFormsBFDefendant defendant) {
        return defendant != null && fullName.equals(defendant.fullName);
    }

    /**
     * Returns a hashcode for this object, implemented for the benifit of
     * collections.
     * 
     * @return a hash code value for this object
     */
    public int hashCode() {
        return fullName.hashCode();
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
        return compareTo((CrestFormsBFDefendant) object);
    }

    /**
     * Compare against another CrestFormsBFFDefendant
     * 
     * @pararm form the object to compare to
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    public int compareTo(CrestFormsBFDefendant defendant) {
        return fullName.compareTo(defendant.fullName);
    }

    /**
     * Create the full name String from its parts
     * 
     * @param firstName
     * @param middleName
     * @param lastName
     * @return the names concatenated seperated by spaces
     * @throws NullPointerException
     *             if any part of the name is null
     */
    private static String createFullName(String firstName, String middleName, String lastName) {
        String trimmedFirstName = firstName.trim();
        String trimmedMiddleName = middleName.trim();
        String trimmedLastName = lastName.trim();

        return trimmedFirstName.length() > 0 ? trimmedMiddleName.length() > 0 ? trimmedLastName.length() > 0 ? trimmedFirstName
                + " " + trimmedMiddleName + " " + trimmedLastName
                : trimmedFirstName + " " + trimmedMiddleName
                : trimmedLastName.length() > 0 ? trimmedFirstName + " " + trimmedLastName : trimmedFirstName
                : trimmedMiddleName.length() > 0 ? trimmedLastName.length() > 0 ? trimmedMiddleName + " "
                        + trimmedLastName : trimmedMiddleName : trimmedLastName.length() > 0 ? trimmedLastName : null;
    }

}
