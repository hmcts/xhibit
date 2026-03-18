//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
///*
// *
// *  CREATED THIS FILE BECASUE OF PROBLEMS WITH SCHEDULEWORKFLOWTEST.JAVA  ....
// *
// *
// */
//
//// JDK
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleWorkFlow;
//
///**
// *
// *
// * CREATED THIS FILE BECASUE OF PROBLEMS WITH SCHEDULEWORKFLOWTEST.JAVA  ....
// *
// *
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author unascribed
// * @version 1.0
// */
//public class ScheduleWorkFlow2Test extends TestCase {
//
//    private InitialContext initContext;
//    private UserTransaction ut = null;
//    private static Logger log = CSServices.getLogger(ScheduleWorkFlow2Test.class);
//
//    public ScheduleWorkFlow2Test(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        try
//        {
//            initContext = new InitialContext();
//            ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//        }
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testUpdateHearingProgress()
//    {
//        try
//        {
//            ut.begin();
//            ScheduleWorkFlow scheduleworkflow = new ScheduleWorkFlow();
//            scheduleworkflow.updateHearingProgress(new Integer(1), new Integer(9));
//            ut.commit();
//            ScheduledHearingMaintainer shm = new ScheduledHearingMaintainer();
//            ScheduledHearing sh = shm.findByPK(new Integer(1));
//
//            assertEquals( new Integer(9), sh.getHearingProgress());
//        }
//        catch (Exception e)
//        {
//            fail(e.toString());
//        }
//    }
//
//
//
////    public void testAddBenchWarrantHearing() {
////        ScheduleWorkFlow scheduleworkflow = new ScheduleWorkFlow();
////        Integer caseNo1=  null  /** @todo fill in non-null value */;
////        String caseType2=  "STRING0";
////        Integer courtId3=  null  /** @todo fill in non-null value */;
////        Integer courtRoomId4=  null  /** @todo fill in non-null value */;
////        Collection defendants5=  null  /** @todo fill in non-null value */;
////        Date notBeforeTime6=  null  /** @todo fill in non-null value */;
////        try {
////            BWHearingValue bWHValue = new BWHearingValue();
////            bWHValue.setCaseNumber( caseNo1 );
////            bWHValue.setCaseType( caseType2 );
////            bWHValue.setCourtId( courtId3 );
////            bWHValue.setCourtRoomId( courtRoomId4 );
////            bWHValue.setDefendants( defendants5 );
////            bWHValue.setNotBeforeTime( notBeforeTime6 );
////
////            scheduleworkflow.addBenchWarrantHearing( bWHValue );
////
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////        }
////        catch(Exception e) {
////            System.err.println("Exception thrown:  "+e);
////        }
////    }
//
//
////    public void testGetCaseAndSittingDetails() {
////        ScheduleWorkFlow scheduleworkflow = new ScheduleWorkFlow();
////        Integer caseNumber1=  null  /** @todo fill in non-null value */;
////        String caseType2=  "STRING0";
////        Integer courtId3=  null  /** @todo fill in non-null value */;
////        Integer courtRoomId4=  null  /** @todo fill in non-null value */;
////        Date sittingTime5=  null  /** @todo fill in non-null value */;
////        try {
////            scheduleworkflow.getCaseAndSittingDetails(caseNumber1, caseType2, courtId3, courtRoomId4, sittingTime5);
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////        }
////        catch(Exception e) {
////            System.err.println("Exception thrown:  "+e);
////        }
////    }
//
////    public void testGetCaseDetails() {
////
////        log.debug("testGetCaseDetails called");
////        ScheduleWorkFlow scheduleworkflow = new ScheduleWorkFlow();
////        Integer caseNumber1= new Integer(20020955)  /** @todo fill in non-null value */;
////        String caseType2=  "T";
////        Integer courtId3=  new Integer(1)  /** @todo fill in non-null value */;
////        try {
////            CaseInfoValue civ = scheduleworkflow.getCaseDetails(caseNumber1, caseType2, courtId3);
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            assertEquals(caseNumber1, civ.getCaseBasicValue().getCaseNumber() );
////            assertEquals(0, civ.getDefendants().size());
////        }
////        catch(Exception e) {
////            System.err.println("Exception thrown:  "+e);
////        }
////    }
//
////    public void testGetSittingDetails() {
////
////        log.debug("testGetSittingDetails called");
////        ScheduleWorkFlow scheduleworkflow = new ScheduleWorkFlow();
////        GregorianCalendar gc = new GregorianCalendar(2002, Calendar.FEBRUARY, 8, 10, 15);
////        Integer courtRoomId1=  new Integer(6)  /** @todo fill in non-null value */;
////        Date sittingTime2=  gc.getTime()  /** @todo fill in non-null value */;
////        try {
////            SittingInfoValue siv = scheduleworkflow.getSittingDetails(courtRoomId1, sittingTime2);
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            assertNotNull(siv);
////            //assertEquals(courtRoomId1, siv.getSittingBasicValue().getCourtRoomID());
////        }
////        catch(Exception e) {
////            fail (e.toString() );
////            System.err.println("Exception thrown:  "+e);
////        }
////    }
//
//}