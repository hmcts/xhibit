package uk.gov.courtservice.xhibit.business.services.messagebroker;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.MessageFactory;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.QueueVO;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.RuleVO;
import uk.gov.courtservice.xhibit.database.messagebroker.MessageBrokerDatabase;

/**
 * <p>Title: Message Driven Bean that takes events from the MessageBrokerQueue</p>
 * <p>Description: A Rule may be described as a message Selector and a set of
 * Queues that have registered an interest in messages that match the Selector.
 * This MDB matches the header properties of the message against a Selector for
 * each of the Rules in the data base. When a match is made, the message is
 * forwarded to each Queue defined for the Rule.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: EDS</p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/MessageBrokerQueue"
 * @weblogic.dispatch-policy exiss.message.broker.execute.queue
 *
 * @author Steve Tully, Will, Fardell
 * @version $Id: MessageBrokerMessageBean.java,v 1.2 2006/07/12 07:45:43 szn20z
 *          Exp $
 */
public class MessageBrokerMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(MessageBrokerMessageBean.class);
    /**
     * Instantiates a RulesManager
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }


    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param msg
     *            a message containing key information that can be used by a
     *            Selector
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(TextMessage textMessage) throws Exception {
    	long itemId=0;
        RulesManager rulesManager = new RulesManager(RulesCache.getInstance().getRules());

        RuleVO rule = rulesManager.findMatchingRule(textMessage);
        if( rule != null ) {
            String queueJndiNames = rule.getQueueJndiNames();
            QueueVO[] queues = rule.getQueues();
            MessageFactory[] factories = new MessageFactory[queues.length];
            for (int x = 0; x < queues.length; x++) {
                factories[x] = new MessageBrokerMessageFactory(queues[x].getJndiName(), textMessage, queueJndiNames);
            }
            CSServices.getJMSServices().send(factories);
            log.info("Message matched rule " + rule + " and has been forwared.");
        }
        else {
            log.info("No matches were made so the message was consumed." );
        }
    }
}
