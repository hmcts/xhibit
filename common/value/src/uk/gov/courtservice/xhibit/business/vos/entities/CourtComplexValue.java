package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Court Complex Value.
 * <p>
 * Not to be confused with RefCourt.
 * </p>
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
public class CourtComplexValue extends CourtBasicValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2383336635859125680L;

	private RefCourtBasicValue refCourt;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String town;

	private String county;

	private String postcode;

	private String country;

	private String telephoneNumber;

	private String faxNumber;

	/**
	 * Default constructor
	 */
	public CourtComplexValue() {
	}

	/**
	 * Initializes the Id and version number
	 * 
	 * @param id
	 * @param version
	 */
	public CourtComplexValue(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * Gets the Ref Court value.
	 * 
	 * @return RefCourtBasicValue
	 */
	public RefCourtBasicValue getRefCourt() {
		return this.refCourt;
	}

	/**
	 * Set the RefCourt value.
	 * 
	 * @param newValue
	 *            RefCourtBasicValue
	 */
	public void setRefCourt(RefCourtBasicValue newValue) {
		this.refCourt = newValue;
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

	public String getCountry() {
		return this.country;
	}

	// Setters
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

	public void setCountry(String param) {
		this.country = param;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	/**
	 * Populates the address details from an AddressBasicValue
	 * 
	 * @param abv
	 */
	public void populateFromAddress(final AddressBasicValue abv) {
		this.address1 = ((abv != null) ? abv.getAddress1() : null);
		this.address2 = ((abv != null) ? abv.getAddress2() : null);
		this.address3 = ((abv != null) ? abv.getAddress3() : null);
		this.address4 = ((abv != null) ? abv.getAddress4() : null);
		this.town = ((abv != null) ? abv.getTown() : null);
		this.county = ((abv != null) ? abv.getCounty() : null);
		this.postcode = ((abv != null) ? abv.getPostcode() : null);
		this.country = ((abv != null) ? abv.getCountry() : null);
	}
}