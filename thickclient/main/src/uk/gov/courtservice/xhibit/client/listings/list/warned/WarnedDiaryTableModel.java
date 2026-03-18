package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.client.listings.list.common.DiaryTableRowModel;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

public class WarnedDiaryTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COL_DATE = 0;
	public static final int COL_NOTE = 1;
    public static final int COL_USER = 2;
		
	/**
	 * Setup empty table
	 */
	public WarnedDiaryTableModel() {
		super();
		setColumnNames(new String[] {"Date", "Note", "User" });
	}
	
	/**
	 * Setup table from supplied model.
	 * @param data
	 */
	public WarnedDiaryTableModel(DiaryTableRowModel[] data) {
		this();
		super.setData(data);
	}
	
	/**
	 * Get the class of the data in the column.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
	    	case COL_DATE:
	    		return Date.class;
	    	case COL_NOTE:
	    		return String.class;
	    	case COL_USER:
	    		return String.class;
	        default:
	        	throw new UnsupportedOperationException("No data type for column " + columnIndex);
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

        DiaryTableRowModel note = (DiaryTableRowModel)_data[rowIndex];
        switch (columnIndex) {
        	
        	case COL_DATE:
        		cellValue = note.getDiaryDate();
        		break;
	        case COL_NOTE:
	            cellValue = note.getCaseNote();
	            break;
	        case COL_USER:
	            cellValue = note.getUserId();
	            break;
	        default:
	            cellValue = null;
	            break;
        }

        return cellValue;
	}
	
	/**
	 * Add the note to the model.
	 * 
	 * @param note
	 */
	public void addDiaryNotes(Collection<DiaryTableRowModel> notes) {
		final List<Object> list = new ArrayList<Object>(getDataAsCollection());

		for (final DiaryTableRowModel note : notes) {
			// Add note if it is not already in the list
			if (!list.contains(note)) {
				list.add(note);
			}
		}
		setData(list);
	}
}
