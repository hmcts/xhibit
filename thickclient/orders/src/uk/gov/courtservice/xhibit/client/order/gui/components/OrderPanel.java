package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderCurrencyPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDate;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDurationComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderXhibitComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;

/**
 * <p>
 * Title: OrderPanel
 * </p>
 * <p>
 * Description: OrderPanel for displaying order details.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */
public class OrderPanel extends XPanel {

    private GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);

    private Calendar c = Calendar.getInstance();

    private LayoutManager layout;

    /**
     * Return the default GridBagConstraints
     * 
     * @return A default set of GridBagConstraints to simplify population.
     */
    protected GridBagConstraints getDefaultConstraints() {
        return (GridBagConstraints) constraints.clone();
    }

    /**
     * Constructs an OrderPanel with GridBagLayout using all GridBagConstraints
     * specified by XHIBIT2 spec.
     */
    public OrderPanel() {
        layout = new GridBagLayout();
        setLayout(layout);
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        // No implementation
    }

    /**
     * Framework method
     * 
     * @param b
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean b) throws CSRecoverableException {
        // No implementation
    }

    /**
     * Sets whether or not this component is enabled. Also iterates through
     * child components and sets their enabled property.
     * 
     * @param enabled
     *            to enable component.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
   
        Component[] components = getComponents();
        int max = components.length;

        for (int i = 0; i < max; i++) {
            if (components[i] instanceof XTimePanel) {
                ((XTimePanel) components[i]).getTimeComponent().setEnabled(enabled);
            } else if (components[i] instanceof XDatePanel) {
                ((XDatePanel) components[i]).getDateComponent().setEnabled(enabled);
            }
            else if(components[i] instanceof OrderXhibitComboBox)
			{
            	 ((OrderXhibitComboBox) components[i]).setEnabled(enabled);
			}
            else if(components[i] instanceof CustomComboBox)
			{
            	 ((CustomComboBox) components[i]).setEnabled(enabled);
			}
            else if(components[i] instanceof OrderDurationComboBox)
			{
            	 ((OrderDurationComboBox) components[i]).setEnabled(enabled);
			}
            
            else if(components[i] instanceof OrderCurrencyPanel)
            {
            	((OrderCurrencyPanel) components[i]).setEnabled(enabled);
            }
            else if(components[i] instanceof OrderDate) {
            	OrderDate orderDate = ((OrderDate) components[i]);
            	boolean orderDateEnabled = enabled;// && 
            			//!(orderDate.isD20DateOfSentenceIfDifferent() && orderDate.isD20AppealCase());
            	orderDate.setEnabled(orderDateEnabled);
            }
            else
            {
            	components[i].setEnabled(enabled);
            }
        }
    }
  
    /**
     * Sets whether the component has validation turned on.
     * 
     * @param b
     *            true if validation turned on
     */
    public void setValidation(boolean b) {
        Component[] components = getComponents();
        int max = components.length;

        for (int i = 0; i < max; i++) {
            if (components[i] instanceof AbstractOrderComponent) {
                if (((AbstractOrderComponent) components[i]).getValidator() != null) {
                    ((AbstractOrderComponent) components[i]).getValidator().setValidationOn(b);
                }
            } else if (components[i] instanceof OrderPanel) {
                ((OrderPanel) components[i]).setValidation(b);
                if (components[i] instanceof AbstractOrderComponent) {
                    ((AbstractOrderComponent) components[i]).getValidator().setValidationOn(b);
                }
            } else if (components[i] instanceof XDatePanel) {
                // @todo turn of validation...
                /*
                 * MDateEntryField field = ((XDatePanel)components[i]).
                 * getDateComponent(); JTextField field1 = new JTextField();
                 */
            } else if (components[i] instanceof XTimePanel) {
                c.set(Calendar.HOUR_OF_DAY, 10);
                c.set(Calendar.MINUTE, 00);
            }
        }
    }
}