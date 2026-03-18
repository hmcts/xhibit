package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.EventObject;

/**
 * <p>
 * Title: DataComponentEvent
 * </p>
 * <p>
 * Description: Used to update line counts
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
public class DataComponentEvent extends EventObject {
    /**
     * Represents a change to the data
     * 
     */
    public DataComponentEvent(DataComponent source) {
        super(source);
    }

    /**
     * Get data component which caused event
     */
    public DataComponent getDataComponent() {
        return (DataComponent) getSource();
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
