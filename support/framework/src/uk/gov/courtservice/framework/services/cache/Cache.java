package uk.gov.courtservice.framework.services.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * A simple cache mechanism to lazily load a cache and its value when required.
 * All accesses to the private cache implementation are via synchronized blocks,
 * therefore this class is thread-safe.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public abstract class Cache {
    protected final Logger log = CSServices.getLogger(getClass());

    private final HashMap cache;

    private final List validators = new ArrayList(3);

    private boolean useCache = true;

    /**
     * Only available constructor, used to set up this cache to a set state,
     * whereby the cache is created and the factory class used to acquire the
     * value of the cached object has been set.
     * 
     * @param defaultSize
     *            The default size for the <code>Map</code> that represents
     *            the cache.
     */
    public Cache(int defaultSize) {
        log.info("Creating cache - with a default size of " + defaultSize);

        this.cache = new HashMap(defaultSize);
    }

    /**
     * Method to set the parameter to indicate that we should (or not) use the
     * caching provided by this class. If set to <i>false</i>, then the cache
     * will not be used to store or retrieve values.
     * 
     * @param useCache
     */
    protected void setUseCache(boolean useCache) {
        log.info("setUseCache::Setting to " + useCache);
        this.useCache = useCache;
    }

    /**
     * Clear all of the elements from the cache.
     */
    public final void clear() {
        log.info("clear()::Attempting to clear the cache");

        synchronized (cache) {
            cache.clear();
        }
    }

    /**
     * Remove the specified element from the cache. If the passed in key is
     * <i>null</i>, or the cache does not contain the key, then no element will
     * be removed.
     * 
     * @param key
     *            The key of the element to remove from the cache.
     */
    public final void remove(Object key) {
        if (key != null) {
            if (log.isInfoEnabled()) {
                log.info("remove()::Attempting to remove from " + "the cache key: \"" + key + "\"");
            }

            synchronized (cache) {
                cache.remove(key);
            }
        }
    }

    /**
     * Add a validator that should execute its validation on the cached object
     * whenever an attempt is made to access its value. It is important to note
     * that the ordering of the validation for multiple validators cannot be
     * guaranteed.
     * 
     * @param validator
     *            The <code>CachedObjectValidator</code> used to validate the
     *            cached object.
     */
    public final void addCachedObjectValidator(CachedObjectValidator validator) {
        if (validator != null) {
            if (log.isInfoEnabled()) {
                log.info("addCachedObjectValidator()::Adding an " + "instance of " + validator.getClass());
            }

            validators.add(validator);
        }
    }

    /**
     * Method to acquire the value of the cached object specified by the passed
     * in key value. If the passed in key is <i>null</i>, then <i>null</i>
     * will be returned.
     * 
     * @param key
     *            The cache lookup key.
     * @return The object that is stored in the cached object
     */
    public final Object get(Object key) {
        if (log.isDebugEnabled()) {
            log.debug("get() :: Trying to acquire value for \"" + key + "\"");
        }

        if (key == null) {
            // or would we want to throw an exception???
            return null;
        }

        // if we do not want to use the cache, then simply return the value...
        if (!isCacheToBeUsed()) {
            log.info("get() :: Not using the cache");
            return getValue(key);
        }

        // we obviously want to use the cache...
        CachedObject cachedObject = getCachedObjectFromCache(key);

        synchronized (cachedObject) {
            if ((cachedObject.getValue() == null) || !isCachedObjectValid(cachedObject)) {
                final Object value = getValue(key);

                if (log.isDebugEnabled()) {
                    log.debug("get() :: Setting value for \"" + key + "\" to \"" + value + "\"");
                }

                cachedObject.setValue(value);
            }

            return cachedObject.getValue();
        }
    }

    /**
     * Method to determine if the cache should be used, based upon the property
     * set by setUseCache (defaults to <i>true</i>). If <i>true</i> then calls
     * to #get(java.lang.Object) will use the cache, otherwise the call will
     * directly call the #getValue(java.lang.Object).
     * 
     * @return <i>true</i> if the cache should be used, <i>false</i>
     *         otherwise.
     * @see #setUseCache(boolean)
     * @see #getValue(java.lang.Object)
     */
    private boolean isCacheToBeUsed() {
        if (log.isDebugEnabled()) {
            log.debug("isCacheToBeUsed() :: Returning " + useCache);
        }

        return this.useCache;
    }

    /**
     * Acquire the <code>CachedObject</code> from the cache for the specified
     * key value. If the cache does not contain an entry for the key, then an
     * entry will be created. The value of the <code>CachedObject</code> will
     * not be populated at this time.
     * 
     * @param key
     *            The cache lookup key.
     * @return The <code>CachedObject</code>, either from the cache, or newly
     *         created (and placed in the cache).
     */
    private CachedObject getCachedObjectFromCache(Object key) {
        if (log.isDebugEnabled()) {
            log.debug("getCachedObjectFromCache() :: Called " + "with key = \"" + key + "\"");
        }

        synchronized (cache) {
            CachedObject cachedObject = (CachedObject) cache.get(key);

            // if there is no object for the specified key, create an empty
            // one, and place in the cache...
            if (cachedObject == null) {
                if (log.isDebugEnabled()) {
                    log.debug("getCachedObjectFromCache() :: key " + " not in cache, placing in...");
                }

                cachedObject = new CachedObject(key);
                cache.put(key, cachedObject);
            }

            return cachedObject;
        }
    }

    /**
     * Private utility method used to iterate over all of the
     * <code>CachedObjectValidator</code>s and perform the validation steps
     * of each.
     * 
     * @param cachedObject
     *            The <code>CachedObject</code> to be validated.
     * @return <i>true</i> if the cached object is valid, <i>false</i>
     *         otherwise.
     */
    private boolean isCachedObjectValid(CachedObject cachedObject) {
        log.debug("isCachedObjectValid() :: Start");

        for (int i = 0, n = validators.size(); i < n; i++) {
            if (!((CachedObjectValidator) validators.get(i)).isValid(cachedObject)) {
                if (log.isDebugEnabled()) {
                    log.debug("isCachedObjectValid() :: " + "Failed validation using " + validators.get(i).getClass()
                            + "; Returning false");
                }
                return false;
            }
        }

        // either no validators, or all passed validation steps...
        log.debug("isCachedObjectValid() :: Returning true");
        return true;
    }

    protected abstract Object getValue(Object key);
}
