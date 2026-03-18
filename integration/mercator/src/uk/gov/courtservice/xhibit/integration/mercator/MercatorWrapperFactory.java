package uk.gov.courtservice.xhibit.integration.mercator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: MercatorWrapperFactory
 * </p>
 * <p>
 * Description: MercatorWrapperFactory generates implementations for the
 * MercatorWrapper .
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author GJS
 * @version 1.0
 */
public class MercatorWrapperFactory {
    public static boolean IS_MERCATOR_DISABLED = false;

    // Properties loaded from property file
    protected static Properties MERCATOR_PROPERTIES = null;

    // Property Values
    protected static int MERCATOR_CONNECTION_TIMEOUT_VALUE = 0;

    protected static int MERCATOR_CONNECTION_RETRY_NBR = 1;

    protected static String MERCATOR_XML_ENCODING = "ISO-8859-1"; 
    
    protected static boolean IS_MERCATOR_TO_XML_FILE = false;

    protected static boolean IS_MERCATOR_TO_DEBUG_LOG = false;

    protected static boolean IS_MERCATOR_VOLUME_STATS_LOGGED = false;

    protected static boolean IS_MERCATOR_PERF_STATS_LOGGED = false;

    protected static boolean IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGED = false;

    // HTTP Connection properties
    private static final String MERCATOR_AGENT_IPADDRESS = "MERCATOR_AGENT_IPADDRESS";

    private static final String MERCATOR_AGENT_PORT = "MERCATOR_AGENT_PORT";

    private static final String MERCATOR_AGENT_NAME = "MERCATOR_AGENT_NAME";

    // HTTP Timeout and Reconnect properties
    private static final String MERCATOR_CONNECTION_TIMEOUT = "MERCATOR_CONNECTION_TIMEOUT";

    private static final String DEFAULT_MERCATOR_CONNECTION_TIMEOUT = "0";

    private static final String MERCATOR_CONNECTION_TIMEOUT_MIN = "MERCATOR_CONNECTION_TIMEOUT_MIN";

    private static final String DEFAULT_MERCATOR_CONNECTION_TIMEOUT_MIN = "60000";

    private static final String MERCATOR_CONNECTION_RETRY = "MERCATOR_CONNECTION_RETRY";

    private static final String DEFAULT_MERCATOR_CONNECTION_RETRY = "1";

    // Logging properties
    private static final String IS_MERCATOR_XML_FILE_LOGGING = "IS_MERCATOR_XML_FILE_LOGGING";

    private static final String DEFAULT_MERCATOR_XML_FILE_LOGGING = "FALSE";

    private static final String IS_MERCATOR_XML_TO_LOG = "IS_MERCATOR_XML_TO_LOG";

    private static final String DEFAULT_MERCATOR_XML_TO_LOG = "FALSE";
    
    // Encoding properties
    private static final String PROPERTY_MERCATOR_XML_ENCODING = "MERCATOR_XML_ENCODING";

    private static final String DEFAULT_MERCATOR_XML_ENCODING = "ISO-8859-1";

    // Performance and Volume Statistics properties
    private static final String IS_MERCATOR_VOLUME_STATS = "IS_MERCATOR_VOLUME_STATS";

    private static final String DEFAULT_MERCATOR_VOLUME_STATS = "FALSE";

    private static final String IS_MERCATOR_PERF_STATS = "IS_MERCATOR_PERF_STATS";

    private static final String DEFAULT_MERCATOR_PERF_STATS = "FALSE";

    private static final String IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING = "IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING";

    private static final String DEFAULT_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING = "FALSE";

    // Mercator Disabled property
    private static final String DISABLE_MERCATOR_USE = "disableMercatorUse";

    private static final String MERCATOR_WRAPPER_TYPE = "MERCATOR_WRAPPER_TYPE";

    private static final String HTTP_MERCATOR_WRAPPER_TYPE = "HTTP";

