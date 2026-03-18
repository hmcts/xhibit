package uk.gov.courtservice.xhibit.rolemapping.services;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryException;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryGroup;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryGroupHierarchy;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroup;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroupBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroupBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroupBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRole;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_role.XhbSecurityRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_role.XhbSecurityRoleBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.activedirectory.ActiveDirectoryControllerLocal;
import uk.gov.courtservice.xhibit.business.services.activedirectory.ActiveDirectoryControllerLocalHome;
import uk.gov.courtservice.xhibit.common.rolemapping.exceptions.RoleMappingException;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.GroupHierarchy;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.RoleMappingCompositeValue;

/**
 * <p>
 * Title: RoleMappingControllerBean
 * </p>
 * <p>
 * Description: Bean class for the role mapping controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * This class was originally in midtier/main, but moved to its own sub-project.
 * All code moved is exactly as it was previously, except changed to use the
 * newer EJB frameworks.
 * 
 * @ejb.bean name="RoleMappingController" description="Role mapping Controller
 *           Bean" type="Stateless" view-type="remote"
 *           jndi-name="RoleMappingControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Meeraj Kunnumpurath
 * @author tz0d5m - moved to seperate sub-project.
 * @version $Id: RoleMappingControllerBean.java,v 1.7 2005/01/17 08:55:20 tz0d5m
 *          Exp $
 */
