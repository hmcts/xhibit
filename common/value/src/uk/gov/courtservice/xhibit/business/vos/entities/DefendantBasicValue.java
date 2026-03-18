package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class DefendantBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer crestDefendantID;

    private String firstName;

    private String middleName;

    private String surname;

    private String initials;

    private java.util.Calendar lastConvictionDate;

    private Integer addressID;

    private Integer courtID;

    private String isCompany;

    private java.util.Calendar dateOfBirth;

    private Integer gender;
    
    private String publicDisplayHide;
    
    private String currentPrisonStatus;

    public DefendantBasicValue() {
        // Empty
    }

    public DefendantBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    public DefendantBasicValue(Integer defendantID, Integer version, Integer crestDefendantID, String firstName,
            String middleName, String surname, String initials, java.util.Calendar dateOfBirth, Integer gender,
            java.util.Calendar lastConvictionDate, Integer addressID, Integer courtID, String isCompany) {

        this(defendantID, version);
        this.crestDefendantID = crestDefendantID;
        this.firstName = firstName;
        this.surname = surname;
        this.initials = initials;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.lastConvictionDate = lastConvictionDate;
        this.addressID = addressID;
        this.isCompany = isCompany;
    }
    

    public void setCrestDefendantID(Integer crestDefendantID) {
        this.crestDefendantID = crestDefendantID;
    }

    public Integer getCrestDefendantID() {
        return crestDefendantID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public void setDateOfBirth(java.util.Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public java.util.Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getGender() {
        return gender;
    }

    public void setLastConvictionDate(java.util.Calendar lastConvictionDate) {
        this.lastConvictionDate = lastConvictionDate;
    }

    public java.util.Calendar getLastConvictionDate() {
        return lastConvictionDate;
    }

    public void setAddressID(Integer addressID) {
        this.addressID = addressID;
    }

    public Integer getAddressID() {
        return addressID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setIsCompany(String isCompany) {
        this.isCompany = isCompany;
    }

    public String getIsCompany() {
        return isCompany;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }
    
    public void setPublicDisplayHide(String publicDisplayHide) {
        this.publicDisplayHide = publicDisplayHide;
    }
    
    public String getPublicDisplayHide() {
        return publicDisplayHide;
    }

    public boolean isHideDefendantInAllCases() {
        if (getPublicDisplayHide() != null) {
            return getPublicDisplayHide().equals(CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG);
        } else {
            return false;
        }
    }
    
    public void setHideDefendantInAllCases(boolean hideDefendantInAllCases) {
        if (hideDefendantInAllCases) {
            setPublicDisplayHide(CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG);
        } else {
            setPublicDisplayHide(null);
        }
    }

	public String getCurrentPrisonStatus() {
		return currentPrisonStatus;
	}

	public void setCurrentPrisonStatus(String currentPrisonStatus) {
		this.currentPrisonStatus = currentPrisonStatus;
	}
}
