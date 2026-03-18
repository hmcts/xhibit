package uk.gov.courtservice.framework.security.providers.authorization;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * An aggregation of all groups
 * 
 * @author Meeraj
 * @version $Id: Groups.java,v 1.4 2006/06/05 12:30:04 bzjrnl Exp $
 */
public class Groups {
    private static final Logger log = Logger.getLogger(Groups.class);

    private final RoleMappingsDAO dao = new RoleMappingsDAO();

    private final Map groups = new HashMap();

    /**
     * Gets the current list of groups
     * 
     * @return
     */
    public Group[] getGroups() {
        return (Group[]) groups.values().toArray(new Group[groups.size()]);
    }

    /**
     * Returns the group for the group name. If doesn't exist creates a new one
     * 
     * @param groupName
     * 
     * @return
     */
    public Group getGroup(String groupName) {
        log.debug("Getting group " + groupName);

        Group group = (Group) groups.get(groupName);

        if (group == null) {
            group = new Group(groupName);
            groups.put(groupName, group);
            log.debug("Group newly created");
        }

        log.debug("Returning group " + groupName);

        return group;
    }

    /**
     * Adds a new group
     * 
     * @param group
     */
    public void addGroup(Group group) {
        groups.put(group.getName(), group);
    }

    /**
     * Replaces the current list of groups
     */
    public void refresh() {
        try {
            // Get data from database
            Groups newGroups = dao.getGroups();

            // Refresh cache
            groups.clear();

            Group[] grps = newGroups.getGroups();

            for (int i = 0; i < grps.length; i++) {
                groups.put(grps[i].getName(), grps[i]);
            }
        } catch (SQLException ex) {
            log.error("refresh() error getting groups", ex);
        }
    }

    /**
     * Checks whether the role mappings exist
     * 
     * @param groupName
     * @param roleName
     * 
     * @return
     */
    public boolean hasMapping(String groupName, String roleName) {
        Group group = (Group) groups.get(groupName);

        if (group == null) {
            return false;
        }

        return group.hasRole(roleName);
    }

    /**
     * Adds the mapping
     * 
     * @param groupName
     * @param roleName
     */
    public void addMapping(String groupName, String roleName) {
        try {
            // Add to persistent store
            dao.addMapping(groupName, roleName);

            // Add to in-memory cache
            getGroup(groupName).addRole(roleName);
        } catch (SQLException ex) {
            log.error("addMapping() error adding mapping for group = " + groupName + ", role = " + roleName, ex);
        }
    }

    public String toString() {
        return groups.toString();
    }
}
