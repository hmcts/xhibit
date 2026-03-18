//package uk.gov.courtservice.business.services.witness.test;
//
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import junit.framework.Assert;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessHome;
//import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerHome;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonSessionNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessCreationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
//import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;
//
//
//public class TestWitnessControllerBean
//    extends AbstractTestClass {
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      WitnessControllerHome home;
//  private uk.gov.courtservice.xhibit.business.services.witness.
//      WitnessController instance;
//
//  public TestWitnessControllerBean(String s) throws NamingException {
//    super(s);
//    home = (WitnessControllerHome)
//        CSServices.getServiceLocator().getRemoteHome(WitnessControllerHome.class);
//  }
//
//  public void setUp() throws Exception {
//    super.setUp();
//    instance = home.create();
//  }
//
//  public void testGetDailyList() throws Exception
//  {
//    Date date = new Date();
//      DailyListWithWitness[] dailyList = instance.getDailyList(COURT_ID, date);
//      Assert.assertNotNull("The daily list is null", dailyList);
//      for (int i = 0; i < dailyList.length; i++) {
//        Assert.assertNotNull("Entry is null.", dailyList[i]);
//        Assert.assertTrue("Court room name empty.",
//                dailyList[i].getCourtRoomName().length() > 0);
//        log.debug(i + ":" + dailyList[i]);
//      }
//  }
//
//  public void testTerminalToCourtId() {
//    try {
//      Integer courtIdByTerminalName = instance.getCourtIdByTerminalName(
//          terminalName);
//      Assert.assertEquals("Returned incorrect court id.", COURT_ID_FOR_TERMINAL,
//                          courtIdByTerminalName);
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           TerminalFindException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (java.rmi.RemoteException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//  }
//
//  public void testGetWitnessDetails() {
//    try {
//      WitnessDetail witnessDetail = instance.getWitnessDetail(WITNESS_ID);
//      Assert.assertNotNull("Witness is null", witnessDetail);
//      Assert.assertEquals("Wrong witness id returned by witnessDetail.getId()",
//                          witnessDetail.getId(), WITNESS_ID);
//      Assert.assertNotNull("Witness type is null", witnessDetail.getType());
//      Assert.assertNotNull("Witness expected arrival time is null",
//                           witnessDetail.getExpected());
//    }
//    catch (WitnessNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (java.rmi.RemoteException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//  }
//
//  public void testUpdateWitnessDetails() {
//    try {
//      WitnessDetail witnessDetail = instance.getWitnessDetail(WITNESS_ID);
//      Assert.assertNotNull("Witness is null", witnessDetail);
//      int age = witnessDetail.getAge();
//      witnessDetail.setAge(age - 1);
//      String currentName = witnessDetail.getName();
//      String newName = "Fred Flintstone";
//      witnessDetail.setName(newName);
//      instance.updateWitnessDetail(witnessDetail);
//      witnessDetail = instance.getWitnessDetail(WITNESS_ID);
//      assertEquals("Witness age update failed:", witnessDetail.getAge(),
//                   (age - 1));
//      assertEquals("Witness name update failed", witnessDetail.getName(),
//                   newName);
//      witnessDetail.setAge(age);
//      witnessDetail.setName(currentName);
//      instance.updateWitnessDetail(witnessDetail);
//    }
//    catch (WitnessNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (java.rmi.RemoteException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//  }
//
//  public void testGetCaseDetails() {
//    try {
//      CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//      Assert.assertNotNull("CaseDetails is null", caseDetails);
//      Assert.assertEquals("Wrong case ID returned by caseDetails.getId() ",
//                          caseDetails.getId(), CASE_ID);
//      Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//      log.info("Successfully obtained case details");
//      log.debug(caseDetails);
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (java.rmi.RemoteException e) {
//      handleException(e);
//    }
//  }
//
//  public void testGetCaseDetailsByCaseNumber() {
//
//    // need to set up case data if this method is to be tested.
//
//    /**try
//      {
//          CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//          caseDetails = instance.getCaseDetails(CASE_ID);
//          Assert.assertNotNull("CaseDetails is null", caseDetails);
//          caseDetails = instance.getCaseDetailsByCaseNumber(new Integer(Integer.parseInt(caseDetails.getCaseNumber())));
//          Assert.assertNotNull("CaseDetails is null", caseDetails);
//          Assert.assertEquals("Wrong case ID returned by caseDetails.getId()", caseDetails.getId(),CASE_ID );
//          // A few sanity checks.
//         Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//          log.info("Successfully obtained case details");
//          log.debug(caseDetails);
//      }
//      catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e)
//      {
//          e.printStackTrace();
//          log.fatal(e);
//          Assert.fail(e.getMessage());
//      }
//      catch (RemoteException e)
//      {
//          handleException(e);
//      }**/
//  }
//
//  public void testGetCaseDetailsByCaseRef() {
//    try {
//      CaseDetail tempCaseDetails = instance.getCaseDetails(CASE_ID);
//      String caseRef = tempCaseDetails.getCaseNumber();
//      CaseDetail caseDetails = instance.getCaseDetailsByCaseRef(caseRef);
//      Assert.assertNotNull("CaseDetails is null", caseDetails);
//      Assert.assertEquals("Wrong case ID returned by caseDetails.getId()",
//                          caseDetails.getId(), CASE_ID);
//      Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//      log.info("Successfully obtained case details by case reference ");
//      log.debug(caseDetails);
//    }
//    catch (CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//  }
//
//  public void testGetCaseDetailsByCaseNumberAndCourtId() {
//
//      log.info("Start - testGetCaseDetailsByCaseNumberAndCourtId");
//    // need to set up case data if this method is to be tested.
//
//    try
//      {
//        log.info("CASE_ID - " + CASE_ID);
//        log.info("COURT_ID - " + COURT_ID);
//        CaseDetail caseDetails = instance.getCaseDetailsByCaseNumberAndCourtId(new Integer(12345678),new Integer(1));
//          Assert.assertNotNull("CaseDetails is null", caseDetails);
//          Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(1)));
//
//          // A few sanity checks.
//         Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//
//         log.debug("CASE ID = -2, COURT ID = 2");
//         caseDetails = instance.getCaseDetailsByCaseNumberAndCourtId(new Integer(12345678),new Integer(2));
//           Assert.assertNotNull("CaseDetails is null", caseDetails);
//           Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(2)));
//
//           // A few sanity checks.
//          Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//
//          log.debug("CASE ID = -3, COURT ID = 3");
//          caseDetails = instance.getCaseDetailsByCaseNumberAndCourtId(new Integer(12345678),new Integer(3));
//            Assert.assertNotNull("CaseDetails is null", caseDetails);
//            Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(3)));
//            Assert.assertFalse(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(1)));
//
//            // A few sanity checks.
//           Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//          log.info("Successfully obtained case details");
//          log.debug(caseDetails);
//      }
//      catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e)
//      {
//          e.printStackTrace();
//          log.fatal(e);
//          Assert.fail(e.getMessage());
//      }
//      catch (RemoteException e)
//      {
//          handleException(e);
//      }
//  }
//
//  public void testGetCaseDetailsByCaseNumberAndTypeAndCourtId() {
//
//      log.info("Start - testGetCaseDetailsByCaseNumberAndTypeAndCourtId");
//    // need to set up case data if this method is to be tested.
//
//    try
//      {
//        log.info("CASE_ID - " + CASE_ID);
//        log.info("COURT_ID - " + COURT_ID);
//        CaseDetail caseDetails = instance.getCaseDetailsByCaseNumberAndTypeAndCourtId(new Integer(12345678),"T", new Integer(1));
//          Assert.assertNotNull("CaseDetails is null", caseDetails);
//          Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(1)));
//
//          // A few sanity checks.
//         Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//
//         log.debug("CASE ID = -2, COURT ID = 2");
//         caseDetails = instance.getCaseDetailsByCaseNumberAndTypeAndCourtId(new Integer(12345678), "T", new Integer(2));
//           Assert.assertNotNull("CaseDetails is null", caseDetails);
//           Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(2)));
//
//           // A few sanity checks.
//          Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//
//          log.debug("CASE ID = -3, COURT ID = 3");
//          caseDetails = instance.getCaseDetailsByCaseNumberAndTypeAndCourtId(new Integer(12345678),"T", new Integer(3));
//            Assert.assertNotNull("CaseDetails is null", caseDetails);
//            Assert.assertTrue(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(3)));
//            Assert.assertFalse(instance.isCurrentCourtCase(caseDetails.getId(), new Integer(1)));
//
//            // A few sanity checks.
//           Assert.assertNotNull("Court name is null", caseDetails.getCourtName());
//          log.info("Successfully obtained case details");
//          log.debug(caseDetails);
//
//            log.debug("CASE ID = -3, COURT ID = 3");
//            caseDetails = instance.getCaseDetailsByCaseNumberAndTypeAndCourtId(new Integer(12345678),"A", new Integer(3));
//              Assert.assertNull("CaseDetails is null", caseDetails);
//
//              log.debug("CASE ID = -3, COURT ID = 3");
//              caseDetails = instance.getCaseDetailsByCaseNumberAndTypeAndCourtId(new Integer(12345678),"U", new Integer(3));
//                Assert.assertNull("CaseDetails is null", caseDetails);
//
//      }
//      catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e)
//      {
//          e.printStackTrace();
//          log.fatal(e);
//          Assert.fail(e.getMessage());
//      }
//      catch (RemoteException e)
//      {
//          handleException(e);
//      }
//  }
//  public void testGetEstimatedCaseDuration() {
//    try {
//      CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//      Assert.assertNotNull("CaseDetails is null", caseDetails);
//      float value = 12.5f;
//      caseDetails.setEstimatedCaseDuration(value, false);
//      caseDetails.update();
//      float newValue = instance.getEstimatedCaseDuration(CASE_ID);
//      assertEquals(value, newValue, value);
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           NoDirectionsForCaseException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//    catch (CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (DurationLessThanMinimumException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (ModificationException e) {
//      e.printStackTrace(); //To change body of catch statement use Options | File Templates.
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.error(e);
//      Assert.assertTrue(e.getMessage(), false);
//    }
//
//  }
//
//  public void testGetJudgeForCase() {
//    try {
//      CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//      Assert.assertNotNull("CaseDetails is null", caseDetails);
//      String judge = caseDetails.getJudgeName();
//      if (judge != null) {
//        Assert.assertTrue("The judge name is too long", judge.length() <= 80);
//      }
//
//      log.debug("Successfully obtained judge details for case");
//      log.debug("Judge name: " + judge);
//    }
//    catch (CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//    catch (NoJudgeForCaseException e) {
//      log.debug("No Judge for Case " + CASE_ID);
//    }
//  }
//
//  public void testCreateWitnessSession() {
//    XhbWitnessHome witnessHome = (XhbWitnessHome) CSServices.getServiceLocator().
//        getLocalHome(XhbWitnessHome.class);
//    try {
//      WitnessSession witnessSession = instance.createWitnessSession(CASE_ID,
//          new Integer(1), "William Gates", "PROSECUTION", "Juvenile", 13,
//          new java.sql.Time( (long) 12.50), "Set of notes");
//      Assert.assertNotNull("WitnessSession is null", witnessSession);
//      witnessHome.findByPrimaryKey(witnessSession.getAssociatedWitnessDetail().
//                                   getId());
//
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           WitnessCreationException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           SkeletonSessionNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//    catch (javax.ejb.FinderException e) {
//      Assert.fail("The witness could not be found but it should exist !");
//      handleException(e);
//    }
//    catch (Exception e) {
//      handleException(e);
//    }
//
//  }
//
//  public void testRemoveWitnessDetail() {
//    XhbWitnessHome witnessHome = (XhbWitnessHome) CSServices.getServiceLocator().
//        getLocalHome(XhbWitnessHome.class);
//    //add a witness detail before removing one
//    try {
//      WitnessSession witnessSession = instance.createWitnessSession(CASE_ID,
//          new Integer(1), "William Gates", "PROSECUTION", "Juvenile", 13,
//          new java.sql.Time( (long) 12.50), "Set of notes");
//      Assert.assertNotNull("WitnessSession is null", witnessSession);
//      witnessSession.getAssociatedWitnessDetail();
//      instance.removeWitnessDetail(witnessSession.getAssociatedWitnessDetail());
//      witnessHome.findByPrimaryKey(witnessSession.getAssociatedWitnessDetail().
//                                   getId());
//      Assert.fail("The witness has been found but should have been removed !");
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//    catch (javax.ejb.FinderException e) {
//      // do nothing as this exception should be thrown
//    }
//    catch (Exception e) {
//      handleException(e);
//    }
//
//  }
//
//  public void testSetEstimatedCaseDuration() {
//    try {
//
//      CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//      float value = 12.5f;
//      caseDetails.setEstimatedCaseDuration(value, false);
//      float newValue = instance.getEstimatedCaseDuration(CASE_ID);
//      assertEquals(value, newValue, value);
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           NoDirectionsForCaseException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (RemoteException e) {
//      handleException(e);
//    }
//    catch (DurationLessThanMinimumException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testGetCourtRoomsByCourtId() {
//    try {
//      ArrayList arrayList;
//      arrayList = instance.getCourtRoomsByCourtId(COURT_ID);
//      Assert.assertTrue("Should be one or more court rooms !",
//                        arrayList.size() > 0);
//      log.debug(":" + arrayList);
//      /**for (int i=0; i<j; i++){
//        Item = (CourtRoom)arrayList.get(i);
//        Assert.assertNotNull("The court room is null",Item);
//        //Assert.assertNotNull("The court room id is null",Item.getCourtRoomId());
//        //Assert.assertNotNull("The crest court room number is null", courtRoom.getCrestCourtRoomNo());
//        //log.debug(i + ":" + courtRoom);
//                   }**/
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testUpdateCaseDetail() {
//    try {
//
//      CaseDetail caseDetails = instance.getCaseDetails(CASE_ID);
//      Assert.assertNotNull("CaseDetail is null", caseDetails);
//      log.info("Successfully obtained case details");
//      log.debug(caseDetails);
//      String currentName = caseDetails.getCpsCaseWorker();
//      float currentECD = caseDetails.getEstimatedCaseDuration();
//      String currentPO = caseDetails.getPoliceOfficerAttending();
//      String newCPS = "Fred Flintstone";
//      float newECD = currentECD + 2;
//      String newtPO = "Bill Gates";
//      caseDetails.setCpsCaseWorker(newCPS);
//      caseDetails.setEstimatedCaseDuration(newECD, false);
//      caseDetails.setPoliceOfficerAttending(newtPO);
//      instance.updateCaseDetail(caseDetails);
//      caseDetails = instance.getCaseDetails(CASE_ID);
//      assertEquals("CaseDetail CpsCaseWorker update failed:",
//                   caseDetails.getCpsCaseWorker(), newCPS);
//      assertEquals("CaseDetail Police Officer Attending update failed",
//                   caseDetails.getPoliceOfficerAttending(), newtPO);
//      assertEquals("CaseDetail EstimatedCaseDuration update failed", caseDetails.getEstimatedCaseDuration(),
//                   newECD, caseDetails.getEstimatedCaseDuration());
//      caseDetails.setCpsCaseWorker(currentName);
//      //caseDetails.setEstimatedCaseDuration(currentECD, false);
//      caseDetails.setPoliceOfficerAttending(currentPO);
//      instance.updateCaseDetail(caseDetails);
//    }
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           NoDirectionsForCaseException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//    catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.
//           CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (DurationLessThanMinimumException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//    catch (java.rmi.RemoteException e) {
//      handleException(e);
//    }
//    catch (CaseModificationException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//  public void testUpdateWitnessSession() {
//    try {
//      WitnessSession witnessSession = instance.createWitnessSession(CASE_ID,
//          new Integer(1), "William Gates", "PROSECUTION", "Juvenile", 13,
//          new java.sql.Time( (long) 12.50), "Set of notes");
//      Assert.assertNotNull("WitnessSession is null", witnessSession);
//      TrialSession trialSession = witnessSession.getTrialSession();
//      Assert.assertNotNull("TrialSession is null", trialSession);
//      String currnetTrialSessionNotes = trialSession.getNotes();
//      String newTrialSessionNotes = "New Notes";
//      trialSession.setNotes(newTrialSessionNotes);
//      witnessSession.setTrialSession(trialSession);
//      instance.updateWitnessSession(witnessSession);
//      assertEquals("UpdateWitnessSession Failed: ",
//                   witnessSession.getTrialSession().getNotes(),
//                   newTrialSessionNotes);
//      trialSession.setNotes(currnetTrialSessionNotes);
//      witnessSession.setTrialSession(trialSession);
//      instance.updateWitnessSession(witnessSession);
//    }
//    catch (WitnessModificationException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//    catch (SkeletonSessionNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//    catch (java.rmi.RemoteException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//    catch (WitnessCreationException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//    catch (CaseNotFoundException e) {
//      e.printStackTrace();
//      log.fatal(e);
//      Assert.fail(e.getMessage());
//    }
//
//  }
//
//}
//
//