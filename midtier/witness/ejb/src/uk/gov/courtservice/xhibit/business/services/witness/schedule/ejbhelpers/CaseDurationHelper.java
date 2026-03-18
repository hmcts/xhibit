package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.6 $
 * 
 */
public class CaseDurationHelper {
    public static final Integer TRIAL_HOUR_UNIT = new Integer(CSServices.getConfigServices().getBundle(
            "XHIBITDirectionsResources").getString("E40702_Hours_DB"));

    public static final Integer TRIAL_DAY_UNIT = new Integer(CSServices.getConfigServices().getBundle(
            "XHIBITDirectionsResources").getString("E40702_Days_DB"));

    public static final Integer TRIAL_WEEK_UNIT = new Integer(CSServices.getConfigServices().getBundle(
            "XHIBITDirectionsResources").getString("E40702_Weeks_DB"));

    public static final Integer TRIAL_MONTH_UNIT = new Integer(CSServices.getConfigServices().getBundle(
            "XHIBITDirectionsResources").getString("E40702_Months_DB"));

    public static final Integer TRIAL_YEAR_UNIT = new Integer(CSServices.getConfigServices().getBundle(
            "XHIBITDirectionsResources").getString("E40702_Years_DB"));

    public static float getDaysFromDurationAndUnit(final float duration, final Integer unit) {
        if (unit.equals(TRIAL_HOUR_UNIT)) {
            return duration / 5;
        } else if (unit.equals(TRIAL_DAY_UNIT)) {
            return duration;
        } else if (unit.equals(TRIAL_MONTH_UNIT)) {
            return duration * 20;
        } else if (unit.equals(TRIAL_WEEK_UNIT)) {
            return duration * 5;
        } else if (unit.equals(TRIAL_YEAR_UNIT)) {
            return duration * 240;
        } else {
            throw new CSUnrecoverableException("The unit of time is not recognized (id was:" + unit + ")");
        }
    }

}
