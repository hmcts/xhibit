package uk.gov.courtservice.xhibit.client.util.text;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * <p>
 * Title: Document Decorator to validate the Document upon trying to insert or
 * remove text.
 * </p>
 * <p>
 * Description: Used for simplifying the creation of validating document
 * decorators. Extend this class and implement the validate method to perform
 * the desired validation of the document.
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
public abstract class ValidatingDocumentDecorator extends DocumentDecoratorAdapter {
    /**
     * Construct a ValidatingDocumentDecorator which delegates to an
     * inplementation of the Document interface.
     */
    public ValidatingDocumentDecorator() {
        super();
    }

    /**
     * Construct a ValidatingDocumentDecorator which delegates to the specified
     * document.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public ValidatingDocumentDecorator(Document wrappedDocument) {
        super(wrappedDocument);
    }

    /**
     * Should return true if the text to be inserted into or removed from the
     * document would result in a valid document.
     * 
     * @param candidate
     *            proposed text after text is inserted into or removed from the
     *            document.
     * @return true if valid.
     */
    public abstract boolean validate(String candidate);

    /**
     * Inserts a string into the document. This will cause a DocumentEvent of
     * type DocumentEvent.EventType.INSERT to be sent to the registered
     * DocumentListers, unless an exception is thrown. If the Document supports
     * undo/redo, an UndoableEditEvent will also be generated.
     * 
     * @param offset
     *            the offset into the document to insert the content str, whose
     *            length >= 0. All positions that track change at or after the
     *            given location will move.
     * @param str
     *            the string to insert.
     * @param a
     *            the attributes to associate with the inserted content. This
     *            may be null if there are no attributes.
     * @throws BadLocationException
     *             the given insert position is not a valid position within the
     *             document
     */
    public final void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offset);
        String afterOffset = currentText.substring(offset, currentText.length());
        String proposedResult = beforeOffset + str + afterOffset;

        if (validate(proposedResult)) {
            super.insertString(offset, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Removes a portion of the content of the document. This will cause a
     * DocumentEvent of type DocumentEvent.EventType.REMOVE to be sent to the
     * registered DocumentListeners, unless an exception is thrown. The
     * notification will be sent to the listeners by calling the removeUpdate
     * method on the DocumentListeners. If the Document supports undo/redo, an
     * UndoableEditEvent will also be generated.
     * 
     * @param offset
     *            the offset from the begining ( >= 0 )
     * @param length
     *            the number of characters to remove ( >= 0 )
     * @throws BadLocationException
     *             some portion of the removal range was not a valid part of the
     *             document. The location in the exception is the first bad
     *             position encountered.
     */
    public final void remove(int offset, int length) throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offset);
        String afterOffset = currentText.substring(length + offset, currentText.length());
        String proposedResult = beforeOffset + afterOffset;

        if (validate(proposedResult)) {
            super.remove(offset, length);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
