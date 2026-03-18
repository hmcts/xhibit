package uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * DefendantOnCaseHistory object.
 * @author waltersn
 *
 */
public interface DefendantOnCaseHistory extends CSEntityLocal {

	public Integer getDefendantOnCaseHistoryId();	
	public void setDefendantOnCaseHistoryId(Integer defendantOnCaseHistoryId);

	public Integer getDefendantHistoryId();	
	public void setDefendantHistoryId(Integer defendantHistoryId);
		
	public Integer getCaseHistoryId();	
	public void setCaseHistoryId(Integer caseHistoryId);
	
	public Integer getDefendantNumber();	
	public void setDefendantNumber(Integer defendantNumber);
}
