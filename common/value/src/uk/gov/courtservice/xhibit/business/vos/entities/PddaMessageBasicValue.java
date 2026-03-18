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
public class PddaMessageBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer pddaMessageId;
	private Integer courtId;
	private Integer courtRoomId;
	private String pddaMessageGuid; 
	private Integer pddaMessageTypeId; 
	private Long pddaMessageDataId;
	private Integer pddaBatchId;
	private Date timeSent;
	private String cpDocumentName;
	private String cpDocumentStatus;
	private String cpResponseGenerated;
	private Integer cpStagingInboundId;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;


    public PddaMessageBasicValue() {
    }

    public PddaMessageBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getPddaMessageId() {
		return pddaMessageId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public Integer getCourtRoomId() {
		return courtRoomId;
	}

	public String getPddaMessageGuid() {
		return pddaMessageGuid;
	}

	public Integer getPddaMessageTypeId() {
		return pddaMessageTypeId;
	}

	public Long getPddaMessageDataId() {
		return pddaMessageDataId;
	}

	public Integer getPddaBatchId() {
		return pddaBatchId;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setPddaMessageId(Integer pddaMessageId) {
		this.pddaMessageId = pddaMessageId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}

	public void setPddaMessageGuid(String pddaMessageGuid) {
		this.pddaMessageGuid = pddaMessageGuid;
	}

	public void setPddaMessageTypeId(Integer pddaMessageTypeId) {
		this.pddaMessageTypeId = pddaMessageTypeId;
	}

	public void setPddaMessageDataId(Long pddaMessageDataId) {
		this.pddaMessageDataId = pddaMessageDataId;
	}

	public void setPddaBatchId(Integer pddaBatchId) {
		this.pddaBatchId = pddaBatchId;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public String getCpDocumentName() {
		return cpDocumentName;
	}

	public void setCpDocumentName(String cpDocumentName) {
		this.cpDocumentName = cpDocumentName;
	}

	public String getCpDocumentStatus() {
		return cpDocumentStatus;
	}

	public void setCpDocumentStatus(String cpDocumentStatus) {
		this.cpDocumentStatus = cpDocumentStatus;
	}

	public String getCpResponseGenerated() {
		return cpResponseGenerated;
	}

	public void setCpResponseGenerated(String cpResponseGenerated) {
		this.cpResponseGenerated = cpResponseGenerated;
	}

	public Integer getCpStagingInboundId() {
		return cpStagingInboundId;
	}

	public void setCpStagingInboundId(Integer cpStagingInboundId) {
		this.cpStagingInboundId = cpStagingInboundId;
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
}