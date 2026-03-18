package uk.gov.courtservice.xhibit.client.schedule;

/**
 * <p>
 * Title: XHIBIT 2
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
 * @author unascribed
 * @version 1.0
 */

public class ScheduledHearingValueHelper {
    private uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue model;

    public ScheduledHearingValueHelper(
            uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue obj) {
        setModel(obj);
    }

    public String toString() {
        return (getModel().getCaseType() == null ? "" : getModel().getCaseType())
                + (getModel().getCaseNumber() == null ? "" : getModel().getCaseNumber().toString());
        // return getModel().getCaseType() + getModel().getCaseNumber();
    }

    public Integer getSittingSequence() {
        return this.model.getSittingSequenceNo();
    }

    public void setSittingSequence() {
        // do nothing...only required for sorting
    }

    public Integer getSequence() {
        return this.model.getSequenceNo();
    }

    public void setSequence() {
        // do nothing...only required for sorting
    }

    public void setModel(uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue model) {
        this.model = model;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue getModel() {
        return model;
    }
}