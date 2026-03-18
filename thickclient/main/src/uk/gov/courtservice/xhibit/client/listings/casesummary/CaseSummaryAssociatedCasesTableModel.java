package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.sql.Timestamp;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseSummaryLinkingValue;

public class CaseSummaryAssociatedCasesTableModel extends CaseSummaryTableModel<CaseSummaryLinkingValue> {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CaseSummaryLinkingValue dataValue = getRow(rowIndex);	
		Object value;
		switch (columnIndex) {
			case 0:
				value = dataValue.getCaseType()+dataValue.getCaseNumber();				
				break;
			case 1:
				value = dataValue.getCaseTitle();
				break;
			case 2:
				value = determineTableDateValue(dataValue);
				break;
			case 3:				
				value = dataValue.getCourtFullName();
				break;				
			case 4:
				 value = dataValue.getLiveStatus();
				 break;
			default:
				value = null; 
		}
		return value;
	}
	
	/**
	 * Determines which date should be displayed in the Committal Date column
	 * based upon Case Type
	 * @param dataValue CaseSummaryLinkingValue object
	 * @return a date string to display in the column
	 */
	private String determineTableDateValue(CaseSummaryLinkingValue dataValue) {
		String returnValue = null;
		if ( dataValue != null ) {
			String caseType = dataValue.getCaseType();
			if ( "S".equals(caseType) ) {
				// Sentance Cases should display the Committal Date 
				returnValue = getFormattedDate(dataValue.getCommittalDate());
			}
			else if ( "A".equals(caseType) ) {
				// Appeal Cases should display the Appeal Lodged Date
				returnValue = getFormattedDate(dataValue.getAppealLodgedDate());
			}
			else if ( "T".equals(caseType) ) {
				String receiptType = dataValue.getReceiptType();
				if ( "EW".equals(receiptType) || "IO".equals(receiptType) || "ST".equals(receiptType) ) {
					// Trial Cases with a receipt type of EW, IO or ST should display the Sent for Trial Date
					returnValue = getFormattedDate(dataValue.getSentForTrialDate());
				}
				else {
					// All other Trial Cases should display the Committal Date
					returnValue = getFormattedDate(dataValue.getCommittalDate());
				}
			}
			else {
				// By default display the Committal Date
				returnValue = getFormattedDate(dataValue.getCommittalDate());
			}
		}
		return returnValue;
	}
	
	private String getFormattedDate(Date date) {
		Timestamp timestamp = date != null ? new Timestamp(date.getTime()) : null;
		return super.getFormattedDate(timestamp);
	}

	@Override
	protected String[] getColumnHeaders(){
		return new String[]{
				getResource( "associatedCasesCaseNumberColumnHeader"),
				getResource( "associatedCasesCaseTitleColumnHeader"),
				getResource( "associatedCasesCommittalDateColumnHeader"),
				getResource( "associatedCasesMagsCourtColumnHeader"),				
				getResource( "associatedCasesBCStatusColumnHeader")
		};
	}

	@Override
	protected Integer[] getColumnWidths() {
		Integer[] columnWidths = super.getColumnWidths();
		columnWidths[3] = Integer.valueOf(150);
		return columnWidths;
	}
}
