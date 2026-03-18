package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: OrderListCheckBoxListener
 * </p>
 * <p>
 * Description: Updates a JComboBox with different data if a JCheckBox is
 * selected
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

public class OrderListCheckBoxListener extends XAction {
    private JComboBox comboBox = null;

    private OrderInitialDataVO model = null;

    public OrderListCheckBoxListener(JComboBox combo, OrderInitialDataVO model) {
        comboBox = combo;
        this.model = model;
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XAction
         *       abstract method
         */
        setValues(comboBox, ((JCheckBox) parm1.getSource()).isSelected());
    }

    private void setValues(JComboBox cBox, boolean ordersInd) {
        cBox.removeAllItems();
        String[] typeVals = this.model.getHelper().getOrderTypes(!ordersInd);
        for (int i = 0; i < typeVals.length; i++) {
            cBox.addItem(typeVals[i]);
            cBox.repaint();
        }
    }
}