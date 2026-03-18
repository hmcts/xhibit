package uk.gov.courtservice.xhibit.business.entities.bwhistory;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;

public interface BwHistoryHome extends javax.ejb.EJBLocalHome {
	
	public BwHistory create(DefendantOnCase defendantOnCase, Date bwIssueDate, Date bwEndDate, 
			   String bcStatusBwIssued, String bcStatusBwEnded, String withdrawn, String absconding,
			   String obsInd, String userDisplayName) throws CreateException;
	
	public BwHistory findByPrimaryKey(Integer bwHistoryId) throws FinderException;
	
	public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
	
	public Collection findOutstandingBenchWarrantsForDefOnCaseId(Integer defendantOnCaseId) throws FinderException;
}