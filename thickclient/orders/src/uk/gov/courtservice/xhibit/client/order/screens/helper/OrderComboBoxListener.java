package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: Xhibit2 ComboBoxListener
 * </p>
 * <p>
 * Description: Reflects combo box selections in text fields
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public abstract class OrderComboBoxListener extends XAction {
    private static final Logger log = CSServices.getLogger(OrderComboBoxListener.class);

    private JTextField textField;

    private OrderInitialDataVO model;

    private JButton wizButton;

    /**
     * Constructor
     */
    public OrderComboBoxListener() {
    }

    /**
     * Constructor
     * 
     * @param tfl
     *            text field
     */
    public OrderComboBoxListener(JTextField tfl) {
        textField = tfl;
    }

    /**
     * Constructor
     * 
     * @param tfl
     *            text field
     * @param oidvo
     *            the model
     * @param def
     *            true if a defendent screen - REFACTOR
     */
    public OrderComboBoxListener(JTextField tfl, OrderInitialDataVO model, boolean def) {
        this(tfl);
        this.model = model;
    }

    /**
     * Constructor
     * 
     * @param tfl
     *            text field
     * @param oidvo
     *            the model
     * @param def
     *            true if a defendent screen - REFACTOR
     */
    public OrderComboBoxListener(JTextField tfl, OrderInitialDataVO model) {
        this(tfl);
        this.model = model;
    }

    /**
     * Process the combo box source
     * 
     * @param msg
     *            the selecetd item
     */
    protected abstract void processInput(String msg);

    /**
     * Set the buttons in CREATE mode
     * 
     * @param button
     */
    protected abstract void createButtons(JButton button);

    /**
     * Set the buttons in VIEW mode
     * 
     * @param button
     */
    protected abstract void viewButtons(JButton button);

    /**
     * Enable/Disable the buttons depending on the mode, and options selected
     */
    protected abstract void enableButtons();

    /**
     * Add a button to the listener
     * 
     * @param button
     *            the button
     */
    public void addButton(JButton button) {
        wizButton = button;
    }

    /**
     * Set the reference to the model
     * 
     * @param odm
     *            the model
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
    }

    /**
     * Store the combobox values in the model
     * 
     * @param e
     */
    public void xActionPerformed(ActionEvent e) {
        if (((JComboBox) e.getSource()).getSelectedItem() != null) {
            processItemFromSource(e);
        } else {
            textField.setText("");
        }

        enableButtons();
    }

    /**
     * Get the item selected and set the buttons accordingly
     * 
     * @param e
     *            the event
     */
    private void processItemFromSource(ActionEvent e) {
        String msg = ((JComboBox) e.getSource()).getSelectedItem().toString();
        textField.setText(msg);
        textField.revalidate();
        if (this.model != null) {
            processInput(msg);
        }
    }

    /**
     * Enable/Disable the buttons depending on he current mode (CREATE/VIEW)
     * 
     * @param btn
     */
    protected void setupButton(JButton btn) {
        boolean enable = true;

        switch (this.model.getMode()) {
            case OrderInitialDataVO.VIEW_MODE: {
                viewButtons(btn);
                break;
            }
            case OrderInitialDataVO.COPY_MODE: {
                viewButtons(btn);
                break;
            }
            default: {
                createButtons(btn);
                break;
            }
        }
    }

    /**
     * Get the Wizard button to enable/disable
     * 
     * @return the button
     */
    protected JButton getWizButton() {
        return wizButton;
    }

    public Object getModel() {
        return this.model;
    }
}