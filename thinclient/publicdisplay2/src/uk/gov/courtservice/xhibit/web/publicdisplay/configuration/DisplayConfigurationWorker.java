package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import uk.gov.courtservice.framework.business.exceptions.CourtNotFoundException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtRotationSetConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;

/**
 * <p>
 * Title: Display Configuration Worker.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * A Display Configuration Worker is concerned with managing the configuration
 * on the thin client side of the displays in the court room. It is aware of
 * Displays, Rotation Sets and is responsible for populating
 * <code>RenderChanges</code> instances with details of documents that need to
 * start rendering, stop rendering or be re-rendered in reponse to configuration
 * changes or in response to data changes.
 * <p>
 * This class defers knowledge of Display Documents to an internally held
 * instance of DisplayDocumentReferenceManager that has specialist knowledge of
 * Display Documents.
 * </p>
 * <p>
 * Please note that this class is locked such that according to the semantic of
 * the <code>RWLock</code> class, to ensure the internal consistency of the
 * data.
 * </p>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis, Bob Boothby.
 * @version $Revision: 1.6 $
 * @see uk.gov.courtservice.xhibit.web.publicdisplay.configuration.RWLock
 */
public class DisplayConfigurationWorker {

    // Get the delegate once.
    private static final PDConfigurationControllerBeanBusinessDelegate pdConfigurationController = PDConfigurationControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    // Lock class.
    private RWLock _readWriteLock = new RWLock();

    // The court ID of the instance.
    private int _courtId;

    // Stores current Display Rotation Sets by display ID.
    private HashMap displayRotationSetsByDisplayId = new HashMap();

    // Stores current Display Rotation Sets by rotation set ID.
    private HashMap displayRotationSetsByRotationSetId = new HashMap();

    // Used to store and manage Display Documents.
    private DisplayDocumentReferenceManager displayDocumentReferenceManager = new DisplayDocumentReferenceManager();

    /**
     * Create an instance of DisplayConfigurationWorker for a court.
     * 
     * @param courtId
     *            The ID of the court that this instance is for.
     */
    public DisplayConfigurationWorker(int courtId) {
        _courtId = courtId;
    }

    /**
     * Returns the <code>RenderChanges</code> that need re-rendering after the
     * <code>CourtConfigurationChange</code>.
     * 
     * @param change
     *            The change to the court configuration that has ooccured.
     * 
     * @return The RenderChanges caused by the court configuration changed.
     */
    public RenderChanges getRenderChanges(CourtConfigurationChange change) {
        if (change.getCourtId().intValue() != _courtId)
            throw new RuntimeException("Problem getting render changes fopr a configuration change: "
                    + "This instance is configured for court: " + _courtId + " and not for court: "
                    + change.getCourtId());
        try {
            this._readWriteLock.isWriteLockObtained();
            ConfigurationTuple configurations = new ConfigurationTuple();
            if (change instanceof CourtDisplayConfigurationChange) {
                getDisplayConfigurationChanges(configurations, change);
            } else if (change instanceof CourtRotationSetConfigurationChange) {
                getRotationSetConfigurationChanges(configurations, change);
            } else // treat it as a general CourtConfigurationChange
            {
                getCourtConfigurationChanges(configurations);
            }

            // Work through the old and new configurations working out what
            // has
            // changed.
            RenderChanges renderChanges = processRenderingChanges(configurations, change.isForceRecreate());
            return renderChanges;
        } finally {
            this._readWriteLock.isWriteLockReleased();
        }
    }

    /**
     * Return the set of Display Documents currently held in configuration for
     * the given combination of document types and court room.
     * 
     * @param documentTypes
     *            The different types of Display Document to find.
     * @param courtRoom
     *            The court room for which to find relevant Display Documents.
     * @return A RenderChanges instance containing the relevant Display
     *         Documents.
     */
    public RenderChanges getRenderChanges(DisplayDocumentType[] documentTypes, CourtRoomIdentifier courtRoom) {
        if (courtRoom.getCourtId().intValue() != _courtId)
            throw new RuntimeException("Problem getting render changes for a court room and display documents: "
                    + "This instance is configured for court: " + _courtId + " and not for court: "
                    + courtRoom.getCourtId());
        try {
            _readWriteLock.isReadLockObtained();
            return displayDocumentReferenceManager.getRenderChanges(documentTypes, courtRoom);
        } finally {
            _readWriteLock.isReadLockReleased();
        }
    }

