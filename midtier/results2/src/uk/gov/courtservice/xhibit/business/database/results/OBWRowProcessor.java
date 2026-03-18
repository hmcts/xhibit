package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.OBWDefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.OBWPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARDefendantValue;

public class OBWRowProcessor extends AbstractRowProcessor{

	private final OBWPrintValue obwList = new OBWPrintValue();
    
    public OBWPrintValue getOBWValues(){
        return obwList;
    }
    
	
	@Override
	public void processRow(Row row) {
        OBWDefendantValue dv = new OBWDefendantValue();
        
        //populate the object
        dv.setCaseId(row.getInteger("case_id"));
        dv.setCaseNumber(row.getString("case_number"));
        dv.setDefendantNumber(row.getInteger("defendant_number"));
        dv.setDefendantName(row.getString("defendant_name"));
        dv.setClassCode(row.getInteger("class_code"));
        dv.setBwIssueDate(row.getString("bw_issue_date"));
        dv.setSolicitorFirmName(row.getString("solicitor_firm_name"));
        dv.setPtiurn(row.getString("ptiurn"));
        dv.setTodayDate(row.getString("today_date"));
        
        //add dv to list
        obwList.getOBWDefendantValues().add(dv);
		
	}

}
