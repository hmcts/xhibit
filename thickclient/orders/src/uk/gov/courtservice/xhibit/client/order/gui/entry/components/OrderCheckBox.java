package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderCheckBox
 * </p>
 * <p>
 * Description: An implementation of a check box -- an item that can be selected
 * or deselected, and which displays its state to the user.
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

public class OrderCheckBox extends AbstractOrderComponent implements FocusListener {
    private static final Logger log = CSServices.getLogger(OrderCheckBox.class);

    /**
     * Initialises and constructs an OrderCheckBox (an implementation of a
     * JCheckBox), with a boolean checked value obtained from the
     * OrderComponentHelper class.
     */
    public void initComponent() {
        log.debug("$$$ Checkbox value " + getHelper().getValue() + " $$$");
        setVisualComponent(new JCheckBox("", getHelper().getBooleanValue()));
    }

    /**
     * Invoked when a component loses the keyboard focus.
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusLost(FocusEvent event) {
        getHelper().setBooleanValue(((JCheckBox) getVisualComponent()).isSelected());
    }

    public void focusGained(FocusEvent fe) {

    }
}
