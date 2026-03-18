package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * <p>
 * Title: Document Decorator to transform text before it is inserted into a
 * Document.
 * </p>
 * <p>
 * Description: Used for simplifying the creation of transforming document
 * decorators. Extend this class and implement the transform method to perform
 * the desired transformation of text that is to be inserted into a document.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public abstract class TransformingDocumentDecorator extends DocumentDecoratorAdapter {
    /**
     * Construct a TransformingDocumentDecorator which delegates to an
     * implementation of the Document interface.
     */
    public TransformingDocumentDecorator() {
        super();
    }

    /**
     * Construct a TransformingDocumentDecorator which delegates to the
     * specified Document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public TransformingDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * Transforms the text that is to be inserted into the document. i.e. only
     * transforms the text entered, not the whole Document.
     * 
     * @param enteredText
     *            text (e.g. that was typed or pasted) to be inserted into the
     *            document.
     * @return the transformed text.
     */
    public abstract String transform(String enteredText);

    /**
     * Inserts a string into the document. This will cause a DocumentEvent of
     * type DocumentEvent.EventType.INSERT to be sent to the registered
     * DocumentListers, unless an exception is thrown. If the Document supports
     * undo/redo, an UndoableEditEvent will also be generated.
     * 
     * @param offset
     *            the offset into the document to insert the content str, length >=
     *            0. All positions that track change at or after the given
     *            location will move.
     * @param str
     *            the string to insert.
     * @param a
     *            the attributes to associate with the inserted content. This
     *            may be null if there are no attributes.
     * @throws BadLocationException
     *             the given insert position is not a valid position within the
     *             document.
     */
    public final void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, transform(str), a);
    }
}
