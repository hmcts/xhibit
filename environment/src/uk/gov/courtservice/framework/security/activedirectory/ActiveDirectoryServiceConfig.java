package uk.gov.courtservice.framework.security.activedirectory;

import netscape.ldap.LDAPv2;

/**
 * The MBean uses a complex and unpredictable proxy mechanism, the purpose of
 * this class is to extract the configuration from the mbean. After this the
 * mbean is no longer required and can be descarded.
 * 
 * @author Will Fardell
 */
public class ActiveDirectoryServiceConfig {
    private final String host;

    private final int port;

    private final String principal;

    private final String credential;
    
    private final int connectionPoolSize;

    private final boolean cacheEnabled;

    private final int cacheSize;

    private final int cacheTTL;

    private final boolean followReferrals;

    private final boolean bindAnonymouslyOnReferrals;

    private final int resultsTimeLimit;

    private final int connectTimeout;

    private final int parallelConnectDelay;

    private final String userBaseDN;

    private final int userSearchScope;

    private final String userFromNameFilter;
    
    private final String userPrincipalAttribute;

    private final String userDisplayAttribute;
    
    private final String userForenameAttribute;
    
    private final String userSurnameAttribute;

    private final String groupBaseDN;

    private final int groupSearchScope;

    private final String groupFromNameFilter;
    
    private final String groupPrincipalAttribute; 
    
    private final String groupMemberOfAttribute;
    
    private final String groupRootPrincipal;


    public ActiveDirectoryServiceConfig(String host, int port, String principal, String credential,
            int connectionPoolSize, boolean cacheEnabled, int cacheSize,
            int cacheTTL, boolean followReferrals, boolean bindAnonymouslyOnReferrals,
            int resultsTimeLimit, int connectTimeout, int parallelConnectDelay,
            String userBaseDN, int userSearchScope, String userFromNameFilter,
            String userPrincipalAttribute, String userDisplayAttribute,
            String userForenameAttribute, String userSurnameAttribute,
            String groupBaseDN, int groupSearchScope, String groupFromNameFilter,
            String groupPrincipalAttribute, String groupMemberOfAttribute,
            String groupRootPrincipal) {
        System.out.println("ActiveDirectoryServiceConfig.constructor: host ="+host);
        if (host == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: host is null"); 
            throw new IllegalArgumentException("host: null");
        }
        System.out.println("ActiveDirectoryServiceConfig.constructor: principal ="+principal);
        if (principal == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: principal is null");
            throw new IllegalArgumentException("principal: null");
        }
        System.out.println("ActiveDirectoryServiceConfig.constructor: credential ="+credential);
        if (credential == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: credential is null");
            throw new IllegalArgumentException("credential: null");
        }
        System.out.println("ActiveDirectoryServiceConfig.constructor: userBaseDN ="+userBaseDN);
        if (userBaseDN == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: userBaseDN is null");
            throw new IllegalArgumentException("userBaseDN: null");
        }
        System.out.println("ActiveDirectoryServiceConfig.constructor: userDisplayAttribute ="+userDisplayAttribute);
        if (userDisplayAttribute == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: userDisplayAttribute is null");
            throw new IllegalArgumentException("userDisplayAttribute: null");
        }
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupBaseDN ="+groupBaseDN);
        if (groupBaseDN == null) {
            System.out.println("ActiveDirectoryServiceConfig.constructor: groupBaseDN is null");
            throw new IllegalArgumentException("groupBaseDN: null");
        }

        this.host = host;
        this.port = port;
        this.principal = principal;
        this.credential = credential;
        System.out.println("ActiveDirectoryServiceConfig.constructor: connectionPoolSize ="+connectionPoolSize);
        this.connectionPoolSize = connectionPoolSize;

        System.out.println("ActiveDirectoryServiceConfig.constructor: cacheEnabled ="+cacheEnabled);
        this.cacheEnabled = cacheEnabled;
        System.out.println("ActiveDirectoryServiceConfig.constructor: cacheSize ="+cacheSize);
        this.cacheSize = cacheSize;
        System.out.println("ActiveDirectoryServiceConfig.constructor: cacheTTL ="+cacheTTL);
        this.cacheTTL = cacheTTL;

        System.out.println("ActiveDirectoryServiceConfig.constructor: followReferrals ="+followReferrals);
        this.followReferrals = followReferrals;
        System.out.println("ActiveDirectoryServiceConfig.constructor: bindAnonymouslyOnReferrals ="+bindAnonymouslyOnReferrals);
        this.bindAnonymouslyOnReferrals = bindAnonymouslyOnReferrals;

        System.out.println("ActiveDirectoryServiceConfig.constructor: cacheSize ="+resultsTimeLimit);
        this.resultsTimeLimit = resultsTimeLimit;
        System.out.println("ActiveDirectoryServiceConfig.constructor: connectTimeout ="+connectTimeout);
        this.connectTimeout = connectTimeout;
        System.out.println("ActiveDirectoryServiceConfig.constructor: parallelConnectDelay ="+parallelConnectDelay);
        this.parallelConnectDelay = parallelConnectDelay;

        System.out.println("ActiveDirectoryServiceConfig.constructor: userBaseDN ="+userBaseDN);
        this.userBaseDN = userBaseDN;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userSearchScope ="+userSearchScope);
        this.userSearchScope = userSearchScope;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userDisplayAttribute ="+userDisplayAttribute);
        this.userDisplayAttribute = userDisplayAttribute;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userForenameAttribute ="+userForenameAttribute);
        this.userForenameAttribute = userForenameAttribute;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userSurnameAttribute ="+userSurnameAttribute);
        this.userSurnameAttribute = userSurnameAttribute;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userFromNameFilter ="+userFromNameFilter);
        this.userFromNameFilter = userFromNameFilter;
        System.out.println("ActiveDirectoryServiceConfig.constructor: userPrincipalAttribute ="+userPrincipalAttribute);
        this.userPrincipalAttribute = userPrincipalAttribute;

        System.out.println("ActiveDirectoryServiceConfig.constructor: groupBaseDN ="+groupBaseDN);
        this.groupBaseDN = groupBaseDN;
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupSearchScope ="+groupSearchScope);
        this.groupSearchScope = groupSearchScope;
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupFromNameFilter ="+groupFromNameFilter);
        this.groupFromNameFilter = groupFromNameFilter;
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupPrincipalAttribute ="+groupPrincipalAttribute);
        this.groupPrincipalAttribute = groupPrincipalAttribute;
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupMemberOfAttribute ="+groupMemberOfAttribute);
        this.groupMemberOfAttribute = groupMemberOfAttribute;
        System.out.println("ActiveDirectoryServiceConfig.constructor: groupRootPrincipal ="+groupRootPrincipal);
        this.groupRootPrincipal = groupRootPrincipal;
    }

