package uk.gov.courtservice.xhibit.common.results.vos;

// XHIBIT
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBasicValue;

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
 * @author unascribed
 * @version 1.0
 */
public class CaseAppReasonSaveValue extends ResultSaveValue {
	
	static final long serialVersionUID = -4782449400799596245L;
	
    private CaseAppReasonValue caseAppReasonValue; // Data!

    // Constructor
    public CaseAppReasonSaveValue(CaseAppReasonValue caseAppReasonValue, String operation, Integer caseId,
            Integer caseNumber, String caseType) {
        super(operation, caseId, caseNumber, caseType, null);
        this.caseAppReasonValue = caseAppReasonValue;
    }

    public CaseAppReasonSaveValue(String appReason, String operation, Integer caseId, Integer caseNumber,
            String caseType) {
        super(operation, caseId, caseNumber, caseType, null);
        this.caseAppReasonValue = new CaseAppReasonValue(appReason, caseId);
    }

    // Get basic value
    public XhbCaseAppReasonBasicValue getCaseAppReasonBasicValue() {
        return caseAppReasonValue.getXhbCaseAppReasonBasicValue();
    }

    // Set basic value
    public void setCaseAppReasonBasicValue(XhbCaseAppReasonBasicValue caseAppReasonBasicValue) {
        caseAppReasonValue.setCaseAppReasonBasicValue(caseAppReasonBasicValue);
    }

    // Accessors
    public void setObsInd(boolean obsInd) {
        caseAppReasonValue.setObsInd(obsInd);
    }

    // wrapper support
    public String getAppReason() {
        return caseAppReasonValue.getAppReason();
    }

    public void setAppReason(String appReason) {
        caseAppReasonValue.setAppReason(appReason);
    }

    public Integer getCaseId() {
        return caseAppReasonValue.getCaseId();
    }

    public void setCaseId(Integer caseId) {
        caseAppReasonValue.setCaseId(caseId);
    }

    public boolean isObsolete() {
        return caseAppReasonValue.isObsolete();
    }

    public Integer getCaseAppReasonId() {
        return caseAppReasonValue.getCaseAppReasonId();
    }

    public Integer getCarId() {
        return caseAppReasonValue.getCarId();
    }

    public void setCarId(Integer carId) {
        caseAppReasonValue.setCarId(carId);
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(CaseAppReasonSaveValue.class.getName());
        buffer.append(" {");
        super.appendDebugParameters(buffer);
        buffer.append(", caseAppReasonValue=");
        caseAppReasonValue.appendDebug(buffer, indent);
        buffer.append("}");
    }

    /**
     * For an insertion operation returns the order in which results should be
     * process currently this is 1. Pleas, 2. Verdicts, 3. CaseAppReason 4.
     * Disposals
     * 
     * @return
     */
    public Integer getOperationSequence() {
        return new Integer(3);
    }
}