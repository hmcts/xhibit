//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.*;
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRLinkedCaseListValueHelper;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRLinkedCaseListValue;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
///**
// * <p>Title: HRLinkedCaseListValueHelperTest</p>
// * <p>Description: Test to get all linked hearings for a hearing record.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HRLinkedCaseListValueHelperTest extends TransactionTestCase
//{
//
//    private static Logger log  = CSServices.getLogger(HRLinkedCaseListValueHelperTest.class);
//
//    public HRLinkedCaseListValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp()
//    {
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
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
//            //Remove the data before the test
//            TestUtils.execSql(TestConstants.DEL_CASES);
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
//            TestUtils.execSql(TestConstants.INS_CASE_1);
//            TestUtils.execSql(TestConstants.INS_CASE_2);
//            TestUtils.execSql(TestConstants.INS_CASE_3);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<,");
//    }
//
//
//    /**
//     * testGetLinkedCaseList
//     */
//    public void testGetLinkedCaseList()
//    {
//        log.debug(">>>>>>>>>>>> testGetLinkedCaseList Starts <<<<<<<<<<<<<");
//
//        HRLinkedCaseListValueHelper hrlinkedcaselistvaluehelper = new HRLinkedCaseListValueHelper();
//
//        Vector cases = new Vector();
//        cases.addElement(TestConstants.CASE_ID_1);
//        cases.addElement(TestConstants.CASE_ID_2);
//        cases.addElement(TestConstants.CASE_ID_3);
//
//
//        try {
//            Collection ret = hrlinkedcaselistvaluehelper.getLinkedCaseList(cases);
//            if(ret.isEmpty())
//            {
//                log.debug("<<<<<<<<<<< The collection is empty >>>>>>>>>>>>>>>");
//            }
//            Iterator it = ret.iterator();
//
//            log.debug("<<<<<<<<<<< The collection size is: "+ret.size()+" >>>>>>>>>>>>>>>");
//
//
//            while(it.hasNext())
//            {
//                HRLinkedCaseListValue value = (HRLinkedCaseListValue)it.next();
//
//                log.debug("case id : " + value.getCaseID());
//                log.debug("case type : " + value.getCaseType());
//                log.debug("case number : " + value.getCaseNumber());
//
//                if(value.getCaseID().intValue() == TestConstants.CASE_ID_1.intValue())
//                {
//                    this.assertTrue(true);
//                }
//                else if(value.getCaseID().intValue() == TestConstants.CASE_ID_2.intValue())
//                {
//                    this.assertTrue(true);
//                }
//                else if(value.getCaseID().intValue() == TestConstants.CASE_ID_3.intValue())
//                {
//                    this.assertTrue(true);
//                }
//                else
//                {
//                    fail();
//                }
//            }
//        }
//        catch(Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testGetLinkedCaseList Ends <<<<<<<<<<<<<");
//        }
//    }
//}