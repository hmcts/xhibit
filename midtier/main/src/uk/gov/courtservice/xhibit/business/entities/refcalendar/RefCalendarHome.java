package uk.gov.courtservice.xhibit.business.entities.refcalendar;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Local home interface for XhbRefCalendar.

 * 
 * @author grewalg
 *
 */
public interface RefCalendarHome extends EJBLocalHome { 

    public RefCalendar create(String avail, Integer courtId, Date calDate, String description, String sysAcAvail) throws CreateException;
    
    public RefCalendar findByPrimaryKey(Integer refCalendarId) throws FinderException;

    public Collection findByCourtIdAndCalDate(Integer courtId, Date fromDate, Date toDate) throws FinderException;

    public Collection findByCalDateAndCourtId(Date calDate, Integer courtId) throws FinderException;
}