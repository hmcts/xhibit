package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplay;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocument;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDd;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;

/**
 * <p>
 * Title: Helper class used in the mapping of database held data about Displays
 * and Rotation Sets to datatypes usable in the presentation tier.
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
 * @author Bob Boothby
 * @version 1.0
 */

public class DisplayRotationSetDataHelper extends CSUnrecoverableException {
    private static final Logger LOG = CSServices.getLogger(DisplayRotationSetDataHelper.class);

    /**
     * Utility method that should be part of the general Sun defined APIs but
     * implemented here for a specific use.
     * 
     * @param array
     *            The array to add to the list.
     * @param list
     *            The list to add the array to.
     */
    public static void addArrayToList(Object[] array, List list) {
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
    }

    /**
     * Utility method that gets all the public display configuration data for a
     * court.
     * 
     * @param courtId
     *            The ID for the court.
     * @param court
     *            The entity bean representing the court.
     * @return The full set of public display configuration data for the court.
     */
    public static DisplayRotationSetData[] getDataForCourt(int courtId, XhbCourt court) {
        // Get all court-associated rotation sets.
        Collection rotationSetsForCourt = court.getXhbRotationSets();

        // Aggregate the DisplayRotationSetData for each rotation set.
        Iterator rotationSetIterator = rotationSetsForCourt.iterator();
        ArrayList displayRotationSetDataList = new ArrayList();
        while (rotationSetIterator.hasNext()) {
            DisplayRotationSetDataHelper.addArrayToList(DisplayRotationSetDataHelper.getDataForDisplayRotationSets(
                    courtId, (XhbRotationSet) rotationSetIterator.next()), displayRotationSetDataList);
        }

        // Turn the list into a strongly typed array and return.
        DisplayRotationSetData[] returnArray = new DisplayRotationSetData[displayRotationSetDataList.size()];
        displayRotationSetDataList.toArray(returnArray);
        return returnArray;
    }

    /**
     * Utility method that gets the all the Display Rotation Set information for
     * a given Rotation Set.
     * 
     * @param courtId
     *            The court to which the rotation set belongs.
     * @param rotationSet
     *            The rotation set to retrieve the data for.
     * @return An array of type <code>DisplayRotationSetData</code>.
     */
    public static DisplayRotationSetData[] getDataForDisplayRotationSets(int courtId, XhbRotationSet rotationSet) {
        Collection rotationSetDisplays = rotationSet.getXhbDisplays();
        int numberOfDisplays = rotationSetDisplays.size();

        // Short circuit unnecessary code here.
        if (numberOfDisplays == 0)
            return new DisplayRotationSetData[0];

        // Set up the return array and populate it.
        DisplayRotationSetData[] returnArray = new DisplayRotationSetData[numberOfDisplays];
        Iterator rotationSetDisplayIterator = rotationSetDisplays.iterator();
        for (int i = 0; rotationSetDisplayIterator.hasNext(); i++) {
            XhbDisplay display = (XhbDisplay) rotationSetDisplayIterator.next();
            returnArray[i] = getDisplayRotationSetData(courtId, display, rotationSet);
        }
        return returnArray;
    }

    /**
     * Utility method that gets the Display Rotation Set information for a given
     * Rotation Set and Display
     * 
     * @param courtId
     *            The court to which the rotation set belongs.
     * @param display
     *            the display entity from which to retrieve the data.
     * @param rotationSet
     *            The rotation set entity to retrieve the data for.
     * @return An instance of DisplayRotationSetData.
     */
    public static DisplayRotationSetData getDisplayRotationSetData(int courtId, XhbDisplay display,
            XhbRotationSet rotationSet) {
        // Construct the URI representing the Display.
        DisplayURI displayURI = getDisplayURI(display);
        int[] courtRoomIds = getCourtRoomIds(display.getXhbCourtRooms(), display);

        // Get the RotationSetDisplayDocument[] that will
        // represent the rotation set for the given display.
        RotationSetDisplayDocument[] rotationSetDDs = getDisplayRotationSetElements(courtId, rotationSet
                .getXhbRotationSetDds(), courtRoomIds);

        // Construct the return object.
        return new DisplayRotationSetData(displayURI, rotationSetDDs, display.getDisplayId().intValue(), rotationSet
                .getRotationSetId().intValue(), display.getXhbDisplayType().getDescriptionCode());
    }

