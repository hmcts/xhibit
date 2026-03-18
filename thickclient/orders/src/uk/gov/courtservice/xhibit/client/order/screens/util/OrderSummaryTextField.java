package uk.gov.courtservice.xhibit.client.order.screens.util;

import javax.swing.JTextField;

/**
 * <p>
 * Title: Xhibit2 OrderSummaryTextField
 * </p>
 * <p>
 * Description: subclass of JTextField to allow for actionEvent to be fired from
 * a disabled text field
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

public class OrderSummaryTextField extends JTextField {

    /**
     * 
     */
    public OrderSummaryTextField() {

    }

    /**
     * 
     * @param s
     */
    public OrderSummaryTextField(String s) {
        super(s);

    }

    /**
     * 
     * @param txt
     */
    public void setText(String txt) {
        super.setText(txt);
        this.fireActionPerformed();

        // Des Johnston SCR 52556
        this.setCaretPosition(0);
    }

}