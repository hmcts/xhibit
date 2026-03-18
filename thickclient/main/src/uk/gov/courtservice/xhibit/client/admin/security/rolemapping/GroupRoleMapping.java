package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_role.XhbSecurityRoleBasicValue;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.GroupHierarchy;

/**
 * <p>
 * Title: Class representing group to roles mapping
 * </p>
 * 
 * <p>
 * Description: This class provides the roles assigned to a group
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class GroupRoleMapping implements java.io.Serializable {
    /** The logger */
    private static final Logger log = Logger.getLogger(GroupRoleMapping.class);

    /** Group Hierarchy as retried from active directory */
    private GroupHierarchy hierarchy;

    /** Groups to roles mapping */
    private TreeMap mappings = new TreeMap();

    /**
     * Creates a GroupRoleMapping based on the given hierarchy, all of the
     * security roles and all of the security group roles.
     * 
     * @param hierarchy
     *            The group hierarch as retrieved from active directory.
     * @param allRoles
     *            All of the security roles.
     * @param allGroupRoles
     *            All of the security group roles.
     * 
     * @throws IllegalArgumentException
     *             if hierarchy or allRoles = null.
     */
    public GroupRoleMapping(GroupHierarchy hierarchy, XhbSecurityRoleBasicValue[] allRoles,
            XhbSecurityGroupRoleBasicValue[] allGroupRoles) {
        if (hierarchy == null) {
            throw new IllegalArgumentException("Hierarchy is null");
        }

        if (allRoles == null) {
            throw new IllegalArgumentException("Role list is null");
        }

        this.hierarchy = hierarchy;

        GroupRoleComparator groupRoleComparator = new GroupRoleComparator();

        Arrays.sort(allGroupRoles, groupRoleComparator);

        if (log.isDebugEnabled()) {
            for (int i = 0; i < allGroupRoles.length; i++) {
                log.debug(allGroupRoles[i].getGroupName() + " \t" + allGroupRoles[i].getRoleName() + " \t"
                        + allGroupRoles[i].getVersion() + " \t" + allGroupRoles[i].getIsEnabled());
            }
        }

        Iterator groups = hierarchy.getAllGroups().iterator();
        while (groups.hasNext()) {
            String group = (String) groups.next();
            GroupRoleValue[] roles = new GroupRoleValue[allRoles.length];

            for (int i = 0; i < allRoles.length; i++) {
                String roleName = allRoles[i].getRoleName();

                XhbSecurityGroupRoleBasicValue tmp = getFirstMatch(allGroupRoles, group, roleName);

                if (tmp != null) {
                    roles[i] = new GroupRoleValue(tmp);
                } else {
                    roles[i] = new GroupRoleValue(group, roleName);
                }
            }

            Arrays.sort(roles);
            // Add the role mappings for the group
            mappings.put(group, roles);
        }
    }

    /**
     * Gets all the groups
     * 
     * @return Name of the group
     */
    public GroupHierarchy getGroupHierarchy() {
        return hierarchy;
    }

    /**
     * Gets all the roles enabled for this group
     * 
     * @param groupName
     *            The group name
     * 
     * @return An array of all the group roles values
     */
    public GroupRoleValue[] getEnabledRoles(String groupName) {
        log.debug("getEnabledRoles - BEGIN - with group = " + groupName);

        ArrayList enabledRoles = new ArrayList();

        GroupRoleValue[] groupRoles = getAllRolesForGroup(groupName);
        for (int i = 0; i < groupRoles.length; i++) {
            if (groupRoles[i].isEnabled()) {
                enabledRoles.add(groupRoles[i]);
            }
        }

        return (GroupRoleValue[]) enabledRoles.toArray(new GroupRoleValue[enabledRoles.size()]);
    }

    /**
     * Enables (or disables) the specified role for the given group and all, if
     * any, of its child groups.
     * 
     * @param group
     *            The group name for which you wish to enable a role.
     * @param role
     *            The GroupRoleValue, the role you wish to enable.
     * @param roleEnabled
     *            boolean set true to enable the given group / role.
     * 
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    public void enableRole(String group, GroupRoleValue role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is null");
        }

        enableChildren(group, role.getRoleName(), role.isEnabled());
    }

    private void enableChildren(String group, String roleName, boolean enabled) {
        List children = hierarchy.getChildrenForGroup(group);
        log.debug("enableChildren(" + group + ", " + roleName + ", " + enabled + ")" + " has " + children.size()
                + " children");

        for (Iterator i = children.iterator(); i.hasNext();) {
            String childGroup = (String) i.next();
            enableChildren(childGroup, roleName, enabled);

            GroupRoleValue[] groupRoles = getAllRolesForGroup(childGroup);
            enableFirstChild(groupRoles, childGroup, roleName, enabled);
        }
    }

    /**
     * For the given group, get its enabled roles and enable them for all child
     * groups.
     * 
     * @param group
     *            The group name.
     */
    public void enableChildRoles(String group) {
        GroupRoleValue[] groupRoles = getAllRolesForGroup(group);
        for (int i = 0; i < groupRoles.length; i++) {
            if (groupRoles[i].isEnabled()) {
                enableChildren(groupRoles[i].getGroupName(), groupRoles[i].getRoleName(), true);
            }
        }
    }

    /**
     * Gets the map of all roles with enabled/disabled idicator for this group
     * 
     * @param group
     *            The group name.
     * 
     * @return An array of all the roles for the given group.
     * 
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    public GroupRoleValue[] getAllRolesForGroup(String group) {
        if (group == null) {
            throw new IllegalArgumentException("Group " + group + " not found.");
        }

        return (GroupRoleValue[]) mappings.get(group);
    }

    /**
     * Gets the derived roles for this group
     * 
     * @param group
     *            The group name.
     * 
     * @return An array of group role values where the role for an ancestor
     *         group of the given group is enabled.
     * 
     * @throws IllegalArgumentException
     */
    public GroupRoleValue[] getDerivedRoles(String group) {
        log.debug("getDerivedRoles - Begin with group = " + group);
        if (group == null) {
            throw new IllegalArgumentException("Group is null");
        }

        TreeMap derivedRoles = new TreeMap();

        // Get all the ancestors and their roles
        Iterator ancestors = hierarchy.getAncestors(group).iterator();

        while (ancestors.hasNext()) {
            String ancestorGroup = (String) ancestors.next();
            GroupRoleValue[] ancestorRoles = getEnabledRoles(ancestorGroup);
            for (int i = 0; i < ancestorRoles.length; i++) {
                derivedRoles.put(ancestorRoles[i].getRoleName(), ancestorRoles[i]);
            }
        }

        // Return the derived roles
        GroupRoleValue[] grv = (GroupRoleValue[]) derivedRoles.values()
                .toArray(new GroupRoleValue[derivedRoles.size()]);

        // sort the array before returning it...
        Arrays.sort(grv);
        return grv;
    }

    /**
     * Get the group/roles that have been changed by the user
     * 
     * @return an array of XhbSecurityGroupRoleBasicValue
     */
    public XhbSecurityGroupRoleBasicValue[] getChangedGroupRoles() {
        // List of changed group roles
        ArrayList changedGroupRoles = new ArrayList();

        Iterator allGroupsIt = hierarchy.getAllGroups().iterator();
        while (allGroupsIt.hasNext()) {
            String groupName = (String) allGroupsIt.next();
            GroupRoleValue[] roles = (GroupRoleValue[]) mappings.get(groupName);
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].hasChanged()) {
                    log.debug("groupName = " + groupName + " has changed!!! " + roles[i].getRoleName());
                    changedGroupRoles.add(roles[i].getXhbSecurityGroupRoleBasicValue());
                }
            }
        }

        return (XhbSecurityGroupRoleBasicValue[]) changedGroupRoles
                .toArray(new XhbSecurityGroupRoleBasicValue[changedGroupRoles.size()]);
    }

    /**
     * Pretty print for the object
     * 
     * @return the group role mapping as a string
     */
    public String toString() {
        return mappings.toString();
    }

    private class GroupRoleComparator implements Comparator {
        public int compare(Object obj1, Object obj2) {
            XhbSecurityGroupRoleBasicValue groupRole1 = (XhbSecurityGroupRoleBasicValue) obj1;
            XhbSecurityGroupRoleBasicValue groupRole2 = (XhbSecurityGroupRoleBasicValue) obj2;

            String groupRoleName1 = groupRole1.getGroupName() + groupRole1.getRoleName();
            String groupRoleName2 = groupRole2.getGroupName() + groupRole2.getRoleName();

            return groupRoleName1.compareTo(groupRoleName2);
        }
    }

    private void enableFirstChild(GroupRoleValue[] groupRoleValues, String groupName, String roleName, boolean enabled) {
        if ((groupName != null) && (roleName != null)) {
            for (int i = 0; i < groupRoleValues.length; i++) {
                if (groupName.equals(groupRoleValues[i].getGroupName())
                        && roleName.equals(groupRoleValues[i].getRoleName())) {
                    groupRoleValues[i].setEnabled(enabled);
                    break;
                }
            }
        }
    }

    private XhbSecurityGroupRoleBasicValue getFirstMatch(XhbSecurityGroupRoleBasicValue[] allGroupRoles,
            String groupName, String roleName) {
        if ((groupName != null) && (roleName != null)) {
            for (int i = 0; i < allGroupRoles.length; i++) {
                if (groupName.equals(allGroupRoles[i].getGroupName())
                        && roleName.equals(allGroupRoles[i].getRoleName())) {
                    return allGroupRoles[i];
                }
            }
        }

        // return null if no match found...
        return null;
    }
}
