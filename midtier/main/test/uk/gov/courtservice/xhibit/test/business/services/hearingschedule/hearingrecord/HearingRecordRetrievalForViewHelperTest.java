//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordRetrievalForViewHelper;
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
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRStartEndDates;
//
///**
// * <p>Title: HearingRecordRetrievalForViewHelperTest</p>
// * <p>Description: This is a test to get all the information for view
// * of hearing record. </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version $Id: HearingRecordRetrievalForViewHelperTest.java,v 1.13 2006/07/11 14:17:00 xzfdtb Exp $
// */
//public class HearingRecordRetrievalForViewHelperTest extends TransactionTestCase
//{
//
//  private static final Logger log = CSServices.getLogger(HearingRecordRetrievalForViewHelperTest.class);
//
//  //flag to switch on and off database inserts. Allows read from 'real data' database without corrupting it with test data
//  private boolean modify_flag = true;
//
//  public HearingRecordRetrievalForViewHelperTest(String s) throws NamingException
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
//
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//    try
//    {
//      if(modify_flag == true)
//      {
//        //delete the data
//        TestUtils.execSql(TestConstants.DEL_HEARING_LEG_REP1);
//        TestUtils.execSql(TestConstants.DEL_HEARING_LEG_REP2);
//        TestUtils.execSql(TestConstants.delExportA);
//        TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//        TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//        TestUtils.execSql(TestConstants.DEL_ADV);
//        TestUtils.execSql(TestConstants.DEL_CHAMBER);
//        TestUtils.execSql(TestConstants.delShLegRep);
//        TestUtils.execSql(TestConstants.delSchedHearDef);
//        TestUtils.execSql(TestConstants.delRefLegRep);
//        TestUtils.execSql(TestConstants.delShJudge);
//        TestUtils.execSql(TestConstants.delShAtt);
//        TestUtils.execSql(TestConstants.delJudge);
//        TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//        TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//        TestUtils.execSql(TestConstants.delShHrg1);
//        TestUtils.execSql(TestConstants.delSitting);
//        TestUtils.execSql(TestConstants.delHrgList);
//        TestUtils.execSql(TestConstants.delDefHrgRec);
//        TestUtils.execSql(TestConstants.delHrg);
//        TestUtils.execSql(TestConstants.delRefHrgType);
//        TestUtils.execSql(TestConstants.delDefOnOffence);
//        TestUtils.execSql(TestConstants.delDefOnCase);
//        TestUtils.execSql(TestConstants.DEL_CR);
//        TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//        TestUtils.execSql(TestConstants.delCase);
//        TestUtils.execSql(TestConstants.DEL_CASES);
//        TestUtils.execSql(TestConstants.DEL_DEF_REF);
//        TestUtils.execSql(TestConstants.delDef);
//        TestUtils.execSql(TestConstants.delSolFirm);
//        TestUtils.execSql(TestConstants.delCCInfo);
//        TestUtils.execSql(TestConstants.delRefCourt);
//        TestUtils.execSql(TestConstants.delCourtRoom);
//        TestUtils.execSql(TestConstants.delCourtSite);
//        TestUtils.execSql(TestConstants.delAddr);
//        TestUtils.execSql(TestConstants.delCourt);
//
//
//        //insert the data
//        TestUtils.execSql(TestConstants.insCourt);
//        TestUtils.execSql(TestConstants.insAddr);
//        TestUtils.execSql(TestConstants.insCourtSite);
//        TestUtils.execSql(TestConstants.insCourtRoom);
//        TestUtils.execSql(TestConstants.insRefCourt);
//        TestUtils.execSql(TestConstants.insDef);
//        TestUtils.execSql(TestConstants.INS_DEL_REF1);
//        TestUtils.execSql(TestConstants.INS_DEL_REF2);
//        TestUtils.execSql(TestConstants.insCase);
//        TestUtils.execSql(TestConstants.insCase2);
//        TestUtils.execSql(TestConstants.insDefOnCase);
//        TestUtils.execSql(TestConstants.insSolFirm);
//        TestUtils.execSql(TestConstants.insCCInfo);
//        TestUtils.execSql(TestConstants.insRefHrgType);
//        TestUtils.execSql(TestConstants.insHrg);
//        TestUtils.execSql(TestConstants.insHrg2);
//        TestUtils.execSql(TestConstants.insDefHrgRec);
//        TestUtils.execSql(TestConstants.insHrgList);
//        TestUtils.execSql(TestConstants.insSitting);
//        TestUtils.execSql(TestConstants.insShHrg1);
//        TestUtils.execSql(TestConstants.insShHrg2);
//        TestUtils.execSql(TestConstants.insShHrg3);
//        TestUtils.execSql(TestConstants.INS_CR_FIRM);//
//        TestUtils.execSql(TestConstants.INS_CR1);//
//        TestUtils.execSql(TestConstants.INS_SH_ATT_CR1);//
//        TestUtils.execSql(TestConstants.insSchedHearDef);
//        TestUtils.execSql(TestConstants.insJudge);
//        TestUtils.execSql(TestConstants.insShAtt);
//        //test the creation of the judge by disable this insert.
//        //TestUtils.execSql(TestConstants.insShJudge);
//        TestUtils.execSql(TestConstants.insRefLegRep1);
//        TestUtils.execSql(TestConstants.insRefLegRep2);
//        TestUtils.execSql(TestConstants.insShLegRep1);
//        TestUtils.execSql(TestConstants.insShLegRep2);
//        TestUtils.execSql(TestConstants.insExportA);
//        TestUtils.execSql(TestConstants.INS_SOL_FIRM);
//        TestUtils.execSql(TestConstants.INS_SOLICITOR);
//        TestUtils.execSql(TestConstants.INS_CHAMBER);
//        TestUtils.execSql(TestConstants.INS_ADV);
//        TestUtils.execSql(TestConstants.INS_HEARING_LEG_REP1_01);
//        TestUtils.execSql(TestConstants.INS_HEARING_LEG_REP1_02);
//      }
//      else
//      {
//        log.debug(">>>> WARNING: DID NOT INSERT TEST DATA ");
//      }
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
//
//  public void testRetrieveHearingRecord()
//  {
//    log.debug(">>>>>>>>>>>> testRetrieveHearingRecord Starts <<<<<<<<<<<<<<");
//
//
//    HearingRecordRetrievalForViewHelper hearingrecordretrievalforviewhelper =
//        new HearingRecordRetrievalForViewHelper();
//
//    try
//    {
//      //define some test data
//      Integer test_hearingID = new Integer(102);
//      Integer test_defID = new Integer(147);
//
//      HearingRecordValue hearingrecordvalueRet = null;
//      if(modify_flag == true)
//      {
//        //just use the regular test data
//        hearingrecordvalueRet = hearingrecordretrievalforviewhelper.
//            retrieveHearingRecord(TestConstants.hearingID, TestConstants.defID);
//      }
//      else
//      {
//        //use something that is in the 'real_data' database
//        hearingrecordvalueRet = hearingrecordretrievalforviewhelper.retrieveHearingRecord(test_hearingID, test_defID);
//      }
//
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
//
//        if( counsel.getStartEndDatesArray().length == 0 ) {
//            log.debug("NO ATTENDANCE DATES");
//        }
//        else {
//          log.debug("attendance dates:");
//          for( int x = 0; x < counsel.getStartEndDatesArray().length; x++ ) {
//              HRStartEndDates item = counsel.getStartEndDatesArray()[x];
//              log.debug("from "+item.getStartDate()+" thru "+item.getEndDate());
//          }
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
//      log.debug("getCollectMagistrateCourtId : " + defValue.getCollectMagistrateCourtId());
//      log.debug("getCollectMagistrateCourtName : " + defValue.getCollectMagistrateCourtName());
//
//
//      HRHearingDisplayValue hrHearingValue = disValue.getHrHearingDisplayValue();
//      log.debug("HRHearingDisplayValue : " + hrHearingValue.toString());
//      //log.debug("getDuration : " + hrHearingValue.getDuration());
//      //log.debug("getEndDate : " + hrHearingValue.getEndDate());
//      log.debug("getHearingID : " + hrHearingValue.getHearingID());
//      //log.debug("getStartDate : " + hrHearingValue.getStartDate());
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
//      log.debug(">>>>>>>>>>>> testRetrieveHearingRecord Ends <<<<<<<<<<<<<<");
//    }
//  }
//}
//