package uk.gov.courtservice.framework.services.cache;

import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * A simple dumb value object used to represent a cached object in the cache.
 * 
 * This value object should only be used internally by the Cache class,
 * associated helpers and the validator classes.
 * 
 * The value that this cached object represents is maintained via a
 * <code>SoftReference</code>, and as such the <code>getValue()</code>
 * method may return <i>null</i> when the reference has been garbage collected.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public class CachedObject {
    private final Object key;

    // set this to a default reference to prevent NPE's
    private SoftReference valueReference = new SoftReference(null);

    private Date cachedTime;

    /**
     * Only available constructor, to take the key value, which should be the
     * same as that used to store the value in the cache.
     * 
     * @param key
     *            The key lookup value.
     */
    public CachedObject(Object key) {
        this.key = key;
    }

    /**
     * Method to acquire the key lookup value.
     * 
     * @return The key lookup value.
     */
    public Object getKey() {
        return this.key;
    }

    /**
     * Method to acquire the value that this cached object holds. The object may
     * be <i>null</i>, which would indicate that no value has yet been set.
     * 
     * @return The value.
     */
    public Object getValue() {
        return this.valueReference.get();
    }

    /**
     * Mutator method to set the value that this cached object will hold. The
     * variable that holds the cached time will also be updated to the system
     * date at this point.
     * 
     * @param value
     *            The value to set.
     */
    public void setValue(Object value) {
        this.valueReference = new SoftReference(value);
        this.cachedTime = new Date();
    }

    /**
     * Accessor method for the time the value was cached. If set to <i>null</i>,
     * then this will indicate that no value has yet been cached.
     * 
     * @return The time the value cached, or <i>null</i> if nothing has been
     *         cached yet.
     */
    public Date getCachedTime() {
        return this.cachedTime;
    }

    public String toString() {
        final StringBuffer buffer = new StringBuffer(60);

        buffer.append("key = ").append(key);
        buffer.append("; valueReference.get() = ").append(valueReference.get());
        buffer.append("; cachedTime = ").append(cachedTime);

        return buffer.toString();
    }
}
