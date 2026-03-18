package uk.gov.courtservice.xhibit.client.misc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * <p>
 * Title: AbstractOrderStatus
 * </p>
 * <p>
 * Description: Temporary order status class...to be replaced with correct value
 * object.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public abstract class AbstractOrderStatus {

    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    abstract public void setStatus(int i);

    abstract public boolean isSaved();

    /**
     * @param s
     * @param propertyChangeListener
     * @see java.beans.PropertyChangeSupport
     */
    public void addPropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {
        changeSupport.addPropertyChangeListener(s, propertyChangeListener);
    }

    /**
     * Adds a property change listener to the OrderStatus
     * 
     * @param propertyChangeListener
     *            The PropertyChangeListener to use.
     * @see java.beans.PropertyChangeSupport
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * @param s
     * @param propertyChangeListener
     * @see java.beans.PropertyChangeSupport
     */
    public void removePropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {
        changeSupport.removePropertyChangeListener(s, propertyChangeListener);
    }

    /**
     * @param propertyChangeListener
     * @see java.beans.PropertyChangeSupport
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.removePropertyChangeListener(propertyChangeListener);
    }

}
