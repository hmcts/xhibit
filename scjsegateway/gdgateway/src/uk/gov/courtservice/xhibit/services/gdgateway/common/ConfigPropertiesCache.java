package uk.gov.courtservice.xhibit.services.gdgateway.common;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.ConfigPropertiesVO;
import uk.gov.courtservice.xhibit.database.gdgateway.CommonGdgateDatabase;

/**
 * <p>
 * Title: Cache of ConfigProperties.
 * </p>
 * <p>
 * Description: Singleton cache of code/values from the database table
 * GDG_CONGIG_PROPERTIES.
 * Users of this class call getCache() to get the cache instance and then call
 * the get on the instance passing in an ConfigPropertyCodes enum.
 * The cache refreshes from the database based on the code
 * ConfigPropertyCodes.CONFIG_PROPERTIES_REFRESH_TIME in the database table
 * itself.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @version $Id: ConfigPropertiesCache.java,v 1.8 2006/10/30 16:06:22 rzvddy Exp $
 */
public class ConfigPropertiesCache {

    protected static final Logger log = CSServices.getLogger(ConfigPropertiesCache.class);

    private static final long MILLISECONDS_IN_A_MINUTE = 1000 * 60;

    private static final long DEFAULT_CACHE_DURATION = MILLISECONDS_IN_A_MINUTE * 30;

    private static final int NUMBER_OF_PROPERTIES = ConfigPropertyCodes.values().length;

    /**
     * Singleton instance of ConfigPropertiesCache
     */
    private static final ConfigPropertiesCache instance = new ConfigPropertiesCache();

    private final CommonGdgateDatabase commonGdgateDatabase;

    /**
     * The cache of config properties.
     */
    private final Map<ConfigPropertyCodes, String> cache;

    /**
     * Lock to ensure that only one thread reads from the database when the
     * cache has expired.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * ReadWrite lock used to ensure that only one thread updates the cache at
     * a time.
     */
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    /**
     * Numerous threads can acquire the read lock to get data from the cache.
     */
    private final Lock readLock = rwl.readLock();

    /**
     * Only one thread at a time can acquire the write lock to refresh the
     * cache.  When a thread has the write lock, no thread can acquire the
     * read lock.
     */
    private final Lock writeLock = rwl.writeLock();

    /**
     * Cache duration in milliseconds before it should reload from the database
     */
    private long cacheDuration = DEFAULT_CACHE_DURATION;

    /**
     * The last time (in milliseconds) that the cache was reloaded.
     */
    private long lastReloadTime = 0;

    private boolean cacheEmpty = true;

    /**
     * Private constructor to prevent instantiation from other classes
     */
    private ConfigPropertiesCache() {
        commonGdgateDatabase = new CommonGdgateDatabase();
        cache = new EnumMap<ConfigPropertyCodes, String>(ConfigPropertyCodes.class);
    }

    /**
     * Updates the cache refresh time from the cache itself if possible,
     * otherwise sets it to the default.
     */
    private void updateCacheRefreshTime() {
        if (log.isDebugEnabled()) log.debug("cacheRefreshTime(): Begin.");
        //final String duration = CSServices.getConfigServices().getProperty(DURATION_PROPERTY);
        final String duration = cache.get(ConfigPropertyCodes.CONFIG_PROPERTIES_REFRESH_TIME);
        if (duration != null) {
            try {
                cacheDuration = Long.parseLong(duration) * MILLISECONDS_IN_A_MINUTE;
            } catch (NumberFormatException e) {
                // if failed, then default to use default...
                log.error(ConfigPropertyCodes.CONFIG_PROPERTIES_REFRESH_TIME + "::Invalid value '" + duration + "'");
                cacheDuration = DEFAULT_CACHE_DURATION;
            }
        } else {
            cacheDuration = DEFAULT_CACHE_DURATION;
            log.error(ConfigPropertyCodes.CONFIG_PROPERTIES_REFRESH_TIME + "::Not set on GDG_CONFIG_PROPERTIES");
        }
        log.info("Config Properties Cache Duration::Changed to " + cacheDuration + "ms");
    }

    /**
     * Our singleton factory method.
     *
     * @return
     */
    public static ConfigPropertiesCache getCache() {
        return instance;
    }

