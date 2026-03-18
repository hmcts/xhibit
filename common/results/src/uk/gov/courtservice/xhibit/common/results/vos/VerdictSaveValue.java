package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;

/**
 * @author Abdul Rahim Hussain
 */
public class VerdictSaveValue extends CourtLogSaveValue {
	
	static final long serialVersionUID = -8767189449025249609L;
	
    private static final String VERDICT = "VERDICT";

    private int verdictType = 0; // internal representation of verdict

    // type (see VerdictTypeEvent)

    private VerdictValue verdictValue; // Data!

    /**
     * DisposalSaveValue for Magistrate General Disposal.
     */
    private DisposalSaveValue disposalSaveValue;

    private Integer crestOffenceId; // Required by CREST

    private Integer crestDefendantId; // Required by CREST

    private Integer crestChargeId; // Required by Court Log

    private Integer crestOffenceSeqNo; // Required by Court Log

    private Integer crestChargeSeqNo; // Required by Court Log

    private String defendantName; // Required by Court Log

    private String offenceDescription; // Required by Court Log

    private String vcoFlag; // Required by CREST (Set by midtier)

    private Date vcoDate; // Required by CREST (Set by midtier)

    private String caseSubType;

    // Required for determining appeal result (Set by midtier)// Required
    // for determining appeal result (Set by midtier)

