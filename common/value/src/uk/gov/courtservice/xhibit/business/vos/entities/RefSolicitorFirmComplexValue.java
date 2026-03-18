package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: RefSolicitorFirmValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class RefSolicitorFirmComplexValue extends RefSolicitorFirmBasicValue {

	// Instance variables to hold details of the address
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

	private String nonSecureEmailAddress;

	private String secureEmailAddress;
	
	private static final long serialVersionUID = 735974736388330097L;

	/**
	 * Default constructor.
	 */
	public RefSolicitorFirmComplexValue() {
	}

	/**
	 * Keu constructor.
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 */
	public RefSolicitorFirmComplexValue(Integer id, Integer version) {

		super(id, version);
	}

/**
 * 
 * @param id
 * @param version
 * @param courtId
 * @param addressId
 * @param crestSofId
 * @param solicitorFirmName
 * @param vatNo
 * @param dxRef
 * @param obsInd
 * @param shortName
 * @param laCode
 */
	public RefSolicitorFirmComplexValue(Integer id, Integer version, Integer courtId, Integer addressId,
			Integer crestSofId, String solicitorFirmName, String vatNo, String dxRef, String obsInd, String shortName, String laCode) {
		super(id, version, courtId, addressId, crestSofId, solicitorFirmName, vatNo, dxRef, obsInd, shortName, laCode);
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

	// Getters
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

	public String getNonsecureEmailAddress() {
		return nonSecureEmailAddress;
	}

	public void setNonsecureEmailAddress(String nonsecureEmailAddress) {
		this.nonSecureEmailAddress = nonsecureEmailAddress;
	}

	public String getSecureEmailAddress() {
		return secureEmailAddress;
	}

	public void setSecureEmailAddress(String secureEmailAddress) {
		this.secureEmailAddress = secureEmailAddress;
	}
}
