package uk.gov.courtservice.framework.security.activedirectory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class ActiveDirectoryServiceStub implements ActiveDirectoryService {
    private static final long serialVersionUID = 60043251143573019L;

    private static final Logger log = Logger.getLogger(ActiveDirectoryServiceStub.class);

    private ActiveDirectoryGroupHierarchy groupHierarchy;

    // ActiveDirectoryGroup indexed by dn
    private Map groupMap;

    // ActiveDirectoryUser indexed by principal
    private Map userMap;

    // String indexed by ActiveDirectoryUser
    private Map userCredentialMap;

    // ActiveDirectoryGroup[] indexed by ActiveDirectoryUser
    private Map userGroupMap;

    // ActiveDirectoryTerminal indexed by name
    private Map terminalMap;

    public ActiveDirectoryServiceStub() {
        // Requires Default constructor
    }

    /**
     * Initialize the new instance of the XhibitAuthenticationService. This
     * ignores the config and instead loads the values from the resource
     * specified with the sytem property
     * uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceStub.properties.
     * If not specified the contents of ActiveDirectoryServiceStub.properties is
     * used.
     * 
     * @param config
     *            the configuration not used by this implementation
     */
    public void initialize(ActiveDirectoryServiceConfig config) {
        initialize(System.getProperty(
                "uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceStub.properties",
                "ActiveDirectoryServiceStub.properties"));
    }

    /**
     * Initialize the new instance of the XhibitAuthenticationService
     * 
     * @param config
     *            the configuration
     */
    private void initialize(String resourceName) {
        Properties properties = loadProperties(resourceName);
        // Initializes groupMap and groupHierarchy
        initializeGroups(properties);
        // Initializes userMap, userCredentialMap and userGroupMap, requires
        // groupMap and groupHierarchy
        initializeUsers(properties);
        // Initializes terminalMap
        initializeTerminals(properties);
    }

    private void initializeGroups(Properties properties) {
        // Stage The Data, need all groups created before making relationships
        groupMap = new HashMap();

        Map parentDnsMap = new HashMap();
        String[] groups = getStringsProperty(properties, "groups");
        for (int i = 0; i < groups.length; i++) {
            String principal = properties.getProperty("group." + groups[i] + ".principal");
            if (principal != null) {
                groupMap.put(groups[i], new ActiveDirectoryGroup(groups[i], principal));
                String[] memberOf = getStringsProperty(properties, "group." + groups[i] + ".memberOf");
                parentDnsMap.put(groups[i], memberOf);
            }
        }

        // Create The Hierarchy
        groupHierarchy = new ActiveDirectoryGroupHierarchy();

        Iterator groupDns = groupMap.keySet().iterator();
        while (groupDns.hasNext()) {
            Object groupDn = groupDns.next();
            ActiveDirectoryGroup group = (ActiveDirectoryGroup) groupMap.get(groupDn);
            String[] parentDns = (String[]) parentDnsMap.get(groupDn);
            for (int i = 0; i < parentDns.length; i++) {
                ActiveDirectoryGroup parent = (ActiveDirectoryGroup) groupMap.get(parentDns[i]);
                groupHierarchy.addRelationship(parent, group);
            }
        }
    }

    private void initializeUsers(Properties properties) {
        userMap = new HashMap();
        userGroupMap = new HashMap();
        userCredentialMap = new HashMap();
        String[] users = getStringsProperty(properties, "users");
        for (int i = 0; i < users.length; i++) {
            String principal = properties.getProperty("user." + users[i] + ".principal");
            String displayName = properties.getProperty("user." + users[i] + ".displayName");
            String givenName = properties.getProperty("user." + users[i] + ".givenName");
            String sn = properties.getProperty("user." + users[i] + ".sn");

            if (principal != null) {
                // Create User
                ActiveDirectoryUser user = new ActiveDirectoryUser(users[i], principal, displayName, givenName, sn);
                userMap.put(principal, user);
                // Store Credential
                String credential = properties.getProperty("user." + users[i] + ".credential");
                userCredentialMap.put(user, credential);

                // Store Groups
                Set memberOfSet = new HashSet();
                String[] memberOfs = getStringsProperty(properties, "user." + users[i] + ".memberOf");
                for (int j = 0; j < memberOfs.length; j++) {
                    ActiveDirectoryGroup group = (ActiveDirectoryGroup) groupMap.get(memberOfs[j]);
                    System.out.println(memberOfs[j] + ": " + group);

                    if (group != null) {
                        addGroup(memberOfSet, group);
                    }
                }
                List memberOfList = new ArrayList();
                memberOfList.addAll(memberOfSet);
                Collections.sort(memberOfList);
                userGroupMap.put(user, memberOfList.toArray(new ActiveDirectoryGroup[memberOfList.size()]));
            }
        }
    }

    private void addGroup(Set groupSet, ActiveDirectoryGroup group) {
        groupSet.add(group);
        ActiveDirectoryGroup[] parents = groupHierarchy.getParents(group);
        for (int i = 0; i < parents.length; i++) {
            addGroup(groupSet, parents[i]);
        }
    }

    private void initializeTerminals(Properties properties) {
        terminalMap = new HashMap();
        String[] terminals = getStringsProperty(properties, "terminals");
        for (int i = 0; i < terminals.length; i++) {
            String name = properties.getProperty("terminal." + terminals[i] + ".name");
            String location = properties.getProperty("terminal." + terminals[i] + ".location");
            terminalMap.put(name, new ActiveDirectoryTerminal(terminals[i], name, location));
        }
    }

    private String[] getStringsProperty(Properties properties, String name) {
        List valueList = new ArrayList();
        String property = properties.getProperty(name);
        if (property != null) {
            StringTokenizer tokenizer = new StringTokenizer(property, ",");
            while (tokenizer.hasMoreTokens()) {
                String value = tokenizer.nextToken().trim();
                if (value.length() > 0) {
                    valueList.add(value);
                }
            }
        }
        return (String[]) valueList.toArray(new String[valueList.size()]);
    }

    private Properties loadProperties(String resourceName) {
        InputStream in = ActiveDirectoryServiceStub.class.getClassLoader().getResourceAsStream(resourceName);
        if (in != null) {
            try {
                Properties properties = new Properties();
                properties.load(in);
                return properties;
            } catch (IOException ioe) {
                throw new ActiveDirectoryException(ioe);
            } finally {
                try {
                    in.close();
                } catch (IOException ioe) {
                    log.warn("An error occured closing the input stream.", ioe);
                }
            }
        }
        throw new ActiveDirectoryException("Resource " + resourceName + " not found.");
    }

    /**
     * Authenticate the user information from LDAP.
     */
    public boolean authenticateUser(ActiveDirectoryUser user, String userCredential) {
        String actualCredential = (String) userCredentialMap.get(user);
        return actualCredential == null || actualCredential.equals(userCredential);
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
        return (ActiveDirectoryUser) userMap.get(principalName);
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
        return new String[0];
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

    private ActiveDirectoryGroup[] _getGroups(final ActiveDirectoryUser user) {
        ActiveDirectoryGroup[] groups = (ActiveDirectoryGroup[]) userGroupMap.get(user);
        if (groups != null) {
            return groups;
        }
        return new ActiveDirectoryGroup[0];
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
        // Use the first root!
        ActiveDirectoryGroup[] roots = groupHierarchy.getRoots();
        if (roots.length > 0) {
            return roots[0];
        }
        return null;
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
        return groupHierarchy;
    }

    /**
     * Get the terminals from the active directory
     */
    public ActiveDirectoryTerminal[] getTerminals() {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryTerminal[] terminals = _getTerminals();
            log.debug("Getting " + terminals.length + " terminals took " + (System.currentTimeMillis() - starttime)
                    + "ms.");
            return terminals;
        }
        return _getTerminals();
    }

    private ActiveDirectoryTerminal[] _getTerminals() {
        List terminalList = new ArrayList();
        terminalList.addAll(terminalMap.values());
        Collections.sort(terminalList);
        return (ActiveDirectoryTerminal[]) terminalList.toArray(new ActiveDirectoryTerminal[terminalList.size()]);
    }

    /**
     * Get the terminals from the active directory
     */
    public ActiveDirectoryTerminal[] getTerminalsByName(String terminalName) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryTerminal[] terminals = _getTerminalsByName(terminalName);
            if (terminals.length == 0) {
                log.debug("Getting " + terminals.length + " terminals took " + (System.currentTimeMillis() - starttime)
                        + "ms.");
            } else if (terminals.length == 1) {
                log.debug("Getting terminal " + terminalName + " took " + (System.currentTimeMillis() - starttime)
                        + "ms.");
            } else {
                log.debug("Getting terminal " + terminalName + " returned " + terminals.length + " terminals and took "
                        + (System.currentTimeMillis() - starttime) + "ms.");
            }
            return terminals;
        }
        return _getTerminalsByName(terminalName);
    }

    private ActiveDirectoryTerminal[] _getTerminalsByName(String terminalName) {
        ActiveDirectoryTerminal terminal = (ActiveDirectoryTerminal) terminalMap.get(terminalName);
        if (terminal != null) {
            return new ActiveDirectoryTerminal[] { terminal };
        }
        return new ActiveDirectoryTerminal[0];
    }

    /**
     * Get the terminals from the active directory
     */
    public ActiveDirectoryTerminal[] getTerminalsByCourt(String courtShortName) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            ActiveDirectoryTerminal[] terminals = _getTerminalsByCourt(courtShortName);
            log.debug("Getting " + terminals.length + " terminals for court " + courtShortName + " took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            return terminals;
        }
        return _getTerminalsByCourt(courtShortName);
    }

    private ActiveDirectoryTerminal[] _getTerminalsByCourt(String courtShortName) {
        List terminalList = new ArrayList();
        Iterator terminals = terminalMap.values().iterator();
        while (terminals.hasNext()) {
            ActiveDirectoryTerminal terminal = (ActiveDirectoryTerminal) terminals.next();
            if (terminal.getLocation().startsWith("/" + courtShortName + "/")) {
                terminalList.add(terminal);
            }
        }
        Collections.sort(terminalList);
        return (ActiveDirectoryTerminal[]) terminalList.toArray(new ActiveDirectoryTerminal[terminalList.size()]);
    }

    //
    // Active Directory Utilities
    //

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

}

// 130.177.1.35 389 weblogic ********
