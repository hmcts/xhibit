package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomAddressText;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers.OrderInputVerifier;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers.OrderVerifierFactory;

/**
 * <p>
 * Title: OrderAddressdetails. A class that creates custom JTextFields for the
 * Orders Address widget component.
 * </p>
 * <p>
 * Description: This class instantiaites the CustomAddressText class to create 6
 * JTextFields, these are Address Line (1-5) and a Postcode field. This class
 * uses a focusLost listener that updates xml (DataEntryTemplate.xml) when
 * textfield values are changed by the user.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Des
 * @version 1.0
 */
public class OrderAddressDetails extends AbstractOrderComponent implements FocusListener {

    private static Logger log = CSServices.getLogger(OrderAddressDetails.class);

    // Components to display address fields.
    private CustomAddressText addressLine1;

    private CustomAddressText addressLine2;

    private CustomAddressText addressLine3;

    private CustomAddressText addressLine4;

    private CustomAddressText addressLine5;

    private CustomAddressText postcode;

    // String array that holds the address text field reference.
    private String newRef[];

    // String to hold Default PostCode held within Blank Schema
    private String defaultPostCode = "AA1 1AA";

    private GridBagConstraints constraints;

    // Address field labels
    private JLabel addressLabel = new JLabel("Address:");

    private JLabel postcodeLabel = new JLabel("Postcode.");

