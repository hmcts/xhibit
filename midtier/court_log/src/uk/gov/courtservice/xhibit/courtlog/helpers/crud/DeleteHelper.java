package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * @author pznwc5 Helper for deleting court log entries
 */
public class DeleteHelper extends CrudHelper {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(CreateHelper.class);

    private DeleteHelper() {
        // made private to prevent external instantiation...
    }

    /**
     * Made consistent with other methods, and made return the CourtLogViewValue
     * 
     * @param logEntryId
     *            Log entry Id
     * @return CourtLogViewValue View value
     * @throws CourtLogException
     */
    public static CourtLogViewValue deleteEntry(OperationContext context) {
        XhbCourtLogEntryBasicValue basicVal = context.getOriginalBasicValue();
        LOG.debug("deleteEntry() - called :: logEntryId: " + basicVal);

        XhbCourtLogEntryBeanHelper2.remove(context.getOriginalBasicValue());
        
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(XhbCourtLogEntry.class.getName(), context.getOriginalBasicValue());
        AuditTrailService auditService = CSServices.getAuditTrailService();
        AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
        event.setSuccess(true);
        auditService.createAuditRecord(event);
        
        return context.getOriginalViewValue();
    }
}
