package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Chamber Basic Value.
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
public class RefChamberComplexValue extends RefChamberBasicValue {

    private AddressBasicValue address = null;

    private CourtBasicValue court = null;
    
	//May be preferable to have a list of contact details, but this will suffice
	private String telephoneNumber;
	private String faxNumber;
	private String emailAddress;
	private String secureEmailAddress;
	
	private static final long serialVersionUID = 526263876674782056L;

    /**
     * Default constructor.
     */
    public RefChamberComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefChamberComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param crestChamberId
     * @param addressId
     * @param obsInd
     * @param isGlobal
     * @param dxRef
     * @param locationCode
     * @param firmName
     */
    public RefChamberComplexValue(Integer id, Integer version, Integer crestChamberId, Integer addressId,
            String obsInd, String isGlobal, String dxRef, String locationCode, String firmName, Integer courtId, String clerkName) {

        super(id, version, crestChamberId, addressId, obsInd, isGlobal, dxRef, locationCode, firmName, courtId, clerkName);
    }

    public AddressBasicValue getAddress() {
        return address;
    }

    public CourtBasicValue getCourt() {
        return court;
    }
    
    public void setCourt(CourtBasicValue court) {
    	this.court = court;
    }

    public void setAddress(AddressBasicValue newValue) {
        this.address = newValue;
    }

    public void setCourtId(CourtBasicValue newValue) {
        this.court = newValue;
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
    
    public String getEmailAddress() {
    	return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
    	this.emailAddress = emailAddress;
    }
    
    public String getSecureEmailAddress() {
    	return secureEmailAddress;
    }
    public void setSecureEmailAddress(String secureEmailAddress) {
    	this.secureEmailAddress = secureEmailAddress;
    }

}