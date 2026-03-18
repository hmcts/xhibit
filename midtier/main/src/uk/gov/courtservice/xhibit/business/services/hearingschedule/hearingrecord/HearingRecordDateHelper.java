package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

import java.util.Date;

/**
 * <p>
 * Title: DateHelper
 * </p>
 * <p>
 * Description: There is a lot of proecessing and validation for different dates
 * for the hearing record. The validation is always done with comparison of the
 * HearingStart and end dates. This class will just hold the firstDate and the
 * endDate.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class HearingRecordDateHelper {

    private Date firstHearingDate;

    private Date lastHearingDate;

    public HearingRecordDateHelper() {
    }

    public HearingRecordDateHelper(Date firstHearingDate, Date lastHearingDate) {
        this.firstHearingDate = firstHearingDate;
        this.lastHearingDate = lastHearingDate;
    }

    // Getters and setters
    public void setFirstHearingDate(Date firstHearingDate) {
        this.firstHearingDate = firstHearingDate;
    }

    public void setLastHearingDate(Date lastHearingDate) {
        this.lastHearingDate = lastHearingDate;
    }

    public Date getFirstHearingDate() {
        return this.firstHearingDate;
    }

    public Date getLastHearingDate() {
        return this.lastHearingDate;
    }
}