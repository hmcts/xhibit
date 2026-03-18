package uk.gov.courtservice.xhibit.business.entities.defendant;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class DefendantBean extends CSEntityBean implements EntityBean {

	public Integer ejbCreate(Integer crestDefendantId, String firstName, String middleName, String surname,
			String initials, java.sql.Timestamp dateOfBirth, String parentGuardian, java.lang.Integer gender,
			java.sql.Timestamp lastConvictionDate, Integer addressId, java.lang.String isCompany, Integer courtId,
			String userDisplayName, String ethnicAppearanceCode, String ethitySelfDefined, String currentPrisonStatus, String prisonId) throws CreateException {
		setCrestDefendantId(crestDefendantId);
		setFirstName(firstName);
		setMiddleName(middleName);
		setSurname(surname);
		setInitials(initials);
		setDateOfBirth(dateOfBirth);
		setParentGuardianName(parentGuardian);
		setGender(gender);
		setLastConvictionDate(lastConvictionDate);
		setAddressId(addressId);
		setIsCompany(isCompany);
		setCourtId(courtId);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setEthnicAppearanceCode(ethnicAppearanceCode);
		setEthnicitySelfDefined(ethitySelfDefined);
		setCurrentPrisonStatus(currentPrisonStatus);
		setPrisonId(prisonId);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer crestDefendantId, String firstName, String middleName, String surname,
			String initials, java.sql.Timestamp dateOfBirth, String parentGuardian, java.lang.Integer gender,
			java.sql.Timestamp lastConvictionDate, Integer addressId, java.lang.String isCompany, Integer courtId,
			String userDisplayName, String ethnicAppearanceCode, String ethitySelfDefined, String currentPrisonStatus, String prisonId) throws CreateException {
		// Empty
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setDefendantId(Integer defendantId);

	public abstract void setCrestDefendantId(Integer crestDefendantId);

	public abstract void setFirstName(String firstName);

	public abstract void setMiddleName(String middleName);

	public abstract void setSurname(String surname);

	public abstract void setInitials(String initials);

	public abstract void setDateOfBirth(java.sql.Timestamp dateOfBirth);

	public abstract void setGender(java.lang.Integer gender);

	public abstract void setLastConvictionDate(java.sql.Timestamp lastConvictionDate);

	public abstract void setAddressId(Integer addressId);

	public abstract void setIsCompany(java.lang.String isCompany);

	public abstract void setCourtId(Integer courtId);

	public abstract void setPublicDisplayHide(String publicDisplayHide);

	public abstract Integer getDefendantId();

	public abstract Integer getCrestDefendantId();

	public abstract String getFirstName();

	public abstract String getMiddleName();

	public abstract String getSurname();

	public abstract String getInitials();

	public abstract java.sql.Timestamp getDateOfBirth();

	public abstract java.lang.Integer getGender();

	public abstract java.sql.Timestamp getLastConvictionDate();

	public abstract Integer getAddressId();

	public abstract java.lang.String getIsCompany();

	public abstract Integer getCourtId();

	public abstract String getPublicDisplayHide();

	public abstract java.lang.String getParentGuardianName();

	public abstract void setParentGuardianName(String parentGuardianName);

	public abstract java.lang.String getEthnicAppearanceCode();

	public abstract void setEthnicAppearanceCode(String ethnicAppearanceCode);

	public abstract java.lang.String getEthnicitySelfDefined();

	public abstract void setEthnicitySelfDefined(String ethnicitySelfDefined);

	public abstract java.lang.String getPrisonId();

	public abstract void setPrisonId(String prisonId);
	
	public abstract java.lang.String getCurrentPrisonStatus();
	
	public abstract void setCurrentPrisonStatus(String currentPrisonStatus);

	// ------------------------------CMR
	// Fields------------------------------------
	public abstract void setDefendantOnCases(java.util.Collection defendantOnCases);

	public abstract void setDefendantReferences(java.util.Collection defendantReferences);

	public abstract java.util.Collection getDefendantOnCases();

	public abstract java.util.Collection getDefendantReferences();
}