package uk.gov.courtservice.framework.security.providers.authorization;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Factory class to create a <code>LDAPContext</code> to connect to the default
 * server. The properties are loaded from the command line if present, or values
 * are defaulted to those located in the application.properties file.
 * 
 * @author tz0d5m
 * @version $Id: LDAPContextFactory.java,v 1.1 2014/06/25 11:17:07 atwells Exp $
 */
public final class LDAPContextFactory {
    /** The log4j <code>Logger</code> instance */
    private static final Logger log = Logger.getLogger(LDAPContextFactory.class);
    
    /**
     * Keys used for ldap
     */
    private static final String USER_PRINCIPAL_ATTRIBUTE = "ldap.userPrincipalAttribute";
    private static final String USER_DISPLAY_ATTRIBUTE = "ldap.userDisplayAttribute";
    private static final String USER_FORENAME_ATTRIBUTE = "ldap.userForenameAttribute";
    private static final String USER_SURNAME_ATTRIBUTE = "ldap.userSurnameAttribute";
    private static final String GROUP_PRINCIPAL_ATTRIBUTE = "ldap.groupPrincipalAttribute";
    private static final String GROUP_MEMBER_OF_ATTRIBUTE = "ldap.groupMemberOfAttribute"; 
    private static final String GROUP_ROOT_PRINCIPAL = "ldap.groupRootPrincipal";
    
    /**
     * Scott Atwell - June 2014
     * Due to issues with the XhibitAuthenticator in NLE for the WL10 upgrade:
     * Leaving a lot of code in this class commented out in case in needs to be reinstated at some stage.
     * Primarily, some code has been left in place to allow for:
     * - Specifying whether or not to use the WebLogic XhibitAuthenticator
     * - If not, then a location on the server can be specified for a properties file to lookup
     * - And finally, the ldap.properties bundled with the MidTier.ear can be used to lookup
     * - See ActiveDirectoryServiceUtil too
     */
    /*private static String HOST = "ldap.host";
    private static String PORT = "ldap.port";
    private static String PRINCIPAL = "ldap.principal";
    private static String CREDENTIAL = "ldap.credential";
    private static String CONNECTION_POOL_SIZE = "ldap.connectionPoolSize";
    private static String CACHE_ENABLED = "ldap.cacheEnabled";
    private static String CACHE_SIZE = "ldap.cacheSize";
    private static String CACHE_TTL = "ldap.cacheTTL";
    private static String FOLLOW_REFERRALS = "ldap.followReferrals";
    private static String BIND_ANONYMOUSLY_ON_REFERRALS = "ldap.bindAnonymouslyOnReferrals";
    private static String RESULTS_TIME_LIMIT = "ldap.resultsTimeLimit";
    private static String CONNECT_TIMEOUT = "ldap.connectTimeout";
    private static String PARALLEL_CONNECT_DELAY = "ldap.parallelConnectDelay";
    private static String USER_BASE_DN = "ldap.userBaseDN";
    private static String USER_SEARCH_SCOPE = "ldap.userSearchScope";
    private static String USER_FROM_NAME_FILTER = "ldap.userFromNameFilter";
    private static String GROUP_BASE_DN = "ldap.groupBaseDN";
    private static String GROUP_SEARCH_SCOPE = "ldap.groupSearchScope";
    private static String GROUP_NAME_FROM_FILTER = "ldap.groupFromNameFilter";*/
    
    /**
     * Public accessors for LDAP entries
     */
    public static String LUPA = "LDAP_USER_PRINCIPAL_ATTRIBUTE";
    public static String LUDA = "LDAP_USER_DISPLAY_ATTRIBUTE";
    public static String LUFA = "LDAP_USER_FORENAME_ATTRIBUTE";
    public static String LUSA = "LDAP_USER_SURNAME_ATTRIBUTE"; 
    public static String LGPA = "LDAP_GROUP_PRINCIPAL_ATTRIBUTE";
    public static String LGMA = "LDAP_GROUP_MEMBER_OF_ATTRIBUTE";
    public static String LGRP = "LDAP_GROUP_ROOT_PRINCIPAL";
    
