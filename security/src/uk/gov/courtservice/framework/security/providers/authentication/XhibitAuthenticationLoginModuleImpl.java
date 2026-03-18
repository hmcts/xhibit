package uk.gov.courtservice.framework.security.providers.authentication;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryGroup;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryService;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryUser;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

/**
 * As boundary class requires paranoid logging.
 * 
 * @author Will Fardell, XDevelopment
 */
public class XhibitAuthenticationLoginModuleImpl implements LoginModule {
    private static final Logger log = Logger.getLogger(XhibitAuthenticationLoginModuleImpl.class);

    /**
     * Mandatory Option Key used to indicate if the module is being used for.
     * Value must be of type Boolean.
     */
    public static final String ASSERTION_FLAG_KEY = "XhibitLoginModule.assertionFlag";

    /**
     * Mandatory Option Key used to pass the authentication service. Value must
     * be of type XhibitAuthenticationService.
     */
    public static final String ACTIVE_DIRECTORY_SERVICE_KEY = "XhibitLoginModule.authenticatorService";

    // True if only asserting user exists
    private boolean assertionOnly;

    // True if the login has been completed successfully
    private boolean loginSucceeded;

    // True if the login has been commited successfully
    private boolean commitSucceeded;

    // The service which performs the authentication
    private ActiveDirectoryService activeDirectoryService;

    // The subject to populate
    private Subject subject;

    // The callback handler to use
    private CallbackHandler callbackHandler;

    // The xhibit user
    private ActiveDirectoryUser user;

    // The xhibit groups
    private ActiveDirectoryGroup[] groups;

    /**
     * Construct a new instance
     */
    public XhibitAuthenticationLoginModuleImpl() {
        if (log.isDebugEnabled()) {
            log.debug("constructor called & success.");
        }
    }

