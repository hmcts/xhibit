package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;
import java.util.Date;

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
 * 
 * This is the value object that holds all value objects that can be updated
 * from the GUI.
 */
public class HRScheduledHearingValue implements HRValueObject {
    private Integer scheduledHearingId;

    private Date originalTime;

    private Collection hrCourtReporter;
    private static final long serialVersionUID = -113757796816979005L;

    public HRScheduledHearingValue() {
    }

    public Integer getScheduledHearingId() {
        return this.scheduledHearingId;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public Date getOriginalTime() {
        return this.originalTime;
    }

    public void setOriginalTime(Date originalTime) {
        this.originalTime = originalTime;
    }

    public Collection getHrCourtReporter() {
        return this.hrCourtReporter;
    }

    public void setHrCourtReporter(Collection hrCourtReporter) {
        this.hrCourtReporter = hrCourtReporter;
    }

}