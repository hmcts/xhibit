//package uk.gov.courtservice.business.services.witness.test;
//
//import junit.framework.Assert;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.reference.
//    interfaces.WitnessReferenceData;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.
//    SkeletonScheduleFactory;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.
//    SkeletonSchedule;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.
//    TrialSession;
//
//import java.util.Date;
//import javax.naming.NamingException;
//import uk.gov.courtservice.xhibit.business.services.witness.
//    SkeletonControllerHome;
//
//public class TestTrialSession
//    extends AbstractTestClass {
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      SkeletonControllerHome home;
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      SkeletonController instance;
//  private Logger log = CSServices.getLogger(TestTrialSession.class);
//  private SkeletonSchedule skeletonSchedule;
//  private WitnessReferenceData referenceData;
//
//  private static final String Notes =
//      "fsdfdsfsafsafdasfdsafdsafsdafdsafsdafdsafdsa";
//  private static final int REMOVED = 4;
//
//  // Should use DAY from AbstarctTestClass but DAY is set 1 and session already exists
//  private static final Integer DAY = new Integer(20);
//
//  public TestTrialSession(String s) throws NamingException {
//    super(s);
//
//    home = (SkeletonControllerHome)
//        CSServices.getServiceLocator().getRemoteHome(SkeletonControllerHome.class);
//  }
//
//  public void setUp() throws Exception {
//    super.setUp();
//    skeletonSchedule = SkeletonScheduleFactory.getInstance().
//        getSkeletonSchedule(CASE_ID);
//
//  }
//
//  public void testUpdate() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      Date now = new Date();
//      session.setAppearanceDate(now);
//      session.update();
//      session = skeletonSchedule.getTrialSession(DAY,
//                                                 TrialSession.MORNING);
//      Assert.assertEquals("Persisted version has not been modified.",
//                          now.getTime() / 1000,
//                          session.getAppearanceDate().getTime() / 1000);
//
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testGetId() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      Assert.assertNotNull("The Id is null", session.getId());
//
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//  }
//
//  public void testGetDayNumber() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      session.setDayNumber(WITNESS_SESSION_DAY_NUMBER);
//
//      Assert.assertEquals("The DayNumber is not equal", session.getDayNumber(),
//                          WITNESS_SESSION_DAY_NUMBER);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//  }
//
//  public void testGetAppearanceDate() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      Date now = new Date();
//      session.setAppearanceDate(now);
//      Assert.assertEquals("The Appearance date returned by session.getAppearanceDate() is not equal to date set by session.setAppearanceDate() ",
//                          session.getAppearanceDate(), now);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testGetSessionType() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      Assert.assertEquals("The session type is incorrect",
//                          session.getSessionType(), TrialSession.MORNING);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testSetSessionType() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      session.setSessionType(TrialSession.AFTERNOON);
//      Assert.assertEquals("Set for session type is incorrect",
//                          session.getSessionType(), TrialSession.AFTERNOON);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testGetNotes() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.MORNING);
//      session.setNotes(Notes);
//      Assert.assertEquals("Set/Get for session notes is incorrect",
//                          session.getNotes(), Notes);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testSetNotes() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.AFTERNOON);
//      session.setNotes(Notes);
//      Assert.assertEquals("Set/Get for session notes is not correct",
//                          session.getNotes(), Notes);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testHasWitnesses() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.AFTERNOON);
//      Assert.assertTrue("hasWitness should be either true or false",
//                        session.hasWitnesses() == true ||
//                        session.hasWitnesses() == false);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testGetDayAndSessionAsDurationInDays() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.AFTERNOON);
//      assertTrue(
//          "Day And Session As Duration In Days returned by trialSession is not positive",
//          session.getDayAndSessionAsDurationInDays() >= 0);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testRemove() {
//    try {
//      TrialSession session = skeletonSchedule.createTrialSession(DAY,
//          TrialSession.AFTERNOON);
//      Assert.assertTrue("The trial session has been removed", session.getMetaState() != REMOVED);
//      session.remove(true);
//      Assert.assertTrue("The trial session has not been removed", session.getMetaState() == REMOVED);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//}