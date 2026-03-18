package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refcalendar.RefCalendar;
import uk.gov.courtservice.xhibit.business.entities.refcalendar.RefCalendarMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCalendarCriteria;

/**
 * This [SysRef] Helper channels all RefCalendar related queries.
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version $Revision: 1.0 $
 */
public class RefCalendarHelper extends AbstractHelper {
	
	private final static String NO = "N";
	private RefCalendarMaintainer refCalendarMaintainer = null;
 
	private CourtMaintainer courtMaintainer = null;

    public RefCalendarHelper() {
    }
    
    @SuppressWarnings("unchecked")
	public Collection findCalendarDays(RefCalendarCriteria criteria) throws SysRefControllerException {
        final String METHOD_NAME = "::findCalendarDays ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection calendarDays = this.newCollection();
        if (criteria.getPrimaryKey() == null) {
        	try {
        		log.debug("find by criteria " + criteria.toString());
        		Collection locals = getRefCalendarMaintainer().findByCalDateAndCourtId(criteria.getStartDate(), criteria.getCourtId());
        		if (!locals.isEmpty()) {
	            	for (RefCalendar local : (Collection<RefCalendar>) locals) {
	            		RefCalendarComplexValue value = getRefCalendarMaintainer().getRefCalendarComplexValue(local);
	            		calendarDays.add(value);
	            	}
        		}
        	} catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
            }
        } else {
            try {
                log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
                RefCalendar localRef = getRefCalendarMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefCalendarComplexValue value = getRefCalendarMaintainer().getRefCalendarComplexValue(localRef);
               	calendarDays.add(value);	
            } catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
            }
        }
        populateComplexValues(criteria, calendarDays);
        log.debug(METHOD_NAME + METHOD_EXIT);
        return calendarDays;
        
    }

    public boolean isCourtAvailableOnDate(Integer courtId, Date date) throws ObjectNotFoundException {
    	String METHOD_NAME = "isCourtAvailableOnDate";
    	log.debug(METHOD_ENTER + METHOD_NAME + "(" + courtId + "," + date + ")");
		boolean result = false;
    	try {
    		@SuppressWarnings("unchecked")
			Collection<RefCalendar> locals = getRefCalendarMaintainer().findByCourtIdAndCalDate(courtId, date, date);
    		if (locals != null && !locals.isEmpty()) {
    			RefCalendar local = ((List<RefCalendar>)locals).get(0);
    			result = !NO.equals(local.getAvail());
    		}
    	} catch (ObjectNotFoundException ex) {
    		CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
    	}
    	log.debug(METHOD_NAME + METHOD_EXIT);
        return result;
    }

    @SuppressWarnings("unchecked")
	private void populateComplexValues(RefCalendarCriteria criteria, Collection calendarDays) throws SysRefControllerException {
    	String METHOD_NAME = "populateComplexValues";
    	log.debug(METHOD_ENTER + METHOD_NAME + "(" + criteria + "," + calendarDays + ")");
    	if (!calendarDays.isEmpty()) {
        	for (RefCalendarComplexValue calendarDay : (Collection<RefCalendarComplexValue>) calendarDays) {
				try {
					// Populate CourtBasicValue
					Court court = this.getCourtMaintainer().findByPrimaryKey(calendarDay.getCourtId());
					CourtBasicValue courtBasicValue = this.getCourtMaintainer().getCourtBasicValue(court);
	        		calendarDay.setCourt(courtBasicValue);
				} catch (ObjectNotFoundException anException) {
					CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
	                throw this.buildSysObjectNotFoundException("ERR_NO", criteria, anException);
				}
        	}
        }
    	log.debug(METHOD_NAME + METHOD_EXIT);
    }
    
    /*
     * findByQuery methods
     * ------------------------------------------------------------------------------------------------
     */
   
   private RefCalendarMaintainer getRefCalendarMaintainer() {
       if (this.refCalendarMaintainer == null) {
           log.debug(": Lazy initialise this.courtSiteMaintainer");
           this.refCalendarMaintainer = new RefCalendarMaintainer();
       }
       return this.refCalendarMaintainer;
   }
   
   private CourtMaintainer getCourtMaintainer() {
       if (this.courtMaintainer == null) {
           log.debug(": Lazy initialise this.courtMaintainer");
           this.courtMaintainer = new CourtMaintainer();
       }
       return this.courtMaintainer;
   }
}