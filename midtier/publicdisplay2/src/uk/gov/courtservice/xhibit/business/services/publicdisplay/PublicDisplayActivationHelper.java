package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query.ActiveCasesInRoomQuery;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ActivateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper;

/**
 * <p>
 * Title: Helper class for query and setting the is_case_active flag
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
 * @author Rakesh Lakhani
 * @version $Id: PublicDisplayActivationHelper.java,v 1.5 2004/07/16 14:57:13
 *          tz0d5m Exp $
 */
public class PublicDisplayActivationHelper {
    /**
     * The DB value for an Active Display = "Y"
     */
    private static final String DISPLAY_ACTIVE = "Y";

    /**
     * The DB value for an Inctive Display = "N"
     */
    private static final String DISPLAY_INACTIVE = "N";

    private static final Logger log = CSServices.getLogger(PublicDisplayActivationHelper.class);

    /**
     * Check the Public display activation status for this particular scheduled
     * hearing.
     * 
     * @param schedHearingId
     * @return boolean true if scheduled hearing is active
     */
    public static boolean isPublicDisplayActive(Integer schedHearingId) {
        XhbScheduledHearing scheduledHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(schedHearingId);
        String isActiveVal = scheduledHearing.getIsCaseActive();

        return (isActiveVal != null && isActiveVal.equals(DISPLAY_ACTIVE));
    }

    /**
     * Sets the public display for this scheduling hearing to Activate.
     * 
     * @param schedHearingId
     */
    public static void activatePublicDisplay(PddaHelper notifier, Integer schedHearingId,
            Date activationDeactivationDate, boolean activate, String userDisplayName) {
        log.debug("activatePublicDisplay() with schedHearingId: " + schedHearingId);

        XhbScheduledHearing scheduledHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(schedHearingId);
        if (activate) {
            CrLiveStatusHelper.activatePublicDisplay(scheduledHearing, activationDeactivationDate);
        } else {
            CrLiveStatusHelper.deactivatePublicDisplay(scheduledHearing, activationDeactivationDate);
        }

        setActivationOfPDforSchedHearing(scheduledHearing, activate);

        // make sure no other sched hearings are active in this court room
        deactivateOtherSchedHearings(scheduledHearing);

        // Notify Public display listener
        Integer courtRoomId = scheduledHearing.getXhbSitting().getCourtRoomId();
        Integer courtId = scheduledHearing.getXhbSitting().getXhbCourtSite().getCourtId();

        sendCaseActivateEvent(notifier, courtId, courtRoomId, activate, userDisplayName);
    }

    /**
     * Send JMS message notifying of change in status
     * 
     * @param notifier
     *            notifer to use
     * @param courtId
     *            The court event occurred in
     * @param courtRoomId
     *            The court room the event occurred in
     * @param activate
     *            Whether the display is activated or deactivated
     */
    private static void sendCaseActivateEvent(PddaHelper notifier, Integer courtId, Integer courtRoomId,
            boolean activate, String userDisplayName) {
    	
    	PublicDisplayActivationHelper pdah = new PublicDisplayActivationHelper(); 
    	String courtName = pdah.getCourtName(courtId);
		Integer courtRoomNo = pdah.getCourtRoomNumber(courtRoomId);
		DisplayablePublicNoticeValue[] publicNotices = null;
		try {
			publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtRoomId);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
        CourtRoomIdentifier courtRoomIdentifier = new CourtRoomIdentifier(courtId, courtRoomId, courtName, courtRoomNo, publicNotices);
        ActivateCaseEvent ace = new ActivateCaseEvent(courtRoomIdentifier, new CaseChangeInformation(activate));
        notifier.sendMessage(ace, userDisplayName);
    }

    /**
     * Set whether the scheduled hearing is activated or deactived
     * 
     * @param schedHearing
     * @param isActive
     */
    private static void setActivationOfPDforSchedHearing(XhbScheduledHearing schedHearing, boolean isActive) {
        XhbScheduledHearingBasicValue schedHearBv = schedHearing.getData();
        schedHearBv.setIsCaseActive(isActive ? DISPLAY_ACTIVE : DISPLAY_INACTIVE);

        XhbScheduledHearingBeanHelper2.update(schedHearBv);
    }

    /**
     * For the sched hearing that is passed in, check that all other hearings in
     * the same court room have their public displays turned off.
     * 
     * @param schedHearingId
     * @throws PublicDisplayControllerException
     */
    private static void deactivateOtherSchedHearings(XhbScheduledHearing schedHearing) {
        log.debug("deactivateOtherSchedHearings() with schedHearingId: " + schedHearing.getPrimaryKey());

        Integer courtRoomId = schedHearing.getXhbSitting().getCourtRoomId();
        Integer hearingListId = schedHearing.getXhbSitting().getListId();

        ActiveCasesInRoomQuery query = new ActiveCasesInRoomQuery();
        Collection shIds = query.getData(hearingListId, courtRoomId, schedHearing.getScheduledHearingId());
        log.debug("Active Cases found: " + shIds.size());
        Iterator iter = shIds.iterator();
        while (iter.hasNext()) {
            Integer thisSchedHearingId = (Integer) iter.next();
            log.debug("Deactivating SH ID: " + thisSchedHearingId);

            XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(thisSchedHearingId);
            setActivationOfPDforSchedHearing(xsh, false);
        }
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