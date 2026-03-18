package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;
import java.math.BigDecimal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class MonetaryOrderTrackingBasicValue extends CSAbstractValue {
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
	//* public MonetaryOrderTrackingBasicValue()
	//*
	//* Purpose : Basic constructor
	//* To call : Nothing
	//* Returns : New MonetaryOrderTrackingBasicValue
	//*   Notes :
	//********************************************************************************
	public MonetaryOrderTrackingBasicValue() {
		super();
	}


	//********************************************************************************
	//* public MonetaryOrderTrackingBasicValue(Integer id, Integer version)
	//*
	//* Purpose : Basic constructor
	//* To call : 
	//* Returns : New MonetaryOrderTrackingBasicValue
	//*   Notes :
	//********************************************************************************
    public MonetaryOrderTrackingBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	//********************************************************************************
	//* public MonetaryOrderTrackingBasicValue(...)
	//*
	//* Purpose : Full constructor
	//* To call : Full set of class parameters
	//* Returns : New MonetaryOrderTrackingBasicValue
	//*   Notes :
	//********************************************************************************
	public MonetaryOrderTrackingBasicValue(Date acknowledgementDate, Integer caseId, Integer collectMagistratesCourtId,
			BigDecimal compensation, BigDecimal costs, String createdBy, Date creationDate, Integer defendantOnCaseId,
			BigDecimal fined, String lastUpdatedBy, Date lastUpdateDate, Integer monetaryOrderTrackingId, String obsInd,
			Date orderDate, Integer version) {
		setAcknowledgementDate(acknowledgementDate);
		setCaseId(caseId);
		setCollectMagistratesCourtId(collectMagistratesCourtId);
		setCompensation(compensation);
		setCosts(costs);
		setCreatedBy(createdBy);
		setCreationDate(creationDate);
		setDefendantOnCaseId(defendantOnCaseId);
		setFined(fined);
		setLastUpdatedBy(lastUpdatedBy);
		setLastUpdateDate(lastUpdateDate);
		setMonetaryOrderTrackingId(monetaryOrderTrackingId);
		setObsInd(obsInd);
		setOrderDate(orderDate);
		setVersion(version);
	}


	//********************************************************************************
	//* public MonetaryOrderTrackingBasicValue(...)
	//*
	//* Purpose : Copy constructor
	//* To call : monetaryOrderTrackingBasicValue - Basic value to copy
	//* Returns : New MonetaryOrderTrackingBasicValue
	//*   Notes :
	//********************************************************************************
	public MonetaryOrderTrackingBasicValue(MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue) {
		setAcknowledgementDate(monetaryOrderTrackingBasicValue.getAcknowledgementDate());
		setCaseId(monetaryOrderTrackingBasicValue.getCaseId());
		setCollectMagistratesCourtId(monetaryOrderTrackingBasicValue.getCollectMagistratesCourtId());
		setCompensation(monetaryOrderTrackingBasicValue.getCompensation());
		setCosts(monetaryOrderTrackingBasicValue.getCosts());
		setCreatedBy(monetaryOrderTrackingBasicValue.getCreatedBy());
		setCreationDate(monetaryOrderTrackingBasicValue.getCreationDate());
		setDefendantOnCaseId(monetaryOrderTrackingBasicValue.getDefendantOnCaseId());
		setFined(monetaryOrderTrackingBasicValue.getFined());
		setLastUpdatedBy(monetaryOrderTrackingBasicValue.getLastUpdatedBy());
		setLastUpdateDate(monetaryOrderTrackingBasicValue.getLastUpdateDate());
		setMonetaryOrderTrackingId(monetaryOrderTrackingBasicValue.getMonetaryOrderTrackingId());
		setObsInd(monetaryOrderTrackingBasicValue.getObsInd());
		setOrderDate(monetaryOrderTrackingBasicValue.getOrderDate());
		setVersion(monetaryOrderTrackingBasicValue.getVersion());
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
