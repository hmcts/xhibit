package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.common.results.vos.NHACaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.NHAReport;

public class NHARowProcessor extends AbstractRowProcessor {

	private NHAReport nhaReport;
	public NHARowProcessor()
	{
		nhaReport = new NHAReport();
	}
	
	@Override
	public void processRow(Row row) {
		NHACaseValue cv = new NHACaseValue();
	    
	    //populate the object
		cv.setCaseId(row.getInteger("case_id"));
		cv.setCaseNumber(row.getString("case_number"));
		cv.setCaseSubType(row.getString("case_sub_type"));
		cv.setDefendantNumber(row.getInteger("defendant_number"));
		cv.setCourtCode(row.getString("crest_court_id"));
		cv.setCourtName(row.getString("court_name"));
		cv.setAppellantName(row.getString("appellant_name"));
		cv.setRespondentName(row.getString("respondent_name"));
		cv.setRespondentSurname(row.getString("respondent_surname"));
		cv.setRespondentAddress(row.getString("respondent_address"));
		cv.setClerkToJusticeName(row.getString("clerk_to_justice"));
		cv.setMagistrateAddress(row.getString("magistrate_address"));
		cv.setCourtAddress(row.getString("court_address").replace("\n", ""));
		cv.setAppealType(row.getString("appeal_type"));
		cv.setMagCourtConvictionDate(XDateFormat.format(row.getDate("mag_conviction_date"), XDateFormat.DATEFORMAT));
		cv.setOrigBodyDecisionDate(XDateFormat.format(row.getDate("orig_body_decision_date"), XDateFormat.DATEFORMAT));
		cv.setDateOfFixture(XDateFormat.format(row.getDate("listing_date"), XDateFormat.DATEFORMAT));
		cv.setTimeOfListing(row.getString("time_listed"));
		cv.setTodaysDate(XDateFormat.format(row.getDate("rpt_run_date"), XDateFormat.DATEFORMAT));
		cv.setCourtPhoneNumber(row.getString("ct_phone_no"));
		cv.setSolicitorFirmName(row.getString("solicitor_firm_name"));
		cv.setDefendantAddress(row.getString("defendant_address"));
		cv.setInCustody(row.getString("in_custody"));
		cv.setMagistrateName(row.getString("mags_court_name"));
		cv.setSolicitorAddress(row.getString("solicitor_address"));
		cv.setRespSolName(row.getString("resp_sol_name"));
		cv.setRespSolAddress(row.getString("resp_sol_address"));
		cv.setListNoteText(row.getString("list_note_text"));
		cv.setPreDefinedListNote(row.getString("predefined_list_note"));
		cv.setHearingVenue(row.getString("hearing_venue"));
		cv.setHearingAddress(row.getString("hearing_address").replace("\n", ""));
		
	    //add cv to list
	    nhaReport.getNHACaseValues().add(cv);
	}

	/**
	 * @return the nhaReport
	 */
	public NHAReport getNHAReport() {
		return nhaReport;
	}

}
