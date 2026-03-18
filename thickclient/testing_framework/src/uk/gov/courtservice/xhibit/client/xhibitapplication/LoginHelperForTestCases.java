package uk.gov.courtservice.xhibit.client.xhibitapplication;

import javax.security.auth.login.LoginException;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: LoginHelperForTestCases.java,v 1.3 2005/02/11 16:43:39 sz0t7n
 *          Exp $
 */

public class LoginHelperForTestCases {
    private static final String JAAS_LOCATION = "java.security.auth.login.config";

    private static LoginHelperForTestCases loginInstance = new LoginHelperForTestCases();

    private String jaas;

    private LoginHelperForTestCases() {
        jaas = getClass().getResource("/config/xhibit_jaas.config").toString();
    }

    private String getJaas() {
        return jaas;
    }

    private static LoginHelperForTestCases getLoginInstance() {
        return loginInstance;
    }

    /**
     * Logs in and sets UserSession in XHIBITSingleton
     */
    public static void login() throws LoginException, CSRecoverableException {
        try {
            String jaas = LoginHelperForTestCases.getLoginInstance().getJaas();
            System.setProperty(JAAS_LOCATION, jaas);
            System.out.println("setProperty('java.security.auth.login.config', '" + jaas + "')");

            CSUserSession csus = CSServices.getCSUserSession(new CallBackHandlerForTesting());
            XhibitSingleton.getInstance().setUserSession(csus);
            csus.login();
        } catch (LoginException ex) {
            ex.printStackTrace(System.err);
            throw ex;
        } catch (CSRecoverableException ex) {
            ex.printStackTrace(System.err);
            throw ex;
        }
    }

    public static void logout() {
        try {
            XhibitSingleton.getInstance().getUserSession().logout();
        } catch (LoginException ex) {
            ex.printStackTrace(System.err);
        }
    }
}