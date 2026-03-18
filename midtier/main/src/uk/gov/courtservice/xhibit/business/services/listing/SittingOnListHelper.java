package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.SittingOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.SittingOnListMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;

/**
 * <p>
 * Title: SittingOnListHelper
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
public class SittingOnListHelper extends AbstractHelper {
	private SittingOnListMaintainer sittingOnListMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public SittingOnListHelper() {
		sittingOnListMaintainer = new SittingOnListMaintainer();
	}

	/**
	 * Description: Find Sitting On List
	 * 
	 * @param sittingOnListId
	 * @return SittingOnListBasicValue
	 * @throws FinderException 
	 */
	public SittingOnListBasicValue findByPrimaryKey(Integer sittingOnListId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByPrimaryKey(sittingOnListId="+sittingOnListId+")");
       	}
		try {
			SittingOnList local = sittingOnListMaintainer.findByPrimaryKey(sittingOnListId);
			SittingOnListBasicValue result = sittingOnListMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of Sitting On List by listId
	 * 
	 * @param listId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection findByListId(Integer listId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByListId(listId="+listId+")");
       	}
		try {
			Collection<SittingOnList> locals = sittingOnListMaintainer.findByListId(listId);
			Collection<SittingOnListBasicValue> results = sittingOnListMaintainer.getBasicValues(locals);
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Save Sitting On List
	 * 
	 * @param SittingOnListBasicValue
	 * @throws OptimisticLockException,
	 *             CreateException, FinderException
	 */
	public void saveSittingOnList(SittingOnListBasicValue basicValue, String userDisplayName)
			throws OptimisticLockException, CreateException, FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: saveSittingOnList(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
		try {
			if (basicValue.getVersion().equals(Integer.valueOf(-1))) {
				sittingOnListMaintainer.create(basicValue, userDisplayName);        		                
        	} else {
        		sittingOnListMaintainer.update(basicValue, userDisplayName);
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