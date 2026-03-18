package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
public class MoveResultsValue {
    private Integer defendantOnOffenceId;

    private Integer caseId;

    private Integer caseNumber;

    private String caseType;

    private String caseSubType;

    private Integer crestOffenceId;

    private Integer crestDefendantId;

    private Integer crestChargeId;

    private String chargeType;

    private Date vcoDate;

    private String vcoFlag;

    public MoveResultsValue(Integer defendantOnOffenceId, Integer caseId, Integer caseNumber, String caseType,
            String caseSubType, Integer crestOffenceId, Integer crestDefendantId, String chargeType,
            Integer crestChargeId, Date vcoDate, String vcoFlag) {
        setDefendantOnOffenceId(defendantOnOffenceId);
        setCaseId(caseId);
        setCaseNumber(caseNumber);
        setCaseType(caseType);
        setCaseSubType(caseSubType);
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
        setChargeType(chargeType);
        setCrestChargeId(crestChargeId);
        setVcoDate(vcoDate);
        setVcoFlag(vcoFlag);
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getCrestDefendantId() {
        return crestDefendantId;
    }

    public void setCrestDefendantId(Integer crestDefendantId) {
        this.crestDefendantId = crestDefendantId;
    }

    public Integer getCrestOffenceId() {
        return crestOffenceId;
    }

    public void setCrestOffenceId(Integer crestOffenceId) {
        this.crestOffenceId = crestOffenceId;
    }

    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }

    public Date getVcoDate() {
        return vcoDate;
    }

    public void setVcoDate(Date vcoDate) {
        this.vcoDate = vcoDate;
    }

    public String getVcoFlag() {
        return vcoFlag;
    }

    public void setVcoFlag(String vcoFlag) {
        this.vcoFlag = vcoFlag;
    }

    public Integer getCrestChargeId() {
        return crestChargeId;
    }

    public void setCrestChargeId(Integer crestChargeId) {
        this.crestChargeId = crestChargeId;
    }

}