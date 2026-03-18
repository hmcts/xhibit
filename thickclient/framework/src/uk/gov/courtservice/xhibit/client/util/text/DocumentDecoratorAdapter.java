package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;

/**
 * <p>
 * Title: Adapter for Document interface to support the decorator pattern.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class provides support for the 'Decorator' style composition of
 * functionality for the Document interface.
 * </p>
 * <p>
 * In most cases you should not need to sub-class this class, but should instead
 * sub-class ValidatingDocumentDecorator or TransformingDocumentDecorator.
 * </p>
 * <p>
 * Sub-classes should override (while calling super.methodName()) to provide
 * additional functionality.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public abstract class DocumentDecoratorAdapter implements Document {
    /** The wrapped Document */
    private Document _wrappedDocument;

    /**
     * Construct a DocumentDecoratorAdapter which delegates to a PlainDocument.
     */
    public DocumentDecoratorAdapter() {
        this(null);
    }

    /**
     * Construct a DocumentDecoratorAdapter which delegates to the specified
     * document. If the document is null then use a PlainDocument.
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     */
    public DocumentDecoratorAdapter(Document wrappedDocument) {
        if (wrappedDocument == null) {
            _wrappedDocument = new PlainDocument();
        } else {
            _wrappedDocument = wrappedDocument;
        }
    }

    /**
     * Returns the number of characters of content currently in the document.
     * 
     * @return number of characters >= 0.
     */
    public int getLength() {
        return _wrappedDocument.getLength();
    }

    /**
     * Registers the given observer to begin receiving notifications when
     * changes are made to the document.
     * 
     * @param listener
     *            the observer to register.
     */
    public void addDocumentListener(DocumentListener listener) {
        _wrappedDocument.addDocumentListener(listener);
    }

    /**
     * Unregisters the given observer from the notification list so it will no
     * longer receive change updates.
     * 
     * @param listener
     *            the observer to unregister.
     */
    public void removeDocumentListener(DocumentListener listener) {
        _wrappedDocument.removeDocumentListener(listener);
    }

    /**
     * Registers the given observer to begin receiving notifications when
     * undoable edits are made to the document.
     * 
     * @param listener
     *            the observer to register.
     */
    public void addUndoableEditListener(UndoableEditListener listener) {
        _wrappedDocument.addUndoableEditListener(listener);
    }

    /**
     * Unregisters the given observer from the notification list so it will no
     * longer receive updates.
     * 
     * @param listener
     *            the observer to unregister.
     */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        _wrappedDocument.removeUndoableEditListener(listener);
    }

    /**
     * Gets properties associated with the document. Allows the storage of
     * things like the document title, author, etc.
     * 
     * @param key
     *            a non null name of a property.
     * @return the property value.
     */
    public Object getProperty(Object key) {
        return _wrappedDocument.getProperty(key);
    }

    /**
     * Puts a new property on the list.
     * 
     * @param key
     *            a non null name of a property.
     * @param value
     *            the property value.
     */
    public void putProperty(Object key, Object value) {
        _wrappedDocument.putProperty(key, value);
    }

    /**
     * Removes a portion of the content of the document. This will cause a
     * DocumentEvent of type DocumentEvent.EventType.REMOVE to be sent to the
     * registered DocumentListeners, unless an exception is thrown. The
     * notification will be sent to the listeners by calling the removeUpdate
     * method on the DocumentListeners.
     * <p>
     * If the Document supports undo/redo, an UndoableEditEvent will also be
     * generated.
     * </p>
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
    public void remove(int offset, int length) throws BadLocationException {
        _wrappedDocument.remove(offset, length);
    }

    /**
     * Inserts a string into the Document. This will cause a DocumentEvent of
     * type DocumentEvent.EventType.INSERT to be sent to the registered
     * DocumentListers, unless an exception is thrown.
     * <p>
     * If the Document supports undo/redo, an UndoableEditEvent will also be
     * generated.
     * </p>
     * 
     * @param offset
     *            the offset into the document to insert the content ( >= 0).
     *            All positions that track change at, or after the given
     *            location will move.
     * @param str
     *            the string to insert.
     * @param a
     *            the attributes to associate with the inserted content. This
     *            may be null if there are no attributes.
     * @throws BadLocationException
     *             the given insert position is not a valid position within the
     *             document
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        _wrappedDocument.insertString(offset, str, a);
    }

    /**
     * Gets the text contained within the given portion of the document.
     * 
     * @param offset
     *            the offset into the document representing the desired start of
     *            the text ( >= 0 ).
     * @param length
     *            the length of the desired string ( >= 0 ).
     * @return the text in a String of length >= 0.
     * @throws BadLocationException
     *             some portion of the given range was not a valid part of the
     *             document. The location in the exception is the first bad
     *             position encountered.
     */
    public String getText(int offset, int length) throws BadLocationException {
        return _wrappedDocument.getText(offset, length);
    }

    /**
     * Gets the text contained within the given portion of the document.
     * 
     * @param offset
     *            the offset into the document representing the desired start of
     *            the text ( >= 0 ).
     * @param length
     *            the length of the desired string ( >= 0 ).
     * @param txt
     *            the Segment object to return the text in.
     * @throws BadLocationException
     *             some portion of the given range was not a valid part of the
     *             document. The location in the exception is the first bad
     *             position encountered.
     */
    public void getText(int offset, int length, Segment txt) throws BadLocationException {
        _wrappedDocument.getText(offset, length, txt);
    }

    /**
     * Returns a position that represents the start of the document. The
     * position returned can be counted on to track change and stay located at
     * the beginning of the document.
     * 
     * @return the position.
     */
    public Position getStartPosition() {
        return _wrappedDocument.getStartPosition();
    }

    /**
     * Returns a position that represents the end of the document. The position
     * returned can be counted on to track change and stay located at the end of
     * the document.
     * 
     * @return the position.
     */
    public Position getEndPosition() {
        return _wrappedDocument.getEndPosition();
    }

    /**
     * This method allows an application to mark a place in a sequence of
     * character content. This mark can then be used to tracks change as
     * insertions and removals are made in the content. The policy is that
     * insertions always occur prior to the current position (the most common
     * case) unless the insertion location is zero, in which case the insertion
     * is forced to a position that follows the original position.
     * 
     * @param offset
     *            the offset from the start of the document ( >= 0 ).
     * @return the position.
     * @throws BadLocationException
     *             if the given position does not represent a valid location in
     *             the associated document.
     */
    public Position createPosition(int offset) throws BadLocationException {
        return _wrappedDocument.createPosition(offset);
    }

    /**
     * Returns all of the root elements that are defined. (Typically only one).
     * 
     * @return the root elements
     */
    public Element[] getRootElements() {
        return _wrappedDocument.getRootElements();
    }

    /**
     * Returns the root element that views should be based upon, unless some
     * other mechanism for assigning views to element structures is provided.
     * 
     * @return the root element.
     */
    public Element getDefaultRootElement() {
        return _wrappedDocument.getDefaultRootElement();
    }

    /**
     * This allows the model to be safely rendered in the presence of currency
     * if the model supports being updated asynchronously. The given runnable
     * will be executed in a way that allows it to safely read the model with no
     * changes while the runnable is being executed. The runnable itself may not
     * make any mutations.
     * 
     * @param r
     *            a Runnable used to render the model.
     */
    public void render(Runnable r) {
        _wrappedDocument.render(r);
    }

    /**
     * Sets the wrapped document
     * 
     * @param wrappedDocument
     */
    public void setWrappedDocument(Document wrappedDocument) {
        _wrappedDocument = wrappedDocument;
    }

}
