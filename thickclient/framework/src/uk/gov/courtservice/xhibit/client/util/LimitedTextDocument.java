package uk.gov.courtservice.xhibit.client.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * Title: LimitedTextDocument
 * </p>
 * <p>
 * Description: Sets a limit on a text field
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
    private int maxLength = 0;

    private int keyCode;

    /**
     * Creates a PlainDocument with a limited length
     * 
     * @param len
     *            an int defining the length of the field
     */
    public LimitedTextDocument(int len) {
        maxLength = len;
    }

    /**
     * Overridden method - checks that the length of the document has not
     * exceeded the maximum, before calling the superclass method.
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