package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;

/**
 * <p>
 * Title: Document decorator that only allows a limited number of characters to
 * be entered.
 * </p>
 * <p>
 * Description: Validates the expected resultant text String after text is
 * entered or removed to ensure that the Document will not contian more
 * characters than the specifed maximum length.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class LimitedTextValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    /** The maximum length of the document. */
    private int _maxLength = 0;

    /**
     * Construct a LimitedTextValidatingDocumentDecorator which will delegate to
     * an implementation of the Document interface with the specified maximum
     * length.
     * 
     * @param maxLength
     *            the maximum length of the document.
     */
    public LimitedTextValidatingDocumentDecorator(int maxLength) {
        super();
        _maxLength = maxLength;
    }

    /**
     * Construct a LimitedTextValidatingDocumentDecorator which delegates to the
     * specified Document and with the specified maximum length.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     * @param maxLength
     *            the maximum length of the document.
     */
    public LimitedTextValidatingDocumentDecorator(Document wrappedDocument, int maxLength) {
        super(wrappedDocument);
        _maxLength = maxLength;
    }

    /**
     * ValidatingDocumentDecorator Implementation returns true if the candidate
     * length is less than or equal to the maximum length for the doucment.
     * 
     * @param candidate
     *            the text to validate.
     * @return true if the candidate text is a valid length.
     * @throws NullPointerException
     *             if candidate is null.
     */
    public boolean validate(String candidate) {
        return candidate.length() <= _maxLength;
    }
}