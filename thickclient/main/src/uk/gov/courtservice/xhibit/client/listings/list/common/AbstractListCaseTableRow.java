package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Collection;

/**
 * A class to represent the data in a table row
 * within the ListCaseTableModel.
 * @author westalll
 *
 */
public abstract class AbstractListCaseTableRow implements ListCaseTableRow {

	protected Integer caseId;
	protected Integer hearingTypeId;
	protected Integer groupNumber;    
	protected String caseNumber;
	protected String caseTitle;
	protected String hearingTypeCode;
	protected String est;
	protected boolean listedOnRight = false;
	protected Collection<Integer> defendantOnCaseIds;
	protected Integer caseDiaryFixtureId;
	protected Integer parentCaseOnListId;
	protected Integer listNotePredefinedId;
	protected String listNoteText;
	
	public AbstractListCaseTableRow() {
		
	}
	
	@Override
	public Integer getCaseId() {
		return caseId;
	}

	@Override
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	@Override
	public Integer getHearingTypeId() {
		return hearingTypeId;
	}

	@Override
	public void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}

	@Override
	public Integer getGroupNumber() {
		return groupNumber;
	}

	@Override
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	@Override
	public String getCaseNumber() {
		return caseNumber;
	}

	@Override
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@Override
	public String getCaseTitle() {
		return caseTitle;
	}

	@Override
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	@Override
	public String getHearingTypeCode() {
		return hearingTypeCode;
	}

	@Override
	public void setHearingTypeCode(String hearingTypeCode) {
		this.hearingTypeCode = hearingTypeCode;
	}
	
	@Override
	public String getEst() {
		return est;
	}

	@Override
	public void setEst(String est) {
		this.est = est;
	}
	
	@Override
	public Collection<Integer> getDefendantOnCaseIds() {
		return defendantOnCaseIds;
	}

	@Override
	public void setDefendantOnCaseIds(Collection<Integer> defendantOnCaseIds) {
		this.defendantOnCaseIds = defendantOnCaseIds;
	}

	@Override
	public Integer getListNotePredefinedId() {
		return listNotePredefinedId;
	}

	@Override
	public void setListNotePredefinedId(Integer listNotePredefinedId) {
		this.listNotePredefinedId = listNotePredefinedId;
	}

	@Override
	public String getListNoteText() {
		return listNoteText;
	}

	@Override
	public void setListNoteText(String listNoteText) {
		this.listNoteText = listNoteText;
	}

	@Override
	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	@Override
	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}

	@Override
	public Integer getParentCaseOnListId() {
		return parentCaseOnListId;
	}

	@Override
	public void setParentCaseOnListId(Integer parentCaseOnListId) {
		this.parentCaseOnListId = parentCaseOnListId;
	}


	@Override
	public boolean isListedOnRight() {
		return listedOnRight;
	}

	@Override
	public void setListedOnRight(boolean listedOnRight) {
		this.listedOnRight = listedOnRight;
	}

	@Override
	abstract public int hashCode();
	
	@Override
	abstract public boolean equals(Object obj);
	

}
