//package uk.gov.courtservice.xhibit.test.business.entities.scheduledhearing;
//
//// jdk
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Vector;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
//
//
//public class ScheduledHearingLinkTest extends TestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  private ScheduledHearingHome home = (ScheduledHearingHome)CSServices.getServiceLocator().getLocalHome(ScheduledHearingHome.class);
//  private Integer linkedShId = new Integer(2);
//
//  public ScheduledHearingLinkTest(String s)
//  {
//      super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//    String linkedShIdUpdate = "update xhb_scheduled_hearing set linked_sh_id = " + linkedShId + " where scheduled_hearing_id in (1, 3, 10, 11)";
//    TestUtils.execSql(linkedShIdUpdate);
//  }
//
//  protected void tearDown()throws Exception
//  {
//    String resetLinkedShIds = "update xhb_scheduled_hearing set linked_sh_id = null";
//    TestUtils.execSql(resetLinkedShIds);
//  }
//
//  public void testFindByLinkedSchedHearingId() throws Exception
//  {
//    log("testFindByLinkedSchedHearingId() start");
//    try
//    {
//      // try a valid linkedShId
//      Collection schedHearings = home.findByLinkedSchedHearingId(linkedShId);
//      assertEquals(schedHearings.size(), 4);
//
//      // collect the schedHearingIds from these scheduled hearings in a vector
//      Vector schedHearingIds = new Vector();
//      Iterator it = schedHearings.iterator();
//      while (it.hasNext())
//      {
//        ScheduledHearing schedHearing = (ScheduledHearing)it.next();
//        schedHearingIds.add(schedHearing.getScheduledHearingId());
//      }
//
//      assertTrue(schedHearingIds.contains(new Integer(1)));
//      assertTrue(schedHearingIds.contains(new Integer(3)));
//      assertTrue(schedHearingIds.contains(new Integer(10)));
//      assertTrue(schedHearingIds.contains(new Integer(11)));
//
//      // try an invalid linkedShId
//      Collection schedHearings2 = home.findByLinkedSchedHearingId(new Integer(999));
//      assertEquals(schedHearings2.size(), 0);
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        fail();
//    }
//  }
//
//  private void log(String msg)
//  {
//      log.debug(msg);
//  }
//}
//