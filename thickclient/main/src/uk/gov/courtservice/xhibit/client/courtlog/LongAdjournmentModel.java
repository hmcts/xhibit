package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Model contains information displayed on the LongAdjournmentPanel
 * </p>
 * <p>
 * Description: The information held is used to create a CourtLogCRUDValue by
 * the LongAdjournmentPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class LongAdjournmentModel extends FreeTextModel {
    private int subEventId;

    private Calendar adjournmentDate;

    private String subEventCode;

    private String name;

    private boolean psrRequired = false;

    private boolean notReserved = false;

    private boolean judgeReserved = false;

    private boolean allDefendants = false;

    private String judgeName;

    /**
     * No arguments required to create an instance of the LongAdjournmentModel
     */
    public LongAdjournmentModel() {
        super();
    }

    // Getters
    public int getSubEventId() {
        return subEventId;
    }

    public Calendar getAdjournmentDate() {
        return adjournmentDate;
    }

    public String getSubEventCode() {
        return subEventCode;
    }

    public String getName() {
        return name;
    }

    public boolean isPsrRequired() {
        return psrRequired;
    }

    public boolean isNotReserved() {
        return notReserved;
    }

    public boolean isJudgeReserved() {
        return judgeReserved;
    }

    public boolean isAllDefendants() {
        return allDefendants;
    }

    public String getJudgeName() {
        return judgeName;
    }

    // Setters
    public void setSubEventId(int id) {
        subEventId = id;
    }

    public void setAdjournmentDate(Calendar date) {
        adjournmentDate = date;
    }

    public void setSubEventCode(String code) {
        subEventCode = code;
    }

    public void setName(String param) {
        name = param;
    }

    public void setPsrRequired(boolean param) {
        psrRequired = param;
    }

    public void setNotReserved(boolean param) {
        notReserved = param;
    }

    public void setAllDefendants(boolean param) {
        allDefendants = param;
    }

    public void setJudgeReserved(boolean param) {
        judgeReserved = param;
    }

    public void setJudgeName(String param) {
        judgeName = param;
    }

    public void printModel() {
        super.printModel();

        XHIBITConstant.info("LongAdjournmentModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("Sub Event ID    : " + getSubEventId());
        XHIBITConstant.info("Sub Event Code  : " + getSubEventCode());
        XHIBITConstant.info("Name            : " + getName());
        XHIBITConstant.info("Adjournment Date: " + getAdjournmentDate());
        XHIBITConstant.info("PSR Required: " + isPsrRequired());
        XHIBITConstant.info("Not Reserved: " + isNotReserved());
        XHIBITConstant.info("Judge Reserved: " + isJudgeReserved());
        XHIBITConstant.info("Judge Name: " + getJudgeName());
        XHIBITConstant.info("All Defendants: " + isAllDefendants());
    }

    public void clearmodel() {
        super.clearmodel();

        setSubEventId(0);
        setAdjournmentDate(null);
        setSubEventCode(null);
        setName(null);
    }
}
