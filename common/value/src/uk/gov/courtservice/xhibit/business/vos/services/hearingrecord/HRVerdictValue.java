package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Date;

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
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class HRVerdictValue implements HRValueObject {
	private static final long serialVersionUID = 477142668129475420L;
	private Integer caseId;

    private Integer verdictId;

    private Date verdictDate;

    private Integer refVerdictId;

    private String verdictCode;

    public Integer getRefVerdictId() {
        return refVerdictId;
    }

    public Date getVerdictDate() {
        return verdictDate;
    }

    public Integer getVerdictId() {
        return verdictId;
    }

    public void setRefVerdictId(Integer refVerdictId) {
        this.refVerdictId = refVerdictId;
    }

    public void setVerdictDate(Date verdictDate) {
        this.verdictDate = verdictDate;
    }

    public void setVerdictId(Integer verdictId) {
        this.verdictId = verdictId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public String getVerdictCode() {
        return verdictCode;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setVerdictCode(String verdictCode) {
        this.verdictCode = verdictCode;
    }
}