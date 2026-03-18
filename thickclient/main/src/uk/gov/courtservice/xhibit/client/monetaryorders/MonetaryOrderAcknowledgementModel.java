package uk.gov.courtservice.xhibit.client.monetaryorders;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerBeanBusinessDelegate;

public class MonetaryOrderAcknowledgementModel {
	private Integer caseId = 0;
	private String caseType = "";
	private Integer caseNumber = 0;
	private Integer courtId = 0;
	private MonetaryOrderTrackingControllerBeanBusinessDelegate monetaryOrderTrackingControllerBeanBusinessDelegate = null; 
	private Collection monetaryOrderAcknowledgements = null;
	
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
	public Integer getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	public Integer getCourtId() {
		return courtId;
	}
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	
	public MonetaryOrderTrackingControllerBeanBusinessDelegate getMonetaryOrderTrackingControllerBeanBusinessDelegate() {
		return monetaryOrderTrackingControllerBeanBusinessDelegate;
	}
	public void setMonetaryOrderTrackingControllerBeanBusinessDelegate (MonetaryOrderTrackingControllerBeanBusinessDelegate monetaryOrderTrackingControllerBeanBusinessDelegate) {
		this.monetaryOrderTrackingControllerBeanBusinessDelegate = monetaryOrderTrackingControllerBeanBusinessDelegate;
	}

	public Collection getMonetaryOrderAcknowledgements() {
		return monetaryOrderAcknowledgements;
	}
	public void setMonetaryOrderAcknowledgements(Collection monetaryOrderAcknowledgements) {
		this.monetaryOrderAcknowledgements = monetaryOrderAcknowledgements;
	}
}
