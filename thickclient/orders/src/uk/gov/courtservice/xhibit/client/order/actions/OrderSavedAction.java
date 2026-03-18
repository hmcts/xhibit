package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: Xhibit2 OrderSignedAction
 * </p>
 * <p>
 * Description: Stores the details entered from the signed dialog and updates
 * the OrderDataModel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderSavedAction extends XAction {
    private OrderInitialDataVO model;

    private JTextField txtFld;

    public OrderSavedAction() {
    }

    public OrderSavedAction(OrderInitialDataVO model, JTextField text) {
        this.model = model;
        txtFld = text;
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XAction
         *       abstract method
         */
        this.model.setSavedDetails(txtFld.getText());
    }
}