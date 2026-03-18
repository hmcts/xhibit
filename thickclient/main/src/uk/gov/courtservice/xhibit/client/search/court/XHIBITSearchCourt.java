package uk.gov.courtservice.xhibit.client.search.court;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * <p>Title: XHIBIT 2</p>
 * <p>Description: An EDS - Court Service Application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Bal Bhamra
 * @version 1.0
 */

/** @deprecated */
public class XHIBITSearchCourt extends XHIBITSearch {

    RefCourtBasicValue court = null;

    /** @deprecated */
    public XHIBITSearchCourt() {
        super();
        super.setInterfaces(new XHIBITSearchCriteria(this), new XHIBITSearchResultItemsList(this),
                new XHIBITSearchResultItemDetail(this));
        super.jbInit();
    }

    protected String getComponentResourceKey() {
        return "court";
    }

    protected void receiveResults(Object o) {
        court = (RefCourtBasicValue) o;
        debug("XHIBITSearchCourt selected Court" + court.getCourtFullName());
    }

    public RefCourtBasicValue getCourt() {
        if (court != null) {
            debug("XHIBITSearchCourt returned Court" + court.getCourtFullName() + " to invoking component.");
        }
        return court;

    }
}