package uk.gov.courtservice.xhibit.client.results.disposals;

/**
 * <p>
 * Title: DataComponentAdapter
 * </p>
 * <p>
 * Description: Used to simplify code which is only interested in some data
 * component events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class DataComponentAdapter implements DataComponentListener {
    /**
     * Called when the component data changes
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void dataChanged(DataComponentEvent e) {
        // Default implementation does nothing.
    }

    /**
     * Called when the component is deleted or restored to group 1
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void deletedG1Changed(DataComponentEvent e) {
        // Default implementation does nothing.
    }

    /**
     * Called when the component is deleted or restored to group 2
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void deletedG2Changed(DataComponentEvent e) {
        // Default implementation does nothing.
    }
}