    /**
     * Look up the old and the new configuration data for a display.
     * 
     * @param configurations
     *            ConfigurationTuple to populate with the old and new
     *            configurations.
     * @param change
     *            The display change object.
     */
    private void getDisplayConfigurationChanges(ConfigurationTuple configurations, CourtConfigurationChange change) {
        CourtDisplayConfigurationChange courtDisplayConfigurationChange = (CourtDisplayConfigurationChange) change;

        // Get the DisplayRotationSetData from the middle tier.
        int displayId = courtDisplayConfigurationChange.getDisplayId();

        configurations.newConfigurationData = pdConfigurationController.getUpdatedDisplay(_courtId, displayId);

        // Get old configuration data.
        DisplayRotationSetData displayRotationSetData = (DisplayRotationSetData) displayRotationSetsByDisplayId
                .get(new Long(displayId));

        if (displayRotationSetData != null) {
            configurations.oldConfigurationData = new DisplayRotationSetData[] { displayRotationSetData };
        }
    }

    /**
     * Look up the old and the new configuration data for a rotation set.
     * 
     * @param configurations
     *            ConfigurationTuple to populate with the old and new
     *            configurations.
     * @param change
     *            The Rotation Set change object.
     */
    private void getRotationSetConfigurationChanges(ConfigurationTuple configurations, CourtConfigurationChange change) {
        CourtRotationSetConfigurationChange courtRotationSetConfigurationChange = (CourtRotationSetConfigurationChange) change;

        // Get the DisplayRotationSetData from the middle tier.
        int rotationSetId = courtRotationSetConfigurationChange.getRotationSetId();
        configurations.newConfigurationData = pdConfigurationController.getUpdatedRotationSet(_courtId, rotationSetId);

        // Get old configuration data.
        DisplayRotationSetData[] displayRotationSetData = (DisplayRotationSetData[]) getRotationSetDisplayRotationSets(
                rotationSetId).toArray(new DisplayRotationSetData[0]);

        if (displayRotationSetData != null) {
            configurations.oldConfigurationData = displayRotationSetData;
        }
    }

    /**
     * Look up the old and the new configuration data for a court.
     * 
     * @param configurations
     *            ConfigurationTuple to populate with the old and new
     *            configurations.
     * @throws CourtNotFoundException
     *             When the court is not found in the middle tier.
     */
    private void getCourtConfigurationChanges(ConfigurationTuple configurations) throws CourtNotFoundException {
        // Get the DisplayRotationSetData from the middle tier.
        configurations.newConfigurationData = pdConfigurationController.getCourtConfiguration(_courtId);

        // Get old configuration data.
        Collection displayRotationSets = displayRotationSetsByDisplayId.values();

        configurations.oldConfigurationData = (DisplayRotationSetData[]) displayRotationSets
                .toArray(configurations.oldConfigurationData);
    }

