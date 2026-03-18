package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface DefOnCaseOnListHome extends EJBLocalHome {
	public DefOnCaseOnList create(Integer defendantOnCaseId, Integer caseOnListId, Integer caseId, 
			String obsInd, String userDisplayName, String isCourtRoomListEntry) throws CreateException;

	public DefOnCaseOnList findByPrimaryKey(Integer caseOnListId) throws FinderException;

	public Collection<DefOnCaseOnList> findByCaseOnListId(Integer caseOnListId) throws FinderException;

	public Collection<DefOnCaseOnList> findByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException;
}