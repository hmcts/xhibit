package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Collection;

/**
 * An interface to describe a table row.
 * @author westalll
 *
 */
public interface ListCaseTableRow {

	Integer getCaseId();

	void setCaseId(Integer caseId);

	Integer getHearingTypeId();

	void setHearingTypeId(Integer hearingTypeId);

	Integer getGroupNumber();

	void setGroupNumber(Integer groupNumber);

	String getCaseNumber();

	void setCaseNumber(String caseNumber);

	String getCaseTitle();

	void setCaseTitle(String caseTitle);

	String getHearingTypeCode();

	void setHearingTypeCode(String hearingTypeCode);

	String getEst();

	void setEst(String est);

	Collection<Integer> getDefendantOnCaseIds();

	void setDefendantOnCaseIds(Collection<Integer> defendantOnCaseIds);

	Integer getListNotePredefinedId();

	void setListNotePredefinedId(Integer listNotePredefinedId);

	String getListNoteText();

	void setListNoteText(String listNoteText);

	Integer getCaseDiaryFixtureId();

	void setCaseDiaryFixtureId(Integer caseDiaryFixtureId);

	Integer getParentCaseOnListId();

	void setParentCaseOnListId(Integer parentCaseOnListId);

	int hashCode();

	boolean equals(Object obj);

	boolean isListedOnRight();

	void setListedOnRight(boolean listedOnRight);

}