package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.URIFactory;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions.InvalidURIFormatException;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StoredObject;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StorerFactory;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.ObjectDoesNotExistException;

/**
 * <p/> Title: The file servlet is responsible for serving up an object for a
 * URI.
 * </p>
 * <p/> <p/> Description: The FileServlet is used by the thin client application
 * to retrieve both DisplayRotationSets and DisplayDocuments from the store and
 * display them in their appropriate frames.
 * </p>
 * <p/> There are two main methods on the FileServlet, getLas:wq <p/>
 * </p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.11 $
 */
public class FileServlet extends HttpServlet {
    private static final Logger log = CSServices.getLogger(FileServlet.class);

    /**
     * Initialize the servlet.
     * 
     * @throws ServletException
     *             (shouldn't be thrown).
     */
    public void init() throws ServletException {
        super.init();
        log.info("Initialized the file servlet.");
    }

    /**
     * This method calculates the last modified time for If-Modified-Since
     * requests from the browser. The default service method will return a
     * HttpServletResponse.SC_NOT_MODIFIED to the browser if the value return
     * here is less than or equal to the value held by the copy in the browsers
     * cache. <p/> In this case we look for the object that re
     * 
     * @param request
     *            the servlet request.
     * @return a long representation of the object the uri species modification
     *         time.
     */
    protected final long getLastModified(HttpServletRequest request) {
        log.info("getLastModified()");
        try {
            AbstractURI uri = extractURIFromRequest(request);
            long lastModifiedTimeRoundedDown = StorerFactory.getInstance().lastModified(uri);

            if (log.isDebugEnabled()) {
                String header = request.getHeader("If-Modified-Since");
                log.debug("getLastModified()");
                log.debug("last modified=" + new Date(lastModifiedTimeRoundedDown) + ", if modified since request= "
                        + header + " for url " + uri);
            }
            return lastModifiedTimeRoundedDown;
        } catch (InvalidURIFormatException e) {
            // This is a hack, it makes certain that the doGet() method gets
            // called so that a proper error can occur.
            log.warn(e);
            return Long.MAX_VALUE;
        }

    }

    /**
     * The standard request from a browser is the only method currently
     * supported.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String header = request.getHeader("If-Modified-Since");
        if (header == null) {
            log.warn("Returning uncached page to: " + request.getRemoteHost());
        }

        AbstractURI uri = extractURIFromRequest(request);
        StoredObject storedObject;
        if (log.isDebugEnabled()) {
            log.debug("Retrieving " + uri);
        }

        try {
            storedObject = StorerFactory.getInstance().retrieve(uri);
        } catch (ObjectDoesNotExistException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No object could be found for the uri '" + uri + "'.");
            log.warn("Could not find object specified by the uri '" + uri + "'.", e);
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(storedObject.getText());
    }

    private static AbstractURI extractURIFromRequest(HttpServletRequest request) {
        String uriString = request.getParameter("uri");

        if (uriString == null) {
            throw new InvalidURIFormatException("The URI supplied to the FileServlet was null.");
        }
        return URIFactory.create(uriString);
    }

}
