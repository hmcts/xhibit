package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters;

import uk.gov.courtservice.xhibit.business.entities.xhb_display_location.XhbDisplayLocationBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.comparables.DigitAwareStringComparable;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
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
 * @version $Id: DisplayLocationAdapter.java,v 1.5 2004/01/30 16:26:21 sz0t7n
 *          Exp $
 */

public class DisplayLocationAdapter implements AdapterSorterInterface {
    private final XhbDisplayLocationBasicValue _value;

    private final DigitAwareStringComparable _comparable;

    public DisplayLocationAdapter(XhbDisplayLocationBasicValue siteBasicValue) {
        _value = siteBasicValue;
        _comparable = new DigitAwareStringComparable(toString());
    }

    public XhbDisplayLocationBasicValue getBasicValue() {
        return _value;
    }

    public String toString() {
        return PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAY_LOCATION.concat(_value
                .getDescriptionCode()));
    }

    public Comparable getSortKey() {
        return _comparable;
    }
}