package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.Collection;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.search.RefAppResultQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefSystemCodeQuery;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResult;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResultMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAppResultCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;

/**
 * This [BizRef] Helper channels all System Code related queries.
 * <p>
 * System code examples are Offences, Pleas, Home Office Proc Codes etc.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.14 $
 */
public class SystemCodeHelper extends AbstractHelper {
    private RefSystemCodeMaintainer systemCodeMaintainer = null;

    private RefAppResultMaintainer appResultMaintainer = null;

    /**
     * Default constructor.
     */
    public SystemCodeHelper() {
        // Empty
    }

    public Collection findSystemCodes(RefSystemCodeCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findSystemCodes ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection<CSAbstractValue> values = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("find by criteria " + criteria.toString());
            values = this.findSystemCodesByQuery(criteria);
        } else {
            log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefSystemCode localRef = this.getSystemCodeMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefSystemCodeBasicValue value = this.getSystemCodeMaintainer().getBasicValue(localRef);
                values = this.newCollection();
                values.add(value);
            } catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return values;
    }

    public Collection findAppResults(RefAppResultCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findAppResults ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection<CSAbstractValue> values = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("find by criteria " + criteria.toString());
            values = this.findAppResultsByQuery(criteria);
        } else {
            log.debug("find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefAppResult localRef = this.getAppResultMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefAppResultBasicValue value = this.getAppResultMaintainer().getBasicValue(localRef);
                values = this.newCollection();
                values.add(value);
            } catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return values;
    }

    /*
     * findByQuery methods
     * ------------------------------------------------------------------------------------------------
     */

    /**
     * Find System Codes matching the given the criteria.
     * <p>
     * This ignores the primary key criterion.
     * </p>
     * 
     * @param criteria
     *            RefSystemCodeCriteria
     * @return java.util.Collection
     */
    private Collection<CSAbstractValue> findSystemCodesByQuery(RefSystemCodeCriteria criteria) {
        final Collection<CSAbstractValue> results = (new RefSystemCodeQuery()).search(criteria);
        log.debug("findSystemCodesByQuery() - returning" + results.size() + " results");
        return results;
    }

    /**
     * Find Ref App Results matching the given the criteria.
     * <p>
     * This ignores the primary key criterion.
     * </p>
     * 
     * @param criteria
     *            RefAppResultCriteria
     * @return java.util.Collection
     */
    private Collection findAppResultsByQuery(RefAppResultCriteria criteria) {
        final Collection results = (new RefAppResultQuery()).search(criteria);
        log.debug("findAppResultsByQuery() - returning" + results.size() + " results");
        return results;
    }

    /*
     * Private helper methods
     * ---------------------------------------------------------------------------------------------
     */
    private RefSystemCodeMaintainer getSystemCodeMaintainer() {
        if (this.systemCodeMaintainer == null) {
            this.systemCodeMaintainer = new RefSystemCodeMaintainer();
        }
        return this.systemCodeMaintainer;
    }

    private RefAppResultMaintainer getAppResultMaintainer() {
        if (this.appResultMaintainer == null) {
            this.appResultMaintainer = new RefAppResultMaintainer();
        }
        return this.appResultMaintainer;
    }
}