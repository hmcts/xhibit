package uk.gov.courtservice.xhibit.business.vos.services.charge;

// jdk
import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version 1.0
 */

public class SignIndValue extends CSAbstractValue {
	
	static final long serialVersionUID = -3592617413495123989L;
	
    public static final int SIGN_IND_DAYS = 28;

    public static final int SIGN_IND_LATE_DAYS = 84;

    private Integer chargeID;

    private Integer numberOfDays;

    private Integer courtID;

    private Integer caseID;

    private Calendar indSignedDate;

    private boolean inCourt; // used for court logging

    private boolean signOutOfTime;

    private Calendar courtLogDate;

    private String judgeName;

    public SignIndValue() {
    }

    public SignIndValue(Integer chargeID, Integer numberOfDays, Integer courtID, Integer caseID,
            Calendar indSignedDate, boolean inCourt, boolean signOutOfTime) {
        this.chargeID = chargeID;
        this.numberOfDays = numberOfDays;
        this.courtID = courtID;
        this.caseID = caseID;
        this.indSignedDate = indSignedDate;
        this.inCourt = inCourt;
        this.signOutOfTime = signOutOfTime;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public Integer getChargeID() {
        return chargeID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public Calendar getIndSignedDate() {
        return indSignedDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public void setIndSignedDate(Calendar indSignedDate) {
        this.indSignedDate = indSignedDate;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public boolean isInCourt() {
        return inCourt;
    }

    public boolean isSignOutOfTime() {
        return signOutOfTime;
    }

    public void setSignOutOfTime(boolean signOutOfTime) {
        this.signOutOfTime = signOutOfTime;
    }

    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
}