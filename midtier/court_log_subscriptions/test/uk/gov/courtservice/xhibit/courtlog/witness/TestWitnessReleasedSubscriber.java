//package uk.gov.courtservice.xhibit.courtlog.witness;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
//import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
//import uk.gov.courtservice.xhibit.courtlog.helpers.crud.CreateHelperFactory;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * Test class for WitnessReleasedSubscriber
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
// *      .WitnessReleasedSubscriber
// * @version $Revision: 1.8 $
// */
//public class TestWitnessReleasedSubscriber extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestWitnessReleasedSubscriber(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Simple test to ensure that the pre-creation logic runs without problems.
//     * The method is not tested thoroughly as each component called from the
//     * method is tested in more detail.
//     *
//     * @throws CourtLogBusinessException
//     * @see uk.gov.courtservice.xhibit.courtlog.witness
//     *      .WitnessReleasedSubscriber#preCreate(uk.gov.courtservice.xhibit
//     *      .courtlog.OperationContext)
//     */
//    public void testPreCreate() throws CourtLogBusinessException
//    {
//        final CourtLogCRUDValue crud =
//                WitnessTestData.createWitnessReleasedCourtLogCRUDValue(getScheduledHearingId());
//        final OperationContext oc = OperationContext.newInstance(crud);
//        new WitnessReleasedSubscriber().preCreate(oc);
//    }
//
//    /**
//     * Simple test to ensure that the post-creation logic runs without problems.
//     * The method is not tested thoroughly as each component called from the
//     * method is tested in more detail.
//     *
//     * @throws CourtLogBusinessException
//     * @see uk.gov.courtservice.xhibit.courtlog.witness
//     *      .WitnessReleasedSubscriber#postCreate(uk.gov.courtservice.xhibit
//     *      .courtlog.OperationContext)
//     */
//    public void testPostCreate() throws CourtLogBusinessException
//    {
//        final CourtLogCRUDValue crud =
//            WitnessTestData.createWitnessReleasedCourtLogCRUDValue(getScheduledHearingId());
//        final OperationContext oc = OperationContext.newInstance(crud);
//
//        // need to ensure that the entry has been created...
//        oc.setNewViewValues(CreateHelperFactory.getCreateHelper(oc).newEntry());
//        new WitnessReleasedSubscriber().postCreate(oc);
//    }
//
//    /**
//     * Simple test to ensure that the post-deletion logic runs without
//     * problems. The method is not tested thoroughly as each component
//     * called from the method is tested in more detail.
//     *
//     * @throws CourtLogBusinessException
//     * @see uk.gov.courtservice.xhibit.courtlog.witness
//     *      .WitnessReleasedSubscriber#postDelete(uk.gov.courtservice.xhibit
//     *      .courtlog.OperationContext)
//     */
//    public void testPostDelete() throws CourtLogBusinessException
//    {
//        final CourtLogCRUDValue crud =
//            WitnessTestData.createWitnessReleasedCourtLogCRUDValue(getScheduledHearingId());
//        crud.setLogEntryId(getCourtLogEntryId());
//        final OperationContext oc = OperationContext.newInstance(crud);
//        new WitnessReleasedSubscriber().postDelete(oc);
//    }
//
//    /**
//     * Test to ensure that the preUpdate method correctly sets up the
//     * <code>CourtLogCRUDValue</code> passed in with all of the properties
//     * set from the entry to be updated.
//     *
//     * @throws CourtLogBusinessException
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.witness
//     *      .WitnessReleasedSubscriber#preUpdate(uk.gov.courtservice.xhibit
//     *      .courtlog.OperationContext)
//     */
//    public void testPreUpdate() throws CourtLogBusinessException
//    {
//        // first look up any current entry for modification...
//        final Long logEntryId = getCourtLogEntryId();
//        final XhbCourtLogEntry xhbCourtLogEntry =
//                EntityHelper.getXhbCourtLogEntry(logEntryId);
//
//        // and set it up to a test log entry xml...
//        xhbCourtLogEntry.setLogEntryXml("<event><oldName>oldValue</oldName>"
//                + "<tmpName>oldValue</tmpName></event>");
//
//        // create a new crud value for update, and set it up for test...
//        final CourtLogCRUDValue crud = new CourtLogCRUDValue();
//        crud.setProperty("newName", "newValue");
//        crud.setProperty("tmpName", "newValue");
//        crud.setLogEntryId(logEntryId);
//
//        // run the method under test...
//        final OperationContext oc = OperationContext.newInstance(crud);
//        new WitnessReleasedSubscriber().preUpdate(oc);
//
//        // now validate that the properties have been set correctly...
//        assertEquals("newName not set copied correctly",
//                crud.getProperty("newName"), "newValue");
//        assertEquals("oldName not set copied correctly",
//                crud.getProperty("oldName"), "oldValue");
//        assertEquals("tmpName not set copied correctly",
//                crud.getProperty("tmpName"), "newValue");
//    }
//}
//