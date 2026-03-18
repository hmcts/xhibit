package uk.gov.courtservice.xhibit.services.scjsegateway.inbound;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;

import uk.gov.courtservice.framework.services.jms.AbstractDeadLetterToFileMDB;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the ScjseInboundDeadLetterQueue
 * </p>
 * <p>
 * Description: Logs to file the message that failed from the ScjseInboundQueue
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
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/ScjseInboundDeadLetterQueue"
 *
 * @author GJS
 * @version $Id: InMessQDLFileMessageBean.java,v 1.1 2006/10/12 17:01:44 qz4rwx Exp $
 */
public class InMessQDLFileMessageBean extends AbstractDeadLetterToFileMDB implements MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    @Override
    public String getTransactionName() {
        return "ScjseInboundQueue";
    }

}
