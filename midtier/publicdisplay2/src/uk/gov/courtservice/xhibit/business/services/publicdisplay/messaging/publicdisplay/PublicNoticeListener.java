package uk.gov.courtservice.xhibit.business.services.publicdisplay.messaging.publicdisplay;

import java.io.Serializable;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.ObjectNotFoundException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import uk.gov.courtservice.framework.business.services.CSMessageBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title:
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
 * @weblogic.message-driven destination-jndi-name="PublicNoticeNotification"
 * 
 * @author Bob Boothby
 * @version $Id: PublicNoticeListener.java,v 1.8 2014/06/20 17:37:22 atwells Exp $
 */
public class PublicNoticeListener extends CSMessageBean implements MessageDrivenBean, MessageListener {
    private PublicDisplayNotifier _publicDisplayNotifier;

    /**
     * Handles initialisation from a configuration file whose name is defined by
     * the key CONFIG_FILE_KEY in the component properties file
     * PROPERTIES_FILE_NAME. Should this key fail to be present, then the file
     * named in DEFAULT_CONFIG_FILE will be used.
     * 
     * @throws CreateException
     *             when there is a problem in configuration.
     * @see DEFAULT_CONFIG_FILE, CONFIG_FILE_KEY, PROPERTIES_FILE_NAME
     */
    public void ejbCreate() throws CreateException {
        _publicDisplayNotifier = new PublicDisplayNotifier();
    }

    /**
     * Method called when a JMS message is received by this MDB, in this case we
     * are expecting ObjectMessages containing instances of
     * CourtLogSubscriptionValue.
     * 
     * @param msg
     *            an object message containing instances of
     *            CourtLogSubscriptionValue
     * @ejb.interface-method view-type="local"
     */
    public void onMessage(Message msg) {
        log.debug("onMessage() - called");

        // Extract the value object from the message.
        CourtLogSubscriptionValue courtLogEntryEvent = getCourtLogSubscriptionValue(msg);

        // Get the case status event...
        PublicNoticeEvent publicNoticeEvent = processCourtLogEntryEvent(courtLogEntryEvent);

        // Publish the CaseStatusEvent...
        publishPublicNoticeEvent(publicNoticeEvent);
    }

    /**
     * Utility method that gets the expected CourtLogSubscriptionValue from the
     * message.
     * 
     * @param msg
     *            the Message to cast to a CourtLogSubscriptionValue
     * @return a CourtLogSubscriptionValue
     * @throws IllegalArgumentException
     *             for null of wrong type of msg
     * @throws JMSException
     *             for other exception
     */
    private CourtLogSubscriptionValue getCourtLogSubscriptionValue(Message msg) {
        CourtLogSubscriptionValue courtLogSubValue = null;
        if (msg != null && msg instanceof ObjectMessage) {
            try {
                Serializable sObj = ((ObjectMessage) msg).getObject();
                if (sObj instanceof CourtLogSubscriptionValue) {
                    courtLogSubValue = (CourtLogSubscriptionValue) sObj;
                }
            } catch (JMSException e) {
                log.error(e);
                throw new CSUnrecoverableException("Unexpected error", e);
            }
        } else {
            throw new IllegalArgumentException("Message is null");
        }

        return courtLogSubValue;
    }

    private PublicNoticeEvent processCourtLogEntryEvent(CourtLogSubscriptionValue courtLogEntryEvent) {
        XhbCourtSite courtSite = null;
        try {
            courtSite = XhbCourtSiteBeanHelper.findByPrimaryKey(new Integer(courtLogEntryEvent.getCourtSiteId()
                    .intValue()));
        } catch (ObjectNotFoundException ex) {
            throw new CSUnrecoverableException("The court site does not exist...", ex);
        }

        String courtName = getCourtName(courtSite.getCourtId());
		Integer courtRoomNo = getCourtRoomNumber(courtLogEntryEvent.getCourtRoomId());
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtLogEntryEvent.getCourtRoomId());
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        CourtRoomIdentifier courtRoom = new CourtRoomIdentifier(courtSite.getCourtId(), courtLogEntryEvent
                .getCourtRoomId(), courtName, courtRoomNo, publicNotices);
        return new PublicNoticeEvent(courtRoom);
    }

    private void publishPublicNoticeEvent(PublicNoticeEvent publicNoticeEvent) {
        _publicDisplayNotifier.sendMessage(publicNoticeEvent);
        PddaHelper pddaHelper = new PddaHelper();
        pddaHelper.sendMessage(publicNoticeEvent, "Pdda", true); // Skip sending to XHIBIT as its handled above
    }
    
    public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
		return courtName;
	}
	
	
    public Integer getCourtRoomNumber(Integer courtRoomId) {
		Integer courtRoomNo = 0;
		try {
			CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
			courtRoomNo = courtRoomMaintainer.findByPrimaryKey(courtRoomId).getCrestCourtRoomNo();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court room number.");
			e.printStackTrace();
		}
		return courtRoomNo;
	}

    /**
     * Tidy up.
     */
    public void ejbRemove() {
        _publicDisplayNotifier.close();
    }
}
