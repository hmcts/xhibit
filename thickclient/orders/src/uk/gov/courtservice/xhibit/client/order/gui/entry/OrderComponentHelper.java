package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDate;

/**
 * <p>
 * Title: OrderComponentHelper
 * </p>
 * <p>
 * Description: Helper class to enable read and write access to name/value
 * fields obtained from xml data file.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class OrderComponentHelper {
    
    private static final Logger log = CSServices.getLogger(OrderComponentHelper.class);
    // contains all order data from xml.
    private OrderData data;

    // unique reference to a specific name.
    private String orderDataReference;

    // collection of all name/value pairs.
    private Hashtable attributes = new Hashtable();

    /**
     * Constructs an OrderComponentHelper.
     * 
     * @param orderData
     *            class encapsulating the data to be used for a specific order.
     * @param ref
     *            unique reference for a given data set.
     * @param xmlAttributes
     *            The attributes associated with the order
     */
    public OrderComponentHelper(OrderData orderData, String ref, Attributes xmlAttributes) {
        data = orderData;
        orderDataReference = ref;
        // The Attribute object is reused so we must copy.
        int length = xmlAttributes.getLength();
        for (int i = 0; i < length; i++) {
            setAttribute(xmlAttributes.getLocalName(i), xmlAttributes.getValue(i));
        }

    }

    /**
     * Return the xpath reference
     * 
     * @return the reference
     */
    public String getOrderDataReference() {
        return this.orderDataReference;
    }

    /**
     * Returns attribute specified by name.
     * 
     * @param name
     *            name of attribute.
     * @return the value of the name/value pair.
     */
    public String getAttribute(String name) {
        return (String) attributes.get(name);
    }

    /**
     * Returns attribute specified by name if not available use the defualt.
     * 
     * @param name
     *            name of attribute.
     * @param defaultValue
     *            Value to use if no attribute supplied
     * @return the value of the name/value pair.
     */
    public String getAttribute(String name, String defaultValue) {
        String attribute = getAttribute(name);
        if (attribute == null) {
            return defaultValue;
        } else {
            return attribute;
        }
    }

    /**
     * Return all child references for this component
     * 
     * @return the references
     */
    public Vector getChildReferences() {
        return data.getChildReferences(this.orderDataReference);
    }

    /**
     * Returns integer value of attribute specified by name.
     * 
     * @param name
     *            name of attribute.
     * @return the value of the name/value pair.
     */
    public int getAttributeAsInt(String name) {
        String s = (String) attributes.get(name);
        if (s == null) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    /**
     * Returns boolean value of attribute specified by name.
     * 
     * @param name
     *            name of attribute.
     * @return the value of the name/value pair.
     */
    public boolean getAttributeAsBoolean(String name) {
        String s = (String) attributes.get(name);
        if (s == null) {
            return false;
        }
        return s.equals("true");
    }

    /**
     * Returns the value of a name/value pair specified by name.
     * 
     * @return the value of the name/value pair.
     */
    public String getValue() {
        if (orderDataReference == null) {
            log.error("GetValue() returns null.  No reference to order data was created.");
            throw new NullPointerException("No reference to order data was created.");
        }
        return (String) data.getValue(orderDataReference);
    }

    /**
     * Returns the value specified by the xpath reference
     * 
     * @param xpath
     *            the reference
     * @return the value
     */
    public String getValue(String xpath) {
        if (xpath == null) {
            log.error("GetValue() returns null.  No reference to order data was created.");
            throw new NullPointerException("No reference to order data was created.");
        }
        return (String) data.getValue(xpath);
    }

    /**
     * Changes the value of the name/value pair specified by name.
     * 
     * @param name
     *            the name
     */
    public void setValue(String name) {
        if (orderDataReference == null) {
            throw new NullPointerException("No reference to order data was created.");
        }
        data.setValue(orderDataReference, name);
    }

    /**
     * Changes the value of the name/value pair specified by name/value.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setValues(String name, String value) {
        if (name == null) {
            throw new NullPointerException("No reference to order data was created.");
        }
        data.setValue(name, value);
    }

    /**
     * Accepts a boolean value and sets the corresponding "true" or "false"
     * string attribute.
     * 
     * @param bool
     *            boolean value
     */
    public void setBooleanValue(boolean bool) {
        setValue(bool ? "true" : "false");
    }

    /**
     * returns the current boolean value attribute
     * 
     * @return returns true
     */
    public boolean getBooleanValue() {
        return true;
    }

    /**
     * Adds new name/value pairs to the HashTable of attributes.
     * 
     * @param localName
     *            name of new attribute
     * @param value
     *            value of new attribute
     */
    private void setAttribute(String localName, String value) {
        attributes.put(localName, value);
    }
}