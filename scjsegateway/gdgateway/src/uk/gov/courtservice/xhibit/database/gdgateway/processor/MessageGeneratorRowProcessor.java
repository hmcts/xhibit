package uk.gov.courtservice.xhibit.database.gdgateway.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.EventMessageVO;

/**
 * <p>
 * Title: MessageGeneratorRowProcessor
 * </p>
 * <p>
 * Description: Class to process the gdg_jms_messages.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author 
 * @version $Id: MessageGeneratorRowProcessor.java,v 1.3 2006/10/17 12:02:56 szfnvt Exp $
 */
public class MessageGeneratorRowProcessor extends AbstractRowProcessor {

    private final List<EventMessageVO> messages = new ArrayList<EventMessageVO>();
    
    public EventMessageVO[] getMessages() {
        return messages.toArray(new EventMessageVO[messages.size()]);
    }
    @Override
    public void processRow(Row row) {
        EventMessageVO event = new EventMessageVO();
        
        event.setExpiryTime(row.getDate("expiry_time"));
        event.setMessageId(new Long(row.getLong("message_id")));
        event.setMessageType(row.getString("message_type"));
        
        messages.add(event);
    }
}
