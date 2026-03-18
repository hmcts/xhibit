package uk.gov.courtservice.xhibit.common.rolemapping.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_role.XhbSecurityRoleBasicValue;

/**
 * <p>
 * Title: RoleMappingCompositeValue
 * </p>
 * <p>
 * Description: RoleMappingCompositeValue is intended to hold the
 * GroupHierarchy, the SecurityRoles and the SecurityGroupRoles for the
 * RoleMapping screen.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class RoleMappingCompositeValue extends CSAbstractValue {
	
	static final long serialVersionUID = -3630103658600492080L;
	
    /** The group hierarchy as defined in active directory */
    private GroupHierarchy hierarchy;

    /** The security roles */
    private XhbSecurityRoleBasicValue[] roles;

    /** The security group roles */
    private XhbSecurityGroupRoleBasicValue[] groupRoles;

    /**
     * Creates a RoleMappingCompositeValue.
     */
    public RoleMappingCompositeValue() {
    }

    /**
     * Gets the group hierarchy.
     * 
     * @return the group hierarchy.
     */
    public GroupHierarchy getGroupHierarchy() {
        return hierarchy;
    }

    /**
     * Sets the group hierarchy.
     * 
     * @param hierarchy
     *            the group hierarchy.
     */
    public void setGroupHierarchy(GroupHierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    /**
     * Gets the XhbSecurityRoles.
     * 
     * @return an array of XhbSecurityRoles.
     */
    public XhbSecurityRoleBasicValue[] getSecurityRoles() {
        return roles;
    }

    /**
     * Sets the Security Roles.
     * 
     * @param roles
     *            an array of XhbSecurityRoles.
     */
    public void setSecurityRoles(XhbSecurityRoleBasicValue[] roles) {
        this.roles = roles;
    }

    /**
     * Gets the XhbSecurityGroupRoles.
     * 
     * @return an array of XhbSecurityGroupRoles.
     */
    public XhbSecurityGroupRoleBasicValue[] getSecurityGroupRoles() {
        return groupRoles;
    }

    /**
     * Sets the Security Group Roles.
     * 
     * @param groupRoles
     *            an array of XhbSecurityGroupRoles.
     */
    public void setSecurityGroupRoles(XhbSecurityGroupRoleBasicValue[] groupRoles) {
        this.groupRoles = groupRoles;
    }

}