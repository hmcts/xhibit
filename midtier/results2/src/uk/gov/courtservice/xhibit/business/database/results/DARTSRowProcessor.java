package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSRetentionPolicyValue;

public class DARTSRowProcessor extends AbstractRowProcessor{

	private final static String YES = "Y";
	private final DARTSPrintValue dartsList = new DARTSPrintValue();
    
    public DARTSPrintValue getDARTSValues(){
        return dartsList;
    }
    
	
	@Override
	public void processRow(Row row) {
		DARTSRetentionPolicyValue drpv = new DARTSRetentionPolicyValue();
        
        //populate the object
		drpv.setLastUpdateDate(XDateFormat.format(row.getDate("crp_last_update_date"), XDateFormat.DATEFORMAT));
        drpv.setCaseType(row.getString("case_type"));
        drpv.setCaseNumber(row.getInteger("case_number"));
        drpv.setPolicyNumber(row.getInteger("policy_no"));
        drpv.setPolicyDescription(row.getString("policy_description"));
        drpv.setHasLife(row.getString("has_life"));
        boolean hasLife = YES.equals(drpv.getHasLife());
        drpv.setDurationDays(hasLife ? 0 : row.getInteger("duration_days"));
        drpv.setDurationMonths(hasLife ? 0 : row.getInteger("duration_months"));
        drpv.setDurationYears(hasLife ? 0 : row.getInteger("duration_years"));
        
        //add drpv to list
        dartsList.getDARTSRetentionPolicyValues().add(drpv);
		
	}

}
