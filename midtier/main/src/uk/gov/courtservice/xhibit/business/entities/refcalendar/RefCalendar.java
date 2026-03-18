package uk.gov.courtservice.xhibit.business.entities.refcalendar;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * RefCalendar main class.
 * 
 * @author grewalg
 *
 */
public interface RefCalendar extends CSEntityLocal {
	
    public String getAvail();
    
    public void setAvail(String avail);

    public Date getCalDate();
    
    public void setCalDate(Date calDate);

    public Integer getCourtId();
    
    public void setCourtId(Integer courtId);

	public String getCreatedBy();
	
	public void setCreatedBy(String createdBy);
    
	public Date getCreationDate();
	
	public void setCreationDate(Date creationDate);
	
    public String getDescription();
    
    public void setDescription(String description);
    
	public Date getLastUpdateDate();
	
	public void setLastUpdateDate(Date lastUpdateDate);

	public String getLastUpdatedBy();
	
	public void setLastUpdatedBy(String lastUpdatedBy);

	public Integer getRefCalendarId();

    public void setRefCalendarId(Integer refCalendarId);
    
    public String getSysAcAvail();
    
    public void setSysAcAvail(String sysAcAvail);
    
	public Integer getVersion();
	
	public void setVersion(Integer version);
}