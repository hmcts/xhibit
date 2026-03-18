package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class NHAObjectorValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer caseId;
	private String caseNumber;
	private String objectorName;
	private String objectorAddress;
	private String solicitorName;
	private String solicitorAddress;

	public Integer getCaseId(){
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseNumber() {
		return caseNumber;
	}


	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}


	public String getObjectorName() {
		return objectorName;
	}


	public void setObjectorName(String objectorName) {
		this.objectorName = objectorName;
	}


	public String getObjectorAddress() {
		return objectorAddress;
	}


	public void setObjectorAddress(String objectorAddress) {
		this.objectorAddress = objectorAddress;
	}
	
	public String getSolicitorName() {
		return solicitorName;
	}


	public void setSolicitorName(String solicitorName) {
		this.solicitorName = solicitorName;
	}


	public String getSolicitorAddress() {
		return solicitorAddress;
	}


	public void setSolicitorAddress(String solicitorAddress) {
		this.solicitorAddress = solicitorAddress;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
