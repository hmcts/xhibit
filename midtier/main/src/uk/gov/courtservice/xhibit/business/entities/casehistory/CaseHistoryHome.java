package uk.gov.courtservice.xhibit.business.entities.casehistory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CaseHistoryHome extends javax.ejb.EJBLocalHome {
	public CaseHistory create(Integer caseHistoryId, String caseType, Integer courtId, Integer caseNumber,
			String psdCTCode, java.sql.Timestamp committalDate, String reasonDeleted, String caseTitle,
			java.sql.Timestamp dateArchived, java.sql.Timestamp sentForTrial, String createdBy, String lastUpdatedBy,
			Integer version) throws CreateException;

	public CaseHistory findByPrimaryKey(Integer caseHistoryId) throws FinderException;

	public CaseHistory findByCaseHistoryId(Integer caseHistoryId) throws FinderException;
	
	public CaseHistory findByCaseNumberCaseTypeAndCourtId(String caseType, String caseNumber, Integer courtId) throws FinderException;

}
