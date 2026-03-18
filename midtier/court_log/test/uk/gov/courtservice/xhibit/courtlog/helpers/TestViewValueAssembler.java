//package uk.gov.courtservice.xhibit.courtlog.helpers;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;
//
///**
// * Test class for the ViewValueAssembler.
// *
// * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
// * @author tz0d5m
// * @version $Revision: 1.7 $
// */
//public class TestViewValueAssembler extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the method under test
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestViewValueAssembler(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Test to ensure that the correct instance of
//     * <code>ViewValueAssembler</code> is returned when an instance of
//     * <code>MultiCaseCourtLogCRUDValue</code> is passed up.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #newInstance(uk.gov.courtservice.xhibit.courtlog.vos
//     *      .CourtLogCRUDValue)
//     */
//    public void testNewInstanceMultiCase()
//    {
//        final CourtLogCRUDValue crud = new MultiCaseCourtLogCRUDValue();
//        // call the method under test...
//        final ViewValueAssembler vv = ViewValueAssembler.newInstance(crud);
//
//        assertTrue(vv.getClass() + " not instance of multi-case assembler",
//                (vv instanceof ViewValueAssembler.MultiCaseViewValueAssembler));
//    }
//
//    /**
//     * Test to ensure that the correct instance of
//     * <code>ViewValueAssembler</code> is returned when an instance of
//     * <code>CourtLogCRUDValue</code> is passed up.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #newInstance(uk.gov.courtservice.xhibit.courtlog.vos
//     *      .CourtLogCRUDValue)
//     */
//    public void testNewInstanceSingleCase()
//    {
//        final CourtLogCRUDValue crud = new CourtLogCRUDValue();
//        // call the method under test...
//        final ViewValueAssembler vv = ViewValueAssembler.newInstance(crud);
//
//        assertTrue(vv.getClass() + " should not be an instance of multi-case assembler",
//                !(vv instanceof ViewValueAssembler.MultiCaseViewValueAssembler));
//    }
//
//    /**
//     * Test method to ensure that the creation of a
//     * <code>CourtLogViewValue</code> from a basic value is populated correctly.
//     * This method only tests to ensure that the version is populated, as all
//     * other fields are tested by the testDoPopulate method
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #createCourtLogViewValue(uk.gov.courtservice.xhibit.business
//     *      .entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue)
//     * @see #testDoPopulate()
//     */
//    public void testCreateCourtLogViewValue()
//    {
//        final XhbCourtLogEntryBasicValue xclebv =
//                EntityHelper.getXhbCourtLogEntryBasicValue(getCourtLogEntryId());
//        // call the method under test...
//        final CourtLogViewValue clvv = ViewValueAssembler.createCourtLogViewValue(xclebv);
//
//        // only need to test the version is set correctly, as the populate
//        // method is tested in testDoPopulate...
//        assertEquals("Version", clvv.getVersion(), xclebv.getVersion());
//        // test the entry id to ensure the doPopulate is called...
//        assertEquals("Log Entry Id", clvv.getLogEntryId(), xclebv.getEntryId());
//    }
//
//    /**
//     * Test method to ensure that the creation of several
//     * <code>CourtLogViewValue</code>s from basic values is populated correctly.
//     * This method only tests to ensure that the version is populated, as all
//     * other fields are tested by the testDoPopulate method
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #createCourtLogViewValues(uk.gov.courtservice.xhibit.business
//     *      .entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue[])
//     * @see #testDoPopulate()
//     */
//    public void testCreateCourtLogViewValues()
//    {
//
//        final XhbCourtLogEntryBasicValue xclebv =
//                EntityHelper.getXhbCourtLogEntryBasicValue(getCourtLogEntryId());
//        final XhbCourtLogEntryBasicValue[] entries = { xclebv, xclebv };
//
//        // call the method under test...
//        final CourtLogViewValue[] values =
//                ViewValueAssembler.createCourtLogViewValues(entries);
//
//        assertEquals("Array sizes are different", entries.length, values.length);
//
//        for (int i = 0; i < values.length; i++)
//        {
//            // only need to test the version is set correctly, as the populate
//            // method is tested in testDoPopulate...
//            assertEquals("Version", values[i].getVersion(), entries[i].getVersion());
//            // test the entry id to ensure the doPopulate is called...
//            assertEquals("Log Entry Id", values[i].getLogEntryId(), entries[i].getEntryId());
//        }
//    }
//
//    /**
//     * Ensure that all of the necessary properties are set on the view value
//     * after the call to the doPopulate method.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #doPopulate(uk.gov.courtservice.xhibit.courtlog.vos
//     *      .CourtLogViewValue, uk.gov.courtservice.xhibit.business.entities
//     *      .xhb_court_log_entry.XhbCourtLogEntryBasicValue)
//     */
//    public void testDoPopulate()
//    {
//        final XhbCourtLogEntryBasicValue xclebv =
//                EntityHelper.getXhbCourtLogEntryBasicValue(getCourtLogEntryId());
//        final CourtLogViewValue clvv = new CourtLogViewValue();
//
//        // call the method under test...
//        ViewValueAssembler.doPopulate(clvv, xclebv);
//
//        // validate that all of the required parameters are set...
//        assertEquals("Case Id", clvv.getCaseId(), xclebv.getCaseId());
//        assertEquals("Defendant On Case Id",
//                clvv.getDefendantOnCaseId(), xclebv.getDefendantOnCaseId());
//        assertEquals("Defendant On Offence Id",
//                clvv.getDefendantOnOffenceId(), xclebv.getDefendantOnOffenceId());
//        assertEquals("Scheduled Hearing Id",
//                clvv.getScheduledHearingId(), xclebv.getScheduledHearingId());
//        assertEquals("Entry Date", clvv.getEntryDate(), xclebv.getDateTime());
//        assertEquals("Log Entry Id", clvv.getLogEntryId(), xclebv.getEntryId());
//        assertEquals("Last Update Date",
//                clvv.getLastUpdateDate(), xclebv.getLastUpdateDate());
//        assertEquals("Log Entry", clvv.getLogEntry(), xclebv.getLogEntryXml());
//    }
//
//    /**
//     * Ensure that after calling the assembleViewValue method, a valid
//     * <code>CourtLogViewValue</code> is returned.  No property validation is
//     * required as these are performed in other test methods.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler
//     *      #assembleViewValue(uk.gov.courtservice.xhibit.business.entities
//     *      .xhb_court_log_entry.XhbCourtLogEntryBasicValue)
//     * @see #testDoPopulate()
//     */
//    public void testAssembleViewValue()
//    {
//        final XhbCourtLogEntryBasicValue xclebv =
//            EntityHelper.getXhbCourtLogEntryBasicValue(getCourtLogEntryId());
//
//        // first, acquire a standard assembler...
//        final ViewValueAssembler vva =
//                ViewValueAssembler.newInstance(new CourtLogCRUDValue());
//
//        // call the method under test
//        final CourtLogViewValue clvv = vva.assembleViewValue(xclebv);
//
//        assertNotNull("View value created must not be null", clvv);
//    }
//}
//