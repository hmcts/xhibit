//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//// JDK
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Date;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.CaseInfoValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.SittingInfoValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.AddHearingValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
//
//public class HearingScheduleControllerBeanTest extends TestCase
//{
//
//    private HearingScheduleController hearingScheduleController;
//    private static Logger log = CSServices.getLogger(HearingScheduleControllerBeanTest.class);
//
//    public HearingScheduleControllerBeanTest(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        try
//        {
//            log.debug("setUp() called");
//            Process p = Runtime.getRuntime().exec("sqlplus exhibit/exhibit@ora9dev @D:/testSql.sql");
//
//            InputStream in= p.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            while ( true ) {
//                String str = br.readLine();
//                if ( str == null )
//                    break;
//                log.debug(str);
//            }
//            log.debug( "setUp :: SQL completed");
//            int result = p.waitFor();
//            log.debug("setup :: Result of sql: " + result);
//
//            HearingScheduleController hearingScheduleController = (HearingScheduleController)CSServices.getEJBServices().createLocalSession(HearingScheduleControllerHome.class);
//        }
//        catch (Exception e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//
//    protected void tearDown() {
//        log.debug("tearDown() called");
//    }
//
//    public void testAddHearing() {
//        log.debug("testAddBenchWarrantHearing() called");
//        AddHearingValue newBWHValue1=  this.createTestAddHearingValue();
//        try {
//            hearingScheduleController.addHearing(newBWHValue1);
//            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        }
//        catch(Exception e) {
//            fail("Exception thrown:  "+ e.toString());
//        }
//    }
//
//    public void testGetCaseDetails() {
//        log.debug("testGetCaseDetails() called");
//        Integer caseNumber1=  null  /** @todo fill in non-null value */;
//        String caseType2=  "STRING0";
//        Integer courtId3=  null  /** @todo fill in non-null value */;
//
//        String caseSubType = "TEST SUB-TYPE" /** @todo fill in test value */;
//        try {
//            CaseInfoValue caseinfovalueRet = hearingScheduleController.getCaseDetails(caseNumber1, caseType2, courtId3);
//
//            assertEquals( caseNumber1, caseinfovalueRet.getCaseBasicValue().getCaseNumber());
//            assertEquals( caseSubType, caseinfovalueRet.getCaseBasicValue().getCaseSubType() );
//        }
//        catch(Exception e) {
//            System.err.println("Exception thrown:  "+ e);
//            fail("Exception thrown:  "+ e.toString());
//        }
//    }
//
//    public void testGetSittingDetails() {
//        log.debug("testGetSittingDetails() called");
//        Integer courtRoomId1=  null  /** @todo fill in non-null value */;
//        Date sittingTime2=  null  /** @todo fill in non-null value */;
//        try {
//            SittingInfoValue sittinginfovalueRet = hearingScheduleController.getSittingDetails(courtRoomId1, sittingTime2);
//
//            assertEquals( sittingTime2, sittinginfovalueRet.getSittingBasicValue().getSittingTime());
//        }
//        catch(Exception e) {
//            System.err.println("Exception thrown:  "+ e);
//            fail("Exception thrown:  "+ e.toString());
//        }
//    }
//
//
//    public void testMoveCase() {
//        log.debug("testMoveCase() called");
//        MoveCaseValue mCValue1=  null  /** @todo fill in non-null value */;
//        try {
//            hearingScheduleController.moveCase(mCValue1);
//            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        }
//        catch(Exception e) {
//            System.err.println("Exception thrown:  "+ e);
//            fail("Exception thrown:  "+ e.toString());
//        }
//    }
//
//    private AddHearingValue createTestAddHearingValue()
//    {
//        AddHearingValue addHearingValue = new AddHearingValue();
//
//        Integer testCaseNumber = null;
//        String  testCaseType = "STRING0";
//        Integer testCourtId = null;
//        Integer testCourtRoomId = null;
//        Date    testNotBeforeTime = new Date();
//
//        addHearingValue.setCaseNumber(testCaseNumber);
//        addHearingValue.setCaseType(testCaseType);
//        addHearingValue.setCourtId(testCourtId);
//        addHearingValue.setCourtRoomId(testCourtRoomId);
//        /** @todo Complete the defendants */
////        addHearingValue.setRefHearingTypeId();
//        /** @todo Complete the defendants */
//        //bwHearingValue.setDefendants();
//        addHearingValue.setNotBeforeTime(testNotBeforeTime);
//
//        return addHearingValue;
//
//    }
//}
//