package uk.gov.courtservice.xhibit.client.listings.list.firm;

import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmDiaryPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Panel for firm listing diary.
 * 
 * @author uphillj
 *
 */
public class FirmListingDiaryPanel extends AbstractDailyFirmDiaryPanel {
	
	private static final long serialVersionUID = 1L;

	public FirmListingDiaryPanel(final ListingDiaryModel listingDiaryModel) {
		super(listingDiaryModel);
	}

	/**
	 * Returns a warned list if one exists or null 
	 * if neither exist for diary date.
	 * Note: Different to Daily.
	 * 
	 * @return ListBasicValue list data.
	 */
	protected ListBasicValue getNonFixedList() {

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
		return XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingsNonFixedNoListFirm");  
	}
	
}
