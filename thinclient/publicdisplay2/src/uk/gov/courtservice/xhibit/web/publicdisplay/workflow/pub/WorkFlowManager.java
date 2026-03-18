package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.exceptions.UnrecognizedEventException;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.impl.DefaultWorkFlowManager;

/**
 * <p>
 * Title: The entry point into the Workflow component.
 * </p>
 * 
 * <p>
 * Description: The WorkFlowManager provides the functionality to remove
 * <code>PublicDisplayEvent</code>
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
public abstract class WorkFlowManager {
    private WorkFlowContext context;

    /**
     * Creates a new WorkFlowManager object.
     * 
     * @param context
     *            TODO:
     */
    public WorkFlowManager(WorkFlowContext context) {
        this.context = context;
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public WorkFlowContext getContext() {
        return context;
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public static WorkFlowManager getInstance(WorkFlowContext context) {
        return new DefaultWorkFlowManager(context);
    }

    /**
     * Process all the supplied workflows for the event that has occured.
     * 
     * @param event
     *            the event that occured.
     */
    public void process(PublicDisplayEvent event) {
        WorkFlow[] flows = getWorkFlowsForEvent(event);

        for (int i = 0; i < flows.length; i++) {
            WorkFlow flow = flows[i];
            flow.process();
        }
    }

    /**
     * Get a list of workflows for the event that occured.
     * 
     * @param event
     *            the event.
     * 
     * @return an array of WorkFlow-s to remove.
     * 
     * @throws UnrecognizedEventException
     *             if the event is not recognized.
     */
    protected abstract WorkFlow[] getWorkFlowsForEvent(PublicDisplayEvent event);
}
