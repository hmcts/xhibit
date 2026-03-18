//package uk.gov.courtservice.xhibit.courtlog.helpers;
//
//import javax.ejb.EJBLocalObject;
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDesc;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDescBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
//import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanNotFoundException;
//import uk.gov.courtservice.xhibit.business.entities.xhb_witness.XhbWitnessBeanNotFoundException;
//import uk.gov.courtservice.xhibit.courtlog.CourtLogTestCase;
//
///**
// * Test class for the EntityHelper.
// *
// * @author tz0d5m
// * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
// */
//public class TestEntityHelper extends CourtLogTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the method under test
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public TestEntityHelper(String name) throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogEventDesc method works correctly
//     * when a valid event desc id is passed up to it, in that a non-null object
//     * is returned that matches the id of the value passed as the primary key.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogEventDesc(java.lang.Long)
//     */
//    public void testGetXhbCourtLogEventDesc()
//    {
//        final Integer primaryKey = getCourtLogEventDescId();
//        final EJBLocalObject bean = EntityHelper.getXhbCourtLogEventDesc(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogEntry method works correctly when
//     * a valid log entry id is passed up to it, in that a non-null object is
//     * returned that matches the id of the value passed as the primary key.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogEntry(java.lang.Long)
//     */
//    public void testGetXhbCourtLogEntry()
//    {
//        final Long primaryKey = getCourtLogEntryId();
//        final EJBLocalObject bean = EntityHelper.getXhbCourtLogEntry(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbWitness method works correctly when a
//     * valid witness id is passed up to it, in that a non-null object is
//     * returned that matches the id of the value passed as the primary key.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbWitness(java.lang.Long)
//     */
//    public void testGetXhbWitness()
//    {
//        final Integer primaryKey = getWitnessId();
//        final EJBLocalObject bean = EntityHelper.getXhbWitness(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbCase method works correctly when a
//     * valid case id is passed up to it, in that a non-null object is
//     * returned that matches the id of the value passed as the primary key.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCase(java.lang.Long)
//     */
//    public void testGetXhbCase()
//    {
//        final Integer primaryKey = getCaseId();
//        final EJBLocalObject bean = EntityHelper.getXhbCase(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbScheduledHearing method works correctly
//     * when a valid scheduled hearing id is passed up to it, in that a
//     * non-null object is returned that matches the id of the value passed as
//     * the primary key.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbScheduledHearing(java.lang.Long)
//     */
//    public void testGetXhbScheduledHearing()
//    {
//        final Integer primaryKey = getScheduledHearingId();
//        final EJBLocalObject bean = EntityHelper.getXhbScheduledHearing(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbDefHearingRecord method works correcly
//     * when a valid hearing id and defendant on case id is passed up to it, in
//     * a non-null object is returned that matches the ids passed up to the
//     * finder method.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbDefHearingRecord(java.lang.Long, java.lang.Long)
//     */
//    public void testGetXhbDefHearingRecord()
//    {
//        // find any def hearing record that exists...
//        final Integer[] ids = getDefHearingRecordHearingIdDefendantOnCaseId();
//
//        // look up the def hearing record...
//        final XhbDefHearingRecord bean = EntityHelper.getXhbDefHearingRecord(ids[0], ids[1]);
//
//        // validate that the def hearing record is what we expect
//        assertNotNull("Returned bean must not be null", bean);
//        assertEquals("Hearing id is not as expected", bean.getHearingId(), ids[0]);
//        assertEquals("Defendant on case id is not as expected",
//                bean.getDefendantOnCaseId(), ids[1]);
//    }
//
//    /**
//     * Test to ensure that the getXhbDefendantOnCase method works correctly
//     * when a valid defendant on case id is passed up to it, in that a non-null
//     * object is returned that matches the id of the value passed as the
//     * primary key.
//     *
//     * @throws A <code>DefendantOnCaseNotFoundException</code> if lookup fails
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbDefendantOnCase(java.lang.Long)
//     */
//    public void testGetXhbDefendantOnCase()
//    {
//        final Integer primaryKey = getDefendantOnCaseId();
//        final EJBLocalObject bean = EntityHelper.getXhbDefendantOnCase(primaryKey);
//        comparePrimaryKeys(primaryKey, bean);
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogCategoryDesc method works
//     * correctly when a valid description is passed up to it, in that a
//     * non-null object is returned that matches the description of the value
//     * passed as the description.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogCategoryDesc(java.lang.String)
//     */
//    public void testGetXhbCourtLogCategoryDescValidDescription()
//    {
//        final String description = getCourtLogCategoryDescDescription();
//        final XhbCourtLogCategoryDesc bean =
//                EntityHelper.getXhbCourtLogCategoryDesc(description);
//        assertNotNull("Returned bean must not be null", bean);
//        assertEquals("Description is not as expected",
//                    bean.getCategoryDescription(), description);
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogEventDesc method works correctly
//     * when an invalid primary key id is passed up to it, in that a
//     * <code>EntityHelper.getXhbCase(caseId)</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogEventDesc(java.lang.Long)
//     */
//    public void testGetXhbCourtLogEventDescInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbCourtLogEventDesc(getInvalidEntityIdInteger());
//            fail("getXhbCourtLogEventDesc() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbCourtLogEventDescBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogEntry method works correctly
//     * when an invalid primary key id is passed up to it, in that a
//     * <code>CourtLogEntryNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogEntry(java.lang.Long)
//     */
//    public void testGetXhbCourtLogEntryInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbCourtLogEntry(getInvalidEntityIdLong());
//            fail("getXhbCourtLogEntry() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbCourtLogEntryBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbWitness method works correctly
//     * when an invalid primary key id is passed up to it, in that a
//     * <code>WitnessNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbWitness(java.lang.Long)
//     */
//    public void testGetXhbWitnessInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbWitness(getInvalidEntityIdInteger());
//            fail("getXhbWitness() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbWitnessBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbCase method works correctly
//     * when an invalid primary key id is passed up to it, in that a
//     * <code>CaseNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCase(java.lang.Long)
//     */
//    public void testGetXhbCaseInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbCase(getInvalidEntityIdInteger());
//            fail("getXhbCase() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbCaseBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbDefHearingRecord method works correctly
//     * when invalid primary key ids are passed up to it, in that a
//     * <code>DefHearingRecordNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbDefHearingRecord(java.lang.Long, java.lang.Long)
//     */
//    public void testGetXhbDefHearingRecordInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbDefHearingRecord(getInvalidEntityIdInteger(),
//                    getInvalidEntityIdInteger());
//            fail("getXhbDefHearingRecord() returned a value for ids "
//                    + getInvalidEntityIdLong() + "; " + getInvalidEntityIdLong());
//        }
//        catch (XhbDefHearingRecordBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbDefendantOnCase method works correctly
//     * when invalid primary key ids are passed up to it, in that a
//     * <code>DefendantOnCaseNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbDefendantOnCase(java.lang.Long)
//     */
//    public void testGetXhbDefendantOnCaseInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbDefendantOnCase(getInvalidEntityIdInteger());
//            fail("getXhbDefendantOnCase() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbDefendantOnCaseBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbScheduledHearing method works correctly
//     * when invalid primary key ids are passed up to it, in that a
//     * <code>ScheduledHearingNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbScheduledHearing(java.lang.Long)
//     */
//    public void testGetXhbScheduledHearingInvalidPrimaryKey()
//    {
//        try
//        {
//            EntityHelper.getXhbScheduledHearing(getInvalidEntityIdInteger());
//            fail("getXhbScheduledHearing() returned a value for id "
//                    + getInvalidEntityIdLong());
//        }
//        catch (XhbScheduledHearingBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Test to ensure that the getXhbCourtLogCategoryDesc method works
//     * correctly when an invalid description is  passed up to it, in that an
//     * <code>XhbCourtLogCategoryDescBeanNotFoundException</code> is thrown.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.util.EntityHelper
//     *      #getXhbCourtLogCategoryDesc(java.lang.String)
//     */
//    public void testGetXhbCourtLogCategoryDescInvalidDescription()
//    {
//        try
//        {
//            // null should not exist on the database (as a not-null constraint)
//            EntityHelper.getXhbCourtLogCategoryDesc(null);
//            fail("getXhbCourtLogCategoryDesc() returned a value for null description");
//        }
//        catch (XhbCourtLogCategoryDescBeanNotFoundException e)
//        {
//            // this is what we expect to happen, so ignore...
//        }
//    }
//
//    /**
//     * Extracted simple tests to ensure that the primary key used to look up an
//     * entity bean matches the primary key of the bean returned from the lookup
//     *
//     * @param primaryKey The primary key passed up to the findByPrimaryKey call
//     * @param bean The entity bean returned from the primary key lookup
//     */
//    private void comparePrimaryKeys(final Number primaryKey, final EJBLocalObject bean)
//    {
//        assertNotNull("Acquired entity bean should not be null", bean);
//        // ensure the pk returned and the value passed match
//        assertEquals("Returned beans pk does not match the passed up pk",
//                    bean.getPrimaryKey(), primaryKey);
//    }
//}
//