    /**
     * Construct an instance of DisplayURI from a <code>XhbDisplay</code>
     * entity bean.
     * 
     * @param display
     *            The entity bean representing the display.
     * @return A DisplayURI representing the display from the entity.
     */
    private static DisplayURI getDisplayURI(XhbDisplay display) {
        DisplayURI displayURI = new DisplayURI(formatString(display.getXhbDisplayLocation().getXhbCourtSite()
                .getXhbCourt().getShortName()), formatString(display.getXhbDisplayLocation().getXhbCourtSite()
                .getCourtSiteCode()), display.getXhbDisplayLocation().getDescriptionCode(), display
                .getDescriptionCode());
        return displayURI;
    }

    /**
     * Formats a string for inclusion in an URI. It replaces spaces with
     * underscores and turns it to lower case.
     * 
     * @param toBeFormatted
     * @return
     */
    private static String formatString(String toBeFormatted) {
        return toBeFormatted.replace(' ', '_').toLowerCase();
    }

    /**
     * Gets a sorted int array of court room IDs from the passed in collection
     * of <code>XhbCourtRoom</code> entity beans. This method will include the
     * unassigned court room id from <code>DisplayDocumentURI</code> if the
     * SHOW_UNASSIGNED_YN field is set on the underlying display entity.
     * 
     * @param courtRooms
     *            The collection of court rooms.
     * @param display
     *            The display for which to get the array of court rooms,
     *            determines whether to use the unassigned court room.
     * @return The court rooms IDs as an int[].
     * @see uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI
     */
    private static int[] getCourtRoomIds(Collection courtRooms, XhbDisplay display) {
        // Sort the court rooms...
        XhbCourtRoom[] courtRoomArray = new XhbCourtRoom[courtRooms.size()];
        courtRooms.toArray(courtRoomArray);
        Arrays.sort(courtRoomArray, CourtRoomComparator.getInstance());
        int[] courtRoomIds = null;
        if (display.getShowUnassignedYn().equals("Y")) // Add the unassigned
        // courtroom.
        {
            courtRoomIds = new int[courtRoomArray.length + 1];
            // Get the court room IDs.
            for (int i = courtRoomArray.length - 1; i >= 0; i--) {
                courtRoomIds[i] = courtRoomArray[i].getCourtRoomId().intValue();
            }
            // Add the unassigned court room id to the last element of the
            // array.
            courtRoomIds[courtRoomArray.length] = DisplayDocumentURI.UNASSIGNED;
        } else
        // We don't care about the unassigned court room.
        {
            courtRoomIds = new int[courtRoomArray.length];
            // Get the court room IDs.
            for (int i = courtRoomArray.length - 1; i >= 0; i--) {
                courtRoomIds[i] = courtRoomArray[i].getCourtRoomId().intValue();
            }
        }
        return courtRoomIds;
    }

    /**
     * This method gathers and sorts the Display Document elements that go to
     * make up Display Rotation Set, resulting in an array of type
     * <code>RotationSetDisplayDocument<code> that represents the Display Documents
     * in a rotation Set.
     *
     * @param courtId                     The court for to which the <code>RotationSetDisplayDocument<code>s
     *                                    belong.
     * @param rotationSetDisplayDocuments The <code>XhbRotationSetDd</code> entity
     *                                    beans that provide data in the construction of the return.
     * @param courtRoomIds                The court room IDs for which the <code>RotationSetDisplayDocument<code>s
     *                                    are to be constructed.
     * @return A fully instantiated array of <code>RotationSetDisplayDocument<code>
     *         representing all Display Documents that make up a Display Rotation Set.
     */
    private static RotationSetDisplayDocument[] getDisplayRotationSetElements(int courtId,
            Collection rotationSetDisplayDocuments, int[] courtRoomIds) {
        // First sort the rotation set display documents
        XhbRotationSetDd[] rotationSetDDArray = new XhbRotationSetDd[rotationSetDisplayDocuments.size()];
        rotationSetDisplayDocuments.toArray(rotationSetDDArray);
        Arrays.sort(rotationSetDDArray, RotationSetDDComparator.getInstance());

        // Create the List that will store the result.
        ArrayList result = new ArrayList();

        // Step over the RotationSetDisplayDocuments.
        for (int i = 0; i < rotationSetDDArray.length; i++) {
            // Add the one or many RotationSetDisplayDocuments
            result.addAll(getRotationSetDDsForDisplayDocument(courtId, rotationSetDDArray[i].getPageDelay().intValue(),
                    rotationSetDDArray[i].getXhbDisplayDocument(), courtRoomIds));
        }

        // Turn the List into the appropriate array type.
        RotationSetDisplayDocument[] returnArray = new RotationSetDisplayDocument[result.size()];
        result.toArray(returnArray);
        return returnArray;
    }

