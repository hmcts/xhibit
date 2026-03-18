package uk.gov.courtservice.xhibit.business.vos.services.defendant;

import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: DefendantValue
 * </p>
 * <p>
 * Description: DefendantValue is intended to represent defendant entities as
 * stored in the defendant table. It contains an AddressValue object
 * corresponding to the defendant's address.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Laurent Bossard
 * @author Ian Hannaford
 * @version 2.0
 * @history 20/02/2003 Ian Hannaford Updated value object to contain a
 *          DefendantOnCaseBasicValue object so that masked names functionality
 *          is catered for.
 * @histroy 07/03/2003 Ian Hannaford Added variable NoOfTics and getters and
 *          setters
 * @history 01/09/2017 Nia Walters adding value objects for case create search defendant/appellant.
 * 
 * @history 07/02/2018 Matt Newman added currentPrisonStatus column
 * 
 * @history 23/02/2018 Chris Kudzin added currentPrisonStatus and PrisonId to defendantValue object types
 */

public class DefendantValue extends CSAbstractValue {
    
    private static final long serialVersionUID = 2L;

    // genders
    public static final Integer GENDER_COMPANY = new Integer(0);

    public static final Integer GENDER_MALE = new Integer(1);

    public static final Integer GENDER_FEMALE = new Integer(2);

    /**
     * @deprecated superseded by
     * @see GENDER_COMPANY
     */
    public static final Integer GENDER_NOT_KNOWN = new Integer(0);

    /**
     * @deprecated superseded by
     * @see GENDER_COMPANY
     */
    public static final Integer GENDER_NOT_SPECIFIED = new Integer(9);

    public static final String IS_MASKED_TRUE = new String("Y");

    public static final String IS_MASKED_FALSE = new String("N");

    public static final String IS_JUVENILE_TRUE = new String("Y");

    public static final String IS_JUVENILE_FALSE = new String("N");

    private Integer defendantID;

    private Integer crestDefendantID;

    private String middleName;

    private String surName;

    private String initials;

    private Calendar dateOfBirth;

    private Integer gender;

    private Calendar lastConvictionDate;

    private AddressValue addressValue;

    private String firstName;

    private Integer courtID;

    private DefendantOnCaseBasicValue defOnCaseBasicValue = new DefendantOnCaseBasicValue();

    private Calendar updateTime;
    
    private Integer addressId;
    
    private boolean hideDefendantInAllCases = false;
    
    //New fields added during case maintenance 
    private String parentGuardianName;
    private String ethnicAppearanceCode;
    private String ethnicitySelfDefined;
    private String prisonId;
    private String isCompany;
    private DefendantReferenceBasicValue prisonerNo;
    private DefendantReferenceBasicValue croNo;
    private DefendantReferenceBasicValue driverNo;
    private DefendantReferenceBasicValue licenceType;
    
    public DefendantReferenceBasicValue getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(DefendantReferenceBasicValue licenceType) {
		this.licenceType = licenceType;
	}

	public DefendantReferenceBasicValue getLicenceIssueNumber() {
		return licenceIssueNumber;
	}

	public void setLicenceIssueNumber(DefendantReferenceBasicValue licenceIssueNumber) {
		this.licenceIssueNumber = licenceIssueNumber;
	}

	private DefendantReferenceBasicValue licenceIssueNumber;
    private String currentPrisonStatus;
    
    private boolean dataChanged = false;
    
    
    public DefendantValue() {
        // empty
    }
    
    public DefendantValue(AddressValue addVal) {
    	this.addressValue = addVal;
    }
    
    private Calendar defensiveCopy(Calendar calendar) {
        return (calendar == null ? null : (Calendar)calendar.clone());
    }
    
    private DefendantOnCaseBasicValue defensiveCopy(
            DefendantOnCaseBasicValue defendantOnCaseBasicValue) {
        return (defendantOnCaseBasicValue == null 
                ? null 
                : new DefendantOnCaseBasicValue(defendantOnCaseBasicValue));
    }
    
    private AddressValue defensiveCopy(AddressValue addressValue) {
        return (addressValue == null 
                ? null 
                : new AddressValue(addressValue));
    }
    
