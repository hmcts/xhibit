package uk.gov.courtservice.xhibit.business.messaging.viewschedule;

import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import uk.gov.courtservice.framework.business.services.CSMessageBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.services.viewschedule.TodaysScheduleSimpleCache;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the Public Display Topic
 * and then invalidates the appropriate TodaysSchedule Cache
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
 *           destination-type="javax.jms.Topic"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @weblogic.message-driven destination-jndi-name="PublicDisplay"
 * 
 * @author Neil Ellis
 * @version $Id: ViewScheduleCacheMessageBean.java,v 1.1 2005/01/21 14:53:30
 *          tz0d5m Exp $
 */
public class ViewScheduleCacheMessageBean extends CSMessageBean implements MessageDrivenBean, MessageListener {
    /**
     * We pick out PublicDisplayEvent messages and if appropriate request
     * invalidation of our cached copy of Today's Schedule.
     * 
     * @param msg
     *            an object message containing instances of PublicDisplayEvent
     */
    public void onMessage(Message msg) {
        log.info("Entered onMessage()");
        try {
            if (!(msg instanceof ObjectMessage)) {
                log.info("Message is not an ObjectMessage so exited onMessage()");
                return;
            }

            ObjectMessage objMsg = (ObjectMessage) msg;
            if (log.isDebugEnabled()) {
                log.debug("Object message received: " + objMsg);
            }

            if (!(objMsg.getObject() instanceof PublicDisplayEvent)) {
                log.info("Object is not a PublicDisplayEvent so exited onMessage()");
                return;
            }

            PublicDisplayEvent payload = (PublicDisplayEvent) objMsg.getObject();
            String thisEventType = payload.getEventType().toString();
            
            if (log.isDebugEnabled()) {
                log.debug("Event type is: "+thisEventType);
            }
            
            if (thisEventType.equals(EventType.CASE_STATUS_EVENT)
                    || thisEventType.equals(EventType.PUBLIC_NOTICE_EVENT)) {
                if (log.isDebugEnabled()) {
                    log.debug("Event received was a " + thisEventType + " event, not for me.");
                }
                return;
            }

            if (thisEventType.equals(EventType.CONFIGURATION_EVENT)) {
                if (((ConfigurationChangeEvent) payload).getChange() instanceof CourtDisplayConfigurationChange) {
                    log.debug("Configuration Event received was display update. Only interested in full refreshed.");
                    return;
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Public display event received and accepted: " + payload);
            }
            TodaysScheduleSimpleCache.getCache().invalidate(payload.getCourtId());
            log.info("Exited onMessage()");
        } catch (JMSException ex) {
            log.fatal(ex.getMessage(), ex);
            throw new CSUnrecoverableException(ex);
        }
    }
}