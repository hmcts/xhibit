package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;

/**
 * @author unascribed
 * @version $Revision: 1.42 $
 */
public class PleaSaveValue extends CourtLogSaveValue {
	
	final static long serialVersionUID = 7897263033397328235L;
	
    private PleaValue pleaValue; // Data!

    private Integer crestOffenceId; // Required by CREST

    private Integer crestDefendantId; // Required by CREST

    private Integer crestOffenceSeqNo; // Required by Court Log

    private Integer crestChargeId; // Required by Court Log

    private Integer crestChargeSeqNo; // Required by Court Log

    private String defendantName; // Required by Court Log

    private String offenceDescription; // Required by Court Log

    private String vcoFlag; // Required by CREST (Set by midtier)

    private Date vcoDate; // Required by CREST (Set by midtier)

    // Constructor
    public PleaSaveValue(PleaValue pleaValue, String operation, Integer caseId, Integer caseNumber, String caseType,
            Integer scheduledHearingId, Integer crestOffenceId, Integer crestDefendantId, Integer crestOffenceSeqNo,
            String chargeType, Integer crestChargeId, Integer crestChargeSeqNo, Integer defendantOnCaseId,
            String defendantName, String offenceDescription, boolean inCourt, Date courtLogDate) {
        super(operation, caseId, caseNumber, caseType, chargeType, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.pleaValue = pleaValue;
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
        setCrestOffenceSeqNo(crestOffenceSeqNo);
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setDefendantOnCaseId(defendantOnCaseId);
        setDefendantName(defendantName);
        setOffenceDescription(offenceDescription);
    }

    // Constructor for indictments without logging
    public PleaSaveValue(PleaValue pleaValue, String operation, Integer caseId, Integer caseNumber, String caseType,
            Integer crestOffenceId, Integer crestDefendantId, String chargeType) {
        super(operation, caseId, caseNumber, caseType, chargeType);
        this.pleaValue = pleaValue;
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
    }

    // Constructor for Plea On Charge
    public PleaSaveValue(PleaValue pleaValue, String operation, Integer caseId, Integer caseNumber, String caseType,
            Integer scheduledHearingId, Integer crestDefendantId, String chargeType, Integer crestChargeId,
            Integer crestChargeSeqNo, Integer defendantOnCaseId, String defendantName, boolean inCourt,
            Date courtLogDate) {
        super(operation, caseId, caseNumber, caseType, chargeType, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.pleaValue = pleaValue;
        setCrestDefendantId(crestDefendantId);
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setDefendantOnCaseId(defendantOnCaseId);
        setDefendantName(defendantName);
    }

    // Get basic value
    public PleaValue getPleaValue() {
        return pleaValue;
    }

    public XhbPleaBasicValue getPleaBasicValue() {
        return pleaValue.getPleaBasicValue();
    }

    // Set basic value
    public void setPleaBasicValue(XhbPleaBasicValue pleaBasicValue) {
        pleaValue.setPleaBasicValue(pleaBasicValue);
    }

    // MidTier Accessors
    public String getVcoFlag() {
        return vcoFlag;
    }

    public void setVcoFlag(String vcoFlag) {
        this.vcoFlag = vcoFlag;
    }

    public Date getVcoDate() {
        return vcoDate;
    }

    public void setVcoDate(Date vcoDate) {
        this.vcoDate = vcoDate;
    }

    // Accessors
    public Date getDatePut() {
        return pleaValue.getDatePut();
    }

    public void setDatePut(Date datePut) {
        pleaValue.setDatePut(datePut);
    }

    public Integer getCrestOffenceId() {
        return crestOffenceId;
    }

    public void setCrestOffenceId(Integer crestOffenceId) {
        this.crestOffenceId = crestOffenceId;
    }

    public Integer getCrestDefendantId() {
        return crestDefendantId;
    }

    public void setCrestDefendantId(Integer crestDefendantId) {
        this.crestDefendantId = crestDefendantId;
    }

    public Integer getCrestOffenceSeqNo() {
        return crestOffenceSeqNo;
    }

    public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
        this.crestOffenceSeqNo = crestOffenceSeqNo;
    }

    public Integer getCrestChargeId() {
        return crestChargeId;
    }

    public void setCrestChargeId(Integer crestChargeId) {
        this.crestChargeId = crestChargeId;
    }

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public String getDefendantName() {
        return defendantName;
    }

    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    public String getOffenceDescription() {
        return offenceDescription;
    }

    public void setOffenceDescription(String offenceDescription) {
        this.offenceDescription = offenceDescription;
    }

    public void setPleaId(Integer pleaId) {
        pleaValue.setPleaId(pleaId);
    }

    public Integer getPleaId() {
        return pleaValue.getPleaId();
    }

    // Save Business
    public Integer getDefendantChargeId() {
        return pleaValue.getDefendantChargeId();
    }

    public boolean getObsInd() {
        return pleaValue.getObsInd();
    }

    public void setObsInd(boolean obsInd) {
        pleaValue.setObsInd(obsInd);
    }

    // Court Log Business
    public String getRefPleaDesc() {
        return pleaValue.getRefPleaDesc();
    }

    public String getRefPleaCode() {
        return pleaValue.getRefPleaCode();
    }

    public Integer getRefPleaId() {
        return pleaValue.getRefPleaId();
    }

    public String getAltRefOffenceDesc() {
        return pleaValue.getAltRefOffenceDesc();
    }

    public String getOtherPleaText() {
        return pleaValue.getOtherPleaText();
    }

    public Boolean getBreachAdmitted() {
        return pleaValue.getBreachAdmitted();
    }

    public boolean isBreachAdmitted() {
        return pleaValue.isBreachAdmitted();
    }

    public Integer getDefendantOnOffenceId() {
        return pleaValue.getDefendantOnOffenceId();
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        pleaValue.setDefendantOnOffenceId(defendantOnOffenceId);
    }

    public Date getPleaDate() {
        return pleaValue.getPleaDate();
    }

    public Integer getOriginalRefPleaId() {
        return pleaValue.getOriginalRefPleaId();
    }

    public Date getOriginalPleaDate() {
        return pleaValue.getOriginalPleaDate();
    }

    public Integer getOriginalRefVerdictId() {
        return pleaValue.getOriginalRefVerdictId();
    }

    public Date getOriginalVerdictDate() {
        return pleaValue.getOriginalVerdictDate();
    }

    // Crest Business
    public String getCrestArraignmentDate() {
        return formatCrestDate(pleaValue.getArraignmentDate());
    }

    public String getCrestDatePut() {
        return formatCrestDate(getDatePut());
    }

    public String getCrestPlea() {
        return pleaValue.getRefPleaCode();
    }

    public String getCrestLessOffPlea() {
        return pleaValue.getAltRefOffenceCode();
    }

    public String getCrestLessOffPleaDesc() {
        return pleaValue.getAltRefOffenceDesc();
    }

    public boolean isCrestLessOffPleaUncoded() {
        return ResultValue.UNCODED_OFFENCE.equalsIgnoreCase(getCrestLessOffPlea());
    }

    public String getCrestOtherPlea() {
        return pleaValue.getOtherPleaText();
    }

    public String getCrestDateType() {
        return getVcoFlag();
    }

    public String getCrestVerdictDate() {
        return formatCrestDate(getVcoDate());
    }

    public Integer getCrestBchId() {
        return getCrestChargeId();
    }

    public String getCrestBreachAdmitted() {
        Boolean admitted = pleaValue.getBreachAdmitted();

        if (admitted == null) {
            return null;
        } else {
            return admitted.booleanValue() ? "Y" : "N";
        }
    }

    // Business
    public boolean isOnOffence() {
        return pleaValue.isOnOffence();
    }

    public boolean isOnCharge() {
        return pleaValue.isOnCharge();
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(PleaSaveValue.class.getName());
        buffer.append(" {");
        super.appendDebugParameters(buffer);
        buffer.append(", crestOffenceId=");
        buffer.append(crestOffenceId);
        buffer.append(", crestDefendantId=");
        buffer.append(crestDefendantId);
        buffer.append(", crestOffenceSeqNo=");
        buffer.append(crestOffenceSeqNo);
        buffer.append(", crestChargeId=");
        buffer.append(crestChargeId);
        buffer.append(", crestChargeSeqNo=");
        buffer.append(crestChargeSeqNo);
        buffer.append(", defendantName=");
        buffer.append(defendantName);
        buffer.append(", offenceDescription=");
        buffer.append(offenceDescription);
        buffer.append(", vcoFlag=");
        buffer.append(vcoFlag);
        buffer.append(", vcoDate=");
        buffer.append(vcoDate);
        buffer.append(", pleaValue=");
        buffer.append(pleaValue);
        buffer.append("}");
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        return new Object[] { getOraCode(), getCourtLogCaseType(), getCourtLogCaseNumber(), getCrestChargeSeqNo(),
                getCrestOffenceSeqNo(), getDefendantName(), getRefPleaCode()

        };
    }

    public Integer getCourtLogEvent() {
        String operation = getOperation();
        if (ResultSaveValue.ADD.equals(operation) || ResultSaveValue.UPDATE.equals(operation)) {
            if (getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
                return PleaTypeEvent.PLEA_FOR_BREACH_EVENT;
            }

            if (getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())){
                return PleaTypeEvent.PLEA_FOR_FAIL2APPEAR_EVENT;
            }
            
            if (isOnSummary()) {
                return PleaTypeEvent.PLEA_FOR_SUMMARY_OFFENCE_EVENT;
            }

            if ((getAltRefOffenceDesc() != null) && (getAltRefOffenceDesc().trim().length() > 0)) {
                return PleaTypeEvent.PLEA_FOR_INDICTMENTS_LESSER_OFFENCE_EVENT;
            }

            return PleaTypeEvent.PLEA_FOR_INDICTMENTS_EVENT;
        } else if (ResultSaveValue.DELETE.equals(operation)) {
            return PleaTypeEvent.PLEA_DELETE_EVENT;
        }

        // @todo - this shouldn't happen, and will throw exception at later
        // date???
        return null;
    }

