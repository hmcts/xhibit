package uk.gov.courtservice.xhibit.client.util.verifiers;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Verifies email addresses.
 * </p>
 * <p>
 * Description: Verifies email addresses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class EmailAddressVerifier extends InputVerifier {
    private static Logger log = CSServices.getLogger(EmailAddressVerifier.class);

    private static final String ERROR_MESSAGE = "gui.email.address.error.message";

    private static final String ERROR_TITLE = "gui.email.address.error.title";

    /**
     * Verify that the email address entered is valid
     * 
     * @param input
     *            the component (textfield) containing the email address
     * @return true if valid
     */
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        input.setBorder(UIManager.getBorder("TextField.border"));
        return isEmailAddressValid(text);
    }

    /**
     * Checks is the component should yield focus
     * 
     * @param input
     *            the component to verify
     * @return true if verified
     */
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = super.shouldYieldFocus(input);
        JTextField tf = (JTextField) input;

        if (!valid) {
            tf.setBorder(BorderFactory.createLineBorder(Color.red));
            tf.requestFocus();
            JOptionPane.showMessageDialog(input, XHIBITConstant.getResource(XhibitBundles.ErrorText, ERROR_MESSAGE),
                    XHIBITConstant.getResource(XhibitBundles.ErrorText, ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
        }
        return valid;
    }

    /**
     * Validates to check if the string is a valid email address. This just
     * checks the format, not the actual address.
     * 
     * @param emailAddress
     *            String to be validated.
     * @return true if field contents are valid.
     * @author xzfdtb
     */

    public static boolean isEmailAddressValid(String emailAddress) {

        // Check there are no quotes in the address
        int k = 0;
        if (emailAddress.indexOf('"') >= 0) {
            return false;
        }

        int i;
        for (k = 0; (i = indexOfAny(emailAddress, ",:", k)) >= 0; k = i + 1) {
            if (emailAddress.charAt(k) != '@') {
                log.error("Illegal address ! " + emailAddress);
                return false;
            }
            if (emailAddress.charAt(i) != ':')
                continue;
            k = i + 1;
            break;
        }

        int j;
        String s1;
        String s2;
        if ((j = emailAddress.indexOf('@', k)) >= 0) {
            if (j == k) {
                log.error("Missing local part of address! " + emailAddress);
                return false;
            }
            if (j == emailAddress.length() - 1) {
                log.error("Missing domain part of address! " + emailAddress);
                return false;
            }
            s1 = emailAddress.substring(k, j);
            s2 = emailAddress.substring(j + 1);
        } else {
            log.error("Missing final '@domain' " + emailAddress);
            return false;
        }
        if (indexOfAny(emailAddress, " \t\n\r") >= 0) {
            log.error("Illegal whitespace in email address! " + emailAddress);
            return false;
        }
        if (indexOfAny(s1, "()<>,;:\\\"[]@") >= 0) {
            log.error("Illegal character in local part of address! " + emailAddress);
            return false;
        }
        if (s2 != null && s2.indexOf('[') < 0 && indexOfAny(s2, "()<>,;:\\\"[]@") >= 0) {
            log.error("Illegal character in domain part of address! " + emailAddress);
            return false;
        }

        return true;
    }

    private static int indexOfAny(String s, String s1) {
        return indexOfAny(s, s1, 0);
    }

    private static int indexOfAny(String s, String s1, int i) {
        try {
            int j = s.length();
            for (int k = i; k < j; k++)
                if (s1.indexOf(s.charAt(k)) >= 0)
                    return k;

            return -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return -1;
        }
    }
}
