//package uk.gov.courtservice.xhibit.test.business.entities.sitting;
//
///**
// *
// * <p>Note: This class is not runnable without removing all the contraints,
// * apart from primary key, of the SITTING table </p>
// * <p>Title: SittingTest </p>
// * <p>Description: To test findByTimeCourtSiteRoomId() methhod of sitting entity</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
///*
//public class SittingTest extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(SittingTest.class);
//
//    public SittingTest(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        log.debug("setUp()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_SITTING");
//        log.debug("Insert Sitting");
//        StringBuffer sb = new StringBuffer("INSERT INTO XHB_SITTING (SITTING_ID, COURT_SITE_ID, COURT_ROOM_ID, SITTING_TIME) VALUES (1, 2, 3, TO_Date( '11/11/2002 12:00:00', 'MM/DD/YYYY HH24:MI:SS'))");
//        TestUtils.execSql(sb.toString());
//    }
//
//    protected void tearDown()throws Exception
//    {
//        log.debug("tearDown()");
//        log.debug("deleting data");
//        TestUtils.execSql("Delete from XHB_SITTING");
//    }
//
//
//    public void testFindByTimeCourtSiteRoomId()
//    {
//        try
//        {
//            Timestamp time;
//
//            SittingHome home = lookupHome();
//            log.debug("testFindByTimeCourtSiteRoomId() - Got SittingHome");
//
//            log.debug("Time = 2002-11-11 13:00:00; Court Site = 2; Court Room = 3");
//            time = Timestamp.valueOf("2002-11-11 13:00:00");
//            Sitting local = home.findByTimeCourtSiteRoomId(time, new Integer(2), new Integer(3));
//            assertEquals(1, local.getSittingId().intValue());
//
//            try
//            {
//                log.debug("Time = 2002-11-11 13:00:00; Court Site = 1; Court Room = 3");
//                local = home.findByTimeCourtSiteRoomId(time, new Integer(1), new Integer(3));
//            }
//            catch(ObjectNotFoundException e)
//            {
//                log.debug("ObjectNotFoundException is thrown");
//            }
//
//            try
//            {
//                log.debug("Time = 2002-11-11 13:00:00; Court Site = 2; Court Room = 2");
//                local = home.findByTimeCourtSiteRoomId(time, new Integer(2), new Integer(2));
//            }
//            catch(ObjectNotFoundException e)
//            {
//                log.debug("ObjectNotFoundException is thrown");
//            }
//
//            try
//            {
//                log.debug("Time = 2002-11-11 11:00:00; Court Site = 2; Court Room = 3");
//                time = Timestamp.valueOf("2002-11-11 11:00:00");
//                local = home.findByTimeCourtSiteRoomId(time, new Integer(2), new Integer(3));
//            }
//            catch(ObjectNotFoundException e)
//            {
//                log.debug("ObjectNotFoundException is thrown");
//            }
//
//            try
//            {
//                log.debug("Time = 2002-11-11 12:00:00; Court Site = 2; Court Room = 3");
//                time = Timestamp.valueOf("2002-11-11 12:00:00");
//                local = home.findByTimeCourtSiteRoomId(time, new Integer(2), new Integer(3));
//            }
//            catch(ObjectNotFoundException e)
//            {
//                log.debug("ObjectNotFoundException is thrown");
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug("testFindByTimeCourtSiteRoomId() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    private SittingHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (SittingHome) ctx.lookup("SittingHome");
//        return (SittingHome) PortableRemoteObject.narrow(home, SittingHome.class);
//    }
//
//}*/