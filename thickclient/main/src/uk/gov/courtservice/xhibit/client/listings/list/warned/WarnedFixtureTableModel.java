package uk.gov.courtservice.xhibit.client.listings.list.warned;

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
 * TableModel to support Warned List.
 * Note: Warned List displays extra columns.
 * @author westalll
 *
 */
public class WarnedFixtureTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COL_FIX_DATE = 		0;
    public static final int COL_SITE_CODE = 	1;
	public static final int COL_GROUP_NO = 		2;
    public static final int COL_CASE_NUMBER = 	3;
	public static final int COL_CASE_TITLE = 	4;
	public static final int COL_HEARING_TYPE = 	5;
	public static final int COL_EST = 			6;
	
	
	/**
	 * Setup empty table
	 */
	public WarnedFixtureTableModel() {
		super();
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(COL_FIX_DATE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableFixDate"));
		columnNames.add(COL_SITE_CODE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableSite"));
		columnNames.add(COL_GROUP_NO, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroup"));
		columnNames.add(COL_CASE_NUMBER, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseNumber"));
		columnNames.add(COL_CASE_TITLE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseTitle"));
		columnNames.add(COL_HEARING_TYPE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingType"));
		columnNames.add(COL_EST, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimate"));
		setColumnNames(columnNames.toArray(new String[0]));
	}
	
	public List<String> getColumnHeaderToolTips() {
		List<String> columnHeaderToolTips = new ArrayList<String>();
		columnHeaderToolTips.add(COL_FIX_DATE, null);
		columnHeaderToolTips.add(COL_SITE_CODE, null);
		columnHeaderToolTips.add(COL_GROUP_NO, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroupToolTip"));
		columnHeaderToolTips.add(COL_CASE_NUMBER, null);
		columnHeaderToolTips.add(COL_CASE_TITLE, null);
		columnHeaderToolTips.add(COL_HEARING_TYPE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingTypeToolTip"));
		columnHeaderToolTips.add(COL_EST, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimateToolTip"));
		return columnHeaderToolTips;
	}
	
	public void setColumnWidths(JTable table) {
		table.getColumnModel().getColumn(COL_FIX_DATE).setMinWidth(80);
		table.getColumnModel().getColumn(COL_FIX_DATE).setMaxWidth(80);
		table.getColumnModel().getColumn(COL_SITE_CODE).setMinWidth(35);
		table.getColumnModel().getColumn(COL_SITE_CODE).setMaxWidth(35);
		table.getColumnModel().getColumn(COL_GROUP_NO).setMinWidth(50);
		table.getColumnModel().getColumn(COL_GROUP_NO).setMaxWidth(50);
		table.getColumnModel().getColumn(COL_CASE_NUMBER).setMinWidth(90);
		table.getColumnModel().getColumn(COL_CASE_NUMBER).setMaxWidth(90);
		table.getColumnModel().getColumn(COL_HEARING_TYPE).setMinWidth(40);
		table.getColumnModel().getColumn(COL_HEARING_TYPE).setMaxWidth(40);
		table.getColumnModel().getColumn(COL_EST).setMinWidth(35);
		table.getColumnModel().getColumn(COL_EST).setMaxWidth(35);
	}

	/**
	 * Setup table from supplied model.
	 * @param data
	 */
	public WarnedFixtureTableModel(WarnedFixtureTableRow[] data) {
		this();
		super.setData(data);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		   
	        switch (columnIndex) {
	        	case COL_FIX_DATE:
	        		return Date.class;
	        	case COL_SITE_CODE:
	        		return String.class;
	        	case COL_GROUP_NO:
	        		return Integer.class;
	        	case COL_CASE_NUMBER:
		            return String.class;
		        case COL_CASE_TITLE:
		            return String.class;
		        case COL_HEARING_TYPE:
		            return String.class;
		        case COL_EST:
		            return String.class;
		        default:
		        	throw new UnsupportedOperationException("No data type for column "+columnIndex+ ".");
	        }
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

        WarnedFixtureTableRow caze = (WarnedFixtureTableRow)_data[rowIndex];
        switch (columnIndex) {
        	case COL_FIX_DATE:
        		cellValue = caze.getListingDate() == null ? "" :  caze.getListingDate();
        		break;
        	case COL_SITE_CODE:
        		cellValue = caze.getCourtSiteCode();
        		break;
        	case COL_GROUP_NO:
        		cellValue = caze.getGroupNumber();
        		break;    
        	case COL_CASE_NUMBER:
	            cellValue = caze.getCaseNumber();
	            break;
	        case COL_CASE_TITLE:
	            cellValue = caze.getCaseTitle();
	            break;
	        case COL_HEARING_TYPE:
	            cellValue = caze.getHearingTypeCode();
	            break;
	        case COL_EST:
	            cellValue = caze.getEst();
	            break;
	        default:
	            cellValue = null;
	            break;
        }

        return cellValue;
	}
	
	/**
	 * Add a collection of cases to the model.
	 * @param listCaseTableRows The collection of rows to add.
	 */
	public void addCases(Collection<WarnedFixtureTableRow> warnedCaseTableRows) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data));
		}

		for (final WarnedFixtureTableRow row : warnedCaseTableRows) {
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
	 * @param warnedCaseTableRow
	 */
	public void removeCase(WarnedFixtureTableRow warnedCaseTableRow) {
		// Convert model to list to be able to remove
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data)); 
		}
		
		// Remove case if it is in the list
		if (list.contains(warnedCaseTableRow)) {
			list.remove(warnedCaseTableRow);
			setData(list);
		}
	}
}