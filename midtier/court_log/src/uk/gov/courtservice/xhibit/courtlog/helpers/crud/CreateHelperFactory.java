package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;

/**
 * Factory class for create helpers
 * 
 * @author pznwc5
 * @version $Revision: 1.7 $
 */
public class CreateHelperFactory {
    private static final Logger LOG = CSServices.getLogger(CreateHelperFactory.class);

    /**
     * Don't instantiate me
     */
    private CreateHelperFactory() {
        // prevent external instantiation...
    }

    /**
     * Creates an instance of a CRUD value creator based on the CRUD value type
     * 
     * @param crudVal
     *            CRUD value
     * @return
     */
    public static CreateHelper getCreateHelper(OperationContext context) {
        CourtLogCRUDValue crudVal = context.getCrudValue();
        if (crudVal instanceof MultiCaseCourtLogCRUDValue) {
            LOG.debug("Creating multi case create helper");
            return new MultiCaseCreateHelper(context);
        }

        if (crudVal.processLinkedCases()) {
            LOG.debug("Creating linked case create helper");
            return new LinkedCaseCreateHelper(context);
        }

        LOG.debug("Creating basic case create helper");
        return new CreateHelper(context);
    }
}
