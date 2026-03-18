package uk.gov.courtservice.framework.security.login;

/**
 * <p>Title: </p>
 * <p>This is a helper class for performing JAAS login </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import uk.gov.courtservice.framework.services.CSServices;

public class JAASLoginHelper {

    /**
     * This is the key under which the default login configuration name is
     * stored
     */
    public static final String DEFAULT_CONFIG_KEY = "defaultLoginConfig";

    /**
     * Login context to use
     */
    private LoginContext loginContext;

    /**
     * Initializes the callback handler and JAAS configuration
     */
    public JAASLoginHelper() throws LoginException {
        this(null, CSServices.getConfigServices().getProperty(DEFAULT_CONFIG_KEY));
    }

    /**
     * Initializes the callback handler and JAAS configuration file
     * 
     * @param callbackHandler
     * @param loginConfig
     */
    public JAASLoginHelper(CallbackHandler callbackHandler) throws LoginException {
        this(callbackHandler, CSServices.getConfigServices().getProperty(DEFAULT_CONFIG_KEY));
    }

    /**
     * Initializes the callback handler and JAAS configuration file
     * 
     * @param callbackHandler
     * @param loginConfig
     */
    public JAASLoginHelper(CallbackHandler callbackHandler, String loginConfig) throws LoginException {

        if (callbackHandler != null)
            loginContext = new LoginContext(loginConfig, callbackHandler);
        else
            loginContext = new LoginContext(loginConfig);

    }

    /**
     * The method performs a login and returns the authenticated subject
     * 
     * @return
     * @throws LoginException
     */
    public Subject login() throws LoginException {

        loginContext.login();
        return loginContext.getSubject();

    }

    /**
     * The method performs a logout
     * 
     * @return
     * @throws LoginException
     */
    public void logout() throws LoginException {
        loginContext.logout();
    }

}