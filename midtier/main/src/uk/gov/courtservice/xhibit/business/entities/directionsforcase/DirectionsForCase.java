package uk.gov.courtservice.xhibit.business.entities.directionsforcase;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DirectionsForCase extends CSEntityLocal {
    
	public Integer getDirectionsForCaseId();
	public void setDirectionsForCaseId(Integer directionsForCaseId);
	
	public String getFreetext();
	public void setFreetext(String freetext);
	
	public Date getDateTime();
	public void setDateTime(Date dateTime);
	
	public Date getListDate();
	public void setListDate(Date listDate);
	
	public String getListType();
	public void setListType(String listType);
	
	public String getListedAs();
	public void setListedAs(String listedAs);
	
	public String getDirectionsText();
	public void setDirectionsText(String directionsText);
	
	public Integer getTrialTimeUnit();
	public void setTrialTimeUnit(Integer trialTimeUnit);
	
	public Float getTrialTimeEstimate();
	public void setTrialTimeEstimate(Float trialTimeEstimate);
	
	public String getHasPanddForm();
	public void setHasPanddForm(String hasPanddForm);
	
	public Integer getCaseId();
	public void setCaseId(Integer caseId);			   
}