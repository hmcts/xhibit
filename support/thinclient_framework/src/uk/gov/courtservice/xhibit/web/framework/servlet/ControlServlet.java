package uk.gov.courtservice.xhibit.web.framework.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.action.Action;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.action.ActionFactory;
import uk.gov.courtservice.xhibit.web.framework.action.ActionNotFoundException;
import uk.gov.courtservice.xhibit.web.framework.response.Response;
import uk.gov.courtservice.xhibit.web.framework.response.ResponseEnvironment;
import uk.gov.courtservice.xhibit.web.framework.response.ResponseFactory;
import uk.gov.courtservice.xhibit.web.framework.response.ResponseNotFoundException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import weblogic.security.spi.WLSGroup;

/**
 * <p>
 * Title: Control Servlet
 * </p>
 * <p>
 * Description: This Servlet Controls The Application Flow, it is configured by
 * the values in a configuration file identified by the resource.properties
 * servlet parameter.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.16 $ $Log:
 *         ControlServlet.java,v $ Revision 1.10 2005/02/11 15:59:51 sz0t7n
 *         Organise imports
 * 
 * Revision 1.9 2004/11/04 14:26:30 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.8 2003/10/01 15:31:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.7 2003/06/26 11:15:47 cawleye Modified code to deal with common
 * properties files.
 * 
 * Revision 1.6 2003/03/25 12:44:21 fz0n8j Logs rather than system.out.
 * 
 * Revision 1.5 2003/03/24 14:36:42 fz0n8j Added external url functionality
 * 
 * Revision 1.4 2003/03/21 15:07:05 fz0n8j Fixed home (default page) property
 * lookup
 * 
 * Revision 1.3 2003/03/21 11:48:26 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.8 2003/03/19 21:54:15 fz0n8j Allow context free
 * 
 * Revision 1.7 2003/03/19 20:13:32 fz0n8j Added better response functionality
 * 
 * Revision 1.6 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.5 2003/03/19 10:31:32 fz0n8j Added support for response methods
 * (redirect forward)
 * 
 * Revision 1.4 2003/03/17 11:32:05 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.3 2003/03/12 14:56:15 fz0n8j Removed redirect switch from code.
 * ecawley
 * 
 * Revision 1.2 2003/03/11 15:50:21 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public class ControlServlet extends AbstractServlet {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(ControlServlet.class);

    /**
     * Action resource name key
     */

    private static final String ACTION_RESOURCE_NAME_KEY = "action.resource.name";

    /**
     * Response resource name key
     */

    private static final String RESPONSE_RESOURCE_NAME_KEY = "response.resource.name";

    /**
     * Common Action resource name key
     */

    private static final String COMMON_ACTION_RESOURCE_NAME_KEY = "commonaction.resource.name";

    /**
     * Common Response resource name key
     */

    private static final String COMMON_RESPONSE_RESOURCE_NAME_KEY = "commonresponse.resource.name";

    /**
     * Default name key
     */

    private static final String DEFAULT_REQUEST_NAME_KEY = "default.request.path";

    /**
     * Default default name key
     */

    private static final String DEFAULT_REQUEST_NAME_DEFAULT = "/home";

    /**
     * The action map
     */
    private ActionFactory actionFactory;

    /**
     * The response map
     */
    private ResponseFactory responseFactory;

    /**
     * The default request
     */
    private String defaultRequest;

    /**
     * <p>
     * Overridden to initialise fields from init parameters.
     * </p>
     * 
     * @throws ServletException
     *             if an exception occurs that interrupts the servlet's normal
     *             operation
     */
    public void init() throws ServletException {
        try {

            String actionResourceName = getInitParameter(ACTION_RESOURCE_NAME_KEY);
            actionFactory = actionResourceName == null ? new ActionFactory() : new ActionFactory(actionResourceName);

            String commonActionResourceName = getInitParameter(COMMON_ACTION_RESOURCE_NAME_KEY);

            log.debug("common action resource name : " + commonActionResourceName);

            if (commonActionResourceName != null) {
                actionFactory.addPropertiesFile(commonActionResourceName);
            }

            log.debug(actionFactory);

            String responseResourceName = getInitParameter(RESPONSE_RESOURCE_NAME_KEY);
            responseFactory = responseResourceName == null ? new ResponseFactory() : new ResponseFactory(
                    responseResourceName);

            String commonResponseResourceName = getInitParameter(COMMON_RESPONSE_RESOURCE_NAME_KEY);

            if (commonResponseResourceName != null) {
                responseFactory.addPropertiesFile(commonResponseResourceName);
            }

            log.debug(responseFactory);

            defaultRequest = getInitParameter(DEFAULT_REQUEST_NAME_KEY, DEFAULT_REQUEST_NAME_DEFAULT);
        } catch (FrameworkException fe) {
            throw new ServletException(fe);
        }
    }

    /**
     * <p>
     * The service method is invoked by the framework to process a http request.
     * </p>
     * 
     * @param request
     *            The Servlet Request Object
     * @param response
     *            The Servlet Response Object
     * @throws IOException
     *             When an IO error occures
     * @throws ServletException
     *             if the request cannot be handled
     */
    public void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException,
            ServletException {
        try {
            String requestPath = servletRequest.getServletPath();
            String contextPath = servletRequest.getContextPath();
            
            String translatedPath = servletRequest.getPathTranslated();
            String pathInfo = servletRequest.getPathInfo(); String requestURI =
            servletRequest.getRequestURI();
             
            log.debug("Request Path: \"" + requestPath + "\"");
            log.debug("Context Path: \"" + contextPath + "\"");
            log.debug("Translated Path: \"" + translatedPath + "\"");
            log.debug("Path Info: \"" + pathInfo + "\""); log.debug("Request Uri: \"" + requestURI + "\"");
            
            // Filter requestPath for TrainingFacilities
            if (contextPath.equals("/TrainingFacilities") && requestPath.equals("/j_security_check")) {
                // Change requestPath to empty
                requestPath = "";
            }
            // Now determine where to redirect and access permissions
            if (contextPath.equals("/ProbationService") || contextPath.equals("/WitnessFacilities") || contextPath.equals("/Admin") || contextPath.equals("/Messaging")
            		|| contextPath.equals("/TrainingFacilities") || contextPath.equals("/DataReset")) {
                Cookie[] cookies = servletRequest.getCookies();
                log.debug("Checking cookies to see if already authenticated");
                boolean authenticated = false;
                if (cookies != null) {
                    for (int i=0; i< cookies.length; i++) {
                        //if (cookies[i].getName().equals("_WL_AUTHCOOKIE_JSESSIONID")) {
                    	if (cookies[i].getName().equals("JSESSIONID")) { // http only
                            authenticated = true;
                        }
                    }
                }
                if (authenticated) {
                	log.debug("User is authenticated");
                } else {
                	log.debug("User is NOT authenticated");
                }
                
                // Now determine whether to send to login page or not
                defaultRequest = "/login.jsp";
                if (contextPath.equals("/Admin") && (authenticated)) {
                	log.debug("We are in /Admin .... servletRequest.getSession() == null is : " + servletRequest.getSession() == null);
                    if (servletRequest.getSession() != null) {
                        if ((servletRequest.getSession().getAttribute("isAdmin") != null) && (servletRequest.getSession().getAttribute("isAdmin").equals("true"))) { // Check if user is a CPS user so that they can see these 2 menu options
                            defaultRequest = "/home";
                        } else {
                            // Session Parameter may not have been setup so check Subject manually
                            Subject s = weblogic.security.Security.getCurrentSubject();
                            log.debug("Checking we are an admin...");
                            if (s != null) {
                                for (Principal p: s.getPrincipals()) {
                                    if (p instanceof WLSGroup) {
                                        if (p.getName().toLowerCase().indexOf("admin") != -1) { // not found
                                            defaultRequest = "/home";
                                            log.debug("Ok we are an admin");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (authenticated) {
                        defaultRequest = "/home";
                        log.debug("We are now setting defaultRequest to /home ....not in /Admin but another thin client app~");
                    }
                }
                
                if (defaultRequest.equals("/login.jsp")) {
                	log.debug("Redirecting to /login.jsp");
                    redirect(contextPath + defaultRequest, servletRequest, servletResponse);
                }   
            }
            if (requestPath == null || requestPath.equals("")) {
            	log.debug("Redirecting somewhere now...");
            	log.debug("Request Path: \"" + requestPath + "\"");
                log.debug("Context Path: \"" + contextPath + "\"");
                log.debug("Translated Path: \"" + translatedPath + "\"");
                log.debug("Path Info: \"" + pathInfo + "\""); log.debug("Request Uri: \"" + requestURI + "\"");
                redirect(contextPath + defaultRequest, servletRequest, servletResponse);
            } else {

                ActionEnvironment actionEnvironment = new ServletActionEnvironment(servletRequest, servletResponse);

                Action action = actionFactory.getActionByRequest(requestPath);

                log.debug("Action:- " + action);

                action.performAction(actionEnvironment);

                String responseName = actionEnvironment.getResponseName();

                ResponseEnvironment responseEnvironment = new ServletResponseEnvironment(servletRequest,
                        servletResponse);

                Response response = responseFactory.getResponseByName(responseName);

                log.debug("Response:- " + response);

                response.performResponse(responseEnvironment);
            }
        } catch (ActionNotFoundException anfe) {
            throw new ServletException(anfe);
        } catch (ResponseNotFoundException rnfe) {
            throw new ServletException(rnfe);
        } catch (FrameworkException fe) {
            throw new ServletException(fe);
        }
    }
}
