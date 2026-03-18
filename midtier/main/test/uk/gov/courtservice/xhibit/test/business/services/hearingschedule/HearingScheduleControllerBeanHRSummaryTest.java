//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//
//// JDK
//
//import junit.framework.TestCase;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingListSummaryValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingSummaryValue;
//import uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord.TestConstants;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//public class HearingScheduleControllerBeanHRSummaryTest extends TestCase {
//    private HearingScheduleController hearingScheduleController;
//    private static Logger log = CSServices.getLogger(HearingScheduleControllerBeanHRSummaryTest.class);
//
//    public HearingScheduleControllerBeanHRSummaryTest(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        try {
//            log.debug("<<<<<<<<<<< setup starting >>>>>>>>>>>");
//            //delete the data
//            TestUtils.execSql(TestConstants.delExportA);
//            TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//            TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//            TestUtils.execSql(TestConstants.DEL_ADV);
//            TestUtils.execSql(TestConstants.DEL_CHAMBER);
//            TestUtils.execSql(TestConstants.delShLegRep);
//            TestUtils.execSql(TestConstants.delSchedHearDef);
//            TestUtils.execSql(TestConstants.delRefLegRep);
//            TestUtils.execSql(TestConstants.delShJudge);
//            TestUtils.execSql(TestConstants.delShAtt);
//            TestUtils.execSql(TestConstants.delJudge);
//            TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//            TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//            TestUtils.execSql(TestConstants.delShHrg1);
//            TestUtils.execSql(TestConstants.delSitting);
//            TestUtils.execSql(TestConstants.delHrgList);
//            TestUtils.execSql(TestConstants.delDefHrgRec);
//            TestUtils.execSql(TestConstants.delHrg);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delDefOnCase);
//            TestUtils.execSql(TestConstants.DEL_CR);
//            TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.DEL_CASES);
//            TestUtils.execSql(TestConstants.DEL_DEF_REF);
//            TestUtils.execSql(TestConstants.delDef);
//            TestUtils.execSql(TestConstants.delSolFirm);
//            TestUtils.execSql(TestConstants.delCCInfo);
//            TestUtils.execSql(TestConstants.delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//            //insert the data
//            TestUtils.execSql(TestConstants.insCourt);
//            TestUtils.execSql(TestConstants.insAddr);
//            TestUtils.execSql(TestConstants.insCourtSite);
//            TestUtils.execSql(TestConstants.insCourtRoom);
//            TestUtils.execSql(TestConstants.insRefCourt);
//            TestUtils.execSql(TestConstants.insDef);
//            TestUtils.execSql(TestConstants.INS_DEL_REF1);
//            TestUtils.execSql(TestConstants.INS_DEL_REF2);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insCase2);
//            TestUtils.execSql(TestConstants.insDefOnCase);
//            TestUtils.execSql(TestConstants.insSolFirm);
//            TestUtils.execSql(TestConstants.insCCInfo);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(TestConstants.insHrg);
//            TestUtils.execSql(TestConstants.insHrg2);
//            TestUtils.execSql(TestConstants.insDefHrgRec);
//            TestUtils.execSql(TestConstants.insHrgList);
//            TestUtils.execSql(TestConstants.insSitting);
//            TestUtils.execSql(TestConstants.insShHrg1);
//            TestUtils.execSql(TestConstants.insShHrg2);
//            TestUtils.execSql(TestConstants.insShHrg3);
//            TestUtils.execSql(TestConstants.INS_CR_FIRM);
//            TestUtils.execSql(TestConstants.INS_CR1);
//            TestUtils.execSql(TestConstants.INS_SH_ATT_CR1);
//            TestUtils.execSql(TestConstants.insSchedHearDef);
//            TestUtils.execSql(TestConstants.insJudge);
//            TestUtils.execSql(TestConstants.insShAtt);
//            //test the creation of the judge by disable this insert.
//            //TestUtils.execSql(TestConstants.insShJudge);
//            TestUtils.execSql(TestConstants.insRefLegRep1);
//            TestUtils.execSql(TestConstants.insRefLegRep2);
//            TestUtils.execSql(TestConstants.insShLegRep1);
//            TestUtils.execSql(TestConstants.insShLegRep2);
//            TestUtils.execSql(TestConstants.insExportA);
//            TestUtils.execSql(TestConstants.INS_SOL_FIRM);
//            TestUtils.execSql(TestConstants.INS_SOLICITOR);
//            TestUtils.execSql(TestConstants.INS_CHAMBER);
//            TestUtils.execSql(TestConstants.INS_ADV);
//        } catch (Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<,");
//    }
//
//    protected void tearDown() {
//        log.debug(">>>>>>>>>>>> tearDown Starts <<<<<<<<<<<<<");
//        try {
//            //delete the data
//            TestUtils.execSql(TestConstants.delExportA);
//            TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//            TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//            TestUtils.execSql(TestConstants.DEL_ADV);
//            TestUtils.execSql(TestConstants.DEL_CHAMBER);
//            TestUtils.execSql(TestConstants.delShLegRep);
//            TestUtils.execSql(TestConstants.delSchedHearDef);
//            TestUtils.execSql(TestConstants.delRefLegRep);
//            TestUtils.execSql(TestConstants.delShJudge);
//            TestUtils.execSql(TestConstants.delShAtt);
//            TestUtils.execSql(TestConstants.delJudge);
//            TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//            TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//            TestUtils.execSql(TestConstants.delShHrg1);
//            TestUtils.execSql(TestConstants.delSitting);
//            TestUtils.execSql(TestConstants.delHrgList);
//            TestUtils.execSql(TestConstants.delDefHrgRec);
//            TestUtils.execSql(TestConstants.delHrg);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delDefOnCase);
//            TestUtils.execSql(TestConstants.DEL_CR);
//            TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.DEL_CASES);
//            TestUtils.execSql(TestConstants.DEL_DEF_REF);//
//            TestUtils.execSql(TestConstants.delDef);
//            TestUtils.execSql(TestConstants.delSolFirm);
//            TestUtils.execSql(TestConstants.delCCInfo);
//            TestUtils.execSql(TestConstants.delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//        } catch (Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> tearDown Ends <<<<<<<<<<<<<");
//    }
//
//    /**
//     * get an instance of the bean
//     * execute the method to be tested. retrieveHearingSummaryValue
//     * assertTrue that the returned HearingSummaryValue is as expected.
//     * assertNotNull
//     * <p/>
//     * assertTrue that the returned value is an instance of a HearingSummaryValue
//     * <p/>
//     * For a given hearingId, invoke the retrieveHearingSummaryValue method.
//     * This returns a HearingSummaryValue.
//     * <p/>
//     * We want to validate that the HearingSummaryValue is valid.
//     * Case1: The hearing has a linked hearing.
//     * This implies that there will be more than one
//     * HearingListSummaryValue in the returned HearingSummaryValue object
//     * Contract that there are more than one hlsv in the HearingSummaryValue
//     * <p/>
//     * NOTE: Lower level tests are used for the composite parts of this method.
//     * This test is just to ensure that all works at a high level.
//     */
//    public void testRetrieveHearingSummaryValue() {
//        try {
//            //get a reference to the bean
//            HearingScheduleController hearingScheduleController =
//                    (HearingScheduleController) CSServices.getEJBServices().createLocalSession(HearingScheduleControllerHome.class);
//
//            //call the target method on the bean
//            HearingSummaryValue hearingSummaryValue = null;
//            hearingSummaryValue = hearingScheduleController.retrieveHearingSummaryValue(TestConstants.hearingID);
//
//            //assertTrue that the return value is as expected.
//            assertNotNull("Expected non-null value to be returned", hearingSummaryValue);
//
//            //expect there to be at least one hearingListSummaryValue
//            //providing a valid hearingId is used
//            Collection hearingListSummaryValues = hearingSummaryValue.getHearingListSummaryValues();
//            assertTrue("Expected there to be at least one HearingListSummaryValue", hearingListSummaryValues.size() > 0);
//
//            //sanity check to assertTrue that there is a hearingListSummary for the
//            //specified hearing
//            //would expect that at the least there is one for the specified hearingID
//            Iterator iterator = hearingListSummaryValues.iterator();
//            HearingListSummaryValue hearingListSummaryValue = null;
//            boolean flag = false;
//            while (iterator.hasNext()) {
//                hearingListSummaryValue = (HearingListSummaryValue) iterator.next();
//                if (hearingListSummaryValue.getHearingID().equals(TestConstants.hearingID)) {
//                    flag = true;
//                }//end if
//            }//end while
//            assertTrue("Expected there to be a HearingSummaryValue for specified hearingId", flag);
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
//
//
//