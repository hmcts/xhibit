package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import junit.framework.TestCase;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.AppealWitnessSwornCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CaseCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulatorFactory;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CrnCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.DefendantCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.JoinderCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.LongAdjournCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.PleaCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.TrialWitnessSwornCjseEventLevelPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.UnrelatedDisposalCjseEventLevelPopulator;

/**
 * <p>Title: Test case to check behaviour of CjseEventLevelPopulatorFactory</p>
 * <p>Description: </p>
 * <p>
 * This test case provides methods that check both for normal behaviour, and
 * behaviour in the event of incorrect requests.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Eds</p>
 * @author Bob Boothby
 * @version 1.0
 */
public class TestCjseEventLevelPopulatorFactory extends TestCase
{
    private static final String INVALID_LEVEL = "GIBBERISH";

    /**
     * Conbstruct an instance of the test.
     */
    public TestCjseEventLevelPopulatorFactory(String name)
    {
        super(name);
    }

    /**
     * Tests that the basic behaviour of the factory works and that we get
     * the expected classes.
     * @throws java.lang.Exception When there is an unexpected failure. Order
     * of likelihood of causes is first configuration issues then an error in
     * the factory code, lastly miscoding of the populators.
     */
    public void testGetPopulators() throws java.lang.Exception
    {
        //Check that we get a CaseCjseEventLevelPopulator.
        CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("CASE");
        assertTrue(
                "Populator not an instance of CaseCjseEventLevelPopulator",
                populator instanceof CaseCjseEventLevelPopulator);

        //Check that we get a DefendantCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("DEFENDANT");
        assertTrue(
                "Populator not an instance of DefendantCjseEventLevelPopulator",
                populator instanceof DefendantCjseEventLevelPopulator);

        //Check that we get a CrnCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("CRN");
        assertTrue(
                "Populator not an instance of CrnCjseEventLevelPopulator",
                populator instanceof CrnCjseEventLevelPopulator);

        //Check that we get a JoinderCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("JOINDER");
        assertTrue(
                "Populator not an instance of JoinderCjseEventLevelPopulator",
                populator instanceof JoinderCjseEventLevelPopulator);

        //Check that we get a TrialWitnessSwornCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("TRIAL_WITNESS_SWORN");
        assertTrue(
                "Populator not an instance of TrialWitnessSwornCjseEventLevelPopulator",
                populator instanceof TrialWitnessSwornCjseEventLevelPopulator);

        //Check that we get an AppealWitnessSwornCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("APPEAL_WITNESS_SWORN");
        assertTrue(
                "Populator not an instance of AppealWitnessSwornCjseEventLevelPopulator",
                populator instanceof AppealWitnessSwornCjseEventLevelPopulator);

        //Check that we get a LongAdjournCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("LONG_ADJOURN");
        assertTrue(
                "Populator not an instance of LongAdjournCjseEventLevelPopulator",
                populator instanceof LongAdjournCjseEventLevelPopulator);

        //Check that we get a PleaCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("PLEA");
        assertTrue(
                "Populator not an instance of PleaCjseEventLevelPopulator",
                populator instanceof PleaCjseEventLevelPopulator);

        //Check that we get a UnrelatedDisposalCjseEventLevelPopulator.
        populator = CjseEventLevelPopulatorFactory.
                getInstance().getCjseEventLevelPopulator("UNRELATED_DISPOSAL");
        assertTrue(
                "Populator not an instance of UnrelatedDisposalCjseEventLevelPopulator",
                populator instanceof UnrelatedDisposalCjseEventLevelPopulator);
    }

