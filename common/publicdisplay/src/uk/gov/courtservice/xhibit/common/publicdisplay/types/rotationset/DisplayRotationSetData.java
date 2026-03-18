package uk.gov.courtservice.xhibit.common.publicdisplay.types.rotationset;

import java.io.Serializable;
import java.util.Arrays;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayURI;

/**
 * <p>
 * Title: Rotation Set data for a Display.
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * This class encapsulates the data about how a Display is configured with
 * respect to a Rotation Set. It is explicitly designed to provide the view of
 * the configuration data most suitable for driving the presentation tier.
 * </p>
 * 
 * <p>
 * This class is designed to be immutable and so makes certain assumptions about
 * caching a pre-calculated hash. If this class should be made mutable please
 * remove the hash pre-calculation.
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
public class DisplayRotationSetData implements Serializable {
	
	static final long serialVersionUID = 6656214779744672384L;
	
    private final DisplayURI _displayURI;

    private final RotationSetDisplayDocument[] _rotationSetDisplayDocuments;

    private final int _displayId;

    private final String _displayType;

    private final int _rotationSetId;

    private int _hashCode;

    /**
     * Constructor for the data element of a Display Rotation Set.
     * 
     * @param displayURI
     *            The URI of the display it is associated with.
     * @param displayDocumentURIs
     *            The URI(s) of the Display Documents.
     * @param displayId
     *            The database ID of the display.
     * @param rotationSetId
     *            The database ID of the Rotation Set.
     * 
     * @pre displayDocumentURIs != null
     * @pre forall RotationSetDisplayDocument doc in displayDocumentURIs | doc !=
     *      null
     * @pre displayURI != null
     */
    public DisplayRotationSetData(DisplayURI displayURI, RotationSetDisplayDocument[] displayDocumentURIs,
            int displayId, int rotationSetId, String displayType) {
        _displayURI = displayURI;
        _rotationSetDisplayDocuments = displayDocumentURIs;
        _displayId = displayId;
        _rotationSetId = rotationSetId;
        _hashCode = calculateHashcode();
        _displayType = displayType;
    }

    /**
     * Get the ID of the Display 'embodied' by this class.
     * 
     * @return the database ID of the Display
     */
    public int getDisplayId() {
        return _displayId;
    }

    /**
     * Get the URI of the display for which this Display Rotation Set applies.
     * 
     * @return The instance of <code>DisplayURI</code> for the particular
     *         Display.
     */
    public DisplayURI getDisplayURI() {
        return _displayURI;
    }

    /**
     * Gets the objects representing the Display Documents making up the
     * rotation set as applied to the Display.
     * 
     * @return an array of <code>RotationSetDisplayDocument</code>
     *         representing the Display Documents in this Display Rotation Set.
     */
    public RotationSetDisplayDocument[] getRotationSetDisplayDocuments() {
        return (RotationSetDisplayDocument[]) _rotationSetDisplayDocuments.clone();
    }

    /**
     * Get the ID of the Rotation Set 'embodied' by this class.
     * 
     * @return the database ID of the Rotation Set
     */
    public int getRotationSetId() {
        return _rotationSetId;
    }

    /**
     * Get the code representing the type of the display.
     * 
     * @return the code representing the type of the display.
     */
    public String getDisplayType() {
        return _displayType;
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
        if (obj instanceof DisplayRotationSetData) {
            return this.equals((DisplayRotationSetData) obj);
        }

        return false;
    }

    /**
     * Indicates whether some other instance of
     * <code>DisplayRotationSetData</code> is "equal to" this one.
     * 
     * @param testable
     *            the reference object with which to compare.
     * 
     * @return true if this object is the same as the obj argument; false
     *         otherwise.
     */
    public boolean equals(DisplayRotationSetData testable) {
        // ASSERT _displayURI.equals(testable._displayURI);
        // IF _displayId == testable._displayId;
        return (_displayId == testable._displayId) && (_rotationSetId == testable._rotationSetId)
                && Arrays.equals(_rotationSetDisplayDocuments, testable._rotationSetDisplayDocuments);
    }

    /**
     * Get the hashcode for this object.
     * 
     * @return This object's hashcode.
     * 
     * @see java.lang.Object.hashCode()
     */
    public int hashCode() {
        return _hashCode;
    }

    /**
     * Calculate the hashcode for this object.
     * 
     * @return the calculated hash code.
     * 
     * @pre forall RotationSetDisplayDocument doc in
     *      _rotationSetDisplayDocuments | doc != null
     */
    private int calculateHashcode() {
        // Expect overflow here, but for the purpose of a hash, it is fine.
        long sum = _displayId + _rotationSetId + _displayURI.hashCode();

        for (int i = _rotationSetDisplayDocuments.length - 1; i >= 0; i--) {
            sum += _rotationSetDisplayDocuments[i].hashCode();
        }

        return (int) sum;
    }
}
