package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Class containing the data model for the Court Calendar panels.
 * 
 * @author grewalg
 *
 */
public class CourtCalendarTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -6467537709432806782L;
	
	private static final Logger log = CSServices.getLogger(CourtCalendarPanel.class);

	private static String[] COLUMN_NAMES = new String[] {
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colMonday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colTuesday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colWednesday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colThursday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colFriday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colSaturday"),
			XHIBITConstant.getResource(XhibitBundles.CourtCalendar, "colSunday") };

	private int rows;
	private int preset;
	private int postset;
	private List<RefCalendarBasicValue> data;
	private List<RefCalendarValue> dates;
	private Calendar startDate;
	private Calendar endDate;
	private Calendar startOfWeek;
	private Calendar endOfWeek;
	private XPanel parent;

	public CourtCalendarTableModel(List<RefCalendarBasicValue> data, XPanel parent) {
		super();
		this.data = data;
		this.parent = parent;
	}

	@Override
	public int getRowCount() {
		return rows;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dates == null) {
			return null;
		}
		RefCalendarValue refCalendar = null;
		if (startOfWeek != null) {
			Calendar datePointer = Calendar.getInstance();
			datePointer.setTime(new Date(startOfWeek.getTimeInMillis()));

			int day = (rowIndex * 7) + columnIndex;
			datePointer.add(Calendar.DATE, day);

			if (!(datePointer.before(startDate) || datePointer.after(endDate)) && ((day - preset) < dates.size())) {
				refCalendar = dates.get(day - preset);
			}
		}
		return refCalendar;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		super.setValueAt(aValue, rowIndex, columnIndex);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Class<?> getColumnClass(int columnIndex) {
		return RefCalendarValue.class;
	}

	public String getColumnName(int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	public boolean isCellEditable(int columnIndex, int rowIndex) {
		return true;
	}

	/**
	 * Populates the Calendar Table after search is performed.
	 * 
	 * @param data
	 * @param fromDate
	 * @param toDate
	 */
	public void populateTableModel(List<RefCalendarBasicValue> data, Calendar fromDate, Calendar toDate) {
		String METHOD_NAME = "populateTableModel";
		log.debug("Entering " + METHOD_NAME + "(" + data + "," + fromDate + "," + toDate +")");
		this.data = data;
		this.startDate = fromDate;
		this.endDate = toDate;
		setDateRange();
		setRefCalendarEntries();
		setRows();
	}

	private void setDateRange() {
		String METHOD_NAME = "setDateRange";
		log.debug("Entering " + METHOD_NAME + "(" + ")");
		startOfWeek = Calendar.getInstance();
		startOfWeek.setTime(startDate.getTime());
		log.debug("startOfWeek: " + startOfWeek);

		int dayOfWeek = startOfWeek.get(Calendar.DAY_OF_WEEK);
		log.debug("dayOfWeek: " + dayOfWeek);
		log.debug("Entering while dayOfWeek != Calendar.MONDAY loop");
		while (dayOfWeek != Calendar.MONDAY) {
			startOfWeek.add(Calendar.DATE, -1);
			dayOfWeek = startOfWeek.get(Calendar.DAY_OF_WEEK);
			log.debug("LOOP dayOfWeek: " + dayOfWeek);
		}
		log.debug("end of loop");
		preset = getDaysRange(startOfWeek, startDate);
		log.debug("preset: " + preset);

		endOfWeek = Calendar.getInstance();
		endOfWeek.setTime(endDate.getTime());
		log.debug("endOfWeek: " + endOfWeek);
		dayOfWeek = endOfWeek.get(Calendar.DAY_OF_WEEK);
		log.debug("dayOfWeek: " + dayOfWeek);
		log.debug("Entering while dayOfWeek != Calendar.SUNDAY loop");
		while (dayOfWeek != Calendar.SUNDAY) {
			endOfWeek.add(Calendar.DATE, 1);
			dayOfWeek = endOfWeek.get(Calendar.DAY_OF_WEEK);
			log.debug("LOOP dayOfWeek: " + dayOfWeek);
		}
		log.debug("end of loop");
		postset = getDaysRange(endDate, endOfWeek);
		log.debug("postset: " + postset);
	}

	/**
	 * Sets the total number of table rows needed.
	 */
	private void setRows() {
		String METHOD_NAME = "setRows";
		log.debug("Entering " + METHOD_NAME + "(" + ")");
		int totalDays = 0;
		if (dates != null && dates.size() > 0) {
			log.debug("dates != null and is greater than 0");
			totalDays = preset + dates.size() + postset;
			rows = (int) Math.round(totalDays / 7d);
			log.debug("totalDays: " + totalDays);
			log.debug("rows: " + rows);
		} else {
			rows = 0;
			log.debug("dates are null so rows: " + rows);
		}
	}

	/**
	 * Sets entries in the RefCalendar between the specified start and end
	 * dates. If no data found then default values are used. -- No need for this check as
	 * it doesn't get to here unless you have the same number of entries back as what you've asked for
	 * so safe enough to do a set the values.
	 */
	private void setRefCalendarEntries() {
		log.debug("Entering setRefCalendarEntries()");
		log.debug("the amount of days we have data for is :"+data.size());
		dates = new ArrayList<RefCalendarValue>();

		for(int i=0;i<data.size();i++) {
			RefCalendarBasicValue entry = data.get(i);
			log.debug("entry is "+entry);
			String description = (entry.getDescription() == null || entry.getDescription().contains("null") ? "":entry.getDescription());
			log.debug("description is "+description);
			Calendar aDate = Calendar.getInstance();
			aDate.setTime(entry.getCalDate());
			log.debug("Day of the week is "+aDate.get(Calendar.DAY_OF_WEEK));
			String sysAcAvail = aDate.get(Calendar.DAY_OF_WEEK)!= Calendar.SATURDAY && aDate.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY ? "N" : entry.getSysAcAvail();
			log.debug("sysAcAvail is "+sysAcAvail);
			dates.add(new RefCalendarValue (entry.getId(), entry.getAvail(),sysAcAvail, entry.getCalDate(),
					description, false, false, entry.getVersion()));
		}
		log.debug("dates: "+dates.toString());
		log.debug("Exiting setRefCalendarEntries()");
	}

	/**
	 * Update domain list to pass for saving to DB.
	 */
	public RefCalendarVO updateTableModelData() {
		RefCalendarVO refCalendarVO = new RefCalendarVO();
		List<RefCalendarBasicValue> toUpdateList = new ArrayList<RefCalendarBasicValue>();

		for (RefCalendarValue rcv : dates) {
			if (rcv.isModified()) {
				decodeAvailability(rcv);
				toUpdateList.add(new RefCalendarBasicValue(rcv.getId(), rcv.getVersion(), rcv.getAvail(),
						rcv.getCalDate(), null, rcv.getDescription(), rcv.getSysAcAvail()));
				rcv.setModified(false);
			}
		}
		refCalendarVO.setUpdateList(toUpdateList);

		return refCalendarVO;
	}

	private int getDaysRange(Calendar startDate, Calendar endDate) {
		String METHOD_NAME = "getDaysRange";
		log.debug("Entering " + METHOD_NAME + "(" + startDate + "," + endDate + ")");
		long start = startDate.getTimeInMillis();
		long end = endDate.getTimeInMillis();
		int days = (int) TimeUnit.MILLISECONDS.toDays(end - start);
		
		log.debug("start: " + start);
		log.debug("end: " + end);
		log.debug("days: " + days);
		return days;
	}

	public List<RefCalendarValue> getTableData() {
		return dates;
	}

	/**
	 * Sets the availability fields according to the day of the week. For e.g.
	 * weekend days use sysAcAvail whereas weekdays use avail.
	 * 
	 * @param val
	 */
	private void decodeAvailability(RefCalendarValue val) {
		String METHOD_NAME = "decodeAvailability";
		log.debug("Entering " + METHOD_NAME + "(" + val + ")");
		Calendar date = Calendar.getInstance();
		date.setTime(val.getCalDate());

		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		log.debug("dayOfWeek: " + dayOfWeek);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			log.debug("day of week is Saturday or Sunday");
			val.setSysAcAvail(val.getAvail());
		} else {
			log.debug("avail is used for weekdays");
			// Do nothing as avail is used for weekdays anyway
		}
	}

	/**
	 * @return the parent
	 */
	public XPanel getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(XPanel parent) {
		this.parent = parent;
	}

	/**
	 * Sets the Parent's modified flag
	 * 
	 * @param hasModified
	 */
	public void setModified(boolean hasModified) {
		getParent().setModified(hasModified);
	}
}
