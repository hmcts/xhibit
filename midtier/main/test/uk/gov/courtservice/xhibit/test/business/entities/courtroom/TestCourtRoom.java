//package uk.gov.courtservice.xhibit.test.business.entities.courtroom;
//
////jdk
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
//import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomHome;
//import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Paul Grove
// * @version 1.0
// */
//
//public class TestCourtRoom extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(getClass());
//    String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    String courtSiteSQL = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 1, 'site name 1', 's', 1, 1,  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    String courtRoomSQL = "INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, LOCATION, CREST_COURT_ROOM_NO, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (  1, 'court room name 1', 'court room desc', 'location', 1, 1,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM') ,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    String publicNoticeSQL = "INSERT INTO XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, PUBLIC_NOTICE_DESC, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, 'public notice description', 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
//    String conPublicNoticeSQL1 = "INSERT INTO XHB_CONFIGURED_PUBLIC_NOTICE (CONFIGURED_PUBLIC_NOTICE_ID, IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, 1, 1, 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
//    String conPublicNoticeSQL2 = "INSERT INTO XHB_CONFIGURED_PUBLIC_NOTICE (CONFIGURED_PUBLIC_NOTICE_ID, IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 2, 1, 1, 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
//
//    public TestCourtRoom(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        log("setUp()");
//        log("deleting data");
//        // dont forget to do in reverse order
//        TestUtils.execSql("delete from xhb_configured_public_notice");
//        TestUtils.execSql("delete from xhb_public_notice");
//        TestUtils.execSql("delete from xhb_court_room");
//        TestUtils.execSql("delete from xhb_court_site");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("delete from xhb_address");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//        TestUtils.execSql(courtSiteSQL);
//        TestUtils.execSql(courtRoomSQL);
//    }
//
//    protected void tearDown() throws Exception
//    {
//        log("tearDown()");
//        log("deleting data");
//        // dont forget to do in reverse order
//        TestUtils.execSql("delete from xhb_configured_public_notice");
//        TestUtils.execSql("delete from xhb_public_notice");
//        TestUtils.execSql("delete from xhb_court_room");
//        TestUtils.execSql("delete from xhb_court_site");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("delete from xhb_address");
//    }
//
//    public void testFindByPrimaryKey()
//    {
//       log("***testFindByPrimaryKey() start");
//       Integer pk = new Integer(1);
//        try
//        {
//
//            CourtRoom courtRoom = (CourtRoom)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtRoomHome.class, pk);
//            log("***courtRoom =" + courtRoom);
//            assertEquals(pk, courtRoom.getCourtRoomId());
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    public void testGetCourtSite()
//    {
//        log("***testGetCourtSite() start");
//        Integer pk = new Integer(1);
//        try
//        {
//            Context ic = new InitialContext();
//            UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
//            ut.begin();
//            CourtRoom courtRoom = (CourtRoom)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtRoomHome.class, pk);
//            log("***courtRoom =" + courtRoom);
//            assertEquals(pk, courtRoom.getCourtRoomId());
//            CourtSite courtSite = courtRoom.getCourtSite();
//            assertEquals(pk,courtSite.getCourtSiteId());
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//    public void testGetConfiguredPublicNotice() throws Exception
//    {
//        log("***testGetConfiguredPublicNotice() start");
//        TestUtils.execSql(publicNoticeSQL);
//        TestUtils.execSql(conPublicNoticeSQL1);
//        TestUtils.execSql(conPublicNoticeSQL2);
//        Integer pk = new Integer(1);
//        try
//        {
//            Context ic = new InitialContext();
//            log("context=" + ic);
//            UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
//            log("transaction=" + ut + " beginning");
//            ut.begin();
//            CourtRoom courtRoom = (CourtRoom)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtRoomHome.class, pk);
//            log("***courtRoom =" + courtRoom);
//            assertEquals(pk, courtRoom.getCourtRoomId());
//
//            /* PFOX : moved publicnotice functionality
//            Collection configuredPublicNotice = courtRoom.getConfiguredPublicNotices();
//            assertTrue(!configuredPublicNotice.isEmpty());
//            assertEquals(2,configuredPublicNotice.size());
//            */
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//
//    private void log(String msg)
//    {
//        log.debug(msg);
//    }
//}