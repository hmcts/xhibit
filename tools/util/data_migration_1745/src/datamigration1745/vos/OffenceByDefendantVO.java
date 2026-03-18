package datamigration1745.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: OffenceByDefendantVO </p>
 * <p>Description: A VO that describes Offence by a Defendant details.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 *
 * @author GJS
 * @version 1.0
 */
public class OffenceByDefendantVO extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private String caseType;
    private Integer caseNumber;
    private String caseSubType;
    private String chargeType;
    private String crnId;
    private String asn;
    private Integer crestOffenceSeqNo;
    private Integer chargeId;
    private Integer crestChargeId;
    private Integer seqNo;
    private Integer offenceId;
    private Integer crestOffenceId;
    private Integer crestChargeSeqNo;

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    /**
     * Empty constructor
     */
    public OffenceByDefendantVO() { }

    public OffenceByDefendantVO(String caseType,
                                Integer caseNumber,
                                String caseSubType,
                                Integer chargeId,
                                Integer crestChargeId,
                                String chargeType,
                                String crnId,
                                String asn,
                                Integer crestChargeSeqNo,
                                Integer offenceId,
                                Integer crestOffenceId,
                                Integer crestOffenceSeqNo,
                                Integer id,
                                Integer seqNo) {
        setCaseType(caseType);
        setCaseNumber(caseNumber);
        setCaseSubType(caseSubType);
        setChargeType(chargeType);
        setCrnId(crnId);
        setAsn(asn);
        setCrestOffenceSeqNo(crestOffenceSeqNo);
        setChargeId(chargeId);
        setCrestChargeId(crestChargeId);
        setOffenceId(offenceId);
        setCrestOffenceId(crestOffenceId);
        setSeqNo(seqNo);
        setId(id);
        setCrestChargeSeqNo(crestChargeSeqNo);
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType=caseType;
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
        this.caseSubType=caseSubType;
    }


    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType=chargeType;
    }


    public String getCrnId() {
        return crnId;
    }

    public void setCrnId(String crnId) {
        this.crnId=crnId;
    }


    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn=asn;
    }


    public Integer getCrestOffenceSeqNo() {
        return crestOffenceSeqNo;
    }

    public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
        this.crestOffenceSeqNo=crestOffenceSeqNo;
    }

    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Case Type=");
        builder.append(caseType);
        builder.append(",Case Number=");
        builder.append(caseNumber);
        builder.append(",Case SubType=");
        builder.append(caseSubType);
        builder.append(",Charge Type=");
        builder.append(chargeType);
        builder.append(",CRN ID=");
        builder.append(crnId);
        builder.append(",ASN=");
        builder.append(asn);
        builder.append(",Crest Offence Seq No=");
        builder.append(crestOffenceSeqNo);
        builder.append("]");
        return builder.toString();
    }

    public Integer getOffenceId() {
        return offenceId;
    }

    public void setOffenceId(Integer offenceId) {
        this.offenceId = offenceId;
    }

    public Integer getCrestChargeId() {
        return crestChargeId;
    }

    public void setCrestChargeId(Integer crestChargeId) {
        this.crestChargeId = crestChargeId;
    }

    public Integer getCrestOffenceId() {
        return crestOffenceId;
    }

    public void setCrestOffenceId(Integer crestOffenceId) {
        this.crestOffenceId = crestOffenceId;
    }
}
