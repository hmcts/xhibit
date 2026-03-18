package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARDefendantValue;

public class DOCARRowProcessor extends AbstractRowProcessor{

	private final DOCARPrintValue docarList = new DOCARPrintValue();
    
    public DOCARPrintValue getDOCARValues(){
        return docarList;
    }
    
	
	@Override
	public void processRow(Row row) {
        DOCARDefendantValue dv = new DOCARDefendantValue();
        
        //populate the object
        dv.setCaseType(row.getString("case_type"));
        dv.setCaseNumber(row.getInteger("case_number"));
        dv.setDefendantNumber(row.getInteger("defendant_number"));
        dv.setFirstName(row.getString("first_name"));
        dv.setMiddleName(row.getString("middle_name"));
        dv.setSurname(row.getString("surname"));
        dv.setFormNGDate(XDateFormat.format(row.getDate("form_ng_sent_date"), XDateFormat.DATEFORMAT));
        
        //add dv to list
        docarList.getDOCARDefendantValues().add(dv);
		
	}

}
