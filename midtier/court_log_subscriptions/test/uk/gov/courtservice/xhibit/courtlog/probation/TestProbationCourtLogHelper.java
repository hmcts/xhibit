//package uk.gov.courtservice.xhibit.courtlog.probation;
//
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
//
///**
// * <p>Title: TestProbationCourtLogHelper</p>
// * <p>Description: Tests the method(s) on the ProbationCourtLogHelper</p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: EDS</p>
// * @author Sarah Tong
// * @version $Id: TestProbationCourtLogHelper.java,v 1.3 2006/07/11 14:16:54 xzfdtb Exp $
// */
//public class TestProbationCourtLogHelper extends TransactionTestCase
//{
//    /**
//     * @todo - Update test data when a new style long adjourn event can be
//     * generated from the client.
//     */
//    private static final Integer CASE_ID = new Integer(1297);
//    private static final Integer COURT_SITE_ID = new Integer(3);
//    private static final Integer COURT_ROOM_ID = new Integer(31);
//
//    private static String selectAll = "SELECT * FROM XHB_PSR_REQUEST";
//    private static String deletePSRRequests = "DELETE FROM XHB_PSR_REQUEST";
//
//    private static final String validPSRLongAdjournEvent =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='30200.xsd'>" +
//            "    <E30200_Long_Adjourn_Options>" +
//            "        <E30200_LAO_PSR_Deft_ID>352</E30200_LAO_PSR_Deft_ID>" +
//            "        <E30200_LAO_Date>21-Jan-2004</E30200_LAO_Date>" +
//            "        <E30200_LAO_Name>TESTING MIDDLE DEFENDANT T20029127-1</E30200_LAO_Name>" +
//            "        <E30200_LAO_Type>E30200_Case_to_be_listed_for_trial</E30200_LAO_Type>" +
//            "        <E30200_LAO_PSR_Required>true</E30200_LAO_PSR_Required>" +
//            "    </E30200_Long_Adjourn_Options>" +
//            "    <free_text></free_text>" +
//            "    <defendant_on_case_id>352</defendant_on_case_id>" +
//            "    <type>30200</type>" +
//            "    <defendant_masked_flag>N</defendant_masked_flag>" +
//            "    <defendant_name>TESTING MIDDLE DEFENDANT T20029127-1</defendant_name>" +
//            "    <defendant_masked_name>Fred</defendant_masked_name>" +
//            "</event>";
//
//    private static final String validLongAdjournEventNoPSR =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='30200.xsd'>" +
//            "    <E30200_Long_Adjourn_Options>" +
//            "        <E30200_LAO_PSR_Deft_ID>352</E30200_LAO_PSR_Deft_ID>" +
//            "        <E30200_LAO_Date>21-Jan-2004</E30200_LAO_Date>" +
//            "        <E30200_LAO_Name>TESTING MIDDLE DEFENDANT T20029127-1</E30200_LAO_Name>" +
//            "        <E30200_LAO_Type>E30200_Case_to_be_listed_for_trial</E30200_LAO_Type>" +
//            "        <E30200_LAO_PSR_Required>false</E30200_LAO_PSR_Required>" +
//            "    </E30200_Long_Adjourn_Options>" +
//            "    <free_text></free_text>" +
//            "    <defendant_on_case_id>352</defendant_on_case_id>" +
//            "    <type>30200</type>" +
//            "    <defendant_masked_flag>N</defendant_masked_flag>" +
//            "    <defendant_name>TESTING MIDDLE DEFENDANT T20029127-1</defendant_name>" +
//            "    <defendant_masked_name>Fred</defendant_masked_name>" +
//            "</event>";
//
//    public TestProbationCourtLogHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate(deletePSRRequests);
//    }
//
//    /**
//     * Checks the request is processed successfully for a valid Long Adjourn
//     * event with PSR Required set. As PSR Required is set this will pass on to
//     * the PSControllerBean and a record should be created in the PSR_Request
//     * table. Just check that the record is created, no need to check the details
//     * as this test is covered in the TestPSControllerBean test class.
//     */
//    public void testProcessRequest() throws Exception
//    {
//        ProbationCourtLogHelper probationcourtloghelper = new ProbationCourtLogHelper();
//        CourtLogSubscriptionValue clSubscrValue =  new CourtLogSubscriptionValue();
//        CourtLogViewValue clViewValue = new CourtLogViewValue();
//        clViewValue.setLogEntry(validPSRLongAdjournEvent);
//        clViewValue.setCaseId(CASE_ID);
//        clSubscrValue.setCourtLogViewValue(clViewValue);
//        clSubscrValue.setCourtSiteId(COURT_SITE_ID);
//        clSubscrValue.setCourtRoomId(COURT_ROOM_ID);
//
//        probationcourtloghelper.processRequest(clSubscrValue);
//
//        // retrieve the record and ensure the correct details have been set
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(selectAll);
//
//            boolean foundRecord = false;
//            while(rs.next())
//            {
//                if (foundRecord)
//                    fail("Multiple PSR Request records found.");
//                foundRecord = true;
//            }
//
//            if (!foundRecord)
//                fail("PSR Request record not created.");
//    }
//
//    /**
//     * Checks the request is processed successfully for a valid Long Adjourn
//     * event without PSR Required set. As PSR Required is not set this will not
//     * pass on to the PSControllerBean and no record should be created in the PSR_Request
//     * table.
//     */
//    public void testProcessRequestNoPSR() throws Exception
//    {
//        ProbationCourtLogHelper probationcourtloghelper = new ProbationCourtLogHelper();
//        CourtLogSubscriptionValue clSubscrValue =  new CourtLogSubscriptionValue();
//        CourtLogViewValue clViewValue = new CourtLogViewValue();
//        clViewValue.setLogEntry(validLongAdjournEventNoPSR);
//        clViewValue.setCaseId(CASE_ID);
//        clSubscrValue.setCourtLogViewValue(clViewValue);
//        clSubscrValue.setCourtSiteId(COURT_SITE_ID);
//        clSubscrValue.setCourtRoomId(COURT_ROOM_ID);
//
//        probationcourtloghelper.processRequest(clSubscrValue);
//
//            // retrieve the record and ensure the correct details have been set
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(selectAll);
//
//            while(rs.next())
//            {
//                fail("No PSR Request record should have been created");
//            }
//    }
//}
//