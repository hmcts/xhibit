package uk.gov.courtservice.xhibit.client.util.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import uk.gov.courtservice.xhibit.client.util.security.TerminalIDFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.framework.client.SessionPropertyKey;
import uk.gov.courtservice.framework.security.ServerRepository;
import uk.gov.courtservice.framework.security.activedirectory.ActiveDirectoryUser;
import uk.gov.courtservice.framework.security.login.JAASLoginHelper;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.scheduling.AsynchronousLoader;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.TerminalNotFoundException;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserLoggedInElsewhereException;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.version.VersionControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

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
 * @author Meeraj Kunnumpurath DISCLAIMER: None of the authorization related
 *         code in this class is mine. DISCLAIMER: All authorizarion, session
 *         properties and user profile DISCLAIMER: properties related code will
 *         be changed (Meeraj)
 * @version 1.0
 */
/*
 * 
 * Ref Date Author Description
 * 
 * PRE00121 06-10-2003 AW Daley isUserInGroup method added
 * 
 */
public class CSUserSessionImpl implements CSUserSession, RoamingTerminalInterface {

    /**
     * Logger
     */
    private static final Logger log = CSServices.getLogger(CSUserSessionImpl.class);

    /**
     * Callback handler for this session
     */
    private CallbackHandler callbackHandler;

    /**
     * Currently authenticated subject
     */
    private Subject subject;

    /**
     * Currently authenticated server
     */
    private String serverName = ServerRepository.DFEUALT_SERVER;

    /**
     * Login configuration to use
     */
    private String loginConfig = CSServices.getConfigServices().getProperty(JAASLoginHelper.DEFAULT_CONFIG_KEY);

    /**
     * JAAS login helper for this session
     */
    private JAASLoginHelper loginHelper;

    /**
     * List of dunctionality assigned to users
     */
    private List<String> userFunctionality = new ArrayList<String>();

    /**
     * Session properties
     */
    private SessionPropertiesMap sessionProperties = new SessionPropertiesMap();
    
    private Long userLoginId = null;