    /*public static String LDAP_HOST = "LDAP_HOST";
    public static String LDAP_PORT = "LDAP_PORT";
    public static String LDAP_PRINCIPAL = "LDAP_PRINCIPAL";
    public static String LDAP_CREDENTIAL = "LDAP_CREDENTIAL";
    public static String LDAP_CONNECTION_POOL_SIZE = "LDAP_CONNECTION_POOL_SIZE";
    public static String LDAP_CACHE_ENABLED = "LDAP_CACHE_ENABLED";
    public static String LDAP_CACHE_SIZE = "LDAP_CACHE_SIZE";
    public static String LDAP_CACHE_TTL = "LDAP_CACHE_TTL";
    public static String LDAP_FOLLOW_REFERRALS = "";
    public static String LDAP_BIND_ANONYMOUSLY_ON_REFERRALS = "LDAP_BIND_ANONYMOUSLY_ON_REFERRALS";
    public static String LDAP_RESULTS_TIME_LIMIT = "LDAP_RESULTS_TIME_LIMIT";
    public static String LDAP_CONNECT_TIMEOUT = "LDAP_CONNECT_TIMEOUT";
    public static String LDAP_PARALLEL_CONNECT_DELAY = "LDAP_PARALLEL_CONNECT_DELAY";
    public static String LDAP_USER_BASE_DN = "LDAP_USER_BASE_DN";
    public static String LDAP_USER_SEARCH_SCOPE = "LDAP_USER_SEARCH_SCOPE";
    public static String LDAP_USER_FROM_NAME_FILTER = "LDAP_USER_FROM_NAME_FILTER";
    public static String LDAP_GROUP_BASE_DN = "LDAP_GROUP_BASE_DN";
    public static String LDAP_GROUP_SEARCH_SCOPE = "LDAP_GROUP_SEARCH_SCOPE";
    public static String LDAP_GROUP_NAME_FROM_FILTER = "LDAP_GROUP_NAME_FROM_FILTER";*/

    /** The environment used to create a context */
    private static final Hashtable<String, String> environment;
    
    /*private static final String XHIBIT_AUTHENTICATOR_INUSE = System.getProperty("xhibitauthenticator.inuse", "true");
    private static final String LDAP_PROPERTIES_LOCATION = System.getProperty("ldap.properties.location");
    private static final String ENVIRONMENT_TYPE = System.getProperty("environment.type", "nle");*/

