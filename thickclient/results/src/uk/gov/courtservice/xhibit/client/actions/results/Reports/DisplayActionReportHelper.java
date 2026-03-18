package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

/**
 * Helper class for Displaying reports.
 * @author westalll
 *
 */
public class DisplayActionReportHelper {
	
	/**
	 * Method to set the common values in ReportAbsttractValue
	 * @param reportList
	 * @throws CSRecoverableException
	 */
	public void setupReportDefaults(ReportAbsttractValue reportList) throws CSRecoverableException {
		//Set the court name here as we don't retrieve it from the database
		reportList.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
		//set the username
		reportList.setUserName(XhibitSingleton.getInstance().getUserSession().getUserName());
		//Set time of report to now.
		reportList.setTimeOfReport(XDateFormat.format(Calendar.getInstance(), XDateFormat.TIMEFORMAT));	
		//Set date of report to today.
		reportList.setDateOfReport(DateFormat.getDateInstance().format(new Date()));
	}	
}
