//package uk.gov.courtservice.xhibit.courtlog.directionsbycase;
//
//import java.sql.Statement;
//import java.sql.Timestamp;
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
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * <p>Title: DirectionsByCaseSubscriberTest</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: </p>
// * @author Bal Bhamra
// * @version 1.0
// */
//public class TestDirectionsByCaseSubscriber extends TransactionTestCase
//{
//    private final Logger log = Logger.getLogger(getClass());
//
//
//    // values from statndard test data
//    private static final Integer CASE_ID_1_CRUD = new Integer(40);
//    private static final Integer CASE_ID_2_CRUD = new Integer(109);
//    private static final Integer CASE_ID_1 = new Integer(40);
//    private static final Integer CASE_ID_2 = new Integer(109);
//
//    // court log events for Directions For Case
//    private static final Integer P_AND_D_FORM   = new Integer(40710);
//    private static final Integer TRIAL_TIME_EST = new Integer(40711);
//    private static final Integer DIRECTIONS     = new Integer(40712);
//
//    // log entries to insert - 2 each to make sure the logic pick up the most recent one
//    private String logEntryDirections =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40712.xsd''>" +
//            "<free_text/><type>40712</type><Directions_By_Case_Options>" +
//            "<E40712_Directions>Current Text</E40712_Directions>" +
//            "</Directions_By_Case_Options></event>";
//    private String logEntryPandD =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40710.xsd''>" +
//            "<free_text/><type>40710</type><Directions_By_Case_Options>" +
//            "<E40710_P_And_D>E40710_Form_Handed_In</E40710_P_And_D>" +
//            "</Directions_By_Case_Options></event>";
//    private String logEntryTrialTime =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40711.xsd''>" +
//            "<free_text/><type>40711</type><Directions_By_Case_Options>" +
//            "<E40711_Time_Estimate><E40711_Time>5.0</E40711_Time>" +
//            "<E40711_Time_Estimate_Options>E40711_Weeks</E40711_Time_Estimate_Options>" +
//            "</E40711_Time_Estimate></Directions_By_Case_Options></event>";
//
//    // earlier log entries - the logic shouldn't pick up these
//    private String logEntryDirections2 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40712.xsd''>" +
//            "<free_text/><type>40712</type><Directions_By_Case_Options>" +
//            "<E40712_Directions>Old Text</E40712_Directions>" +
//            "</Directions_By_Case_Options></event>";
//    private String logEntryPandD2 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40710.xsd''>" +
//            "<free_text/><type>40710</type><Directions_By_Case_Options>" +
//            "<E40710_P_And_D>E40710_Form_Not_Handed_In</E40710_P_And_D>" +
//            "</Directions_By_Case_Options></event>";
//    private String logEntryTrialTime2 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''40711.xsd''>" +
//            "<free_text/><type>40711</type><Directions_By_Case_Options>" +
//            "<E40711_Time_Estimate><E40711_Time>77.0</E40711_Time>" +
//            "<E40711_Time_Estimate_Options>E40711_Months</E40711_Time_Estimate_Options>" +
//            "</E40711_Time_Estimate></Directions_By_Case_Options></event>";
//
//
//    public TestDirectionsByCaseSubscriber(String name) throws NamingException
//    {
//        super(name, true); /** @todo Set to true to leave DB in original state */
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        // for each event we need to remove all court log events for one case
//        // AND delete all but add one event for another case
//
//        connection = dataSource.getConnection();
//        // find the last court log entry - we will delete anything created after
//        // this in the tearDown
//        Statement stmt = connection.createStatement();
//
//        //******************************************
//        // initialise the directions_for_case table
//        //******************************************
//
//        //Delete existing Directions for Case 1
//        stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_CASE WHERE CASE_ID = " +
//                          CASE_ID_1);
//
//        //Insert a record for Directions for Case 1
//        stmt.executeQuery("INSERT INTO XHB_DIRECTIONS_FOR_CASE " +
//                          "(CASE_ID, DIRECTIONS_TEXT," +
//                          "HAS_PANDD_FORM, TRIAL_TIME_ESTIMATE, TRIAL_TIME_UNIT) " +
//                          "VALUES (" + CASE_ID_1 + ", 'Original directions text for Case 1', " +
//                          "'Y', 12, 1)");
//
//        //Delete existing Directions for Case 2
//        stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_CASE WHERE CASE_ID = " +
//                          CASE_ID_2);
//
//        //Insert a record for Directions for Case 2
//        stmt.executeQuery("INSERT INTO XHB_DIRECTIONS_FOR_CASE " +
//                          "(CASE_ID, DIRECTIONS_TEXT," +
//                          "HAS_PANDD_FORM, TRIAL_TIME_ESTIMATE, TRIAL_TIME_UNIT) " +
//                          "VALUES (" + CASE_ID_2 + ", 'Original directions text for Case 2', " +
//                          "'Y', 12, 1)");
//
//
//        //******************************************
//        // initialise the court_log_entry table
//        //******************************************
//
//        //**** Direction Text Events ***
//
//        // Remove existing Directions Text for Case 1
//        stmt.executeQuery("DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + DIRECTIONS +"') " +
//                          "AND case_id = " + CASE_ID_1);
//
//        // Insert a Direction Text event for Case 1
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_1 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + DIRECTIONS +"'), " +
//                          "'" + logEntryDirections + "')");
//
//        // Remove existing Directions Text events for Case 2
//        stmt.executeQuery( //
//                          "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + DIRECTIONS +"') " +
//                          "AND case_id = " + CASE_ID_2);
//
//        // Insert a Direction Text event for Case 2
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + DIRECTIONS +"'), " +
//                          "'" + logEntryDirections + "')");
//
//        // Insert another Direction Text event for Case 2 with earlier date
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + DIRECTIONS +"'), " +
//                          "'" + logEntryDirections2 + "')");
//
//        //**** Direction Text Events ***
//        stmt.executeQuery( // P and D (40710)
//                          "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + P_AND_D_FORM +"') " +
//                          "AND case_id = " + CASE_ID_1);
//        stmt.executeQuery( // Trial Time (40711)
//                          "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + TRIAL_TIME_EST +"') " +
//                          "AND case_id = " + CASE_ID_1);
//
//        stmt.executeQuery( // P and D (40710)
//                          "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + P_AND_D_FORM +"') " +
//                          "AND case_id = " + CASE_ID_2);
//
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + P_AND_D_FORM +"'), " +
//                          "'" + logEntryPandD + "')");
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + P_AND_D_FORM +"'), " +
//                          "'" + logEntryPandD2 + "')");
//
//        stmt.executeQuery( // Trial Time (40711)
//                          "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + TRIAL_TIME_EST +"') " +
//                          "AND case_id = " + CASE_ID_2);
//
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-1, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + TRIAL_TIME_EST +"'), " +
//                          "'" + logEntryTrialTime + "')");
//
//        stmt.executeQuery("INSERT INTO XHB_COURT_LOG_ENTRY " +
//                          "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                          "VALUES (" + CASE_ID_2 + ", SYSDATE-2, " +
//                          "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                          "event_type = '" + TRIAL_TIME_EST +"'), " +
//                          "'" + logEntryTrialTime2 + "')");
//    }
//
//    public void testPreCreate()
//    {
//        log.debug("start - testPreCreate()");
//        log.debug("TO BE DONE");
//        log.debug("end - testPreCreate()");
//    }
//
//    public void testPreDelete()
//    {
//        log.debug("start - testPreDelete()");
//        log.debug("TO BE DONE");
//        log.debug("end - testPreDelete()");
//    }
//
//    public void testPostDelete()
//    {
//        log.debug("start - testPostDelete()");
//        // create new
////        CourtLogCRUDValue crudValue = new CourtLogCRUDValue();
////        OperationContext oc = new OperationContext(crudValue);
//        log.debug("end - testPostDelete()");
//    }
//
//    public void testPreUpdate()
//    {
//        log.debug("start - testPreUpdate()");
//        log.debug("TO BE DONE");
//        log.debug("end - testPreUpdate()");
//    }
//
//    /**
//     * Test to ensure that the post deletion logic for the direction by case events
//     * works correctly when a single event exists. This version checks the
//     * Directions Text is set to null when the event is deleted..
//     *
//     * @throws CaseNotFoundException if the method under test throws it
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .directionbycase.DirectionByCaseSubscriber#postDelete(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPerformPostDeleteDirectionsTextSingle()
//    {
//
//        // call a delete where there are no other events in the db, the
//        // detail of this event type should be removed from the directions record
//        DirectionsByCaseSubscriber directionsByCaseSubscriber= new DirectionsByCaseSubscriber();
//
//// Test 1 - Directions Text
//
//        CourtLogCRUDValue clcv1 = new CourtLogCRUDValue();
//        clcv1.setCaseId(CASE_ID_1);
//        clcv1.setEntryDate(new Date());
//        clcv1.setEventType(DIRECTIONS);
//        OperationContext oc1 = OperationContext.newInstance(clcv1);
//
//        Vector cleVector = new Vector( XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(
//                CASE_ID_1,
//                DIRECTIONS,
//                new Timestamp( 0 ) ) );
//
//        XhbCourtLogEntryBasicValue cle = ((XhbCourtLogEntry)cleVector.lastElement( )).getData( );
//
//            /** @todo Use DeleteHelper to delete latest courtlog event , then call postDelete*/
//            /** @todo Ask Meeraj if can make below static */
////            DeleteHelper deleteHelper = new DeleteHelper();
////            deleteHelper.deleteEntry(oc2);
////CANNOT set OriginalBasicValue(XhbCourtLogEntryBasicValue) so doing below instead
//
//        XhbCourtLogEntryBeanHelper2.remove(cle);
//
//        directionsByCaseSubscriber.postDelete(oc1);
//
//        XhbDirectionsForCase dirForCase = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_1);
//        log.debug("testPerformPostDeleteDirectionsTextSingle - Directions Text: "+ dirForCase.getDirectionsText());
//        assertNull("Directions Text should be null : ", dirForCase.getDirectionsText());
//    }
//
//    /**
//       * Test to ensure that the post deletion logic for the direction by case events
//       * works correctly when a many events exist. This version tests whether the previous
//       * event updates the directions for case when the latest event is deleted.
//       *
//       * @throws CaseNotFoundException if the method under test throws it
//       * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//       *      .directionbycase.DirectionByCaseSubscriber#postDelete(
//       *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPostDeleteDirectionsByCaseTextMany()
//    {
//        // call a delete where there are no other events in the db, the
//        // detail of this event type should be removed from the directions record
//        DirectionsByCaseSubscriber directionsByCaseSubscriber= new DirectionsByCaseSubscriber();
//
//        CourtLogCRUDValue clcv2 = new CourtLogCRUDValue();
//        clcv2.setCaseId(CASE_ID_2);
//        clcv2.setEntryDate(new Date());
//        clcv2.setEventType(DIRECTIONS);
//        OperationContext oc2 = OperationContext.newInstance(clcv2);
//
//        Vector cleVector = new Vector( XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(
//                CASE_ID_1,
//                DIRECTIONS,
//                new Timestamp( 0 ) ) );
//
//        XhbCourtLogEntryBasicValue cle = ((XhbCourtLogEntry)cleVector.lastElement( )).getData( );
//
///** @todo Use DeleteHelper to delete latest courtlog event , then call postDelete*/
///** @todo Ask Meeraj if can make below static */
////            DeleteHelper deleteHelper = new DeleteHelper();
////            deleteHelper.deleteEntry(oc2);
////CANNOT set OriginalBasicValue(XhbCourtLogEntryBasicValue) so doing below instead
//
//        XhbCourtLogEntryBeanHelper2.remove(cle);
//
//        directionsByCaseSubscriber.postDelete(oc2);
//
//        XhbDirectionsForCase dirForCase = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_2);
//        log.debug("testPerformPostDeleteDirectionsTextMany - Directions Text: "+ dirForCase.getDirectionsText());
//
//        assertEquals("Directions Text : ", "Old Text", dirForCase.getDirectionsText());
//    }
//
//    /**
//     * Test for the has p and d event
//     */
//    public void testPerformPostDeletePandD()
//    {
//        // call a delete where there are no other events in the db, the
//        // detail of this event type should be removed from the directions record
//        DirectionsByCaseSubscriber directionsByCaseSubscriber = new DirectionsByCaseSubscriber();
//
//        // Test 1 - Pleas and Directions
//        CourtLogCRUDValue clcv1 = new CourtLogCRUDValue();
//        clcv1.setCaseId(CASE_ID_1_CRUD);
//        clcv1.setEntryDate(new Date());
//        clcv1.setEventType(P_AND_D_FORM);
//
//        OperationContext oc1 = OperationContext.newInstance(clcv1);
//        directionsByCaseSubscriber.postDelete(oc1);
//
//        XhbDirectionsForCase dirForCase1 = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_1);
//        assertNull("P and D Form should be null: ", dirForCase1.getHasPanddForm());
//
//        // Test 2 - Pleas and Directions
//        CourtLogCRUDValue clcv2 = new CourtLogCRUDValue();
//        clcv2.setCaseId(CASE_ID_2_CRUD);
//        clcv2.setEntryDate(new Date());
//        clcv2.setEventType(P_AND_D_FORM);
//
//        OperationContext oc2 = OperationContext.newInstance(clcv2);
//        directionsByCaseSubscriber.postDelete(oc2);
//
//        XhbDirectionsForCase dirForCase2 = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_2);
//        assertNull("P and D Form should be Y: ", dirForCase2.getHasPanddForm());
//
//    }
//
//    /**
//     * Test for the trial time estimate event (includes units)
//     */
//    public void testPerformTrialTimeEst()
//    {
//        // call a delete where there are no other events in the db, the
//        // detail of this event type should be removed from the directions record
//        DirectionsByCaseSubscriber directionsByCaseSubscriber = new DirectionsByCaseSubscriber();
//
//        CourtLogCRUDValue clvv1 =  new CourtLogCRUDValue();
//        clvv1.setCaseId(CASE_ID_1);
//        clvv1.setEntryDate(new Date());
//        clvv1.setEventType(TRIAL_TIME_EST);
//
//        OperationContext oc1 = OperationContext.newInstance(clvv1);
//        directionsByCaseSubscriber.postDelete(oc1);
//
//        XhbDirectionsForCase dirForCase1 = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_1);
//        assertEquals("Trial Time Estimate : ", null, dirForCase1.getTrialTimeEstimate());
//        assertEquals("Trial Time Unit : ", null, dirForCase1.getTrialTimeUnit());
//
//        // call a delete where there is an earlier event in the db, the directions
//        // record should be updated with the details of this earlier event
//        CourtLogCRUDValue clvv2 =  new CourtLogCRUDValue();
//        clvv2.setCaseId(CASE_ID_2);
//        clvv2.setEntryDate(new Date());
//        clvv2.setEventType(TRIAL_TIME_EST);
//
//        OperationContext oc2 = OperationContext.newInstance(clvv2);
//        directionsByCaseSubscriber.postDelete(oc2);
//
//        XhbDirectionsForCase dirForCase2 = XhbDirectionsForCaseBeanHelper2.findByCaseID(CASE_ID_1);
//        assertEquals("Trial Time Estimate : ", new Float(5.0), dirForCase2.getTrialTimeEstimate());
//        assertEquals("Trial Time Unit : ", new Integer(3), dirForCase2.getTrialTimeUnit());
//     }
//}
//
//
//