    private static final String STUB_MERCATOR_WRAPPER_TYPE = "STUB";

    private static final String DEFAULT_MERCATOR_WRAPPER_TYPE = "HTTP";

    private static MercatorWrapperFactory mercatorWrapperFactory = new MercatorWrapperFactory();

    private static Map MERCATOR_WRAPPER_CLASSES;

    private static List mercatorURLList = new ArrayList();

    private static int loadBalanceCount = 0;

    private static Logger log = CSServices.getLogger(MercatorWrapperFactory.class);

    static {
        IS_MERCATOR_DISABLED = isDisableMercatorUse();
        MERCATOR_PROPERTIES = getMercatorProperties();

        IS_MERCATOR_TO_XML_FILE = isMercatorXmlFileLogging();
        IS_MERCATOR_TO_DEBUG_LOG = isXmlToLog();
        IS_MERCATOR_VOLUME_STATS_LOGGED = isMercatorVolumeStats();
        IS_MERCATOR_PERF_STATS_LOGGED = isMercatorPerfStats();
        IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGED = isMercatorBusinessErrorsFileLogging();
        MERCATOR_XML_ENCODING = getMercatorXmlEncoding();
        getMercatorAgentUrl(null);
        MERCATOR_CONNECTION_TIMEOUT_VALUE = getConnectionTimeout();
        MERCATOR_CONNECTION_RETRY_NBR = getConnectionRetryAttempts();

        MERCATOR_WRAPPER_CLASSES = new HashMap();
        MERCATOR_WRAPPER_CLASSES.put(HTTP_MERCATOR_WRAPPER_TYPE,
                "uk.gov.courtservice.xhibit.integration.mercator.HTTPMercatorWrapperImpl");
        MERCATOR_WRAPPER_CLASSES.put(STUB_MERCATOR_WRAPPER_TYPE,
                "uk.gov.courtservice.xhibit.integration.mercator.StubMercatorWrapperImpl");
    }

    /**
     * Zero parm constructor for Factory
     * 
     * @roseuid 3DDBB1460363
     */
    private MercatorWrapperFactory() {
    }

    /**
     * Returns the singleton factory object.
     * 
     * @return MercatorWrapperFactory
     */
    public static MercatorWrapperFactory getInstance() {
        if (mercatorWrapperFactory == null) {
            createMercatorWrapperFactory();
        }
        return mercatorWrapperFactory;
    }

    /**
     * Creates the singleton factory object. This method is synchronized as it
     * can be accessed from multiple threads but we only ever want to create one
     * factory (it is called from a SLSB so there may be more than one active at
     * the same time)
     * 
     * @return void
     */
    private synchronized static void createMercatorWrapperFactory() {
        mercatorWrapperFactory = new MercatorWrapperFactory();
    }

    /**
     * Returns a Mercator Wrapper object
     * 
     * 
     * @return MercatorWrapper
     */
    public MercatorWrapper getMercatorWrapper() {
        return createMercatorWrapper();
    }

    /**
     * Creates an Mercator Wrapper object.
     * 
     * In future the production system will always use HTTPMercatorWrapperImpl
     * 
     * @return MercatorWrapper
     */
    protected MercatorWrapper createMercatorWrapper() {
        try {
            String className = (String) MERCATOR_WRAPPER_CLASSES.get(getMercatorWrapperType());
            if (log.isDebugEnabled())
                log.debug("createMercatorWrapper className:" + className);
            return (MercatorWrapper) Class.forName(className).newInstance();
        } catch (Throwable ex) {
            if (log.isDebugEnabled())
                log.debug("createMercatorWrapper failed returning HTTPMercatorWrapperImpl. Error was: " + ex);
            return new HTTPMercatorWrapperImpl();
        }
    }

