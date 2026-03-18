package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefAdvocate Complex Value.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * <p>
 * The set methods for this class should only be called if
 * <code>populateFromRefChamber()</code> has not been called previously
 * (altough there is no validation for this). This should therefore generally
 * only be from a fast lane reader
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class RefAdvocateComplexValue extends RefAdvocateBasicValue {
    // instance variables for this complex object, populate from ref
    // chambers
    // or directly via set methods (should only be performed from fast lane
    // reader population). These replace previous ref chamber value object
    // instance variable
    private String firmName = null;

    private String address1 = null;

    private String address2 = null;

    private String address3 = null;

    private String address4 = null;

    private String town = null;

    private String county = null;

    private String postcode = null;
    
    private String dxRef = null;
    
    private static final long serialVersionUID = 7837219689501644150L;

    /**
     * Default constructor
     */
    public RefAdvocateComplexValue() {
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param Integer
     *            version
     */
    public RefAdvocateComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Populate the local variables of this complex value object from the passed
     * in <code>RefChamberComplexValue</code> value object. Note that this
     * will have no effect on any superclass values.
     * 
     * @param RefChamberComplexValue
     */
    public void populateFromRefChamber(final RefChamberComplexValue refChamber) {
        final AddressBasicValue abv = ((refChamber != null) ? refChamber.getAddress() : null);

        this.firmName = ((refChamber != null) ? refChamber.getFirmName() : null);
        this.address1 = ((abv != null) ? abv.getAddress1() : null);
        this.address2 = ((abv != null) ? abv.getAddress2() : null);
        this.town = ((abv != null) ? abv.getTown() : null);
        this.county = ((abv != null) ? abv.getCounty() : null);
        this.postcode = ((abv != null) ? abv.getPostcode() : null);
        /* Added dxRef field */
        this.dxRef = ((refChamber !=null) ? refChamber.getDxRef() : null);
    }

    /**
     * @return Returns the address1.
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * @param address1
     *            The address1 to set.
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return Returns the address2.
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * @param address2
     *            The address2 to set.
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return Returns the address3.
     */
    public String getAddress3() {
        return this.address3;
    }

    /**
     * @param address3
     *            The address3 to set.
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * @return Returns the address4.
     */
    public String getAddress4() {
        return this.address4;
    }

    /**
     * @param address4
     *            The address4 to set.
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    /**
     * @return Returns the county.
     */
    public String getCounty() {
        return this.county;
    }

    /**
     * @param county
     *            The county to set.
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return Returns the firmName.
     */
    public String getFirmName() {
        return this.firmName;
    }

    /**
     * @param firmName
     *            The firmName to set.
     */
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    /**
     * @return Returns the postcode.
     */
    public String getPostcode() {
        return this.postcode;
    }

    /**
     * @param postcode
     *            The postcode to set.
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return Returns the town.
     */
    public String getTown() {
        return this.town;
    }

    /**
     * @param town
     *            The town to set.
     */
    public void setTown(String town) {
        this.town = town;
    }
    
    /**
     * @return Returns the Doc Ex reference.
     */
    public String getdxRef() {
        return this.dxRef;
    }

    /**
     * @param dxRef
     *            The dxRef to set.
     */
    public void setdxRef(String dxRef) {
        this.dxRef = dxRef;
    }
}
