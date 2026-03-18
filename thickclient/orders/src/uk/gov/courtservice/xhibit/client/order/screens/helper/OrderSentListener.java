package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import mseries.Calendar.MFieldListener;
import mseries.ui.MDateEntryField;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: OrderSentListener
 * </p>
 * <p>
 * Description: Listens on the two input fields on the sent dialog and
 * enables/disables the OK button
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class OrderSentListener extends XAction implements MFieldListener, FocusListener {

    private JButton button = null;

    private SentDetailsVO sent = null;

    private MDateEntryField dateField = null;

    /**
     * Constructor
     * 
     * @param sentDetailsVO
     *            the model
     * @param btn
     *            the button to enable/disable
     */
    public OrderSentListener(SentDetailsVO sgnVO, JButton btn) {
        sent = sgnVO;
        button = btn;
    }

    /**
     * Sets the date field
     * 
     * @param dtFld
     *            the datefield
     */
    public void setDateField(MDateEntryField dtFld) {
        dateField = dtFld;
    }

    /**
     * XActionPerformed
     * 
     * @param e
     */
    public void xActionPerformed(ActionEvent e) {
        sent.setSentDate(dateField.getText());
    }

    /**
     * Set the date when the field is exited
     * 
     * @param fe
     */
    public void fieldExited(FocusEvent fe) {
        sent.setSentDate(dateField.getText());
    }

    /**
     * Empty implementation of fieldEntered
     * 
     * @param fe
     */
    public void fieldEntered(FocusEvent fe) {
        // do nothing
    }

    /**
     * Returns the SentDetailsVO object
     * 
     * @return
     */
    public SentDetailsVO getSentVO() {
        return sent;
    }

    /**
     * Empty implementation of focusgained
     * 
     * @param event
     */
    public void focusGained(FocusEvent event) {
        // No implementation
    }

    /**
     * Enable/disable the button when focus is lost
     * 
     * @param event
     */
    public void focusLost(FocusEvent event) {
        // Disable the OK button if the (Surname) field is empty
        button.setEnabled(!((JTextField) event.getSource()).getText().equals(""));
    }
}