package uk.gov.courtservice.xhibit.crlivestatus;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_display.XhbCrLiveDisplay;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_display.XhbCrLiveDisplayBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_display.XhbCrLiveDisplayBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_internet.XhbCrLiveInternet;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_internet.XhbCrLiveInternetBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_internet.XhbCrLiveInternetBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.CourtLogXslHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.MaskingTranslator;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.TranslationContext;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.TranslationType;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * A helper class to contain all of the logic for manipulating the
 * xhb_cr_live_status entry for a court room and scheduled hearings.
 * 
 * @author pznwc5
 * @author tz0d5m
 * @version $Revision: 1.7 $
 */
public class CrLiveStatusHelper {
    private static final Logger log = CSServices.getLogger(CrLiveStatusHelper.class);

    /** Stylesheet used to remove freetext */
    private static final String REMOVE_FREE_TEXT_STYLESHEET = "config/courtlog/transformer/remove_free_text.xsl";

    private CrLiveStatusHelper() {
        // prevent external instantiation
    }

    /**
     * Updates the live status when a case is moved. The record's scheduled
     * hearing, public display and internet statuses are all set to null.
     * 
     * @param scheduledHearingId
     * @param courtRoomId
     */
    public static void moveCaseFromRoom(Integer scheduledHearingId, Integer courtRoomId) {
        log.debug("moveCaseFromRoom() - start");

        XhbCourtRoom courtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKey(courtRoomId);
        XhbCrLiveInternet xcli = getCrLiveInternet(courtRoom);
        XhbCrLiveDisplay xcld = getCrLiveDisplay(courtRoom);
        XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(scheduledHearingId);

        XhbScheduledHearing liveInternetScheduledHearing = xcli.getXhbScheduledHearing();
        XhbScheduledHearing liveDisplayScheduledHearing = xcld.getXhbScheduledHearing();

        if ((liveInternetScheduledHearing != null) && liveInternetScheduledHearing.isIdentical(xsh)) {
            clearCrLiveInternetStatus(xcli);
        }

        if ((liveDisplayScheduledHearing != null) && liveDisplayScheduledHearing.isIdentical(xsh)) {
            clearCrLiveDisplayStatus(xcld);
        }

        log.debug("moveCaseFromRoom() - end");
    }

    /**
     * Method to indicate that the public display should be activated. This will
     * clear out the xhb_cr_live_display row entries for the court room that the
     * scheduled hearing is assigned to, and then assign the passed in scheduled
     * hearing to the cr live status.
     * 
     * @param xsh
     * @param activationDate
     */
    public static void activatePublicDisplay(XhbScheduledHearing xsh, Date activationDate) {
        log.debug("activatePublicDisplay() - start");

        // set xhb_cr_live_display details
        final XhbCrLiveDisplay xcld = getCrLiveDisplay(xsh.getXhbSitting().getXhbCourtRoom());

        // ensure that the current status is cleared...
        clearCrLiveDisplayStatus(xcld);

        // now set this scheduled hearing to the status...
        xcld.setXhbScheduledHearing(xsh);
        xcld.setTimeStatusSet(activationDate);

        // set xhb_cr_live_internet details
        final XhbCrLiveInternet xcli = getCrLiveInternet(xsh.getXhbSitting().getXhbCourtRoom());

        // ensure that the current status is cleared...
        clearCrLiveInternetStatus(xcli);

        // now set this scheduled hearing to the status...
        xcli.setXhbScheduledHearing(xsh);
        xcli.setTimeStatusSet(activationDate);

        log.debug("activatePublicDisplay() - end");
    }

    /**
     * Method to indicate that the public display should be deactivated if the
     * scheduled hearing passed in is not already de-activated. This will clear
     * out the xhb_cr_live_status row entries for the court room that the
     * scheduled hearing is assigned to.
     * 
     * @param xsh
     * @param deactivationDate
     */
    public static void deactivatePublicDisplay(XhbScheduledHearing xsh, Date deactivationDate) {
        log.debug("deactivatePublicDisplay() - start");

        // set xhb_cr_live_display details
        final XhbCrLiveDisplay xcld = getCrLiveDisplay(xsh.getXhbSitting().getXhbCourtRoom());
        final XhbScheduledHearing liveDisplayScheduledHearing = xcld.getXhbScheduledHearing();

        if ((liveDisplayScheduledHearing != null) && liveDisplayScheduledHearing.isIdentical(xsh)) {
            clearCrLiveDisplayStatus(xcld);
            // for consistency, also set the time status set...
            xcld.setTimeStatusSet(deactivationDate);
        }

        // set xhb_cr_live_internet details
        final XhbCrLiveInternet xcli = getCrLiveInternet(xsh.getXhbSitting().getXhbCourtRoom());
        final XhbScheduledHearing liveInternetScheduledHearing = xcli.getXhbScheduledHearing();

        if ((liveInternetScheduledHearing != null) && liveInternetScheduledHearing.isIdentical(xsh)) {
            clearCrLiveInternetStatus(xcli);
            // for consistency, also set the time status set...
            xcli.setTimeStatusSet(deactivationDate);
        }

        log.debug("deactivatePublicDisplay() - end");
    }

