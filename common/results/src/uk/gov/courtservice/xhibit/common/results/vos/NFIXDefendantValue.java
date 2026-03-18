package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class NFIXDefendantValue implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String defendantName;
	private String solicitorReference;
	
	/**
	 * @return the defendantName
	 */
	public String getDefendantName() {
		return defendantName;
	}
	/**
	 * @param defendantName the defendantName to set
	 */
	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}
	
	public String getSolicitorReference() {
		return solicitorReference;
	}
	public void setSolicitorReference(String solicitorReference) {
		this.solicitorReference = solicitorReference;
	}
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
