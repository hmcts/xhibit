//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
////J2EE
//import javax.ejb.ObjectNotFoundException;
//import javax.naming.NamingException;
//
////other
//import org.apache.log4j.Logger;
//
////jdk
//import java.util.Collection;
//import java.util.Vector;
//
////XHIBIT
////Framework
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.*;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRSHLegRepValueHelper;
//
//import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
//
///**
// *
// * <p>Title: HRSHLegRepValueHelperTest</p>
// * <p>Description: Test the HRSHLegRepValueHelper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HRSHLegRepValueHelperTest extends TransactionTestCase
//{
//
//
//    private static Logger log  = CSServices.getLogger(HRSHLegRepValueHelperTest.class);
//
//    private SHLegRepBasicValue basicValue = null;
//    private SHLegRepComplexValue complexValue = null;
//    private HRSHLegRepValue hrShLegRepValue = null;
//    private HRSHLegRepValueHelper hrshlegrepvaluehelper = new HRSHLegRepValueHelper();
//
//    public HRSHLegRepValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//
//        try
//        {
//            //delete the data
//            TestUtils.execSql(TestConstants.delShLegRep);
//            TestUtils.execSql(TestConstants.delSchedHearDef);
//            TestUtils.execSql(TestConstants.delDefOnCase);
//            TestUtils.execSql(TestConstants.delDef);
//            TestUtils.execSql(TestConstants.delSolFirm);
//            TestUtils.execSql(TestConstants.delCCInfo);
//            TestUtils.execSql(TestConstants.delRefLegRep);
//            TestUtils.execSql(TestConstants.delShHrg1);
//            TestUtils.execSql(TestConstants.delHrg);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delSitting);
//            TestUtils.execSql(TestConstants.delHrgList);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//
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
//            //insert data
//            TestUtils.execSql(TestConstants.insCourt);
//            TestUtils.execSql(TestConstants.insAddr);
//            TestUtils.execSql(TestConstants.insCourtSite);
//            TestUtils.execSql(TestConstants.insCourtRoom);
//            TestUtils.execSql(TestConstants.insRefCourt);
//            TestUtils.execSql(TestConstants.insHrgList);
//            TestUtils.execSql(TestConstants.insSitting);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insHrg);
//            TestUtils.execSql(TestConstants.insShHrg1);
//            TestUtils.execSql(TestConstants.insDef);
//            TestUtils.execSql(TestConstants.insDefOnCase);
//            TestUtils.execSql(TestConstants.insSolFirm);
//            TestUtils.execSql(TestConstants.insCCInfo);
//            TestUtils.execSql(TestConstants.insSchedHearDef);
//            TestUtils.execSql(TestConstants.insRefLegRep1);
//            TestUtils.execSql(TestConstants.insShLegRep1);
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Set the first ShLegRep <<<<<<<<<<<<<<<<<<<<<<");
//            ShLegRepMaintainer shLegRepMaintainer = new ShLegRepMaintainer();
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> Get the entity <<<<<<<<<<<<<<<<<<<<<<");
//            ShLegRep shLegRep = (ShLegRep)shLegRepMaintainer.findByPrimaryKey(TestConstants.shLegRep1ID);
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> set the hrShLegRepValue <<<<<<<<<<<<<<<<<<<<<<");
//            hrShLegRepValue = new HRSHLegRepValue(shLegRep.getShLegRepId(), shLegRep.getVersion());
//            hrShLegRepValue.setRefDefenceCategoryID(new Integer(9999));
//            hrShLegRepValue.setRefLegRepID(shLegRep.getRefLegalRepId());
//
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> set the basicValue <<<<<<<<<<<<<<<<<<<<<<");
//            basicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//            log.debug(">>>>>>>>>>>>>>>>>>>>>> set the complexValue <<<<<<<<<<<<<<<<<<<<<<");
//            complexValue = shLegRepMaintainer.getShLegRepComplexValue(shLegRep);
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
//     * testBuildBasicValue
//     */
//    public void testBuildBasicValue()
//    {
//        log.debug(">>>>>>>>>>>> testBuildBasicValue Starts <<<<<<<<<<<<<");
//        try
//        {
//            SHLegRepBasicValue shlegrepbasicvalueRet = hrshlegrepvaluehelper.
//                    buildBasicValue(hrShLegRepValue);
//
//            log.debug("id 1 : " + shlegrepbasicvalueRet.getId() + ", id 2 : " + hrShLegRepValue.getId());
//            this.assertEquals(shlegrepbasicvalueRet.getId(), hrShLegRepValue.getId());
//
//            log.debug("version 1 : " +shlegrepbasicvalueRet.getVersion() + ", version 2 : " +hrShLegRepValue.getVersion());
//            this.assertEquals(shlegrepbasicvalueRet.getVersion(), hrShLegRepValue.getVersion());
//
//            log.debug("defCate 1 : " +shlegrepbasicvalueRet.getRefDefenceCategoryID() + ", defCate 2 : " +hrShLegRepValue.getRefDefenceCategoryID());
//            this.assertEquals(shlegrepbasicvalueRet.getRefDefenceCategoryID(), hrShLegRepValue.getRefDefenceCategoryID());
//
//            log.debug("refLegrepID 1 : " +shlegrepbasicvalueRet.getRefLegalRepID() + ", refLegrepID 2 : " +hrShLegRepValue.getRefLegRepID());
//            this.assertEquals(shlegrepbasicvalueRet.getRefLegalRepID(), hrShLegRepValue.getRefLegRepID());
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testBuildBasicValue Ends <<<<<<<<<<<<<");
//        }
//    }
//
//
//    /**
//     * testBuildHRSHLegRepValueFromBasic
//     */
//    public void testBuildHRSHLegRepValueFromBasic()
//    {
//        log.debug(">>>>>>>>>>>> testBuildHRSHLegRepValueFromBasic Starts <<<<<<<<<<<<<");
//        try
//        {
//            HRSHLegRepValue hrshlegrepvalueRet = hrshlegrepvaluehelper.
//                    buildHRSHLegRepValueFromBasic(basicValue);
//
//            this.assertEquals(hrshlegrepvalueRet.getId(), basicValue.getId());
//            this.assertEquals(hrshlegrepvalueRet.getVersion(), basicValue.getVersion());
//            this.assertEquals(hrshlegrepvalueRet.getRefDefenceCategoryID(), basicValue.getRefDefenceCategoryID());
//            this.assertEquals(hrshlegrepvalueRet.getRefLegRepID(), basicValue.getRefLegalRepID());
//
//            assertEquals(hrshlegrepvalueRet.getRefDefenceCategoryDesc(), "REPRESENTATION ORDER CLAIM");
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testBuildHRSHLegRepValueFromBasic Ends <<<<<<<<<<<<<");
//        }
//    }
//
//    /**
//     * testBuildHRSHLegRepValueFromComplex
//     */
//    public void testBuildHRSHLegRepValueFromComplex()
//    {
//        log.debug(">>>>>>>>>>>> testBuildHRSHLegRepValueFromComplex Starts <<<<<<<<<<<<<");
//        try
//        {
//            HRSHLegRepValue hrshlegrepvalueRet = hrshlegrepvaluehelper.
//                    buildHRSHLegRepValueFromComplex(complexValue);
//            if(hrshlegrepvalueRet != null)
//            {
//                this.assertEquals(hrshlegrepvalueRet.getId(), complexValue.getId());
//                this.assertEquals(hrshlegrepvalueRet.getVersion(), complexValue.getVersion());
//                this.assertEquals(hrshlegrepvalueRet.getRefDefenceCategoryID(), complexValue.getRefDefenceCategoryID());
//                this.assertEquals(hrshlegrepvalueRet.getRefLegRepID(), complexValue.getRefLegalRepID());
//
//                assertEquals(hrshlegrepvalueRet.getRefDefenceCategoryDesc(), "REPRESENTATION ORDER CLAIM");
//            }
//            else
//            {
//                log.debug("The hrshlegrepvalueRet is null!!!!!! Fail the test!");
//                this.fail();
//            }
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testBuildHRSHLegRepValueFromComplex Ends <<<<<<<<<<<<<");
//        }
//    }
//}