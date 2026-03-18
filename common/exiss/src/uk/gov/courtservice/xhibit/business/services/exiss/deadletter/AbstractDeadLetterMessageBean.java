package uk.gov.courtservice.xhibit.business.services.exiss.deadletter;

import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;

/**
 * <p>
 * Title: Abstract class for listening to dead letter queues
 * </p>
 * <p>
 * Description: Send a message to the tracker advising of the error
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Will, Fardell
 * @version $Id: AbstractDeadLetterMessageBean.java,v 1.3 2014/06/19 18:38:59 atwells Exp $
 */
public abstract class AbstractDeadLetterMessageBean extends CSTextMessageBean implements MessageDrivenBean,
        MessageListener {

    private static final long serialVersionUID = 1L;

    private final Logger log = CSServices.getLogger(getClass());

    /**
     * Notify tracker there was a problem with the notifier
     *
     * @param msg
     *            a message containing the queues to which an ExISS message has
     *            been sent
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(final TextMessage textMessage) throws Exception {
        
        log.info("AbstractDeadLetterMessageBean executed");
        
        long itemId = getItemId(textMessage);

        ItemTrackingInternalCode code = getInternalCode();

        CSServices.getJMSServices().send(new ItemTrackingMessageFactory("xhibit/jms/ExissItemTrackingQueue", itemId, code));

        log.info("Forwarded code " + code + " to item tracking for id " + itemId + ".");
    }

    /**
     * Implemented by concrete sub classes
     *
     * @return the code to send to the notifier
     */
    public abstract ItemTrackingInternalCode getInternalCode();

    /**
     * Implemented by concrete sub classes to return the item id from the message
     *
     * @return the item id
     */
    public abstract long getItemId(TextMessage textMessage) throws JMSException;

}