//package uk.gov.courtservice.xhibit.courtlog.endhearing;
//
//import java.util.Collection;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBeanHelper2;
//import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.OperationContext;
//import uk.gov.courtservice.xhibit.courtlog.endhearing.EndHearingSubscriber;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.HearingAlreadyEndedException;
//import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * Test class for EndHearingSubscriber, certain functionality will not be
// * tested in this class, in particular the hearing ended already tests, as this
// * is already validation in the <code>TestEndHearingHelper</code> class, and
// * does not require repeating.
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.endhearing
// *      .EndHearingSubscriber
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category.endhearing
// *      .TestEndHearingHelper
// */
//public class TestEndHearingSubscriber extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestEndHearingSubscriber(String name) throws NamingException {
//        super(name);
//    }
//
//    /**
//     * Test to ensure that the post creation logic for the end hearing events
//     * works correctly. This method ensures that the DefHearingRecord for the
//     * passed up defendant has its start and end dates set if all goes through
//     * correctly
//     *
//     * @throws CourtLogBusinessException if the method under test throws it
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingSubscriber#postCreate(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPostCreateDefHearingNotEnded() throws CourtLogBusinessException
//    {
//        // find any def hearing record that exists...
//        final Integer[] ids = getDefHearingRecordHearingIdDefendantOnCaseId();
//        // and get the entities for its hearing and defHearingRecord...
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(ids[0]);
//        final XhbDefHearingRecord defHearingRecord =
//                EntityHelper.getXhbDefHearingRecord(ids[0], ids[1]);
//
//        // to pass validation, ensure hearing and defHearingRecord not ended...
//        TestEndHearingHelper.ensureHearingNotEnded(hearing);
//        TestEndHearingHelper.ensureDefHearingRecordNotEnded(defHearingRecord);
//
//        final OperationContext oc = createOperationContext(
//                getScheduledHearingId(hearing), ids[1]);
//
//        performCreateLogic(oc);
//
//        assertNotNull("After post create, hearing start date should be set",
//                defHearingRecord.getHearingStartDate());
//        assertNotNull("After post create, hearing end date should be set",
//                defHearingRecord.getHearingEndDate());
//    }
//
//    /**
//     * Test to ensure that the post creation logic for the end hearing events
//     * works correctly. This method ensures that the correct exception is
//     * thrown if the DefHearingRecord for the passed up defendant has its start
//     * and end dates set to anything other than <i>null </i>.
//     *
//     * @throws CourtLogBusinessException if the method under test throws it
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingSubscriber#postCreate(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPostCreateDefHearingEnded() throws CourtLogBusinessException
//    {
//        // find any def hearing record that exists...
//        final Integer[] ids = getDefHearingRecordHearingIdDefendantOnCaseId();
//        // and get the entities for its hearing and defHearingRecord...
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(ids[0]);
//        final XhbDefHearingRecord defHearingRecord =
//                EntityHelper.getXhbDefHearingRecord(ids[0], ids[1]);
//
//        // to pass validation, ensure hearing not ended, and for test, ensure
//        // defHearingRecord HAS ended
//        TestEndHearingHelper.ensureHearingNotEnded(hearing);
//        TestEndHearingHelper.ensureDefHearingRecordEnded(defHearingRecord);
//
//        final OperationContext oc = createOperationContext(
//                getScheduledHearingId(hearing), ids[1]);
//
//        try
//        {
//            performCreateLogic(oc);
//            fail("A HearingAlreadyEndedException should have been thrown");
//        }
//        catch (HearingAlreadyEndedException e)
//        {
//            // hearing already ended exception handled as expected
//        }
//    }
//
//    /**
//     * Test to ensure that the post deletion logic for the end hearing events
//     * works correctly when a defendant is passed through. This version simply
//     * tests that the method calls return without throwing exceptions.
//     *
//     * @throws CourtLogBusinessException if the method under test throws it
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingSubscriber#postCreate(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPostDeleteWithoutDefendant() throws CourtLogBusinessException
//    {
//        // get any hearing from the database...
//        final Integer hearingId = getHearingId();
//
//        // in order to pass the validation, need to ensure hearing not ended
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(hearingId);
//        TestEndHearingHelper.ensureHearingNotEnded(hearing);
//
//        final OperationContext oc = createOperationContext(
//                getScheduledHearingId(hearing), null);
//
//        this.peformDeleteLogic(oc);
//    }
//
//    /**
//     * Test to ensure that the post deletion logic for the end hearing events
//     * works correctly when a defendant is passed through. This version ensures
//     * that the defHearingRecord for the passed up defendant has its start and
//     * dates reset to <i>null </i>.
//     *
//     * @throws CourtLogBusinessException if the method under test throws it
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingSubscriber#postCreate(
//     *      uk.gov.courtservice.xhibit.courtlog.OperationContext)
//     */
//    public void testPostDeleteWithDefendant() throws CourtLogBusinessException
//    {
//        // find any def hearing record that exists...
//        final Integer[] ids = getDefHearingRecordHearingIdDefendantOnCaseId();
//        // and get the entities for its hearing and defHearingRecord...
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(ids[0]);
//        final XhbDefHearingRecord defHearingRecord =
//                EntityHelper.getXhbDefHearingRecord(ids[0], ids[1]);
//
//        // to pass validation, ensure hearing not ended, and for test, ensure
//        // defHearingRecord HAS ended
//        TestEndHearingHelper.ensureHearingNotEnded(hearing);
//        TestEndHearingHelper.ensureDefHearingRecordEnded(defHearingRecord);
//
//        final OperationContext oc = createOperationContext(
//                getScheduledHearingId(hearing), ids[1]);
//
//        peformDeleteLogic(oc);
//
//        // now check that the defendant record has been updated correctly
//        assertNull("XhbDefHearingRecord start date not cleared to null",
//                defHearingRecord.getHearingStartDate());
//        assertNull("XhbDefHearingRecord end date not cleared to null",
//                defHearingRecord.getHearingEndDate());
//    }
//
//    /**
//     * Private utility method used to construct the <code>OperationContext</code>
//     * that is used by the test cases of this class, with a defendant on case
//     * set (if passed in)
//     *
//     * @return The newly constructed <code>OperationContext</code>
//     */
//    private OperationContext createOperationContext(Integer scheduledHearingId,
//                                                    Integer defendantOnCaseId)
//    {
//        // construct the CRUD value to pass to the end hearing delete logic
//        final CourtLogCRUDValue clcv = new CourtLogCRUDValue();
//        clcv.setEventType(new Integer(30600));
//        clcv.setDefendantOnCaseId(defendantOnCaseId);
//        clcv.setProperty(EndHearingHelper.SCHEDULED_HEARING_ID_PROPERTY,
//                    scheduledHearingId);
//
//        return OperationContext.newInstance(clcv);
//    }
//
//    /**
//     * Utility method to acquire any scheduled hearing for the passed in hearing.
//     *
//     * @param hearing The hearing we want a scheduled hearing for
//     * @return The primary key of the looked up scheduled hearing.
//     */
//    private Integer getScheduledHearingId(XhbHearing hearing)
//    {
//        assertNotNull("hearing cannot be null", hearing);
//        final Collection shs = hearing.getXhbScheduledHearings();
//        assertNotNull("scheduled hearings should not be null", shs);
//        assertTrue("Need at least one scheduled hearing", (shs.size() > 0));
//        final XhbScheduledHearing xsh = (XhbScheduledHearing) shs.iterator().next();
//        assertNotNull("Scheduled cannot be null", xsh);
//        return xsh.getScheduledHearingId();
//    }
//
//    /**
//     * Private helper method used to extract the common code for actually
//     * performing the calls to the subscriber for creating, it first constructs
//     * a new instance of the subscriber, performs the preCreate logic, and then
//     * performs the postCreate logic.  Note, that no event will actually be
//     * created, this just validates the business logic.
//     *
//     * @param context The <code>OperationContext</code> passed up to the pre
//     *        and post create subscriber methods.
//     * @throws HearingAlreadyEndedException If the hearing (or def hearing) we
//     *         are trying to create an end hearing event for has previously
//     *         been ended.
//     * @throws CourtLogBusinessException If any other problem occurs.
//     */
//    private void performCreateLogic(OperationContext context)
//            throws HearingAlreadyEndedException,
//                   CourtLogBusinessException
//    {
//        // preCreate method is required to set up some parameters on the context...
//        final EndHearingSubscriber endHearingSubscriber = new EndHearingSubscriber();
//        endHearingSubscriber.preCreate(context);
//        endHearingSubscriber.postCreate(context);
//    }
//
//    /**
//     * Private helper method used to extract the common code for actually
//     * performing the calls to the subscriber for deleting, it first constructs
//     * a new instance of the subscriber, performs the preDelete logic, and then
//     * performs the postDelete logic.  Note, that no event exists to actually
//     * delete, this just validates the business logic.
//     *
//     * @param context The <code>OperationContext</code> passed up to the pre
//     *        and post delte subscriber methods.
//     * @throws HearingAlreadyEndedException If the hearing we are trying to
//     *         delete has previously been ended
//     */
//    private void peformDeleteLogic(OperationContext context)
//            throws HearingAlreadyEndedException
//    {
//        // preDelete method is required to set up some parameters on the context...
//        final EndHearingSubscriber endHearingSubscriber = new EndHearingSubscriber();
//        endHearingSubscriber.preDelete(context);
//        endHearingSubscriber.postDelete(context);
//    }
//}
//