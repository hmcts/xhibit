package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventObject;

/**
 * <p>
 * Title: InsertComponentEvent
 * </p>
 * <p>
 * Description: Notified when the insert component is changed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 */

public class InsertComponentEvent extends EventObject {

    /**
     * Represents a change to the number of lines in the insert component
     * 
     * @param oldLineCount
     *            The oldLineCount
     * @param newLineCount
     *            The newLineCount
     */
    public InsertComponentEvent(Object source) {
        super(source);
    }

    /**
     * Returns a string that displays and identifies this object's properties.
     * 
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() + " [source=" + getSource() + "]";
    }
}
