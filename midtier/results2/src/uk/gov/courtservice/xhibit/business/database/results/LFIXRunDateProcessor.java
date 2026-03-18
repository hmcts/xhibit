package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDate;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDateValue;

public class LFIXRunDateProcessor extends AbstractRowProcessor {

	private LFIXRunDate lfixRunDate;
	
	public LFIXRunDateProcessor() {
		lfixRunDate = new LFIXRunDate();
	}
	
	@Override
	public void processRow(Row row) {
		
		LFIXRunDateValue lfixrv = new LFIXRunDateValue();
		
	    //populate the object
		
		lfixrv.setRundate(XDateFormat.format(row.getDate("RUNDATE"),XDateFormat.DATEFORMAT));
		
	    //add to list
				
		lfixRunDate.getLfixRunDateValues().add(lfixrv);
	}

	/**
	 * @return the lfixRunDate
	 */
	public LFIXRunDate getLfixRunDate() {
		return lfixRunDate;
	}
}

