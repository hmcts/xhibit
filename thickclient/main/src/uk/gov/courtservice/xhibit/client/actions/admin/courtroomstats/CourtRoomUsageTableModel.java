package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * Table model for a Court Room usage Table.
 * @author westalll
 *
 */
public class CourtRoomUsageTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COL_COURT_ROOM_NAME = 0;
	public static final int COL_SITTING_DATE= 1;
    public static final int COL_AM_TIME= 2;
	public static final int COL_PM_TIME = 3;
	
	private XDialog parent;


	private static final List EDITABLE_COLUMNS = Arrays.asList(COL_AM_TIME, COL_PM_TIME); 
	/**
	 * Setup empty table
	 */
	public CourtRoomUsageTableModel(final XDialog parent) {
		super();
		setColumnNames(new String[] { 
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.roomusage.col.courtname"), 
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.roomusage.col.sittingdate"), 
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.roomusage.col.amhoursmins"), 
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.roomusage.col.pmhoursmins") });
		this.parent = parent;
	}
	
	/**
	 * Setup table from supplied model.
	 * @param data
	 */
	public CourtRoomUsageTableModel(CourtRoomUsageRow[] data, final XDialog parent) {
		this(parent);
		super.setData(data);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	        switch (columnIndex) {
	        	case COL_COURT_ROOM_NAME:
	        		return String.class;
	        	case COL_SITTING_DATE:
	        		return Date.class;
	        	case COL_AM_TIME:
		            return String.class;
	        	case COL_PM_TIME:
		            return String.class;    
		        default:
		        	throw new UnsupportedOperationException("No data type for column "+columnIndex+ ".");
	        }
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return EDITABLE_COLUMNS.contains(columnIndex);
	}
		
	/**
	 * Get the value from the model for the row and column.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
        Object cellValue;

        if (_data.length <= 0) {
            return null;
        }

        final CourtRoomUsageRow row = (CourtRoomUsageRow)_data[rowIndex];
        switch (columnIndex) {
        	case COL_COURT_ROOM_NAME:
        		cellValue = row.getCourtRoomName();
        		break;
        	case COL_SITTING_DATE:
        		cellValue = row.getSittingDate();
        		break;    
        	case COL_AM_TIME:
			cellValue = String.format("%1$02d:%2$02d", getNotNullInteger(row.getAmHours()),
					getNotNullInteger(row.getAmMins()));
	            break;
	        case COL_PM_TIME:
			cellValue = String.format("%1$02d:%2$02d", getNotNullInteger(row.getPmHours()),
					getNotNullInteger(row.getPmMins()));
	            break;
	        default:
	            cellValue = null;
	            break;
        }

        return cellValue;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		final CourtRoomUsageRow row = (CourtRoomUsageRow)_data[rowIndex];
		switch (columnIndex) {
		
		case COL_AM_TIME: {
			if (validateTimeText((String) aValue)) {
				final String values[] = ((String) aValue).split(":");
				row.setAmHours(Integer.parseInt(values[0]));
				row.setAmMins(Integer.parseInt(values[1]));
				row.setDirty(true);
				this.fireTableDataChanged();
			} else {
				setErrorMessage((String) aValue);
			}
			break;
		}
		case COL_PM_TIME: {
			if (validateTimeText((String) aValue)) {
				final String values[] = ((String) aValue).split(":");
				row.setPmHours(Integer.parseInt(values[0]));
				row.setPmMins(Integer.parseInt(values[1]));
				row.setDirty(true);
				this.fireTableDataChanged();
			} else {
				setErrorMessage((String) aValue);
			}
			break;
		}
		default:
			break;
		}
	}
	
	//Validate that the time format is sort of right.
	private Boolean validateTimeText(final String text) {
		Pattern pattern = Pattern.compile("[0-1][0-9]:[0-5][0-9]");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	
	private void setErrorMessage(String aValue) {
		
		String errorMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.ErrorText, 
				"CourtroomStatistics.invalidCourtRoomUsageTime"), new Object[] { aValue});
		
		XMessageBox.alert(parent,
				XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"), true,
				XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		
	}
	
	private Integer getNotNullInteger(Integer value) {
		return value == null ?  Integer.valueOf(0) : value;
	}
	
	
	/**
	 * Add the case to the model.
	 * 
	 * @param caseModel
	 */
	public void addCase(CourtRoomUsageRow row) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data)); 
		}
		
		// Add case if it is not already in the list
		if (!list.contains(row)) {
			list.add(row);
			setData(list);
		}
	}
	

	/**
	 * Add a collection of cases to the model.
	 * @param rows The collection of rows to add.
	 */
	public void addRows(Collection<CourtRoomUsageRow> rows) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data));
		}

		for (final CourtRoomUsageRow row : rows) {
			// Add case if it is not already in the list
			if (!list.contains(row)) {
				list.add(row);
			}
		}
		setData(list);
	}
	
	/**
	 * Remove the case from the model.
	 * 
	 * @param CourtRoomUsageRow
	 */
	public void removeCase(CourtRoomUsageRow row) {
		// Convert model to list to be able to remove
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data)); 
		}
		
		// Remove case if it is in the list
		if (list.contains(row)) {
			list.remove(row);
			setData(list);
		}
	}
}

