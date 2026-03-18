package uk.gov.courtservice.xhibit.services.scjsegateway.common;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;

import uk.gov.courtservice.framework.services.jms.AbstractDeadLetterToFileMDB;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the DeadLetterQueue
 * </p>
 * <p>
 * Description: Logs to file the message that failed from the all the other Dead Letter
 * Queues and the ScjseItemTrackingQueue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @ejb.security-identity run-as="XHBInternal"
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/DeadLetterQueue"
 *
 * @author GJS
 * @version $Id: DeadLetterFileMessageBean.java,v 1.1 2006/10/12 17:01:00 qz4rwx Exp $
 */
public class DeadLetterFileMessageBean extends AbstractDeadLetterToFileMDB implements MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    @Override
    public String getTransactionName() {
        return "DeadLetterQueue";
    }

}
