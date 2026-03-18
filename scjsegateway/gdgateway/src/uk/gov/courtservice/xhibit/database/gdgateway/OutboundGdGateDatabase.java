package uk.gov.courtservice.xhibit.database.gdgateway;

import static java.sql.Types.*;

import java.sql.Timestamp;

import uk.gov.cjse.schemas.endpoint.types.ExecMode;
import uk.gov.courtservice.framework.jdbc.core.AbstractGdGateDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.processor.OutboundMessageRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundUpdateStatusVO;

/**
 * <p>
 * Title: Database utility class for Outbound GDGate components.
 * </p>
 * <p>
 * Description: A utility class used to extract all of the database actions
 * performed by the for the Outbound in the GDGate domain.
 * This class is thread-safe as it holds no state.
 * Note - the reads for config data and org unit Id are expected to be in a common
 * database class and the message generator database access routines will also have
 * their own class
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundGdGateDatabase.java,v 1.7 2006/10/20 11:27:10 rzvddy Exp $
 */
public class OutboundGdGateDatabase extends AbstractGdGateDatabase {

    /**
     * The Request_Id and CLOB are the two imports, returning the Request_Id
     */
    private static final String INSERT_OUTBOUND_MESSAGE = "{? = call GDG_SCJSE_GATEWAY_OUTBOUND_PKG.create_gdg_outbound_clob_msg(?,?,?,?,?,?,?)}";

    /**
     * SQL types for INSERT_OUTBOUND_MESSAGE stored function (uses static imports)
     */
    private static final int[] INSERT_OUTBOUND_MSG_TYPES = new int[] { BIGINT, VARCHAR, VARCHAR, VARCHAR, DATE,
            INTEGER, CLOB };

    /**
     * The Request_Id is the import, returning the Outbound_Message attributes
     */
    private static final String GET_OUTBOUND_MESSAGE = "{call GDG_SCJSE_GATEWAY_OUTBOUND_PKG.read_outbound_message_and_clob(?,?)}";

    /**
     * SQL types for GET_OUTBOUND_MESSAGE stored function (uses static imports)
     */
    private static final int[] GET_OUTBOUND_MESSAGE_TYPE = new int[] { BIGINT };

    /** 
     * The Request_Id, Outbound_Status_Id, Failure_Code and Failure_Text are the imports,
     * returning the Request_Id
     */
    private static final String UPDATE_OUTBOUND_MESSAGE_STATUS = "{? = call GDG_SCJSE_GATEWAY_OUTBOUND_PKG.update_gdg_outbound_message(?,?,?,?)}";

    /**
     * SQL types for UPDATE_OUTBOUND_MESSAGE_STATUS stored function (uses static imports)
     */
    private static final int[] UPDATE_OUTBOUND_MSG_STATUS_TYPES = new int[] { BIGINT, BIGINT, VARCHAR, VARCHAR };

    /**
     * Insert a new Outbound Message and CLOB into the GD Gate database.
     *
     * @param value - the OutboundMessageVO
     *
     * @return the RequestId.
     */
    public Long insertOutboundMessage(OutboundMessageVO value) {
        if (log.isDebugEnabled()) {
            log.debug("About to insert OutboundMessage:" + value.toString());
        }

        final StoredFunction sf = createStoredFunction(INSERT_OUTBOUND_MESSAGE);

        sf.registerInTypes(INSERT_OUTBOUND_MSG_TYPES);

        Timestamp requestTime = convertToTimestamp(value.getRequestTimestamp());
        Integer sendAttempts = value.getSendAttempts() == null ? null : value.getSendAttempts().intValue();
        String execMode = value.getExecMode() == null ? ExecMode._asynch : value.getExecMode();

        Long requestId = (Long) sf.executeFunction(new Object[] { value.getRequestId(), value.getSourceIdentifier(),
                value.getDestinationIdentifier(), execMode, requestTime, sendAttempts, value.getClobData() }, BIGINT);

        if (log.isDebugEnabled()) {
            log.debug("OutboundMessage inserted with requestId:" + requestId);
        }

        return requestId;
    }

    /**
     * Updates the status on an Outbound Message and optionally creates
     * an associated Outbound Failure on the GD Gate database.
     *
     * @param value - the OutboundUpdateStatusVO
     *
     * @return the RequestId.
     */
    public Long updateOutboundMessageStatus(OutboundUpdateStatusVO value) {
        if (log.isDebugEnabled()) {
            log.debug("About to update OutboundMessage:" + value.toString());
        }

        final StoredFunction sf = createStoredFunction(UPDATE_OUTBOUND_MESSAGE_STATUS);

        sf.registerInTypes(UPDATE_OUTBOUND_MSG_STATUS_TYPES);

        Long requestId = (Long) sf.executeFunction(new Object[] { value.getRequestId(), value.getOutboundStatusCode(),
                value.getFailureCode(), value.getFailureText() }, BIGINT);

        if (log.isDebugEnabled()) {
            log.debug("OutboundMessage updated with requestId:" + requestId);
        }

        return requestId;
    }

    /**
     * Gets the OutboundMessage for the given requestId.
     *
     * @param requestId
     * @return the <code>OutboundMessageVO</code>
     */
    public OutboundMessageVO getMessageByRequestId(Long requestId) {
        if (log.isDebugEnabled()) {
            log.debug("About to get OutboundMessage,requestId:" + requestId);
        }

        final OutboundMessageRowProcessor rp = new OutboundMessageRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_OUTBOUND_MESSAGE);

        sp.registerInTypes(GET_OUTBOUND_MESSAGE_TYPE);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { requestId });

        if (log.isDebugEnabled()) {
            log.debug("OutboundMessage retrieved:" + rp.getOutboundMessage().toString());
        }

        return rp.getOutboundMessage();
    }
}
