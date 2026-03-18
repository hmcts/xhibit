package uk.gov.courtservice.xhibit.client.order.screens.helper;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderDefendantComboBoxListener
 * </p>
 * <p>
 * Description: Reflects combo box selections in Defendant text fields
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

public class OrderDefendantComboBoxListener extends OrderComboBoxListener {
    private static final Logger log = CSServices.getLogger(OrderDefendantComboBoxListener.class);

    /**
     * Constructor
     * 
     * @param tfl
     *            text field
     * @param oidvo
     *            the model
     */
    public OrderDefendantComboBoxListener(JTextField tfl, OrderInitialDataVO model) {
        super(tfl, model);
    }

    /**
     * Process the selected Defendant
     * 
     * @param msg
     *            the selected item
     */
    protected void processInput(String msg) {
        if (msg != null && msg.equals("") == false) {
            OrderInitialDataHelper helper = ((OrderInitialDataVO) getModel()).getHelper();
            Integer defOnCaseID = helper.getDefendantOnCaseID(msg);
            if (helper != null && defOnCaseID != null) {
                int id = defOnCaseID.intValue();
                ((OrderInitialDataVO) getModel()).setDefendantOnCaseID(id);
            }
        } else {
            ((OrderInitialDataVO) getModel()).setDefendantOnCaseID(null);
        }
    }

    /**
     * Enable/Disable the buttons according to the mode (CREATE/VIEW)
     */
    protected void enableButtons() {
        if (!((OrderInitialDataVO) getModel()).isMonetaryOrder() && !((OrderInitialDataVO) getModel()).isD20Order()) {
            if (getWizButton() != null && ((OrderInitialDataVO) getModel()) != null) {
                setupButton(getWizButton());
            }
        }
    }

    /**
     * Buttons in VIEW mode
     * 
     * @param button
     */
    protected void viewButtons(JButton button) {
        button.setEnabled(isValidDefendantID());
    }

    /**
     * Buttons in CREATE mode
     * 
     * @param button
     */
    protected void createButtons(JButton button) {
        button.setEnabled(checkDefendantAndOrderType());
    }

    /**
     * Check that both Defendant and Order Type have been selected
     * 
     * @return
     */
    private boolean checkDefendantAndOrderType() {
        return isValidDefendantID() && isValidOrderType();
    }

    /**
     * Check that Defendant ID is not null/empty
     * 
     * @return
     */
    private boolean isValidDefendantID() {
        if (((OrderInitialDataVO) getModel()).getDefendantID() == null
                || ((OrderInitialDataVO) getModel()).getDefendantID().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check that Order Type is not null/empty
     * 
     * @return
     */
    private boolean isValidOrderType() {
        if (((OrderInitialDataVO) getModel()).getOrderTypeID() == null
                || ((OrderInitialDataVO) getModel()).getOrderTypeID().equals("")) {
            return false;
        } else {
            return true;
        }
    }
}