package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;

public class WarnedListingDiaryModel extends ListingDiaryModel {
	
	private Calendar listEndDate;

	public WarnedListingDiaryModel(final Calendar listDate, final Calendar listEndDate, final ListModel listModel) {
		super(listDate, listModel);
		this.listEndDate = listEndDate;
	}

	public Calendar getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Calendar listEndDate) {
		this.listEndDate = listEndDate;
	}

}
