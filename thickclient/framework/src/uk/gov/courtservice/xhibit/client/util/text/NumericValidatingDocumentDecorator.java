package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;

/**
 * <p>
 * Title: Document decorator that only allows positive numbers to be entered.
 * </p>
 * <p>
 * Description: Validates the expected resultant text String after text is
 * entered or removed to ensure that the Document will only contain a valid
 * positive number.
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
public class NumericValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    /**
     * Construct a NumericValidatingDocumentDecorator which will delegate to an
     * implementation of the Document interface.
     */
    public NumericValidatingDocumentDecorator() {
        super();
    }

    /**
     * Construct a NumericValidatingDocumentDecorator which delegates to the
     * specified Document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public NumericValidatingDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * ValidatingDocumentDecorator Implementation returns true if the candidate
     * text:<br>
     * 1. length equals zero or<br>
     * 2. is a valid positive Long.
     * 
     * @param candidate
     *            the text (proposed result of the Document) to validate.
     * @return true if the candidate text is valid positive long number.
     */
    public boolean validate(String candidate) {
        if (candidate.length() == 0) {
            return true;
        } else {
            try {
                long number = Long.parseLong(candidate);
                if (number >= 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
    }
}