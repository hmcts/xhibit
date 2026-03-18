package uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge;

import java.io.Serializable;

/**
 * <p>Title: ChargeVO</p>
 * <p>Description: A VO that contains selected Charge information sufficient to populate the Original charges screen.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
public class ChargeVO implements Serializable {
    private Integer defendantOnCaseId;
    private Integer chargeId;
    private String chargeType;
    private Integer crestChargeSeqNo;
    private Integer offenceId;
    private String crestOffenceFreetext;
    private Integer crestOffenceSeqNo;
    private Integer defendantOnOffenceId;
    private Integer seqNo;
    private Integer refOffenceId;
    private String offenceCode;
    private String offenceDesc;
    private static final long serialVersionUID = 5835265722007255850L;
    
    public ChargeVO() {
        // Default constructor
    }
    
    public ChargeVO(Integer defendantOnCaseId, String chargeType) {
        this.defendantOnCaseId = defendantOnCaseId;
        this.chargeType = chargeType;
    }
    
    public Integer getChargeId() {
        return chargeId;
    }
    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }
    public String getChargeType() {
        return chargeType;
    }
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }
    public String getCrestOffenceFreetext() {
        return crestOffenceFreetext;
    }
    public void setCrestOffenceFreetext(String crestOffenceFreetext) {
        this.crestOffenceFreetext = crestOffenceFreetext;
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
    public Integer getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public Integer getCrestOffenceSeqNo() {
        return crestOffenceSeqNo;
    }

    public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
        this.crestOffenceSeqNo = crestOffenceSeqNo;
    }

    public String getOffenceCode() {
        return offenceCode;
    }

    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    public String getOffenceDesc() {
        return offenceDesc;
    }

    public void setOffenceDesc(String offenceDesc) {
        this.offenceDesc = offenceDesc;
    }

    public Integer getRefOffenceId() {
        return refOffenceId;
    }

    public void setRefOffenceId(Integer refOffenceId) {
        this.refOffenceId = refOffenceId;
    }
}
