package uk.gov.courtservice.xhibit.database.exiss.itemtracking;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractExissDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.xhibit.business.entities.exi_ref_tracking_status.ExiRefTrackingStatusBasicValue;
import uk.gov.courtservice.xhibit.business.vos.exiss.itemtracking.ItemOutboundTrackingVO;
import uk.gov.courtservice.xhibit.database.exiss.itemtracking.processor.ExiRefTrackingStatusRowProcessor;

/**
 * <p>
 * Title: Database utility class for ExISS ItemTracking component.
 * </p>
 * <p>
 * Description: A utility class used to extract all of the database actions
 * performed by the ItemTracking component in the Flow to ExISS aspect of
 * RFC1492. This class is thread-safe as it holds no state.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * 
 * @author Rob Sumner
 */

public class ItemTrackingDatabase extends AbstractExissDatabase {
    /**
     * Used for creating Debug text
     */
    private static final String NL = System.getProperty("line.separator", "\n");

    private static final String TAB = "    ";

    private static final String GET_EXI_REF_STATUS = "{ call exi_flow_to_exiss_pkg.get_tracking_statuses(?) }";

    private static final String INSERT_ITEM_OUTBOUND_TRACKING = "{ ? = call exi_flow_to_exiss_pkg.insert_item_outbound_tracking(?,?,?) }";

    /**
     * Get the list of ExISS Tracking Status Codes.
     * 
     * @return A collection containing <code>ExiRefStatusVO</code> objects
     *         corresponding to all the entries in the EXI_REF_STATUS Table in
     *         the EXI database.
     */

    public Map<String, ExiRefTrackingStatusBasicValue> getStatusMap() {
        final ExiRefTrackingStatusRowProcessor rp = new ExiRefTrackingStatusRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_EXI_REF_STATUS);
        sp.registerInTypes(new int[] {});
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {});

        Map<String, ExiRefTrackingStatusBasicValue> statusMap = rp.getStatusMap();

        if (log.isDebugEnabled()) {
            log.debug(toDebug(statusMap));
        } else {
            log.info("Loaded " + statusMap.size() + " statuses.");
        }

        return statusMap;
    }

    /**
     * Insert a new tracking value into the database.
     * 
     * @param value -
     *            The value object containing the information to insert.
     */
    public Long insertItemOutboundTracking(ItemOutboundTrackingVO value) throws DataAccessException {
        final StoredFunction sf = createStoredFunction(INSERT_ITEM_OUTBOUND_TRACKING);
        sf.registerInTypes(new int[] { Types.BIGINT, Types.VARCHAR, Types.DATE });
        Long trackingId = (Long) sf.executeFunction(new Object[] { value.getItemId(),
                value.getRefTrackingStatusInternalCode(), new Timestamp(value.getTrackingDate().getTime()) },
                Types.BIGINT);
        log.info("Added new status <" + trackingId + "/" + value.getRefTrackingStatusInternalCode() + "> for item "
                + value.getItemId() + ".");
        return trackingId;
    }

    private static String toDebug(Map<String, ExiRefTrackingStatusBasicValue> statusMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loaded ");
        builder.append(statusMap.size());
        builder.append(" statuses:");
        for (String key : statusMap.keySet()) {
            builder.append(NL);
            builder.append(TAB);
            builder.append(key);
            builder.append(": ExiRefTrackingStatusBasicValue[");
            builder.append(statusMap.get(key));
            builder.append("]");
        }
        return builder.toString();
    }
}
