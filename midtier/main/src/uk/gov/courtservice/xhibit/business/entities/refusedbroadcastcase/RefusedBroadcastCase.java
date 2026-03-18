package uk.gov.courtservice.xhibit.business.entities.refusedbroadcastcase;


import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefusedBroadcastCase extends CSEntityLocal {
   
	public Integer getRefusedBroadcastCaseId();
	public void setRefusedBroadcastCaseId(Integer refBroadcastCaseId);
	
	public Integer getTeleAppRefusedReasonId();
	public void setTeleAppRefusedReasonId(Integer teleAppRefusedReasonId);

	public String getCreatedBy();
	public void setCreatedBy(String createdBy);

	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);
	
	public void setObsInd(String obsInd);
    public String getObsInd();
	
	public void setCaseId(Integer caseId);
    public Integer getCaseId();
    
    public abstract void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);
    public abstract uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();


}