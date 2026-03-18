package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * PDDA Message Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class PddaBatchBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer pddaBatchId; 
	private Integer noOfRecordsInBatch; 
	private Date batchOpenedDatetime; 
	private Date batchClosedDatetime;
	private Integer batchStatusId; 
	private String batchMessage; 
	private Integer batchNoResends; 
	private Date batchSentTime;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;


    public PddaBatchBasicValue() {
    }

    public PddaBatchBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getPddaBatchId() {
		return pddaBatchId;
	}

	public Integer getNoOfRecordsInBatch() {
		return noOfRecordsInBatch;
	}

	public Date getBatchOpenedDatetime() {
		return batchOpenedDatetime;
	}

	public Date getBatchClosedDatetime() {
		return batchClosedDatetime;
	}

	public Integer getBatchStatusId() {
		return batchStatusId;
	}

	public String getBatchMessage() {
		return batchMessage;
	}

	public Integer getBatchNoResends() {
		return batchNoResends;
	}

	public Date getBatchSentTime() {
		return batchSentTime;
	}

	public void setPddaBatchId(Integer pddaBatchId) {
		this.pddaBatchId = pddaBatchId;
	}

	public void setNoOfRecordsInBatch(Integer noOfRecordsInBatch) {
		this.noOfRecordsInBatch = noOfRecordsInBatch;
	}

	public void setBatchOpenedDatetime(Date batchOpenedDatetime) {
		this.batchOpenedDatetime = batchOpenedDatetime;
	}

	public void setBatchClosedDatetime(Date batchClosedDatetime) {
		this.batchClosedDatetime = batchClosedDatetime;
	}

	public void setBatchStatusId(Integer batchStatusId) {
		this.batchStatusId = batchStatusId;
	}

	public void setBatchMessage(String batchMessage) {
		this.batchMessage = batchMessage;
	}

	public void setBatchNoResends(Integer batchNoResends) {
		this.batchNoResends = batchNoResends;
	}

	public void setBatchSentTime(Date batchSentTime) {
		this.batchSentTime = batchSentTime;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Displaying contents of this PddaBatchBasicValue object.");
		sb.append("PddaBatchId: "+this.getPddaBatchId());
		sb.append("No. Records in Batch: "+this.getNoOfRecordsInBatch());
		sb.append("BatchOpenedDatetime: "+this.getBatchOpenedDatetime());
		sb.append("BatchClosedDatetime: "+this.getBatchClosedDatetime());
		sb.append("BatchStatusId: "+this.getBatchStatusId());
		sb.append("BatchMessage: "+this.getBatchMessage());
		sb.append("BatchNoResends: "+this.getBatchNoResends());
		sb.append("BatchSentDatetime: "+this.getBatchSentTime());
		sb.append("BatchLastUpdateDate: "+this.getLastUpdateDate());
		
		
		return sb.toString();
	}
}