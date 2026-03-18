package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CaseNonAvailDays extends CSEntityLocal {
 
	public Integer getNadId();
	public Integer getCaseId();
	public Date getStartDate();
	public Date getEndDate();
	public String getReason();
	public String getObsInd();
	
	public void setNadId(Integer nadId);
	public void setCaseId(Integer caseId);
	public void setStartDate(Date startDate);
	public void setEndDate(Date endDate);
	public void setReason(String reason);
	public void setObsInd(String obsInd);
}