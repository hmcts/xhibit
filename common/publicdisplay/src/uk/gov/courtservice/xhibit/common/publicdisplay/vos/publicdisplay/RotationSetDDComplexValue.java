package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;

/**
 * <p>
 * Title: Rotation Set / Display Document complex value
 * </p>
 * 
 * <p>
 * Description: This VO holds information from the rotation set DD basic value
 * for ordering and delay, and the display document basic value for identifying
 * the document.
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
 * @version $Id: RotationSetDDComplexValue.java,v 1.2 2004/03/29 14:09:57 pznwc5
 *          Exp $
 */
public class RotationSetDDComplexValue extends CSAbstractValue {
	
	static final long serialVersionUID = -6304749725662813977L;
	
    private XhbDisplayDocumentBasicValue displayDocumentBasicValue;

    private XhbRotationSetDdBasicValue rotationSetDDBasicValue;

    /**
     * Create the complex value with the display document and the rotation set
     * DD
     * 
     * @param rotationSetDDBasicValue
     *            the many-to-many relationship
     * @param displayDocumentBasicValue
     *            the display document
     */
    public RotationSetDDComplexValue(XhbRotationSetDdBasicValue rotationSetDDBasicValue,
            XhbDisplayDocumentBasicValue displayDocumentBasicValue) {
        setRotationSetDDBasicValue(rotationSetDDBasicValue);
        setDisplayDocumentBasicValue(displayDocumentBasicValue);
    }

    /**
     * Set the display document. This should not be updated in the GUI, only by
     * the uk.gov.courtservice.xhibit.business.services.publicdisplay
     * 
     * @param displayDocumentBasicValue
     *            display document for the XhbRotationSetDdBasicValue
     */
    public void setDisplayDocumentBasicValue(XhbDisplayDocumentBasicValue displayDocumentBasicValue) {
        this.displayDocumentBasicValue = displayDocumentBasicValue;
    }

    /**
     * Get the document
     * 
     * @return the display document
     */
    public XhbDisplayDocumentBasicValue getDisplayDocumentBasicValue() {
        return displayDocumentBasicValue;
    }

    /**
     * Delegated method call that gets the display document id from the
     * RotationSetDDBasicValue
     * 
     * @return Primary key of the display document
     */
    public Integer getDisplayDocumentId() {
        return displayDocumentBasicValue.getDisplayDocumentId();
    }

    /**
     * Set a new delay or order
     * 
     * @param rotationSetDDBasicValue
     *            new value
     */
    public void setRotationSetDDBasicValue(XhbRotationSetDdBasicValue rotationSetDDBasicValue) {
        this.rotationSetDDBasicValue = rotationSetDDBasicValue;
    }

    /**
     * Get the many-to-many relationship information of rotation set to display
     * document. This will give ordering and delay.
     * 
     * @return XhbRotationSetDdBasicValue
     */
    public XhbRotationSetDdBasicValue getRotationSetDDBasicValue() {
        return rotationSetDDBasicValue;
    }

    /**
     * Delegated method call that gets the rotation set Id from the
     * RotationSetDDBasicValue
     * 
     * @return Primary key of the rotation set
     */
    public Integer getRotationSetDdId() {
        return rotationSetDDBasicValue.getRotationSetDdId();
    }

    /**
     * Override the equals method to compare the id's of the associated basic
     * value only if both primary keys are not null (this may not be the case if
     * a new RotationSetDDBasicValue is being added. In this case, the display
     * document id's are compared as these can never be null.
     * 
     * @param complexValue
     *            A DisplayLocationComplexValue
     * 
     * @return true if the object with same rotation set dd primary key or same
     *         display document primary key
     */
    public boolean equals(Object complexValue) {
        if ((rotationSetDDBasicValue != null) && complexValue instanceof RotationSetDDComplexValue
                && (((RotationSetDDComplexValue) complexValue).rotationSetDDBasicValue != null)) {
            if ((((RotationSetDDComplexValue) complexValue).rotationSetDDBasicValue.getPrimaryKey() != null)
                    && (rotationSetDDBasicValue.getPrimaryKey() != null)) {
                return ((RotationSetDDComplexValue) complexValue).rotationSetDDBasicValue
                        .equals(rotationSetDDBasicValue);
            } else {
                return ((RotationSetDDComplexValue) complexValue).getDisplayDocumentId().equals(
                        rotationSetDDBasicValue.getDisplayDocumentId());
            }
        } else {
            return false;
        }
    }
}