    /**
     * Looks up the Mercator Wrapper Type from properties It it isn't specified
     * then the default Mercator Wrapper Type is returned. If mercator is
     * disabled then the STUB is returned
     * 
     * @return String
     */
    private String getMercatorWrapperType() {
        String mercatorWrapperType = CSServices.getConfigServices().getProperty(MERCATOR_WRAPPER_TYPE,
                DEFAULT_MERCATOR_WRAPPER_TYPE).toUpperCase();
        log.debug("mercatorWrapperType: " + mercatorWrapperType);

        if (IS_MERCATOR_DISABLED) {
            if (log.isDebugEnabled())
                log.debug("Mercator is disabled to return the Mercator Wrapper Type of Stub");
            return STUB_MERCATOR_WRAPPER_TYPE;
        } else {
            return mercatorWrapperType;
        }
    }

    /**
     * Method to execute transactions using a round robin against the mercator
     * agent(s)
     * 
     * This method is synchronized to ensure the loadBalanceCount doesn't ever
     * get bigger that the mercatorURLList
     * 
     * @param String
     *            failedUrl represents a URL already tried
     * @return String representing the Mercator Agent URL
     */
    protected synchronized static String getMercatorAgentUrl(String failedUrl) {
        String mercatorAgentUrl = null;

        if (mercatorURLList == null || mercatorURLList.isEmpty()) {
            createMercatorAgentUrlArray();
        }

        log.debug("<< current loadBalanceCount: " + loadBalanceCount + " >>");

        if (mercatorURLList.size() == 1 || loadBalanceCount > (mercatorURLList.size() - 1)) {
            loadBalanceCount = 0;
            log.debug("<< Reset loadBalanceCount to zero: " + loadBalanceCount + " >>");
        }

        log.debug("<< Lookup mercatorAgentUrl entry loadBalanceCount: " + loadBalanceCount + " >>");

        try {
            mercatorAgentUrl = ((MercatorURL) mercatorURLList.get(loadBalanceCount)).getMercatorAgentUrl();

            if (failedUrl != null && mercatorURLList.size() != 1 && mercatorAgentUrl.equals(failedUrl)) {
                log.debug("<< Looked-up mercatorAgentUrl entry at loadBalanceCount: " + loadBalanceCount
                        + " is  the same as failedUrl: " + failedUrl + " so a different URL will be tried" + " >>");

                loadBalanceCount++;

                if (loadBalanceCount > (mercatorURLList.size() - 1)) {
                    loadBalanceCount = 0;
                    log.debug("<< Reset loadBalanceCount to zero: " + loadBalanceCount + " >>");
                }

                mercatorAgentUrl = ((MercatorURL) mercatorURLList.get(loadBalanceCount)).getMercatorAgentUrl();
            }
        } catch (Exception e) {
            log.warn("<< Exception retrieving the MerecatorURL from the list so just return the first entry, "
                    + " loadBalanceCount: " + loadBalanceCount + " exception: " + e + " >>");
            mercatorAgentUrl = ((MercatorURL) mercatorURLList.get(0)).getMercatorAgentUrl();
        }

        loadBalanceCount++;

        if (mercatorURLList.size() == 1 || loadBalanceCount > (mercatorURLList.size() - 1)) {
            loadBalanceCount = 0;
            log.debug("<< Reset loadBalanceCount to zero: " + loadBalanceCount + " >>");
        }

        log.debug("<< mercatorAgentUrl: " + mercatorAgentUrl + " >>");

        return mercatorAgentUrl;
    }

