//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Vector;
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
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudge;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordRetrievalForViewHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordUpdateHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCaseValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCourtReporterValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingDisplayValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJudgeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJusticeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRLinkedCaseListValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRScheduledHearingValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordDisplayValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
//
///**
// * <p>Title: HearingRecordUpdateHelperTest</p>
// * <p>Description: Test the update of the HearingRecordHelper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version $Id: HearingRecordUpdateHelperTest.java,v 1.16 2006/07/11 14:17:00 xzfdtb Exp $
// */
//public class HearingRecordUpdateHelperTest extends TransactionTestCase
//{
//
//  private static final Logger log  = CSServices.getLogger(HearingRecordUpdateHelperTest.class);
//
//  //ASSUME THE FOLLOWING EXIST
//  //COURT_ID = 1
//  //REF_COURT_ID = 1
//  //COURT_ROOM_ID = 1
//  //COURT_SITE_ID = 1
//
//
//  //keys
//
//  private HearingRecordUpdateValue updateValue = new HearingRecordUpdateValue();
//  private DefHearingRecordValue defHrgValue = null;
//  private HearingBasicValue hearingBasicValue = null;
//  private HRSHJudgeValue judgeValue = null;
//  private HRSHLegRepValue legRepValue1 = null;
//  private HRSHLegRepValue legRepValue2 = null;
//
//
//  /**
//   * HearingRecordUpdateHelperTest
//   * @param s
//   */
//  public HearingRecordUpdateHelperTest(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//
//  /**
//   * setUp()
//   */
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//    try
//    {
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
//      //insert the data
//      TestUtils.execSql(TestConstants.insCourt);
//      TestUtils.execSql(TestConstants.insAddr);
//      TestUtils.execSql(TestConstants.insCourtSite);
//      TestUtils.execSql(TestConstants.insCourtRoom);
//      TestUtils.execSql(TestConstants.insRefCourt);
//      TestUtils.execSql(TestConstants.insDef);
//      TestUtils.execSql(TestConstants.INS_DEL_REF1);
//      TestUtils.execSql(TestConstants.INS_DEL_REF2);
//      TestUtils.execSql(TestConstants.insCase);
//      TestUtils.execSql(TestConstants.insCase2);
//      TestUtils.execSql(TestConstants.insDefOnCase);
//      TestUtils.execSql(TestConstants.insSolFirm);
//      TestUtils.execSql(TestConstants.insCCInfo);
//      TestUtils.execSql(TestConstants.insRefHrgType);
//      TestUtils.execSql(TestConstants.insHrg);
//      TestUtils.execSql(TestConstants.insHrg2);
//      TestUtils.execSql(TestConstants.insDefHrgRec);
//      TestUtils.execSql(TestConstants.insHrgList);
//      TestUtils.execSql(TestConstants.insSitting);
//      TestUtils.execSql(TestConstants.insShHrg1);
//      TestUtils.execSql(TestConstants.insShHrg2);
//      TestUtils.execSql(TestConstants.insShHrg3);
//      TestUtils.execSql(TestConstants.INS_CR_FIRM);//
//      TestUtils.execSql(TestConstants.INS_CR1);//
//      TestUtils.execSql(TestConstants.INS_SH_ATT_CR1);//
//      TestUtils.execSql(TestConstants.insSchedHearDef);
//      TestUtils.execSql(TestConstants.insJudge);
//      TestUtils.execSql(TestConstants.insShAtt);
//      //test the creation of the judge by disable this insert.
//      TestUtils.execSql(TestConstants.insShJudge);
//      TestUtils.execSql(TestConstants.insRefLegRep1);
//      TestUtils.execSql(TestConstants.insRefLegRep2);
//      TestUtils.execSql(TestConstants.insShLegRep1);
//      TestUtils.execSql(TestConstants.insShLegRep2);
//      TestUtils.execSql(TestConstants.insExportA);
//      TestUtils.execSql(TestConstants.INS_SOL_FIRM);
//      TestUtils.execSql(TestConstants.INS_SOLICITOR);
//      TestUtils.execSql(TestConstants.INS_CHAMBER);
//      TestUtils.execSql(TestConstants.INS_ADV);
//
//      log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the VOs <<<<<<<<<<<<<<<<<<<<<<");
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the DefHearingRecord <<<<<<<<<<<<<<<<<<<<<<");
//            //set up the VO's
//            DefHearingRecordMaintainer defHrgRecMaintainer = new DefHearingRecordMaintainer();
//            DefHearingRecord entity = defHrgRecMaintainer.findByPrimaryKey(TestConstants.defHrgRecID);
//            defHrgValue = new DefHearingRecordValue(entity.getHearingRecordId(), entity.getVersion());
//            defHrgValue.setAdjournedDate(entity.getAdjournedDate());
//            defHrgValue.setDateBailApplication(entity.getDateBailApplication());
//            defHrgValue.setDefendantOnCaseID(entity.getDefendantOnCaseId());
////            defHrgValue.setEndBailStatus(entity.getEndBailStatus());
//            //set the endBailStatus to something different to test X12B059
//            defHrgValue.setEndBailStatus("J");
//            defHrgValue.setHearingDateFreetext1(entity.getHearingDateFreeTxt1());
//            defHrgValue.setHearingDateFreetext2(entity.getHearingDateFreeTxt2());
//            //defHrgValue.setHearingDateFreetext3(entity.getHearingDateFreeTxt3());
//            defHrgValue.setHearingDateFreetext3("freetext text text text 333");
//            defHrgValue.setHearingID(entity.getHearingId());
//            defHrgValue.setIsAdjourned(entity.getIsAdjourned());
//            defHrgValue.setIsHraApplication(entity.getIsHraApplication());
//            defHrgValue.setNewBailStatus(entity.getNewBailStatus());
//            defHrgValue.setOralEvidence(entity.getOralEvidence());
//            defHrgValue.setRefAdjournmentID(entity.getRefAdjournmentId());
//            defHrgValue.setRefDefHearingTypeID(entity.getRefDefHearingTypeId());
//            defHrgValue.setResultBailApplication(entity.getResultBailApplication());
//            defHrgValue.setStartBailStatus(entity.getStartBailStatus());
//            defHrgValue.setStartDateNewBailStatus(entity.getStartDateNewBailStatus());
//            defHrgValue.setSubstBailApplication(entity.getSubstBailApplication());
//
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the Hearing <<<<<<<<<<<<<<<<<<<<<<");
//            HearingMaintainer hrgMaintainer = new HearingMaintainer();
//            Hearing hearing = hrgMaintainer.findByPK(TestConstants.hearingID);
//            hearingBasicValue = hrgMaintainer.getHearingBasicValue(hearing);
//            hearingBasicValue.setMpHearingType("P");
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the ShJudge <<<<<<<<<<<<<<<<<<<<<<");
//            ShJudgeMaintainer judgeMaintainer = new ShJudgeMaintainer();
//            ShJudge judge = judgeMaintainer.findByPrimaryKey(TestConstants.shJudgeID);
//            log.debug("The judge entity is : " + judge.toString());
//            judgeValue = new HRSHJudgeValue(judge.getShJudgeId(), judge.getVersion());
//            judgeValue.setRefJudgeID(judge.getRefJudgeId());
//            judgeValue.setDeputyHCJ("Y");
//            judgeValue.setSHAttendeeID(judge.getShAttendeeId());
//            log.debug("The judge value is : " + judgeValue.toString());
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the first ShLegRep <<<<<<<<<<<<<<<<<<<<<<");
//            //get 2 shLegReps.
//            ShLegRepMaintainer legRepMaintainer = new ShLegRepMaintainer();
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set legRep1 <<<<<<<<<<<<<<<<<<<<<<");
//            ShLegRep legRep1 = legRepMaintainer.findByPrimaryKey(TestConstants.shLegRep1ID);
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set HRSHLegRepValue <<<<<<<<<<<<<<<<<<<<<<");
//            legRepValue1 = new HRSHLegRepValue(legRep1.getShLegRepId(), legRep1.getVersion());
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set setRefDefenceCategoryID <<<<<<<<<<<<<<<<<<<<<<");
//            legRepValue1.setRefDefenceCategoryID(legRep1.getRefDefenceCategoryId());
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set setRefLegRepID <<<<<<<<<<<<<<<<<<<<<<");
//            legRepValue1.setRefLegRepID(legRep1.getRefLegalRepId());
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the second ShLegRep <<<<<<<<<<<<<<<<<<<<<<");
//            ShLegRep legRep2 = legRepMaintainer.findByPrimaryKey(TestConstants.shLegRep2ID);
//            legRepValue2 = new HRSHLegRepValue(legRep2.getShLegRepId(), legRep2.getVersion());
//            //legRepValue2.setRefDefenceCategoryID(legRep2.getRefDefenceCategoryId());
//            legRepValue2.setRefDefenceCategoryID(new Integer(999));
//            legRepValue2.setRefLegRepID(legRep2.getRefLegalRepId());
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the legReps <<<<<<<<<<<<<<<<<<<<<<");
//            //set the legreps
//            Vector legReps = new Vector();
//            legReps.addElement(legRepValue1);
//            legReps.addElement(legRepValue2);
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue <<<<<<<<<<<<<<<<<<<<<<");
//            //set the final update value
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(defHrgValue) <<<<<<<<<<<<<<<<<<<<<<");
//            updateValue.setDefHearingRecordValue(defHrgValue);
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(hearingValue) <<<<<<<<<<<<<<<<<<<<<<");
//            updateValue.setHearingBasicValue(hearingBasicValue);
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(judgeValue) <<<<<<<<<<<<<<<<<<<<<<");
//            updateValue.setHrSHJudgeValue(judgeValue);
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the UpdateValue(legReps) <<<<<<<<<<<<<<<<<<<<<<");
//            updateValue.setHrSHLegRepValues(legReps);
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
//    HearingRecordUpdateHelper hearingrecordupdatehelper = new HearingRecordUpdateHelper();
//
//    try
//    {
//      hearingrecordupdatehelper.updateHearingRecord(updateValue);
//
//      //if get this far the update was successful else exception thrown and the
//      //test will be a failure.
//      assertTrue(true);
//
//      //to help fix bug X12B059
//      //it would be nice at this stage to do a retrieve and ensure that the data was as expected.
//      //this is just replicating what was done in the HearingRecordRetrievalHelperTest
//      HearingRecordRetrievalForViewHelper hearingrecordretrievalforviewhelper = new HearingRecordRetrievalForViewHelper();
//
//      DefHearingRecordMaintainer defHrgRecMaintainer = new DefHearingRecordMaintainer();
//      DefHearingRecord entity = defHrgRecMaintainer.findByPrimaryKey(TestConstants.defHrgRecID);
//      Integer hearingID = entity.getHearingId();
//      Integer defendantID = TestConstants.defID;
//
//      log.debug("Checking the update with new values: hearingID = " + hearingID);
//      log.debug("Checking the update with new values: defendantID : " + defendantID);
//      HearingRecordValue hearingrecordvalueRet = hearingrecordretrievalforviewhelper.
//          retrieveHearingRecord(hearingID, TestConstants.defID);
//
//      //now that the retrieval has been done, just check that it has the values we expect
//      log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>||<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>||<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>||<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>> Start the printing of the values <<<<<<<<<<<<<<");
//
//      HearingRecordDisplayValue disValue = hearingrecordvalueRet.getHearingRecordDisplayValue();
//      log.debug("HearingRecordDisplayValue : " + disValue.toString());
//
//      String courtClerk = hearingrecordvalueRet.getCourtClerkExporter();
//      log.debug("courtClerk : " + courtClerk);
//
//      Integer exportID = hearingrecordvalueRet.getExportID();
//      log.debug("exportID : " + exportID.toString());
//
//      String status = hearingrecordvalueRet.getExportStatus();
//      log.debug("status : " + status);
//
//      Collection counsels = disValue.getHrCounselValue();
//      Iterator it1 = counsels.iterator();
//      while(it1.hasNext())
//      {
//        HRCounselValue counsel = (HRCounselValue)it1.next();
//        log.debug("COUNSEL");
//        log.debug("getBarNumber : "+counsel.getBarNumber());
//        log.debug("getFirstName : "+counsel.getFirstName());
//        log.debug("getLegalRepType : "+counsel.getLegalRepType());
//        log.debug("getLegalRole : "+counsel.getLegalRole());
//        log.debug("getMiddleName : "+counsel.getMiddleName());
//        log.debug("getRefAdvocateOrSolicitorID : "+counsel.getRefAdvocateOrSolicitorID());
//        log.debug("getRefChamberOrSolicitorFirmID : "+counsel.getRefChamberOrSolicitorFirmID());
//        log.debug("getRefLegalRepID : "+counsel.getRefLegalRepID());
//        log.debug("getSurname : "+counsel.getSurname());
//
//        if(counsel.getAddressBasicValue() != null)
//        {
//          log.debug("Counsel Address : "+counsel.getAddressBasicValue().toString());
//        }
//      }
//
//      Collection linkedCases = disValue.getHrLinkedCaseListValue();
//      if(linkedCases != null)
//      {
//        Iterator it2 = linkedCases.iterator();
//        while(it2.hasNext())
//        {
//          HRLinkedCaseListValue caselinked = (HRLinkedCaseListValue)it2.next();
//          log.debug("linkedCases : "+caselinked.toString());
//          log.debug("getCaseID : "+caselinked.getCaseID());
//          log.debug("getCaseNumber : "+caselinked.getCaseNumber());
//          log.debug("getCaseType : "+caselinked.getCaseType());
//        }
//      }
//
//      HRCaseValue caseValue = disValue.getHrCaseValue();
//      log.debug("HRCaseValue : " + caseValue.toString());
//      log.debug("getCaseID : " + caseValue.getCaseID());
//      log.debug("getCaseNumber : " + caseValue.getCaseNumber());
//      log.debug("getCaseSubType : " + caseValue.getCaseSubType());
//      log.debug("getCaseType : " + caseValue.getCaseType());
//      log.debug("getCrestSeveredInd : " + caseValue.getCrestSeveredInd());
//      log.debug("getEstPDHTrialLength : " + caseValue.getEstPDHTrialLength());
//      log.debug("getLengthTape : " + caseValue.getLengthTape());
//      log.debug("getNoPageProsEvidence : " + caseValue.getNoPageProsEvidence());
//      log.debug("getNoProsWitness : " + caseValue.getNoProsWitness());
//
//      HRDefendantValue defValue = disValue.getHrDefendantValue();
//      log.debug("HRDefendantValue : " + defValue.toString());
//      log.debug("getCroNumber : " + defValue.getCroNumber());
//      log.debug("getDefendantID : " + defValue.getDefendantID());
//      log.debug("getDefHearingRecordID : " + defValue.getDefHearingRecordID());
//      log.debug("getDriverNumber : " + defValue.getDriverNumber());
//      log.debug("getFinalDrivingLicenseStatus : " + defValue.getFinalDrivingLicenseStatus());
//      log.debug("getFirstName : " + defValue.getFirstName());
//      log.debug("getLastConvictionDate : " + defValue.getLastConvictionDate());
//      log.debug("getMiddleName : " + defValue.getMiddleName());
//      log.debug("getNoOfTICs : " + defValue.getNoOfTICs());
//      log.debug("getSurname : " + defValue.getSurname());
//
//
//      HRHearingDisplayValue hrHearingValue = disValue.getHrHearingDisplayValue();
//      log.debug("HRHearingDisplayValue : " + hrHearingValue.toString());
//      log.debug("getHearingID : " + hrHearingValue.getHearingID());
//
//
//      Collection justices = disValue.getHrHearingDisplayValue().getHrJusticeValues();
//      Iterator it3 = justices.iterator();
//      while(it3.hasNext())
//      {
//        HRJusticeValue justice = (HRJusticeValue)it3.next();
//        log.debug("justices : "+justice.toString());
//        log.debug("getHearingID : "+justice.getHearingID());
//        log.debug("getJusticeName : "+justice.getJusticeName());
//        log.debug("getShJusticeID : "+justice.getShJusticeID());
//      }
//
//      Collection schedules = disValue.getHrHearingDisplayValue().getHrScheduledHearingValues();
//      Iterator it4 = schedules.iterator();
//      while(it4.hasNext())
//      {
//        HRScheduledHearingValue schedule = (HRScheduledHearingValue)it4.next();
//        log.debug("schedules : "+schedule.toString());
//        log.debug("getOriginalTime : "+schedule.getOriginalTime());
//        log.debug("getScheduledHearingId : "+schedule.getScheduledHearingId());
//
//        Collection courtReps = schedule.getHrCourtReporter();
//        if(courtReps != null)
//        {
//          Iterator it4a = courtReps.iterator();
//          while(it4a.hasNext())
//          {
//            HRCourtReporterValue reporter = (HRCourtReporterValue)it4a.next();
//            log.debug("HRCourtReporterValue : "+reporter.toString());
//            log.debug("getFirstName : "+reporter.getFirstName());
//            log.debug("getMiddleName : "+reporter.getMiddleName());
//            log.debug("getRefCourtReporterID : "+reporter.getRefCourtReporterID());
//            log.debug("getSurname : "+reporter.getSurname());
//          }
//        }
//        else
//        {
//          log.debug("NO COURT REPORTERS");
//        }
//      }
//
//      HRJudgeValue hrjudge = disValue.getHrHearingDisplayValue().getHrJudgeValue();
//      log.debug("HRJudgeValue : " + hrjudge.toString());
//      log.debug("getJudgeFirstName : " + hrjudge.getJudgeFirstName());
//      log.debug("getJudgeFullListTitle1 : " + hrjudge.getJudgeFullListTitle1());
//      log.debug("getJudgeFullListTitle2 : " + hrjudge.getJudgeFullListTitle2());
//      log.debug("getJudgeFullListTitle3 : " + hrjudge.getJudgeFullListTitle3());
//      log.debug("getJudgeMiddleName : " + hrjudge.getJudgeMiddleName());
//      log.debug("getJudgeSurname : " + hrjudge.getJudgeSurname());
//      log.debug("getRefJudgeID : " + hrjudge.getRefJudgeID());
//
//
//      //update values - no need to print all values one by one since using reflection.
//      HearingRecordUpdateValue updValue = hearingrecordvalueRet.getHearingRecordUpdateValue();
//      log.debug("HearingRecordUpdateValue : " + updValue.toString());
//
//      DefHearingRecordValue defHR = updValue.getDefHearingRecordValue();
//      log.debug("DefHearingRecordValue : " + defHR.toString());
//      log.debug("getAdjournedDate : " + defHR.getAdjournedDate());
//      log.debug("getDateBailApplication : " + defHR.getDateBailApplication());
//      log.debug("getDefendantOnCaseID : " + defHR.getDefendantOnCaseID());
//      log.debug("getEndBailStatus : " + defHR.getEndBailStatus());
//
//
//      //assert that the endBail status is as we expect it to be.
//      //X12BO59 Bug suggests that there is a problem either writing/retrieving
//
//
//
//      String actual = defHR.getEndBailStatus();
//      String expected = "J";  //entity.getEndBailStatus();
//
//      assertEquals("Expected the retrieved endBailStatus to be the same that was saved ", expected, actual);
//
//
//      log.debug("getHearingDateFreetext1 : " + defHR.getHearingDateFreetext1());
//      log.debug("getHearingDateFreetext2 : " + defHR.getHearingDateFreetext2());
//      log.debug("getHearingDateFreetext3 : " + defHR.getHearingDateFreetext3());
//      log.debug("getHearingID : " + defHR.getHearingID());
//      log.debug("getId : " + defHR.getId());
//
//
//      HearingBasicValue hearingBasicValue = updValue.getHearingBasicValue();
//      log.debug("HearingBasicValue : " + hearingBasicValue.toString());
//      log.debug("getId : " + hearingBasicValue.getId());
//      log.debug("getLinkedHearingID : " + hearingBasicValue.getLinkedHearingID());
//      log.debug("getMpHearingType : " + hearingBasicValue.getMpHearingType());
//
//      HRSHJudgeValue judge = updValue.getHrSHJudgeValue();
//      log.debug("HRSHJudgeValue : " + judge.toString());
//      log.debug("getDeputyHCJ : " + judge.getDeputyHCJ());
//      log.debug("getRefJudgeID : " + judge.getRefJudgeID());
//      log.debug("getSHAttendeeID : " + judge.getSHAttendeeID());
//      log.debug("getId : " + judge.getId());
//
//      Collection shlegreps = updValue.getHrSHLegRepValues();
//      Iterator it5 = shlegreps.iterator();
//      while(it5.hasNext())
//      {
//        HRSHLegRepValue value = (HRSHLegRepValue)it5.next();
//        log.debug("HRSHLegRepValue : " + value.toString());
//        log.debug("getRefDefenceCategoryID : " + value.getRefDefenceCategoryID());
//        log.debug("getRefLegRepID : " + value.getRefLegRepID());
//        log.debug("getId : " + value.getId());
//      }
//
//      assertTrue(true);
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
//