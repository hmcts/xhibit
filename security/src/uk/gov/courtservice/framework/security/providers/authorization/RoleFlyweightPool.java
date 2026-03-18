package uk.gov.courtservice.framework.security.providers.authorization;

import java.util.HashMap;

import weblogic.security.service.SecurityRole;

/**
 * @author Meeraj
 * @version $Id: RoleFlyweightPool.java,v 1.4 2006/06/05 12:30:05 bzjrnl Exp $
 */
public class RoleFlyweightPool {
    /** Singleton instance */
    private static RoleFlyweightPool instance = new RoleFlyweightPool();

    /** Cache of roles */
    private HashMap roles = new HashMap();

    /**
     * Private constrcutor to stop instantiation
     */
    private RoleFlyweightPool() {
        // Reduce permisions
    }

    /**
     * Singleton accessor
     * 
     * @return
     */
    public static RoleFlyweightPool getInstance() {
        return instance;
    }

    /**
     * Gets the role from the pool
     * 
     * @param roleName
     * 
     * @return
     */
    public SecurityRole getRole(String roleName) {
        return (SecurityRole) roles.get(roleName);
    }

    /**
     * Adds a role to the pool
     * 
     * @param roleName
     */
    public void addRole(String roleName) {
        roles.put(roleName, new RoleFlyweightPool.XhibitSecurityRoleImpl(roleName));
    }

    /**
     * Inner class that implements the security role
     * 
     * @author Meeraj
     */
    private static class XhibitSecurityRoleImpl implements SecurityRole {
        /** The name of the role */
        private String roleName;

        /** The hashcode for the role */
        private int hashCode;

        /**
         * Create a Xhibit role mapper role instance.
         * 
         * @param roleName
         *            A String containing the role's name.
         */
        XhibitSecurityRoleImpl(String roleName) {
            this.roleName = roleName;
            this.hashCode = roleName.hashCode() + 17;
        }

        /**
         * Deterimines if two role instances are the same.
         * 
         * @param genericRole
         *            An Object for another role.
         * 
         * @return A boolean indicating if this role is the same as the one
         *         passed in.
         * 
         * @see Object
         */
        public boolean equals(Object genericRole) {
            // if the other role is null, we're not the same
            if (genericRole == null) {
                return false;
            }

            // if we're the same java object, we're the same
            if (this == genericRole) {
                return true;
            }

            // if the other role is not a Xhibit role mapper role,
            // we're not the same
            if (!(genericRole instanceof RoleFlyweightPool.XhibitSecurityRoleImpl)) {
                return false;
            }

            // Cast the other role to a Xhibit role mapper role.
            RoleFlyweightPool.XhibitSecurityRoleImpl xhibitRole = (RoleFlyweightPool.XhibitSecurityRoleImpl) genericRole;

            // if our names don't match, we're not the same
            if (!roleName.equals(xhibitRole.getName())) {
                return false;
            }

            // we're the same
            return true;
        }

        /**
         * Convert the role to a printable string.
         * 
         * @return A String containing the role's name.
         * 
         * @see Object
         */
        public String toString() {
            return roleName;
        }

        /**
         * Get the role's hash code.
         * 
         * @return an int containing the role's hash code.
         * 
         * @see Object
         */
        public int hashCode() {
            return hashCode;
        }

        /**
         * Get the role's name
         * 
         * @return A String containing the role's name
         * 
         * @see SecurityRole
         */
        public String getName() {
            return roleName;
        }

        /**
         * Get the role's description.
         * 
         * @return A String containing the role's description. Returns an empty
         *         string since the Xhibit role mapper doesn't support role
         *         descriptions.
         * 
         * @see SecurityRole
         */
        public String getDescription() {
            return "";
        }
    }
}
