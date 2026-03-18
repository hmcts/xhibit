package uk.gov.courtservice.xhibit.business.services.messagebroker;

import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.RuleVO;
import uk.gov.courtservice.xhibit.database.messagebroker.MessageBrokerDatabase;

public class RulesCache {
    private static final Logger log = CSServices.getLogger(RulesCache.class);
    
    private static RulesCache instance;

    private RuleVO[] cache;

    private long cacheTime;

    /**
     * Get the single instance of the cache
     * 
     * @return the cache
     */
    public synchronized static RulesCache getInstance() {
        if (instance == null) {
            instance = new RulesCache();
        }
        return instance;
    }
    
    protected RulesCache() {
        // Stop construction from outside package
    }

    /**
     * Get the Rules from the cache
     * 
     * @return the cache
     */
    public synchronized RuleVO[] getRules() {
        long currentTime = System.currentTimeMillis();
        if (cache == null || (cacheTime + getCacheDuration()) < currentTime) {
            cache = createRules();
            cacheTime = currentTime;
        }
        return cache;
    }
    
    protected RuleVO[] createRules() {
        MessageBrokerDatabase database = new MessageBrokerDatabase();
        return database.getRules();
    }

    private static final long getCacheDuration() {
        Properties props = CSServices.getConfigServices().getProperties( "rulesManager" );
        String value = props.getProperty( "cacheDuration" );
        
        return _getCacheDuration( value );
    }
    
    protected static long _getCacheDuration( String cacheDuration ) {
        if( cacheDuration != null ) {
            try {
                return new Long(cacheDuration).longValue();
            }
            catch( NumberFormatException nfe ) {
                log.warn( "Illegal value for rulesManager.cacheDuration <" + cacheDuration + ">" );
            }
        }
        return 15 * 60 * 1000;
    }
}