    public DefendantValue(DefendantValue defendantValue) {
        if (defendantValue == null) {
            return;
        }

        this.setId(defendantValue.getId());
        this.setVersion(defendantValue.getVersion());
            
        this.defendantID = defendantValue.defendantID;
        this.crestDefendantID = defendantValue.crestDefendantID;
        this.middleName = defendantValue.middleName;
        this.surName = defendantValue.surName;
        this.initials = defendantValue.initials;
        this.dateOfBirth = defensiveCopy(defendantValue.dateOfBirth);
        this.gender = defendantValue.gender;
        this.lastConvictionDate = defensiveCopy(defendantValue.lastConvictionDate);
        this.addressValue = defensiveCopy(defendantValue.addressValue);
        this.firstName = defendantValue.firstName;
        this.courtID = defendantValue.courtID;
        setDirty(defendantValue.isDirty());
        this.defOnCaseBasicValue = defensiveCopy(defendantValue.defOnCaseBasicValue);
        this.updateTime = defensiveCopy(defendantValue.updateTime);
        this.addressId = defendantValue.addressId;
        this.hideDefendantInAllCases = defendantValue.hideDefendantInAllCases;
        this.currentPrisonStatus = defendantValue.getCurrentPrisonStatus();
        this.prisonId = defendantValue.getPrisonId();
    }
    
    /**
     * @param crestDefendantID
     *            The crest defendant ID
     * @param firstName
     *            The first name
     * @param middleName
     *            The middle name(s)
     * @param surName
     *            The surname.
     * @param initials
     *            The initials
     * @param dateOfBirth
     *            The date of birth.
     * @param gender
     *            The gender
     * @param lastConvictionDate
     *            the date of last conviction.
     * @param courtID
     *            the identifier for the court.
     * @param currentPrisonStatus
     * 		      if the defendant is in custody 
     * @param prisonId
     * 			  the prisonID of the selected defendant
     * @return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue
     * 
     * @roseuid 3DBD41AD02DA
     */
    public DefendantValue(Integer crestDefendantID, String firstName, String middleName, String surName,
            String initials, Calendar dateOfBirth, Integer gender, Calendar lastConvictionDate, Integer courtID, String currentPrisonStatus, String prisonId) {
        this.crestDefendantID = crestDefendantID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surName = surName;
        this.initials = initials;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.lastConvictionDate = lastConvictionDate;
        this.courtID = courtID;
        this.currentPrisonStatus  = currentPrisonStatus;
        this.prisonId = prisonId;
    }

    
    /**
     * @param defendantID
     *            THe defendant Id
     * @param crestDefendantID
     *            The crest defendant ID
     * @param firstName
     *            The first name
     * @param middleName
     *            The middle name(s)
     * @param surName
     *            The surname.
     * @param initials
     *            The initials
     * @param dateOfBirth
     *            The date of birth.
     * @param gender
     *            The gender
     * @param lastConvictionDate
     *            the date of last conviction.
     * @param courtID
     *            the identifier for the court.
     * @param currentPrisonStatus
     * 		      if the defendant is in custody 
     * @param prisonId
     * 			  the prisonID of the selected defendant
     * @return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue
     * @roseuid 3DBD41AD02DA
     */
    public DefendantValue(Integer defendantID, Integer crestDefendantID, String firstName, String middleName,
            String surName, String initials, Calendar dateOfBirth, Integer gender, Calendar lastConvictionDate,
            Integer courtID, String currentPrisonStatus, String prisonId) {
        this(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate,
                courtID, currentPrisonStatus, prisonId);
        this.defendantID = defendantID;
    }

