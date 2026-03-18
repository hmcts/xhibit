package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeType;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class ChargeWizardModel {
    private boolean offenceRequired = false;

    private Integer defendantID;

    private Integer caseID;
    
    private Integer defendantOnCaseID;

    private Integer chargeID;

    private List<OffenceValue> addedOffences;
    
    private String caseType;

    private ChargeType chargeType;

    private Integer courtId;

    public ChargeWizardModel() {
        // empty
    }

    public Integer getDefendantID() {
        return defendantID;
    }

    public void setDefendantID(Integer defendantID) {
        this.defendantID = defendantID;
    }

    public Integer getDefendantOnCaseID() {
        return defendantOnCaseID;
    }

    public void setDefendantOnCaseID(Integer defendantOnCaseID) {
        this.defendantOnCaseID = defendantOnCaseID;
    }
    
    public List<OffenceValue> getAddedOffences() {
        return addedOffences;
    }
    
    public void setAddedOffences(List<OffenceValue> addedOffences) {
        this.addedOffences = addedOffences;
    }

    public boolean isOffenceRequired() {
        return offenceRequired;
    }

    public void setOffenceRequired(boolean offenceRequired) {
        this.offenceRequired = offenceRequired;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public Integer getChargeID() {
        return chargeID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
    
    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return this.courtId;
    }
}