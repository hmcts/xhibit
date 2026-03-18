package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

/**
 * Values come from Hearing
 * 
 * @author Anthony Martin
 * @version 1.1
 */
public class HearingRecordDisplayValue implements HRValueObject {

    private HRDefendantValue hrDefendantValue;

    private HRCaseValue hrCaseValue;

    private Collection hrCounselValue;

    private Collection hrLinkedCaseListValue;

    private HRHearingDisplayValue hrHearingDisplayValue;

    private HRVerdictValue verdictValue;
    
    private static final long serialVersionUID = 2853378248598981093L;

    public HRCaseValue getHrCaseValue() {
        return hrCaseValue;
    }

    public HRDefendantValue getHrDefendantValue() {
        return hrDefendantValue;
    }

    public HRHearingDisplayValue getHrHearingDisplayValue() {
        return hrHearingDisplayValue;
    }

    public void setHrHearingDisplayValue(HRHearingDisplayValue hrHearingDisplayValue) {
        this.hrHearingDisplayValue = hrHearingDisplayValue;
    }

    public void setHrDefendantValue(HRDefendantValue hrDefendantValue) {
        this.hrDefendantValue = hrDefendantValue;
    }

    public void setHrCaseValue(HRCaseValue hrCaseValue) {
        this.hrCaseValue = hrCaseValue;
    }

    public Collection getHrCounselValue() {
        return hrCounselValue;
    }

    public void setHrCounselValue(Collection hrCounselValue) {
        this.hrCounselValue = hrCounselValue;
    }

    public Collection getHrLinkedCaseListValue() {
        return hrLinkedCaseListValue;
    }

    public void setHrLinkedCaseListValue(Collection hrLinkedCaseListValue) {
        this.hrLinkedCaseListValue = hrLinkedCaseListValue;
    }

    public HRVerdictValue getVerdictValue() {
        return this.verdictValue;
    }

    public void setVerdictValue(HRVerdictValue verdictValue) {
        this.verdictValue = verdictValue;
    }
}
