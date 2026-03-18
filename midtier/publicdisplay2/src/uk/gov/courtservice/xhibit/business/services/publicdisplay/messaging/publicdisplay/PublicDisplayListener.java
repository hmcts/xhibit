package uk.gov.courtservice.xhibit.business.services.publicdisplay.messaging.publicdisplay;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.ObjectNotFoundException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import uk.gov.courtservice.framework.business.services.CSMessageBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ActivateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseCourtRoomEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: Message Driven Bean that takes events from
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
 * @weblogic.message-driven destination-jndi-name="PublicDisplayNotification"
 * 
 * @author Bob Boothby
 * @version $Id: PublicDisplayListener.java,v 1.12 2005/03/05 00:24:18 sz0t7n
 *          Exp $
 */
public class PublicDisplayListener extends CSMessageBean implements MessageDrivenBean, MessageListener {
    private static final Integer PUBLIC_DISPLAY_IS_ACTIVE = new Integer(2);

    private static final Integer PUBLIC_DISPLAY_IS_NOT_ACTIVE = new Integer(4);

    private PublicDisplayNotifier _publicDisplayNotifier;

    private CaseControllerLocal _caseControllerHome;

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
        _caseControllerHome = (CaseControllerLocal) CSServices.getServiceLocator().getLocalHome(
                CaseControllerLocalHome.class);
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
        String methodName = "onMessage() - ";
        log.debug(methodName + "called");
        if (msg != null && msg instanceof ObjectMessage) {
            try {
                Serializable sObj = ((ObjectMessage) msg).getObject();
                if (sObj instanceof CourtLogSubscriptionValue) {
                    PublicDisplayEvent pde = processCourtLogEntryEvent((CourtLogSubscriptionValue) sObj);
                    publishPublicDisplayEvent(pde);
                }
                // else
                // do nothing... handled elsewhere.
            } catch (JMSException e) {
                log.error(e);
                throw new CSUnrecoverableException("Unexpected error", e);
            }
        } else {
            throw new IllegalArgumentException("Message is null");
        }

        // Publish the CaseStatusEvent...
        log.debug(methodName + "published event.");
    }

    private CaseCourtRoomEvent processCourtLogEntryEvent(CourtLogSubscriptionValue courtLogEntryEvent) {
        String methodName = "processCourtLogEntryEvent - ";
        log.debug(methodName + "entered.");

        XhbCourtSite courtSite = XhbCourtRoomBeanHelper2.findByPrimaryKey(
                new Integer(courtLogEntryEvent.getCourtRoomId().intValue())).getXhbCourtSite();
        log.debug(methodName + "got court site.");

        Calendar eventDay = Calendar.getInstance();
        eventDay.setTime(courtLogEntryEvent.getCourtLogViewValue().getEntryDate());
        log.debug(methodName + "got the day.");

        boolean caseActive = isCaseActive(courtLogEntryEvent.getCourtLogViewValue().getCaseId(), eventDay);
        log.debug(methodName + "got case active.");

        String courtName = getCourtName(courtLogEntryEvent.getCourtSiteId());
		Integer courtRoomNo = getCourtRoomNumber(courtLogEntryEvent.getCourtRoomId());
		
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtLogEntryEvent.getCourtRoomId());
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        CourtRoomIdentifier courtRoomId = new CourtRoomIdentifier(courtSite.getCourtId(), courtLogEntryEvent
                .getCourtRoomId(), courtName, courtRoomNo, publicNotices);
        log.debug(methodName + " got court room id.");

        if (courtLogEntryEvent.getPnEventType().equals(PUBLIC_DISPLAY_IS_ACTIVE)
                || courtLogEntryEvent.getPnEventType().equals(PUBLIC_DISPLAY_IS_NOT_ACTIVE)) {
            return new ActivateCaseEvent(courtRoomId, new CaseChangeInformation(caseActive));
        }
        
        CaseCourtLogInformation caseCourtLogEntry = new CaseCourtLogInformation(courtLogEntryEvent, caseActive);
        log.debug(methodName + " created case court log information.");
        
        // Set values for PDDA
    	CourtLogSubscriptionValue clsv = caseCourtLogEntry.getCourtLogSubscriptionValue();
    	if (clsv != null) {
    		CourtLogViewValue clvv = clsv.getCourtLogViewValue();
    		if (clvv != null) {
    			HashMap<String, String> caseData = getCaseData(clvv.getCaseId());
    			String caseType = caseData.get("caseType");
    			Integer caseNumber = 0;
				
				String caseNumberStr = caseData.get("caseNumber");
				if (caseNumberStr != null) {
					try {
						caseNumber = Integer.valueOf(caseNumberStr);
					} catch (NumberFormatException nfe) {
						log.error(methodName + " invalid caseNumber format: " + caseNumberStr);
					}
				}
    			clvv.setCaseType(caseType);
    			clvv.setCaseNumber(caseNumber);
    		}
    	}
        return new CaseStatusEvent(courtRoomId, caseCourtLogEntry);
    }
    
    private HashMap<String, String> getCaseData(Integer caseId) {
    	HashMap<String, String> caseData = new HashMap<String, String>();
    	XhbCase caseEntity = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
    	if (caseEntity != null) {
    		caseData.put("caseType", caseEntity.getCaseType());
    		caseData.put("caseNumber", caseEntity.getCaseNumber()+"");
    	}
    	
    	return caseData;
    }

    private boolean isCaseActive(Integer caseId, Calendar day) {
        String methodName = "isCaseActive - ";
        try {
            boolean returnValue = false;
            log.debug(methodName + "got case controller.");

            ScheduledHearingValue[] scheduledHearing = _caseControllerHome
                    .getScheduledHearingsForCaseOnDay(caseId, day);
            log.debug(methodName + " got " + scheduledHearing.length + " scheduled hearings.");

            for (int i = 0; (i < scheduledHearing.length) && (returnValue == false); i++) {
                log.debug(methodName + "checking value");
                returnValue = scheduledHearing[i].getCaseActive().equals("Y");
            }
            log.debug(methodName + "returning value: " + returnValue);
            return returnValue;
        } catch (CaseControllerException ex) {
            log.error("Something has gone wrong in getting the scheduled hearings.", ex);
            throw new CSUnrecoverableException("Could not get Scheduled hearings.", ex);
        }
    }

    private void publishPublicDisplayEvent(PublicDisplayEvent publicDisplayEvent) {
        _publicDisplayNotifier.sendMessage(publicDisplayEvent);
        PddaHelper pddaHelper = new PddaHelper();
        pddaHelper.sendMessage(publicDisplayEvent, "Pdda", true); // Skip sending to XHIBIT as its handled above
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
