package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: CustomAddressText. A class that creates custom JTextFields, each field
 * is given a reference which is generated in class OrderAddressDetails.
 * </p>
 * <p>
 * Description: This class is used to create text fields for the Orders Address
 * widget component.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */
public class CustomAddressText extends TextLimitedJTextField implements FocusListener {
    private static final Logger log = CSServices.getLogger(CustomAddressText.class);

    // Component that contains the address field reference.
    private String reference;

    // String to check for mandatory field values.
    private boolean required;

    // String to hold default/original values
    private String name;

    /**
     * Restores the list of court names to the original list.
     * 
     * @param name
     *            Contains the address name.
     * @param ref
     *            Contains the address filed reference.
     * @param size
     *            determines the size of the JTextField component.
     */
    public CustomAddressText(String name, String ref, int size, boolean mandatory, String length) {
        super(name, size, length);
        this.required = mandatory;
        this.reference = ref;
        this.name = name;
        this.addFocusListener(this);
    }

    /**
     * Return the address text field component.
     * 
     * @return this
     */
    public TextLimitedJTextField getTextField() {
        // return this.addressText;
        return this;
    }

    /**
     * Return the address text field reference.
     * 
     * @return reference
     */
    public String getRef() {
        return this.reference;
    }

    public boolean getRequired() {
        return this.required;
    }

    /**
     * Empty implmentation of focusGained
     * 
     * @param fe
     */
    public void focusGained(FocusEvent fe) {
        // No implementation
    }

    /**
     * Check the value of the text field and reset if neccessary
     * 
     * @param fe
     */
    public void focusLost(FocusEvent fe) {
        if (getTextField().getText() == null || getTextField().getText().trim().equals("")) {
            // if address field is empty, and is line 1 or line 2 (required)
            // reset to original value
        	/** 04/08/2015 - Remove this logic as it impacts the copy order process for these fields */
            /**if (getRequired()) {
                getTextField().setText(this.name);
            }*/
        }
    }

}