    /**
     * This method takes the old and new configurations, compares them, stores
     * the changes and returns a digest of render changes.
     * 
     * @param configurationChange
     *            The old and new configurations.
     * @param forceRecreate
     *            Whether to return all documents for rendering or only those
     *            that need to start and stop rendering.
     * @return An instance of RenderChanges populated with everything that needs
     *         rendering.
     */
    private RenderChanges processRenderingChanges(ConfigurationTuple configurationChange, boolean forceRecreate) {
        // Sort the changes.
        configurationChange.sort();

        // Simplify reference to the two arrays.
        DisplayRotationSetData[] oldConfig = configurationChange.oldConfigurationData;
        DisplayRotationSetData[] newConfig = configurationChange.newConfigurationData;

        // Get the comparator now, because we WILL be using it extensively.
        DisplayRotationSetDataByDisplayComparator comparator = DisplayRotationSetDataByDisplayComparator.getInstance();

        // Set up variables for stepping through arrays
        int oldConfigIndex = 0; // position in the old configuration array.
        int newConfigIndex = 0; // position in the new configuration array.
        int oldConfigLength = oldConfig.length;
        int newConfigLength = newConfig.length;

        RenderChanges renderChanges = new RenderChanges();
        // Now step through the arrays in parallel.
        // This kind of remove is a ladder remove as we work our way through
        // The sorted arrays.
        while (oldConfigIndex < oldConfigLength && newConfigIndex < newConfigLength) {
            // We check at the current indexes in the configuration arrays
            // the
            // relationship between the display ids for the
            // DisplayRotationSetData.
            int comparison = comparator.compare(oldConfig[oldConfigIndex], newConfig[newConfigIndex]);
            if (comparison > 0)
            // The DisplayRotationSetData in the old configuration has a
            // display ID greater than that of the one in the new
            // configuration, this means that a display has been added.
            {
                // Scenarios:
                // * When a display has been added to the court.
                // * At first time of initialisation.
                addDisplayRotationSetData(renderChanges, newConfig[newConfigIndex], forceRecreate);

                // Step up to next new display configuration data.
                newConfigIndex++;
            } else if (comparison < 0)
            // the DisplayRotationSetData in the old configuration has
            // a display ID less than that of the one in the new
            // configuration.
            // This means that a display has been removed.
            {
                // Scenarios:
                // * When a display has been removed from a court.
                // * In event of a failure, when a display has changed it's
                // rotation set but the CourtDisplayChangeEvent has not
                // occurred. For now we will remove it completely.
                removeDisplayRotationSetData(renderChanges, oldConfig[oldConfigIndex], true);
                // Step up to next old display configuration data.
                oldConfigIndex++;
            } else if (comparison == 0)
            // The DisplayRotationSetData in the old configuration is for
            // the same display as the one in the new configuration.
            // This means that the display may have been updated.
            {
                // Scenarios:
                // * When a Display has been altered.
                // * When a Rotation Set has been altered.
                replaceDisplayRotationSetData(renderChanges, oldConfig[oldConfigIndex], newConfig[newConfigIndex],
                        forceRecreate);

                // Step up to next new and old display configuration data.
                oldConfigIndex++;
                newConfigIndex++;
            }
        }

        // Do mopping up here of remaining uncontested configuration data.
        // First remove displays that are not in the current configuration.
        for (int i = oldConfigIndex; i < oldConfigLength; i++) {
            removeDisplayRotationSetData(renderChanges, oldConfig[i], true);
        }
        // Next add displays that are new.
        for (int i = newConfigIndex; i < newConfigLength; i++) {
            addDisplayRotationSetData(renderChanges, newConfig[i], forceRecreate);
        }

        // Populate the RenderChanges with the accumulated information about
        // which display documents need re-rendering.
        displayDocumentReferenceManager.fillInRenderChanges(renderChanges);

        return renderChanges;
    }

    /**
     * This method updates the cached configuration data by adding an old
     * Display Rotation Set and populates the RenderChanges in order to start
     * rendering of the Display Rotation Set.
     * 
     * @param renderChanges
     *            The instance of <code>RenderChanges</code> to populate.
     * @param displayRotationSetData
     *            The Display Rotation Set to add.
     * @param forceRecreate
     *            Whether all the documents in the new Display Rotation Set
     *            should be rendered regardless of whether they have been
     *            rendered before.
     */
    private void addDisplayRotationSetData(RenderChanges renderChanges, DisplayRotationSetData displayRotationSetData,
            boolean forceRecreate) {
        displayRotationSetsByDisplayId.put(new Long(displayRotationSetData.getDisplayId()), displayRotationSetData);
        getRotationSetDisplayRotationSets(displayRotationSetData.getRotationSetId()).add(displayRotationSetData);
        renderChanges.addStartRotationSet(displayRotationSetData);
        if (forceRecreate) {
            addAllDisplayDocumentsToRenderChanges(renderChanges, displayRotationSetData);
        }
        // Add Display Document References.
        displayDocumentReferenceManager.addDisplayDocumentReferences(displayRotationSetData);
    }

    /**
     * This method updates the cached configuration data by removing an old
     * Display Rotation Set and populates the RenderChanges in order to finish
     * rendering of the Display Rotation Set.
     * 
     * @param renderChanges
     *            The instance of <code>RenderChanges</code> to populate.
     * @param displayRotationSetData
     *            The Display Rotation Set to remove.
     * @param removeDisplayRotationSet
     *            Whether to stop rendering the display rotation set. Only to be
     *            used in scenarios where the display has been remove from the
     *            configuration.
     */
    private void removeDisplayRotationSetData(RenderChanges renderChanges,
            DisplayRotationSetData displayRotationSetData, boolean removeDisplayRotationSet) {
        displayRotationSetsByDisplayId.remove(new Long(displayRotationSetData.getDisplayId()));
        getRotationSetDisplayRotationSets(displayRotationSetData.getRotationSetId()).remove(displayRotationSetData);
        // Remove Display Document References.
        displayDocumentReferenceManager.removeDisplayDocumentReferences(displayRotationSetData);
        if (removeDisplayRotationSet)
            renderChanges.addStopRotationSet(displayRotationSetData);
    }

