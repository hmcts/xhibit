package uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * DefendantOnCaseHistory Bean.
 * @author waltersn
 *
 */
abstract public class DefendantOnCaseHistoryBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer defendantOnCaseHistoryId, Integer defendantHistoryId, Integer caseHistoryId, Integer defendantNumber) throws CreateException {
		setDefendantOnCaseHistoryId(defendantOnCaseHistoryId);
		setDefendantHistoryId(defendantHistoryId);
		setCaseHistoryId(caseHistoryId);
		setDefendantNumber(defendantNumber);
		return null;
	}

	public void ejbPostCreate(Integer defendantOnCaseHistoryId, Integer defendantHistoryId, Integer caseHistoryId, Integer defendantNumber) throws CreateException {
	}
	
	public abstract Integer getDefendantOnCaseHistoryId();	
	public abstract void setDefendantOnCaseHistoryId(Integer defendantOnCaseHistoryId);

	public abstract Integer getDefendantHistoryId();	
	public abstract void setDefendantHistoryId(Integer defendantHistoryId);
		
	public abstract Integer getCaseHistoryId();	
	public abstract void setCaseHistoryId(Integer caseHistoryId);
	
	public abstract Integer getDefendantNumber();	
	public abstract void setDefendantNumber(Integer defendantNumber);

}
