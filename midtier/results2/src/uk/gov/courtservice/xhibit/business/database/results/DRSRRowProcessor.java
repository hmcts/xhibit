package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReport;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReportValue;


public class DRSRRowProcessor extends AbstractRowProcessor {
	
private final DRSRReport drsrList = new DRSRReport();
	
	public DRSRReport getDRSRReportValues(){
		return drsrList;
	}
	
	@Override	
	public void processRow(Row row){
		DRSRReportValue ov = new DRSRReportValue();
		ov.setCaseNumber(row.getString("case_number"));
		ov.setListingDate(row.getString("listing_Date"));
		ov.setHtyp(row.getString("htyp"));
		ov.setSittingSequenceNo(row.getString("sitting_Sequence_No"));
		ov.setCourtRoomName(row.getString("court_room_name"));
		ov.setSite(row.getString("site"));
		drsrList.getDrsrReportValues().add(ov);
	}
}