    /**
     * Method to create the Mercator Agent URL from properties.
     * 
     * There are 3 properties that may be set - each may be either one entry or
     * a comma separated list If there are multiple IP addresses there may be
     * one port or as many ports as there are IP addresses If there are multiple
     * ports there may be one name or as many names as there are ports
     * 
     * This method is synchronized to ensure we only setup the mercatorURLList
     * once
     * 
     * The format of each Mercator Agent URL is as follows
     * http://%IP_ADDRESS%:%PORT%/%NAME eg http://130.177.3.216:8081/appcomm
     * 
     * @return void
     */
    private synchronized static void createMercatorAgentUrlArray() {
        String mercatorAgentIPAddress = CSServices.getConfigServices().getProperty(MERCATOR_AGENT_IPADDRESS, "");

        String mercatorAgentPort = CSServices.getConfigServices().getProperty(MERCATOR_AGENT_PORT, "");

        String mercatorAgentName = CSServices.getConfigServices().getProperty(MERCATOR_AGENT_NAME, "");

        if (mercatorAgentIPAddress == null || mercatorAgentIPAddress.trim().equals("") || mercatorAgentPort == null
                || mercatorAgentPort.trim().equals("") || mercatorAgentName == null
                || mercatorAgentName.trim().equals("")) {
            // Throws a CSUnrecoverableException to report a configuration
            // problem - the three URL properties must be set correctly
            throw new CSUnrecoverableException(
                    "Configuration Error: Ensure MERCATOR_AGENT_IPADDRESS, MERCATOR_AGENT_PORT and MERCATOR_AGENT_NAME are set");
        }

        if (mercatorURLList == null) {
            mercatorURLList = new ArrayList();
        }

        StringTokenizer st = null;
        String token = null;
        List mercatorAgentIPAddressList = new ArrayList();
        List mercatorAgentPortList = new ArrayList();
        List mercatorAgentNameList = new ArrayList();

        if (mercatorAgentIPAddress.indexOf(",") != -1) {
            st = new StringTokenizer(mercatorAgentIPAddress.trim(), ",");

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                log.debug("***** Mercator Agent IP Address token is: " + token);
                mercatorAgentIPAddressList.add(token);
            }
        } else {
            log.debug("***** Mercator Agent IP Address (one entry) is: " + mercatorAgentIPAddress);
            mercatorAgentIPAddressList.add(mercatorAgentIPAddress);
        }

