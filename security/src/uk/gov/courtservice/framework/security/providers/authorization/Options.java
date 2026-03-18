package uk.gov.courtservice.framework.security.providers.authorization;

/**
 * @author Meeraj
 * @version $Id: Options.java,v 1.4 2014/06/20 17:58:25 atwells Exp $
 */
public final class Options {
    /** System property to disable role mapper */
    private static final String DISABLE_ROLE_MAPPER = "disable.role.mapper";

    /** System property to disable role deployment */
    private static final String DISABLE_ROLE_DEPLOYMENT = "disable.role.deployment";

    /** Disable role mapper */
    private static final boolean roleMapperDisabled = getBoolean(DISABLE_ROLE_MAPPER);

    /** Disable role deployment */
    private static final boolean roleDeploymentDisabled = getBoolean(DISABLE_ROLE_DEPLOYMENT);

    /**
     * Utility method that returns true if a system property is present
     * 
     * @param property
     * @return
     */
    private static final boolean getBoolean(final String property) {
        return (System.getProperty(property) != null);
    }

    /**
     * Checks whether the role mapper is disabled
     * 
     * @return
     */
    public static final boolean isRoleMapperDisabled() {
        //System.out.println("Options:  role mapper disabled = "+roleMapperDisabled);
        return roleMapperDisabled;
    }

    /**
     * Checks whether the role deployment is disabled
     * 
     * @return
     */
    public static final boolean isRoleDeploymentDisabled() {
        return roleDeploymentDisabled;
    }
}