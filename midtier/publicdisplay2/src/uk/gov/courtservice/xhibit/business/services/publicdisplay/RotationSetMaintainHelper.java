package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.Collection;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocument;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDd;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.DisplayDocumentNotFoundException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.PublicDisplayCheckedException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.RotationSetNotFoundCheckedException;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtRotationSetConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

/**
 * <p>
 * Title: Rotation Set Maintain Helper
 * </p>
 * <p>
 * Description: Helper methods for creating and editting a rotation set.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author unascribed
 * @version $Id: RotationSetMaintainHelper.java,v 1.8 2006/04/12 13:18:29 bzjrnl
 *          Exp $
 */

public class RotationSetMaintainHelper {
    private static final Logger log = CSServices.getLogger(RotationSetMaintainHelper.class);

    /**
     * Creates a new rotation set with associated documents
     *
     * @param newRotationSet
     *            This object must contain a RotationSetBasic value with court
     *            Id populated and a list of RotationSetDDComplex values with
     *            RotationSetDDBasicValues and valid DisplayDocumentBasicValues
     */
    public static void createRotationSets(RotationSetComplexValue newRotationSet) {
        // Get the court id and lookup the court local reference
        Integer courtId = newRotationSet.getCourtId();
        XhbCourt courtLocal = null;
        try {
            courtLocal = XhbCourtBeanHelper.findByPrimaryKey(courtId);
        } catch (ObjectNotFoundException ex) {
            // Not finding the coutr is unexpected
            throw new uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException(courtId, ex);
        }

        // Create the rotation set local reference
        XhbRotationSet rotationSetLocal = XhbRotationSetBeanHelper.createLocal(newRotationSet
                .getRotationSetBasicValue(), courtLocal);

        addRotationSetDds(rotationSetLocal, newRotationSet);

    }

    /**
     * Updates the rotation set with display documents that have been selected
     * <p/> Note: sends a RotationSet changed JMS configuration message
     *
     * @param rotationSet
     *            The rotation set being updated and an array of display
     *            documents with ordering and delay information
     * @throws PublicDisplayCheckedException
     *             Thrown if rotation set does not exist in the DB
     */
    public static void setDisplayDocumentsForRotationSet(RotationSetComplexValue rotationSet,
            PddaHelper notifier, String userDisplayName) throws PublicDisplayCheckedException {
        // A flag to identify if any changes have been made that require
        // a JMS message to be sent
        boolean hasNotifyChanges = false;

        Integer rotationSetId = rotationSet.getRotationSetId();

        XhbRotationSet rotationSetLocal = null;
        try {
            rotationSetLocal = XhbRotationSetBeanHelper.findByPrimaryKey(rotationSetId);
        } catch (ObjectNotFoundException ex) {
            throw new RotationSetNotFoundCheckedException(rotationSetId, ex);
        }

        // Check that we are not editting a system RS.
        if (rotationSetLocal.getDefaultYn().equals("Y")) {
            throw new PublicDisplayCheckedException("pubdisp.rotationset.editsystem", new String[] { rotationSetLocal
                    .getDescription() }, "Can not edit a system rotation set");
        }

        rotationSetLocal.setData(rotationSet.getRotationSetBasicValue());

        RotationSetDDComplexValue[] rotationSetDdComplexValues = rotationSet.getRotationSetDDComplexValues();

        // Updates or deletes rotation set dds
        boolean rotationSetDDsUpdated = updateOrDeleteRotationSetDds(rotationSetLocal, rotationSet);

        // Add new rotation set dds
        boolean rotationSetDDsAdded = addRotationSetDds(rotationSetLocal, rotationSet);

        hasNotifyChanges = hasNotifyChanges || rotationSetDDsAdded || rotationSetDDsUpdated;
        // If a change has occurred that should require re-rendering
        // then send a message.
        if (hasNotifyChanges) {
            sendNotification(rotationSet, notifier, userDisplayName);
        }
    }

    /**
     * This deletes a rotation set only if it is not a system rotation set and
     * is not assigned to any displays
     *
     * @param rotationSet
     * @throws PublicDisplayCheckedException
     */
    public static void deleteRotationSet(RotationSetComplexValue rotationSet) throws PublicDisplayCheckedException {
        XhbRotationSet rotationSetLocal;
        // Check optimistic locking.
        // Get the rotation set from the DB
        try {
            rotationSetLocal = XhbRotationSetBeanHelper.findByPrimaryKey(rotationSet.getRotationSetId());
        } catch (ObjectNotFoundException ex) {
            // if the object could not be found, then it is already deleted.
            // Even though this is an unexpected condition, it results in
            // what
            // we wanted so no need to report an error.
            return;
        }

        // Check that we are not deleting a system RS.
        if (rotationSetLocal.getDefaultYn().equals("Y")) {
            throw new PublicDisplayCheckedException("pubdisp.rotationset.deletesystem", new String[] { rotationSetLocal
                    .getDescription() }, "Can not delete a system rotation set");
        }

        // Check the rotation set is not assigned to any displays.
        if (!rotationSetLocal.getXhbDisplays().isEmpty()) {
            throw new PublicDisplayCheckedException("pubdisp.rotationset.assigned", new String[] { rotationSetLocal
                    .getDescription() }, "Can not delete a that is currently in use");
        }

        // Delete the rotation set and the rotation set dd's
        RotationSetDDComplexValue[] rsDdComplex = rotationSet.getRotationSetDDComplexValues();
        for (int i = 0; i < rsDdComplex.length; i++) {
            try {
                XhbRotationSetDdBeanHelper.remove(rsDdComplex[i].getRotationSetDDBasicValue());
            } catch (ObjectNotFoundException ex) {
                // The object is already deleted so continue without
                // reporting an error
                log.debug("Rotation set DD already deleted");
            }
        }
        try {
            XhbRotationSetBeanHelper.remove(rotationSet.getRotationSetBasicValue());
        } catch (ObjectNotFoundException ex) {
            // The object is already deleted so continue without
            // reporting an error
            log.debug("Rotation set already deleted");
        }
    }

