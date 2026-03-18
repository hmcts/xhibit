package uk.gov.courtservice.xhibit.business.vos.entities;

//import uk.gov.courtservice.xhibit.business.entities.xhb_ref_prosecutor_agency.XhbRefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;

public class RefProsecutorAgencyComplexValue extends RefProsecutorAgencyBasicValue {
	private static final long serialVersionUID = 1L;
	private String fullName;
	private AddressBasicValue refProsecutorAddress;
	//May be preferable to have a list of contact details, but this will suffice
	private String telephoneNumber;
	private String faxNumber;
	private String nonsecureEmailAddress;
	private String secureEmailAddress;

	public RefProsecutorAgencyComplexValue() {
    }
    public RefProsecutorAgencyComplexValue(Integer refProsecutorAgencyId, String refProsecutorAgencyType) {
        super(refProsecutorAgencyId, refProsecutorAgencyType);
    }


    public String getFullName() {
		return fullName;
	}
    public void setFullName(String fullName) {
		this.fullName = fullName;
	}
    public void setFullName() {
    	//--- Make full name string ---
		fullName = "";
		if (getTitle() != null) {
			fullName = getTitle();
		}
		if (getInitials() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getInitials();
		}
		if (getProsecutorName1() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getProsecutorName1();
		}
		if (getProsecutorName2() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getProsecutorName2();
		}
		if (getProsecutorName3() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getProsecutorName3();
		}
    }

	public AddressBasicValue getAddress() {
    	return refProsecutorAddress;
    }
    public void setAddress(AddressBasicValue addressBasicValue) {
    	refProsecutorAddress = addressBasicValue;
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
    	return nonsecureEmailAddress;
    }
    public void setNonsecureEmailAddress(String nonsecureEmailAddress) {
    	this.nonsecureEmailAddress = nonsecureEmailAddress;
    }
    
    public String getSecureEmailAddress() {
    	return secureEmailAddress;
    }
    public void setSecureEmailAddress(String secureEmailAddress) {
    	this.secureEmailAddress = secureEmailAddress;
    }
    
}
