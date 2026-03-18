package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import javax.swing.JOptionPane;

/**
 * <p>
 * Title: Error Dialog to display critical errors if Xhibit2 gui fails load.
 * </p>
 * <p>
 * Description:Error Dialog to display critical errors if Xhibit2 gui fails
 * load.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class OrdersExceptionDialog extends JOptionPane {
    // message to display to the user.
    private String msg;

    // title of message which appears as title of dialog.
    private String title;

    /**
     * Constructs an OrderExceptionsDialog.
     */
    public OrdersExceptionDialog() {
        this.msg = "error";
        this.title = "error";
    }

    /**
     * Sets the error message and title.
     * 
     * @param m
     *            error message.
     * @param t
     *            error title.
     */
    public void setMessages(String m, String t) {
        this.msg = m;
        this.title = t;
    }

    /**
     * show the error dialog.
     */
    public void showOrdersExceptionDialog() {
        this.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}
