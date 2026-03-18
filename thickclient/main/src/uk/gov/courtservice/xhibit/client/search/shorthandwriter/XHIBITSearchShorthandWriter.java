package uk.gov.courtservice.xhibit.client.search.shorthandwriter;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterValue;
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

public class XHIBITSearchShorthandWriter extends XHIBITSearch {
    RefCourtReporterValue refCourtReporter = null;

    public XHIBITSearchShorthandWriter() {
        super();
        setInterfaces(new XHIBITSearchCriteria(this), new XHIBITSearchResultItemsList(this),
                new XHIBITSearchResultItemDetail(this));
        super.jbInit();
    }

    protected String getComponentResourceKey() {
        return "shorthandwriter";
    }

    protected void receiveResults(Object o) {
        refCourtReporter = (RefCourtReporterValue) o;
        debug("XHIBITSearchShorthandWriter selected Shorthand Writer : " + refCourtReporter.getFirstName() + " "
                + refCourtReporter.getSurname());
    }

    public RefCourtReporterValue getRefCourtReporter() {
        if (this.refCourtReporter == null) {
            debug("XHIBITSearchShorthandWriter returns Shorthand Writer : 'null' to invoking component.");
        } else {
            debug("XHIBITSearchShorthandWriter returns Shorthand Writer : " + refCourtReporter.getFirstName() + " "
                    + refCourtReporter.getSurname() + " to invoking component.");
        }
        return refCourtReporter;
    }
}