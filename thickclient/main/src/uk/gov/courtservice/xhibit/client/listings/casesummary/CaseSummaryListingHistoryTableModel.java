package uk.gov.courtservice.xhibit.client.listings.casesummary;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingHistoryInformation;

public class CaseSummaryListingHistoryTableModel extends CaseSummaryTableModel<CaseListingHistoryInformation> {

	private static final long serialVersionUID = 1L;
	private static final String EMPTY_STRING = "";
	private static final String YES = "Y";
	private static final String TOP_PRIORITY = "TOP";
	private static final String RESERVED = "RESD";
	private static final String FLOATER = "FLTR";

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CaseListingHistoryInformation caseOnList = getRow(rowIndex);
		Object value;
		switch (columnIndex) {
			case 0:
				value = getFormattedDate(caseOnList.getTimeListed());
				break;
			case 1:
				value = caseOnList.getListType().toUpperCase();
				break;			
			case 2:
				value = getPriority(caseOnList);
				break;
			case 3:
				value = getFormattedCourtRoom(caseOnList);
				break;
			case 4:
				value = caseOnList.getHearingTypeCode();
				break;
			case 5:
				value = getFormattedDate(caseOnList.getDateOfRemoval());
				break;				
			case 6:	
				value =  caseOnList.getReasonForRemoval();
				break;
			default:
				value = null; 
		}
		return value;
	}	
	
	private String getPriority(CaseListingHistoryInformation listHistoryInformation) {
		if (listHistoryInformation.isWarned() ||
				listHistoryInformation.isFixture()) {
			return EMPTY_STRING;				
		} else if (YES.equals(listHistoryInformation.getFloaterCase())) {
			return FLOATER;		 
		} else if (YES.equals(listHistoryInformation.getReserved())) {
			return RESERVED;
		}
		return TOP_PRIORITY;
	}
	
	private String getFormattedCourtRoom(CaseListingHistoryInformation listHistoryInformation) {
		String formattedCourtRoom = listHistoryInformation.getCourtSiteCode();
		if (!listHistoryInformation.isWarned() &&
				!listHistoryInformation.isFixture()) {
			if (listHistoryInformation.getCourtRoomNo() != null && !EMPTY_STRING.equals(listHistoryInformation.getCourtRoomNo())) {
				formattedCourtRoom += " / ";
				formattedCourtRoom += listHistoryInformation.getCourtRoomNo();
			}
			if (listHistoryInformation.getSittingNumber() != null) {
				formattedCourtRoom += "-";
				formattedCourtRoom += listHistoryInformation.getSittingNumber();
			}
		}
		return formattedCourtRoom;
	}		
		
	public CaseListingHistoryInformation getListHistory(int rowIndex)
	{
		return getRow(rowIndex);
	}
	
	@Override	
	protected String[] getColumnHeaders() {
		return new String[]{ 
				getResource( "listHistoryListDateColumnHeader"),
				getResource( "listHistoryListTypeColumnHeader"),
				getResource( "listHistoryPriorityColumnHeader"),
				getResource( "listHistoryCourtRoomColumnHeader"),
				getResource( "listHistoryHearingTypeColumnHeader"),
				getResource( "listHistoryRemovalDateColumnHeader"),
				getResource( "listHistoryReasonColumnHeader")
		};
	}

	@Override
	protected Integer[] getColumnWidths() {
		Integer[] columnWidths = super.getColumnWidths();
		columnWidths[6] = Integer.valueOf(150);
		return columnWidths;
	}
}
