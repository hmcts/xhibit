package uk.gov.courtservice.xhibit.business.services.defendant;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseOnListValue;

public class RedelListValueRowProcessor extends AbstractRowProcessor {

	private ArrayList<CaseOnListValue> caseOnListValue = new ArrayList<CaseOnListValue>();
	
	@Override
	public void processRow(Row row) {

		CaseOnListValue colV = new CaseOnListValue();
		colV.setCaseOnListId(row.getInteger("CASE_ON_LIST_ID"));
		colV.setCaseId(row.getInteger("CASE_ID"));
		colV.setListId(row.getInteger("LIST_ID"));
		colV.setObsInd(row.getString("OBS_IND"));
		caseOnListValue.add(colV);
	}
	
	public ArrayList<CaseOnListValue> getResults() {
		return caseOnListValue;
	}
}
