package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXValue;

public class CFIXRowProcessor extends AbstractRowProcessor {
	
	private final  CFIXReport cfixList = new CFIXReport ();
	
	public CFIXReport getCFIXValues (){
		return cfixList;
	}
	
	@Override 
	public void processRow(Row row){
		CFIXValue cv = new CFIXValue();
		
		cv.setHearingDate(XDateFormat.format(row.getDate("hearing_date"), XDateFormat.DATEFORMAT));
		cv.setCaseNumber(row.getString("case_number"));
		cv.setCaseTitle(row.getString("case_title"));
		cv.setClassCode(row.getString("class_code"));
		cv.setBcStatus(row.getString("bc_status"));
		cv.setHearingType(row.getString("hearing_type"));
		cv.setEst(row.getString("est"));
		cv.setSite(row.getString("site"));
		cv.setNoteType(row.getString("note_type"));
		cv.setDiaryNoteText(row.getString("diary_note_text"));
		cv.setJudgeRequired(row.getString("required_judge"));
		cv.setVideoLinkRequired(row.getString("video_link_required"));
		cv.setListNote(row.getString("ln"));
		
		cfixList.getCFIXValues().add(cv);
	}
	
}





