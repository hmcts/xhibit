package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCCaseNumReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCCaseNumValue;

public class INFTRPCCaseRowProcessor extends AbstractRowProcessor {

private final INFTRPCCaseNumReport inftrpcCaseNum = new	INFTRPCCaseNumReport ();

public INFTRPCCaseNumReport getINFTRPCCaseNumValues(){
	return inftrpcCaseNum;
}

@Override
	public void processRow(Row row) {
	INFTRPCCaseNumValue ov = new INFTRPCCaseNumValue();	
	
	ov.setCaseNumber(row.getString("case_Number"));
	ov.setCode(row.getString("code"));	
	ov.setDescription(row.getString("description"));
	 
	inftrpcCaseNum.getInftrpcCaseNumValues().add(ov);
	
	}

}
