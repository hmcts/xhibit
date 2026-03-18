//
//package uk.gov.courtservice.xhibit.test.business.services.caze;
//
//import java.rmi.RemoteException;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Iterator;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseController;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerHome;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseLoadingException;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.caze.SchedHearingLocationValue;
//import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
//import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;
//
//
//public class TestRemoteCaseControllerBean extends TestCase
//{
//  private CaseController controller;
//
//  public TestRemoteCaseControllerBean(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//    try
//    {
//      controller = (CaseController)CSServices.getEJBServices().
//                   createRemoteSession(CaseControllerHome.class);
//
//      TestUtils.execSql("update xhb_hearing_list set start_date = trunc(sysdate), end_date = trunc(sysdate) where list_id = 6");
//      TestUtils.execSql("update xhb_scheduled_hearing set not_before_time = trunc(sysdate), original_time = trunc(sysdate) where sitting_id in (select sitting_id from xhb_sitting where list_id = 6)");
//    }
//    catch( Exception e )
//    {
//      e.printStackTrace();
//    }
//  }
//
//  protected void tearDown()
//  {
//    try
//    {
//      TestUtils.execSql("update xhb_hearing_list set start_date = to_date('01/21/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), end_date = to_date('01/21/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM') where list_id = 6");
//      TestUtils.execSql("update xhb_scheduled_hearing set not_before_time = to_date('02/14/2003 12:57:20 AM', 'MM/DD/YYYY HH:MI:SS AM'), original_time = to_date('02/14/2003 12:57:20 AM', 'MM/DD/YYYY HH:MI:SS AM') where sitting_id in (select sitting_id from xhb_sitting where list_id = 6)");
//    }
//    catch( Exception e )
//    {
//      e.printStackTrace();
//    }
//  }
//
//  public void testGetScheduledHearingsForCase()
//  {
//    try
//    {
//      Integer caseID =  new Integer(6);
//      Collection collectionRet = controller.getScheduledHearings(caseID);
//      Iterator iteratorRet = collectionRet.iterator();
//      ScheduledHearingValue shv = (ScheduledHearingValue)iteratorRet.next();
//      assertEquals(shv.getScheduledHearingID(),new Integer(1));
//      // Test scheduledHearingDate by day then month then year
//      // assume shv.getScheduledHearingDate() = ORIGINAL_TIME in XHB_SCHEDULED_HEARING
//      // This is set to today in the setUp() method
//      Calendar today = Calendar.getInstance();
//      assertEquals(today.get(Calendar.DAY_OF_MONTH), shv.getScheduledHearingDate().get(Calendar.DAY_OF_MONTH));
//      assertEquals(today.get(Calendar.MONTH),(shv.getScheduledHearingDate().get(Calendar.MONTH)));
//      assertEquals(today.get(Calendar.YEAR), shv.getScheduledHearingDate().get(Calendar.YEAR));
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testGetScheduledHearingsByTypeNumberCourt()
//  {
//    try
//    {
//      String caseType = "A";
//      Integer caseNumber = new Integer(20020118);
//      Integer courtId = new Integer(1);
//      Collection collectionRet = controller.getScheduledHearings(caseType, caseNumber, courtId);
//      Iterator iteratorRet = collectionRet.iterator();
//      ScheduledHearingValue shv = (ScheduledHearingValue)iteratorRet.next();
//      assertEquals(shv.getScheduledHearingID(),new Integer(1));
//      // Test scheduledHearingDate by day then month then year
//      // assume shv.getScheduledHearingDate() = ORIGINAL_TIME in XHB_SCHEDULED_HEARING
//      // This is set to today in the setUp() method
//      Calendar today = Calendar.getInstance();
//      assertEquals(today.get(Calendar.DAY_OF_MONTH), shv.getScheduledHearingDate().get(Calendar.DAY_OF_MONTH));
//      assertEquals(today.get(Calendar.MONTH), (shv.getScheduledHearingDate().get(Calendar.MONTH)));
//      assertEquals(today.get(Calendar.YEAR), shv.getScheduledHearingDate().get(Calendar.YEAR));
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testFindCaseId()
//  {
//    String caseType = "A";
//    Integer caseNumber = new Integer(20020118);
//    Integer courtId = new Integer(1);
//
//    // try valid case values
//    try
//    {
//      Integer caseId = controller.findCaseId(caseType, caseNumber, courtId);
//      assertEquals(6, caseId.intValue());
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//
//    // try invalid case values
//    try
//    {
//      Integer caseId = controller.findCaseId("X", caseNumber, courtId);
//      fail();
//    }
//    catch (CaseControllerException e)
//    {
//      assertTrue(e instanceof CaseControllerException);
//      assertEquals("Could not find case for caseNumber 20020118, caseType X and courtId 1", e.getUserMessage());
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testGetDefendants()
//  {
//    try
//    {
//      Integer caseID =  new Integer(6);
//      Collection collectionRet = controller.getDefendants(caseID);
//      Iterator iteratorRet = collectionRet.iterator();
//      DefendantValue defendantRet = (DefendantValue)iteratorRet.next();
//      assertEquals(1, defendantRet.getCourtID().intValue());
//      assertEquals(28259, defendantRet.getCrestDefendantID().intValue());
//      assertEquals(1, defendantRet.getDateOfBirth().get(Calendar.DAY_OF_MONTH));
//      assertEquals(04, defendantRet.getDateOfBirth().get(Calendar.MONTH)+1); // MONTH has vos 0-11
//      assertEquals(2070, defendantRet.getDateOfBirth().get(Calendar.YEAR));
//      assertEquals("ZMWIVQ", defendantRet.getFirstName());
//      assertEquals(1, defendantRet.getGender().intValue());
//      assertEquals("A", defendantRet.getInitials());
//      assertNull(defendantRet.getLastConvictionDate());
//      assertEquals("", defendantRet.getMiddleName());
//      assertEquals("TFBVILE", defendantRet.getSurName());
//      DefendantOnCaseBasicValue docBasicValue = defendantRet.getDefOnCaseBasicValue();
//      assertNotNull(docBasicValue);
//      assertEquals(1, docBasicValue.getId().intValue());
//      assertEquals("N", docBasicValue.getIsJuvenile());
//      assertEquals("N", docBasicValue.getIsMasked());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testGetTodaysSchedHearingLocation()
//  {
//    try
//    {
//
//      // Try a valid case with schedHearing today
//      // Make sure the date is today
////      TestUtils testUtils = new TestUtils();
////      testUtils.execSql("update xhb_scheduled_hearing set original_time = sysdate where scheduled_hearing_id = 51115");
//
//      Integer caseID1 =  new Integer(6);
//      SchedHearingLocationValue schedHearingLocationValue = controller.getTodaysSchedHearingLocation(caseID1);
//
//      assertNotNull(schedHearingLocationValue);
//      assertEquals(schedHearingLocationValue.getCourtSiteID(),new Integer(1));
//      assertEquals(schedHearingLocationValue.getCourtRoomID(),new Integer(6));
//
//      // Reset the date to it's original value
////      testUtils.execSql("update xhb_scheduled_hearing set original_time = to_date( '10/10/2002', 'MM/DD/YYYY') where scheduled_hearing_id = 51115");
//
//      // Try an invalid case
//      Integer caseID3 =  new Integer(2);
//      SchedHearingLocationValue schedHearingLocation1  = controller.getTodaysSchedHearingLocation(caseID3);
//      assertNull(schedHearingLocation1);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  public void testRefreshLeaseTime()
//  {
//    System.out.println("____________________________________________________________");
//    System.out.println("testRefreshLeaseTime()");
//
//    try
//    {  Integer caseID = new Integer(1);
//       CaseAccessValue val = controller.refreshLeaseTime( caseID );
//       Integer retCaseID = val.getCaseId();
//       assertNotNull(retCaseID);
//       assertEquals(caseID.intValue(), retCaseID.intValue());
//    }
//    catch( CaseLoadingException e ){ System.out.println("Pass");}
//    catch( Exception e )
//    {  System.out.println("ERROR: ");
//      // e.printStackTrace();
//       fail(e.toString());
//    }
//
//    //business delegate
//    try
//   {
//      Integer caseID = new Integer(1);
//      CaseAccessValue val = controller.refreshLeaseTime( caseID );
//      Integer retCaseID = val.getCaseId();
//      assertNotNull(retCaseID);
//      assertEquals(caseID.intValue(), retCaseID.intValue());
//   }
//   catch( CaseLoadingException e ){ System.out.println("Pass");}
//   catch( Exception e )
//   {  System.out.println("ERROR: ");
//     // e.printStackTrace();
//      fail(e.toString());
//    }
//
//  }
//
//  public void testOpenCase()
//  {
//    System.out.println("____________________________________________________________");
//    System.out.println("testOpenCase()");
//
//    try
//    {  Integer caseID = new Integer(1);
//       CaseAccessValue val = controller.openCase( caseID );
//       Integer retCaseID = val.getCaseId();
//       assertNotNull(retCaseID);
//       assertEquals(caseID.intValue(), retCaseID.intValue());
//    }
//    catch( NullPointerException e ){ //this is the error thrown by the integration server: remove this later
//      System.out.println("Null pointer exception thrown");
//    }
//    catch( Exception e )
//    {  System.out.println("ERROR: ");
//       e.printStackTrace();
//       fail( e.toString());
//    }
//
//    //busines delegate
//    try
//    {
//       Integer caseID = new Integer(1);
//       CaseAccessValue val = controller.openCase( caseID );
//       Integer retCaseID = val.getCaseId();
//       assertNotNull(retCaseID);
//       assertEquals(caseID.intValue(), retCaseID.intValue());
//    }
//    catch( NullPointerException e ){ //this is the error thrown by the integration server: remove this later
//      System.out.println("Null pointer exception thrown");
//    }
//    catch( Exception e )
//    {  System.out.println("ERROR: ");
//       e.printStackTrace();
//       fail( e.toString());
//    }
//  }
//
//  public void testCheckCaseAccess()
//  {
//    System.out.println("____________________________________________________________");
//    System.out.println("testCheckCaseAccess()");
//
//    try
//    {  Integer caseID = new Integer(1);
//       CaseAccessValue val = controller.checkCaseAccess( caseID );
//       Integer retCaseID = val.getCaseId();
//       assertNotNull(retCaseID);
//       assertEquals(caseID.intValue(), retCaseID.intValue());
//
//
//       //business delegate
//       caseID = new Integer(1);
//       val = controller.checkCaseAccess( caseID );
//       retCaseID = val.getCaseId();
//       assertNotNull(retCaseID);
//       assertEquals(caseID.intValue(), retCaseID.intValue());
//
//    }
//    catch( Exception e )
//    {  System.out.println("ERROR: ");
//       e.printStackTrace();
//       fail();
//     }
//  }
//
//
//}
//