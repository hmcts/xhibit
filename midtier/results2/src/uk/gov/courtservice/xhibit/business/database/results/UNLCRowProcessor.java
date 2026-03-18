package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.UNLCReport;
import uk.gov.courtservice.xhibit.common.results.vos.UNLCReportValue;

public class UNLCRowProcessor extends AbstractRowProcessor {

	private final UNLCReport unlcList = new UNLCReport();
	
	public UNLCReport getUNLCReportValues(){
		return unlcList;
	}
	
	@Override	
	public void processRow(Row row){
		UNLCReportValue ov = new UNLCReportValue();
		
		ov.setCaseNumber(row.getString("case_number"));
		ov.setCaseTitle(row.getString("case_title"));
		ov.setJuvenile(row.getString("juvenile"));
		ov.setCommitedSent(row.getString("commited_sent"));
		ov.setClassCode(row.getString("class_code"));
		ov.setHearingType(row.getString("hearing_type"));
		ov.setMonitoringCategoryCode(row.getString("monitoring_category_code"));
		ov.setLoEst(row.getString("loest"));
		ov.setCaseGroupeNumber(row.getString("case_group_number"));
		ov.setFirstNad(row.getString("first_nad"));
		ov.setListed(row.getString("listed"));
		
		unlcList.getUnlcReportValues().add(ov);
		
	}
	
}