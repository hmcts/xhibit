package uk.gov.courtservice.framework.services.validation;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Constraint
 * </p>
 * <p>
 * Description: A class representing the constraints placed on the value and
 * type of an attribute.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

class Constraint {
    private Logger log = CSServices.getLogger(Constraint.class);

    /* The identifier for this constraint */
    private String identifier;

    /* The Java data type for this constraint */
    private int dataType;

    /* Minimum inclusive value allowed */
    private double minInclusive;

    /* Minimum exclusive value allowed */
    private double minExclusive;

    /* Maximum inclusive value allowed */
    private double maxInclusive;

    /* Maximum exclusive value allowed */
    private double maxExclusive;

    /* Allowed vos */
    private List allowedValues;

    /* Min and max lengths of attribute */
    private Integer minLength;

    private Integer maxLength;

    /**
     * <p>
     * This will create a new <code>Constraints</code> with the specified
     * identifier as the "name".
     * </p>
     * 
     * @param identifier
     *            <code>String</code> identifier for <code>Constraint</code>.
     */
    Constraint(String identifier) {
        this.identifier = identifier;

        // Null out vos
        minInclusive = Double.NaN;
        minExclusive = Double.NaN;
        maxInclusive = Double.NaN;
        maxExclusive = Double.NaN;

        // Allocate storage for allowed vos
        allowedValues = new LinkedList();
    }

    /**
     * <p>
     * This will return the identifier for this <code>Constraint</code>.
     * </p>
     * 
     * @return <code>String</code> - identifier for this constraint.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * This will allow the data type for the constraint to be set. The type is
     * specified as a Java <code>String</code>.
     * </p>
     * 
     * @param dataType
     *            <code>String</code> that is the Java data type for this
     *            constraint.
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * <p>
     * This will return the <code>String</code> version of the Java data type
     * for this constraint.
     * </p>
     * 
     * @return <code>String</code> - the data type for this constraint.
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * <p>
     * This will set the minimum allowed value for this data type (inclusive).
     * </p>
     * 
     * @param minInclusive
     *            minimum allowed value (inclusive)
     */
    void setMinInclusive(double minInclusive) {
        this.minInclusive = minInclusive;
    }

    /**
     * <p>
     * This will return the minimum allowed value for this data type
     * (inclusive).
     * </p>
     * 
     * @return <code>double</code> - minimum value allowed (inclusive)
     */
    public double getMinInclusive() {
        return minInclusive;
    }

    /**
     * <p>
     * This will return <code>true</code> if a minimum value (inclusive)
     * constraint exists.
     * </p>
     * 
     * @return <code>boolean</code> - whether there is a constraint for the
     *         minimum value (inclusive)
     */
    public boolean hasMinInclusive() {
        return (!(new Double(minInclusive)).isNaN());
    }

    /**
     * <p>
     * This will set the minimum allowed value for this data type (exclusive).
     * </p>
     * 
     * @param minInclusive
     *            minimum allowed value (exclusive)
     */
    void setMinExclusive(double minExclusive) {
        this.minExclusive = minExclusive;
    }

    /**
     * <p>
     * This will return the minimum allowed value for this data type
     * (exclusive).
     * </p>
     * 
     * @return <code>double</code> - minimum value allowed (exclusive)
     */
    public double getMinExclusive() {
        return minExclusive;
    }

    /**
     * <p>
     * This will return <code>true</code> if a minimum value (exclusive)
     * constraint exists.
     * </p>
     * 
     * @return <code>boolean</code> - whether there is a constraint for the
     *         minimum value (exclusive)
     */
    public boolean hasMinExclusive() {
        return (!(new Double(minExclusive)).isNaN());
    }

    /**
     * <p>
     * This will set the maximum allowed value for this data type (inclusive).
     * </p>
     * 
     * @param maxInclusive
     *            maximum allowed value (inclusive)
     */
    void setMaxInclusive(double maxInclusive) {
        this.maxInclusive = maxInclusive;
    }

    /**
     * <p>
     * This will return the maximum allowed value for this data type
     * (inclusive).
     * </p>
     * 
     * @return <code>double</code> - maximum value allowed (inclusive)
     */
    public double getMaxInclusive() {
        return maxInclusive;
    }

    /**
     * <p>
     * This will return <code>true</code> if a maximum value (inclusive)
     * constraint exists.
     * </p>
     * 
     * @return <code>boolean</code> - whether there is a constraint for the
     *         maximum value (inclusive)
     */
    public boolean hasMaxInclusive() {
        return (!(new Double(maxInclusive)).isNaN());
    }

    /**
     * <p>
     * This will set the maximum allowed value for this data type (exclusive).
     * </p>
     * 
     * @param maxInclusive
     *            maximum allowed value (exclusive)
     */
    void setMaxExclusive(double maxExclusive) {
        this.maxExclusive = maxExclusive;
    }

    /**
     * <p>
     * This will return the maximum allowed value for this data type
     * (exclusive).
     * </p>
     * 
     * @return <code>double</code> - maximum value allowed (exclusive)
     */
    public double getMaxExclusive() {
        return maxExclusive;
    }

    /**
     * <p>
     * This will return <code>true</code> if a maximum value (exclusive)
     * constraint exists.
     * </p>
     * 
     * @return <code>boolean</code> - whether there is a constraint for the
     *         maximum value (exclusive)
     */
    public boolean hasMaxExclusive() {
        return (!(new Double(maxExclusive)).isNaN());
    }

    /**
     * <p>
     * This will add another value to the list of allowed vos for this data
     * type.
     * </p>
     * 
     * @param value
     *            <code>String</code> value to add.
     */
    void addAllowedValue(String value) {
        allowedValues.add(value);
    }

    /**
     * <p>
     * This will return the list of allowed vos for this data type.
     * </p>
     * 
     * @return <code>List</code> - allowed vos for this
     *         <code>Constraint</code>.
     */
    public List getAllowedValues() {
        return allowedValues;
    }

    /**
     * <p>
     * This will indicate if there are a set of allowed vos for this data type.
     * </p>
     * 
     * @return <code>boolean</code> - whether there are allowed vos for this
     *         type.
     */
    public boolean hasAllowedValues() {
        return (allowedValues.size() > 0);
    }

    void setMinLength(Integer length) {
        minLength = length;
        if (maxLength != null && minLength.intValue() > maxLength.intValue()) {
            log.warn("Constraint " + identifier + " has minLength(" + minLength + ") > maxLength(" + maxLength
                    + ") value: " + "Update appropriate XSD file");
        }
    }

    public Integer getMinLength() {
        return minLength;
    }

    public boolean hasMinLength() {
        return (minLength != null);
    }

    void setMaxLength(Integer length) {
        maxLength = length;
        if (minLength != null && minLength.intValue() > maxLength.intValue()) {
            log.warn("Constraint " + identifier + " has minLength(" + minLength + ") > maxLength(" + maxLength
                    + ") value: " + "Update appropriate XSD file");
        }
    }

    /**
     * 
     * @return max length
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * 
     * @return true if maxLength is a constraint
     */
    public boolean hasMaxLength() {
        return (maxLength != null);
    }

    /**
     * 
     * @return attribute names and vos
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(" identifier: ").append(identifier).append(" dataType: ").append(dataType).append(" minInclusive: ")
                .append(minInclusive).append(" minExclusive: ").append(minExclusive).append(" maxInclusive: ").append(
                        maxInclusive).append(" maxExclusive: ").append(maxExclusive).append(" allowedValues: ").append(
                        allowedValues).append(" minLength: ").append(minLength).append(" maxLength: ")
                .append(maxLength);

        return buf.toString();
    }
}