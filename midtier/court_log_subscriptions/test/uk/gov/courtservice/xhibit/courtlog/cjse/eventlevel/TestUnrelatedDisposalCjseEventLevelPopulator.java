//package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;
//
//import javax.ejb.ObjectNotFoundException;
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
///**
// * Test class to validate that <code>UnrelatedDisposalCjseEventLevelPopulator</code>
// * works as expected, in that it delegates to the correct populator.
// * <p>
// * This class will not verify any results returned, only that the correct
// * populator was used, as it is the responsibility of those populator's test
// * classes to do those tests
// * </p>
// * @see uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.
// *      UnrelatedDisposalCjseEventLevelPopulator
// */
//public class TestUnrelatedDisposalCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//    /**
//     * Only avaliable constructor that takes the test class name as a parameter
//     * @param name The name of the test class
//     * @throws NamingException as required by parent classes
//     * @see uk.gov.courtservice.framework.testutils.junit.TransactionTestCase#
//     *      TransactionTestCase(java.lang.String, boolean, java.lang.String,
//     *      java.lang.String)
//     */
//    public TestUnrelatedDisposalCjseEventLevelPopulator(String name)
//                                                        throws NamingException
//    {
//        super(name);
//    }
//
//    /**
//     * JUnit test to ensure that the correct populator is used when a
//     * crn-level unrelated disposal is expected
//     */
//    public void testPopulateCrnLevel()
//    {
//        // perform tests with valid, non-null values on all parameters
//        populateWithSpecifiedDetails(caseId, defendantOnCaseId,
//                defendantOnOffenceId, schedHearingId);
//    }
//
//    /**
//     * JUnit test to ensure that the correct populator is used when a
//     * defendant-level unrelated disposal is expected
//     */
//    public void testPopulateDefendantLevel()
//    {
//        // perform tests with null on the defendantOnOffenceId parameter
//        populateWithSpecifiedDetails(caseId, defendantOnCaseId, null,
//                schedHearingId);
//    }
//
//    /**
//     * JUnit test to ensure that the <code>CjseEventLevelPopulatorFactory</code>
//     * contains the correct populator for the description "UNRELATED_DISPOSAL",
//     * which would be an instance of the class
//     * <code>UnrelatedDisposalCjseEventLevelPopulator</code>
//     */
//    public void testUnrelatedDisposalFactoryInclusion()
//    {
//        final CjseEventLevelPopulator populator = getUnrelatedDisposalPopulator();
//        final boolean correctInstance =
//                (populator instanceof UnrelatedDisposalCjseEventLevelPopulator);
//
//        assertTrue("UNRELATED_DISPOSAL in the factory must be an instance of "
//                + "UnrelatedDisposalCjseEventLevelPopulator", correctInstance);
//    }
//
//    /**
//     * Private helper method used to acquire the populator from the
//     * <code>CjseEventLevelPopulatorFactory</code>
//     * @return The populator from the factory
//     * @see uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.
//     *      CjseEventLevelPopulatorFactory#getCjseEventLevelPopulator(
//     *      java.lang.String)
//     */
//    private CjseEventLevelPopulator getUnrelatedDisposalPopulator()
//    {
//        return CjseEventLevelPopulatorFactory.getInstance().
//                getCjseEventLevelPopulator("UNRELATED_DISPOSAL");
//    }
//
//    /**
//     * Seperated out method to acquire the <code>XhbCase</code> Object from
//     * an EJB lookup.
//     * @param caseIdLong - A <code>Long</code> value of the case to look up,
//     * "Long" appended to end of parameter name to avoid confusion with name
//     * set in parent class
//     * @return The <code>XhbCase</code> found by the ejb lookup
//     */
//    private XhbCase getCase(Integer caseIdLong)
//    {
//        try
//        {
//            return XhbCaseBeanHelper.findByPrimaryKey(caseIdLong);
//        }
//        catch (ObjectNotFoundException e)
//        {
//            fail("Exception: Couldn't find case " + caseIdLong);
//            return null;
//        }
//    }
//
//    /**
//     * Perform the actual tests to ensure that the correct populator will be
//     * called based upon the passed in parameters
//     * "Long" appended to end of parameter names to avoid confusion with name
//     * set in parent class
//     * @param caseIdLong
//     * @param defendantOnCaseIdLong
//     * @param defendantOnOffenceIdLong
//     * @param schedHearingIdLong
//     */
//    private void populateWithSpecifiedDetails(Integer caseIdLong,
//												Integer defendantOnCaseIdLong,
//												Integer defendantOnOffenceIdLong,
//												Integer schedHearingIdLong)
//    {
//        XhbCase theCase = getCase(caseIdLong);
//        CourtLogViewValue viewValue = new CourtLogViewValue();
//        viewValue.setDefendantOnCaseId(defendantOnCaseIdLong);
//        viewValue.setDefendantOnOffenceId(defendantOnOffenceIdLong);
//
//        CourtLogSubscriptionValue eventInfo = new CourtLogSubscriptionValue(viewValue);
//
//        // set up the passed in values onto the subscription value object
//        eventInfo.setHearingId(schedHearingIdLong);
//
//        CjseEventLevelPopulator populator = getUnrelatedDisposalPopulator();
//        EventParameters eventParameters = new EventParameters();
//
//        // the string description (used as the key) of the expected event level
//        String expectedLevelDescription =
//                (defendantOnOffenceIdLong != null)
//                        ? CjseEventLevelPopulatorFactory.CRN_EVENT_LEVEL
//                        : CjseEventLevelPopulatorFactory.DEFENDANT_EVENT_LEVEL;
//
//        // the level of the expected populator used
//        Integer expectedLevel = CjseEventLevelPopulatorFactory.getInstance().
//                getEventLevelForDescription(expectedLevelDescription);
//
//        // work out the error message that should be dispayed if the wrong populator
//        // is called
//        String errorMessage = "For defendant on offence to be ["
//                + defendantOnOffenceIdLong + "], the level called should be that"
//                + " for [" + expectedLevelDescription + "] level";
//
//
//        //Attempt Population.
//        EventLevelAndIdentifier levelAndIdentifier =
//                populator.populate(eventParameters, theCase, eventInfo);
//
//        assertEquals(errorMessage, expectedLevel, levelAndIdentifier.getEventLevel());
//    }
//}
//