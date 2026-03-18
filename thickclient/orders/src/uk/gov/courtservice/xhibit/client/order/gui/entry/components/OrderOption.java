package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.MalformedOrderDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;

/**
 * <p>
 * Title: OrderOption
 * </p>
 * <p>
 * Description: Class to construct an OrderPanel containing single or multiple
 * radio buttons.
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
public class OrderOption extends AbstractOrderComponent implements ItemListener, MouseListener {
    private static final Logger log = CSServices.getLogger(OrderOption.class);

    private OrderPanel panel;

    private MultiLineRadioButton buttonRadio;

    private GridBagConstraints constraints;

    private Vector childComponents = new Vector();

    private boolean componentInitialised = false;

    /**
     * Constructor
     */
    public OrderOption() {
        this.constraints = getDefaultConstraints();
    }

    /**
     * Create radiobuttons and radiobutton labels - organise in a GridBagLayout
     * 
     * @throws OrderComponentException
     */
    public void initComponent() throws OrderComponentException {
        boolean selected = getHelper().getAttributeAsBoolean("selected");
        constraints.gridx = 0;
        constraints.gridy = 0;
        // Assign label name to a string linked to xml attribute
        buttonRadio = new MultiLineRadioButton(getHelper().getAttribute("label"));

        buttonRadio.setSelected(selected);
        add(buttonRadio, constraints);
        panel = new OrderPanel();
        panel.setEnabled(selected);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(panel, constraints);
        buttonRadio.addItemListener(this);
        buttonRadio.addMouseListener(this);
        this.validate();
        setVisualComponent(this);
        componentInitialised = true;
    }

    /**
     * Set the parent component for this component
     * 
     * @param componentParent
     *            the parent component
     */
    public void setComponentParent(OrderComponent componentParent) {
        super.setComponentParent(componentParent);
        if (componentParent instanceof OrderChoice) {
            ((OrderChoice) getComponentParent()).addButton(buttonRadio);
            OrderComponent parent = this.getComponentParent();
            OrderComponentHelper parentHelper = parent.getHelper();
            log.debug("Parent Radio Button refererence :" + parentHelper.getOrderDataReference());
            if (parentHelper.getOrderDataReference().equals("/")) {
                // Put some code in to check whether selected attribute
                // exists or whether we are using an
                // xsd:boolean and need to check the element value.
                log.debug("Child Radio Button item :" + this.getHelper().getAttribute("name"));
                log.debug("Radio Button item :" + this.getHelper().getValue());
                buttonRadio.setSelected(Boolean.valueOf(this.getHelper().getValue()).booleanValue());
                log.debug("Button selected state: " + buttonRadio.isSelected());
            } else {
                setRadioButtons(parentHelper);
            }

        } else {
            throw new OrderComponentException("Parent of an OrderOption is " + "not an OrderChoice. It is a '"
                    + componentParent.getClass().getName() + "'.");
        }
    }

    /**
     * Enable or disable the radio buttons depending on the value retrieved from
     * the parent helper
     * 
     * @param parentHelper
     *            the helper
     */
    protected void setRadioButtons(OrderComponentHelper parentHelper) {
        String rbRef = null;
        try {
            rbRef = parentHelper.getValue();
            String myValue = this.getHelper().getAttribute("value");
            if (myValue != null && rbRef != null && rbRef.equals(myValue)) {
                log.debug("Radio Button group :" + rbRef);
                buttonRadio.setSelected(true);
            } else {
                log.debug("Not Radio Button group :" + myValue);
            }
        } catch (MalformedOrderDataException ex) {
            // A remand order will throw this exception.
            // This section is just to reset the radio buttons, so we can
            // ignore this - for the time being...
            ex.printStackTrace();
            log.debug("$$$ MalformedOrderDataException " + ex.getMessage());
        }
    }

    /**
     * Return all child components of this component.
     * 
     * @return the child components
     */
    public Vector getChildComponents() {
        return this.childComponents;
    }

    /**
     * return the JRadioButton within this component.
     * 
     * @return JRadioButton contained within this component.
     */
    public JRadioButton getButtonRadio() {
        return this.buttonRadio;
    }

    /**
     * Return the visual part of this component.
     * 
     * @return The visual JComponent.
     */
    public JComponent getVisualLeafComponent() {
        return panel;
    }

    /**
     * Get the panel containing the radio buttons.
     * 
     * @return the panel
     */
    public OrderPanel getPanel() {
        return panel;
    }

    /**
     * Return if the component has a label associated with it.
     * 
     * @return false.
     */
    public boolean isLabelled() {
        return false;
    }

    /**
     * Check state of radiobutton which is linked to xml attribute "value"
     */
    public void itemStateChanged(ItemEvent e) {
        boolean selected = e.getStateChange() == ItemEvent.SELECTED;
        panel.setEnabled(selected);
        // this.setValidation(selected);

        if (getHelper().getOrderDataReference() != null)// isBoolean)
        {
            getHelper().setBooleanValue(selected);
        } else if (selected) {
            setItemValue();
        }
    }

    /**
     * Sets the value on the DOM according what to held on the "value" attribute
     * 
     * @throws OrderComponentException
     */
    protected void setItemValue() throws OrderComponentException {
        String value = getHelper().getAttribute("value");
        if (value != null) {
            getComponentParent().getHelper().setValue(value);
        } else {
            throw new OrderComponentException("No reference supplied for "
                    + "an OrderOption and no value specified either one " + "or the other must be specified.");
        }
    }

    /**
     * Enables/disables the various components within the widget
     * 
     * @param validationOn
     *            true if components are to be enabled
     */
    public void switchValidation(boolean validationOn) {
        this.setValidation(validationOn);
    }

    /**
     * 
     * @param event
     */
    public void mousePressed(MouseEvent event) {

        // Get all child components of this component
        Enumeration enum1 = this.childComponents.elements();
        while (enum1.hasMoreElements()) {
            log.debug(enum1.nextElement().getClass().getName());
        }

        // Get the button group containing this component.
        ButtonGroup group = ((OrderChoice) event.getComponent().getParent().getParent()).getButtonGroup();
        // Vector to hold the selected button - this should only be one button.
        Vector buttonCacheSelected = new Vector();
        // Vector to hold the unselected buttons - may be several.
        Vector buttonCacheUnSelected = new Vector();
        Enumeration enumeration = group.getElements();

        addButtonsToCache(buttonCacheSelected, buttonCacheUnSelected, enumeration);

        Enumeration e_1 = buttonCacheSelected.elements();
        Enumeration e_2 = buttonCacheUnSelected.elements();

        validateSelectedButtons(e_1);

        validateUnSelectedButtons(e_2);
    }

    /**
     * Add buttons to selected or unselected caches
     * 
     * @param buttonCacheSelected
     *            vector of selected buttons
     * @param buttonCacheUnSelected
     *            vector of unselected buttons
     * @param enum
     *            enumeration of all buttons
     */
    private void addButtonsToCache(Vector buttonCacheSelected, Vector buttonCacheUnSelected, Enumeration enumeration) {
        // Iterate over all buttons within the button group and add them to the
        // appropriate cache.
        while (enumeration.hasMoreElements()) {
            JRadioButton b = (JRadioButton) enumeration.nextElement();
            if (b.isSelected()) {
                buttonCacheSelected.add(b);
            } else {
                buttonCacheUnSelected.add(b);
            }
        }
    }

    /**
     * Enable or disable the selected buttons
     * 
     * @param e_1
     *            enumeration of selected buttons
     */
    private void validateSelectedButtons(Enumeration e_1) {
        // disable all sub components of the unselected buttons
        while (e_1.hasMoreElements()) {
            OrderPanel p = ((OrderOption) ((JRadioButton) e_1.nextElement()).getParent()).getPanel();
            p.setValidation(false);
        }
    }

    /**
     * Enable or disable the unselected buttons
     * 
     * @param e_2
     *            enumeration of unselected buttons
     */
    private void validateUnSelectedButtons(Enumeration e_2) {
        // enable all sub components of the selected buttons
        while (e_2.hasMoreElements()) {
            OrderPanel p = ((OrderOption) ((JRadioButton) e_2.nextElement()).getParent()).getPanel();
            p.setValidation(true);
        }
    }

    /**
     * Empty implementation of mouseClicked method
     * 
     * @param event
     */
    public void mouseClicked(MouseEvent event) {
        // No implementation
    }

    /**
     * Empty implementation of mouseReleased method
     * 
     * @param event
     */
    public void mouseReleased(MouseEvent event) {
        // No implementation
    }

    /**
     * Empty implementation of mouseEntered method
     * 
     * @param event
     */
    public void mouseEntered(MouseEvent event) {
        // No implementation
    }

    /**
     * Empty implementation of mouseExited method
     * 
     * @param event
     */
    public void mouseExited(MouseEvent event) {
        // No implementation
    }

    /**
     * Enables/disables the option panel depending on the options selected
     * 
     * @param enable
     *            true to enable otherwise false
     */
    public void setEnabled(boolean enable) {
        if (componentInitialised) {
            this.buttonRadio.setEnabled(enable);
            panel.setEnabled(enable && this.buttonRadio.isSelected());
        }
    }

    /**
     * If this class is extended, this allows the component parent to be set
     * without creating the default radio buttons.
     * 
     * @see OrderYesNoOption
     * @param componentParent
     *            The parent class of the option
     */
    protected void setSubclassComponentParent(OrderComponent componentParent) {
        super.setComponentParent(componentParent);
    }

    public class MultiLineRadioButton extends JRadioButton {
    	private static final long serialVersionUID = 1L;
		private String originalText;
		
		public MultiLineRadioButton(String text) {
    		super(text);
    		originalText = text;
    		setMultilineText();
    	}
    	
    	@Override
    	public void setEnabled(boolean b) {
    		super.setEnabled(b);
    		setMultilineText();
    	}

    	private void setMultilineText() {
    		if (isMultilineText(originalText)) {
    			this.setText(addLineBreaks(originalText, isEnabled()));
    		}
    	}
    }
}
