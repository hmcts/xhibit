package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SHJudgeBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHJudge
 * enitity CMP fields.
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

public class SHJudgeBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -7155651462089132286L;
	private Integer refJudgeID;

    private Integer shAttendeeID;

    private String deputyHCJ;

    public SHJudgeBasicValue() {
        super();
    }

    public SHJudgeBasicValue(Integer version) {
        super(version);
    }

    public SHJudgeBasicValue(Integer shJudgeID, Integer version) {
        super(shJudgeID, version);
    }

    public void setRefJudgeID(Integer refJudgeID) {
        this.refJudgeID = refJudgeID;
    }

    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setShAttendeeID(Integer shAttendeeID) {
        this.shAttendeeID = shAttendeeID;
    }

    public Integer getShAttendeeID() {
        return shAttendeeID;
    }

    public void setDeputyHCJ(String deputyHCJ) {
        this.deputyHCJ = deputyHCJ;
    }

    public String getDeputyHCJ() {
        return deputyHCJ;
    }
}