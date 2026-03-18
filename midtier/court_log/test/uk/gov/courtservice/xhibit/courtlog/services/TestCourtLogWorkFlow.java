//package uk.gov.courtservice.xhibit.courtlog.services;
//
//import java.util.Collection;
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category.XhbCourtLogCategory;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanNotFoundException;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
//import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * Test class for the CourtLogWorkFlow
// *
// * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
// * @author tz0d5m
// * @version $Revision: 1.22 $
// */
//public class TestCourtLogWorkFlow extends CourtLogTestCase
//{
//    // as we are only testing that methods return values, ensure that we will
//    // pick up all of the court log entries...
//    private final Date fromDate = new Date(0L);                // 01/01/1970
//    private final Date toDate   = new Date(76686048000000L);   // 01/01/2500
//
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the test method
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestCourtLogWorkFlow(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    public void testNewEntry()
//    {
//        // @todo implement...
//        fail("Not implemented yet...");
//    }
//
//    /**
//     * Test method to ensure that the deleteEntry method deleted the court log
//     * entry from the database of an entry that does exist.
//     *
//     * @throws CourtLogBusinessException If there is any problems deleting the
//     *         entry
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #deleteEntry(java.lang.Integer, boolean)
//     */
//    public void testDeleteEntry() throws CourtLogBusinessException
//    {
//        final Long courtLogEntryId = getCourtLogEntryId();
//
//        // run the method under test...
//        CourtLogWorkFlow.deleteEntry(courtLogEntryId, false);
//
//        try
//        {
//            EntityHelper.getXhbCourtLogEntry(courtLogEntryId);
//            fail("Court log entry should have been deleted...");
//        }
//        catch (XhbCourtLogEntryBeanNotFoundException e)
//        {
//            // expected exception, so ignore...
//        }
//    }
//
//    /**
//     * Test method to ensure that the updateEntry method updated the court log
//     * entry from the database of an entry that does exist.
//     *
//     * @throws CourtLogBusinessException If there is any problems updating the
//     *         entry
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #updateEntry(uk.gov.courtservice.xhibit.courtlog.vos
//     *      .CourtLogCRUDValue)
//     */
//    public void testUpdateEntry() throws CourtLogBusinessException
//    {
//        // acquire any currently existing log entry to test the update...
//        final Long courtLogEntryId = getCourtLogEntryId();
//        final String testText = "This is some test free text";
//
//        // get the CRUD value for updating...
//        CourtLogCRUDValue value = CourtLogWorkFlow.getEntry(courtLogEntryId);
//        value.setEntryFreeText(testText);
//
//        CourtLogWorkFlow.updateEntry(value);
//
//        // now acquire the crud again and check the entryFreeText...
//        value = CourtLogWorkFlow.getEntry(courtLogEntryId);
//        assertEquals("Free text is not as expected",
//                value.getEntryFreeText(), testText);
//    }
//
//    /**
//     * Test method to ensure that the getCourtLog method that takes only the
//     * case id as a parameter, returns only court log events with the correct
//     * case id.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getCourtLog(java.lang.Integer)
//     */
//    public void testGetCourtLogInteger()
//    {
//        // first acquire the case id of any court log entry...
//        final Long logEntryId = getCourtLogEntryId();
//
//        final XhbCourtLogEntry xhbCourtLogEntry =
//                EntityHelper.getXhbCourtLogEntry(logEntryId);
//        final Integer caseId = xhbCourtLogEntry.getCaseId();
//
//        final CourtLogViewValue[] courtLog = CourtLogWorkFlow.getCourtLog(caseId);
//
//        validateCourtLog(caseId, logEntryId, courtLog);
//    }
//
//    /**
//     * Test method to ensure that the getCourtLog method that takes the
//     * case id and from and to dates as parameters, returns only court log
//     * events with the correct case id.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getCourtLog(java.lang.Integer, java.util.Date, java.util.Date)
//     */
//    public void testGetCourtLogIntegerDateDate()
//    {
//        // first acquire the case id of any court log entry...
//        final Long logEntryId = getCourtLogEntryId();
//
//        final XhbCourtLogEntry xhbCourtLogEntry =
//                EntityHelper.getXhbCourtLogEntry(logEntryId);
//        final Integer caseId = xhbCourtLogEntry.getCaseId();
//
//        final CourtLogViewValue[] courtLog = CourtLogWorkFlow.getCourtLog(
//                caseId, fromDate, toDate);
//
//        validateCourtLog(caseId, logEntryId, courtLog);
//    }
//
//    /**
//     * Test method to ensure that the getEntry method returns a CRUD value for
//     * a valid court log entry id.  This ensures that the id field is populated
//     * and is as expected.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getEntry(java.lang.Integer)
//     */
//    public void testGetEntry()
//    {
//         final Long courtLogEntryId = getCourtLogEntryId();
//         // run the method under test...
//         final CourtLogCRUDValue crud = CourtLogWorkFlow.getEntry(courtLogEntryId);
//
//         assertNotNull("Acquired CRUD value should not be null", crud);
//         assertEquals("Acquired CRUD's id does not match passed value",
//                 crud.getLogEntryId(), courtLogEntryId);
//    }
//
//    /**
//     * Test method to ensure that the getCourtLog method that takes the
//     * case id and the category description, returns only court log
//     * events with the correct case id.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getCourtLog(java.lang.Integer, java.lang.String)
//     */
//    public void testGetCourtLogIntegerString()
//    {
//        final Long logEntryId = getCourtLogEntryId();
//        final XhbCourtLogEntry xhbCourtLogEntry =
//                EntityHelper.getXhbCourtLogEntry(logEntryId);
//
//        // acquire any of the categories for the selected court log event...
//        final Collection categories = xhbCourtLogEntry.getXhbCourtLogEventDesc().getXhbCourtLogCategories();
//        final XhbCourtLogCategory category = (XhbCourtLogCategory) categories.iterator().next();
//        final String categoryDesc = category.getXhbCourtLogCategoryDesc().getCategoryDescription();
//
//        final Integer caseId = xhbCourtLogEntry.getCaseId();
//        final CourtLogViewValue[] courtLog = CourtLogWorkFlow.getCourtLog(caseId, categoryDesc);
//
//        validateCourtLog(caseId, logEntryId, courtLog);
//    }
//
//    public void testHasMoreEvents()
//    {
//        // @todo implement...
//        fail("Not implemented yet...");
//    }
//
//    /**
//     * Test method to ensure that the getCourtLog method that takes the
//     * case id, category description and fromDate and toDate, returns only
//     * court log events with the correct case id.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getCourtLog(java.lang.Integer, java.lang.String, java.util.Date,
//     *      java.util.Date)
//     */
//    public void testGetCourtLogIntegerStringDateDate()
//    {
//        final Long logEntryId = getCourtLogEntryId();
//        final XhbCourtLogEntry xhbCourtLogEntry =
//                EntityHelper.getXhbCourtLogEntry(logEntryId);
//
//        // acquire any of the categories for the selected court log event...
//        final Collection categories = xhbCourtLogEntry.getXhbCourtLogEventDesc().getXhbCourtLogCategories();
//        final XhbCourtLogCategory category = (XhbCourtLogCategory) categories.iterator().next();
//        final String categoryDesc = category.getXhbCourtLogCategoryDesc().getCategoryDescription();
//
//        final Integer caseId = xhbCourtLogEntry.getCaseId();
//        final CourtLogViewValue[] courtLog = CourtLogWorkFlow.getCourtLog(
//                caseId, categoryDesc, fromDate, toDate);
//
//        validateCourtLog(caseId, logEntryId, courtLog);
//    }
//
//    /**
//     * Test method to ensure that the getCourtLogScheduledHearingValuesForCase
//     * method that takes the case id as parameter, returns valid value objects
//     * and that an expected scheduled hearing is found in the list.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getCourtLogScheduledHearingValuesForCase(java.lang.Integer)
//     */
//    public void testGetCourtLogScheduledHearingValuesForCase()
//    {
//        final String methodName = "testGetCourtLogScheduledHearingValuesForCase() - ";
//
//        final Integer scheduledHearingId = getScheduledHearingId();
//        final Integer caseId =
//                EntityHelper.getXhbScheduledHearing(scheduledHearingId)
//                .getXhbHearing().getCaseId();
//
//        final CourtLogScheduledHearingValue[] values =
//                CourtLogWorkFlow.getCourtLogScheduledHearingValuesForCase(caseId);
//
//        assertNotNull("Returned array should never be null", values);
//        boolean originalScheduledHearingFound = false;
//
//        for (int i = 0; i < values.length; i++)
//        {
//            log.debug(methodName + "values[" + i + "] = " + values[i]);
//            assertNotNull("Entry should not be null", values[i]);
//
//            if (values[i].getScheduledHearingId().equals(scheduledHearingId))
//            {
//                originalScheduledHearingFound = true;
//            }
//        }
//
//        assertTrue("Scheduled hearing we know exists was not found",
//                originalScheduledHearingFound);
//    }
//
//    /**
//     * Test method to ensure that the getEventTypesByCategoryDesc returns a
//     * valid array of <code>Integer</code>s for a known category description.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow
//     *      #getEventTypesByCategoryDesc(java.lang.String[])
//     */
//    public void testGetEventTypesByCategoryDesc()
//    {
//        // acquire a description that exists in the database...
//        final String categoryDesc = getCourtLogCategoryDescDescription();
//
//        final Integer[] eventTypes = CourtLogWorkFlow.getEventTypesByCategoryDesc(
//                new String[] { categoryDesc });
//
//        assertNotNull("Returned event types should never be null", eventTypes);
//        assertTrue("At least 1 event must be returned", (eventTypes.length > 0));
//
//        // now ensure that there are no null values returned in the array...
//        for (int i = 0; i < eventTypes.length; i++)
//        {
//            assertNotNull("No null values should be returned", eventTypes[i]);
//        }
//    }
//
//    /**
//     * Private helper method used to validate a returned array of court log
//     * entries.
//     *
//     * @param caseId The caseId that every event must belong to
//     * @param guaranteedLogEntryId A primary key of a court log entry that
//     *        is guaranteed to exist, and be contained in the passed array
//     * @param courtLogEntries The array of court log entries acquired from
//     *        a query, must have at least one entry in it
//     */
//    private void validateCourtLog(Integer caseId,
//                                  Long guaranteedLogEntryId,
//                                  CourtLogViewValue[] courtLogEntries)
//    {
//        // we know for a fact that there is at least one court log entry...
//        assertTrue("There should be at least one court log entry",
//                (courtLogEntries.length >= 1));
//
//        boolean originalFound = false;
//
//        for (int i = 0; i < courtLogEntries.length; i++)
//        {
//            assertEquals("Case id is not as expected",
//                    caseId, courtLogEntries[i].getCaseId());
//
//            if (courtLogEntries[i].getLogEntryId().equals(guaranteedLogEntryId))
//            {
//                originalFound = true;
//            }
//        }
//
//        assertTrue("The entry we new to exist has not been returned",
//                originalFound);
//    }
//}
//