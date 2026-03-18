package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventListener;

/**
 * <p>
 * Title: DataComponentListener
 * </p>
 * <p>
 * Description: Used to render line datas
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public interface DataComponentListener extends EventListener {
    /**
     * Called when the component data changes
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void dataChanged(DataComponentEvent e);

    /**
     * Called when the component is deleted or restored to group 1
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void deletedG1Changed(DataComponentEvent e);

    /**
     * Called when the component is deleted or restored to group 2
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void deletedG2Changed(DataComponentEvent e);
}