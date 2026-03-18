package uk.gov.courtservice.framework.security.login;

/**
 * <p>Title: </p>
 * <p>Description: This class provides a user, password, URL based callback</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import uk.gov.courtservice.framework.security.ServerRepository;
import weblogic.security.auth.callback.URLCallback;

public class UserNamePasswordURLCallbackHandler implements CallbackHandler {

    /**
     * User name
     */
    private String userName = null;

    /**
     * Password
     */
    private String password = null;

    /**
     * URL
     */
    private String url = null;

    /**
     * Constructor initializes the user name and password
     * 
     * @param userName
     * @param password
     */
    public UserNamePasswordURLCallbackHandler(String userName, String password) {
        this(userName, password, ServerRepository.getAuthenticationURL(ServerRepository.DFEUALT_SERVER));
    }

    /**
     * Constructor initializes the user name, password and URL
     * 
     * @param userName
     * @param password
     * @param url
     */
    public UserNamePasswordURLCallbackHandler(String userName, String password, String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
    }

    /**
     * Handles the callback
     * 
     * @param callbacks
     * @throws UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            // User name
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName(userName);
            }
            // Password
            else if (callbacks[i] instanceof URLCallback) {
                URLCallback uc = (URLCallback) callbacks[i];
                uc.setURL(url);
            }
            // Authentication URL
            else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }

        }

    }

}