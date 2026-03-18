package uk.gov.courtservice.xhibit.business.services.exiss.itemtracking;

import java.sql.Date;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.exiss.itemtracking.ItemOutboundTrackingVO;
import uk.gov.courtservice.xhibit.database.exiss.itemtracking.ItemTrackingDatabase;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the ExissItemTrackingQueue
 * </p>
 * <p>
 * Description: MDB takes messages from the ExissItemTrackingQueue and writes
 * the tracking information into ItemTrackingDatabase.
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissItemTrackingQueue"
 * @weblogic.dispatch-policy item.tracking.execute.queue
 *
 * @author Rob Sumner, Will Fardell
 * @version $Id: ItemTrackingMessageBean.java,v 1.4 2006/07/13 15:36:54 jzj6wd
 *          Exp $
 */
public class ItemTrackingMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(ItemTrackingMessageBean.class);

    private static final int TRACKING_DISABLED = 20401;

    private ItemTrackingDatabase database;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new ItemTrackingDatabase();
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
        ItemOutboundTrackingVO value = new ItemOutboundTrackingVO();

        long itemId = message.getLongProperty(ItemTrackingMessagePropertyName.ITEM_ID.toString());
        value.setItemId(new Long(itemId));
        value.setRefTrackingStatusInternalCode(message.getStringProperty(ItemTrackingMessagePropertyName.INTERNAL_CODE
                .toString()));
        value.setTrackingDate(new Date(message.getJMSTimestamp()));

        try {
            database.insertItemOutboundTracking(value);
        } catch (DataAccessException dae) {
            handleDataAccessException(dae, value);
        } catch (Throwable t) {
            log.fatal("[insertItemOutboundTracking] catch Throwable " + t.getClass());
        }
    }

    private void handleDataAccessException(DataAccessException dae, ItemOutboundTrackingVO value) {
        log.info("catch DataAccessException " + dae.getClass());
        Throwable t = dae.getCause();
        if ((t instanceof SQLException) && (((SQLException) t).getErrorCode() == TRACKING_DISABLED)) {
            log.warn("Tracking disabled for status <"
                    + value.getRefTrackingStatusInternalCode() + ">");
        } else {
            log.fatal("catch DataAccessException about to rethrow");
            log.fatal("catch DataAccessException column :" + dae.getColumn());
            log.fatal("catch DataAccessException errorID:" + dae.getErrorID());
            log.fatal("catch DataAccessException message:" + dae.getMessage());
            log.fatal("catch DataAccessException cause  :" + dae.getCause());
            if (t instanceof SQLException) {
                log.fatal("SQLException");
                SQLException sqle = (SQLException) t;
                log.fatal("SQLException errorCode:" + sqle.getErrorCode());
                log.fatal("SQLException sqlState :" + sqle.getSQLState());
                log.fatal("SQLException message  :" + sqle.getMessage());
                log.fatal("SQLException cause    :" + sqle.getCause());
            }

            throw dae;
        }
    }
}
