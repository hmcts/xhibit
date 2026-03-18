package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SHLegRepComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHLegRep
 * enitity CMR fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class SHLegRepComplexValue extends SHLegRepBasicValue {

	private static final long serialVersionUID = -8415126008696251816L;
    private SchedHearingAttendeeBasicValue schedHearingAttendee;

    private SchedHearingDefendantBasicValue schedHearingDefendant;

    public SHLegRepComplexValue() {
        super();
    }

    public SHLegRepComplexValue(Integer version) {
        super(version);
    }

    public SHLegRepComplexValue(Integer shLegRepID, Integer version) {
        super(shLegRepID, version);
    }

    // ------------
    // CMR fields
    // ------------

    public SchedHearingDefendantBasicValue getSchedHearingDefendant() {
        return schedHearingDefendant;
    }

    public void setSchedHearingDefendant(SchedHearingDefendantBasicValue schedHearingDefendant) {
        this.schedHearingDefendant = schedHearingDefendant;
    }

}