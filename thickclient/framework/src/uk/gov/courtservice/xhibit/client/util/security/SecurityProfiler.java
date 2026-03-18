package uk.gov.courtservice.xhibit.client.util.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_role.XhbSecurityRoleBasicValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.rolemapping.exceptions.RoleMappingException;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.RoleMappingCompositeValue;

//import uk.gov.courtservice.xhibit.client.actions.FunctionList;

/**
 * <p>
 * Title: Security Profiler
 * </p>
 * <p>
 * Description: Utility Class for profiling client security
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2005)
 * @version 1.0
 */

public class SecurityProfiler {
    private static final Logger log = CSServices.getLogger(SecurityProfiler.class);

    /**
     * Stop unnecesary construction
     */
    private SecurityProfiler() {
        // Change Access Permisions
    }

    // Used in logging below
    private static final String NL = System.getProperty("line.seperator", "\n");

    private static final String BLANK = " ";

    private static final int READ_ACTION = 1;

    private static final int EDIT_ACTION = 2;

    /**
     * Return true if the scheduler servlet has been enabled by setting the
     * system property
     * 
     */
    public static boolean enabled() {
        String property = System.getProperty("enableSecurityProfiler");
        return property != null && property.equalsIgnoreCase("TRUE");
    }

    public static void profile() {
        profileRoleActionGroup();
        profileGroupRoleAction();
    }

    /**
     * Log the functionalities with out mappings to groups and the unrecognised
     * functionalities.
     */
    public static void profileRoleActionGroup() {
        if (XhibitSingleton.getInstance().getUserSession().hasAccess("XHBSecurityAdmin")) {
            try {
                RoleMappingCompositeValue roleMappingCompositeValue = getRoleMapping();
                FunctionList[] functions = SecurityHelper.getFunctions();

                String[] roleNames = getRoleNames(roleMappingCompositeValue, functions);

                for (int i = 0; i < roleNames.length; i++) {
                    logFunction(roleMappingCompositeValue, functions, roleNames[i]);
                }
            } catch (RoleMappingException e) {
                log.warn("Could not log functions problem accessing DB.", e);
            }
        } else {
            log.warn("Could not log functions as insufficient permisions.");
        }
    }

    /**
     * Outputs to console all the Read and Edit Actions for each Role assigned
     * to the each group available Grouping by 'Group(Actor), Role(Function),
     * Action'
     * 
     */
    public static void profileGroupRoleAction() {
        log.debug("profile:START");
        if (XhibitSingleton.getInstance().getUserSession().hasAccess("XHBSecurityAdmin")) {
            try {
                log.debug("profile:User has Access");

                RoleMappingCompositeValue roleMappingCompositeValue = getRoleMapping();
                log.debug("profile: RoleMappingCompositeValue retrieved");

                FunctionList[] functions = SecurityHelper.getFunctions();
                log.debug("profile: FunctionList[] retrieved");

                String[] groupNames = getGroupNames(roleMappingCompositeValue);
                log.debug("profile: groupNames retrieved");

                if (groupNames != null) {
                    log.debug("profile: groupNames.length: " + groupNames.length);

                    processReadActions(functions, groupNames, roleMappingCompositeValue);
                    processEditActions(functions, groupNames, roleMappingCompositeValue);
                }
            } catch (RoleMappingException e) {
                log.warn("Could not log functions problem accessing DB.", e);
            }
        } else {
            log.warn("Could not log functions as insufficient permisions.");
        }
    }

    // Processes all Read actions belonging to each Role and Group
    private static void processReadActions(FunctionList[] functions, String[] groupNames,
            RoleMappingCompositeValue roleMappingCompositeValue) {
        log.debug("Processing Read Actions");
        processActions(functions, groupNames, roleMappingCompositeValue, READ_ACTION);
    }

    // Processes all Edit actions belonging to each Role and Group
    private static void processEditActions(FunctionList[] functions, String[] groupNames,
            RoleMappingCompositeValue roleMappingCompositeValue) {
        log.debug("Processing Edit Actions");
        processActions(functions, groupNames, roleMappingCompositeValue, EDIT_ACTION);
    }

