package uk.gov.courtservice.xhibit.business.services.defendant;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import oracle.jdbc.internal.OracleTypes;
import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseSummaryLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseOnListValue;

public class DefendantOnCaseDatabaseManager extends AbstractXhibitDatabase {

	@SuppressWarnings("unchecked")
	public Collection<DefendantOnCaseBasicValue> findDefendantsOnActiveCases(Integer defendantId, Integer courtId) {
		if (log.isDebugEnabled()) {
			log.debug("findDefendantsOnActiveCases(defendantId="+defendantId+", courtId="+courtId+")");
		}
		final ReflectionRowProcessor docRowProcessor = new ReflectionRowProcessor(DefendantOnCaseBasicValue.class);
		docRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_linking_pkg.get_defendants_on_active_cases(?, ?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(docRowProcessor);
		sp.execute(new Object[] {courtId, defendantId });
		return docRowProcessor.getResults();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CaseSummaryLinkingValue> findCasesByGroupNumber(Integer courtId, Integer caseGroupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("findCasesByGroupNumber(courtId="+courtId+", caseGroupNumber="+caseGroupNumber+")");
		}
		final ReflectionRowProcessor cbvRowProcessor = new ReflectionRowProcessor(CaseSummaryLinkingValue.class);
		cbvRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_linking_pkg.get_cases_by_group_number(?, ?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(cbvRowProcessor);
		sp.execute(new Object[] {courtId, caseGroupNumber });
		return cbvRowProcessor.getResults();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CaseLinkingValue> findLinkedCases(Integer courtId, Integer defendantId, Integer caseGroupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("findLinkedCases(courtId="+courtId+", defendantId="+defendantId+", caseGroupNumber="+caseGroupNumber+")");
		}
		final ReflectionRowProcessor cbvRowProcessor = new ReflectionRowProcessor(CaseLinkingValue.class);
		cbvRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_linking_pkg.get_linked_cases(?, ?, ?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER});
		sp.setRowProcessor(cbvRowProcessor);
		sp.execute(new Object[] {courtId, defendantId, caseGroupNumber });
		return cbvRowProcessor.getResults();
	}
	
	@SuppressWarnings("unchecked")
	public Integer returnCountActiveCases(Integer caseId) {	
		if (log.isDebugEnabled()) {
			log.debug("returnCountActiveCases(caseId="+caseId+")");
		}
		Connection conn = null;
		CallableStatement statement = null;
		
		try {
			conn = CSServices.getServiceLocator().getDataSource().getConnection();
			statement = conn.prepareCall("{ call xhb_case_linking_pkg.return_count_active_cases(?, ?) }");
			statement.registerOutParameter(1, OracleTypes.NUMBER);
			statement.setObject(2, caseId);
			statement.execute();	
			return statement.getInt(1);	
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Unable to return count of num defendants active on given case: " + caseId);
			return -1;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CaseLinkingValue> findActiveCasesWithGroupNumber(Integer courtId, Integer groupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("findActiveCasesWithGroupNumber(courtId="+courtId+", groupNumber="+groupNumber+")");
		}
		final ReflectionRowProcessor clvRowProcessor = new ReflectionRowProcessor(CaseLinkingValue.class);
		clvRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_linking_pkg.find_active_cases_with_group(?, ?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER});
		sp.setRowProcessor(clvRowProcessor);
		sp.execute(new Object[] { courtId, groupNumber });
		return clvRowProcessor.getResults();
	}

	// will return a single case entry (the case id of supplied case) if common defendants are present
	// else will return nothing
	@SuppressWarnings("unchecked")
	public Collection<CaseLinkingValue> findCommonDefendantsWithGroupNumber(Integer caseId, Integer courtId, Integer groupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("findCommonDefendantsWithGroupNumber(caseId="+caseId+", courtId="+courtId+", groupNumber="+groupNumber+")");
		}
		final ReflectionRowProcessor clvRowProcessor = new ReflectionRowProcessor(CaseLinkingValue.class);
		clvRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_case_linking_pkg.find_common_defendants_grouped(?, ?, ?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER });
		sp.setRowProcessor(clvRowProcessor);
		sp.execute(new Object[] { caseId, courtId, groupNumber });
		return clvRowProcessor.getResults();
	}

	public void REDELSentenceTrialDelete(Integer defendantOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("REDELSentenceTrialDelete(defendantOnCaseId="+defendantOnCaseId+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.sentence_trial_delete(?) }");
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.executeUpdate(new Object[] { defendantOnCaseId });
	}

	public void REDELAppealDelete(Integer caseId, Integer defendantOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("REDELAppealDelete(caseId="+caseId+", defendantOnCaseId="+defendantOnCaseId+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.appeal_delete(?, ?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
		sp.executeUpdate(new Object[] { caseId, defendantOnCaseId });
	}

	public Collection<CaseDiaryFixtureBasicValue> findREDELFutureFixtures(Integer caseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("findREDELFutureFixtures(caseId="+caseId+", currentDate="+currentDate+")");
		}
		final RedelValueRowProcessor redelRowProcessor = new RedelValueRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.find_future_fixtures(?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(redelRowProcessor);
		sp.execute(new Object[] { caseId, new java.sql.Date(currentDate.getTime()) });
		Collection<CaseDiaryFixtureValue> caseDiaryFixtureValue = redelRowProcessor.getResults();
		Collection<CaseDiaryFixtureBasicValue> caseDiaryFixtureBasicValue = new ArrayList<CaseDiaryFixtureBasicValue>();
		Iterator iter = caseDiaryFixtureValue.iterator();
		while (iter.hasNext()) {
			CaseDiaryFixtureValue cdfv = (CaseDiaryFixtureValue) iter.next();
			CaseDiaryFixtureBasicValue val = createCaseDiaryBasicValue(cdfv);
			caseDiaryFixtureBasicValue.add(val);
		}
		return caseDiaryFixtureBasicValue;
	}

	public Collection<CaseOnListBasicValue> findREDELFutureListings(Integer caseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("findREDELFutureListings(caseId="+caseId+", currentDate="+currentDate+")");
		}
		final RedelListValueRowProcessor redelRowProcessor = new RedelListValueRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.find_future_listings(?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		sp.setRowProcessor(redelRowProcessor);
		sp.execute(new Object[] { caseId, new java.sql.Date(currentDate.getTime()) });
		Collection<CaseOnListValue> caseOnListValue = redelRowProcessor.getResults();
		Collection<CaseOnListBasicValue> caseOnListBasicValue = new ArrayList<CaseOnListBasicValue>();
		Iterator iter = caseOnListValue.iterator();
		while (iter.hasNext()) {
			CaseOnListValue colV = (CaseOnListValue) iter.next();
			CaseOnListBasicValue val = createCaseOnListBasicValue(colV);
			caseOnListBasicValue.add(val);
		}
		return caseOnListBasicValue;
	}

	private CaseDiaryFixtureBasicValue createCaseDiaryBasicValue(CaseDiaryFixtureValue local) {
		if (log.isDebugEnabled()) {
			log.debug("createCaseDiaryBasicValue(local="+local+")");
		}
		CaseDiaryFixtureBasicValue cdfBV = new CaseDiaryFixtureBasicValue();
		cdfBV.setCaseDiaryFixtureId(local.getCaseDiaryFixtureId());
		cdfBV.setCaseListingEntryId(local.getCaseListingEntryId());
		cdfBV.setHearingTypeId(local.getHearingTypeId());
		cdfBV.setObsInd(local.getObsInd());
		return cdfBV;
	}

	private CaseOnListBasicValue createCaseOnListBasicValue(CaseOnListValue local) {
		if (log.isDebugEnabled()) {
			log.debug("createCaseOnListBasicValue(local="+local+")");
		}
		CaseOnListBasicValue colBV = new CaseOnListBasicValue();
		colBV.setCaseOnListId(local.getCaseOnListId());
		colBV.setCaseId(local.getCaseId());
		colBV.setListId(local.getListId());
		colBV.setObsInd(local.getObsInd());
		return colBV;
	}
	
	public Collection<FixtureDeftAttendingBasicValue> findREDELFutureFixturesDef(Integer caseId, Integer defOnCaseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("findREDELFutureFixturesDef(caseId="+caseId+", defOnCaseId="+defOnCaseId+", currentDate="+currentDate+")");
		}
		final RedelFixtureDeftAttendingRowProcessor redelRowProcessor = new RedelFixtureDeftAttendingRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.find_future_fixtures_def(?,?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.DATE });
		sp.setRowProcessor(redelRowProcessor);
		sp.execute(new Object[] { caseId, defOnCaseId, new java.sql.Date(currentDate.getTime()) });
		Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttendingValues = redelRowProcessor.getResults();

		return fixtureDeftAttendingValues;
	}
	
	public Collection<DefOnCaseOnListBasicValue> findREDELFutureListingsDef(Integer caseId, Integer defOnCaseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("findREDELFutureListingsDef(caseId="+caseId+", defOnCaseId="+defOnCaseId+", currentDate="+currentDate+")");
		}
		final RedelFixtureDeftAttendingRowProcessor redelRowProcessor = new RedelFixtureDeftAttendingRowProcessor();
		redelRowProcessor.setDefOnCaseOnListResults(true);	
		final StoredProcedure sp = createStoredProcedure("{ call xhb_redel_pkg.find_future_hearings_def(?,?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.DATE });
		sp.setRowProcessor(redelRowProcessor);
		sp.execute(new Object[] { caseId, defOnCaseId, new java.sql.Date(currentDate.getTime()) });
		Collection<DefOnCaseOnListBasicValue> fixtureDeftAttendingValues = redelRowProcessor.getResults();

		return fixtureDeftAttendingValues;
	}
}