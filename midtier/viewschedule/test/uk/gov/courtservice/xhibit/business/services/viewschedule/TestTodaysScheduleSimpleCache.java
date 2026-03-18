//package uk.gov.courtservice.xhibit.business.services.viewschedule;
//
//import java.util.Date;
//
//import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;
//import junit.framework.TestCase;
//
///**
// * @author tz0d5m
// * @version $Revision: 1.2 $
// *
// * @see uk.gov.courtservice.xhibit.business.services.viewschedule
// *      .TodaysScheduleSimpleCache
// */
//public class TestTodaysScheduleSimpleCache extends TestCase
//{
//    /** The amount of time to sleep to imitate the DB query */
//    protected static final long QUERY_TIME = 100;
//    protected static final long ONE_DAY = (((1000 * 60) * 60) * 24);
//
//    private TodaysScheduleTestCache cache;
//    private String message;
//    private Thread[] testThreads;
//
//    /**
//     * Constructor for TestTodaysScheduleSimpleCache.
//     * @param name
//     */
//    public TestTodaysScheduleSimpleCache(String name)
//    {
//        super(name);
//    }
//
//    public void setUp()
//    {
//        this.cache = new TodaysScheduleTestCache();
//        this.message = null;
//        // ensure that the thread group is never null...
//        this.testThreads = new Thread[5];
//    }
//
//    public void testMultipleAccessToCacheForSameCourt()
//    {
//        final Integer courtId = new Integer(1);
//
//        TodaysScheduleValue tsv1 = cache.retrieve(courtId, new Date());
//        assertEquals("Incorrect courtId", courtId, tsv1.getCourtId());
//
//        for (int i = 0; i < 10; i++)
//        {
//            final long startTime = System.currentTimeMillis();
//            TodaysScheduleValue tsv = cache.retrieve(courtId, new Date());
//            final long endTime = System.currentTimeMillis();
//
//            assertEquals("Incorrect courtId", courtId, tsv.getCourtId());
//
//            // This should take no more than 20% of the query wait time...
//            // this figure is so that a significantly small figure can be used
//            // to run the queries often and quickly, although the actual speed
//            // should be significantly quicker than this!
//            if ((endTime - startTime) > (QUERY_TIME / 5)) {
//                fail("Time taken for court id " + courtId + " was "
//                        + (endTime - startTime) + " which is unacceptable");
//            }
//        }
//    }
//
//    public void testMultipleAccessToCacheForSameCourtDifferentDays()
//    {
//        final Integer courtId = new Integer(1);
//
//        TodaysScheduleValue tsv1 = cache.retrieve(courtId, new Date());
//        assertEquals("Incorrect courtId", courtId, tsv1.getCourtId());
//
//        for (int i = 1; i <= 5; i++)
//        {
//            final Date date = new Date(System.currentTimeMillis() + (i * ONE_DAY));
//            
//            final long startTime = System.currentTimeMillis();
//            TodaysScheduleValue tsv = cache.retrieve(courtId, date);
//            final long endTime = System.currentTimeMillis();
//
//            assertEquals("Incorrect courtId", courtId, tsv.getCourtId());
//
//            // This should take at least the query wait time...
//            if ((endTime - startTime) < QUERY_TIME) {
//                fail("Time taken for court id " + courtId + " was "
//                        + (endTime - startTime) + " which is unacceptable");
//            }
//        }
//    }
//
//    public void testConcurrentAccessToCache()
//    {
//        // not totally fair starting thread at same time as instantiating, but
//        // we are just testing the general contention issues here...
//        for (int i = 0; i < testThreads.length; i++)
//        {
//            testThreads[i] = new TodaysScheduleTestCacheThread(new Integer(i));
//            testThreads[i].start();
//        }
//
//        joinThreads();
//
//        // as the validation was performed in a seperate thread, need to see
//        // if any of them had an error...
//        if (message != null)
//        {
//            fail(message);
//        }
//    }
//
//    private void joinThreads()
//    {
//        for (int i = 0; i < testThreads.length; i++)
//        {
//            try
//            {
//                testThreads[i].join();
//            }
//            catch (InterruptedException e)
//            {
//                // ignore...
//            }
//        }
//    }
//
//    private void interruptAllThreads()
//    {
//        for (int i = 0; i < testThreads.length; i++)
//        {
//            if (testThreads[i] != null)
//            {
//                testThreads[i].interrupt();
//            }
//        }
//    }
//    
//    private class TodaysScheduleTestCacheThread extends Thread
//    {
//        private final Integer courtId;
//
//        public TodaysScheduleTestCacheThread(Integer courtId)
//        {
//            this.courtId = courtId;
//        }
//
//        public void run()
//        {
//            final long startTime = System.currentTimeMillis();
//            TodaysScheduleValue tsv = cache.retrieve(this.courtId, new Date());
//            final long endTime = System.currentTimeMillis();
//
//            assertEquals("Incorrect courtId", this.courtId, tsv.getCourtId());
//
//            // ensure duration is at most 110% of the query time...
//            if ((endTime - startTime) > QUERY_TIME + (QUERY_TIME / 10) && (message == null))
//            {
//                message = "Time taken for court id " + this.courtId + " was "
//                        + (endTime - startTime) + " which is unacceptable";
//                interruptAllThreads();
//            }
//        }
//    }
//
//    private class TodaysScheduleTestCache extends TodaysScheduleSimpleCache
//    {
//        /**
//         * Overridden method to remove the database call, and replace with a
//         * sleep for a specified amount of time to imitate the DB call.
//         *
//         * @see uk.gov.courtservice.framework.services.cache.Cache
//         *      #getValue(java.lang.Object)
//         */
//        protected Object getValue(Object key)
//        {
//            final TodaysScheduleSimpleCache.CacheKey cacheKey =
//                    (TodaysScheduleSimpleCache.CacheKey) key;
//
//            TodaysScheduleValue schedule = new TodaysScheduleValue(cacheKey.courtId);
//
//            // sleep for an amount of time similar to the amount of time taken
//            // to run the actual query...
//            try
//            {
//                //System.out.println("!!!!DUMMY getSchedule() SLEEPING...");
//                Thread.sleep(QUERY_TIME);
//            }
//            catch (InterruptedException e)
//            {
//                // ignore...
//            }
//
//            return schedule;
//        }
//    }
//}
//