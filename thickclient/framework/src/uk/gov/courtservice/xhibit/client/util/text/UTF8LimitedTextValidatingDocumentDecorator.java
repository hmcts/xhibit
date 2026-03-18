package uk.gov.courtservice.xhibit.client.util.text;

import java.io.UnsupportedEncodingException;

import javax.swing.text.Document;

/**
 * <p>
 * Title: Document decorator that only allows a limited number of characters
 * UTF8 to be entered.
 * </p>
 * <p>
 * Description: Validates the expected resultant text String after text is
 * entered or removed to ensure that the Document will not contian more
 * characters than the specifed maximum length.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class UTF8LimitedTextValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    /** The maximum length of the document. */
    private int maxUTF8Chars = 0;

    /**
     * Construct a UTF8limitedTextValidatingDocumentDecorator which will
     * delegate to an implementation of the Document interface with the
     * specified maximum length.
     * 
     * @param maxLength
     *            the maximum length of the document.
     */
    public UTF8LimitedTextValidatingDocumentDecorator(int maxLength) {
        super();
        maxUTF8Chars = maxLength;
    }

    /**
     * Construct a UTF8limitedTextValidatingDocumentDecorator which delegates to
     * the specified Document and with the specified maximum length.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     * @param maxLength
     *            the maximum length of the document.
     */
    public UTF8LimitedTextValidatingDocumentDecorator(Document wrappedDocument, int maxLength) {
        super(wrappedDocument);
        maxUTF8Chars = maxLength;
    }

    /**
     * ValidatingDocumentDecorator Implementation returns true if the candidate
     * length is less than or equal to the maximum length for the doucment.
     * 
     * @param candidate
     *            the text to validate.
     * @return true if the candidate text is a valid length.
     */
    public boolean validate(String candidate) {
        return getUtf8Length(candidate) <= maxUTF8Chars;
    }

    /**
     * Utility method for calculating the number of bytes used by the string
     * when encoded in UTF-8
     */

    private static int getUtf8Length(String str) {
        // Note this is not a very efficient way of calculating this but should
        // not matter
        // for the small number of strings used in judges comments if we cant
        // work it out
        // assume worst case, ie each char is encoded over 3 bytes!
        try {
            return str.length() == 0 ? 0 : str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException uee) {
            return str.length() * 3;
        }
    }
}
