package uk.gov.courtservice.xhibit.database.gdgateway;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractGdGateDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.processor.InboundMessageRowProcessor;

import static java.sql.Types.*;

/**
 * <p>
 * Title: Database utility class for Inbound GDGate component.
 * </p>
 * <p>
 * Description: A utility class used to extract all of the database actions
 * performed by the Inbound component in the GDGate aspect. This class is
 * thread-safe as it holds no state.
 * </p>
 * 
 * @author Colette Surtees gzw0qg
 * @version $Id: InboundGdgateDatabase.java,v 1.14 2006/10/20 11:27:10 rzvddy Exp $
 */
public class InboundGdgateDatabase extends AbstractGdGateDatabase {

    /**
     * The stored function for inserting inbound messages.
     */
    private static final String INSERT_INBOUND_MSG = "{ ? = call gdg_scjse_gateway_inbound_pkg.create_inbound_msg_clob(?,?,?,?,?,?) }";

    /**
     * SQL types for INSERT_INBOUND_MSG stored function (uses static imports)
     */
    private static final int[] INSERT_INBOUND_MSG_TYPES = new int[] { VARCHAR, VARCHAR, VARCHAR, VARCHAR, DATE, CLOB };

    /**
     * The stored procedure to get inbound messages
     */
    private static final String GET_INBOUND_MSG = "{ call gdg_scjse_gateway_inbound_pkg.get_inbound_message(?,?) }";

    /**
     * The stored procedure to Increment the Malformed XML count and throw exception if count exceeded.
     */
    private static final String INCREMENT_MALFORMED_XML_COUNT = "{ ? = call gdg_scjse_gateway_inbound_pkg.increment_malformed_xml_count() }";
    
    /**
     * Insert a new inbound message value into the database.
     * 
     * @param value -
     *            The value object containing the information to insert.
     * 
     * @return the inbound message id.
     */
    public Long insertInboundMessages(InboundMessageVO value) {
        log.info("insertInboundMessages(InboundMessageVO) started");

        final StoredFunction sf = createStoredFunction(INSERT_INBOUND_MSG);
        sf.registerInTypes(INSERT_INBOUND_MSG_TYPES);
        Timestamp requestTime = convertToTimestamp(value.getRequestTimeStamp());
        Long id = (Long) sf.executeFunction(new Object[] { value.getRequestIdentifier(), value.getSourceIdentifier(),
                value.getDestinationIdentifer(), value.getExecMode(),
                requestTime, value.getClobData() }, BIGINT);

        if (log.isDebugEnabled()) {
            log.debug(toDebugInsertInboundMessages(id, value.getRequestIdentifier(), value.getSourceIdentifier()));
        }
        log.info("insertInboundMessages(InboundMessageVO) completed");
        return id;
    }

    /**
     * Gets the InboundMessage for the given messageId.
     * 
     * @param messageId
     *            the Id of the message
     * @return the <code>InboundMessageVO</code>
     */
    public InboundMessageVO getMessageByMessageId(Long inboundMessageId) {
        log.info("getMessageByMessageId(" + inboundMessageId + ") started");
        final InboundMessageRowProcessor rp = new InboundMessageRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_INBOUND_MSG);
        sp.registerInTypes(new int[] { BIGINT });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { inboundMessageId });
        log.info("getMessageByMessageId(Long) completed");
        return rp.getInboundMessage();
    }
    /**
     * The stored procedure to increment the value of the record that coresponds to
     * the "MSG_MALFORMED_XML_FAIL_COUNT" record on gdg_config_properties and return 
     * the new value throw an SQL exception with error code -20001 if the count has 
     * been exceeded
     * 
     * @throws SQLException
     */
    public String incrementMalformedXMLCount() throws SQLException
    {
        log.info("incrementMalformedXMLCount() started");
        final StoredFunction sf = createStoredFunction(INCREMENT_MALFORMED_XML_COUNT);            
        final String newValue = (String)sf.executeFunction(new Object[]{}, Types.VARCHAR);
        log.info("incrementMalformedXMLCount() completed");
        return newValue;
    }
    /**
     * Debug for insertInboundMessage
     * 
     * @param inboundMessageId
     *            the database generated key for the inserted message.
     * @param requestId
     *            the inbound message request id.
     * @param sourceId
     *            the inbound message source id.
     * @return the main information for the inserted inbound message as a
     *         String.
     */
    private String toDebugInsertInboundMessages(Long inboundMessageId, String requestId, String sourceId) {
        final StringBuilder builder = new StringBuilder();
        builder.append("InboundMessage inserted: InboundMessageId ");
        builder.append(inboundMessageId);
        builder.append("; request id ");
        builder.append(requestId);
        builder.append("; source id ");
        builder.append(sourceId);
        return builder.toString();
    }
}
