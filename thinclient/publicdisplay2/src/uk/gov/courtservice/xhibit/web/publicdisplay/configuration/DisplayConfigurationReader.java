package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;

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
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bob Boothby
 * @version $Revision: 1.5 $
 */
public abstract class DisplayConfigurationReader {
    private static DisplayConfigurationReader _instance;

    /**
     * Get the singleton instance of DisplayConfigurationReader.
     * 
     * @return The appropriate instance of DisplayConfigurationReader.
     */
    public static synchronized DisplayConfigurationReader getInstance() {
        // This construct provides the ability to recover if the constructor
        // fails.
        System.out.println("DisplayConfigurationReader : 1");
        if (_instance == null)
            _instance = new DefaultDisplayConfigurationReader();
        return _instance;
    }

    /**
     * Returns the <code>Renderable</code>s that need re-rendering after the
     * <code>CourtConfigurationChange</code>.
     * 
     * @param change
     *            The change to the court configuration that has ooccured.
     * 
     * @return The RenderChanges caused by the court configuration changed.
     */
    public abstract RenderChanges getRenderChanges(CourtConfigurationChange change);

    /**
     * Returns the <code>Renderable</code>s that need re-rendering after a
     * subclass of a <code>CourtRoomEvent</code>.
     * 
     * 
     * @return The RenderChanges caused by the event.
     */
    public abstract RenderChanges getRenderChanges(DisplayDocumentType[] displayDocumentTypes,
            CourtRoomIdentifier courtRoomIdentifier);

    /**
     * Get the configured court IDs.
     * 
     * @return
     */
    public abstract int[] getConfiguredCourtIds();

}
