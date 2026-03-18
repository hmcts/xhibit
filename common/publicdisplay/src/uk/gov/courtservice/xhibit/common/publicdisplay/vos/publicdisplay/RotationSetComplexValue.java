package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import java.util.HashSet;
import java.util.Iterator;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;

/**
 * <p>
 * Title: Rotation Set Complex Value
 * </p>
 * 
 * <p>
 * Description: Holds information to identify the rotation, the display pages
 * within that rotation set (with ordering and delays) and a list of screens the
 * rotation set is assigned to
 * </p>
 * 
 * <p>
 * This VO can be used for creating a new rotation set. Simply add a rotation
 * set basic value for the name and an array of RotationSetDDComplexValue's to
 * identify the display documents, ordering and delay.<br>
 * The list of screens can be left null as these are not required or update
 * </p>
 * 
 * <p>
 * This VO can also be used for updating a rotation set. Update the rotation set
 * basic value with the new name of the rotation set and/or modify the list of
 * RotationSetDDComplexValue's to change the display documents, ordering and/or
 * delay.
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
 * @version $Id: RotationSetComplexValue.java,v 1.5 2005/02/10 13:51:06 sz0t7n
 *          Exp $
 */
public class RotationSetComplexValue extends CSAbstractValue {
	
	static final long serialVersionUID = -1474521472546880003L;
	
    /**
     * Hashset used to store the display documents. This will prevent the
     * display document being repeated in the rotation set
     */
    private HashSet rotationSetDDComplexValues = new HashSet();

    private XhbRotationSetBasicValue rotationSetBasicValue;

    private DisplayBasicValueSortAdapter[] displayBasicValue;

    /**
     * Delegated method that gets the court Id from the rotation set basic value
     * 
     * @return Court Id the rotation set is defined for
     */
    public Integer getCourtId() {
        return rotationSetBasicValue.getCourtId();
    }

    /**
     * Set a list of displays that have this rotation set assigned This will be
     * set by the uk.gov.courtservice.xhibit.business.services.publicdisplay and
     * is not required to be set or amended by any GUI components.
     * 
     * @param displayBasicValue
     */
    public void setDisplayBasicValues(DisplayBasicValueSortAdapter[] displayBasicValue) {
        this.displayBasicValue = displayBasicValue;
    }

    /**
     * Return the list of displays that have this rotation set assigned
     * 
     * @return
     */
    public DisplayBasicValueSortAdapter[] getDisplayBasicValues() {
        return displayBasicValue;
    }

    /**
     * Set the rotation set. When creating a new rotation set, the basic value
     * MUST have the court id populated
     * 
     * @param rotationSetBasicValue
     *            the rotation set information
     */
    public void setRotationSetBasicValue(XhbRotationSetBasicValue rotationSetBasicValue) {
        this.rotationSetBasicValue = rotationSetBasicValue;
    }

    /**
     * Get the rotation set that this VO applies to
     * 
     * @return
     */
    public XhbRotationSetBasicValue getRotationSetBasicValue() {
        return rotationSetBasicValue;
    }

    /**
     * Add an array of RotationSetDDComplexValue's in one go. This will clear
     * the internal hashset before adding this list
     * 
     * @param newRotationSetDDComplexValues
     *            array of documents to hold
     */
    public void setRotationSetDDComplexValues(RotationSetDDComplexValue[] newRotationSetDDComplexValues) {
        rotationSetDDComplexValues.clear();

        for (int i = 0; i < newRotationSetDDComplexValues.length; i++) {
            addRotationSetDDComplexValue(newRotationSetDDComplexValues[i]);
        }
    }

    /**
     * Get the current list of display documents with ordering and delay
     * information
     * 
     * @return array of RotationSetDDComplexValues
     */
    public RotationSetDDComplexValue[] getRotationSetDDComplexValues() {
        return ((RotationSetDDComplexValue[]) rotationSetDDComplexValues
                .toArray(new RotationSetDDComplexValue[rotationSetDDComplexValues.size()]));
    }

    /**
     * Returns a rotation set Dd for the passed id
     * 
     * @param rotationSetDdId
     * 
     * @return
     */
    public XhbRotationSetDdBasicValue getRotationSetDd(Integer rotationSetDdId) {
        Iterator rotationSetDdIter = this.rotationSetDDComplexValues.iterator();

        while (rotationSetDdIter.hasNext()) {
            RotationSetDDComplexValue rotationSetDd = (RotationSetDDComplexValue) rotationSetDdIter.next();

            if (rotationSetDdId.equals(rotationSetDd.getRotationSetDdId())) {
                return rotationSetDd.getRotationSetDDBasicValue();
            }
        }

        return null;
    }

    /**
     * Delegated method call that gets the rotation set id from the rotation set
     * basic value. Note: this may return null if the rotation set has not been
     * saved.
     * 
     * @return The rotation set Id this VO belongs to
     */
    public Integer getRotationSetId() {
        return rotationSetBasicValue.getRotationSetId();
    }

    /**
     * Add a single item. Useful if iterating throw collections and optionally
     * choosing which to add This will append to the existing list.
     * 
     * @param rotationSetDDComplexValue
     *            Document to add
     */
    public void addRotationSetDDComplexValue(RotationSetDDComplexValue rotationSetDDComplexValue) {
        rotationSetDDComplexValues.add(rotationSetDDComplexValue);
    }

    /**
     * Checks whether the rotation set dd is there
     * 
     * @param rotationSetDdId
     * 
     * @return
     */
    public boolean hasRotationSetDd(Integer rotationSetDdId) {
        Iterator rotationSetDdIter = this.rotationSetDDComplexValues.iterator();

        while (rotationSetDdIter.hasNext()) {
            RotationSetDDComplexValue rotationSetDd = (RotationSetDDComplexValue) rotationSetDdIter.next();

            if (rotationSetDdId.equals(rotationSetDd.getRotationSetDdId())) {
                return true;
            }
        }

        return false;
    }
}
