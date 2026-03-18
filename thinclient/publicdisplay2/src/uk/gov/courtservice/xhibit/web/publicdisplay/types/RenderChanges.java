package uk.gov.courtservice.xhibit.web.publicdisplay.types;

import java.util.HashSet;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.document.DisplayDocument;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset.DisplayRotationSet;

/**
 * <p>
 * Title: Changes to rendering.
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * This class is used to carry details of changes to rendering, usually
 * resulting from changes to the display/rotation set configuration made by the
 * end user.
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
public class RenderChanges {
    private final HashSet displayRotationSetsToStartRendering = new HashSet();

    private final HashSet displayRotationSetsToStopRendering = new HashSet();

    private final HashSet documentsToStartRendering = new HashSet();

    private final HashSet documentsToStopRendering = new HashSet();

    /**
     * The new display rotation sets to render.
     * 
     * @return an array of DisplayRotationSet-s which need rendering.
     */
    public DisplayRotationSet[] getDisplayRotationSetsToStartRendering() {
        return (DisplayRotationSet[]) displayRotationSetsToStartRendering
                .toArray(new DisplayRotationSet[displayRotationSetsToStartRendering.size()]);
    }

    /**
     * The display rotation that no longer need rendering.
     * 
     * @return an array of DisplayRotationSet-s which no longer need rendering.
     */
    public DisplayRotationSet[] getDisplayRotationSetsToStopRendering() {
        return (DisplayRotationSet[]) displayRotationSetsToStopRendering
                .toArray(new DisplayRotationSet[displayRotationSetsToStopRendering.size()]);
    }

    /**
     * The documents that need to be rendererd.
     * 
     * @return The documents that need to be rendererd.
     */
    public DisplayDocument[] getDocumentsToStartRendering() {
        return (DisplayDocument[]) documentsToStartRendering.toArray(new DisplayDocument[documentsToStartRendering
                .size()]);
    }

    /**
     * The documents that need to be removed from the rendering remove.
     * 
     * @return an array of DisplayDocument-s that need to be removed from the
     *         rendering remove.
     */
    public DisplayDocument[] getDocumentsToStopRendering() {
        return (DisplayDocument[]) documentsToStopRendering
                .toArray(new DisplayDocument[documentsToStopRendering.size()]);
    }

    /**
     * Adds a document to the list of documents that need to be rendered.
     * 
     * @param doc
     *            document to be rendered.
     * 
     * @return true if the document was not already in the list.
     */
    public boolean addStartDocument(final DisplayDocumentURI doc) {
        return documentsToStartRendering.add(new DisplayDocument(doc));
    }

    /**
     * Adds a set to the list of rotation sets that need to be rendered.
     * 
     * @param set
     *            the rotation set.
     * 
     * @return true if the set was not already in the list.
     */
    public boolean addStartRotationSet(final DisplayRotationSetData set) {
        return displayRotationSetsToStartRendering.add(new DisplayRotationSet(set));
    }

    /**
     * Adds a document to the list of documents that should nolonger be
     * rendered.
     * 
     * @param doc
     *            document to stop rendereding.
     * 
     * @return true if the document was not already in the list.
     */
    public boolean addStopDocument(final DisplayDocumentURI doc) {
        return documentsToStopRendering.add(new DisplayDocument(doc));
    }

    /**
     * Adds a set to the list of rotation sets that should nolonger be rendered.
     * 
     * @param set
     *            the rotation set.
     * 
     * @return true if the set was not already in the list.
     */
    public boolean addStopRotationSet(final DisplayRotationSetData set) {
        return displayRotationSetsToStopRendering.add(new DisplayRotationSet(set));
    }

    /**
     * Produce a string representation of the render changes.
     * 
     * @return a string representation of the render changes.
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
    private void appendToBuffer(StringBuffer buffer, String lineIndent) {
        buffer.append("RenderChanges:\n");
        buffer.append("Display documents to start rendering:\n");
        appendDisplayDocuments(buffer, this.getDocumentsToStartRendering(), lineIndent);
        buffer.append("Display documents to stop rendering:\n");
        appendDisplayDocuments(buffer, this.getDocumentsToStopRendering(), lineIndent);
        buffer.append("Display Rotation Sets to start rendering:\n");
        appendDisplayRotationSets(buffer, this.getDisplayRotationSetsToStartRendering(), lineIndent);
        buffer.append("Display Rotation Sets to stop rendering:\n");
        appendDisplayRotationSets(buffer, this.getDisplayRotationSetsToStopRendering(), lineIndent);
    }

    private void appendDisplayDocuments(StringBuffer buffer, DisplayDocument[] displayDocuments, String lineIndent) {
        lineIndent += "\t";
        for (int i = 0; i < displayDocuments.length; i++) {
            buffer.append(lineIndent);
            buffer.append(displayDocuments[i].getUri().toString()).append("\n");
        }
    }

    private void appendDisplayRotationSets(StringBuffer buffer, DisplayRotationSet[] displayRotationSets,
            String lineIndent) {
        lineIndent += "\t";
        for (int i = 0; i < displayRotationSets.length; i++) {
            displayRotationSets[i].appendToBuffer(buffer, lineIndent);
        }
    }

}
