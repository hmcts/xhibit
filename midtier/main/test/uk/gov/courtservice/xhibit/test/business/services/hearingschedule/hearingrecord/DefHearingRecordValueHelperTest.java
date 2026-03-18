//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.DefHearingRecordValueHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
//
///**
// * <p>Title: DefHearingRecordValueHelperTest</p>
// * <p>Description: Test the DefHearingRecordValueHelper. </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author unascribed
// * @version $Id: DefHearingRecordValueHelperTest.java,v 1.11 2006/07/11 14:16:59 xzfdtb Exp $
// */
//public class DefHearingRecordValueHelperTest extends TransactionTestCase
//{
//
//
//   private static Logger log  = CSServices.getLogger(DefHearingRecordValueHelperTest.class);
//
//   private DefHearingRecordMaintainer maintainer = null;
//   private DefHearingRecordValue value = null;
//   private DefHearingRecordBasicValue basicValue = null;
//   private DefHearingRecord entity = null;
//
//
//  public DefHearingRecordValueHelperTest(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  /**
//   * setUp
//   */
//  protected void setUp() throws Exception
//  {
//      super.setUp();
//      log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<");
//      try
//      {
//          //remove data
//          TestUtils.execSql(TestConstants.delDefHrgRec);
//          TestUtils.execSql(TestConstants.delDefOnCase);
//          TestUtils.execSql(TestConstants.delDef);
//          TestUtils.execSql(TestConstants.delHrg);
//          TestUtils.execSql(TestConstants.delRefHrgType);
//          TestUtils.execSql(TestConstants.delCase);
//          TestUtils.execSql(TestConstants.delRefCourt);
//          TestUtils.execSql(TestConstants.delCourtRoom);
//          TestUtils.execSql(TestConstants.delCourtSite);
//          TestUtils.execSql(TestConstants.delAddr);
//          TestUtils.execSql(TestConstants.delCourt);
//
//          //insert data
//          TestUtils.execSql(TestConstants.insCourt);
//          TestUtils.execSql(TestConstants.insAddr);
//          TestUtils.execSql(TestConstants.insCourtSite);
//          TestUtils.execSql(TestConstants.insCourtRoom);
//          TestUtils.execSql(TestConstants.insRefCourt);
//          TestUtils.execSql(TestConstants.insRefHrgType);
//          TestUtils.execSql(TestConstants.insCase);
//          TestUtils.execSql(TestConstants.insHrg);
//          TestUtils.execSql(TestConstants.insDef);
//          TestUtils.execSql(TestConstants.insDefOnCase);
//          TestUtils.execSql(TestConstants.insDefHrgRec);
//
//          log.debug(">>>>>>>>>> find by pk : " + TestConstants.defHrgRecID.intValue());
//          maintainer = new DefHearingRecordMaintainer();
//          entity = maintainer.findByPrimaryKey(TestConstants.defHrgRecID);
//
//          log.debug(">>>>>>>>>> set the DefHearingRecordValue");
//          value = new DefHearingRecordValue(entity.getHearingRecordId(), entity.getVersion());
//          value.setAdjournedDate(entity.getAdjournedDate());
//          value.setDateBailApplication(entity.getDateBailApplication());
//          value.setDefendantOnCaseID(entity.getDefendantOnCaseId());
//          value.setEndBailStatus(entity.getEndBailStatus());
//          value.setHearingDateFreetext1(entity.getHearingDateFreeTxt1());
//          value.setHearingDateFreetext2(entity.getHearingDateFreeTxt2());
//          value.setHearingDateFreetext3(entity.getHearingDateFreeTxt3());
//          value.setHearingID(entity.getHearingId());
//          value.setIsAdjourned(entity.getIsAdjourned());
//          value.setIsHraApplication(entity.getIsHraApplication());
//          value.setNewBailStatus(entity.getNewBailStatus());
//          value.setOralEvidence(entity.getOralEvidence());
//          value.setRefAdjournmentID(entity.getRefAdjournmentId());
//          value.setRefDefHearingTypeID(entity.getRefDefHearingTypeId());
//          value.setResultBailApplication(entity.getResultBailApplication());
//          value.setStartBailStatus(entity.getStartBailStatus());
//          value.setStartDateNewBailStatus(entity.getStartDateNewBailStatus());
//          value.setSubstBailApplication(entity.getSubstBailApplication());
//
//          log.debug(">>>>>>>>>> set the DefHearingRecordBasicValue");
//          basicValue = new DefHearingRecordBasicValue(entity.getHearingRecordId(), entity.getVersion());
//          basicValue.setAdjournedDate(entity.getAdjournedDate());
//          basicValue.setDateBailApplication(entity.getDateBailApplication());
//          basicValue.setDefendantOnCaseID(entity.getDefendantOnCaseId());
//          basicValue.setEndBailStatus(entity.getEndBailStatus());
//          basicValue.setHearingDateFreetext1(entity.getHearingDateFreeTxt1());
//          basicValue.setHearingDateFreetext2(entity.getHearingDateFreeTxt2());
//          basicValue.setHearingDateFreetext3(entity.getHearingDateFreeTxt3());
//          basicValue.setHearingID(entity.getHearingId());
//          basicValue.setIsAdjourned(entity.getIsAdjourned());
//          basicValue.setIsHraApplication(entity.getIsHraApplication());
//          basicValue.setNewBailStatus(entity.getNewBailStatus());
//          basicValue.setOralEvidence(entity.getOralEvidence());
//          basicValue.setRefAdjournmentID(entity.getRefAdjournmentId());
//          basicValue.setRefDefHearingTypeID(entity.getRefDefHearingTypeId());
//          basicValue.setResultBailApplication(entity.getResultBailApplication());
//          basicValue.setStartBailStatus(entity.getStartBailStatus());
//          basicValue.setStartDateNewBailStatus(entity.getStartDateNewBailStatus());
//          basicValue.setSubstBailApplication(entity.getSubstBailApplication());
//      }
//      catch (Exception e)
//      {
//          log.debug(e.toString());
//          e.printStackTrace();
//          fail();
//      }
//      log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<");
//  }
//
//  /**
//   * testBuildBasicValue
//   */
//  public void testBuildBasicValue()
//  {
//    log.debug(">>>>>>>>>>>> testBuildBasicValue Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//    DefHearingRecordValueHelper defhearingrecordvaluehelper = new DefHearingRecordValueHelper();
//    try {
//      log.debug("value past in : " + value.toString());
//      DefHearingRecordBasicValue defhearingrecordbasicvalueRet =
//          defhearingrecordvaluehelper.buildBasicValue(value);
//
//      log.debug("start the comparison");
//      assertEquals(defhearingrecordbasicvalueRet.getAdjournedDate(), value.getAdjournedDate());
//      assertEquals(defhearingrecordbasicvalueRet.getDateBailApplication(), value.getDateBailApplication());
//      assertEquals(defhearingrecordbasicvalueRet.getDefendantOnCaseID(), value.getDefendantOnCaseID());
//      assertEquals(defhearingrecordbasicvalueRet.getEndBailStatus(), value.getEndBailStatus());
//      assertEquals(defhearingrecordbasicvalueRet.getHearingDateFreetext1(), value.getHearingDateFreetext1());
//      assertEquals(defhearingrecordbasicvalueRet.getHearingDateFreetext2(), value.getHearingDateFreetext2());
//      assertEquals(defhearingrecordbasicvalueRet.getHearingDateFreetext3(), value.getHearingDateFreetext3());
//      assertEquals(defhearingrecordbasicvalueRet.getHearingID(), value.getHearingID());
//      assertEquals(defhearingrecordbasicvalueRet.getIsAdjourned(), value.getIsAdjourned());
//      assertEquals(defhearingrecordbasicvalueRet.getIsHraApplication(), value.getIsHraApplication());
//      assertEquals(defhearingrecordbasicvalueRet.getNewBailStatus(), value.getNewBailStatus());
//      assertEquals(defhearingrecordbasicvalueRet.getOralEvidence(), value.getOralEvidence());
//      assertEquals(defhearingrecordbasicvalueRet.getRefAdjournmentID(), value.getRefAdjournmentID());
//      assertEquals(defhearingrecordbasicvalueRet.getRefDefHearingTypeID(), value.getRefDefHearingTypeID());
//      assertEquals(defhearingrecordbasicvalueRet.getResultBailApplication(), value.getResultBailApplication());
//      assertEquals(defhearingrecordbasicvalueRet.getStartBailStatus(), value.getStartBailStatus());
//      assertEquals(defhearingrecordbasicvalueRet.getStartDateNewBailStatus(), value.getStartDateNewBailStatus());
//      assertEquals(defhearingrecordbasicvalueRet.getSubstBailApplication(), value.getSubstBailApplication());
//      assertEquals(defhearingrecordbasicvalueRet.getVersion(), value.getVersion());
//
//      log.debug("defhearingrecordbasicvalueRet after in : " + defhearingrecordbasicvalueRet.toString());
//    }
//    catch(Exception e) {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>> testBuildBasicValue Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  public void testGetDefHearingRecordValue()
//  {
//      log.debug(">>>>>>>>>>>> testGetDefHearingRecordValue Starts <<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//      DefHearingRecordValueHelper defhearingrecordvaluehelper = new DefHearingRecordValueHelper();
//      try
//      {
//          log.debug("defHrgRecID past in : " + TestConstants.defHrgRecID.toString());
//
//          log.debug("defOnCaseID passed in : " + TestConstants.defOnCaseID.toString());
//          log.debug("hearingID passed in : "+ TestConstants.hearingID.toString());
//
//          DefHearingRecordValue dhrValue = defhearingrecordvaluehelper.
//                  getDefHearingRecordValue(TestConstants.defOnCaseID, TestConstants.hearingID);
//
//          log.debug("start the comparison");
//          assertEquals(dhrValue.getAdjournedDate(), basicValue.getAdjournedDate());
//          assertEquals(dhrValue.getDateBailApplication(), basicValue.getDateBailApplication());
//          assertEquals(dhrValue.getDefendantOnCaseID(), basicValue.getDefendantOnCaseID());
//          assertEquals(dhrValue.getEndBailStatus(), basicValue.getEndBailStatus());
//          assertEquals(dhrValue.getHearingDateFreetext1(), basicValue.getHearingDateFreetext1());
//          assertEquals(dhrValue.getHearingDateFreetext2(), basicValue.getHearingDateFreetext2());
//          assertEquals(dhrValue.getHearingDateFreetext3(), basicValue.getHearingDateFreetext3());
//          assertEquals(dhrValue.getHearingID(), basicValue.getHearingID());
//          assertEquals(dhrValue.getIsAdjourned(), basicValue.getIsAdjourned());
//          assertEquals(dhrValue.getIsHraApplication(), basicValue.getIsHraApplication());
//          assertEquals(dhrValue.getNewBailStatus(), basicValue.getNewBailStatus());
//          assertEquals(dhrValue.getOralEvidence(), basicValue.getOralEvidence());
//          assertEquals(dhrValue.getRefAdjournmentID(), basicValue.getRefAdjournmentID());
//          assertEquals(dhrValue.getRefDefHearingTypeID(), basicValue.getRefDefHearingTypeID());
//          assertEquals(dhrValue.getResultBailApplication(), basicValue.getResultBailApplication());
//          assertEquals(dhrValue.getStartBailStatus(), basicValue.getStartBailStatus());
//          assertEquals(dhrValue.getStartDateNewBailStatus(), basicValue.getStartDateNewBailStatus());
//          assertEquals(dhrValue.getSubstBailApplication(), basicValue.getSubstBailApplication());
//          assertEquals(dhrValue.getVersion(), basicValue.getVersion());
//
//          log.debug("defHrgRecID after in : " + TestConstants.defHrgRecID.toString());
//      }
//      catch(Exception e)
//      {
//          log.debug(e.toString());
//          e.printStackTrace();
//          fail();
//      }
//      finally
//      {
//          log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//          log.debug(">>>>>>>>>>>> testGetDefHearingRecordValue Ends <<<<<<<<<<<<<");
//      }
//  }
//}
//