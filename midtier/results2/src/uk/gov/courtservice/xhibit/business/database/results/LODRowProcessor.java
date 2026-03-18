package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.LODReport;
import uk.gov.courtservice.xhibit.common.results.vos.LODReportValue;

public class LODRowProcessor extends AbstractRowProcessor {

	private LODReport lodReport;
	
	public LODRowProcessor()
	{
		lodReport = new LODReport();
	}
	
	@Override
	public void processRow(Row row) {
		LODReportValue lodrv = new LODReportValue();
	    
	    //populate the object2
		lodrv.setCaseNumber(row.getString("CASE_NUMBER"));
		lodrv.setCaseTitle(row.getString("CASE_TITLE"));
		lodrv.setDiaryNoteText(row.getString("DIARY_NOTE_TEXT"));
		lodrv.setCreationDate(XDateFormat.format(row.getDate("CREATION_DATE"),XDateFormat.DATEFORMAT));
		
	    //add lodrv to list
	    lodReport.getLodReportValues().add(lodrv);
	}

	/**
	 * @return the lodReport
	 */
	public LODReport getLodReport() {
		return lodReport;
	}
}