    static {
        log.debug("static() - start of static initialisation block");

        try {
            final Properties props = new Properties();
            props.load(LDAPContextFactory.class.getClassLoader().getResourceAsStream("ldap.properties"));
            
            // ldap properties
            final String userPrincipalAttribute = props.getProperty(USER_PRINCIPAL_ATTRIBUTE);
            final String userDisplayAttribute = props.getProperty(USER_DISPLAY_ATTRIBUTE);
            final String userForenameAttribute = props.getProperty(USER_FORENAME_ATTRIBUTE);
            final String userSurnameAttribute = props.getProperty(USER_SURNAME_ATTRIBUTE);
            final String groupPrincipalAttribute = props.getProperty(GROUP_PRINCIPAL_ATTRIBUTE);
            final String groupMemberOfAttribute = props.getProperty(GROUP_MEMBER_OF_ATTRIBUTE);
            final String groupRootPrincipal = props.getProperty(GROUP_ROOT_PRINCIPAL);
            
            // construct the environment using the properties acquired...
            environment = new Hashtable<String, String>();
            environment.put(LUPA, userPrincipalAttribute);
            environment.put(LUDA, userDisplayAttribute);
            environment.put(LUFA, userForenameAttribute);
            environment.put(LUSA, userSurnameAttribute);
            environment.put(LGPA, groupPrincipalAttribute);
            environment.put(LGMA, groupMemberOfAttribute);
            environment.put(LGRP, groupRootPrincipal);
            
            /*if (XHIBIT_AUTHENTICATOR_INUSE != null && XHIBIT_AUTHENTICATOR_INUSE.equals("false")) {
                try {
                    FileInputStream in = new FileInputStream(LDAP_PROPERTIES_LOCATION);
                    props.load(in);
                    
                    final String host = props.getProperty(HOST);
                    final String port = props.getProperty(PORT);
                    final String principal = props.getProperty(PRINCIPAL);
                    final String credential = props.getProperty(CREDENTIAL);
                    final String connectionPoolSize = props.getProperty(CONNECTION_POOL_SIZE);
                    final String cacheEnabled = props.getProperty(CACHE_ENABLED);
                    final String cacheSize = props.getProperty(CACHE_SIZE);
                    final String cacheTTL = props.getProperty(CACHE_TTL);
                    final String followReferrals = props.getProperty(FOLLOW_REFERRALS);
                    final String bindAnonymouslyOnReferrals = props.getProperty(BIND_ANONYMOUSLY_ON_REFERRALS);
                    final String resultsTimeLimit = props.getProperty(RESULTS_TIME_LIMIT);
                    final String connectTimeout = props.getProperty(CONNECT_TIMEOUT);
                    final String parallelConnectDelay = props.getProperty(PARALLEL_CONNECT_DELAY);
                    final String userBaseDN = props.getProperty(USER_BASE_DN);
                    final String userSearchScope = props.getProperty(USER_SEARCH_SCOPE);
                    final String userFromNameFilter = props.getProperty(USER_FROM_NAME_FILTER);
                    final String groupBaseDN = props.getProperty(GROUP_BASE_DN);
                    final String groupSearchScope = props.getProperty(GROUP_SEARCH_SCOPE);
                    final String groupFromNameFilter = props.getProperty(GROUP_NAME_FROM_FILTER);
                    
                    environment.put(LDAP_HOST, host);
                    environment.put(LDAP_PORT, port);
                    environment.put(LDAP_PRINCIPAL, principal);
                    environment.put(LDAP_CREDENTIAL, credential);
                    environment.put(LDAP_CONNECTION_POOL_SIZE, connectionPoolSize);
                    environment.put(LDAP_CACHE_ENABLED, cacheEnabled);
                    environment.put(LDAP_CACHE_SIZE, cacheSize);
                    environment.put(LDAP_CACHE_TTL, cacheTTL);
                    environment.put(LDAP_FOLLOW_REFERRALS, followReferrals);
                    environment.put(LDAP_BIND_ANONYMOUSLY_ON_REFERRALS, bindAnonymouslyOnReferrals);
                    environment.put(LDAP_RESULTS_TIME_LIMIT, resultsTimeLimit);
                    environment.put(LDAP_CONNECT_TIMEOUT, connectTimeout);
                    environment.put(LDAP_PARALLEL_CONNECT_DELAY, parallelConnectDelay);
                    environment.put(LDAP_USER_BASE_DN, userBaseDN);
                    environment.put(LDAP_USER_SEARCH_SCOPE, userSearchScope);
                    environment.put(LDAP_USER_FROM_NAME_FILTER, userFromNameFilter);
                    environment.put(LDAP_GROUP_BASE_DN, groupBaseDN);
                    environment.put(LDAP_GROUP_SEARCH_SCOPE, groupSearchScope);
                    environment.put(LDAP_GROUP_NAME_FROM_FILTER, groupFromNameFilter);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error loading properties from external file so load from embeeded ldap.properties");
                    props.load(LDAPContextFactory.class.getClassLoader().getResourceAsStream("ldap.properties"));
                    
                    if (ENVIRONMENT_TYPE != null) {
                        System.out.println("environment.type="+ENVIRONMENT_TYPE);
                        setPropertyValuesByEnvironment(ENVIRONMENT_TYPE);
                        System.out.println("HOST = "+HOST);
                        final String host = props.getProperty(HOST);
                        final String port = props.getProperty(PORT);
                        final String principal = props.getProperty(PRINCIPAL);
                        final String credential = props.getProperty(CREDENTIAL);
                        final String connectionPoolSize = props.getProperty(CONNECTION_POOL_SIZE);
                        final String cacheEnabled = props.getProperty(CACHE_ENABLED);
                        final String cacheSize = props.getProperty(CACHE_SIZE);
                        final String cacheTTL = props.getProperty(CACHE_TTL);
                        final String followReferrals = props.getProperty(FOLLOW_REFERRALS);
                        final String bindAnonymouslyOnReferrals = props.getProperty(BIND_ANONYMOUSLY_ON_REFERRALS);
                        final String resultsTimeLimit = props.getProperty(RESULTS_TIME_LIMIT);
                        final String connectTimeout = props.getProperty(CONNECT_TIMEOUT);
                        final String parallelConnectDelay = props.getProperty(PARALLEL_CONNECT_DELAY);
                        final String userBaseDN = props.getProperty(USER_BASE_DN);
                        final String userSearchScope = props.getProperty(USER_SEARCH_SCOPE);
                        final String userFromNameFilter = props.getProperty(USER_FROM_NAME_FILTER);
                        final String groupBaseDN = props.getProperty(GROUP_BASE_DN);
                        final String groupSearchScope = props.getProperty(GROUP_SEARCH_SCOPE);
                        final String groupFromNameFilter = props.getProperty(GROUP_NAME_FROM_FILTER);
                        
                        environment.put(LDAP_HOST, host);
                        environment.put(LDAP_PORT, port);
                        environment.put(LDAP_PRINCIPAL, principal);
                        environment.put(LDAP_CREDENTIAL, credential);
                        environment.put(LDAP_CONNECTION_POOL_SIZE, connectionPoolSize);
                        environment.put(LDAP_CACHE_ENABLED, cacheEnabled);
                        environment.put(LDAP_CACHE_SIZE, cacheSize);
                        environment.put(LDAP_CACHE_TTL, cacheTTL);
                        environment.put(LDAP_FOLLOW_REFERRALS, followReferrals);
                        environment.put(LDAP_BIND_ANONYMOUSLY_ON_REFERRALS, bindAnonymouslyOnReferrals);
                        environment.put(LDAP_RESULTS_TIME_LIMIT, resultsTimeLimit);
                        environment.put(LDAP_CONNECT_TIMEOUT, connectTimeout);
                        environment.put(LDAP_PARALLEL_CONNECT_DELAY, parallelConnectDelay);
                        environment.put(LDAP_USER_BASE_DN, userBaseDN);
                        environment.put(LDAP_USER_SEARCH_SCOPE, userSearchScope);
                        environment.put(LDAP_USER_FROM_NAME_FILTER, userFromNameFilter);
                        environment.put(LDAP_GROUP_BASE_DN, groupBaseDN);
                        environment.put(LDAP_GROUP_SEARCH_SCOPE, groupSearchScope);
                        environment.put(LDAP_GROUP_NAME_FROM_FILTER, groupFromNameFilter);
                        
                    } else {
                        System.out.println("Environment system property is null");
                    }
                }
            }*/
        } catch (final IOException e) {
            throw new NestedException(e);
        }

        log.debug("static() - end of static initialisation block");
    }

