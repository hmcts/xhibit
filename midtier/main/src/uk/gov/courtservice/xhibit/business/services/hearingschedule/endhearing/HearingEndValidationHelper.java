package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: HearingEndValidationHelper
 * </p>
 * <p>
 * Description: Validation class that will contain validations etc.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 */
public class HearingEndValidationHelper {
    private static final Logger log = CSServices.getLogger(HearingEndValidationHelper.class);

    /**
     * Validate the start and end date. The start date must be before the end
     * date. Neither start or end date can be in the future.
     * 
     * @param startDate
     *            Date
     * @param endDate
     *            Date
     * @throws HearingEndedDateCalculationException
     */
    public static void validateDates(Date startDate, Date endDate) throws HearingEndedDateCalculationException {
        log.debug("HearingEndValidationHelper.validateDates(startDate, endDate) called");
        log.debug("startDate : " + startDate + ", endDate : " + endDate);

        // Set the start, end and today Calendar objects with time removed
        Calendar startCal = HearingEndHelper.convertDateToCalendarDay(startDate);
        Calendar endCal = HearingEndHelper.convertDateToCalendarDay(endDate);
        Calendar todayCal = HearingEndHelper.convertDateToCalendarDay(new Date());

        // check start date so not in future
        if (startCal.after(todayCal)) {
            throw new HearingEndedDateCalculationException(EndHearingConstants.START_AFTER_TODAY, "StartDate "
                    + startCal.getTime() + " is after Today's date " + todayCal.getTime());
        }

        // check end date so not in future
        if (endCal.after(todayCal)) {
            throw new HearingEndedDateCalculationException(EndHearingConstants.END_AFTER_TODAY, "EndDate "
                    + endCal.getTime() + " is after Today's date " + todayCal.getTime());
        }

        // check so that startdate is not after end date.
        if (startCal.after(endCal)) {
            throw new HearingEndedDateCalculationException(EndHearingConstants.START_AFTER_END, "StartDate "
                    + startCal.getTime() + " is after EndDate " + endCal.getTime());
        }
    }
}
