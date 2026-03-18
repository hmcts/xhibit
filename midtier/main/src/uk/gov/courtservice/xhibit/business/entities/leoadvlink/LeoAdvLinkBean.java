package uk.gov.courtservice.xhibit.business.entities.leoadvlink;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class LeoAdvLinkBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(
            Integer legalAidOrderId, 
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            String crestAdvCategory,
            Integer crestPostNumber,
            String available,
            String newRowFlag,
            String obsInd, String userDisplayName)
    throws CreateException {
        setLegalAidOrderId(legalAidOrderId);
        setDefendantOnCaseId(defendantOnCaseId);
        setRefAdvocateId(refAdvocateId);
        setCrestAdvCategory(crestAdvCategory);
        setCrestPostNumber(crestPostNumber);
        setAvailable(available);
        setNewRowFlag(newRowFlag);
        setObsInd(obsInd);
        
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }
    
    public void ejbPostCreate(
            Integer legalAidOrderId, 
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            String crestAdvCategory,
            Integer crestPostNumber,
            String available,
            String newRowFlag,
            String obsInd, String userDisplayName) throws CreateException {
        // Empty
    }
    
    public abstract void setLeoAdvLinkId(Integer leoAdvLinkId);
    public abstract Integer getLeoAdvLinkId();
    
    public abstract void setLegalAidOrderId(Integer legalAidOrderId);
    public abstract Integer getLegalAidOrderId();
    
    public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
    public abstract Integer getDefendantOnCaseId();
    
    public abstract void setRefAdvocateId(Integer refAdvocateId);
    public abstract Integer getRefAdvocateId();
    
    public abstract void setCrestAdvCategory(String crestAdvCategory);
    public abstract String getCrestAdvCategory();
    
    public abstract void setCrestPostNumber(Integer crestPostNumber);
    public abstract Integer getCrestPostNumber();
    
    public abstract void setAvailable(String available);
    public abstract String getAvailable();
    
    public abstract void setNewRowFlag(String newRowFlag);
    public abstract String getNewRowFlag();
    
    public abstract void setObsInd(String obsInd);
    public abstract String getObsInd();
    
}
