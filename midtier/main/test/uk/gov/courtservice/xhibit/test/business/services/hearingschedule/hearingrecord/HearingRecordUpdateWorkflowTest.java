//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.*;
//import uk.gov.courtservice.xhibit.business.entities.hearing.*;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.*;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.*;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.*;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.*;
//
//import org.apache.log4j.Logger;
//
//import java.util.Collection;
//import java.util.Vector;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: HearingRecordUpdateWorkflowTest</p>
// * <p>Description: Test the update of the Hearing Record via the workflow</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HearingRecordUpdateWorkflowTest extends TransactionTestCase {
//
//  private static Logger log  = CSServices.getLogger(HearingRecordUpdateWorkflowTest.class);
//
//
//  //ASSUME THE FOLLOWING EXIST
//  //COURT_ID = 1
//  //REF_COURT_ID = 1
//  //COURT_ROOM_ID = 1
//  //COURT_SITE_ID = 1
//
//   private HearingRecordUpdateValue updateValue = new HearingRecordUpdateValue();
//   private DefHearingRecordValue defHrgValue = null;;
//   private HearingBasicValue hearingValue = null;
//   private HRSHJudgeValue judgeValue = null;
//   private HRSHLegRepValue legRepValue1 = null;
//   private HRSHLegRepValue legRepValue2 = null;
//
//
//  /**
//   * HearingRecordUpdateWorkflowTest
//   * @param s
//   */
//  public HearingRecordUpdateWorkflowTest(String s) throws NamingException
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
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//
//    try
//    {
//      //delete the data
//     TestUtils.execSql(TestConstants.delShLegRep);
//     TestUtils.execSql(TestConstants.delSchedHearDef);
//     TestUtils.execSql(TestConstants.delRefLegRep);
//     TestUtils.execSql(TestConstants.delShJudge);
//     TestUtils.execSql(TestConstants.delShAtt);
//     TestUtils.execSql(TestConstants.delJudge);
//     TestUtils.execSql(TestConstants.delShHrg1);
//     TestUtils.execSql(TestConstants.delSitting);
//     TestUtils.execSql(TestConstants.delHrgList);
//     TestUtils.execSql(TestConstants.delDefHrgRec);
//     TestUtils.execSql(TestConstants.delHrg);
//     TestUtils.execSql(TestConstants.delRefHrgType);
//     TestUtils.execSql(TestConstants.delDefOnCase);
//     TestUtils.execSql(TestConstants.delCase);
//     TestUtils.execSql(TestConstants.delDef);
//     TestUtils.execSql(TestConstants.delSolFirm);
//     TestUtils.execSql(TestConstants.delCCInfo);
//     TestUtils.execSql(TestConstants.delRefCourt);
//     TestUtils.execSql(TestConstants.delCourtRoom);
//     TestUtils.execSql(TestConstants.delCourtSite);
//     TestUtils.execSql(TestConstants.delAddr);
//     TestUtils.execSql(TestConstants.delCourt);
//
//      //insert the data
//     TestUtils.execSql(TestConstants.insCourt);
//     TestUtils.execSql(TestConstants.insAddr);
//     TestUtils.execSql(TestConstants.insCourtSite);
//     TestUtils.execSql(TestConstants.insCourtRoom);
//     TestUtils.execSql(TestConstants.insRefCourt);
//     TestUtils.execSql(TestConstants.insDef);
//     TestUtils.execSql(TestConstants.insCase);
//     TestUtils.execSql(TestConstants.insDefOnCase);
//     TestUtils.execSql(TestConstants.insSolFirm);
//     TestUtils.execSql(TestConstants.insCCInfo);
//     TestUtils.execSql(TestConstants.insRefHrgType);
//     TestUtils.execSql(TestConstants.insHrg);
//     TestUtils.execSql(TestConstants.insDefHrgRec);
//     TestUtils.execSql(TestConstants.insHrgList);
//     TestUtils.execSql(TestConstants.insSitting);
//     TestUtils.execSql(TestConstants.insShHrg1);
//     TestUtils.execSql(TestConstants.insShHrg2);
//     TestUtils.execSql(TestConstants.insSchedHearDef);
//     TestUtils.execSql(TestConstants.insJudge);
//     TestUtils.execSql(TestConstants.insShAtt);
//     TestUtils.execSql(TestConstants.insShJudge);
//     TestUtils.execSql(TestConstants.insRefLegRep1);
//     TestUtils.execSql(TestConstants.insRefLegRep2);
//     TestUtils.execSql(TestConstants.insShLegRep1);
//     TestUtils.execSql(TestConstants.insShLegRep2);
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the VOs <<<<<<<<<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the DefHearingRecord <<<<<<<<<<<<<<<<<<<<<<");
//      //set up the VO's
//      DefHearingRecordMaintainer defHrgRecMaintainer = new DefHearingRecordMaintainer();
//      DefHearingRecord entity = (DefHearingRecord)defHrgRecMaintainer.findByPrimaryKey(TestConstants.defHrgRecID);
//      defHrgValue = new DefHearingRecordValue(entity.getHearingRecordId(), entity.getVersion());
//      defHrgValue.setAdjournedDate(entity.getAdjournedDate());
//      defHrgValue.setDateBailApplication(entity.getDateBailApplication());
//      defHrgValue.setDefendantOnCaseID(entity.getDefendantOnCaseId());
//      defHrgValue.setEndBailStatus(entity.getEndBailStatus());
//      defHrgValue.setHearingDateFreetext1(entity.getHearingDateFreeTxt1());
//      defHrgValue.setHearingDateFreetext2(entity.getHearingDateFreeTxt2());
//      //defHrgValue.setHearingDateFreetext3(entity.getHearingDateFreeTxt3());
//      defHrgValue.setHearingDateFreetext3("freetext text text text 333");
//      defHrgValue.setHearingID(entity.getHearingId());
//      defHrgValue.setIsAdjourned(entity.getIsAdjourned());
//      defHrgValue.setIsHraApplication(entity.getIsHraApplication());
//      defHrgValue.setNewBailStatus(entity.getNewBailStatus());
//      defHrgValue.setOralEvidence(entity.getOralEvidence());
//      defHrgValue.setRefAdjournmentID(entity.getRefAdjournmentId());
//      defHrgValue.setRefDefHearingTypeID(entity.getRefDefHearingTypeId());
//      defHrgValue.setResultBailApplication(entity.getResultBailApplication());
//      defHrgValue.setStartBailStatus(entity.getStartBailStatus());
//      defHrgValue.setStartDateNewBailStatus(entity.getStartDateNewBailStatus());
//      defHrgValue.setSubstBailApplication(entity.getSubstBailApplication());
//
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the Hearing <<<<<<<<<<<<<<<<<<<<<<");
//      HearingMaintainer hrgMaintainer = new HearingMaintainer();
//      Hearing hearing = (Hearing)hrgMaintainer.findByPK(TestConstants.hearingID);
//      hearingValue = hrgMaintainer.getHearingBasicValue(hearing);
//      hearingValue.setMpHearingType("P");
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the ShJudge <<<<<<<<<<<<<<<<<<<<<<");
//      ShJudgeMaintainer judgeMaintainer = new ShJudgeMaintainer();
//      ShJudge judge = (ShJudge)judgeMaintainer.findByPrimaryKey(TestConstants.shJudgeID);
//      log.debug("The judge entity is : " + judge.toString());
//      judgeValue = new HRSHJudgeValue(judge.getShJudgeId(), judge.getVersion());
//      judgeValue.setRefJudgeID(judge.getRefJudgeId());
//      judgeValue.setDeputyHCJ("Y");
//      judgeValue.setSHAttendeeID(judge.getShAttendeeId());
//      log.debug("The judge value is : " + judgeValue.toString());
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the first ShLegRep <<<<<<<<<<<<<<<<<<<<<<");
//      //get 2 shLegReps.
//      ShLegRepMaintainer legRepMaintainer = new ShLegRepMaintainer();
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set legRep1 <<<<<<<<<<<<<<<<<<<<<<");
//      ShLegRep legRep1 = (ShLegRep)legRepMaintainer.findByPrimaryKey(TestConstants.shLegRep1ID);
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set HRSHLegRepValue <<<<<<<<<<<<<<<<<<<<<<");
//      legRepValue1 = new HRSHLegRepValue(legRep1.getShLegRepId(), legRep1.getVersion());
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set setRefDefenceCategoryID <<<<<<<<<<<<<<<<<<<<<<");
//      legRepValue1.setRefDefenceCategoryID(legRep1.getRefDefenceCategoryId());
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set setRefLegRepID <<<<<<<<<<<<<<<<<<<<<<");
//      legRepValue1.setRefLegRepID(legRep1.getRefLegalRepId());
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the second ShLegRep <<<<<<<<<<<<<<<<<<<<<<");
//      ShLegRep legRep2 = (ShLegRep)legRepMaintainer.findByPrimaryKey(TestConstants.shLegRep2ID);
//      legRepValue2 = new HRSHLegRepValue(legRep2.getShLegRepId(), legRep2.getVersion());
//      //legRepValue2.setRefDefenceCategoryID(legRep2.getRefDefenceCategoryId());
//      legRepValue2.setRefDefenceCategoryID(new Integer(999));
//      legRepValue2.setRefLegRepID(legRep2.getRefLegalRepId());
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the legReps <<<<<<<<<<<<<<<<<<<<<<");
//      //set the legreps
//      Vector legReps = new Vector();
//      legReps.addElement(legRepValue1);
//      legReps.addElement(legRepValue2);
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue <<<<<<<<<<<<<<<<<<<<<<");
//      //set the final update value
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(defHrgValue) <<<<<<<<<<<<<<<<<<<<<<");
//      updateValue.setDefHearingRecordValue(defHrgValue);
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(hearingValue) <<<<<<<<<<<<<<<<<<<<<<");
//      updateValue.setHearingBasicValue(hearingValue);
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(judgeValue) <<<<<<<<<<<<<<<<<<<<<<");
//      updateValue.setHrSHJudgeValue(judgeValue);
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(legReps) <<<<<<<<<<<<<<<<<<<<<<");
//      updateValue.setHrSHLegRepValues((Collection)legReps);
//
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<,");
//  }
//
//  public void testUpdateHearingRecord()
//  {
//    log.debug(">>>>>>>>>>>> testUpdateHearingRecord Starts <<<<<<<<<<<<<");
//    HearingRecordWorkflow workflow = new HearingRecordWorkflow();
//
//    try
//    {
//      workflow.updateHearingRecord(updateValue);
//
//      //if get this far the update was successful else exception thrown and the
//      //test will be a failure.
//      this.assertTrue(true);
//
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUpdateHearingRecord Ends <<<<<<<<<<<<<,");
//    }
//  }
//}