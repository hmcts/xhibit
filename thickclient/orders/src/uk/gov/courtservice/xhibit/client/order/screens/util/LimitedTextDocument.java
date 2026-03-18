package uk.gov.courtservice.xhibit.client.order.screens.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
 * @author Neil Entwistle
 * @version 1.0
 */

public class LimitedTextDocument extends PlainDocument {
    private static final Logger log = CSServices.getLogger(LimitedTextDocument.class);

    private int maxLength = 0;

    /**
     * 
     * @param len
     */
    public LimitedTextDocument(int len) {
        maxLength = len;
    }

    /**
     * Overrides insertString method - checks that the total length is valid.
     * 
     * @param offset
     * @param str
     * @param a
     * @throws BadLocationException
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        char[] insertChars = str.toCharArray();

        boolean valid = true;
        boolean fit = true;

        if (insertChars.length + getLength() <= maxLength) {
            super.insertString(offset, str, a);
        }
    }
}