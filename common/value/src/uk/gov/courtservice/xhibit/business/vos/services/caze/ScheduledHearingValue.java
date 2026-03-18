package uk.gov.courtservice.xhibit.business.vos.services.caze;

// jdk
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
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version 1.0
 */

public class ScheduledHearingValue extends CSAbstractValue {
    private Integer scheduledHearingID;

    private Calendar scheduledHearingDate;
    private static final long serialVersionUID = 4378160364569859576L;
    
    private String caseActive;

    public ScheduledHearingValue() {
    }

    public Calendar getScheduledHearingDate() {
        return scheduledHearingDate;
    }

    public Integer getScheduledHearingID() {
        return scheduledHearingID;
    }

    public void setScheduledHearingID(Integer scheduledHearingID) {
        this.scheduledHearingID = scheduledHearingID;
    }

    public void setScheduledHearingDate(Calendar scheduledHearingDate) {
        this.scheduledHearingDate = scheduledHearingDate;
    }

    public String getCaseActive() {
        return caseActive;
    }

    public void setCaseActive(String caseActive) {
        this.caseActive = caseActive;
    }
}