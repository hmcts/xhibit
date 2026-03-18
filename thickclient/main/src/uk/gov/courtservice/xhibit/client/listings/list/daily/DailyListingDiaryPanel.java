package uk.gov.courtservice.xhibit.client.listings.list.daily;

import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmDiaryPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Panel for daily listing diary.
 * 
 * @author westalll
 *
 */
public class DailyListingDiaryPanel extends AbstractDailyFirmDiaryPanel {

	private static final long serialVersionUID = -3318403267053926275L;
	
	/**
	 * Constructor for DailyListingDiaryPanel
	 * 
	 * @param The
	 *            DailyListModel to initialise from.
	 */
	public DailyListingDiaryPanel(final ListingDiaryModel listingDiaryModel) {
		super(listingDiaryModel);
	}

	/**
	 * Returns a firm list if one exists for the diary date,
	 * a warned list if firm does not exist and warned does,
	 * or null if neither exist for diary date.
	 * 
	 * Note: Different to Firm.
	 * 
	 * @return ListBasicValue list data.
	 */
	protected ListBasicValue getNonFixedList() {
		// If we have a firm list, return
		final ListBasicValue firmList = getFirmList();
		if (firmList != null) {
			return firmList;
		}

		// If we have a warned list return.
		final ListBasicValue warnedList = getWarnedList();
		if (warnedList != null) {
			return warnedList;
		}
		
		// We haven't returned a list so return null
		return null;
	}

	@Override
	protected String getNoListTypeMessage() {
		return XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingsNonFixedNoListDaily");  
	}
}