    /**
     * Constructor
     * 
     * @param defendantID
     *            THe defendant Id
     * @param crestDefendantID
     *            The crest defendant ID
     * @param firstName
     *            The first name
     * @param middleName
     *            The middle name(s)
     * @param surName
     *            The surname.
     * @param initials
     *            The initials
     * @param dateOfBirth
     *            The date of birth.
     * @param gender
     *            The gender
     * @param lastConvictionDate
     *            the date of last conviction.
     * @param courtID
     *            the identifier for the court.
     * @param addressValue
     *            the address of the defendant
     * @param currentPrisonStatus
     * 		      if the defendant is in custody 
     * @param prisonId
     * 			  the prisonID of the selected defendant
     * 
     * @return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue
     * @roseuid 3DABF47C02E9
     */
    public DefendantValue(Integer defendantID, Integer crestDefendantID, String firstName, String middleName,
            String surName, String initials, Calendar dateOfBirth, Integer gender, Calendar lastConvictionDate,
            Integer courtID, AddressValue addressValue, String currentPrisonStatus, String prisonId) {
        this(defendantID, crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender,
                lastConvictionDate, courtID, currentPrisonStatus, prisonId);
        this.addressValue = addressValue;
    }
    
    //This method name MUST start with "get". This means that it is picked up 
    //in to toString() method used by the UpdateDefendantPanel.isChanged(). 
    //If changed the name is changed to not be "get*" the code cannot tell if the
    // value has been altered and so ammendments may not be saved.
    public boolean getHideDefendantInAllCases() {
        return hideDefendantInAllCases;
    }
    
    public void setHideDefendantInCallCases(boolean hideDefendantInAllCases) {
        this.hideDefendantInAllCases = hideDefendantInAllCases;
    }

    public Calendar getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getDefendantID() {
        return defendantID;
    }

    public void setDefendantID(Integer defendantID) {
        this.defendantID = defendantID;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getCrestDefendantID() {
        return crestDefendantID;
    }

    /**
     * @return java.lang.String
     */
    public String getFirstName() {
        if (firstName != null) {
            return firstName;
        } else {
            return "";
        }
    }

    /**
     * @return java.lang.String
     */
    public String getMiddleName() {
        if (middleName != null)
            return middleName;
        else
            return "";
    }

    /**
     * @return java.lang.String
     */
    public String getSurName() {
        return surName;
    }

    /**
     * @return java.lang.String
     */
    public String getInitials() {
        if (initials != null)
            return initials;
        else
            return "";
    }

    /**
     * @return java.util.Date
     */
    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @return java.lang.String
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * @return java.util.Date
     */
    public Calendar getLastConvictionDate() {
        return lastConvictionDate;
    }

    /**
     * @return uk.gov.courtservice.xhibit.business.services.address.AddressValue
     */
    public AddressValue getAddressValue() {
        return addressValue;
    }

    /**
     * @param firstName
     *            String
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param middleName
     *            String
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @param surName
     *            String
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * @param initials
     *            String
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * @param dateOfBirth
     *            Date
     */
    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @param gender
     *            Integer
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * @param lastConvictionDate
     *            Date
     */
    public void setLastConvictionDate(Calendar lastConvictionDate) {
        this.lastConvictionDate = lastConvictionDate;
    }

    /**
     * @param addressValue
     *            uk.gov.courtservice.xhibit.business.services.address.AddressValue
     */
    public void setAddressValue(AddressValue addressValue) {
        this.addressValue = addressValue;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    /**
     * @param defOnCaseBasicValue
     *            the defendant on case value object.
     */
    public void setDefOnCaseBasicValue(DefendantOnCaseBasicValue defOnCaseBasicValue) {
        this.defOnCaseBasicValue = defOnCaseBasicValue;
    }

    /**
     * @return DefendantOnCaseBasicValue a DefendantOnCaseBasicValue object
     */
    public DefendantOnCaseBasicValue getDefOnCaseBasicValue() {
        return this.defOnCaseBasicValue;
    }

    /**
     * This methods returns a string indicating whether the defendant is
     * juvenile. This field is not updatable from the GUI.
     * 
     * @return String either "Y" or "N" depeding on value held in DB
     */
    public String getIsJuvenile() {
        String returnVal = IS_JUVENILE_FALSE;

        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getIsJuvenile() != null) {
        	if (!GENDER_COMPANY.equals(getGender())) {
        		returnVal = this.defOnCaseBasicValue.getIsJuvenile();
        	}
        }

        return returnVal;
    }

    /**
     * This methods returns a string indicating whether the defendants name
     * should be masked.
     * 
     * @return String either "Y" or "N" depeding on value held in DB
     */
    public String getIsMasked() {
        String returnVal = IS_MASKED_FALSE;
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getIsMasked() != null) {
            returnVal = this.defOnCaseBasicValue.getIsMasked();
        }

        return returnVal;
    }

