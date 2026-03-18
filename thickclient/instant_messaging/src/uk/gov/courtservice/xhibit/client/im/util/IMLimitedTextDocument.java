package uk.gov.courtservice.xhibit.client.im.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * Title: IMLimitedTextDocument
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

public class IMLimitedTextDocument extends PlainDocument {
    private int maxLength = 0;

    private int keyCode;

    private IMLimitedTextDocument() {
    }

    public IMLimitedTextDocument(int len) {
        this();
        maxLength = len;
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        char[] insertChars = str.toCharArray();

        boolean valid = true;
        boolean fit = true;

        if (insertChars.length + getLength() <= maxLength) {
            super.insertString(offset, str, a);
        }
    }
}