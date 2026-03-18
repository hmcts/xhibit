package uk.gov.courtservice.xhibit.business.vos.services.monetaryordertracking;

import java.util.Date;
import java.math.BigDecimal;

public class MonetaryOrderTrackingValue {
	private static final long serialVersionUID = 1L;
	private Date acknowledgementDate;
	private Integer caseId;
	private Integer collectMagistratesCourtId;
	private BigDecimal compensation;
	private BigDecimal costs;
	private String createdBy;
	private Date creationDate;
	private Integer defendantOnCaseId;
	private BigDecimal fined;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Integer monetaryOrderTrackingId;
	private String obsInd;
	private Date orderDate;
	private Integer version;

	//********************************************************************************
	//* Constructor
	//*
	//* Purpose : Constructor
	//* To call : 
	//* Returns : 
	//*   Notes :
	//********************************************************************************
	public MonetaryOrderTrackingValue() {
	}


	//********************************************************************************
	//* Getters/Setters
	//*
	//* Purpose : Get/Set class variables
	//* To call : 
	//* Returns : 
	//*   Notes :
	//********************************************************************************
	//--- acknowledgementDate ---
	public Date getAcknowledgementDate() { return acknowledgementDate; };
	public void setAcknowledgementDate(Date acknowledgementDate) { this.acknowledgementDate = acknowledgementDate; };
	//--- caseId ---
	public Integer getCaseId() { return caseId; };
	public void setCaseId(Integer caseId) { this.caseId = caseId; };
	//--- collectMagistratesCourt ---
	public Integer getCollectMagistratesCourtId() { return collectMagistratesCourtId; };
	public void setCollectMagistratesCourtId(Integer collectMagistratesCourtId) { this.collectMagistratesCourtId = collectMagistratesCourtId; };
	//--- compensation ---
	public BigDecimal getCompensation() { return compensation; };
	public void setCompensation(BigDecimal compensation) { this.compensation = compensation; };
	//--- costs ---
	public BigDecimal getCosts() { return costs; };
	public void setCosts(BigDecimal costs) { this.costs = costs; };
	//--- createdBy ---
	public String getCreatedBy() { return createdBy; };
	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; };
	//--- creationDate ---
	public Date getCreationDate() { return creationDate; };
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; };
	//--- defendantOnCaseId ---
	public Integer getDefendantOnCaseId() { return defendantOnCaseId; };
	public void setDefendantOnCaseId(Integer defendantOnCaseId) { this.defendantOnCaseId = defendantOnCaseId; };
	//--- fined ---
	public BigDecimal getFined() { return fined; };
	public void setFined(BigDecimal fined) { this.fined = fined; };
	//--- lastUpdatedBy ---
	public String getLastUpdatedBy() { return lastUpdatedBy; };
	public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; };
	//--- lastUpdateDate ---
	public Date getLastUpdateDate() { return lastUpdateDate; };
	public void setLastUpdateDate(Date lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; };
	//--- monetaryOrderTrackingId ---
	public Integer getMonetaryOrderTrackingId() { return monetaryOrderTrackingId; };
	public void setMonetaryOrderTrackingId(Integer monetaryOrderTrackingId) { this.monetaryOrderTrackingId = monetaryOrderTrackingId; };
	//--- obsInd ---
	public String getObsInd() { return obsInd; };
	public void setObsInd(String obsInd) { this.obsInd = obsInd; };
	//--- orderDate ---
	public Date getOrderDate() { return orderDate; };
	public void setOrderDate(Date orderDate) { this.orderDate = orderDate; };
	//--- version ---
	public Integer getVersion() { return version; };
	public void setVersion(Integer version) { this.version = version; };

}
