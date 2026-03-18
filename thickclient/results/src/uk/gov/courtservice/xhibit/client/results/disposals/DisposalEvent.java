package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventObject;

/**
 * <p>
 * Title: DisposalEvent
 * </p>
 * <p>
 * Description: Used to represent a change to a disposal
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
public class DisposalEvent extends EventObject {
    /**
     * True if the disposal is now complete
     */
    private boolean complete;

    /**
     * Represents a change to the disposal editor
     * 
     */
    public DisposalEvent(Object source, boolean complete) {
        super(source);
        this.complete = complete;
    }

    /**
     * Return true if disposal is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Returns a string that displays and identifies this object's properties.
     * 
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() + " [source=" + getSource() + ", complete=" + isComplete() + "]";
    }
}
