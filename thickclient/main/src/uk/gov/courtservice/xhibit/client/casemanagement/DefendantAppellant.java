package uk.gov.courtservice.xhibit.client.casemanagement;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;

public class DefendantAppellant {

	private static final String MALE = "Male";
	private static final String FEMALE = "Female";
	private static final String COMPANY ="Company";
	
	// need the defedant id when saving to database
	private Integer defendantId;
	private Integer defendantOnCaseId;
	// Personal details
	private String title;
	private String initials;
	private String surname;
	private String firstName;
	private String otherName;
	private String dateOfBirth;
	private String parentGuardian;
	private String gender;
	private String prisonerNumber;
	private String prisonId;
	private int courtId;
	private int caseId;
	// Address details
	private String addressLineOne;
	private String addressLineTwo;
	private String addressLineThree;
	private String addressLineFour;
	private String town;
	private String county;
	private String postcode;
	private String driverNumber;
	private String licenceType;
	private String licenceIssueNumber;
	private String isCompany;
	private Integer addressId;
	// Text Fields
	private String aSN;
	boolean hasRepresentation;
	private DefOnCaseRefSolFirmValue defOnCaseRefSolFirm;
	
	private String pNC;
	private String pTIURN;
	private String maskName;
	// Dropdown Values
	private String ethnicAppearance;
	private String ethnicSelfDefined;
	private DropdownCodeStringValue bCStatus;
	private RefSystemCodeBasicValue hateCrime;
	private DropdownCodeStringValue nationality;
	// Date Fields
	private Date magCourtConviction;
	private Date originalDateOfSentence;
	private Date dateDrivingDisqual;
	private Date firstDate;
	private Date finalDate;
	// Checkbox
	private String currentPrisonStatus;
	private boolean isMasked;
	private boolean hasHateCrime;

	boolean juvenile;

	// DefendantReference id's
	private Integer defRef1;
	private Integer defRef2;
	private Integer defRefLicenceTypeId;
	private Integer defRefLicenceIssueNumberId;


