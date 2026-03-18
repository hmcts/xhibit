package uk.gov.courtservice.framework.security.providers.authentication;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceConfig;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryServiceFactory;
import uk.gov.courtservice.framework.security.providers.authorization.LDAPContextFactory;
import weblogic.management.security.ProviderMBean;
import weblogic.security.provider.PrincipalValidatorImpl;
import weblogic.security.spi.AuthenticationProviderV2;
import weblogic.security.spi.IdentityAsserterV2;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityProvider;
import weblogic.security.spi.SecurityServices;

/**
 * As boundary class requires paranoid logging.
 * 
 * @author Will Fardell, XDevelopment
 */
public class XhibitAuthenticationProviderImpl implements AuthenticationProviderV2 {
    private static final Logger log = Logger.getLogger(XhibitAuthenticationProviderImpl.class);

    private String description;

    private LoginModuleControlFlag controlFlag;

    private ActiveDirectoryService activeDirectoryService;

    /**
     * Construct a new instance of the XhibitAuthenticationProviderImpl.
     */
    public XhibitAuthenticationProviderImpl() {
        if (log.isDebugEnabled()) {
            log.debug("constructor called & success.");
        }
    }

    /**
     * Initialize the Xhibit Authenticator.
     * 
     * @param mbean
     *            A ProviderMBean that holds the Xhibit Authenticator's
     *            configuration data. This mbean must be an instance of the
     *            Xhibit Authenticator's mbean.
     * 
     * @param services
     *            The SecurityServices gives access to the auditor so that the
     *            provider can to post audit events. The Xhibit Authenticator
     *            doesn't use this parameter.
     * 
     * @see SecurityProvider
     */
    public void initialize(ProviderMBean mbean, SecurityServices services) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("initialize called.");
            }
            _initialize(mbean);
            if (log.isDebugEnabled()) {
                log.debug("initialize success.");
            }
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("initialize exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("initialize error.", e);
            }
            throw e;
        }
    }

    private void _initialize(ProviderMBean mbean) {
        if (!(mbean instanceof XhibitAuthenticatorMBean)) {
            throw new IllegalArgumentException("mbean: " + mbean);
        }
        XhibitAuthenticatorMBean xhibitAuthenticatorMBean = (XhibitAuthenticatorMBean) mbean;

        this.description = xhibitAuthenticatorMBean.getDescription() + " - " + xhibitAuthenticatorMBean.getVersion();
        this.controlFlag = parseControlFlag(xhibitAuthenticatorMBean.getControlFlag());
        this.activeDirectoryService = getActiveDirectoryService(xhibitAuthenticatorMBean);
    }

    private boolean isInitialized() {
        // Check last initialization step has been completed
        return activeDirectoryService != null;
    }

    /**
     * Shutdown the Xhibit authenticator. Free any references set by
     * initialzation
     */
    public void shutdown() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("shutdown called.");
            }
            _shutdown();
            if (log.isDebugEnabled()) {
                log.debug("shutdown success.");
            }
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("shutdown exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("shutdown error.", e);
            }
            throw e;
        }
    }

    private void _shutdown() {
        activeDirectoryService = null;
        controlFlag = null;
        description = null;
    }

    /**
     * Returns this providers identity asserter object.
     * 
     * @return null since the Xhibit Authenticator doesn't support identity
     *         assertion (that is, mapping a token to a user name). Do not
     *         confuse this with using a login module in identity assertion mode
     *         where the login module shouldn't try to validate the user.
     */
    public IdentityAsserterV2 getIdentityAsserter() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getIdentityAsserter called.");
            }
            IdentityAsserterV2 identityAsserter = _getIdentityAsserter();
            if (log.isDebugEnabled()) {
                log.debug("getIdentityAsserter success: " + identityAsserter + ".");
            }
            return identityAsserter;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getIdentityAsserter exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getIdentityAsserter error.", e);
            }
            throw e;
        }
    }

    private IdentityAsserterV2 _getIdentityAsserter() {
        if (!isInitialized()) {
            throw new IllegalStateException("XhibitAuthenticationProviderImpl not initialized.");
        }
        return null;
    }

    /**
     * Create a JAAS AppConfigurationEntry (which tells JAAS how to create the
     * login module and how to use it). The XhibitAuthenticationProviderImpl
     * uses the XhibitLoginModuleImpl this requires an
     * XhibitAuthenticationService and a flag to be set to indicate if just
     * asserting.
     * 
     * @return An AppConfigurationEntry that tells JAAS how to use the simple
     *         sample authenticator's login module for authentication.
     */
    public AppConfigurationEntry getLoginModuleConfiguration() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getLoginModuleConfiguration called.");
            }
            AppConfigurationEntry appConfigurationEntry = _getLoginModuleConfiguration();
            if (log.isDebugEnabled()) {
                log.debug("getLoginModuleConfiguration success: " + appConfigurationEntry + ".");
            }
            return appConfigurationEntry;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getLoginModuleConfiguration exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getLoginModuleConfiguration error.", e);
            }
            throw e;
        }
    }

    private AppConfigurationEntry _getLoginModuleConfiguration() {
        if (!isInitialized()) {
            throw new IllegalStateException("XhibitAuthenticationProviderImpl not initialized.");
        }
        return createAppConfigurationEntry(false);
    }

    /**
     * Create a JAAS AppConfigurationEntry (which tells JAAS how to create the
     * login module and how to use it). The XhibitAuthenticationProviderImpl
     * uses the XhibitLoginModuleImpl this requires an
     * XhibitAuthenticationService and a flag to be set to indicate if just
     * asserting.
     * 
     * @return An AppConfigurationEntry that tells JAAS how to use the simple
     *         sample authenticator's login module for identity assertion.
     */
    public AppConfigurationEntry getAssertionModuleConfiguration() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getAssertionModuleConfiguration called.");
            }
            AppConfigurationEntry appConfigurationEntry = _getAssertionModuleConfiguration();
            if (log.isDebugEnabled()) {
                log.debug("getAssertionModuleConfiguration success: " + appConfigurationEntry + ".");
            }
            return appConfigurationEntry;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getAssertionModuleConfiguration exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getAssertionModuleConfiguration error.", e);
            }
            throw e;
        }
    }

    private AppConfigurationEntry _getAssertionModuleConfiguration() {
        if (!isInitialized()) {
            throw new IllegalStateException("XhibitAuthenticationProviderImpl not initialized.");
        }
        return createAppConfigurationEntry(true);
    }

    private AppConfigurationEntry createAppConfigurationEntry(boolean assertion) {
        return new AppConfigurationEntry(XhibitAuthenticationLoginModuleImpl.class.getName(), controlFlag,
                createConfigurationOptions(assertion));
    }

    private Map createConfigurationOptions(boolean assertion) {
        Map options = new HashMap();
        options.put(XhibitAuthenticationLoginModuleImpl.ASSERTION_FLAG_KEY, assertion ? Boolean.TRUE : Boolean.FALSE);

        options.put(XhibitAuthenticationLoginModuleImpl.ACTIVE_DIRECTORY_SERVICE_KEY, activeDirectoryService);
        return options;
    }

    /**
     * Return the principal validator that can validate the principals that the
     * authenticator's login module puts into the subject.
     * 
     * Since the Xhibit Authenticator uses the built in WLSUserImpl and
     * WLSGroupImpl principal classes, just returns the built in
     * PrincipalValidatorImpl that knows how to handle these kinds of
     * principals.
     * 
     * @return A PrincipalValidator that can validate the principals that the
     *         Xhibit Authenticator's login module puts in the subject.
     */
    public PrincipalValidator getPrincipalValidator() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getPrincipalValidator called.");
            }
            PrincipalValidator principalValidator = _getPrincipalValidator();
            if (log.isDebugEnabled()) {
                log.debug("getPrincipalValidator success: " + principalValidator + ".");
            }
            return principalValidator;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getPrincipalValidator exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getPrincipalValidator error.", e);
            }
            throw e;
        }
    }

    private PrincipalValidator _getPrincipalValidator() {
        return new PrincipalValidatorImpl();
    }

    /**
     * Get a description of the provider.
     * 
     * @return a description of the provider
     */
    public String getDescription() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("getDescription called.");
            }
            String description = _getDescription();
            if (log.isDebugEnabled()) {
                log.debug("getDescription success: \"" + description + "\".");
            }
            return description;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getDescription exception", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("getDescription error.", e);
            }
            throw e;
        }
    }

    private String _getDescription() {
        if (!isInitialized()) {
            throw new IllegalStateException("XhibitAuthenticationProviderImpl not initialized.");
        }
        return description;
    }

    private static LoginModuleControlFlag parseControlFlag(String controlFlag) {
        if ("REQUISITE".equalsIgnoreCase(controlFlag)) {
            return LoginModuleControlFlag.REQUISITE;
        }
        if ("SUFFICIENT".equalsIgnoreCase(controlFlag)) {
            return LoginModuleControlFlag.SUFFICIENT;
        }
        if ("OPTIONAL".equalsIgnoreCase(controlFlag)) {
            return LoginModuleControlFlag.OPTIONAL;
        }
        return LoginModuleControlFlag.REQUIRED;
    }

    /**
     * Get the active directory service configured by this mbean.
     * 
     * @param mbean
     *            the mbean to get the service for
     * @return the service
     */
    public static ActiveDirectoryService getActiveDirectoryService(XhibitAuthenticatorMBean mbean) {
        return ActiveDirectoryServiceFactory.getActiveDirectoryService(getConfig(mbean));
    }

    /**
     * 
     * The MBean uses a complex and unpredictable proxy mechanism, the purpose
     * of this class is to extract the configuration from the mbean. After this
     * the mbean is no longer required and can be descarded. Construct a new
     * config from the mbean. Note how the mbean is discarded imediatly!
     * 
     * @param mbean
     * @return the new config
     */
    public static ActiveDirectoryServiceConfig getConfig(XhibitAuthenticatorMBean mbean) {
        Context ldapctx = LDAPContextFactory.getLDAPContext();
        ActiveDirectoryServiceConfig adsc = null;
        try {
            Hashtable<String, String> ldapctxHash = (Hashtable) ldapctx.getEnvironment();
            adsc = new ActiveDirectoryServiceConfig(mbean.getHost(), mbean.getPort(), mbean.getPrincipal(),
                    mbean.getCredential(), mbean.getConnectionPoolSize(), mbean.isCacheEnabled(), mbean.getCacheSize(), mbean.getCacheTTL(), mbean.isFollowReferrals(),
                    mbean.isBindAnonymouslyOnReferrals(), mbean.getResultsTimeLimit(), mbean.getConnectTimeout(), mbean.getParallelConnectDelay(),
                    mbean.getUserBaseDN(), ActiveDirectoryServiceConfig.parseScope(mbean.getUserSearchScope()),
                    mbean.getUserFromNameFilter(),
                    ldapctxHash.get(LDAPContextFactory.LUPA),
                    ldapctxHash.get(LDAPContextFactory.LUDA),
                    ldapctxHash.get(LDAPContextFactory.LUFA),
                    ldapctxHash.get(LDAPContextFactory.LUSA),
                    mbean.getGroupBaseDN(),
                    ActiveDirectoryServiceConfig.parseScope(mbean.getGroupSearchScope()), mbean.getGroupFromNameFilter(),
                    ldapctxHash.get(LDAPContextFactory.LGPA),
                    ldapctxHash.get(LDAPContextFactory.LGMA),
                    ldapctxHash.get(LDAPContextFactory.LGRP));
            /*return new ActiveDirectoryServiceConfig(mbean.getHost(), mbean.getPort(), mbean.getPrincipal(),
                    mbean.getCredential(), mbean.getConnectionPoolSize(), mbean.isCacheEnabled(), mbean.getCacheSize(), mbean.getCacheTTL(), mbean.isFollowReferrals(),
                    mbean.isBindAnonymouslyOnReferrals(), mbean.getResultsTimeLimit(), mbean.getConnectTimeout(), mbean.getParallelConnectDelay(),
                    mbean.getUserBaseDN(), ActiveDirectoryServiceConfig.parseScope(mbean.getUserSearchScope()),
                    mbean.getUserFromNameFilter(), LDAPContextUtil.getUserPrincipalAttribute(),
                    LDAPContextUtil.getUserDisplayAttribute(), LDAPContextUtil.getUserForenameAttribute(),
                    LDAPContextUtil.getUserSurnameAttribute(), mbean.getGroupBaseDN(),
                    ActiveDirectoryServiceConfig.parseScope(mbean.getGroupSearchScope()), mbean.getGroupFromNameFilter(),
                    LDAPContextUtil.getGroupPrincipalAttribute(),
                    LDAPContextUtil.getGroupMemberOfAttribute(),
                    LDAPContextUtil.getGroupRootPrincipal());*/
        } catch (NamingException ne) {
            ne.printStackTrace();
        } finally {
            return adsc;
        }
    }
}
