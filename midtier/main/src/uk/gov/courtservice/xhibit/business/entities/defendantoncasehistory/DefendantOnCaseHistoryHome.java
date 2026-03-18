package uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DefendantOnCaseHistoryHome extends javax.ejb.EJBLocalHome {
	public DefendantOnCaseHistory create(Integer defendantOnCaseHistoryId, Integer defendantHistoryId, Integer caseHistoryId, Integer defendantNumber) throws CreateException;

	public DefendantOnCaseHistory findByPrimaryKey(Integer defendantOnCaseHistoryId) throws FinderException;
	
	public Collection<DefendantOnCaseHistory> findByCaseHistoryId(Integer caseHistoryId) throws FinderException;
	
	public Collection<DefendantOnCaseHistory> findByDefendantHistoryId(Integer defendantHistoryId) throws FinderException;

}