    /**
     * Gets a config property value from the cache using the given key.
     * @param key the key to lookup in the cache.
     * @return the config property value
     */
    public String get(ConfigPropertyCodes key) {
        if (log.isDebugEnabled()) log.debug("get(key = " + key +"): Begin");
        if (cacheExpired() && (cacheEmpty || databaseNotBeingAccessed())) {
            loadCache();
        }

        readLock.lock();
        try {
            String value = cache.get(key);
            if (value == null) {
                log.fatal("get(key): key: " + key + " does not exist!");
                throw new RuntimeException("cache key: " + key + " does not exist.");
            }
            return value;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Loads the cache with config properties.  This gets a write lock (to
     * prevent other threads reading the cache) before clearing the cache and
     * re-populating it.  finally releases the wite lock.
     */
    private void loadCache() {
        if (log.isDebugEnabled()) log.debug("loadCache(): Begin.");
        Collection<ConfigPropertiesVO> properties = retrieveProperties();
        if (properties == null) {
            return;
        } else if (properties.size() < NUMBER_OF_PROPERTIES) {
            log.fatal("loadCache() - Retrieved " + properties.size() + " Should have " + NUMBER_OF_PROPERTIES);
            logCacheLoadError();
            throw new ConfigPropertiesCacheLoadException("loadCache() - Retrieved " + properties.size() + " Should have " + NUMBER_OF_PROPERTIES);
        }

        writeLock.lock();
        if (log.isDebugEnabled()) log.debug("loadCache(): got writeLock.");
        try {
            cache.clear();
            for (ConfigPropertiesVO item : properties) {
                try {
                    ConfigPropertyCodes cpc = Enum.valueOf(ConfigPropertyCodes.class, item.getPropertyCode());
                    cache.put(cpc, item.getPropertyValue());
                } catch (IllegalArgumentException iae) {
                    // All properties on GDG_CONFIG_PROPERTIES should be
                    // referenced using the enum ConfigPropertyCodes.
                    log.warn("Unknown property code: " + item.getPropertyCode(), iae);
                }
            }
            cacheEmpty = false;
            updateCacheRefreshTime();
        } finally {
            writeLock.unlock();
            if (log.isDebugEnabled()) log.debug("loadCache(): released writeLock.");
        }
    }

    /**
     * Retrieves the config proprties from the database.
     * First gets a lock so that only one thread will read from the database
     * then checks if the cache has expired in case a previous thread has just
     * read from the database.  Then reads from the database and resets expiry
     * time before releasing the lock.
     * @return Collection of ConfigPropertiesVO
     */
    private Collection<ConfigPropertiesVO> retrieveProperties() {
        if (log.isDebugEnabled()) log.debug("retrieveProperties(): Begin.");
        Collection<ConfigPropertiesVO>  c = null;
        lock.lock();
        if (log.isDebugEnabled()) log.debug("retrieveProperties(): got lock.");
        try {
            if (!cacheExpired()) {
                log.info("retrieveProperties(): previous thread already got properties from db.");
                return null;
            }
            try {
                if (log.isDebugEnabled()) log.debug("retrieveProperties(): about to get properties from db.");
                c = commonGdgateDatabase.getProperties();
                if (log.isDebugEnabled()) log.debug("retrieveProperties(): got properties from db.");
                lastReloadTime = System.currentTimeMillis();
            } catch (DataAccessException dae) {
                log.fatal("Error retrieving from database table GDG_CONFIG_PROPERTIES", dae);
                throw new ConfigPropertiesCacheLoadException("Error loading from table GDG_CONFIG_PROPERTIES", dae);
            }
        } finally {
            lock.unlock();
        }
        return c;
    }

    /**
     * See if the cache has expired.
     * @return true if lastReloadTime plus the cache duration is less than the
     * current time.
     */
    private boolean cacheExpired() {
        return (lastReloadTime + cacheDuration) < System.currentTimeMillis();
    }

    /**
     * See if the database is not being accessed.
     * @return true if there is no lock, i.e. no database read in progress.
     */
    private boolean databaseNotBeingAccessed() {
        return !lock.isLocked();
    }

    /**
     * Logging for cache load error.  Displays all of the expected config
     * property codes from the enum ConfigPropertyCodes.
     */
    private void logCacheLoadError() {
        log.fatal("loadCache(): Not enough properties retrieved.");
        StringBuilder sb = new StringBuilder("The following PROPERTY_CODES should exist on GDG_CONFIG_PROPERTIES");
        for (ConfigPropertyCodes code : ConfigPropertyCodes.values()) {
            sb.append(code.toString());
            sb.append("\n");
        }
        log.fatal(sb.toString());
    }

}
