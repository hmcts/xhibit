package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: CaseLevelEventsModel
 * </p>
 * <p>
 * Description: Model for the CaseLevelEventsPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class CaseLevelEventsModel extends CSAbstractValue {
    private CourtLogCRUDValue[] courtLogCRUDValues;

    private CaseLevelEventsValue caseLevelEventsValue;

    private XhibitApplicationController xac;

    public CaseLevelEventsModel() {
    }

    public CourtLogCRUDValue[] getCourtLogCRUDValues() {
        return courtLogCRUDValues;
    }

    public void setCourtLogCRUDValues(CourtLogCRUDValue[] courtLogCRUDValues) {
        this.courtLogCRUDValues = courtLogCRUDValues;
    }

    public void setCaseLevelEventsValue(CaseLevelEventsValue caseLevelEventsValue) {
        this.caseLevelEventsValue = caseLevelEventsValue;
    }

    public CaseLevelEventsValue getCaseLevelEventsValue() {
        return caseLevelEventsValue;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void setXac(XhibitApplicationController xac) {
        this.xac = xac;
    }
}
