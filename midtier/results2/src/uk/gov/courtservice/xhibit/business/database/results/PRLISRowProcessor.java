package uk.gov.courtservice.xhibit.business.database.results;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISCaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISReport;

public class PRLISRowProcessor extends AbstractRowProcessor{

	private final PRLISReport prlisReport = new PRLISReport();
    
    public PRLISReport getPRLISReport(){
        return prlisReport;
    }
    
	
	@Override
	public void processRow(Row row) {
        PRLISCaseValue cv = new PRLISCaseValue();
        
        //populate the object
        cv.setCaseType(row.getString("case_type"));
        cv.setCaseNumber(row.getString("case_number"));
        cv.setDefendantFirstName(row.getString("first_name"));
        cv.setDefendantMiddleName(row.getString("middle_name"));
        cv.setDefandantSurname(row.getString("surname"));
        cv.setMagistratesCourtName(row.getString("magistrates_court"));
        cv.setMagistratesTransferredCourtName(row.getString("magistrates_transferred_court"));
        
        DefendantValue d = new DefendantValue();
        d.setGender(row.getInteger("gender"));
        cv.setDefendantGender(d.getGenderString());
        
        cv.setDefendantDateOfBirth(XDateFormat.format(row.getDate("date_of_birth"), XDateFormat.DATEFORMAT));
        cv.setDateOfHearing(XDateFormat.format(row.getDate("preliminary_date_of_hearing"), XDateFormat.DATEFORMAT));
        cv.setSolicitorName(row.getString("solicitor_firm_name"));
        cv.setSolicitorPhoneNumber(row.getString("solicitor_phone_number"));
        cv.setBcStatus(row.getString("current_bc_status"));
        cv.setProsecutorName(row.getString("prosecutor_name"));
        cv.setPtiurn(row.getString("ptiurn"));
        cv.setClassCode(row.getString("class_code"));
        cv.setSentForTrialDate(XDateFormat.format(row.getDate("sent_for_trial_date"), XDateFormat.DATEFORMAT));
        cv.setTransferredDate(XDateFormat.format(row.getDate("date_trans_from"), XDateFormat.DATEFORMAT));
        cv.setCommittalDate(XDateFormat.format(row.getDate("committal_date"), XDateFormat.DATEFORMAT));
        cv.setAppealLodgedDate(XDateFormat.format(row.getDate("appeal_lodged_date"), XDateFormat.DATEFORMAT));
        cv.setCharges(row.getString("charges"));    
        cv.setCaseId(row.getInteger("case_id"));
        cv.setDefendantNumberText(row.getString("defendant_number"));
        cv.setFirstDefNo(row.getInteger("first_def_no"));
        cv.setCurrentDefNo(row.getInteger("current_def_no"));
        
        //add dv to list
        prlisReport.getPrlisCaseValues().add(cv);
		
	}

}