    // Uses groupNames, functions and roleMappingCompositeValue to detemine
    // actions for each role assigned to each group
    private static void processActions(FunctionList[] functions, String[] groupNames,
            RoleMappingCompositeValue roleMappingCompositeValue, int action) {
        StringBuffer buffer = new StringBuffer();
        appendDebug(buffer, "Group", "Role", "Action");
        for (int i = 0; i < groupNames.length; i++) {
            String[] groupRoles = getGroupRoles(roleMappingCompositeValue, groupNames[i]);
            if (groupRoles != null) {
                for (int m = 0; m < groupRoles.length; m++) {
                    // for each role, need to get list of read actions and
                    // print
                    FunctionList function = getFunction(functions, groupRoles[m]);

                    if (function != null) {
                        String[] actions = getActions(function, action);
                        if (actions != null) {
                            for (int n = 0; n < actions.length; n++) {
                                // Group, Role, Action
                                appendDebug(buffer, groupNames[i], groupRoles[m], actions[n]);
                            }
                        } else {
                            log.debug("Not a read or edit action");
                            return;
                        }
                    } else {
                        // Group, Role, No Action exists
                        appendDebug(buffer, groupNames[i], groupRoles[m], BLANK);
                    }
                }
            } else {
                // Group, No Role exist, No Action exists.
                log.debug("profile: groupRoles is null");
                appendDebug(buffer, groupNames[i], BLANK, BLANK);
            }
        }
        // output results of processing
        log.debug(buffer);
    }

    private static String[] getActions(FunctionList function, int action) {
        if (action == READ_ACTION) {
            return SecurityHelper.getReadActions(function);
        } else if (action == EDIT_ACTION) {
            return SecurityHelper.getEditActions(function);
        }
        return null;
    }

    private static String[] getRoleNames(RoleMappingCompositeValue roleMappingCompositeValue, FunctionList[] functions) {
        // Get all the role names from the functions and role objects
        Set roleNameSet = new HashSet();
        for (int i = 0; i < functions.length; i++) {
            roleNameSet.add(functions[i].toString());
        }
        XhbSecurityRoleBasicValue[] roles = roleMappingCompositeValue.getSecurityRoles();
        for (int i = 0; i < roles.length; i++) {
            roleNameSet.add(roles[i].getRoleName());
        }

        // Sort the role names
        List roleNameList = new ArrayList();
        roleNameList.addAll(roleNameSet);
        Collections.sort(roleNameList);

        // Return the role names
        return (String[]) roleNameList.toArray(new String[roleNameList.size()]);
    }

    private static void logFunction(RoleMappingCompositeValue roleMappingCompositeValue, FunctionList[] functions,
            String roleName) {
        // Get the client Data
        FunctionList function = getFunction(functions, roleName);
        String[] readActions;
        String[] editActions;
        if (function != null) {
            readActions = SecurityHelper.getReadActions(function);
            editActions = SecurityHelper.getEditActions(function);
        } else {
            readActions = null;
            editActions = null;
        }

        // Get the DB data

        XhbSecurityRoleBasicValue role = getRole(roleMappingCompositeValue, roleName);
        XhbSecurityGroupRoleBasicValue[] groups;
        if (role != null) {
            groups = getGroups(roleMappingCompositeValue, role);
        } else {
            groups = null;
        }

        // Log the data
        if (function == null || role == null || groups == null || groups.length == 0) {
            if (log.isEnabledFor(Level.WARN)) {
                log.warn(toDebug(roleName, function, readActions, editActions, role, groups));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(toDebug(roleName, function, readActions, editActions, role, groups));
            }

        }
    }

    private static void appendDebug(StringBuffer buffer, String groupName, String groupRole, String action) {
        buffer.append(groupName);
        buffer.append(",");
        buffer.append(groupRole);
        buffer.append(",");
        buffer.append(action);
        buffer.append(NL);
    }

    private static String toDebug(String roleName, FunctionList function, String[] readActions, String[] editActions,
            XhbSecurityRoleBasicValue role, XhbSecurityGroupRoleBasicValue[] groups) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Role ");
        buffer.append(roleName);

        if (function != null) {
            if (role != null) {
                buffer.append(" DB and Client");
                buffer.append(" {");
                buffer.append(NL);
                buffer.append("     readActions=");
                appendDebug(buffer, readActions);
                buffer.append(NL);
                buffer.append("     editActions=");
                appendDebug(buffer, editActions);
                buffer.append(NL);
                buffer.append("     groups=");
                appendDebug(buffer, groups);
            } else {
                buffer.append(" Client Only");
                buffer.append(" {");
                buffer.append(NL);
                buffer.append("     readActions=");
                appendDebug(buffer, readActions);
                buffer.append(NL);
                buffer.append("     editActions=");
                appendDebug(buffer, editActions);
            }
        } else {
            if (role != null) {
                buffer.append(" DB Only");
                buffer.append(NL);
                buffer.append("     groups=");
                appendDebug(buffer, groups);
            } else {
                buffer.append(" Unrecognised");
            }
        }

