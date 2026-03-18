package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.List;

/**
 * Court Site Complex Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class CourtSiteComplexValue extends CourtSiteBasicValue {

	private static final long serialVersionUID = 9210159139170708502L;
	private AddressBasicValue address = null;

    private CourtBasicValue court = null;
    
    private List<CourtRoomBasicValue> courtRooms = null;
    
    private CourtSatelliteBasicValue courtSatellite = null;

	// Instance variables to hold details of the address
	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String town;

	private String county;

	private String postcode;
	
	private String telephoneNumber;

	private String faxNumber;
	
    /**
     * Default constructor
     */
    public CourtSiteComplexValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtSiteComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Get address.
     * 
     * @return AddressBasicValue
     */
    public AddressBasicValue getAddress() {
        return this.address;
    }

    /**
     * Gets the Court.
     * 
     * @return CourtBasicValue
     */
    public CourtBasicValue getCourt() {
        return this.court;
    }

    public String getAddress1() {
		return this.address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public String getAddress3() {
		return this.address3;
	}

	public String getAddress4() {
		return this.address4;
	}

	public String getTown() {
		return this.town;
	}

	public String getCounty() {
		return this.county;
	}

	public String getPostcode() {
		return this.postcode;
	}
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public String getFaxNumber() {
		return faxNumber;
	}
	
    /**
	 * @return the courtRooms
	 */
	public List<CourtRoomBasicValue> getCourtRooms() {
		return courtRooms;
	}

	/**
	 * @return the courtSatellite
	 */
	public CourtSatelliteBasicValue getCourtSatellite() {
		return courtSatellite;
	}

	/**
     * Set the related address.
     * 
     * @param newValue
     */
    public void setAddress(AddressBasicValue newValue) {
        this.address = newValue;
    }

    /**
     * Sets the related Court id
     * 
     * @param newValue
     *            Integer
     */
    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }
    
    public void setAddress1(String param) {
		this.address1 = param;
	}

	public void setAddress2(String param) {
		this.address2 = param;
	}

	public void setAddress3(String param) {
		this.address3 = param;
	}

	public void setAddress4(String param) {
		this.address4 = param;
	}

	public void setTown(String param) {
		this.town = param;
	}

	public void setCounty(String param) {
		this.county = param;
	}

	public void setPostcode(String param) {
		this.postcode = param;
	}
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	/**
	 * @param courtRooms the courtRooms to set
	 */
	public void setCourtRooms(List<CourtRoomBasicValue> courtRooms) {
		this.courtRooms = courtRooms;
	}

	/**
	 * @param courtSatellite the courtSatellite to set
	 */
	public void setCourtSatellite(CourtSatelliteBasicValue courtSatellite) {
		this.courtSatellite = courtSatellite;
	}
}