        if (mercatorAgentPort.indexOf(",") != -1) {
            st = new StringTokenizer(mercatorAgentPort.trim(), ",");

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                log.debug("***** Mercator Agent Port token is: " + token);
                mercatorAgentPortList.add(token);
            }
        } else {
            log.debug("***** Mercator Agent Port (one entry) is: " + mercatorAgentPort);
            mercatorAgentPortList.add(mercatorAgentPort);
        }

        if (mercatorAgentName.indexOf(",") != -1) {
            st = new StringTokenizer(mercatorAgentName.trim(), ",");

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                log.debug("***** Mercator Agent Name token is: " + token);
                mercatorAgentNameList.add(token);
            }
        } else {
            log.debug("***** Mercator Agent Name (one entry) is: " + mercatorAgentName);
            mercatorAgentNameList.add(mercatorAgentName);
        }

        boolean multiplePorts = false;

        if (mercatorAgentPortList.size() > 1) {
            multiplePorts = true;
        }

        boolean multipleNames = false;

        if (mercatorAgentNameList.size() > 1) {
            multipleNames = true;
        }

        MercatorURL mercatorAgentURLEntry = null;

        for (int i = 0; i < mercatorAgentIPAddressList.size(); i++) {
            if (multiplePorts && multipleNames) {
                mercatorAgentURLEntry = new MercatorURL("http://" + mercatorAgentIPAddressList.get(i) + ":"
                        + mercatorAgentPortList.get(i) + "/" + mercatorAgentNameList.get(i));
            } else if (multiplePorts) {
                mercatorAgentURLEntry = new MercatorURL("http://" + mercatorAgentIPAddressList.get(i) + ":"
                        + mercatorAgentPortList.get(i) + "/" + mercatorAgentNameList.get(0));
            } else if (multipleNames) {
                mercatorAgentURLEntry = new MercatorURL("http://" + mercatorAgentIPAddressList.get(i) + ":"
                        + mercatorAgentPortList.get(0) + "/" + mercatorAgentNameList.get(i));
            } else {
                mercatorAgentURLEntry = new MercatorURL("http://" + mercatorAgentIPAddressList.get(i) + ":"
                        + mercatorAgentPortList.get(0) + "/" + mercatorAgentNameList.get(0));
            }

            log.debug("<< mercatorAgentURLEntry: " + mercatorAgentURLEntry.getMercatorAgentUrl() + " >>");

            mercatorURLList.add(mercatorAgentURLEntry);
        } // end for

        if (log.isDebugEnabled()) {
            for (int j = 0; j < mercatorURLList.size(); j++) {
                log.debug("<< mercatorURLList Entry at position: " + j + " is: "
                        + ((MercatorURL) mercatorURLList.get(j)).getMercatorAgentUrl() + " >>");
            }
        }
    }
    

    /**
     * Get the encoding to use for the XML
     * 
     * @return String
     */
    private static String getMercatorXmlEncoding() {
        String mercatorXmlEncoding = CSServices.getConfigServices().getProperty(PROPERTY_MERCATOR_XML_ENCODING,
        		DEFAULT_MERCATOR_XML_ENCODING);

        log.debug("<< getMercatorXmlEncoding: " + mercatorXmlEncoding + " >>");

        return mercatorXmlEncoding;
    }
    
    /**
     * Return true if we require the deserialized XML from the Request and
     * Response to be logged to the separate XML files on the hard drive for
     * testing Will be FALSE by default and could be enabled in testing or
     * production if required
     * 
     * @return boolean
     */
    private static boolean isMercatorXmlFileLogging() {
        String isMercatorXmlFileLogging = CSServices.getConfigServices().getProperty(IS_MERCATOR_XML_FILE_LOGGING,
                DEFAULT_MERCATOR_XML_FILE_LOGGING);

        log.debug("<< isMercatorXmlFileLogging: " + isMercatorXmlFileLogging + " >>");

        return isMercatorXmlFileLogging != null && isMercatorXmlFileLogging.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require the mercator business errors to be logged to
     * file Will be FALSE by default and could be enabled in testing or
     * production if required
     * 
     * @return boolean
     */
    private static boolean isMercatorBusinessErrorsFileLogging() {
        String isMercatorBusinessErrorsFileLogging = CSServices.getConfigServices().getProperty(
                IS_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING, DEFAULT_MERCATOR_BUSINESS_ERRORS_FILE_LOGGING);

        log.debug("<< isMercatorBusinessErrorsFileLogging: " + isMercatorBusinessErrorsFileLogging + " >>");

        return isMercatorBusinessErrorsFileLogging != null
                && isMercatorBusinessErrorsFileLogging.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require the deserialized XML from the Request and
     * Response to be logged to the weblogic log file Will be FALSE by default
     * and could be enabled in testing or production if required although we
     * don't want to clog up this log - better to log the mercator Request and
     * Response to separate files
     * 
     * @return boolean
     */
    private static boolean isXmlToLog() {
        String isXmlToLog = CSServices.getConfigServices().getProperty(IS_MERCATOR_XML_TO_LOG,
                DEFAULT_MERCATOR_XML_TO_LOG);

        log.debug("<< isXmlToLog: " + isXmlToLog + " >>");

        return isXmlToLog != null && isXmlToLog.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if mercator volume statistics are required Will be FALSE by
     * default and could be enabled in testing or production if required
     * 
     * @return boolean
     */
    private static boolean isMercatorVolumeStats() {
        String isMercatorVolumeStats = CSServices.getConfigServices().getProperty(IS_MERCATOR_VOLUME_STATS,
                DEFAULT_MERCATOR_VOLUME_STATS);

        log.debug("<< isMercatorVolumeStats: " + isMercatorVolumeStats + " >>");

        return isMercatorVolumeStats != null && isMercatorVolumeStats.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if mercator performance statistics are required Will be FALSE
     * by default and could be enabled in production if required
     * 
     * @return boolean
     */
    private static boolean isMercatorPerfStats() {
        String isMercatorPerfStats = CSServices.getConfigServices().getProperty(IS_MERCATOR_PERF_STATS,
                DEFAULT_MERCATOR_PERF_STATS);

        log.debug("<< isMercatorPerfStats: " + isMercatorPerfStats + " >>");

        return isMercatorPerfStats != null && isMercatorPerfStats.equalsIgnoreCase("TRUE");
    }

    /**
     * Returns the connection timeout (milliseconds)
     * 
     * @return int
     */
    private static int getConnectionTimeout() {
        int connectionTimeout = 0;
        int minRecommendedConnectionTimeout = 60000;

        try {
            connectionTimeout = new Integer(CSServices.getConfigServices().getProperty(MERCATOR_CONNECTION_TIMEOUT,
                    DEFAULT_MERCATOR_CONNECTION_TIMEOUT)).intValue();
        } catch (Exception e) {
            log.warn("<< ********** HTTP connectionTimeout lookup failed default to 0: " + e + " >>");
        }

        try {
            minRecommendedConnectionTimeout = new Integer(CSServices.getConfigServices().getProperty(
                    MERCATOR_CONNECTION_TIMEOUT_MIN, DEFAULT_MERCATOR_CONNECTION_TIMEOUT_MIN)).intValue();
        } catch (Exception e) {
            log.warn("<< ********** HTTP minimum recommended connectionTimeout lookup failed default to 60000: " + e
                    + " >>");
        }

        if (connectionTimeout >= minRecommendedConnectionTimeout) {
            log.debug("<< ********** HTTP connectionTimeout: " + connectionTimeout + " >>");
        } else {
            if (connectionTimeout == 0) {
                log.warn("<< ********** HTTP connectionTimeout has been configured to 0 "
                        + "(infinite timeout) Timeout set (in milliseconds): " + connectionTimeout
                        + " Minimum Timeout (in milliseconds) recommended: " + minRecommendedConnectionTimeout + " >>");
            } else {
                log.warn("<< ********** HTTP connectionTimeout has been configured to "
                        + " less than recommended minimum. Timeout set (in milliseconds): " + connectionTimeout
                        + " Minimum Timeout (in milliseconds) recommended: " + minRecommendedConnectionTimeout + " >>");
            }
        }

        return connectionTimeout;
    }

    /**
     * Returns the number of retries that will be attempted to connect to
     * Mercator
     * 
     * @return int
     */
    private static int getConnectionRetryAttempts() {
        int connectionRetryAttempts = 0;

        try {
            connectionRetryAttempts = new Integer(CSServices.getConfigServices().getProperty(MERCATOR_CONNECTION_RETRY,
                    DEFAULT_MERCATOR_CONNECTION_RETRY)).intValue();
        } catch (Exception e) {
            log.warn("<< ********** connectionRetryAttempts lookup failed default to 0: " + e + " >>");
        }

        log.debug("<< ********** connectionRetryAttempts: " + connectionRetryAttempts + " >>");
        return connectionRetryAttempts;
    }

    /**
     * Method to return the properties set in the property file that stores all
     * the default setting for executing the mercator map.
     * 
     * @return Properties mercator map specific
     */
    protected static Properties getMercatorProperties() {
        Properties properties = null;

        try {
            properties = CSServices.getConfigServices().getProperties("integration.mercatorwrap");
        } catch (Exception e) {
            log.warn("<< ********** getMercatorProperties lookup failed returning null: " + e + " >>");
        }

        return properties;
    }

    /**
     * Return true if the scheduler servlet has been disabled by system property
     */
    private static boolean isDisableMercatorUse() {
        String property = System.getProperty(DISABLE_MERCATOR_USE);
        return property != null && property.equalsIgnoreCase("TRUE");
    }
}
