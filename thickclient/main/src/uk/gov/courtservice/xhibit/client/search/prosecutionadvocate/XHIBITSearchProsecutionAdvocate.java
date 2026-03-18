package uk.gov.courtservice.xhibit.client.search.prosecutionadvocate;

import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XHIBITSearchProsecutionAdvocate extends XHIBITSearch {
    uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ProsecutionAdvocateValue prosecutionAdvocate = null;

    public XHIBITSearchProsecutionAdvocate() {
        super();
        super.setInterfaces(new XHIBITSearchCriteria(this), new XHIBITSearchResultItemsList(this),
                new XHIBITSearchResultItemDetail(this));
        super.jbInit();
    }

    protected String getComponentResourceKey() {
        return "prosecutionadvocate";
    }

    protected void receiveResults(Object o) {
        prosecutionAdvocate = (uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ProsecutionAdvocateValue) o;
        debug("XHIBITSearchProsecutionAdvocate selected ProsecutionAdvocate : " + prosecutionAdvocate.getFirstName()
                + " " + prosecutionAdvocate.getSurname());
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ProsecutionAdvocateValue getProsecutionAdvocate() {
        debug("XHIBITSearchProsecutionAdvocate returns ProsecutionAdvocate : " + prosecutionAdvocate.getFirstName()
                + " " + prosecutionAdvocate.getSurname() + " to invoking component.");
        return prosecutionAdvocate;
    }
}