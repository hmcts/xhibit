package uk.gov.courtservice.xhibit.client.listings.listresults;

import java.util.Date;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class ListResultsModel {

	protected XhibitApplicationController xac;
	private Date listDate;
	
	public ListResultsModel(XhibitApplicationController xac) {
		setXac(xac);
	}
	
	public XhibitApplicationController getXac() {
		return xac;
	}

	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}
	
	/**
	 * Strips the time from a date object
	 * @param date	The Date object to be processed
	 * @return the date object minus the time
	 */
	public Date getDateWithoutTime(Date date) {
		return date != null ? DateTimeUtilities.stripTimeToUtilDate(date) : null;
	}

	/**
	 * @return the listDate
	 */
	public Date getListDate() {
		return listDate;
	}

	/**
	 * @param listDate the listDate to set
	 */
	public void setListDate(Date listDate) {
		this.listDate = getDateWithoutTime(listDate);
	}

}
