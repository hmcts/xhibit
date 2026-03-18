package uk.gov.courtservice.xhibit.web.framework.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.courtservice.xhibit.web.framework.util.PathNotFoundException;

/**
 * <p>
 * Title: Abstract Servlet
 * </p>
 * <p>
 * Description: This Servlet implements functionality useful to all servlets,
 * all http servlets should extend this class.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.6 $
 * 
 * $Log: AbstractServlet.java,v $
 * Revision 1.6  2006/06/05 12:30:25  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.5 2006/05/31 14:23:55 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.4 2003/10/01 15:31:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.3 2003/03/21 11:48:25 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:04 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 15:46:38 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public abstract class AbstractServlet extends HttpServlet {
    /**
     * Empty default constructor
     */
    public AbstractServlet() {
    }

    /**
     * <p>
     * If the requested initialisation parameter does not exist return the
     * alternative
     * </p>
     * 
     * @param name
     *            a String specifying the name of the initialization parameter
     * @param alternative
     *            a String containing the value to return if the initialization
     *            parameter is not found
     * @return A String containing the value of the initalization parameter or
     *         the alternative
     */
    public String getInitParameter(String name, String alternative) {
        String parameter = getInitParameter(name);
        if (parameter != null) {
            return parameter;
        } else {
            return alternative;
        }
    }

    /**
     * <p>
     * Convenience method for forwarding (server side) a request, through a
     * RequestDespatcher obtained from the ServletContext.
     * </p>
     * 
     * @param path
     *            a String specifying the location of the resource
     * @param request -
     *            a HttpServletRequest object that represents the request the
     *            client makes of the servlet
     * @param response -
     *            a ServletResponse object that represents the response the
     *            servlet returns to the client
     * @throws ServletException -
     *             if the target resource throws this exception
     * @throws IOException -
     *             if an error occures forwarding to this resource
     * @throws IllegalStateException -
     *             if the response was already committed
     * @throws PathNotFoundException -
     *             if the target path can not be found
     */
    public void forward(String path, HttpServletRequest request, HttpServletResponse response)
            throws PathNotFoundException, IOException, ServletException, IllegalStateException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(path);
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    /**
     * <p>
     * Convenience method for redirecting (client side) a request, through the
     * response it provides symatry with the forward (server side) method. Note
     * before sending we check the path is valid.
     * </p>
     * 
     * @param location
     *            a String specifying the location of the resource
     * @param request -
     *            a HttpServletRequest object that represents the request the
     *            client makes of the servlet
     * @param response -
     *            a HttpServletResponse object that represents the response the
     *            servlet returns to the client
     * @throws ServletException -
     *             if the target resource throws this exception
     * @throws IOException -
     *             if an error occures forwarding to this resource
     * @throws IllegalStateException -
     *             if the response was already committed
     * @throws PathNotFoundException -
     *             if the target path can not be found
     */
    public void redirect(String path, HttpServletRequest request, HttpServletResponse response)
            throws PathNotFoundException, IOException, ServletException, IllegalStateException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(path);
        if (dispatcher != null) {
            response.sendRedirect(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }
}
