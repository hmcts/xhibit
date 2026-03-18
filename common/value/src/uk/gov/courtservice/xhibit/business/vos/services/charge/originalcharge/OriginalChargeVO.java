package uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: OriginalChargeVO</p>
 * <p>Description: A VO that describes an Original Charge.</p>
 * <p>It is used to transport data from the client to the mid-tier.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
public class OriginalChargeVO extends CSAbstractValue {
    private Integer courtId;
    private Integer caseId;
    private Integer defendantId;
    private Integer defendantOnCaseId;
    private String originalCharge;
    private Integer seqNo;
    private Integer defendantOnOffenceId;
    private Integer offenceId;
    private Integer chargeId;
    private String trxCode;
    private Integer refOffenceId;
    private String chargeType;
    private static final long serialVersionUID = 3445088225093426672L;

    public String getChargeType() {
        return chargeType;
    }
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }
    public Integer getRefOffenceId() {
        return refOffenceId;
    }
    public void setRefOffenceId(Integer refOffenceId) {
        this.refOffenceId = refOffenceId;
    }
    public Integer getCaseId() {
        return caseId;
    }
    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
    public Integer getChargeId() {
        return chargeId;
    }
    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }
    public Integer getDefendantId() {
        return defendantId;
    }
    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }
    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }
    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }
    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }
    public Integer getOffenceId() {
        return offenceId;
    }
    public void setOffenceId(Integer offenceId) {
        this.offenceId = offenceId;
    }
    public String getOriginalCharge() {
        return originalCharge;
    }
    public void setOriginalCharge(String originalCharge) {
        this.originalCharge = originalCharge;
    }
    public Integer getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    public String getTrxCode() {
        return trxCode;
    }
    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
    }
    public Integer getCourtId() {
        return courtId;
    }
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }
}
