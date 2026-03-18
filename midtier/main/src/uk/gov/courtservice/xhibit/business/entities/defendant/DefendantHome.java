package uk.gov.courtservice.xhibit.business.entities.defendant;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DefendantHome extends javax.ejb.EJBLocalHome {
    public Defendant create(Integer crestDefendantId, String firstName, String middleName, String surname,
            String initials, java.sql.Timestamp dateOfBirth, String ParentGuardianName, Integer gender, java.sql.Timestamp lastConvictionDate,
            Integer addressId, java.lang.String isCompany, Integer courtId, String userDisplayName, String ethnicAppearanceCode, String ethnicitySelfDefined, String currentPrisonStatus, String prisonId) throws CreateException;

    public Defendant findByPrimaryKey(Integer defendantId) throws FinderException;

    public Defendant findByKeyAndVersion(Integer defendantId, Integer version) throws FinderException;
    
    public Defendant findMinCrestDefendantId() throws FinderException;

    public Defendant findMaxCrestDefendantId() throws FinderException;

	public Collection findDefendantByCourtIdSurname(Integer courtId, String surname) throws FinderException;

	public Collection findDefendantByCourtIdSurnameGenderFirstName(Integer courtId, String firstName, String surname, Integer gender) throws FinderException;
	
	public Collection findDefendantByCourtIdSurnameGender(Integer courtId, String surname, Integer gender) throws FinderException;

	public Collection findDefendantByCourtIdFirstNameSurname(Integer courtId, String firstName, String surname) throws FinderException;

}