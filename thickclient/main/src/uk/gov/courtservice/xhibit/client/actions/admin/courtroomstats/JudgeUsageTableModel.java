package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * Class to represent a Judge usage table model
 * @author westalll
 *
 */

public class JudgeUsageTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COL_JUDGE_NAME = 0;
	public static final int COL_SITTING_DATE= 1;
    public static final int COL_COURT_ROOM_NAME= 2;
	public static final int COL_SAT_IN_COURT = 3;
	public static final int COL_SAT_IN_CHAMBERS = 4;
	
	private static final String SAT_IN_COURT = "CRT";
	private static final String SAT_IN_CHAMBERS = "CHA";
	
	private static final List EDITABLE_COLUMNS = Arrays.asList(COL_SAT_IN_COURT, COL_SAT_IN_CHAMBERS); 

	/**
	 * Setup empty table
	 */
	public JudgeUsageTableModel() {
		super();
		setColumnNames(new String[] { 
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.judgeusage.col.judgename"),
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.judgeusage.col.sittingdate"),
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.judgeusage.col.roomname"),
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.judgeusage.col.satincourt"),
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.judgeusage.col.satinchambers") });
	}
	
	public void setColumnWidths(JTable table) {
		table.getColumnModel().getColumn(COL_JUDGE_NAME).setPreferredWidth(600);
		table.getColumnModel().getColumn(COL_SITTING_DATE).setPreferredWidth(150);
		table.getColumnModel().getColumn(COL_COURT_ROOM_NAME).setPreferredWidth(150);
		table.getColumnModel().getColumn(COL_SAT_IN_COURT).setPreferredWidth(150);
		table.getColumnModel().getColumn(COL_SAT_IN_CHAMBERS).setPreferredWidth(150);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		   
	        switch (columnIndex) {
	        	case COL_JUDGE_NAME:
	        		return String.class;
	        	case COL_SITTING_DATE:
	        		return Date.class;
	        	case COL_COURT_ROOM_NAME:
		            return String.class;
		        case COL_SAT_IN_COURT:
		            return Boolean.class;
		        case COL_SAT_IN_CHAMBERS:
		            return Boolean.class;
		        default:
		        	throw new UnsupportedOperationException("No data type for column "+columnIndex+ ".");
	        }
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return EDITABLE_COLUMNS.contains(columnIndex);
	}
		
	/**
	 * Setup table from supplied model.
	 * @param data
	 */
	public JudgeUsageTableModel(JudgeUsageRow[] data) {
		this();
		super.setData(data);
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

        JudgeUsageRow row = (JudgeUsageRow)_data[rowIndex];
        switch (columnIndex) {
        	case COL_JUDGE_NAME:
        		cellValue = row.getJudgeName();
        		break;
        	case COL_SITTING_DATE:
        		cellValue = row.getSittingDate();
        		break;    
        	case COL_COURT_ROOM_NAME:
	            cellValue = row.getCourtRoomName();
	            break;
	        case COL_SAT_IN_COURT:
	            cellValue = SAT_IN_COURT.equals(row.getCourtChambersInd());
	            break;
	        case COL_SAT_IN_CHAMBERS:
	        	 cellValue = SAT_IN_CHAMBERS.equals(row.getCourtChambersInd());
	            break;
	        default:
	            cellValue = null;
	            break;
        }

        return cellValue;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		final JudgeUsageRow row = (JudgeUsageRow) _data[rowIndex];
		switch (columnIndex) {

		// One of these has to be set, not both.
		case COL_SAT_IN_COURT: {
			row.setCourtChambersInd((Boolean) aValue == true ? SAT_IN_COURT : SAT_IN_CHAMBERS);
			row.setDirty(true);
			this.fireTableDataChanged();
			break;
		}
		case COL_SAT_IN_CHAMBERS: {
			row.setCourtChambersInd((Boolean) aValue == true ? SAT_IN_CHAMBERS : SAT_IN_COURT);
			row.setDirty(true);
			this.fireTableDataChanged();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * Add the case to the model.
	 * 
	 * @param caseModel
	 */
	public void addCase(JudgeUsageRow row) {
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
	public void addRows(Collection<JudgeUsageRow> rows) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data));
		}

		for (final JudgeUsageRow row : rows) {
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
	 * @param JudgeUsageRow
	 */
	public void removeCase(JudgeUsageRow row) {
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

