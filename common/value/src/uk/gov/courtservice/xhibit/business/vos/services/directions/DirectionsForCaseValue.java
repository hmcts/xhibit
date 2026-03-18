package uk.gov.courtservice.xhibit.business.vos.services.directions;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

public class DirectionsForCaseValue extends CSAbstractValue {
    public static final String HAS_P_AND_D_FORM_TRUE = "Y";

    public static final String HAS_P_AND_D_FORM_FALSE = "N";

    private static final long serialVersionUID = 3511383182773979497L;
    
    private XhbDirectionsForCaseBasicValue directionsForCaseBasicValue;

    // one each of all the court log events generated from the Directions
    // For
    // Case
    private CourtLogCRUDValue[] courtLogCRUDValues;

    /**
     * @roseuid 3E2BF63F007F
     */
    public DirectionsForCaseValue() {
    }

    public void setDirectionsForCaseBasicValue(XhbDirectionsForCaseBasicValue dirForCaseBasicValue) {
        this.directionsForCaseBasicValue = dirForCaseBasicValue;
    }

    public XhbDirectionsForCaseBasicValue getDirectionsForCaseBasicValue() {
        return this.directionsForCaseBasicValue;
    }

    public CourtLogCRUDValue[] getCourtLogCRUDValues() {
        return this.courtLogCRUDValues;
    }

    public void setCourtLogCRUDValues(CourtLogCRUDValue[] courtLogValues) {
        this.courtLogCRUDValues = courtLogValues;
    }
}