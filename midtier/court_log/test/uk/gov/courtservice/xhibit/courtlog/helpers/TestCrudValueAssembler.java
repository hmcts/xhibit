//package uk.gov.courtservice.xhibit.courtlog.helpers;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * Test class for the CrudValueAssembler.
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.helpers.CrudValueAssembler
// */
//public class TestCrudValueAssembler extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the method under test
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestCrudValueAssembler(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Test to ensure that the createCourtLogCRUDValue method works correctly
//     * when a valid log entry is passed up to it, in that a non-null object
//     * is returned that matches the values expected.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.helpers.CrudValueAssembler
//     *      #createCourtLogCRUDValue(uk.gov.courtservice.xhibit.business
//     *      .entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue)
//     */
//    public void testCreateCourtLogCRUDValue()
//    {
//        final Long logEntryId = getCourtLogEntryId();
//        final XhbCourtLogEntryBasicValue value =
//                EntityHelper.getXhbCourtLogEntryBasicValue(logEntryId);
//
//        final CourtLogCRUDValue crud =
//                CrudValueAssembler.createCourtLogCRUDValue(value);
//
//        assertNotNull("Created crud value must not be null", crud);
//
//        assertEquals("Case Id", crud.getCaseId(), value.getCaseId());
//        assertEquals("Defendant On Case Id",
//                crud.getDefendantOnCaseId(), value.getDefendantOnCaseId());
//        assertEquals("Defendant On Offence Id",
//                crud.getDefendantOnOffenceId(), value.getDefendantOnOffenceId());
//        assertEquals("Entry Date", crud.getEntryDate(), value.getDateTime());
//        assertEquals("Log Entry Id", crud.getLogEntryId(), value.getEntryId());
//        assertEquals("Last Update Date",
//                crud.getLastUpdateDate(), value.getLastUpdateDate());
//
//        // @todo need to check the log entry generated as well...
//        //assertEquals("", crud.getLogEntry(), value.getLogEntryXml());
//    }
//}
//