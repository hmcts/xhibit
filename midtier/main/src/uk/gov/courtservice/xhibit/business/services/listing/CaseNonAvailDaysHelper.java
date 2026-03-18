package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseNonAvailDays;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseNonAvailDaysMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseNonAvailDaysBasicValue;

/**
 * <p>
 * Title: CaseNonAvailDaysHelper
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
public class CaseNonAvailDaysHelper extends AbstractHelper {
	private static final Logger LOG = CSServices.getLogger(CaseNonAvailDaysHelper.class);
	
	private CaseNonAvailDaysMaintainer caseNonAvailDaysMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseNonAvailDaysHelper() {
		caseNonAvailDaysMaintainer = new CaseNonAvailDaysMaintainer();
	}

	/**
	 * Description: Find non available days
	 * 
	 * @param caseNonAvailDaysId
	 * @return CaseNonAvailDaysBasicValue
	 * @throws FinderException 
	 */
	public CaseNonAvailDaysBasicValue findByPrimaryKey(Integer caseNonAvailDaysId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: findByPrimaryKey(caseNonAvailDaysId="+caseNonAvailDaysId+")");
       	}
		try {
			CaseNonAvailDays local = caseNonAvailDaysMaintainer.findByPrimaryKey(caseNonAvailDaysId);
			CaseNonAvailDaysBasicValue result = caseNonAvailDaysMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of non available days by caseId
	 * 
	 * @param caseId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection<CaseNonAvailDaysBasicValue> findByCaseId(Integer caseId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: findByCaseId(caseId="+caseId+")");
       	}
		try {
			Collection result = newCollection();
			Collection locals = caseNonAvailDaysMaintainer.findByCaseId(caseId);
			for (CaseNonAvailDays local : (Collection<CaseNonAvailDays>) locals) {
				CaseNonAvailDaysBasicValue value = caseNonAvailDaysMaintainer.getBasicValue(local);
				result.add(value);
			}
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Return true if non available days exist for caseId and date
	 * 
	 * @param caseId
	 * @param date
	 * @return 
	 * @return true if non-available day
	 * @throws FinderException 
	 */
	public Boolean isCaseNonAvailDay(Integer caseId, Date date) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: isCaseNonAvailDay(caseId="+caseId+", date="+date+")");
       	}
		try {
			Collection locals = caseNonAvailDaysMaintainer.findByCaseIdAndDate(caseId, date);
			return !locals.isEmpty();
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Return true if non available days exist for caseId and dates
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @return 
	 * @return true if non-available day
	 * @throws FinderException 
	 */
	public Boolean isCaseNonAvailDay(Integer caseId, Date fromDate, Date toDate) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: isCaseNonAvailDay(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+")");
       	}
		try {
			Collection locals = caseNonAvailDaysMaintainer.findByCaseIdAndDate(caseId, fromDate, toDate);
			return !locals.isEmpty();
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Save Case Listing Non Available Days
	 * 
	 * @param CaseNonAvailDaysBasicValue
	 * @param String
	 * @returns Integer
	 * @throws FinderException, CreateException 
	 */
    public Integer saveCaseNonAvailDaysBasicValue(CaseNonAvailDaysBasicValue basicValue, String userDisplayName) throws FinderException, CreateException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveCaseNonAvailDaysBasicValue(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        CaseNonAvailDays local = null;
        try {
        	if (basicValue.getId() == null) {
        		local = (CaseNonAvailDays) caseNonAvailDaysMaintainer.create(basicValue, userDisplayName);        		
        	} else {
        		local = (CaseNonAvailDays) caseNonAvailDaysMaintainer.update(basicValue, userDisplayName);        		
        	}
        	return local.getNadId();
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, ListingsControllerBean.class);
	        throw ex;
	    } catch (EJBException ex) {
	    	CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	    	throw ex;
	    }
	}
    
    /**
	 * Description: Soft Delete Case Listing Non Available Days
	 * 
	 * @param CaseNonAvailDaysBasicValue
	 * @param String
	 * @throws FinderException 
	 */
    public void deleteCaseNonAvailDaysBasicValue(CaseNonAvailDaysBasicValue basicValue, String displayName) throws FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: deleteCaseNonAvailDaysBasicValue(basicValue="+basicValue+", displayName="+displayName+")");
       	}
        try {
        	caseNonAvailDaysMaintainer.delete(basicValue,displayName);
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
	    }    	
    }  
	
}