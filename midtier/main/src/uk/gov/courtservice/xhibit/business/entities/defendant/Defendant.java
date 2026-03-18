package uk.gov.courtservice.xhibit.business.entities.defendant;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface Defendant extends CSEntityLocal {
    public Integer getDefendantId();

    public void setCrestDefendantId(Integer crestDefendantId);

    public Integer getCrestDefendantId();

    public void setFirstName(String firstName);

    public String getFirstName();

    public void setMiddleName(String middleName);

    public String getMiddleName();

    public void setSurname(String surname);

    public String getSurname();

    public void setInitials(String initials);

    public String getInitials();

    public void setDateOfBirth(Timestamp dateOfBirth);

    public Timestamp getDateOfBirth();

    public void setGender(Integer gender);

    public Integer getGender();

    public void setLastConvictionDate(Timestamp lastConvictionDate);

    public Timestamp getLastConvictionDate();

    public void setAddressId(Integer addressId);

    public Integer getAddressId();

    public void setIsCompany(String isCompany);

    public String getIsCompany();

    public void setCourtId(Integer courtId);

    public Integer getCourtId();

    public void setDefendantOnCases(Collection defendantOnCases);

    public void setDefendantReferences(Collection defendantReferences);

    public Collection getDefendantOnCases();

    public Collection getDefendantReferences();
    
    public void setPublicDisplayHide(String publicDisplayHide);
    
    public String getPublicDisplayHide();
    
    public java.lang.String getParentGuardianName(  ) ;

    public void setParentGuardianName( java.lang.String parentGuardianName ) ;

    public java.lang.String getEthnicAppearanceCode(  ) ;

    public void setEthnicAppearanceCode( java.lang.String ethnicAppearanceCode ) ;

    public java.lang.String getEthnicitySelfDefined(  ) ;

    public void setEthnicitySelfDefined( java.lang.String ethnicitySelfDefined ) ;
    
    public java.lang.String getPrisonId(  ) ;

    public void setPrisonId( java.lang.String prisonId ) ;
    
    public java.lang.String getCurrentPrisonStatus( );
    
    public void setCurrentPrisonStatus( java.lang.String currentPrisonStatus);
}