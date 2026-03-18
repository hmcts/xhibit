//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.exception.OptimisticLockException;
//import uk.gov.courtservice.framework.services.CSServices;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import junit.framework.*;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HRDefendantValueHelper;
//import org.apache.log4j.Logger;
//import java.util.Date;
//import java.util.Collection;
//import java.util.Iterator;
//import java.text.SimpleDateFormat;
//
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
//public class HRDefendantValueHelperTest extends TransactionTestCase
//{
//    //logger
//    private static Logger log = CSServices.getLogger(HRDefendantValueHelper.class);
//
//    private String firstName = "Marie";
//    private String middleName = "middlename";
//    private String surname = "Holmberg";
//    private Integer noOfTICs = new Integer(1);
//    private String driverNumber = "driverNumber";
//    private Integer finalDrivingLicenseStatus = new Integer(2);
//    private String croNumber = "croNumber";
//    private Integer colMagCourtId = new Integer(666);
//    private String colMagCourtName = "SOUTH WARWICKSHIRE YOUTH COURT";
//
//    public HRDefendantValueHelperTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//
//    /**
//     * setUp()
//     */
//    protected void setUp()
//    {
//        log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<,");
//        try
//        {
//            super.setUp();
//
//            //delete the data
//            TestUtils.execSql(TestConstants.delExportA);
//            TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//            TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
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
//            TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//            TestUtils.execSql(TestConstants.DEL_ADV);
//            TestUtils.execSql(TestConstants.DEL_CR);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.DEL_CASES);
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
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insDefOnCase);
//            TestUtils.execSql(TestConstants.insSolFirm);
//            TestUtils.execSql(TestConstants.insCCInfo);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(TestConstants.insHrg);
//            TestUtils.execSql(TestConstants.insDefHrgRec);
//            TestUtils.execSql(TestConstants.insHrgList);
//            TestUtils.execSql(TestConstants.insSitting);
//            TestUtils.execSql(TestConstants.insShHrg1);
//            TestUtils.execSql(TestConstants.insShHrg2);
//            TestUtils.execSql(TestConstants.insSchedHearDef);
//            TestUtils.execSql(TestConstants.insJudge);
//            TestUtils.execSql(TestConstants.insShAtt);
//            TestUtils.execSql(TestConstants.insShJudge);
//            TestUtils.execSql(TestConstants.insRefLegRep1);
//            TestUtils.execSql(TestConstants.insRefLegRep2);
//            TestUtils.execSql(TestConstants.insShLegRep1);
//            TestUtils.execSql(TestConstants.insShLegRep2);
//            TestUtils.execSql(TestConstants.insExportA);
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
//    /**
//     * Ask the HRDefendantValueHelper to get a HRDefendantValue
//     */
//    public void testGetDefendantHrValue()
//    {
//        HRDefendantValueHelper hrdefendantvaluehelper = new HRDefendantValueHelper();
//        try
//        {
//            log.debug("entered testGetDefendantHrValue");
//            HRDefendantValue hrDefValue = hrdefendantvaluehelper.getDefendantHrValue(TestConstants.defID, TestConstants.caseID, TestConstants.hearingID);
//
//            //is the new hrDefValue what was expected?
//            assertNotNull("Expected the caseValue returned to be a non-null value: ", hrDefValue);
//
//            assertEquals(firstName, hrDefValue.getFirstName());
//            assertEquals(surname, hrDefValue.getSurname());
//            assertEquals(TestConstants.defHrgRecID, hrDefValue.getDefHearingRecordID());
//
//            log.debug("finished testGetDefendantHrValue");
//
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    /**
//     * Ask the HRDefendantValueHelper to get a HRDefendantValue
//     */
//    public void testGetDefendantHrValue2()
//    {
//        HRDefendantValueHelper hrdefendantvaluehelper = new HRDefendantValueHelper();
//        try
//        {
//
//            log.debug("entered testGetDefendantHrValue");
//
//            // Format the current time.
//            SimpleDateFormat formatter = new SimpleDateFormat ("MM.dd.yyyy hh:mm:ss a");
//            java.util.Date currentTime = new Date();
//            String dateString = formatter.format(currentTime);
//            log.debug("---------------- " + dateString + "----------------------");
//
//            TestUtils.execSql("update xhb_defendant set last_conviction_date = TO_Date( '" + dateString + "', 'MM/DD/YYYY HH:MI:SS AM') where defendant_id = " + TestConstants.defID.intValue());
//            TestUtils.execSql("update xhb_defendant set court_id = " + TestConstants.courtID + " where defendant_id = " + TestConstants.defID.intValue());
//            TestUtils.execSql("update xhb_defendant set middle_name = '" + middleName + "' where defendant_id = " + TestConstants.defID.intValue());
//
//            TestUtils.execSql("update xhb_defendant_on_case set no_of_tics = " + 1 + " where defendant_on_case_id = " + TestConstants.defOnCaseID);
//            TestUtils.execSql("update xhb_defendant_on_case set final_driving_licence_status = " + 2 + " where defendant_on_case_id = " + TestConstants.defOnCaseID);
//
//            TestUtils.execSql("insert into xhb_defendant_reference (defendant_id, reference_value, reference_name) values ("+TestConstants.defID.intValue()+", 'driverNumber', 'DRIVER_NO')");
//            TestUtils.execSql("insert into xhb_defendant_reference (defendant_id, reference_value, reference_name) values ("+TestConstants.defID.intValue()+", 'croNumber', 'CRO_NO')");
//
//            HRDefendantValue hrDefValue = hrdefendantvaluehelper.getDefendantHrValue(TestConstants.defID, TestConstants.caseID, TestConstants.hearingID);
//
//            assertNotNull("Expected the caseValue returned to be a non-null value: ", hrDefValue);
//            assertEquals(firstName, hrDefValue.getFirstName());
//            assertEquals(middleName, hrDefValue.getMiddleName());
//            assertEquals(surname, hrDefValue.getSurname());
//            log.debug(" ----- sanity check number two " + hrDefValue.getLastConvictionDate() + " ---- ----- -----");
//            log.debug("----------- sanity number three " + formatter.format(hrDefValue.getLastConvictionDate()) + "-------");
//            assertEquals(dateString, formatter.format(hrDefValue.getLastConvictionDate()));
//            assertEquals(noOfTICs, hrDefValue.getNoOfTICs());
//            assertEquals(driverNumber, hrDefValue.getDriverNumber());
//            assertEquals(finalDrivingLicenseStatus, hrDefValue.getFinalDrivingLicenseStatus());
//            assertEquals(croNumber, hrDefValue.getCroNumber());
//            assertEquals(TestConstants.defHrgRecID, hrDefValue.getDefHearingRecordID());
//            assertEquals(colMagCourtId, hrDefValue.getCollectMagistrateCourtId());
//            assertEquals(colMagCourtName, hrDefValue.getCollectMagistrateCourtName());
//
//            log.debug("finished testGetDefendantHrValue");
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//}