package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;

/**
 * <p>
 * Title: Display Configuration
 * </p>
 * 
 * <p>
 * Description: A display configuration defines the display (aka screen), the
 * rotation set assigned to that display, and the list of court rooms assigned
 * to that display.
 * </p>
 * 
 * <p>
 * This object is also used for updating. Changing the rotation set and/or the
 * court rooms and calling the method updateDisplayConfiguration in the
 * PDConfigurationController will update the database and cause the relevant
 * pages to be re-rendered.
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
 * @author Rakesh Lakhani
 * @version $Id: DisplayConfiguration.java,v 1.6 2006/06/05 12:28:26 bzjrnl Exp $
 */
public class DisplayConfiguration extends CSAbstractValue {
	
	static final long serialVersionUID = -912195225609058977L;

    /**
     * Use this variable for setting showUnassigned
     */
    public static final String DB_YES = "Y";

    /**
     * Use this variable for setting showUnassigned
     */
    public static final String DB_NO = "N";

    private XhbDisplayBasicValue displayBasicValue;

    private XhbRotationSetBasicValue rotationSetBasicValue;

    private XhbCourtRoomBasicValue[] courtRoomBasicValues;

    private boolean courtRoomsChanged = false;

    private boolean rotationSetChanged = false;

    /**
     * Only constructor that sets the values typically from the DB. The set
     * methods are not used as they internally set a flag that shows them as
     * modified, which is used when updating a display configuration
     */
    public DisplayConfiguration(XhbDisplayBasicValue displayBasicValue, XhbRotationSetBasicValue rotationSetBasicValue,
            XhbCourtRoomBasicValue[] courtRoomBasicValues) {
        this.displayBasicValue = displayBasicValue;
        this.rotationSetBasicValue = rotationSetBasicValue;
        this.courtRoomBasicValues = courtRoomBasicValues;
    }

    /**
     * Sets a new collection of court rooms for the display Internally sets a
     * flag to indicate the court rooms have changed
     * 
     * @param courtRoomBasicValues
     *            new list of court rooms
     */
    public void setCourtRoomBasicValues(XhbCourtRoomBasicValue[] courtRoomBasicValues) {
        this.courtRoomBasicValues = courtRoomBasicValues;
        setCourtRoomsChanged(true);
    }

    public void setShowUnassigned(String showUnassigned) {
        this.displayBasicValue.setShowUnassignedYn(showUnassigned);
        setCourtRoomsChanged(true);
    }

    /**
     * Get the current assignment of court rooms
     * 
     * @return array of court rooms
     */
    public XhbCourtRoomBasicValue[] getCourtRoomBasicValues() {
        return courtRoomBasicValues;
    }

    /**
     * Query if there is a new list of court rooms
     * 
     * @return true if new court rooms
     */
    public boolean isCourtRoomsChanged() {
        return courtRoomsChanged;
    }

    /**
     * Returns information about the display
     * 
     * @return XhbDisplayBasicValue
     */
    public XhbDisplayBasicValue getDisplayBasicValue() {
        return displayBasicValue;
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public Integer getDisplayId() {
        return displayBasicValue.getDisplayId();
    }

    // This method has been commented out as the display should not be
    // changed
    // once constructed.
    // public void setDisplayBasicValue(XhbDisplayBasicValue
    // displayBasicValue)
    // {
    // this.displayBasicValue = displayBasicValue;
    // }

    /**
     * Sets a new rotation set to the display. Internally sets a flag to
     * indicate the rotation set has changed
     * 
     * @param rotationSetBasicValue
     *            new rotation set
     */
    public void setRotationSetBasicValue(XhbRotationSetBasicValue rotationSetBasicValue) {
        this.rotationSetBasicValue = rotationSetBasicValue;
        this.displayBasicValue.setRotationSetId(rotationSetBasicValue.getPrimaryKey());
        setRotationSetChanged(true);
    }

    /**
     * Get the currently assigned rotation set
     * 
     * @return current rotation set assigned to the display
     */
    public XhbRotationSetBasicValue getRotationSetBasicValue() {
        return rotationSetBasicValue;
    }

    /**
     * Query if the rotation set has changed
     * 
     * @return true if new rotation set
     */
    public boolean isRotationSetChanged() {
        return rotationSetChanged;
    }

    /**
     * @return
     */
    public Integer getRotationSetId() {
        return rotationSetBasicValue.getRotationSetId();
    }

    /**
     * Update that a new list of court rooms has been added
     * 
     * @param courtRoomsChanged
     *            true to indicate modified
     */
    private void setCourtRoomsChanged(boolean courtRoomsChanged) {
        this.courtRoomsChanged = courtRoomsChanged;
    }

    /**
     * Update that the rotation set has been modified
     * 
     * @param rotationSetChanged
     *            true to indicate modified
     */
    private void setRotationSetChanged(boolean rotationSetChanged) {
        this.rotationSetChanged = rotationSetChanged;
    }
}