    /**
     * This test attempts to get a non existent event level populator and checks
     * that the failure is as expected.
     * @throws java.lang.Exception When there is an unexpected mode of failure.
     */
    public void testGetNonExistentPopulator() throws java.lang.Exception
    {
        try
        {
            //Check that we get a CaseCjseEventLevelPopulator.
            CjseEventLevelPopulatorFactory.
                    getInstance().getCjseEventLevelPopulator(INVALID_LEVEL);
            fail("The request for a non-existent event level has not " +
                       "resulted in an exception of any kind.");
        }
        catch(CSUnrecoverableException csue)
        {
            assertEquals(
                    "Error message incorrect: '" + csue.getMessage() + "'",
                    csue.getMessage(),
                    "There is no CjseEventLevelPopulator defined for level: " +
                    INVALID_LEVEL);
        }
    }

    /**
     * Tests the retreival of event levels from descriptions.
     * @throws java.lang.Exception When there is an unexpected failure. Order
     * of likelihood of causes is first configuration issues then an error in
     * the factory code, lastly miscoding of the populators.
     */
    public void testGetLevels() throws java.lang.Exception
    {
        // check case level
        Integer level = CjseEventLevelPopulatorFactory.
                              getInstance().getEventLevelForDescription("CASE");
        assertEquals(
                "Incorrect event level for 'CASE'",
                new Integer(1), level);

        // check defendant level
        level = CjseEventLevelPopulatorFactory.
                         getInstance().getEventLevelForDescription("DEFENDANT");
        assertEquals(
                "Incorrect event level for 'DEFENDANT'",
                new Integer(3), level);

        // check crn level
        level = CjseEventLevelPopulatorFactory.
                               getInstance().getEventLevelForDescription("CRN");
        assertEquals(
                "Incorrect event level for 'CRN'",
                new Integer(2), level);

        // check joinder level
        level = CjseEventLevelPopulatorFactory.
                           getInstance().getEventLevelForDescription("JOINDER");
        assertEquals(
                "Incorrect event level for 'JOINDER'",
                new Integer(-1), level);

        // check trial witness level
        level = CjseEventLevelPopulatorFactory.
               getInstance().getEventLevelForDescription("TRIAL_WITNESS_SWORN");
        assertEquals(
                "Incorrect event level for 'TRIAL_WITNESS_SWORN'",
                new Integer(-1), level);

        // check appeal witness level
        level = CjseEventLevelPopulatorFactory.
               getInstance().getEventLevelForDescription("APPEAL_WITNESS_SWORN");
        assertEquals(
                "Incorrect event level for 'APPEAL_WITNESS_SWORN'",
                new Integer(-1), level);

        // check long adjourn level
        level = CjseEventLevelPopulatorFactory.
               getInstance().getEventLevelForDescription("LONG_ADJOURN");
        assertEquals(
                "Incorrect event level for 'LONG_ADJOURN'",
                new Integer(-1), level);

        // check plea level
        level = CjseEventLevelPopulatorFactory.
               getInstance().getEventLevelForDescription("PLEA");
        assertEquals(
                "Incorrect event level for 'PLEA'",
                new Integer(-1), level);

        // check unrelated disposal level
        level = CjseEventLevelPopulatorFactory.
                getInstance().getEventLevelForDescription("UNRELATED_DISPOSAL");
        assertEquals(
                "Incorrect event level for 'UNRELATED_DISPOSAL'",
                new Integer(-1), level);
    }

    /**
     * This test attempts to get a non existent event level populator and checks
     * that the failure is as expected.
     * @throws java.lang.Exception When there is an unexpected mode of failure.
     */
    public void testGetNonExistentLevel() throws java.lang.Exception
    {
        try
        {
            //Check that we get a CaseCjseEventLevelPopulator.
            Integer level = CjseEventLevelPopulatorFactory.
                    getInstance().getEventLevelForDescription(INVALID_LEVEL);
            fail("The request for a non-existent event level has not " +
                    "resulted in an exception of any kind. Level return = " +
                    level);
        }
        catch(CSUnrecoverableException csue)
        {
            assertEquals(
                    "Error message incorrect: '" + csue.getMessage() + "'",
                    csue.getMessage(),
                    "There is no event level defined for description: " +
                    INVALID_LEVEL);
        }
    }

}