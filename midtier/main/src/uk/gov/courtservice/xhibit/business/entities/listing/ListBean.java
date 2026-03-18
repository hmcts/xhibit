package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ListBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer listTypeId, Integer listParentId, Integer courtId,
			String draftOrFinal, Integer listNumber, Date listStartDate, Date listEndDate, Date publishDate,
			String publishStatus, String publishErrorReason, String obsInd, String userDisplayName) 
					throws CreateException {	
		setListTypeId(listTypeId);	
		setListParentId(listParentId);	
		setCourtId(courtId);
		setDraftOrFinal(draftOrFinal);
		setListNumber(listNumber);	
		setListStartDate(listStartDate);
		setListEndDate(listEndDate);
		setPublishDate(publishDate);
		setPublishStatus(publishStatus);
		setPublishErrorReason(publishErrorReason);	
		setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer listTypeId, Integer listParentId, Integer courtId,
			String draftOrFinal, Integer listNumber, Date listStartDate, Date listEndDate, Date publishDate,
			String publishStatus, String publishErrorReason, String obsInd, String userDisplayName) 
					throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------    
	public abstract Integer getListId();
	public abstract Integer getListTypeId();
	public abstract Integer getListParentId();
	public abstract Integer getCourtId();
	public abstract String getDraftOrFinal();
	public abstract Integer getListNumber();
	public abstract Date getListStartDate();
	public abstract Date getListEndDate();
	public abstract Date getPublishDate();
	public abstract String getPublishStatus();
	public abstract String getPublishErrorReason();
	public abstract String getObsInd();	
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate( );
	public abstract Date getLastUpdateDate( );
	public abstract Integer getVersion();
	
	public abstract void setListId(Integer listId);	
	public abstract void setListTypeId(Integer listTypeId);	
	public abstract void setListParentId(Integer listParentId);	
	public abstract void setCourtId(Integer courtId);
	public abstract void setDraftOrFinal(String draftOrFinal);
	public abstract void setListNumber(Integer listNumber);	
	public abstract void setListStartDate(Date listStartDate);
	public abstract void setListEndDate(Date listEndDate);
	public abstract void setPublishDate(Date publishDate);
	public abstract void setPublishStatus(String publishStatus);
	public abstract void setPublishErrorReason(String publishErrorReason);	
	public abstract void setObsInd(String obsInd);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate );
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract void setVersion(Integer version);
    
}