    // Constructor for Verdict On Offence
    public VerdictSaveValue(VerdictValue verdictValue, String operation, Integer caseId, Integer caseNumber,
            String caseType, Integer scheduledHearingId, String caseSubType, Integer crestOffenceId,
            Integer crestDefendantId, Integer crestOffenceSeqNo, String chargeType, Integer crestChargeId,
            Integer crestChargeSeqNo, Integer defendantOnCaseId, String defendantName, String offenceDescription,
            boolean inCourt, Date courtLogDate) {
        super(operation, caseId, caseNumber, caseType, chargeType, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.verdictValue = verdictValue;
        setCaseSubType(caseSubType);
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
        setCrestOffenceSeqNo(crestOffenceSeqNo);
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setDefendantName(defendantName);
        setOffenceDescription(offenceDescription);
    }

    // Constructor for Verdict On Offence without logging
    public VerdictSaveValue(VerdictValue verdictValue, String operation, Integer caseId, Integer caseNumber,
            String caseType, String caseSubType, Integer crestOffenceId, Integer crestDefendantId, String chargeType) {
        super(operation, caseId, caseNumber, caseType, chargeType);
        this.verdictValue = verdictValue;
        setCaseSubType(caseSubType);
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
    }

    // Constructor for Verdict on Case
    public VerdictSaveValue(VerdictValue verdictValue, String operation, Integer caseId, Integer caseNumber,
            String caseType, Integer scheduledHearingId, String caseSubType, Integer defendantOnCaseId,
            String defendantName, boolean inCourt, Date courtLogDate) {
        super(operation, caseId, caseNumber, caseType, null, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.verdictValue = verdictValue;
        setCaseSubType(caseSubType);
        setDefendantName(defendantName);
    }

    // Constructor for Verdict (Appeal Result) on Disposal (Magistrate
    // General)
    public VerdictSaveValue(VerdictValue verdictValue, String operation, Integer caseId, Integer caseNumber,
            String caseType, Integer scheduledHearingId, String caseSubType, String chargeType, Integer crestChargeId,
            Integer crestChargeSeqNo, Integer defendantOnCaseId, String defendantName, boolean inCourt,
            Date courtLogDate) {
        super(operation, caseId, caseNumber, caseType, chargeType, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.verdictValue = verdictValue;
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setCaseSubType(caseSubType);
        setDefendantName(defendantName);
    }

    // Constructor for Misc Appeal result
    public VerdictSaveValue(VerdictValue verdictValue, String operation, Integer caseId, Integer caseNumber,
            String caseType, Integer scheduledHearingId, String caseSubType, Integer defendantOnCaseId,
            String defendantName, boolean inCourt, Date courtLogDate, Date hearingDate, Long lastCalculatedDuration) {
        super(operation, caseId, caseNumber, caseType, null, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);
        this.verdictValue = verdictValue;
        setCaseSubType(caseSubType);
        setDefendantName(defendantName);
        setLastCalculatedDuration(lastCalculatedDuration);
        setHearingDate(hearingDate);
    }

    // Get basic value
    public VerdictValue getVerdictValue() {
        return verdictValue;
    }

    public XhbVerdictBasicValue getVerdictBasicValue() {
        return verdictValue.getXhbVerdictBasicValue();
    }

    public void setVerdictBasicValue(XhbVerdictBasicValue xhbVerdictBasicValue) {
        verdictValue.setVerdictBasicValue(xhbVerdictBasicValue);
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

    public Date getHearingDate() {
        return verdictValue.getHearingDate();
    }

    public void setHearingDate(Date hearingDate) {
        verdictValue.setHearingDate(hearingDate);
    }

    public void setLastCalculatedDuration(Long lastCalculatedDuration) {
        verdictValue.setLastCalculatedDuration(lastCalculatedDuration);
    }

    public Long getLastCalculatedDuration() {
        return verdictValue.getLastCalculatedDuration();
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    // Accessors
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

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public Integer getCrestChargeId() {
        return crestChargeId;
    }

    public void setCrestChargeId(Integer crestChargeId) {
        this.crestChargeId = crestChargeId;
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

    public void setObsInd(boolean obsInd) {
        verdictValue.setObsInd(obsInd);
    }

    public DisposalSaveValue getDisposalSaveValue() {
        return disposalSaveValue;
    }

    public void setDisposalSaveValue(DisposalSaveValue disposalSaveValue) {
        this.disposalSaveValue = disposalSaveValue;
    }

    // Court Log Business
    public String getRefVerdictDesc() {
        return verdictValue.getRefVerdictDesc();
    }

    public String getRefVerdictCode() {
        return verdictValue.getRefVerdictCode();
    }

    public String getAltRefOffenceDesc() {
        if (isCriminalAppealOnOffence()) {
            return verdictValue.getAppLesserOffence();
        } else {
            return verdictValue.getAltRefOffenceDesc();
        }
    }

    // Crest Business

    public String getCrestVerdict() {
        return verdictValue.getRefVerdictCode();
    }

    public String getCrestLessOffVerdict() {
        if (isCriminalAppealOnOffence()) {
            return verdictValue.getAppLesserOffence();
        } else {
            return verdictValue.getAltRefOffenceCode();
        }
    }

    public String getCrestLessOffVerdictDesc() {
        return verdictValue.getAltRefOffenceDesc();
    }

    public boolean isCrestLessOffVerdictUncoded() {
        return ResultValue.UNCODED_OFFENCE.equalsIgnoreCase(getCrestLessOffVerdict());
    }

    public boolean hasAppealLesserOffence() {
        return verdictValue.getAppLesserOffence() != null;
    }

    public String getCrestOtherVerdict() {
        return verdictValue.getOtherVerdictText();
    }

    public String getCrestDateType() {
        return getVcoFlag();
    }

    // xhb_verdict.verdict_date is never sent to Mercator, it is assumed
    // that
    // vcoDate = verdict date
    public String getCrestVerdictDate() {
        return formatCrestDate(getVcoDate());
    }

    public String getCrestCaseAppResult() {
        return getCrestVerdict();
    }

    public String getCrestOffenceAppResult() {
        return verdictValue.getRefAppealOffenceCode();
    }

    public String getCrestAppResultDate() {
        // Appeal results do not have VCO dates therefore send verdict date
        return formatCrestDate(getVerdictDate());
    }

    public String getCrestHearingDate() {
        return formatCrestDate(getHearingDate());
    }

    public Integer getCrestDuration() {
        Long lastCalculatedDuration = getLastCalculatedDuration();
        if (lastCalculatedDuration == null) {
            return null;
        } else {
            // Convert from milliseconds into minutes
            return new Integer((int) (lastCalculatedDuration.longValue() / 60000));
        }
    }

    // Business
    public boolean isCriminalAppeal() {
        return isCriminalAppeal(getCaseSubType());
    }

    public boolean isMiscAppeal() {
        return isMiscAppeal(getCaseSubType());
    }

    public boolean isCriminalAppealOnCase() {
        return isCriminalAppeal() && isOnCase();
    }

    public boolean isCriminalAppealOnOffence() {
        return isCriminalAppeal() && isOnOffence();
    }

    private boolean isMiscellaneousAppealOnCase() {
        return isMiscAppeal() && isOnCase();
    }

    public int getVerdictType() {
        if (verdictType != 0) {
            return verdictType;
        } else if (VERDICT.equals(verdictValue.getRefVerdictType())) {
            verdictType = getNonAppealVerdictType();
        } else {
            verdictType = getAppealVerdictType();
        }
        return verdictType;
    }

    public Integer getCourtLogEvent() {
        return getCourtLogEvent(getVerdictType());
    }

    // wrapper support
    public Integer getDefendantChargeId() {
        return verdictValue.getDefendantChargeId();
    }

    public Integer getDefendantOnOffenceId() {
        return this.verdictValue.getDefendantOnOffenceId();
    }

    public Integer getJurorsAssenting() {
        return this.verdictValue.getJurorsAssenting();
    }

    public Integer getJurorsDissenting() {
        return this.verdictValue.getJurorsDissenting();
    }

    public int getAssenting() {
        return verdictValue.getJurorsAssenting() != null ? getJurorsAssenting().intValue() : 0;
    }

    public int getDissenting() {
        return verdictValue.getJurorsDissenting() != null ? getJurorsDissenting().intValue() : 0;
    }

    public boolean isObsolete() {
        return this.verdictValue.isObsolete();
    }

    public Integer getRefVerdictId() {
        return this.verdictValue.getRefVerdictId();
    }

    public Calendar getVerdictDateCalendar() {
        return verdictValue.getVerdictDateCalendar();
    }

    public Date getVerdictDate() {
        return verdictValue.getVerdictDate();
    }

    public Integer getRefAppResultId() {
        return this.verdictValue.getRefAppResultId();
    }

    public String getOtherVerdictText() {
        return verdictValue.getOtherVerdictText();
    }

    public Integer getCaseId() {
        return this.verdictValue.getCaseId();
    }

    public boolean isOnOffence() {
        return this.verdictValue.isOnOffence();
    }

    public boolean isOnCharge() {
        return this.verdictValue.isOnCharge();
    }

    public boolean isOnCase() {
        return this.verdictValue.isOnCase();
    }

    public boolean isOnDisposal() {
        return this.verdictValue.isOnDisposal();
    }

    public boolean isResultForMagistrateGeneralDisposal() {
        return isCriminalAppeal() && isOnDisposal();
    }

    public void setCccTransToRefCourtId(Integer cccTransToRefCourtId) {
        this.verdictValue.setCccTransToRefCourtId(cccTransToRefCourtId);
    }

    public Integer getCccTransToRefCourtId() {
        return this.verdictValue.getCccTransToRefCourtId();
    }

    public void setCccTransToRefCourtCode(String cccTransToRefCourtCode) {
        this.verdictValue.setCccTransToRefCourtCode(cccTransToRefCourtCode);
    }

    public String getCccTransToRefCourtCode() {
        return this.verdictValue.getCccTransToRefCourtCode();
    }

    public void setCccTransToRefCourtDesc(String cccTransToRefCourtDesc) {
        this.verdictValue.setCccTransToRefCourtDesc(cccTransToRefCourtDesc);
    }

    public String getCccTransToRefCourtDesc() {
        return this.verdictValue.getCccTransToRefCourtDesc();
    }

    public Integer getOriginalRefPleaId() {
        return verdictValue.getOriginalRefPleaId();
    }

    public Date getOriginalPleaDate() {
        return verdictValue.getOriginalPleaDate();
    }

    public Integer getOriginalRefVerdictId() {
        return verdictValue.getOriginalRefVerdictId();
    }

    public Date getOriginalVerdictDate() {
        return verdictValue.getOriginalVerdictDate();
    }

    // Return true if verdict must record juror details
    public boolean hasAssentingDissenting() {
        return verdictValue.hasAssentingDissenting();
    }

    // Return true if verdict can record juror details
    public boolean getJurorsAssentingOption() {
        return verdictValue.getJurorsAssentingOption();
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(VerdictSaveValue.class.getName());
        buffer.append(" {");
        super.appendDebugParameters(buffer);
        buffer.append(", verdictType=");
        buffer.append(verdictType);
        buffer.append(", crestOffenceId=");
        buffer.append(crestOffenceId);
        buffer.append(", crestDefendantId=");
        buffer.append(crestDefendantId);
        buffer.append(", crestChargeId=");
        buffer.append(crestChargeId);
        buffer.append(", crestOffenceSeqNo=");
        buffer.append(crestOffenceSeqNo);
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
        buffer.append(", caseSubType=");
        buffer.append(caseSubType);
        buffer.append(", verdictValue=");
        buffer.append(verdictValue);
        buffer.append("}");
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        return new Object[] { getOraCode(), getCourtLogCaseType(), getCourtLogCaseNumber(), getCrestChargeSeqNo(),
                getCrestOffenceSeqNo(), getDefendantName(), getRefVerdictCode()

        };
    }

    // private methods
    private int getNonAppealVerdictType() {
        if (ResultSaveValue.DELETE.equals(getOperation())) {
            return VerdictTypeEvent.DELETE_VERDICT_TYPE;
        }

        String refVerdictCode = getRefVerdictCode();
        if (VerdictTypeEvent.GUILTY.equals(refVerdictCode)) {
            return majorityOrUnamimous(getAssenting(), getDissenting());
        } else if (VerdictTypeEvent.GUILTY_JUDGES_DIRECTION.equals(refVerdictCode)) {
            return VerdictTypeEvent.GUILTY_JUDGES_DIRECTION_TYPE;
        } else if (VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA.equals(refVerdictCode)) {
            return VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA_TYPE;
        } else if (VerdictTypeEvent.NOT_GUILTY.equals(refVerdictCode)) {
            return VerdictTypeEvent.NOT_GUILTY_TYPE;
        } else if (VerdictTypeEvent.NOT_GUILTY_JU.equals(refVerdictCode)) {
            return VerdictTypeEvent.NOT_GUILTY_JU_TYPE;
        } else if (VerdictTypeEvent.NOT_GUILTY_JUDGES_DIRECTION.equals(refVerdictCode)) {
            return VerdictTypeEvent.NOT_GUILTY_JUDGES_DIRECTION_TYPE;
        } else if (VerdictTypeEvent.NOT_GUILTY_JUDGE_UNDER_DUC_VA2004.equals(refVerdictCode)) {
            return VerdictTypeEvent.NOT_GUILTY_JUDGE_UNDER_DUC_VA2004_TYPE;
        } else if (VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA.equals(refVerdictCode)) {
            return VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA_TYPE;
        } else if (VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA.equals(refVerdictCode)) {
            return VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA_TYPE;
        } else if (VerdictTypeEvent.ALTERNATE.equals(refVerdictCode)) {
            return VerdictTypeEvent.ALTERNATE_TYPE;
        } else if (VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION.equals(refVerdictCode)) {
            return VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION_TYPE;
        } else if (VerdictTypeEvent.LESSER.equals(refVerdictCode)) {
            return VerdictTypeEvent.LESSER_TYPE;
        } else if (VerdictTypeEvent.LESSER_JUDGES_DIRECTION.equals(refVerdictCode)) {
            return VerdictTypeEvent.LESSER_JUDGES_DIRECTION_TYPE;
        } else {
            return VerdictTypeEvent.OTHER_TYPE;
        }
    }

    private int getAppealVerdictType() {
        if (ResultSaveValue.DELETE.equals(getOperation())) {
            if (isOnCase()) {
                return VerdictTypeEvent.DELETE_APPEAL_CASE_TYPE;
            } else if (isOnOffence()) {
                return VerdictTypeEvent.DELETE_APPEAL_OFFENCE_TYPE;
            } else if (isOnDisposal()) {
                return VerdictTypeEvent.DELETE_APPEAL_DISPOSAL_TYPE;
            }
        } else {
            if (isMiscellaneousAppealOnCase()) {
                return VerdictTypeEvent.CASE_LEVEL_MISC_APPEAL_RESULT_TYPE;
            }

            if (isCriminalAppeal()) {
                if (isOnCase()) {
                    return VerdictTypeEvent.CASE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE;
                }

                if (isOnDisposal()) {
                    return VerdictTypeEvent.DISPOSAL_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE;
                }

                final String altOffence = getAltRefOffenceDesc();

                if ((altOffence != null) && (altOffence.trim().length() > 0)) {
                    return VerdictTypeEvent.OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE_ALT;
                } else {
                    return VerdictTypeEvent.OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE;
                }
            }
        }
        return VerdictTypeEvent.INVALID_TYPE;
    }

    private int majorityOrUnamimous(int assenting, int dissenting) {
        if (dissenting == 0) // unanimous
        {
            return VerdictTypeEvent.GUILTY_UNANIMOUS_TYPE;
        } else if (assenting > dissenting) // majority
        {
            return VerdictTypeEvent.GUILTY_MAJORITY_TYPE;
        } else {
            return VerdictTypeEvent.INVALID_TYPE;
        }
    }

    private Integer getCourtLogEvent(int type) {
        switch (type) {
        case VerdictTypeEvent.GUILTY_MAJORITY_TYPE:
            return VerdictTypeEvent.GUILTY_MAJORITY_EVENT;
        case VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA_TYPE:
            return VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA_EVENT;
        case VerdictTypeEvent.GUILTY_UNANIMOUS_TYPE:
            return VerdictTypeEvent.GUILTY_UNANIMOUS_EVENT;
        case VerdictTypeEvent.GUILTY_JUDGES_DIRECTION_TYPE:
            return VerdictTypeEvent.GUILTY_JUDGES_DIRECTION_EVENT;
        case VerdictTypeEvent.NOT_GUILTY_TYPE:
            return VerdictTypeEvent.NOT_GUILTY_EVENT;
        case VerdictTypeEvent.NOT_GUILTY_JU_TYPE:
            return VerdictTypeEvent.NOT_GUILTY_EVENT;
        case VerdictTypeEvent.NOT_GUILTY_JUDGES_DIRECTION_TYPE:
            return VerdictTypeEvent.NOT_GUILTY_EVENT;
        case VerdictTypeEvent.NOT_GUILTY_JUDGE_UNDER_DUC_VA2004_TYPE:
            return VerdictTypeEvent.NOT_GUILTY_EVENT;
        case VerdictTypeEvent.ALTERNATE_TYPE:
            return VerdictTypeEvent.ALTERNATE_OFFENCE_EVENT;
        case VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION_TYPE:
            return VerdictTypeEvent.ALTERNATE_OFFENCE_EVENT;
        case VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA_TYPE:
            return VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA_EVENT;
        case VerdictTypeEvent.LESSER_TYPE:
            return VerdictTypeEvent.LESSER_OFFENCE_EVENT;
        case VerdictTypeEvent.LESSER_JUDGES_DIRECTION_TYPE:
            return VerdictTypeEvent.LESSER_OFFENCE_EVENT;
        case VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA_TYPE:
            return VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA_EVENT;
        case VerdictTypeEvent.OTHER_TYPE:
            return VerdictTypeEvent.OTHER_EVENT;
        case VerdictTypeEvent.DELETE_VERDICT_TYPE:
            return VerdictTypeEvent.DeleteVerdictOnIndictment;
        case VerdictTypeEvent.DELETE_APPEAL_CASE_TYPE:
            return VerdictTypeEvent.DeleteCaseLevelAppealResult;
        case VerdictTypeEvent.DELETE_APPEAL_OFFENCE_TYPE:
            return VerdictTypeEvent.DeleteOffenceLevelAppealResult;
        case VerdictTypeEvent.DELETE_APPEAL_DISPOSAL_TYPE:
            return VerdictTypeEvent.DeleteDisposalLevelAppealResult;
        case VerdictTypeEvent.CASE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE:
            return VerdictTypeEvent.CaseLevelCriminalAppealResult;
        case VerdictTypeEvent.OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE:
            return VerdictTypeEvent.OffenceLevelCriminalAppealResult;
        case VerdictTypeEvent.OFFENCE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE_ALT:
            return VerdictTypeEvent.OffenceLevelCriminalAppealResultWithAltOffence;
        case VerdictTypeEvent.CASE_LEVEL_MISC_APPEAL_RESULT_TYPE:
            return VerdictTypeEvent.CaseLevelMiscellaneousAppealResult;
        case VerdictTypeEvent.DISPOSAL_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE:
            return VerdictTypeEvent.DisposalLevelCriminalAppealResult;
        default:
            return null;
        }
    }
}
