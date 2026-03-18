package uk.gov.courtservice.xhibit.business.entities.refadvocate;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
abstract public class RefAdvocateBean extends CSEntityBean {

    public Integer ejbCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName) throws CreateException {

        setAdvTypeInd(advTypeInd);
        setBarNo(barNo);
        setCreatedBy(userDisplayName);
        setCrestAdvocateId(crestId);
        setCrestChamberId(crestChamberId);
        setHonours(honours);
        setIsGlobal(isGlobal);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setVatNo(vatNo);
        setYearOfCall(yearOfCall);
        return null;
    }

    public void ejbPostCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName) throws CreateException {
    }
    
    public Integer ejbCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName, Integer refChamberId) throws CreateException {

        setAdvTypeInd(advTypeInd);
        setBarNo(barNo);
        setCreatedBy(userDisplayName);
        setCrestAdvocateId(crestId);
        setCrestChamberId(crestChamberId);
        setHonours(honours);
        setIsGlobal(isGlobal);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setVatNo(vatNo);
        setYearOfCall(yearOfCall);
        setRefChamberId(refChamberId);
        return null;
    }

    public void ejbPostCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName, Integer refChamberId) throws CreateException {
    }
    
    public Integer ejbCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName, RefLegalRepresentative refLegalRep, RefChamber refChamber) throws CreateException {

        setAdvTypeInd(advTypeInd);
        setBarNo(barNo);
        setCreatedBy(userDisplayName);
        setCrestAdvocateId(crestId);
        setCrestChamberId(crestChamberId);
        setHonours(honours);
        setIsGlobal(isGlobal);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setVatNo(vatNo);
        setYearOfCall(yearOfCall);      
        return null;
    }

    public void ejbPostCreate(Integer barNo, Integer crestId, Integer crestChamberId, Integer yearOfCall,
            String advTypeInd, String honours, String isGlobal, String obsInd, String vatNo, String userDisplayName, RefLegalRepresentative refLegalRep, RefChamber refChamber) throws CreateException {
    	setRefLegalRepresentative(refLegalRep);
        setRefChamber(refChamber);
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract Integer getBarNo();

    public abstract Integer getCrestAdvocateId();

    public abstract Integer getCrestChamberId();

    public abstract Integer getRefAdvocateId();

    public abstract Integer getYearOfCall();

    public abstract String getAdvTypeInd();

    public abstract String getHonours();

    public abstract String getIsGlobal();

    public abstract String getObsInd();

    public abstract String getVatNo();
    
    public abstract Integer getRefChamberId();

    public abstract void setAdvTypeInd(String advTypeInd);

    public abstract void setBarNo(Integer barNo);

    public abstract void setCrestAdvocateId(Integer crestAdvocateId);

    public abstract void setCrestChamberId(Integer crestChamberId);

    public abstract void setHonours(String honours);

    public abstract void setIsGlobal(String isGlobal);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefAdvocateId(Integer refAdvocateId);

    public abstract void setVatNo(String vatNo);

    public abstract void setYearOfCall(Integer yearOfCall);
    
    public abstract void setRefChamberId(Integer refChamberId);

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber getRefChamber();

    public abstract void setRefChamber(uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamber refChamber);

    public abstract uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative getRefLegalRepresentative();

    public abstract java.lang.Integer getLegalRepId();

    public abstract void setRefLegalRepresentative(
            uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative refLegalRepresentative);

    public abstract void setLegalRepId(java.lang.Integer legalRepId);
}