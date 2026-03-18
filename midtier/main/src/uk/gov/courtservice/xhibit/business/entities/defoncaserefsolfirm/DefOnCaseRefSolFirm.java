package uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DefOnCaseRefSolFirm extends CSEntityLocal {

	public Integer getDefOnCaseRefSolFirmId();

	public void setDefOnCaseRefSolFirmId(Integer defOnCaseRefSolFirmId);

	public void setDefendantOnCaseId(Integer defendantOnCaseId);

	public Integer getDefendantOnCaseId();

	public void setRefSolicitorFirmId(Integer refSolicitorFirmId);

	public Integer getRefSolicitorFirmId();

	public void setCrestCpfId(Integer crestCpfId);

	public Integer getCrestCpfId();

	public void setRepType(String repType);

	public String getRepType();

	public void setRepStDate(Date repStDate);

	public Date getRepStDate();

	public void setRepEndDate(Date repEndDate);

	public Date getRepEndDate();

	public void setLastUpdateDate(Date lastUpdateDate);

	public Date getLastUpdateDate();

	public void setCreationDate(Date creationDate);

	public Date getCreationDate();

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public String getLastUpdatedBy();

	public void setLastUpdatedBy(String lastUpdatedBy);

	public Integer getVersion();

	public void setVersion(Integer version);

	public String getSolicitorRef();

	public void setSolicitorRef(String solicitorRef);

	public String getObsInd();
	
	public void setObsInd(String string);
	
	public Integer getLegalAidOrderId();
	
	public void setLegalAidOrderId(Integer legalAidOrderId);

}
