//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRCaseValueHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCaseValue;
//
//import javax.naming.NamingException;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author unascribed
// * @version 1.0
// */
//
//public class HRCaseValueHelperTest extends TransactionTestCase {
//
//    private static Logger log = CSServices.getLogger(HRCaseValueHelperTest.class);
//    private CaseMaintainer maintainer = new CaseMaintainer();
//
//    private String caseType = "T";
//    private String caseSubType = new String("O");
//    private Integer caseNumber = new Integer(200);
//    private Integer estPDHTrialLength = new Integer(24);
//    private String crestSeveredInd = new String("y");
//    private Integer noProsWitness = new Integer(10);
//    private Integer noPageProsEveidence = new Integer(11);
//    private Integer lengthTape = new Integer(12);
//
////refCourt
//    private String insRefCourt = "insert into xhb_ref_court (ref_court_id, court_full_name, court_id) values (" + TestConstants.refCourtID.intValue() + ", 'BAS magistrates court', " + TestConstants.courtID.intValue() + ")";
//    private String delRefCourt = "delete from xhb_ref_court where ref_court_id = " + TestConstants.refCourtID.intValue();
//
////Case
//    private String insCase = "insert into xhb_case(case_ID, case_Type, case_Sub_Type, case_number, est_PDH_Trial_Length, severed_ind, no_Pros_Witness, no_Page_Pros_Evidence, length_tape, ref_court_id, court_id) values (" + TestConstants.caseID.intValue() + ",'" + caseType + "' ,'" + caseSubType + "' , " + caseNumber.intValue() + ", " + estPDHTrialLength.intValue() + ", '" + crestSeveredInd + "', " + noProsWitness.intValue() + ", " + noPageProsEveidence.intValue() + ", " + lengthTape.intValue() + ", " + TestConstants.refCourtID.intValue() + ", " + TestConstants.courtID.intValue() + ")";
//
//    public HRCaseValueHelperTest(String s) throws NamingException {
//        super(s, true);
//    }
//
//    /**
//     *
//     */
//    protected void setUp() throws Exception {
//        super.setUp();
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<");
//        try {
//            //delete the data
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//
//            //insert the data
//            TestUtils.execSql(TestConstants.insCourt);
//            TestUtils.execSql(TestConstants.insAddr);
//            TestUtils.execSql(TestConstants.insCourtSite);
//            TestUtils.execSql(TestConstants.insCourtRoom);
//            TestUtils.execSql(insRefCourt);
//            TestUtils.execSql(insCase);
//
//        } catch (Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<");
//    }
//
//    /**
//     * Get a HRCaseValue for a given ID
//     * Contract that the returned HRCaseValue is as expected
//     */
//    public void testGetHRCaseValue() {
//        try {
//            HRCaseValueHelper helper = new HRCaseValueHelper();
//            HRCaseValue caseValue = helper.getHRCaseValue(TestConstants.caseID);
//            //assertTrue that the caseValue returned is as expected
//            assertNotNull("Expected the caseValue returned to be a non-null value: ", caseValue);
//            assertEquals(TestConstants.caseID, caseValue.getCaseID());
//            assertEquals(caseType, caseValue.getCaseType());
//            assertEquals(caseSubType, caseValue.getCaseSubType());
//            assertEquals(caseNumber, caseValue.getCaseNumber());
//            assertEquals(estPDHTrialLength, caseValue.getEstPDHTrialLength());
//            assertEquals(crestSeveredInd, caseValue.getCrestSeveredInd());
//            assertEquals(noProsWitness, caseValue.getNoProsWitness());
//            assertEquals(noPageProsEveidence, caseValue.getNoPageProsEvidence());
//            assertEquals(lengthTape, caseValue.getLengthTape());
//        } catch (HearingRecordException hre) {
//            hre.printStackTrace();
//            fail();
//        }
//    }
//}