    /**
     * Constructor initializes the callback handler
     * 
     * @param callbackHandler
     */
    public CSUserSessionImpl(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    /**
     * Empty
     * 
     * @param callbackHandler
     */
    public CSUserSessionImpl() {
        // empty
    }

    /**
     * Returns whether the user has access to the specified functionality
     * 
     * @param functionality
     * @return
     */
    public boolean hasAccess(String functionality) {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        return userFunctionality.contains(functionality);
    }

    /**
     * Returns whether the user has access to specified list of functionalities
     * 
     * @param funtionality
     * @return
     */
    public boolean hasAccess(List funtionality) {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        Iterator iterator = funtionality.iterator();
        while (iterator.hasNext())
            if (userFunctionality.contains(iterator.next()))
                return true;

        return false;
    }

    /**
     * Returns the list of functionalities
     * 
     * @return
     */
    public List getFunctionality() {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        return userFunctionality;
    }

    /**
     * Logs in the user
     * 
     * @throws LoginException
     */
    public void login() throws LoginException, TerminalNotFoundException, UserLoggedInElsewhereException {
        //try{
        if (callbackHandler != null)
            loginHelper = new JAASLoginHelper(callbackHandler, loginConfig);
        else
            loginHelper = new JAASLoginHelper();
        
        // Check the terminal is registered correctly
        String terminalName = getTerminalID();
        // Note if there is no entry in XHB_TERMINAL_DEFAULT then 'null' will be returned so that will have to be handled properly here!
        XhbTerminalDefaultBasicValue[] terminalDefault = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance().getTerminalDefaultByName(terminalName);
        if (terminalDefault.length == 0) {
            // Error - if not handled will cause a generic error rather than specifically about terminal not registered correctly
            throw new TerminalNotFoundException(terminalName);
        }
        
        subject = loginHelper.login();
        //}catch(LoginException ex){
            //Login failed, register this with midtier
            //unsuccessfulLogin(ex);
            //throw ex;
        //}
        if (log.isDebugEnabled()) {
            try {
                log.debug("Logged in subject " + XhibitDelegateHelper.getUserTerminalDelegate().getSubjectDetails(subject) + ".");
            } catch (Exception e) {
                log.debug("Logged in subject : test");
                e.printStackTrace();
            }
            
        }
        
        try{
            // Initalise
            initSession();
            
            setUserLoginId(XhibitDelegateHelper.getUserTerminalDelegate().createNewUserTerminalSession(getUserName(), getTerminalID()));

        }catch(UserLoggedInElsewhereException e){
            unsuccessfulLogin(e);
            throw e;
        }catch(TerminalNotFoundException e){
            unsuccessfulLogin(e);
            throw e;
        }
        

        // Profile the security if enabled
        // This step is expensive so should be disabled by default
        if (SecurityProfiler.enabled()) {
            SecurityProfiler.profile();
        }

        // Once an user has successfully logged in can start AsynchronousLoader
        AsynchronousLoader.getInstance().init();
    }
    
    private void unsuccessfulLogin(Exception e) throws LoginException{
        XhibitDelegateHelper.getUserTerminalDelegate().logUnsuccessfulLoginAttempt(getTerminalID(),e, sessionProperties.get(UserTerminalProperties.DISPLAY_NAME).toString());
        _logout();
    }

    /**
     * Logs out the user
     * 
     * @throws LoginException
     */
    public void logout() throws LoginException {
        if (log.isDebugEnabled()) {
            //String subjectDetails = XhibitDelegateHelper.getUserTerminalDelegate().getSubjectDetails();
            _logout();
            //log.debug("Logged out subject " + subjectDetails + ".");
        } else {
            _logout();
        }
    }

    private void _logout() throws LoginException {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");
            
       if(userLoginId != null){
    	   	XhibitDelegateHelper.getUserTerminalDelegate().removeUserTerminalSession(getUserLoginId());
       }
        // Stop AsynchronousLoader before logging out.
        AsynchronousLoader.getInstance().finishLoading();

        loginHelper.logout();
        subject = null;
    }

    /**
     * Returns whether the user is logged in
     * 
     * @return
     */
    public boolean isLoggedIn() {
        return subject != null;
    }

    /**
     * Gets a unique session id
     * 
     * @return
     */
    public String getSessionID() {
        return "TestSessionID";
    }

    /**
     * Gets a session property
     * 
     * @param key
     * @return
     */
    public String getSessionProperty(SessionPropertyKey key) {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");
        Object obj = sessionProperties.get(key);
        return (obj == null ? null : obj.toString());
    }

    /**
     * Reloads the session information
     */
    public void updateSessionInfo() throws TerminalNotFoundException {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        initSession();
        if (roamingValue != null)
            updateRoamingTerminalProperties(roamingValue);
    }

    /**
     * Gets a user profile property
     * 
     * @param Key
     * @return
     */
    public String getUserProfileProperty(String key) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Gets the currently authenticated server name
     * 
     * @return
     */
    public String getServer() {
        return serverName;
    }

    /**
     * Sets the currently authenticated server name
     * 
     * @param serverName
     */
    public void setServer(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Gets the list of available servers
     * 
     * @return
     */
    public Set getServers() {
        return ServerRepository.getServers();
    }

    /**
     * Gets the currently authenticated subject
     * 
     * @return
     */
    public Subject getUserSubject() {
        if (isLoggedIn()) {
            return subject;
        }
        return null;
    }

    /**
     * Gets the user name for the currently authenticated subject
     * 
     * @return
     */
    public String getUserName() {
        if (isLoggedIn()) {
            return getSessionProperty(UserTerminalProperties.USER_NAME);
        }
        throw new IllegalStateException("Not logged in.");
    }

    /**
     * Sets the login config
     * 
     * @param loginConfig
     * @return
     */
    public void setLoginConfig(String loginConfig) {
        this.loginConfig = loginConfig;
    }

    /**
     * Sets the callback handler for login
     * 
     * @param callbackHandler
     */
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    /**
     * Determines if the user is a member of the specified group
     * 
     * @param group
     * @return
     */
    public boolean isUserInGroup(String group) {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        return XhibitDelegateHelper.getUserTerminalDelegate().isUserInGroup(group, subject);
    }

    private RoamingTerminalValue roamingValue;

    public void updateRoamingTerminalProperties(RoamingTerminalValue value) {
        roamingValue = value;
        updateSession(UserTerminalProperties.COURT_ID, value.getCourtId());
        updateSession(UserTerminalProperties.COURT_SITE_ID, value.getCourtSiteId());
        updateSession(UserTerminalProperties.COURT_ROOM_ID, value.getCourtRoomId());
        updateSession(UserTerminalProperties.COURT_NAME, value.getCourtName());
        updateSession(UserTerminalProperties.TERMINAL_LOCATION, value.getTerminalLocation());
    }

    private void updateSession(UserTerminalProperties prop, Object value) {
        if (value != null) {
            sessionProperties.put(prop, value);
        }
    }

    /**
     * Initializes the session
     * 
     * @param terminalID
     */
    private void initSession() throws TerminalNotFoundException {
        // Get the user session delegate
        UserTerminalControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getUserTerminalDelegate();

        // Get the list of functionalities available for the current subject
        String functionalities[] = delegate.getFunctionalities(subject);

        userFunctionality.clear();
        for (int i = 0; functionalities != null && i < functionalities.length; i++) {
            userFunctionality.add(functionalities[i]);
        }

        // Get the session property for the terminal
        sessionProperties.clear();
        sessionProperties = delegate.getXHIBITTerminalLocation(getUserName(subject), getTerminalID());
        if (log.isDebugEnabled()) {
            log.debug("Session Property Debug ::");
            log.debug("Keys:" + java.util.Arrays.asList(sessionProperties.keySet().toArray()).toString());
            log.debug("Values:" + java.util.Arrays.asList(sessionProperties.values().toArray()).toString());
        }

    }
    
    private String getUserName(Subject subject) {
        String userName = "";
        Iterator iter = subject.getPrincipals().iterator();
        if (iter.hasNext()) {
            userName = iter.next().toString();
        } else {
            userName = "Unavailable";
        }

        return userName;
    }

    /**
     * Gets the terminal identifier
     */
    private String getTerminalID() {
        log.debug("looking up terminal ID");
        /* The default factory will just use 
         *  InetAddress.getLocalHost().getHostName();
         * however, for testing, a custom factory can be
         * provided that is able to return terminal IDs
         * specified in the xhibit.bat file.  For instructions see
         * XHIBIT/tools/util/terminal_id_chooser/README.txt
         */
        String hostname = TerminalIDFactory.getInstance().getTerminalID();
        log.debug("terminal ID=" + hostname);
        return hostname;
    }

    /**
     * Gets the country of the court where the users terminal is located
     * 
     * @return the country
     */
    public String getCountry() {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        return getSessionProperty(UserTerminalProperties.COUNTRY);
    }

    /**
     * Gets the language of the court where the users terminal is located
     * 
     * @return the country
     */
    public String getLanguage() {
        if (!isLoggedIn())
            throw new IllegalStateException("Not logged in.");

        return getSessionProperty(UserTerminalProperties.LANGUAGE);
    }

	public void setUserLoginId(Long userLoginId) {
		this.userLoginId = userLoginId;
	}

	public Long getUserLoginId() {
		return userLoginId;
	}

}