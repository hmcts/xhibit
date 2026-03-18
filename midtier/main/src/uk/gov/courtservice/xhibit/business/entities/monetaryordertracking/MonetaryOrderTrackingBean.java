package uk.gov.courtservice.xhibit.business.entities.monetaryordertracking;

import java.util.Date;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;

abstract public class MonetaryOrderTrackingBean extends CSEntityBean implements EntityBean {
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Date acknowledgementDate, Case caze, Integer collectMagistratesCourtId,
			BigDecimal compensation, BigDecimal costs, String createdBy, Date creationDate, DefendantOnCase defOnCase,
			BigDecimal fined, String lastUpdatedBy, Date lastUpdateDate, Integer monetaryOrderTrackingId, String obsInd,
			Date orderDate, Integer version) throws CreateException {
		setAcknowledgementDate(acknowledgementDate);
		setCollectMagistratesCourtId(collectMagistratesCourtId);
		setCompensation(compensation);
		setCosts(costs);
		setCreatedBy(createdBy);
		setCreationDate(creationDate);
		setFined(fined);
		setLastUpdatedBy(lastUpdatedBy);
		setLastUpdateDate(lastUpdateDate);
		setMonetaryOrderTrackingId(monetaryOrderTrackingId);
		setObsInd(obsInd);
		setOrderDate(orderDate);
		setVersion(version);
		return null;
	}


	@SuppressWarnings("unused")
	public void ejbPostCreate(Date acknowledgementDate, Case caze, Integer collectMagistratesCourtId,
			BigDecimal compensation, BigDecimal costs, String createdBy, Date creationDate, DefendantOnCase defOnCase,
			BigDecimal fined, String lastUpdatedBy, Date lastUpdateDate, Integer monetaryOrderTrackingId, String obsInd,
			Date orderDate, Integer version) throws CreateException {
		//Nothing
		setCaze(caze);
		setDefendantOnCases(defOnCase);
	}

	//--- acknowledgementDate ---
	public abstract Date getAcknowledgementDate();
	public abstract void setAcknowledgementDate(Date acknowledgementDate);
	//--- caseId ---
	public abstract Integer getCaseId();
	public abstract void setCaseId(Integer caseId);
	//--- collectMagistratesCourt ---
	public abstract Integer getCollectMagistratesCourtId();
	public abstract void setCollectMagistratesCourtId(Integer collectMagistratesCourtId);
	//--- compensation ---
	public abstract BigDecimal getCompensation();
	public abstract void setCompensation(BigDecimal compensation);
	//--- costs ---
	public abstract BigDecimal getCosts();
	public abstract void setCosts(BigDecimal costs);
	//--- createdBy ---
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);
	//--- creationDate ---
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	//--- defendantOnCaseId ---
	public abstract Integer getDefendantOnCaseId();
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	//--- fined ---
	public abstract BigDecimal getFined();
	public abstract void setFined(BigDecimal fined);
	//--- lastUpdatedBy ---
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	//--- lastUpdateDate ---
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	//--- monetaryOrderTrackingId ---
	public abstract Integer getMonetaryOrderTrackingId();
	public abstract void setMonetaryOrderTrackingId(Integer monetaryOrderTrackingId);
	//--- obsInd ---
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	//--- orderDate ---
	public abstract Date getOrderDate();
	public abstract void setOrderDate(Date orderDate);
	//--- version ---
	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);
	
	public abstract void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);
	public abstract uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();
	
	public abstract void setDefendantOnCases(uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase defendantOnCase);
	public abstract uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase getDefendantOnCases();
}
