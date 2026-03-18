//package uk.gov.courtservice.xhibit.courtlog.witness;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.business.entities.xhb_witness.XhbWitness;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogCategoryDescription;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessReleasedException;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessSwornException;
//import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * Test class for WitnessHelper
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
// *      .WitnessHelper
// * @version $Revision: 1.20 $
// */
//public class TestWitnessHelper extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestWitnessHelper(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#isAppealEventType(java.lang.Integer)
//     */
//    public void testIsAppealEventType()
//    {
//        assertTrue(WitnessHelper.APPEAL_WITNESS_RELEASED_INT
//                + " should be an appeal case, but false returned",
//                WitnessHelper.isAppealWitnessEvent(WitnessHelper.APPEAL_WITNESS_RELEASED_INT));
//        assertTrue(WitnessHelper.TRIAL_WITNESS_RELEASED_INT
//                + " should not be an appeal case, but true returned",
//                !WitnessHelper.isAppealWitnessEvent(WitnessHelper.TRIAL_WITNESS_RELEASED_INT));
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#isTrialEventType(java.lang.Integer)
//     */
//    public void testIsTrialEventType()
//    {
//        assertTrue(WitnessHelper.TRIAL_WITNESS_RELEASED_INT
//                + " should be a trial case, but false returned",
//                WitnessHelper.isTrialWitnessEvent(WitnessHelper.TRIAL_WITNESS_RELEASED_INT));
//        assertTrue(WitnessHelper.APPEAL_WITNESS_RELEASED_INT
//                + " should not be a trial case, but true returned",
//                !WitnessHelper.isAppealWitnessEvent(WitnessHelper.APPEAL_WITNESS_RELEASED_INT));
//    }
//
//    /**
//     * Test to ensure that the getMostRecentEventForCase method works
//     * correctly, in that the court log event returned is the most recent
//     * entry for the category in the court log table.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#getMostRecentEventForCase(java.lang.Integer,
//     *      java.lang.String, java.util.Date)
//     */
//    public void testGetMostRecentEventForCase()
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//        final Integer caseId = WitnessTestData.getCaseId(scheduledHearingId);
//
//        WitnessTestData.constructTrialCourtLog(scheduledHearingId, connection);
//
//        // no need to test thoroughly as tested in testGetEventsForCategory()
//        final Collection courtLogEntries =
//                WitnessHelper.getEventsForCase(caseId,
//                                               CourtLogCategoryDescription.WITNESS_CATEGORY_DESC,
//                                               new Date());
//
//        assertNotNull(courtLogEntries);
//        assertTrue(courtLogEntries.size() > 0);
//
//        final XhbCourtLogEntry mostRecentCourtLogEntry =
//                WitnessHelper.getMostRecentEventForCase(caseId,
//                        CourtLogCategoryDescription.WITNESS_CATEGORY_DESC, new Date(),
//                        new Integer[] { WitnessHelper.TRIAL_WITNESS_SWORN_INT,
//                        WitnessHelper.TRIAL_WITNESS_RELEASED_INT });
//
//        assertEquals(courtLogEntries.iterator().next(), mostRecentCourtLogEntry);
//    }
//
//    /**
//     * Test to ensure that the getEventsForCase returns a valid
//     * <code>Collection</code> (not <i>null</i>), and that it contains the
//     * expected values (as set up by the test case), and that they are
//     * correctly ordered descending by the dateTime field.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#getEventsForCase(java.lang.Integer,
//     *      java.lang.String, java.util.Date)
//     */
//    public void testGetEventsForCase()
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//        final Integer caseId = WitnessTestData.getCaseId(scheduledHearingId);
//
//        // run the method that we want to test...
//        final Collection courtLogEntries = WitnessHelper.getEventsForCase(
//                caseId, CourtLogCategoryDescription.WITNESS_CATEGORY_DESC, new Date());
//
//        assertNotNull("Returned collection should never be null", courtLogEntries);
//        assertEquals("The number of entries created does not match the number "
//                + "of entries returned", WitnessTestData.NUMBER_OF_WITNESS_CATEGORY_EVENTS,
//                courtLogEntries.size());
//
//        final Iterator it = courtLogEntries.iterator();
//        Date lastDate = null;
//
//        while (it.hasNext())
//        {
//            final XhbCourtLogEntry courtLogEntry = (XhbCourtLogEntry) it.next();
//
//            if (lastDate != null)
//            {
//                assertTrue("Returned collection is not in descending dateTime order",
//                        courtLogEntry.getDateTime().before(lastDate));
//            }
//
//            lastDate = courtLogEntry.getDateTime();
//        }
//    }
//
//    /**
//     * Test to ensure that the checkAppealWitnessEvents method fails if
//     * a witness released event is performed prior to a witness sworn event,
//     * and that it succeeds when a witness sworn event is performed after a
//     * valid witness released.
//     *
//     * @throws WitnessSwornException If thrown from methods under test
//     * @throws WitnessReleasedException If an unexpected
//     *         <code>WitnessReleasedException</code> is thrown
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#checkAppealWitnessEvents(uk.gov.courtservice.xhibit
//     *      .courtlog.vos.CourtLogCRUDValue)
//     */
//    public void testCheckAppealWitnessEvents() throws WitnessSwornException,
//                                                      WitnessReleasedException
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//
//        WitnessTestData.constructAppealCourtLog(getScheduledHearingId());
//        final CourtLogCRUDValue cLCV =
//            WitnessTestData.createWitnessReleasedCourtLogCRUDValue(scheduledHearingId);
//        cLCV.setEventType(WitnessHelper.APPEAL_WITNESS_RELEASED_INT);
//
//        try
//        {
//            WitnessHelper.checkAppealWitnessEvents(cLCV);
//            fail("WitnessReleasedException should have been thrown");
//        }
//        catch (WitnessReleasedException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//
//        // a witness sworn event should go through fine...
//        cLCV.setEventType(WitnessHelper.APPEAL_WITNESS_SWORN_INT);
//        WitnessHelper.checkAppealWitnessEvents(cLCV);
//    }
//
//    /**
//     * Test to ensure that the checkTrialWitnessEvents method fails if
//     * a witness released event is performed prior to a witness sworn event,
//     * and that it succeeds when a witness sworn event is performed after a
//     * valid witness released.
//     *
//     * @throws WitnessSwornException If thrown from methods under test
//     * @throws WitnessReleasedException If an unexpected
//     *         <code>WitnessReleasedException</code> is thrown
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#checkTrialWitnessEvents(uk.gov.courtservice.xhibit
//     *      .courtlog.vos.CourtLogCRUDValue)
//     */
//    public void testCheckTrialWitnessEvents() throws WitnessSwornException,
//                                                     WitnessReleasedException
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//        WitnessTestData.constructTrialCourtLog(scheduledHearingId, connection);
//
//        final CourtLogCRUDValue cLCV =
//                WitnessTestData.createWitnessReleasedCourtLogCRUDValue(scheduledHearingId);
//        //cLCV.setEventType(WitnessHelper.TRIAL_WITNESS_RELEASED_INT);
//
//        try
//        {
//            WitnessHelper.checkTrialWitnessEvents(cLCV);
//            fail("WitnessReleasedException should have been thrown");
//        }
//        catch (WitnessReleasedException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//
//        // a witness sworn event should go through fine...
//        cLCV.setEventType(WitnessHelper.TRIAL_WITNESS_SWORN_INT);
//        WitnessHelper.checkTrialWitnessEvents(cLCV);
//    }
//
//    /**
//     * Test to ensure that the removeReleaseInfo method works correctly,
//     * in that the required fields are correctly cleared out.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#removeReleaseInfo(java.lang.Integer)
//     */
//    public void testRemoveReleaseInfo()
//    {
//        final Integer witnessId = this.getWitnessId();
//        final XhbWitness xhbWitness = EntityHelper.getXhbWitness(witnessId);
//
//        // ensure that the witness has the items to be cleared out set
//        xhbWitness.setReleasedDateTime(new Date());
//        xhbWitness.setCalculatedWitnessTime(new Date());
//
//        WitnessHelper.removeReleaseInfo(witnessId);
//
//        assertNull("releasedDateTime not cleared",
//                xhbWitness.getReleasedDateTime());
//        assertNull("calculatedWitnessTime not cleared",
//                xhbWitness.getCalculatedWitnessTime());
//    }
//
//    /**
//     * Test to ensure that the validateTrialWitnessId method works correctly,
//     * in that if no witness id is specified on the options map (only if the
//     * options map exists), then "-1" is put there.  Otherwise, the witness id
//     * is left as it was.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#validateTrialWitnessId(uk.gov.courtservice.xhibit
//     *      .courtlog.vos.CourtLogCRUDValue)
//     */
//    public void testValidateTrialWitnessId()
//    {
//        final Map map = new HashMap();
//        final String witnessIdKey = "E20904_WSO_ID";
//
//        final CourtLogCRUDValue cLCV = new CourtLogCRUDValue();
//        cLCV.getPropertyMap().put("E20904_Witness_Sworn_Options", map);
//
//        // run the method under test...
//        WitnessHelper.validateTrialWitnessId(cLCV);
//
//        // as we did not put any witness id on, it should be set to "-1"
//        assertEquals(map.get(witnessIdKey), "-1");
//
//        // now test to ensure it is not reset if one is present...
//        map.put(witnessIdKey, "12345");
//
//        // run the method under test again ...
//        WitnessHelper.validateTrialWitnessId(cLCV);
//
//        // as we did not put any witness id on, it should be set to "-1"
//        assertEquals(map.get(witnessIdKey), "12345");
//    }
//
//    public static void main(String[] args) throws Exception
//    {
//        TestWitnessHelper twh = new TestWitnessHelper("moo");
//
//        twh.testValidateTrialWitnessId();
//        twh.testValidateAppealWitnessId();
//
//    }
//
//
//
//    /**
//     * Test to ensure that the validateAppealWitnessId method works correctly,
//     * in that if no witness id is specified on the options map (only if the
//     * options map exists), then "-1" is put there.  Otherwise, the witness id
//     * is left as it was.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#validateAppealWitnessId(uk.gov.courtservice.xhibit
//     *      .courtlog.vos.CourtLogCRUDValue)
//     */
//    public void testValidateAppealWitnessId()
//    {
//        final Map map = new HashMap();
//        final String witnessIdKey = "E20603_Witness_ID";
//
//        final CourtLogCRUDValue cLCV = new CourtLogCRUDValue();
//        cLCV.getPropertyMap().put("E20603_Witness_Sworn_Options", map);
//
//        // run the method under test...
//        WitnessHelper.validateAppealWitnessId(cLCV);
//
//        // as we did not put any witness id on, it should be set to "-1"
//        assertEquals(map.get(witnessIdKey), "-1");
//
//        // now test to ensure it is not reset if one is present...
//        map.put(witnessIdKey, "12345");
//
//        // run the method under test again ...
//        WitnessHelper.validateAppealWitnessId(cLCV);
//
//        // as we did not put any witness id on, it should be set to "-1"
//        assertEquals(map.get(witnessIdKey), "12345");
//    }
//
//    /**
//     * Test to ensure that the releaseWitness method works correctly,
//     * in that the witness has the correct fields set and cleared.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#releaseWitness(java.util.Date,java.lang.Integer,
//     *      java.lang.Integer)
//     */
//    public void testReleaseWitness()
//    {
//        // get any witness, and set it up for test...
//        final XhbWitness witness = EntityHelper.getXhbWitness(getWitnessId());
//
//        witness.setActualArrivalDateTime(new Date());
//
//        witness.setReleasedDateTime(null);
//        witness.setCalculatedWitnessTime(null);
//        witness.setMobilenumber("0123456789");
//        witness.setPagernumber("0123456789");
//        witness.setPagernet("0123456789");
//
//        // perform the release test...
//        WitnessHelper.releaseWitness(new Date(), witness.getCaseId(),
//                witness.getWitnessId());
//
//        // now ensure that all of the required fields are set, and the correct
//        // fields are cleared...
//        assertNotNull(witness.getReleasedDateTime());
//        assertNotNull(witness.getCalculatedWitnessTime());
//        assertNull(witness.getMobilenumber());
//        assertNull(witness.getPagernumber());
//        assertNull(witness.getPagernet());
//    }
//
//    /**
//     * Test to ensure that the getLastWitnessId method works correctly,
//     * in that the last witness id for the court log constructed for the test
//     * data is that as expected.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessHelper#getLastWitnessId(java.lang.Integer, java.util.Date)
//     */
//    public void testGetLastWitnessId()
//    {
//        final Integer scheduledHearingId = getScheduledHearingId();
//        final Integer caseId = WitnessTestData.getCaseId(scheduledHearingId);
//
//        WitnessTestData.constructTrialCourtLog(scheduledHearingId, connection);
//
//        final Integer lastWitnessId = WitnessHelper.getLastWitnessId(
//                WitnessHelper.TRIAL_WITNESS_SWORN_INT, caseId, new Date());
//
//        assertEquals("Last witness id is incorrect",
//                lastWitnessId, WitnessTestData.LAST_WITNESS_ID);
//    }
//}
//