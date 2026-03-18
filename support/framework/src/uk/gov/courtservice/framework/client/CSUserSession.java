package uk.gov.courtservice.framework.client;

//jdk
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Paul Grove
 * @version 1.0
 */
/*
 * 
 * Ref Date Author Description
 * 
 * PRE00121 06-10-2003 AW Daley isUserInGroup method added
 * 
 */
public interface CSUserSession {

    /**
     * Returns whether the user has access to the specified functionality
     * 
     * @param functionality
     * @return
     */
    public boolean hasAccess(String functionality);

    /**
     * Returns whether the user has access to specified list of functionalities
     * 
     * @param funtionality
     * @return
     */
    public boolean hasAccess(List funtionality);

    /**
     * Returns the list of functionalities
     * 
     * @return
     */
    public List getFunctionality();

    /**
     * Logs in the user
     * 
     * @throws LoginException
     */
    public void login() throws LoginException, CSRecoverableException;

    /**
     * Logs out the user
     * 
     * @throws LoginException
     */
    public void logout() throws LoginException;

    /**
     * Returns whether the user is logged in
     * 
     * @return
     */
    public boolean isLoggedIn();

    /**
     * Gets a unique session id
     * 
     * @return
     */
    public String getSessionID();

    /**
     * Gets a session property
     * 
     * @param key
     * @return
     */
    public String getSessionProperty(SessionPropertyKey key);

    /**
     * Reloads the session information
     */
    public void updateSessionInfo() throws CSRecoverableException;

    /**
     * Gets a user profile property
     * 
     * @param Key
     * @return
     */
    public String getUserProfileProperty(String Key);

    /**
     * Gets the currently authenticated server name
     * 
     * @return
     */
    public String getServer();

    /**
     * Sets the currently authenticated server name
     * 
     * @param serverName
     */
    public void setServer(String serverName);

    /**
     * Gets the list of available servers
     * 
     * @return
     */
    public Set getServers();

    /**
     * Gets the currently authenticated subject
     * 
     * @return
     */
    public Subject getUserSubject();

    /**
     * Gets the user name for the currently authenticated subject
     * 
     * @return
     */
    public String getUserName();

    /**
     * Sets the login config
     * 
     * @param loginConfig
     * @return
     */
    public void setLoginConfig(String loginConfig);

    /**
     * Sets the callback handler for login
     * 
     * @param callbackHandler
     */
    public void setCallbackHandler(CallbackHandler callbackHandler);

    /**
     * Determines if the user is a member of the specified group
     * 
     * @param group
     * @return
     */
    public boolean isUserInGroup(String group);

    /**
     * Returns the country of the court, where the users terminal is located
     * 
     * @param none
     * @return
     */
    public String getCountry();

    /**
     * Returns the language of the court, where the users terminal is located
     * 
     * @param none
     * @return
     */
    public String getLanguage();

    public Long getUserLoginId();
    
    public void setUserLoginId(Long userLoginId);
}