//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRJudgeValueHelper;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJudgeValue;
//
//import org.apache.log4j.Logger;
//
//import java.util.Collection;
//import java.util.Iterator;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
//
///**
// * <p>Title: HRJudgeValueHelperTest</p>
// * <p>Description: Test the HRJudgeValueHelper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HRJudgeValueHelperTest extends TransactionTestCase
//{
//
//    private static Logger log  = CSServices.getLogger(HRJudgeValueHelperTest.class);
//
//    /**
//     *
//     * @param s
//     */
//    public HRJudgeValueHelperTest(String s) throws NamingException
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
//
//        TestUtils.execSql(TestConstants.delJudge);
//        TestUtils.execSql(TestConstants.delCourt);
//
//        //insert the data
//        TestUtils.execSql(TestConstants.insCourt);
//        TestUtils.execSql(TestConstants.insJudge);
//
//        log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<<");
//    }
//
//    /**
//     * testBuildHRJudge
//     */
//    public void testBuildHRJudge()
//    {
//        log.debug(">>>>>>>>>>>> testBuildHRJudge Starts <<<<<<<<<<<<<<");
//        HRJudgeValueHelper hrjudgevaluehelper = new HRJudgeValueHelper();
//
//        try
//        {
//            Collection hrValues = hrjudgevaluehelper.buildHRJudge(TestConstants.refJudgeID);
//            Iterator it = hrValues.iterator();
//
//            if(hrValues.isEmpty())
//            {
//                this.fail();
//            }
//
//            while(it.hasNext())
//            {
//                HRJudgeValue value = (HRJudgeValue)it.next();
//                log.debug("Judge id : " + value.getRefJudgeID());
//                log.debug("judge firstname : " + value.getJudgeFirstName());
//                log.debug("middlename : " + value.getJudgeMiddleName());
//                log.debug("surname : " + value.getJudgeSurname());
//                log.debug("full 1 : " + value.getJudgeFullListTitle1());
//                log.debug("full 2 : " + value.getJudgeFullListTitle2());
//                log.debug("full 3 : " + value.getJudgeFullListTitle3());
//
//                if(value.getRefJudgeID().intValue() == TestConstants.refJudgeID.intValue())
//                {
//                    this.assertTrue(true);
//                }
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
//            log.debug(">>>>>>>>>>>> testBuildHRJudge Ends <<<<<<<<<<<<<<");
//        }
//    }
//}