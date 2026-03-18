package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HRSHJudgeValue
 * </p>
 * <p>
 * Description: This is an updateable value object for hearing record. It will
 * be used to set the deputyHCJ flag to Yes(=Y) or No(=N).
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HRSHJudgeValue extends CSAbstractValue {

    // private Integer shJudgeID; //PK - unique identifier - should use
    // super class id instead.
    private Integer refJudgeID;

    private Integer sHAttendeeID;

    private String deputyHCJ;
    
    private static final long serialVersionUID = -3729217269183658074L;

    public HRSHJudgeValue() {
    }

    public HRSHJudgeValue(Integer id, Integer version) {
        super(id, version);
    }

    // refJudgeID
    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setRefJudgeID(Integer refJudgeID) {
        this.refJudgeID = refJudgeID;
    }

    // shAttendeeID
    public Integer getSHAttendeeID() {
        return sHAttendeeID;
    }

    public void setSHAttendeeID(Integer shAttendeeID) {
        this.sHAttendeeID = shAttendeeID;
    }

    // deputyHCJ
    public String getDeputyHCJ() {
        return deputyHCJ;
    }

    public void setDeputyHCJ(String deputyHCJ) {
        this.deputyHCJ = deputyHCJ;
    }
}
