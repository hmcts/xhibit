package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

public class AttendeeHistoryValue extends CSAbstractValue {
	private static final long serialVersionUID = -1951207141375296237L;
    private ScheduledHearingBasicValue scheduledHearingBasicValue;

    private Collection personValueList;

    public AttendeeHistoryValue() {
    }

    public AttendeeHistoryValue(Integer id, Integer version) {
        super(id, version);
    }

    public void setPersonValueList(Collection personValueList) {
        this.personValueList = personValueList;
    }

    public Collection getPersonValueList() {
        return this.personValueList;
    }

    public void setScheduledHearingBasicValue(ScheduledHearingBasicValue basicValue) {
        this.scheduledHearingBasicValue = basicValue;
    }

    public ScheduledHearingBasicValue getScheduledHearingBasicValue() {
        return this.scheduledHearingBasicValue;
    }
}