package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventListener;

/**
 * <p>
 * Title: InsertComponent
 * </p>
 * <p>
 * Description: Used to render line inserts
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
public interface InsertComponentListener extends EventListener {
    /**
     * Called when the data in the insert component changes
     * 
     * @param e
     *            the event that characterizes the change.
     */
    public void insertChanged(InsertComponentEvent e);

}