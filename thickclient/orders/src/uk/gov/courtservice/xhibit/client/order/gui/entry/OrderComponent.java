package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.util.Vector;

import javax.swing.JComponent;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

/**
 * <p>
 * Title: OrderComponent
 * </p>
 * <p>
 * Description: OrderComponent interface to enforce desired functionality
 * requirements such as setting the visual component for display, obtaining the
 * component label and initialsing the component.
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

public interface OrderComponent {

    /**
     * Initialises the component
     * 
     * @param adapter
     *            The helper
     * @param isParentAnOrderOption
     *            Flag to show if parent is an Option
     * @param opt
     *            The option
     * @throws OrderComponentException
     */
    public void init(OrderComponentHelper adapter, boolean isParentAnOrderOption, OrderOption opt)
            throws OrderComponentException;

    /**
     * Initialises the component
     * 
     * @param helper
     *            The helper
     * @throws OrderComponentException
     */
    public void init(OrderComponentHelper helper) throws OrderComponentException;

    /**
     * Initialises an contained components
     * 
     * @throws OrderComponentException
     */
    public void initComponent() throws OrderComponentException;

    /**
     * Returns the component displayed on the screen
     * 
     * @return The visual component
     */
    public JComponent getVisualComponent();

    /**
     * Turn validation on or off
     * 
     * @param b
     *            true if validation on
     */
    public void switchValidation(boolean b);

    /**
     * Add a child component
     * 
     * @param oc
     *            The component to add
     */
    public void addChild(OrderComponent oc);

    /**
     * Return the visual leaf component
     * 
     * @return The component
     */
    public JComponent getVisualLeafComponent();

    /**
     * Return all child components of this component
     * 
     * @return The child components
     */
    public Vector getAllChildComponents();

    /**
     * Return the label for the component
     * 
     * @return The component label
     */
    public JComponent getComponentLabel();

    /**
     * Sets the visual component
     * 
     * @param orderComponent
     *            The component
     */
    public void setVisualComponent(JComponent orderComponent);

    /**
     * Return the ComponentHelper
     * 
     * @return The helper
     */
    public OrderComponentHelper getHelper();

    /**
     * Sets the component helper
     * 
     * @param adapter
     *            The helper
     */
    public void setHelper(OrderComponentHelper adapter);

    /**
     * Returns if the component is labelled
     * 
     * @return true if labelled
     */
    public boolean isLabelled();

    /**
     * Sets the parent of the component
     * 
     * @param parent
     *            The parent of the component
     */
    void setComponentParent(OrderComponent parent);
    
    /**
     * Sets whether or not the component is enabled
     * @param enabled
     */
    void setEnabled(boolean enabled);
    

    
    
}