	public String getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}

	public String getLicenceIssueNumber() {
		return licenceIssueNumber;
	}

	public void setLicenceIssueNumber(String licenceIssueNumber) {
		this.licenceIssueNumber = licenceIssueNumber;
	}

	public boolean getHasRepresentation() {
		return hasRepresentation;
	}

	public void setHasRepresentation(boolean hasRepresentation) {
		this.hasRepresentation = hasRepresentation;
	}
	
	public Integer getDefRefLicenceTypeId() {
		return defRefLicenceTypeId;
	}

	public void setDefRefLicenceTypeId(Integer defRefLicenceTypeId) {
		this.defRefLicenceTypeId = defRefLicenceTypeId;
	}

	public Integer getDefRefLicenceIssueNumberId() {
		return defRefLicenceIssueNumberId;
	}

	public void setDefRefLicenceIssueNumberId(Integer defRefLicenceIssueNumberId) {
		this.defRefLicenceIssueNumberId = defRefLicenceIssueNumberId;
	}

	private Integer positionInTable;
	private boolean dataChanged = false;

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public boolean isJuvenile() {
		return juvenile && !COMPANY.equals(gender);
	}

	public void setJuvenile(boolean juvenile) {
		this.juvenile = juvenile;
	}

	public DefendantAppellant() {
	}

	public DefendantAppellant(String firstName, String surname, String otherName, String dob, String gender) {
		this.firstName = firstName;
		this.surname = surname;
		this.otherName = otherName;
		this.dateOfBirth = dob;
		this.gender = gender;
	}

	// getters and setters required for DefendantAppellantsearch and tab page in
	// callback function

	public boolean getIsMasked() {
		return isMasked;
	}

	public void setIsMasked(boolean isMasked) {
		this.isMasked = isMasked;
	}

	public boolean gethasHateCrime() {
		return hasHateCrime;
	}

	public void setHasHateCrime(boolean hasHateCrime) {
		this.hasHateCrime = hasHateCrime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPrisonerNumber() {
		return prisonerNumber;
	}

	public void setPrisonerNumber(String prisonerNumber) {
		this.prisonerNumber = prisonerNumber;
	}

	public String getPrisonId() {
		return prisonId;
	}

	public void setPrisonId(String prisonId) {
		this.prisonId = prisonId;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public String getAddressLineThree() {
		return addressLineThree;
	}

	public void setAddressLineThree(String addressLineThree) {
		this.addressLineThree = addressLineThree;
	}

	public String getAddressLineFour() {
		return addressLineFour;
	}

	public void setAddressLineFour(String addressLineFour) {
		this.addressLineFour = addressLineFour;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}

	public String getEthnicAppearance() {
		return ethnicAppearance;
	}

	public void setEthnicAppearance(String ethnicAppearance) {
		this.ethnicAppearance = ethnicAppearance;
	}

	public String getEthnicSelfDefined() {
		return ethnicSelfDefined;
	}

	public void setEthnicSelfDefined(String ethnicSelfDefined) {
		this.ethnicSelfDefined = ethnicSelfDefined;
	}

	public DropdownCodeStringValue getBCStatus() {
		return bCStatus;
	}

	public void setBCStatus(DropdownCodeStringValue bCStatus) {
		this.bCStatus = bCStatus;
	}

	public RefSystemCodeBasicValue getHateCrime() {
		return hateCrime;
	}

	public void setHateCrime(RefSystemCodeBasicValue hateCrime) {
		this.hateCrime = hateCrime;
	}

	public DropdownCodeStringValue getNationality() {
		return nationality;
	}

	public void setNationality(DropdownCodeStringValue nationality) {
		this.nationality = nationality;
	}

	public Date getMagCourtConviction() {
		return magCourtConviction;
	}

	public void setMagCourtConviction(Date magCourtConviction) {
		this.magCourtConviction = magCourtConviction;
	}

	public Date getOriginalDateOfSentence() {
		return originalDateOfSentence;
	}

	public void setOriginalDateOfSentence(Date originalDateOfSentence) {
		this.originalDateOfSentence = originalDateOfSentence;
	}

	public Date getDateDrivingDisqual() {
		return dateDrivingDisqual;
	}

	public void setDateDrivingDisqual(Date dateDrivingDisqual) {
		this.dateDrivingDisqual = dateDrivingDisqual;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
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

	public void setParentGuardian(String parentGuardian) {
		if (parentGuardian != null) {
			this.parentGuardian = parentGuardian;
		} else {
			this.parentGuardian = "";
		}
	}

	public String getParentGuardian() {
		return parentGuardian;
	}

	public String getASN() {
		return aSN;
	}

	public void setASN(String aSN) {
		this.aSN = aSN;
	}

	public String getMaskName() {
		return maskName;
	}

	public void setMaskName(String maskName) {
		this.maskName = maskName;
	}

	public String getPNC() {
		return pNC;
	}

	public void setPNC(String pNC) {
		this.pNC = pNC;
	}

	public String getPTIURN() {
		return pTIURN;
	}

	public void setPTIURN(String pTIURN) {
		this.pTIURN = pTIURN;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setGender(int gender) {
		if (gender == 1) {
			this.gender = MALE;
		}
		if (gender == 2) {
			this.gender = FEMALE;
		}
		if (gender == 0) {
			this.gender = COMPANY;
		}

	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	public int getCourtId() {
		return courtId;
	}

	public void setCourtId(int courtId) {
		this.courtId = courtId;
	}

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public void setIsCompany(String isCompany) {
		this.isCompany = isCompany;
	}

	public String getIsCompany() {
		return isCompany;
	}

	public Integer getDefRef1() {
		return defRef1;
	}

	public void setDefRef1(Integer defRef1) {
		this.defRef1 = defRef1;
	}

	public Integer getDefRef2() {
		return defRef2;
	}

	public void setDefRef2(Integer defRef2) {
		this.defRef2 = defRef2;
	}

	public Integer getPositionInTable() {
		return positionInTable;
	}

	public void setPositionInTable(Integer positionInTable) {
		this.positionInTable = positionInTable;
	}

	/**
	 * @return the defOnCaseRefSolFirm
	 */
	public DefOnCaseRefSolFirmValue getDefOnCaseRefSolFirm() {
		return defOnCaseRefSolFirm;
	}

	/**
	 * @param defOnCaseRefSolFirm
	 *            the defOnCaseRefSolFirm to set
	 */
	public void setDefOnCaseRefSolFirm(DefOnCaseRefSolFirmValue defOnCaseRefSolFirm) {
		this.defOnCaseRefSolFirm = defOnCaseRefSolFirm;
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

	public boolean compareDefApp(DefendantAppellant defApp) {
		boolean equal = false;

		equal |= !compareObjectsWithNullCheck(this.firstName, defApp.getFirstName());
		equal |= !compareObjectsWithNullCheck(this.surname, defApp.getSurname());
		equal |= !compareObjectsWithNullCheck(this.otherName, defApp.getOtherName());
		equal |= !compareObjectsWithNullCheck(this.dateOfBirth, defApp.getDateOfBirth());
		equal |= !compareObjectsWithNullCheck(this.gender, defApp.getGender());

		return equal;
	}

	// returns true if objects match
	private boolean compareObjectsWithNullCheck(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		} else if (obj1 == null) {
			if (obj2.getClass().equals(String.class)) {
				return ((String) obj2).isEmpty();
			}
			return false;
		} else if (obj2 == null) {
			if (obj1.getClass().equals(String.class)) {
				return((String) obj1).isEmpty();
			}
			return false;
		} else { // both are not null
			if (obj1.getClass().equals(obj2.getClass()) && obj1.equals(obj2) ) {
				return true;
			}
		}
		return false;
	}
}
