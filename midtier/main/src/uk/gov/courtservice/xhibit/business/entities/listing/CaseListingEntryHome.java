package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CaseListingEntryHome extends javax.ejb.EJBLocalHome
{
	public CaseListingEntry create(Integer caseId, Integer refJudgeTypeId, 
			Integer courtId, Integer courtSiteId, Integer judgeId,  String obsInd, String userDisplayName)
			throws CreateException;

	public CaseListingEntry findByPrimaryKey(Integer pk) 
			throws FinderException;

	public CaseListingEntry findByCaseId(Integer caseId) 
			throws FinderException;
	
	public Collection<CaseListingEntry> findByJudgeId(Integer judgeId) 
			throws FinderException;
}
