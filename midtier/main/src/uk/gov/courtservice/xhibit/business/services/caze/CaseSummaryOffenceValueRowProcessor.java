package uk.gov.courtservice.xhibit.business.services.caze;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseSummaryOffenceValue;

public class CaseSummaryOffenceValueRowProcessor extends AbstractRowProcessor {

	private ArrayList<CaseSummaryOffenceValue> offences = new ArrayList<CaseSummaryOffenceValue>(); 
	
	@Override
	public void processRow(Row row) {
		CaseSummaryOffenceValue offence = new CaseSummaryOffenceValue();
		offence.setOffenceId(row.getInteger("offence_id"));
		offence.setChargeId(row.getInteger("charge_id"));
		offence.setCrestOffenceSeqNo(row.getInteger("crest_offence_seq_no"));
		offence.setOffenceDesc(row.getString("offence_desc"));
		offences.add(offence);
	}
	
	public ArrayList<CaseSummaryOffenceValue> getResults(){
		return offences;
	}
}
