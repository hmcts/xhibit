package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

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
 * @version $Id: DisplaySiteAdapter.java,v 1.4 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class DisplaySiteAdapter implements AdapterSorterInterface {
    private final XhbCourtSiteBasicValue _value;

    public DisplaySiteAdapter(XhbCourtSiteBasicValue siteBasicValue) {
        _value = siteBasicValue;
    }

    public XhbCourtSiteBasicValue getBasicValue() {
        return _value;
    }

    public String toString() {
        return _value.getDisplayName();
    }

    public Comparable getSortKey() {
        return _value.getCourtSiteCode();
    }
}