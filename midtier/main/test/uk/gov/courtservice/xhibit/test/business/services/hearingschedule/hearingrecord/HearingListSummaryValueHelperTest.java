//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.Collection;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingListSummaryValueHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
//
//
///**
// * Test for HearingRecordHearingListSummaryValueHelper
// *
// * @author Anthony Martin
// * @version 1.1
// */
//public class HearingListSummaryValueHelperTest extends TransactionTestCase {
//
////************
//private static Logger log  = CSServices.getLogger(HearingListSummaryValueHelperTest.class);
//
////keys
//
//private Integer UTypeCaseID = new Integer(-902);
//
//private Integer linkedHearingID = new Integer(-905);
//private Integer erroneousLinkedHearingID = new Integer(-906);
//
///** different than standard inserts and deletes **/
//private String delTypeUCase = "delete from xhb_case where case_id = " + UTypeCaseID.intValue();
//private String insTypeUCase = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+UTypeCaseID.intValue()+", 20030310, 'U', " + TestConstants.refCourtID + ", " + TestConstants.courtID + ")";
//private String delLinkedHrg = "delete from xhb_hearing where hearing_id =  "+linkedHearingID.intValue();
//private String delErroneousLinkedHrg = "delete from xhb_hearing where hearing_id = " + erroneousLinkedHearingID.intValue();
//private String insLinkedHrg = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) "+
//                        "values ("+linkedHearingID.intValue()+", "+ TestConstants.caseID + ", "+  TestConstants.refHrgTypeID + ", "+ TestConstants.courtID + ", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , " + TestConstants.linkedHearingId + ")";
//
//private String insErroneousLinkedHrg = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) "+
//                        "values ("+erroneousLinkedHearingID.intValue()+", "+ TestConstants.caseID + ", " + TestConstants.refHrgTypeID + ", " + TestConstants.courtID + ", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 777)";
///** different than standard inserts and deletes*/
//
//
//private HearingRecordUpdateValue updateValue = new HearingRecordUpdateValue();
//private DefHearingRecordValue defHrgValue = null;;
//private HRHearingValue hearingValue = null;
//private HRSHJudgeValue judgeValue = null;
//private HRSHLegRepValue legRepValue1 = null;
//private HRSHLegRepValue legRepValue2 = null;
//
//private boolean modify_database = true;  //flag to indicate whether test should modify database or not
//
//
//    public HearingListSummaryValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//  /**
//   * setUp()
//   */
//  protected void setUp() throws Exception
//  {
//      super.setUp();
//
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//    try
//    {
//      if(modify_database == true)
//      {
//        //delete the data
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
//        TestUtils.execSql(delLinkedHrg);   //non-standard
//        TestUtils.execSql(delErroneousLinkedHrg);   //non-standard
//        TestUtils.execSql(TestConstants.delDefHrgRec);
//        TestUtils.execSql(TestConstants.delHrg);
//        TestUtils.execSql(TestConstants.delRefHrgType);
//        TestUtils.execSql(TestConstants.delDefOnCase);
//        TestUtils.execSql(TestConstants.DEL_CR);
//        TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//        TestUtils.execSql(TestConstants.delCase);
//        TestUtils.execSql(TestConstants.DEL_CASES);
//        TestUtils.execSql(delTypeUCase);      //one of the non standard ones
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
//        TestUtils.execSql(insTypeUCase);   //non-standard
//        TestUtils.execSql(TestConstants.insDefOnCase);
//        TestUtils.execSql(TestConstants.insSolFirm);
//        TestUtils.execSql(TestConstants.insCCInfo);
//        TestUtils.execSql(TestConstants.insRefHrgType);
//        TestUtils.execSql(TestConstants.insHrg);
//        TestUtils.execSql(insLinkedHrg); //non-standard
//        TestUtils.execSql(insErroneousLinkedHrg); //non-standard
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
//      }
//      else
//      {
//        log.debug("WARNING - test did not insert any data to database");
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
//   /**
//     * testFindAllLinkedHearings
//     */
//    public void testFindAllLinkedHearings() {
//        try {
//          //Case1:
//            log.debug("Case 1: Find linked hearings for a linked hearing that doesn't exist");
//            Integer linked_hearing_id = new Integer(-485);    //assuming that this doesn't exist in the DB
//            HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//            Collection linkedHearings = helper.findAllLinkedHearings(linked_hearing_id);
//            assertTrue("Case 1: expected size is 0: actual size is " + linkedHearings.size(),
//                       linkedHearings.size() == 0);
//            //Case2:
//            log.debug("Case 2: Find linked hearings for a hearing with three linked hearings.");
//            log.debug("predicted result: 3");
//            linkedHearings = helper.findAllLinkedHearings(TestConstants.linkedHearingId);
//            assertNotNull(linkedHearings);
//            assertTrue("Case 2:expected there to be three linked hearings: actual is " + linkedHearings.size(),
//                       linkedHearings.size() == 3);
//            //Case3:
//            log.debug("Case 3:");
//            //Find linked hearings for a hearing
//            //that has an erroneous linked_hearing_id,
//            //i.e. there are no linked hearings matching the given linked hearing id
//            //this would mean that the test should return 1 basic value representing
//            //the hearing itself.
//             log.debug("predicted result: 1");
//             linkedHearingID = new Integer(777);    //as per insert of erroneousLinkedHearingId
//             linkedHearings = helper.findAllLinkedHearings(linkedHearingID);
//             assertNotNull(linkedHearings);
//             assertTrue("Case3: expected there to be one linked hearing: actual = " + linkedHearings.size(),
//                        linkedHearings.size() == 1);
//        }
//        catch(Exception e) {
//             log.debug(e.toString());
//             e.printStackTrace();
//             fail();
//        }
//    }
//
//    /**
//     * Getting a basic value object from the helper class using a hearingID.
//     * Want to assert that the returned object is a basicValueObject.
//     */
//    public void testFindHearingBasicValue() {
//        try {
//            log.debug("Test 4: Create a Basic Value from hearing_id.");
//            HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//            HearingBasicValue basic = helper.findHearingBasicValue(TestConstants.hearingID);
//
//            assertNotNull(basic);
//            assertTrue(basic instanceof HearingBasicValue);
//            assertNotNull(basic.getCaseID());
//            assertNotNull(basic.getRefHearingTypeID());
//            assertNotNull(basic.getCourtID());
//            assertNotNull(basic.getMpHearingType());
//            assertNotNull(basic.getLinkedHearingID());
//            assertNotNull(basic.getHearingStartDate());
//            assertNotNull(basic.getHearingEndDate());
//        }
//        catch(Exception e) {
//             log.debug(e.toString());
//             e.printStackTrace();
//             fail();
//        }
//    }
//
//    /**
//     *
//     * THIS IS A PRIVATE METHOD IN THE HearingListSummaryValueHelper.
//     *
//     * Case 1: DefHearingRecord exists for given hearingID and defendantOnCaseID.
//     * Just retrieve the HearingListSummaryValue and verify it has corresponding information included
//     *
//    public void testFindHearingListSummaryValue1()
//    {
//      try
//      {
//        DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//        Integer hearingId = TestConstants.hearingID;
//        Integer defendantOnCaseID = TestConstants.defOnCaseID;
//        DefHearingRecord expected_local = maintainer.findByDefendantOnCaseIDAndHearingID(defendantOnCaseID, hearingId);
//        log.debug("retrieved the record from the maintainer");
//        //this shows us the record exists
//
//        //now retrieve record using our method
//        HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//        HearingMaintainer hearingMaintainer = new HearingMaintainer();
//        Hearing hearingLocal = hearingMaintainer.findByPK(hearingId);
//        HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearingLocal);
//        log.debug("Expected hearingBasicValue " + hbv);
//        //the method we want to test
//        HearingListSummaryValue hslv = helper.findHearingListSummaryValue(hbv);
//        //want to show that hearingIds are the same
//        log.debug("expected " + hearingId);
//        log.debug("actual " + hslv.getHearingID());
//        assertTrue(hslv.getHearingID().equals(hearingId));
//        //want to show that defendants are as expected
//        Collection hrDefendants = hslv.getHrDefendantValues();
//        Iterator iterator = hrDefendants.iterator();
//        HRDefendantValue defendantValue = null;
//        Integer defHearingRecordID = null;
//        boolean flag = false;
//        log.debug("Iterating over defendantValues - expecting : " + expected_local.getHearingRecordId());
//        while(iterator.hasNext())
//        {
//          defendantValue = (HRDefendantValue)iterator.next();
//          defHearingRecordID = defendantValue.getDefHearingRecordID();
//          log.debug("trying to match with " + defHearingRecordID);
//          //show that the DefHearingRecord retrieved is the right one
//          if(defHearingRecordID.equals(expected_local.getHearingRecordId()))
//          {
//            flag = true;
//          }
//        }
//        assertTrue("Expecting the DefHearingRecordIds to be the same", flag);
//        //case id
//        assertEquals("Expecting Case id to be the same", hbv.getCaseID(), hslv.getCaseID());
//        //linked hearing id
//        assertEquals("Expecting linked hearing to be the same", hbv.getLinkedHearingID(), hslv.getLinkedHearingID());
//      }
//      catch(Exception e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//    */
//
//    /**
//     *
//     * THIS IS A PRIVATE METHOD IN THE HearingListSummaryValueHelper.
//     *
//     * case 2:
//     * the defHearingRecordID doesn't exist. We want it to be created
//     * we prove this by searching with maintainer with hearingId and defendantOnCaseID and getting nothing
//     * then call our findHearingListSummaryValue method
//     * do another search and show that it finds a record this time
//     * show that they have the same primary keys
//     *
//    public void testFindHearingListSummaryValue2()
//    {
//      try
//      {
//        //first of all, delete the record
//        ut.begin();
//        log.debug("\n\n\ndeleting record ..");
//        TestUtils.execSql("delete from xhb_def_hearing_record where hearing_id = " + TestConstants.hearingID.intValue());
//        ut.commit();
//        log.debug("\n\n\n deleted DefHrgRec record ");
//
//        //check that the record was properly deleted
//        DefHearingRecordMaintainer defHearingRecordMaintainer = new DefHearingRecordMaintainer();
//        boolean checkDeleted;
//        try
//        {
//          //try to find the record that has been deleted.
//          log.debug("\n\n\nChecking if the record was deleted <<<<<<<<<<<<<");
//          DefHearingRecord entity = defHearingRecordMaintainer.findByDefendantOnCaseIDAndHearingID(TestConstants.defOnCaseID, TestConstants.hearingID);
//          checkDeleted = false;  // this line is only reached if record exist, else exception failed
//        }
//        catch(Exception ex)
//        {
//          //this is expected
//          //record was deleted so objectnotfoundexception is thrown
//          checkDeleted = true;
//          log.debug("\n\n\n The record was not found so we know that it has been sucessfully removed.");
//        }
//
//        assertTrue("failed to delete the existing DefHearingRecord", checkDeleted);
//        //we now know that the record has been delted
//
//        //now try to retrieve record using our method
//        ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//        ut.begin();
//        HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//        HearingMaintainer hearingMaintainer = new HearingMaintainer();
//        Hearing hearingLocal = hearingMaintainer.findByPK(TestConstants.hearingID);
//        HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearingLocal);
//        log.debug("Expected hearingBasicValue " + hbv);
//
//        //the method we want to test
//        HearingListSummaryValue hslv = helper.findHearingListSummaryValue(hbv);
//        ut.commit();
//        log.debug("--------- succesfully called find hearingListSummaryValue and committed ------ ");
//        log.debug(" now test to see if the record was created properly");
//
//        //ideally the defHearingRecord should have been created as a 'side effect'
//        //do another search to prove that it now exists.
//        DefHearingRecord local = null;
//        try
//        {
//          local = defHearingRecordMaintainer.findByDefendantOnCaseIDAndHearingID(TestConstants.defOnCaseID, TestConstants.hearingID);
//        }
//        catch(ObjectNotFoundException e)
//        {
//          //this is not expected this time
//          e.printStackTrace();
//          fail();
//        }
//        log.debug("Have shown that record has been created");
//
//        //want to show that hearingIds are the same
//        log.debug("expected " + TestConstants.hearingID);
//        log.debug("actual " + hslv.getHearingID());
//        assertTrue(hslv.getHearingID().equals(TestConstants.hearingID));
//        //want to show that defendants are as expected
//        //showing that the newly created record is the same as the retrieved
//        Collection hrDefendants = hslv.getHrDefendantValues();
//        Iterator iterator = hrDefendants.iterator();
//        HRDefendantValue defendantValue = null;
//        Integer defHearingRecordID = null;
//        boolean flag = false;
//        log.debug("Iterating over defendantValues - expecting : " + local.getHearingRecordId());
//        while(iterator.hasNext())
//        {
//          defendantValue = (HRDefendantValue)iterator.next();
//          defHearingRecordID = defendantValue.getDefHearingRecordID();
//          log.debug("trying to match with " + defHearingRecordID);
//          //show that the DefHearingRecord retrieved is the right one
//          if(defHearingRecordID.equals(local.getHearingRecordId()))
//          {
//            flag = true;
//          }
//        }
//        assertTrue("Expecting the DefHearingRecordIds to be the same", flag);
//        //case id
//        assertEquals("Expecting Case id to be the same", hbv.getCaseID(), hslv.getCaseID());
//        //linked hearing id
//        assertEquals("Expecting linked hearing to be the same", hbv.getLinkedHearingID(), hslv.getLinkedHearingID());
//      }
//      catch(Exception e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }*/
//
//    /**
//     *
//     * THIS IS A PRIVATE METHOD IN THE HearingListSummaryValueHelper.
//     *
//     * case:3 DefHearingRecordID does not exist
//     * we provide a caseID that does not require creation
//     * show that the appropriate hslv returned, but
//     * should get a null for the DefHearingRecordID
//     * but the summary should be ok.
//     * this shows the DefHearingRecord was not created.
//     *
//    public void testFindHearingListSummaryValue3()
//    {
//      try
//      {
//        //show that our record does not already exist
//        DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//        Integer hearingId = new Integer(904);
//        Integer defendantOnCaseID = new Integer(8888);
//        boolean record_missing = false; //at start indeterminate if record exists
//        DefHearingRecord local = null;
//        try
//        {
//          local = maintainer.findByDefendantOnCaseIDAndHearingID(defendantOnCaseID, hearingId);
//        }
//        catch(ObjectNotFoundException e)
//        {
//          //this is expected
//          log.debug("Proved that the record was missing");
//          record_missing = true;  //we have shown record doesn't exist
//        }
//        assertTrue("Expected there to be no record in the database ", record_missing);
//
//        //create a basic value and use it to findHearingListSummaryValue
//        HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//        HearingMaintainer hearingMaintainer = new HearingMaintainer();
//        Hearing hearingLocal = hearingMaintainer.findByPK(hearingId);
//        HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearingLocal);
//        log.debug("Expected hearingBasicValue " + hbv);
//        //the method we want to test
//        HearingListSummaryValue hslv = helper.findHearingListSummaryValue(hbv);
//
//        //show that this has not resulted in a create
//        //trying to find the record and having it fail should 'prove' this
//        try
//        {
//          local = maintainer.findByDefendantOnCaseIDAndHearingID(defendantOnCaseID, hearingId);
//        }
//        catch(ObjectNotFoundException e)
//        {
//          //this is expected
//          log.debug("Proved that the record was missing");
//          record_missing = true;  //we have shown record doesn't exist
//        }
//        assertTrue("Expected there to be no record in the database ", record_missing);
//      }
//      catch(Exception e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//    */
//
//
//    /**
//     *
//     * THIS IS A PRIVATE METHOD IN THE HearingListSummaryValueHelper.
//     *
//     *
//     * Getting summaryValueObject for a given HearingID.
//     * Want to prove that this returns a valid HearingListSummaryValue
//     *
//     *
//    public void testFindHearingListSummaryValue() {
//        try {
//          //HearingBasicValue hearingBasicValue = makeHearingBasicValue();
//          //lets not create our own hearing basic value. Take it from the database
//          Integer hearingID = new Integer(904);
//          HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//          HearingBasicValue basic = helper.findHearingBasicValue(hearingID);
//          HearingListSummaryValue summary = helper.findHearingListSummaryValue(basic);
//
//          log.debug("basic.getCaseID " + basic.getCaseID());
//
//          log.debug("summary.getCaseID()" + summary.getCaseID());
//          log.debug("getCaseType() " + summary.getCaseType());
//          log.debug("getCaseSubType() " + summary.getCaseSubType());
//          log.debug("getCaseNumber() " + summary.getCaseNumber());
//          log.debug("getHearingID() " + summary.getHearingID());
//          log.debug("getRefHearingTypeID()" + summary.getRefHearingTypeID());
//          log.debug("getLinkedHearingID()" + summary.getLinkedHearingID());
//          log.debug("getRefHearingTypeCode()" + summary.getRefHearingTypeCode());
//          log.debug("getRefHearingTypeDesc()" + summary.getRefHearingTypeDesc());
//
//            assertNotNull("Summary should not be null", summary);
//            assertTrue(summary instanceof HearingListSummaryValue);
//            assertNotNull(summary.getCaseID());
//            assertNotNull(summary.getCaseType());
//            assertNotNull(summary.getCaseNumber());
//            assertNotNull(summary.getHearingID());
//            assertNotNull(summary.getRefHearingTypeID());
//            assertNotNull(summary.getLinkedHearingID());
//            assertNotNull(summary.getRefHearingTypeCode());
//            assertNotNull(summary.getRefHearingTypeDesc());
//            assertNotNull(summary.getHrDefendantValues());
//
//            Collection defs = summary.getHrDefendantValues();
//            Iterator defsIt = defs.iterator();
//            while(defsIt.hasNext()) {
//                HRDefendantValue def = (HRDefendantValue)defsIt.next();
//
//                assertNotNull(def);
//                assertTrue(def instanceof HRDefendantValue);
//
//               // with 'good' test data this will pass
//               // assertNotNull(def.getFirstName());
//               // assertNotNull(def.getSurname());
//               // assertNotNull(def.getDefHearingRecordID());
//            }
//        }
//        catch(Exception e) {
//             log.debug(e.toString());
//             e.printStackTrace();
//             fail();
//        }
//    }
//    */
//
//    /**
//     *
//     * THIS IS A PRIVATE METHOD IN THE HearingListSummaryValueHelper.
//     *
//     *
//    public void testForBugWhereIncorrectNumberOfDefendantsRetrieved()
//    {
//      try
//      {
//        DefHearingRecordMaintainer maintainer = new DefHearingRecordMaintainer();
//        Integer hearingId = new Integer(98);  //these are specific to this particular test
//        Integer defendantOnCaseID = new Integer(145);  //these are specific to this particular test
//        DefHearingRecord expected_local = maintainer.findByDefendantOnCaseIDAndHearingID(defendantOnCaseID, hearingId);
//        log.debug("retrieved the record from the maintainer");
//        //this shows us the record exists
//
//        //now retrieve record using our method
//        HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//        HearingMaintainer hearingMaintainer = new HearingMaintainer();
//        Hearing hearingLocal = hearingMaintainer.findByPK(hearingId);
//        HearingBasicValue hbv = hearingMaintainer.getHearingBasicValue(hearingLocal);
//        log.debug("Expected hearingBasicValue " + hbv);
//        //the method we want to test
//        HearingListSummaryValue hslv = helper.findHearingListSummaryValue(hbv);
//        //want to show that hearingIds are the same
//        log.debug("expected " + hearingId);
//        log.debug("actual " + hslv.getHearingID());
//        assertTrue(hslv.getHearingID().equals(hearingId));
//        //want to show that defendants are as expected
//        Collection hrDefendants = hslv.getHrDefendantValues();
//        Iterator iterator = hrDefendants.iterator();
//        HRDefendantValue defendantValue = null;
//        Integer defHearingRecordID = null;
//        log.debug("Iterating over defendantValues - expecting there to be three defendants");
//        int count = 0;
//        while(iterator.hasNext())
//        {
//          defendantValue = (HRDefendantValue)iterator.next();
//          log.debug("\n\n\n\n retrieved defendantID " + defendantValue.getDefendantID());
//          count++;
//        }
//        assert("Failed to retrieve the expected number of defendant values ", count == 3);
//      }
//      catch(Exception e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }*/
//}
//