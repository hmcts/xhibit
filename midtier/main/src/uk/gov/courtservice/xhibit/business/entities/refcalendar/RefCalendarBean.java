package uk.gov.courtservice.xhibit.business.entities.refcalendar;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Entity Bean class for RefCalendar.
 *  
 * @author grewalg
 *
 */
public abstract class RefCalendarBean extends CSEntityBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6929640854253611619L;

	public Integer ejbCreate(String avail, Integer courtId, Date calDate, String description, String sysAcAvail) throws CreateException {

		setAvail(avail);
		setCourtId(courtId);
		setCalDate(calDate);
		setDescription(description);
		setSysAcAvail(sysAcAvail);

		return null;
	}

	public void ejbPostCreate(String avail, Integer courtId, Date calDate, String description, String sysAcAvail) throws CreateException {
	}

	public abstract String getAvail();

	public abstract void setAvail(String avail);

	public abstract Date getCalDate();

	public abstract void setCalDate(Date calDate);

    public abstract String getCreatedBy();
    
	public abstract void setCreatedBy(String createdBy);

	public abstract String getLastUpdatedBy();
	
	public abstract void setLastUpdatedBy(String lastUpdatedBy);

	public abstract Date getCreationDate( );
	
	public abstract void setCreationDate(Date creationDate );

	public abstract Date getLastUpdateDate( );
	
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	   
	public abstract Integer getVersion();
	
	public abstract void setVersion(Integer version);
	
	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract Integer getRefCalendarId();

	public abstract void setRefCalendarId(Integer refCalendarId);

	public abstract String getSysAcAvail();

	public abstract void setSysAcAvail(String sysAcAvail);
}
