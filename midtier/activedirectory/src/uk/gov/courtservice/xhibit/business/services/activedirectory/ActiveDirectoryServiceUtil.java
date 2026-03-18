package uk.gov.courtservice.xhibit.business.services.activedirectory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import netscape.ldap.LDAPv2;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryException;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceConfig;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceFactory;
import uk.gov.courtservice.framework.security.providers.authorization.LDAPContextFactory;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;

/**
 * 
 * @author Will Fardell, This is a wrapper aroung
 * 
 * Scott Atwell - June 2014
 * Due to issues with the XhibitAuthenticator in NLE for the WL10 upgrade:
 * Leaving a lot of code in this class commented out in case in needs to be reinstated at some stage.
 * Primarily, some code has been left in place to allow for:
 * - Specifying whether or not to use the WebLogic XhibitAuthenticator
 * - If not, then a location on the server can be specified for a properties file to lookup
 * - And finally, the ldap.properties bundled with the MidTier.ear can be used to lookup
 * - See LDAPContextFactory too
 */
public class ActiveDirectoryServiceUtil {

    private static final Class[] EMPTY_ARGUMENT_CLASSES = new Class[0];

    private static final Object[] EMPTY_ARGUMENT_VALUES = new Object[0];

    private static final String XHIBIT_AUTHENTICATOR_NAME = System.getProperty(
            "uk.gov.courtservice.framework.security.providers.authentication.xhibitauthenticator.name",
            "XhibitAuthenticator");
    
    //private static final String XHIBIT_AUTHENTICATOR_INUSE = System.getProperty("xhibitauthenticator.inuse", "true");
    
    //private static final String LDAP_PROPERTIES_LOCATION = System.getProperty("ldap.properties.location");

    private static final Logger log = Logger.getLogger(ActiveDirectoryServiceUtil.class);

    private ActiveDirectoryServiceUtil() {
        // Stop construction
    }

    /**
     * Lookup the mbean in the default realm in the default domain in the
     * current context. Use the configuration to get an instance of the service
     * 
     * @return the ActiveDirectoryService from the current context
     * @throws ActiveDirectoryException
     *             if an error occures
     */
    public static ActiveDirectoryService getActiveDirectoryService() {
        return ActiveDirectoryServiceFactory.getActiveDirectoryService(getConfig(lookupAuthenticationProviderMBean()));
    }

    /**
     * Lookup the mbean in the default realm in the default domain in the
     * current context.
     * 
     * @return the XhibitAuthenticatorMBean from the current context
     * @throws ActiveDirectoryException
     *             if an error occures
     */
    private static AuthenticationProviderMBean lookupAuthenticationProviderMBean() {
        try {
            InitialContext context = new InitialContext();
            try {
                MBeanHome home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
                DomainMBean domain = home.getActiveDomain();
                SecurityConfigurationMBean security = domain.getSecurityConfiguration();
                RealmMBean realm = security.findDefaultRealm();
                AuthenticationProviderMBean[] providers = realm.getAuthenticationProviders();

                if (log.isDebugEnabled()) {
                    log.info("Looking for displayName: " + XHIBIT_AUTHENTICATOR_NAME);
                }
                System.out.println("ActiveDirectoryServiceUtil.lookupAuthProviderMBean: Looking for authenticator: " + XHIBIT_AUTHENTICATOR_NAME);

                for (int i = 0; i < providers.length; i++) {
                    if (log.isDebugEnabled()) {
                        log.info("Checking provider displayName: " + providers[i].wls_getDisplayName());
                    }
                    System.out.println("ActiveDirectoryServiceUtil.lookupAuthProviderMBean: Checking provider displayName: " + providers[i]);

                    if (XHIBIT_AUTHENTICATOR_NAME.equals(providers[i].wls_getDisplayName())) {
                        System.out.println("ActiveDirectoryServiceUtil.lookupAuthProviderMBean: Found a matching provider:"+providers[i]);
                        Class c = providers[i].getClass();
                        System.out.println("Printing all methods for : "+c.getName());
                        Method m[] = c.getMethods();
                        for (int x=0; x<m.length; x++) {
                            System.out.println("MethodName: "+ m[x]);
                        }
                        return providers[i];
                    }
                }
                throw new ActiveDirectoryException("Could not find XhibitAuthenticator " + XHIBIT_AUTHENTICATOR_NAME
                        + ".");
            } finally {
                try {
                    context.close();
                } catch (NamingException ne) {
                    log.warn("An error occured closing the context.", ne);
                }
            }
        } catch (NamingException ne) {
            throw new ActiveDirectoryException(ne);
        }
    }

