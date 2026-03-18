package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseOnListMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;

/**
 * <p>
 * Title: CaseOnListHelper
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
public class CaseOnListHelper extends AbstractHelper {
	
	private CaseOnListMaintainer caseOnListMaintainer;
	private ListHelper listHelper;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseOnListHelper() {
		caseOnListMaintainer = new CaseOnListMaintainer();
		listHelper = new ListHelper();
	}

	/**
	 * Description: Find Case On List
	 * 
	 * @param caseOnListId
	 * @return CaseOnListBasicValue
	 * @throws FinderException 
	 */
	public CaseOnListBasicValue findByPrimaryKey(Integer caseOnListId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByPrimaryKey(caseOnListId="+caseOnListId+")");
       	}
		try {
			CaseOnList local = caseOnListMaintainer.findByPrimaryKey(caseOnListId);
			CaseOnListBasicValue result = caseOnListMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of Case On List by caseId
	 * 
	 * @param caseId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection<CaseOnListBasicValue> findByCaseId(Integer caseId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByCaseId(caseId="+caseId+")");
       	}
		try {
			Collection<CaseOnList> locals = caseOnListMaintainer.findByCaseId(caseId);
			Collection<CaseOnListBasicValue> results = caseOnListMaintainer.getBasicValues(locals);
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of Case On List by courtSiteId and CourtRoomId
	 * 
	 * @param courtSiteId
	 * @param courtRoomId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection findByCourtSiteIdAndCourtRoomId(Integer courtSiteId, Integer courtRoomId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByCourtSiteIdAndCourtRoomId(courtSiteId="+courtSiteId+", courtRoomId="+courtRoomId+")");
       	}
		try {
			Collection<CaseOnList> locals = caseOnListMaintainer.findByCourtSiteIdAndCourtRoomId(courtSiteId, courtRoomId);
			Collection<CaseOnListBasicValue> results = caseOnListMaintainer.getBasicValues(locals);
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}
	
	/**
	 * Description: Delete Case On List (mark as obsolete)
	 * 
	 * @param CaseOnListComplexValue
	 * @throws FinderException
	 */
	public void deleteCaseOnList(CaseOnListComplexValue complexValue, String userDisplayName) 
			throws FinderException, OptimisticLockException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteCaseOnList(complexValue="+complexValue+", userDisplayName="+userDisplayName+")");
       	}
		try {
			caseOnListMaintainer.delete(complexValue, userDisplayName);	
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	/**
	 * Description: Save Case On List
	 * 
	 * @param CaseOnListBasicValue
	 * @throws OptimisticLockException,
	 *             CreateException, FinderException
	 */
	public void saveCaseOnList(CaseOnListBasicValue basicValue, String userDisplayName)
			throws OptimisticLockException, CreateException, FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: saveCaseOnList(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
		try {
			if (basicValue.getVersion().equals(Integer.valueOf(-1))) {
				caseOnListMaintainer.create(basicValue, userDisplayName);        		                
        	} else {
        		caseOnListMaintainer.update(basicValue, userDisplayName);
        	}			
		} catch (OptimisticLockException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
}