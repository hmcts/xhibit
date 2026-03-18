package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

public class GroupRoleValue implements Comparable {
    private boolean changed = false;

    private String description = null;

    XhbSecurityGroupRoleBasicValue sgrbv = null;

    public GroupRoleValue(String groupName, String roleName) {
        this.sgrbv = new XhbSecurityGroupRoleBasicValue();
        this.sgrbv.setGroupName(groupName);
        this.sgrbv.setRoleName(roleName);
        this.sgrbv.setIsEnabled("N");
    }

    public GroupRoleValue(XhbSecurityGroupRoleBasicValue sgrbv) {
        this.sgrbv = sgrbv;
    }

    public boolean hasChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getDescription() {
        if (description == null) {
            String roleName = sgrbv.getRoleName();

            description = ResourceBundleHelper.getResource(XhibitBundles.SecurityRoles, roleName);

            // if still not description, not found in the properties file,
            // so
            // display the key...
            if (description == null) {
                description = roleName;
            }
        }

        return description;
    }

    public int compareTo(Object o) {
        GroupRoleValue other = (GroupRoleValue) o;
        return this.getDescription().compareTo(other.getDescription());
    }

    public boolean isEnabled() {
        return "Y".equalsIgnoreCase(sgrbv.getIsEnabled());
    }

    public void setEnabled(boolean input) {
        sgrbv.setIsEnabled(input ? "Y" : "N");
        this.changed = true;
    }

    public String getRoleName() {
        return sgrbv.getRoleName();
    }

    public String getGroupName() {
        return sgrbv.getGroupName();
    }

    public XhbSecurityGroupRoleBasicValue getXhbSecurityGroupRoleBasicValue() {
        return this.sgrbv;
    }
}
