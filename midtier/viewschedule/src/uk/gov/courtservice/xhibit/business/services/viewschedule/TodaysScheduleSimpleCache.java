package uk.gov.courtservice.xhibit.business.services.viewschedule;

import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.cache.Cache;
import uk.gov.courtservice.framework.services.cache.validators.TimeCachedObjectValidator;
import uk.gov.courtservice.xhibit.business.database.query.schedule.ScheduleQuery;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;

/**
 * <p>
 * Title: Cache for Today's Schedule requests.
 * </p>
 * <p>
 * Description: This class reduces the impact of multiple Today's Schedule
 * requests from the same court. It is a simple thread safe read-through cache.
 * </p>
 * <p>
 * Note the cache only caches requests for today. If a request is made and the
 * cache contains data for yesterday then the entire cache will be invalidated.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 */
public class TodaysScheduleSimpleCache extends Cache {
    private static final TodaysScheduleSimpleCache instance = new TodaysScheduleSimpleCache();

    private static final String USE_CACHE_PROPERTY = "TodaysScheduleSimpleCache.useCache";

    private static final String DURATION_PROPERTY = "TodaysScheduleSimpleCache.duration";

    private static final long DEFAULT_CACHE_DURATION = (1000 * 60) * 15; // 15

    // minutes...

    private static final int DEFAULT_NUMBER_OF_COURTS = 78;

    private Calendar lastUpdateDate = Calendar.getInstance();

    protected TodaysScheduleSimpleCache() {
        super(DEFAULT_NUMBER_OF_COURTS);

        final String useCache = CSServices.getConfigServices().getProperty(USE_CACHE_PROPERTY);
        final String duration = CSServices.getConfigServices().getProperty(DURATION_PROPERTY);

        if (useCache != null) {
            try {
                setUseCache(Boolean.valueOf(useCache).booleanValue());
                log.info(USE_CACHE_PROPERTY + "::Changed to " + useCache);
            } catch (Exception e) {
                // if failed, then default to using cache...
                log.warn(USE_CACHE_PROPERTY + "::Invalid value '" + useCache + "'");
            }
        }

        long cacheDuration = DEFAULT_CACHE_DURATION;
        if (duration != null) {
            try {
                cacheDuration = Long.parseLong(duration);
                log.info(DURATION_PROPERTY + "::Changed to " + cacheDuration + "ms");
            } catch (Exception e) {
                // if failed, then default to use default...
                log.warn(DURATION_PROPERTY + "::Invalid value '" + duration + "'");
            }
        }

        addCachedObjectValidator(new TimeCachedObjectValidator(cacheDuration));
    }

    /**
     * Our singleton factory method.
     * 
     * @return
     */
    public static TodaysScheduleSimpleCache getCache() {
        return instance;
    }

    /**
     * Removes a cached copy of today's schedule for the given courtId if there
     * is a copy.
     * 
     * @param courtId
     */
    public void invalidate(Integer courtId) {
        // although the CacheKey is hashed only on the courtId, removing the
        // CacheKey instance for the courtId makes the code significantly
        // easier to understand...
    	log.debug("invalidate CacheKey courtId:" + courtId);
        remove(new CacheKey(courtId, null));
    }

    /**
     * <p/> Retrieves a TodaysScheduleValue for the supplied courtId and date.
     * Note the cache only caches requests for today.
     * </p>
     * <p>
     * Note the cache only caches requests for today. If a request is made and
     * the cache contains data for yesterday then the entire cache will be
     * invalidated.
     * </p>
     * 
     * @param courtId
     *            the court for which we require a schedule
     * @param date
     *            the date for which we require the schedule.
     * @return a today's schedule.
     */
    public TodaysScheduleValue retrieve(Integer courtId, Date date) {
        log.debug("Entered retrieve()");

        final Calendar now = Calendar.getInstance();
        log.debug("Calendar now: " + now.get(Calendar.DAY_OF_YEAR));
        final Calendar requestedDate = Calendar.getInstance();
        requestedDate.setTime(date);
        log.debug("Calendar requestedDate: " + requestedDate.get(Calendar.DAY_OF_YEAR));

        // if the cache is out of date, then clear the cache...
        if (lastUpdateDate.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
            clear();
            log.debug("retrieve, cache cleared");
        }

        final CacheKey cacheKey = new CacheKey(courtId, date);
        log.debug("retrieve, create new CacheKey courtId: " + courtId);

        if (requestedDate.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
            if (log.isInfoEnabled()) {
                log.info("Schedule requested for " + date + ", that is not today so using live copy.");
            }
            // do not use the cache so get the value directly...
            return (TodaysScheduleValue) getValue(cacheKey);
        }

        this.lastUpdateDate = now;
        return (TodaysScheduleValue) get(cacheKey);
    }

    /**
     * Obtains the actual schedule itself.
     * 
     * @param key
     *            An instance of <code>CacheKey</code> to acquire the schedule
     *            for.
     * @return a live copy of today's schedule.
     */
    protected Object getValue(Object key) {
    	log.debug("Entered getSchedule()");
        // if we are using the cache, then we will always require the
        // latest date...
        final CacheKey cacheKey = (CacheKey) key;
        log.debug("getSchedule() cacheKey, courtId: " + cacheKey.courtId);
        log.debug("getSchedule() cacheKey, date: " + cacheKey.date);

        TodaysScheduleValue schedule = new ScheduleQuery().getSchedule(cacheKey.courtId, cacheKey.date);
        if (schedule != null) {
        	log.debug("getValue() schedule not null.");
        	log.debug("getValue() schedule collection size: " + schedule.getScheduledHearings().size());
        	log.debug("getValue() schedule id: " + schedule.getId());
        } else {
        	log.debug("getValue() schedule null.");
        }
        log.debug("Exited getSchedule()");

        return schedule;
    }

    /**
     * Custom key class to allow multiple parameters to be used in the getValue
     * method.
     * 
     * @author tz0d5m
     */
    protected class CacheKey {
        protected final Integer courtId;

        protected final Date date;

        public CacheKey(Integer courtId, Date date) {
            this.courtId = courtId;
            this.date = date;
        }

        /**
         * This custom cache key object must only be keyed on the courtId,
         * therefore the equals method is only based on the courtId.
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {
            return (obj instanceof CacheKey) && courtId.equals(((CacheKey) obj).courtId);
        }

        /**
         * This custom cache key object must only be keyed on the courtId,
         * therefore the hash is only based on the courtId.
         * 
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return courtId.hashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return super.toString() + " - courtId = " + courtId + "; date = " + date;
        }
    }
}
