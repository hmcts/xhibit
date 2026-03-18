package uk.gov.courtservice.framework.testutils.junit.threaded;

import java.io.PrintStream;
import java.io.PrintWriter;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * <p>
 * Title: A runnable class intended to provide support for tightly synchronized
 * unit tests.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * The problem with running multi threaded JUnit test is that if one puts
 * assertions in threads other than the main test thread, then the assertion
 * will go astray, additionally any unexpected <code>RuntimeException</code>s
 * can also disappear. This class is an instance of runnable that will capture
 * any <code>Throwable</code>s and pass them to a common
 * <code>RunnableFailureGatherer</code>.
 * </p>
 * <p>
 * To create a multi-threaded unit test, create concrete implementations of this
 * class and override the <code>runTest()</code> method to perform the
 * appropriate assertions.
 * </p>
 * <p>
 * To avoid having eternal runnables in unit tests, the instances of this class
 * have a finite lifespan. This is implemented by calling
 * <code>stop()</stop> on the thread that is running them. Thus it is important not
 * to use this class with a thread pool.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Bob Boothby
 * @version 1.0
 */

public abstract class FailureGatheringRunnable extends Assert implements Runnable {
    /**
     * The default lifespan of this class from starting running it, currently
     * set to 30 seconds.
     */
    public static final long DEFAULT_TEST_LIFESPAN = 30000l;

    /**
     * The name applied to all assertion failures thrown from the assertions
     * made in the thread run.
     */
    private String _name;

    /**
     * The reference to the common class that holds all assertion failures
     * thrown by the various threads in the test.
     */
    private FailureGatherer _failures;

    /**
     * When the this class started running.
     */
    private long _startTime;

    /**
     * The lifespan assigned to the particular instance.
     */
    private long _testLifespan;

    /**
     * Whether the test has finished.
     */
    private boolean _finished = false;

    /**
     * The thread in which this portion of testing is running.
     */
    private Thread _runningThread;

    /**
     * Basic constructor for the class.
     * 
     * @param name
     *            The name to attach to tthis portion of the test.
     * @param assertionFailures
     *            The object used to gather assertion failures.
     */
    public FailureGatheringRunnable(String name, FailureGatherer assertionFailures) {
        this(name, assertionFailures, DEFAULT_TEST_LIFESPAN);
    }

    /**
     * More complex constructor allowing an override on how long the test should
     * last.
     * 
     * @param name
     *            The name to attach to this portion of the test.
     * @param assertionFailures
     *            The class used to gather assertion failures.
     * @param testLifespan
     *            Time in ms. that this portion of the test should be allowed to
     *            run.
     */
    public FailureGatheringRunnable(String name, FailureGatherer assertionFailures, long testLifespan) {
        this._failures = assertionFailures;
        // Register the test with the assertion failures class to allow it to
        // wait
        // for completion before returning any
        // <code>AssertionFailedError</code>.
        assertionFailures.registerJUnitRunnableTest(this);
        _name = name;
        _testLifespan = testLifespan;
    }

    /**
     * Implementation of runnable <code>run()</code> method that handles the
     * running of the portion of the test case encoded in the class.
     */
    public void run() {
        // Store the start time.
        _startTime = System.currentTimeMillis();
        setFinished(false); // make sure that we have registered to start.
        try {
            runTest(); // run the portion of the test.
        } catch (Throwable problem) // record any problems for handling by
        // the main
        // test thread.
        {
            _failures.recordRunnableFailure(new RunnableProblemWrapper(_name, problem));
        } finally {
            setFinished(true); // we have registered the finish
        }
    }

    /**
     * This method should be implemented to a the portion of the test that need
     * to be run on this separate thread.
     */
    public abstract void runTest();

    /**
     * Set whether the test has finished.
     * 
     * @param finished
     *            true if finished, false if not.
     */
    private synchronized void setFinished(boolean finished) {
        _finished = finished;
        if (_finished)
            _runningThread = null;
        else
            // Store a reference to the running thread so that we can stop
            // it if
            // necessary
            _runningThread = Thread.currentThread();
    }

    /**
     * This method is very similar in concept to <code>Thread.join()</code>
     * however it also provides 'expiry' of the test portion according to the
     * default or passed in life span.
     * <p>
     * When a thread calls this method it waits until the test portion thread
     * finishes or the lifespan times out.
     * 
     * @throws InterruptedException
     *             When the calling thread is interrupted.
     */
    public void join() throws InterruptedException {
        boolean completed = false;
        while (System.currentTimeMillis() - _startTime < DEFAULT_TEST_LIFESPAN) {
            synchronized (this) {
                completed = _finished;
            }
            if (completed)
                return;
            else
                _runningThread.join(1000l);
        }
        synchronized (this) {
            if (_runningThread != null)// Hopefully if we time out, then this
            // value will not be null, but it is worth checking.
            {
                // I know that this behaviour is deprecated, however in this
                // case I would argue that we want our unit tests to recover
                // when there is a problem and not cause the JVM to hang.
                _runningThread.stop();
            }
        }
        fail("Thread - '" + _name + "' has failed to return in " + _testLifespan + "ms.");
    }

    /**
     * <p>
     * Title: Wrapper for any problems encountered in running this portion of
     * the unit test.
     * </p>
     * <p>
     * Description:
     * </p>
     * <p>
     * Wrapper class that allows the passing back of any problems to the
     * originating test thread, while reporting the name of the portion of the
     * test that failed.
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
    private class RunnableProblemWrapper extends AssertionFailedError {
        private static final long serialVersionUID = 1559876658631172717L;

        private Throwable _problem;

        private String _name;

        /**
         * Create an instance of the wrapper class.
         * 
         * @param name
         *            The name of the portion of the test that caused the
         *            problem.
         * @param problem
         *            The Throwable cause of the problem.
         */
        public RunnableProblemWrapper(String name, Throwable problem) {
            super(name + " - " + problem.getMessage());
            _problem = problem;
            _name = name;
        }

        /**
         * Prints the stack trace with the name of the portion of the test that
         * caused the trace to the standard error output stream.
         */
        public void printStackTrace() {
            System.err.print(_name + " - ");
            _problem.printStackTrace();
        }

        /**
         * Prints the stack trace with the name of the portion of the test that
         * caused the trace.
         * 
         * @param ps
         *            The PrintStream to record the stack trace to.
         */
        public void printStackTrace(PrintStream ps) {
            ps.print(_name + " - ");
            _problem.printStackTrace(ps);
        }

        /**
         * Prints the stack trace with the name of the portion of the test that
         * caused the trace.
         * 
         * @param pw
         *            The PrintWriter to record the stack trace to.
         */
        public void printStackTrace(PrintWriter pw) {
            pw.print(_name + " - ");
            _problem.printStackTrace(pw);
        }
    }
}
