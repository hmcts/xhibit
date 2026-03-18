package uk.gov.courtservice.xhibit.business.services.translation;

import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundles;

public class TranslationBundlesCache {
    private static final Locale DEFAULT_LOCALE = getDefaultLocale();

    private static final long CACHE_DURATION = getCacheDuration();

    private static final Logger log = CSServices.getLogger(TranslationBundlesCache.class);

    private static TranslationBundlesCache instance;

    /**
     * Get the single instance of the cache
     * 
     * @return the cache
     */
    public synchronized static TranslationBundlesCache getInstance() {
        if (instance == null) {
            instance = new TranslationBundlesCache();
        }
        return instance;
    }

    private TranslationBundles cache;

    private long cacheTime;

    /**
     * Get the translation bundles from the cache
     * 
     * @return the cache
     */
    public synchronized TranslationBundles getTranslationBundles() {
        long currentTime = System.currentTimeMillis();
        if (cache == null || (cacheTime + CACHE_DURATION) < currentTime) {
            cache = createTranslationBundles();
            cacheTime = currentTime;
        }
        return cache;
    }

    //
    // Parameter Utilities
    //

    private static final Locale getDefaultLocale() {
        return new Locale(getDefaultLanguage(), getDefaultCountry());
    }

    private static final String getDefaultLanguage() {
        return System.getProperty(
                "uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundlesCache.defaultLanguage", "en");
    }

    private static final String getDefaultCountry() {
        return System.getProperty(
                "uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundlesCache.defaultCountry", "GB");
    }

    private static final long getCacheDuration() {
        String cacheDuration = System
                .getProperty("uk.gov.courtservice.xhibit.business.vos.translation.TranslationBundlesCache.cacheDuration");
        if (cacheDuration != null) {
            try {
                return Long.parseLong(cacheDuration);
            } catch (NumberFormatException nfe) {
                log.warn("Cache Duration: \"" + cacheDuration + "\" is invalid.");
                // return default
            }
        }

        return 15 * 60 * 1000; // Default 15 Minutes!
    }

    //
    // Translation Bundles Utilities
    //

    protected TranslationBundles createTranslationBundles() {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            TranslationBundles translationBundles = _createTranslationBundles();
            log.debug("Loading translations " + translationBundles + " took "
                    + (System.currentTimeMillis() - starttime) + " ms.");
            return translationBundles;
        }
        return _createTranslationBundles();
    }

    protected TranslationBundles _createTranslationBundles() {
        return TranslationBundlesFactory.getInstance().createTranslationBundles(DEFAULT_LOCALE);
    }
}
