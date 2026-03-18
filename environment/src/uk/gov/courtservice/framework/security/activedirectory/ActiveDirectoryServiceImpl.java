package uk.gov.courtservice.framework.security.activedirectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import netscape.ldap.LDAPCache;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPControl;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPRebind;
import netscape.ldap.LDAPReferralException;
import netscape.ldap.LDAPSearchConstraints;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPTraceWriter;
import netscape.ldap.LDAPUrl;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPv3;
import netscape.ldap.util.ConnectionPool;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.security.ldap.LDAPConnectTimeoutSocketFactory;
import uk.gov.courtservice.framework.security.ldap.LDAPSearchResultsHandler;
import uk.gov.courtservice.framework.security.ldap.LDAPServerDomainScopeControl;
import uk.gov.courtservice.framework.security.ldap.LDAPSimplePagedResults;
import uk.gov.courtservice.framework.security.ldap.LDAPSimplePagedResultsControl;
import uk.gov.courtservice.framework.security.ldap.LDAPSimplePagedResultsResponse;

public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {
    private static final long serialVersionUID = 60043251143573019L;

    private static final Logger log = Logger.getLogger(ActiveDirectoryServiceImpl.class);

    private ActiveDirectoryServiceConfig config;

    private LDAPRebind ldapRebind;

    private LDAPCache ldapCache;

    private ConnectionPool connectionPool;

    /**
     * Create a new instance of the XhibitAuthenticationService
     */
    public ActiveDirectoryServiceImpl() {
        try {
            LDAPSimplePagedResults.register();
            log.debug("Registered control LDAPSimplePagedResults.");
        } catch (LDAPException ldape) {
            log.error("Could not register control LDAPSimplePagedResults.", ldape);
            throw new ActiveDirectoryException(ldape);
        }
    }

    /**
     * Initialize the new instance of the XhibitAuthenticationService
     * 
     * @param config
     *            the configuration
     */
    public void initialize(ActiveDirectoryServiceConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config: null");
        }

        // The order of initialisation is very important the mbean must be
        // set before the rebind and cache are created. The rebind and cache
        // must be set before the connection pool is created. Note the
        // rebind, cache and pool can all be set to null depending on the
        // been configuration.

        this.config = config;
        this.ldapRebind = createRebind();
        this.ldapCache = createCache();
        this.connectionPool = createConnectionPool();

        if (log.isDebugEnabled()) {
            log.debug("Initialized active directory service:- " + config);
        }
    }

    /**
     * Authenticate the user information from LDAP.
     */
    public boolean authenticateUser(ActiveDirectoryUser user, String userCredential) {
        LDAPConnection connection = openConnection();
        try {
            return authenticateUser(connection, user, userCredential);
        } finally {
            closeConnection(connection);
        }
    }

    private boolean authenticateUser(LDAPConnection connection, ActiveDirectoryUser user, String userCredential) {
        // Attempt to bind the connection to the user. If the attempt succeds
        // return true. If the attempt fails due to bad credentials return
        // false. If an error occures propogate. Once complete esential
        // connection is bound back to the configured user so can be reused.
        try {
            connection.bind(3, user.getDn(), userCredential);
            if (log.isDebugEnabled()) {
                log.debug("Bound user " + user.getPrincipalName() + ".");
            }
            return true;
        } catch (LDAPException ldape) {
            int ldapResultCode = ldape.getLDAPResultCode();
            if (ldapResultCode == LDAPException.INAPPROPRIATE_AUTHENTICATION
                    || ldapResultCode == LDAPException.INVALID_CREDENTIALS) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not bind user " + user.getPrincipalName() + ".", ldape);
                }
                return false;
            }
            log.error("An error occured binding user " + user.getPrincipalName() + ".", ldape);
            throw new ActiveDirectoryException(ldape);
        } finally {
            try {
                connection.bind(3, config.getPrincipal(), config.getCredential());
            } catch (LDAPException ldape) {
                log.error("An error occured rebinding admin user " + config.getPrincipal() + ".", ldape);
                throw new ActiveDirectoryException(ldape);
            }
        }
    }

    /**
     * Get the user information from LDAP.
     */
    public ActiveDirectoryUser getUser(String principalName) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryUser user = _getUser(principalName);
            if (user == null) {
                log.debug("Not finding user " + principalName + " took " + (System.currentTimeMillis() - starttime)
                        + "ms.");
            } else {
                log.debug("Finding user " + user + " took " + (System.currentTimeMillis() - starttime) + "ms.");
            }
            return user;
        }
        return _getUser(principalName);
    }

    private ActiveDirectoryUser _getUser(final String principalName) {
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            private ActiveDirectoryUser user;

            public void handleEntry(LDAPEntry entry) {
                user = new ActiveDirectoryUser(entry.getDN(), principalName, getStringValue(entry, config
                        .getUserDisplayAttribute()), getStringValue(entry, config.getUserForenameAttribute()),
                        getStringValue(entry, config.getUserSurnameAttribute()));
                //user = new ActiveDirectoryUser(entry.getDN(), principalName, getStringValue(entry, getStringValue(entry, config.getUserNameAttribute())));
            }

            public Object getValue() {
                return user;
            }
        };
        
        search(config.getUserBaseDN(), config.getUserSearchScope(), "(&(" + config.getUserPrincipalAttribute()
                + "=" + principalName + ")" + stripLeadingFilterData(config.getUserFromNameFilter()) + ")",
                new String[] { config.getUserDisplayAttribute(), config.getUserForenameAttribute(),
                        config.getUserSurnameAttribute() }, handler);
        
        //search(config.getUserBaseDN(), config.getUserSearchScope(), "(&(samAccountName=)" + config.getUserFromNameFilter() + ")",
        //        new String[] { config.getUserNameAttribute() }, handler);

        return (ActiveDirectoryUser) handler.getValue();
    }
    
    /**
     * Needed in WL10.3 due to mismatch in configuration in XHIBIT Authenticator in WebLogic
     * and what is required for a valid search to work.
     * 
     * @param filter
     * @return
     */
    private String stripLeadingFilterData(String filter) {
       String returnFilter = "";
       
       if (filter.toLowerCase().startsWith("(&(samaccountName=%u)(objectclass=user))")) {
           StringBuffer sb = new StringBuffer();
           sb.append("(objectclass=user)");
           sb.append(filter.substring(40));
           returnFilter = sb.toString();
       }
       
       return returnFilter;
    }

    /**
     * List the valid set of controls for the server
     */
    public String[] getSupportedControls() {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            String[] controls = _getSupportedControls();
            log.debug("Getting controls " + formatControlsDebug(controls) + " took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            return controls;
        }
        return _getSupportedControls();
    }

    private String[] _getSupportedControls() {
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            private String[] controls;

            public void handleEntry(LDAPEntry entry) {
                controls = getStringValueArray(entry, "supportedControl");
            }

            public Object getValue() {
                return controls == null ? new String[0] : controls;
            }
        };

        search("", LDAPv2.SCOPE_BASE, null, new String[] { "supportedControl" }, handler);

        return (String[]) handler.getValue();
    }

    /**
     * Get the groups the user is a member of
     * 
     * @param user
     * @return the groups
     */
    public ActiveDirectoryGroup[] getGroups(ActiveDirectoryUser user) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryGroup[] groups = _getGroups(user);
            log.debug("Getting groups " + formatGroupsDebug(groups) + " for user " + user.getPrincipalName() + " took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            return groups;
        }
        return _getGroups(user);
    }

    private ActiveDirectoryGroup[] _getGroups(ActiveDirectoryUser user) {
        List groupList = new ArrayList();

        byte[][] groupSids = getGroupSids(user);
        for (int i = 0; i < groupSids.length; i++) {
            ActiveDirectoryGroup group = getGroup(groupSids[i]);
            if (group != null) {
                groupList.add(group);
            } else {
                log.warn("User " + user.getPrincipalName() + " tokenGroups contains sid " + formatSid(groupSids[i])
                        + " which could not be resolved");
            }
        }

        return (ActiveDirectoryGroup[]) groupList.toArray(new ActiveDirectoryGroup[groupList.size()]);
    }

    private byte[][] getGroupSids(ActiveDirectoryUser user) {
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            byte[][] groupSids;

            public void handleEntry(LDAPEntry entry) {
                groupSids = getByteValueArray(entry, "tokenGroups");
            }

            public Object getValue() {
                return groupSids;
            }
        };

        search(user.getDn(), LDAPv2.SCOPE_BASE, null, new String[] { "tokenGroups" }, handler);

        return (byte[][]) handler.getValue();
    }

    private ActiveDirectoryGroup getGroup(byte[] groupSid) {
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            private ActiveDirectoryGroup group = null;

            public void handleEntry(LDAPEntry entry) {
                /*String groupPrincipal = getStringValue(entry, config.getGroupPrincipalAttribute());
                if (groupPrincipal != null) {
                    group = new ActiveDirectoryGroup(entry.getDN(), groupPrincipal);
                } else {
                    log.warn("Ignoring incomplete group entry dn: " + entry.getDN() + " groupPrincipal: "
                            + groupPrincipal + ".");
                }*/
            }

            public Object getValue() {
                return group;
            }
        };

        search(config.getGroupBaseDN(), config.getGroupSearchScope(), "(&(objectSid=" + formatSid(groupSid) + ")"
                + config.getGroupFromNameFilter() + ")", null, handler);

        return (ActiveDirectoryGroup) handler.getValue();
    }

    /**
     * Get the active directory group root.
     */
    public ActiveDirectoryGroup getRootGroup() {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryGroup root = _getRootGroup();
            log.debug("Getting group root " + root + " took " + (System.currentTimeMillis() - starttime) + "ms.");
            return root;
        }
        return _getRootGroup();
    }

    private ActiveDirectoryGroup _getRootGroup() {
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            private ActiveDirectoryGroup group = null;

            public void handleEntry(LDAPEntry entry) {
                String groupPrincipal = getStringValue(entry, "cn");
                if (groupPrincipal != null) {
                    group = new ActiveDirectoryGroup(entry.getDN(), groupPrincipal);
                } else {
                    log.warn("Ignoring incomplete group entry dn: " + entry.getDN() + " groupPrincipal: "
                            + groupPrincipal + ".");
                }
            }

            public Object getValue() {
                return group;
            }
        };

        search(config.getGroupBaseDN(), config.getGroupSearchScope(), "(&" + config.getGroupFromNameFilter() + ")", null, handler);

        return (ActiveDirectoryGroup) handler.getValue();
    }

    /**
     * Get the active directory group hierarchy.
     */
    public ActiveDirectoryGroupHierarchy getGroupHierarchy() {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryGroupHierarchy hierarchy = _getGroupHierarchy();
            log.debug("Getting group hierarchy " + hierarchy + " took " + (System.currentTimeMillis() - starttime)
                    + "ms.");
            return hierarchy;
        }
        return _getGroupHierarchy();
    }

    private ActiveDirectoryGroupHierarchy _getGroupHierarchy() {
        // Create the handler
        LDAPSearchResultsHandler handler = new LDAPSearchResultsHandler() {
            private final Map groupMap = new HashMap();

            private final Map parentDnsMap = new HashMap();

            // Store the information, we can only build hierarchy once all
            // results mapped.
            public void handleEntry(LDAPEntry entry) {
                String dn = entry.getDN();
                String groupPrincipal = getStringValue(entry, "cn");
                if (groupPrincipal != null) {
                    String[] parentDns = getStringValueArray(entry, "memberOf");
                    parentDnsMap.put(dn, parentDns);
                    groupMap.put(dn, new ActiveDirectoryGroup(dn, groupPrincipal));
                } else {
                    log.warn("Ignoring incomplete group entry dn: " + entry.getDN() + " groupPrincipal: "
                            + groupPrincipal + ".");
                }
            }

            // Process group data to build build hierarchy.
            public Object getValue() {
                ActiveDirectoryGroupHierarchy hierarchy = new ActiveDirectoryGroupHierarchy();

                Iterator childDns = groupMap.keySet().iterator();
                while (childDns.hasNext()) {
                    String childDn = (String) childDns.next();
                    ActiveDirectoryGroup child = (ActiveDirectoryGroup) groupMap.get(childDn);
                    if (child != null) {
                        String[] parentDns = (String[]) parentDnsMap.get(childDn);
                        for (int i = 0; i < parentDns.length; i++) {
                            ActiveDirectoryGroup parent = (ActiveDirectoryGroup) groupMap.get(parentDns[i]);
                            if (parent != null) {
                                hierarchy.addRelationship(parent, child);
                            } else {
                                log.warn("Could not find parent " + parentDns[i] + " when building group hierarchy.");
                            }
                        }
                    } else {
                        log.warn("Could not find child " + childDn + " when building group hierarchy.");
                    }
                }

                return hierarchy;
            }
        };

        // Perform the seach, use paging as result set could be large
        pagedSearch(config.getGroupBaseDN(), config.getGroupSearchScope(), config.getGroupFromNameFilter(),
                null, handler);

        // Extract the value
        return (ActiveDirectoryGroupHierarchy) handler.getValue();
    }


    //
    // Search Management
    //
    private void search(String dn, int scope, String filter, String[] attributes, LDAPSearchResultsHandler handler) {
        try {
            LDAPConnection connection = openConnection();
            try {
                search(connection, dn, scope, filter, attributes, handler);
            } finally {
                closeConnection(connection);
            }
        } catch (LDAPException ldape) {
            throw new ActiveDirectoryException(ldape);
        }
    }

    private void search(LDAPConnection connection, String dn, int scope, String filter, String[] attributes,
            LDAPSearchResultsHandler handler) throws LDAPException {
        // Search
        LDAPSearchResults results = connection.search(dn, scope, filter, attributes, false);

        // Process the results
        handleSearchResults(results, handler);
    }

    private void pagedSearch(String dn, int scope, String filter, String[] attributes, LDAPSearchResultsHandler handler) {
        try {
            LDAPConnection connection = openConnection();
            try {
                pagedSearch(connection, dn, scope, filter, attributes, handler);
            } finally {
                closeConnection(connection);
            }
        } catch (LDAPException ldape) {
            throw new ActiveDirectoryException(ldape);
        }
    }

    private void pagedSearch(LDAPConnection connection, String dn, int scope, String filter, String[] attributes,
            LDAPSearchResultsHandler handler) throws LDAPException {
        // Search using SimplePaging
        byte[] cookie = null;
        do {
            // Search
            LDAPSearchResults results = connection.search(dn, scope, filter, attributes, false, getPagingConstraints(
                    connection, cookie));
            // Process the results
            handleSearchResults(results, handler);

            // Get Paging Cookie
            cookie = getPagingCookie(results);
        } while (cookie != null);
    }

    private LDAPSearchConstraints getPagingConstraints(LDAPConnection connection, byte[] cookie) {
        // Note the following clones the default constraints
        LDAPSearchConstraints constraints = connection.getSearchConstraints();

        // Hardcoded page size to be 1000 for WL10.3
        LDAPControl pagedResultControl = new LDAPSimplePagedResultsControl(1000, cookie);

        // Add the new control to the current controls if any.
        LDAPControl[] currentControls = constraints.getServerControls();
        if (currentControls == null || currentControls.length == 0) {
            constraints.setServerControls(pagedResultControl);
        } else {
            LDAPControl[] newControls = new LDAPControl[currentControls.length + 1];
            newControls[0] = pagedResultControl;
            System.arraycopy(currentControls, 0, newControls, 1, currentControls.length);
            constraints.setServerControls(newControls);
        }

        return constraints;
    }

    private byte[] getPagingCookie(LDAPSearchResults results) {
        LDAPControl[] controls = results.getResponseControls();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i] instanceof LDAPSimplePagedResultsResponse) {
                return ((LDAPSimplePagedResultsResponse) controls[i]).getCookie();
            }
        }
        return null;
    }

    private void handleSearchResults(LDAPSearchResults results, LDAPSearchResultsHandler handler) throws LDAPException {
        while (results.hasMoreElements()) {
            Object result = results.nextElement();
            if (result instanceof LDAPEntry) {
                handler.handleEntry((LDAPEntry) result);
            } else if (result instanceof LDAPReferralException) {
                // If following referals an error has occured
                if (ldapRebind != null) {
                    throw (LDAPReferralException) result;
                }
                // Otherwise log to show we are ignoring
                if (log.isDebugEnabled()) {
                    log.debug("Ignoring referal to urls "
                            + formatLDAPUrlDebug(((LDAPReferralException) result).getURLs()) + ".");
                }
            } else if (result instanceof LDAPException) {
                throw (LDAPException) result;
            } else {
                if (log.isEnabledFor(Level.WARN)) {
                    log.warn("Ignoring unregonised result: " + result);
                }
            }
        }
    }

    //
    // Connection Management
    //    

    private LDAPConnection openConnection() {
        if (connectionPool != null) {
            return connectionPool.getConnection();
        } else {
            return createConnection();
        }
    }

    private void closeConnection(LDAPConnection connection) {
        try {
            if (connection != null) {
                if (connectionPool != null) {
                    connectionPool.close(connection);
                } else {
                    connection.disconnect();
                }
            }
        } catch (LDAPException ldape) {
            if (log.isEnabledFor(Level.WARN)) {
                log.warn("An error occured closing ldap connection", ldape);
            }
        }
    }

    //
    // Factory Methods
    //  

    private ConnectionPool createConnectionPool() {
        try {
            // Default initialsize to 1 as not available in WL10.3
            ConnectionPool pool = new ConnectionPool(1, config.getConnectionPoolSize(), createConnection());
            pool.setDebug(log.isDebugEnabled());
            return pool;
        } catch (LDAPException ldape) {
            throw new ActiveDirectoryException(ldape);
        }
    }

    private LDAPConnection createConnection() {
        try {
            LDAPConnection connection = new LDAPConnection();

            // Enable Debug
            if (log.isDebugEnabled()) {
                connection.setProperty(LDAPConnection.TRACE_PROPERTY, new LDAPTraceWriter() {
                    public void write(String message) {
                        log.debug(message);
                    }
                });
                connection.setProperty("debug", "true");
            }

            // Size Limit
            connection.setOption(LDAPv2.SIZELIMIT, new Integer(1000));

            // Batch Size
            connection.setOption(LDAPv2.BATCHSIZE, new Integer(1));

            // Time Limit
            connection.setOption(LDAPv2.TIMELIMIT, new Integer(config.getResultsTimeLimit() * 1000));

            // Connect Timeout
            connection.setConnectTimeout(config.getConnectTimeout());

            // Parallel Connect Delay
            connection.setConnSetupDelay(config.getParallelConnectDelay());

            // Socket Timeout - Not applicable in WL10.3
            //connection.setSocketFactory(new LDAPConnectTimeoutSocketFactory(config.getSocketTimeout() * 1000));

            if (ldapCache != null) {
                // Use cache if optional cache provided
                connection.setCache(ldapCache);
            }

            if (ldapRebind != null) {
                // Automatically follow referrals if optional rebind provided
                connection.setOption(LDAPv2.REFERRALS, Boolean.TRUE);
                connection.setOption(LDAPv2.REFERRALS_REBIND_PROC, ldapRebind);
            } else {
                // Add a non critical server control to request active directory
                // not generate referals
                connection.setOption(LDAPv3.SERVERCONTROLS, new LDAPServerDomainScopeControl(false));
            }

            // Connect and Authorize
            connect(connection, config.getHost(), config.getPort());
            bind(connection, config.getPrincipal(), config.getCredential());

            return connection;
        } catch (LDAPException ldape) {
            throw new ActiveDirectoryException(ldape);
        }
    }

    private static void connect(LDAPConnection connection, String host, int port) throws LDAPException {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            _connect(connection, host, port);
            log.debug("Connect took " + (System.currentTimeMillis() - starttime) + "ms.");
        } else {
            _connect(connection, host, port);
        }
    }

    private static void _connect(LDAPConnection connection, String host, int port) throws LDAPException {
        connection.connect(host, port);
    }

    private static void bind(LDAPConnection connection, String principal, String credential) throws LDAPException {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            _bind(connection, principal, credential);
            log.debug("Bind took " + (System.currentTimeMillis() - starttime) + "ms.");
        } else {
            _bind(connection, principal, credential);
        }
    }

    private static void _bind(LDAPConnection connection, String principal, String credential) throws LDAPException {
        connection.bind(3, principal, credential);
    }

    private LDAPRebind createRebind() {
        if (config.isFollowReferrals()) {
            if (config.isBindAnonymouslyOnReferrals()) {
                return new ActiveDirectoryLDAPRebind();
            }
            return new ActiveDirectoryLDAPRebind(config.getPrincipal(), config.getCredential());
        }
        return null;
    }

    private LDAPCache createCache() {
        if (config.isCacheEnabled()) {
            return new LDAPCache(config.getCacheSize(), config.getCacheTTL());
        }
        return null;
    }

    //
    // Active Directory Utilities
    //

    private static final char[] DIGITS = "0123456789ABCDEF".toCharArray();

    private static final String formatSid(byte[] sid) {
        char[] buffer = new char[sid.length * 3];
        for (int readIndex = 0, writeIndex = 0; readIndex < sid.length; readIndex++) {
            buffer[writeIndex++] = '\\';
            buffer[writeIndex++] = DIGITS[(sid[readIndex] >> 4) & 0xf];
            buffer[writeIndex++] = DIGITS[sid[readIndex] & 0xf];
        }
        return new String(buffer);
    }

    private static final String formatGroupsDebug(ActiveDirectoryGroup[] groups) {
        if (groups != null) {
            if (0 < groups.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("{");
                buffer.append(groups[0]);
                for (int i = 1; i < groups.length; i++) {
                    buffer.append(",");
                    buffer.append(groups[i]);
                }
                buffer.append("}");
                return buffer.toString();
            }
            return "{}";
        }
        return "null";
    }

    private static final String formatControlsDebug(String[] controls) {
        if (controls != null) {
            if (0 < controls.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("{");
                buffer.append(controls[0]);
                for (int i = 1; i < controls.length; i++) {
                    buffer.append(",");
                    buffer.append(controls[i]);
                }
                buffer.append("}");
                return buffer.toString();
            }
            return "{}";
        }
        return "null";
    }

    private static String formatLDAPUrlDebug(LDAPUrl[] urls) {
        if (urls == null) {
            return "null";
        }
        if (urls.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append(urls[0]);

        for (int i = 1; i < urls.length; i++) {
            builder.append(",");
            builder.append(urls[i]);
        }

        return builder.toString();
    }

}

// 130.177.1.35 389 weblogic ********
