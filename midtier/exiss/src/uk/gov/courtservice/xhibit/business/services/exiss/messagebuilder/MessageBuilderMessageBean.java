package uk.gov.courtservice.xhibit.business.services.exiss.messagebuilder;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.DartsConfiguration;
import uk.gov.courtservice.xhibit.business.services.darts.DartsMessageFactory;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;
import uk.gov.courtservice.xhibit.business.services.exiss.messagebroker.ExissMessageBrokerMessageFactory;
import uk.gov.courtservice.xhibit.business.services.messagebroker.MessageBrokerMessageFactory;
import uk.gov.courtservice.xhibit.business.services.messagebroker.MessageBrokerMessagePropertyName;
import uk.gov.courtservice.xhibit.database.exiss.messagebuilder.MessageBuilderDatabase;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the
 * ExissMessageBuilderQueue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissMessageBuilderQueue"
 * @weblogic.dispatch-policy message.builder.execute.queue
 *
 * @author Steve Tully, Will, Fardell
 * @version $Id: MessageBuilderMessageBean.java,v 1.3 2006/07/12 15:52:35 bzjrnl
 *          Exp $
 */
public class MessageBuilderMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(MessageBuilderMessageBean.class);

    private MessageBuilderDatabase database;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new MessageBuilderDatabase();
    }

    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param msg
     *            an object message containing instances of
     *            CourtLogSubscriptionValue
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(TextMessage message) throws Exception {
        long itemId = message.getLongProperty(MessageBrokerMessagePropertyName.ITEM_ID.toString());

        // Retrieve Data
        String clobData = database.getClobData(itemId);
        Map<String, String> propertyMap = database.getProperties(itemId);
        // Hack to add XHBMessageIdentifier, this should be done in SP. WDF
        // 14/07/2006
        propertyMap.put("XHBMessageIdentifier", String.valueOf(itemId));
        if (log.isDebugEnabled()) {
            log.debug("Added property - XHBMessageIdentifier: " + String.valueOf(itemId));
        }

        // Retrieve Data
        CSServices.getJMSServices().send(
                new ExissMessageBrokerMessageFactory("xhibit/jms/ExissMessageBrokerQueue", propertyMap, clobData),
                new ItemTrackingMessageFactory("xhibit/jms/ExissItemTrackingQueue", itemId,
                        ItemTrackingInternalCode.SENT_TO_WS_BRIDGE));

        log.info("Built exiss message for id " + itemId + " containing " + propertyMap.size() + " properties and "
                + clobData.length() + " characters of text.");
        
        //Get DARTS config and status        
        DartsConfiguration dartsConfig = DartsConfiguration.getInstance();
        if (dartsConfig.isDartsActive()) {
            String xhbItemType = propertyMap.get("XHBExissMessageType");
            for (String doctype : dartsConfig.getDoctypesArray()) {
                if (doctype.equals(xhbItemType)) {
                    
                    Map<String, String> dartsPropertyMap = new HashMap<String, String>();
                    dartsPropertyMap.put("xhibitMessageCode", xhbItemType );
                    dartsPropertyMap.put("exissMessageCode", xhbItemType );
                    
                    if (log.isDebugEnabled()) {
                        log.debug("Sending msg to Darts with type: " + xhbItemType);
                        log.debug("Sending Doc msg to Darts DartsMessageOutboundQueue: " + doctype);
                    }
                    CSServices.getJMSServices().send(
                            new DartsMessageFactory("jms/darts/DartsMessageOutboundQueue", dartsPropertyMap, clobData) 
                            );
                    break;
                }
            }
        }
    }
}
