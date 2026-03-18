package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Date;

/**
 * <p>
 * Title: HRStartEndDates
 * </p>
 * <p>
 * Description: Convenience class to hold a combination of start and end dates
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class HRStartEndDates implements HRValueObject {
	private static final long serialVersionUID = -6531144895023767463L;
	private Date startDate;

    private Date endDate;

    public HRStartEndDates() {
    }

    public HRStartEndDates(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}