package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderChoice
 * </p>
 * <p>
 * Description: This class is used to create a multiple-exclusion scope for a
 * set of buttons. Creating a set of buttons with the same ButtonGroup object
 * means that turning "on" one of those buttons turns off all other buttons in
 * the group.
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
public class OrderChoice extends AbstractOrderComponent {
    // ButtonGroup instance
    private ButtonGroup buttonGroup;

    /**
     * Creates a new ButtonGroup and sets it as a VisualComponent for display.
     */
    public void initComponent() {
        buttonGroup = new ButtonGroup();
        setVisualComponent(this);
    }

    /**
     * Adds a button to the ButtonGroup.
     * 
     * @param b
     *            button to add to ButtonGroup
     */
    public void addButton(AbstractButton b) {
        buttonGroup.add(b);
    }

    public ButtonGroup getButtonGroup() {
        return this.buttonGroup;
    }

    /**
     * Method returns false
     * 
     * @return false
     */
    public boolean isLabelled() {
        return false;
    }

}
