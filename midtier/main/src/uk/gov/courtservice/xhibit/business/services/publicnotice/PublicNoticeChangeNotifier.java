package uk.gov.courtservice.xhibit.business.services.publicnotice;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: Looks after the Notification coming from Public Notices susbsytem
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * 
 * @author Pat Fox,Bob Boles
 * @created 17 February 2003
 * @version 1.0 This Object is responsibe for firing the correct event to the
 *          correct Queue which the public display system is listening.
 */
public class PublicNoticeChangeNotifier implements PublicNoticeConstants {
    private static final Logger log = CSServices.getLogger(PublicNoticeChangeNotifier.class);

    private static final Integer REPORTING_RESTRICTIONS = new Integer(21200);

    private static final Integer REPORTING_RESTRICTIONS_LIFTED = new Integer(21201);

    private static PddaHelper _pddaHelper = new PddaHelper();

    /**
     * Constructor for the PublicNoticeChangeNotifier object
     */
    private PublicNoticeChangeNotifier() {
    }

    /**
     * This takes in a CourtRoom Id and sends an PublicNoticeSubscriptionValue
     * to the correct JMS Queue which Public Displays is listening on.
     * 
     * @param xhbCourtRoomId
     *            Description of the Parameter
     */
    public static void sendNotificationtoPublicDisplays(int xhbCourtRoomId, boolean reportingRestrictionsChanged,
    		String userDisplayName) {
        sendNotificationToNewPublicDisplays(xhbCourtRoomId, reportingRestrictionsChanged, userDisplayName);
    }

    /**
     * This takes in a CourtLogSubscriptionValue Id and sends an
     * PublicNoticeSubscriptionValue to the correct JMS Queue which Public
     * Displays is listening on.
     * 
     * @param courtLogSubscriptionValue
     *            Description of the Parameter
     */
    public static void sendNotificationtoPublicDisplays(CourtLogSubscriptionValue courtLogSubscriptionValue,
    		String userDisplayName) {
        Integer eventType = courtLogSubscriptionValue.getCourtLogViewValue().getEventType();

        sendNotificationToNewPublicDisplays(courtLogSubscriptionValue, eventType.equals(REPORTING_RESTRICTIONS)
                || eventType.equals(REPORTING_RESTRICTIONS_LIFTED), userDisplayName);
    }

    private static void sendNotificationToNewPublicDisplays(int xhbCourtRoomId, boolean reportingRestrictionsChanged,
    		String userDisplayName) {
        XhbCourtRoom courtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKey(new Integer(xhbCourtRoomId));
        
        PublicNoticeChangeNotifier pncn = new PublicNoticeChangeNotifier(); 
        String courtName = pncn.getCourtName(courtRoom.getXhbCourtSite().getCourtId());
		Integer courtRoomNo = pncn.getCourtRoomNumber(xhbCourtRoomId);
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtRoom.getCourtRoomId());
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        CourtRoomIdentifier courtRoomId = new CourtRoomIdentifier(courtRoom.getXhbCourtSite().getCourtId(), courtRoom
                .getCourtRoomId(), courtName, courtRoomNo, publicNotices);
        _pddaHelper.sendMessage(new PublicNoticeEvent(courtRoomId, reportingRestrictionsChanged), userDisplayName);
    }

    private static void sendNotificationToNewPublicDisplays(CourtLogSubscriptionValue courtLogSubscriptionValue,
            boolean reportingRestrictionsChanged, String userDisplayName) {
        sendNotificationToNewPublicDisplays(courtLogSubscriptionValue.getCourtRoomId().intValue(),
                reportingRestrictionsChanged, userDisplayName);
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
}