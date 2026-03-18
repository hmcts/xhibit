package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.search.RefHearingTypeQuery;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;

/**
 * This [BizRef] Helper channels all Hearing related queries.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.9 $
 */
public class HearingHelper extends AbstractHelper {
    private RefHearingTypeMaintainer refHearingTypeMaintainer = null;

    /**
     * Default constructor.
     */
    public HearingHelper() {
    }

    /**
     * If the criteria contains a PrimaryKey, find Hearing Type reference data
     * using that, otherwise create and execute a query.
     * 
     * @param RefHearingTypeCriteria
     *            criteria
     * @return Collection of RefHearingTypeBasicValue
     * @throws BisRefControllerException
     */
    public Collection findHearingTypes(RefHearingTypeCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findHearingTypes ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findHearingTypesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefHearingType localRef = this.getRefHearingTypeMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefHearingTypeBasicValue value = this.getRefHearingTypeMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    }    
        
	/**
	 * Description: Find hearing type by primary key
	 * 
	 * @param refHearingTypeId
	 * @return RefHearingTypeBasicValue
	 * @throws BisRefControllerException 
	 */
	public RefHearingTypeBasicValue findHearingTypeById(Integer refHearingTypeId) throws BisRefControllerException {
		try {
			RefHearingType local = getRefHearingTypeMaintainer().findByPrimaryKey(refHearingTypeId);
			RefHearingTypeBasicValue result = getRefHearingTypeMaintainer().getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new BisRefControllerException("ERR_NO", "Object with key [" + refHearingTypeId + "] not found",ex);
    	}
	}
	
	/**
	 * Description: Find hearing type by courtId and category
	 * 
	 * @param courtId
	 * @param category
	 * @return Collection<RefHearingTypeBasicValue>
	 * @throws BisRefControllerException   
	 */
	public Collection findHearingTypesByCourtIdAndCategory(Integer courtId, String category) throws BisRefControllerException {
		try {
			Collection locals = getRefHearingTypeMaintainer().findByCourtIdAndCategory(courtId, category);
			Collection results = getRefHearingTypeMaintainer().getBasicValues(locals);
			return results;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw new BisRefControllerException("ERR_NO", "Object with key [" + courtId + "] not found",ex);
    	}
	}
	
    private Collection findHearingTypesByQuery(RefHearingTypeCriteria criteria) {
        final Collection results = (new RefHearingTypeQuery()).search(criteria);
        log.debug("findHearingTypesByQuery() - returning" + results.size() + " results");
        return results;
    }

    /* Maintainer accessors */

    private RefHearingTypeMaintainer getRefHearingTypeMaintainer() {

        if (this.refHearingTypeMaintainer == null) {
            log.debug(": Lazy initialise this.RefHearingTypeMaintainer");
            this.refHearingTypeMaintainer = new RefHearingTypeMaintainer();
        }
        return this.refHearingTypeMaintainer;
    }
}