    /**
     * 
     * The MBean uses a complex and unpredictable proxy mechanism, the purpose
     * of this class is to extract the configuration from the mbean. After this
     * the mbean is no longer required and can be descarded. Construct a new
     * config from the mbean. Note how the mbean is discarded imediatly! We use
     * reflection to get the value from the mbean, this ensurese that their is
     * no dependency on the mbean.
     * 
     * @param mbean
     * @return the new config
     */
    private static ActiveDirectoryServiceConfig getConfig(AuthenticationProviderMBean mbean) {
        System.out.println("Getting config!!!!!");
        Context ldapctx = LDAPContextFactory.getLDAPContext();
        ActiveDirectoryServiceConfig adsc = null;
        try {
            System.out.println("ActiveDirectoryServiceUtil.getConfig: Entry");
            
            /*if (XHIBIT_AUTHENTICATOR_INUSE == null || LDAP_PROPERTIES_LOCATION == null) {
                System.out.println("Error reading system properties - null returned");
                return null;
            }
            if (XHIBIT_AUTHENTICATOR_INUSE.equals("false") && LDAP_PROPERTIES_LOCATION.length() == 0) {
                System.out.println("It has been specified that the XhibitAuthenticator is not to be used yet the location is not valid.");
                System.out.println("ldap.properties.location = "+LDAP_PROPERTIES_LOCATION);
                return null;
            }*/
            
            Hashtable<String, String> ldapctxHash = (Hashtable) ldapctx.getEnvironment();
            /*return new ActiveDirectoryServiceConfig("10.63.127.14", 389, "weblogic103", "password",
                    6, true, 32, 60, true, false, 0, 0, 0, "DC=ops,DC=cs,DC=root",
                    LDAPv2.SCOPE_SUB, "(&(sAMAccountName=%u)(objectclass=user))", "sAMAccountName",
                    "devuser3", "DC=ops,DC=cs,DC=root", LDAPv2.SCOPE_SUB, "(objectClass=group)", "cn", "memberOf", "XHIBIT User");*/
            System.out.println("ActiveDirectoryServiceUtil.getConfig: Got ldapCtxHash:null="+(ldapctxHash==null));
            String upa = ldapctxHash.get(LDAPContextFactory.LUPA);
            System.out.println("ActiveDirectoryServiceUtil.getConfig: lupa="+upa);
            String uda = ldapctxHash.get(LDAPContextFactory.LUDA);
            String ufa = ldapctxHash.get(LDAPContextFactory.LUFA);
            String usa = ldapctxHash.get(LDAPContextFactory.LUSA);
            String gpa = ldapctxHash.get(LDAPContextFactory.LGPA);
            String gma = ldapctxHash.get(LDAPContextFactory.LGMA);
            String grp = ldapctxHash.get(LDAPContextFactory.LGRP);
            System.out.println("ActiveDirectoryServiceUtil.getConfig: after lgrp");
            
            System.out.println("ActiveDirectoryServiceUtil.getConfig: about to call adsc");
            //if (XHIBIT_AUTHENTICATOR_INUSE != null && XHIBIT_AUTHENTICATOR_INUSE.equals("true")) {
                System.out.println("ActiveDirectoryServiceUtil.getConfig: Getting adsc using XhibitAuthenticator");
                adsc = new ActiveDirectoryServiceConfig(getString(mbean, "getHost"),
                        getInt(mbean, "getPort"),
                        getString(mbean, "getPrincipal"),
                        getString(mbean, "getCredential"),
                        getInt(mbean, "getConnectionPoolSize"),
                        getBoolean(mbean, "isCacheEnabled"),
                        getInt(mbean, "getCacheSize"),
                        getInt(mbean, "getCacheTTL"),
                        getBoolean(mbean, "isFollowReferrals"),
                        getBoolean(mbean, "isBindAnonymouslyOnReferrals"),
                        getInt(mbean, "getResultsTimeLimit"),
                        getInt(mbean, "getConnectTimeout"),
                        getInt(mbean, "getParallelConnectDelay"),
                        getString(mbean, "getUserBaseDN"),
                        ActiveDirectoryServiceConfig.parseScope(getString(mbean, "getUserSearchScope")),
                        getString(mbean, "getUserFromNameFilter"), 
                        upa, uda, ufa, usa,
                        getString(mbean, "getGroupBaseDN"),
                        ActiveDirectoryServiceConfig.parseScope(getString(mbean, "getGroupSearchScope")),
                        getString(mbean, "getGroupFromNameFilter"),
                        gpa, gma, grp);
            /*} else {
                System.out.println("ActiveDirectoryServiceUtil.getConfig: about to get host from propertiesd file");
                System.out.println("ActiveDirectoryServiceUtil.getConfig: properties file = "+LDAP_PROPERTIES_LOCATION);
                String host = ldapctxHash.get(LDAPContextFactory.LDAP_HOST);
                System.out.println("ActiveDirectoryServiceUtil.getConfig: host="+host);
                int port = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_PORT)).intValue();
                String principal = ldapctxHash.get(LDAPContextFactory.LDAP_PRINCIPAL);
                String credential = ldapctxHash.get(LDAPContextFactory.LDAP_CREDENTIAL);
                int connectionPoolSize = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_CONNECTION_POOL_SIZE)).intValue();
                boolean cacheEnabled = new Boolean(ldapctxHash.get(LDAPContextFactory.LDAP_CACHE_ENABLED)).booleanValue();
                int cacheSize = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_CACHE_SIZE)).intValue();
                int cacheTTL = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_CACHE_TTL)).intValue();
                boolean followReferrals = new Boolean(ldapctxHash.get(LDAPContextFactory.LDAP_FOLLOW_REFERRALS)).booleanValue();
                boolean bindAnonymouslyOnReferrals = new Boolean(ldapctxHash.get(LDAPContextFactory.LDAP_BIND_ANONYMOUSLY_ON_REFERRALS)).booleanValue();
                int resultsTimeLimit = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_RESULTS_TIME_LIMIT)).intValue();
                int connectTimeout = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_CONNECT_TIMEOUT)).intValue();
                int parallelConnectDelay = new Integer(ldapctxHash.get(LDAPContextFactory.LDAP_PARALLEL_CONNECT_DELAY)).intValue();
                String userBaseDN = ldapctxHash.get(LDAPContextFactory.LDAP_USER_BASE_DN);
                String userSearchScope = ldapctxHash.get(LDAPContextFactory.LDAP_USER_SEARCH_SCOPE);
                String userFromNameFromFilter = ldapctxHash.get(LDAPContextFactory.LDAP_USER_FROM_NAME_FILTER);
                String groupBaseDN = ldapctxHash.get(LDAPContextFactory.LDAP_GROUP_BASE_DN);
                String groupSearchScope = ldapctxHash.get(LDAPContextFactory.LDAP_GROUP_SEARCH_SCOPE);
                String groupFromNameFilter = ldapctxHash.get(LDAPContextFactory.LDAP_GROUP_NAME_FROM_FILTER);
                
                adsc = new ActiveDirectoryServiceConfig(host, port, principal, credential, connectionPoolSize, cacheEnabled, cacheSize,
                        cacheTTL, followReferrals, bindAnonymouslyOnReferrals, resultsTimeLimit, connectTimeout, parallelConnectDelay,
                        userBaseDN, ActiveDirectoryServiceConfig.parseScope(userSearchScope), userFromNameFromFilter, upa, uda, ufa, usa, 
                        groupBaseDN, ActiveDirectoryServiceConfig.parseScope(groupSearchScope), groupFromNameFilter, gpa, gma, grp);
            }
            System.out.println("ActiveDirectoryServiceUtil.getConfig: got adsc");*/
            /*return new ActiveDirectoryServiceConfig(getString(mbean, "getHost"),
                    getInt(mbean, "getPort"),
                    getString(mbean, "getPrincipal"),
                    getString(mbean, "getCredential"),
                    getInt(mbean, "getConnectionPoolSize"),
                    getBoolean(mbean, "isCacheEnabled"),
                    getInt(mbean, "getCacheSize"),
                    getInt(mbean, "getCacheTTL"),
                    getBoolean(mbean, "isFollowReferrals"),
                    getBoolean(mbean, "isBindAnonymouslyOnReferrals"),
                    getInt(mbean, "getResultsTimeLimit"),
                    getInt(mbean, "getConnectTimeout"),
                    getInt(mbean, "getParallelConnectDelay"),
                    getString(mbean, "getUserBaseDN"),
                    ActiveDirectoryServiceConfig.parseScope(getString(mbean, "getUserSearchScope")),
                    getString(mbean, "getUserFromNameFilter"), 
                    "samAccountName", "displayName", "givenName", "sn",
                    getString(mbean, "getGroupBaseDN"),
                    ActiveDirectoryServiceConfig.parseScope(getString(mbean, "getGroupSearchScope")),
                    getString(mbean, "getGroupFromNameFilter"),
                    "cn", "memberOf", "XHIBIT User");*/
        } catch (NamingException ne) {
            ne.printStackTrace();
        } finally {
            return adsc;
        }
    }

