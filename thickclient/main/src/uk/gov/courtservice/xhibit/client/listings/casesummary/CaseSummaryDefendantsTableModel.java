package uk.gov.courtservice.xhibit.client.listings.casesummary;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class CaseSummaryDefendantsTableModel extends CaseSummaryTableModel<DefendantValue> {

	private static final long serialVersionUID = 1L;
	public static interface TableId {
		public static int DEFENDANT_PANEL = 0;
		public static int LIST_HISTORY_PANEL = 1;
	}
	private Integer tableId;

	public CaseSummaryDefendantsTableModel(int tableId) {
		super();
		this.tableId = tableId;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DefendantValue defendant = getRow(rowIndex);
		Object value = null;		
		if (tableId.equals(TableId.DEFENDANT_PANEL)) {
			switch (columnIndex) {
			case 0:
				value = Character.toString(defendant.getGenderString().charAt(0));
				break;
			case 1:
				value = defendant.getFirstName();
				break;
			case 2:
				value = defendant.getMiddleName();
				break;
			case 3:
				value = defendant.getSurName();
				break;
			case 4:
				value = DefendantValue.IS_JUVENILE_TRUE.equals(defendant.getIsJuvenile()) ?
						 DefendantValue.IS_JUVENILE_TRUE : DefendantValue.IS_JUVENILE_FALSE;
				break;
			default:
				value = null; 
			}
		} else if (tableId.equals(TableId.LIST_HISTORY_PANEL)) {				
			switch (columnIndex) {
			case 0:
				value = defendant.getSurName();
				break;
			case 1:
				value = defendant.getFirstName();
				break;
			case 2:
				value = defendant.getMiddleName();
				break;
			case 3:				
				value = Character.toString(defendant.getGenderString().charAt(0));
				break;
			case 4:
				 value = DefendantValue.IS_JUVENILE_TRUE.equals(defendant.getIsJuvenile()) ?
						 DefendantValue.IS_JUVENILE_TRUE : DefendantValue.IS_JUVENILE_FALSE;
				 break;
			default:
				value = null; 
			}
		}
		return value;
	}
	
	public DefendantValue getDefendant(int rowIndex)
	{
		return getRow(rowIndex);
	}
	
	@Override
	protected String[] getColumnHeaders(){
		if (tableId.equals(TableId.DEFENDANT_PANEL)) {
			return new String[]{
					getResource( "listHistorySexColumnHeader"),
					getResource( "listHistoryFirstNameColumnHeader"),
					getResource( "listHistoryOtherNamesColumnHeader"),
					getResource( "listHistorySurnameColumnHeader"),
					getResource( "listHistoryIsJuvenileColumnHeader")
			};
		} else if (tableId.equals(TableId.LIST_HISTORY_PANEL)) { 
			return new String[]{					
					getResource( "listHistorySurnameColumnHeader"),
					getResource( "listHistoryFirstNameColumnHeader"),
					getResource( "listHistoryOtherNamesColumnHeader"),
					getResource( "listHistorySexColumnHeader"),
					getResource( "listHistoryJuvenileColumnHeader")
			};
		}
		return null;
	}

	@Override
	protected Integer[] getColumnWidths() {
		Integer[] columnWidths = super.getColumnWidths();
	    if (tableId.equals(TableId.LIST_HISTORY_PANEL)) {
	    	columnWidths[0] = Integer.valueOf(170);
	    	columnWidths[1] = Integer.valueOf(170);
	    	columnWidths[2] = Integer.valueOf(170);
	    }
		return columnWidths;
	}	
}
