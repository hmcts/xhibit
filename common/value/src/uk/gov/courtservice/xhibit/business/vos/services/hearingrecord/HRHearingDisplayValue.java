package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

/**
 * Value object to store the read-only hearing information required on a CREST
 * form 'A'. This will also hold realted read-only value objects. found from
 * Hearing
 */
public class HRHearingDisplayValue implements HRValueObject {
    private Integer hearingID;

    private HRJudgeValue hrJudgeValue;

    private Collection hrJusticeValues; // max of 4

    private Collection hrScheduledHearingValues;
    
    private static final long serialVersionUID = -962131153032323043L;

    public Collection getHrScheduledHearingValues() {
        return this.hrScheduledHearingValues;
    }

    public void setHrScheduledHearingValues(Collection hrScheduledHearingValues) {
        this.hrScheduledHearingValues = hrScheduledHearingValues;
    }

    public HRHearingDisplayValue() {
    }

    public HRHearingDisplayValue(Integer id) {
        this.hearingID = id;
    }

    public Integer getHearingID() {
        return hearingID;
    }

    public void setHearingID(Integer id) {
        this.hearingID = id;
    }

    public HRJudgeValue getHrJudgeValue() {
        return hrJudgeValue;
    }

    public Collection getHrJusticeValues() {
        return hrJusticeValues;
    }

    public void setHrJusticeValues(Collection hrJusticeValues) {
        this.hrJusticeValues = hrJusticeValues;
    }

    public void setHrJudgeValue(HRJudgeValue hrJudgeValue) {
        this.hrJudgeValue = hrJudgeValue;
    }
}
