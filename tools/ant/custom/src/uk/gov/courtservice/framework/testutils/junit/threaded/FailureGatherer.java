package uk.gov.courtservice.framework.testutils.junit.threaded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import junit.framework.AssertionFailedError;

/**
 * <p>
 * Title: Class designed to provide failure gathering and coordination
 * capabilities when working with the <code>FailureGatheringRunnable</code>
 * class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is not intended to be used in isolation, it should always be used
 * with instances of <code>FailureGatheringRunnable</code>.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class FailureGatherer {

    /**
     * The <code>FailureGatheringRunnable</code>s that are using this class.
     */
    private List _failureGatheringRunnables = Collections.synchronizedList(new ArrayList());

    /**
     * The failures raised while running any portions of the test in
     * <code>FailureGatheringRunnable</code>s.
     */
    private List _failures = Collections.synchronizedList(new ArrayList());

    /**
     * This method allows the <code>FailureGatheringRunnable</code>s to
     * register themselves so that the <code>checkForFailures</code> method
     * can wait for their completion or time out.
     * 
     * @param failureGatheringRunnable
     *            The <code>FailureGatheringRunnable</code> to add.
     */
    public void registerJUnitRunnableTest(FailureGatheringRunnable failureGatheringRunnable) {
        _failureGatheringRunnables.add(failureGatheringRunnable);
    }

    /**
     * Used by <code>FailureGatheringRunnable</code> to record any problems in
     * the portion of the test that they were running.
     * 
     * @param afe
     *            the problem.
     */
    public void recordRunnableFailure(AssertionFailedError afe) {
        _failures.add(afe);
    }

    /**
     * To be called by the main test thread to wait for the
     * <code>FailureGatheringRunnable</code>s using this class to complete
     * (or time out) and throw the first failure that occurred
     * 
     * @throws InterruptedException
     */
    public void checkForFailures() throws InterruptedException, AssertionFailedError {
        // Wait for all the threads to finish;
        ListIterator listIterator = _failureGatheringRunnables.listIterator();
        while (listIterator.hasNext()) {
            ((FailureGatheringRunnable) listIterator.next()).join();
        }

        // Throw any errors if they have occured.
        listIterator = _failures.listIterator();
        if (listIterator.hasNext()) {
            throw (AssertionFailedError) listIterator.next();
        }
    }
}