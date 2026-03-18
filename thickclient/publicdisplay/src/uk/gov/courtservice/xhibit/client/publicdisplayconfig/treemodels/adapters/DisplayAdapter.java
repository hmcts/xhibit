package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters;

import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;

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
 * @version $Id: DisplayAdapter.java,v 1.5 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class DisplayAdapter implements AdapterSorterInterface {
    private static final String key = "pd.displaydescription.";

    private final XhbDisplayBasicValue _value;

    public DisplayAdapter(XhbDisplayBasicValue siteBasicValue) {
        _value = siteBasicValue;
    }

    public XhbDisplayBasicValue getBasicValue() {
        return _value;
    }

    public String toString() {
        return PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAY.concat(_value.getDescriptionCode()));
    }

    public Comparable getSortKey() {
        return toString();
    }
}