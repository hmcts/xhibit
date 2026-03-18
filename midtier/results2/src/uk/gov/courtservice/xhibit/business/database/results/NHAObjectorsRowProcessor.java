package uk.gov.courtservice.xhibit.business.database.results;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.NHAObjectorValue;

public class NHAObjectorsRowProcessor extends AbstractRowProcessor {

	private ArrayList<NHAObjectorValue> objectorList;
	public NHAObjectorsRowProcessor()
	{
		objectorList = new ArrayList<NHAObjectorValue>();
	}
	
	@Override
	public void processRow(Row row) {
	    
	    //populate the object
		NHAObjectorValue nhaObjector = new NHAObjectorValue();
		nhaObjector.setCaseId(row.getInteger("case_id"));
		nhaObjector.setCaseNumber(row.getString("case_number"));
		nhaObjector.setObjectorName(row.getString("objector_name"));
		nhaObjector.setObjectorAddress(row.getString("objector_address"));
		nhaObjector.setSolicitorName(row.getString("solicitor_name"));
		nhaObjector.setSolicitorAddress(row.getString("solicitor_address"));
		
		// Add to the list
		objectorList.add(nhaObjector);
	}

	/**
	 * @return the nhaReport
	 */
	public ArrayList<NHAObjectorValue> getNHAObjectorList() {
		return objectorList;
	}

}