    private static boolean getBoolean(AuthenticationProviderMBean mbean, String methodName) {
        try {
            System.out.println("ActiveDirectoryServiceUtil.getBoolean: entry");
            if (mbean != null) {
                System.out.println("ActiveDirectoryServiceUtil.getBoolean: mbean="+mbean);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getBoolean: mbean=null");
            }
            if (methodName != null) {
                System.out.println("ActiveDirectoryServiceUtil.getBoolean: methodName="+methodName);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getBoolean: methodName=null");
            }
            return ((Boolean) getObject(mbean, methodName)).booleanValue();
        } catch (ClassCastException cce) {
            throw new ActiveDirectoryException(cce);
        } catch (NullPointerException npe) {
            throw new ActiveDirectoryException(npe);
        }
    }

    private static int getInt(AuthenticationProviderMBean mbean, String methodName) {
        try {
            System.out.println("ActiveDirectoryServiceUtil.getInt: entry");
            if (mbean != null) {
                System.out.println("ActiveDirectoryServiceUtil.getInt: mbean="+mbean);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getInt: mbean=null");
            }
            if (methodName != null) {
                System.out.println("ActiveDirectoryServiceUtil.getInt: methodName="+methodName);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getInt: methodName=null");
            }
            return ((Integer) getObject(mbean, methodName)).intValue();
        } catch (ClassCastException cce) {
            throw new ActiveDirectoryException(cce);
        } catch (NullPointerException npe) {
            throw new ActiveDirectoryException(npe);
        }
    }

