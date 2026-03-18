package uk.gov.courtservice.xhibit.client.util;

import java.beans.PropertyChangeListener;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * <p>
 * Title: PropertyChangeSupport
 * </p>
 * <p>
 * Description: This class can be extended to give another class the ability to
 * fire property change events. It allows listeners to be put on to listen for
 * these property change events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author R. Lakhani
 * @version 1.0
 */

public abstract class PropertyChangeHelper {

    private SwingPropertyChangeSupport changeSupport;

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all properties.
     * <p>
     * A PropertyChangeEvent will get fired in response to setting a bound
     * property, such as setFont, setBackground, or setForeground. Note that if
     * the current component is inheriting its foreground, background, or font
     * from its container, then no event will be fired in response to a change
     * in the inherited property.
     * 
     * @param listener
     *            the PropertyChangeListener to be added
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new SwingPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener for a specific property. The listener will
     * be invoked only when a call on firePropertyChange names that specific
     * property.
     * 
     * If listener is null, no exception is thrown and no action is performed.
     * 
     * @param propertyName
     *            the name of the property to listen on
     * @param listener
     *            the PropertyChangeListener to be added
     */
    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new SwingPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list. This removes a
     * PropertyChangeListener that was registered for all properties.
     * 
     * @param listener
     *            the PropertyChangeListener to be removed
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    /**
     * Removes a PropertyChangeListener for a specific property. If listener is
     * null, no exception is thrown and no action is performed.
     * 
     * @param propertyName
     *            the name of the property that was listened on
     * @param listener
     *            the PropertyChangeListener to be removed
     */
    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Supports reporting bound property changes. If oldValue and newValue are
     * not equal and the PropertyChangeEvent listener list isn't empty, then
     * fire a PropertyChange event to each listener. This method has an
     * overloaded method for each primitive type. For example, here's how to
     * write a bound property set method whose value is an int:
     * 
     * <pre>
     * public void setFoo(int newValue) {
     *     int oldValue = foo;
     *     foo = newValue;
     *     firePropertyChange(&quot;foo&quot;, oldValue, newValue);
     * }
     * </pre>
     * 
     * @param propertyName
     *            the programmatic name of the property that was changed
     * @param oldValue
     *            the old value of the property
     * @param newValue
     *            the new value of the property
     * @see java.beans.PropertyChangeSupport
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /*
     * PENDING(hmuller) in JDK1.2 the following firePropertyChange overloads
     * should additional check for a non-empty listener list with
     * changeSupport.hasListeners(propertyName) before calling
     * firePropertyChange.
     */

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Byte(oldValue), new Byte(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Character(oldValue), new Character(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Short(oldValue), new Short(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Long(oldValue), new Long(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Float(oldValue), new Float(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Double(oldValue), new Double(newValue));
        }
    }

    /**
     * Reports a bound property change.
     * 
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if ((changeSupport != null) && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, new Boolean(oldValue), new Boolean(newValue));
        }
    }
}