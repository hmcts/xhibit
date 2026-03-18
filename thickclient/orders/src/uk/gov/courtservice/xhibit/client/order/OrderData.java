/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 9:50:53 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

import org.w3c.dom.Document;

/**
 * OrderData encapsulates the data to be used for a specific order and the
 * accessor/mutator methods. It also provides methods to allow other classes to
 * listen to changes made to it.
 * 
 * @author Neil Ellis & Neil Entwistle
 */

public abstract class OrderData {
    /**
     * The property change support used to implement the property change events.
     */
    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    /**
     * Changes the value of the name/value pair specified by name.
     * 
     * @param name
     *            the name/path of the pair to change.
     * @param value
     *            the value to change to.
     */
    abstract public void setValue(String name, String value);

    /**
     * Returns the value of a name/value pair specified by name.
     * 
     * @param name
     *            the name/path of the name/value pair.
     * @return the value of the name/value pair.
     */
    abstract public Object getValue(String name);

    /**
     * @see PropertyChangeSupport
     * @param s
     * @param propertyChangeListener
     */
    public void addPropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {
        changeSupport.addPropertyChangeListener(s, propertyChangeListener);
    }

    /**
     * Finds the references of the children of a particular reference.
     * 
     * @param name
     *            The reference to the parent data.
     * @return A Vector of names which can be used to reference the children of
     *         the given reference.
     */
    abstract public Vector getChildReferences(String name);

    /**
     * Adds a property change listener.
     * 
     * @see PropertyChangeSupport
     * @param propertyChangeListener
     *            The object which is listening to the OrderData.
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * Removes a property change listener.
     * 
     * @see PropertyChangeSupport
     * @param s
     *            Removes the listener associated with this field.
     * @param propertyChangeListener
     *            The listener to be removed.
     */
    public void removePropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {
        changeSupport.removePropertyChangeListener(s, propertyChangeListener);
    }

    /**
     * @see PropertyChangeSupport
     * @param propertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    abstract public void mergeNarrative(Document narrative);
}
