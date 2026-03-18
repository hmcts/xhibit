package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class RemandReasonsBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer remandReasonsId;
	private Integer defendantOnCaseId;
	private Integer orderId;
	private Integer remandReasonDescriptionId;
	private String additionalInformation;
	private String obsInd;
 	
	public RemandReasonsBasicValue() {
		super();
	}
	
	public RemandReasonsBasicValue(Integer id, Integer version) {
		super(id, version);
	}
 	
	public Integer getRemandReasonsId() {
		return remandReasonsId;
	}

	public void setRemandReasonsId(Integer remandReasonsId) {
		this.remandReasonsId = remandReasonsId;
	}	
		
	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}	
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getRemandReasonDescriptionId() {
		return remandReasonDescriptionId;
	}

	public void setRemandReasonDescriptionId(Integer remandReasonDescriptionId) {
		this.remandReasonDescriptionId = remandReasonDescriptionId;
	}
	
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
