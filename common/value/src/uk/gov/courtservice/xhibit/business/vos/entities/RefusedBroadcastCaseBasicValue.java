package uk.gov.courtservice.xhibit.business.vos.entities;


import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefusedBroadcastCaseBasicValue
 * </p>
 * <p>
 * Description: RefusedBroadcastCaseBasicValue is intended to represent 
 * refused broadcast case basic values in the refusedbroadcastcase table.
 * </p>
 * 
 */

public class RefusedBroadcastCaseBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	
	private Integer caseId;
	private Integer teleAppRefusedReasonId;
	private String obsolete;
	
	public RefusedBroadcastCaseBasicValue(Integer id, Integer version) {
		super(id, version);
	}
	public RefusedBroadcastCaseBasicValue(Integer teleAppRefusedReasonId) {
		this.teleAppRefusedReasonId = teleAppRefusedReasonId;
	}
	public RefusedBroadcastCaseBasicValue(Integer teleAppRefusedReasonId, String obsolete) {
		this.teleAppRefusedReasonId = teleAppRefusedReasonId;
		this.obsolete = obsolete;
	}
	
	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public Integer getTeleAppRefusedReasonId() {
		return teleAppRefusedReasonId;
	}
	
	public void setTeleAppRefusedReasonId(Integer teleAppRefusedReasonId) {
		this.teleAppRefusedReasonId = teleAppRefusedReasonId;
	}
	public String getObsolete() {
		return obsolete;
	}
	public void setObsolete(String obsolete) {
		this.obsolete = obsolete;
	}
		
	
}
