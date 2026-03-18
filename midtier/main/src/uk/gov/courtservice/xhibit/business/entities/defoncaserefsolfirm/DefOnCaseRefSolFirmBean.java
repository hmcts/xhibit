package uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class DefOnCaseRefSolFirmBean extends CSEntityBean implements EntityBean {

	public Integer ejbCreate(Integer DefOnCaseRefSolFirmId, Integer defendantOnCaseId, Integer refSolicitorFirmId,
			Integer crestCpfId, String repType, java.util.Date repStDate, java.util.Date repEndDate,
			java.util.Date lastUpdateDate, java.util.Date creationDate, String createdBy, String lastUpdatedBy,
			Integer version, String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException {

		setDefOnCaseRefSolFirmId(DefOnCaseRefSolFirmId);
		setDefendantOnCaseId(defendantOnCaseId);
		setRefSolicitorFirmId(refSolicitorFirmId);
		setCrestCpfId(crestCpfId);
		setRepStDate(repStDate);
		setRepEndDate(repEndDate);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setSolicitorRef(solicitorRef);
		setRepType(repType);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);

		return null;
	}

	public Integer ejbCreate(Integer defendantOnCaseId, Integer refSolicitorFirmId, Integer crestCpfId, String repType,
			java.util.Date repStDate, java.util.Date repEndDate, java.util.Date creationDate, String createdBy, String lastUpdatedBy,
			String solicitorRef, String obsInd, Integer legalAidOrderId) throws CreateException {

		setDefendantOnCaseId(defendantOnCaseId);
		setRefSolicitorFirmId(refSolicitorFirmId);
		setCrestCpfId(crestCpfId);
		setRepStDate(repStDate);
		setRepEndDate(repEndDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setSolicitorRef(solicitorRef);
		setRepType(repType);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);

		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer DefOnCaseRefSolFirmId, Integer defendantOnCaseId, Integer refSolicitorFirmId,
			Integer crestCpfId, String repType, java.util.Date repStDate, java.util.Date repEndDate,
			java.util.Date lastUpdateDate, java.util.Date creationDate, String createdBy, String lastUpdatedBy,
			Integer version, String solicitorRef, String obsInd, Integer legalAidOrderId)
			throws CreateException {
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer defendantOnCaseId, Integer refSolicitorFirmId, Integer crestCpfId, String repType,
			java.util.Date repStDate, java.util.Date repEndDate, java.util.Date creationDate, String createdBy, String lastUpdatedBy,
			String solicitorRef, String obsInd, Integer legalAidOrderId) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setDefOnCaseRefSolFirmId(Integer DefOnCaseRefSolFirmId);

	public abstract Integer getDefOnCaseRefSolFirmId();

	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);

	public abstract Integer getDefendantOnCaseId();

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

	public abstract void setRepType(String repType);

	public abstract String getRepType();
	
	public abstract String getObsInd();
	
	public abstract void setObsInd(String string);
	
	public abstract void setLegalAidOrderId(Integer legalAidOrderId);

	public abstract Integer getLegalAidOrderId();

}