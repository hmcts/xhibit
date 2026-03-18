package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderSummaryOrderTypeListener
 * </p>
 * <p>
 * Description: Listens on the OrdersSummaryPanel for the Order Type being
 * updated. When triggered, the OrderDataModel is updated with the appropriate
 * value.
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

public class OrderSummaryOrderTypeListener extends OrderSummaryTextListener {

    /**
     * Constructor
     */
    private OrderSummaryOrderTypeListener() {
    }

    /**
     * Constructor - Sets the order type on the OrderDataModel
     * 
     * @param odm
     *            OrderInitialDataVO
     * @param txt
     *            JTextField to be updated
     */
    public OrderSummaryOrderTypeListener(OrderInitialDataVO model, JTextField txt) {
        super(model, txt);
    }

    /**
     * Sets the order type on the OrderDataModel
     * 
     * @param e
     *            ActionEvent
     */
    public void xActionPerformed(ActionEvent e) {
        // ordDM.setOrderType(textField.getText());
    }
}