    /**
     * Initialize the module.
     * 
     * @param subject
     *            the Subject to be authenticated.
     * 
     * @param callbackHandler
     *            a CallbackHandler for communicating with the end user
     *            (prompting for usernames and passwords, for example).
     * 
     * @param sharedState -
     *            state shared with other configured LoginModules.
     * 
     * @param options
     *            options specified in the login Configuration for this
     *            particular LoginModule. Options must contain the assertion
     *            flag and the authentication service.
     */
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("initialize called.");
            }
            _initialize(subject, callbackHandler, sharedState, options);
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

    private void _initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {

        // Check Arguments
        if (subject == null) {
            throw new IllegalArgumentException("subject: null");
        }
        if (callbackHandler == null) {
            throw new IllegalArgumentException("callbackHandler: null");
        }
        if (sharedState == null) {
            throw new IllegalArgumentException("sharedState: null");
        }
        if (options == null) {
            throw new IllegalArgumentException("options: null");
        }

        // Get the options
        activeDirectoryService = getActiveDirectoryService(options);
        assertionOnly = getAssertionFlag(options);

        // Store the subject and handler subject last as used for initialization
        // check
        this.callbackHandler = callbackHandler;
        this.subject = subject;
    }

    /**
     * @return true if the module has been initialized
     */
    public boolean isInitialized() {
        // Check last initialization step has been completed
        return subject != null;
    }

    /**
     * Method to authenticate a Subject (phase 1). The implementation of this
     * method authenticates a Subject. For example, it may prompt for Subject
     * information such as a username and password and then attempt to verify
     * the password. This method saves the result of the authentication attempt
     * as private state within the LoginModule.
     * 
     * @return true if the authentication succeeded, or false if this
     *         LoginModule should be ignored.
     */
    public boolean login() throws LoginException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("login called.");
            }
            boolean success = _login();
            if (log.isDebugEnabled()) {
                log.debug("login " + (success ? "success." : "ignore."));
            }
            return success;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("login exception.", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("login error.", e);
            }
            throw e;
        }
    }

    public boolean _login() throws LoginException {
        if (!isInitialized()) {
            throw new LoginException("XhibitLoginModuleImpl not initialized.");
        }

        try {
            NameCallback nameCallback = new NameCallback("username: ");
            if (assertionOnly) {
                if (log.isDebugEnabled()) {
                    log.debug("login assert.");
                }
                callbackHandler.handle(new Callback[] { nameCallback });
                return assertUser(nameCallback.getName());
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("login authenticate.");
                }
                PasswordCallback passwordCallback = new PasswordCallback("password: ", false);
                callbackHandler.handle(new Callback[] { passwordCallback, nameCallback });
                return authenticateUser(nameCallback.getName(), new String(passwordCallback.getPassword()));
            }
        } catch (IOException ioe) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("An IO error occured during login.", ioe);
            }
            throw new LoginException("Login Error: " + ioe);
        } catch (UnsupportedCallbackException uce) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("A callback error occured during login.", uce);
            }
            throw new LoginException("Login Error: " + uce + " " + uce.getCallback());
        } catch (MBeanException mbe) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("An mbean error occured during login.", mbe);
            }
            throw new LoginException("Login Error: " + mbe);
        }
    }

    private boolean assertUser(String userName) throws LoginException, MBeanException {
        // Assert user exists
        user = activeDirectoryService.getUser(userName);
        if (user == null) {
            throw new FailedLoginException("Assertion Failed: User " + userName + " does not exist.");
        }

        groups = activeDirectoryService.getGroups(user);

        // Update state, and return.
        loginSucceeded = true;
        return true;

    }

    private boolean authenticateUser(String userName, String userCredential) throws LoginException, MBeanException {
        // Assert user exists, credentials match and retrieve groups
        user = activeDirectoryService.getUser(userName);
        if (user == null) {
            throw new FailedLoginException("Authentication Failed: User " + userName + " does not exist.");
        }

        if (!activeDirectoryService.authenticateUser(user, userCredential)) {
            throw new FailedLoginException("Authentication Failed: User " + userName + " credentials invalid.");
        }

        groups = activeDirectoryService.getGroups(user);

        // Update state, and return.
        loginSucceeded = true;
        return true;
    }

    /**
     * This method is called if the LoginContext's overall authentication
     * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
     * LoginModules succeeded).
     * 
     * If this LoginModule's own authentication attempt succeeded (checked by
     * retrieving the private state saved by the login method), then this method
     * associates relevant Principals and Credentials with the Subject located
     * in the LoginModule. If this LoginModule's own authentication attempted
     * failed, then this method removes/destroys any state that was originally
     * saved.
     * 
     * @return true if this method succeeded, or false if this LoginModule
     *         should be ignored.
     */
    public boolean commit() throws LoginException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("commit called.");
            }
            boolean success = _commit();
            if (log.isDebugEnabled()) {
                log.debug("commit " + (success ? "success." : "ignore."));
            }
            return success;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("commit exception.", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("commit error.", e);
            }
            throw e;
        }
    }

    private boolean _commit() throws LoginException {
        if (!isInitialized()) {
            throw new LoginException("XhibitLoginModuleImpl not initialized.");
        }

        if (loginSucceeded) {
            Set principles = subject.getPrincipals();

            // Add User Principle
            if (user != null) {
                principles.add(new WLSUserImpl(user.getPrincipalName()));
            }
            // Add Group Principle
            if (groups != null) {
                for (int i = 0; i < groups.length; i++) {
                    principles.add(new WLSGroupImpl(groups[i].getPrincipalName()));
                }
            }
            // Update State, Log and return
            commitSucceeded = true;
            return true;
        }

        // Clear any users and groups
        user = null;
        groups = null;

        // Update State and return
        commitSucceeded = false;
        return false;
    }

    /**
     * Method to abort the authentication process (phase 2). This method is
     * called if the LoginContext's overall authentication failed. (the relevant
     * REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did not
     * succeed).
     * 
     * If this LoginModule's own authentication attempt succeeded (checked by
     * retrieving the private state saved by the login method), then this method
     * cleans up any state that was originally saved.
     * 
     * @return true if this method succeeded, or false if this LoginModule
     *         should be ignored.
     */
    public boolean abort() throws LoginException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("abort called.");
            }
            boolean success = _abort();
            if (log.isDebugEnabled()) {
                log.debug("abort " + (success ? "success." : "ignore."));
            }
            return success;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("abort exception.", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("abort error.", e);
            }
            throw e;
        }
    }

    private boolean _abort() throws LoginException {
        if (!isInitialized()) {
            throw new LoginException("XhibitLoginModuleImpl not initialized.");
        }

        boolean abortSucceeded = loginSucceeded;

        // Reset State
        commitSucceeded = false;
        loginSucceeded = false;
        user = null;
        groups = null;

        return abortSucceeded;
    }

    /**
     * Method which logs out a Subject
     * 
     * @return true if this method succeeded, or false if this LoginModule
     *         should be ignored.
     */
    public boolean logout() throws LoginException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("logout called.");
            }
            boolean success = _logout();
            if (log.isDebugEnabled()) {
                log.debug("logout " + (success ? "success." : "ignore."));
            }
            return success;
        } catch (RuntimeException e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("logout exception.", e);
            }
            throw e;
        } catch (Error e) {
            if (log.isEnabledFor(Level.ERROR)) {
                log.error("logout error.", e);
            }
            throw e;
        }
    }

    private boolean _logout() throws LoginException {
        if (!isInitialized()) {
            throw new LoginException("XhibitLoginModuleImpl not initialized.");
        }

        boolean logoutSucceeded = commitSucceeded;

        // Reset State
        commitSucceeded = false;
        loginSucceeded = false;
        user = null;
        groups = null;

        return logoutSucceeded;

    }

    /**
     * Interigate the options
     * 
     * @param options
     *            the initialisation options
     * @return true if the assertion flag is set in the options, else false
     * @throws IllegalArgumentException
     *             if the option can not be found
     */
    private static final boolean getAssertionFlag(Map options) {
        Object option = options.get(ASSERTION_FLAG_KEY);
        if (log.isDebugEnabled()) {
            log.debug("Option " + ASSERTION_FLAG_KEY + ": " + option);
        }
        if (option instanceof Boolean) {
            return ((Boolean) option).booleanValue();
        }
        throw new IllegalArgumentException("options[" + ASSERTION_FLAG_KEY + "]: null");
    }

    /**
     * Interigate the options
     * 
     * @param options
     *            the initialisation options
     * @return the XhibitAuthenticatorService to use
     * @throws IllegalArgumentException
     *             if the option can not be found
     */
    private static final ActiveDirectoryService getActiveDirectoryService(Map options) {
        Object option = options.get(ACTIVE_DIRECTORY_SERVICE_KEY);
        if (log.isDebugEnabled()) {
            log.debug("Option " + ACTIVE_DIRECTORY_SERVICE_KEY + ": " + option);
        }
        if (option instanceof ActiveDirectoryService) {
            return (ActiveDirectoryService) option;
        }
        throw new IllegalArgumentException("options[" + ACTIVE_DIRECTORY_SERVICE_KEY + "]: null");
    }

}
