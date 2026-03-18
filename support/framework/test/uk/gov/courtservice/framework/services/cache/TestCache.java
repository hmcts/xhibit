package uk.gov.courtservice.framework.services.cache;

import junit.framework.TestCase;

/**
 * Test class to ensure that the Cache implementation is performance and
 * scalable.
 * 
 * @author tz0d5m
 * @version $Revision: 1.4 $
 */
public class TestCache extends TestCase {
    /** The amount of sleep to replace the DB query */
    private static final long QUERY_TIME = 100;

    private String message;

    private Thread[] testThreads;

    private Cache cache;

    /**
     * Constructor for TestTodaysScheduleSimpleCache.
     * 
     * @param name
     */
    public TestCache(String name) {
        super(name);
    }

    public void setUp() {
        this.cache = new Cache(10) {
            public Object getValue(Object key) {
                // sleep for an amount of time similar to the amount of time
                // taken
                // to run the actual query...
                try {
                    // System.out.println("!!!!DUMMY getValue()
                    // SLEEPING...");
                    Thread.sleep(QUERY_TIME);
                } catch (InterruptedException e) {
                    // ignore...
                }

                return new TestCacheVO(key);
            }
        };

        this.message = null;
        // ensure that the thread group is never null...
        this.testThreads = new Thread[5];
    }

    public void testMultipleAccessToCacheForSameKey() {
        final Object key = new Integer(1);

        TestCacheVO tsv1 = (TestCacheVO) cache.get(key);
        assertEquals("Incorrect key", key, tsv1.key);

        for (int i = 0; i < 10; i++) {
            final long startTime = System.currentTimeMillis();
            TestCacheVO tsv = (TestCacheVO) cache.get(key);
            final long endTime = System.currentTimeMillis();

            assertEquals("Incorrect key", key, tsv.key);

            // This should take no more than 20% of the query wait time...
            // this figure is so that a significantly small figure can be
            // used
            // to run the queries often and quickly, although the actual
            // speed
            // should be significantly quicker than this!
            if ((endTime - startTime) > (QUERY_TIME / 5)) {
                fail("Time taken for key " + key + " was " + (endTime - startTime) + " which is unacceptable");
            }
        }
    }

/*    public void testConcurrentAccessToCache() {
        // not totally fair starting thread at same time as instantiating, but
        // we are just testing the general contention issues here...
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = new TestCacheThread(new Integer(i));
            testThreads[i].start();
        }

        joinThreads();

        // as the validation was performed in a seperate thread, need to see
        // if any of them had an error...
        if (message != null) {
            fail(message);
        }
    }
*/
    private void joinThreads() {
        for (int i = 0; i < testThreads.length; i++) {
            try {
                testThreads[i].join();
            } catch (InterruptedException e) {
                // ignore...
            }
        }
    }

    private void interruptAllThreads() {
        for (int i = 0; i < testThreads.length; i++) {
            if (testThreads[i] != null) {
                testThreads[i].interrupt();
            }
        }
    }

    private class TestCacheThread extends Thread {
        private final Object key;

        public TestCacheThread(Object key) {
            this.key = key;
        }

        public void run() {
            final long startTime = System.currentTimeMillis();
            TestCacheVO tsv = (TestCacheVO) cache.get(this.key);
            final long endTime = System.currentTimeMillis();

            assertEquals("Incorrect key", this.key, tsv.key);

            // ensure duration is at most 110% of the query time...
            if ((endTime - startTime) > QUERY_TIME + (QUERY_TIME / 10) && (message == null)) {
                message = "Time taken for key " + this.key + " was " + (endTime - startTime) + " which is unacceptable";
                interruptAllThreads();
            }
        }
    }

    private class TestCacheVO {
        public final Object key;

        public TestCacheVO(Object key) {
            this.key = key;
        }
    }
}
