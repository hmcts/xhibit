package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PreliminaryHearingsValue
 * </p>
 * <p>
 * Description: Used to pass data from the preliminary hearings screens to the
 * PreliminaryHearings class
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

public class PreliminaryHearingsValue extends CSAbstractValue {
    private Collection directionsForDefendantValue;

    private CaseLevelEventsModel caseLevelEventsModel;

    public PreliminaryHearingsValue() {
    }

    public CaseLevelEventsModel getCaseLevelEventsModel() {
        return caseLevelEventsModel;
    }

    public Collection getDirectionsForDefendantValue() {
        return directionsForDefendantValue;
    }

    public void setCaseLevelEventsModel(CaseLevelEventsModel caseLevelEventsModel) {
        this.caseLevelEventsModel = caseLevelEventsModel;
    }

    public void setDirectionsForDefendantValue(Collection directionsForDefendantValue) {
        this.directionsForDefendantValue = directionsForDefendantValue;
    }
}