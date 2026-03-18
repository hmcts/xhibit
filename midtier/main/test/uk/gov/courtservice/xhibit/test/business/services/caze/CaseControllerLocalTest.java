//package uk.gov.courtservice.xhibit.test.business.services.caze;
//
//// j2ee, jdk
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.caze.Case;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
//import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
//import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
//
///**
// * <p>Title: </p>
// * <p>Description: Tests the updateCase() methods on the CaseController</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Sarah Tong
// * @version $Id: CaseControllerLocalTest.java,v 1.5 2006/07/11 14:16:58 xzfdtb Exp $
// */
//public class CaseControllerLocalTest extends TransactionTestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  CaseControllerLocal caseController = (CaseControllerLocal)CSServices.getEJBServices()
//                                  .createLocalSession(CaseControllerLocalHome.class);
//
//  // values from the set of standard test data
//  private static final Integer CASE_ID_1 = new Integer(1);
//  private static final Integer CASE_ID_2 = new Integer(2);
//
//  public CaseControllerLocalTest(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//  }
//
//  public void testUpdateCase()
//  {
//    try
//    {
//      CaseMaintainer caseMaintainer = new CaseMaintainer();
//
//      // get the first case
//      Case caseBean1 =
//          (Case)CSServices.getEJBServices().
//                         findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_1);
//      CaseBasicValue cbv1 = caseMaintainer.getCaseBasicValue(caseBean1);
//
//      // set the values to update
//      cbv1.setEstPDHTrialLength(new Integer(888));
//      cbv1.setNoProsWitness(new Integer(111));
//      cbv1.setNoPageProsEvidence(new Integer(222));
//      cbv1.setLengthTape(new Integer(444));
//      cbv1.setJudgeReasonForAppeal("New reason for appeal");
//      cbv1.setIndictmentInfo1("New Info 1");
//      cbv1.setIndictmentInfo2("New Info 2");
//      cbv1.setIndictmentInfo3("New Info 3");
//      cbv1.setIndictmentInfo4("New Info 4");
//      cbv1.setIndictmentInfo5("New Info 5");
//      cbv1.setIndictmentInfo6("New Info 6");
//
//      caseController.updateCase(cbv1);
//
//      Case caseBean2 =
//          (Case)CSServices.getEJBServices().
//                         findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_1);
//
//      assertEquals(888, caseBean2.getEstPDHTrialLength().intValue());
//      assertEquals(111, caseBean2.getNoProsWitness().intValue());
//      assertEquals(222, caseBean2.getNoPageProsEvidence().intValue());
//      assertEquals(444, caseBean2.getLengthTape().intValue());
//      assertEquals("New reason for appeal", caseBean2.getJudgeReasonForAppeal());
//      assertEquals("New Info 1", caseBean2.getIndictmentInfo1());
//      assertEquals("New Info 2", caseBean2.getIndictmentInfo2());
//      assertEquals("New Info 3", caseBean2.getIndictmentInfo3());
//      assertEquals("New Info 4", caseBean2.getIndictmentInfo4());
//      assertEquals("New Info 5", caseBean2.getIndictmentInfo5());
//      assertEquals("New Info 6", caseBean2.getIndictmentInfo6());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail("Exception thrown:  "+e.getMessage());
//    }
//  }
//
//  public void testUpdateCaseArray()
//  {
//    try
//    {
//      CaseMaintainer caseMaintainer = new CaseMaintainer();
//
//      // get the first case
//      Case caseBean1 =
//          (Case)CSServices.getEJBServices().
//                         findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_1);
//      CaseBasicValue cbv1 = caseMaintainer.getCaseBasicValue(caseBean1);
//
//      // set the values to update
//      cbv1.setEstPDHTrialLength(new Integer(888));
//      cbv1.setNoProsWitness(new Integer(111));
//      cbv1.setNoPageProsEvidence(new Integer(222));
//      cbv1.setLengthTape(new Integer(444));
//      cbv1.setJudgeReasonForAppeal("New reason for appeal");
//      cbv1.setIndictmentInfo1("New Info 1");
//      cbv1.setIndictmentInfo2("New Info 2");
//      cbv1.setIndictmentInfo3("New Info 3");
//      cbv1.setIndictmentInfo4("New Info 4");
//      cbv1.setIndictmentInfo5("New Info 5");
//      cbv1.setIndictmentInfo6("New Info 6");
//
//      // get the second case
//      Case caseBean2 = (
//          Case)CSServices.getEJBServices().
//                        findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_2);
//      CaseBasicValue cbv2 = caseMaintainer.getCaseBasicValue(caseBean2);
//
//      // set the values to update
//      cbv2.setEstPDHTrialLength(new Integer(8888));
//      cbv2.setNoProsWitness(new Integer(1111));
//      cbv2.setNoPageProsEvidence(new Integer(2222));
//      cbv2.setLengthTape(new Integer(4444));
//      cbv2.setJudgeReasonForAppeal("New reason for appeal 2");
//      cbv2.setIndictmentInfo1("New Info 1 2");
//      cbv2.setIndictmentInfo2("New Info 2 2");
//      cbv2.setIndictmentInfo3("New Info 3 2");
//      cbv2.setIndictmentInfo4("New Info 4 2");
//      cbv2.setIndictmentInfo5("New Info 5 2");
//      cbv2.setIndictmentInfo6("New Info 6 2");
//
//      CaseBasicValue[] cbvArray = new CaseBasicValue[] {cbv1, cbv2};
//
//      caseController.updateCase(cbvArray);
//
//      // check the first updates
//      Case caseBean3 =
//          (Case)CSServices.getEJBServices().
//                         findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_1);
//
//      assertEquals(888, caseBean3.getEstPDHTrialLength().intValue());
//      assertEquals(111, caseBean3.getNoProsWitness().intValue());
//      assertEquals(222, caseBean3.getNoPageProsEvidence().intValue());
//      assertEquals(444, caseBean3.getLengthTape().intValue());
//      assertEquals("New reason for appeal", caseBean3.getJudgeReasonForAppeal());
//      assertEquals("New Info 1", caseBean3.getIndictmentInfo1());
//      assertEquals("New Info 2", caseBean3.getIndictmentInfo2());
//      assertEquals("New Info 3", caseBean3.getIndictmentInfo3());
//      assertEquals("New Info 4", caseBean3.getIndictmentInfo4());
//      assertEquals("New Info 5", caseBean3.getIndictmentInfo5());
//      assertEquals("New Info 6", caseBean3.getIndictmentInfo6());
//
//      // check the second updates
//      Case caseBean4 =
//          (Case)CSServices.getEJBServices().
//                         findLocalEntityByPrimaryKey(CaseHome.class, CASE_ID_2);
//
//      assertEquals(8888, caseBean4.getEstPDHTrialLength().intValue());
//      assertEquals(1111, caseBean4.getNoProsWitness().intValue());
//      assertEquals(2222, caseBean4.getNoPageProsEvidence().intValue());
//      assertEquals(4444, caseBean4.getLengthTape().intValue());
//      assertEquals("New reason for appeal 2", caseBean4.getJudgeReasonForAppeal());
//      assertEquals("New Info 1 2", caseBean4.getIndictmentInfo1());
//      assertEquals("New Info 2 2", caseBean4.getIndictmentInfo2());
//      assertEquals("New Info 3 2", caseBean4.getIndictmentInfo3());
//      assertEquals("New Info 4 2", caseBean4.getIndictmentInfo4());
//      assertEquals("New Info 5 2", caseBean4.getIndictmentInfo5());
//      assertEquals("New Info 6 2", caseBean4.getIndictmentInfo6());
//
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail("Exception thrown:  "+e.getMessage());
//    }
//  }
//
////  public void testGetSchedHearingLocationsForDate()
////  {
////    Calendar date = Calendar.getInstance();
////    date.set(2003, Calendar.MAY, 3); // year, month, date
////    try
////    {
////      SchedHearingLocationValue[] retVals =
////          caseController.getSchedHearingLocationsForDate(CASE_ID_1, date);
////      assertEquals("Number of sched hearings", 1, retVals.length);
////      assertEquals("Sched hearing id", new Integer(1), retVals[0].getShedHearingID());
////    }
////    catch (CaseControllerException e)
////    {
////      e.printStackTrace();
////      fail("Exception thrown:  "+e.getMessage());
////    }
////  }
//}
//