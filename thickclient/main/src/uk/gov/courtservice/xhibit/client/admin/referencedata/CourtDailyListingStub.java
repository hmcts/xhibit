package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CourtDailyListingStub {

	private static CourtDailyListingStub stub;

	private static List<XhbList> dailyListings;

	private static final String[] publishStatus = { "SUCCESS", "FAILURE" };

	public static CourtDailyListingStub getInstance() {
		if (stub == null) {
			stub = new CourtDailyListingStub();
		}
		return stub;
	}

	private CourtDailyListingStub() {
		dailyListings = new ArrayList<XhbList>();
		populateDailyListing();
	}

	/**
	 * Create data for xhb list.
	 */
	private static void populateDailyListing() {
		Calendar datePointer = Calendar.getInstance();
		Date now = new Date();
		datePointer.setTime(now);
		int year = datePointer.get(Calendar.YEAR);
		int month = datePointer.get(Calendar.MONTH);
		int day = datePointer.get(Calendar.DAY_OF_MONTH);
		datePointer = new GregorianCalendar(year, month, day);

		Calendar date;

		String status = null;
		String listType = null;
		for (int i = 0; i < 100; i++) {
			date = Calendar.getInstance();
			date.setTime(datePointer.getTime());
			if (i % 5 == 0) {
				status = publishStatus[1];
			} else {
				status = publishStatus[0];
			}
			if (i % 11 == 0) {
				listType = "weekly";
			} else {
				listType = "daily";
			}
			XhbList xhbList = new XhbList(81, date, status, listType);
			dailyListings.add(xhbList);
			datePointer.add(Calendar.DATE, -1);
		}
	}

	/**
	 * Returns true or false depending if a daily listing was found for a given
	 * court id and date.
	 * 
	 * @param courtId
	 * @param date
	 * @return
	 */
	public boolean findDailyListing(int courtId, Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		for (XhbList dailyList : dailyListings) {
			if (calDate.equals(dailyList.getListStartDate()) && courtId == dailyList.getCourtId()
					&& publishStatus[0].equals(dailyList.getPublishStatus())
					&& "daily".equals(dailyList.getListType())) {
				return true;
			}
		}
		return false;
	}
}
