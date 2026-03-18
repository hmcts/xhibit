package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Factory class for create helpers
 * 
 * @author pznwc5
 * @version $Revision: 1.3 $
 */
public class UpdateHelperFactory {
    private static final Logger LOG = CSServices.getLogger(UpdateHelperFactory.class);

    /**
     * Don't instantiate me
     */
    private UpdateHelperFactory() {
        // prevent external instantiation...
    }

    /**
     * Creates an instance of a CRUD value creator based on the CRUD value type
     * 
     * @param crudVal
     *            CRUD value
     * @return
     */
    public static UpdateHelper getUpdateHelper(OperationContext context) {
        CourtLogCRUDValue crudVal = context.getCrudValue();
        

        if (crudVal.processLinkedCases()) {
            LOG.debug("Creating linked case create helper");
            return new LinkedCaseUpdateHelper(context);
        }

        LOG.debug("Creating basic case create helper");
        //return new LinkedCaseUpdateHelper(context);
        return new UpdateHelper(context);
    }
}
