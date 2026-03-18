package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_display_location.XhbDisplayLocationBasicValue;

/**
 * <p>
 * Title: Display Location Complex Value
 * </p>
 * 
 * <p>
 * Description: VO that holds information about the location and the display
 * within that location
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
 * @version $Id: DisplayLocationComplexValue.java,v 1.1 2004/01/15 10:34:19
 *          rz3jq5 Exp $
 */
public class DisplayLocationComplexValue extends CSAbstractValue {
	
	static final long serialVersionUID = -7278823187046747441L;
	
    private XhbDisplayLocationBasicValue displayLocationBasicValue;

    private XhbDisplayBasicValue[] displayBasicValue;

    /**
     * Get the list of displays in the location
     * 
     * @return array of display basic values
     */
    public XhbDisplayBasicValue[] getDisplayBasicValue() {
        return displayBasicValue;
    }

    /**
     * Set the displays in the location
     * 
     * @param displayBasicValue
     *            array of display basic values
     */
    public void setDisplayBasicValues(XhbDisplayBasicValue[] displayBasicValue) {
        this.displayBasicValue = displayBasicValue;
    }

    /**
     * Set a location
     * 
     * @param displayLocationBasicValue
     *            the location basic value
     */
    public void setDisplayLocationBasicValue(XhbDisplayLocationBasicValue displayLocationBasicValue) {
        this.displayLocationBasicValue = displayLocationBasicValue;
    }

    /**
     * Get information about the location
     * 
     * @return XhbDisplayLocationBasicValue
     */
    public XhbDisplayLocationBasicValue getDisplayLocationBasicValue() {
        return displayLocationBasicValue;
    }

    /**
     * Override the equals method to compare the id's of the associated basic
     * value
     * 
     * @param complexValue
     *            A DisplayLocationComplexValue
     * 
     * @return
     */
    public boolean equals(Object complexValue) {
        if ((displayLocationBasicValue != null) && (complexValue instanceof DisplayLocationComplexValue)
                && (((DisplayLocationComplexValue) complexValue).displayLocationBasicValue != null)) {
            return ((DisplayLocationComplexValue) complexValue).displayLocationBasicValue
                    .equals(displayLocationBasicValue);
        } else {
            return false;
        }
    }
}
