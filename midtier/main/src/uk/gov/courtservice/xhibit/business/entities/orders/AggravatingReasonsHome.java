package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface AggravatingReasonsHome extends EJBLocalHome {
	public AggravatingReasons create(Integer aggravatingReasonsId, Integer defendantOnCaseId, Integer refAggravatingReasonsId, 
			String obsInd, String userDisplayName) throws CreateException;

	public AggravatingReasons findByPrimaryKey(Integer aggravatingReasonsId) throws FinderException;
	public Collection findNonObsoleteByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
	public Collection findByDefOnCaseIdAndRefAggId(Integer defendantOnCaseId, Integer refAggravatingReasonsId) throws FinderException;
}