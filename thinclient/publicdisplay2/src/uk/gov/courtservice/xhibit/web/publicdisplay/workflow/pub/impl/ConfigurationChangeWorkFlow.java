package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.impl;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.RenderChanges;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.WorkFlowContext;

/**
 * <p>
 * Title: ConfigurationChangeWorkFlow
 * </p>
 * 
 * <p>
 * Description: The workflow for a configuration change
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
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class ConfigurationChangeWorkFlow extends AbstractBasicWorkFlow {
    /**
     * Creates a new ConfigurationChangeWorkFlow object.
     * 
     * @param context
     *            TODO:
     * @param event
     *            TODO:
     */
    public ConfigurationChangeWorkFlow(WorkFlowContext context, ConfigurationChangeEvent event) {
        super(context);

        CourtConfigurationChange change = event.getChange();

        // Pass the event to the configuration to establish the exact
        // display documents and rotation sets to be re-rendered.
        DisplayConfigurationReader configurationReader = getContext().getDisplayConfigurationReader();
        RenderChanges renderChanges = configurationReader.getRenderChanges(change);
        setRenderChanges(renderChanges);
    }

    /**
     * Configuration change events are handled by this workflow. Gets render
     * changes from the configuration, renders the document changes, then
     * renders the rotations sets.
     * 
     * @param event
     *            Must be a ConfigurationChangeEvent
     * 
     * @throws IllegalArgumentException
     *             Thrown if the event is not a ConfigurationChangeEvent
     */
    public void process() {
        // Render and store display documents.
        processDisplayDocumentChanges();

        // Render and store rotation sets.
        processRotationSetChanges();
    }
}
