package uk.gov.courtservice.framework.services.cache;

/**
 * Interface to define a validator of <code>CachedObject</code>s.
 * Implementations must be thread-safe.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public interface CachedObjectValidator {
    /**
     * Method to determine if the passed in <code>CachedObject</code> is
     * valid.
     * 
     * @param cachedObject
     *            The <code>CachedObject</code> to validate.
     * @return <i>true</i> if valid, <i>false</i> otherwise.
     */
    public boolean isValid(CachedObject cachedObject);
}
