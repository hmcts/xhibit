package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;

public class DefendantIdValue implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer defendantId;

	public DefendantIdValue() {
		this(null);
	}

	public DefendantIdValue(Integer defendantId) {
		setDefendantId(defendantId);
	}
	
	/**
	 * @return the defendantId
	 */
	public Integer getDefendantId() {
		return defendantId;
	}

	/**
	 * @param defendantId the defendantId to set
	 */
	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}
}
