package uk.gov.courtservice.xhibit.database.exiss.itemtracking.processor;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.exi_ref_tracking_status.ExiRefTrackingStatusBasicValue;

/**
 * <p>
 * Title: ExiRefStatusRowProcessor
 * </p>
 * <p>
 * Description: Class to process the ExiRefStatus rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rob Sumner
 * @version 1.0
 * 
 */

public class ExiRefTrackingStatusRowProcessor extends AbstractRowProcessor {

    // Instance cache.
    private final Map<String, ExiRefTrackingStatusBasicValue> statusMap = new HashMap<String, ExiRefTrackingStatusBasicValue>();

    /**
     * Get the statuses
     * @return an array containing the statuses
     */
    public Map<String, ExiRefTrackingStatusBasicValue> getStatusMap() {
        return statusMap;
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        ExiRefTrackingStatusBasicValue item = new ExiRefTrackingStatusBasicValue();
        
        // Put the data into the VO
        item.setStatusId(row.getInteger("status_id"));
        item.setInternalCode(row.getString("internal_code"));
        item.setInternalName(row.getString("internal_name"));
        item.setTrackingEnabled(row.getString("tracking_enabled"));

        // Add the VO to the ArrayList
        statusMap.put(item.getInternalCode(), item);
    }

}
