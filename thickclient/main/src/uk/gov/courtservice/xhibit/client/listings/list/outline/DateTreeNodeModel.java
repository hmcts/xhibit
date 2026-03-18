package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Model class for data required for a date in the outline control.
 * 
 * @author uphillj
 *
 */
public class DateTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;

	private Calendar listDate;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public Calendar getListDate() {
		return listDate;
	}

	public void setListDate(Calendar listDate) {
		this.listDate = listDate;
	}

	@Override
	public String getDisplayName() {
		return dateFormat.format(listDate.getTime());
	}
	
}
