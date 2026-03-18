package uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.DisplayRotationSetData;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset.RotationSetDisplayDocument;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.AbstractRenderAndStoreableType;

/**
 * <p/> Title: A concrete rotation set linked to a display.
 * </p>
 * <p/> <p/> Description: A DisplayRotationSet encapsulates the list of concrete
 * DisplayDocuments and their timings. It is uniquely described by the
 * DisplayURI contained within the DisplayRotationSetData. The
 * DisplayRotationSetData contained within this class actually contains the
 * DisplayDocumentURIs and timings whereas this class provides all the lifecycle
 * functionality so that it can be passed through a workflow.
 * </p>
 * <p>
 * Note that there is no direct relationship between a DisplayDocument and a
 * DisplayRotationSet. This may seem unusual however they never interact
 * directly it is indirectly through their 'primary keys' the URIs that there is
 * a relationship.
 * </p>
 * <p>
 * The lifecycle of a DisplayRotationSet is simpler than it's counterpart the
 * DisplayDocument, it is first rendered through the render() method and then
 * stored through the store() method. The workflow methods create() and remove()
 * are implemented, the create() method calls the render() and store() methods;
 * the remove() method is in the superclass as this is common to
 * DisplayDocuments and DisplayRotationSets.
 * </p>
 * <p/> Copyright: Copyright (c) 2004
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.9 $
 */
public class DisplayRotationSet extends AbstractRenderAndStoreableType {
    private DisplayRotationSetData displayRotationSetData;

    private String renderedString;

    /**
     * Creates a new DisplayRotationSet object.
     * 
     * @param displayRotationSetData
     *            the actual data that pertains to the rotation set.
     */
    public DisplayRotationSet(final DisplayRotationSetData displayRotationSetData) {
        this.displayRotationSetData = displayRotationSetData;
    }

    /**
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable#getRenderableType()
     */
    public String getRenderableType() {
        return Renderable.ROTATION_SET;
    }

    /**
     * Returns an array of RotationSetDisplayDocuments for this rotation set.
     * These are coupling of a DisplayDocument with it's delay.
     * 
     * @return the RotationSetDisplayDocuments within this DisplayRotationSet.
     */
    public RotationSetDisplayDocument[] getRotationSetDisplayDocuments() {
        return displayRotationSetData.getRotationSetDisplayDocuments();
    }

    /**
     * Get the type of the display.
     * 
     * @return a string representing the type of the display.
     */
    public String getDisplayType() {
        return displayRotationSetData.getDisplayType();
    }

    /**
     * @see uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.Identifiable#getUri()
     */
    public AbstractURI getUri() {
        return displayRotationSetData.getDisplayURI();
    }

    /**
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.Createable#create()
     */
    public void create() {
        render();
        store();
    }

    /**
     * Returns the contents of the rotation set in a debuggable format.
     * 
     * @return the contents of the rotation set in a debuggable format.
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

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
        buffer.append(getUri().toString()).append("\n");
        RotationSetDisplayDocument[] rsDisplayDocuments = getRotationSetDisplayDocuments();
        lineIndent += "\t";
        for (int j = 0; j < rsDisplayDocuments.length; j++) {
            rsDisplayDocuments[j].appendToBuffer(buffer, lineIndent);
        }

    }
}
