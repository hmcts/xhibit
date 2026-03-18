package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;

/**
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.27 $
 */
public class DisposalSaveValue extends CourtLogSaveValue {
	
	static final long serialVersionUID = -8328741340101093046L;
	
    private DisposalValue disposalValue; // Data!

    private DisposalReferenceValue disposalReferenceValue; // Reference

    // Data!

    private Integer crestOffenceId; // Required by CREST

    private Integer crestDefendantId; // Required by CREST

    private Integer psdDisId; // Required by CREST

    private Integer crestChargeId; // Required by Court Log

    private Integer crestOffenceSeqNo; // Required by Court Log

    private Integer crestChargeSeqNo; // Required by Court Log

    private String defendantName; // Required by Court Log

    private String caseSubType; // Required by Court Log

    // Constructor for Disposal on Case (Unrelated)
    public DisposalSaveValue(DisposalValue disposalValue, DisposalReferenceValue disposalReferenceValue,
            String operation, Integer caseId, Integer caseNumber, String caseType, String caseSubType,
            Integer crestDefendantId, Integer defendantOnCaseId, String defendantName, boolean inCourt,
            Date courtLogDate, Integer scheduledHearingId, Integer psdDisId) {
        super(operation, caseId, caseNumber, caseType, null, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);

        this.disposalValue = disposalValue;
        this.disposalReferenceValue = disposalReferenceValue;

        setCaseSubType(caseSubType);
        setCrestDefendantId(crestDefendantId);
        setDefendantOnCaseId(defendantOnCaseId);
        setDefendantName(defendantName);
        setPsdDisId(psdDisId);
    }

    // Constructor for Disposal on Offence
    public DisposalSaveValue(DisposalValue disposalValue, DisposalReferenceValue disposalReferenceValue,
            String operation, Integer caseId, Integer caseNumber, String caseType, String caseSubType,
            Integer crestOffenceId, Integer crestDefendantId, Integer crestOffenceSeqNo, Integer crestChargeId,
            Integer crestChargeSeqNo, String chargeType, Integer defendantOnCaseId, String defendantName,
            boolean inCourt, Date courtLogDate, Integer scheduledHearingId, Integer psdDisId) {
        super(operation, caseId, caseNumber, caseType, chargeType, defendantOnCaseId, scheduledHearingId, courtLogDate,
                inCourt);

        this.disposalValue = disposalValue;
        this.disposalReferenceValue = disposalReferenceValue;

        setCaseSubType(caseSubType);
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
        setCrestOffenceSeqNo(crestOffenceSeqNo);
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setDefendantOnCaseId(defendantOnCaseId);
        setDefendantName(defendantName);
        setPsdDisId(psdDisId);
    }

    // Constructor for Disposal on Offence non logged
    public DisposalSaveValue(DisposalValue disposalValue, DisposalReferenceValue disposalReferenceValue,
            String operation, Integer caseId, Integer caseNumber, String caseType, String caseSubType,
            Integer crestOffenceId, Integer crestDefendantId, Integer crestChargeId, String chargeType, Integer psdDisId) {
        super(operation, caseId, caseNumber, caseType, chargeType);

        this.disposalValue = disposalValue;
        this.disposalReferenceValue = disposalReferenceValue;

        setCaseSubType(caseSubType);
        setCrestOffenceId(crestOffenceId);
        setCrestDefendantId(crestDefendantId);
        setCrestOffenceSeqNo(crestOffenceSeqNo);
        setCrestChargeId(crestChargeId);
        setCrestChargeSeqNo(crestChargeSeqNo);
        setPsdDisId(psdDisId);
    }

    // Get basic value
    public DisposalValue getDisposalValue() {
        return disposalValue;
    }

    public XhbDisposal2BasicValue getDisposalBasicValue() {
        return disposalValue.getDisposal();
    }

    public void setDisposalBasicValue(XhbDisposal2BasicValue xhbDisposal2BasicValue) {
        disposalValue.setDisposal(xhbDisposal2BasicValue);
    }

    public DisposalReferenceValue getDisposalReferenceValue() {
        return disposalReferenceValue;
    }

