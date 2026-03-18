package uk.gov.courtservice.xhibit.business.services.defendant;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;

public class RedelValueRowProcessor extends AbstractRowProcessor {

	private ArrayList<CaseDiaryFixtureValue> caseDiaryFixture = new ArrayList<CaseDiaryFixtureValue>();
	
	@Override
	public void processRow(Row row) {

		CaseDiaryFixtureValue cDF = new CaseDiaryFixtureValue(null, null, null);
		cDF.setCaseDiaryFixtureId(row.getInteger("CASE_DIARY_FIXTURE_ID"));
		cDF.setCaseListingEntryId(row.getInteger("CASE_LISTING_ENTRY_ID"));
		cDF.setHearingTypeId(row.getInteger("HEARING_TYPE_ID"));
		cDF.setObsInd(row.getString("OBS_IND"));
		caseDiaryFixture.add(cDF);
	}
	
	public ArrayList<CaseDiaryFixtureValue> getResults() {
		return caseDiaryFixture;
	}
}
