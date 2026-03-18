//package uk.gov.courtservice.xhibit.web.publicdisplay;
//
//import junit.framework.Assert;
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.testutils.junit.threaded.FailureGatherer;
//import uk.gov.courtservice.framework.testutils.junit.threaded.FailureGatheringRunnable;
//import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.RWLock;
//
//
///**
// * <p>
// * Title: Unit test for the RWLock class.
// * </p>
// *
// * <p>
// * Description:
// * </p>
// *
// * <p>
// * Multi-threaded unit test intended to check the locking semantic for the
// * asymmetric locking capabilities of this class.
// * </p>
// *
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// *
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestRWLock extends TestCase
//{
//    private static final String INITIAL_MESSAGE = "At start of sequence.";
//    private static final String FIRST_WRITE_MESSAGE = "Written by first write lock.";
//    private static final String SECOND_WRITE_MESSAGE = "Written by second write lock.";
//    private static final long JOIN_TIMEOUT = 300L;
//
//    /**
//     * Instance of RWLock used in the tests.
//     */
//    private RWLock testLock = new RWLock();
//
//    /**
//     * This variable is used to hold the results of write happening during
//     * write locks.
//     */
//    private String threadVariable = null;
//
//    /**
//     * Creates a new TestRWLock object.
//     *
//     * @param s TODO:
//     */
//    public TestRWLock(String s)
//    {
//        super(s);
//    }
//
//    /**
//     * Tests that a write lock can't occur until the read locks have been
//     * released. It gets two read locks before trying to get the write lock
//     * and checks that no write occurs in between read lock releases.
//     *
//     * @throws InterruptedException If the main thread of execution is
//     *         interrupted.
//     */
//    public void testConcurrentReadAndWriteLocks()
//        throws InterruptedException
//    {
//        FailureGatherer failureGatherer = new FailureGatherer();
//
//        //This thread is expected to perform the write.
//        //note this thread is not YET started.
//        Thread writeThread = new Thread(new FailureGatheringRunnable("Write Thread.", failureGatherer)
//            {
//                public void runTest()
//                {
//                    try
//                    {
//                        //get write lock.
//                        assertTrue("Failed to get write lock.", testLock.isWriteLockObtained());
//                        assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//                        //do the write.
//                        threadVariable = FIRST_WRITE_MESSAGE;
//                    }
//                    finally
//                    {
//                        //release the write lock.
//                        assertTrue("Failed to release write Lock.", testLock.isWriteLockReleased());
//                        assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//                    }
//                }
//            }
//        );
//
//        try
//        {
//            //Get read lock.
//            assertTrue("Failed to get read lock.", testLock.isReadLockObtained());
//            Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//
//            //get second read lock.
//            try
//            {
//                assertTrue("Failed to get read lock.", testLock.isReadLockObtained());
//                Assert.assertEquals("Number of locks invalid.", 2, testLock.getGivenLocks());
//
//                //start the write thread and wait for it.
//                writeThread.start();
//                Thread.currentThread().yield();
//                writeThread.join(JOIN_TIMEOUT);
//
//                //Make sure it did not yet succeed.
//                assertEquals("Did not see the expected message.", INITIAL_MESSAGE, threadVariable);
//            }
//            finally
//            {
//                assertTrue("Failed to release read Lock.", testLock.isReadLockReleased());
//                Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//            }
//
//            writeThread.join(JOIN_TIMEOUT);
//
//            //Make sure it did not yet succeed.
//            assertEquals("Did not see the expected message.", INITIAL_MESSAGE, threadVariable);
//        }
//        finally
//        {
//            assertTrue("Failed to release read Lock.", testLock.isReadLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//
//        //Check for failures.
//        failureGatherer.checkForFailures();
//
//        //Check that the write completed.
//        assertEquals("Did not see the expected message.", FIRST_WRITE_MESSAGE, threadVariable);
//    }
//
//    /**
//     * This test checks that multiple read locks can be achieved
//     * simultaneously.
//     */
//    public void testConcurrentReadLock()
//    {
//        try
//        {
//            assertTrue("Failed to get read lock.", testLock.isReadLockObtained());
//            Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//
//            try
//            {
//                assertTrue("Failed to get read lock.", testLock.isReadLockObtained());
//                Assert.assertEquals("Number of locks invalid.", 2, testLock.getGivenLocks());
//            }
//            finally
//            {
//                assertTrue("Failed to release read Lock.", testLock.isReadLockReleased());
//                Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//            }
//        }
//        finally
//        {
//            assertTrue("Failed to release read Lock.", testLock.isReadLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//    }
//
//    /**
//     * @throws InterruptedException If the main thread of execution is
//     *         interrupted.
//     */
//    public void testConcurrentWriteAndReadLocks()
//        throws InterruptedException
//    {
//        FailureGatherer failureGatherer = new FailureGatherer();
//
//        //This thread is expected to perform the read.
//        //note this thread is not YET started.
//        Thread readThread = new Thread(new FailureGatheringRunnable("Read Thread", failureGatherer)
//            {
//                public void runTest()
//                {
//                    try
//                    {
//                        //attempt to get read lock, expecting failure.
//                        assertTrue("Succeeded in getting read lock.", !testLock.isReadLockObtained());
//                    }
//                    finally
//                    {
//                        //attempt to release the read lock.
//                        //This should fail (return false) as the read lock should
//                        //never have been achieved.
//                        assertTrue("Managed to release read Lock.", !testLock.isReadLockReleased());
//                    }
//                }
//            }
//        );
//
//        try
//        {
//            //get write lock.
//            assertTrue("Failed to get write lock.", testLock.isWriteLockObtained());
//            Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//            //start the read thread and wait for it.
//            readThread.start();
//            Thread.currentThread().yield();
//            readThread.join(JOIN_TIMEOUT);
//
//            //We will only get the interrupted exception thrown causing
//            //the read lock failure if the write lock really has worked.
//            readThread.interrupt();
//        }
//        catch (Throwable t)
//        {
//            t.printStackTrace();
//        }
//        finally
//        {
//            assertTrue("Failed to release write Lock.", testLock.isWriteLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//
//        //Check for failures.
//        failureGatherer.checkForFailures();
//    }
//
//    /**
//     * Tests the behaviour such that there can be only one write lock in
//     * operation, but any following write locks wait.
//     *
//     * @throws InterruptedException If the main thread of execution is
//     *         interrupted.
//     */
//    public void testConcurrentWriteLocks()
//        throws InterruptedException
//    {
//        FailureGatherer failureGatherer = new FailureGatherer();
//
//        //This thread is expected to perform the second write.
//        //note this thread is not YET started.
//        Thread secondWriteThread = new Thread(new FailureGatheringRunnable("Second Write Thread", failureGatherer)
//            {
//                public void runTest()
//                {
//                    try
//                    {
//                        //get second lock.
//                        assertTrue("Failed to get second write lock.", testLock.isWriteLockObtained());
//                        Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//                        //do the second write.
//                        threadVariable = SECOND_WRITE_MESSAGE;
//                    }
//                    finally
//                    {
//                        //release the second write lock.
//                        assertTrue("Failed to release second write Lock.", testLock.isWriteLockReleased());
//                        Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//                    }
//                }
//            }
//        );
//
//        try
//        {
//            //Get first lock.
//            assertTrue("Failed to get first write lock.", testLock.isWriteLockObtained());
//            Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//            //Do the first write.
//            threadVariable = FIRST_WRITE_MESSAGE;
//
//            //Start the second write.
//            secondWriteThread.start();
//            Thread.currentThread().yield();
//
//            //give it a chance to attempt to lock.
//            secondWriteThread.join(JOIN_TIMEOUT);
//
//            //Make sure it did not yet succeed.
//            assertEquals("Did not see the expected message.", FIRST_WRITE_MESSAGE, threadVariable);
//        }
//        finally
//        {
//            //Release first write lock.
//            assertTrue("Failed to release write Lock.", testLock.isWriteLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//
//        failureGatherer.checkForFailures();
//
//        //Check that the second write completed.
//        assertEquals("Did not see the expected message.", SECOND_WRITE_MESSAGE, threadVariable);
//    }
//
//    /**
//     * This test checks that a simple single read lock can be achieved.
//     */
//    public void testReadLock()
//    {
//        try
//        {
//            assertTrue("Failed to get read lock.", testLock.isReadLockObtained());
//            Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//        }
//        finally
//        {
//            assertTrue("Failed to release read Lock.", testLock.isReadLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//    }
//
//    /**
//     * This test checks that a simple single write lock can be achieved.
//     */
//    public void testWriteLock()
//    {
//        try
//        {
//            assertTrue("Failed to get write lock.", testLock.isWriteLockObtained());
//            Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//        }
//        finally
//        {
//            assertTrue("Failed to release write Lock.", testLock.isWriteLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//    }
//
//    /**
//     * TODO:
//     *
//     * @throws InterruptedException TODO:
//     */
//    public void testWriteLockPriority()
//        throws InterruptedException
//    {
//        FailureGatherer failureGatherer = new FailureGatherer();
//
//        //This thread is expected to perform the read.
//        //note this thread is not YET started.
//        Thread readThread = new Thread(new FailureGatheringRunnable("Read Thread", failureGatherer)
//            {
//                public void runTest()
//                {
//                    try
//                    {
//                        //attempt to get read lock, expecting failure.
//                        assertTrue("Failed in getting read lock.", testLock.isReadLockObtained());
//                        Assert.assertEquals("Number of locks invalid.", 1, testLock.getGivenLocks());
//
//                        // test the second write has taken priority over the read.
//                        assertEquals("Did not see the expected message.", SECOND_WRITE_MESSAGE, threadVariable);
//                    }
//                    finally
//                    {
//                        //release the read lock.
//                        assertTrue("Failed to release write Lock.", testLock.isReadLockReleased());
//                        Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//                    }
//                }
//            }
//        );
//
//        //This thread is expected to perform the write.
//        //note this thread is not YET started.
//        Thread writeThread = new Thread(new FailureGatheringRunnable("Write Thread", failureGatherer)
//            {
//                public void runTest()
//                {
//                    try
//                    {
//                        //get write lock.
//                        assertTrue("Failed to get second write lock.", testLock.isWriteLockObtained());
//                        Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//                        // test the first write message has completed successfully.
//                        assertEquals("Did not see the expected message.", FIRST_WRITE_MESSAGE, threadVariable);
//
//                        //do the write.
//                        threadVariable = SECOND_WRITE_MESSAGE;
//                    }
//                    finally
//                    {
//                        //release the write lock.
//                        assertTrue("Failed to release second write Lock.", testLock.isWriteLockReleased());
//                        Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//                    }
//                }
//            }
//        );
//
//        try
//        {
//            assertTrue("Failed to get write lock.", testLock.isWriteLockObtained());
//            Assert.assertEquals("Number of locks invalid.", -1, testLock.getGivenLocks());
//
//            readThread.start();
//            Thread.currentThread().yield();
//            readThread.join(JOIN_TIMEOUT);
//
//            writeThread.start();
//            Thread.currentThread().yield();
//            writeThread.join(JOIN_TIMEOUT);
//
//            threadVariable = FIRST_WRITE_MESSAGE;
//        }
//        finally
//        {
//            assertTrue("Failed to release write Lock.", testLock.isWriteLockReleased());
//            Assert.assertEquals("Number of locks invalid.", 0, testLock.getGivenLocks());
//        }
//
//        //Check for failures.
//        failureGatherer.checkForFailures();
//    }
//
//    /**
//     * TODO:
//     */
//    protected void setUp()
//    {
//        threadVariable = INITIAL_MESSAGE;
//    }
//
//    /**
//     * TODO:
//     */
//    protected void tearDown() {}
//}
//