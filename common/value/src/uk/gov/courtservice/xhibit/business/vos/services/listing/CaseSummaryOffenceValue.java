package uk.gov.courtservice.xhibit.business.vos.services.listing;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseSummaryOffenceValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Vincent
 * @version 1.0
 */

public class CaseSummaryOffenceValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer offenceId;
	private Integer chargeId;
	private Integer crestOffenceSeqNo;
	private String offenceDesc;

	/**
	 * @return the offenceId
	 */
	public Integer getOffenceId() {
		return offenceId;
	}

	/**
	 * @param offenceId the offenceId to set
	 */
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	/**
	 * @return the chargeId
	 */
	public Integer getChargeId() {
		return chargeId;
	}

	/**
	 * @param chargeId the chargeId to set
	 */
	public void setChargeId(Integer chargeId) {
		this.chargeId = chargeId;
	}

	/**
	 * @return the crestOffenceSeqNo
	 */
	public Integer getCrestOffenceSeqNo() {
		return crestOffenceSeqNo;
	}

	/**
	 * @param crestOffenceSeqNo the crestOffenceSeqNo to set
	 */
	public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
		this.crestOffenceSeqNo = crestOffenceSeqNo;
	}

	/**
	 * @return the offenceDesc
	 */
	public String getOffenceDesc() {
		return offenceDesc;
	}

	/**
	 * @param offenceDesc the offenceDesc to set
	 */
	public void setOffenceDesc(String offenceDesc) {
		this.offenceDesc = offenceDesc;
	}


}
