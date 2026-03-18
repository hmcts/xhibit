//package uk.gov.courtservice.xhibit.test.business.services.directions;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.rmi.PortableRemoteObject;
//import javax.sql.DataSource;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
//import uk.gov.courtservice.xhibit.business.services.directions.DirectionsControllerBeanBusinessDelegate;
//import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
//import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
//import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
//
///**
// * <p>Title: TestResultsDirections</p>
// * <p>Description: Tests the directions functionality of the ResultsController</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Ian Hannaford, Sarah Tong
// * @version $Id: TestResultsDirections.java,v 1.3 2006/07/13 12:58:02 xzfdtb Exp $
// */
//public class TestResultsDirections extends TestCase
//{
//    private static final Logger log =  CSServices.getLogger(TestResultsDirections.class);
//
//    // values from standard test data
//    private static final Integer DEF_ON_CASE_ID_VAL_1 = new Integer(10);
//    private static final Integer CASE_ID_VAL = new Integer(40);
//
//    private DirectionsControllerBeanBusinessDelegate delegate;
//
//    private static String directionsForCaseQuery =
//            "SELECT *\n" +
//            "FROM   XHB_DIRECTIONS_FOR_CASE\n" +
//            "WHERE  DIRECTIONS_FOR_CASE_ID = (\n" +
//            "       SELECT MAX(DIRECTIONS_FOR_CASE_ID)\n" +
//            "       FROM   XHB_DIRECTIONS_FOR_CASE)";
//
//    private static String directionsForDefendantQuery =
//            "SELECT *\n" +
//            "FROM   XHB_DIRECTIONS_FOR_DEFENDANT\n" +
//            "WHERE  DIRECTIONS_FOR_DEFENDANT_ID = (\n" +
//            "       SELECT MAX(DIRECTIONS_FOR_DEFENDANT_ID)\n" +
//            "       FROM   XHB_DIRECTIONS_FOR_DEFENDANT)";
//
//    // DirectionsForCase column names
//    private static final String DIR_FOR_CASE_ID = "DIRECTIONS_FOR_CASE_ID";
//    private static final String HAS_PANDD_FORM  = "HAS_PANDD_FORM";
//    private static final String TRIAL_TIME_EST  = "TRIAL_TIME_ESTIMATE";
//    private static final String TRIAL_TIME_UNIT = "TRIAL_TIME_UNIT";
//    private static final String DIRECTIONS_TEXT = "DIRECTIONS_TEXT";
//    private static final String DATE_TIME       = "DATE_TIME";
//    private static final String CASE_ID         = "CASE_ID";
//    private static final String DEF_ON_CASE_ID  = "DEFENDANT_ON_CASE_ID";
//
//    // DirectionsForDefendant column names
//    private static final String DIR_FOR_DEF_ID  = "DIRECTIONS_FOR_DEFENDANT_ID";
//    private static final String IS_IDENTIFIED   = "IS_IDENTIFIED";
//    private static final String ARRAIGNED       = "ARRAIGNED";
//    private static final String BAIL_STATUS     = "BAIL_STATUS";
//    private static final String NEW_BAIL_COND   = "NEW_BAIL_CONDITIONS";
//    private static final String CERT_ATTENDANCE = "CERT_ATTENDANCE";
//    private static final String FILED_FORM_B    = "FILED_FORM_B";
//    private static final String TO_BE_FILED_BY  = "TO_BE_FILED_BY";
//
//    private Calendar todayCal = Calendar.getInstance();
//    private Date todayDate = todayCal.getTime();
//
//    private UserTransaction transaction;
//    private DataSource dataSource;
//    private Connection connection;
//
//    public static final String DEFAULT_DATA_SOURCE = "XhibitOracleTxDataSource";
//    public static final String DEFAULT_TRANSACTION = "weblogic/transaction/UserTransaction";
//
//    private Integer toDeleteDirForCase;
//    private Integer toDeleteDirForDef;
//
//    private int maxCourtLogEntry = -1;
//
//    public TestResultsDirections(String s) throws NamingException
//    {
//        super(s);
//        delegate = DirectionsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//
//        InitialContext ic = new InitialContext();
//        Object obj = ic.lookup(DEFAULT_TRANSACTION);
//        transaction = (UserTransaction) PortableRemoteObject.narrow(obj, UserTransaction.class);
//        obj = ic.lookup(DEFAULT_DATA_SOURCE);
//        dataSource = (DataSource) PortableRemoteObject.narrow(obj, DataSource.class);
//    }
//
//    protected void setUp() throws Exception
//    {
//        connection = dataSource.getConnection();
//
//        // find the last court log entry - we will delete anything created after
//        // this in the tearDown
//        Statement stmt = connection.createStatement();
//
//        ResultSet rs = stmt.executeQuery("SELECT MAX(entry_id) FROM XHB_COURT_LOG_ENTRY");
//        if(rs.next())
//        {
//            maxCourtLogEntry = rs.getInt(1);
//        }
//
//        // add DirectionsForCase, DirectionsForDefendant and DirectionAttend
//        // records to test updating
//    }
//
//    protected void tearDown()
//    {
//        try
//        {
//            // clear out the directions we've added
//            Statement stmt = connection.createStatement();
//
//            if (toDeleteDirForCase != null)
//            {
//                stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_CASE " +
//                                  "WHERE DIRECTIONS_FOR_CASE_ID = " +
//                                  toDeleteDirForCase.intValue());
//            }
//            if (toDeleteDirForDef != null)
//            {
//                stmt.executeQuery("DELETE FROM XHB_DIRECTIONS_FOR_DEFENDANT " +
//                                  "WHERE DIRECTIONS_FOR_DEFENDANT_ID = " +
//                                  toDeleteDirForDef.intValue());
//            }
//            if (maxCourtLogEntry != -1)
//            {
//                stmt.executeQuery(
//                        "DELETE FROM XHB_COURT_LOG_ENTRY WHERE entry_id > " +
//                        maxCourtLogEntry);
//            }
//        }
//        catch (SQLException ex)
//        {
//            ex.printStackTrace();
//        }
//        finally
//        {
//            try
//            {
//                connection.close();
//            }
//            catch (SQLException ex)
//            {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Tests adding new Directions where no Directions records exist
//     */
//    public void testInsertDirections() throws Exception
//    {
//        log.debug("testDirections() start new");
//        DirectionsValue dirValue = new DirectionsValue();
//
//        // set up a new DirectionsForCaseValue
//        DirectionsForCaseValue dirForCaseValue = new DirectionsForCaseValue();
//        getDirForCaseValue(dirForCaseValue);
//
//        // put DirectionsForCaseValue into DirectionsValue
//        dirValue.setDirectionsForCaseValue(dirForCaseValue);
//
//        // set up a new DirectionsForDefendantValue
//        DirectionsForDefendantValue dirForDefValue = new DirectionsForDefendantValue();
//        getDirForDefValue(dirForDefValue);
//
//        ArrayList dirForDefCol = new ArrayList();
//        dirForDefCol.add(dirForDefValue);
//
//        // put DirectionsForDefendantValue into DirectionsValue
//        dirValue.setDirectionsForDefendantValue(dirForDefCol);
//
//        // call saveDirections()
//        log.debug("Calling save...");
//
//        delegate.saveDirections(dirValue);
//        log.debug("Directions saved.");
//
//        // Retrieve the most recent DirectionsForCase, DirectionAttend and
//        // DirectionsForDefendant records
//        DirectionsForCaseValue dirForCaseValueRet =
//                                   getNewDirectionsForCase();
//        toDeleteDirForCase = dirForCaseValueRet.getDirectionsForCaseBasicValue().getPrimaryKey();
//        DirectionsForDefendantValue dirForDefValueRet =
//                                             getNewDirectionsForDefendant();
//        toDeleteDirForDef = dirForDefValueRet.getDirectionsForDefendantBasicValue().getPrimaryKey();
//
//        // compare these to the values we set
//        compareDirForCaseValues(dirForCaseValue, dirForCaseValueRet);
//        compareDirForDefValues(dirForDefValue, dirForDefValueRet);
//        log.debug("testDirections() finished");
//    }
//
//    /**
//     * Tests updating the Directions in the system with a newer version
//     */
//    public void todoUpdateLatestDirections()
//    {
//        // find the id
//
//        // update directions
//
//        // check updated
//
//    }
//
//    /**
//     * Tests adding Directions where the date of the Directions is before the
//     * existing Directions in the system. This is unlikely to occur in practice
//     * but is possible as the user is able to change the date of the Directions
//     * when saving.
//     */
//    public void todoInsertOldDirections()
//    {
//        // find the id
//
//        // update directions
//
//        // check updated
//    }
//
//    private void getDirForDefValue(DirectionsForDefendantValue dirForDefValue)
//    {
//        XhbDirectionsForDefendantBasicValue dirForDef = new XhbDirectionsForDefendantBasicValue();
//        dirForDef.setArraigned("Y");
//        dirForDef.setBailStatus("Y");
//        dirForDef.setCertAttendance("4");
//        dirForDef.setFiledFormB("Y");
//        dirForDef.setIsIdentified("Y");
//        dirForDef.setNewBailConditions("CONDITIONS");
//        dirForDef.setDefendantOnCaseId(DEF_ON_CASE_ID_VAL_1);
//        dirForDef.setDateTime(todayCal.getTime());
//        dirForDefValue.setDirectionsForDefendantBasicValue(dirForDef);
//
//        // create the directions for defendant court log events
//        // arraigned
//        CourtLogCRUDValue arraignedCRUD = new CourtLogCRUDValue();
//        arraignedCRUD.setCaseId(CASE_ID_VAL);
//        arraignedCRUD.setDefendantOnCaseId(DEF_ON_CASE_ID_VAL_1);
//        arraignedCRUD.setEntryDate(todayDate);
//        arraignedCRUD.setEventType(new Integer(40705));
//        HashMap arraignedProps = new HashMap();
//        arraignedProps.put("E40705_Arraignment", "true");
//        arraignedProps.put("Defendant_Name","DEFENDANT T20028897-2");
//        arraignedCRUD.setProperty("Direction_By_Defendant_Options",arraignedProps);
//        // bail and custody
//        CourtLogCRUDValue bailCRUD = new CourtLogCRUDValue();
//        bailCRUD.setCaseId(CASE_ID_VAL);
//        bailCRUD.setDefendantOnCaseId(DEF_ON_CASE_ID_VAL_1);
//        bailCRUD.setEntryDate(todayDate);
//        bailCRUD.setEventType(new Integer(40706));
//        HashMap bailProps = new HashMap();
//        bailProps.put("E40705_Arraignment", "Y");
//        bailProps.put("Defendant_Name","DEFENDANT T20028897-2");
//        bailCRUD.setProperty("Direction_By_Defendant_Options",bailProps);
//
//        CourtLogCRUDValue[] cruds = new CourtLogCRUDValue[] {
//                                                           arraignedCRUD};
//        dirForDefValue.setCourtLogCRUDValue(cruds);
//    }
//
//    private void getDirForCaseValue(DirectionsForCaseValue dirForCaseValue)
//    {
//        XhbDirectionsForCaseBasicValue dirForCaseBV = new XhbDirectionsForCaseBasicValue();
//        dirForCaseBV.setHasPanddForm("Y");
//        dirForCaseBV.setTrialTimeEstimate(new Float(6));
//        dirForCaseBV.setTrialTimeUnit(new Integer(2));
//        dirForCaseBV.setDirectionsText("THIS IS DIRECTIONS TEXT");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(10,01,2003);
//        dirForCaseBV.setDateTime(calendar.getTime());
//        dirForCaseBV.setCaseId(CASE_ID_VAL);
//        dirForCaseValue.setDirectionsForCaseBasicValue(dirForCaseBV);
//
//        // create the directions for case court log events
//        // directions
//        CourtLogCRUDValue directionsCRUD = new CourtLogCRUDValue();
//        directionsCRUD.setCaseId(CASE_ID_VAL);
//        directionsCRUD.setEntryDate(todayDate);
//        directionsCRUD.setEventType(new Integer(40712));
//        HashMap directionsProps = new HashMap();
//        directionsProps.put("E40712_Directions", "THIS IS DIRECTIONS TEXT");
//        directionsCRUD.setProperty("Directions_By_Case_Options", directionsProps);
//
//        // P and D
//        CourtLogCRUDValue panddCRUD = new CourtLogCRUDValue();
//        panddCRUD.setCaseId(CASE_ID_VAL);
//        panddCRUD.setEntryDate(todayDate);
//        panddCRUD.setEventType(new Integer(40710));
//        HashMap panddProps = new HashMap();
//        panddProps.put("E40710_P_And_D", "E40710_Form_Handed_In");
//        panddCRUD.setProperty("Directions_By_Case_Options", panddProps);
//
//        // trial time
//        CourtLogCRUDValue trialTimeCRUD = new CourtLogCRUDValue();
//        trialTimeCRUD.setCaseId(CASE_ID_VAL);
//        trialTimeCRUD.setEntryDate(todayDate);
//        trialTimeCRUD.setEventType(new Integer(40711));
//        HashMap trialTimeProps = new HashMap();
//        HashMap trialTimeOptions = new HashMap();
//        trialTimeOptions.put("E40711_Time_Estimate_Options","E40711_Days");
//        trialTimeOptions.put("E40711_Time","6.3");
//        trialTimeProps.put("E40711_Time_Estimate", trialTimeOptions);
//        trialTimeCRUD.setProperty("Directions_By_Case_Options", trialTimeProps);
//
//        CourtLogCRUDValue[] cruds = new CourtLogCRUDValue[] {
//                                                     directionsCRUD,
//                                                     panddCRUD,
//                                                     trialTimeCRUD};
//        dirForCaseValue.setCourtLogCRUDValues(cruds);
//    }
//
//    private void compareDirForCaseValues(DirectionsForCaseValue expected,
//                                         DirectionsForCaseValue actual)
//    {
//        XhbDirectionsForCaseBasicValue expectedBV =
//                expected.getDirectionsForCaseBasicValue();
//        XhbDirectionsForCaseBasicValue actualBV =
//                actual.getDirectionsForCaseBasicValue();
//
//        // compare values in the basic value objects
//        assertEquals("Case Id: ",
//                     expectedBV.getCaseId(), actualBV.getCaseId());
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c1.setTime(expectedBV.getDateTime());
//        c2.setTime(actualBV.getDateTime());
//        compareCalendarDates(c1, c2, "DateTime");
//        assertEquals("Directions Text: ",
//                     expectedBV.getDirectionsText(), actualBV.getDirectionsText());
//        assertEquals("Has P and D: ",
//                     expectedBV.getHasPanddForm(), actualBV.getHasPanddForm());
//        assertEquals("Trial Time Estimate: ",
//                     expectedBV.getTrialTimeEstimate(), actualBV.getTrialTimeEstimate());
//        assertEquals("Trial Time Unit: ",
//                     expectedBV.getTrialTimeUnit(), actualBV.getTrialTimeUnit());
//    }
//
//    private void compareCalendarDates(Calendar expectedCalDate,
//                                      Calendar actualCalDate,
//                                      String message)
//    {
//        if (expectedCalDate == null && actualCalDate == null)
//        {
//            return;
//        }
//        assertEquals(message + " year: ",
//                     expectedCalDate.get(Calendar.YEAR),
//                     actualCalDate.get(Calendar.YEAR));
//        assertEquals(message + " month: ",
//                     expectedCalDate.get(Calendar.MONTH),
//                     actualCalDate.get(Calendar.MONTH));
//        assertEquals(message + " day: ",
//                     expectedCalDate.get(Calendar.DAY_OF_MONTH),
//                     actualCalDate.get(Calendar.DAY_OF_MONTH));
//        assertEquals(message + " hour: ",
//                     expectedCalDate.get(Calendar.HOUR),
//                     actualCalDate.get(Calendar.HOUR));
//        assertEquals(message + " minute: ",
//                     expectedCalDate.get(Calendar.MINUTE),
//                     actualCalDate.get(Calendar.MINUTE));
//        assertEquals(message + " second: ",
//                     expectedCalDate.get(Calendar.SECOND),
//                     actualCalDate.get(Calendar.SECOND));
//    }
//
//    private DirectionsForDefendantValue getNewDirectionsForDefendant()
//    {
//        // Find the DirectionsForCase record
//        DirectionsForDefendantValue dirForDefValueRet = null;
//        try
//        {
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(directionsForDefendantQuery);
//            if(rs.next())
//            {
//                dirForDefValueRet = getDirForDefValueFromResultSet(rs);
//            }
//        }
//        catch (SQLException ex)
//        {
//            ex.printStackTrace();
//            fail("SQLException attempting to read Directions For Defendant from the db: " +
//                 ex.getMessage());
//        }
//        return dirForDefValueRet;
//    }
//
//    private DirectionsForDefendantValue getDirForDefValueFromResultSet(ResultSet rs)
//    {
//        DirectionsForDefendantValue dirForDefValue =
//                                              new DirectionsForDefendantValue();
//        XhbDirectionsForDefendantBasicValue dirForDefBV =
//                                         new XhbDirectionsForDefendantBasicValue();
//
//        try
//        {
////            dirForDefBV.setId(
////                    getIntegerFromResultSetColumn(rs, DIR_FOR_DEF_ID));
//            dirForDefBV.setArraigned(rs.getString(ARRAIGNED));
//            dirForDefBV.setBailStatus(rs.getString(BAIL_STATUS));
//            dirForDefBV.setCertAttendance(rs.getString(CERT_ATTENDANCE));
//            dirForDefBV.setDateTime(
//                    getCalendarFromResultSetColumn(rs, DATE_TIME).getTime());
//            dirForDefBV.setDefendantOnCaseId(
//                    getIntegerFromResultSetColumn(rs, DEF_ON_CASE_ID));
//            dirForDefBV.setFiledFormB(rs.getString(FILED_FORM_B));
//            dirForDefBV.setIsIdentified(rs.getString(IS_IDENTIFIED));
//            dirForDefBV.setNewBailConditions(rs.getString(NEW_BAIL_COND));
//            dirForDefBV.setToBeFiledBy(
//                    getCalendarFromResultSetColumn(rs, TO_BE_FILED_BY).getTime());
//            dirForDefValue.setDirectionsForDefendantBasicValue(dirForDefBV);
//        }
//        catch (SQLException ex)
//        {
//            ex.printStackTrace();
//            fail("SQLException attempting to read Directions For Def result set: " +
//                 ex.getMessage());
//        }
//
//        return dirForDefValue;
//    }
//
//    private void compareDirForDefValues(DirectionsForDefendantValue expected,
//                                        DirectionsForDefendantValue actual)
//    {
//        XhbDirectionsForDefendantBasicValue expectedBV =
//                expected.getDirectionsForDefendantBasicValue();
//        XhbDirectionsForDefendantBasicValue actualBV =
//                actual.getDirectionsForDefendantBasicValue();
//
//        assertEquals("Arrainged : ", expectedBV.getArraigned(), actualBV.getArraigned());
//        assertEquals("Bail Status : ", expectedBV.getBailStatus(), actualBV.getBailStatus());
//        assertEquals("Cert Attendance : ", expectedBV.getCertAttendance(), actualBV.getCertAttendance());
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c1.setTime(expectedBV.getDateTime());
//        c2.setTime(actualBV.getDateTime());
//        compareCalendarDates(c1, c2, "DateTime");
//        assertEquals("Def On Case ID : ", expectedBV.getDefendantOnCaseId(), actualBV.getDefendantOnCaseId());
//        assertEquals("Form B : ", expectedBV.getFiledFormB(), actualBV.getFiledFormB());
//        assertEquals("Is Identified : ", expectedBV.getIsIdentified(), actualBV.getIsIdentified());
//        assertEquals("New Bail Cond : ", expectedBV.getNewBailConditions(), actualBV.getNewBailConditions());
//        Calendar c3 = Calendar.getInstance();
//        c3.setTime(expectedBV.getToBeFiledBy());
//        Calendar c4 = Calendar.getInstance();
//        c4.setTime(actualBV.getToBeFiledBy());
//        compareCalendarDates(c3, c4, "ToBeFiledBy");
//    }
//
//    /**
//     * Down and dirty way of getting an Integer out of a database, in particular
//     * for Oracle databases which have a habit of returning BigDecimal.
//     * @param rs The result set from which to retrieve the column.
//     * @param column The name of the column.
//     * @return An Integer representation of the column which will be null if the
//     * entry is null
//     * @throws SQLException When there is a problem connecting to the database.
//     */
//    private static Integer getIntegerFromResultSetColumn(
//            ResultSet rs, String column) throws SQLException
//    {
//        int value = rs.getInt(column);
//        if(rs.wasNull())
//            return null;
//        return new Integer(value);
//    }
//
//    /**
//     * Down and dirty way of getting a Float out of a database.
//     * @param rs The result set from which to retrieve the column.
//     * @param column The name of the column.
//     * @return A Float representation of the column which will be null if the
//     * entry is null
//     * @throws SQLException When there is a problem connecting to the database.
//     */
//    private static Float getFloatFromResultSetColumn(
//            ResultSet rs, String column) throws SQLException
//    {
//        float value = rs.getFloat(column);
//        if(rs.wasNull())
//            return null;
//        return new Float(value);
//    }
//
//    /**
//     * Down and dirty way of getting a Calendar out of a database.
//     * @param rs The result set from which to retrieve the column.
//     * @param column The name of the column.
//     * @return A Calendar representation of the column which will be null if the
//     * entry is null
//     * @throws SQLException When there is a problem connecting to the database.
//     */
//    private static Calendar getCalendarFromResultSetColumn(
//            ResultSet rs, String column) throws SQLException
//    {
//        Timestamp value = rs.getTimestamp(column);
//        if(rs.wasNull())
//            return null;
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(value);
//        return cal;
//    }
//
//    private DirectionsForCaseValue getDirForCaseValFromResultSet(ResultSet rs)
//    {
//        DirectionsForCaseValue dirForCaseValue = new DirectionsForCaseValue();
//        XhbDirectionsForCaseBasicValue dirForCaseBV = new XhbDirectionsForCaseBasicValue();
//
//        try
//        {
//            dirForCaseBV.setCaseId(getIntegerFromResultSetColumn(rs, CASE_ID));
//            dirForCaseBV.setDateTime(
//                    getCalendarFromResultSetColumn(rs, DATE_TIME).getTime());
//            dirForCaseBV.setDirectionsText(rs.getString(DIRECTIONS_TEXT));
//            dirForCaseBV.setHasPanddForm(rs.getString(HAS_PANDD_FORM));
//            dirForCaseBV.setTrialTimeEstimate(
//                    getFloatFromResultSetColumn(rs,TRIAL_TIME_EST));
//            dirForCaseBV.setTrialTimeUnit(
//                    getIntegerFromResultSetColumn(rs, TRIAL_TIME_UNIT));
////            dirForCaseBV.setId(
////                    getIntegerFromResultSetColumn(rs, DIR_FOR_CASE_ID));
//            dirForCaseValue.setDirectionsForCaseBasicValue(dirForCaseBV);
//        }
//        catch (SQLException ex)
//        {
//            ex.printStackTrace();
//            fail("SQLException attempting to read Directions For Case result set: " +
//                 ex.getMessage());
//        }
//
//        return dirForCaseValue;
//    }
//
//    private DirectionsForCaseValue getNewDirectionsForCase()
//    {
//        DirectionsForCaseValue dirForCaseValueRet = null;
//        try
//        {
//            //Find the DirectionsForCase record
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(directionsForCaseQuery);
//            if(rs.next())
//            {
//                dirForCaseValueRet = getDirForCaseValFromResultSet(rs);
//            }
//        }
//        catch (SQLException ex)
//        {
//            ex.printStackTrace();
//            fail("SQLException attempting to read Directions For Case from the db: " +
//                 ex.getMessage());
//        }
//        return dirForCaseValueRet;
//    }
//}