    /**
     * @description The table below summarises the logic used error codes are
     *              CREST error codes this method returns false instead of an
     *              error code. |Case Type|Breach Type|Receipt Type|Breach
     *              Plea|Error Code|Breach Date Put|Error Code| | T | B | Any |
     *              CM | 4 | CM | 6 | | S | C | BB | CM | 4 | CM | 6 | | S | B |
     *              <>BB | CM | 4 | CM | 6 | | S | C | <>BB | N | 5 | N | 7 | N =
     *              Mandatory Null. CM = Conditional Mandatory. If
     *              BREACH.HO_PROC_CODE is in 31, 53, 56, 58 then *can be* null
     *              else mandatory.
     * @param caseType
     * @param breachType
     * @param receiptType
     * @param breachHoProcCode
     * @param pleaCode
     * @param datePut
     * @return
     */

    public static boolean isBreachPleaValid(String caseType, String breachType, String receiptType,
            String breachHoProcCode, String pleaCode, Date datePut) {
        if (caseType == null || breachType == null || receiptType == null) {
            throw new IllegalArgumentException("Null parameter supplied");
        }

        Boolean shouldBreachPleaBeSet = shouldBreachPleaBeSet(caseType, breachType, receiptType, breachHoProcCode);
        if (shouldBreachPleaBeSet == null) {
            return true;
        } else if (shouldBreachPleaBeSet.booleanValue()) {
            if (pleaCode == null || pleaCode.trim() == "" || datePut == null) {
                return false;
            }
        } else {
            if (pleaCode != null || datePut != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMandatoryPleaSet(String caseType, String breachType, String receiptType,
            String breachHoProcCode, String pleaCode, Date datePut) {
        if (caseType == null || breachType == null || receiptType == null) {
            throw new IllegalArgumentException("Null parameter supplied");
        }

        Boolean shouldBreachPleaBeSet = shouldBreachPleaBeSet(caseType, breachType, receiptType, breachHoProcCode);
        if (shouldBreachPleaBeSet == null) {
            return true;
        } else if (shouldBreachPleaBeSet.booleanValue()) {
            if (pleaCode == null || pleaCode.trim() == "") {
                return false;
            }
        }
        return true;
    }

    public static boolean isMandatoryNullPleaSet(String caseType, String breachType, String receiptType,
            String breachHoProcCode, String pleaCode, Date datePut) {
        if (caseType == null || breachType == null || receiptType == null) {
            throw new IllegalArgumentException("Null parameter supplied");
        }

        Boolean shouldBreachPleaBeSet = shouldBreachPleaBeSet(caseType, breachType, receiptType, breachHoProcCode);

        if (shouldBreachPleaBeSet == null) {
            return true; // I guess it doesn't matter here
        }

        if (!shouldBreachPleaBeSet.booleanValue()) {
            if (pleaCode != null || datePut != null) {
                return false;
            }
            return true;
        }
        return true;
    }

    private static Boolean shouldBreachPleaBeSet(String caseType, String breachType, String receiptType,
            String breachHoProcCode) {
        if ("T".equals(caseType) && "B".equals(breachType) && isNotSpecialHoProcCode(breachHoProcCode)) {
            return new Boolean(true);// mandatory yes
        } else if ("S".equals(caseType)) {
            if ("C".equals(breachType)) {
                if ("BB".equals(receiptType)) {
                    if (isNotSpecialHoProcCode(breachHoProcCode)) {
                        return new Boolean(true);// mandatory yes
                    }
                } else {
                    return new Boolean(false); // mandatory no
                }
            } else if ("B".equals(breachType) && !("BB".equals(receiptType))
                    && isNotSpecialHoProcCode(breachHoProcCode)) {
                return new Boolean(true);// mandatory yes
            }
        }
        return null; // optional
    }

    /**
     * Determines whether passed in code is a special HO Proc Code.
     * 
     * @param breachHoProcCode
     * @return
     */

    // NOTE: Ensure the HO proc codes below are consistent with the folloing
    // files:
    // 1)
    // Xhibit.thickclient.main.src.uk.gov.courtservice.xhibit.client.maintaincharges.BreachPanel
    // - String [] HO_PROC_CODES
    // 2)
    // Xhibit.support.config.src.gov.courtservice.xhibit.support.config.src.config.bundles.errorText.properties
    // - add HO Proc codes to existing messages detailing Ho Proc Codes.
    public static boolean isNotSpecialHoProcCode(String breachHoProcCode) {
        // Faster to compare Strings as opposed to converting to Integer then
        // checking intValue
        return !("31".equals(breachHoProcCode)) && !("44".equals(breachHoProcCode)) && !("53".equals(breachHoProcCode))
                && !("56".equals(breachHoProcCode)) && !("58".equals(breachHoProcCode))
                && !("66".equals(breachHoProcCode)) && !("67".equals(breachHoProcCode))
                && !("81".equals(breachHoProcCode)) && !("82".equals(breachHoProcCode))
                && !("83".equals(breachHoProcCode)) && !("84".equals(breachHoProcCode));
    }
}
