package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSReportList;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSValue;

public class ADJSSRowProcessor extends AbstractRowProcessor {
    
	private final ADJSSReportList adjssReportList = new ADJSSReportList();
	
	public ADJSSReportList getADJSSReportList(){
	    return adjssReportList;
	}
	
	
	@Override
	public void processRow(Row row) {
	    ADJSSValue dv = new ADJSSValue();
	    String formattedDate;
	    
	    //populate the object
	    dv.setCaseNumber(row.getString("case_number"));
	    dv.setDefendantName(row.getString("defendant_name"));
	    dv.setJudgeName(row.getString("judge_name"));
	    
	    formattedDate = XDateFormat.format(row.getDate("hearing_date"), XDateFormat.DATEFORMAT);
	    dv.setHearingDate(formattedDate.replace("-", " ").toUpperCase());
	    
	    formattedDate = XDateFormat.format(row.getDate("adjourned_date"), XDateFormat.DATEFORMAT);
	    dv.setHearingAdjournedDate(formattedDate.replace("-", " ").toUpperCase());
	    
	    dv.setPtiUrn(row.getString("ptiurn"));
	    dv.setReason(row.getString("reason"));
	    dv.setListDate(XDateFormat.format(row.getDate("listing_date"), XDateFormat.DATEFORMAT));
	    //add dv to list
	    adjssReportList.getADJSSDefendantValues().add(dv);
	}
}
