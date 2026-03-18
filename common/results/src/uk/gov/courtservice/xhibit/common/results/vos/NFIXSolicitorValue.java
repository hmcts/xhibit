package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.util.List;

public class NFIXSolicitorValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private String defendantSolicitorName;
	private String defendantSolicitorAddress;
	private List<NFIXDefendantValue> defendants;
	private String solicitorId;
	
	public String getSolicitorId() {
		return solicitorId;
	}
	public void setSolicitorId(String solicitorId) {
		this.solicitorId = solicitorId;
	}
	/**
	 * @return the defendantSolicitorName
	 */
	public String getDefendantSolicitorName() {
		return defendantSolicitorName;
	}
	/**
	 * @param defendantSolicitorName the defendantSolicitorName to set
	 */
	public void setDefendantSolicitorName(String defendantSolicitorName) {
		this.defendantSolicitorName = defendantSolicitorName;
	}
	/**
	 * @return the defendantSolicitorAddress
	 */
	public String getDefendantSolicitorAddress() {
		return defendantSolicitorAddress;
	}
	/**
	 * @param defendantSolicitorAddress the defendantSolicitorAddress to set
	 */
	public void setDefendantSolicitorAddress(String defendantSolicitorAddress) {
		this.defendantSolicitorAddress = defendantSolicitorAddress;
	}
	
	/**
	 * @return the defendantName
	 */
	public List<NFIXDefendantValue> getDefendant() {
		return defendants;
	}
	/**
	 * @param defendantName the defendantName to set
	 */
	public void setDefendants(List<NFIXDefendantValue> defendants) {
		this.defendants = defendants;
	}
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
