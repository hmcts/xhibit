package uk.gov.courtservice.xhibit.client.search.offence;

import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * @deprecated
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
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class XHIBITSearchOffence extends XHIBITSearch {

    RefOffenceValue offence = null;

    /**
     * @deprecated
     */
    public XHIBITSearchOffence() {
        super();
        // super.setInterfaces(new XHIBITSearchCriteria(this), new
        // XHIBITSearchResultItemsList(this), new
        // XHIBITSearchResultItemDetail(this));
        super.jbInit();
    }

    public String getComponentResourceKey() {
        return "offence";
    }

    protected void receiveResults(Object o) {
        debug("it is: " + o.getClass());

        RefOffenceBasicValue tmp = (RefOffenceBasicValue) o;

        RefOffenceValue tmp2 = new RefOffenceValue();
        tmp2.setActSection(tmp.getActSection() == null ? "" : tmp.getActSection());
        tmp2.setCourtId(tmp.getCourtId() == null ? new Integer(1) : tmp.getCourtId());
        tmp2.setDvlcCode(tmp.getDvlcCode() == null ? "" : tmp.getDvlcCode());
        tmp2.setHoClass(tmp.getHoClass() == null ? "" : tmp.getHoClass());
        tmp2.setHoProcType(tmp.getHoProcType() == null ? "" : tmp.getHoProcType());
        tmp2.setHoSubClass(tmp.getHoSubClass() == null ? "" : tmp.getHoSubClass());
        tmp2.setObsInd(tmp.getObsInd() == null ? "" : tmp.getObsInd());
        tmp2.setOffenceClass(tmp.getOffenceClass() == null ? "" : tmp.getOffenceClass());
        tmp2.setOffenceCode(tmp.getOffenceCode() == null ? "" : tmp.getOffenceCode());
        tmp2.setOffenceDesc(tmp.getOffenceDesc() == null ? "" : tmp.getOffenceDesc());
        tmp2.setOffenceDesc2(tmp.getOffenceDesc2() == null ? "" : tmp.getOffenceDesc2());
        tmp2.setOffenceGroup(tmp.getOffenceGroup() == null ? "" : tmp.getOffenceGroup());
        tmp2.setOffenceType(tmp.getOffenceType() == null ? "" : tmp.getOffenceType());
        tmp2.setRefOffenceID(new Integer(0));
        tmp2.setStatute(tmp.getStatute());
        tmp2.setXhbVersion("");

        offence = tmp2;
        debug("XHIBITSearchOffence selected Offence:  " + offence.toString());
    }

    public RefOffenceValue getOffence() {
        debug("XHIBITSearchOffence returns Offence:  " + offence + " to invoking component");
        return this.offence;
    }
}