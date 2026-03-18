//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//// j2ee
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//// thirdparty
//import junit.framework.Assert;
//import junit.framework.TestCase;
//
//// xhibit
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule.ScheduleWorkFlow;
//import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
//
//public class ScheduleWorkFlow3Test extends TestCase
//{
//  private InitialContext initContext;
//  private UserTransaction ut = null;
//
//  public ScheduleWorkFlow3Test(String s)
//  {
//      super(s);
//  }
//
//  protected void setUp()
//  {
//    try
//    {
//        initContext = new InitialContext();
//        ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//    }
//    catch (Exception e)
//    {
//        e.printStackTrace();
//    }
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testGetScheduledHearings()
//  {
//    ScheduleWorkFlow scheduleWorkflow = new ScheduleWorkFlow();
//    Integer schedHearingIds[] = new Integer[2];
//    schedHearingIds[0] = new Integer(1);
//    schedHearingIds[1] = new Integer(2);
//    try
//    {
//      ut.begin();
//      ScheduledHearingValue scheduledhearingvalueRet[] = scheduleWorkflow.getScheduledHearings(schedHearingIds);
//      ScheduledHearingValue shv1 = scheduledhearingvalueRet[0];
//      ScheduledHearingValue shv2 = scheduledhearingvalueRet[1];
//      Assert.assertEquals(6, shv1.getCaseId().intValue());
//      Assert.assertEquals(7, shv2.getCaseId().intValue());
//      Assert.assertEquals(6, shv1.getCourtRoomId().intValue());
//      Assert.assertEquals(6, shv2.getCourtRoomId().intValue());
//      Assert.assertEquals(0, shv1.getHearingProgress().intValue());
//      Assert.assertEquals(0, shv2.getHearingProgress().intValue());
//      ut.commit();
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        Assert.fail();
//    }
//  }
//}