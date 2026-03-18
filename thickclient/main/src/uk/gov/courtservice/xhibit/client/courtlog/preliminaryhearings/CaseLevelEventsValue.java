package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

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
 * @author unascribed
 * @version 1.0
 */

public class CaseLevelEventsValue extends CSAbstractValue {

    private Calendar indictmentBy;

    private Integer caseId;

    private Integer scheduledHearingId;

    private String freeText;

    private Calendar dateTime;

    private Boolean caseCalledOn;

    public CaseLevelEventsValue() {
    }

    public Calendar getIndictmentBy() {
        return indictmentBy;
    }

    public void setIndictmentBy(Calendar indictmentBy) {
        this.indictmentBy = indictmentBy;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getCaseCalledOn() {
        return caseCalledOn;
    }

    public void setCaseCalledOn(Boolean caseCalledOn) {
        this.caseCalledOn = caseCalledOn;
    }
}