    private static String getString(AuthenticationProviderMBean mbean, String methodName) {
        try {
            System.out.println("ActiveDirectoryServiceUtil.getString: entry");
            if (mbean != null) {
                System.out.println("ActiveDirectoryServiceUtil.getString: mbean="+mbean);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getString: mbean=null");
            }
            if (methodName != null) {
                System.out.println("ActiveDirectoryServiceUtil.getString: methodName="+methodName);
            } else {
                System.out.println("ActiveDirectoryServiceUtil.getString: methodName=null");
            }
            return (String) getObject(mbean, methodName);
        } catch (ClassCastException cce) {
            throw new ActiveDirectoryException(cce);
        }
    }

    private static Object getObject(AuthenticationProviderMBean mbean, String methodName) {
        try {
            System.out.println("ActiveDirectoryServiceUtil entry");
            log.debug("ActiveDirectoryServiceUtil entry");
            if (mbean != null) {
                if (mbean.getName() != null) {
                    System.out.println("mbean name = " +mbean.getName());
                    log.debug("ActiveDirectoryServiceUtil entry");
                } else {
                    System.out.println("mbean.getname is null");
                    log.debug("ActiveDirectoryServiceUtil entry");
                }
            } else {
                System.out.println("mbean is null");
                log.debug("ActiveDirectoryServiceUtil entry");
            }
            System.out.println("method name = " +methodName+":");
            log.debug("ActiveDirectoryServiceUtil entry");
            Class clazz = mbean.getClass();
            System.out.println("ADSU here");
            log.debug("ActiveDirectoryServiceUtil entry");
            Method method = clazz.getMethod(methodName, EMPTY_ARGUMENT_CLASSES);
            System.out.println("ActiveDirectoryServiceUtil.getObject: method "+method.getName()+"about to be invoked");
            
            try {
                method.invoke(mbean, EMPTY_ARGUMENT_VALUES);
            } catch (Exception e) {
                System.out.println("Exception invoking method:"+method.getName());
                e.printStackTrace();
                e.getCause();
                System.out.println("End of exception handling");
            }
            return method.invoke(mbean, EMPTY_ARGUMENT_VALUES);
        } catch (SecurityException e) {
            System.out.println("Err 1:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        } catch (NoSuchMethodException e) {
            System.out.println("Err 2:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        } catch (IllegalArgumentException e) {
            System.out.println("Err 3:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        } catch (IllegalAccessException e) {
            System.out.println("Err 4:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        } catch (InvocationTargetException e) {
            System.out.println("Err 5:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        } catch (Exception e) {
            System.out.println("Err 6:");
            System.out.println("Exception is : "+e.toString());
            e.printStackTrace();
            throw new ActiveDirectoryException(e);
        }
    }

}
