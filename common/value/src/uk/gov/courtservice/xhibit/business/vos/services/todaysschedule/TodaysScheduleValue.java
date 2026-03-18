package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: TodaysScheduleValue
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
 * @author Joseph Babad
 * @version $Id: TodaysScheduleValue.java,v 1.3 2006/06/05 12:28:49 bzjrnl Exp $
 */
public class TodaysScheduleValue extends CSAbstractValue {
	private static final long serialVersionUID = 7416485197946078058L;
	private Integer courtId;

    private Collection scheduledHearings = new Vector();

    public TodaysScheduleValue() {
    }

    public TodaysScheduleValue(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Collection getScheduledHearings() {
        return scheduledHearings;
    }

    public void setScheduledHearings(Collection scheduledHearings) {
        this.scheduledHearings = new Vector(scheduledHearings);
    }

    /**
     * @param scheduledHearingValue
     */
    public void addScheduledHearing(ScheduledHearingValue scheduledHearingValue) {
        scheduledHearings.add(scheduledHearingValue);
    }
}
