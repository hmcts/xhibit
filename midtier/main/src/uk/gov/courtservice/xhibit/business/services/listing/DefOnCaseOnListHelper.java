package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.DefOnCaseOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.DefOnCaseOnListMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;

/**
 * <p>
 * Title: DefOnCaseOnListHelper
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
public class DefOnCaseOnListHelper extends AbstractHelper {
	private static final Logger LOG = CSServices.getLogger(DefOnCaseOnListHelper.class);
	
	private DefOnCaseOnListMaintainer defOnCaseOnListMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public DefOnCaseOnListHelper() {
		defOnCaseOnListMaintainer = new DefOnCaseOnListMaintainer();
	}

	/**
	 * Description: Find Defendant On Case On List
	 * 
	 * @param defOnCaseOnListId
	 * @return DefOnCaseOnListBasicValue
	 * @throws FinderException 
	 */
	public DefOnCaseOnListBasicValue findByPrimaryKey(Integer defOnCaseOnListId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByPrimaryKey(defOnCaseOnListId="+defOnCaseOnListId+")");
       	}          
		try {
			DefOnCaseOnList local = defOnCaseOnListMaintainer.findByPrimaryKey(defOnCaseOnListId);
			DefOnCaseOnListBasicValue result = defOnCaseOnListMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}
	}

	/**
	 * Description: Find Collection of Defendant On Case On List by caseOnListId
	 * 
	 * @param caseOnListId
	 * @return Collection
	 * @throws FinderException 
	 */
	public Collection findByCaseOnListId(Integer caseOnListId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByCaseOnListId(caseOnListId="+caseOnListId+")");
       	}
		try {
			Collection<DefOnCaseOnList> local = defOnCaseOnListMaintainer.findByCaseOnListId(caseOnListId);
			Collection<DefOnCaseOnListBasicValue> results = defOnCaseOnListMaintainer.getBasicValues(local);
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}	
	}

	/**
	 * Description: Find Collection of Defendant On Case On List by defendantOnCaseId
	 * 
	 * @param defendantOnCaseId
	 * @return Collection
	 * @throws FinderException 
	 */
	public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByDefendantOnCaseId(defendantOnCaseId="+defendantOnCaseId+")");
       	}
		try {
			Collection<DefOnCaseOnList> local = defOnCaseOnListMaintainer.findByDefendantOnCaseId(defendantOnCaseId);
			Collection<DefOnCaseOnListBasicValue> results = defOnCaseOnListMaintainer.getBasicValues(local);
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
    	}	
	}
}