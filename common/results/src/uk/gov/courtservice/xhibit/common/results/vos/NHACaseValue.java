package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class NHACaseValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer caseId;
	private String caseNumber;
	private String caseSubType;
	private Integer defendantNumber;
	private String courtCode;
	private String courtName;
	private String appellantName;
	private String respondentName;
	private String respondentSurname;
	private String respondentAddress;
	private String clerkToJusticeName;
	private String courtAddress;
	private String appealType;
	private String magCourtConvictionDate;
	private String origBodyDecisionDate;
	private String dateOfFixture;
	private String timeOfListing;
	private String todaysDate;
	private String courtPhoneNumber;
	private String solicitorFirmName;
	private String defendantAddress;
	private String magistrateName;
	private String magistrateAddress;
	private String inCustody;
	private String solicitorAddress;
	private String respSolName;
	private String respSolAddress;
	private String listNoteText;
	private String preDefinedListNote;
	private String hearingVenue;
	private String hearingAddress;
	private ArrayList<NHAObjectorValue> objectors;
	
	public NHACaseValue()
	{
		objectors = new ArrayList<NHAObjectorValue>();
	}

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


	public Integer getDefendantNumber() {
		return defendantNumber;
	}


	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}


	public String getCourtCode() {
		return courtCode;
	}


	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}


	public String getCourtName() {
		return courtName;
	}


	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}


	public String getAppellantName() {
		return appellantName;
	}


	public void setAppellantName(String appellantName) {
		this.appellantName = appellantName;
	}


	public String getRespondentName() {
		return respondentName;
	}


	public void setRespondentName(String respondentName) {
		this.respondentName = respondentName;
	}


	public String getRespondentAddress() {
		return respondentAddress;
	}


	public void setRespondentAddress(String respondentAddress) {
		this.respondentAddress = respondentAddress;
	}

	public String getClerkToJusticeName() {
		return clerkToJusticeName;
	}


	public void setClerkToJusticeName(String clerkToJusticeName) {
		this.clerkToJusticeName = clerkToJusticeName;
	}

	public String getCourtAddress() {
		return courtAddress;
	}


	public void setCourtAddress(String courtAddress) {
		this.courtAddress = courtAddress;
	}


	public String getMagistrateAddress() {
		return magistrateAddress;
	}


	public void setMagistrateAddress(String magistrateAddress) {
		this.magistrateAddress = magistrateAddress;
	}




	public String getAppealType() {
		return appealType;
	}


	public void setAppealType(String appealType) {
		this.appealType = appealType;
	}


	public String getMagCourtConvictionDate() {
		return magCourtConvictionDate;
	}


	public void setMagCourtConvictionDate(String magCourtConvictionDate) {
		this.magCourtConvictionDate = magCourtConvictionDate;
	}

	public String getDateOfFixture() {
		return dateOfFixture;
	}


	public void setDateOfFixture(String dateOfFixture) {
		this.dateOfFixture = dateOfFixture;
	}
	

	public String getTimeOfListing() {
		return timeOfListing;
	}


	public void setTimeOfListing(String timeOfListing) {
		this.timeOfListing = timeOfListing;
	}


	public String getTodaysDate() {
		return todaysDate;
	}


	public void setTodaysDate(String todaysDate) {
		this.todaysDate = todaysDate;
	}
	
	public String getCourtPhoneNumber() {
		return courtPhoneNumber;
	}


	public void setCourtPhoneNumber(String courtPhoneNumber) {
		this.courtPhoneNumber = courtPhoneNumber;
	}
	
	
	public String getSolicitorFirmName() {
		return solicitorFirmName;
	}


	public void setSolicitorFirmName(String solicitorFirmName) {
		this.solicitorFirmName = solicitorFirmName;
	}


	public String getDefendantAddress() {
		return defendantAddress;
	}


	public void setDefendantAddress(String defendantAddress) {
		this.defendantAddress = defendantAddress;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getInCustody() {
		return inCustody;
	}

	public void setInCustody(String inCustody) {
		this.inCustody = inCustody;
	}

	public String getMagistrateName() {
		return magistrateName;
	}

	public void setMagistrateName(String magistrateName) {
		this.magistrateName = magistrateName;
	}
	
	public ArrayList<NHAObjectorValue> getObjectors() {
		return objectors;
	}
	public void setObjectors(ArrayList<NHAObjectorValue> objectors) {
		this.objectors = objectors;
	}

	/**
	 * @return the caseSubType
	 */
	public String getCaseSubType() {
		return caseSubType;
	}

	/**
	 * @param caseSubType the caseSubType to set
	 */
	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}

	/**
	 * @return the respondentSurname
	 */
	public String getRespondentSurname() {
		return respondentSurname;
	}

	/**
	 * @param respondentSurname the respondentSurname to set
	 */
	public void setRespondentSurname(String respondentSurname) {
		this.respondentSurname = respondentSurname;
	}

	/**
	 * @return the origBodyDecisionDate
	 */
	public String getOrigBodyDecisionDate() {
		return origBodyDecisionDate;
	}

	/**
	 * @param origBodyDecisionDate the origBodyDecisionDate to set
	 */
	public void setOrigBodyDecisionDate(String origBodyDecisionDate) {
		this.origBodyDecisionDate = origBodyDecisionDate;
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
	 * @return the respSolName
	 */
	public String getRespSolName() {
		return respSolName;
	}

	/**
	 * @param respSolName the respSolName to set
	 */
	public void setRespSolName(String respSolName) {
		this.respSolName = respSolName;
	}

	/**
	 * @return the respSolAddress
	 */
	public String getRespSolAddress() {
		return respSolAddress;
	}

	/**
	 * @param respSolAddress the respSolAddress to set
	 */
	public void setRespSolAddress(String respSolAddress) {
		this.respSolAddress = respSolAddress;
	}

	/**
	 * @return the listNoteText
	 */
	public String getListNoteText() {
		return listNoteText;
	}

	/**
	 * @param listNoteText the listNoteText to set
	 */
	public void setListNoteText(String listNoteText) {
		this.listNoteText = listNoteText;
	}

	/**
	 * @return the preDefinedListNote
	 */
	public String getPreDefinedListNote() {
		return preDefinedListNote;
	}

	/**
	 * @param preDefinedListNote the preDefinedListNote to set
	 */
	public void setPreDefinedListNote(String preDefinedListNote) {
		this.preDefinedListNote = preDefinedListNote;
	}

	/**
	 * @return the hearingVenue
	 */
	public String getHearingVenue() {
		return hearingVenue;
	}

	/**
	 * @param hearingVenue the hearingVenue to set
	 */
	public void setHearingVenue(String hearingVenue) {
		this.hearingVenue = hearingVenue;
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
}
