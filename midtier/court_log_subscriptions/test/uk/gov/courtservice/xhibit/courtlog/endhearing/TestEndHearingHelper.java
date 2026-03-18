//package uk.gov.courtservice.xhibit.courtlog.endhearing;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBeanHelper2;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//import uk.gov.courtservice.xhibit.courtlog.exceptions.HearingAlreadyEndedException;
//import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
//
///**
// * Test class for the EndHearingHelper.
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
// *      .endhearing.EndHearingHelper
// */
//public class TestEndHearingHelper extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the parent class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestEndHearingHelper(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Helper method used to ensure the specified def hearing record has not
//     * ended, by setting the hearing start and end dates to <i>null</i>
//     *
//     * @param defHearingRecord The defHearingRecord we want ensure not ended
//     */
//    protected static void ensureDefHearingRecordNotEnded(XhbDefHearingRecord defHearingRecord)
//    {
//        assertNotNull("DefHearingRecordpassed cannot be null", defHearingRecord);
//        defHearingRecord.setHearingStartDate(null);
//        defHearingRecord.setHearingEndDate(null);
//    }
//
//    /**
//     * Helper method used to ensure the specified def hearing record has
//     * ended, by setting the hearing start and end dates to the current date.
//     *
//     * @param defHearingRecord The defHearingRecord we want ensure ended
//     */
//    protected static void ensureDefHearingRecordEnded(XhbDefHearingRecord defHearingRecord)
//    {
//        assertNotNull("DefHearingRecordpassed cannot be null", defHearingRecord);
//        defHearingRecord.setHearingStartDate(new Timestamp(System.currentTimeMillis()));
//        defHearingRecord.setHearingEndDate(new Timestamp(System.currentTimeMillis()));
//    }
//
//    /**
//     * Helper method used to ensure the specified hearing has not ended,
//     * by setting the hearing start and end dates to <i>null</i>
//     *
//     * @param hearing The hearing we want ensure not ended
//     */
//    protected static void ensureHearingNotEnded(XhbHearing hearing)
//    {
//        assertNotNull("Hearing passed to ensureHearingNotEnded cannot be null", hearing);
//        hearing.setHearingStartDate(null);
//        hearing.setHearingEndDate(null);
//    }
//
//    /**
//     * Helper method used to ensure the specified hearing has ended,
//     * by setting the hearing start and end dates to the current date
//     *
//     * @param hearing The hearing we want ensure ended
//     */
//    private static void ensureHearingEnded(XhbHearing hearing)
//    {
//        assertNotNull("Hearing passed to ensureHearingEnded cannot be null",  hearing);
//        hearing.setHearingStartDate(new Date());
//        hearing.setHearingEndDate(new Date());
//    }
//
//    /**
//     * Test to ensure that the validate logic does not allow processing to
//     * continue (i.e. a <code>HearingAlreadyEndedException</code> is thrown) if
//     * the hearing has ended
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingHelper#validateHearingNotEnded(uk.gov
//     *      .courtservice.xhibit.business.entities.xhb_hearing.XhbHearing)
//     */
//    public void testValidateHearingEnded()
//    {
//        // this method is tested seperately, but use to avoid duplicate code
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(getHearingId());
//        ensureHearingEnded(hearing);
//
//        try
//        {
//            EndHearingHelper.validateHearingNotEnded(hearing);
//            fail("Hearing already ended, but perform indicates not...");
//        }
//        catch (HearingAlreadyEndedException e)
//        {
//            // Hearing already ended handled correctly...
//        }
//    }
//
//    /**
//     * Test to ensure that the validate logic allows processing to continue
//     * (i.e. no exceptions thrown) if the hearing has not ended
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingHelper#validateHearingNotEnded(uk.gov
//     *      .courtservice.xhibit.business.entities.xhb_hearing.XhbHearing)
//     */
//    public void testValidateHearingNotEnded()
//    {
//        // this method is tested seperately, but use to avoid duplicate code
//        final XhbHearing hearing = XhbHearingBeanHelper2.findByPrimaryKey(getHearingId());
//        ensureHearingNotEnded(hearing);
//
//        try
//        {
//            EndHearingHelper.validateHearingNotEnded(hearing);
//            // Would allow update as hearing not ended...
//        }
//        catch (HearingAlreadyEndedException e)
//        {
//            fail("Hearing not ended, but perform indicates it has" + e.getMessage());
//        }
//    }
//
//    /**
//     * Test to ensure that the validate logic does not allow processing to
//     * continue (i.e. a <code>HearingAlreadyEndedException</code> is thrown) if
//     * the def hearing has ended.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingHelper#validateDefHearingNotEnded(java.lang
//     *      .Integer, java.lang.Integer)
//     */
//    public void testValidateDefHearingEnded()
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
//        TestEndHearingHelper.ensureDefHearingRecordEnded(defHearingRecord);
//
//        try
//        {
//            EndHearingHelper.validateDefHearingNotEnded(defHearingRecord
//                    .getDefendantOnCaseId(), hearing.getHearingId());
//            fail("Def hearing already ended, but perform indicates not...");
//        }
//        catch (HearingAlreadyEndedException e)
//        {
//            // Def hearing already ended handled correctly...
//        }
//    }
//    /**
//     * Test to ensure that the validate logic allows processing to continue
//     * (i.e. no exceptions thrown) if the def hearing has not ended
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingHelper#validateDefHearingNotEnded(java.lang
//     *      .Integer, java.lang.Integer)
//     */
//    public void testValidateDefHearingNotEnded()
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
//        try
//        {
//            EndHearingHelper.validateDefHearingNotEnded(defHearingRecord
//                    .getDefendantOnCaseId(), hearing.getHearingId());
//        }
//        catch (HearingAlreadyEndedException e)
//        {
//            fail("Def hearing not ended, but perform indicates it has");
//        }
//    }
//
//    /**
//     * Simple test method to ensure the getHearingScheduleController method
//     * returns without a <i>null</i>.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
//     *      .endhearing.EndHearingHelper#getHearingScheduleController()
//     */
//    public void testGetHearingScheduleController()
//    {
//        final HearingScheduleControllerLocal hearingScheduleController =
//                EndHearingHelper.getHearingScheduleController();
//
//        assertNotNull("Acquired hearing schedule controller should not be null",
//                hearingScheduleController);
//    }
//}
//