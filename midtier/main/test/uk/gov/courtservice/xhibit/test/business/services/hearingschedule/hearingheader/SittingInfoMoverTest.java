//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingheader;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader.SittingInfoMover;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
//import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
//import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
//import java.util.Collection;
//import java.util.Iterator;
//
//
//public class SittingInfoMoverTest extends TestCase
//{
//
//  private InitialContext initContext;
//  private UserTransaction ut = null;
//
//  public SittingInfoMoverTest(String s)
//  {
//    super(s);
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
//    try {
//      TestUtils.execSql("delete from XHB_SCHED_HEARING_ATTENDEE where SH_ATTENDEE_ID NOT IN (4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,29,30,31,32)");
//      TestUtils.execSql("delete from XHB_SH_JUSTICE where SH_JUSTICE_ID NOT IN (1,2,3,4)");
//    } catch(java.lang.Exception jle) {
//      System.out.println("oops " + jle);
//    }
//  }
//
//  public void testMoveSittingInfo()
//  {
//    try
//    {
//      ut.begin();
//      SittingInfoMover sittinginfomover = new SittingInfoMover();
//      Integer shID1 =  new Integer(12);  /** @todo fill in non-null value */;
//      sittinginfomover.moveSittingInfo(shID1);
//      ut.commit();
//
//      SchedHearingAttendeeMaintainer attendeeMaintainer = new SchedHearingAttendeeMaintainer();
//      ScheduledHearingMaintainer shmaintainer = new ScheduledHearingMaintainer();
//      SittingMaintainer sittingMaintainer = new SittingMaintainer();
//      ShJusticeMaintainer justiceMaintainer = new ShJusticeMaintainer();
//
//      ScheduledHearing sh = shmaintainer.findByPK(new Integer(12));
//      Sitting s = sittingMaintainer.findByPK(sh.getSittingId());
//      Collection attendees = attendeeMaintainer.findByScheduledHearingId(new Integer(12));
//
//      assertTrue("must have attendess", !attendees.isEmpty());
//
//      /** find judge record */
//      boolean judgeFoundAndCorrect = false;
//
//      for(Iterator i = attendees.iterator(); i.hasNext() && !judgeFoundAndCorrect; ) {
//        SchedHearingAttendee next = (SchedHearingAttendee)i.next();
//
//        if(next.getAttendeeType().equals("J")) {
//          assertEquals("ref judge id not set correctly", next.getRefJudgeId(), s.getRefJudgeId());
//          judgeFoundAndCorrect = true;
//          i.remove();
//        }
//      }
//
//      assertTrue("must have a just setup", judgeFoundAndCorrect);
//
//      boolean justicesOK = false;
//      boolean justice1OK = false;
//      boolean justice2OK = false;
//      boolean justice3OK = false;
//      boolean justice4OK = false;
//
//      /** far to difficult to check the correctness of the justices as we
//       *  don't have any knowledge of which shJusticeID == justice1 etc..
//       * this is because the id or the name can exist on the sitting instead
//       * of definately the id
//       */
//
//      if(s.getRefJustice1Id() != null || s.getJusticeName1() != null) {
//        for(Iterator i = attendees.iterator(); i.hasNext() && !justice1OK; ) {
//          SchedHearingAttendee next = (SchedHearingAttendee)i.next();
//
//          if(next.getAttendeeType() == "JP") {
//            assertEquals("ref judge id not set correctly", next.getRefJudgeId(), s.getRefJudgeId());
//
//            ShJustice shj = justiceMaintainer.findByPrimaryKey(next.getShJusticeId());
//
//            assertTrue("hearing id not set correctly", shj.getHearingId().equals(sh.getHearingId()));
//            justice1OK = true;
//            i.remove();
//          } else {
//            justice1OK = true;
//          }
//        }
//      } else {
//        justice1OK = true;
//      }
//
//      if(s.getRefJustice2Id() != null || s.getJusticeName2() != null) {
//        for(Iterator i = attendees.iterator(); i.hasNext() && !justice2OK; ) {
//          SchedHearingAttendee next = (SchedHearingAttendee)i.next();
//
//          if(next.getAttendeeType() == "JP") {
//            assertEquals("ref judge id not set correctly", next.getRefJudgeId(), s.getRefJudgeId());
//
//            ShJustice shj = justiceMaintainer.findByPrimaryKey(next.getShJusticeId());
//
//            assertTrue("hearing id not set correctly", shj.getHearingId().equals(sh.getHearingId()));
//            justice2OK = true;
//            i.remove();
//          } else {
//            justice2OK = true;
//          }
//        }
//      } else {
//        justice2OK = true;
//      }
//
//      if(s.getRefJustice3Id() != null || s.getJusticeName3() != null) {
//        for(Iterator i = attendees.iterator(); i.hasNext() && !justice3OK; ) {
//          SchedHearingAttendee next = (SchedHearingAttendee)i.next();
//
//          if(next.getAttendeeType() == "JP") {
//            assertEquals("ref judge id not set correctly", next.getRefJudgeId(), s.getRefJudgeId());
//
//            ShJustice shj = justiceMaintainer.findByPrimaryKey(next.getShJusticeId());
//
//            assertTrue("hearing id not set correctly", shj.getHearingId().equals(sh.getHearingId()));
//            justice3OK = true;
//            i.remove();
//          } else {
//            justice3OK = true;
//          }
//        }
//      } else {
//        justice3OK = true;
//      }
//
//      if(s.getRefJustice4Id() != null || s.getJusticeName4() != null) {
//        for(Iterator i = attendees.iterator(); i.hasNext() && !justice4OK; ) {
//          SchedHearingAttendee next = (SchedHearingAttendee)i.next();
//
//          if(next.getAttendeeType() == "JP") {
//            assertEquals("ref judge id not set correctly", next.getRefJudgeId(), s.getRefJudgeId());
//
//            ShJustice shj = justiceMaintainer.findByPrimaryKey(next.getShJusticeId());
//
//            assertTrue("hearing id not set correctly", shj.getHearingId().equals(sh.getHearingId()));
//            justice4OK = true;
//            i.remove();
//          } else {
//            justice4OK = true;
//          }
//        }
//      } else {
//        justice4OK = true;
//      }
//
//      justicesOK = justice1OK && justice2OK  && justice3OK  && justice4OK;
//
//      assertTrue("need to find judge record + relevant justice records", judgeFoundAndCorrect && justicesOK);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//  }
//}