    /**
     * @return Returns the bindAnonymouslyOnReferrals.
     */
    public boolean isBindAnonymouslyOnReferrals() {
        return bindAnonymouslyOnReferrals;
    }

    /**
     * @return Returns the displayNameAttribute.
     */
    public String getUserDisplayAttribute() {
        return userDisplayAttribute;
    }
    
    /**
     * @return Returns the userSurnameAttribute.
     */
    public String getUserSurnameAttribute() {
        return userSurnameAttribute;
    }
    
    /**
     * @return Returns the userForenameAttribute.
     */
    public String getUserForenameAttribute() {
        return userForenameAttribute;
    }
    
    /**
     * @return Returns the userPrincipalAttribute.
     */
    public String getUserPrincipalAttribute() {
        return userPrincipalAttribute;
    }

    /**
     * @return Returns the cacheEnabled.
     */
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    /**
     * @return Returns the cacheSize.
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * @return Returns the cacheTTL.
     */
    public int getCacheTTL() {
        return cacheTTL;
    }

    /**
     * @return Returns the connectTimeout.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @return Returns the credential.
     */
    public String getCredential() {
        return credential;
    }

    /**
     * @return Returns the followReferrals.
     */
    public boolean isFollowReferrals() {
        return followReferrals;
    }

    /**
     * @return Returns the groupFilterExtension.
     */
    public String getGroupFromNameFilter() {
        return groupFromNameFilter;
    }

    /**
     * @return Returns the groupSearchBase.
     */
    public String getGroupBaseDN() {
        return groupBaseDN;
    }

    /**
     * @return Returns the groupSearchScope.
     */
    public int getGroupSearchScope() {
        return groupSearchScope;
    }

