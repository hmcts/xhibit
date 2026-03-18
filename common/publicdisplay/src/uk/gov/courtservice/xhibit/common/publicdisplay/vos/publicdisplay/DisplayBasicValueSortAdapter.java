package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.comparables.DigitAwareStringComparable;

/**
 * <p>
 * Title: Adapter class around XhbDisplayBasicValue so that they can be sorted
 * in a more logical order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisplayBasicValueSortAdapter.java,v 1.1 2004/01/30 16:18:18
 *          sz0t7n Exp $
 */

public class DisplayBasicValueSortAdapter extends DigitAwareStringComparable {
    
	static final long serialVersionUID = 2188624613620306735L;
	
	XhbDisplayBasicValue _value;

    public DisplayBasicValueSortAdapter(XhbDisplayBasicValue value, String displayText) {
        super(displayText);
        _value = value;
    }

    public XhbDisplayBasicValue getBasicValue() {
        return _value;
    }
}