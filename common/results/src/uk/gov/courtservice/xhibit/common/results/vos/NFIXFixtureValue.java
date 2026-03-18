package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;

public class NFIXFixtureValue implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<NFIXSolicitorValue> nfixSolicitorValues;
	private String caseNumber;
	private String fixtureDate;
	private String courtAddress;
	private String hearingAddress;
	private String hearingCodeDescription;
	private String courtName;
	private String courtTelephone;
	private Integer fixtureId;
	private String listNote;
	private String predefinedListNote;
	
	public NFIXFixtureValue(){
		nfixSolicitorValues = new ArrayList<NFIXSolicitorValue>();
	}

	public List<NFIXSolicitorValue> getNfixSolicitorValues() {
		return nfixSolicitorValues;
	}

	public void setNfixSolicitorValues(List<NFIXSolicitorValue> nfixSolicitorValues) {
		this.nfixSolicitorValues = nfixSolicitorValues;
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
	 * @return the fixtureDate
	 */
	public String getFixtureDate() {
		return fixtureDate;
	}
	/**
	 * @param fixtureDate the fixtureDate to set
	 */
	public void setFixtureDate(String fixtureDate) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			this.fixtureDate = XDateFormat.format(formatter.parse(fixtureDate), XDateFormat.DATEFORMAT);
		}catch(ParseException e){
			this.fixtureDate = fixtureDate;
		}
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
	 * @return the hearingAddress
	 */
	public String getHearingAddress() {
		return hearingAddress;
	}

	/**
	 * @param hearingAddress the hearingAddress to set
	 */
	public void setHearingAddress(String hearingAddress) {
		this.hearingAddress = hearingAddress;
	}

	/**
	 * @return the hearingCodeDescription
	 */
	public String getHearingCodeDescription() {
		return hearingCodeDescription;
	}
	/**
	 * @param hearingCodeDescription the hearingCodeDescription to set
	 */
	public void setHearingCodeDescription(String hearingCodeDescription) {
		this.hearingCodeDescription = hearingCodeDescription;
	}
	
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getCourtTelephone() {
		return courtTelephone;
	}
	public void setCourtTelephone(String courtTelephone) {
		this.courtTelephone = courtTelephone;
	}
	
	public Integer getFixtureId() {
		return fixtureId;
	}
	
	public void setFixtureId(Integer fixtureId)
	{
		this.fixtureId = fixtureId;
	}
	public String getListNote() {
		return listNote;
	}
	public void setListNote(String listNote) {
		this.listNote = listNote;
	}
	public String getPredefinedListNote() {
		return predefinedListNote;
	}
	public void setPredefinedListNote(String predefinedListNote) {
		this.predefinedListNote = predefinedListNote;
	}
}