public class RoleMappingControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 2892902737638628603L;

    /** Default for the enabled flags */
    private static final String GROUP_ROLE_ENABLED = "Y";

    private static final String GROUP_ROLE_DISABLED = "N";

    // Both the below variables are set in the ejbCreate() method...
    private ActiveDirectoryControllerLocal activeDirectoryController;

    /** The notifier to indicate when the roles need to be refreshed */
    private RoleMappingNotifier roleMappingNotifier;

    /**
     * Custom creation method, used to set up this bean for use. This method
     * creates the notifier for the message driven bean. Also, creates the
     * initial directory context to connect to Active Directory.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        log.debug("ejbCreate() - Begin");
        super.ejbCreate();
        activeDirectoryController = (ActiveDirectoryControllerLocal) CSServices.getEJBServices().createLocalSession(
                ActiveDirectoryControllerLocalHome.class);

        this.roleMappingNotifier = new RoleMappingNotifier();

        log.debug("ejbCreate() - End");
    }

    /**
     * Custom removal method. This method ensures that the message driven bean
     * notifier is closed correctly.
     * 
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() {
        this.roleMappingNotifier.close();
    }

    /**
     * Get all the data for the role mappings screen
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @return RoleMappingCompositeValue which consists of the group hierarchy,
     *         all of the roles and the group role mapping.
     * @throws RoleMappingException
     */
    public RoleMappingCompositeValue getRoleMappingCompositeValue() throws RoleMappingException {
        log.debug("getRoleMappingCompositeValue() - Begin");
        GroupHierarchy hierarchy;

        // Get the refreshed list of groups
        try {
            hierarchy = getGroupHierarchy();
        } catch (RoleMappingException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new RoleMappingException(ex.getUserMessageAsMessage().getKey(), ex.getMessage());
        }

        log.debug("getRoleMappingCompositeValue() - Number of groups: " + hierarchy.getAllGroups().size());

        XhbSecurityRoleBasicValue[] roles = XhbSecurityRoleBeanHelper2.findNonSystemRolesValue();

        log.debug("getRoleMappingCompositeValue() - got roles");

        XhbSecurityGroupRoleBasicValue[] groupRoles = XhbSecurityGroupRoleBeanHelper2.findAllValue();

        log.debug("getRoleMappingCompositeValue() - got group roles");

        RoleMappingCompositeValue rmcv = new RoleMappingCompositeValue();
        rmcv.setGroupHierarchy(hierarchy);
        rmcv.setSecurityRoles(roles);
        rmcv.setSecurityGroupRoles(groupRoles);

        log.debug("getRoleMappingCompositeValue() - End");
        return rmcv;
    }

    /**
     * Sets the group role mappings in XHIBIT
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param groupRoles
     *            to roles mappings
     */
    public void setGroupRoles(XhbSecurityGroupRoleBasicValue[] groupRoles) {
        log.debug("setGroupRoles() - Begin");

        if (groupRoles == null) {
            throw new IllegalArgumentException("groupRoles is null");
        }

        for (int i = 0; i < groupRoles.length; i++) {
            XhbSecurityGroupRoleBasicValue aGroupRole = groupRoles[i];
            if (log.isDebugEnabled()) {
                log.debug("setGroupRoles - processing group = " + aGroupRole.getGroupName() + " role = "
                        + aGroupRole.getRoleName());
            }

            if (aGroupRole.getSecurityGroupRoleId() == null) {
                log.debug("setGroupRoles - adding group/role");

                aGroupRole.setIsEnabled(GROUP_ROLE_ENABLED);
                aGroupRole.setIsEnabledByDefault(GROUP_ROLE_DISABLED);

                XhbSecurityGroupRoleBeanHelper2.create(aGroupRole);
                
                /*Audit the role change action*/
                doAudit(aGroupRole);
            } else {
                log.debug("setGroupRoles - updating group/role");

                XhbSecurityGroupRoleBasicValue existingValue = 
                    XhbSecurityGroupRoleBeanHelper2.findByPrimaryKeyValue(aGroupRole.getSecurityGroupRoleId());
                
                if(!existingValue.getIsEnabled().equals(aGroupRole.getIsEnabled())){                
                    // update securityGroupRole
                    XhbSecurityGroupRoleBeanHelper2.updateLocal(aGroupRole);
                    doAudit(aGroupRole);
                }
            }
        }

        if (groupRoles.length > 0) {
            // then we would have updated at least one role, so notify...
            this.roleMappingNotifier.sendMessage(groupRoles.length + " roles created/updated");
        }
    }
    
    private void doAudit(XhbSecurityGroupRoleBasicValue aGroupRole){
        /*Audit the role change action*/
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put(XhbSecurityGroupRoleBasicValue.class.getName(), aGroupRole);
        AuditTrailService auditService = CSServices.getAuditTrailService();
        AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
        event.setSuccess(true);
        auditService.createAuditRecord(event);
    }

    /**
     * Gets the hierarchy of groups
     * 
     * @return GroupHierarchy
     * @throws RoleMappingException
     */
    private GroupHierarchy getGroupHierarchy() throws RoleMappingException {
        // Get the GroupHierarchy from active directory
        GroupHierarchy groupHierarchy = loadGroupHierarchy();

        // Update the GroupHierarchy from the database
        addNew(groupHierarchy.getAllGroups());
        removeObsolete(groupHierarchy.getAllGroups());

        // Return
        return groupHierarchy;
    }

    private GroupHierarchy loadGroupHierarchy() throws RoleMappingException {
        try {
            return convertHierarchy(activeDirectoryController.getGroupHierarchy(), activeDirectoryController
                    .getRootGroup());
        } catch (ActiveDirectoryException ade) {
            log.debug("groupHierarchy is invalid, exception will be thrown");
            throw new RoleMappingException("rolemapping.noGroupHierarchy",
                    "The group hierarchy is null after trying to get it from AD.");
        }
    }

    // Convert the active directory group hierarchy into the local version
    private static GroupHierarchy convertHierarchy(ActiveDirectoryGroupHierarchy hierarchy, ActiveDirectoryGroup root) {
        GroupHierarchy localHierarchy = new GroupHierarchy(root.getPrincipalName());
        populateHierarchy(localHierarchy, hierarchy, root);
        return localHierarchy;
    }

    // Recurse depth first over the hierarchy parents MUST be added before
    // their
    // children!
    private static void populateHierarchy(GroupHierarchy localHierarchy,
            ActiveDirectoryGroupHierarchy activeDirectoryHierarchy, ActiveDirectoryGroup parent) {
        ActiveDirectoryGroup[] children = activeDirectoryHierarchy.getChildren(parent);
        for (int i = 0; i < children.length; i++) {
            localHierarchy.addGroup(parent.getPrincipalName(), children[i].getPrincipalName());
            populateHierarchy(localHierarchy, activeDirectoryHierarchy, children[i]);
        }
    }

    /**
     * Adds the newly added groups in AD to the database
     * 
     * @param newGroups
     *            all the group retrieved from active directory
     * @throws EJBException
     */
    private void addNew(List newGroups) {
        log.debug("addNew - Begin");
        for (Iterator it = newGroups.iterator(); it.hasNext();) {
            String group = (String) it.next();

            try {
                // Try to find the group
                XhbSecurityGroupBeanHelper2.findByPrimaryKey(group);
            } catch (XhbSecurityGroupBeanNotFoundException ex) {
                if (log.isDebugEnabled()) {
                    log.debug("addNew - could not find group " + group + ", so about to add it");
                }

                XhbSecurityGroupBasicValue value = new XhbSecurityGroupBasicValue();
                value.setDescription("N/A");
                value.setGroupName(group);
                XhbSecurityGroupBeanHelper2.createLocal(value);
            }
        }
        log.debug("addNew - End");
    }

    /**
     * Removes the groups removed from AD in the database
     * 
     * @param newGroups
     *            all the group retrieved from active directory
     * @throws EJBException
     */
    private void removeObsolete(LinkedList newGroups) {
        log.debug("removeObsolete - Begin");
        try {
            // Get all the groups in the DB
            Iterator oldGroups = XhbSecurityGroupBeanHelper2.findAll().iterator();

            while (oldGroups.hasNext()) {
                XhbSecurityGroup group = (XhbSecurityGroup) oldGroups.next();

                // If group not in AD remove
                if (!newGroups.contains(group.getGroupName())) {
                    if (log.isDebugEnabled()) {
                        log.debug("Removing group:" + group.getGroupName());
                    }

                    // Does a cascade delete to remove security group roles.
                    group.remove();
                }
            }
        } catch (RemoveException ex) {
            // Remove exception is unexpected
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
        log.debug("removeObsolete - End");
    }
}
