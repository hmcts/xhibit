package uk.gov.courtservice.xhibit.database.messagegenerator.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.messagegenerator.MessageVO;

/**
 * <p>
 * Title: SelectorRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Selector rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class MessageRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<MessageVO> messageList = new ArrayList<MessageVO>();

    /**
     * Returns the data
     * 
     * @return Collection
     */
    public MessageVO[] getMessages() {
        return (MessageVO[]) messageList.toArray(new MessageVO[messageList.size()]);
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        messageList.add(new MessageVO(row.getLong("ITEM_ID"), row.getString("ITEM_TYPE"), row.getString("TARGET")));
    }

}
