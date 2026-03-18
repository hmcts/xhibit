//package uk.gov.courtservice.xhibit.crlivestatus;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Collection;
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
//import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_status.XhbCrLiveStatus;
//import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_status.XhbCrLiveStatusBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_cr_live_status.XhbCrLiveStatusBeanHelper2;
//import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * Test class for the CrLiveStatusHelper.
// *
// * @author tz0d5m
// * @version $Revision: 1.9 $
// * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
// */
//public class TestCrLiveStatusHelper extends TransactionTestCase
//{
//    private static final String NO_INFO = "No information to display";
//    private static final String TEST_TEXT = "Some String";
//    private static final Integer TEST_EVENT_TYPE = new Integer(20914);
//    private static final String TEST_EVENT_TEXT = "<?xml version='1.0' encoding='UTF-8'?>"
//        + "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
//        + "xsi:noNamespaceSchemaLocation='20914.xsd'><time>10:32</time>"
//        + "<date>01/07/04</date><free_text/><type>" + TEST_EVENT_TYPE + "</type></event>";
//
//    private XhbScheduledHearing scheduledHearing;
//    private XhbCrLiveStatus crLiveStatus;
//
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter.  The transaction will always be rolled back.
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestCrLiveStatusHelper(String name) throws NamingException
//    {
//        super(name, true);
//    }
//
//    /**
//     * Test method to ensure that when the a scheduled hearing is moved from a
//     * room the scheduled hearing assigned to the cr live status entry is
//     * removed, and that the public display and internet status fields are
//     * cleared.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #moveCaseFromRoom(Integer, Integer)
//     */
//    public void testMoveCaseFromRoom() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        this.crLiveStatus.setXhbScheduledHearing(this.scheduledHearing);
//        this.crLiveStatus.setPublicDisplayStatus(TEST_TEXT);
//        this.crLiveStatus.setInternetStatus(TEST_TEXT);
//        this.crLiveStatus.setInternetHelpCode(TEST_TEXT);
//
//        CrLiveStatusHelper.moveCaseFromRoom(this.scheduledHearing.getScheduledHearingId(),
//                                            this.crLiveStatus.getCourtRoomId());
//
//        assertCrLiveStatusVariablesCleared(this.crLiveStatus);
//
//        assertNull("Scheduled hearing id was not set to null",
//                this.crLiveStatus.getScheduledHearingId());
//    }
//
//    /**
//     * Test method to ensure that when the publicdisplay is activated, the
//     * scheduled hearing is assigned to the cr live status entry, and that the
//     * public display and internet status fields are cleared.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #activatePublicDisplay(XhbScheduledHearing, Date)
//     */
//    public void testActivatePublicDisplay() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        CrLiveStatusHelper.activatePublicDisplay(this.scheduledHearing, new Date());
//
//        assertEquals("Scheduled hearing ids must match",
//                this.crLiveStatus.getScheduledHearingId(),
//                this.scheduledHearing.getScheduledHearingId());
//
//        // all of the other fields should be cleared to their defaults...
//        assertCrLiveStatusVariablesCleared(this.crLiveStatus);
//    }
//
//    /**
//     * Test method to ensure that when the publicdisplay is deactivated, the
//     * scheduled hearing assigned to the cr live status entry is removed, and
//     * that the public display and internet status fields are cleared.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #deactivatePublicDisplay(XhbScheduledHearing, Date)
//     */
//    public void testDeactivatePublicDisplay() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        this.crLiveStatus.setXhbScheduledHearing(this.scheduledHearing);
//        this.crLiveStatus.setPublicDisplayStatus(TEST_TEXT);
//        this.crLiveStatus.setInternetStatus(TEST_TEXT);
//        this.crLiveStatus.setInternetHelpCode(TEST_TEXT);
//
//        CrLiveStatusHelper.deactivatePublicDisplay(this.scheduledHearing, new Date());
//
//        assertCrLiveStatusVariablesCleared(this.crLiveStatus);
//
//        assertNull("Scheduled hearing id was not set to null",
//                this.crLiveStatus.getScheduledHearingId());
//    }
//
//    /**
//     * Test method to ensure that when a new internet court log event is
//     * created the cr live status table is only updated if the event is
//     * newer than the time currently in there for an event.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #updateInternetStatus(CourtLogViewValue)
//     */
//    public void testUpdateInternetStatus() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        this.crLiveStatus.setXhbScheduledHearing(this.scheduledHearing);
//        this.crLiveStatus.setPublicDisplayStatus(TEST_TEXT);
//
//        Date pastDate   = new Date(System.currentTimeMillis() - 10000);
//        Date futureDate = new Date(System.currentTimeMillis() + 10000);
//
//        CourtLogViewValue clvv = new CourtLogViewValue();
//        clvv.setEventType(TEST_EVENT_TYPE);
//        clvv.setLogEntry(TEST_EVENT_TEXT);
//        clvv.setScheduledHearingId(this.scheduledHearing.getScheduledHearingId());
//
//        // validate that past dates do not update the public display status...
//        clvv.setEntryDate(pastDate);
//        CrLiveStatusHelper.updateInternetStatus(clvv);
//        assertEquals("Past date should not update the public display status",
//                this.crLiveStatus.getInternetStatus(), NO_INFO);
//
//        // validate that future dates do update the public display status...
//        clvv.setEntryDate(futureDate);
//        CrLiveStatusHelper.updateInternetStatus(clvv);
//        assertTrue("Future date should update the public display status",
//                !NO_INFO.equals(this.crLiveStatus.getInternetStatus()));
//        assertEquals("Future date should update the time status set",
//                this.crLiveStatus.getTimeStatusSet(), futureDate);
//    }
//
//    /**
//     * Test method to ensure that when a new public display court log event is
//     * created the cr live status table is only updated if the event is
//     * newer than the time currently in there for an event.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #updatePublicDisplayStatus(CourtLogViewValue)
//     */
//    public void testUpdatePublicDisplayStatus() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        this.crLiveStatus.setXhbScheduledHearing(this.scheduledHearing);
//        this.crLiveStatus.setPublicDisplayStatus(TEST_TEXT);
//
//        Date pastDate   = new Date(System.currentTimeMillis() - 10000);
//        Date futureDate = new Date(System.currentTimeMillis() + 10000);
//
//        CourtLogViewValue clvv = new CourtLogViewValue();
//        clvv.setLogEntry(TEST_EVENT_TEXT);
//        clvv.setScheduledHearingId(this.scheduledHearing.getScheduledHearingId());
//
//        // validate that past dates do not update the public display status...
//        clvv.setEntryDate(pastDate);
//        CrLiveStatusHelper.updatePublicDisplayStatus(clvv);
//        assertEquals("Past date should not update the public display status",
//                this.crLiveStatus.getPublicDisplayStatus(), TEST_TEXT);
//
//        // validate that future dates do update the public display status...
//        clvv.setEntryDate(futureDate);
//        CrLiveStatusHelper.updatePublicDisplayStatus(clvv);
//        assertTrue("Future date should update the public display status",
//                !TEST_TEXT.equals(this.crLiveStatus.getPublicDisplayStatus()));
//        assertEquals("Future date should update the time status set",
//                this.crLiveStatus.getTimeStatusSet(), futureDate);
//    }
//
//    /**
//     * Test method to ensure that when a public display event is deleted, the
//     * public display status is only cleared if the date/time of the court log
//     * event to delete is exactly the same as that for the entry in the cr live
//     * status table.
//     *
//     * @throws SQLException
//     * @see uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper
//     *      #deletePublicDisplayStatus(CourtLogViewValue)
//     */
//    public void testDeletePublicDisplayStatus() throws SQLException
//    {
//        // ensure we have a standard set of data to start with...
//        loadData();
//
//        this.crLiveStatus.setXhbScheduledHearing(this.scheduledHearing);
//        this.crLiveStatus.setPublicDisplayStatus(TEST_TEXT);
//
//        Date pastDate   = new Date(System.currentTimeMillis() - 10000);
//        Date futureDate = new Date(System.currentTimeMillis() + 10000);
//
//        CourtLogViewValue clvv = new CourtLogViewValue();
//        clvv.setScheduledHearingId(this.scheduledHearing.getScheduledHearingId());
//
//        // validate that past dates do not update the public display status...
//        clvv.setEntryDate(pastDate);
//        CrLiveStatusHelper.deletePublicDisplayStatus(clvv);
//        assertEquals("Past date should not clear the public display status",
//                this.crLiveStatus.getPublicDisplayStatus(), TEST_TEXT);
//
//        // validate that future dates do not update the public display status...
//        clvv.setEntryDate(futureDate);
//        CrLiveStatusHelper.deletePublicDisplayStatus(clvv);
//        assertEquals("Future date should not clear the public display status",
//                this.crLiveStatus.getPublicDisplayStatus(), TEST_TEXT);
//
//        // validate that the date/time on the crLiveStatus passed through
//        // correctly clears the public display status...
//        clvv.setEntryDate(this.crLiveStatus.getTimeStatusSet());
//        CrLiveStatusHelper.deletePublicDisplayStatus(clvv);
//        assertNull("Correct time should clear the public display status",
//                this.crLiveStatus.getPublicDisplayStatus());
//    }
//
//    /**
//     * Private helper method to set up the data.  This is not performed in the
//     * setUp method, as if an exception is thrown the transaction may not get
//     * rolled back, extracting to here ensures that this will happen (in the
//     * stepDown method).
//     *
//     * @throws SQLException
//     */
//    private void loadData() throws SQLException
//    {
//        Statement stmt = null;
//        ResultSet rset = null;
//
//        try
//        {
//            String query = "SELECT scheduled_hearing_id FROM xhb_scheduled_hearing";
//
//            stmt = connection.createStatement();
//            rset = stmt.executeQuery(query);
//
//            if (!rset.next())
//            {
//                fail("XHB_SCHEDULED_HEARING is empty.");
//            }
//
//            this.scheduledHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(new Integer(rset.getInt(1)));
//        }
//        finally
//        {
//            if (rset != null) { try { rset.close(); } catch (Exception e) { } }
//            if (stmt != null) { try { stmt.close(); } catch (Exception e) { } }
//        }
//
//        XhbCourtRoom xcr = this.scheduledHearing.getXhbSitting().getXhbCourtRoom();
//
//        Collection col = xcr.getXhbCrLiveStatuses();
//
//        if (col.size() == 0)
//        {
//            // create the xhb_cr_live_status entry...
//            XhbCrLiveStatusBasicValue xclsbv = new XhbCrLiveStatusBasicValue();
//            this.crLiveStatus = XhbCrLiveStatusBeanHelper2.createLocal(xclsbv);
//        }
//        else
//        {
//            // make sure that we clear out the value...
//            this.crLiveStatus = (XhbCrLiveStatus) col.iterator().next();
//        }
//
//        this.crLiveStatus.setXhbScheduledHearing(null);
//        this.crLiveStatus.setInternetStatus(NO_INFO);
//        this.crLiveStatus.setInternetHelpCode(null);
//        this.crLiveStatus.setPublicDisplayStatus(null);
//        this.crLiveStatus.setTimeStatusSet(new Date());
//    }
//
//    /**
//     * Private helper method, used to extract the common code to ensure that
//     * the cr live status entity has the public display status, internet status
//     * and internet help code all cleared out to their defaul values.
//     */
//    private static void assertCrLiveStatusVariablesCleared(XhbCrLiveStatus xcls)
//    {
//        assertNull("Public display status was not set to null",
//                xcls.getPublicDisplayStatus());
//        assertEquals("Internet status was not set to correctly",
//                xcls.getInternetStatus(), NO_INFO);
//        assertNull("Internet help code was not set to null",
//                xcls.getInternetHelpCode());
//    }
//}
//