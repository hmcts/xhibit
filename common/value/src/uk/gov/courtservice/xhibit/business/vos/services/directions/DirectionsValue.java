package uk.gov.courtservice.xhibit.business.vos.services.directions;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DirectionsValue extends CSAbstractValue {
    private Collection directionsForDefendantValue = new ArrayList();
    private static final long serialVersionUID = -4358892768296023307L;

    private DirectionsForCaseValue directionsForCaseValue = null;

    public DirectionsForCaseValue getDirectionsForCaseValue() {
        return directionsForCaseValue;
    }

    public Collection getDirectionsForDefendantValue() {
        return directionsForDefendantValue;
    }

    public void setDirectionsForCaseValue(DirectionsForCaseValue directionsForCaseValue) {
        this.directionsForCaseValue = directionsForCaseValue;
    }

    public void setDirectionsForDefendantValue(Collection directionsForDefendantValue) {
        this.directionsForDefendantValue = directionsForDefendantValue;
    }
}