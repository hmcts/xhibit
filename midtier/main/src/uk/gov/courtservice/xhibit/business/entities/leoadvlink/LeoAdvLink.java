package uk.gov.courtservice.xhibit.business.entities.leoadvlink;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface LeoAdvLink extends CSEntityLocal {
    
    public void setLeoAdvLinkId(Integer leoAdvLinkId);
    public Integer getLeoAdvLinkId();
    
    public void setLegalAidOrderId(Integer legalAidOrderId);
    public Integer getLegalAidOrderId();
    
    public void setDefendantOnCaseId(Integer defendantOnCaseId);
    public Integer getDefendantOnCaseId();
    
    public void setRefAdvocateId(Integer refAdvocateId);
    public Integer getRefAdvocateId();
    
    public void setCrestAdvCategory(String crestAdvCategory);
    public String getCrestAdvCategory();
    
    public void setCrestPostNumber(Integer crestPostNumber);
    public Integer getCrestPostNumber();
    
    public void setAvailable(String available);
    public String getAvailable();
    
    public void setNewRowFlag(String newRowFlag);
    public String getNewRowFlag();
    
    public void setObsInd(String obsInd);
    public String getObsInd();
    
}