    /**
     * Synthesise a <code>List</code> of
     * <code>RotationSetDisplayDocument</code> from the passed in parameters.
     * The list will either consist of one entry if the Display Document handles
     * multiple court rooms or as many entries as there are courtRoomIds if the
     * DisplayDocument only handles one court room at a time. It is important to
     * note that for now no URI will be returned for the combination of
     * non-multiple court documents and the unassigned court.
     * 
     * @param courtId
     *            The ID of the court to which the
     *            <code>RotationSetDisplayDocument</code>s belong.
     * @param pageDelay
     *            The time for which a page of the DisplayDocument is to appear
     *            on the public display.
     * @param displayDocument
     *            The Entity Bean representing the Display Document.
     * @param courtRoomIds
     *            The court rooms for which the Rotation Set Display Documents
     *            will be generated.
     * @return A list of <code>RotationSetDisplayDocument<code>s/
     */
    private static List getRotationSetDDsForDisplayDocument(int courtId, int pageDelay,
            XhbDisplayDocument displayDocument, int[] courtRoomIds) {

        Locale documentLocale = createLocale(displayDocument.getLanguage(), displayDocument.getCountry());

        // Get the type of the display document.
        DisplayDocumentType type = DisplayDocumentType.getDisplayDocumentType(displayDocument.getDescriptionCode(),
                displayDocument.getLanguage(), displayDocument.getCountry());

        if (LOG.isDebugEnabled()) {
            LOG.debug("getRotationSetDDsForDisplayDocument:: document type :" + type.toString());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < courtRoomIds.length; i++) {
                sb.append(courtRoomIds[i]);
                sb.append(i < courtRoomIds.length - 1 ? "," : "");
            }
            LOG.debug("getRotationSetDDsForDisplayDocument:: court room ids:" + sb.toString());
        }

        ArrayList results = new ArrayList();

        // Check whether we are dealing with a document that copes with
        // multiple courts.
        if (displayDocument.getMultipleCourtYn().equalsIgnoreCase("Y")) {
            results.add(new RotationSetDisplayDocument(new DisplayDocumentURI(documentLocale, courtId, type,
                    courtRoomIds), pageDelay));
        } else
        // or one that needs to be split into multiple documents dealing each
        // with one court.
        {
            int loopLength = courtRoomIds.length;

            if (loopLength == 0) {
                LOG.fatal("Found display document with no court rooms", new Exception());
            }
            // If we are dealing with a non multiple courts document, then
            // we
            // do not do unassigned cases for now.
            if (loopLength > 0 && courtRoomIds[loopLength - 1] == DisplayDocumentURI.UNASSIGNED) {
                loopLength--;
            }

            for (int i = 0; i < loopLength; i++) {
                results.add(new RotationSetDisplayDocument(new DisplayDocumentURI(documentLocale, courtId, type,
                        new int[] { courtRoomIds[i] }), pageDelay));
            }
        }
        return results;
    }

    private static final Locale createLocale(String language, String country) {
        if (language != null) {
            if (country != null) {
                return new Locale(language, country);
            } else {
                return new Locale(language, "");
            }
        }
        return Locale.getDefault();
    }
}