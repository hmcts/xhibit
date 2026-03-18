package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseDiaryFixtureBasicValue
 * </p>
 * <p>
 * Description: CaseDiaryFixtureBasicValue is intended to represent case entities as stored
 * in the CaseDiaryFixture table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class CaseDiaryFixtureBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer caseDiaryFixtureId;
	private Integer caseListingEntryId;
    private Date listingDate;
    private String fixtureNoticeRequired;
    private Integer hearingTypeId;
    private Integer listNotePreDefinedId;
    private String listNoteText;
    private Integer preDefNoteClassId;
    private Integer freeTextNoteClassId;
    private Integer vacationPreDefinedRsonId;
    private String vacationFreetextReason;
    private String status;
    private String obsInd;
    private Integer courtSiteId;
	private Date dateVacated;
 
	public CaseDiaryFixtureBasicValue() {
        super();
    }
    
	public CaseDiaryFixtureBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}

	public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}

	public Date getListingDate() {
		return listingDate;
	}

	public void setListingDate(Date listingDate) {
		this.listingDate = listingDate;
	}

	public String getFixtureNoticeRequired() {
		return fixtureNoticeRequired;
	}

	public void setFixtureNoticeRequired(String fixtureNoticeRequired) {
		this.fixtureNoticeRequired = fixtureNoticeRequired;
	}

	public Integer getHearingTypeId() {
		return hearingTypeId;
	}

	public void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}

	public Integer getListNotePreDefinedId() {
		return listNotePreDefinedId;
	}

	public void setListNotePreDefinedId(Integer listNotePreDefinedId) {
		this.listNotePreDefinedId = listNotePreDefinedId;
	}

	public String getListNoteText() {
		return listNoteText;
	}

	public void setListNoteText(String listNoteText) {
		this.listNoteText = listNoteText;
	}

	public Integer getPreDefNoteClassId() {
		return preDefNoteClassId;
	}

	public void setPreDefNoteClassId(Integer preDefNoteClassId) {
		this.preDefNoteClassId = preDefNoteClassId;
	}

	public Integer getFreeTextNoteClassId() {
		return freeTextNoteClassId;
	}

	public void setFreeTextNoteClassId(Integer freeTextNoteClassId) {
		this.freeTextNoteClassId = freeTextNoteClassId;
	}

	public Integer getVacationPreDefinedRsonId() {
		return vacationPreDefinedRsonId;
	}

	public void setVacationPreDefinedRsonId(Integer vacationPreDefinedRsonId) {
		this.vacationPreDefinedRsonId = vacationPreDefinedRsonId;
	}

	public String getVacationFreetextReason() {
		return vacationFreetextReason;
	}

	public void setVacationFreetextReason(String vacationFreetextReason) {
		this.vacationFreetextReason = vacationFreetextReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	public Integer getCourtSiteId() {
		return courtSiteId;
	}

	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}
	
	public Date getDateVacated() {
		return dateVacated;
	}

	public void setDateVacated(Date dateVacated) {
		this.dateVacated = dateVacated;
	}
}
