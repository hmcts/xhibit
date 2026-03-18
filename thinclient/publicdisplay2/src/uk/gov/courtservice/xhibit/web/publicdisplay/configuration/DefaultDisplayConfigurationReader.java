package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class DefaultDisplayConfigurationReader extends DisplayConfigurationReader {
    private static final HashMap WORKER_INSTANCES = new HashMap();

    // Get the delegate once.
    private static final PDConfigurationControllerBeanBusinessDelegate pdConfigurationController = PDConfigurationControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    private final int[] courtsForPublicDisplay;

    public DefaultDisplayConfigurationReader() {
        System.out.println("DefaultDisplayConfigurationReader : 1");
        courtsForPublicDisplay = pdConfigurationController.getCourtsForPublicDisplay();
        System.out.println("DefaultDisplayConfigurationReader : 2");
        for (int i = courtsForPublicDisplay.length - 1; i > -1; i--) {
            WORKER_INSTANCES.put(new Integer(courtsForPublicDisplay[i]), new DisplayConfigurationWorker(
                    courtsForPublicDisplay[i]));
        }
        System.out.println("DefaultDisplayConfigurationReader : 3");
    }

    /**
     * TODO:
     * 
     * @param change
     *            TODO:
     * 
     * @return TODO:
     */
    public RenderChanges getRenderChanges(CourtConfigurationChange change) {
        DisplayConfigurationWorker worker = (DisplayConfigurationWorker) DefaultDisplayConfigurationReader.WORKER_INSTANCES
                .get(change.getCourtId());
        RenderChanges renderChanges = worker.getRenderChanges(change);
        return renderChanges;
    }

    /**
     * @todo Document me..
     * @param documentTypes
     * @param courtRoom
     * @return
     */
    public RenderChanges getRenderChanges(DisplayDocumentType[] documentTypes, CourtRoomIdentifier courtRoom) {
        DisplayConfigurationWorker worker = (DisplayConfigurationWorker) DefaultDisplayConfigurationReader.WORKER_INSTANCES
                .get(courtRoom.getCourtId());
        if (worker != null)
            return worker.getRenderChanges(documentTypes, courtRoom);
        else
            return new RenderChanges();
    }

    /**
     * Get the configured court IDs.
     * 
     * @return
     */
    public int[] getConfiguredCourtIds() {
        return courtsForPublicDisplay;
    }
}
