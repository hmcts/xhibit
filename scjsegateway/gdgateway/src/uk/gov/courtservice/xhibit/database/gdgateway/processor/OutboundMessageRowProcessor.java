package uk.gov.courtservice.xhibit.database.gdgateway.processor;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;

/**
 * <p>
 * Title: OutboundMessageRowProcessor
 * </p>
 * <p>
 * Description: Class to process the gdg_outbound_messages and gdg_outbound_clobs rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundMessageRowProcessor.java,v 1.2 2006/09/13 13:33:57 qz4rwx Exp $
 */
public class OutboundMessageRowProcessor extends AbstractRowProcessor {

    private OutboundMessageVO outboundMessage = new OutboundMessageVO();

    /**
     * @return OutboundMessageVO
     */
    public OutboundMessageVO getOutboundMessage() {
        return outboundMessage;
    }

    /**
     * @param row: Note the result set will only contain one row
     */
    public void processRow(Row row) {
        OutboundMessageVO item = new OutboundMessageVO();

        item.setRequestId(new Long(row.getLong("request_id")));
        item.setSourceIdentifier(row.getString("source_identifier"));
        item.setDestinationIdentifier(row.getString("destination_identifier"));
        item.setExecMode(row.getString("exec_mode"));
        item.setRequestTimestamp(row.getTimestamp("request_timestamp"));
        item.setSendAttempts(new Long(row.getLong("send_attempts")));
        item.setOutboundStatusId(new Long(row.getLong("outbound_status_id")));
        item.setClobData(row.getString("clob_data"));

        outboundMessage = item;
    }
}

