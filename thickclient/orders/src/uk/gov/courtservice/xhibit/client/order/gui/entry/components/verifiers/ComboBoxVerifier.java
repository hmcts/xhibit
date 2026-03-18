package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ComboBoxVerifier extends OrderInputVerifier {
    private static final Logger log = CSServices.getLogger(ComboBoxVerifier.class);

    private JComboBox cBox;

    /**
     * Constructor for IntVerifier
     */
    public ComboBoxVerifier() {
    }

    /**
     * Verify the component
     * 
     * @param input
     *            the component to verify
     * @return true if varification is okay
     */
    public boolean verify(JComponent input) {
        ((JComboBox) input).hidePopup();
        return !((JComboBox) input).isPopupVisible();
    }

    /**
     * Indicates if the component should allow focus to be yielded
     * 
     * @param input
     *            the component to check
     * @return true if focus can be yielded
     */
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = super.shouldYieldFocus(input);
        return valid;
    }

    /**
     * Checks that a value is entered and returns false only if required is set
     * to true.
     * 
     * @param value
     *            String contents of field.
     * @return true or false.
     */
    protected boolean checkLength(String value) {

        return true;
    }

    /**
     * Validates field by checking if it contains a valid string.
     * 
     * @param s
     *            String to be validated.
     * @return true if field contents are valid.
     */
    public boolean isValid(String s) {
        return true;
    }

}