        buffer.append(NL);
        buffer.append("}");
        return buffer.toString();
    }

    private static void appendDebug(StringBuffer buffer, XhbSecurityGroupRoleBasicValue[] groups) {
        if (groups != null) {
            if (0 < groups.length) {
                buffer.append("{");
                buffer.append(groups[0].getGroupName());
                for (int i = 1; i < groups.length; i++) {
                    buffer.append(",");
                    buffer.append(groups[i].getGroupName());
                }
                buffer.append("}");
            } else {
                buffer.append("{}");
            }
        } else {
            buffer.append("null");
        }
    }

    private static void appendDebug(StringBuffer buffer, String[] values) {
        if (values != null) {
            if (0 < values.length) {
                buffer.append("{");
                buffer.append(values[0]);
                for (int i = 1; i < values.length; i++) {
                    buffer.append(",");
                    buffer.append(values[i]);
                }
                buffer.append("}");
            } else {
                buffer.append("{}");
            }
        } else {
            buffer.append("null");
        }
    }

    // Client Utilities
    private static FunctionList getFunction(FunctionList[] functions, String roleName) {
        for (int i = 0; i < functions.length; i++) {
            if (roleName.equals(functions[i].toString())) {
                return functions[i];
            }
        }
        return null;
    }

    // Entity Utils
    private static XhbSecurityRoleBasicValue getRole(RoleMappingCompositeValue roleMappingCompositeValue,
            String roleName) {
        XhbSecurityRoleBasicValue[] roles = roleMappingCompositeValue.getSecurityRoles();
        for (int i = 0; i < roles.length; i++) {
            if (roleName.equals(roles[i].getRoleName())) {
                return roles[i];
            }
        }
        return null;
    }

    private static XhbSecurityGroupRoleBasicValue[] getGroups(RoleMappingCompositeValue roleMappingCompositeValue,
            XhbSecurityRoleBasicValue role) {
        String roleName = role.getRoleName();

        // Get the groups
        List groupList = new ArrayList();

        XhbSecurityGroupRoleBasicValue[] groups = roleMappingCompositeValue.getSecurityGroupRoles();
        for (int i = 0; i < groups.length; i++) {
            if (roleName.equals(groups[i].getRoleName())) {
                groupList.add(groups[i]);
            }
        }

        // Sort the groups (by group name)
        Collections.sort(groupList, new Comparator() {
            public int compare(Object o1, Object o2) {
                String groupName1 = ((XhbSecurityGroupRoleBasicValue) o1).getGroupName();
                String groupName2 = ((XhbSecurityGroupRoleBasicValue) o2).getGroupName();
                return groupName1.compareTo(groupName2);
            }
        });

        // Return the groups
        return (XhbSecurityGroupRoleBasicValue[]) groupList
                .toArray(new XhbSecurityGroupRoleBasicValue[groupList.size()]);
    }

    // Get Roles assigned to a Group(passed in as argument)
    private static String[] getGroupRoles(RoleMappingCompositeValue roleMappingCompositeValue, String groupName) {
        // Get the Roles
        List roleList = new ArrayList();
        HashSet roleSet = new HashSet();

        XhbSecurityGroupRoleBasicValue[] groups = roleMappingCompositeValue.getSecurityGroupRoles();
        for (int i = 0; i < groups.length; i++) {
            if (groupName.equals(groups[i].getGroupName())) {
                roleList.add(groups[i].getRoleName());
            }
        }

        // Sort the roles (by role name)
        Collections.sort(roleList, new Comparator() {
            public int compare(Object o1, Object o2) {
                String roleName1 = o1.toString();
                String roleName2 = o2.toString();
                return roleName1.compareTo(roleName2);
            }
        });

        // Return the roles
        return (String[]) roleList.toArray(new String[roleList.size()]);
    }

    // Returns a list of all group names available in the Security Group
    // Role mappings
    private static String[] getGroupNames(RoleMappingCompositeValue roleMappingCompositeValue) {
        HashSet groupNames = new HashSet();
        XhbSecurityGroupRoleBasicValue[] groupsroles = roleMappingCompositeValue.getSecurityGroupRoles();
        for (int i = 0; i < groupsroles.length; i++) {
            groupNames.add(groupsroles[i].getGroupName());
        }

        String[] names = new String[groupNames.size()];

        // create a List from the HashSet
        List groupList = new ArrayList(groupNames);

        // Sort the groups (by group name)
        Collections.sort(groupList, new Comparator() {
            public int compare(Object o1, Object o2) {
                String roleName1 = o1.toString();
                String roleName2 = o2.toString();
                return roleName1.compareTo(roleName2);
            }
        });
        return (String[]) groupList.toArray(new String[groupList.size()]);
    }

    // Delegate Utilities
    private static RoleMappingCompositeValue getRoleMapping() throws RoleMappingException {
        return XhibitDelegateHelper.getRoleMappingDelegate().getRoleMappingCompositeValue();
    }
}