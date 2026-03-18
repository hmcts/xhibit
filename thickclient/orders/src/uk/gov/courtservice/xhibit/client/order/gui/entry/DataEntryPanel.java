package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.awt.GridBagConstraints;
import java.util.Hashtable;

import javax.swing.JComponent;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;

/**
 * <p>
 * Title: DataEntryPanel
 * </p>
 * <p>
 * Description: Panel containing all components specified by the data template
 * xml. The components are placed using the GridBagLayout.
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

public class DataEntryPanel extends OrderPanel {
    // Collection of all components to be displayed.
    private Hashtable componentCache = new Hashtable();

    // Contraints to fine tune layout of component.
    private GridBagConstraints constraints;

    /**
     * Creates a DataEntryPanel and initialises the constraints to the default
     * specified by OrderPanel super class.
     */
    public DataEntryPanel() {
        super();
        this.constraints = getDefaultConstraints();
        componentCache.clear();
    }

    public void addComponent(OrderComponent oc) throws OrderComponentException {
        addComponent(this, oc);
    }

    public void addComponent(OrderComponent parent, OrderComponent oc) throws OrderComponentException {
        parent.addChild(oc);
        addComponent(parent.getVisualLeafComponent(), oc);
        oc.setComponentParent(parent);
    }

    /**
     * Adds the visual component to the DataEntryPanel. The default constraints
     * are modified differently depending on whether the visual component is
     * labelled or not.
     * 
     * @param jc
     *            Visual Leaf Component
     * @param oc
     *            OrderComponent
     * @throws OrderComponentException
     *             Exception thrown for invalid components.
     */
    private void addComponent(JComponent jc, OrderComponent oc) throws OrderComponentException {
        constraints.gridy++;

        // Labelled entry makes use of two columns, unlabelled span 2
        if (oc.isLabelled()) {
            constraints.gridx = 0;
            constraints.gridwidth = 1;
            constraints.anchor = constraints.NORTHWEST;
            jc.add(oc.getComponentLabel(), constraints);
            constraints.gridx = 1;
            constraints.weightx = 100.0;
            constraints.fill = GridBagConstraints.NONE;
            jc.add(oc.getVisualComponent(), constraints);
            constraints.fill = GridBagConstraints.BOTH;
        } else {
            constraints.weightx = 0.5;
            constraints.gridwidth = 2;
            constraints.gridx = 0;
            constraints.anchor = constraints.WEST;
            jc.add(oc.getVisualComponent(), constraints);
            constraints.gridwidth = 1;
        }

        String name = oc.getHelper().getAttribute("name");

        if (name == null)
            throw new OrderComponentException(
                    "Attempted to add a component without a name, component names are compulsory.");

        componentCache.put(name, oc);

    }

    /**
     * Get component from cache by name.
     * 
     * @param name
     *            name of component instance .
     * @return OrderComponent.
     */
    public OrderComponent getComponent(String name) {
        return (OrderComponent) componentCache.get(name);
    }

    /**
     * Returns the DataEntryPanel itself.
     * 
     * @return DataEntryPanel.
     */
    public JComponent getRootContainer() {
        return this;
    }

}
