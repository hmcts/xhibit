package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class RemandReasonDescriptionBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer remandReasonDescriptionId;
	private String reasonCategory;
	private String reasonDescription;
	private String obsInd;
 	
	public RemandReasonDescriptionBasicValue(Integer id, Integer version) {
		super(id, version);
	}
 		
	public Integer getRemandReasonDescriptionId() {
		return remandReasonDescriptionId;
	}

	public void setRemandReasonDescriptionId(Integer remandReasonDescriptionId) {
		this.remandReasonDescriptionId = remandReasonDescriptionId;
	}
		
	public String getReasonCategory() {
		return reasonCategory;
	}

	public void setReasonCategory(String reasonCategory) {
		this.reasonCategory = reasonCategory;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
