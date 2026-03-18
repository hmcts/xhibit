package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface List extends CSEntityLocal {
 	
	public void setListId(Integer listId);	
	public void setListTypeId(Integer listTypeId);	
	public void setListParentId(Integer listParentId);	
	public void setCourtId(Integer courtId);
	public void setDraftOrFinal(String draftOrFinal);
	public void setListNumber(Integer listNumber);	
	public void setListStartDate(Date listStartDate);
	public void setListEndDate(Date listEndDate);
	public void setPublishDate(Date publishDate);
	public void setPublishStatus(String publishStatus);
	public void setPublishErrorReason(String publishErrorReason);	
	public void setObsInd(String obsInd);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);
	public void setVersion(Integer version);

	public Integer getListId();
	public Integer getListTypeId();
	public Integer getListParentId();
	public Integer getCourtId();
	public String getDraftOrFinal();
	public Integer getListNumber();
	public Date getListStartDate();
	public Date getListEndDate();
	public Date getPublishDate();
	public String getPublishStatus();
	public String getPublishErrorReason();
	public String getObsInd();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	public Integer getVersion();
	

}