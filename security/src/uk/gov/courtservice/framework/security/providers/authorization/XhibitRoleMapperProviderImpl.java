package uk.gov.courtservice.framework.security.providers.authorization;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import weblogic.management.security.ProviderMBean;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.ApplicationInfo;
import weblogic.security.spi.DeployHandleCreationException;
import weblogic.security.spi.DeployRoleHandle;
import weblogic.security.spi.DeployableRoleProviderV2;
import weblogic.security.spi.Resource;
import weblogic.security.spi.RoleMapper;
import weblogic.security.spi.SecurityProvider;
import weblogic.security.spi.SecurityServices;
import weblogic.security.spi.VersionableApplicationProvider;
import weblogic.security.spi.ApplicationInfo.ComponentType;

/**
 * This class implements the Weblogic role mapper and deployable role provider
 * for Xhibit. This role mapper uses the information from a set of database
 * tables
 */
public final class XhibitRoleMapperProviderImpl implements DeployableRoleProviderV2, RoleMapper, VersionableApplicationProvider {
    private static final Map NO_ROLES = Collections.unmodifiableMap(new HashMap(1));

    private static final Logger log = Logger.getLogger(XhibitRoleMapperProviderImpl.class);

    private final RoleFlyweightPool rolePool = RoleFlyweightPool.getInstance();

    private final RoleMappings roleMappings = new RoleMappings();

    private JMSListener listener;

    private String rolePrefix;

    /**
     * Initialize the Xhibit role mapper.
     * 
     * @param mbean
     *            A ProviderMBean that holds the Xhibit role mapper's
     *            configuration data. This mbean must be an instance of the
     *            Xhibit role mapper's mbean.
     * @param services
     *            The SecurityServices gives access to the auditor so that the
     *            provider can to post audit events. The Xhibit role mapper
     *            doesn't use this parameter.
     * 
     * @see SecurityProvider
     */
    public void initialize(ProviderMBean mbean, SecurityServices services) {
        log.info("Role mapper disabled: " + Options.isRoleMapperDisabled());
        log.info("Role deployment disabled: " + Options.isRoleDeploymentDisabled());

        // If we are using the role mapper, then set it up...
        if (!Options.isRoleMapperDisabled()) {
            // Cast the mbean from a generic ProviderMBean to a
            // XhibitRoleMapperMBean.
            XhibitRoleMapperMBean myMBean = (XhibitRoleMapperMBean) mbean;

            // Instantiate the helper that manages this provider's role
            // definitions
            ConnectionUtil.initialize(myMBean.getDriverName(), myMBean.getURL(), myMBean.getPrincipal(), myMBean
                    .getCredential());
            log.debug("Connection pool initialized");
            //System.out.println("Connection pool initialized");

            // Load the cache from database
            this.roleMappings.loadData();
            log.debug("Cache loaded");
            //System.out.println("Cache loaded");

            this.rolePrefix = myMBean.getRolePrefix();
            //log.debug("rolePrefix:" + rolePrefix);
            //System.out.println("rolePrefix:" + rolePrefix);

            final String conectionFactoryName = myMBean.getJMSConnectionFactoryName();
            final String destinationName = myMBean.getJMSDestinationName();
            final int connectionAttempts = Integer.parseInt(myMBean.getJMSConnectionAttempts());
            final long connectionSleepTime = Long.parseLong(myMBean.getJMSConnectionSleepTime());

            this.listener = new JMSListener(roleMappings, conectionFactoryName, destinationName, connectionAttempts,
                    connectionSleepTime);
            listener.startListening();
        }
    }

    /**
     * Get the Xhibit role mapper's description.
     * 
     * @return A String containing a brief description of the Xhibit role
     *         mapper.
     * 
     * @see SecurityProvider
     */
    public String getDescription() {
        return "";
    }

    /**
     * Shutdown the Xhibit role mapper.
     */
    public void shutdown() {
        if (this.listener != null) {
            this.listener.stopListening();
        }

        ConnectionUtil.close();
    }

    /**
     * Gets the Xhibit role mapper provider's role mapper object.
     * 
     * @return The Xhibit role mapper provider's RoleMapper object.
     */
    public RoleMapper getRoleMapper() {
        return this;
    }

