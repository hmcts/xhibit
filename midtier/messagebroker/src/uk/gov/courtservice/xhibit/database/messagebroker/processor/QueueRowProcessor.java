package uk.gov.courtservice.xhibit.database.messagebroker.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.QueueVO;

/**
 * <p>
 * Title: QueueRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Queue rows.
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
public class QueueRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<QueueVO> queueList = new ArrayList<QueueVO>();

    /**
     * Returns the data
     * 
     * @return Collection
     */
    public QueueVO[] getQueues() {
        return (QueueVO[]) queueList.toArray(new QueueVO[queueList.size()]);
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        QueueVO queue = new QueueVO();

        // Populate the vo
        queue.setId(row.getInteger("queue_id"));
        queue.setJndiName(row.getString("jndi_name"));
        queue.setDescription(row.getString("description"));

        // Add the VO to the ArrayList
        queueList.add(queue);
    }

}
