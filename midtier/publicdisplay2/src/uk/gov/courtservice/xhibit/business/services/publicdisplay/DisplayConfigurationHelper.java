package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplay;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.CourtRoomNotFoundException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.DisplayNotFoundCheckedException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.DisplayNotFoundException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.RotationSetNotFoundCheckedException;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: Display Configuration Helper
 * </p>
 * <p>
 * Description: Helper class to update a display configuration<br>
 * A display corresponds to one physical screen and the configuration includes
 * the assigned rotation set and the court rooms it covers.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisplayConfigurationHelper.java,v 1.6 2005/11/17 10:55:46
 *          bzjrnl Exp $
 */

public class DisplayConfigurationHelper {
	
	private static final Logger log = CSServices.getLogger(DisplayConfigurationHelper.class);

    public static DisplayConfiguration getDisplayConfiguration(Integer displayId)

    {
        try {
            XhbDisplay display = XhbDisplayBeanHelper.findByPrimaryKey(displayId);
            DisplayConfiguration displayConfig = new DisplayConfiguration(display.getData(), display
                    .getXhbRotationSetData(), getDisplayCourtRooms(display));
            return displayConfig;
        } catch (ObjectNotFoundException ex) {
            throw new DisplayNotFoundException(displayId, ex);
        }
    }

    private static XhbCourtRoomBasicValue[] getDisplayCourtRooms(XhbDisplay display) {
        boolean isMultiSite = false;
        Collection col = display.getXhbCourtRooms();
        if (!col.isEmpty()) {
            Integer courtId = ((XhbCourtRoom) col.iterator().next()).getXhbCourtSite().getCourtId();
            isMultiSite = isCourtMultiSite(courtId);
        }
        if (isMultiSite) {
            return getMultiSiteCourtRoomData(display.getXhbCourtRooms());
        } else {
            return display.getXhbCourtRoomsData();
        }
    }

    private static XhbCourtRoomBasicValue[] getMultiSiteCourtRoomData(Collection values) {
        XhbCourtRoomBasicValue[] returnValues = new XhbCourtRoomBasicValue[values.size()];
        HashMap courtSites = new HashMap();
        int i = 0;

        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            XhbCourtRoom courtRoom = (XhbCourtRoom) iter.next();

            Integer courtSiteId = courtRoom.getCourtSiteId();
            XhbCourtSite thisSite;
            if (courtSites.containsKey(courtSiteId)) {
                thisSite = (XhbCourtSite) courtSites.get(courtSiteId);
            } else {
                thisSite = courtRoom.getXhbCourtSite();
                courtSites.put(courtSiteId, thisSite);
            }
            returnValues[i] = courtRoom.getMultiSiteData(thisSite.getShortName(), thisSite.getCourtSiteCode());

            i++;
        }
        return returnValues;
    }

    private static boolean isCourtMultiSite(Integer courtId) {
        return (XhbCourtBeanHelper2.findByPrimaryKey(courtId).getXhbCourtSites().size() > 1);
    }

    /**
     * Updates the display configuration with changes
     * 
     * Note: sends a DisplayConfigurationChanged JMS configuration message
     * 
     * @param displayConfiguration
     *            The updated display configuration to be stored
     */
    public static void updateDisplayConfiguration(DisplayConfiguration displayConfiguration,
            PddaHelper notifier, String userDisplayName) throws RotationSetNotFoundCheckedException, DisplayNotFoundCheckedException {

        // Lookup the display local reference
        Integer displayId = displayConfiguration.getDisplayId();
        XhbDisplay displayLocal = null;
        try {
            displayLocal = XhbDisplayBeanHelper.findByPrimaryKey(displayId);
        } catch (ObjectNotFoundException ex) {
            throw new DisplayNotFoundException(displayId, ex);
        }

        // if the rotation set has been updated write back to DB
        if (displayConfiguration.isRotationSetChanged()) {
            setRotationSet(displayConfiguration, displayLocal);
        }

        // if the court rooms have been updated write back to DB
        if (displayConfiguration.isCourtRoomsChanged()) {
            try {
                XhbDisplayBeanHelper.update(displayConfiguration.getDisplayBasicValue());
            } catch (ObjectNotFoundException ex) {
                throw new DisplayNotFoundCheckedException(displayConfiguration.getDisplayBasicValue().getPrimaryKey(),
                        ex);
            }
            setCourtRooms(displayConfiguration, displayLocal);
        }

        // if RS or courtrooms have changed send JMS message for displayId
        if (displayConfiguration.isCourtRoomsChanged() || displayConfiguration.isRotationSetChanged()) {
            sendNotification(displayId, displayLocal, notifier, userDisplayName);
        }
    }

    /**
     * Sets the court rooms
     * 
     * @param displayConfiguration
     *            Display configuration
     * @param displayLocal
     *            Display local reference
     */
    private static void setCourtRooms(DisplayConfiguration displayConfiguration, XhbDisplay displayLocal) {
        /**
         * if the courts have been changed: Delete the current ones and create
         * with ones passed in. Note: we are not doing optimistic lock checking
         * because this cross reference table will not have a version added
         */
        XhbCourtRoomBasicValue[] courtRoomBasicValues = displayConfiguration.getCourtRoomBasicValues();

        Collection courtRoomLocals = displayLocal.getXhbCourtRooms();

        // delete existing collection
        courtRoomLocals.clear();

        // Add new ones
        for (int i = 0; i < courtRoomBasicValues.length; i++) {
            Integer courtRoomId = null;
            try {
                courtRoomId = courtRoomBasicValues[i].getPrimaryKey();
                courtRoomLocals.add(XhbCourtRoomBeanHelper.findByPrimaryKey(courtRoomId));
            } catch (ObjectNotFoundException ex) {
                throw new CourtRoomNotFoundException(courtRoomId, ex);
            }
        }
    }

    /**
     * Set the rotation set
     * 
     * @param displayConfiguration
     *            Display configuration
     * @param displayLocal
     *            Display local reference
     * @throws RotationSetNotFoundCheckedException
     *             If the rotation set is not found
     */
    private static void setRotationSet(DisplayConfiguration displayConfiguration, XhbDisplay displayLocal)
            throws RotationSetNotFoundCheckedException {
        Integer rotationSetId = displayConfiguration.getRotationSetId();
        try {
            XhbRotationSet rotationSetLocal = XhbRotationSetBeanHelper.findByPrimaryKey(rotationSetId);
            displayLocal.setXhbRotationSet(rotationSetLocal);
        } catch (ObjectNotFoundException ex) {
            throw new RotationSetNotFoundCheckedException(rotationSetId, ex);
        }
    }

    /**
     * Sends a JMS notification
     * 
     * @param displayId
     *            Dipslay Id
     * @param displayLocal
     *            Display local reference
     */
    private static void sendNotification(Integer displayId, XhbDisplay displayLocal, PddaHelper notifier, String userDisplayName) {
    	try {
	        // find court Id
	        Integer courtId = displayLocal.getXhbDisplayLocation().getXhbCourtSite().getCourtId();
	        CourtMaintainer courtMaintainer = new CourtMaintainer();
			String courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
	        CourtConfigurationChange ccc = new CourtDisplayConfigurationChange(courtId.intValue(), courtName, displayId.intValue());
	        ConfigurationChangeEvent ccEvent = new ConfigurationChangeEvent(ccc);
	        notifier.sendMessage(ccEvent, userDisplayName);
    	} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
    }
}