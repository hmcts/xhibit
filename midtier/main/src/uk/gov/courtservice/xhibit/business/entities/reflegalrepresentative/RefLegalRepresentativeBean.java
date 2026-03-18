package uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
abstract public class RefLegalRepresentativeBean extends CSEntityBean {

    public Integer ejbCreate(String first, String middle, String surname, String title, String initials, String type,
            String obsInd, CSEntityLocal court, String userDisplayName) throws CreateException {
        setFirstName(first);
        setLegalRepType(type);
        setMiddleName(middle);
        setObsInd(obsInd);
        setSurname(surname);
        setTitle(title);
        setInitials(initials);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        // NOTE: must set the mandatory CMR fields in ejbPostCreate (database
        // insert is delayed until then)
        return null;
    }

    public void ejbPostCreate(String first, String middle, String surname, String title, String initials, String type,
            String obsInd, CSEntityLocal court, String userDisplayName) throws CreateException {
        setCourt((Court) court);
    }

    public abstract Integer getRefLegalRepId(); // PK

    public abstract void setRefLegalRepId(Integer refLegalRepId); // temporary

    // to
    // get
    // around
    // JB
    // designer
    // foibles

    public abstract String getFirstName();

    public abstract String getInitials();

    public abstract String getLegalRepType();

    public abstract String getMiddleName();

    public abstract String getObsInd();

    public abstract String getSurname();

    public abstract String getTitle();

    public abstract Integer getCourtId();

    public abstract void setCourtId(Integer courtId);

    public abstract void setFirstName(String firstName);

    public abstract void setInitials(String initials);

    public abstract void setLegalRepType(String legalRepType);

    public abstract void setMiddleName(String middleName);

    public abstract void setObsInd(String obsInd);

    public abstract void setSurname(String surname);

    public abstract void setTitle(String title);

    public abstract Court getCourt();

    public abstract void setCourt(Court court);
}