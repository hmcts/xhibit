package uk.gov.courtservice.xhibit.business.services.darts;


import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.naming.*;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;


/**
 * <p>
 * Title: DartsConfiguration
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class holds the configuration parameters entered into the DAR_DARTS_CONFIG table.  It is a singleton class that refreshes 
 * values on a periodic basis dependent on cache time set in the config params.  This class is used throughout the DARTS interface
 * and is the controlling class for all the configurable options. 
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela, Paul Milner 
 * @version 1.1 20090105 
 
 */
public class DartsConfiguration {
    
    private static final long DEFAULT_CACHE_EXPIRY_PERIOD = 60000;
    
    private static final String CACHE_TIME_KEY = "cache.time";
    private static final String DARTS_ACTIVE_KEY = "darts.active";
    private static final String DARTS_DOCTYPES_KEY = "darts.doctypes";
    private static final String DARTS_CONFIG_PROPERTIES_STRING =
        "SELECT DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE FROM DAR_DARTS_CONFIG";
    
    private static final Logger log = CSServices.getLogger(DartsConfiguration.class);
    
    private static DartsConfiguration instance = new DartsConfiguration();
    private static Properties dartsProperties;
    
    private static long _nextRefreshTime = System.currentTimeMillis();
    
    /**
     * Singleton constructor. 
     */
    private DartsConfiguration() 
    {
        // Stop construction from outside package
    }
    
    
    /**
     * Get the single instance of the cache
     * 
     * @return the instance
     * @throws DartsException 
     */
    public static DartsConfiguration getInstance() throws DartsException 
    {
        if (log.isDebugEnabled())  {
            log.debug("Getting Darts config instance (v=8)");
        }    
        instance.checkRefresh();
        return instance;
    }
    
     
    /**
     * Internal method that check the freshness of held properties and refreshes if the cache 
     * time has expired.  The cache time is configurable through the DB. 
     */
    private void checkRefresh() 
    {
        try {
            /* log.debug("CHECKING REFRESH"); */
            long currentTime = System.currentTimeMillis();
            if (dartsProperties == null) {
                refreshDartsProperties();
            } 
            else if (_nextRefreshTime < currentTime) {
                if (log.isDebugEnabled()) {
                    log.debug("Refreshing dartsProperties (from Darts schema)");
                }
                refreshDartsProperties();
            }
    } catch (NumberFormatException nfe) {
            log.error("CacheTime not a valid number, default props created: " + nfe.getMessage());
            createDefaultProperties();
        } catch (DartsException de) {
            log.error("Unable to create a Darts configuration:" + de.getMessage());
            createDefaultProperties();
        }    
    }// end of checkRefresh()


    private void refreshDartsProperties() throws DartsException 
    {
        getDartsPropertiesFromDatabase();        
        
        if (dartsProperties == null) {
            throw new DartsException("DB refresh failed.");
        }
        return;
    }// end refreshDartsProperties()
    
    
    public boolean isDartsActive() {
        checkRefresh();
        String dartsActiveAsString = dartsProperties.getProperty(DARTS_ACTIVE_KEY);
        boolean dartsActive = Boolean.valueOf(dartsActiveAsString).booleanValue();
        return dartsActive;
    }

    public int getCacheTime() {
        checkRefresh();
        String dartsCacheTimeAsString = dartsProperties.getProperty(CACHE_TIME_KEY);
        int dartsCacheTime = Integer.valueOf(dartsCacheTimeAsString).intValue();
        return dartsCacheTime;
    }
    
    public String getDoctypes() {
        checkRefresh();
        String dartsDoctypes = dartsProperties.getProperty(DARTS_DOCTYPES_KEY);
        return dartsDoctypes;
    }
    
