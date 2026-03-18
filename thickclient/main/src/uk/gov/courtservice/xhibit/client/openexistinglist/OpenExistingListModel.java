package uk.gov.courtservice.xhibit.client.openexistinglist;

import java.util.Date;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.client.listings.notes.AbstractNotesModel;

public class OpenExistingListModel  extends AbstractNotesModel {

	private Date dateFrom;
	private Date dateTo;
	
	/**
	 * Strips the time from a date object
	 * @param date	The Date object to be processed
	 * @return the date object minus the time
	 */
	public Date getDateWithoutTime(Date date) {
		return date != null ? DateTimeUtilities.stripTimeToUtilDate(date) : null;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = getDateWithoutTime(dateFrom);
	}

	/**
	 * @return the dateTo
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(Date dateTo) {
		this.dateTo = getDateWithoutTime(dateTo);
	}



}
