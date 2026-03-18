package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ListBasicValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class ListBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	public static interface DraftOrFinal {
    	public static final String DRAFT = "D"; 
    	public static final String FINAL = "F"; 
    }

	public static interface PublishStatus {
    	public static final String SUCCESS = "SUCCESS"; 
    	public static final String FAILURE = "FAILURE"; 
    	public static final String DELETED = "DELETED"; 
    }

	private Integer listId;
	private Integer listTypeId;
	private Integer listParentId;
	private Integer courtId;
	private String draftOrFinal;
	private Integer listNumber;
	private Date listStartDate;
	private Date listEndDate;
	private Date publishDate;
	private String publishStatus;
	private String publishErrorReason;
	private String obsInd;
	private String createdBy;
	private String lastUpdatedBy; 
	private Date creationDate;
	private Date lastUpdateDate;

	public ListBasicValue() {
		super();
	}

	public ListBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public Integer getListTypeId() {
		return listTypeId;
	}

	public void setListTypeId(Integer listTypeId) {
		this.listTypeId = listTypeId;
	}

	public Integer getListParentId() {
		return listParentId;
	}

	public void setListParentId(Integer listParentId) {
		this.listParentId = listParentId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getDraftOrFinal() {
		return draftOrFinal;
	}

	public void setDraftOrFinal(String draftOrFinal) {
		this.draftOrFinal = draftOrFinal;
	}

	public Integer getListNumber() {
		return listNumber;
	}

	public void setListNumber(Integer listNumber) {
		this.listNumber = listNumber;
	}

	public Date getListStartDate() {
		return listStartDate;
	}

	public void setListStartDate(Date listStartDate) {
		this.listStartDate = listStartDate;
	}

	public Date getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Date listEndDate) {
		this.listEndDate = listEndDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public boolean isPublished() {
		return PublishStatus.SUCCESS.equals(getPublishStatus()) ||
				PublishStatus.FAILURE.equals(getPublishStatus());
	}
	
	public boolean isPublishedSuccess() {
		return PublishStatus.SUCCESS.equals(getPublishStatus());
	}
	
	public boolean isPublishedFailure() {
		return PublishStatus.FAILURE.equals(getPublishStatus());
	}
	
	public boolean isSaved() {
		return getPublishStatus() == null ||
				getPublishStatus().equals("");
	}
	
	public boolean isDeleted() {
		return PublishStatus.DELETED.equals(getPublishStatus());
	}

	public boolean isDraft() {
		return DraftOrFinal.DRAFT.equals(getDraftOrFinal());
	}

	public String getPublishErrorReason() {
		return publishErrorReason;
	}

	public void setPublishErrorReason(String publishErrorReason) {
		this.publishErrorReason = publishErrorReason;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}	
}