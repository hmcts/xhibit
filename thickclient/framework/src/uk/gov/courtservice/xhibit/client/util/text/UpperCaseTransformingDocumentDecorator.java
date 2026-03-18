package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;

/**
 * <p>
 * Title: Document decorator to transform the text inserted into a Document to
 * upper case.
 * </p>
 * <p>
 * Description: Transforms text entered into a Document to upper case.
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

public class UpperCaseTransformingDocumentDecorator extends TransformingDocumentDecorator {
    /**
     * Construct an UpperCaseTransformingDocumentDecorator which will delegate
     * to an implementation of the Document interface.
     */
    public UpperCaseTransformingDocumentDecorator() {
        super();
    }

    /**
     * Construct an UpperCaseTransformingDocumentDecorator which delegates to
     * the specified Document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public UpperCaseTransformingDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * TransformingDocumentDecorator Implementation transforms the text entered
     * to upper case. i.e. only transforms the text entered, not the whole
     * Document.
     * 
     * @param enteredText
     *            the entered text to transform to upper case.
     * @return the fragment text transformed to upper case.
     */
    public String transform(String enteredText) {
        return enteredText != null ? enteredText.toUpperCase() : null;
    }
}
