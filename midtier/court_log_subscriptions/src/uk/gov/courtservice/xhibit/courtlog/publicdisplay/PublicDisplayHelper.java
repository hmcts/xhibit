package uk.gov.courtservice.xhibit.courtlog.publicdisplay;

import java.util.HashMap;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseCourtRoomEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * @author pznwc5
 * 
 * Helper class for sending public display messages
 */
public class PublicDisplayHelper {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(PublicDisplayHelper.class);

    // private static final Integer PUBLIC_DISPLAY_IS_ACTIVE = new
    // Integer(2);

    // private static final Integer PUBLIC_DISPLAY_IS_NOT_ACTIVE = new
    // Integer(4);

    private static PublicDisplayNotifier _publicDisplayNotifier = new PublicDisplayNotifier();

    /**
     * Sends a JMS message to public display
     * 
     * @param subVal
     */
    public static void sendMessage(Integer courtRoomId) {
        sendMessage(null, courtRoomId);
    }

    /**
     * Sends a JMS message to public display
     * 
     * @param subVal
     */
    public static void sendMessage(CourtLogSubscriptionValue subVal) {
        sendMessage(subVal, subVal.getCourtRoomId());
    }

    /**
     * Sends a JMS message to public display
     * 
     * @param subVal
     */
    private static void sendMessage(CourtLogSubscriptionValue subVal, Integer courtRoomId) {
    	String methodName = "sendMessage";
    	
        XhbCourtSite courtSite = XhbCourtRoomBeanHelper2.findByPrimaryKey(courtRoomId).getXhbCourtSite();
        LOG.info(methodName + "; Court site retrieved: " + courtSite.getCourtSiteName());

        // Date entryDate = DateTimeUtilities.stripTime(
        // subVal.getCourtLogViewValue().getEntryDate());
        // at this moment in time the case will always be active, as it is
        // already in xhb_cr_live_Status...
        boolean caseActive = true;
        // isCaseActive(subVal.getCourtLogViewValue().getCaseId(), entryDate);
        LOG.info(methodName + "; Case active: " + caseActive);

        PublicDisplayHelper pdh = new PublicDisplayHelper();
        String courtName = pdh.getCourtName(courtSite.getCourtId());
		Integer courtRoomNo = pdh.getCourtRoomNumber(courtRoomId);
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtRoomId);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			LOG.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        CourtRoomIdentifier courtRoomIdentifier =
        		new CourtRoomIdentifier(courtSite.getCourtId(), courtRoomId, courtName, courtRoomNo, publicNotices);
        CaseCourtRoomEvent event = null;
        // @todo Need to check this with Rakesh. I am not sure whether this is
        // valid anymore - Meeraj
        /*
         * if (subVal.getPnEventType().equals(PUBLIC_DISPLAY_IS_ACTIVE) ||
         * subVal.getPnEventType().equals(PUBLIC_DISPLAY_IS_NOT_ACTIVE)) {
         * LOG.debug("Event type is case activation"); event = new
         * ActivateCaseEvent(courtRoomId, new
         * CaseChangeInformation(caseActive)); } else {
         */
        LOG.info(methodName + "; Event type is case status");
        CaseCourtLogInformation caseCourtLogEntry = new CaseCourtLogInformation(subVal, caseActive);
        
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
						LOG.error(methodName + " invalid caseNumber format: " + caseNumberStr);
					}
				}
    			clvv.setCaseType(caseType);
    			clvv.setCaseNumber(caseNumber);
    		}
    	}
        
        event = new CaseStatusEvent(courtRoomIdentifier, caseCourtLogEntry);
        // }
        publishPublicDisplayEvent(event);
    }
    
    private static HashMap<String, String> getCaseData(Integer caseId) {
    	HashMap<String, String> caseData = new HashMap<String, String>();
    	XhbCase caseEntity = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
    	if (caseEntity != null) {
    		caseData.put("caseType", caseEntity.getCaseType());
    		caseData.put("caseNumber", caseEntity.getCaseNumber()+"");
    	}
    	
    	return caseData;
    }
    
    public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			LOG.error("Cannot find the court site name.");
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
			LOG.error("Cannot find the court room number.");
			e.printStackTrace();
		}
		return courtRoomNo;
	}

    /**
     * Sends the message
     * 
     * @param publicDisplayEvent
     */
    private static void publishPublicDisplayEvent(PublicDisplayEvent publicDisplayEvent) {
        _publicDisplayNotifier.sendMessage(publicDisplayEvent);
        PddaHelper pddaHelper = new PddaHelper();
        pddaHelper.sendMessage(publicDisplayEvent, "Pdda", true); // Skip sending to XHIBIT as its handled above
    }
}
