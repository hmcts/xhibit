package uk.gov.courtservice.xhibit.client.order.screens.helper;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderTypeComboBoxListener
 * </p>
 * <p>
 * Description: Reflects combo box selections in Order Type text fields
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

public class OrderTypeComboBoxListener extends OrderComboBoxListener {
    private static final Logger log = CSServices.getLogger(OrderTypeComboBoxListener.class);

    /**
     * Constructor
     * 
     * @param tfl
     *            text field
     * @param oidvo
     *            the model
     */
    public OrderTypeComboBoxListener(JTextField tfl, OrderInitialDataVO model) {
        super(tfl, model);
    }

    /**
     * Enable/Disable the buttons according to the mode
     */
    protected void enableButtons() {
        if (getWizButton() != null && ((OrderInitialDataVO) getModel()) != null) {
            if (((OrderInitialDataVO) getModel()).getOrderType() != null) {
                setupButton(getWizButton());
            }
        }
    }

    /**
     * Process the selected Order Type
     * 
     * @param msg
     *            the selected item
     */
    protected void processInput(String msg) {
        if (msg != null && msg.equals("") == false) {
        	String orderType = "";
            OrderInitialDataHelper helper = ((OrderInitialDataVO) getModel()).getHelper();
            if (((OrderInitialDataVO) getModel()).isMonetaryOrder()) {
                orderType = "Monetary Order";
            } else if (((OrderInitialDataVO) getModel()).isD20Order()) {
                orderType = "D20";
            } else {
            	orderType = msg;
            }
            XhbOrderTypeValue ord = helper.getOrderType(orderType);
            Integer ordID = ord.getOrderTypeId();
            if (helper != null && ordID != null) {
                int id = ordID.intValue();
                ((OrderInitialDataVO) getModel()).setOrderID(ord.getDescription());
                ((OrderInitialDataVO) getModel()).setOrderType(orderType);
                
                if ( orderType.equals("D20")) {
                	String d20Interim = "";
                	if ( msg.startsWith("Interim")) {
                		d20Interim = "Y";
                	}
                	else {
                		d20Interim = "N";
                	}
                	
                	((OrderInitialDataVO) getModel()).setD20Interim( d20Interim );
                }
            }
        }
    }

    /**
     * The buttons in VIEW mode
     * 
     * @param button
     */
    protected void viewButtons(JButton button) {
        button.setEnabled(enableByOrderType());
    }

    /**
     * The buttons in CREATE mode
     * 
     * @param button
     */
    protected void createButtons(JButton button) {
        button.setEnabled(enableByOrderType());
    }

    /**
     * Chack if the Order Type has been selected
     * 
     * @return
     */
    private boolean enableByOrderType() {
        if (((OrderInitialDataVO) getModel()).getOrderType().equals("")) {
            return false;
        } else {
            return true;
        }
    }
}