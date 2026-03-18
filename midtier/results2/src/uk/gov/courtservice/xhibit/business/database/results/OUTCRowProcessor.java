package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.OUTCReport;
import uk.gov.courtservice.xhibit.common.results.vos.OUTCReportValue;

public class OUTCRowProcessor extends AbstractRowProcessor {

	private final OUTCReport outcList = new OUTCReport();
	
	public OUTCReport getOUTCReportValues(){
		return outcList;
	}
	
	@Override	
	public void processRow(Row row){
		OUTCReportValue ov = new OUTCReportValue();
		
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
		
		outcList.getOutcReportValues().add(ov);
		
	}
	
}
