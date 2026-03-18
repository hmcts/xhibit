//package uk.gov.courtservice.xhibit.test.business.entities.court;
//
////jdk
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.court.Court;
//import uk.gov.courtservice.xhibit.business.entities.court.CourtHome;
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
// * @author Paul Grove
// * @version 1.0
// */
//
//public class TestCourt extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(getClass());
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
//    public  static String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
//    public  static String courtSite1SQL = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 1, 'site name 1', 's', 1, 1,  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    public  static String courtSite2SQL = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 2, 'site name 2', 's', 1, 1,  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//    public  static String publicNotice1SQL = "INSERT INTO XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, PUBLIC_NOTICE_DESC, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, 'public notice description 1', 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
//    public  static String publicNotice2SQL = "INSERT INTO XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, PUBLIC_NOTICE_DESC, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 2, 'public notice description 2', 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
//
//
//    public TestCourt(String s) throws Exception
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        log("setUp()");
//        log("deleting data");
//        // dont forget to do in reverse order
//        TestUtils.execSql("delete from xhb_public_notice");
//        TestUtils.execSql("delete from xhb_court_site");
//        TestUtils.execSql("delete from xhb_court");
//        TestUtils.execSql("delete from xhb_address");
//        TestUtils.execSql(addrSQL);
//        TestUtils.execSql(courtSQL);
//        TestUtils.execSql(courtSite1SQL);
//    }
//
//    protected void tearDown() throws Exception
//    {
//        log("tearDown()");
//        log("deleting data");
//        // dont forget to do in reverse order
//        TestUtils.execSql("delete from xhb_public_notice");
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
//            Court court = (Court)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtHome.class, pk);
//            log("***court =" + court);
//            assertEquals(pk, court.getCourtId());
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    public void testGetCourtSites()
//    {
//        log("***testGetCourtSites() start");
//
//        Integer pk = new Integer(1);
//        try
//        {
//            TestUtils.execSql(courtSite2SQL);
//            Court court = (Court)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtHome.class, pk);
//            log("***court found id =" + court.getCourtId());
//            Context ic = new InitialContext();
//            UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
//            ut.begin();
//            assertEquals(pk, court.getCourtId());
//            log("getting court sites");
//            Collection courtsites = court.getCourtSites();
//            log("court sites = " + courtsites);
//            assertEquals(2,courtsites.size());
//            ut.commit();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//    /*
//    public void testGetPublicNotices() throws Exception
//    {
//        log("***testGetPublicNotices() start");
//
//        Integer pk = new Integer(1);
//        try
//        {
//
//            Court court = (Court)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtHome.class, pk);
//            log("***court =" + court);
//
//
//            assertEquals(pk, court.getCourtId());
//            Context ic = new InitialContext();
//            log("context=" + ic);
//            log("getting transaction");
//            UserTransaction ut = (UserTransaction) ic.lookup("java:comp/UserTransaction");
//            log("transaction=" + ut);
//            ut.begin();
//            //Collection publicNotices = court.getPublicNotices();
//            Collection publicNotices = TestUtils.getCollection(court, "getPublicNotices");
//            log("publicNotices=" + publicNotices);
//            log("checking isempty");
//            assertTrue(!publicNotices.isEmpty());
//            log("checking size");
//            assertEquals(2,publicNotices.size());
//
//            ut.commit();
//
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//    */
//    private void log(String msg)
//    {
//        System.out.println(msg);
//        //log.debug(msg);
//    }
//}