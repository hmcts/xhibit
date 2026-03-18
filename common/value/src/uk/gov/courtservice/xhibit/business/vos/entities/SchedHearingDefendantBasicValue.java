package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SchedHearingDefendantBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SchedHearingDefendant enitity CMP fields.
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

public class SchedHearingDefendantBasicValue extends CSAbstractValue {
	
	static final long serialVersionUID = 2654030221684376088L;
	
    private Integer scheduledHearingID;

    private Integer defendantOnCaseID;

    public SchedHearingDefendantBasicValue() {
        super();
    }

    public SchedHearingDefendantBasicValue(Integer version) {
        super(version);
    }

    public SchedHearingDefendantBasicValue(Integer schedHearDefID, Integer version) {
        super(schedHearDefID, version);
    }

    public void setScheduledHearingID(Integer scheduledHearingID) {
        this.scheduledHearingID = scheduledHearingID;
    }

    public Integer getScheduledHearingID() {
        return scheduledHearingID;
    }

    public void setDefendantOnCaseID(Integer defendantOnCaseID) {
        this.defendantOnCaseID = defendantOnCaseID;
    }

    public Integer getDefendantOnCaseID() {
        return defendantOnCaseID;
    }
}