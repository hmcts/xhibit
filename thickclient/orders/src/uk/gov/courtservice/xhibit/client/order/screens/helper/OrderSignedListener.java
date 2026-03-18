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
 * Title: OrderSignedListener
 * </p>
 * <p>
 * Description: Listens on the two input fields on the signed dialog and
 * enables/disables the OK button
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

public class OrderSignedListener extends XAction implements MFieldListener, FocusListener {

    private JButton button = null;

    private SignedDetailsVO sign = null;
    
    private SentDetailsVO sent = null;

    private MDateEntryField dateField = null;

    /**
     * Constructor
     * 
     * @param sgnVO
     *            the model
     * @param btn
     *            the button to enable/disable
     */
    public OrderSignedListener(SignedDetailsVO sgnVO, JButton btn) {
        sign = sgnVO;
        button = btn;
    }

    public OrderSignedListener(SentDetailsVO sentVO, JButton okButton) {
    	sent = sentVO;
        button = okButton;
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
    	if (sign != null) {
    		sign.setSignedDate(dateField.getText());
    	} else if (sent != null) {
    		sent.setSentDate(dateField.getText());
    	}
    }

    /**
     * Set the date when the field is exited
     * 
     * @param fe
     */
    public void fieldExited(FocusEvent fe) {
    	if (sign != null) {
    		sign.setSignedDate(dateField.getText());
    	} else if (sent != null) {
    		sent.setSentDate(dateField.getText());
    	}
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
     * Returns the SignedDetailsVO object
     * 
     * @return
     */
    public SignedDetailsVO getSignVO() {
        return sign;
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