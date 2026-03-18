//package uk.gov.courtservice.business.services.witness.test;
//
//import javax.naming.NamingException;
//
//import junit.framework.Assert;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
//
//
//public class TestSkeletonSchedule
//    extends AbstractTestClass {
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      SkeletonControllerHome home;
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      SkeletonController instance;
//  private Logger log = CSServices.getLogger(TestSkeletonSchedule.class);
//  private SkeletonSchedule skeletonSchedule; // test against interface
//
//  public TestSkeletonSchedule(String s) throws NamingException {
//    super(s);
//    home = (SkeletonControllerHome)
//        CSServices.getServiceLocator().getRemoteHome(SkeletonControllerHome.class);
//  }
//
//  public void setUp() throws Exception {
//    super.setUp();
//    instance = home.create();
//    try {
//      skeletonSchedule = instance.getSkeletonSchedule(NEW_CASE_ID);
//    }
//    catch (Exception e) {
//
//    }
//    if (skeletonSchedule == null) {
//      skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, false);
//    }
//  }
//
//  public void testGetTrialSessions() {
//    try {
//      TrialSession[] trialSessions = skeletonSchedule.getTrialSessions();
//      if (trialSessions != null && trialSessions.length > 0) {
//        for (int i = 0; i < trialSessions.length; i++) {
//          TrialSession trialSession = trialSessions[i];
//          log.debug("Trial session " + i + " : " + trialSession);
//          assertNotNull("The Id is null", trialSession.getId());
//          assertTrue(
//              "Day And Session As Duration In Days returned by trialSession is not positive",
//              trialSession.getDayAndSessionAsDurationInDays() >= 0);
//          assertTrue("Week is not positive", trialSession.getWeek() >= 0);
//          assertTrue("Has witnesses should be either true or false",
//                     trialSession.hasWitnesses() == true ||
//                     trialSession.hasWitnesses() == false);
//          assertTrue("Day number is not positive",
//                     trialSession.getDayNumber() >= 0);
//          trialSession.update();
//        }
//      }
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testCreateTrialSession() {
//    try {
//      TrialSession trialSession = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.AFTERNOON);
//
//      assertNotNull("TrialSession is null", trialSession);
//      assertNotNull("Trial Session Id is null", trialSession.getId());
//      log.debug("Created Trial Session with ID: " + trialSession.getId());
//      log.debug(trialSession);
//      assertNotNull("Trial Session Id is null", trialSession.getSessionType());
//      assertEquals("Wrong day for TrialSession", DAY.intValue(),
//                   trialSession.getDayNumber());
//      assertEquals("Wrong session for TrialSession", TrialSession.AFTERNOON,
//                   trialSession.getSessionType());
//      trialSession.remove(true); // remove trial session even if there are witnesses.
//
//      java.util.Date date = new java.util.Date();
//
//      trialSession = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING, date);
//
//      assertNotNull("TrialSession is null", trialSession);
//      assertNotNull("Trial Session Id is null", trialSession.getId());
//      log.debug("Created Trial Session with ID: " + trialSession.getId());
//      log.debug(trialSession);
//      assertNotNull("Trial Session Id is null", trialSession.getSessionType());
//      assertEquals("Wrong day for TrialSession", DAY.intValue(),
//                   trialSession.getDayNumber());
//      assertEquals("Wrong session for TrialSession", TrialSession.MORNING,
//                   trialSession.getSessionType());
//      trialSession.remove(true);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testGetTrialSessionsIncludingDummyValuesForDuration() {
//    try {
//      float estimatedTime = 12.5f;
//      TrialSession[] trialSessions = skeletonSchedule.
//          getTrialSessionsIncludingDummyValuesForDuration(estimatedTime);
//      if (trialSessions != null && trialSessions.length > 0) {
//        for (int i = 0; i < trialSessions.length; i++) {
//          TrialSession trialSession = trialSessions[i];
//          log.debug("Trial session " + i + " : " + trialSession);
//          assertTrue(
//              "Day And Session As Duration In Days returned by trialSession is not positive",
//              trialSession.getDayAndSessionAsDurationInDays() >= 0);
//          assertTrue("Week is not positive", trialSession.getWeek() >= 0);
//          assertTrue(
//              "Day And Session As Duration In Days returned by trialSession is not positive",
//              trialSession.getDayAndSessionAsDurationInDays() >= 0);
//          assertTrue("Day number is not positive",
//                     trialSession.getDayNumber() >= 0);
//          assertTrue(
//              "Session should be less than or equal to estimated duration",
//              trialSession.getDayAndSessionAsDurationInDays() <=
//              estimatedTime);
//
//          log.debug("****************************** THIS TEST FAILS HERE 23/02/04 Jide Fakoya ********************************************* ");
//
//          assertTrue("Has witnesses should be either true or false",
//                     trialSession.hasWitnesses() == true ||
//                     trialSession.hasWitnesses() == false);
//
//          trialSession.update();
//        }
//      }
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testGetCaseDetail() {
//    try {
//
//      CaseDetail caseDetail = skeletonSchedule.getCaseDetail();
//      assertNotNull("CaseDetails is null", caseDetail);
//      assertNotNull("Case Details Id is null", caseDetail.getId());
//      assertEquals("Wrong case Id returned by CaseDetail", caseDetail.getId(),
//                   NEW_CASE_ID);
//      assertNotNull("Court Code returned by CaseDetail is null",
//                    caseDetail.getCourtCode());
//      assertTrue("Estimated Case Duration is negative", caseDetail.getEstimatedCaseDuration()>=0);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testGetCaseId() {
//    try {
//      Integer caseId = skeletonSchedule.getCaseId();
//      assertNotNull("Case Id returned by skeletonSchedule is null", caseId);
//      assertEquals("Wrong case Id returned", caseId, NEW_CASE_ID);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testGetDeliveryStatus() {
//    try {
//      String deliveryStatus = skeletonSchedule.getDeliveryStatus();
//      if (deliveryStatus != null && deliveryStatus.length() > 0) {
//        assertTrue("DeliveryStatus is greater than permissible string length",
//                   deliveryStatus.length() <= 20);
//      }
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testIsDeliverable() {
//    try {
//      Assert.assertTrue("isDeliverable should be either true or false",
//                        skeletonSchedule.isDeliverable() == true ||
//                        skeletonSchedule.isDeliverable() == false);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testIsIssued() {
//    try {
//      Assert.assertTrue("isIssued should be either true or false",
//                        skeletonSchedule.isIssued() == true ||
//                        skeletonSchedule.isIssued() == false);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testIssue() {
//    try {
//      skeletonSchedule.issue();
//      Assert.assertTrue("The skeletonSchedule has been issued, skeletonSchedule.isIssued should return true",
//                        skeletonSchedule.isIssued() == true);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testSetDeliverable() {
//    try {
//      skeletonSchedule.setDeliverable(true);
//      Assert.assertTrue("The skeletonSchedule has been set to deliverable, skeletonSchedule.IsDeliverable() should return true",
//                        skeletonSchedule.isDeliverable() == true);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testSetDeliveryStatus() {
//    try {
//      skeletonSchedule.setDeliveryStatus("READY");
//      Assert.assertNotNull("Get delivery status returned null",
//                           skeletonSchedule.getDeliveryStatus());
//      Assert.assertTrue(
//          "The Delivery Status returned by skeletonSchedule.getDeliveryStatus() is incorrect",
//          skeletonSchedule.getDeliveryStatus().equals("READY"));
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//
//  public void testGetId() {
//
//    try {
//      Integer Id = skeletonSchedule.getId();
//      assertNotNull("Id returned by skeletonSchedule is null", Id);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//  }
//}
//