package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SchedHearingDefendantComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SchedHearingDefendant enitity CMR fields.
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
public class SchedHearingDefendantComplexValue extends SchedHearingDefendantBasicValue {
	
	private static final long serialVersionUID = 1731370447728935752L;
    private ScheduledHearingBasicValue scheduledHearing;

    public SchedHearingDefendantComplexValue() {
        super();
    }

    public SchedHearingDefendantComplexValue(Integer version) {
        super(version);
    }

    public SchedHearingDefendantComplexValue(Integer schedHearDefID, Integer version) {
        super(schedHearDefID, version);
    }

    public ScheduledHearingBasicValue getScheduledHearing() {
        return scheduledHearing;
    }

    public void setScheduledHearing(ScheduledHearingBasicValue scheduledHearing) {
        this.scheduledHearing = scheduledHearing;
    }

    /*
     * public SchedHearingDefendantComplexValue(Integer schedHearDefID, Integer
     * scheduledHearingID, DefendantOnCaseBasicValue defendantOnCase, Integer
     * version, String createdBy, String lastUpdatedBy, Date lastUpdateDate,
     * Date creationDate, Collection shLegReps) { super(schedHearDefID,
     * scheduledHearingID, defendantOnCase.getDefendantID(), version, createdBy,
     * lastUpdatedBy, lastUpdateDate, creationDate);
     * 
     * this.defendantOnCase = defendantOnCase; this.shLegReps = shLegReps; }
     */
}