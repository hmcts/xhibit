package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import java.util.Date;

/**
 * <p>
 * Title: AssignedRepresentativesCriteriaValue
 * </p>
 * <p>
 * Description: Criteria value object for use by GUI to retrieve list of
 * scheduled hearings for a specific date. Used for assigning representatives to
 * a case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class AssignedRepresentativesCriteriaValue {

    private Integer courtId;

    private Date scheduleDate;

    private Integer courtRoomId;

    public AssignedRepresentativesCriteriaValue(Integer courtId, Date scheduleDate) {
        setCourtId(courtId);
        setDate(scheduleDate);
    }

    // getters

    public Integer getCourtId() {
        return this.courtId;
    }

    public Integer getCourtRoomId() {
        return this.courtRoomId;
    }

    public Date getDate() {
        return this.scheduleDate;
    }

    // setters

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public void setDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

}