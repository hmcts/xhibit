//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRJusticeValueHelper;
//
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJusticeValue;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: HRJusticeValueHelperTest</p>
// * <p>Description: Test the HRJusticeValueHelper </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HRJusticeValueHelperTest extends TransactionTestCase
//{
//
//
//    private static Logger log  = CSServices.getLogger(HRJusticeValueHelperTest.class);
//
//    /**
//     * HRJusticeValueHelperTest
//     * @param s
//     */
//    public HRJusticeValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    /**
//     * setUp
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<<");
//        try
//        {
//            //delete the data
//            TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
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
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insHrg);
//            TestUtils.execSql(TestConstants.INS_SH_JUSTICE);
//
//
//
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<<");
//    }
//
//    /**
//     * testGetHrJusticeValue
//     */
//    public void testGetHrJusticeValue()
//    {
//        log.debug(">>>>>>>>>>>> testGetHrJusticeValue Starts <<<<<<<<<<<<<<");
//        HRJusticeValueHelper hrjusticevaluehelper = new HRJusticeValueHelper();
//        try
//        {
//            HRJusticeValue hrjusticevalueRet = hrjusticevaluehelper.getHrJusticeValue(TestConstants.SH_JUSTICE_ID);
//            log.debug("getShJusticeID : " + hrjusticevalueRet.getShJusticeID());
//            log.debug("getJusticeName : " + hrjusticevalueRet.getJusticeName());
//            log.debug("hrjusticevalueRet : " + hrjusticevalueRet.getHearingID());
//
//            if(hrjusticevalueRet.getShJusticeID().intValue() == TestConstants.SH_JUSTICE_ID.intValue())
//            {
//                this.assertTrue(true);
//            }
//            else
//            {
//                this.fail();
//            }
//
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testGetHrJusticeValue ends <<<<<<<<<<<<<<");
//        }
//    }
//
//}