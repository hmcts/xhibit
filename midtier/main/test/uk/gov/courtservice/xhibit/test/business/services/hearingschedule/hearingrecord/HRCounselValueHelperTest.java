//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.*;
//import uk.gov.courtservice.xhibit.business.entities.hearing.*;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.*;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.*;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.*;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.*;
//
//import org.apache.log4j.Logger;
//
//import java.util.Collection;
//import java.util.Vector;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: HRCounselValueHelperTest</p>
// * <p>Description: Test for getting and building the HRCounselValue </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author unascribed
// * @version 1.0
// */
//
//public class HRCounselValueHelperTest extends TransactionTestCase
//{
//
//    private static Logger log  = CSServices.getLogger(HRCounselValueHelperTest.class);
//
//    public HRCounselValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp()
//    {
//
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<<");
//        try
//        {
//            super.setUp();
//            //delete the data
//            TestUtils.execSql(TestConstants.DEL_HEARING_LEG_REP1);
//            TestUtils.execSql(TestConstants.DEL_HEARING_LEG_REP2);
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
//            TestUtils.execSql(TestConstants.INS_CR_FIRM);//
//            TestUtils.execSql(TestConstants.INS_CR1);//
//            TestUtils.execSql(TestConstants.INS_SH_ATT_CR1);//
//            TestUtils.execSql(TestConstants.insSchedHearDef);
//            TestUtils.execSql(TestConstants.insJudge);
//            TestUtils.execSql(TestConstants.insShAtt);
//            //TestUtils.execSql(TestConstants.insShJudge); //test the creation of the judge
//            TestUtils.execSql(TestConstants.insRefLegRep1);
//            TestUtils.execSql(TestConstants.insRefLegRep2);
//            TestUtils.execSql(TestConstants.insShLegRep1);
//            TestUtils.execSql(TestConstants.insShLegRep2);
//            TestUtils.execSql(TestConstants.insExportA);
//            TestUtils.execSql(TestConstants.INS_SOL_FIRM);
//            TestUtils.execSql(TestConstants.INS_SOLICITOR);
//            TestUtils.execSql(TestConstants.INS_CHAMBER);
//            TestUtils.execSql(TestConstants.INS_ADV);
//            TestUtils.execSql(TestConstants.INS_HEARING_LEG_REP1_01);
//            TestUtils.execSql(TestConstants.INS_HEARING_LEG_REP1_02);
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
//     * testGetLegalRepresentative1
//     */
//    public void testGetRefLegalRepresentative1()
//    {
//        log.debug(">>>>>>>>>>>> testGetRefLegalRepresentative1 Starts <<<<<<<<<<<<<<");
//        HRCounselValueHelper hrcounselvaluehelper = new HRCounselValueHelper();
//        String legalRole2=  "A";
//        try {
//			/**
//			* @todo: pass a hearingID into the getRefLegalRepresentative() method call
//			* as the 4th parameter
//			*/
//            HRCounselValue hrcounselvalueRet = hrcounselvaluehelper.
//                    getRefLegalRepresentative(TestConstants.refLegRep1ID, legalRole2, "R", TestConstants.hearingID);
//
//
//            log.debug("bar number : "+hrcounselvalueRet.getFirstName());
//            log.debug("getMiddleName : "+hrcounselvalueRet.getMiddleName());
//            log.debug("getSurname : "+hrcounselvalueRet.getSurname());
//            log.debug("getLegalRole : "+hrcounselvalueRet.getLegalRole());
//            log.debug("getBarNumber : "+hrcounselvalueRet.getBarNumber());
//            log.debug("getAddressBasicValue : "+hrcounselvalueRet.getAddressBasicValue().toString());
//            log.debug("getRefAdvocateOrSolicitorID : "+hrcounselvalueRet.getRefAdvocateOrSolicitorID());
//            log.debug("getRefChamberOrSolicitorFirmID : "+hrcounselvalueRet.getRefChamberOrSolicitorFirmID());
//
//            log.debug("RefLegRepID 1:"+hrcounselvalueRet.getRefLegalRepID()+", 2:"+TestConstants.refLegRep1ID);
//            this.assertEquals(hrcounselvalueRet.getRefLegalRepID(), TestConstants.refLegRep1ID);
//
//            log.debug("legal role 1:"+hrcounselvalueRet.getLegalRole()+", 2:"+legalRole2);
//            this.assertEquals(hrcounselvalueRet.getLegalRole(), legalRole2);
//
//            this.assertTrue("Found " + hrcounselvalueRet.getStartEndDatesArray().length +
//                            " start/end dates for hearingID/refLegalRepID:" +
//                            TestConstants.hearingID + "/" + TestConstants.refLegRep1ID + ".  " +
//                            "There should be TWO",
//                            hrcounselvalueRet.getStartEndDatesArray().length == 2);
//        }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testGetRefLegalRepresentative1 Ends <<<<<<<<<<<<<<");
//        }
//    }
//
//
//    /**
//     * testGetLegalRepresentative2
//     * just test with another leg rep.
//     */
//    public void testGetRefLegalRepresentative2()
//    {
//        log.debug(">>>>>>>>>>>> testGetRefLegalRepresentative2 Starts <<<<<<<<<<<<<<");
//        HRCounselValueHelper hrcounselvaluehelper = new HRCounselValueHelper();
//        String legalRole2=  "S";
//        try {
//			/**
//			* @todo: pass a hearingID into the getRefLegalRepresentative() method call
//			* as the 4th parameter
//			*/
//            HRCounselValue hrcounselvalueRet = hrcounselvaluehelper.
//                    getRefLegalRepresentative(TestConstants.refLegRep2ID, legalRole2, "", TestConstants.hearingID);
//
//            log.debug("bar number : "+hrcounselvalueRet.getFirstName());
//            log.debug("getMiddleName : "+hrcounselvalueRet.getMiddleName());
//            log.debug("getSurname : "+hrcounselvalueRet.getSurname());
//            log.debug("getLegalRole : "+hrcounselvalueRet.getLegalRole());
//            log.debug("getBarNumber : "+hrcounselvalueRet.getBarNumber());
//            log.debug("getAddressBasicValue : "+hrcounselvalueRet.getAddressBasicValue().toString());
//            log.debug("getRefAdvocateOrSolicitorID : "+hrcounselvalueRet.getRefAdvocateOrSolicitorID());
//            log.debug("getRefChamberOrSolicitorFirmID : "+hrcounselvalueRet.getRefChamberOrSolicitorFirmID());
//
//            log.debug("RefLegRepID 1:"+hrcounselvalueRet.getRefLegalRepID()+", 2:"+TestConstants.refLegRep2ID);
//            this.assertEquals(hrcounselvalueRet.getRefLegalRepID(), TestConstants.refLegRep2ID);
//
//            log.debug("legal role 1:"+hrcounselvalueRet.getLegalRole()+", 2:"+legalRole2);
//            this.assertEquals(hrcounselvalueRet.getLegalRole(), legalRole2);
//
//            this.assertTrue("Found " + hrcounselvalueRet.getStartEndDatesArray().length +
//                            " start/end dates for hearingID/refLegalRepID:" +
//                            TestConstants.hearingID + "/" + TestConstants.refLegRep2ID + ".  " +
//                            "There should be NONE",
//                            hrcounselvalueRet.getStartEndDatesArray().length == 0);
//       }
//        catch(Exception e) {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//        finally
//        {
//            log.debug(">>>>>>>>>>>> testGetRefLegalRepresentative2 Ends <<<<<<<<<<<<<<");
//        }
//    }
//}