package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

public class ListCaseTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

	public static final int COL_GROUP_NO = 0;
    public static final int COL_CASE_NUMBER = 1;
	public static final int COL_CASE_TITLE = 2;
	public static final int COL_HEARING_TYPE = 3;
	public static final int COL_EST = 4;

	/**
	 * Setup empty table
	 */
	public ListCaseTableModel() {
		super();

		List<String> columnNames = new ArrayList<String>();
		columnNames.add(COL_GROUP_NO, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroup"));
		columnNames.add(COL_CASE_NUMBER, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseNumber"));
		columnNames.add(COL_CASE_TITLE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseTitle"));
		columnNames.add(COL_HEARING_TYPE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingType"));
		columnNames.add(COL_EST, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimate"));
		setColumnNames(columnNames.toArray(new String[0]));
	}
	
	public List<String> getColumnHeaderToolTips() {
		List<String> columnHeaderToolTips = new ArrayList<String>();
		columnHeaderToolTips.add(COL_GROUP_NO, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroupToolTip"));
		columnHeaderToolTips.add(COL_CASE_NUMBER, null);
		columnHeaderToolTips.add(COL_CASE_TITLE, null);
		columnHeaderToolTips.add(COL_HEARING_TYPE, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingTypeToolTip"));
		columnHeaderToolTips.add(COL_EST, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimateToolTip"));
		return columnHeaderToolTips;
	}
	
	public void setColumnWidths(JTable table) {
		table.getColumnModel().getColumn(COL_GROUP_NO).setMinWidth(50);
		table.getColumnModel().getColumn(COL_GROUP_NO).setMaxWidth(50);
		table.getColumnModel().getColumn(COL_CASE_NUMBER).setMinWidth(90);
		table.getColumnModel().getColumn(COL_CASE_NUMBER).setMaxWidth(90);
		table.getColumnModel().getColumn(COL_EST).setMinWidth(35);
		table.getColumnModel().getColumn(COL_EST).setMaxWidth(35);
		table.getColumnModel().getColumn(COL_HEARING_TYPE).setMinWidth(40);
		table.getColumnModel().getColumn(COL_HEARING_TYPE).setMaxWidth(40);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		   
	        switch (columnIndex) {
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
	 * Setup table from supplied model.
	 * @param data
	 */
	public ListCaseTableModel(ListCaseTableRow[] data) {
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

        ListCaseTableRow caze = (ListCaseTableRow)_data[rowIndex];
        switch (columnIndex) {
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
	 * Add the case to the model.
	 * 
	 * @param caseModel
	 */
	public void addCase(ListCaseTableRow listCaseTableRow) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data)); 
		}
		
		// Add case if it is not already in the list
		if (!list.contains(listCaseTableRow)) {
			list.add(listCaseTableRow);
			setData(list);
		}
	}
	

	/**
	 * Add a collection of cases to the model.
	 * @param listCaseTableRows The collection of rows to add.
	 */
	public void addCases(Collection<ListCaseTableRow> listCaseTableRows) {
		// Convert model to list to be able to add
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data));
		}

		for (final ListCaseTableRow row : listCaseTableRows) {
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
	 * @param listCaseTableRow
	 */
	public void removeCase(ListCaseTableRow listCaseTableRow) {
		// Convert model to list to be able to remove
		final List<Object> list = new ArrayList<Object>();
		if (_data != null) {
			list.addAll(Arrays.asList(_data)); 
		}
		
		// Remove case if it is in the list
		if (list.contains(listCaseTableRow)) {
			list.remove(listCaseTableRow);
			setData(list);
		}
	}
	
	/**
	 * Remove all the case from the model.
	 */
	public void removeAllCases() {
		if (_data != null) {
			List<Object> list = Arrays.asList(_data);
			for (Object tableRow : list) {
				removeCase((ListCaseTableRow) tableRow);
			}
		}
	}


}