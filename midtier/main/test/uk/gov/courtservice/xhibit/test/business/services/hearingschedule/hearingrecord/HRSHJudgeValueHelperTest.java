//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.*;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.*;
//import uk.gov.courtservice.xhibit.business.vos.entities.*;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.*;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeComplexValue;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//
//
///**
// * <p>Title: HRSHJudgeValueHelperTest</p>
// * <p>Description: Test class for HRSHJudgeValueHelper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HRSHJudgeValueHelperTest extends TransactionTestCase
//{
//
//    private static Logger log  = CSServices.getLogger(HRSHJudgeValueHelperTest.class);
//    private HRSHJudgeValue hrSHJudgeValue =  null;
//    private SHJudgeBasicValue basicValue=  null;
//    private SHJudgeComplexValue complexValue =  null;
//
//    /**
//     * HRSHJudgeValueHelperTest
//     * @param s
//     */
//    public HRSHJudgeValueHelperTest(String s) throws Exception
//    {
//        super(s, true);
//    }
//
//    /**
//     * setUp
//     */
//    protected void setUp() throws Exception
//    {
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<");
//        try
//        {
//            super.setUp();
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
//
//
//            TestUtils.execSql(TestConstants.delShJudge);
//            TestUtils.execSql(TestConstants.delShAtt);
//            TestUtils.execSql(TestConstants.delJudge);
//            TestUtils.execSql(TestConstants.delShHrg1);
//            TestUtils.execSql(TestConstants.delSitting);
//            TestUtils.execSql(TestConstants.delHrgList);
//            TestUtils.execSql(TestConstants.delHrg);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.delRefCourt);
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
//            TestUtils.execSql(TestConstants.insRefCourt);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(TestConstants.insHrg);
//            TestUtils.execSql(TestConstants.insHrgList);
//            TestUtils.execSql(TestConstants.insSitting);
//            TestUtils.execSql(TestConstants.insShHrg1);
//            TestUtils.execSql(TestConstants.insShHrg2);
//            TestUtils.execSql(TestConstants.insJudge);
//            TestUtils.execSql(TestConstants.insShAtt);
//            TestUtils.execSql(TestConstants.insShJudge);
//            log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testGetHRJudgeValue()
//    {
//        log.debug(">>>>>>>>>>>> testGetHRJudgeValue Starts <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//
//        try
//        {
//            HRSHJudgeValueHelper hrshjudgevaluehelper = new HRSHJudgeValueHelper();
//            HRSHJudgeValue value = hrshjudgevaluehelper.getHRJudgeValue(TestConstants.shAttID);
//            log.debug("shid :" + value.getId());
//            log.debug("shattid : " + value.getSHAttendeeID());
//            log.debug("refJudgeid : " + value.getRefJudgeID());
//            this.assertEquals(TestConstants.shJudgeID, value.getId());
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testGetHRJudgeValue ends <<<<<<<<<<<<<");
//            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//        }
//    }
//
//
//    /**
//     * testBuildBasicValue
//     */
//    public void testBuildBasicValue()
//    {
//        log.debug(">>>>>>>>>>>> testBuildBasicValue Starts <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//        try
//        {
//            this.setHrShJudgeValue();
//            HRSHJudgeValueHelper hrshjudgevaluehelper = new HRSHJudgeValueHelper();
//            SHJudgeBasicValue shjudgebasicvalueRet = hrshjudgevaluehelper.
//                    buildBasicValue(hrSHJudgeValue);
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//            log.debug(">>>>>>>>>>>> testBuildBasicValue Ends <<<<<<<<<<<<<");
//        }
//    }
//
//    /**
//     * testBuildHRSHJudgeValueFromBasic
//     */
//    public void testBuildHRSHJudgeValueFromBasic()
//    {
//        log.debug(">>>>>>>>>>>> testBuildHRSHJudgeValueFromBasic Starts <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//        try
//        {
//            this.setJudgeBasicValue();
//            HRSHJudgeValueHelper hrshjudgevaluehelper = new HRSHJudgeValueHelper();
//            HRSHJudgeValue hrshjudgevalueRet = hrshjudgevaluehelper.
//                    buildHRSHJudgeValueFromBasic(basicValue);
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//            log.debug(">>>>>>>>>>>> testBuildHRSHJudgeValueFromBasic Ends <<<<<<<<<<<<<");
//        }
//    }
//
//    /**
//     * testBuildHRSHJudgeValueFromComplex
//     */
//    public void testBuildHRSHJudgeValueFromComplex()
//    {
//        log.debug(">>>>>>>>>>>> testBuildHRSHJudgeValueFromComplex Starts <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//        try
//        {
//            this.setJudgeComplexValue();
//            HRSHJudgeValueHelper hrshjudgevaluehelper = new HRSHJudgeValueHelper();
//            HRSHJudgeValue hrshjudgevalueRet = hrshjudgevaluehelper.
//                    buildHRSHJudgeValueFromComplex(complexValue);
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
//            log.debug(">>>>>>>>>>>> testBuildHRSHJudgeValueFromComplex Ends <<<<<<<<<<<<<");
//        }
//    }
//
//
//    /**
//     * setHrShJudgeValue
//     */
//    private void setHrShJudgeValue()
//    {
//        log.debug(">>>>>>>>>>>> setHrShJudgeValue Starts <<<<<<<<<<<<<");
//        hrSHJudgeValue = new HRSHJudgeValue(new Integer(999), new Integer(1));
//        hrSHJudgeValue.setDeputyHCJ("Y");
//        hrSHJudgeValue.setRefJudgeID(new Integer(2));
//        hrSHJudgeValue.setSHAttendeeID(new Integer(3));
//        log.debug(">>>>>>>>>>>> setHrShJudgeValue Ends <<<<<<<<<<<<<");
//    }
//
//
//    /**
//     * setJudgeBasicValue
//     */
//    private void setJudgeBasicValue()
//    {
//        log.debug(">>>>>>>>>>>> setJudgeBasicValue Starts <<<<<<<<<<<<<");
//        basicValue = new SHJudgeBasicValue(new Integer(999), new Integer(1));
//        basicValue.setDeputyHCJ("Y");
//        basicValue.setRefJudgeID(new Integer(2));
//        basicValue.setShAttendeeID(new Integer(3));
//        log.debug(">>>>>>>>>>>> setJudgeBasicValue Ends <<<<<<<<<<<<<");
//    }
//
//    /**
//     * setJudgeComplexValue
//     */
//    private void setJudgeComplexValue()
//    {
//        log.debug(">>>>>>>>>>>> setJudgeComplexValue Starts <<<<<<<<<<<<<");
//        complexValue = new SHJudgeComplexValue(new Integer(999), new Integer(1));
//        complexValue.setDeputyHCJ("Y");
//        complexValue.setRefJudgeID(new Integer(2));
//        complexValue.setShAttendeeID(new Integer(3));
//        log.debug(">>>>>>>>>>>> setJudgeComplexValue Ends <<<<<<<<<<<<<");
//    }
//
//}