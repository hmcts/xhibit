//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingListSummaryValueHelper;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.*;
//
//import javax.naming.NamingException;
//import java.util.Collection;
//import java.util.Iterator;
//
//public class HearingRecordRetrievalHelperTest extends TransactionTestCase {
//
//    public HearingRecordRetrievalHelperTest(String s) throws NamingException {
//        super(s, true);
//    }
//
//    //************
//    private static Logger log = CSServices.getLogger(HearingListSummaryValueHelperTest.class);
//
//
//    private String createLinksForFour = "update xhb_hearing set linked_hearing_id = 2 where hearing_id = 4";
//    private String createLinksForFive = "update xhb_hearing set linked_hearing_id = 2 where hearing_id = 5";
//    private String createLinksForSix = "update xhb_hearing set linked_hearing_id = 3 where hearing_id = 6";
//    private String destroyLinksForFour = "update xhb_hearing set linked_hearing_id = null where hearing_id = 4";
//    private String destroyLinksForFive = "update xhb_hearing set linked_hearing_id = null where hearing_id = 5";
//    private String destroyLinksForSix = "update xhb_hearing set linked_hearing_id = null where hearing_id = 6";
//
//
//    //keys
//    private Integer UTypeCaseID = new Integer(-902);
//    private Integer defOnCaseID_two = new Integer(-99999);
//    private Integer linkedHearingID = new Integer(-905);
//    private Integer erroneousLinkedHearingID = new Integer(-906);
//
//
//    //U Case
//    private String delTypeUCase = "delete from xhb_case where case_id = " + UTypeCaseID.intValue();
//    private String insTypeUCase = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values (" + UTypeCaseID.intValue() + ", 20030310, 'U', " + TestConstants.refCourtID.intValue() + ", " + TestConstants.courtID.intValue() + ")";
//
//    //DefOnCase 2
//    private String delDefOnCase_two = "delete from xhb_defendant_on_case where defendant_on_case_id = " + defOnCaseID_two.intValue();
//    private String insDefOnCase_two = "insert into xhb_defendant_on_case (defendant_on_case_id, case_id, defendant_id) values (" + defOnCaseID_two.intValue() + ", " + TestConstants.caseID.intValue() + ", " + TestConstants.defID.intValue() + ")";
//
//    //hearing
//    private String delLinkedHrg = "delete from xhb_hearing where hearing_id =  " + linkedHearingID.intValue();
//    private String delErroneousLinkedHrg = "delete from xhb_hearing where hearing_id = " + erroneousLinkedHearingID.intValue();
//
//    private String insLinkedHrg = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) " +
//            "values (" + linkedHearingID.intValue() + ", " + TestConstants.caseID.intValue() + ", " + TestConstants.refHrgTypeID.intValue() + ", " + TestConstants.courtID.intValue() + ", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//
//    private String insErroneousLinkedHrg = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) " +
//            "values (" + erroneousLinkedHearingID.intValue() + ", " + TestConstants.caseID.intValue() + ", " + TestConstants.refHrgTypeID.intValue() + ", " + TestConstants.courtID.intValue() + ", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 777)";
//
//
//    private HearingRecordUpdateValue updateValue = new HearingRecordUpdateValue();
//    private DefHearingRecordValue defHrgValue = null;;
//    private HRHearingValue hearingValue = null;
//    private HRSHJudgeValue judgeValue = null;
//    private HRSHLegRepValue legRepValue1 = null;
//    private HRSHLegRepValue legRepValue2 = null;
//
//
//    private boolean modify_flag = true;  //to indicate if DB may be modified during the test
//
//
//    /**
//     * setUp
//     */
//    protected void setUp() throws Exception {
//        super.setUp();
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//
//        try {
//            if (modify_flag == true) {
//                //delete the data
//                TestUtils.execSql(TestConstants.delExportA);
//                TestUtils.execSql(TestConstants.delShLegRep);
//                TestUtils.execSql(TestConstants.delSchedHearDef);
//                TestUtils.execSql(TestConstants.delRefLegRep);
//                TestUtils.execSql(TestConstants.delShJudge);
//                TestUtils.execSql(TestConstants.delShAtt);
//                TestUtils.execSql(TestConstants.delJudge);
//                TestUtils.execSql(TestConstants.delShHrg1);
//                TestUtils.execSql(TestConstants.delSitting);
//                TestUtils.execSql(TestConstants.delHrgList);
//                TestUtils.execSql(TestConstants.delDefHrgRec);
//                TestUtils.execSql(TestConstants.delHrg);
//                TestUtils.execSql(delLinkedHrg);
//                TestUtils.execSql(delErroneousLinkedHrg);
//                TestUtils.execSql(TestConstants.delRefHrgType);
//                TestUtils.execSql(TestConstants.delDefOnCase);
//                TestUtils.execSql(delDefOnCase_two);
//                TestUtils.execSql(TestConstants.delCase);
//                TestUtils.execSql(TestConstants.delDef);
//                TestUtils.execSql(TestConstants.delSolFirm);
//                TestUtils.execSql(TestConstants.delCCInfo);
//                TestUtils.execSql(TestConstants.delRefCourt);
//                TestUtils.execSql(TestConstants.delCourtRoom);
//                TestUtils.execSql(TestConstants.delCourtSite);
//                TestUtils.execSql(TestConstants.delAddr);
//                TestUtils.execSql(TestConstants.delCourt);
//
//
//                //insert the data
//                TestUtils.execSql(TestConstants.insCourt);
//                TestUtils.execSql(TestConstants.insAddr);
//                TestUtils.execSql(TestConstants.insCourtSite);
//                TestUtils.execSql(TestConstants.insCourtRoom);
//                TestUtils.execSql(TestConstants.insRefCourt);
//                TestUtils.execSql(TestConstants.insDef);
//                TestUtils.execSql(TestConstants.insCase);
//                TestUtils.execSql(insTypeUCase);
//                TestUtils.execSql(TestConstants.insDefOnCase);
//                TestUtils.execSql(insDefOnCase_two);
//                TestUtils.execSql(TestConstants.insSolFirm);
//                TestUtils.execSql(TestConstants.insCCInfo);
//                TestUtils.execSql(TestConstants.insRefHrgType);
//                TestUtils.execSql(TestConstants.insHrg);
//                TestUtils.execSql(insLinkedHrg);
//                TestUtils.execSql(insErroneousLinkedHrg);
//                TestUtils.execSql(TestConstants.insDefHrgRec);
//                TestUtils.execSql(TestConstants.insHrgList);
//                TestUtils.execSql(TestConstants.insSitting);
//                TestUtils.execSql(TestConstants.insShHrg1);
//                TestUtils.execSql(TestConstants.insShHrg2);
//                TestUtils.execSql(TestConstants.insSchedHearDef);
//                TestUtils.execSql(TestConstants.insJudge);
//                TestUtils.execSql(TestConstants.insShAtt);
//                TestUtils.execSql(TestConstants.insShJudge);
//                TestUtils.execSql(TestConstants.insRefLegRep1);
//                TestUtils.execSql(TestConstants.insRefLegRep2);
//                TestUtils.execSql(TestConstants.insShLegRep1);
//                TestUtils.execSql(TestConstants.insShLegRep2);
//                TestUtils.execSql(TestConstants.insExportA);
//                //     TestUtils.execSql(insDefOnCaseNotOnDHR);
//
//                //add rest of setup
//                TestUtils.execSql(createLinksForFour);
//                TestUtils.execSql(createLinksForFive);
//                TestUtils.execSql(createLinksForSix);
//            } else {
//                log.debug("\n\n\n\n\nWARNING: didn't run the setup scripts");
//            }
//        } catch (Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<,");
//    }
//
//    /**
//     * With a given hearingId, invke the retrieveHearingSummaryValue method.
//     * This returns a HearingSummaryValue.
//     * We want to validate that the HearingSummaryValue is valid.
//     * <p/>
//     * Case1: The hearing has a linked hearing.
//     * This implies that there will be more than one
//     * HearingListSummaryValue in the returned HearingSummaryValue object
//     * Contract that there are more than one hlsv in the HearingSummaryValue
//     * <p/>
//     * Case2: The hearing has no linked hearings.
//     * This implies that there should only be one HearingListSummaryValue
//     * Contract that it has at least one HearingListSummaryValue object.
//     * <p/>
//     * Common Elements
//     * For Case1 and Case2 the 'original' hearingId should have a corresponding
//     * HearingListSummaryValue in the Collection.
//     */
//
//    public void testRetrieveHearingSummaryValue() {
//        try {
//            //Case1:
//            Integer hearingId = null;
//            if (modify_flag == true) {
//                //using our own test data
//                hearingId = new Integer(4);
//            } else {
//                hearingId = new Integer(90);
//            }
//            HearingListSummaryValueHelper helper = new HearingListSummaryValueHelper();
//            HearingSummaryValue hearingSummaryValue = helper.retrieveHearingSummaryValue(hearingId);
//            /*
//            this serialisation test passed. No need to run it anymore
//            //for Sherie we want to prove that this is serializable
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ObjectOutputStream s = new ObjectOutputStream(out);
//            s.writeObject(hearingSummaryValue);
//            s.flush();
//            log.debug("\n\n------------ending Serialization test-----------------");
//            assertNotNull(hearingSummaryValue);
//            assertTrue(hearingSummaryValue instanceof HearingSummaryValue);
//            assertTrue(hearingSummaryValue instanceof Serializable);
//            */
//            //prove that the hearingSummaryValue is valid for case1
//            Collection hearingListSummaryValues = hearingSummaryValue.getHearingListSummaryValues();
//            assertNotNull(hearingSummaryValue);
//            //assertTrue that collection is greater than one
//            log.debug("retrieveHearingSummaryValues got a collection of size " + hearingListSummaryValues.size());
//            if (modify_flag == true) {
//                //not sure if working with our own test data, so not really sure if there are linked hearings or not!!
//                assertTrue("retrieveHearingSummaryValue did not find all linked hearings", hearingListSummaryValues.size() > 1);
//            }
//            //make sure it has a HearingListSummaryValue that corresponds with the original hearingId
//            boolean flag = false;     //indicates if original id was found
//            Iterator iterator = hearingListSummaryValues.iterator();
//            while (iterator.hasNext()) {
//                HearingListSummaryValue hearingListSummaryValue = (HearingListSummaryValue) iterator.next();
//                // indicating that there is something here
//                log.debug("*********** entered hearingListSummaryValue iteration (case1)");
//                log.debug("hearingListSummaryValue.getHearingID() = " + hearingListSummaryValue.getHearingID());
//                log.debug("trying to match hearingId passed into method = " + hearingId);
//
//                if (hearingListSummaryValue.getHearingID().equals(hearingId)) {
//                    flag = true;
//                    log.debug("setting flag to true - test should pass");
//                }
//            }
//
//            assertTrue("Original hearingId was not found in the returned HearingSummaryValue", flag == true);
//
//            //Case2:
//            if (modify_flag == true) {
//                //means wer are using our own data
//                hearingId = new Integer(6);
//            } else {
//                hearingId = new Integer(90);
//            }
//            //get a new transaction
//            hearingSummaryValue = helper.retrieveHearingSummaryValue(hearingId);
//            //prove that the returned hearingSummaryValue is valid for Case2
//            hearingListSummaryValues = hearingSummaryValue.getHearingListSummaryValues();
//            assertNotNull(hearingSummaryValue);
//            //assertTrue that the collection is exactly one in size
//            assertTrue("retrieveHearingSummaryValue idid not retrieve the right HearingListSummaryValues", hearingListSummaryValues.size() == 1);
//            //make sure it has a HearingListSummaryValue that corresponds with the original hearingId
//            flag = false; //indicates if original id was found
//            iterator = hearingListSummaryValues.iterator();
//            while (iterator.hasNext()) {
//                // indicating that there is something here
//                HearingListSummaryValue hearingListSummaryValue = (HearingListSummaryValue) iterator.next();
//                log.debug("*********** entered hearingListSummaryValue iteration (case2)");
//                log.debug("hearingListSummaryValue.getHearingID() = " + hearingListSummaryValue.getHearingID());
//                log.debug("hearingId passed into method           = " + hearingId);
//
//                if (hearingListSummaryValue.getHearingID().equals(hearingId)) {
//                    flag = true;
//                }
//            }
//            assertTrue("Original hearingId was not found in the returned HearingSummaryValue", flag);
//        } catch (Exception e) {
//            System.err.println("Exception thrown:  " + e);
//        }
//    }
//}