package uk.gov.courtservice.framework.services.locator;

import java.security.PrivilegedActionException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.security.SubjectManager;

/**
 * @author Meeraj
 * @title LazyLoadingServiceLocatorImpl
 */
public class LazyLoadingServiceLocatorImpl extends ServiceLocatorImpl {

    private Map jndiCache = Collections.synchronizedMap(new HashMap());

    /**
     * Utility method to lookup objects
     * 
     * @param serverName
     * @return
     */
    protected Object lookup(String jndiName) {
        if (!jndiCache.containsKey(jndiName)) {
            LookupHelper lookupHelper = new LookupHelper(getEnv(), jndiName);
            try {
                Object obj = SubjectManager.getInstance().runAs(lookupHelper);
                jndiCache.put(jndiName, obj);
            } catch (PrivilegedActionException ex) {
                throw handleException(ex);
            }
        }
        return jndiCache.get(jndiName);
    }

}
