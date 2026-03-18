package uk.gov.courtservice.xhibit.common.results.vos;

// jdk
import java.io.Serializable;

import uk.gov.courtservice.framework.business.vos.Parameterizeable;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBasicValue;

public class CaseAppReasonValue extends ResultValue implements Comparable, Parameterizeable, Serializable {
    
	static final long serialVersionUID = -2535319851953158454L;
	
	private XhbCaseAppReasonBasicValue caseAppReason;

    // Constructors
    public CaseAppReasonValue() {
        this.caseAppReason = new XhbCaseAppReasonBasicValue();
    }

    public CaseAppReasonValue(XhbCaseAppReasonBasicValue caseAppReason) {
        if (caseAppReason == null) {
            throw new IllegalArgumentException("caseAppReason is null");
        }
        this.caseAppReason = caseAppReason;
    }

    public CaseAppReasonValue(String appReason, Integer caseId) {
        if (appReason == null) {
            throw new IllegalArgumentException("appReason is null");
        }
        this.caseAppReason = new XhbCaseAppReasonBasicValue();
        this.caseAppReason.setAppReason(appReason);
        this.caseAppReason.setCaseId(caseId);

    }

    public XhbCaseAppReasonBasicValue getXhbCaseAppReasonBasicValue() {
        return caseAppReason;
    }

    // Set basic value
    public void setCaseAppReasonBasicValue(XhbCaseAppReasonBasicValue caseAppReasonBasicValue) {
        this.caseAppReason = caseAppReasonBasicValue;
    }

    // Comparable implementation
    public int compareTo(Object obj) {
        return compareTo((CaseAppReasonValue) obj);
    }

    public int compareTo(CaseAppReasonValue value) {
        int value1 = getCarId(this);
        int value2 = getCarId(value);
        return (value1 < value2 ? -1 : (value1 == value2 ? 0 : 1));
    }

    private static int getCarId(CaseAppReasonValue value) {
        // Return max value for null so they are unsorted at end!
        Integer carId = value.getCarId();
        return carId == null ? Integer.MAX_VALUE : carId.intValue();
    }

    // accessors and mutators

    // wrapper support

    public String getAppReason() {
        return caseAppReason.getAppReason();
    }

    public void setAppReason(String appReason) {
        caseAppReason.setAppReason(appReason);
    }

    public Integer getCaseId() {
        return caseAppReason.getCaseId();
    }

    public void setCaseId(Integer caseId) {
        caseAppReason.setCaseId(caseId);
    }

    public boolean isObsolete() {
        return "Y".equals(caseAppReason.getObsInd());
    }

    public void setObsInd(boolean obsInd) {
        caseAppReason.setObsInd(obsInd ? "Y" : "N");
    }

    public Integer getCaseAppReasonId() {
        return caseAppReason.getCaseAppReasonId();
    }

    public Integer getCarId() {
        return caseAppReason.getCarId();
    }

    public void setCarId(Integer carId) {
        caseAppReason.setCarId(carId);
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(CaseAppReasonValue.class.getName());
        buffer.append(" {caseAppReason=");
        buffer.append(caseAppReason);
        buffer.append("}");
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        /**
         * @todo decide what parameters to pass in message
         */
        return new String[0];
    }

}