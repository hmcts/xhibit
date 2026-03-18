package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader;

import java.util.Calendar;
import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;

/**
 * <p>
 * Title: HearingHeaderValue
 * </p>
 * <p>
 * Description: Value Object for representing a Hearing Header display data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: HearingHeaderValue.java,v 1.4 2006/06/05 12:28:46 bzjrnl Exp $
 */
public class HearingHeaderValue extends CSAbstractValue {
	private static final long serialVersionUID = 7547993989389500589L;
	private Calendar timeListed;

    private String hearingType;

    private Collection staffValues;

    private Collection legalRepValues;

    private Collection attendeeHistoryValues;
    
    private Collection shJusticeValues;

    private CaseSchedHearingValue[] caseSchedHearingValues;

    private Integer hearingProgress;

    private CaseBasicValue hhCase;

    public HearingHeaderValue() {
    }

    public void setTimeListed(Calendar timeListed) {
        this.timeListed = timeListed;
    }

    public Calendar getTimeListed() {
        return timeListed;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    public String getHearingType() {
        return hearingType;
    }

    public void setStaffValues(Collection staffValues) {
        this.staffValues = staffValues;
    }

    public java.util.Collection getStaffValues() {
        return staffValues;
    }

    public void setLegalRepValues(Collection legalRepValues) {
        this.legalRepValues = legalRepValues;
    }

    public java.util.Collection getLegalRepValues() {
        return legalRepValues;
    }

    public void setAttendeeHistoryValues(Collection attendeeHistoryValues) {
        this.attendeeHistoryValues = attendeeHistoryValues;
    }

    public java.util.Collection getAttendeeHistoryValues() {
        return attendeeHistoryValues;
    }

    public void setCaseSchedHearingValues(CaseSchedHearingValue[] caseSchedHearingValues) {
        this.caseSchedHearingValues = caseSchedHearingValues;
    }

    public CaseSchedHearingValue[] getCaseSchedHearingValues() {
        return caseSchedHearingValues;
    }

    public void setHearingProgress(Integer hearingProgress) {
        this.hearingProgress = hearingProgress;
    }

    public Integer getHearingProgress() {
        return hearingProgress;
    }

    public void setHhCase(CaseBasicValue hhCase) {
        this.hhCase = hhCase;
    }

    public CaseBasicValue getHhCase() {
        return hhCase;
    }

	public Collection getShJusticeValues() {
		return shJusticeValues;
	}

	public void setShJusticeValues(Collection shJusticeValues) {
		this.shJusticeValues = shJusticeValues;
	}
}