    /**
     * Acquire the <code>LDAPContext</code> used to connect to the default server.
     * 
     * @return A new <code>LDAPContext</code>.
     */
    public static Context getLDAPContext() {
        try {
            return new InitialContext(environment);
        } catch (final NamingException e) {
            throw new NestedException(e);
        }
    }
    
    /*private static void setPropertyValuesByEnvironment(String env) {
        HOST = "ldap."+env+".host";
        PORT = "ldap."+env+".port";
        PRINCIPAL = "ldap."+env+".principal";
        CREDENTIAL = "ldap."+env+".credential";
        CONNECTION_POOL_SIZE = "ldap."+env+".connectionPoolSize";
        CACHE_ENABLED = "ldap."+env+".cacheEnabled";
        CACHE_SIZE = "ldap."+env+".cacheSize";
        CACHE_TTL = "ldap."+env+".cacheTTL";
        FOLLOW_REFERRALS = "ldap."+env+".followReferrals";
        BIND_ANONYMOUSLY_ON_REFERRALS = "ldap."+env+".bindAnonymouslyOnReferrals";
        RESULTS_TIME_LIMIT = "ldap."+env+".resultsTimeLimit";
        CONNECT_TIMEOUT = "ldap."+env+".connectTimeout";
        PARALLEL_CONNECT_DELAY = "ldap."+env+".parallelConnectDelay";
        USER_BASE_DN = "ldap."+env+".userBaseDN";
        USER_SEARCH_SCOPE = "ldap."+env+".userSearchScope";
        USER_FROM_NAME_FILTER = "ldap."+env+".userFromNameFilter";
        GROUP_BASE_DN = "ldap."+env+".groupBaseDN";
        GROUP_SEARCH_SCOPE = "ldap."+env+".groupSearchScope";
        GROUP_NAME_FROM_FILTER = "ldap."+env+".groupFromNameFilter";
    }*/
}
