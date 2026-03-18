package uk.gov.courtservice.xhibit.business.database;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;


/**
 * A utility class used to extract all of the database actions performed by the
 * validation 2 sub-project. This class is thread-safe as it holds no
 * state. Not static for testing see getDataSource for more info.
 * 
 * @author fardellwi
 * @version $Id: ValidationDatabase.java,v 1.2 2010/04/19 08:08:52 dunnepi Exp $
 */
public class ValidationDatabase extends AbstractXhibitDatabase {
    
    private static final String UPDATE_CONTROL_STATUS_FUNCTION = "{?= call xhb_validation_pkg.update_validation(?,?,?) }";
    
    /**
     * Update the validation status supplying any details of failuers.
     *  
     * @param validationId the primary key of the row to update
     * @param status the status to update 
     * @param details the details if any
     */    
    public void updateValidation(final long validationId, final String status, final String details) {
        if (log.isDebugEnabled()) {
            log.debug("updateStatus() - listId=" + validationId + "; success=" + status + "; details=" + details);
        }
        
        //truncated to fit in the 4000 length XHB_VALIDATION.Details field. Truncated to 3500 as simple protection against multi-byte character
       String truncatedDetails =  details.substring(0, Math.min(details.length(), 3500));
        
        final StoredFunction func = createStoredFunction(UPDATE_CONTROL_STATUS_FUNCTION);
        func.registerInTypes(new int[] { Types.BIGINT,Types.VARCHAR,Types.VARCHAR});
        Long returnVal = (Long) func.executeFunction(new Object[] { validationId, status, truncatedDetails },Types.BIGINT);
        log.debug("returned "+returnVal);
    }
}
