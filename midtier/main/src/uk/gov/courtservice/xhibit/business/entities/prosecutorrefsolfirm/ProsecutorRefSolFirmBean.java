package uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;
import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ProsecutorRefSolFirmBean extends CSEntityBean implements EntityBean {

	public Integer ejbCreate(Integer crestCpfId, String repType, Date repStartDate,
			Date repEndDate, RefSolicitorFirm RefSolicitorFirm, CaseProsecutorAgency caseProsAgency, String createdBy,
			String lastUpdatedBy, String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException {

		setCrestCpfId(crestCpfId);
		setRepType(repType);
		setRepStDate(repStartDate);
		setRepEndDate(repEndDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setSolicitorRef(solicitorRef);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer crestCpfId, String repType, Date repStartDate,
			Date repEndDate, RefSolicitorFirm RefSolicitorFirm, CaseProsecutorAgency caseProsAgency, String createdBy, String lastUpdatedBy,
			String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException {
		setRefSolicitorFirm(RefSolicitorFirm);
		setCaseProsecutorAgency(caseProsAgency);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setProsecutorRefSolFirmId(Integer prosecutorRefSolFirmId);

	public abstract Integer getProsecutorRefSolFirmId();

	public abstract void setRefSolicitorFirmId(Integer refSolicitorFirmId);

	public abstract Integer getRefSolicitorFirmId();

	public abstract void setCrestCpfId(Integer crestCpfId);

	public abstract Integer getCrestCpfId();

	public abstract void setRepStDate(java.util.Date repStDate);

	public abstract java.util.Date getRepStDate();

	public abstract void setRepEndDate(java.util.Date repEndDate);

	public abstract java.util.Date getRepEndDate();

	public abstract void setLastUpdateDate(java.util.Date lastUpdateDate);

	public abstract java.util.Date getLastUpdateDate();

	public abstract void setCreationDate(java.util.Date creationDate);

	public abstract java.util.Date getCreationDate();

	public abstract void setCreatedBy(String createdBy);

	public abstract String getCreatedBy();

	public abstract void setLastUpdatedBy(String lastUpdatedBy);

	public abstract String getLastUpdatedBy();

	public abstract void setVersion(Integer version);

	public abstract Integer getVersion();

	public abstract void setSolicitorRef(String solicitorRef);

	public abstract String getSolicitorRef();

	public abstract void setCaseProsAgencyId(Integer caseProsAgencyId);

	public abstract Integer getCaseProsAgencyId();

	public abstract void setRepType(String repType);

	public abstract String getRepType();

	public abstract RefSolicitorFirm getRefSolicitorFirm();

	public abstract void setRefSolicitorFirm(RefSolicitorFirm refSolicitorFirm);
	
	public abstract CaseProsecutorAgency getCaseProsecutorAgency();

	public abstract void setCaseProsecutorAgency(CaseProsecutorAgency caseProsecutorAgency);
	
	public abstract String getObsInd();
	
	public abstract void setObsInd(String obsInd);
	
	public abstract void setLegalAidOrderId(Integer legalAidOrderId);

	public abstract Integer getLegalAidOrderId();

}