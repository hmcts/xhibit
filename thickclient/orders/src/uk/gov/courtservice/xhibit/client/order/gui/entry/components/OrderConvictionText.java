package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderConvictionText. Class utilising custom class
 * TextLimitedJTextField for single-line editing of plain text.
 * </p>
 * <p>
 * Description: This class components are derived from class
 * TextLimitedJTextField. On initaial screen entry, where null xml values are
 * returned, set the component's enabled property to true otherwise set it to
 * false.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */
public class OrderConvictionText extends AbstractOrderComponent implements FocusListener {

    private static final Logger log = CSServices.getLogger(OrderConvictionText.class);

    private static final String ORDER_CHARGE_XPATH = "//ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement";

    // Maximum length of conviction text
    private static final String MAX_CONVICTION_TEXT = "1000";

    // Number of columns to display
    private static final int NUM_COLS = 40;

    // Number of rows to display
    private static final int NUM_ROWS = 2;

    private TextLimitedJTextArea textFieldText;

    boolean isDefault = true;

    /**
     * Create a TextLimitedJTextField component, retrieve xml values as first
     * argument. If no values are returned then set the components enabled
     * properties to true, otherwise set the enabled properties to false.
     * 
     * @throws OrderComponentException
     *             This is a general runtime exception thrown by
     *             OrderComponents.
     */
    public void initComponent() throws OrderComponentException {

        /**
         * Code used to construct the text to be displayed within the
         * Convictions textfield. Data is obtained from the XML (all
         * OffenceStatements), manipulated i.e. find out how many convictions of
         * each type there are and construct the text to appear within the
         * textfield
         */
        log.debug("CREATE CONVICTION TEXT FIELD");

        // String storedConviction = getHelper().getValue();
        ArrayList offenceDesc = new ArrayList(); // hold descriptions
        String textFieldValue = null; // to hold value of textfield text

        int noOfCharges = calculateNumberOfCharges(offenceDesc);

        String[] chargeStatements = setChargeStatements(offenceDesc);

        // S.Bachra 7/5/03 Check to see if only 1 offence (the default)
        if (noOfCharges == 0) {
            textFieldValue = processSingleCharge(textFieldValue);
        } else {
            textFieldValue = processMultipleCharges(textFieldValue, noOfCharges, chargeStatements);
        }

        log.debug("TEXTFIELD VALUE: " + textFieldValue);

        // Limit of text field set to 255 as an arbitrary large value
        // textFieldText = new TextLimitedJTextArea(textFieldValue, 20, "255");
        textFieldText = new TextLimitedJTextArea(textFieldValue, NUM_ROWS, NUM_COLS, MAX_CONVICTION_TEXT);

        textFieldText.getTextArea().addFocusListener(this);

        // If noOfCharges > 0, disable the field, otherwise leave it
        // editable/enabled
        textFieldText.getTextArea().setEditable(noOfCharges <= 0);
        textFieldText.setEnabled(noOfCharges <= 0);

        setVisualComponent(textFieldText);

    }

    /**
     * Converts a single charge to a single text value to be displayed on the
     * order.
     * 
     * @param textFieldValue
     *            the text to update
     * @return the charge converted to a text value
     */
    private String processSingleCharge(String textFieldValue) {
        log.debug("ONLY DEFAULT OFFENCE");
        String charge = getHelper().getValue(ORDER_CHARGE_XPATH);
        log.debug("********** Charge Text1 : " + charge);
        if (charge != null && charge.length() != 1) {
            log.debug("Charge Exists within XML");
            textFieldValue = charge;
        } else {
            textFieldValue = null;
        }
        return textFieldValue;
    }

    /**
     * Converts multiple charges to a single text value to be displayed on the
     * order.
     * 
     * @param textFieldValue
     *            the text to update
     * @param noOfCharges
     *            the number of charges
     * @param chargeStatements
     *            the list of charge statements
     * @return the charges converted to a text value
     */
    private String processMultipleCharges(String textFieldValue, int noOfCharges, String[] chargeStatements) {
        // only carry out if offences present
        // populate array
        // S.Bachra 7/5/03 Start the array from 1 - element 0 is the default
        // and needs to be ignored

        log.debug("MORE THAN 1 OFFENCE");
        isDefault = false; // set to false to indicate that there are
        // charges present
        java.util.Arrays.sort(chargeStatements); // sort the array
        // Alphabetically

        String descToCheck = null; // hold each description to check in
        // loop
        String previousDesc = chargeStatements[0]; // set to first array
        // element
        int descCounter = 0; // count to hold no of descriptions found

        // Check how many different charge types there are. If only 1, do not
        // add
        // the semicolon on the end
        HashSet set = new HashSet();
        for (int i = 0; i < chargeStatements.length; i++) {
            // Will not allow duplicate entries so can find out how many
            // types.
            set.add(chargeStatements[i]);
        }
        String delim = set.size() > 1 ? "; " : "";

        StringBuffer sbuffer = new StringBuffer(); // to hold convictions
        // text
        // with count details

        // loop through offences to count
        for (int i = 0; i < noOfCharges; i++) {
            if (i == set.size()) {
                delim = "";
            }
            descToCheck = chargeStatements[i];
            if (!descToCheck.equals(previousDesc)) {
                // If only one charge of each type, do not display charge X n
                sbuffer.append(previousDesc + (descCounter == 1 ? delim : (" X " + descCounter + delim)));
                previousDesc = chargeStatements[i];
                descCounter = 0;
            }
            descCounter++;
        }
        // If only one charge of each type, do not display charge X n
        // add on last description text
        sbuffer.append(previousDesc + (descCounter == 1 ? delim : (" X " + descCounter + delim)));

        textFieldValue = sbuffer.toString(); // set value of textfield */
        return textFieldValue;
    }

    /**
     * Converts the list of offence descriptions to a string array of charge
     * statements
     * 
     * @param offenceDesc
     *            the list of offence descriptions
     * @return an array of charge statements
     */
    private String[] setChargeStatements(ArrayList offenceDesc) {
        // convert ArrayList to String Array
        String[] chargeStatements = new String[offenceDesc.size()];
        offenceDesc.toArray(chargeStatements);

        log.debug("Charge Statement Array Size: " + chargeStatements.length);

        log.debug("DISPLAY CHARGES");
        for (int i = 0; i < chargeStatements.length; i++) {
            log.debug("Charge: " + i + ":" + chargeStatements[i]);
        }
        return chargeStatements;
    }

    /**
     * Calculates the number of charges based on the values retrieved from the
     * order
     * 
     * @param offenceDesc
     *            a list of offences
     * @return the number of charges
     */
    private int calculateNumberOfCharges(ArrayList offenceDesc) {
        int noOfCharges = 0;
        String chargeText;

        for (int i = 2; i < 30; i++) {
            chargeText = getHelper().getValue("//ord:Defendant/ord:Charges/ord:Charge[" + i + "]/ord:OffenceStatement");
            if (chargeText != null && chargeText.length() != 1) {
                noOfCharges++;
                offenceDesc.add(chargeText);
            }
        }

        log.debug("No Of Charges: " + offenceDesc.size());
        return noOfCharges;
    }

    /**
     * Set the value on the dom
     * 
     * @param event
     *            Invoked when a component loses the keyboard focus.
     */
    public void focusLost(FocusEvent event) {
        // only update if there is only the default charge
        if (isDefault) {
            getHelper().setValue(textFieldText.getTextArea().getText());
        }
    }

    /**
     * Sets the caret position to the first (left) character
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusGained(FocusEvent event) {
        textFieldText.getTextArea().setCaretPosition(0);
    }
}