package uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DefOnCaseRefSolFirmHome extends javax.ejb.EJBLocalHome {
	public DefOnCaseRefSolFirm create(Integer DefOnCaseRefSolFirmId, Integer defendantOnCaseId,
			Integer refSolicitorFirmId, Integer crestCpfId, String repType, java.util.Date repStDate,
			java.util.Date repEndDate, java.util.Date lastUpdateDate, java.util.Date creationDate, String createdBy,
			String lastUpdatedBy, Integer version, String solicitorRef, String obsInd, Integer legalAidOrderId) throws CreateException;

	public DefOnCaseRefSolFirm create(Integer defendantOnCaseId, Integer refSolicitorFirmId, Integer crestCpfId,
			String repType, java.util.Date repStDate, java.util.Date repEndDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException;

	public DefOnCaseRefSolFirm findByPrimaryKey(Integer defendantId) throws FinderException;

	public Collection findPrivateRepByDefendantOnCaseId(Integer defOnCaseId)
			throws FinderException;
	
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId)
			throws FinderException;


}