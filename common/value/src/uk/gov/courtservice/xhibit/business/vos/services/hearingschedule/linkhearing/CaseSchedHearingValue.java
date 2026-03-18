package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing;

// jdk
import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

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
 * @author Sarah Tong
 * @version 1.0
 */

public class CaseSchedHearingValue implements Serializable {
	private static final long serialVersionUID = 2780213908638059247L;
	private Integer caseId;

    private String caseType;

    private Integer caseNumber;

    private ScheduledHearingBasicValue shbValue = new ScheduledHearingBasicValue();

    public CaseSchedHearingValue() {
    }

    public Integer getCaseId() {
        return caseId;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public Integer getHearingProgress() {
        return shbValue.getHearingProgress(); // (0=TBH, 1=IP, 2=FIN)
    }

    public Integer getScheduledHearingId() {
        return shbValue.getId();
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setHearingProgress(Integer hearingProgress) {
        shbValue.setHearingProgress(hearingProgress); // (0=TBH, 1=IP, 2=FIN)
    }

    public ScheduledHearingBasicValue getShbValue() {
        return shbValue;
    }

    public void setShbValue(ScheduledHearingBasicValue shbValue) {
        this.shbValue = shbValue;
    }
}