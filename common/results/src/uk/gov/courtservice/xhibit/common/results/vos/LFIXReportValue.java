package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class LFIXReportValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;
	
	private String hearingtype;
	private String casenumber;
	private String defendant;
	private String representative;
	private String ptiurn;
	private String hearingdate;
	private String hearingvenue;
	private String prosecutor;
	private String notes;
	private String courtaddress;
	private String casediaryfixture;
	private String telephoneno;
	
	public String getHearingtype() {
		return hearingtype;
	}

	public void setHearingtype(String hearingtype) {
		this.hearingtype = hearingtype;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getDefendant() {
		return defendant;
	}

	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getPtiurn() {
		return ptiurn;
	}

	public void setPtiurn(String ptiurn) {
		this.ptiurn = ptiurn;
	}

	public String getHearingdate() {
		return hearingdate;
	}

	public void setHearingdate(String hearingdate) {
		this.hearingdate = hearingdate;
	}

	public String getHearingvenue() {
		return hearingvenue;
	}

	public void setHearingvenue(String hearingvenue) {
		this.hearingvenue = hearingvenue;
	}

	public String getProsecutor() {
		return prosecutor;
	}

	public void setProsecutor(String prosecutor) {
		this.prosecutor = prosecutor;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the courtaddress
	 */
	public String getCourtaddress() {
		return courtaddress;
	}

	/**
	 * @param courtaddress the courtaddress to set
	 */
	public void setCourtaddress(String courtaddress) {
		this.courtaddress = courtaddress;
	}

	public String getCasediaryfixture() {
		return casediaryfixture;
	}

	public void setCasediaryfixture(String casediaryfixture) {
		this.casediaryfixture = casediaryfixture;
	}

	public String getTelephoneno() {
		return telephoneno;
	}

	public void setTelephoneno(String telephoneno) {
		this.telephoneno = telephoneno;
	}
}
