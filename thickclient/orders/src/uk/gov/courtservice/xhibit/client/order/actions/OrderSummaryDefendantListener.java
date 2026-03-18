package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderSummaryDefendantListener
 * </p>
 * <p>
 * Description: Listens on the OrdersSummaryPanel for the Defendant Name being
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
 * @see OrderSummaryTextListener
 */

public class OrderSummaryDefendantListener extends OrderSummaryTextListener {

    /**
     * Constructor
     */
    public OrderSummaryDefendantListener() {
    }

    /**
     * Constructor
     * 
     * @param odm
     *            OrderInitialDataVO holding order data
     * @param txt
     *            JTextField to update
     */
    public OrderSummaryDefendantListener(OrderInitialDataVO model, JTextField txt) {
        super(model, txt);
    }

    /**
     * Sets the defendant name on the OrderDataModel
     * 
     * @param e
     *            ActionEvent
     */
    public void xActionPerformed(ActionEvent e) {
        this.model.setDefendantName(textField.getText());
    }

    /**
     * Set the model
     * 
     * @param oidvo
     *            the model
     */
    public void setModel(OrderInitialDataVO model) {
        super.setModel(model);
    }
}