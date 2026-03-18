//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.*;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.*;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.*;
//
//import org.apache.log4j.Logger;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Vector;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
//
///**
// * <p>Title: HRCourtReporterValueHelperTest</p>
// * <p>Description: Test for the Court Reporter Value Helper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//
//public class HRCourtReporterValueHelperTest extends TransactionTestCase
//{
//
//  private static Logger log  = CSServices.getLogger(HRCourtReporterValueHelperTest.class);
//
//
//  public HRCourtReporterValueHelperTest(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  /**
//   * setUp
//   */
//  protected void setUp()
//  {
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<<");
//    try
//    {
//        super.setUp();
//      //delete the data
//      TestUtils.execSql(TestConstants.delExportA);
//      TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//      TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//      TestUtils.execSql(TestConstants.DEL_ADV);
//      TestUtils.execSql(TestConstants.DEL_CHAMBER);
//      TestUtils.execSql(TestConstants.delShLegRep);
//      TestUtils.execSql(TestConstants.delSchedHearDef);
//      TestUtils.execSql(TestConstants.delRefLegRep);
//      TestUtils.execSql(TestConstants.delShJudge);
//      TestUtils.execSql(TestConstants.delShAtt);
//      TestUtils.execSql(TestConstants.delJudge);
//      TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//      TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//      TestUtils.execSql(TestConstants.delShHrg1);
//      TestUtils.execSql(TestConstants.delSitting);
//      TestUtils.execSql(TestConstants.delHrgList);
//      TestUtils.execSql(TestConstants.delDefHrgRec);
//      TestUtils.execSql(TestConstants.delHrg);
//      TestUtils.execSql(TestConstants.delRefHrgType);
//      TestUtils.execSql(TestConstants.delDefOnCase);
//      TestUtils.execSql(TestConstants.DEL_CR);
//      TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//      TestUtils.execSql(TestConstants.delCase);
//      TestUtils.execSql(TestConstants.DEL_CASES);
//      TestUtils.execSql(TestConstants.DEL_DEF_REF);//
//      TestUtils.execSql(TestConstants.delDef);
//      TestUtils.execSql(TestConstants.delSolFirm);
//      TestUtils.execSql(TestConstants.delCCInfo);
//      TestUtils.execSql(TestConstants.delRefCourt);
//      TestUtils.execSql(TestConstants.delCourtRoom);
//      TestUtils.execSql(TestConstants.delCourtSite);
//      TestUtils.execSql(TestConstants.delAddr);
//      TestUtils.execSql(TestConstants.delCourt);
//
//      //delete the data
//      TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//      TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//      TestUtils.execSql(TestConstants.DEL_ADV);
//      TestUtils.execSql(TestConstants.DEL_CHAMBER);
//      TestUtils.execSql(TestConstants.delShLegRep);
//      TestUtils.execSql(TestConstants.delSchedHearDef);
//      TestUtils.execSql(TestConstants.delRefLegRep);
//      TestUtils.execSql(TestConstants.delShJudge);
//      TestUtils.execSql(TestConstants.delShAtt);
//      TestUtils.execSql(TestConstants.delJudge);
//      TestUtils.execSql(TestConstants.delShHrg1);
//      TestUtils.execSql(TestConstants.delSitting);
//      TestUtils.execSql(TestConstants.delHrgList);
//      TestUtils.execSql(TestConstants.delDefHrgRec);
//      TestUtils.execSql(TestConstants.delHrg);
//      TestUtils.execSql(TestConstants.delRefHrgType);
//      TestUtils.execSql(TestConstants.delDefOnCase);
//      TestUtils.execSql(TestConstants.delCase);
//      TestUtils.execSql(TestConstants.delDef);
//      TestUtils.execSql(TestConstants.delSolFirm);
//      TestUtils.execSql(TestConstants.delCCInfo);
//      TestUtils.execSql(TestConstants.delRefCourt);
//      TestUtils.execSql(TestConstants.delCourtRoom);
//      TestUtils.execSql(TestConstants.delCourtSite);
//      TestUtils.execSql(TestConstants.delAddr);
//      TestUtils.execSql(TestConstants.delCourt);
//
//      //insert the data
//      TestUtils.execSql(TestConstants.insCourt);
//      TestUtils.execSql(TestConstants.insAddr);
//      TestUtils.execSql(TestConstants.insCourtSite);
//      TestUtils.execSql(TestConstants.insCourtRoom);
//      TestUtils.execSql(TestConstants.insRefCourt);
//      TestUtils.execSql(TestConstants.insCase);
//      TestUtils.execSql(TestConstants.insRefHrgType);
//      TestUtils.execSql(TestConstants.insHrg);
//      TestUtils.execSql(TestConstants.insHrgList);
//      TestUtils.execSql(TestConstants.insSitting);
//      TestUtils.execSql(TestConstants.insShHrg1);
//      TestUtils.execSql(TestConstants.INS_CR_FIRM);
//      TestUtils.execSql(TestConstants.INS_CR1);
//      TestUtils.execSql(TestConstants.INS_SH_ATT_CR1);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<<");
//  }
//
//  /**
//   * testBuildHRCourtReporters
//   */
//  public void testBuildHRCourtReporters()
//  {
//
//    log.debug(">>>>>>>>>>>> testBuildHRCourtReporters Starts <<<<<<<<<<<<<<");
//    HRCourtReporterValueHelper hrcourtreportervaluehelper = new HRCourtReporterValueHelper();
//
//    try
//    {
//      Collection collectionRet = hrcourtreportervaluehelper.buildHRCourtReporters(
//          TestConstants.COURT_REPORTER);
//
//      Iterator it = collectionRet.iterator();
//
//      if(collectionRet.isEmpty())
//      {
//        log.debug("The collection was empty so fail the test");
//        fail();
//      }
//
//      while(it.hasNext())
//      {
//        log.debug("Try to get the HRCourtReporterValue.....");
//        HRCourtReporterValue hrValue = (HRCourtReporterValue)it.next();
//        log.debug("getFirstName : " + hrValue.getFirstName());
//        log.debug("getMiddleName : " + hrValue.getMiddleName());
//        log.debug("getRefCourtReporterID : " + hrValue.getRefCourtReporterID());
//        log.debug("getSurname : " + hrValue.getSurname());
//        log.debug("refCourtRepID 1: " +hrValue.getRefCourtReporterID() + ", 2: " + TestConstants.COURT_REPORTER);
//        this.assertEquals(hrValue.getRefCourtReporterID(), TestConstants.COURT_REPORTER);
//      }
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testBuildHRCourtReporters ends <<<<<<<<<<<<<<");
//    }
//  }
//}
//