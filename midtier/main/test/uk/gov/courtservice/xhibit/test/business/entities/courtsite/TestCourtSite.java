//package uk.gov.courtservice.xhibit.test.business.entities.courtsite;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.court.Court;
//import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSite;
//import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteHome;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//import java.util.Collection;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Pete Raymond
// * @version 1.0
// */
//
//public class TestCourtSite extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(getClass());
//    String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    String courtSiteSQL = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 1, 'site name 1', 's', 1, 1,  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    String courtRoom1SQL = "INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, LOCATION, CREST_COURT_ROOM_NO, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (  1, 'court room name 1', 'court room desc', 'location', 1, 1,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM') ,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    String courtRoom2SQL = "INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, LOCATION, CREST_COURT_ROOM_NO, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (  2, 'court room name 2', 'court room desc', 'location', 1, 1,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM') ,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//    public TestCourtSite(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        log("setUp()");
//        log("deleting data");
//        // dont forget to do in reverse order
//        TestUtils.execSql("delete from xhb_court_room");
//        TestUtils.execSql("delete from xhb_court_site");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("delete from xhb_address");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//        TestUtils.execSql(courtSiteSQL);
//    }
//
//    protected void tearDown() throws Exception
//    {
//        log("tearDown()");
//        log("deleting data");
//        // dont forget to do in reverse order
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
//            CourtSite courtSite = (CourtSite)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtSiteHome.class, pk);
//            log("***courtSite =" + courtSite);
//            assertEquals(pk, courtSite.getCourtSiteId());
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    public void testGetCourt()
//    {
//        log("***testGetCourt() start");
//        Integer pk = new Integer(1);
//        try
//        {
//            Context ic = new InitialContext();
//            UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
//            ut.begin();
//            CourtSite courtSite = (CourtSite)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtSiteHome.class, pk);
//            log("***courtSite =" + courtSite);
//            assertEquals(pk, courtSite.getCourtSiteId());
//            Court court = courtSite.getCourt();
//            assertEquals(pk,court.getCourtId());
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//    public void testGetCourtRooms() throws Exception
//    {
//        log("***testGetCourtRooms() start");
//        TestUtils.execSql(courtRoom1SQL);
//        TestUtils.execSql(courtRoom2SQL);
//        Integer pk = new Integer(1);
//        try
//        {
//            Context ic = new InitialContext();
//            UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
//            ut.begin();
//            CourtSite courtSite = (CourtSite)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtSiteHome.class, pk);
//            log("***courtSite =" + courtSite);
//            assertEquals(pk, courtSite.getCourtSiteId());
//            Collection courtRooms = courtSite.getCourtRooms();
//            assertTrue(!courtRooms.isEmpty());
//            assertEquals(2,courtRooms.size());
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
//