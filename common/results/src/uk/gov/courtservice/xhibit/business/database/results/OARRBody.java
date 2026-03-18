package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class OARRBody implements Serializable {
private static final long serialVersionUID = 1L;
	
	private String caseNumber;
	private String courtCode;
	private String prosecutorName;
	private String prosecutorAddress;
	private String amendmentDate;
	private String juniorCounsel;
	private String queensCounsel;
	private String newSolicitorName;
	private String newSolicitorAddress;
	private String newSolicitorDx;
	private String previousSolicitorName;
	private String previousSolicitorAddress;
	private String previousSolicitorDx;
	private String amendmentType;	
	private String courtAddress;
	private String courtTelephone;
	
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
	 * @return the prosecutorName
	 */
	public String getProsecutorName() {
		return prosecutorName;
	}


	/**
	 * @param prosecutorName the prosecutorName to set
	 */
	public void setProsecutorName(String prosecutorName) {
		this.prosecutorName = prosecutorName;
	}

	/**
	 * @return the prosecutorAddress
	 */
	public String getProsecutorAddress() {
		return prosecutorAddress;
	}


	/**
	 * @param prosecutorAddress the prosecutorAddress to set
	 */
	public void setProsecutorAddress(String prosecutorAddress) {
		this.prosecutorAddress = prosecutorAddress;
	}


	/**
	 * @return the orderDate
	 */
	public String getAmendmentDate() {
		return amendmentDate;
	}


	/**
	 * @param orderDate the orderDate to set
	 */
	public void setAmendmentDate(String amendmentDate) {
		this.amendmentDate = amendmentDate;
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
	 * @return the newSolicitorName
	 */
	public String getNewSolicitorName() {
		return newSolicitorName;
	}


	/**
	 * @param newSolicitorName the newSolicitorName to set
	 */
	public void setNewSolicitorName(String newSolicitorName) {
		this.newSolicitorName = newSolicitorName;
	}


	/**
	 * @return the newSolicitorAddress
	 */
	public String getNewSolicitorAddress() {
		return newSolicitorAddress;
	}


	/**
	 * @param newSolicitorAddress the newSolicitorAddress to set
	 */
	public void setNewSolicitorAddress(String newSolicitorAddress) {
		this.newSolicitorAddress = newSolicitorAddress;
	}


	/**
	 * @return the newSolicitorDx
	 */
	public String getNewSolicitorDx() {
		return newSolicitorDx;
	}


	/**
	 * @param newSolicitorDx the newSolicitorDx to set
	 */
	public void setNewSolicitorDx(String newSolicitorDx) {
		this.newSolicitorDx = newSolicitorDx;
	}


	/**
	 * @return the previousSolicitorName
	 */
	public String getPreviousSolicitorName() {
		return previousSolicitorName;
	}


	/**
	 * @param previousSolicitorName the previousSolicitorName to set
	 */
	public void setPreviousSolicitorName(String previousSolicitorName) {
		this.previousSolicitorName = previousSolicitorName;
	}


	/**
	 * @return the previousSolicitorAddress
	 */
	public String getPreviousSolicitorAddress() {
		return previousSolicitorAddress;
	}


	/**
	 * @param previousSolicitorAddress the previousSolicitorAddress to set
	 */
	public void setPreviousSolicitorAddress(String previousSolicitorAddress) {
		this.previousSolicitorAddress = previousSolicitorAddress;
	}


	/**
	 * @return the previousSolicitorDx
	 */
	public String getPreviousSolicitorDx() {
		return previousSolicitorDx;
	}


	/**
	 * @param previousSolicitorDx the previousSolicitorDx to set
	 */
	public void setPreviousSolicitorDx(String previousSolicitorDx) {
		this.previousSolicitorDx = previousSolicitorDx;
	}

	/**
	 * @return the amendmentType
	 */
	public String getAmendmentType() {
		return amendmentType;
	}


	/**
	 * @param amendmentType the amendmentType to set
	 */
	public void setAmendmentType(String amendmentType) {
		this.amendmentType = amendmentType;
	}


	/**
	 * @return the courtAddress
	 */
	public String getCourtAddress() {
		return courtAddress;
	}


	/**
	 * @param courtAddress the courtAddress to set
	 */
	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}


	/**
	 * @return the courtTelephone
	 */
	public String getCourtTelephone() {
		return courtTelephone;
	}


	/**
	 * @param courtTelephone the courtTelephone to set
	 */
	public void setCourtTelephone(String courtTelephone) {
		this.courtTelephone = courtTelephone;
	}
}
