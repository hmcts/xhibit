package uk.gov.courtservice.xhibit.client.util;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * Title: Whole Number Formatter
 * </p>
 * <p>
 * Description: This field will not allow entry of non integers
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class WholeNumberDocument extends PlainDocument {
    // XWholeNumberDocument
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int j = 0;

        for (int i = 0; i < result.length; i++) {
            if (Character.isDigit(source[i]))
                result[j++] = source[i];
            else {
                Toolkit.getDefaultToolkit().beep();
                // System.err.println("insertString: " + source[i]);
            }
        }
        super.insertString(offs, new String(result, 0, j), a);
    }
}