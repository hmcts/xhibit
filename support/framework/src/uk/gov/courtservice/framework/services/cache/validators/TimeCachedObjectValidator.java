package uk.gov.courtservice.framework.services.cache.validators;

import uk.gov.courtservice.framework.services.cache.CachedObject;
import uk.gov.courtservice.framework.services.cache.CachedObjectValidator;

/**
 * Custom validation of <code>CachedObject</code>s, this will ensure that an
 * object in the cache is only valid for a certain amount of time.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public class TimeCachedObjectValidator implements CachedObjectValidator {
    private long cacheDuration = -1;

    /**
     * Constructor to create a time-limited cache validator.
     * 
     * @param cacheDuration
     *            The duration that the cache is valid for, if set to lower than
     *            0 then the isValid method will always return <i>true</i>. If
     *            set to 0, then the isValid method will always return <i>false</i>.
     */
    public TimeCachedObjectValidator(long cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    /**
     * @see uk.gov.courtservice.framework.services.cache.CachedObjectValidator
     *      #isValid(uk.gov.courtservice.framework.services.cache.CachedObject)
     */
    public boolean isValid(CachedObject cachedObject) {
        if (cacheDuration == 0) {
            return false;
        }

        if (cacheDuration < 0) {
            return true;
        }

        if ((cachedObject != null) && (cachedObject.getCachedTime() != null)) {
            if (cachedObject.getCachedTime().getTime() + cacheDuration < System.currentTimeMillis()) {
                return false;
            }
        }

        return true;
    }
}