    /**
     * This method intelligently handles the replacement of a Display Rotation
     * Set with a new one and copes with the force remove flag.
     * 
     * @param renderChanges
     *            The instance of <code>RenderChanges</code> to populate.
     * @param oldDisplayRotationSetData
     *            The old Display Rotation Set.
     * @param newDisplayRotationSetData
     *            The new Display Rotation Set.
     * @param forceRecreate
     *            Whether all the documents in the new Display Rotation Set
     *            should be rendered regardless of whether they have been
     *            rendered before.
     */
    private void replaceDisplayRotationSetData(RenderChanges renderChanges,
            DisplayRotationSetData oldDisplayRotationSetData, DisplayRotationSetData newDisplayRotationSetData,
            boolean forceRecreate) {
        if (!oldDisplayRotationSetData.equals(newDisplayRotationSetData))
        // There has been a change.
        {
            removeDisplayRotationSetData(renderChanges, oldDisplayRotationSetData, false);
            addDisplayRotationSetData(renderChanges, newDisplayRotationSetData, forceRecreate);
        } else if (forceRecreate) // No change but we must re-render.
        {
            addDisplayRotationSetData(renderChanges, newDisplayRotationSetData, forceRecreate);
        }
    }

    /**
     * Used as part of force remove to add all display documents in a Display
     * Rotatoin Set to the set of documents to render regardless of whether they
     * are currently rendered.
     * 
     * @param renderChanges
     *            The instance of <code>RenderChanges</code> to populate.
     * @param displayRotationSetData
     *            The Display Rotation Set whose Display Documents <i>must</i>
     *            be rendered.
     */
    private void addAllDisplayDocumentsToRenderChanges(RenderChanges renderChanges,
            DisplayRotationSetData displayRotationSetData) {
        RotationSetDisplayDocument[] rsDDs = displayRotationSetData.getRotationSetDisplayDocuments();
        for (int i = rsDDs.length - 1; i >= 0; i--) {
            renderChanges.addStartDocument(rsDDs[i].getDisplayDocumentURI());
        }
    }

    /**
     * This method is written to encompass the logic around retrieving the set
     * of Display Rotation Sets for a given Rotation Set.
     * 
     * @param rotationSetId
     *            The ID of the Rotation Set.
     * @return A Set of types <code>DisplayRotationSetData</code> representing
     *         all the Display Rotation Sets associated with the Rotation Set.
     */
    private Set getRotationSetDisplayRotationSets(long rotationSetId) {
        Long key = new Long(rotationSetId);
        Set rotationSetDisplayRotationSets = (Set) displayRotationSetsByRotationSetId.get(key);
        // Check that we have already got information about the rotation set.
        if (rotationSetDisplayRotationSets == null) {
            // Create a new Set for the rotation set.
            rotationSetDisplayRotationSets = new HashSet();
            displayRotationSetsByRotationSetId.put(key, rotationSetDisplayRotationSets);
        }
        return rotationSetDisplayRotationSets;
    }

    /**
     * <p>
     * Title: Configuration Data Tuple
     * </p>
     * <p>
     * Description:
     * </p>
     * <p>
     * This class is used to encapsulate the old and new configuration data for
     * a given court, display or rotation set and provides the ability to sort
     * it by display ID.
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
    private class ConfigurationTuple {
        private DisplayRotationSetData[] oldConfigurationData = new DisplayRotationSetData[0];

        private DisplayRotationSetData[] newConfigurationData = new DisplayRotationSetData[0];

        /**
         * Sort the encapsulated old and new configuration data by display ID.
         */
        private void sort() {
            Arrays.sort(oldConfigurationData, DisplayRotationSetDataByDisplayComparator.getInstance());
            Arrays.sort(newConfigurationData, DisplayRotationSetDataByDisplayComparator.getInstance());
        }
    }
}