    /**
     * Update the internet status if the passed in court log event is more
     * recent than the last displayed internet event for the case whose
     * scheduled hearing is passed in on the view value.
     * 
     * @param clvv
     *            The <code>CourtLogViewValue</code> of the event that was
     *            created.
     */
    public static void updateInternetStatus(CourtLogViewValue clvv) {
        log.debug("updateInternetStatus() - start");

        XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(clvv.getScheduledHearingId());
        XhbCrLiveInternet xcli = getCrLiveInternet(xsh);

        if ((xcli != null) && !xcli.getTimeStatusSet().after(clvv.getEntryDate())) {
            log.debug("updateInternetStatus() - updating the entry");

            xcli.setTimeStatusSet(clvv.getEntryDate());
            xcli.setStatus(getInternetStatus(clvv));
        }

        log.debug("updateInternetStatus() - end");
    }

    /**
     * Update the public display status if the passed in court log event is more
     * recent than the last displayed public display event for the case whose
     * scheduled hearing is passed in on the view value.
     * 
     * @param clvv
     *            The <code>CourtLogViewValue</code> of the event that was
     *            created or updated.
     * @return <i>true</i> if there is a cr live status entry for the scheduled
     *         hearing on the log event passed in and the court log event passed
     *         in is more recent than the time status set of the live status
     *         entry, <i>false</i> will be returned otherwise.
     */
    public static boolean updatePublicDisplayStatus(CourtLogViewValue clvv) {
        log.debug("updatePublicDisplayStatus() - start");
        XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(clvv.getScheduledHearingId());
        XhbCrLiveDisplay xcld = getCrLiveDisplay(xsh);

        if ((xcld != null) && !xcld.getTimeStatusSet().after(clvv.getEntryDate())) {
            xcld.setTimeStatusSet(clvv.getEntryDate());
            xcld.setStatus(getPublicDisplayStatus(clvv));

            log.debug("updatePublicDisplayStatus() - returning true");
            return true;
        }

        log.debug("updatePublicDisplayStatus() - returning false");
        return false;
    }

    /**
     * Clear out the public display status if the deleted court log event was
     * deemed to be the event displayed on the public display.
     * 
     * @param clvv
     *            The <code>CourtLogViewValue</code> of the event that was
     *            deleted.
     * @return <i>true</i> if there is a cr live status entry for the scheduled
     *         hearing on the log event passed in, the current public display
     *         status is not set to <i>null</i>, and the court log event passed
     *         in is more recent than the time status set of the live status
     *         entry, <i>false</i> will be returned otherwise.
     */
    public static boolean deletePublicDisplayStatus(CourtLogViewValue clvv) {
        log.debug("deletePublicDisplayStatus() - start");

        XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(clvv.getScheduledHearingId());
        XhbCrLiveDisplay xcld = getCrLiveDisplay(xsh);

        if ((xcld != null) && (xcld.getStatus() != null) && xcld.getTimeStatusSet().equals(clvv.getEntryDate())) {
            xcld.setStatus(null);

            log.debug("deletePublicDisplayStatus() - returning true");
            return true;
        }

        log.debug("deletePublicDisplayStatus() - returning false");
        return false;
    }