    /**
     * Determines what roles the current subject is in for this resource.
     * 
     * @param subject
     *            A Subject that contains the user and groups.
     * @param resource
     *            The Resource the Subject is trying to access.
     * @param handler
     *            Not used by the Xhibit role mapper.
     * 
     * @return a Map containing SecurityRoles identifying the computed roles.
     */
    public Map getRoles(Subject subject, Resource resource, ContextHandler handler) {
        //System.out.println("XhibitRoleMapperImpl.getRoles");
        // We are not using the role mapper
        if (Options.isRoleMapperDisabled()) {
            return NO_ROLES;
        }
        if (resource!=null) {
            log.debug("XhibitRoleMapperImpl.getRoles: ResourceType="+resource.getType());
        }
        /*if (handler!=null) {
            log.debug("XhibitRoleMapperImpl.getRoles: handler size="+handler.size());
        }*/

        // Make a list for the roles
        Map ret = new HashMap();

        try {
            String[] roles = roleMappings.getRoles(resource, subject);

            // loop over all the roles in our "database" for this resource
            for (int i = 0; (roles != null) && (i < roles.length); i++) {
                ret.put(roles[i], rolePool.getRole(roles[i]));
                //System.out.println("role="+roles[i].toString());
            }
        } catch (Throwable th) {
            log.fatal(th.getMessage(), th);
        }

        // special handling for no matching roles
        if (ret.isEmpty()) {
            return NO_ROLES;
        }

        // return the roles we found.
        return ret;
    }

    /**
     * Stores the role definitions specified in a deployed webapp or EJB in the
     * Xhibit role mapper's properties file.
     * 
     * @param resource
     *            A Resource that identifies the webapp or EJB.
     * @param roleName
     *            A String containing the name of the role (scoped by this
     *            resource).
     * @param principalNames
     *            An array of String containing the users and groups that are in
     *            this role on this resource (that is, the role definition).
     * 
     */
    public void deployRole(DeployRoleHandle drh, Resource resource, String roleName, String[] principalNames) {
        // We are not using the role mapper
        if (Options.isRoleMapperDisabled() || Options.isRoleDeploymentDisabled()) {
            return;
        }

        // We are only interested in XHIBIT roles
        if (!roleName.startsWith(rolePrefix)) {
            return;
        }

        try {
            roleMappings.addMapping(resource, roleName, principalNames);
        } catch (Throwable th) {
            log.fatal(th.getMessage(), th);
        }
    }

    /**
     * We don't support role undeployment.
     * 
     * @param resource
     *            A Resource that identifies the webapp or EJB.
     * @param roleName
     *            A String containing the name of the role (scoped by this
     *            resource).
     * 
     */
    public void undeployRole(Resource resource, String roleName) {
        if (log.isEnabledFor(Level.WARN)) {
            log.warn("Could not undeploy role " + roleName + ". Undeployment of roles is not supported.");
        }
    }
    
    // The following methods have had to be added as the interface has change in WL10
    
    public void undeployAllRoles(DeployRoleHandle drh) {
        System.out.println("undeployAllRoles");
    }
    
    public void deleteApplicationRoles(ApplicationInfo ai) {
        System.out.println("deleteApplicationRoles");
    }
    
    public void endDeployRoles(DeployRoleHandle drh) {
        System.out.println("endDeployRoles");
    }
    
    public DeployRoleHandle startDeployRoles(ApplicationInfo ai) throws DeployHandleCreationException {
        System.out.println("startDeployRoles");
        String appId = ai.getApplicationIdentifier();
        System.out.println("appId="+appId);
        ComponentType compType = ai.getComponentType();
        System.out.println("compType.name="+compType.getName());
        String compName = ai.getComponentName();
        System.out.println("compName="+compName);
        
        DeployRoleHandle handle = new XhibitDeployRoleHandle(appId, compName, compType);
        return handle;
    }
    
    public void createApplicationVersion(String appId, String sourceAppId) {
        System.out.println("createApplicationVersion");
    }
    
    public void deleteApplicationVersion(String appId) {
        System.out.println("deleteApplicationVersion");
    }
    
    public void deleteApplication(String appId) {
        System.out.println("deleteApplication");
    }
    
}


class XhibitDeployRoleHandle implements DeployRoleHandle {
    
    Date date;
    String application;
    String component;
    ComponentType componentType;
    
    XhibitDeployRoleHandle(String app, String comp, ComponentType type) {
        this.application = app;
        this.component = comp;
        this.componentType = type;
        this.date = new Date();
    }
    
    public String getApplication() {
        return application;
    }
    
    public String getComponent() {
        return component;
    }
    
    public ComponentType getComponentType() {
        return componentType;
    }
    
    public String toString() {
        String name = component;
        if (componentType == ComponentType.APPLICATION) {
            name = application;
        }
        return componentType + "" + name + " [" + date.toString() +"]";
    }
}