    /**
     * Adds new rotation set dds to the rotation sets
     *
     * @param rotationSetLocal
     *            Rotation set to which the rotation set dds are added
     * @param rotationSetDdComplexValues
     *            Rotation set DDs to be added
     * @return
     */
    private static boolean addRotationSetDds(XhbRotationSet rotationSetLocal, RotationSetComplexValue rotationSet) {
        boolean hasAdded = false;

        RotationSetDDComplexValue[] rotationSetDdComplexValues = rotationSet.getRotationSetDDComplexValues();

        XhbDisplayDocument displayDocumentLocal = null;
        for (int i = 0; i < rotationSetDdComplexValues.length; i++) {
            XhbRotationSetDdBasicValue rotationSetDdBasicValue = rotationSetDdComplexValues[i]
                    .getRotationSetDDBasicValue();

            if (rotationSetDdBasicValue.getPrimaryKey() != null)
                continue;

            // Lookup the display document local reference
            Integer displayDocumentId = rotationSetDdComplexValues[i].getDisplayDocumentId();
            try {
                displayDocumentLocal = XhbDisplayDocumentBeanHelper.findByPrimaryKey(displayDocumentId);
            } catch (ObjectNotFoundException ex) {
                // Not finding the display document is unexpected
                throw new DisplayDocumentNotFoundException(displayDocumentId, ex);
            }

            // Create the rotation set dd local reference
            XhbRotationSetDdBeanHelper.create(rotationSetDdComplexValues[i].getRotationSetDDBasicValue(),
                    rotationSetLocal, displayDocumentLocal);

            if (!hasAdded)
                hasAdded = true;
        }

        return hasAdded;
    }

    private static boolean updateOrDeleteRotationSetDds(XhbRotationSet rotationSetLocal,
            RotationSetComplexValue rotationSet) {
        boolean hasUpdatedOrDeleted = false;

        // get the local references of existing rotation set DD's
        Collection tmp = rotationSetLocal.getXhbRotationSetDds();
        XhbRotationSetDd[] rotationSetDdLocals = (XhbRotationSetDd[]) tmp.toArray(new XhbRotationSetDd[tmp.size()]);

        // Now iterate through the locals.
        // If for each local a corresponding value basic value can be found
        // in the data passed it, attempt to update it,
        // otherwise it has been removed, so remove it from the DB.
        for (int i = 0; i < rotationSetDdLocals.length; i++) {

            XhbRotationSetDd rotationSetDdLocal = rotationSetDdLocals[i];
            Integer rotationSetDdId = rotationSetDdLocal.getRotationSetDdId();
            if (rotationSet.hasRotationSetDd(rotationSetDdId)) {
                XhbRotationSetDdBasicValue rotationSetDd = rotationSet.getRotationSetDd(rotationSetDdId);
                log.debug("Rotation Set DD for id " + rotationSetDd.getPrimaryKey() + " found.");
                log.debug("Page order = " + rotationSetDd.getOrdering() + ", delay = " + rotationSetDd.getPageDelay());
                rotationSetDdLocal.setData(rotationSetDd);
                hasUpdatedOrDeleted = true;
            } else {
                // Could not use the following line as it was failing
                // rotationSetDdIter.remove();
                try {
                    rotationSetDdLocal.remove();
                } catch (javax.ejb.RemoveException ex) {
                    throw new javax.ejb.EJBException("Error removing RotationSetDD", ex);
                }
                hasUpdatedOrDeleted = true;
            }
        }
        return hasUpdatedOrDeleted;
    }

    /**
     * Sends a JMS notification
     *
     * @param rotationSet
     *            The rotation set that has been changed
     */
    private static void sendNotification(RotationSetComplexValue rotationSet, PddaHelper notifier, String userDisplayName) {
    	try {
	    	CourtMaintainer courtMaintainer = new CourtMaintainer();
			String courtName = courtMaintainer.findByPrimaryKey(rotationSet.getRotationSetBasicValue().getCourtId()).getCourtName();
	        CourtConfigurationChange ccc = new CourtRotationSetConfigurationChange(rotationSet.getRotationSetBasicValue()
	                .getCourtId().intValue(), courtName, rotationSet.getRotationSetBasicValue().getPrimaryKey().intValue());
	        ConfigurationChangeEvent ccEvent = new ConfigurationChangeEvent(ccc);
	        notifier.sendMessage(ccEvent, userDisplayName);
    	} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
    }

}