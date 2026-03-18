package uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;

public interface ProsecutorRefSolFirmHome extends javax.ejb.EJBLocalHome {
	public ProsecutorRefSolFirm create(Integer crestCpfId, String repType, Date repStartDate,
			Date repEndDate, RefSolicitorFirm refSolicitorFirm, CaseProsecutorAgency caseProsAgency, String createdBy, String lastUpdatedBy,
			String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException;

	public ProsecutorRefSolFirm findByPrimaryKey(Integer prosecutorRefSolFirmId) throws FinderException;
	
	public Collection findPrivateRepByCaseProsAgency(Integer caseProsAgencyId)
			throws FinderException;
	
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId)
			throws FinderException;

}