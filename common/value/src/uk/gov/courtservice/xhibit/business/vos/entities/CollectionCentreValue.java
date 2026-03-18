package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CollectionCentreValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class CollectionCentreValue extends CSAbstractValue {
	private static final long serialVersionUID = 8420588865608321207L;
    // Display Name of the collection centre
    private String displayName;

    // Full Name of the collection centre
    private String fullName;

    // Description of the collection centre
    private String description;

    // Email address of the collecion centre
    private String emailAddress;
    
    // Postal address of the collecion centre
    private String address;

    /**
     * Default constructor
     */
    public CollectionCentreValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CollectionCentreValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Returns display name
     * 
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name
     * 
     * @param val
     */
    public void setDisplayName(String val) {
        displayName = val;
    }

    /**
     * Returns full name
     * 
     * @return
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name
     * 
     * @param val
     */
    public void setFullName(String val) {
        fullName = val;
    }

    /**
     * Returns description
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets terminal description
     * 
     * @param val
     */
    public void setDescription(String val) {
        description = val;
    }

    /**
     * Returns email address
     * 
     * @return
     */
    public void setEmailAddress(String val) {
        emailAddress = val;
    }

    /**
     * Sets email address
     * 
     * @param val
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}