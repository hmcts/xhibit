package uk.gov.courtservice.framework.security.providers.authorization;

import weblogic.security.service.SecurityRole;

/**
 * This is a class internal to the role mapping provider
 */
class XhibitSecurityRoleImpl implements SecurityRole {
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
     * @return A boolean indicating if this role is the same as the one passed
     *         in.
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
        if (!(genericRole instanceof XhibitSecurityRoleImpl)) {
            return false;
        }

        // Cast the other role to a Xhibit role mapper role.
        XhibitSecurityRoleImpl XhibitRole = (XhibitSecurityRoleImpl) genericRole;

        // if our names don't match, we're not the same
        if (!roleName.equals(XhibitRole.getName())) {
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
