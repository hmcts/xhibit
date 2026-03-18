package uk.gov.courtservice.xhibit.business.models.crestformsbf;

import java.io.Serializable;

/**
 * <p>
 * Title: The Crest Form B - F Case
 * </p>
 * <p>
 * Description: The case a crest form b - f belongs to.
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

public class CrestFormsBFCase implements Comparable, Serializable {

    private final Integer id;

    private final String type;

    private final String subType;

    private final Integer number;

    private final boolean linked;

    private final String reference;
    
    private static final long serialVersionUID = -6171167705730265999L;

    /**
     * Construct a case with the specified data
     * 
     * @param id
     * @param type
     * @param subType
     * @param number
     * @param linked
     */
    public CrestFormsBFCase(Integer id, String type, String subType, Integer number, boolean linked) {
        if (id == null) {
            throw new IllegalArgumentException("id: " + id);
        }
        if (type == null || !isValidType(type)) {
            throw new IllegalArgumentException("type: " + type);
        }
        if (subType == null || !isValidSubType(type, subType)) {
            throw new IllegalArgumentException("subType: " + subType);
        }
        if (number == null) {
            throw new IllegalArgumentException("number:" + number);
        }

        this.id = id;
        this.type = type;
        this.subType = subType;
        this.number = number;
        this.linked = linked;
        this.reference = createReference(type, number);
    }

    /**
     * Returns true if this case is a criminal appeal The appeal case has a sub
     * type of either 'S' - Sentence 'C' - Conviction 'B' - Both Sentence and
     * Conviction
     * 
     * @return true if this case is a criminal appeal, otherwise false
     */
    public boolean isCriminalAppeal() {
        return "A".equals(type) && ("C".equals(subType) || "S".equals(subType) || "B".equals(subType));
    }

    /**
     * Returns true if this case is a miscellaneous appeal
     * 
     * @return true if this case is a miscellaneous appeal, otherwise false
     */
    public boolean isMiscAppeal() {
        return "A".equals(type) && "O".equals(subType);
    }

    /**
     * Get the id (primary key) of the case
     * 
     * @return An <code>Integer</code> representing the case id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the type of the case the valid types are
     * 
     * 'S' for Sentence 'T' for Trial 'A' for Appeal
     * 
     * The following types do not have forms so are not valid here
     * 
     * 'U' for cases where the defendant is juvenile OR case move from other
     * court OR other 'B' for Bail 'C' for Cxxx
     * 
     * @return A <code>String</code> containing the case type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the sub type of the case the valid values are
     * 
     * 'C' for Criminal Appeal 'O' for Miscellaneous Appeal '' for all other
     * Cases
     * 
     * @return A <code>String</code> containing the case sub type or the empty
     *         String if none is set
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Get the number of the case
     * 
     * @return An <code>Integer</code> representing the case number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * A flag to indicate if this case was the root of the request or represents
     * a linked case. In a given form list only one case should not linked.
     * 
     * @return true if the case is a linked case or false if the case was the
     *         requested case
     */
    public boolean isLinked() {
        return linked;
    }

    /**
     * Get the case reference <case-type><case-number>
     * 
     * @return A <code>String</code> containing the case reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @return a <code>String</code>containing a representation of this
     *         object
     */
    public String toString() {
        return "id: " + id + " type: " + type + " subType: " + subType + " number: " + number + " linked: " + linked;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param object
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public final boolean equals(Object object) {
        return object instanceof CrestFormsBFCase && equals((CrestFormsBFCase) object);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param caze
     *            the reference with which to compare
     * @return true if this object is the same as the argument, false otherwise
     */
    public boolean equals(CrestFormsBFCase caze) {
        return caze != null && reference.equals(caze.reference);
    }

    /**
     * Returns a hashcode for this object, implemented for the benifit of
     * collections.
     * 
     * @return a hash code value for this object
     */
    public int hashCode() {
        return reference.hashCode();
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
        return compareTo((CrestFormsBFCase) object);
    }

    /**
     * Compare against another CrestFormsBFFCase
     * 
     * @pararm form the object to compare to
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    public int compareTo(CrestFormsBFCase caze) {
        return reference.compareTo(caze.reference);
    }

    /**
     * Determine if type is valid.
     * 
     * Valid types are 'S' 'T' 'A'
     * 
     * @param type
     * @return true if type is valid, otherwise false
     */
    private static boolean isValidType(String type) {
        return true; // loosen checking of Type of Case

        // S.Bachra 19/8/03 Loosen checking of Type of Case
        // Originally checked as per below
        // Unsure of rules to determine valid Types
        // return "S".equals(type) || "T".equals(type) || "A".equals(type);
    }

    /**
     * Determine if subType is valid for type.
     * 
     * subType should be '' unless type is 'A' when it should be 'C' (criminal)
     * or 'O' (misc)
     * 
     * @param type
     * @param subType
     * @return true if type and sub type are a valid combination, otherwise
     *         false
     */
    private static boolean isValidSubType(String type, String subType) {
        return true; // loosen checking of the Sub Type of Case

        // S.Bachra 19/8/03 Loosen checking of Type of Case
        // Originally checked as per below
        // Unsure of rules to determine valid Sub Types
        /*
         * if("A".equals(type)) { return "C".equals(subType) ||
         * "O".equals(subType); } else { return " ".equals(subType); }
         */
    }

    /**
     * Create a case reference of the form <type><number>
     * 
     * @param type
     * @param number
     * @return the case reference
     */
    private static final String createReference(String type, Integer number) {
        return type + number;
    }

}
