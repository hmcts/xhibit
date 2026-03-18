package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Finds the daily List for specified court id and calendar date.
 * 
 * @author grewalg
 *
 */
public class CourtDailyListing {

	private static CourtDailyListing instance;
	
	private static BisRefControllerBeanBusinessDelegate bizRefDelegate;

	public static CourtDailyListing getInstance() {
		if (instance == null) {
			instance = new CourtDailyListing();
			bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		}
		return instance;
	}

	private CourtDailyListing() {
	}
	/**
	 * Returns true or false depending if a daily listing was found for a given
	 * court id and date.
	 * 
	 * @param courtId
	 * @param date
	 * @return
	 */
	public boolean hasDailyListing(int courtId, Date diaryDate) {
		Collection<ListBasicValue> listings = bizRefDelegate.getFinalDailyListByCourtAndDate(courtId, diaryDate);
		if (listings != null && listings.size() >= 1) {
			return true;
		} else {
			return false;
		}
	}
}
