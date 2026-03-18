package uk.gov.courtservice.xhibit.business.entities.monetaryordertracking;

import java.util.Date;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import java.math.BigDecimal;

public interface MonetaryOrderTracking extends CSEntityLocal {

	public Date getAcknowledgementDate();
	public void setAcknowledgementDate(Date acknowledgementDate);

	public Integer getCaseId();
	public void setCaseId(Integer caseId);

	public Integer getCollectMagistratesCourtId();
	public void setCollectMagistratesCourtId(Integer collectMagistratesCourtId);

	public BigDecimal getCompensation();
	public void setCompensation(BigDecimal compensation);

	public BigDecimal getCosts();
	public void setCosts(BigDecimal costs);

	public String getCreatedBy();
	public void setCreatedBy(String createdBy);

	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public Integer getDefendantOnCaseId();
	public void setDefendantOnCaseId(Integer defendantOnCaseId);

	public BigDecimal getFined();
	public void setFined(BigDecimal fined);

	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);

	public Date getLastUpdateDate();
	public void setLastUpdateDate(Date lastUpdateDate);

	public Integer getMonetaryOrderTrackingId();
	public void setMonetaryOrderTrackingId(Integer monetaryOrderTrackingId);

	public String getObsInd();
	public void setObsInd(String obsInd);

	public Date getOrderDate();
	public void setOrderDate(Date orderDate);

	public Integer getVersion();
	public void setVersion(Integer version);
	
	public void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);
	public uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();
	
	public void setDefendantOnCases(uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase defendantOnCase);
	public uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase getDefendantOnCases();
	
}