    /**
     * @return Returns the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @return Returns the initialPoolSize.
     */
    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }


    /**
     * @return Returns the parallelConnectDelay.
     */
    public int getParallelConnectDelay() {
        return parallelConnectDelay;
    }

    /**
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return Returns the principal.
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * @return Returns the resultTimeLimit.
     */
    public int getResultsTimeLimit() {
        return resultsTimeLimit;
    }

    /**
     * @return Returns the userFilterExtension.
     */
    public String getUserFromNameFilter() {
        return userFromNameFilter;
    }

    /**
     * @return Returns the userSearchBase.
     */
    public String getUserBaseDN() {
        return userBaseDN;
    }

    /**
     * @return Returns the userSearchScope.
     */
    public int getUserSearchScope() {
        return userSearchScope;
    }



    /**
     * @return true if the object is a config and equal to this object
     */
    public boolean equals(Object object) {
        return (object instanceof ActiveDirectoryServiceConfig) && equals((ActiveDirectoryServiceConfig) object);
    }

    /**
     * @return true if the configurations are equal
     */

    public boolean equals(ActiveDirectoryServiceConfig config) {
        return config != null && host.equals(config.host) && port == config.port && principal.equals(config.principal)
                && credential.equals(config.credential) 
                && connectionPoolSize == config.connectionPoolSize
                && cacheEnabled == config.cacheEnabled && cacheSize == config.cacheSize && cacheTTL == config.cacheTTL
                && followReferrals == config.followReferrals
                && bindAnonymouslyOnReferrals == config.bindAnonymouslyOnReferrals && resultsTimeLimit == config.resultsTimeLimit
                && connectTimeout == config.connectTimeout && parallelConnectDelay == config.parallelConnectDelay
                && userBaseDN.equals(config.userBaseDN)
                && userSearchScope == config.userSearchScope && userFromNameFilter.equals(config.userFromNameFilter)
                && groupBaseDN.equals(config.groupBaseDN) && groupSearchScope == config.groupSearchScope
                && groupFromNameFilter.equals(config.groupFromNameFilter)
                && groupPrincipalAttribute.equals(config.groupPrincipalAttribute)
                && groupMemberOfAttribute.equals(config.groupMemberOfAttribute)
                && groupRootPrincipal.equals(config.groupRootPrincipal);
    }

    /**
     * Return a String representation of the configuration
     */
    public String toString() {
        return "ActiveDirectoryServiceConfig[\n    host: " + host + "\n    port: " + port + "\n    principal: "
                + principal + "\n    credential: " + formatCredential(credential)
                + "\n    connectionPoolSize: " + connectionPoolSize
                + "\n    cacheEnabled: " + cacheEnabled + "\n    cacheSize: " + cacheSize + "\n    cacheTTL: "
                + cacheTTL + "\n    followReferrals: " + followReferrals + "\n    bindAnonymouslyOnReferrals: "
                + bindAnonymouslyOnReferrals
                + "\n    resultsTimeLimit: " + resultsTimeLimit + "\n    connectTimeout: "
                + connectTimeout + "\n    parallelConnectDelay: " + parallelConnectDelay
                + "\n    userBaseDN: " + userBaseDN + "\n    userSearchScope: "
                + formatScope(userSearchScope) + "\n    userFromNameFilter: " + userFromNameFilter
                + "\n    userPrincipalAttribute: " + userPrincipalAttribute
                + "\n    userDisplayAttribute: " + userDisplayAttribute
                + "\n    userForenameAttribute: " + userForenameAttribute
                + "\n    userSurnameAttribute: " + userSurnameAttribute
                + "\n    groupBaseDN: " + groupBaseDN
                + "\n    groupSearchScope: " + formatScope(groupSearchScope) + "\n    groupFromNameFilter: "
                + groupFromNameFilter
                + "\n    groupPrincipalAttribute: " + groupPrincipalAttribute
                + "\n    groupMemberOfAttribute: " + groupMemberOfAttribute
                + "\n    groupRootPrincipal: " + groupRootPrincipal;
    }

    //
    // UTils
    //    

    /**
     * Convert the scope
     * 
     * @param a
     *            String describing the scope
     * @return the scope the scope value
     */
    public static int parseScope(String scope) {
        if ("onelevel".equalsIgnoreCase(scope)) {
            return LDAPv2.SCOPE_ONE;
        }
        if ("base".equalsIgnoreCase(scope)) {
            return LDAPv2.SCOPE_BASE;
        }
        return LDAPv2.SCOPE_SUB;
    }

    /**
     * Convert the scope
     * 
     * @param scope
     *            the scope value
     * @return a String describing the scope
     */
    private static String formatScope(int scope) {
        if (scope == LDAPv2.SCOPE_ONE) {
            return "onelevel";
        }
        if (scope == LDAPv2.SCOPE_BASE) {
            return "base";
        }
        return "subtree";
    }

    /**
     * Convert the credential to a string containing '*' char
     * 
     * @param credential
     *            the string to format
     * @return a String containg '*' chars
     */
    private static String formatCredential(String s) {
        if (s == null) {
            return null;
        }
        char[] buffer = new char[s.length()];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = '*';
        }
        return new String(buffer);
    }
}
