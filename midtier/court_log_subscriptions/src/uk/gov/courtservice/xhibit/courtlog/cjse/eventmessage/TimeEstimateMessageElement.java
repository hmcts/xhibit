package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: TimeEstimateMessageElement
 * </p>
 * <p>
 * Description: Retrieves the trial time estimate, in days, from either the
 * 'Time estimation for trial' (20901) or the 'Directions For Case' (40702)
 * Xhibit court log event.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: TimeEstimateMessageElement.java,v 1.1 2004/04/20 14:40:58
 *          pznwc5 Exp $
 */

public class TimeEstimateMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(TimeEstimateMessageElement.class);

    // The Directions by Case Xhibit event type
    private static final Integer DIRECTIONS_BY_CASE = new Integer(40711);

    // The key to the Time Estimate XPaths in the properties file
    private static final String TIME_EST_XPATH_KEY = "timeEstXpath";

    private static final String TIME_EST_UNITS_XPATH_KEY = "timeEstUnitsXpath";

    private static final String DIR_TIME_EST_XPATH_KEY = "dirTimeEstXpath";

    private static final String DIR_TIME_EST_UNITS_XPATH_KEY = "dirTimeEstUnitsXpath";

    // Time unit values
    private static final String WEEKS_KEY = "timeEstWeeks";

    private static final String MONTHS_KEY = "timeEstMonths";

    private static final String DIR_WEEKS_KEY = "dirTimeEstWeeks";

    private static final String DIR_MONTHS_KEY = "dirTimeEstMonths";

    private static final int DAYS_IN_WEEK = 7;

    // uses the same value as
    private static final double DAYS_IN_MONTH = 365.0 / 12.0;

    private Properties _messageElementProperties;

    private String _xpathTime;

    private String _xpathUnits;

    private String _dirXpathTime;

    private String _dirXpathUnits;

    /**
     * load properties file and retrieve xpaths
     */
    public TimeEstimateMessageElement() {
        _messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        _xpathTime = _messageElementProperties.getProperty(TIME_EST_XPATH_KEY);
        _xpathUnits = _messageElementProperties.getProperty(TIME_EST_UNITS_XPATH_KEY);
        _dirXpathTime = _messageElementProperties.getProperty(DIR_TIME_EST_XPATH_KEY);
        _dirXpathUnits = _messageElementProperties.getProperty(DIR_TIME_EST_UNITS_XPATH_KEY);
    }

    /**
     * Retrieves the Trial Time Estimate from the court log event. The estimate
     * is required in days but may be recorded in days, weeks or months from the
     * GUI. If recorded in wekks or months the value is multiplied up to a
     * 'days' value - decimal places are also required (in the case of months).
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the estimate.
     * @param theCase
     *            not used
     * @return The Trial Time Estimate in Days
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);
        String time;
        String units;
        // check which court log event this has come from
        if (value.getCourtLogViewValue().getEventType().equals(DIRECTIONS_BY_CASE)) {
            // get the time value and the units from the Directions By Case
            // event
            time = CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _dirXpathTime);
            units = CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _dirXpathUnits);
            log.debug("time = " + time + " and units = " + units);
            time = calculateDays(time, units, DIR_WEEKS_KEY, DIR_MONTHS_KEY);
        } else {
            // get the time value and the units from the Time estimation for
            // trial event
            time = CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _xpathTime);
            units = CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _xpathUnits);
            log.debug("time = " + time + " and units = " + units);
            time = calculateDays(time, units, WEEKS_KEY, MONTHS_KEY);
        }

        log.debug("Returning TimeEstimateMessageElement " + time);
        return time;
    }

    private String calculateDays(String time, String units, String weeksKey, String monthsKey) {
        // convert to days value if recorded in weeks or months
        if (units.equals(_messageElementProperties.getProperty(weeksKey))) {
            time = (new Double(time)).doubleValue() * DAYS_IN_WEEK + "";
            if (time.indexOf(".0") != -1) {
                // trim trailing '.0'
                time = time.substring(0, time.indexOf("."));
            }
        } else if (units.equals(_messageElementProperties.getProperty(monthsKey))) {
            time = getDaysFromMonths(time);
        }
        return time;
    }

    private String getDaysFromMonths(String numMonths) {
        String days = DAYS_IN_MONTH * (new Double(numMonths)).doubleValue() + "";
        if (days.indexOf(".0") != -1) {
            // trim trailing '.0'
            days = days.substring(0, days.indexOf("."));
        } else {
            // keep last two dps only (to conserve space in text message)
            days = days.substring(0, days.indexOf(".") + 3);
        }

        return days;
    }
}