    // MidTier Accessors
    // Accessors
    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public String getCaseSubType() {
        return caseSubType;
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

    public Integer getCrestOffenceSeqNo() {
        return crestOffenceSeqNo;
    }

    public void setCrestOffenceSeqNo(Integer crestOffenceSeqNo) {
        this.crestOffenceSeqNo = crestOffenceSeqNo;
    }

    public String getDefendantName() {
        return defendantName;
    }

    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    public void setObsInd(boolean obsInd) {
        disposalValue.setObsInd(obsInd);
    }

    public boolean getObsInd() {
        return disposalValue.getObsInd();
    }

    public void setPsdDisId(Integer psdDisId) {
        this.psdDisId = psdDisId;
    }

    public Integer getPsdDisId() {
        return psdDisId;
    }

    // Crest Business

    public Integer getTemplateVersion() {
        return new Integer(disposalReferenceValue.getTemplateVersion());
    }

    public String getDisposalCode() {
        return disposalReferenceValue.getDisposalCode();
    }

    public boolean isCriminalAppeal() {
        return isCriminalAppeal(getCaseSubType());
    }

    // Get the disposal line save value objects (for saving to CREST ie
    // conversion to MVO)
    public DisposalLineSaveValue[] getDisposalLineSaveValues() {
        List arrayList = new ArrayList();

        for (int i = 0, c = disposalReferenceValue.getLineCount(); i < c; i++) {
            DisposalLineReferenceValue line = disposalReferenceValue.getLine(i);
            Integer refDisposalLineId = new Integer(line.getRefDisposalLineId());
            Integer dilSeqNo = new Integer(line.getDilSeqNo());
            for (int j = 0, s = disposalValue.getLineCount(refDisposalLineId); j < s; j++) {
                arrayList.add(new DisposalLineSaveValue(disposalValue.getLine(refDisposalLineId, j), dilSeqNo));
            }
        }

        return (DisposalLineSaveValue[]) arrayList.toArray(new DisposalLineSaveValue[arrayList.size()]);
    }

    // Get the disposal line value objects (for saving to XHIBIT)
    public XhbDisposalLineBasicValue[] getDisposalLineValues() {
        List arrayList = new ArrayList();

        for (int i = 0, c = disposalReferenceValue.getLineCount(); i < c; i++) {
            Integer refDisposalLineId = new Integer(disposalReferenceValue.getLine(i).getRefDisposalLineId());
            for (int j = 0, s = disposalValue.getLineCount(refDisposalLineId); j < s; j++) {
                arrayList.add(disposalValue.getLine(refDisposalLineId, j));
            }
        }

        return (XhbDisposalLineBasicValue[]) arrayList.toArray(new XhbDisposalLineBasicValue[arrayList.size()]);
    }

    // Set the disposal line values (update keys and versions after create)
    // lines MUST all exist allready
    public void setDisposalLineValues(XhbDisposalLineBasicValue[] disposalLineValues) {
        if (disposalLineValues == null) {
            throw new IllegalArgumentException("disposalLineValues: null");
        }

        for (int i = 0; i < disposalLineValues.length; i++) {
            disposalValue.setLine(disposalLineValues[i]);
        }
    }

    // wrapper support
    public Integer getDisposal2Id() {
        return disposalValue.getDisposal2Id();
    }

    public boolean isUnrelatedDisposal() {
        return disposalValue.isUnrelatedDisposal();
    }

    public boolean isRelatedDisposal() {
        return disposalValue.isRelatedDisposal();
    }

    public boolean isMagistrateGeneralDisposal() {
        return disposalValue.isMagistrateGeneralDisposal();
    }

    public boolean isVariationDisposal() {
        return disposalValue.isVariationDisposal();
    }

    public boolean isVariationForMagistrateGeneralDisposal() {
        return isCriminalAppeal() && isVariationDisposal() && isUnrelatedDisposal();
    }

    // method required to be overridden for determining if a defendant on
    // offence is to be set...
    public boolean isOnOffence() {
        return isRelatedDisposal();
    }

    public Integer getDisId() {
        return disposalValue.getDisId();
    }

    public void setDisId(Integer disId) {
        disposalValue.setDisId(disId);
    }

    public String getCourtType() {
        return disposalValue.getCourtType();
    }

    public void setCourtType(String courtType) {
        disposalValue.setCourtType(courtType);
    }

    public Integer getPsdDisposal2Id() {
        return disposalValue.getPsdDisposal2Id();
    }

    public void setPsdDisposal2Id(Integer psdDisposal2Id) {
        disposalValue.setPsdDisposal2Id(psdDisposal2Id);
    }

    public Integer getRefDisposalTypeId() {
        return new Integer(disposalValue.getRefDisposalTypeId());
    }

    public void setRefDisposalTypeId(Integer refDisposalTypeId) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId is null");
        }
        disposalValue.setRefDisposalTypeId(refDisposalTypeId.intValue());
    }

    public Integer getDefendantOnOffenceId() {
        return new Integer(disposalValue.getDefendantOnOffenceId());
    }

    // wrapper support for DisposalReference

    public String getRefDisposalTitle() {
        return this.disposalReferenceValue.getTitle();
    }

    public String getRefDisposalCode() {
        return this.disposalReferenceValue.getDisposalCode();
    }

    public String getDisposalDetail() {
        return this.disposalReferenceValue.getResultSheetText(this.disposalValue);
    }

    /**
     * Gets the disposal line data for the given prompt.
     * 
     * @param prompt
     *            the disposal line prompt.
     * @return the data for the given disposal line prompt.
     */
    public String getDisposalLineDataByPrompt(final String prompt) {
        return this.disposalReferenceValue.getDisposalLineDataByPrompt(this.disposalValue, prompt);
    }

    // court log business

    public Integer getCourtLogEvent() {
        String operation = getOperation();
        if (isRelatedDisposal()) {
            if (ResultSaveValue.ADD.equals(operation))// create
            {
                return DisposalTypeEvent.CREATE_RELATED_DISPOSAL;
            } else if (ResultSaveValue.UPDATE.equals(operation))// update
            {
                return DisposalTypeEvent.EDIT_RELATED_DISPOSAL;
            } else if (ResultSaveValue.DELETE.equals(operation)) // delete
            {
                return DisposalTypeEvent.DELETE_RELATED_DISPOSAL;
            }
        } else {
            if (ResultSaveValue.ADD.equals(operation))// create
            {
                return DisposalTypeEvent.CREATE_UNRELATED_DISPOSAL;
            } else if (ResultSaveValue.UPDATE.equals(operation))// update
            {
                return DisposalTypeEvent.EDIT_UNRELATED_DISPOSAL;
            } else if (ResultSaveValue.DELETE.equals(operation)) // delete
            {
                return DisposalTypeEvent.DELETE_UNRELATED_DISPOSAL;
            }
        }

        // only get to here if nothing else...
        return new Integer(-1);
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        return new Object[] { getOraCode(), getCourtLogCaseType(), getCourtLogCaseNumber(), getCrestChargeSeqNo(),
                getCrestOffenceSeqNo(), getDefendantName(), getDisposalCode()

        };
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(DisposalSaveValue.class.getName());
        buffer.append(" {");
        super.appendDebugParameters(buffer);
        buffer.append(", crestOffenceId=");
        buffer.append(crestOffenceId);
        buffer.append(", crestDefendantId=");
        buffer.append(crestDefendantId);
        buffer.append(", psdDisId=");
        buffer.append(psdDisId);
        buffer.append(", crestChargeId=");
        buffer.append(crestChargeId);
        buffer.append(", crestOffenceSeqNo=");
        buffer.append(crestOffenceSeqNo);
        buffer.append(", crestChargeSeqNo=");
        buffer.append(crestChargeSeqNo);
        buffer.append(", defendantName=");
        buffer.append(defendantName);
        buffer.append(", caseSubType=");
        buffer.append(caseSubType);
        indent += 1;
        appendLine(buffer, indent);
        disposalValue.appendDebug(buffer, indent);
        buffer.append(",");
        appendLine(buffer, indent);
        disposalReferenceValue.appendDebug(buffer, indent);
        indent -= 1;
        appendLine(buffer, indent);
        buffer.append("}");
    }
}
