package uk.gov.courtservice.xhibit.client.listings.details;

import java.util.Collection;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CaseListingDetailModel {

    public static interface ValidValues {
        public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();       
    }
	protected interface JudgeTypes {
		public static String HIGHCOURT_JUDGE = "HJ";
		public static String CIRCUIT_JUDGE = "CJ";
	}
	
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String EMPTY_STRING = "";	
	
	private Integer caseId;
	private Integer courtId;
	private CaseBasicValue caseBasicValue;
	private CaseListingEntryBasicValue caseListingEntryBasicValue;
	private DiaryNoteEntryBasicValue highlightDiaryNoteEntry;
	private DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry;
	private DiaryNoteEntryBasicValue freeTextDiaryNoteEntry;
	private DiaryNoteEntryBasicValue interpreterDiaryNoteEntry;	
	private RefSystemCodeBasicValue refJudgeType;
	private DirectionsForCaseBasicValue directionsForCase;
	private RefSystemCodeBasicValue ticketType;
	private RefJudgeBasicValue refJudge;
	private Collection<CaseDiaryFixtureComplexValue> fixtures;
	private Collection<DefendantValue> defendants;
	private XhibitApplicationController xac;
	private boolean exitImmediately = false;
	
	public CaseListingDetailModel(XhibitApplicationController xac, Integer caseId, Integer courtId) {
		setXac(xac);
		setCaseId(caseId);
		setCourtId(courtId);
		clearmodel();
	}
	
	public void clearmodel() {
        setCaseBasicValue(null);
        setCaseListingEntryBasicValue(null);
		setHighlightDiaryNoteEntry(null);
		setInterpreterDiaryNoteEntry(null);
		setPreDefinedDiaryNoteEntry(null);
		setFreeTextDiaryNoteEntry(null);
		setInterpreterDiaryNoteEntry(null);
		setRefJudgeType(null);
		setTicketType(null);
    	setDirectionsForCase(null);
		setRefJudge(null);
		setFixtures(null);
		setDefendants(null);
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public XhibitApplicationController getXac() {
		return xac;
	}

	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public String getCaseTitle() {
        return getCaseBasicValue().getCaseTitle();
	}

	public String getCaseType() {
        return getCaseBasicValue().getCaseType();
	}
    
	public void setCaseType(String caseType) {
    	getCaseBasicValue().setCaseType(caseType);
	}

	public Integer getCaseNumber() {
        return getCaseBasicValue().getCaseNumber();
	}
    
	public void setCaseNumber(Integer caseNumber) {
    	getCaseBasicValue().setCaseNumber(caseNumber);
	}

	public Boolean getSecureCourtAsBoolean() {
		return getBooleanFromString(getSecureCourt());
	}
    
	public String getSecureCourt() {
		return getCaseBasicValue().getSecureCourt();
	}

	public void setSecureCourtAsBoolean(boolean secureCourt) {
		setSecureCourt(getStringFromBoolean(secureCourt));
	}
	
	public void setSecureCourt(String secureCourt) {
		getCaseBasicValue().setSecureCourt(secureCourt);
	}

	public boolean getVideoLinkRequiredAsBoolean() 
	{
		return getBooleanFromString(getVideoLinkRequired());
	}
	
	public String getVideoLinkRequired() {
		return getCaseBasicValue().getVideoLinkRequired();
	}

	public void setVideoLinkRequiredAsBoolean(boolean videoLinkRequired) {
		setVideoLinkRequired(getStringFromBoolean(videoLinkRequired));
	}
	
	public void setVideoLinkRequired(String videoLinkRequired) {
		getCaseBasicValue().setVideoLinkRequired(videoLinkRequired);
	}

	public DiaryNoteEntryBasicValue getHighlightDiaryNoteEntry() {
		return highlightDiaryNoteEntry;
	}

	public void setHighlightDiaryNoteEntry(DiaryNoteEntryBasicValue highlightDiaryNoteEntry) {
		this.highlightDiaryNoteEntry = highlightDiaryNoteEntry;
	}
	
	public DiaryNoteEntryBasicValue getPreDefinedDiaryNoteEntry() {
		return preDefinedDiaryNoteEntry;
	}

	public void setPreDefinedDiaryNoteEntry(DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry) {
		this.preDefinedDiaryNoteEntry = preDefinedDiaryNoteEntry;
	}
	
	public DiaryNoteEntryBasicValue getInterpreterDiaryNoteEntry() {
		return interpreterDiaryNoteEntry;
	}

	public void setInterpreterDiaryNoteEntry(DiaryNoteEntryBasicValue interpreterDiaryNoteEntry) {
		this.interpreterDiaryNoteEntry = interpreterDiaryNoteEntry;
	}

	public DiaryNoteEntryBasicValue getFreeTextDiaryNoteEntry() {
		return freeTextDiaryNoteEntry;
	}
	
	public void setFreeTextDiaryNoteEntry(DiaryNoteEntryBasicValue freeTextDiaryNoteEntry) {
		this.freeTextDiaryNoteEntry = freeTextDiaryNoteEntry;
	}
	
	public RefSystemCodeBasicValue getRefJudgeType() {
		return refJudgeType;
	}

	public void setRefJudgeType(RefSystemCodeBasicValue refJudgeType) {
		this.refJudgeType = refJudgeType;
		setRefJudgeTypeId(refJudgeType != null ? refJudgeType.getId() : null);
	}

	private void setRefJudgeTypeId(Integer refJudgeTypeId) {
		if (getCaseListingEntryBasicValue() != null) {
			getCaseListingEntryBasicValue().setRefJudgeTypeId(refJudgeTypeId);
		}
	}
	
	public RefSystemCodeBasicValue getTicketType() {
		return ticketType;
	}

	public void setTicketType(RefSystemCodeBasicValue ticketType) {
		this.ticketType = ticketType;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	
	public Integer getCourtSiteId() {
		return getCaseListingEntryBasicValue().getCourtSiteId();
	}

	public void setCourtSiteId(Integer courtSiteId) {
		getCaseListingEntryBasicValue().setCourtSiteId(courtSiteId);
	}

	public RefJudgeBasicValue getRefJudge() {
		return refJudge;
	}

	public void setRefJudge(RefJudgeBasicValue refJudge) {
		this.refJudge = refJudge;
		setRefJudgeId(refJudge != null ? refJudge.getId() : null);
	}

	private void setRefJudgeId(Integer refJudgeId) {
		if (getCaseListingEntryBasicValue() != null) {
			getCaseListingEntryBasicValue().setJudgeId(refJudgeId);
		} 
	}
	
	public String getTimeEst() {		
		if (getDirectionsForCase().getTrialTimeEstimate() != null) {
			Integer timeEstInt = getDirectionsForCase().getTrialTimeEstimate().intValue();
			return String.valueOf(timeEstInt);
	}
		return null;
	}
	
	public void setTimeEst(String timeEst) {
		Float timeEstFloat = timeEst != null && !timeEst.isEmpty() ? Float.valueOf(timeEst) : null;
		getDirectionsForCase().setTrialTimeEstimate(timeEstFloat);
	}
	
	public Integer getTimeEstUnit() {
		return getDirectionsForCase().getTrialTimeUnit();
	}
	
	public void setTimeEstUnit(Integer timeEstUnit) {
		getDirectionsForCase().setTrialTimeUnit(timeEstUnit);
	}
	
	public Collection<CaseDiaryFixtureComplexValue> getFixtures() {
		return fixtures;
	}

	public void setFixtures(Collection<CaseDiaryFixtureComplexValue> fixtures) {
		this.fixtures = fixtures;
	}
	
	public String getHighlightNote() {
		return getHighlightDiaryNoteEntry() != null ? getHighlightDiaryNoteEntry().getDiaryNoteText() : null;
	}

	public void setHighlightNote(String highlightNote) {
		getHighlightDiaryNoteEntry().setDiaryNoteText(highlightNote);
	}
	
	public Integer getHighlightClassificationId() {
		return getHighlightDiaryNoteEntry() != null ? getHighlightDiaryNoteEntry().getNoteClassificationId() : null;
	}

	public void setHighlightNoteClassificationId(Integer highlightNoteClassId) {
		getHighlightDiaryNoteEntry().setNoteClassificationId(highlightNoteClassId);
	}

	public String getFreeText() {
		return getFreeTextDiaryNoteEntry() != null ? getFreeTextDiaryNoteEntry().getDiaryNoteText() : null;
	}
	
	public void setFreeTextNote(String freeTextNote) {
		getFreeTextDiaryNoteEntry().setDiaryNoteText(freeTextNote);
	}

	public String getInterpreterNote() {
		return getInterpreterDiaryNoteEntry() != null ? getInterpreterDiaryNoteEntry().getDiaryNoteText() : null;
	}
	
	public void setInterpreterNote(String interpreterNote) {
		getInterpreterDiaryNoteEntry().setDiaryNoteText(interpreterNote);
	}
	
	public void setInterpreterNoteClassificationId(Integer interpreterNoteClassId) {
		getInterpreterDiaryNoteEntry().setNoteClassificationId(interpreterNoteClassId);
	}
	
	public Integer getFreeTextClassificationId() {
		return getFreeTextDiaryNoteEntry() != null ? getFreeTextDiaryNoteEntry().getNoteClassificationId() : null;
	}

	public void setFreeTextNoteClassificationId(Integer freeTextNoteClassId) {
		getFreeTextDiaryNoteEntry().setNoteClassificationId(freeTextNoteClassId);
	}

	public Integer getPreDefinedNoteTypeId() {
		return getPreDefinedDiaryNoteEntry() != null ? getPreDefinedDiaryNoteEntry().getDiaryNotePreDefinedId() : null;
	}

	public void setPreDefinedNoteTypeId(Integer preDefinedNoteTypeId) {
		getPreDefinedDiaryNoteEntry().setDiaryNotePreDefinedId(preDefinedNoteTypeId);
	}	

	public Integer getPreDefinedClassificationId() {
		return getPreDefinedDiaryNoteEntry() != null ? getPreDefinedDiaryNoteEntry().getNoteClassificationId() : null;
	}
	
	public void setPreDefinedClassificationId(Integer preDefinedNoteClassId) {
		getPreDefinedDiaryNoteEntry().setNoteClassificationId(preDefinedNoteClassId);
	}

	public String getReqJudge() {
		return getRefJudge() != null ? getFullName(
				getRefJudge().getJudgeType() != null ? getRefJudge().getJudgeType() : EMPTY_STRING, 
				getRefJudge().getTitle() != null ? getRefJudge().getTitle() : EMPTY_STRING, 
			    getRefJudge().getFirstName() != null ? getRefJudge().getFirstName() : EMPTY_STRING, 
				getRefJudge().getSurname() != null ? getRefJudge().getSurname() : EMPTY_STRING) : null;
	}
	
	public boolean getBooleanFromString(String val) {
		return val != null && val.equals(YES);
	}
    
    public String getStringFromBoolean(boolean val) {
    	return val ? YES : NO;
    }
    
	public String getFullName(String judgeType, String title, String firstName, String surname) {
		return judgeType.concat(" ").concat(title).concat(" ").concat(firstName).concat(" ").concat(surname);
	}

	public Collection<DefendantValue> getDefendants() {
		return defendants;
	}

	public void setDefendants(Collection<DefendantValue> defendants) {
		this.defendants = defendants;
	}

	public String getCaseSection28Name1() {
		return getCaseBasicValue().getSection28Name1();
	} 
	
	public void setCaseSection28Name1(String caseSection28Name1) {
		getCaseBasicValue().setSection28Name1(caseSection28Name1);
	} 
	
	public String getCaseSection28Name2() {
		return getCaseBasicValue().getSection28Name2();
	}

	public void setCaseSection28Name2(String caseSection28Name2) {
		getCaseBasicValue().setSection28Name2(caseSection28Name2);
	} 
	
	public String getCaseSection28Phone1() {
		return getCaseBasicValue().getSection28Phone1();
	}

	public void setCaseSection28Phone1(String caseSection28Phone1) {
		getCaseBasicValue().setSection28Phone1(caseSection28Phone1);
	} 
	
	public String getCaseSection28Phone2() {
		return getCaseBasicValue().getSection28Phone2();
	}
	
	public void setCaseSection28Phone2(String caseSection28Phone2) {
		getCaseBasicValue().setSection28Phone2(caseSection28Phone2);
	}

	public Integer getDefaultHearingTypeId() {
		return getCaseBasicValue().getDefaultHearingType();
	}

	public void setDefaultHearingTypeId(Integer defaultHearingTypeId) {
		getCaseBasicValue().setDefaultHearingType(defaultHearingTypeId);
	}

	public boolean isHighCourtJudgeType() { 
		if (getRefJudgeType() != null) {
			return JudgeTypes.HIGHCOURT_JUDGE.equals(getRefJudgeType().getCode());	
		}
		return false;
	}
	
	public boolean isCircuitJudgeType() { 
		if (getRefJudgeType() != null) {
			return JudgeTypes.CIRCUIT_JUDGE.equals(getRefJudgeType().getCode());	
		}
		return false;
	}
	
	public Integer getRefJudgeTypeId() {
		return getRefJudgeType() != null ? getRefJudgeType().getId() : null;
	}
	
	public Integer getRefJudgeId() {
		return getRefJudge() != null ? getRefJudge().getId() : null;
	}

	public CaseBasicValue getCaseBasicValue() {
		return caseBasicValue;
	}

	public void setCaseBasicValue(CaseBasicValue caseBasicValue) {
		this.caseBasicValue = caseBasicValue;
	}

	public CaseListingEntryBasicValue getCaseListingEntryBasicValue() {
		return caseListingEntryBasicValue;
	}

	public void setCaseListingEntryBasicValue(CaseListingEntryBasicValue caseListingEntryBasicValue) {
		this.caseListingEntryBasicValue = caseListingEntryBasicValue;
	}

	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCase;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCase) {
		this.directionsForCase = directionsForCase;
	}

	public Integer getCaseListingEntryId() {
		return getCaseListingEntryBasicValue() != null ? getCaseListingEntryBasicValue().getCaseListingEntryId() : null;
	}

	public boolean isExitImmediately() {
		return exitImmediately;
	}

	public void setExitImmediately(boolean exitImmediately) {
		this.exitImmediately = exitImmediately;
	}

	public boolean isReadOnly() {
		return getCaseBasicValue().getDateTransTo() != null;
	}
}
