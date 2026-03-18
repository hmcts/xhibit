//
//package uk.gov.courtservice.xhibit.test.business.services.defendant;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
//
//public class TestDefendantDefOnCaseDetails extends TransactionTestCase
//{
//    DefendantControllerBeanBusinessDelegate delegate = DefendantControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//
//    private Integer caseId = new Integer(1);
//    private Integer defendantId = new Integer(1);
//
//    // initial values
//    private Integer noOfTics = new Integer(5);
//    private Integer finalDrivingLicenceStatus = new Integer(2);
//    private Integer collectMagistrateCourtId = new Integer(666);
//    private String colMagCourtName = "SOUTH WARWICKSHIRE YOUTH COURT";
//    private String driverNumber = "123456789";
//    private String croNumber = "987654321";
//
//    // update values
//    private Integer noOfTicsUpd = new Integer(6);
//    private Integer finalDrivingLicenceStatusUpd = new Integer(0);
//    private Integer collectMagistrateCourtIdUpd = new Integer(3);
//    private String colMagCourtNameUpd = "DUNMOW MAGISTRATES' COURT";
//    private String driverNumberUpd = "555555555";
//    private String croNumberUpd = "888888888";
//
//    public TestDefendantDefOnCaseDetails(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        TestUtils.execSql(
//                "update xhb_defendant_on_case " +
//                "set no_of_tics = " + noOfTics +
//                " final_driving_licence_status =  " + finalDrivingLicenceStatus +
//                " collect_magistrate_court_id =  " + collectMagistrateCourtId +
//                " where defendant_id = " + defendantId + " and " +
//                "case_id = " + caseId + ";");
//        TestUtils.execSql(
//                "delete from xhb_defendant_reference where defendant_id = " +
//                defendantId);
//        TestUtils.execSql(
//                "insert into xhb_defendant_reference " +
//                "(reference_value, reference_name, defendant_id) " +
//                "values " +
//                "('" + driverNumber + "', 'DRIVER_NO', " + defendantId + ")");
//
//        TestUtils.execSql(
//                "insert into xhb_defendant_reference " +
//                "(reference_value, reference_name, defendant_id) " +
//                "values " +
//                "('" + croNumber + "', 'DRIVER_NO', " + defendantId + ")");
//    }
//
//    /**
//     * Tests retrieving defendant details - only checks the values we are
//     * interested in i.e. values we update via Xhibit
//     */
//    public void testGetDefendantDetails()
//    {
//        try
//        {
//            DefendantOnCaseValue defOnCaseValue =
//                    delegate.getDefendantOnCaseDetails(defendantId, caseId) ;
//            assertNotNull(defOnCaseValue);
//            assertEquals(colMagCourtName, defOnCaseValue.getColMagCourtName());
//            assertEquals(collectMagistrateCourtId,
//                         defOnCaseValue.getDefendantOnCaseBVO().getCollectMagistrateCourtId());
//            assertEquals(croNumber, defOnCaseValue.getCRONumber());
//            assertEquals(driverNumber, defOnCaseValue.getDriverNumber());
//            assertEquals(finalDrivingLicenceStatus, defOnCaseValue.getFinalDrivingLicienceStatus());
//            assertEquals(noOfTics, defOnCaseValue.getNoOfTics());
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * Tests updating defendant details - only checks the values we are
//     * interested in i.e. values we update via Xhibit
//     */
//    public void testUpdateDefOnCaseDetails()
//    {
//        try
//        {
//            DefendantOnCaseValue defOnCaseValue =
//                    delegate.getDefendantOnCaseDetails(defendantId, caseId);
//
//            defOnCaseValue.setCRONumber(croNumberUpd);
//            defOnCaseValue.setDriverNumber(driverNumberUpd);
//            defOnCaseValue.setFinalDrivingLicienceStatus(finalDrivingLicenceStatusUpd);
//            defOnCaseValue.setNoOfTics(noOfTicsUpd);
//
//            delegate.updateDefendantOnCaseDetails(defOnCaseValue);
//
//            DefendantOnCaseValue defOnCaseValue2 =
//                    delegate.getDefendantOnCaseDetails(defendantId, caseId);
//
//            assertNotNull(defOnCaseValue2);
//            assertEquals(colMagCourtNameUpd, defOnCaseValue2.getColMagCourtName());
//            assertEquals(collectMagistrateCourtIdUpd,
//                         defOnCaseValue2.getDefendantOnCaseBVO().getCollectMagistrateCourtId());
//            assertEquals(croNumberUpd, defOnCaseValue2.getCRONumber());
//            assertEquals(driverNumberUpd, defOnCaseValue2.getDriverNumber());
//            assertEquals(finalDrivingLicenceStatusUpd, defOnCaseValue2.getFinalDrivingLicienceStatus());
//            assertEquals(noOfTicsUpd, defOnCaseValue2.getNoOfTics());
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            fail();
//        }
//    }
//}