package uk.gov.courtservice.xhibit.business.services.viewschedule;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * @author Meeraj
 * @title ViewSCheduleException
 */
public class DailyListException extends CSBusinessException {

    private static final long serialVersionUID = -8867160635449081084L;
    /**
     * Error key for no daily list data
     */
    public static final String DAILY_LIST_NO_DATA = "viewSchedule.dailyList.noData";

    /**
     * Default constructor
     * 
     */
    public DailyListException() {
        super(DAILY_LIST_NO_DATA, "No data found");
    }

}
