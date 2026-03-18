package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderConvictionTextArea.
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
 */
public class OrderConvictionTextArea extends AbstractOrderComponent implements FocusListener {

    private static final Logger log = CSServices.getLogger(OrderConvictionTextArea.class);

    private static final String ORDER_CHARGE_XPATH = "//ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement";
    
    private static final String ORDER_LINKED_CHARGE_XPATH = "//ord:LinkedOffences/ord:LinkedOffence[1]/ord:LinkedOffenceStatement";

    // Maximum length of conviction text
    private static final String MAX_CONVICTION_TEXT = "1000";

    // Number of columns to display
    private static final int NUM_COLS = 50;

    // Number of rows to display
    private static final int NUM_ROWS = 10;

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
        log.debug("CREATE CONVICTION TEXT AREA");

        ArrayList offenceDesc = new ArrayList(); // hold descriptions
        String textFieldValue = null; // to hold value of textfield text

        int noOfCharges = calculateNumberOfCharges(offenceDesc);
        int noOfLinkedCharges = calculateNumberOfLinkedCharges(offenceDesc);

        // S.Bachra 7/5/03 Check to see if only 1 offence (the default)
        if (noOfCharges == 0 && noOfLinkedCharges == 0) {
            textFieldValue = processSingleCharge(textFieldValue);
        } else {
            textFieldValue = processMultipleCharges(textFieldValue, noOfCharges, noOfLinkedCharges);
        }

        log.debug("TEXTFIELD VALUE: " + textFieldValue);

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
        }else {
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
     * @return the charges converted to a text value
     */
    private String processMultipleCharges(String textFieldValue, int noOfCharges, int noOfLinkedCharges) {
        // only carry out if offences present
        log.debug("MORE THAN 1 OFFENCE");
        isDefault = false; // set to false to indicate that there are
        // charges present

        StringBuffer text = new StringBuffer();
        String chargeText;
        String caseNumber;
        int trueNoOfCharges = noOfCharges + 1;
        for (int i = 2; i <= trueNoOfCharges; i++) {
            chargeText = getHelper().getValue("//ord:Defendant/ord:Charges/ord:Charge[" + i + "]/ord:OffenceStatement");
            caseNumber = getHelper().getValue("//ord:Defendant/ord:Charges/ord:Charge[" + i + "]/ord:CaseNumber");

            if (chargeText != null && chargeText.length() != 1) {
                log.debug("VALUE TO APPEND : " + caseNumber + " " + chargeText);
                text.append(caseNumber);
                text.append(" / ");
                text.append(chargeText);
                text.append('\n');
            }
        }
        //Now repeat for linked offences
        if (noOfLinkedCharges != 0){
            int trueNoOfLinkedCharges = noOfLinkedCharges + 1;
            for (int i = 1; i <= trueNoOfLinkedCharges; i++) {
                chargeText = getHelper().getValue("//ord:LinkedOffences/ord:LinkedOffence[" + i + "]/ord:LinkedOffenceStatement");
                caseNumber = getHelper().getValue("//ord:LinkedOffences/ord:LinkedOffence[" + i + "]/ord:LinkedCaseNumber");

                if (chargeText != null && chargeText.length() != 1) {
                    log.debug("VALUE TO APPEND : " + caseNumber + " " + chargeText);
                    text.append(caseNumber);
                    text.append(" / ");
                    text.append(chargeText);
                    text.append('\n');
                }
            }
        }
        textFieldValue = text.toString(); // set value of textfield */
        return textFieldValue;
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
     * Calculates the number of charges based on the values retrieved from the
     * order
     * 
     * @param offenceDesc
     *            a list of offences
     * @return the number of charges
     */
    private int calculateNumberOfLinkedCharges(ArrayList offenceDesc) {
        int noOfCharges = 0;
        String chargeText;

        //Now add linked charges if any present
        for (int i = 1; i < 30; i++) {
            chargeText = getHelper().getValue("//ord:LinkedOffences/ord:LinkedOffence[" + i + "]/ord:LinkedOffenceStatement");
            if (chargeText != null && chargeText.length() != 1) {
                noOfCharges++;
                offenceDesc.add(chargeText);
            }
        }

        log.debug("No Of Linked Charges: " + offenceDesc.size());
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