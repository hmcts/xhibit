package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;

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
 * @author Bal Bhamra
 * @version 1.0
 */

public class CourtStaffHistoryRow {
    public final PersonValue person;

    public Date startDate;

    private final Logger log = CSServices.getLogger(UpdateCourtStaffModel.class);

    public CourtStaffHistoryRow(PersonValue person, Date startDate) {
        this.person = person;
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        try {
            this.startDate = startDate;
        } catch (Exception e) {
            log.error(e);
        }
    }

    public Integer getSchedHearingAttendeeId() {
        return person.getParentId();
    }

    public void setSchedHearingAttendeeId(Integer schedHearingAttendeeId) {
        person.setParentId(schedHearingAttendeeId);
    }
}