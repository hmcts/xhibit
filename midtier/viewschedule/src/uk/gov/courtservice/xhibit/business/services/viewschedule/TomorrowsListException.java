package uk.gov.courtservice.xhibit.business.services.viewschedule;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * @author Meeraj
 * @title ViewSCheduleException
 */
public class TomorrowsListException extends CSBusinessException {

    private static final long serialVersionUID = -8459461017375060031L;
    /**
     * Error key for no daily list data
     */
    public static final String TOMORROWS_LIST_NO_DATA = "viewSchedule.tomorrowslist.noData";

    /**
     * Default constructor
     * 
     */
    public TomorrowsListException() {
        super(TOMORROWS_LIST_NO_DATA, "No data found");
    }

}
