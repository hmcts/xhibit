package uk.gov.courtservice.xhibit.business.services.defendant;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;

public class RedelFixtureDeftAttendingRowProcessor extends AbstractRowProcessor {

	private ArrayList<FixtureDeftAttendingBasicValue> fixtureDeftAttendingColl = new ArrayList<FixtureDeftAttendingBasicValue>();
	
	private ArrayList<DefOnCaseOnListBasicValue> defOnCaseOnListColl = new ArrayList<DefOnCaseOnListBasicValue>();
	
	private boolean defOnCaseOnListResults = false;
	
	@Override
	public void processRow(Row row) {
		if (defOnCaseOnListResults) { 
			DefOnCaseOnListBasicValue docolBV = new DefOnCaseOnListBasicValue();
			
			docolBV.setDefendantOnCaseId(row.getInteger("DEFENDANT_ON_CASE_ID"));
			docolBV.setCaseOnListId(row.getInteger("CASE_ON_LIST_ID"));
			docolBV.setDefOnCaseOnListId(row.getInteger("DEF_ON_CASE_ON_LIST_ID"));
			docolBV.setCaseId(row.getInteger("CASE_ID"));
			
			defOnCaseOnListColl.add(docolBV);
		} else {
			FixtureDeftAttendingBasicValue fdaBV = new FixtureDeftAttendingBasicValue();
			
			fdaBV.setDefendantOnCaseId(row.getInteger("DEFENDANT_ON_CASE_ID"));
			fdaBV.setCaseDiaryFixtureId(row.getInteger("CASE_DIARY_FIXTURE_ID"));
			fdaBV.setAttending(row.getString("ATTENDING"));
			fdaBV.setObsInd(row.getString("OBS_IND"));
			fdaBV.setFixtureDeftAttendingId(row.getInteger("FIXTURE_DEFT_ATTENDING_ID"));
			
			fixtureDeftAttendingColl.add(fdaBV);
		}
	}
	
	public Collection getResults() {
		if (defOnCaseOnListResults) {
			return defOnCaseOnListColl;
		} else {
			return fixtureDeftAttendingColl;
		}
	}
	
	public void setDefOnCaseOnListResults(boolean value) {
		defOnCaseOnListResults = value;
	}
}