    /**
     * Create the address text fields by instantiating the CustomAddressText
     * class. Add FocusListener event for each address field.
     * 
     * @throws OrderComponentException
     */
    public void initComponent() throws OrderComponentException {
        this.constraints = getDefaultConstraints();
        newRef = constructNewReferences(getHelper().getOrderDataReference());
        boolean requiredFields = true;
        if (getHelper().getAttribute("required") != null) {
            requiredFields = new Boolean(getHelper().getAttribute("required")).booleanValue();
        }
        createAddressLines(requiredFields);

        // Check to see if the post code is the default
        final String postcodeString = getHelper().getValue(newRef[5]);
        if (postcodeString != null && postcodeString.equals(defaultPostCode)) {
            // The value is the default so set the textfield text to empty
            postcode = new CustomAddressText("", newRef[5], 8, false, "8");
        } else if (postcodeString != null) {
            // There is a value other that the default
            postcode = new CustomAddressText(postcodeString.trim(), newRef[5], 8, false, "8");
        } else {
        	postcode = new CustomAddressText("", newRef[5], 8, false, "8");
        }
        postcode.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                // Uppercase the input character
                event.setKeyChar(String.valueOf(event.getKeyChar()).toUpperCase().charAt(0));
            }
        });
        OrderInputVerifier verifier = getVerifier(getHelper(), false, OrderInputVerifier.POSTCODE_TYPE);
        postcode.setInputVerifier(verifier);

        OrderPanel mainAddressPanel = this.createPanel();
        this.add(mainAddressPanel);
        setVisualComponent(this);
        setFields();
    }

    /**
     * Create the five address lines
     */
    private void createAddressLines(boolean required) {
        if (getHelper().getValue(newRef[0]) != null) {
            addressLine1 = new CustomAddressText(getHelper().getValue(newRef[0]).trim(), newRef[0], 20, true, "25");
        } else {
            addressLine1 = new CustomAddressText("", newRef[0], 20, true, "25");
        }
        addressLine1.setInputVerifier(getVerifier(getHelper(), required, OrderInputVerifier.ADDRESS_TYPE));
        
        if (getHelper().getValue(newRef[1]) != null) {
            addressLine2 = new CustomAddressText(getHelper().getValue(newRef[1]).trim(), newRef[1], 20, true, "25");
        } else {
            addressLine2 = new CustomAddressText("", newRef[1], 20, true, "25");
        }
        addressLine2.setInputVerifier(getVerifier(getHelper(), required, OrderInputVerifier.ADDRESS_TYPE));
        
        if (getHelper().getValue(newRef[2]) != null) {
            addressLine3 = new CustomAddressText(getHelper().getValue(newRef[2]).trim(), newRef[2], 20, false, "25");
        } else {
            addressLine3 = new CustomAddressText("", newRef[2], 20, true, "25");
        }
        addressLine3.setInputVerifier(getVerifier(getHelper(), false, OrderInputVerifier.ADDRESS_TYPE));
        
        if (getHelper().getValue(newRef[3]) != null) {
            addressLine4 = new CustomAddressText(getHelper().getValue(newRef[3]).trim(), newRef[3], 20, false, "25");
        } else {
            addressLine4 = new CustomAddressText("", newRef[3], 20, true, "25");
        }
        addressLine4.setInputVerifier(getVerifier(getHelper(), false, OrderInputVerifier.ADDRESS_TYPE));
        
        if (getHelper().getValue(newRef[4]) != null) {
            addressLine5 = new CustomAddressText(getHelper().getValue(newRef[4]).trim(), newRef[4], 20, false, "25");
        } else {
            addressLine5 = new CustomAddressText("", newRef[4], 20, true, "25");
        }
        addressLine5.setInputVerifier(getVerifier(getHelper(), false, OrderInputVerifier.ADDRESS_TYPE));
    }

    /**
     * Create a JPanel and arrange components using GridBag constraints.
     */
    private OrderPanel createPanel() {
        OrderPanel addressPanel = new OrderPanel();

        GridBagLayout gb = new GridBagLayout();
        addressPanel.setLayout(gb);

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;

        addressPanel.add(addressLabel, constraints);
        // Create address line 1
        constraints.gridy = 1;
        addressPanel.add(addressLine1, constraints);
        // Create address line 2
        constraints.gridy = 2;
        addressPanel.add(addressLine2, constraints);
        // Create address line 3
        constraints.gridy = 3;
        addressPanel.add(addressLine3, constraints);
        // Create address line 4
        constraints.gridy = 4;
        addressPanel.add(addressLine4, constraints);
        // Create address line 5
        constraints.gridy = 5;
        addressPanel.add(addressLine5, constraints);

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        // Create post code
        addressPanel.add(postcodeLabel, constraints);

        constraints.insets = new Insets(0, 0, 0, 20);
        constraints.gridx = 1;
        constraints.gridy = 6;
        addressPanel.add(postcode, constraints);

        add(addressPanel, constraints);
        return addressPanel;
    }

    /**
     * Ensure this object's enabled properties are set to false where the 'At
     * following address' radio button is set to true (upon initial startup of
     * the screen).
     */
    public void setFields() {
        if ((getValidator() != null) // validator not null
                && // AND
                (getValidator().getOpt() != null) // option not null
                && // AND
                (getValidator().getOpt().getButtonRadio().isSelected())) // radio
        // button
        // selected
        {
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
    }

    /**
     * Place the xml address field references into a string array.
     * 
     * @param ref
     */
    private String[] constructNewReferences(String ref) {
        String[] addressArray = new String[6];
        for (int i = 0; i <= 4; i++) {
            addressArray[i] = ref + "[" + (i + 1) + "]";
        }
        if ((ref != null) && (ref.endsWith("apd:Line"))) {
            String postcodeRef = ref.substring(0, ref.indexOf("/apd:Line"));
            addressArray[5] = postcodeRef + "/apd:PostCode";
        } else { // 
            addressArray[5] = "//ord:LiveSleep//apd:PostCode";
        }
        return addressArray;
    }

    /**
     * Update the xml on the preview screen when address fields are changed.
     * Also ensure mandatory fields are not null, i.e. address lines 1 and 2.
     * 
     * @param e
     *            Invoked when a component looses the keyboard focus.
     */
    public void focusLost(FocusEvent event) {
        // Check if the event is temporary - e.g. when the error dialog appears
        // a trmporary focusLost event will be triggered. This could be avided
        // if we display
        // error messages in (e.g.) the status bar. See also OrderTextField
        if (!event.isTemporary()) {
            CustomAddressText b = ((CustomAddressText) event.getSource());
            getValidator().setRequired(b.getRequired());
            // If field is not valid set border to red
            if (!getValidator().isValid(b.getText())) {
                b.setBorder(BorderFactory.createLineBorder(Color.red));
                b.requestFocus();
            }
            // Otherwise update the xml
            else {
                String trimmedAddress = b.getText().toString().trim();
                getHelper().setValues(b.getRef(), (trimmedAddress.equals("") ? " " : trimmedAddress));
            }
        }
    }

    /**
     * Empty implmentatioin of focusGained
     * 
     * @param fe
     */
    public void focusGained(FocusEvent fe) {
        // No implementation
    }

    private OrderInputVerifier getVerifier(OrderComponentHelper helper, boolean required, String type) {
        OrderInputVerifier verifier = OrderVerifierFactory.createVerifier(type);
        verifier.setHelper(helper);
        verifier.setRequired(required);
        return verifier;
    }
}
