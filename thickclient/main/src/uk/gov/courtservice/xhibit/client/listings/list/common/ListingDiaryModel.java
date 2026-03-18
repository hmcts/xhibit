package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Calendar;

/**
 * A simple model to represent a list type.
 * @author westalll
 *
 */
public class ListingDiaryModel {
	
	private Calendar listDate;
	private ListModel listModel;
	
	
	/**
	 * Constructor.
	 * 
	 * @param listDate
	 *            The date this list relates to.
	 * @param listTypeEnum
	 *            The type of list.
	 */
	public ListingDiaryModel(final Calendar listDate, final ListModel listModel) {
		this.listDate = listDate;
		this.listModel = listModel;
	}
	
	public Calendar getListDate() {
		return listDate;
	}
	public void setListDate(Calendar listDate) {
		this.listDate = listDate;
	}

	public ListModel getListModel() {
		return listModel;
	}
}
