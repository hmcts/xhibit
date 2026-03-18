package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.io.Serializable;
import java.util.Date;

/**
 * Search criteria for the [System] Reference data type, RefCalendar.
 * <p>
 * NB. There is also a *Business* Reference Type named Court!
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.1
 */
public class RefCalendarCriteria extends AbstractSearchCriteria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String TABLE_NAME = "RefCalendar";

    protected interface AttributeNames {
    	
        public static final String COURT_ID = "courtId";
        
    	public static final String START_DATE = "startDate";  
    	
    }

    /**
     * Default constructor.
     */
    public RefCalendarCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }
    
    public Date getStartDate() {
    	String timeVal = this.getAttribute(AttributeNames.START_DATE);
   		return timeVal != null ? new Date(Long.valueOf(timeVal)) : null;	
    }

    public void setStartDate(Date newValue) {
        this.setAttribute(AttributeNames.START_DATE, String.valueOf(newValue.getTime()));
    }
    
    public Integer getCourtId() {
        String courtId = getAttribute(AttributeNames.COURT_ID);
        if (courtId == null)
            return null;
        else
            return Integer.valueOf(courtId);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }
 
    /**
     * Returns the ordered list of arguments for criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getStartDate() };
    }
}