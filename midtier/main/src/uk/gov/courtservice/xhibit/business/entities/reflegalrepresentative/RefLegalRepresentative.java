package uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

/*
 import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;
 import uk.gov.courtservice.xhibit.business.entities.solicitor.Solicitor; */
/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefLegalRepresentative extends CSEntityLocal {

    public Integer getRefLegalRepId(); // PK

    public void setRefLegalRepId(Integer newValue); // temporary to get

    // around JB designer
    // foibles

    public String getFirstName();

    public String getInitials();

    public String getLegalRepType();

    public String getMiddleName();

    public String getObsInd();

    public String getSurname();

    public String getTitle();

    public void setFirstName(String newValue);

    public void setInitials(String newValue);

    public void setLegalRepType(String newValue);

    public void setMiddleName(String newValue);

    public void setObsInd(String newValue);

    public void setSurname(String newValue);

    public void setTitle(String newValue);

    public abstract Court getCourt();

    public abstract void setCourt(Court court);

    /*
     * - relationships changed to be uni-directional from Solicitor/RefAdvocate
     * TO RefLegalRepresentative public RefAdvocate getRefAdvocate(); public
     * void setRefAdvocate(RefAdvocate refAdvocate);
     * 
     * public Solicitor getSolicitor(); public void setSolicitor(Solicitor
     * refAdvocate);
     */
}