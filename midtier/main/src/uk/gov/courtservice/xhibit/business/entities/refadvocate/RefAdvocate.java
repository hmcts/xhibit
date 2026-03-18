package uk.gov.courtservice.xhibit.business.entities.refadvocate;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefAdvocate extends CSEntityLocal {

    public Integer getCrestAdvocateId(); // PK

    public Integer getCrestChamberId();

    public Integer getRefAdvocateId();

    public String getAdvTypeInd();

    public String getIsGlobal();

    public Integer getBarNo();

    public Integer getYearOfCall();

    public String getHonours();

    public String getObsInd();

    public String getVatNo();

    public Integer getLegalRepId();
    
    public Integer getRefChamberId();

    public void setAdvTypeInd(String advTypeInd);

    public void setBarNo(Integer barNo);

    public void setCrestAdvocateId(Integer crestAdvocateId);

    public void setCrestChamberId(Integer crestChamberId);

    public void setHonours(String honours);

    public void setIsGlobal(String isGlobal);

    public void setObsInd(String obsInd);

    public void setVatNo(String vatNo);

    public void setYearOfCall(Integer yearOfCall);

    public void setLegalRepId(Integer val);
    
    public void setRefChamberId(Integer val);

    public abstract RefChamber getRefChamber();

    public abstract void setRefChamber(RefChamber refChamber);

    public abstract RefLegalRepresentative getRefLegalRepresentative();

    public abstract void setRefLegalRepresentative(RefLegalRepresentative refLegalRepresentative);
}