    /**
     * Generic get method for any property held within the property set.
     * 
     *  @param propertyName : String
     *                  The name corresponding to DARTS_PROPERTY_NAME in the config DB.
     *  @return The value 
     *                  corresponding DARTS_PROPERTY_VALUE within the config DB.                
     */
    public String getProperty(String propertyName) {
        checkRefresh();
        String propertyValue = dartsProperties.getProperty(propertyName);
        if(log.isDebugEnabled()){
            log.debug("Returning DARTS property: name=" + propertyName + ", value=" + propertyValue);
        }
        return propertyValue;
    }
    
    /**
     * Special get method to return the Document type String back as a
     * String Array
     */
    public String[] getDoctypesArray() {
        String doctypesAsString = getDoctypes();
        if (doctypesAsString!=null) {
            String[] doctypes = doctypesAsString.split(",");
            return doctypes;            
        } else {
            return null;
        }
    }
    
    
    /**
     * Internal method that retrieves a fresh set of properties from the DB. 
     */
    private synchronized void getDartsPropertiesFromDatabase() throws DartsException 
    {
        // Another thread has already refreshed then exit imediately
        long currentTime = System.currentTimeMillis();
        if (_nextRefreshTime > currentTime) {
            return;
        }

        Properties refreshedProperties = new Properties();
        Connection connection = null;
        Statement statement = null;
        
        try {
            // log.debug("DartsConfiguration (using Darts schema): Trying to get the Darts properties from database... ");
            Context initialContext = CSServices.getServiceLocator().getInitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("DartsOracleTxDataSource");
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute(DARTS_CONFIG_PROPERTIES_STRING);
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                String propertyName = result.getString("DARTS_PROPERTY_NAME");
                String propertyValue = result.getString("DARTS_PROPERTY_VALUE");
                if (propertyValue==null) {
                    log.warn("Null value found for Darts property " + propertyName);
                    propertyValue = "";
                }
                refreshedProperties.put(propertyName,propertyValue);
                if(log.isDebugEnabled()){
                    log.debug("adding Darts property name=" + propertyName + ",value=" + propertyValue);
                }
            }
            dartsProperties = refreshedProperties;

            long cacheExpiryPeriod;
            String cacheExpiryPeriodAsString = dartsProperties.getProperty(CACHE_TIME_KEY);
            if (cacheExpiryPeriodAsString != null) {
                cacheExpiryPeriod = Long.valueOf(cacheExpiryPeriodAsString);
            } else {
                cacheExpiryPeriod = DEFAULT_CACHE_EXPIRY_PERIOD;
            }
            _nextRefreshTime = currentTime + cacheExpiryPeriod;

            result.close();
            statement.close();
            connection.close();
        
        }
        catch (SQLException se) {
            log.error("Error on SQL connection/statement :" + se.getMessage());
        }
        catch (NamingException ne) {
            log.error("Error on looking up datasource :" +  ne.getMessage() );
        }
        
        finally {
            try {
                if (connection!=null) connection.close();
            }
            catch (SQLException se) {
                log.error("Error on closing SQL connection/statement :" + se.getMessage());
            }
        }
        return;
    }// end of getDartsPropertiesFromDatabase()
    
    private static void createDefaultProperties() {
        Properties defaultProps = new Properties();
        defaultProps.put("MAX_LOCK_ATTEMPTS", "3");
        defaultProps.put("BULK_COUNT", "10");
        defaultProps.put("darts.doctypes", "DL,NEWCASE,UPDCASE");
        defaultProps.put("cache.time", "60000");
        defaultProps.put("darts.active", "true");
        defaultProps.put("darts.repository", "moj_darts");
        defaultProps.put("darts.user", "dmadmin");
        defaultProps.put("darts.password", "dmadmin");
        defaultProps.put("darts.moduleName", "darts");
        defaultProps.put("darts.contextRoot", "http://<unknown_URL>/service");
        defaultProps.put("darts.retry.max.times", "3");
        defaultProps.put("darts.retry.interval", "2000000");
        defaultProps.put("darts.conn_retry_wait", "450000");
        dartsProperties = defaultProps;
        long currentTime = System.currentTimeMillis();
        _nextRefreshTime = currentTime + DEFAULT_CACHE_EXPIRY_PERIOD;

    }

}// end of class
