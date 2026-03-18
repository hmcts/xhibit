package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventListener;

/**
 * <p>
 * Title: Disposal
 * </p>
 * <p>
 * Description: Used to listen to changes to disposals
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
public interface DisposalListener extends EventListener {
    /**
     * Called when the disposal changes
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void disposalChanged(DisposalEvent e);
}