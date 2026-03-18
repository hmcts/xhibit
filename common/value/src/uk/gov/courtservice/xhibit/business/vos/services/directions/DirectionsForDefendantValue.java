package uk.gov.courtservice.xhibit.business.vos.services.directions;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

public class DirectionsForDefendantValue extends CSAbstractValue {

    public static final String IDENTIFIED_TRUE = "Y";

    public static final String IDENTIFIED_FALSE = "N";

    public static final String ARRAINGED_TRUE = "Y";

    public static final String ARRAINGED_FALSE = "N";

    public static final String FILED_FORM_B_TRUE = "Y";

    public static final String FILED_FROM_B_FALSE = "N";

    private XhbDirectionsForDefendantBasicValue directionsForDefendantBasicValue;

    private CourtLogCRUDValue[] courtLogCRUDValues;

    private DefendantBasicValue defendantBasicValue;
    private static final long serialVersionUID = 791207292140619375L;
    

    /**
     * @roseuid 3E2BF63F010B
     */
    public DirectionsForDefendantValue() {
    }

    public XhbDirectionsForDefendantBasicValue getDirectionsForDefendantBasicValue() {
        return this.directionsForDefendantBasicValue;
    }

    public void setDirectionsForDefendantBasicValue(XhbDirectionsForDefendantBasicValue dirForDefBV) {
        this.directionsForDefendantBasicValue = dirForDefBV;
    }

    public CourtLogCRUDValue[] getCourtLogCRUDValues() {
        return this.courtLogCRUDValues;
    }

    public void setCourtLogCRUDValue(CourtLogCRUDValue[] courtLogValues) {
        this.courtLogCRUDValues = courtLogValues;
    }

    public String getFirstName() {
        return getDefendantBasicValue().getFirstName();
    }

    public String getSurname() {
        return getDefendantBasicValue().getSurname();
    }

    public void setFirstName(String firstName) {
        // NULL implementation
    }

    public void setSurname(String surname) {
        // NULL implementation
    }

    public DefendantBasicValue getDefendantBasicValue() {
        return defendantBasicValue;
    }

    public void setDefendantBasicValue(DefendantBasicValue defendantBasicValue) {
        this.defendantBasicValue = defendantBasicValue;
    }
}