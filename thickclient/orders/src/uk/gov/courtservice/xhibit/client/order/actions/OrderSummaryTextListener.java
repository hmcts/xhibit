package uk.gov.courtservice.xhibit.client.order.actions;

import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: Xhibit2 OrderSummaryTextListener
 * </p>
 * <p>
 * Description: Abstract class that should be subclassed if a new order summary
 * listener is required for a new data item
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

abstract public class OrderSummaryTextListener extends XAction {

    /**
     * Reference to the OrderInitialDataVO
     */
    protected OrderInitialDataVO model;

    /**
     * Reference to the JTextField
     */
    protected JTextField textField;

    /**
     * Constructor
     */
    protected OrderSummaryTextListener() {
    }

    /**
     * Constructor
     * 
     * @param model
     *            OrderInitialDataVO
     * @param txt
     *            JTextField
     */
    public OrderSummaryTextListener(OrderInitialDataVO model, JTextField txt) {
        this();
        this.model = model;
        textField = txt;
    }

    protected void setModel(OrderInitialDataVO model) {
        this.model = model;
    }

}