    /**
     * Get the internet status by transforming the passed in value object and
     * adding the defendant masked names if appropriate.
     * 
     * @param viewValue
     * @return The translated xml <code>String</code>.
     */
    private static String getInternetStatus(CourtLogViewValue viewValue) {
        log.debug("getInternetStatus() - start");
        TranslationContext context = new TranslationContext();

        if (viewValue.getDefendantOnCaseId() != null) {
            XhbDefendantOnCase defOnCase = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(viewValue
                    .getDefendantOnCaseId());

            context.put(MaskingTranslator.MASKED_NAME, defOnCase.getMaskedName());
            context.put(MaskingTranslator.MASKED_FLAG, defOnCase.getIsMasked());
        }

        log.debug("getInternetStatus() - end");
        return CourtLogXslHelper.translateEvent(viewValue, Locale.UK, TranslationType.INTERNET, context,
                REMOVE_FREE_TEXT_STYLESHEET);
    }

    /**
     * Get the public display status by transforming the passed in value object
     * by removing the free text.
     * 
     * @param viewValue
     * @return The translated xml <code>String</code>.
     */
    private static String getPublicDisplayStatus(CourtLogViewValue viewValue) {
        log.debug("getPublicDisplayStatus() - start and finish");
        return CourtLogXslHelper.translateEvent(viewValue, Locale.UK, TranslationType.PUBLIC_DISPLAY,
                REMOVE_FREE_TEXT_STYLESHEET);
    }

    /**
     * Gets the CR live Intenet status for the court room passed in, if
     * currently one does not exist, it will be created.
     * 
     * @param courtRoom
     *            Court room
     * @return CR live status
     */
    private static XhbCrLiveInternet getCrLiveInternet(XhbCourtRoom courtRoom) {
        Collection liveStatuses = courtRoom.getXhbCrLiveInternets();

        // This will always be 0 or 1 due to a unique constraint in the database
        if (liveStatuses.size() > 0) {
            log.debug("CR live Internet found");
            return (XhbCrLiveInternet) liveStatuses.iterator().next();
        }

        // otherwise, create a new one...
        log.debug("Creating CR live internet");
        final XhbCrLiveInternetBasicValue xclibv = new XhbCrLiveInternetBasicValue();
        xclibv.setTimeStatusSet(new Date());

        return XhbCrLiveInternetBeanHelper2.createLocal(xclibv, courtRoom, null);
    }

    private static XhbCrLiveInternet getCrLiveInternet(XhbScheduledHearing xsh) {
        Collection col = xsh.getXhbCrLiveInternets();
        log.debug("getCrLiveInternet(" + xsh.getScheduledHearingId() + ") has " + col.size()
                + " cr_live_internet entries");

        if (col.size() > 0) {
            return (XhbCrLiveInternet) col.iterator().next();
        }

        // no live internet for the scheduled hearing so return null
        return null;
    }

    private static XhbCrLiveDisplay getCrLiveDisplay(XhbCourtRoom courtRoom) {
        Collection liveStatuses = courtRoom.getXhbCrLiveDisplays();

        // This will always be 0 or 1 due to a unique constraint in the database
        if (liveStatuses.size() > 0) {
            log.debug("CR live Internet found");
            return (XhbCrLiveDisplay) liveStatuses.iterator().next();
        }

        // otherwise, create a new one...
        log.debug("Creating CR live internet");
        final XhbCrLiveDisplayBasicValue xcldbv = new XhbCrLiveDisplayBasicValue();
        xcldbv.setTimeStatusSet(new Date());

        // TODO May need to set a scheduled hearing id retrieved from the court
        // room.

        return XhbCrLiveDisplayBeanHelper2.createLocal(xcldbv, courtRoom, null);
    }

    private static XhbCrLiveDisplay getCrLiveDisplay(XhbScheduledHearing xsh) {
        Collection col = xsh.getXhbCrLiveDisplays();
        log.debug("getCrLiveDisplay(" + xsh.getScheduledHearingId() + ") has " + col.size()
                + " cr_live_display entries");

        if (col.size() > 0) {
            return (XhbCrLiveDisplay) col.iterator().next();
        }

        // no live display for the scheduled hearing so return null
        return null;
    }

    private static void clearCrLiveInternetStatus(XhbCrLiveInternet crLiveInternet) {
        log.debug("clearCRLiveInternetStatus() - for id " + crLiveInternet.getCrLiveInternetId());
        crLiveInternet.setXhbScheduledHearing(null);
        crLiveInternet.setStatus(null);
    }

    private static void clearCrLiveDisplayStatus(XhbCrLiveDisplay crLiveDisplay) {
        log.debug("clearCRLiveDisplayStatus() - for id " + crLiveDisplay.getCrLiveDisplayId());
        crLiveDisplay.setXhbScheduledHearing(null);
        crLiveDisplay.setStatus(null);
    }
}
