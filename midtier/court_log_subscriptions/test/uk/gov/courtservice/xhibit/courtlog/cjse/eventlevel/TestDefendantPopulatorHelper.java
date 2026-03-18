//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.DefendantPopulatorHelper;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
///**
// * <p>Title: TestDefendantPopulatorHelper</p>
// * <p>Description: Tests the pnc conversion utility method, the
// * populateDefendantattributes method is tested via
// * TestDefendantCjseEventLevelPopulator</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Sarah Tong
// * @version $Id: TestDefendantPopulatorHelper.java,v 1.4 2006/07/11 14:16:53 xzfdtb Exp $
// */
//public class TestDefendantPopulatorHelper extends TransactionTestCase
//{
//    // pnc format: [0-9]{4}[0-9]{7}[A-Za-z0-9]{1} where the first 4 digits
//    // are the year. In crest this is [0-9]{2}[0-9]{7}[A-Za-z0-9]{1} - the
//    // century has been trimmed from the year - we need to add this back on.
//
//    // boundary test values
//    private static final String crestPncId1 = "031234567a";
//    private static final String crestPncId2 = "991234567a";
//    // mid value tests
//    private static final String crestPncId3 = "001234567a";
//    private static final String crestPncId4 = "961234567a";
//
//    // correctly converted values
//    private static final String pncId1 = "20031234567a";
//    private static final String pncId2 = "19991234567a";
//    // mid value tests
//    private static final String pncId3 = "20001234567a";
//    private static final String pncId4 = "19961234567a";
//
//    // values from statndard test data
//    private static final Integer caseId             = new Integer(22);
//    private static final Integer defendantOnCaseId1 = new Integer(3);
//    private static final Integer defendantOnCaseId2 = new Integer(4);
//    private static final Integer defendantOnCaseId3 = new Integer(5);
//    private static final Integer schedHearingId     = new Integer(18);
//
//    // values from data created in setup
//    private static final String prisonerId1 = "ab1234"; //[A-Za-z]{2}[0-9]{4})|([0-9]{5}
//    private static final String croNumber1  = "123454612"; // [0-9]{6}[0-9]{2}
//    private static final String prisonerId2 = "cd5678"; //[A-Za-z]{2}[0-9]{4})|([0-9]{5}
//    private static final String croNumber2  = "65432121"; // [0-9]{6}[0-9]{2}
//
//
//
//    public TestDefendantPopulatorHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        // add the prisoner, cro and juvenile data for def \ def on case 3
//        DatabaseUtil.executeSql(connection,
//                "INSERT INTO XHB_DEFENDANT_REFERENCE " +
//                "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//                "VALUES " +
//                "  ('PRISONER_NO', '" + prisonerId1 + "', 3)");
//        DatabaseUtil.executeSql(connection,
//                "INSERT INTO XHB_DEFENDANT_REFERENCE " +
//                "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//                "VALUES " +
//                "  ('CRO_NO', '" + croNumber1 + "', 3)");
//        DatabaseUtil.executeSql(connection,
//                "UPDATE XHB_DEFENDANT_ON_CASE " +
//                "SET IS_JUVENILE = 'Y' WHERE DEFENDANT_ON_CASE_ID = 3");
//        // add the same prisoner, cro and juvenile data for def \ def on case 4 & 5
//        for (int i = 4; i < 6; i++)
//        {
//            DatabaseUtil.executeSql(connection,
//            "INSERT INTO XHB_DEFENDANT_REFERENCE " +
//            "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//            "VALUES " +
//            "  ('PRISONER_NO', '" + prisonerId2 + "', " + i + ")");
//            DatabaseUtil.executeSql(connection,
//                    "INSERT INTO XHB_DEFENDANT_REFERENCE " +
//                    "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//                    "VALUES " +
//                    "  ('CRO_NO', '" + croNumber2 + "', " + i + ")");
//            DatabaseUtil.executeSql(connection,
//                    "UPDATE XHB_DEFENDANT_ON_CASE " +
//                    "SET IS_JUVENILE = 'N' WHERE DEFENDANT_ON_CASE_ID = " + i);
//        }
//
//        // add prison id for defendantId 3
//        DatabaseUtil.executeSql(connection,
//                "UPDATE XHB_DEFENDANT " +
//                "SET PRISON_ID = 'ABCD' WHERE DEFENDANT_ID = 3");
//
//        // add pncId for defendantOnCase 2 format: [0-9]{4}[0-9]{7}[A-Za-z0-9]{1}?
//        DatabaseUtil.executeSql(connection,
//                "UPDATE XHB_DEFENDANT_ON_CASE " +
//                "SET PNC_ID = '" + crestPncId1 + "' WHERE DEFENDANT_ON_CASE_ID = 3");
//
//        // add defHearingRecords with bail status for defOnCase 2 & hearingId 18
//        DatabaseUtil.executeSql(connection,
//                "INSERT INTO XHB_DEF_HEARING_RECORD " +
//                " (DEFENDANT_ON_CASE_ID, HEARING_ID, START_BAIL_STATUS, END_BAIL_STATUS) " +
//                "VALUES " +
//                " (3, " + schedHearingId + ", 'B', 'J')");
//
//        // Add an earlier record so we can make sure this one is ignored
//        DatabaseUtil.executeSql(connection,
//                "INSERT INTO XHB_DEF_HEARING_RECORD " +
//                " (DEFENDANT_ON_CASE_ID, HEARING_ID, START_BAIL_STATUS, END_BAIL_STATUS, CREATION_DATE) " +
//                "VALUES " +
//                " (3, " + schedHearingId + ", 'B', 'C', (sysdate-1))");
//
//        // add the same values for defendantIs\defOnCaseIds 4 & 5
//        for (int i = 4; i < 6; i++)
//        {
//            DatabaseUtil.executeSql(connection,
//            "UPDATE XHB_DEFENDANT " +
//            "SET PRISON_ID = 'EFGH' WHERE DEFENDANT_ID = " + i);
//
//            DatabaseUtil.executeSql(connection,
//                    "UPDATE XHB_DEFENDANT_ON_CASE " +
//                    "SET PNC_ID = '" + crestPncId2 + "' WHERE DEFENDANT_ON_CASE_ID = " + i);
//
//            DatabaseUtil.executeSql(connection,
//                    "INSERT INTO XHB_DEF_HEARING_RECORD " +
//                    " (DEFENDANT_ON_CASE_ID, HEARING_ID, START_BAIL_STATUS, END_BAIL_STATUS) " +
//                    "VALUES " +
//                    " (" + i + ", " + schedHearingId + ", 'B', 'B')");
//        }
//    }
//
//    public void testConvertCrestPncId()
//    {
//        DefendantPopulatorHelper defendantPopulatorHelper = new DefendantPopulatorHelper();
//        String stringRet1 = defendantPopulatorHelper.convertCrestPncId(crestPncId1);
//        assertEquals(pncId1, stringRet1);
//        String stringRet2 = defendantPopulatorHelper.convertCrestPncId(crestPncId2);
//        assertEquals(pncId2, stringRet2);
//        String stringRet3 = defendantPopulatorHelper.convertCrestPncId(crestPncId3);
//        assertEquals(pncId3, stringRet3);
//        String stringRet4 = defendantPopulatorHelper.convertCrestPncId(crestPncId4);
//        assertEquals(pncId4, stringRet4);
//    }
//
//    /**
//     * populateDefendantAttributes is already tested via
//     * TestDefendantCjseEventLevelPopulator, however this test accepts multiple
//     * defendantOnCaseIds and makes sure the eradication of duplicate values in
//     * all attributes is functioning correctly
//     */
//    public void testPopulateDefendantAttributes()
//    {
//        XhbCase theCase = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
//
//        // get the XhbDefendantOnCase local refs for the lead case
//        Collection defendantOnCases = theCase.getXhbDefendantOnCases();
//
//        EventParameters eventParameters = new EventParameters();
//
//        DefendantPopulatorHelper defendantPopulatorHelper = new DefendantPopulatorHelper();
//        defendantPopulatorHelper.populateDefendantAttributes(eventParameters, defendantOnCases);
//
//        // should have two values for each attribute as two out of the thre will
//        // be the same
//        assertEquals(2, eventParameters.getPrisons().getPrisonCount());
//        assertEquals(2, eventParameters.getPNCIDs().getPNCIDCount());
//        assertEquals(2, eventParameters.getPrisonerIDs().getPrisonerIDCount());
//        // leave one empty to check no records behaviour
//        assertEquals(null, eventParameters.getPTIURNs());
//        assertEquals(2, eventParameters.getCRONumbers().getCRONumberCount());
//        // Gender 1-Male, 2-Female otherwise Unknown
//        assertEquals(2, eventParameters.getGenders().getGenderCount());
//        assertEquals(2, eventParameters.getBailStatuses().getBailStatusCodeCount());
//        // Make sure the bail statuses we have are 'B' and 'J', the earlier
//        // status 'C' is not taken
//        String[] bailCodeArray = eventParameters.getBailStatuses().getBailStatusCode();
//        List bailCodeList = Arrays.asList(bailCodeArray);
//        assertTrue("Bail status codes should not contain 'C'",!bailCodeList.contains("C"));
//        assertEquals(2, eventParameters.getIsJuveniles().getIsJuvenileCount());
//    }
//}
//