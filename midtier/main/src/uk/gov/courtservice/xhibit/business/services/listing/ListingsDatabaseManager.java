package uk.gov.courtservice.xhibit.business.services.listing;

import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseIdValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingHistoryInformation;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListingResultsInformation;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class ListingsDatabaseManager extends AbstractXhibitDatabase {

	public Collection<ListBasicValue> findListsByRowNumber(Integer courtId, String listType, Integer rowNumberLimit) {
		if (log.isDebugEnabled()) {
			log.debug("findListsByRowNumber(courtId="+courtId+", listType="+listType+", rowNumberLimit="+rowNumberLimit+")");
		}
		String commandLine = "{ call xhb_listing_pkg.get_list(?,?,?,?) }";
		int[] parameterTypes = new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER };
		Object[] parameters = new Object[] {courtId, listType, rowNumberLimit};
       return findLists(commandLine, parameterTypes, parameters);
	}

	public Collection<CaseListingHistoryInformation> findCaseListHistory(Integer caseId, Integer rowNumberLimit) {
		if (log.isDebugEnabled()) {
			log.debug("findCaseListHistory(caseId="+caseId+", rowNumberLimit="+rowNumberLimit+")");
		}
		final ReflectionRowProcessor caseListHistoryRowProcessor = new ReflectionRowProcessor(CaseListingHistoryInformation.class);
		caseListHistoryRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.get_case_list_history(?,?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
        sp.setRowProcessor(caseListHistoryRowProcessor);
        sp.execute(new Object[] {caseId, rowNumberLimit});
        @SuppressWarnings("unchecked")
        Collection<CaseListingHistoryInformation> caseListingInformationValues = caseListHistoryRowProcessor.getResults();
       return caseListingInformationValues;
	}

	public Collection<ListBasicValue> findLatestListByDate(Integer courtId, String listType, Date diaryDate) {
		if (log.isDebugEnabled()) {
			log.debug("findLatestListByDate(courtId="+courtId+", listType="+listType+", diaryDate="+diaryDate+")");
		}
		String commandLine = "{ call xhb_listing_pkg.get_latest_list(?,?,?,?) }";
		int[] parameterTypes = new int[] { Types.INTEGER, Types.VARCHAR, Types.DATE };
		Object[] parameters = new Object[] {courtId, listType, new java.sql.Date(diaryDate.getTime()) };
        return findLists(commandLine, parameterTypes, parameters);
	} 	

	public Collection<ListBasicValue> findListsInDateRange(Integer courtId, String listType, Date fromDate, Date toDate) {
		if (log.isDebugEnabled()) {
			log.debug("findListsInDateRange(courtId="+courtId+", listType="+listType+", fromDate="+fromDate+", toDate="+toDate+")");
		}
		String commandLine = "{ call xhb_listing_pkg.get_list_in_date_range(?,?,?,?,?) }";
		int[] parameterTypes = new int[] { Types.INTEGER, Types.VARCHAR, Types.DATE, Types.DATE};
		Object[] parameters = new Object[] {courtId, listType, new java.sql.Date(fromDate.getTime()),new java.sql.Date(toDate.getTime()) };
        return findLists(commandLine, parameterTypes, parameters);
	}
	
	public Collection<ListBasicValue> findFinalDailyListByDate(Integer courtId, Date diaryDate) {	
		if (log.isDebugEnabled()) {
			log.debug("findFinalDailyListByDate(courtId="+courtId+", diaryDate="+diaryDate+")");
		}
		String commandLine = "{ call xhb_listing_pkg.get_final_list_for_date_daily(?,?,?) }";
		int[] parameterTypes = new int[] { Types.INTEGER, Types.DATE };
		Object[] parameters = new Object[] {courtId, new java.sql.Date(diaryDate.getTime()) };
        return findLists(commandLine, parameterTypes, parameters);
	} 

	public Collection<DefendantValue> getDefendantsOnList(Integer caseOnListId) {
		if (log.isDebugEnabled()) {
			log.debug("getDefendantsOnList(caseOnListId="+caseOnListId+")");
		}
		final DefendantValueRowProcessor defendantOnListRowProcessor = new DefendantValueRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.get_defendants_on_list(?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(defendantOnListRowProcessor);
        sp.execute(new Object[] {caseOnListId});
        Collection<DefendantValue> defendantOnCaseValues = defendantOnListRowProcessor.getResults();
       return defendantOnCaseValues;
	}
	
	public Collection<DefendantValue> getDefendantsOnFixture(Integer casediaryFixtureId) {
		if (log.isDebugEnabled()) {
			log.debug("getDefendantsOnFixture(casediaryFixtureId="+casediaryFixtureId+")");
		}
		final DefendantValueRowProcessor defendantOnListRowProcessor = new DefendantValueRowProcessor();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.get_defendants_on_fixture(?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(defendantOnListRowProcessor);
        sp.execute(new Object[] {casediaryFixtureId});
        Collection<DefendantValue> defendantOnCaseValues = defendantOnListRowProcessor.getResults();
       return defendantOnCaseValues;
	}

	public Integer getNextCaseOnListId() {
		if (log.isDebugEnabled()) {
			log.debug("getNextCaseOnListId()");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_listing_pkg.get_next_col_id() }");
		final Integer id = (Integer) sf.executeFunction(new Object[0], Types.INTEGER);
        return id;
	}
	
	public Integer getNextSittingOnListId() {
		if (log.isDebugEnabled()) {
			log.debug("getNextSittingOnListId()");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_listing_pkg.get_next_sol_id() }");
		final Integer id = (Integer) sf.executeFunction(new Object[0], Types.INTEGER);
        return id;
	}
	
	/**
     * Find Cases on a list using List Date, Court Site and Court Room
     * @param listDate List Date
     * @param courtSiteId Court Site Id
     * @param courtRoomId Court Room Id
     * @param courtId	Court Id (required if isAllCourtSites is true)
     * @param isAllCourtSites true if to return records for all sites, else false
     * @param isAllCourtRooms true if to return records for all court rooms, else false
     * @param isFloaterCourtRooms true if to return floater cases, else false
     * @return Cases on List
	 * @return collection of the results set ListingResultsInformation
	 */
	public Collection<ListingResultsInformation> findCasesOnListByDateSiteAndRoom(
			Date listDate, 
			Integer courtSiteId, 
			Integer courtRoomId, 
			Integer courtId, 
			Boolean isAllCourtSites,
			Boolean isAllCourtRooms, 
			Boolean isFloaterCourtRooms) {
		
		if (log.isDebugEnabled()) {
			log.debug("findCasesOnListByDateSiteAndRoom(listDate="+listDate+", courtSiteId="+courtSiteId+", courtRoomId="+courtRoomId+
				", courtId="+courtId+", isAllCourtSites="+isAllCourtSites+", isAllCourtRooms="+isAllCourtRooms+", isFloaterCourtRooms="+isFloaterCourtRooms+")");
		}
		Integer mode = 4;							// Search on a specific court site and room
		if ( isAllCourtSites ) mode = 1;			// Search all court sites for a court
		else if ( isFloaterCourtRooms ) mode = 2;	// Search for floater cases
		else if ( isAllCourtRooms ) mode = 3;		// Search all court rooms in a court site
		
		final ReflectionRowProcessor caseOnListRowProcessor = new ReflectionRowProcessor(ListingResultsInformation.class);
		caseOnListRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.find_cases_for_list_results(?,?,?,?,?,?) }");
        sp.registerInTypes(new int[] { Types.DATE, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER });
        sp.setRowProcessor(caseOnListRowProcessor);
        sp.execute(new Object[] {new java.sql.Date(listDate.getTime()), courtSiteId, courtRoomId, courtId, mode});
        @SuppressWarnings("unchecked")
		Collection<ListingResultsInformation> listingResultsInformationValues = caseOnListRowProcessor.getResults();
       return listingResultsInformationValues;
	}

	private Collection<ListBasicValue> findLists(String commandLine, int[] parameterTypes, Object[] parameters) {
		if (log.isDebugEnabled()) {
			log.debug("findLists(commandLine="+commandLine+", parameterTypes="+parameterTypes+", parameters="+parameters+")");
		}
		final ReflectionRowProcessor listRowProcessor = new ReflectionRowProcessor(ListBasicValue.class);
		listRowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure(commandLine);
        sp.registerInTypes(parameterTypes);
        sp.setRowProcessor(listRowProcessor);
        sp.execute(parameters);
        @SuppressWarnings("unchecked")
		Collection<ListBasicValue> lists = listRowProcessor.getResults();
        return lists;		
	}
	
	public void deleteList(Integer listId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteList(listId="+listId+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.delete_list(?) }");
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.executeUpdate(new Object[] {listId });
	}
	
	public String validateListsForFixture(Integer caseId, Date fixtureDate) {
		if (log.isDebugEnabled()) {
			log.debug("validateListsForFixture(caseId="+caseId+", fixtureDate="+fixtureDate+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_listing_pkg.validate_lists_for_fixture(?,?) }");
		sf.registerInTypes(new int[] { Types.INTEGER, Types.DATE });
		final String validationCode = (String) sf.executeFunction(new Object[] {caseId, fixtureDate}, Types.VARCHAR);
        return validationCode;
	}
	
	public void setListPublishStatus(Integer listId, Date publishDate, String publishStatus, String publishErrorReason){
		if (log.isDebugEnabled()) {
			log.debug("setListPublishStatus(listId="+listId+", publishDate="+publishDate+", publishStatus="+publishStatus+", publishErrorReason="+publishErrorReason+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_list_distribution_pkg.set_list_publish_status(?,?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.VARCHAR, Types.VARCHAR});
		Timestamp sqlPublishDate = publishDate != null ? new java.sql.Timestamp(publishDate.getTime()) : null;
		sp.executeUpdate(new Object[] {listId, sqlPublishDate, publishStatus, publishErrorReason});
	}

	public void publishList(Integer listId){
		if (log.isDebugEnabled()) {
			log.debug("publishList(listId="+listId+")");
		}
		final StoredProcedure sp = createStoredProcedure("{ call xhb_list_distribution_pkg.publish_list(?) }");
		sp.registerInTypes(new int[] { Types.INTEGER });
		sp.executeUpdate(new Object[] {listId });
	}

	public java.lang.String getDailyPrisonList(Integer listId) { 
		if (log.isDebugEnabled()) {
			log.debug("getDailyPrisonList(listId="+listId+")");
		}
		final String uniqueId = "";
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_get_xml_reports.get_daily_prison_list(?,?) }");
		sf.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		final String xml = StringStrategy.getValue((Clob) sf.executeFunction(new Object[] {listId , uniqueId}, Types.CLOB));
		return xml;
	}

	public Integer getFixtureCount(Integer courtId, Date fromDate, Date toDate) {
		if (log.isDebugEnabled()) {
			log.debug("getFixtureCount(courtId="+courtId+", fromDate="+fromDate+", toDate="+toDate+")");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_listing_pkg.get_fixture_count(?,?,?) }");
		sf.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.DATE });
		final Integer id = (Integer) sf.executeFunction(new Object[] {courtId, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime())}, Types.INTEGER);
        return id;
	}
	
	public Collection<CaseIdValue> getCasesByFilter(Integer courtId, String caseType, String caseClass, 
												String bcStatus, String hearingTypeCode, Integer timeEstFrom, 
												Integer timeEstTo, Integer units, Integer refJudgeType, 
    											Integer unitsWeeks, String secureCourtRoom,	String juvenileOnly) {
		if (log.isDebugEnabled()) {
			log.debug("getCasesByFilter(courtId="+courtId+", caseType="+caseType+", caseClass="+caseClass+", bcStatus="+bcStatus+", hearingTypeCode="+hearingTypeCode+
				", timeEstFrom="+timeEstFrom+", timeEstTo="+timeEstTo+", units="+units+", refJudgeType="+refJudgeType+", unitsWeeks="+unitsWeeks+", secureCourtRoom="+secureCourtRoom+
				", juvenileOnly="+juvenileOnly+")");
		}
		// Setup the results row processor
		final ReflectionRowProcessor listRowProcessor = new ReflectionRowProcessor(CaseIdValue.class);
		listRowProcessor.registerDefaultBindings();
		
		// Setup and execute the procedure call
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.get_cases_by_filter(?,?,?,?,?,?,?,?,?,?,?,?,?) }");
		sp.registerInTypes(new int [] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, 
										Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
										Types.INTEGER, Types.INTEGER, Types.INTEGER, 
				 						Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
		sp.setRowProcessor(listRowProcessor);
		sp.execute(new Object[] {courtId, caseType, caseClass, 
									bcStatus, hearingTypeCode, timeEstFrom, 
									timeEstTo, units, refJudgeType, 
				 					unitsWeeks, secureCourtRoom, juvenileOnly});
				
		// Collect the results from the row processor 
        @SuppressWarnings("unchecked")
		Collection<CaseIdValue> results = listRowProcessor.getResults();
	    return results;
	}

	@SuppressWarnings("unchecked")
	public Collection<CaseOnListComplexValue> findUnlistedCasesOnList(final Integer listId, final String reserved)  throws FinderException {
		if (log.isDebugEnabled()) {
			log.debug("findUnlistedCasesOnList(listId="+listId+", reserved="+reserved+")");
		}
		final ListHelper listHelper = new ListHelper();
		final ReflectionRowProcessor rowProcessor = new ReflectionRowProcessor(CaseOnListBasicValue.class);
		rowProcessor.registerDefaultBindings();
		final StoredProcedure sp = createStoredProcedure("{ call xhb_listing_pkg.get_unlisted_cases_on_list(?,?,?) }");
		sp.registerInTypes(new int[] { Types.INTEGER, Types.VARCHAR });
		sp.setRowProcessor(rowProcessor);
		sp.execute(new Object[] {listId, reserved});
		Collection<CaseOnListComplexValue> results = new ArrayList<CaseOnListComplexValue>();
		// Populate the complex values from the basic
		for (CaseOnListBasicValue basicValue : (Collection<CaseOnListBasicValue>) rowProcessor.getResults()) {
			CaseOnListComplexValue result = new CaseOnListComplexValue(basicValue);
			listHelper.populateComplexValue(result);
			results.add(result);
		}
		return results;
	}

}