//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
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
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;
//
//
///**
// * <p>Title: </p>
// * <p>Description: This test must be run locally as it makes direct use of maintiners
// * and entities to test results</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Sarah Tong
// * @version $Id: LinkHearingMethodsTest.java,v 1.6 2006/07/11 14:16:58 xzfdtb Exp $
// */
//public class LinkHearingMethodsTest extends TestCase
//{
//  Logger log = CSServices.getLogger( LinkHearingMethodsTest.class );
//  HearingScheduleController controller = null;
//
//  public LinkHearingMethodsTest(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//    controller = (HearingScheduleController)CSServices.getEJBServices().
//                   createLocalSession(HearingScheduleControllerHome.class);
//
//    String createLinks =  "update xhb_scheduled_hearing set linked_sh_id = 4 where scheduled_hearing_id in (6, 10)";
//    String createLinks2 =  "update xhb_scheduled_hearing set linked_sh_id = 5 where scheduled_hearing_id in (8, 11, 12)";
//    String createLinks3 =  "update xhb_scheduled_hearing set linked_sh_id = 6 where scheduled_hearing_id in (16, 17)";
//    TestUtils.execSql(createLinks);
//    TestUtils.execSql(createLinks2);
//    TestUtils.execSql(createLinks3);
//  }
//
//  protected void tearDown() throws Exception
//  {
//    String resetLinks = "update xhb_scheduled_hearing set linked_sh_id = null";
//    TestUtils.execSql(resetLinks);
//    resetLinks = "update xhb_hearing set linked_hearing_id = null";
//    TestUtils.execSql(resetLinks);
//  }
//
//  public void testSuggestLinkCases()
//  {
//    // There are 9 other scheduled hearings in the same sitting as this one which
//    // should all be brought back as suggestions
//    Integer scheduledHearingId1 =  new Integer(1);
//
//    try
//    {
//      LinkSuggestionValue linkSuggestionValueRet = controller.suggestLinkCases(scheduledHearingId1);
//
//      // check the current case
//      CaseSchedHearingValue currentCase = linkSuggestionValueRet.getCurrentCase();
//      assertTrue(checkCaseSchedHearingValue(currentCase, 6, 20020118, "A", 0, 1));
//
//      // check the suggestions, can't check ordering as all test data has
//      // hearing_progress as 0, can't check filtering as all test data has
//      // linked_sh_id as null (these routines have been tested locally but
//      // can't be tested here without making the private methods public)
//      Collection allSuggestions = linkSuggestionValueRet.getAllSuggestions();
//      assertNotNull(allSuggestions);
//      assertEquals(allSuggestions.size(),9);
//      Iterator it = allSuggestions.iterator();
//      while (it.hasNext())
//      {
//        currentCase = (CaseSchedHearingValue)it.next();
//        switch ( currentCase.getScheduledHearingId().intValue() )
//        {
//          case 2:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 7, 20020119, "A", 0, 2));
//            break;
//          case 3:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 8, 20020645, "S", 0, 3));
//            break;
//          case 4:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 9, 20020955, "T", 0, 4));
//            break;
//          case 5:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 10, 20020956, "T", 0, 5));
//            break;
//          case 6:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 11, 20020957, "T", 0, 6));
//            break;
//          case 7:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 12, 20020958, "T", 0, 7));
//            break;
//          case 8:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 13, 20020959, "T", 0, 8));
//            break;
//          case 9:
//            assertTrue(checkCaseSchedHearingValue(currentCase, 14, 20020960, "T", 0, 9));
//            break;
//        }
//      }
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test linking a group of scheduled hearings, none of which are already linked
//   */
//  public void testLinkCasesNewGroup()
//  {
//    // the collection of CaseSchedHearingValues for the scheduled hearings to link
//    CaseSchedHearingValue[] caseSchedHearingValues = new CaseSchedHearingValue[3];
//
//    try
//    {
//      // the lead scheduled hearing, i.e. the one who's link id to use if it exists
//      Integer leadScheduledHearingId = new Integer(1);
//      Integer leadHearingId = null;
//
//      // call suggestLinkCases to get CaseSchedHearingValues for the scheduled hearings
//      // we want to link - can't just create them here as we do not know what the version
//      // number will be as it will be updated each time the test is run
//      LinkSuggestionValue linkSuggestionValue = controller.suggestLinkCases(new Integer(2));
//      Collection cshValues = linkSuggestionValue.getAllSuggestions();
//      Iterator it = cshValues.iterator();
//      int i = 0;
//      while (it.hasNext())
//      {
//        CaseSchedHearingValue cshValue = (CaseSchedHearingValue)it.next();
//        if (cshValue.getScheduledHearingId().equals(new Integer(1)) ||
//            cshValue.getScheduledHearingId().equals(new Integer(3)) ||
//            cshValue.getScheduledHearingId().equals(new Integer(4)))
//        {
//          // get the lead hearing id, we need this to check results later
//          if (cshValue.getScheduledHearingId().equals(new Integer(1)))
//            leadHearingId = cshValue.getShbValue().getHearingID();
//
//          caseSchedHearingValues[i] = cshValue;
//          i++;
//        }
//      }
//
//      controller.linkCases(caseSchedHearingValues, leadScheduledHearingId);
//      CaseSchedHearingValue[] linkedSchedHearings = controller.getLinkedSchedHearingsByShId(leadScheduledHearingId);
//      assertNotNull(linkedSchedHearings);
//      assertEquals(3, linkedSchedHearings.length);
//      Vector retSchedHearingIds = new Vector();
//      for ( int j = 0; j < 3; j++)
//      {
//        retSchedHearingIds.add(linkedSchedHearings[j].getScheduledHearingId());
//      }
//
//      assertTrue(retSchedHearingIds.contains(new Integer(1)));
//      assertTrue(retSchedHearingIds.contains(new Integer(3)));
//      assertTrue(retSchedHearingIds.contains(new Integer(4)));
//
//      // check also the link hearings which should have been created
//      HearingMaintainer hearingMaintainer = new HearingMaintainer();
//      assertNotNull(leadHearingId);
//
//      Hearing hearing = hearingMaintainer.findByPK(leadHearingId);
//      Integer linkHearingId = hearing.getLinkedHearingId();
//      assertNotNull(linkHearingId);
//
//      Collection hearings = hearingMaintainer.findByLinkedHearingId(linkHearingId);
//      Iterator it2 = hearings.iterator();
//      Vector hearingIds = new Vector();
//      while (it2.hasNext())
//      {
//        Integer hearingId = ((Hearing)it2.next()).getHearingId();
//        hearingIds.add(hearingId);
//      }
//
//      // hearings 4, 6 and 7 should have been linked
//      assertTrue(hearingIds.contains(new Integer(4)));
//      assertTrue(hearingIds.contains(new Integer(6)));
//      assertTrue(hearingIds.contains(new Integer(7)));
//      assertEquals(3,hearingIds.size());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test linking a group of scheduled hearings, two of which are already linked
//   * to other scheduled hearings
//   */
//  public void testLinkCasesExistingGroup()
//  {
//    // the collection of CaseSchedHearingValues for the scheduled hearings to link
//    CaseSchedHearingValue[] caseSchedHearingValues = new CaseSchedHearingValue[3];
//
//    // the lead scheduled hearing, i.e. the one who's link id to use if it exists
//    Integer leadScheduledHearingId = new Integer(6);
//    Integer leadHearingId = null;
//
//    try
//    {
//      // call suggestLinkCases to get CaseSchedHearingValues for the scheduled hearings
//      // we want to link - can't just create them here as we do not know what the version
//      // number will be as it will be updated each time the test is run
//      LinkSuggestionValue linkSuggestionValue = controller.suggestLinkCases(new Integer(2));
//      Collection cshValues = linkSuggestionValue.getAllSuggestions();
//      Iterator it = cshValues.iterator();
//      int i = 0;
//      while (it.hasNext())
//      {
//        CaseSchedHearingValue cshValue = (CaseSchedHearingValue)it.next();
//        if (cshValue.getScheduledHearingId().equals(new Integer(5)) ||
//            cshValue.getScheduledHearingId().equals(new Integer(6)) ||
//            cshValue.getScheduledHearingId().equals(new Integer(8)))
//        {
//          // get the lead hearing id, we need this to check results later
//          if (cshValue.getScheduledHearingId().equals(new Integer(6)))
//            leadHearingId = cshValue.getShbValue().getHearingID();
//
//          caseSchedHearingValues[i] = cshValue;
//          i++;
//        }
//      }
//
//      controller.linkCases(caseSchedHearingValues, leadScheduledHearingId);
//      CaseSchedHearingValue[] linkedSchedHearings = controller.getLinkedSchedHearingsByShId(leadScheduledHearingId);
//      assertNotNull(linkedSchedHearings);
//      assertEquals(linkedSchedHearings.length, 6);
//      Vector retSchedHearingIds = new Vector();
//      for ( int j = 0; j < 6; j++)
//      {
//        retSchedHearingIds.add(linkedSchedHearings[j].getScheduledHearingId());
//        assertEquals(4,linkedSchedHearings[j].getShbValue().getLinkedSHID().intValue());
//      }
//
//      assertTrue(retSchedHearingIds.contains(new Integer(5)));
//      assertTrue(retSchedHearingIds.contains(new Integer(6)));
//      assertTrue(retSchedHearingIds.contains(new Integer(8)));
//      assertTrue(retSchedHearingIds.contains(new Integer(10)));
//      assertTrue(retSchedHearingIds.contains(new Integer(11)));
//      assertTrue(retSchedHearingIds.contains(new Integer(12)));
//
//      // check also the link hearings which should have been created
//      HearingMaintainer hearingMaintainer = new HearingMaintainer();
//
//      assertNotNull(leadHearingId);
//
//      Hearing hearing = hearingMaintainer.findByPK(leadHearingId);
//      Integer linkHearingId = hearing.getLinkedHearingId();
//
//      assertNotNull(linkHearingId);
//
//      Collection hearings = hearingMaintainer.findByLinkedHearingId(linkHearingId);
//      Iterator it2 = hearings.iterator();
//      Vector hearingIds = new Vector();
//      while (it2.hasNext())
//      {
//        Integer hearingId = ((Hearing)it2.next()).getHearingId();
//        hearingIds.add(hearingId);
//      }
//
//      // hearings 9, 8, 11, 13, 14 and 15 should have been linked
//      assertTrue(hearingIds.contains(new Integer(9)));
//      assertTrue(hearingIds.contains(new Integer(8)));
//      assertTrue(hearingIds.contains(new Integer(11)));
//      assertTrue(hearingIds.contains(new Integer(13)));
//      assertTrue(hearingIds.contains(new Integer(14)));
//      assertTrue(hearingIds.contains(new Integer(15)));
//      assertEquals(6,hearingIds.size());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testUnlinkCase()
//  {
//    CaseSchedHearingValue cshValue;
//
//    try
//    {
//      // call suggestLinkCases to get CaseSchedHearingValue for the scheduled hearing
//      // we want to unlink - can't just create it here as we do not know what the version
//      // number will be as it will be updated each time the test is run
//      LinkSuggestionValue linkSuggestionValue = controller.suggestLinkCases(new Integer(2));
//      Collection cshValues = linkSuggestionValue.getAllSuggestions();
//      Iterator it = cshValues.iterator();
//      boolean tested = false;
//      while (it.hasNext())
//      {
//        CaseSchedHearingValue retCshValue = (CaseSchedHearingValue)it.next();
//        if (retCshValue.getScheduledHearingId().equals(new Integer(6)))
//        {
//          cshValue = retCshValue;
//          // unlink this scheduled hearing
//          controller.unLinkCase(cshValue.getShbValue());
//
//          // check this scheduled hearing is no longer linked
//          CaseSchedHearingValue[] linkedCshValues = controller.getLinkedSchedHearingsByShId(new Integer(6));
//          assertEquals(0, linkedCshValues.length);
//          tested = true;
//          break;
//        }
//      }
//      assertTrue(tested);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  //-----------------------------Private Methods------------------------------//
//
//  private boolean checkCaseSchedHearingValue(CaseSchedHearingValue cshv,
//                                             int caseId,
//                                             int caseNumber,
//                                             String caseType,
//                                             int hearingProgress,
//                                             int schedHearingId)
//  {
//    log.debug("checkCaseSchedHearingValue for schedHearingId " + schedHearingId);
//    assertNotNull(cshv);
//    assertEquals(caseId, cshv.getCaseId().intValue());
//    assertEquals(caseNumber, cshv.getCaseNumber().intValue());
//    assertEquals(caseType, cshv.getCaseType());
//    assertEquals(hearingProgress, cshv.getHearingProgress().intValue());
//    assertEquals(schedHearingId, cshv.getScheduledHearingId().intValue());
//    ScheduledHearingBasicValue schedHearngBasicVal = cshv.getShbValue();
//    assertEquals(schedHearingId, schedHearngBasicVal.getId().intValue());
//    return true;
//  }
//}
//