package uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;

/**
 * <p>
 * Title: A Display Document as part of a Rotation Set.
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * When a Display Document 'becomes' part of a rotation set it has a page delay
 * associated with it. This page delay is how long that the page(s) of the
 * Display Document are (each) displayed on the browser when it is part of the
 * Rotation Set.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class RotationSetDisplayDocument implements Serializable {
	
	static final long serialVersionUID = 4314382381407407835L;
	
    private final DisplayDocumentURI _displayDocumentURI;

    private final long _pageDelay;

    /**
     * Construct an instance of this class for inclusion in a
     * <code>DisplayRotationSetData</code> instance.
     * 
     * @param displayDocumentUri
     *            The display document.
     * @param pageDelay
     *            The time that a page of the document is displayed.
     */
    public RotationSetDisplayDocument(final DisplayDocumentURI displayDocumentUri, final long pageDelay) {
        _displayDocumentURI = displayDocumentUri;
        _pageDelay = pageDelay;
    }

    /**
     * Get details of the Display Document.
     * 
     * @return An instance of DisplayDocumentURI with details of the Display
     *         Document as part of the overall Display Rotation Set.
     */
    public DisplayDocumentURI getDisplayDocumentURI() {
        return _displayDocumentURI;
    }

    /**
     * Get how long each page of the document as rendered should be shown.
     * 
     * @return the time in ?seconds?
     */
    public long getPageDelay() {
        return _pageDelay;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param obj
     *            the reference object with which to compare.
     * 
     * @return true if this object is the same as the obj argument; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof RotationSetDisplayDocument) {
            return this.equals((RotationSetDisplayDocument) obj);
        }

        return false;
    }

    /**
     * Indicates whether some other instance of
     * <code>RotationSetDisplayDocument</code> is "equal to" this one.
     * 
     * @param testable
     *            the instance of <code>RotationSetDisplayDocument</code> with
     *            which to compare.
     * 
     * @return true if this is the same as the testable argument; false
     *         otherwise.
     */
    public boolean equals(RotationSetDisplayDocument testable) {
        return (this._pageDelay == testable._pageDelay)
                && this._displayDocumentURI.equals(testable._displayDocumentURI);
    }

    /**
     * Get the hashcode for this object.
     * 
     * @return This object's hashcode.
     * 
     * @see java.lang.Object.hashCode()
     */
    public int hashCode() {
        return (int) (_pageDelay + _displayDocumentURI.hashCode());
    }

    /**
     * Get an human readable string representation of the rotation set display
     * document.
     * 
     * @return
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        appendToBuffer(buffer, "");
        return buffer.toString();
    }

    /**
     * For the purposes of more efficient debugging, appends the string
     * representation to the supplied buffer.
     * 
     * @param buffer
     *            The buffer to append to.
     * @param lineIndent
     *            The indent to prepend to the string representation.
     */
    public void appendToBuffer(StringBuffer buffer, String lineIndent) {
        buffer.append(lineIndent);
        buffer.append("Page Delay: ").append(_pageDelay);
        buffer.append(" URI: ").append(_displayDocumentURI.toString());
        buffer.append("\n");

    }
}