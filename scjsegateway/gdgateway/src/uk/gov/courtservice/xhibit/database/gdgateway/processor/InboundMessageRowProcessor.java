package uk.gov.courtservice.xhibit.database.gdgateway.processor;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;

/**
 * <p>
 * Title: InboundMessageRowProcessor
 * </p>
 * <p>
 * Description: Class to process the gdg_inbound_message and gdg_inbound_clobs rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Colette Surtees
 * @version $Id: InboundMessageRowProcessor.java,v 1.2 2006/08/25 12:07:07 gzw0qg Exp $
 */
public class InboundMessageRowProcessor extends AbstractRowProcessor {

    // declare the return VO
    private InboundMessageVO inboundMessage = new InboundMessageVO();

    /**
     * Returns the data
     * 
     * @return InboundMessageVO
     *           the inbound message
     */
    public InboundMessageVO getInboundMessage() {
        return inboundMessage;
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            the result set will only contain one row
     */
    public void processRow(Row row) {
        InboundMessageVO item = new InboundMessageVO();
        item.setInboundMessageId(row.getLong("id"));
        item.setRequestIdentifier(row.getString("request_identifier"));
        item.setSourceIdentifier(row.getString("source_identifier"));
        item.setDestinationIdentifer(row.getString("destination_identifier"));
        item.setExecMode(row.getString("exec_mode"));   
        item.setRequestTimeStamp(row.getDate("request_timestamp"));
        item.setClobData(row.getString("clob_data"));       
        
        inboundMessage = item;
    }
}

