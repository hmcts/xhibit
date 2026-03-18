package uk.gov.courtservice.xhibit.business.services.exiss.inbound;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.JMSServicesException;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;
import uk.gov.courtservice.xhibit.business.services.exiss.outbound.ExiRefTypes;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemInboundVO;
import uk.gov.courtservice.xhibit.business.vos.exiss.inbound.ItemOutboundVO;
import uk.gov.courtservice.xhibit.database.exiss.inbound.InboundDatabase;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the InboundMessageBean
 * </p>
 * <p>
 * Description:
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissInboundQueue"
 * @weblogic.dispatch-policy exiss.inbound.execute.queue
 *
 * @author Steve Tully
 * @version $Id: InboundMessageBean.java,v 1.14 2014/06/20 16:46:04 atwells Exp $
 */
public class InboundMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(InboundMessageBean.class);

    private InboundDatabase database;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new InboundDatabase();
    }

    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param textMessage
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(final TextMessage textMessage) throws Exception {
        log.info("onTextMessage called");

        String body = textMessage.getText();

        try {
            log.debug("about to create an exi_item_inbound record");
            Long inboundItemId = database.createItemInboundRecord(new ItemInboundVO(body));
            log.debug("created an exi_item_inbound record with id: " + inboundItemId.longValue());

            log.debug("about to create exi_item_inbound_properties records");
            Long[] inboundPropertyIds = database.createItemInboundProps(inboundItemId, getPropertiesMap(textMessage));
            if (log.isDebugEnabled()) {
                log.debug("created exi_item_inbound_properties records with the following IDs:");
                log.debug(getIds(inboundPropertyIds));
            }

            // Get the payload type
            String payloadType = textMessage.getStringProperty(InboundMessagePropertyName.PAYLOAD_TYPE.toString());
            log.debug("payloadType is: " + payloadType);

            if (payloadType == null) {
                log.warn("No further processing applicable as a PayloadType was not supplied!");
            } else {
                // If the payload type is a DELIVERERROR, write a record to
                // exi_item_outbound
                if (isDeliverError(payloadType)) {
                    log.debug("about to create an exi_item_outbound record");
                    Long outboundItemId = createItemOutboundRecord(body);
                    log.debug("created new EXI item Outbound record with ID: " + outboundItemId);
                }

                // If the payload type is a EXCEPTION, send a message to the
                // ExisItemTrackingQueue
                if (isException(payloadType)) {
                    String correlationId = textMessage.getStringProperty(InboundMessagePropertyName.CORRELATION_ID.toString());
                    if (correlationId == null) {
                        log.warn("No message sent to ExissItemTrackingQueue as a CorrelationID was not supplied!");
                    }
                    else {
                        sendItemTrackingMessage(new Long(correlationId));
                    }
                }
            }
        } catch (JMSServicesException jmsse) {
            log.error("onTextMessage JMSServicesException " + jmsse, jmsse);
            throw jmsse;
        } catch (DataAccessException dae) {
            log.fatal("onTextMessage DataAccessException " + dae, dae);
            throw dae;
        } catch (Exception e) {
            log.fatal("onTextMessage Exception " + e, e);
            throw e;
        }

        log.info("onTextMessage completed");
    }

    /**
     * Returns a HashMap containing the name/value pairs of all the properties
     * from the JMS message
     *
     * @param textMessage
     * @return HashMap
     * @throws JMSException
     */
    private HashMap getPropertiesMap(TextMessage textMessage) throws JMSException {
        HashMap<String, String> map = new HashMap<String, String>();
        Enumeration<String> propertyNames = textMessage.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String property = propertyNames.nextElement();
            map.put(property, textMessage.getStringProperty(property));
        }

        return map;
    }

    /**
     * Convenience method to build a comma separated list of IDs
     *
     * @param ids
     */
    private String getIds(Long[] ids) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        for (int x = 0; x < ids.length; x++) {
            builder.append((x == 0 ? ids[x] : ", " + ids[x]));
        }
        builder.append(" }");
        return builder.toString();
    }

    /**
     * Requests the creation of an exi_item_outbound_record
     *
     * @param body -
     *            the details that will be used to populate the clob_data column
     * @return Long - the primary key value of the record that was created
     */
    private Long createItemOutboundRecord(String body) {
        return database.createItemOutboundRecord(new ItemOutboundVO(ExiRefTypes.DELIVERERROR.getInternalCode(), null,
                null, null, body, new Date(), null));
    }

    /**
     * Sends a mesage to the ExISS Item Tracking Queue
     *
     * @param correlationId -
     *            the primary key of the exi_item_outbound record to which the
     *            tracking record relates
     */
    private void sendItemTrackingMessage(Long correlationId) {
        log.debug("sendItemTrackingMessage called correlationId = " + correlationId);
        CSServices.getJMSServices().send(
                new ItemTrackingMessageFactory("xhibit/jms/ExissItemTrackingQueue", correlationId.longValue(),
                        ItemTrackingInternalCode.EXCEPTION));
        log.debug("sendItemTrackingMessage completed");
    }

    /**
     * Convenience method to determine whether the payload contains a Deliver
     * Error
     *
     * @param payloadType
     * @return true if the payload is a DeliverError
     */
    protected boolean isDeliverError(String payloadType) {
        return isValueMatched(payloadType, InboundMessagePayloadType.DELIVERERROR.toString());
    }

    /**
     * Convenience method to determine whether the payload contains a business
     * Exception
     *
     * @param payloadType
     * @return true if the payload is a business Exception
     */
    protected boolean isException(String payloadType) {
        return isValueMatched(payloadType, InboundMessagePayloadType.EXCEPTION.toString());
    }

    /**
     * Convenience method to compare the two values and return true if they
     * match
     *
     * @param payloadType
     * @param messageType
     * @return true if the two values match
     */
    private boolean isValueMatched(String payloadType, String messageType) {
        return (payloadType.equalsIgnoreCase(messageType) ? true : false);
    }
}