    /**
     * This methods returns the masked name if there is one.
     * 
     * @return masked name String
     */
    public String getMaskedName() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getMaskedName() != null) {
            returnVal = this.defOnCaseBasicValue.getMaskedName();
        }

        return returnVal;
    }

    /**
     * This methods sets a field indicating whether the defendant name is
     * masked. This field is not updatable from the GUI.
     * 
     * @param isMasked
     *            either "Y" or "N" depeding on value held in DB
     */
    public void setIsMasked(String isMasked) {
        this.defOnCaseBasicValue.setIsMasked(isMasked);
    }

    /**
     * This methods sets the masked name for the defendant. This field is not
     * updatable from the GUI.
     * 
     * @param maskedName
     *            the masked name of the defendant.
     */
    public void setMaskedName(String maskedName) {
        this.defOnCaseBasicValue.setMaskedName(maskedName);
    }

    public Integer getAddressId() {
        return addressId;
    }
    
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    
    /**
     * This methods sets the asn for the defendant.
     * 
     * @param asn
     *            the asn of the defendant.
     */
    public void setAsn(String asn) {
        this.defOnCaseBasicValue.setAsn(asn);
    }
    
    /**
     * This methods returns the asn if there is one.
     * 
     * @return asn String
     */
    public String getAsn() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getAsn() != null) {
            returnVal = this.defOnCaseBasicValue.getAsn();
        }

        return returnVal;
    }
    
    /**
     * This methods sets the ptiurn for the defendant.
     * 
     * @param ptiurn
     *            the ptiurn of the defendant.
     */
    public void setPtiurn(String ptiurn) {
        this.defOnCaseBasicValue.setPtiurn(ptiurn);
    }
    
    /**
     * This methods returns the ptiurn if there is one.
     * 
     * @return ptiurn String
     */
    public String getPtiurn() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getPtiurn() != null) {
            returnVal = this.defOnCaseBasicValue.getPtiurn();
        }

        return returnVal;
    }
    
    /**
     * This methods returns the hate type if there is one.
     * 
     * @return hateType String
     */
    public String getHateType() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getHateType() != null) {
            returnVal = this.defOnCaseBasicValue.getHateType();
        }

        return returnVal;
    }
    
    /**
     * This methods returns the hate indicator if there is one.
     * 
     * @return hate indicator String
     */
    public String getHateIndicator() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getHateIndicator() != null) {
            returnVal = this.defOnCaseBasicValue.getHateIndicator();
        }

        return returnVal;
    }
    
    /**
     * This methods returns the hate sent indicator if there is one.
     * 
     * @return hate sent indicator String
     */
    public String getHateSentIndicator() {
        String returnVal = "";
        if (this.defOnCaseBasicValue != null && this.defOnCaseBasicValue.getHateSentIndicator() != null) {
            returnVal = this.defOnCaseBasicValue.getHateSentIndicator();
        }

        return returnVal;
    }
    
    /**
     * This methods sets the hate indicator for the defendant.
     * 
     * @param hateIndicator
     *            the hate indicator flag of the defendant.
     */
    public void setHateIndicator(String hateIndicator) {
        this.defOnCaseBasicValue.setHateIndicator(hateIndicator);
    }

    
    /**
     * This methods sets the hate type for the defendant.
     * 
     * @param hateType
     *            the hate type of the defendant.
     */
    public void setHateType(String hateType) {
        this.defOnCaseBasicValue.setHateType(hateType);
    }
    
    /**
     * This methods sets the hate sent indicator for the defendant.
     * 
     * @param hateSentIndicator
     *            the hate sent indicator flag of the defendant.
     */
    public void setHateSentIndicator(String hateSentIndicator) {
        this.defOnCaseBasicValue.setHateSentIndicator(hateSentIndicator);
    }
    
    /**
     * Returns the parentGuardianName
     * @todo support OracleClob,OracleBlob on WLS
     * @return the parentGuardianName
     */
    public java.lang.String getParentGuardianName(  ) {
    	return this.parentGuardianName;
    }

    /**
     * Sets the parentGuardianName
     * @param java.lang.String the new parentGuardianName value
     */
    public void setParentGuardianName(String parentGuardianName ) {
    	this.parentGuardianName = parentGuardianName;
    	
    }
    /**
     * Returns the ethnicAppearanceCode
     * @todo support OracleClob,OracleBlob on WLS
     * @return the ethnicAppearanceCode
     */
    public String getEthnicAppearanceCode(  ) {
    	return this.ethnicAppearanceCode;
    }

    /**
     * Sets the ethnicAppearanceCode
     * @param java.lang.String the new ethnicAppearanceCode value
     */
    public void setEthnicAppearanceCode(String ethnicAppearanceCode ) {
    	this.ethnicAppearanceCode = ethnicAppearanceCode;
    }

    /**
     * Returns the ethnicitySelfDefined
     * @todo support OracleClob,OracleBlob on WLS
     * @return the ethnicitySelfDefined
     */
    public String getEthnicitySelfDefined(  ) {
    	return this.ethnicitySelfDefined;
    }

    /**
     * Sets the ethnicitySelfDefined
     * @param java.lang.String the new ethnicitySelfDefined value
     */
    public void setEthnicitySelfDefined(String ethnicitySelfDefined ) {
    	this.ethnicitySelfDefined=ethnicitySelfDefined;
    }
    
    /**
     * Returns the prisonId
     * @todo support OracleClob,OracleBlob on WLS
     * @return the prisonId
     */
    public java.lang.String getPrisonId(  ) {
    	return this.prisonId;
    }

    /**
     * Sets the prisonId
     * @param java.lang.String the new prisonId value
     */
    public void setPrisonId(String prisonId ) {
    	this.prisonId = prisonId;
    }
    /**
     * Returns the isCompany
     * @todo support OracleClob,OracleBlob on WLS
     * @return the isCompany
     */
    public java.lang.String getIsCompany(  ) {
    	return this.isCompany;
    }

    /**
     * Sets the isCompany
     * @param java.lang.String the new isCompany value
     */
    public void setIsCompany(String isCompany ) {
    	this.isCompany = isCompany;
    }
    
    public String getGenderString() {
    	Integer genderNo = getGender();
    	if (genderNo != null) {
    		if (genderNo == 0)
    			return "Company";
    		if (genderNo == 1)
    			return "Male";
    		if (genderNo == 2)
    			return "Female";
    	}
   		return "Unknown";
    }
    
    public DefendantReferenceBasicValue getPrisonerNo() {
		return prisonerNo;
	}

	public void setPrisonerNo(DefendantReferenceBasicValue prisonerNo) {
		this.prisonerNo = prisonerNo;
	}

	public DefendantReferenceBasicValue getCroNo() {
		return croNo;
	}

	public void setCroNo(DefendantReferenceBasicValue croNo) {
		this.croNo = croNo;
	}

	public DefendantReferenceBasicValue getDriverNo() {
		return driverNo;
	}

	public void setDriverNo(DefendantReferenceBasicValue driverNo) {
		this.driverNo = driverNo;
	}

	public String getCurrentPrisonStatus() {
		return currentPrisonStatus;
	}

	public void setCurrentPrisonStatus(String currentPrisonStatus) {
		this.currentPrisonStatus = currentPrisonStatus;
	}

	public boolean isDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	
	public boolean compareDefValueAddress(DefendantValue defVal) {
		if (this.getAddressValue() != null) {
			if (defVal.getAddressValue() != null) {
				if (this.getAddressValue().getHumanReadableAddressString().equals(defVal.getAddressValue().getHumanReadableAddressString())) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			if (defVal.getAddressValue() != null) {
				return true;
			} else {
				return false;
			}
		}
	}

}