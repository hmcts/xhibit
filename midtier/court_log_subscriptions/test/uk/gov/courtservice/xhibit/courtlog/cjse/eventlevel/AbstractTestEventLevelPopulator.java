//package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//import javax.naming.NamingException;
//import java.sql.SQLException;
//import java.util.Vector;
//
///**
// * <p>Title: AbstractTestEventLevelPopulator</p>
// * <p>Description: Sets up data and provides population test used by all
// * TestXXXCjseEventLevelPopulator classes. Prevents duplication of test data
// * set up code and assertTrue statement groups.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// *
// * @author Sarah Tong
// */
//public abstract class AbstractTestEventLevelPopulator extends TransactionTestCase {
//    // values from standard set of test data
//    public static final Integer defendantOnCaseId = new Integer(4);
//    public static final Integer defendantOnOffenceId = new Integer(7);
//    public static final Integer caseId = new Integer(22);
//    public static final Integer[] caseIds = new Integer[]{new Integer(22),
//                                                    new Integer(31),
//                                                    new Integer(40)};
//    /**
//     * @todo CourtLogSubscriptionValue will be changed to primary keys
//     * as a Longs, when this happens the array below will no longer be
//     * needed
//     */
//    public static final Integer[] intCaseIds = new Integer[]{new Integer(22),
//                                                             new Integer(31),
//                                                             new Integer(40)};
//    // case file ids for these cases
//    private static final String caseFileId1 = "T20028899";
//    private static final String caseFileId2 = "T20028898";
//    private static final String caseFileId3 = "T20028897";
//    public static final Integer schedHearingId = new Integer(18);
//    // this data has defendantId: 4, defendantOnOffenceIds: 7,8
//    // hearing id 18
//
//    // values from data created in setup
//    public static final String prisonerId = "ab1234"; //[A-Za-z]{2}[0-9]{4})|([0-9]{5}
//    public static final String croNumber = "123454612"; // [0-9]{6}[0-9]{2}
//    // crn format: [0-9]{2}[A-Za-z0-9]{4}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}[0-9]{3}
//    public static final String crnId1 = "11AAAA22SSSSSSSSSSSD123";
//    public static final String crnId2 = "22ZZZZ33XXXXXXXXXXXC456";
//    public static final String asnId1 = "11AAAA22SSSSSSSSSSSD";
//    public static final String asnId2 = "22ZZZZ33XXXXXXXXXXXC";
//    public static final String crestPncId1 = "031234567a";
//    public static final String pncId1 = "20031234567a";
//    private static final String prisonId1 = "ABCD";
//    private static final String prisonId2 = "EFGH";
//    private static final String prisonId3 = "IJKL";
//
//    private Vector crnIds = new Vector();
//
//    public AbstractTestEventLevelPopulator(String s) throws NamingException {
//        super(s, true);
//    }
//
//    // insert the test data used by the joinder, case, def and crn populator tests
//    protected void setUp() throws Exception {
//        super.setUp();
//        crnIds.add("11AAAA22SSSSSSSSSSSD789");
//        crnIds.add("11AAAA22SSSSSSSSSSSD987");
//        crnIds.add("11AAAA22SSSSSSSSSSSD654");
//        crnIds.add("11AAAA22SSSSSSSSSSSD321");
//        crnIds.add("11BBBB22SSSSSSSSSSSD123");
//        crnIds.add(crnId1);
//        crnIds.add(crnId2);
//        crnIds.add("11CCCC22SSSSSSSSSSSD123");
//        crnIds.add("11DDDD22SSSSSSSSSSSD123");
//        // add prisoner, cro and juvenile data for def \ def on case 4
//        executeSql("INSERT INTO XHB_DEFENDANT_REFERENCE " +
//                "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//                "VALUES " +
//                "  ('PRISONER_NO', '" + prisonerId + "', 4)");
//        executeSql("INSERT INTO XHB_DEFENDANT_REFERENCE " +
//                "  (REFERENCE_NAME, REFERENCE_VALUE, DEFENDANT_ID)" +
//                "VALUES " +
//                "  ('CRO_NO', '" + croNumber + "', 4)");
//        executeSql("UPDATE XHB_DEFENDANT_ON_CASE " +
//                "SET IS_JUVENILE = 'Y' WHERE DEFENDANT_ON_CASE_ID = 4");
//
//        // add crnId for defOnOffence 2, 3, 4, 5, 6, 7, 8 ,9, 10
//        for (int i = 2; i < 11; i++) {
//            executeSql("UPDATE XHB_DEFENDANT_ON_OFFENCE " +
//                    "SET CRN_ID = '" + crnIds.elementAt(i - 2) + "' " +
//                    "WHERE DEFENDANT_ON_OFFENCE_ID = " + i);
//        }
//        // add pncId for defendantOnCase 4 format: [0-9]{2}[0-9]{7}[A-Za-z0-9]{1}
//        executeSql("UPDATE XHB_DEFENDANT_ON_CASE " +
//                "SET PNC_ID = '" + crestPncId1 + "' WHERE DEFENDANT_ON_CASE_ID = 4");
//
//        // add defHearingRecords with bail status for defOnCase 4 & hearingId 18
//        executeSql("INSERT INTO XHB_DEF_HEARING_RECORD " +
//                " (DEFENDANT_ON_CASE_ID, HEARING_ID, START_BAIL_STATUS, END_BAIL_STATUS) " +
//                "VALUES " +
//                " (4, 18, 'B', 'J')");
//        // add prison id for defendantId 3
//        executeSql("UPDATE XHB_DEFENDANT " +
//                "SET PRISON_ID = '" + prisonId1 + "' WHERE DEFENDANT_ID = 3");
//        // add prison id for defendantId 4
//        executeSql("UPDATE XHB_DEFENDANT " +
//                "SET PRISON_ID = '" + prisonId2 + "' WHERE DEFENDANT_ID = 4");
//        // add prison id for defendantId 5
//        executeSql("UPDATE XHB_DEFENDANT " +
//                "SET PRISON_ID = '" + prisonId3 + "' WHERE DEFENDANT_ID = 5");
//    }
//
//    /**
//     * Test the populated defendant attributes based on defendantOnCaseId 4
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkSingleDefAttributes(EventParameters eventParameters) {
//        System.out.println("checkSingleDefAttributes() start");
//        assertEquals(prisonId2, eventParameters.getPrisons().getPrison(0));
//        assertEquals(pncId1, eventParameters.getPNCIDs().getPNCID(0));
//        assertEquals(prisonerId, eventParameters.getPrisonerIDs().getPrisonerID(0));
//        // leave one empty to check no records behaviour
//        assertEquals(null, eventParameters.getPTIURNs());
//        assertEquals(croNumber, eventParameters.getCRONumbers().getCRONumber(0));
//        // Gender 1-Male, 2-Female otherwise Unknown
//        assertEquals("Female", eventParameters.getGenders().getGender(0).toString());
//        assertEquals("J", eventParameters.getBailStatuses().getBailStatusCode(0));
//        assertEquals(true, eventParameters.getIsJuveniles().getIsJuvenile(0));
//    }
//
//    /**
//     * Test the crn attributes based on defendantOnOffenceIds 7 & 8 (from
//     * defendantOnCaseId 4)
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkCrnsForDef(EventParameters eventParameters) {
//        System.out.println("checkCrnsForDef() start");
//        // check asns and crns
//        assertEquals(2, eventParameters.getCRNIDs().getCRNIDCount());
//        assertEquals(2, eventParameters.getASNs().getASNCount());
//        Vector asns = new Vector();
//        asns.add(eventParameters.getASNs().getASN(0));
//        asns.add(eventParameters.getASNs().getASN(1));
//        Vector crns = new Vector();
//        crns.add(eventParameters.getCRNIDs().getCRNID(0));
//        crns.add(eventParameters.getCRNIDs().getCRNID(1));
//        assertTrue(crns.contains(crnId1));
//        assertTrue(crns.contains(crnId2));
//        assertTrue(asns.contains(asnId1));
//        assertTrue(asns.contains(asnId2));
//    }
//
//    /**
//     * Test the case attributes based on caseId 22
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkSingleCase(EventParameters eventParameters) {
//        System.out.println("checkSingleCase() start");
//        assertEquals(caseFileId1, eventParameters.getCaseFileIDs().getCaseFileID(0));
//    }
//
//    /**
//     * Test the crn attributes based on defendantOnOfenceId 7
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkSingleCrn(EventParameters eventParameters) {
//        System.out.println("checkSingleCrn() start");
//        assertEquals(crnId1, eventParameters.getCRNIDs().getCRNID(0));
//        assertEquals(asnId1, eventParameters.getASNs().getASN(0));
//    }
//
//    /**
//     * Check the case attributes based on caseIds 22, 31, 40
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkJoinderCases(EventParameters eventParameters) {
//        assertEquals(3, eventParameters.getCaseFileIDs().getCaseFileIDCount());
//        Vector caseFileIds = new Vector();
//        caseFileIds.add(eventParameters.getCaseFileIDs().getCaseFileID(0));
//        caseFileIds.add(eventParameters.getCaseFileIDs().getCaseFileID(1));
//        caseFileIds.add(eventParameters.getCaseFileIDs().getCaseFileID(2));
//        assertTrue(caseFileIds.contains(caseFileId1));
//        assertTrue(caseFileIds.contains(caseFileId2));
//        assertTrue(caseFileIds.contains(caseFileId3));
//    }
//
//    /**
//     * Check the defendant attributes based on case 22 (defOnCases 3,4,5).
//     * Basic check to make sure we have all 3 defOnCase present, a full check of
//     * defendant level population is performed by the checkSingleDefAttributes()
//     * method.
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkCaseDefendants(EventParameters eventParameters) {
//        assertEquals(3, eventParameters.getPrisons().getPrisonCount());
//        Vector prisons = new Vector();
//        prisons.add(eventParameters.getPrisons().getPrison(0));
//        prisons.add(eventParameters.getPrisons().getPrison(1));
//        prisons.add(eventParameters.getPrisons().getPrison(2));
//        assertTrue(prisons.contains(prisonId1));
//        assertTrue(prisons.contains(prisonId2));
//        assertTrue(prisons.contains(prisonId3));
//    }
//
//    /**
//     * Checks the crn attributes for case 22 (defOnOffences 2 -10).
//     * Basic check to make sure we have all 9 defOnOffences present, a full check
//     * of crn level population is performed by the checkSingleCrn()
//     * method.
//     *
//     * @param eventParameters contains the attributes to check
//     */
//    public void checkCrnsForCase(EventParameters eventParameters) {
//        assertEquals(9, eventParameters.getCRNIDs().getCRNIDCount());
//        Vector returnedCrnIds = new Vector();
//        for (int i = 0; i < 9; i++) {
//            returnedCrnIds.add(eventParameters.getCRNIDs().getCRNID(i));
//        }
//
//        assertTrue(returnedCrnIds.containsAll(crnIds));
//    }
//
//    /**
//     * Delegate helper method to call the database utility class for the
//     * correct execution of sql scripts using the framework-available database
//     * connection.
//     *
//     * @param sql The SQL <code>String</code> to execute
//     * @throws SQLException
//     * @see uk.gov.courtservice.framework.testutils.DatabaseUtil#executeSql(
//            *      java.sql.connection, java.lang.String);
//     */
//    protected void executeSql(String sql) throws SQLException {
//        DatabaseUtil.executeSql(connection, sql);
//    }
//}
//