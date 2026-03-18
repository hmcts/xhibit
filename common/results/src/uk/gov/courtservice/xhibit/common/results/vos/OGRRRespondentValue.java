package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class OGRRRespondentValue implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String caseNumber;
	private String caseType;
	private String magCourtConvictionDate;
	private String courtCode;
	private String courtName;
	private String courtFullName;
	private String respondentName;
	private String respondentAddress;
	private String respondentDx;
	private String orderDate;
	private String juniorCounsel;
	private String queensCounsel;
	private String grantedBy;
	private String solicitorName;
	private String solicitorAddress;
	private String solicitorDx;
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the caseNumber
	 */
	public String getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @param caseNumber the caseNumber to set
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	/**
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}

	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	/**
	 * @return the magCourtConvictionDate
	 */
	public String getMagCourtConvictionDate() {
		return magCourtConvictionDate;
	}

	/**
	 * @param magCourtConvictionDate the magCourtConvictionDate to set
	 */
	public void setMagCourtConvictionDate(String magCourtConvictionDate) {
		this.magCourtConvictionDate = magCourtConvictionDate;
	}

	/**
	 * @return the courtCode
	 */
	public String getCourtCode() {
		return courtCode;
	}

	/**
	 * @param courtCode the courtCode to set
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the courtFullName
	 */
	public String getCourtFullName() {
		return courtFullName;
	}

	/**
	 * @param courtFullName the courtFullName to set
	 */
	public void setCourtFullName(String courtFullName) {
		this.courtFullName = courtFullName;
	}

	/**
	 * @return the respondentName
	 */
	public String getRespondentName() {
		return respondentName;
	}

	/**
	 * @param respondenttName the respondentName to set
	 */
	public void setRespondentName(String respondentName) {
		this.respondentName = respondentName;
	}

	/**
	 * @return the respondentAddress
	 */
	public String getRespondentAddress() {
		return respondentAddress;
	}

	/**
	 * @param respondentAddress the respondentAddress to set
	 */
	public void setRespondentAddress(String respondentAddress) {
		this.respondentAddress = respondentAddress;
	}

	/**
	 * @return the respondentDx
	 */
	public String getRespondentDx() {
		return respondentDx;
	}

	/**
	 * @param respondentDx the respondentDx to set
	 */
	public void setRespondentDx(String respondentDx) {
		this.respondentDx = respondentDx;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the solicitorName
	 */
	public String getSolicitorName() {
		return solicitorName;
	}

	/**
	 * @param solicitorName the solicitorName to set
	 */
	public void setSolicitorName(String solicitorName) {
		this.solicitorName = solicitorName;
	}

	/**
	 * @return the solicitorAddress
	 */
	public String getSolicitorAddress() {
		return solicitorAddress;
	}

	/**
	 * @param solicitorAddress the solicitorAddress to set
	 */
	public void setSolicitorAddress(String solicitorAddress) {
		this.solicitorAddress = solicitorAddress;
	}

	/**
	 * @return the solicitorDx
	 */
	public String getSolicitorDx() {
		return solicitorDx;
	}

	/**
	 * @param solicitorDx the solicitorDx to set
	 */
	public void setSolicitorDx(String solicitorDx) {
		this.solicitorDx = solicitorDx;
	}

	/**
	 * @return the juniorCounsel
	 */
	public String getJuniorCounsel() {
		return juniorCounsel;
	}

	/**
	 * @param juniorCounsel the juniorCounsel to set
	 */
	public void setJuniorCounsel(String juniorCounsel) {
		this.juniorCounsel = juniorCounsel;
	}

	/**
	 * @return the queensCounsel
	 */
	public String getQueensCounsel() {
		return queensCounsel;
	}

	/**
	 * @param queensCounsel the queensCounsel to set
	 */
	public void setQueensCounsel(String queensCounsel) {
		this.queensCounsel = queensCounsel;
	}

	/**
	 * @return the grantedBy
	 */
	public String getGrantedBy() {
		return grantedBy;
	}

	/**
	 * @param grantedBy the grantedBy to set
	 */
	public void setGrantedBy(String grantedBy) {
		this.grantedBy = grantedBy;
	}



}
