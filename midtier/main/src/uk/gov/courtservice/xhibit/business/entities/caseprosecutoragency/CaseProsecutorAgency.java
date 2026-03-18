package uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency;

import java.sql.Timestamp;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.casereference.CaseReference;
import uk.gov.courtservice.xhibit.business.entities.refmonitoringcategory.RefMonitoringCategory;

public interface CaseProsecutorAgency extends CSEntityLocal {

	public java.lang.Integer getCaseProsAgencyId();

	public void setCaseProsAgencyId(java.lang.Integer caseProsAgencyId);

	public java.lang.String getProsecutorType();

	public void setProsecutorType(java.lang.String prosecutorType);

	public java.lang.Integer getCaseId();

	public void setCaseId(java.lang.Integer caseId);

	public java.lang.Integer getRefProsecutorAgencyId();

	public void setRefProsecutorAgencyId(java.lang.Integer refProsecutorAgencyId);

	public java.lang.String getCreatedBy();

	public void setCreatedBy(java.lang.String createdBy);

	public java.lang.String getLastUpdatedBy();

	public void setLastUpdatedBy(java.lang.String lastUpdatedBy);

	public java.lang.Integer getVersion();

	public void setVersion(java.lang.Integer version);

	public java.lang.String getRespondentStatus();

	public void setRespondentStatus(java.lang.String respondentStatus);

	public java.util.Collection getProsecutorRefSolFirms();

	public void setProsecutorRefSolFirms(java.util.Collection prosecutorRefSolFirms);

	public java.lang.String getObsInd();
	
	public void setObsInd(java.lang.String obsInd);
}