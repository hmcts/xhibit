package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * 
 * <p>
 * Title: DateValidator
 * </p>
 * <p>
 * Description: Validates dates
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class DateValidator extends JOptionPane {
    private static final Logger log = CSServices.getLogger(DateValidator.class);

    private static final String msg = "Required format is yyyy-mm-dd";

    private static final String title = "Invalid Date";

    private SimpleDateFormat format;

    /**
     * Constructor
     * 
     * @param format
     */
    public DateValidator(String format) {
        this.format = new SimpleDateFormat(format);
    }

    /**
     * Checks if the string supplied is a valid date
     * 
     * @param text
     *            the date as a string
     * @return true if valid
     */
    public boolean isValid(String text) {
        boolean isValid = true;
        if (text.trim().length() == 10) {
            try {
                format.parse(text);
                if ((validateDateSection(text.substring(0, 4))) && (validateDateSection(text.substring(5, 7)))
                        && (validateDateSection(text.substring(8, 10)))) {
                    // Do nothing
                } else {
                    isValid = false;
                }
            } catch (ParseException pe) {
                // not a valid date format.
                isValid = false;
            }
        } else {

            isValid = false;
        }

        if (!isValid) {
            this.showMessageDialog(null, this.msg, this.title, JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }

    /**
     * Checks that the portion of the date supplied is numeric
     * 
     * @param date
     *            the date as a string
     * @return true if valid
     */
    private boolean validateDateSection(String date) {
        boolean isValid = true;
        for (int j = 0; j < date.length(); j++) {
            try {
                String s = "" + date.charAt(j);
                int i = Integer.parseInt(s);
            } catch (Exception nfe) {
                isValid = false;
            }

        }
        return isValid;
    }

}
