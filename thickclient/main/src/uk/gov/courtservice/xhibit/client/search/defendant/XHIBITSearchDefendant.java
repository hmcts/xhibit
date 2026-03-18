package uk.gov.courtservice.xhibit.client.search.defendant;

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

public class XHIBITSearchDefendant extends XHIBITSearch {
    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendant = null;

    public XHIBITSearchDefendant() {
        super();
        super.setInterfaces(new XHIBITSearchCriteria(this), new XHIBITSearchResultItemsList(this),
                new XHIBITSearchResultItemDetail(this));
        super.jbInit();
    }

    protected String getComponentResourceKey() {
        return "defendant";
    }

    protected void receiveResults(Object o) {
        defendant = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) o;
        debug("XHIBITSearchDefendant selected Defendant : " + defendant.getFirstName() + " " + defendant.getSurName());
    }

    public uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue getDefendant() {
        debug("XHIBITSearchDefendant returned Defendant : " + defendant.getFirstName() + " " + defendant.getSurName());
        return defendant;
    }

}