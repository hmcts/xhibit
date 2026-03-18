package uk.gov.courtservice.xhibit.business.services.publicnotice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_configured_public_notice.XhbConfiguredPublicNotice;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_definitive_public_notice.XhbDefinitivePublicNotice;
import uk.gov.courtservice.xhibit.business.entities.xhb_public_notice.XhbPublicNotice;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: PublicNoticeWorkFlow - 'nerve centre' of the Public Notice Subsystem
 * </p>
 * <p>
 * Description: see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Pat Fox, Bob Boles
 * @version $Id: PublicNoticeWorkFlow.java,v 1.3 2006/06/05 12:29:54 bzjrnl Exp $
 */
public class PublicNoticeWorkFlow implements PublicNoticeConstants {

    /**
     * Logger log
     */
    private static final Logger log = CSServices.getLogger(PublicNoticeWorkFlow.class);

    /**
     * Gets the allPublicNoticesForCourtRoom attribute of the
     * PublicNoticeWorkFlow object
     * 
     * @param xhbCourtRoomId
     *            Description of the Parameter
     * @return The allPublicNoticesForCourtRoom value
     * @throws PublicNoticeCourtRoomUnknownException
     *             Description of the Exception
     */
    public static DisplayablePublicNoticeValue[] getAllPublicNoticesForCourtRoom(int xhbCourtRoomId)
            throws PublicNoticeCourtRoomUnknownException {

        log.debug(" 01 Enter the getAllPublicNoticesForCourtRoom method for " + xhbCourtRoomId);

        // construct the array given a collection of ConfigurePublicNotices

        DisplayablePublicNoticeValue[] l_displayablePublicNoticeValues = constructDisplayablePublicNoticeValuesForCourtRoom(xhbCourtRoomId);

        log.debug(" 49 Sorting the Array into Priority");

        // sort the array of displayablePublicNoticeValues
        Arrays.sort(l_displayablePublicNoticeValues);

        log.debug(" 50 Exiting the getAllPublicNoticesForCourtRoom method");

        return l_displayablePublicNoticeValues;
    }

    /**
     * Sets the Acivation Status on the Configured Public Notices which
     * correspond to the Displayable Public notices passed in. If an activation
     * status is updated it sends a notification to the Public Display System.
     * 
     * @param publicNotices
     *            Array of displayablePublicNotices with updated isactive status
     * @param xhbCourtRoomId
     *            the target court room ID
     * @throws PublicNoticeCourtRoomUnknownException
     * @throws PublicNoticeInvalidSelectionException
     */
    public static void setAllPublicNoticesForCourtRoom(DisplayablePublicNoticeValue[] publicNotices, int xhbCourtRoomId,
    		String userDisplayName)
            throws PublicNoticeCourtRoomUnknownException, PublicNoticeInvalidSelectionException {
        boolean l_sendNotification = false;
        boolean reportingRestrictionsChanged = false;

        log.debug(" 01 Enter the setAllPublicNoticesForCourtRoom method");

        // need to check the PublicNoticeInvalidSelection does not encroach on
        // any rules
        PublicNoticeSelectionValidator.validateSelection(publicNotices);

        log.debug("02 public notices are validated");

        // iterate through the array of displayable are validated public Notices
        for (int i = 0; i < publicNotices.length; i++) {

            // update if marked as dirty( IsActive/status change)
            if (publicNotices[i].getDirty()) {
                updateConfiguredPublicNoticeActivationState(publicNotices[i]);
                // if any of the ActivationStates are updated need to send a
                // notification to Public displays.
                l_sendNotification = true;
                Integer definitiveNotice = publicNotices[i].getDefinitivePublicNotice();

                // Need to determine whether reporting restrictions were changed
                // or not.
                if (!reportingRestrictionsChanged && definitiveNotice.equals(REPORTING_RESTRICTIONS)
                        || definitiveNotice.equals(REPORTING_RESTRICTIONS_LIFTED)) {
                    reportingRestrictionsChanged = true;
                }
            }
        }

        // Fire a Notification to the Public Display systems to update
        // If something has been updated. Only want to send ONE notification.
        if (l_sendNotification) {
            log.debug("Send the Notication to Public displays");
            sendNotification(xhbCourtRoomId, reportingRestrictionsChanged, userDisplayName);
        }
    }

    /**
     * Based on the CourlogSubscriptions event type it decides what configured
     * public notices Activation Status is updated(If any ). When an Activation
     * status is updated a notification is sent to the public Displays
     * subsystem.
     * 
     * @param courtLogSubscriptionValue
     *            The new publicNoticeforCourtRoom value
     * @throws PublicNoticeException
     *             Description of the Exception
     */

