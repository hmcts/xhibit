//package uk.gov.courtservice.xhibit.courtlog.jury;
//
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Vector;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.services.XMLServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: </p>
// * @author unascribed
// * @version $Revision: 1.3 $
// */
//public class TestJuryStopSubscriber extends TransactionTestCase
//{
//    private final Logger log = Logger.getLogger( getClass( ) );
//    private final JuryStartSubscriber subscriber = new JuryStartSubscriber( );
//    private final XMLServices xmlServices = CSServices.getXMLServices( );
//
//    private static final Integer CASE_ID_1 = new Integer( 1 );
//    private static final Integer DEFENDANT_ON_CASE_ID_1 = new Integer( 1 );
//    //private static final Integer DEFENDANT_ID_1 = new Integer( 1 );
//    private static final Integer CASE_ID_2 = new Integer( 2 );
//    private static final Integer DEFENDANT_ON_CASE_ID_2 = new Integer( 2 );
//    //private static final Integer DEFENDANT_ID_2 = new Integer( 2 );
//    private static final Integer CASE_ID_3 = new Integer( 3 );
//    private static final Integer DEFENDANT_ON_CASE_ID_3 = new Integer( 3 );
//    //private static final Integer DEFENDANT_ID_3 = new Integer( 3 );
//
//    private static final Integer JURY_RETIRES_EVENT = new Integer( 20914 );
//    private static final Integer JURY_RETURNS_EVENT = new Integer( 20913 );
//
//    //private static final String JURY_OUT_HOURS_XPATH = "event/E20913_Jury_Out_Time_Hours";
//    //private static final String JURY_OUT_MINUTES_XPATH = "event/E20913_Jury_Out_Time_Minutes";
//
//    private static final Integer[] CASE_IDS = new Integer[] {
//        CASE_ID_1,
//        CASE_ID_2,
//        CASE_ID_3 };
//
//    private static final Integer[] EVENT_TYPES = new Integer[] {
//        JURY_RETIRES_EVENT,
//        JURY_RETURNS_EVENT };
//
//    private String logEntryJuryRetire01_01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''" +
//            JURY_RETIRES_EVENT + ".xsd''>" +
//            "<free_text>Freetext for JuryRetire caseID:1, sequence number:1</freetext>" +
//            "<type>" + JURY_RETIRES_EVENT + "</type>" +
//            "</event>";
//    private String logEntryJuryRetire02_01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''" +
//            JURY_RETIRES_EVENT + ".xsd''>" +
//            "<free_text>Freetext for JuryRetire caseID:2, sequence number:1</freetext>" +
//            "<type>" + JURY_RETIRES_EVENT + "</type>" +
//            "</event>";
//    private String logEntryJuryRetire03_01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''" +
//            JURY_RETIRES_EVENT + ".xsd''>" +
//            "<free_text>Freetext for JuryRetire caseID:3, sequence number:1</freetext>" +
//            "<type>" + JURY_RETIRES_EVENT + "</type>" +
//            "</event>";
//
//    private String logEntryJuryReturn01_01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''" +
//            JURY_RETURNS_EVENT + ".xsd''>" +
//            "<free_text>Freetext for JuryReturn caseID:1, sequence number:1. Jury out for 1 hours 10 minutes</freetext>" +
//            "<type>" + JURY_RETURNS_EVENT + "</type>" +
//            "<E20913_Jury_Returns_Options><E20913_JTO_Type>E20913_Other</E20913_JTO_Type></E20913_Jury_Returns_Options>" +
//            "<E20913_Jury_Out_Time_Hours>1</E20913_Jury_Out_Time_Hours>" +
//            "<E20913_Jury_Out_Time_Minutes>10</E20913_Jury_Out_Time_Minutes>" +
//            "</event>";
//    private String logEntryJuryReturn02_01 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi=''http://www.w3.org/2001/XMLSchema-instance'' xsi:noNamespaceSchemaLocation=''" +
//            JURY_RETURNS_EVENT + ".xsd''>" +
//            "<free_text>Freetext for JuryReturn caseID:2, sequence number:1. Jury out for 2 hours 20 minutes</freetext>" +
//            "<type>" + JURY_RETURNS_EVENT + "</type>" +
//            "<E20913_Jury_Returns_Options><E20913_JTO_Type>E20913_Other</E20913_JTO_Type></E20913_Jury_Returns_Options>" +
//            "<E20913_Jury_Out_Time_Hours>2</E20913_Jury_Out_Time_Hours>" +
//            "<E20913_Jury_Out_Time_Minutes>20</E20913_Jury_Out_Time_Minutes>" +
//            "</event>";
//
//    public TestJuryStopSubscriber( String name )
//    throws NamingException {
//        super( name, true );
//    }
//
//    protected void setUp( )
//    throws Exception {
//        super.setUp( );
//
//        connection = dataSource.getConnection( );
//
//        Statement stmt = connection.createStatement( );
//
//        // Remove all jury calculation events for all cases
//        for( int x = 0; x < EVENT_TYPES.length; x++ ) {
//            for( int y = 0; y < CASE_IDS.length; y++ ) {
//                stmt.executeQuery( "DELETE FROM XHB_COURT_LOG_ENTRY WHERE event_desc_id = " +
//                                   "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                                   "event_type = " + EVENT_TYPES[x] + " ) " +
//                                   "AND case_id = " + CASE_IDS[y] );
//            }
//        }
//
//        // Insert rows for CASE_ID_1
//        stmt.executeQuery( "INSERT INTO XHB_COURT_LOG_ENTRY " +
//                           "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                           "VALUES (" + CASE_ID_1 + ", to_date('01-MAY-2004 1000','DD-MON-YYYY HH24MI'), " +
//                           "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                           "event_type = '" + JURY_RETIRES_EVENT +"'), " +
//                           "'" + logEntryJuryRetire01_01 + "')" );
//        stmt.executeQuery( "INSERT INTO XHB_COURT_LOG_ENTRY " +
//                           "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                           "VALUES (" + CASE_ID_1 + ", to_date('01-MAY-2004 1200','DD-MON-YYYY HH24MI'), " +
//                           "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                           "event_type = '" + JURY_RETURNS_EVENT +"'), " +
//                           "'" + logEntryJuryReturn01_01 + "')" );
//
//        // Insert rows for CASE_ID_2
//        stmt.executeQuery( "INSERT INTO XHB_COURT_LOG_ENTRY " +
//                           "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                           "VALUES (" + CASE_ID_2 + ", to_date('01-MAY-2004 1030','DD-MON-YYYY HH24MI'), " +
//                           "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                           "event_type = '" + JURY_RETIRES_EVENT +"'), " +
//                           "'" + logEntryJuryRetire02_01 + "')" );
//        stmt.executeQuery( "INSERT INTO XHB_COURT_LOG_ENTRY " +
//                           "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                           "VALUES (" + CASE_ID_2 + ", to_date('01-MAY-2004 1200','DD-MON-YYYY HH24MI'), " +
//                           "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                           "event_type = '" + JURY_RETURNS_EVENT +"'), " +
//                           "'" + logEntryJuryReturn02_01 + "')" );
//
//        // Insert rows for CASE_ID_3
//        stmt.executeQuery( "INSERT INTO XHB_COURT_LOG_ENTRY " +
//                           "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML) " +
//                           "VALUES (" + CASE_ID_3 + ", to_date('01-MAY-2004 1100','DD-MON-YYYY HH24MI'), " +
//                           "(SELECT event_desc_id FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//                           "event_type = '" + JURY_RETIRES_EVENT +"'), " +
//                           "'" + logEntryJuryRetire03_01 + "')" );
//    }
//
//    /**
//     * Test the postCreate processing when there is a prior jury retires event.
//     */
//    public void testPostCreate( ) {
//        log.debug( "start - testPostCreate( )" );
//
//        XhbCourtLogEntryBasicValue cle = null;
//        int hours   = -1;
//        int minutes = -1;
//
//        cle = getLastEvent( CASE_ID_1, JURY_RETURNS_EVENT );
//        hours   = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Hours" ) );
//        minutes = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Minutes" ) );
//
//        assertTrue( "Jury Out Time Hours prior to test should be 1!",    hours   == 1 );
//        assertTrue( "Jury Out Time Minutes prior to test should be 10!", minutes == 10 );
//
//        CourtLogCRUDValue crudValue = buildCRUDValue(
//            CASE_ID_1,
//            JURY_RETIRES_EVENT,
//            DEFENDANT_ON_CASE_ID_1
//        );
//
//        OperationContext context = OperationContext.newInstance( crudValue );
//        try {
//            subscriber.postCreate( context );
//        }
//        catch( Exception e ) {
//            e.printStackTrace( );
//        }
//
//        cle = getLastEvent( CASE_ID_1, JURY_RETURNS_EVENT );
//        hours   = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Hours" ) );
//        minutes = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Minutes" ) );
//
//        assertTrue( "Jury Out Time Hours after to test should be 2!",    hours  == 2 );
//        assertTrue( "Jury Out Time Minutes after to test should be 0!", minutes == 0 );
//
//        log.debug( "end - testPostCreate( )" );
//    }
//
//    /**
//     * Test the postUpdate processing when there is a prior jury returns event.
//     */
//    public void testPostUpdate( ) {
//        log.debug( "start - testPostUpdate( )" );
//
//        XhbCourtLogEntryBasicValue cle = null;
//        int hours   = -1;
//        int minutes = -1;
//
//        cle = getLastEvent( CASE_ID_2, JURY_RETURNS_EVENT );
//        hours   = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Hours" ) );
//        minutes = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Minutes" ) );
//
//        assertTrue( "Jury Out Time Hours prior to test should be 2!",    hours   == 2 );
//        assertTrue( "Jury Out Time Minutes prior to test should be 20!", minutes == 20 );
//
//        CourtLogCRUDValue crudValue = buildCRUDValue(
//            CASE_ID_2,
//            JURY_RETIRES_EVENT,
//            DEFENDANT_ON_CASE_ID_2
//        );
//
//        OperationContext context = OperationContext.newInstance( crudValue );
//        try {
//            subscriber.postUpdate( context );
//        }
//        catch( Exception e ) {
//            e.printStackTrace( );
//        }
//
//        cle = getLastEvent( CASE_ID_2, JURY_RETURNS_EVENT );
//        hours   = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Hours" ) );
//        minutes = Integer.parseInt( getValueFromXml( cle.getLogEntryXml( ), "E20913_Jury_Out_Time_Minutes" ) );
//
//        assertTrue( "Jury Out Time Hours after to test should be 1!",    hours   == 1 );
//        assertTrue( "Jury Out Time Minutes after to test should be 30!", minutes == 30 );
//
//        log.debug( "end - testPostUpdate( )" );
//    }
//
//    /**
//     * Test the postDelete processing when there is a prior jury returns event.
//     */
//    public void testPostDelete( ) {
//        log.debug( "start - testPostDelete( )" );
//
//        XhbCourtLogEntryBasicValue cle = null;
//
//        CourtLogCRUDValue crudValue = buildCRUDValue(
//            CASE_ID_3,
//            JURY_RETIRES_EVENT,
//            DEFENDANT_ON_CASE_ID_3
//        );
//
//        OperationContext context = OperationContext.newInstance( crudValue );
//        try {
//            subscriber.postUpdate( context );
//        }
//        catch( Exception e ) {
//            e.printStackTrace( );
//        }
//
//        cle = getLastEvent( CASE_ID_3, JURY_RETURNS_EVENT );
//        assertNull( "Jury Returns Event should not be returned!", cle );
//
//        cle = getLastEvent( CASE_ID_3, JURY_RETIRES_EVENT );
//        assertEquals( "Entry Date on entity should match the crudValue!",
//                      cle.getDateTime( ),
//                      crudValue.getEntryDate( ) );
//
//        log.debug( "end - testPostDelete( )" );
//    }
//
//    /**
//     * Build a CRUD value using the parameters supplied.
//     * @param caseId
//     * @param eventType
//     * @param defOnCaseId
//     * @return the CRUD value
//     */
//    private CourtLogCRUDValue buildCRUDValue( Integer caseId,
//                                              Integer eventType,
//                                              Integer defOnCaseId )
//    {
//        Calendar transactionDate = Calendar.getInstance( );
//        transactionDate.set( 2004, 4, 1, 12, 0, 0 );
//
//        CourtLogCRUDValue crud = new CourtLogCRUDValue();
//        crud.setCaseId(caseId);
//        crud.setEntryDate(transactionDate.getTime( ));
//        crud.setEventType(eventType);
//        crud.setDefendantOnCaseId(defOnCaseId);
//
//        return crud;
//    }
//
//    /**
//     * No customer finder method exists to return the last event of a given type
//     * so instead use the custom finder to obtain all events of a given case/event
//     * type combination - this returns data in ascending entryDate order - and
//     * return the last one.
//     * @param caseId
//     * @param eventType
//     * @return the court log entry that coresponds to the most recent one for
//     * the given case/event type combination
//     */
//    private XhbCourtLogEntryBasicValue getLastEvent( Integer caseId,
//                                                     Integer eventType )
//    {
//        Vector cleVector = new Vector( XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(
//                caseId,
//                eventType,
//                new Timestamp( 0 ) ) );
//
//        if( cleVector != null && cleVector.size( ) > 0 ) {
//            return( ((XhbCourtLogEntry)cleVector.lastElement( )).getData( ) );
//        }
//        else {
//            return null;
//        }
//    }
//
//    /**
//     * Returns the value of the requested attribute from the supplied XML string.
//     * @param xmlString - the XML string to be searched
//     * @param attribute - the attribute( tag ) to be searched for
//     * @return the value of the attribute
//     */
//    private String getValueFromXml( String xmlString, String attribute ) {
//        return xmlServices.getXpathValueFromXmlString( xmlString, attribute );
//    }
//}
//
//
//