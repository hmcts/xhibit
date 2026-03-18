package uk.gov.courtservice.framework.services.locator;

import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.security.SubjectManager;

/**
 * @author Meeraj
 * @title EagerServiceLocatorImpl
 */
public class EagerLoadingServiceLocatorImpl extends ServiceLocatorImpl {

    private Map jndiCache = new HashMap();

    /**
     * Eager loads the service locator cache
     */
    protected EagerLoadingServiceLocatorImpl() {
        try {

            LookupHelper lookupHelper = new LookupHelper(getEnv());
            jndiCache = (HashMap) SubjectManager.getInstance().runAs(lookupHelper);
        } catch (PrivilegedActionException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Utility method to lookup objects
     * 
     * @param serverName
     * @return
     */
    protected Object lookup(String jndiName) {
        if (!jndiCache.containsKey(jndiName)) {
            throw new CSUnrecoverableException("Object not found, " + jndiName);
        }
        return jndiCache.get(jndiName);
    }

}
