package uk.gov.courtservice.framework.security.providers.authorization;

import java.util.Set;
import java.util.TreeSet;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import weblogic.security.SubjectUtils;

/**
 * Represents a role and the groups that have the role
 * 
 * @author Meeraj
 * @version $Id: Group.java,v 1.3 2006/06/05 12:30:04 bzjrnl Exp $
 */
public class Group {
    private static final Logger logger = Logger.getLogger(Group.class);

    private final RoleFlyweightPool rolePool = RoleFlyweightPool.getInstance();

    private final Set roles = new TreeSet();

    private final String name;

    /**
     * Creates a group
     * 
     * @param newName
     *            name
     * 
     * @throws IllegalArgumentException
     */
    public Group(String newName) {
        if (newName == null) {
            throw new IllegalArgumentException("newName");
        }

        name = newName;
    }

    /**
     * Returns the role name
     * 
     * @return Group name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether the role is assigned to the group
     * 
     * @param roleName
     * 
     * @return
     */
    public boolean hasRole(String roleName) {
        return roles.contains(roleName);
    }

    /**
     * Returns the roles assigned to this group
     * 
     * @return
     */
    public String[] getRoles() {
        return (String[]) roles.toArray(new String[roles.size()]);
    }

    /**
     * Assigns a role to the specified group
     * 
     * @param roleName
     */
    public void addRole(String roleName) {
        logger.debug("Adding role:" + roleName);
        roles.add(roleName);

        // Add the role to the pool
        rolePool.addRole(roleName);
        logger.debug("Added role:" + roleName);
    }

    public boolean isMember(Subject subject) {
        return SubjectUtils.isUserInGroup(subject, name);
    }

    public boolean equals(Object obj) {
        return obj instanceof Group && ((Group) obj).getName().equals(name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return roles.toString();
    }
}
