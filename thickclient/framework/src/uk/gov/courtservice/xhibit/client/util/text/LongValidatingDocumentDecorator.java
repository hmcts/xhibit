package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;

/**
 * <p>
 * Title: Document decorator that only allows Long numbers to be entered.
 * </p>
 * <p>
 * Description: Validates the expected resultant text String after text is
 * entered or removed to ensure that the Document will only contain a valid Long
 * number.
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
public class LongValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    /**
     * Construct a LongValidatingDocumentDecorator which will delegate to an
     * implementation of the Document interface.
     */
    public LongValidatingDocumentDecorator() {
        super();
    }

    /**
     * Construct a LongValidatingDocumentDecorator which delegates to the
     * specified Document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public LongValidatingDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * ValidatingDocumentDecorator Implementation returns true if the candidate
     * text:<br>
     * 1. length equals zero or<br>
     * 2. is a minus sign (i.e. the first character entered or the only
     * remaining character is a minus sign) or<br>
     * 3. is a valid Long.
     * 
     * @param candidate
     *            the text to validate.
     * @return true if the candidate text is valid.
     */
    public boolean validate(String candidate) {
        if (candidate.length() == 0 || candidate.equals("-")) {
            return true;
        } else {
            try {
                Long.parseLong(candidate);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
    }
}