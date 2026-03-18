package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.Collection;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.xhibit.business.database.query.search.RefOffenceQuery;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffence;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;

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
 * @version $Revision: 1.17 $
 */
public class DisposalHelper extends AbstractHelper {
    private RefOffenceMaintainer refOffenceMaintainer = null;

    /**
     * Default constructor.
     */
    public DisposalHelper() {
    }

    public Collection findOffences(RefOffenceCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findOffences ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());

            // order by offence code
            results = this.findOffencesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefOffence localRef = this.getRefOffenceMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefOffenceBasicValue value = this.getRefOffenceMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /*
     * Private findByQuery methods
     * -------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    private Collection findOffencesByQuery(RefOffenceCriteria criteria) {
        final Collection results = (new RefOffenceQuery()).search(criteria);
        log.debug("findOffencesByQuery() - returning" + results.size() + " results");
        return results;
    }

    /* Maintainer accessors */

    private RefOffenceMaintainer getRefOffenceMaintainer() {

        if (this.refOffenceMaintainer == null) {
            log.debug(": Lazy initialise this.refOffenceMaintainer");
            this.refOffenceMaintainer = new RefOffenceMaintainer();
        }
        return this.refOffenceMaintainer;
    }
}