//package uk.gov.courtservice.xhibit.courtlog.directionsbydefendant;
//
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Vector;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendant;
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * <p>Title: DirectionsByCaseSubscriberTest</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: </p>
// * @author Stephen Tully
// * @version 1.0
// */
//public class TestDirectionsByDefendantSubscriber extends TransactionTestCase
//{
//    private final Logger log = Logger.getLogger(getClass());
//
//
//    // values from statndard test data
//    private static final Integer CASE_ID_1 = new Integer(1);
//    private static final Integer DEFENDANT_ON_CASE_ID_1 = new Integer(1);
//    //private static final Integer DEFENDANT_ID_1 = new Integer(1);
//    private static final Integer CASE_ID_2 = new Integer(2);
//    private static final Integer DEFENDANT_ON_CASE_ID_2 = new Integer(2);
//    //private static final Integer DEFENDANT_ID_2 = new Integer(2);
//
//    // court log events for Directions For Case
//    private static final Integer IDENTIFIED       = new Integer(40704);
//    private static final Integer ARRAIGNED        = new Integer(40705);
//    private static final Integer BAIL_AND_CUSTODY = new Integer(40706);
//    private static final Integer CERT_OF_ATTEND   = new Integer(40707);
//    private static final Integer FORM_B           = new Integer(40708);
//    private static final Integer[] EVENT_TYPES    = new Integer[] {
//        IDENTIFIED,
//        ARRAIGNED,
//        BAIL_AND_CUSTODY,
//        CERT_OF_ATTEND,
//        FORM_B
//    };
//
//    // log entries to insert - 2 each to make sure the logic pick up the most recent one
//    private String logEntryIdentified01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40704.xsd''>" +
//            "<free_text/><type>40704</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40704_Identification>E40704_Defendant_Identified</E40704_Identification>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryArraigned01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40705.xsd''>" +
//            "<free_text/><type>40705</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40705_Arraignment>true</E40705_Arraignment>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryBailOrCustody01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40706.xsd''>" +
//            "<free_text/><type>40706</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40706_Bail_Or_Custody>" +
//            "<E40706_Bail_Or_Custody_Options>E40706_RIC</E40706_Bail_Or_Custody_Options>" +
//            "<E40706_Bail_Or_Custody_Options_Conditions>Bail or custody conditions 1</E40706_Bail_Or_Custody_Options_Conditions>" +
//            "</E40706_Bail_Or_Custody>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryCertOfAttend01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40707.xsd''>" +
//            "<free_text/><type>40707</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40707_Certificate_Of_Attendance>E40707_Granted</E40707_Certificate_Of_Attendance>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryFormB01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40708.xsd''>" +
//            "<free_text/><type>40708</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40708_Form_B>" +
//            "<E40708_Form_B_Options>E40708_Filed</E40708_Form_B_Options>" +
//            "<E40708_To_Be_Filed_Date>05-MAY-2004</E40708_To_Be_Filed_Date>" +
//            "</E40708_Form_B>" +
//            "</Directions_By_Defendant_Options></event>";
//
//    // earlier log entries - the logic shouldn't pick up these
//    private String logEntryIdentified02 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40704.xsd''>" +
//            "<free_text/><type>40704</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40704_Identification>E40704_No_Reply</E40704_Identification>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryArraigned02 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40705.xsd''>" +
//            "<free_text/><type>40705</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40705_Arraignment>false</E40705_Arraignment>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryBailOrCustody02 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40706.xsd''>" +
//            "<free_text/><type>40706</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40706_Bail_Or_Custody>" +
//            "<E40706_Bail_Or_Custody_Options>E40706_Bail_As_Before</E40706_Bail_Or_Custody_Options>" +
//            "<E40706_Bail_Or_Custody_Options_Conditions>Bail or custody conditions 2</E40706_Bail_Or_Custody_Options_Conditions>" +
//            "</E40706_Bail_Or_Custody>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryCertOfAttend02 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40707.xsd''>" +
//            "<free_text/><type>40707</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40707_Certificate_Of_Attendance>E40707_Refused</E40707_Certificate_Of_Attendance>" +
//            "</Directions_By_Defendant_Options></event>";
//    private String logEntryFormB02 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40708.xsd''>" +
//            "<free_text/><type>40708</type>" +
//            "<Directions_By_Defendant_Options><Defendant_Name>Joe Bloggs</Defendant_Name>" +
//            "<E40708_Form_B>" +
//            "<E40708_Form_B_Options>E40708_To_Be_Filed</E40708_Form_B_Options>" +
//            "<E40708_To_Be_Filed_Date>04-JUL-2004</E40708_To_Be_Filed_Date>" +
//            "</E40708_Form_B>" +
//            "</Directions_By_Defendant_Options></event>";
//
//    public TestDirectionsByDefendantSubscriber(String name)
//    throws NamingException {
//        super(name, true);
//    }
//
//    protected void setUp()
//    throws Exception
//    {
//        super.setUp();
//
//        connection = dataSource.getConnection();
//
//        Statement stmt = connection.createStatement();
//
//        //************************************
//        // Initialise directions for defendant
//        //************************************
//        //Delete existing Directions for Defendant 1
//        stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_DEFENDANT WHERE DEFENDANT_ON_CASE_ID = " +
//                          DEFENDANT_ON_CASE_ID_1);
//
//        //Insert a record for Directions for Defendant 1
//        stmt.executeQuery("INSERT INTO XHB_DIRECTIONS_FOR_DEFENDANT " +
//                          "(DEFENDANT_ON_CASE_ID, FREETEXT," +
//                          "IS_IDENTIFIED, TO_BE_FILED_BY, FILED_FORM_B, CERT_ATTENDANCE, " +
//                          "NEW_BAIL_CONDITIONS, BAIL_STATUS, ARRAIGNED) " +
//                          "VALUES (" + DEFENDANT_ON_CASE_ID_1 + ", 'Original directions text for defendant 1', " +
//                          "'Y', '01-JAN-2004', 'Y', '1', " +
//                          "'Original bail conditions for defendant 1', '1', 'Y')");
//
//        //Delete existing Directions for Defendant 2
//        stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_DEFENDANT WHERE DEFENDANT_ON_CASE_ID = " +
//                          DEFENDANT_ON_CASE_ID_2);
//
//        //Insert a record for Directions for Defendant 2
//        stmt.executeQuery("INSERT INTO XHB_DIRECTIONS_FOR_DEFENDANT " +
//                          "(DEFENDANT_ON_CASE_ID, FREETEXT," +
//                          "IS_IDENTIFIED, TO_BE_FILED_BY, FILED_FORM_B, CERT_ATTENDANCE, " +
//                          "NEW_BAIL_CONDITIONS, BAIL_STATUS, ARRAIGNED) " +
//                          "VALUES (" + DEFENDANT_ON_CASE_ID_2 + ", 'Original directions text for defendant 2', " +
//                          "'N', '02-JAN-2004', 'N', '2', " +
//                          "'Original bail conditions for defendant 2', '2', 'N')");
//
//
//        //******************************************
//        // initialise the court_log_entry table
//        //******************************************
//        // Remove any existing Court Log Entry for Case 1
//        for( int x = 0; x < EVENT_TYPES.length; x++ ) {
//            stmt.executeQuery("DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                              "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                              "event_type = '" + EVENT_TYPES[x] + "') " +
//                              "AND case_id = " + CASE_ID_1);
//        }
//
//        // Insert Court Log Entrys for Case 1
//        // IDENTIFIED
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + IDENTIFIED +"'), " +
//                          "'" + logEntryIdentified01 + "')");
//        // ARRAIGNED
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + ARRAIGNED +"'), " +
//                          "'" + logEntryArraigned01 + "')");
//        // BAIL_AND_CUSTODY
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + BAIL_AND_CUSTODY +"'), " +
//                          "'" + logEntryBailOrCustody01 + "')");
//        // CERT_OF_ATTEND
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + CERT_OF_ATTEND +"'), " +
//                          "'" + logEntryCertOfAttend01 + "')");
//        // FORM_B
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + FORM_B +"'), " +
//                          "'" + logEntryFormB01 + "')");
//
//        // Remove any existing Court Log Entry for Case 2
//        for( int x = 0; x < EVENT_TYPES.length; x++ ) {
//            stmt.executeQuery("DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                              "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                              "event_type = '" + EVENT_TYPES[x] + "') " +
//                              "AND case_id = " + CASE_ID_2);
//        }
//
//        // Insert Court Log Entrys for Case 2
//        // IDENTIFIED
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + IDENTIFIED +"'), " +
//                          "'" + logEntryIdentified01 + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + IDENTIFIED +"'), " +
//                          "'" + logEntryIdentified02 + "')");
//
//        // ASSAIGNED
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + ARRAIGNED +"'), " +
//                          "'" + logEntryArraigned01 + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + ARRAIGNED +"'), " +
//                          "'" + logEntryArraigned02 + "')");
//
//        // BAIL_AND_CUSTODY
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + BAIL_AND_CUSTODY +"'), " +
//                          "'" + logEntryBailOrCustody01 + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + BAIL_AND_CUSTODY +"'), " +
//                          "'" + logEntryBailOrCustody02 + "')");
//
//        // CERT_OF_ATTEND
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + CERT_OF_ATTEND +"'), " +
//                          "'" + logEntryCertOfAttend01 + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + CERT_OF_ATTEND +"'), " +
//                          "'" + logEntryCertOfAttend02 + "')");
//
//        // FORM_B
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + FORM_B +"'), " +
//                          "'" + logEntryFormB01 + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + FORM_B +"'), " +
//                          "'" + logEntryFormB02 + "')");
//    }
//
//    public void testPostDelete() {
//        log.debug("start - testPostDelete()");
//        performPostDeleteSingleEvents();
//        performPostDeleteManyEvents();
//        log.debug("end - testPostDelete()");
//    }
//
//    /**
//     * Test to ensure that the post deletion logic for the direction by defendant events
//     * works correctly when a single event exists for the defendant on case.
//     * This version checks that specific fields on the directions for defendant
//     * record are initialised when the court log event is deleted.
//     */
//    public void performPostDeleteSingleEvents() {
//        //DirectionsByDefendantSubscriber subscriber = new DirectionsByDefendantSubscriber();
//        XhbDirectionsForDefendant direction;
//
//        // Test 1 - Identified
//        direction = executeTest(CASE_ID_1,IDENTIFIED,DEFENDANT_ON_CASE_ID_1);
//        assertNull("isIdentified should be null: ", direction.getIsIdentified());
//
//        // Test 2 - Arraigned
//        direction = executeTest(CASE_ID_1,ARRAIGNED,DEFENDANT_ON_CASE_ID_1);
//        assertNull("arraigned should be null: ", direction.getArraigned());
//
//        // Test 3 - Bail And Custody
//        direction = executeTest(CASE_ID_1,BAIL_AND_CUSTODY,DEFENDANT_ON_CASE_ID_1);
//        assertNull("bailStatus should be null: ", direction.getBailStatus());
//        assertNull("newBailConditions should be null: ", direction.getNewBailConditions());
//
//        // Test 4 - Certified Attendance
//        direction = executeTest(CASE_ID_1,CERT_OF_ATTEND,DEFENDANT_ON_CASE_ID_1);
//        assertNull("certAttendance should be null: ", direction.getCertAttendance());
//
//        // Test 5 - Form B
//        direction = executeTest(CASE_ID_1,FORM_B,DEFENDANT_ON_CASE_ID_1);
//        assertNull("filedFormB should be null: ", direction.getFiledFormB());
//        assertNull("toBeFiledBy should be null: ", direction.getToBeFiledBy());
//    }
//
//    /**
//     * Test to ensure that the post deletion logic for the direction by defendant events
//     * works correctly when many events exist. This version tests whether the previous
//     * event updates the directions for defendant when the latest event is deleted.
//     */
//    public void performPostDeleteManyEvents()
//    {
//        // call a delete where there are no other events in the db, the
//        // detail of this event type should be removed from the directions record
//        //DirectionsByCaseSubscriber directionsByCaseSubscriber= new DirectionsByCaseSubscriber();
//        XhbDirectionsForDefendant direction;
//
//        // Test 1 - Identified
//        direction = executeTest(CASE_ID_2,IDENTIFIED,DEFENDANT_ON_CASE_ID_2);
//        assertEquals("isIdentified should be 'N'", "N", direction.getIsIdentified());
//
//        // Test 2 - Arraigned
//        direction = executeTest(CASE_ID_2,ARRAIGNED,DEFENDANT_ON_CASE_ID_2);
//        assertEquals("arraigned should be 'N'", "N", direction.getArraigned());
//
//        // Test 3 - Bail & Custody
//        direction = executeTest(CASE_ID_2,BAIL_AND_CUSTODY,DEFENDANT_ON_CASE_ID_2);
//        assertEquals("bailStatus should be '2'", "2", direction.getBailStatus());
//        assertEquals("newBailConditions should be 'Bail or custody conditions 2'",
//                     "Bail or custody conditions 2",
//                     direction.getNewBailConditions());
//
//        // Test 4 - Certified Attendance
//        direction = executeTest(CASE_ID_2,CERT_OF_ATTEND,DEFENDANT_ON_CASE_ID_2);
//        assertEquals("certAttendance should be '2'", "2", direction.getCertAttendance());
//
//        // Test 5 - Form B
//        Calendar date = Calendar.getInstance();
//        date.set(2004,6,4,0,0,0);
//        direction = executeTest(CASE_ID_2,FORM_B,DEFENDANT_ON_CASE_ID_2);
//        assertEquals("filedFormB should be 'N'", "N", direction.getFiledFormB());
//        assertEquals("toBeFiledBy should be '04/JUL/2004'", date.getTime(), direction.getToBeFiledBy());
//    }
//
//    /**
//     * The steps for the test are:
//     * - build a CRUD value to satisfy the test for the case/event/defendant on
//     *   case
//     * - instantiate an OperationContext for the CRUD value
//     * - get the court log entry that coresponds to the most recent event of the
//     *   given type and then remove it to simulate the deletion having occurred
//     * - call the subscriber's postDelete logic
//     * - return the updated directions for defendant record
//     * @param caseId
//     * @param eventType
//     * @param defOnCaseId
//     * @return the updated directions for defendant record
//     */
//    private XhbDirectionsForDefendant executeTest(
//            Integer caseId,
//            Integer eventType,
//            Integer defOnCaseId)
//    {
//        DirectionsByDefendantSubscriber subscriber = new DirectionsByDefendantSubscriber();
//        CourtLogCRUDValue crud = buildCRUDValue(caseId,eventType,defOnCaseId);
//        OperationContext oc = OperationContext.newInstance(crud);
//
//        XhbCourtLogEntryBasicValue cle = getLastEvent( caseId, eventType );
//
//        XhbCourtLogEntryBeanHelper2.remove( cle );
//        subscriber.postDelete(oc);
//        return getDirectionForDefendant(defOnCaseId);
//    }
//
//    /**
//     * Build a CRUD value using the parameters supplied.
//     * @param caseId
//     * @param eventType
//     * @param defOnCaseId
//     * @return the CRUD value
//     */
//    private CourtLogCRUDValue buildCRUDValue( Integer caseId, Integer eventType, Integer defOnCaseId ) {
//        CourtLogCRUDValue crud = new CourtLogCRUDValue();
//        crud.setCaseId(caseId);
//        crud.setEntryDate(new Date());
//        crud.setEventType(eventType);
//        crud.setDefendantOnCaseId(defOnCaseId);
//
//        return crud;
//    }
//
//    /**
//     * Return the court log entry that coresponds to the most recent entry for
//     * the caseID/eventType combination.
//     * @param caseId
//     * @param eventType
//     * @return the court log entry that coresponds to the most recent one for
//     * the given case/event type combination
//     */
//    private XhbCourtLogEntryBasicValue getLastEvent(Integer caseId, Integer eventType) {
//        Vector cleVector = new Vector( XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(
//                caseId,
//                eventType,
//                new Timestamp( 0 ) ) );
//
//        return( ((XhbCourtLogEntry)cleVector.lastElement( )).getData( ) );
//    }
//
//    /**
//     * Return a directions for defendant record using the parameter supplied.
//     * @param defOnCaseId
//     * @return the directions for defendant record for the given defendant on
//     * case
//     */
//    private XhbDirectionsForDefendant getDirectionForDefendant(Integer defOnCaseId) {
//        return XhbDirectionsForDefendantBeanHelper2.findByPrimaryKey(defOnCaseId);
//    }
//}
//
//
//