    public static void setPublicNoticeforCourtRoom(CourtLogSubscriptionValue courtLogSubscriptionValue,
    		String userDisplayName)
            throws PublicNoticeException {

        log.debug(" 01 Enter the setPublicNoticeforCourtRoom xhbCourtRoomID"
                + courtLogSubscriptionValue.getCourtRoomId());

        // updates are carried out need to send a notification
        if (updateConfiguredPublicNoticeActivationState(courtLogSubscriptionValue)) {
            log.debug(" 0 updated sending Event" + courtLogSubscriptionValue.getCourtRoomId());
            sendNotification(courtLogSubscriptionValue, userDisplayName);
        }
    }

    /**
     * updateConfiguredPublicNoticeActivationState updates the Activation State
     * based on the contents of the DisplayablePublicNotice Value.
     * 
     * @param publicNotice
     *            parameter for updateConfiguredPublicNoticeActivationState
     */
    private static void updateConfiguredPublicNoticeActivationState(DisplayablePublicNoticeValue publicNotice) {
        PublicNoticeMaintainer.updateIsActive(publicNotice);
    }

    /**
     * sendNotification sends a notification to the public display system.
     * 
     * @param courtLogSubscriptionValue
     *            parameter for sendNotification
     */
    private static void sendNotification(CourtLogSubscriptionValue courtLogSubscriptionValue, String userDisplayName) {
        PublicNoticeChangeNotifier.sendNotificationtoPublicDisplays(courtLogSubscriptionValue, userDisplayName);
    }

    /**
     * updateConfiguredPublicNoticeActivationState
     * 
     * @param courtLogSubscriptionValue
     *            parameter for updateConfiguredPublicNoticeActivationState
     * @return the returned boolean
     * @throws PublicNoticeException -
     */
    private static boolean updateConfiguredPublicNoticeActivationState(
            CourtLogSubscriptionValue courtLogSubscriptionValue) throws PublicNoticeException {
        boolean l_updated = PublicNoticeSelectionManipulator.manipulateSelection(courtLogSubscriptionValue);
        return l_updated;
    }

    /**
     * sendNotification sends a notification to the public display system.
     * 
     * @param xhbCourtRoomId
     *            parameter for sendNotification
     */
    private static void sendNotification(int xhbCourtRoomId, boolean reportingRestrictionsChanged, String userDisplayName) {
        PublicNoticeChangeNotifier.sendNotificationtoPublicDisplays(xhbCourtRoomId, reportingRestrictionsChanged, userDisplayName);
    }

    /**
     * constructs the Array of DisplayablePublicNoticeValue objects given an
     * Xhibit Court Room Id.
     * 
     * @return Array of DisplayPublicNotices
     */

    private static DisplayablePublicNoticeValue[] constructDisplayablePublicNoticeValuesForCourtRoom(
            int l_xhbCourtRoomId) throws PublicNoticeCourtRoomUnknownException {
        Integer courtRoomId = new Integer(l_xhbCourtRoomId);
        XhbCourtRoom courtRoom;
        try {
            courtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKey(courtRoomId);
        } catch (XhbCourtRoomBeanNotFoundException ex) {
            throw new PublicNoticeCourtRoomUnknownException(courtRoomId, ex);
        }

        Collection configuredPublicNotices = courtRoom.getXhbConfiguredPublicNotices();

        ArrayList displayablePublicNotices = new ArrayList();
        Iterator iterator = configuredPublicNotices.iterator();
        while (iterator.hasNext()) {
            displayablePublicNotices.add(createDisplayablePublicNotice((XhbConfiguredPublicNotice) iterator.next()));
        }

        return (DisplayablePublicNoticeValue[]) displayablePublicNotices
                .toArray(new DisplayablePublicNoticeValue[displayablePublicNotices.size()]);
    }

    private static DisplayablePublicNoticeValue createDisplayablePublicNotice(
            XhbConfiguredPublicNotice l_configuredPublicNotice) {
        if (log.isDebugEnabled()) {
            log.debug(" creating a displayablePublicNoticeValue for configredPN "
                    + l_configuredPublicNotice.getConfiguredPublicNoticeId());
        }

        XhbPublicNotice l_publicNotice = l_configuredPublicNotice.getXhbPublicNotice();
        XhbDefinitivePublicNotice l_definitivePublicNotice = l_publicNotice.getXhbDefinitivePublicNotice();

        boolean isActive = false;
        if (l_configuredPublicNotice.getIsActive() != null && l_configuredPublicNotice.getIsActive().equals("1")) {
            isActive = true;
        }

        return new DisplayablePublicNoticeValue(l_configuredPublicNotice.getConfiguredPublicNoticeId(), l_publicNotice
                .getPublicNoticeDesc(), isActive, l_configuredPublicNotice.getVersion(), l_definitivePublicNotice
                .getDefinitivePnId(), l_definitivePublicNotice.getPriority().intValue());
    }

}