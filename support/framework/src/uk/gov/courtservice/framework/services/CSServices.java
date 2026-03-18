package uk.gov.courtservice.framework.services;

import javax.security.auth.callback.CallbackHandler;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.audit.AuditService;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.framework.services.config.ConfigServicesImpl;
import uk.gov.courtservice.framework.services.ejb.EJBServicesImpl;
import uk.gov.courtservice.framework.services.errorhandling.DefaultErrorHandler;
import uk.gov.courtservice.framework.services.errorhandling.ErrorHandlerType;
import uk.gov.courtservice.framework.services.errorhandling.ErrorHandlerTypes;
import uk.gov.courtservice.framework.services.jms.JMSServicesImpl;
import uk.gov.courtservice.framework.services.locator.ServiceLocatorImpl;
import uk.gov.courtservice.framework.services.printing.PrintServicesImpl;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;

/**
 * <p>
 * Title: CSServices
 * </p>
 * <p>
 * Description: CSServices is the factory class through which all access to
 * framework services is obtained
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version $Id: CSServices.java,v 1.28 2009/05/27 08:58:01 powellja Exp $
 *          <p>
 *          Paul Grove added user session methods 11/11/02
 *          </p>
 */
public class CSServices {
    // Key by which user session class is stored in the bundle
    private static final String USER_SESSION_CLASS_KEY = "userSessionClass";

    // private static final String SERVER_NAME_PROP = "SERVER_NAME";
    // private static final String LOG4J_ROLLING_FILE_KEY =
    // "log4j.appender.RollingFile.File";
    // private static final String LOG4J_LOGFILE_DIRECTORY =
    // "log4j.logfile.directory";

    /**
     * Private constructor
     */
    private CSServices() {
    }

    /**
     * Gets the service locator for unauthenticated access to the default server
     * 
     * @return
     * @throws CSResourceUnavailableException
     */
    public static ServiceLocator getServiceLocator() {
        return ServiceLocatorImpl.getInstance();
    }

    /**
     * Gets the config services
     * 
     * @return
     */
    public static ConfigServices getConfigServices() {
        return ConfigServicesImpl.getInstance();
    }
    
    /**
     * Gets the EJB service
     * 
     * @return
     * @throws CSResourceUnavailableException
     */
    public static EJBServices getEJBServices() {
        return EJBServicesImpl.getInstance();
    }
    /**
     * Gets the JMS service
     * 
     * @return
     * @throws CSResourceUnavailableException
     */
    public static JMSServices getJMSServices() {
        return JMSServicesImpl.getInstance();
    }

    /**
     * Gets the logger
     * 
     * @param klass
     * @return
     */
    public static Logger getLogger(Class klass) {

        return Logger.getLogger(klass);
    }

    /**
     * Gets the default error handler
     * 
     * @return
     */
    public static ErrorHandler getDefaultErrorHandler() {
        return DefaultErrorHandler.getInstance();
    }

    /**
     * Gets the typed error handler
     * 
     * @param type
     * @return
     */
    public static ErrorHandler getErrorHandler(ErrorHandlerType type) {
        if (type == ErrorHandlerTypes.DEFAULT) {
            return getDefaultErrorHandler();
        } else {
            throw new IllegalArgumentException("ErrorHandlerType=" + type + " not handled");
        }
    }

    /**
     * Gets the audit service
     * 
     * @return
     */
    public static AuditService getAuditService() {
        return AuditService.getInstance();
    }

    /**
     * Gets the user session
     * 
     * @return
     */
    public static CSUserSession getCSUserSession() {

        try {
            String userSessionClass = CSServices.getConfigServices().getProperty(USER_SESSION_CLASS_KEY);
            return (CSUserSession) Class.forName(userSessionClass).newInstance();
        } catch (Exception ex) {
            CSConfigurationException e = new CSConfigurationException(ex);
            CSServices.getDefaultErrorHandler().handleError(e, CSServices.class);
            throw e;
        }

    }

    /**
     * Gets the user session
     * 
     * @param callbackHandler
     * @return
     */
    public static CSUserSession getCSUserSession(CallbackHandler callbackHandler) {

        CSUserSession userSession = getCSUserSession();
        userSession.setCallbackHandler(callbackHandler);

        return userSession;
    }

    /**
     * Get an instance of XMLServices.
     * 
     * @return
     */
    public static XMLServices getXMLServices() {
        return XMLServicesImpl.getInstance();
    }

    /**
     * Get an instance of XSLServices
     * 
     * @return the XSLServices singleton
     */
    public static XSLServices getXSLServices() {
        return XSLServices.getInstance();
    }

    /**
     * Get an instance of the print services
     * 
     * @return the PrintServices singleton
     */
    public static PrintServices getPrintServices() {
        return PrintServicesImpl.getInstance();
    }

    /**
     * Get an instance of the locale services
     * 
     * @return the LocaleServices singleton
     */
    public static LocaleServices getLocaleServices() {
        return LocaleServices.getInstance();
    }

    /**
     * Discover the concreten instance of the service to use
     * 
     * @return the discovered service
     */
    public static DiscoveryServices getDiscoveryServices() {
        return DiscoveryServices.getInstance();
    }
    
    public static AuditTrailService getAuditTrailService(){
        return AuditTrailService.getInstance();
    }
}